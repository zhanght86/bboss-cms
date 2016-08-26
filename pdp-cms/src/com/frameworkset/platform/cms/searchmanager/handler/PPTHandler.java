package com.frameworkset.platform.cms.searchmanager.handler;

import java.io.FileInputStream;
import java.io.InputStream;

import com.frameworkset.platform.cms.searchmanager.extractors.CmsExtractorMsPowerPoint;
import com.frameworkset.platform.cms.searchmanager.extractors.I_CmsExtractionResult;
import com.frameworkset.platform.cms.searchmanager.extractors.I_CmsTextExtractor;

public class PPTHandler extends ContentHandlerBase {
	
	
	public PPTHandler(String version){
		this.version = version;
	}
	
	
	/**
	 * parse content
	 */
	public void parse(InputStream in) {
		try {
			this.reset();
			
			I_CmsTextExtractor pptExtractor = new CmsExtractorMsPowerPoint(version);
			I_CmsExtractionResult er = pptExtractor.extractText(in);
			
			this.contents = new StringBuffer(er.getContent());
			this.metoInfo = er.getMetaInfo();
			this.fileFormat = ContentHandler.PPT_FILEFOMAT;
			
			if(this.contents == null)
				this.contents = new StringBuffer("");
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
        try {
//            java.net.URL url = new java.net.URL("http://yjspy.csu.edu.cn/xtxspb.doc");
//            java.net.HttpURLConnection con = (HttpURLConnection) url.openConnection();
//            InputStream in = con.getInputStream();
        	
            InputStream in = new FileInputStream("D:\\workspace\\cms\\gfsgfsgfs.ppt");
            PPTHandler ppthandler = new PPTHandler(ContentHandler.VERSION_2003);
            ppthandler.parse(in);
            System.out.println("title:" + ppthandler.getTitle() );
            System.out.println("Author:" + ppthandler.getAuthor() );
            System.out.println("Content:" + ppthandler.getContents() );
            System.out.println("Description:" + ppthandler.getDescription() );
        	
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
		
		
	}
}
	
