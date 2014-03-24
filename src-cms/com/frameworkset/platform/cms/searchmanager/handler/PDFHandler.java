package com.frameworkset.platform.cms.searchmanager.handler;

/*--
 Copyright (C) @year@ i2a and David Duddleston. All rights reserved.
 */

/**
 * <p><code>PDFHandler</code>
 *	Content handler for PDF documents.
 * </p>
 *
 * @author <a href="mailto:david@i2a.com">David Duddleston</a>
 * @version 1.0
 */
import java.io.*;
/**
 * Insert the type's description here.
 * Creation date: (2/21/2001 7:19:20 PM)
 * @author:
 */

import com.frameworkset.platform.cms.searchmanager.extractors.CmsExtractorPdf;
import com.frameworkset.platform.cms.searchmanager.extractors.I_CmsExtractionResult;
import com.frameworkset.platform.cms.searchmanager.extractors.I_CmsTextExtractor;


public class PDFHandler extends ContentHandlerBase {
	
//	private static final PDFHandler instance = new PDFHandler();
	
	public PDFHandler(){
		
	}
	
//	public static PDFHandler getInstance()
//	{
//		return instance;
//	}
	
	/**
     * Parse Content.
     *
     */
    public void parse(InputStream in) { 
    	try {
    		
			this.reset();
			I_CmsTextExtractor pptExtractor = CmsExtractorPdf.getExtractor();
			I_CmsExtractionResult er = pptExtractor.extractText(in);
			
			this.contents = new StringBuffer(er.getContent());
			this.metoInfo = er.getMetaInfo();
			this.fileFormat = ContentHandler.PDF_FILEFOMAT;
			
			if(this.contents == null)
				this.contents = new StringBuffer("");
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
    }

    public static void main(String[] args) {

        try {
        	
            //String path = "z:/webapps/dev/pdf/HealthPlansOnline.pdf";]
            InputStream in = new FileInputStream("D:\\workspace\\cms\\Web服务事务处理应用模型.pdf");
//        	InputStream in = new FileInputStream("D:\\workspace\\cms\\Quality of service for workflows and web service processes.pdf");
        	PDFHandler p = new PDFHandler();
            p.parse(in);
//            System.out.println("Title: " + p.getTitle());
//            System.out.println("Author: " + p.getAuthor());
//            System.out.println("Published " + p.getPublished());
//            System.out.println("Keywords: " + p.getKeywords());
//            System.out.println("Description: " + p.getDescription());
            System.out.println("Content: " + p.getTitle());
            System.out.println("Content: " + p.getAuthor());
            System.out.println("Content: " + p.getFileFormat());
            System.out.println("Content: " + p.getPublished());
        	
        } catch (Exception e) {e.printStackTrace();}
    }
}
