package com.frameworkset.platform.cms.searchmanager.handler;

import java.io.FileInputStream;
import java.io.InputStream;

import com.frameworkset.platform.cms.searchmanager.extractors.CmsExtractorPdf;
import com.frameworkset.platform.cms.searchmanager.extractors.CmsExtractorRtf;
import com.frameworkset.platform.cms.searchmanager.extractors.I_CmsExtractionResult;
import com.frameworkset.platform.cms.searchmanager.extractors.I_CmsTextExtractor;

public class RTFHandler extends ContentHandlerBase {
	
	public RTFHandler(){
		
	}
	
	
	/**
     * Parse Content.
     *
     */
    public void parse(InputStream in) { 
    	try {
    		
			this.reset();
			I_CmsTextExtractor rtfExtractor = new CmsExtractorRtf();
			I_CmsExtractionResult er = rtfExtractor.extractText(in);
			
			this.contents = new StringBuffer(er.getContent());
			this.metoInfo = er.getMetaInfo();
			this.fileFormat = ContentHandler.RTF_FILEFOMAT;
			
			if(this.contents == null)
				this.contents = new StringBuffer("");
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
    }

    public static void main(String[] args) {

        try {
        	
            //String path = "z:/webapps/dev/pdf/HealthPlansOnline.pdf";]
            InputStream in = new FileInputStream("D:\\workspace\\cms\\测试rtf.rtf");
//        	InputStream in = new FileInputStream("D:\\workspace\\cms\\Quality of service for workflows and web service processes.pdf");
            RTFHandler p = new RTFHandler();
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
            System.out.println("Content: " + p.getContents());
        	
        } catch (Exception e) {e.printStackTrace();}
    }
}
