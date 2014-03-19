package com.frameworkset.platform.cms.searchmanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.platform.cms.searchmanager.bean.CMSSearchIndex;
import com.frameworkset.platform.cms.searchmanager.handler.ContentHandler;
import com.frameworkset.platform.cms.searchmanager.handler.DBHandler;
import com.frameworkset.platform.cms.searchmanager.handler.ExcelHandler;
import com.frameworkset.platform.cms.searchmanager.handler.HTMLHandler;
import com.frameworkset.platform.cms.searchmanager.handler.PDFHandler;
import com.frameworkset.platform.cms.searchmanager.handler.PPTHandler;
import com.frameworkset.platform.cms.searchmanager.handler.RTFHandler;
import com.frameworkset.platform.cms.searchmanager.handler.WordHandler;
import com.frameworkset.platform.cms.sitemanager.Site;
import com.frameworkset.platform.cms.util.CMSUtil;


/**
 *  
 * <p>Title: CMSCrawler.java}</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date Jul 14, 2007 12:11:55 PM
 * @author huiqiong.zeng
 * @version 1.0
 */

public class CMSCrawler  { 
	private static final Logger log = Logger.getLogger(CMSCrawler.class);
	private CMSSearchIndex index;
	private int searchType; 
//	private IndexReader reader;
	private IndexWriter writer;
	private ContentHandler handler;
	private String contentType;
	private String indexDirectory;			//索引文件绝对路径，也及当前index的索引文件库
	private long lastModified;
	private String currentURL;	//当前连接
	private String contextpath;
	private String currentURI;	//当前连接URI
	private boolean create;
	/**
	 * lucence 索引的唯一标识
	 */
	private String uid;

	private String sourceDirectory; 		//站内索引时得给出源文件绝对路径,或者ftp路径
	//频道有多个,是字符串类型,需要修改sourceDirectory,weida
	
	private Stack linkQueue;				//站外索引用
	private String[] domainUrls;			//站外索引用
	private TreeSet links;                  //已爬记录，避免重复
	private String rootURL;					//站外搜索时，当页面连接是以“/”开头的字符串时用
    private String currentURLPath;			//站外用
    
    public CMSCrawler(){
    	
    }
    
	public  CMSCrawler(CMSSearchIndex index,String contextpath,boolean create){
		this.index = index;
		this.contextpath = contextpath;
		searchType = index.getSearchType();
		indexDirectory = index.getAbsoluteIndexPath();
		if(searchType == 0){			//站内频道索引
			//sourceDirectory = CMSUtil.getChannelPubDestinction(index.getSiteId()+"",index.getChnlId()+"");
			String[] chnlIds = index.getChnlId().split(",");

			sourceDirectory = "";
			if(chnlIds.length>=1)
			{
				sourceDirectory = CMSUtil.getChannelPubDestinction(index.getSiteId()+"",chnlIds[0]+"");
				for(int r=1; r<chnlIds.length; r++)
				{
					//sourceDirectory = CMSUtil.getChannelPubDestinction(index.getSiteId()+"",chnlIds[r]+"");
					sourceDirectory = sourceDirectory + "," + CMSUtil.getChannelPubDestinction(index.getSiteId()+"",chnlIds[r]+"");
				}
			}
			
			//频道有多个,是字符串类型,需要修改sourceDirectory,weida
		}else if(searchType == 2){		//整站索引
			sourceDirectory = CMSUtil.getSitePubDestinction(index.getSiteId()+"");
		}else if(searchType == 3){		//站群索引
			String[] siteIds = index.getSite_Ids().split(",");
			sourceDirectory = "";
			if(siteIds.length>=1)
			{
				sourceDirectory = CMSUtil.getSitePubDestinction(siteIds[0]+"");
				for(int r=1; r<siteIds.length; r++)
				{
					sourceDirectory = sourceDirectory + "," + CMSUtil.getSitePubDestinction(siteIds[r]+"");
				}
			}
		}else if(searchType == 1){		//站外索引
			linkQueue = new Stack();
			links = new TreeSet();
			String[] seeds = index.getStartUrl().split(",");
			for(int i=0;i<seeds.length;i++){
				linkQueue.push(seeds[i]);
				links.add(seeds[i]);
			}
			domainUrls = index.getDomainUrl().split(",");
			
			if(seeds[0].indexOf("http://") != -1 && seeds[0].indexOf("/", 8) != -1)
				rootURL = seeds[0].substring(0, seeds[0].indexOf("/", 8));
			else if(seeds[0].indexOf("https://") != -1 && seeds[0].indexOf("/", 8) != -1)
				rootURL = seeds[0].substring(0, seeds[0].indexOf("/", 9));
		}else if(searchType == 4){}//库表索引
	}
	public void crawl(){		
			try{
				File f = new File(indexDirectory);
				if(!f.exists())
					f.mkdirs();
				Directory dir = FSDirectory.open(f);
			      // :Post-Release-Update-Version.LUCENE_XY:
			      Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_47);
			      IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_47, analyzer);

			      if (create) {
			        // Create a new index in the directory, removing any
			        // previously indexed documents:
			        iwc.setOpenMode(OpenMode.CREATE);
			      } else {
			        // Add new documents to an existing index:
			        iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
			      }

			      // Optional: for better indexing performance, if you
			      // are indexing many documents, increase the RAM
			      // buffer.  But if you do this, increase the max heap
			      // size to the JVM (eg add -Xmx512m or -Xmx1g):
			      //
			      // iwc.setRAMBufferSizeMB(256.0);
			      
			      IndexWriter writer = new IndexWriter(dir, iwc);
				
//-----------------
//				File file = new File(indexDirectory);
//		        if (!file.exists()) {		//若索引文件库不已经存在，则建立索引文件库		        	
//		        	IndexWriter indexwriter = new IndexWriter(indexDirectory, new StandardAnalyzer(), true);						//将创建新的或者覆盖已有的索引文件
//		        	indexwriter.close();
//		        } else {					//若索引文件库已经存在，则删除所有索引文件
//		        	reader = IndexReader.open(indexDirectory);		        	
//		        	if(IndexReader.isLocked(indexDirectory))		//如有其它线程在追加索引，则不建索引（处理后台线程情况），直接返回				
//		        		return;		        		
//		            int k = reader.numDocs();
//		            for (int i = 0; i < k; i++) {
//		            	reader.delete(i);
//		            }		
//		            reader.close();
//		        }
//		        writer = new IndexWriter(indexDirectory,						//可以不断的往现有的索引文件中追加记录
//		                		new StandardAnalyzer(), false);
//		        writer.maxFieldLength = Integer.MAX_VALUE;      		        
		        if(searchType == 0 || searchType == 2 || searchType == 3){			//站内频道或者整站索引或者站群索引
		        	Site site = CMSUtil.getSiteCacheManager().getSite(index.getSiteId()+"");
		        	if(site!=null && (site.getPublishDestination() == 0 || site.getPublishDestination() == 2)){		//本地检索
		        		if(site.getPublishDestination() == 2)
		        		{
		        			File srcfile = new File(sourceDirectory);
			        		indexLocalDoc(srcfile);
		        		}
		        		else
		        		{
		        			String[] sourceDirectorys = sourceDirectory.split(",");			        		
			    			for(int r=0; r<sourceDirectorys.length; r++)
			    			{
			    				File srcfile = new File(sourceDirectorys[r]);
				        		indexLocalDoc(srcfile);		        		
			    			}
			        		//频道有多个,是字符串类型,需要修改sourceDirectory,weida	
		        		}	        		    				            	
		        	}else{							//对ftp上的文件建立检索
		        		indexFtpDoc();		        		
		        	}		        	
		        }else if(searchType == 1){											//站外索引
		        	indexWebDoc();
		        }else if(searchType == 4){//库表索引
					indexDBTable(index);
				}
		        System.out.println("建立索引完毕！");
//		        writer.optimize();								//优化索引文件（合并下文件），以便快速查询
		        							//关闭索引，不然锁不能正常释放
		    }catch(Exception e){
			    e.printStackTrace();
		    }	    
		    finally
		    {
		    	if(writer != null)
		    	try {
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
//				if(reader != null)
//			    	try {
//			    		reader.close();
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}	
		    }
	}
	
	/**
	 * 本地索引，递规遍历所有指定目录下的索引文件
	 * @param srcfile
	 */
	private void indexLocalDoc(File srcfile) throws Exception{
		
		if(srcfile.isDirectory()){		//若为目录，则对改目录下所有文件递规建立索引
			log.debug("建立目录[" + srcfile.getAbsolutePath() + "]下所有文件的索引开始.");
			File[] files = srcfile.listFiles();
			Arrays.sort(files);
			for(int i=0;i<files.length;i++)
			{
				try
				{
					
					indexLocalDoc(files[i]);
				}
				catch(Exception e)
				{
					
				}
			}
			log.debug("建立目录[" + srcfile.getAbsolutePath() + "]的索引结束.");
		}else{
			
			log.debug("建立文件[" + srcfile.getAbsolutePath() + "]下所有文件的索引开始.");
			try
			{
				//获取访问地址
//				currentURL = new CMSSearchManager().
//										getPublishedFileUrl(srcfile.getAbsolutePath(),index.getSiteId() + "");
				currentURL = CMSSearchManager.
						getPublishedIndexFileUrl(srcfile.getAbsolutePath(),index.getSiteId() + "",contextpath);
				this.currentURI = CMSSearchManager.getPublishedIndexFileUri(srcfile.getAbsolutePath(),index.getSiteId() + "");
				this.uid = currentURL;
			}
			catch(Exception e)
			{
				log.error("建立文件[srcfile]" + e.getMessage(),e);
				throw e;
			}
			
			String filePath = srcfile.getPath();
			if(filePath.endsWith(".html") || filePath.endsWith(".htm") || 
					filePath.endsWith(".txt") || filePath.endsWith(".pdf") ||
					filePath.endsWith(".doc") || filePath.endsWith(".xls") ||
					filePath.endsWith(".ppt") || filePath.endsWith(".rtf")){
				
				if (filePath.endsWith(".html") || 	    	// index .html files
						filePath.endsWith(".htm") || 	    	// index .htm files
						filePath.endsWith(".txt")){			// index .txt files
					
					contentType = ContentHandler.TEXT_HTML_FILEFOMAT;
					
				}else if(filePath.endsWith(".doc")){		// index .doc files
					
					contentType = ContentHandler.WORD_FILEFOMAT;
					
				}else if(filePath.endsWith(".pdf")) { 		// index .pdf files
					
					contentType = ContentHandler.PDF_FILEFOMAT;
					
				}else if(filePath.endsWith(".xls")) {
					
					contentType = ContentHandler.EXCEL_FILEFOMAT;
					
				}else if(filePath.endsWith(".ppt")) {
					
					contentType = ContentHandler.PPT_FILEFOMAT;
					
				}else if(filePath.endsWith(".rtf")) {
					
					contentType = ContentHandler.RTF_FILEFOMAT;
					
				}
				
				lastModified = srcfile.lastModified();
				
				//在建索引文件时过滤掉文件名前缀不为content的文件
				//String fileName = srcfile.getName().substring(0,srcfile.getName().indexOf("_"));
				//if(fileName != null && fileName.equals("content")){
					this.handler = this.handleLocalFile(srcfile,contentType);
					indexLucene();
					log.debug("建立文件[" + srcfile.getAbsolutePath() + "]下所有文件的索引结束.");
				//}
			}
		}
		
	}
	
	/**
	 * 库表索引，对满足查询条件的数据逐行扫描
	 * @param srcfile
	 */
	private void indexDBTable(CMSSearchIndex index) throws Exception{
		
		log.debug("建立库表" + index.getDb_name() + ":" + index.getTable_name() + "下的索引开始.");
		System.out.println("建立库表" + index.getDb_name() + ":" + index.getTable_name() + "下的索引开始.");
		
		String db_name = index.getDb_name();
        String table_name = index.getTable_name(); 
        
        String content_fields = index.getContent_fields();
        String title_field = index.getTitle_field();
        String description_field = index.getDescription_field();
        String keyword_field = index.getKeyword_field();
        String primarys = index.getPrimarys();
        
        String content_types = index.getContent_types();
        String content_path = index.getContent_path();
        
        //域对应多字段
        String[] array_content_fields = content_fields.split(",");
        String this_content_fields = "";
        String[] array_content_path = content_path.split(",");
        String this_content_path = "";
        String[] array_title_field = title_field.split(",");
        String this_title_field = "";
        String[] array_description_field = description_field.split(",");
        String this_description_field = "";
        String[] array_keyword_field = keyword_field.split(",");
        String this_keyword_field = "";
        String[] array_primarys = primarys.split(",");
        String this_primarys = "";
        
        
        
        String publishtime_field = index.getPublishtime_field();        
        String access_url = index.getAccess_url();           
        String wheres = index.getWheres();
        
        DBUtil db = new DBUtil();
        
        String sql = "select * from " + table_name;
        if(wheres.equals(null) || wheres==null)
        	wheres = "";
        if(!(wheres=="") && !wheres.equals(""))
        	sql = sql + " where " + wheres;
        System.out.println("当前库表索引查询语句："+ sql);
        	
        try{
        	db.executeSelect(db_name, sql);
        	
    		if(db.size() >0)
    			for(int i=0;i<db.size();i++){    				
    				System.out.println("索引第"+ (i+1) + "条数据，共" + db.size() + "条");
    				Map map = new HashMap();
    				//需要依照content_types来选择如何解析content_fields
    				String filePath = "";
    				String fileType = "";
    				String fileContent = "";
    				
    		        if(content_types.equals("text") || content_types=="text"){//内容域为文本格式
    		        	for(int j=0;j<array_content_fields.length;j++){
        		        	this_content_fields = this_content_fields + "。" + db.getString(i, array_content_fields[j]);
        		        } 
    		        }else if(content_types.equals("file") || content_types=="file"){//内容域为文件路径格式
    		        	filePath = db.getString(i, content_path);
    		        }else if(content_types.equals("blog") || content_types=="blog"){//内容域为Blog文件格式
    		        	fileType = array_content_path[0];
    		        	fileContent = array_content_path[1];
    		        }  		
    		        
    		        for(int j=0;j<array_title_field.length;j++){
    		        	this_title_field = this_title_field + "。" + db.getString(i, array_title_field[j]);
    		        }    		        
    		        for(int j=0;j<array_description_field.length;j++){
    		        	this_description_field = this_description_field + "。" + db.getString(i, array_description_field[j]);
    		        }    		        
    		        for(int j=0;j<array_keyword_field.length;j++){
    		        	this_keyword_field = this_keyword_field + "。" + db.getString(i, array_keyword_field[j]);
    		        }
    		        for(int j=0;j<array_primarys.length;j++){
    		        	this_primarys = this_primarys + "。" + db.getString(i, array_primarys[j]);
    		        }    		        
    		        Date this_publishtime_field = db.getDate(i, publishtime_field);    		           		         
    		        map.put("content_fields", this_content_fields);
    				map.put("title_field", this_title_field);
    				map.put("description_field", this_description_field);
    				map.put("keyword_field", this_keyword_field);
    				map.put("publishtime_field", this_publishtime_field);
    				
    				currentURL = access_url;
    				uid = this_primarys;
    				contentType = ContentHandler.DBT_FILEFOMAT;	
    				this.handler = this.handleDBTable(map,contentType);  
    				
    				if(content_types.equals("file") || content_types=="file"){
    					if(filePath!="" && filePath!=null && !filePath.equals("") && !filePath.equals(null)){
//    						filePath = "D:\\workspace\\CMS\\creatorcms\\sitepublish\\site200\\zjcz\\content_35182.html";
    						File file = new File(filePath);			        		
			        		log.debug("建立文件[" + file.getAbsolutePath() + "]的索引开始");
			        		if(filePath.endsWith(".html") || filePath.endsWith(".htm") || 
			        			filePath.endsWith(".txt") || filePath.endsWith(".pdf") ||
			        			filePath.endsWith(".doc") || filePath.endsWith(".xls") ||
			        			filePath.endsWith(".ppt") || filePath.endsWith(".rtf")){			        				
			        			if(filePath.endsWith(".html") || 	    	// index .html files
			        					filePath.endsWith(".htm") || 	    	// index .htm files
			        					filePath.endsWith(".txt")){			// index .txt files			        					
			        				contentType = ContentHandler.TEXT_HTML_FILEFOMAT;			        					
			        			}else if(filePath.endsWith(".doc")){		// index .doc files			        					
			        				contentType = ContentHandler.WORD_FILEFOMAT;			        					
			        			}else if(filePath.endsWith(".pdf")) { 		// index .pdf files			        					
			        				contentType = ContentHandler.PDF_FILEFOMAT;			        					
			        			}else if(filePath.endsWith(".xls")) {			        					
			        				contentType = ContentHandler.EXCEL_FILEFOMAT;			        					
			        			}else if(filePath.endsWith(".ppt")) {			        					
			        				contentType = ContentHandler.PPT_FILEFOMAT;			        					
			        			}else if(filePath.endsWith(".rtf")) {			        					
			        				contentType = ContentHandler.RTF_FILEFOMAT;			        					
			        			}			        				
			        			lastModified = file.lastModified();
			        			this.handler = this.handleLocalFile(file,contentType);
			        		}			        		
    					}
    				}else if(content_types.equals("blog") || content_types=="blog"){
    					ContentHandler handler = null;
    					if(fileType.indexOf(".html")>-1 || fileType.indexOf(".htm")>-1 || fileType.indexOf(".txt")>-1){
    						contentType = ContentHandler.TEXT_HTML_FILEFOMAT;
    						handler = HTMLHandler.getInstance();
   						 	((HTMLHandler) handler).setFileType("notaspx");
    					}    						
    					if(fileType.indexOf(".doc")>-1){
    						contentType = ContentHandler.WORD_FILEFOMAT;
    						handler = PDFHandler.getInstance();
    					}    						
    					if(fileType.indexOf(".pdf")>-1){
    						contentType = ContentHandler.PDF_FILEFOMAT;
    						handler = WordHandler.getInstance();
    					}    						
    					if(fileType.indexOf(".xls")>-1){
    						contentType = ContentHandler.EXCEL_FILEFOMAT;
    						handler = ExcelHandler.getInstance();
    					}    						
    					if(fileType.indexOf(".ppt")>-1){
    						contentType = ContentHandler.PPT_FILEFOMAT;
    						handler = PPTHandler.getInstance();
    					}    						
    					if(fileType.indexOf(".rtf")>-1){
    						contentType = ContentHandler.RTF_FILEFOMAT;
    						handler = RTFHandler.getInstance();
    					}
    					InputStream in = db.getBinaryStream(i, fileContent);
    					handler.parse(in);
    				}  
    				indexLucene();
    			}	
        }catch(Exception e){e.printStackTrace();}   
	}
	
	/**
	 * web索引，遍历所有连接
	 * @throws Exception
	 */
	private void indexWebDoc() throws Exception{
		while(!linkQueue.isEmpty()){
			 
			String s = (String) linkQueue.pop();
			if (!s.startsWith(rootURL) ) {
				if(s.indexOf("http://") != -1 && s.indexOf("/", 8) != -1)
					rootURL = s.substring(0, s.indexOf("/", 8));
				else if(s.indexOf("https://") != -1 && s.indexOf("/", 8) != -1)
					rootURL = s.substring(0, s.indexOf("/", 9));
            }
			System.out.println("扫描" + s + "中...");
			String s2 = scanPage(s);
			System.out.println(s2);
			//writer.optimize();    
		}
	}
	private void indexFtpDoc() throws Exception{
		throw new Exception("尚未实现！");
		/**********************************************************************
		 * todo!
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 **********************************************************************/
	}
	
	/**
	 * 处理本地文件,同时为外部提供调用接口（新发布文档时，动态的处理文档增加索引）
	 * @param srcfile
	 * @throws Exception
	 */
	public ContentHandler handleLocalFile(File srcfile,String contentType) throws Exception {
		 System.out.println("扫描" + srcfile.getAbsolutePath() + "中...");
		 
		 ContentHandler handler = null;
		 if(contentType.equals(ContentHandler.TEXT_HTML_FILEFOMAT)){
			 
			 handler = HTMLHandler.getInstance();
			 ((HTMLHandler) handler).setFileType("notaspx");
			 
		 }else if(contentType.equals(ContentHandler.PDF_FILEFOMAT)){
			 
			 handler = PDFHandler.getInstance();
			 
		 }else if(contentType.equals(ContentHandler.WORD_FILEFOMAT)){
			 
			 handler = WordHandler.getInstance();
			 
		 }else if(contentType.equals(ContentHandler.EXCEL_FILEFOMAT)){
			 
			 handler = ExcelHandler.getInstance();
			 
		 }else if(contentType.equals(ContentHandler.PPT_FILEFOMAT)){
			 
			 handler = PPTHandler.getInstance();
			 
		 }else if(contentType.equals(ContentHandler.RTF_FILEFOMAT)){
			 
			 handler = RTFHandler.getInstance();
			 
		 }
//		 String content = FileUtil.getFileContent(srcfile, "UTF-8");
//		 byte[] bytes  = content.getBytes( );
//		
//		 handler.parse( new ByteArrayInputStream(bytes,0,bytes.length));	
		 handler.parse(new FileInputStream(srcfile));				//解析
		 
		 System.out.println("解析正常！");
		 
		 return handler;
	 }
	
	/**
	 * 处理本地文件,同时为外部提供调用接口（新发布文档时，动态的处理文档增加索引）
	 * @param srcfile
	 * @throws Exception
	 */
	public ContentHandler handleDBTable(Map map,String contentType) throws Exception {
		 ContentHandler handler = null;
		 if(contentType.equals(ContentHandler.DBT_FILEFOMAT)){			 
			 handler = DBHandler.getInstance();			 
		 }		 
		 handler.parse(map);				//解析		 
		 System.out.println("解析正常！");		 
		 return handler;
	 }
	
	/**
	 * 处理网络文件，不为外部提供接口,因为handleLink处理结果并没有返回，而是直接对数据成员赋值了
	 */
	private ContentHandler handWebFile(HttpURLConnection httpurlconnection,String contentType) throws Exception{
		 System.out.println("扫描网页" + httpurlconnection.getURL().toString() + "中...");
		
		 ContentHandler handler = null;
		 if(contentType.equals(ContentHandler.TEXT_HTML_FILEFOMAT)){
			 
			 handler = HTMLHandler.getInstance();
			 ((HTMLHandler) handler).setFileType("notaspx");
			 
			 //处理网页连接
			 if (handler.getRobotFollow()) {
			     List list = handler.getLinks();
			     for (int i = 0; i < list.size(); i++) {
			    	 
			         handleLink((String) list.get(i));
			     }
			
			 }
		 }else if(contentType.equals(ContentHandler.PDF_FILEFOMAT)){
			 
			 handler = PDFHandler.getInstance();
			 
		 }else if(contentType.equals(ContentHandler.WORD_FILEFOMAT)){
			 
			 handler = WordHandler.getInstance();
			 
		 }else if(contentType.equals(ContentHandler.EXCEL_FILEFOMAT)){
			 
			 handler = ExcelHandler.getInstance();
			 
		 }else if(contentType.equals(ContentHandler.PPT_FILEFOMAT)){
			 
			 handler = PPTHandler.getInstance();
			 
		 }else if(contentType.equals(ContentHandler.RTF_FILEFOMAT)){
			 
			 handler = RTFHandler.getInstance();
			 
		 }
		 
		 handler.parse(httpurlconnection.getInputStream());				//解析
		 
		 System.out.println("解析正常！");
		 return handler;
	}
	
	private void handleLink(String s) {
	     String s1 = s.toLowerCase();
	     if (!s1.startsWith("http://") && !s1.startsWith("https://")) {
	        s = parseHREF(s, s1);
	        if (s != null) {
	           s1 = s.toLowerCase();
	        }
	     }
	     if (s != null && inScope(s1) && !links.contains(s1)) {
	        links.add(s1);
	        linkQueue.push(s);
	     }
	}
	public boolean inScope(String s) {
        for (int i = 0; i < domainUrls.length; i++) {
            if (s.indexOf(domainUrls[i]) != -1) {
                return true;
            }
        }

        return false;
    }
	private String parseHREF(String s, String s1) {
		if (s1.startsWith("/")) {
			if(rootURL != null)
				s = rootURL + s;
			else 
				s = null;
        } else
        if (s1.startsWith("./")) {
            s = currentURLPath + s.substring(1, s.length());
        } else
        if (s1.startsWith("../")) {
            int i;
            for (i = 1; s1.indexOf("../", i * 3) != -1; i++) {}
            int k = currentURLPath.length();
            for (int l = i; l-- > 0; ) {
                k = currentURLPath.lastIndexOf("/", k) - 1;
            }

            s = currentURLPath.substring(0, k + 2) +
                s.substring(3 * i, s.length());
        } else
        if (s1.startsWith("javascript:")) {
            s = parseJavaScript(s, s1);
        } else
        if (s1.startsWith("#")) {
            s = null;
        } else
        if (s1.startsWith("mailto:")) {
            s = null;
        } else {
            s = currentURLPath + "/" + s;
        }
        int j;
        if (s != null && (j = s.indexOf("#")) != -1) {
            s = s.substring(0, j);
        }
        return s;
	} 
	
	private String parseJavaScript(String s, String s1) {
	    if (s1.startsWith("pop", 11)) {
	        int i = s1.indexOf("'", 13);
	        if (i != -1) {
	           int j = s1.indexOf("'", i + 1);
	           if (j != -1) {
	               s = s.substring(i + 1, j);
	               return parseHREF(s, s.toLowerCase());
	            }
	        }
	        return null;
	   } else {
	        return null;
	   }
	 } 
	
		/**
		 * 扫描网页，用于外部链接的处理
		 * @param s
		 * @return
		 */
		private String scanPage(String s) {
	        String s1 = "连接正常";
	        try {
	            URL _currentURL = new URL(s);
	            currentURLPath = s.substring(0, s.lastIndexOf("/"));
	            HttpURLConnection httpurlconnection = (HttpURLConnection)
	                                                  _currentURL.openConnection();
	            httpurlconnection.setRequestProperty("User-Agent",
	                    "i2a Web Search Engine Crawler");
	            
	            httpurlconnection.connect();
	           
	            if (httpurlconnection.getResponseCode() == 200) {
	            	this.currentURL = _currentURL.toString();
	            	this.uid = currentURL;
	                //文件格式判断
	                contentType = httpurlconnection.getContentType();
	                if(s.endsWith(".doc") || s.endsWith(".xls") ||
	                		s.endsWith(".ppt") || s.endsWith(".pdf") || s.endsWith(".rtf") || 
	                		contentType.indexOf("text/html") != -1){
	                	
		                if(contentType.indexOf("text/html") != -1){
		                	
		                	contentType = ContentHandler.TEXT_HTML_FILEFOMAT;
		                	
		                }else if (s.endsWith(".doc")) {
		                	
		                	contentType = ContentHandler.WORD_FILEFOMAT;
		                    
		                } else if(s.endsWith(".xls")){
		                	
		                	contentType = ContentHandler.EXCEL_FILEFOMAT;
		                	
		                }else if(s.endsWith(".ppt")){

		                	contentType = ContentHandler.PPT_FILEFOMAT;
		                	
		                }else if(s.endsWith(".pdf")){

		                	contentType = ContentHandler.PDF_FILEFOMAT;
		                	
		                }else if(s.endsWith(".rtf")){

		                	contentType = ContentHandler.RTF_FILEFOMAT;
		                	
		                }
		                
		                lastModified = httpurlconnection.getLastModified();
						this.handler = this.handWebFile(httpurlconnection,contentType);
						
						if(!contentType.equals(ContentHandler.TEXT_HTML_FILEFOMAT))
							indexLucene();
						else if (handler.getRobotIndex()) {					
							if (s.indexOf("aspx") != -1) {
	                            ((HTMLHandler) handler).setFileType("aspx");
	                        } else {
	                            ((HTMLHandler) handler).setFileType("notaspx");
	                        }
						    indexLucene();
						 }
						
	                }else {
                        s1 = "Not an excepted content type : " + contentType;
                    }
	            } else {
	                s1 = "连接超时！";
	            }
	            httpurlconnection.disconnect();
	        } catch (MalformedURLException malformedurlexception) {
	            s1 = malformedurlexception.toString();
	        } catch (UnknownHostException unknownhostexception) {
	            s1 = unknownhostexception.toString();
	        } catch (IOException ioexception) {
	            s1 = ioexception.toString();
	        } catch (Exception exception) {
	            s1 = exception.toString();
	        }
	        return s1;
	    }
	private void indexLucene() {
        try {

            log.debug("开始分析！");
        	
            Document document = new Document();
//            document.add(new StringField("uid", uid, Field.Store.YES));
            FieldType fieldType = new FieldType();
			fieldType.setStored(false);
			fieldType.setIndexed(true);
			fieldType.setStoreTermVectors(true);
			document.add(new Field("uid",uid, fieldType));
            document.add(new StringField("url", currentURL, Field.Store.YES));
            if(currentURI == null)
            	currentURI = currentURL;
            document.add(new StringField("uri", currentURI, Field.Store.YES));
            document.add(new StringField("contentType", contentType, Field.Store.YES));
            document.add(new LongField("lastModified",
                                      lastModified, Field.Store.NO));
//            document.add(Field.Text("content", handler.getContents()));
            if (handler.getContents() != null) {
            	document.add(new TextField("content", handler.getContents(), Field.Store.YES));
            }
            if (handler.getTitle() != null) {
                document.add(new TextField("title", handler.getTitle(), Field.Store.YES));
            }
            if (handler.getKeywords() != null) {
                document.add(new TextField("keyword", handler.getKeywords(), Field.Store.YES));
            }
            if (handler.getDescription() != null) {
                document.add(new TextField("description", handler.getDescription(), Field.Store.YES));
            }
            if (handler.getCategories() != null) {
                document.add(new TextField("categories", handler.getCategories(), Field.Store.YES));
            }
            if (handler.getPublished() != -1L) {
                document.add(new LongField("published",
                                           handler.
                        getPublished(), Field.Store.NO));
            } else {
                document.add(new LongField("published",
                                           lastModified, Field.Store.NO));
            }
            /**
             * 增加一个文件格式的域
             */
            document.add(new StringField("fileFormat",handler.getFileFormat(), Field.Store.YES));

            if (handler.getHREF() != null) {
                String s = handler.getHREF();
                int i = s.indexOf("$link");
                s = s.substring(0, i) + currentURL.toString() +
                    s.substring(i + 5, s.length());
                document.add(new StringField("href", s, Field.Store.YES));
            }
            
            /* meta
			 * site,站点名字
			 * channel,频道名字
			 * dockind,文档类型
			 * title,标题
			 * subtitle,显示标题
			 * secondtitle,副标题
			 * author,作者
			 * keywords,关键词
			 * abstracts,摘要内容
			 * */
            if (handler.getSite() != null) {
            	document.add(new StringField("site", handler.getSite(), Field.Store.YES));
	        }
            if (handler.getChannel() != null) {
            	document.add(new StringField("channel", handler.getChannel(), Field.Store.YES));
	        }
            if (handler.getDockind() != null) {
            	document.add(new StringField("dockind", handler.getDockind(), Field.Store.YES));
	        }
            if (handler.getSubtitle() != null) {
            	document.add(new StringField("subtitle", handler.getSubtitle(), Field.Store.YES));
	        }
            if (handler.getAuthor() != null) {
            	document.add(new StringField("author", handler.getAuthor(), Field.Store.YES));
	        }
            if (handler.getAbstracts() != null) {
            	document.add(new StringField("abstracts", handler.getAbstracts(), Field.Store.YES));
	        }
            if (handler.getAbstracts() != null) {
            	document.add(new StringField("copyright", handler.getCopyright(), Field.Store.YES));
	        }
            if (handler.getDocwtime() != -1L) {
                document.add(new LongField("docwtime",
                                          handler.
                                        		   getDocwtime(), Field.Store.YES));
            } else {
                document.add(new LongField("docwtime",
                                           lastModified, Field.Store.YES));
            }
            
            if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
                // New index, so we just add the document (no old document can be there):
//                System.out.println("adding " + file);
                writer.addDocument(document);
              } else {
                // Existing index (an old copy of this document may have been indexed) so 
                // we use updateDocument instead to replace the old one matching the exact 
                // path, if present:
//                System.out.println("updating " + file);
                writer.updateDocument(new Term("uid", uid), document);
              }
            log.debug("插入索引记录成功！");
        } catch (Exception exception) {
            
        	log.error("", exception);
        }
    }
	/**
	 * 提供插入索引记录的接口
	 * @param writer
	 * @param handlerParameter
	 * @param currentURLStr
	 * @param contentTypeParameter
	 * @param lastModifiedParameter
	 */
	public void indexLucene(IndexWriter writer,ContentHandler handler,
			String currentURLStr,String contentType,long lastModified) {
		try {
			Document document = new Document();
			currentURL = currentURLStr;
			uid = currentURLStr;
			FieldType fieldType = new FieldType();
			fieldType.setStored(false);
			fieldType.setIndexed(true);
			fieldType.setStoreTermVectors(false);
			document.add(new Field("uid",currentURLStr, fieldType));
			document.add(new StringField("url", currentURLStr, Field.Store.YES));
			document.add(new StringField("contentType", contentType, Field.Store.YES));
			document.add(new LongField("lastModified",
			                           lastModified, Field.Store.YES));
			document.add(new TextField("content", handler.getContents(), Field.Store.YES));
			if (handler.getTitle() != null) {
			    document.add(new TextField("title", handler.getTitle(), Field.Store.YES));
			}
			if (handler.getKeywords() != null) {
			    document.add(new TextField("keyword", handler.getKeywords(), Field.Store.YES));
			}
			if (handler.getDescription() != null) {
			    document.add(new TextField("description", handler.getDescription(), Field.Store.YES));
			}
			if (handler.getCategories() != null) {
			    document.add(new TextField("categories", handler.getCategories(), Field.Store.YES));
			}
			if (handler.getPublished() != -1L) {
			    document.add(new LongField("published",
			                              handler.
			            getPublished(), Field.Store.YES));
			} else {
			    document.add(new LongField("published",
			                               lastModified, Field.Store.YES));
			}
			/**
			 * 增加一个文件格式的域
			 */
			document.add(new StringField("fileFormat",handler.getFileFormat(), Field.Store.YES));
			
			if (handler.getHREF() != null) {
			    String s = handler.getHREF();
			    int i = s.indexOf("$link");
			    s = s.substring(0, i) + currentURLStr.toString() +
			        s.substring(i + 5, s.length());
			    document.add(new StringField("href", s, Field.Store.YES));
			}
			
			/* meta
			 * site,站点名字
			 * channel,频道名字
			 * dockind,文档类型
			 * title,标题
			 * subtitle,显示标题
			 * secondtitle,副标题
			 * author,作者
			 * keywords,关键词
			 * abstracts,摘要内容
			 * */
            if (handler.getSite() != null) {
            	document.add(new StringField("site", handler.getSite(), Field.Store.YES));
	        }
            if (handler.getChannel() != null) {
            	document.add(new StringField("channel", handler.getChannel(), Field.Store.YES));
	        }
            if (handler.getDockind() != null) {
            	document.add(new StringField("dockind", handler.getDockind(), Field.Store.YES));
	        }
            if (handler.getSubtitle() != null) {
            	document.add(new StringField("subtitle", handler.getSubtitle(), Field.Store.YES));
	        }
            if (handler.getAuthor() != null) {
            	document.add(new StringField("author", handler.getAuthor(), Field.Store.YES));
	        }
            if (handler.getAbstracts() != null) {
            	document.add(new StringField("abstracts", handler.getAbstracts(), Field.Store.YES));
	        }
            if (handler.getAbstracts() != null) {
            	document.add(new StringField("copyright", handler.getCopyright(), Field.Store.YES));
	        }
            if (handler.getDocwtime() != -1L) {
                document.add(new LongField("docwtime",
                                           handler.
                                        		   getDocwtime(), Field.Store.YES));
            } else {
                document.add(new LongField("docwtime",
                                           lastModified, Field.Store.YES));
            }
			
            if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
                // New index, so we just add the document (no old document can be there):
//                System.out.println("adding " + file);
                writer.addDocument(document);
              } else {
                // Existing index (an old copy of this document may have been indexed) so 
                // we use updateDocument instead to replace the old one matching the exact 
                // path, if present:
//                System.out.println("updating " + file);
                writer.updateDocument(new Term("uid", uid), document);
              }
			log.debug("插入索引记录成功！");
		} catch (Exception exception) {
			
			log.error("", exception);
		}
	}
	
	
	/*
	 * 删除索引名为indexbaseName下的uid值记录
	 * */
	public void deleteIndex(String indexbaseName,String uid )
	{
		CMSSearchIndex index = null;
		IndexWriter writer = null;
		try {
			index = CMSSearchManager.getIndexIdByIndexName(indexbaseName);
			String absoluteIndexFilePath = CMSSearchManager.getAbsoluteIndexFilePath(index);
			File f = new File(absoluteIndexFilePath);
			if(f.exists()){
				Directory dir = FSDirectory.open(f);
			      // :Post-Release-Update-Version.LUCENE_XY:
			      Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_47);
			      IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_47, analyzer);

			     
			        // Add new documents to an existing index:
			        iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
			      
			      

			      // Optional: for better indexing performance, if you
			      // are indexing many documents, increase the RAM
			      // buffer.  But if you do this, increase the max heap
			      // size to the JVM (eg add -Xmx512m or -Xmx1g):
			      //
			      // iwc.setRAMBufferSizeMB(256.0);
			      
			      writer = new IndexWriter(dir, iwc);
				    
				if(!CMSSearchManager.isIndexLocked(index)){
					Term term = new Term("uid",uid);
					writer.deleteDocuments(term);
					
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		finally
		{
			if(writer != null)
				try {
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

	public void updateIndex(String indexbaseName, Object indexobject)
	{
		
	}
	
	public void addIndex(String indexbaseName, Object indexobject)
	{
		
	}
	
}