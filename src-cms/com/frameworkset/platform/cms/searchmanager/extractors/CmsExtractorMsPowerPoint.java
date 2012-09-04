/*
 * File   : $Source: /usr/local/cvs/opencms/src/org/opencms/search/extractors/CmsExtractorMsPowerPoint.java,v $
 * Date   : $Date: 2006/03/27 14:53:01 $
 * Version: $Revision: 1.8 $
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


import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.model.TextRun;
import org.apache.poi.hslf.usermodel.SlideShow;
import org.apache.poi.poifs.eventfilesystem.POIFSReader;
import org.apache.poi.poifs.eventfilesystem.POIFSReaderEvent;
import org.apache.poi.poifs.eventfilesystem.POIFSReaderListener;
import org.apache.poi.poifs.filesystem.DocumentInputStream;
import org.apache.poi.util.LittleEndian;

import com.frameworkset.platform.cms.driver.i18n.CmsEncoder;

/**
 * Extracts the text form an MS PowerPoint document.<p>
 * 
 * @author Alexander Kandzior 
 * 
 * @version $Revision: 1.8 $ 
 * 
 * @since 6.0.0 
 */
public final class CmsExtractorMsPowerPoint extends A_CmsTextExtractorMsOfficeBase implements java.io.Serializable{

    /**
     * Hide the public constructor.<p> 
     */
    private CmsExtractorMsPowerPoint() {

    }

    /**
     * Returns an instance of this text extractor.<p> 
     * 
     * @return an instance of this text extractor
     */
    public static I_CmsTextExtractor getExtractor() {

        // since this extractor requires a member variable we have no static instance
        return new CmsExtractorMsPowerPoint();
    }

    /** 
     * @see org.opencms.search.extractors.I_CmsTextExtractor#extractText(java.io.InputStream, java.lang.String)
     */
    public I_CmsExtractionResult extractText(InputStream in, String encoding) throws Exception {
    	
    	//first extract the table content
        String result = extractPPTContent(getStreamCopy(in));
        result = removeControlChars(result);
        
        //now extract the meta information using POI 
        POIFSReader reader = new POIFSReader();
        reader.registerListener(this);
        reader.read(getStreamCopy(in)); 
        Map metaInfo = extractMetaInformation();

        //free some memory
        cleanup();
        
        // return the final result
        return new CmsExtractionResult(result, metaInfo);
    }

    protected String extractPPTContent(InputStream in) throws Exception{
    	SlideShow ppt = new SlideShow(in);
        Slide[] slide = ppt.getSlides();
        StringBuffer content = new StringBuffer();
   		 for(int i=0;i<slide.length;i++){
   			 TextRun[] tr = slide[i].getTextRuns();
   			 for(int j = 0; j<tr.length; j++){
   				content.append(" ");
   				content.append(slide[i].getTextRuns()[j].getText());
   			 }
   		 }
   		 return content.toString();
    }
}