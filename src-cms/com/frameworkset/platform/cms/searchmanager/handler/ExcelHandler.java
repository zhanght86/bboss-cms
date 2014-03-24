package com.frameworkset.platform.cms.searchmanager.handler;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

import com.frameworkset.platform.cms.searchmanager.extractors.CmsExtractorMsExcel;
import com.frameworkset.platform.cms.searchmanager.extractors.I_CmsExtractionResult;
import com.frameworkset.platform.cms.searchmanager.extractors.I_CmsTextExtractor;

public class ExcelHandler extends ContentHandlerBase {
	
//	private static final ExcelHandler instance = new ExcelHandler();
	
	public ExcelHandler(String version){
		this.version = version;
	}
	
//	public static ExcelHandler getInstance()
//	{
//		return instance;
//	}
	/**
	 * parse content
	 */
	public void parse(InputStream in) {
		try {
			this.reset();
			I_CmsTextExtractor xlsExtractor = CmsExtractorMsExcel.
																getExtractor();
			I_CmsExtractionResult er = xlsExtractor.extractText(in);
			this.contents = new StringBuffer(er.getContent());
			this.metoInfo = er.getMetaInfo();
			this.fileFormat = ContentHandler.EXCEL_FILEFOMAT;
			
			if(this.contents == null)
				this.contents = new StringBuffer("");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public static void main(String[] args) {
        try {
            java.net.URL url = new java.net.URL("http://yjspy.csu.edu.cn/YJS/fujian/down/kccxdjb.xls");
            java.net.HttpURLConnection con = (HttpURLConnection) url.openConnection();
            InputStream in = con.getInputStream();
//            InputStream in = new FileInputStream("D:\\workspace\\cms\\测试excel.xls");
            ExcelHandler xlshandler = new ExcelHandler(ContentHandler.VERSION_2003);
            xlshandler.parse(in);
            System.out.println("contentType:" + con.getContentType());
            System.out.println("title:" + xlshandler.getTitle() );
            System.out.println("Author:" + xlshandler.getAuthor() );
            System.out.println("Content:" + xlshandler.getContents() );
            System.out.println("Description:" + xlshandler.getDescription() );
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
    }
}
