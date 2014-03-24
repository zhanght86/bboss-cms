/*
 * File   : $Source: /usr/local/cvs/opencms/src/org/opencms/search/extractors/CmsExtractorMsWord.java,v $
 * Date   : $Date: 2005/07/29 10:35:06 $
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

import java.io.InputStream;
import java.util.Map;

import org.apache.poi.POIXMLProperties.CoreProperties;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
//import org.apache.poi.poifs.eventfilesystem.POIFSReader;

/**
 * Extracts the text form an MS Word document.<p>
 * 
 * @author Alexander Kandzior 
 * 
 * @version $Revision: 1.8 $ 
 * 
 * @since 6.0.0 
 */
public final class CmsExtractorMsWord extends A_CmsTextExtractorMsOfficeBase implements java.io.Serializable {

	private String version = "doc";//docx
    /** Static member instance of the extractor. */
//    private static final CmsExtractorMsWord INSTANCE = new CmsExtractorMsWord();

    /**
     * Hide the public constructor.<p> 
     */
    public CmsExtractorMsWord(String version) {
    	this.version = version;
        // noop
    }

//    /**
//     * Returns an instance of this text extractor.<p> 
//     * 
//     * @return an instance of this text extractor
//     */
//    public static I_CmsTextExtractor getExtractor() {
//
//        return INSTANCE;
//    }

    /** 
     * @see org.opencms.search.extractors.I_CmsTextExtractor#extractText(java.io.InputStream, java.lang.String)
     */
    public I_CmsExtractionResult extractText(InputStream in, String encoding) throws Exception {

        // first extract the text using the text actraction libary
//    	WordTextExtractorFactory factory = new WordTextExtractorFactory();
    	
//        TextExtractor wordExtractor = factory.textExtractor(getStreamCopy(in));
        

//        String result = wordExtractor.getText();
//        String result = wordExtractor.extractText(getStreamCopy(in));
    	StringBuffer content = new StringBuffer();
//    	org.apache.poi.hwpf
    	
    	
//    	 HWPFDocument doc = new HWPFDocument(in);
//         Range range = doc.getRange();
//         int paragraphCount = range.numParagraphs();// 段落
//         for (int i = 0; i < paragraphCount; i++) {// 遍历段落读取数据
//             Paragraph pp = range.getParagraph(i);
//             content.append(pp.text());
//         }
    	 String result = null;
    	 Map metaInfo = null;
    	if(version.equals("doc"))
    	{
    		WordExtractor ex  = new WordExtractor(in);
    		result = ex.getText();   
    		 SummaryInformation info = ex.getSummaryInformation();
             this.m_summary = info;
             this.m_documentSummary = ex.getDocSummaryInformation();
             metaInfo = extractMetaInformation();
    	}
    	else
    	{
    		XWPFDocument doc = new XWPFDocument(in);
    		XWPFWordExtractor ex = new XWPFWordExtractor(doc);
    		result = ex.getText();
    		cp = ex.getCoreProperties();
    		 metaInfo = extractMetaInformation();
//    		 SummaryInformation info = doc.getSummaryInformation();
//             this.m_summary = info;
//             this.m_documentSummary = doc.getDocSummaryInformation();
    	}
    	
        
         
         
         
    	
    	
    	
    	
      
//        result = removeControlChars(result);

//     String result = content.toString();
        // now extract the meta information using POI 
//        POIFSReader reader = new POIFSReader();
//        reader.registerListener(this);
//        reader.read(getStreamCopy(getStreamCopy(in)));
        
       

        // free some memory
        cleanup();
        
        // return the final result
        return new CmsExtractionResult(result, metaInfo);
    }
}