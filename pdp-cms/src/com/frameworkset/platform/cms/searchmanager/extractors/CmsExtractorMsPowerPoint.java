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


import java.io.InputStream;
import java.util.Map;

import org.apache.poi.hslf.HSLFSlideShow;
import org.apache.poi.hslf.extractor.PowerPointExtractor;
import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.model.TextRun;
import org.apache.poi.hslf.usermodel.SlideShow;
import org.apache.poi.xslf.extractor.XSLFPowerPointExtractor;
import org.apache.poi.xslf.usermodel.XMLSlideShow;

import com.frameworkset.platform.cms.searchmanager.handler.ContentHandler;

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
    public CmsExtractorMsPowerPoint(String version) {
    	this.version = version; 
    }

//    /**
//     * Returns an instance of this text extractor.<p> 
//     * 
//     * @return an instance of this text extractor
//     */
//    public static I_CmsTextExtractor getExtractor() {
//
//        // since this extractor requires a member variable we have no static instance
//        return new CmsExtractorMsPowerPoint();
//    }

    /** 
     * @see org.opencms.search.extractors.I_CmsTextExtractor#extractText(java.io.InputStream, java.lang.String)
     */
    public I_CmsExtractionResult extractText(InputStream in, String encoding) throws Exception {
    	
    	//first extract the table content
//        String result = extractPPTContent(getStreamCopy(in));
//        result = removeControlChars(result);
//        
//        //now extract the meta information using POI 
//        POIFSReader reader = new POIFSReader();
//        reader.registerListener(this);
//        reader.read(getStreamCopy(in)); 
//        Map metaInfo = extractMetaInformation();
    	String result = null;
    	 Map metaInfo = null;
    	if(this.version.equals(ContentHandler.VERSION_2003))
    	{
    		result = readPowerPoint( in);
    		metaInfo = extractMetaInformation();
    	}
    	else
    	{
    		readPowerPoint2007(in);
    		metaInfo = extractMetaInformation();
    	}

        //free some memory
        cleanup();
        
        // return the final result
        return new CmsExtractionResult(result, metaInfo);
    }
    
    /** 
        * 处理ppt 
          * @param path 
          * @return 
          */  
        public  String readPowerPoint(InputStream in) {  
            String content = null;  
            try {  
            	HSLFSlideShow slideShow = new HSLFSlideShow(in);
            	org.apache.poi.hslf.extractor.PowerPointExtractor extractor = new PowerPointExtractor(slideShow);
            	this.m_documentSummary = extractor.getDocSummaryInformation();
            	this.m_summary = extractor.getSummaryInformation();
            	content = extractor.getText();
//                 SlideShow ss = new SlideShow(new HSLFSlideShow(in));// is  
//                // 为文件的InputStream，建立SlideShow  
//                Slide[] slides = ss.getSlides();// 获得每一张幻灯片  
//                 for (int i = 0; i < slides.length; i++) {  
//                    TextRun[] t = slides[i].getTextRuns();// 为了取得幻灯片的文字内容，建立TextRun  
//                     for (int j = 0; j < t.length; j++) {  
//                         content.append(t[j].getText());// 这里会将文字内容加到content中去  
//                    }  
//                 }  
            } catch (Exception ex) {  
               System.out.println(ex.toString());  
             }  
             return content;  
        } 
        
        /** 
         * 处理ppt 
           * @param path 
           * @return 
           */  
         public  String readPowerPoint2007(InputStream in) {  
             String content = null;  
             try {  
            
            	 XMLSlideShow xmlslideshow = new XMLSlideShow(in);
             	org.apache.poi.xslf.extractor.XSLFPowerPointExtractor extractor = new XSLFPowerPointExtractor(xmlslideshow);
             	this.cp = extractor.getCoreProperties();
             	content = extractor.getText();
//                  SlideShow ss = new SlideShow(new HSLFSlideShow(in));// is  
//                 // 为文件的InputStream，建立SlideShow  
//                 Slide[] slides = ss.getSlides();// 获得每一张幻灯片  
//                  for (int i = 0; i < slides.length; i++) {  
//                     TextRun[] t = slides[i].getTextRuns();// 为了取得幻灯片的文字内容，建立TextRun  
//                      for (int j = 0; j < t.length; j++) {  
//                          content.append(t[j].getText());// 这里会将文字内容加到content中去  
//                     }  
//                  }  
             } catch (Exception ex) {  
                System.out.println(ex.toString());  
              }  
              return content;  
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