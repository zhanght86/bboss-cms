/*
 * File   : $Source: /usr/local/cvs/opencms/src/org/opencms/search/extractors/A_CmsTextExtractorMsOfficeBase.java,v $
 * Date   : $Date: 2005/07/29 10:35:06 $
 * Version: $Revision: 1.7 $
 *
 * This library is part of OpenCms -
 * the Open Source Content Mananagement System
 *
 * Copyright (c) 2005 Alkacon Software GmbH (http://www.alkacon.com)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * For further information about Alkacon Software GmbH, please see the
 * company website: http://www.alkacon.com
 *
 * For further information about OpenCms, please see the
 * project website: http://www.opencms.org
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.frameworkset.platform.cms.searchmanager.extractors;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.POIXMLProperties.CoreProperties;
import org.apache.poi.hpsf.DocumentSummaryInformation;
import org.apache.poi.hpsf.PropertySetFactory;
import org.apache.poi.hpsf.Section;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hpsf.wellknown.PropertyIDMap;
import org.apache.poi.poifs.eventfilesystem.POIFSReaderEvent;
import org.apache.poi.poifs.eventfilesystem.POIFSReaderListener;

import com.frameworkset.platform.cms.driver.util.CmsStringUtil;

/**
 * Base class to extract summary information from MS office documents.<p>
 * 
 * @author Alexander Kandzior 
 * 
 * @version $Revision: 1.7 $ 
 * 
 * @since 6.0.0 
 */
public abstract class A_CmsTextExtractorMsOfficeBase extends A_CmsTextExtractor implements POIFSReaderListener,java.io.Serializable {

    /** Windows Cp1252 endocing (western europe) is used as default for single byte fields. */
    protected static final String ENCODING_CP1252 = "Cp1252";

    /** UTF-16 encoding is used for double byte fields. */
    protected static final String ENCODING_UTF16 = "GBK";

    /** Event event name for a MS PowerPoint document. */
    protected static final String POWERPOINT_EVENT_NAME = "PowerPoint Document";

    /** PPT text byte atom. */
    protected static final int PPT_TEXTBYTE_ATOM = 4008;

    /** PPT text char atom. */
    protected static final int PPT_TEXTCHAR_ATOM = 4000;

    /** The summary of the POI document. */
    protected DocumentSummaryInformation m_documentSummary;

    /** The summary of the POI document. */
    protected SummaryInformation m_summary;
    protected CoreProperties cp;
    protected String version;
    /**
     * @see org.apache.poi.poifs.eventfilesystem.POIFSReaderListener#processPOIFSReaderEvent(org.apache.poi.poifs.eventfilesystem.POIFSReaderEvent)
     */
    public void processPOIFSReaderEvent(POIFSReaderEvent event) {

        try {
            if ((m_summary == null) && event.getName().startsWith(SummaryInformation.DEFAULT_STREAM_NAME)) {
                m_summary = (SummaryInformation)PropertySetFactory.create(event.getStream());
                return;
            }
            if ((m_documentSummary == null)
                && event.getName().startsWith(DocumentSummaryInformation.DEFAULT_STREAM_NAME)) {
                m_documentSummary = (DocumentSummaryInformation)PropertySetFactory.create(event.getStream());
                return;
            }
        } catch (Exception e) {
            // ignore
        }
    }

    /**
     * Cleans up some internal memory.<p> 
     */
    protected void cleanup() {

        m_summary = null;
        m_documentSummary = null;
        this.m_inputBuffer = null;
    }
    protected Map extractMetaInformation()
    {
    	if(cp == null)
    		return extractMetaInformation2003();
    	else
    		return extractMetaInformation2007();
    		
    }
    /**
     * Returns a map with the extracted meta information from the document.<p>
     * 
     * @return a map with the extracted meta information from the document
     */
    protected Map extractMetaInformation2003() {

    	
        Map metaInfo = new HashMap();
        String meta;
        if (m_summary != null) {
            // can't use convenience methods on summary since they can't deal with multiple sections
//            Section section = (Section)m_summary.getSections().get(0);

            meta = m_summary.getTitle();
            if (CmsStringUtil.isNotEmpty(meta)) {
                metaInfo.put(I_CmsExtractionResult.META_TITLE, meta);
            }
            meta = m_summary.getKeywords();
            if (CmsStringUtil.isNotEmpty(meta)) {
                metaInfo.put(I_CmsExtractionResult.META_KEYWORDS, meta);
            }
            meta = m_summary.getSubject();
            if (CmsStringUtil.isNotEmpty(meta)) {
                metaInfo.put(I_CmsExtractionResult.META_SUBJECT, meta);
            }
            meta = m_summary.getComments();
            if (CmsStringUtil.isNotEmpty(meta)) {
                metaInfo.put(I_CmsExtractionResult.META_COMMENTS, meta);
            }
            // extract other available meta information
            meta = m_summary.getAuthor();
            if (CmsStringUtil.isNotEmpty(meta)) {
                metaInfo.put(I_CmsExtractionResult.META_AUTHOR, meta);
            }
            Date date;
            date = m_summary.getCreateDateTime();
            if ((date != null) && (date.getTime() > 0)) {
                // it's unlikley any PowerPoint documents where created before 1970, 
                // and apparently POI contains an issue calculating the time correctly sometimes
                metaInfo.put(I_CmsExtractionResult.META_DATE_CREATED, date);
            }
            date = m_summary.getLastSaveDateTime();
            if ((date != null) && (date.getTime() > 0)) {
                metaInfo.put(I_CmsExtractionResult.META_DATE_LASTMODIFIED, date);
            }
        }
        if (m_documentSummary != null) {
            // can't use convenience methods on document since they can't deal with multiple sections
//            Section section = (Section)m_documentSummary.getSections().get(0);

            // extract available meta information from document summary
            meta =  m_documentSummary.getCompany();
            if (CmsStringUtil.isNotEmpty(meta)) {
                metaInfo.put(I_CmsExtractionResult.META_COMPANY, meta);
            }
            meta =  m_documentSummary.getManager();
            if (CmsStringUtil.isNotEmpty(meta)) {
                metaInfo.put(I_CmsExtractionResult.META_MANAGER, meta);
            }
            meta =  m_documentSummary.getCategory();
            if (CmsStringUtil.isNotEmpty(meta)) {
                metaInfo.put(I_CmsExtractionResult.META_CATEGORY, meta);
            }
        }

        return metaInfo;
    }
    
    /**
     * Returns a map with the extracted meta information from the document.<p>
     * 
     * @return a map with the extracted meta information from the document
     */
    protected Map extractMetaInformation2007() {

    	
        Map metaInfo = new HashMap();
        String meta;
        if (cp != null) {
            // can't use convenience methods on summary since they can't deal with multiple sections
//            Section section = (Section)m_summary.getSections().get(0);
        	
            meta = cp.getTitle();
            if (CmsStringUtil.isNotEmpty(meta)) {
                metaInfo.put(I_CmsExtractionResult.META_TITLE, meta);
            }
            meta = cp.getKeywords();
            if (CmsStringUtil.isNotEmpty(meta)) {
                metaInfo.put(I_CmsExtractionResult.META_KEYWORDS, meta);
            }
            meta = cp.getSubject();
            if (CmsStringUtil.isNotEmpty(meta)) {
                metaInfo.put(I_CmsExtractionResult.META_SUBJECT, meta);
            }
            meta = cp.getDescription();
            if (CmsStringUtil.isNotEmpty(meta)) {
                metaInfo.put(I_CmsExtractionResult.META_COMMENTS, meta);
            }
            // extract other available meta information
            meta = cp.getCreator();
            if (CmsStringUtil.isNotEmpty(meta)) {
                metaInfo.put(I_CmsExtractionResult.META_AUTHOR, meta);
            }
            Date date;
            date = cp.getCreated();
            if ((date != null) && (date.getTime() > 0)) {
                // it's unlikley any PowerPoint documents where created before 1970, 
                // and apparently POI contains an issue calculating the time correctly sometimes
                metaInfo.put(I_CmsExtractionResult.META_DATE_CREATED, date);
            }
            date = cp.getModified();
            if ((date != null) && (date.getTime() > 0)) {
                metaInfo.put(I_CmsExtractionResult.META_DATE_LASTMODIFIED, date);
            }
        }
        if (cp != null) {
            // can't use convenience methods on document since they can't deal with multiple sections
//            Section section = (Section)m_documentSummary.getSections().get(0);

            // extract available meta information from document summary
//            meta =  cp.getUnderlyingProperties().getCompany();
//            if (CmsStringUtil.isNotEmpty(meta)) {
//                metaInfo.put(I_CmsExtractionResult.META_COMPANY, meta);
//            }
//            meta =  cp.getManager();
//            if (CmsStringUtil.isNotEmpty(meta)) {
//                metaInfo.put(I_CmsExtractionResult.META_MANAGER, meta);
//            }
            meta =  cp.getCategory();
            if (CmsStringUtil.isNotEmpty(meta)) {
                metaInfo.put(I_CmsExtractionResult.META_CATEGORY, meta);
            }
        }

        return metaInfo;
    }
}