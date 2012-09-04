package com.frameworkset.platform.cms.searchmanager.handler;

import java.io.*;

import com.frameworkset.platform.cms.searchmanager.extractors.CmsExtractorMsWord;
import com.frameworkset.platform.cms.searchmanager.extractors.I_CmsExtractionResult;
import com.frameworkset.platform.cms.searchmanager.extractors.I_CmsTextExtractor;

import java.net.*;

/**
 * 处理word文档
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author ZHQ
 * @version 1.0
 */
public class WordHandler extends ContentHandlerBase {
	
	private static final WordHandler instance = new WordHandler();
	
	private WordHandler(){
		
	}
	
	public static WordHandler getInstance()
	{
		return instance;
	}
	
    /**
     * Parse Content.
     */
    public void parse(InputStream in) { 
    	try {
			this.reset();
			I_CmsTextExtractor wordExtractor = CmsExtractorMsWord.getExtractor();
			I_CmsExtractionResult er = wordExtractor.extractText(in);
			this.contents = new StringBuffer(er.getContent());
			this.metoInfo = er.getMetaInfo();
			this.fileFormat = ContentHandler.WORD_FILEFOMAT;
			
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
            InputStream in = new FileInputStream("D:\\workspace\\cms\\word测试.doc");
            WordHandler wordhandler = new WordHandler();
            wordhandler.parse(in);
//            System.out.println("contentType:" + con.getContentType());
            System.out.println("title:" + wordhandler.getTitle() );
            System.out.println("Author:" + wordhandler.getAuthor() );
            System.out.println("Contents:" + wordhandler.getContents());
            
            InputStream in2 = new FileInputStream("D:\\workspace\\cms\\20070709024320984.doc");
            WordHandler wordhandler2 = new WordHandler();
            wordhandler2.parse(in2);
//            System.out.println("contentType:" + con.getContentType());
            System.out.println("title:" + wordhandler2.getTitle() );
            System.out.println("Author:" + wordhandler2.getAuthor() );
            System.out.println("Contents:" + wordhandler2.getContents());
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
    }
}
