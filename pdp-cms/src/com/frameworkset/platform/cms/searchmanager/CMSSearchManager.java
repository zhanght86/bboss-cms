package com.frameworkset.platform.cms.searchmanager;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.common.tag.pager.ListInfo;
import com.frameworkset.platform.cms.channelmanager.Channel;
import com.frameworkset.platform.cms.channelmanager.ChannelManager;
import com.frameworkset.platform.cms.channelmanager.ChannelManagerImpl;
import com.frameworkset.platform.cms.documentmanager.DocumentManager;
import com.frameworkset.platform.cms.documentmanager.DocumentManagerImpl;
import com.frameworkset.platform.cms.flowmanager.FlowManagerException;
import com.frameworkset.platform.cms.searchmanager.bean.CMSSearchHit;
import com.frameworkset.platform.cms.searchmanager.bean.CMSSearchIndex;
import com.frameworkset.platform.cms.searchmanager.handler.ContentHandler;
import com.frameworkset.platform.cms.sitemanager.Site;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.platform.config.ConfigManager;
import com.frameworkset.util.SimpleStringUtil;

public class CMSSearchManager {
	private static Logger log = LoggerFactory.getLogger(CMSSearchManager.class);
	/**类型-站点频道索引*/
	public static final int SEARCHTYPE_SITECHANNEL = 0;
	/**类型-站外索引*/
	public static final int SEARCHTYPE_EXTERNAL = 1;
	
	/**类型-站内索引*/
	public static final int SEARCHTYPE_SITE = 2;
	
	/**类型-站群索引*/
	public static final int SEARCHTYPE_SITES = 3;
	
	/**类型-库表索引*/
	public static final int SEARCHTYPE_DBTABLE = 4;
	
//	public static float searchTime=0;    //记录检索时间
	
	/**
	 * 获取站内搜索的索引列表，包括站内频道和整个站点索引
	 * @param sql
	 * @param offset
	 * @param maxPagesize
	 * @return
	 */
	public ListInfo getLocalSearchIndexList(String sql,int offset,int maxPagesize) throws Exception{
		DBUtil db = new DBUtil();
		DBUtil db1 = new DBUtil();
		ListInfo listInfo = new ListInfo(); 
		try{
			db.executeSelect(sql,offset,maxPagesize);
			List indexList = new ArrayList();
			for(int i=0;i<db.size();i++){
				CMSSearchIndex searchIndex = new CMSSearchIndex();
				searchIndex.setId(db.getInt(i,"id"));
				String siteId = db.getInt(i,"site_id")+"";
				searchIndex.setSiteId(siteId);
				searchIndex.setSiteName(db.getString(i,"siteName"));
				
				int searchType = db.getInt(i,"search_type");
				searchIndex.setSearchType(searchType);			//站内搜索,包括站内频道和整个站点
				String sql1;
				//对于站点频道索引要设置索引的频道id以及频道名
	            if(searchType == CMSSearchManager.SEARCHTYPE_SITECHANNEL){
	            	String chnlId = db.getString(i,"chnl_or_domain");
	            	
//	            	searchIndex.setChnlId(Integer.parseInt(chnlId));	            	
//	            	sql1 = "select display_name from td_cms_channel where channel_id = " + chnlId;
//	            	db1.executeSelect(sql1);
//	            	if(db1.size()>0)
//	            		searchIndex.setChnlName(db1.getString(0,"display_name"));
	            	
	            	searchIndex.setChnlId(chnlId);
	            	String[] chnlIds = chnlId.split(",");
	            	String chnlNames = "";	            	
	            	if(chnlIds.length>=1)
	            	{
	            		
		            	sql1 = "select display_name from td_cms_channel where channel_id = " + chnlIds[0];
		            	db1.executeSelect(sql1);
		            	if(db1.size()>0)
		            	{
		            		chnlNames = db1.getString(0,"display_name");
		            	}		            		
	            		for(int r=1; r<chnlIds.length; r++)
		            	{
			            	sql1 = "select display_name from td_cms_channel where channel_id = " + chnlIds[r];
			            	db1.executeSelect(sql1);
			            	if(db1.size()>0)
			            	{
			            		chnlNames = chnlNames + "," + db1.getString(0,"display_name");
			            	}
		            	}
	            		searchIndex.setChnlName(chnlNames);
	            	}
	            	//有多个频道,id改成字符串,weida
	            
	            }
				
				searchIndex.setLever(db.getInt(i,"lever"));
				searchIndex.setDay(db.getInt(i,"day"));
				searchIndex.setTime(db.getInt(i,"time"));
				
				String indexName = db.getString(i,"name");
				searchIndex.setIndexName(indexName);
				
				//索引库相对路径在在配置文件config-manager.xml中配
				String indexPath = getIndexRootPath();
				searchIndex.setIndexPath(indexPath);			
				
				searchIndex.setAbsoluteIndexPath(
						this.getAbsoluteIndexFilePath(indexPath,"in_" + indexName,siteId+""));
				indexList.add(searchIndex);
			}
			listInfo.setDatas(indexList);
			listInfo.setTotalSize(db.getTotalSize());
		 }catch(SQLException e){
			 e.printStackTrace();
			 throw new Exception("获取站内索引列表时失败！" + e.getMessage());
		 }
		return listInfo;
	}
	/**
	 * 获取站群搜索的索引列表，weida
	 * @param sql
	 * @param offset
	 * @param maxPagesize
	 * @return
	 */
	public ListInfo getSitesSearchIndexList(String sql,int offset,int maxPagesize) throws Exception{
		DBUtil db = new DBUtil();
		DBUtil db1 = new DBUtil();
		ListInfo listInfo = new ListInfo(); 
		try{
			db.executeSelect(sql,offset,maxPagesize);
			List indexList = new ArrayList();
			for(int i=0;i<db.size();i++){
				CMSSearchIndex searchIndex = new CMSSearchIndex();
				searchIndex.setId(db.getInt(i,"id"));				
				String siteId = db.getInt(i,"site_id")+"";
				searchIndex.setSiteId(siteId);
				
				String siteId2 = db.getString(i,"site_ids")+"";				
				String[] siteIds = siteId2.split(",");
				String siteNames = "";
				DBUtil db2 = new DBUtil();
				String sql2 = "select name from td_cms_site where site_id =";
				if(siteIds.length>0)
				{
					if(!(siteIds[0].equals("")) && !(siteIds[0].equals(null)))
					{
						db2.executeSelect(sql2 + siteIds[0]);
						
						//
						
						siteNames = db2.getString(0,"name");
					}
					for(int r=1;r<siteIds.length;r++)
					{
						db2.executeSelect(sql2 + siteIds[r]);
						siteNames = siteNames + "," + db2.getString(0,"name");
					}
				}
				//searchIndex.setSiteName(db.getString(i,"siteName"));
				searchIndex.setSiteNames(siteNames);
				
				int searchType = db.getInt(i,"search_type");
				searchIndex.setSearchType(searchType);			
				searchIndex.setLever(db.getInt(i,"lever"));
				searchIndex.setDay(db.getInt(i,"day"));
				searchIndex.setTime(db.getInt(i,"time"));
				String indexName = db.getString(i,"name");
				searchIndex.setIndexName(indexName);
				String indexPath = getIndexRootPath();
				searchIndex.setIndexPath(indexPath);	
				searchIndex.setAbsoluteIndexPath(
						this.getAbsoluteIndexFilePath(indexPath,"sites_" + indexName,siteId+""));
				indexList.add(searchIndex);
			}
			listInfo.setDatas(indexList);
			listInfo.setTotalSize(db.getTotalSize());
		 }catch(SQLException e){
			 e.printStackTrace();
			 throw new Exception("获取站群索引列表时失败！" + e.getMessage());
		 }
		return listInfo;
	}
	/**
	 * 获取数据库搜索的索引列表，weida
	 * @param sql
	 * @param offset
	 * @param maxPagesize
	 * @return
	 */
	public ListInfo getDBTSearchIndexList(String sql,int offset,int maxPagesize) throws Exception{
		DBUtil db = new DBUtil();

		ListInfo listInfo = new ListInfo(); 
		try{
			db.executeSelect(sql,offset,maxPagesize);
			List indexList = new ArrayList();
			for(int i=0;i<db.size();i++){
				CMSSearchIndex searchIndex = new CMSSearchIndex();
				searchIndex.setId(db.getInt(i,"id"));				
				String siteId = db.getInt(i,"site_id")+"";
				searchIndex.setSiteId(siteId);	
				searchIndex.setSearchType(db.getInt(i,"search_type"));			
				searchIndex.setLever(db.getInt(i,"lever"));
				searchIndex.setDay(db.getInt(i,"day"));
				searchIndex.setTime(db.getInt(i,"time"));
				searchIndex.setDb_name(db.getString(i,"dbName"));
				searchIndex.setTable_name(db.getString(i,"tableName"));
				String indexName = db.getString(i,"name");
				searchIndex.setIndexName(indexName);
				String indexPath = getIndexRootPath();
				searchIndex.setIndexPath(indexPath);	
				searchIndex.setAbsoluteIndexPath(
						this.getAbsoluteIndexFilePath(indexPath,"db_" + indexName,siteId+""));
				indexList.add(searchIndex);
			}
			listInfo.setDatas(indexList);
			listInfo.setTotalSize(db.getTotalSize());
		 }catch(SQLException e){
			 e.printStackTrace();
			 throw new Exception("获取数据库索引列表时失败！" + e.getMessage());
		 }
		return listInfo;
	}
	/**
	 * 获取站外搜索的索引列表
	 * @param sql
	 * @param offset
	 * @param maxPagesize
	 * @return
	 * @throws Exception
	 */
	public ListInfo getWebSearchIndexList(String sql,int offset,int maxPagesize) throws Exception{
		DBUtil db = new DBUtil();
		ListInfo listInfo = new ListInfo(); 
		try{
			db.executeSelect(sql,offset,maxPagesize);
			List indexList = new ArrayList();
			for(int i=0;i<db.size();i++){
				CMSSearchIndex searchIndex = new CMSSearchIndex();
				searchIndex.setId(db.getInt(i,"id"));
				String siteId = db.getInt(i,"site_id")+"";
				searchIndex.setSiteId(siteId);
				searchIndex.setSiteName(db.getString(i,"siteName"));
				
				searchIndex.setLever(db.getInt(i,"lever"));
				searchIndex.setDay(db.getInt(i,"day"));
				searchIndex.setTime(db.getInt(i,"time"));
				
				searchIndex.setSearchType(CMSSearchManager.SEARCHTYPE_EXTERNAL);			//站外搜索
				
				searchIndex.setDomainUrl(db.getString(i,"chnl_or_domain"));			//站外搜索
				searchIndex.setStartUrl(db.getString(i,"start_url"));				//站外搜索
				
				String indexName = db.getString(i,"name");
				searchIndex.setIndexName(indexName);
				
				//索引库相对路径在在配置文件config-manager.xml中配
				String indexPath = getIndexRootPath();
				searchIndex.setIndexPath(indexPath);	
				
				searchIndex.setAbsoluteIndexPath(
						this.getAbsoluteIndexFilePath(indexPath,"out_" + indexName,siteId+""));
				indexList.add(searchIndex);
			}
			listInfo.setDatas(indexList);
			listInfo.setTotalSize(db.getTotalSize());
		 }catch(SQLException e){
			 e.printStackTrace();
			 throw new Exception("获取站内索引列表时失败！" + e.getMessage());
		 }
		return listInfo;
		
	}
	/**
	 * 获取所有索引列表，包括所有站点的站内搜索和站外搜索,以便后台启动的线程定时的遍历这些索引
	 * @return
	 */
	public List getIndexList() throws Exception{
		String sql = "select * from td_cms_site_search";
		return this.getIndexList(sql);
	}
	/**
	 * 索引库相对路径在在配置文件config-manager.xml中配，暂时未添加
	 * @return
	 * @throws Exception
	 */
	public static String getIndexRootPath() throws Exception{
		
		return ConfigManager.getInstance().
									getConfigValue("cms.index.root.path","/WEB-INF/search/");
	}
	/**
	 * 根据相对的索引库路径，获取绝对索引库路径
	 * @param indexPath
	 * @return
	 */
	public static String getAbsoluteIndexRootPath(String indexPath) throws Exception{
		//web工程根目录
		String appRootPath = CMSUtil.getAppRootPath();
		
		String indexLocation = CMSUtil.getPath(appRootPath,indexPath);
		
		//若该目录不存在则创建相应的目录
		File file = new File(indexLocation);
		if(!file.exists())
			file.mkdir();
		
		return indexLocation;
	}
	
	/**
	 * 获取索引文件的绝对路径
	 * 如：D:\workspace\cms\creatorcms\WEB-INF\search\chenzhougov\in_走进郴州
	 * 必须保证除最后一级目录外其它所有目录都存在，如D:\workspace\cms\creatorcms\WEB-INF\search\chenzhougov存在
	 * 最后一级目录在建索引时建，如in_走进郴州
	 * @param indexPath
	 * @param indexFileName  "in_"或"out_"或"site_"或"sites_"或"db_" + 索引名
	 * @param siteId  站内搜索传来站点id;若为站外搜索则siteId传来null;
	 * @return
	 * @throws Exception
	 */
	public static String getAbsoluteIndexFilePath(String indexPath,String indexFileName,String siteId) throws Exception{
		String indexFilePath = getAbsoluteIndexRootPath(indexPath);
		if(siteId == null){			
			indexFilePath = CMSUtil.getPath(indexFilePath,indexFileName);
		}else{						
			Site site = CMSUtil.getSiteCacheManager().getSite(siteId);
			if(site!=null){
				indexFilePath = CMSUtil.getPath(indexFilePath,site.getSecondName());
				//若该目录不存在则创建相应的目录
				File file = new File(indexFilePath);
				
				if(!file.exists())
					file.mkdir();
				indexFilePath = CMSUtil.getPath(indexFilePath,indexFileName);
			}
		}
		return indexFilePath.replace('\\','/');
	}
	/**
	 * 获取索引文件的绝对路径，
	 * 必须保证除最后一级目录外其它所有目录都存在，
	 * 最后一级目录在建索引时建
	 * @param index
	 * @return
	 * @throws Exception
	 */
	public static String getAbsoluteIndexFilePath(CMSSearchIndex index) throws Exception{
		if(index.getSearchType() == CMSSearchManager.SEARCHTYPE_SITECHANNEL){   //站内频道搜索
			return getAbsoluteIndexFilePath(index.getIndexPath(),
						"in_" + index.getIndexName(),index.getSiteId()+"");
		}else if(index.getSearchType() == CMSSearchManager.SEARCHTYPE_EXTERNAL){			//站外搜索
			return getAbsoluteIndexFilePath(index.getIndexPath(),
						"out_" + index.getIndexName(),index.getSiteId()+"");
		}else if(index.getSearchType() == CMSSearchManager.SEARCHTYPE_SITES){			//站群索引搜索
			return getAbsoluteIndexFilePath(index.getIndexPath(),
						"sites_" + index.getIndexName(),index.getSiteId()+"");
		}else if(index.getSearchType() == CMSSearchManager.SEARCHTYPE_SITE){			//整站索引
			return getAbsoluteIndexFilePath(index.getIndexPath(),
					"site_" + index.getIndexName(),index.getSiteId()+"");
		}else {  																	//库表检索
			return getAbsoluteIndexFilePath(index.getIndexPath(),
					"db_" + index.getIndexName(),index.getSiteId()+"");
		}
	}
	/**
	 * 获取指定站点下的指定类型的所有索引，搜索时在用户没有选择索引的情况下，用于获取索引
	 * @param siteId
	 * @param flag 0－站内频道索引；1－站外站点索引；2－整站索引
	 * @return
	 * @throws Exception
	 */
	public List getIndexListOfSite(String siteId,int flag) throws Exception {
		String sql = "select * from td_cms_site_search where site_id = " + siteId + " and search_type = " + flag;
		return this.getIndexList(sql);
	}
	
	/**
	 * 获取指定索引id的索引
	 * @param indexId
	 * @return
	 */
	public CMSSearchIndex getIndex(String indexId) throws Exception{
		String sql = "select * from td_cms_site_search where id = " + indexId;
		return this.getIndexBySQL(sql);
	}
	/**
	 * 根据频道id获取指定索引
	 * @param chnlId
	 * @return
	 * @throws Exception
	 */
	public CMSSearchIndex getIndexByChnlId(String chnlId) throws Exception{
		//String sql = "select * from td_cms_site_search where chnl_or_domain = '" + chnlId + "'";
		String sql = "select * from td_cms_site_search where chnl_or_domain like '%" + chnlId + "%'";
		//因为频道id现在是保存多个在chnlId,weida
		return this.getIndexBySQL(sql); 
	}
	
	/**
	 * 根据频道id获取相关频道索引和整站索引,weida
	 * @param chnlId
	 * @return
	 * @throws Exception
	 */
	public ArrayList getIndexsByChnlId(String chnlId) throws Exception{
		String sql = "select * from td_cms_site_search where search_type='0' and chnl_or_domain like '%" + chnlId + "%'";
		return this.getIndexsBySQL(sql); 
	}
	
	/**
	 * 根据文档id获取当前频道相关索引和整站索引,weida
	 * @param docId
	 * @return
	 * @throws Exception
	 */
	public ArrayList getIndexsByDocId(String docId) throws Exception{
		DocumentManager dm = new DocumentManagerImpl();
		String chnlId = String.valueOf(dm.getDocChnlId(Integer.valueOf(docId).intValue()));
		ChannelManager cm = new ChannelManagerImpl();
		Channel channel = cm.getChannelInfo(chnlId);
		String siteId = String.valueOf(channel.getSiteId());
		String sql = "select * from td_cms_site_search where (search_type='0' and chnl_or_domain like '%" + chnlId + "%')" +
		" or (search_type='2' and site_id='" + siteId + "')";
		return this.getIndexsBySQL(sql); 
	}	
	
	/**
	 * 根据站点id获取整站索引和相关站群索引,weida
	 * @param siteId
	 * @return
	 * @throws Exception
	 */
	public ArrayList getIndexsBySiteId(String siteId) throws Exception{
		String sql = "select * from td_cms_site_search where (search_type='2' and site_id='" + siteId + "')" +
				" or (search_type='3' and (site_ids='" + siteId + "' or site_ids like '%" + siteId + "," + "%' or site_ids like '%" + "," + siteId + "%'))";
		return this.getIndexsBySQL(sql); 
	}
	
	/**
	 * 由索引名称得到索引id以获取指定索引,weida
	 * @param indexName
	 * @return
	 * @throws Exception
	 */
	public static CMSSearchIndex getIndexIdByIndexName(String indexName) throws Exception{
		String sql = "select * from td_cms_site_search where name = '" + indexName + "'";
		return getIndexBySQL(sql); 
	}
	
	
	private static CMSSearchIndex getIndexBySQL(String sql) throws Exception{
		DBUtil db = new DBUtil();
		DBUtil db1 = new DBUtil();
		DBUtil db2 = new DBUtil();
		DBUtil db3 = new DBUtil();
		DBUtil db4 = new DBUtil();
		try{
			db.executeSelect(sql);
			if(db.size()>0){
				CMSSearchIndex searchIndex = new CMSSearchIndex();
				searchIndex.setId(db.getInt(0,"id"));				
				String siteId = db.getInt(0,"site_id")+"";
				searchIndex.setSiteId(siteId);
				String sql1 = "select second_name from td_cms_site where site_id = " + siteId;
				db1.executeSelect(sql1);
				if(db1.size()>0){
					searchIndex.setSiteName(db1.getString(0,"second_name"));		//设置站点名
				}				
				searchIndex.setLever(db.getInt(0,"lever"));
				searchIndex.setDay(db.getInt(0,"day"));
				searchIndex.setTime(db.getInt(0,"time"));				
				String indexName = db.getString(0,"name");
				searchIndex.setIndexName(indexName);				
				//索引库相对路径在在配置文件config-manager.xml中配
				String indexPath = getIndexRootPath();
				searchIndex.setIndexPath(indexPath);					
				int searchType = db.getInt(0,"search_type");
				searchIndex.setSearchType(searchType);
				if(searchType==CMSSearchManager.SEARCHTYPE_SITECHANNEL){		//站内频道搜索					
					String chnlId = db.getString(0,"chnl_or_domain");
					searchIndex.setChnlId(chnlId);
	            	String[] chnlIds = chnlId.split(",");
	            	String chnlNames = "";	
	            	if(chnlIds.length>=1)
	            	{
	            		if( !(chnlIds[0].equals("")) && !(chnlIds[0]==null) )
	            		{
	            			sql1 = "select display_name from td_cms_channel where channel_id = " + chnlIds[0];
			            	db1.executeSelect(sql1);
			            	if(db1.size()>0)
			            	{
			            		chnlNames = db1.getString(0,"display_name");
			            	}
	            		}		        	            		
	            		for(int r=1; r<chnlIds.length; r++)
		            	{
			            	sql1 = "select display_name from td_cms_channel where channel_id = " + chnlIds[r];
			            	db2.executeSelect(sql1);
			            	if(db2.size()>0)
			            	{
			            		chnlNames = chnlNames + "," + db2.getString(0,"display_name");
			            	}
		            	}
	            	}
	            	searchIndex.setChnlName(chnlNames);
	            	//频道可以有多个,id改成了字符串,weida	            	
					searchIndex.setAbsoluteIndexPath(
							getAbsoluteIndexFilePath(indexPath,"in_" + indexName,siteId+""));
				}else if(searchType==CMSSearchManager.SEARCHTYPE_EXTERNAL){					//站外搜索
					searchIndex.setDomainUrl(db.getString(0,"chnl_or_domain"));
					searchIndex.setStartUrl(db.getString(0,"start_url"));
					searchIndex.setAbsoluteIndexPath(
							getAbsoluteIndexFilePath(indexPath,"out_" + indexName,siteId+""));
				}else if(searchType==CMSSearchManager.SEARCHTYPE_SITE)	{					//整站检索搜索
					searchIndex.setAbsoluteIndexPath(
							getAbsoluteIndexFilePath(indexPath,"site_" + indexName,siteId+""));
				}else if(searchType==CMSSearchManager.SEARCHTYPE_SITES)	{					//站群检索搜索					
					String sitesIds = db.getString(0,"site_ids");		
					searchIndex.setSite_Ids(sitesIds);
					String[] siteIdss = sitesIds.split(",");
					String siteNames = "";					
					
					
					String sql3 = "select second_name from td_cms_site where site_id =";
					if(siteIdss.length>0)
					{
						if(!(siteIdss[0].equals("")) && !(siteIdss[0].equals(null)))
						{
							db3.executeSelect(sql3 + siteIdss[0]);
							siteNames = db3.getString(0,"second_name");
						}
						for(int r=1;r<siteIdss.length;r++)
						{
							db3.executeSelect(sql3 + siteIdss[r]);
							siteNames = siteNames + "," + db3.getString(0,"second_name");
						}
					}
					searchIndex.setSiteNames(siteNames);					
					searchIndex.setAbsoluteIndexPath(
						getAbsoluteIndexFilePath(indexPath,"sites_" + indexName,siteId+""));
			}else if(searchType==CMSSearchManager.SEARCHTYPE_DBTABLE)	{					//库表检索搜索
				String sql4 = "select * from td_cms_dbtsearch_detail t where t.id =" + db.getInt(0,"id");
				db4.executeSelect(sql4);				
				searchIndex.setWheres(db4.getString(0,"WHERES"));
				searchIndex.setAccess_url(db4.getString(0,"ACCESS_URL"));				
				searchIndex.setDb_name(db4.getString(0,"DB_NAME"));
				searchIndex.setTable_name(db4.getString(0,"TABLE_NAME"));
				searchIndex.setPrimarys(db4.getString(0,"PRIMARYS"));
				searchIndex.setTitle_field(db4.getString(0,"TITLE_FIELD"));
				searchIndex.setKeyword_field(db4.getString(0,"KEYWORD_FIELD"));
				searchIndex.setPublishtime_field(db4.getString(0,"PUBLISHTIME_FIELD"));
				searchIndex.setDescription_field(db4.getString(0,"DESCRIPTION_FIELD"));
				searchIndex.setContent_fields(db4.getString(0,"CONTENT_FIELDS"));
				searchIndex.setContent_types(db4.getString(0,"CONTENT_TYPES"));
				searchIndex.setContent_path(db4.getString(0,"CONTENT_PATH"));				
				searchIndex.setAbsoluteIndexPath(
						getAbsoluteIndexFilePath(indexPath,"db_" + indexName,siteId+""));
			}
				return searchIndex;
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 由sql得到所有相关索引
	 */
	private ArrayList getIndexsBySQL(String sql) throws Exception{
		DBUtil db = new DBUtil();
		DBUtil db1 = new DBUtil();
		DBUtil db2 = new DBUtil();
		ArrayList indexs = new ArrayList();
		try{
			db.executeSelect(sql);
			if(db.size()>0){
			for(int h=0;h<db.size();h++){
				CMSSearchIndex searchIndex = new CMSSearchIndex();
				searchIndex.setId(db.getInt(h,"id"));				
				String siteId = db.getInt(h,"site_id")+"";
				searchIndex.setSiteId(siteId);
				String sql1 = "select name from td_cms_site where site_id = " + siteId;
				db1.executeSelect(sql1);
				if(db1.size()>0){
					searchIndex.setSiteName(db1.getString(0,"name"));		//设置站点名
				}				
				searchIndex.setLever(db.getInt(h,"lever"));
				searchIndex.setDay(db.getInt(h,"day"));
				searchIndex.setTime(db.getInt(h,"time"));				
				String indexName = db.getString(h,"name");
				searchIndex.setIndexName(indexName);				
				//索引库相对路径在在配置文件config-manager.xml中配
				String indexPath = getIndexRootPath();
				searchIndex.setIndexPath(indexPath);					
				int searchType = db.getInt(h,"search_type");
				searchIndex.setSearchType(searchType);
				if(searchType==CMSSearchManager.SEARCHTYPE_SITECHANNEL){		//站内频道搜索
					String chnlId = db.getString(h,"chnl_or_domain");
					searchIndex.setChnlId(chnlId);
	            	String[] chnlIds = chnlId.split(",");
	            	String chnlNames = "";	
	            	if(chnlIds.length>=1)
	            	{
	            		if( !(chnlIds[0].equals("")) && !(chnlIds[0]==null) )
	            		{
	            			sql1 = "select display_name from td_cms_channel where channel_id = " + chnlIds[0];
			            	db1.executeSelect(sql1);
			            	if(db1.size()>0)
			            	{
			            		chnlNames = db1.getString(0,"display_name");
			            	}
	            		}		        	            		
	            		for(int r=1; r<chnlIds.length; r++)
		            	{
			            	sql1 = "select display_name from td_cms_channel where channel_id = " + chnlIds[r];
			            	db2.executeSelect(sql1);
			            	if(db2.size()>0)
			            	{
			            		chnlNames = chnlNames + "," + db2.getString(0,"display_name");
			            	}
		            	}
	            	}
	            	searchIndex.setChnlName(chnlNames);
	            	//频道可以有多个,id改成了字符串,weida	            	
					searchIndex.setAbsoluteIndexPath(
							this.getAbsoluteIndexFilePath(indexPath,"in_" + indexName,siteId+""));
				}else if(searchType==CMSSearchManager.SEARCHTYPE_EXTERNAL){					//站外搜索
					searchIndex.setDomainUrl(db.getString(h,"chnl_or_domain"));
					searchIndex.setStartUrl(db.getString(h,"start_url"));
					searchIndex.setAbsoluteIndexPath(
							this.getAbsoluteIndexFilePath(indexPath,"out_" + indexName,siteId+""));
				}else if(searchType==CMSSearchManager.SEARCHTYPE_SITE)	{					//整站检索搜索
					searchIndex.setAbsoluteIndexPath(
							this.getAbsoluteIndexFilePath(indexPath,"site_" + indexName,siteId+""));
				}else if(searchType==CMSSearchManager.SEARCHTYPE_SITES)	{					//站群检索搜索					
					String sitesIds = db.getString(h,"site_ids");		
					searchIndex.setSite_Ids(sitesIds);
					String[] siteIdss = sitesIds.split(",");
					String siteNames = "";
					DBUtil db3 = new DBUtil();
					String sql3 = "select name from td_cms_site where site_id =";
					if(siteIdss.length>0)
					{
						if(!(siteIdss[0].equals("")) && !(siteIdss[0].equals(null)))
						{
							db3.executeSelect(sql3 + siteIdss[0]);
							siteNames = db3.getString(0,"name");
						}
						for(int r=1;r<siteIdss.length;r++)
						{
							db3.executeSelect(sql3 + siteIdss[r]);
							siteNames = siteNames + "," + db3.getString(0,"name");
						}
					}
					searchIndex.setSiteNames(siteNames);					
					searchIndex.setAbsoluteIndexPath(
						this.getAbsoluteIndexFilePath(indexPath,"sites_" + indexName,siteId+""));
			}else if(searchType==CMSSearchManager.SEARCHTYPE_DBTABLE)	{					//库表检索搜索
				searchIndex.setAbsoluteIndexPath(
						this.getAbsoluteIndexFilePath(indexPath,"db_" + indexName,siteId+""));
			}
				indexs.add(searchIndex);
			}}
			return indexs;
		}catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 开始建立索引
	 * @param searchIndex
	 * @throws Exception
	 */
	public static void startCrawler(CMSSearchIndex searchIndex,String contextpath) throws Exception{
		if((searchIndex.getSiteId() == null)||(searchIndex.getSiteId().equals(null))){
			throw new Exception("站内索引，站点id不能为空！");
		}
		CMSCrawler crawler = new CMSCrawler(searchIndex,contextpath,false);
		crawler.crawl();
	}
	
	
	/**
	 * 删除指定索引
	 * @param indexId
	 * @throws Exception
	 */
	public void deleteIndex(String indexId) throws Exception{
		DBUtil db = new DBUtil();
		File absoluteIndexFile;
		String absoluteIndexFilePath = "";
		String sql = "select * from td_cms_site_search where id = " + indexId;
		try{
			db.executeSelect(sql);
			String indexFileName = "";
			int searchType = CMSSearchManager.SEARCHTYPE_SITECHANNEL;
			if(db.size()>0){
				indexFileName = db.getString(0,"name");
				searchType = db.getInt(0,"search_type");
			}
			if(searchType == CMSSearchManager.SEARCHTYPE_SITECHANNEL)
				indexFileName = "in_" + indexFileName;
			else if(searchType == CMSSearchManager.SEARCHTYPE_EXTERNAL)
				indexFileName = "out_" + indexFileName;
			else if(searchType == CMSSearchManager.SEARCHTYPE_SITE)
				indexFileName = "site_" + indexFileName;
			else if(searchType == CMSSearchManager.SEARCHTYPE_SITES)
				indexFileName = "sites_" + indexFileName;
			else if(searchType == CMSSearchManager.SEARCHTYPE_DBTABLE)
				indexFileName = "db_" + indexFileName;
			
			absoluteIndexFilePath = this.getAbsoluteIndexFilePath(this.getIndexRootPath(),indexFileName,db.getInt(0,"site_id")+"");
			absoluteIndexFile = new File(absoluteIndexFilePath);
			if(absoluteIndexFile.exists()){			//先删除物理文件
				if(!isIndexLocked(absoluteIndexFilePath))
					this.deleteFilesAndDirector(absoluteIndexFile);
				else
					throw new Exception("索引文件已经上锁，暂时无法删除：" + absoluteIndexFilePath);
			}	
			
			//删除库表索引的详细信息
			if(searchType == CMSSearchManager.SEARCHTYPE_DBTABLE){}{
				String dbSql = "delete from TD_CMS_DBTSEARCH_DETAIL where id = " + indexId;
				DBUtil dbDb = new DBUtil();
				dbDb.executeDelete(dbSql);
			}
			//将索引从数据库中删除
			sql = "delete from td_cms_site_search where id = " + indexId;
			db.executeDelete(sql);
			
		}catch(SQLException e){
			e.printStackTrace();
			throw e;
		}
	}
	/**
	 * 删除物理文件
	 * @param file
	 */
	public void deleteFilesAndDirector(File file){
		File f=file;
		if(f.isDirectory() ){
			File files[]=f.listFiles() ;
			for (int i = 0; i < files.length; i++) {
				deleteFilesAndDirector(files[i]);
			}
			f.delete();
		}else{
			f.delete();
		}
	}
	/**
	 * 批量强制删除索引
	 * @param indexIds
	 * @throws Exception
	 */
	public void deleteIndex(String[] indexIds) throws Exception{
		for(int i=0;i<indexIds.length;i++){
			this.deleteIndex(indexIds[i]);
		}
	}
	
	/**
	 * 批量强制删除索引文件
	 * @param indexIds
	 * @throws Exception
	 */
	public void deleteIndexFiles(String[] indexIds) throws Exception{
		log.debug("删除索引文件！");
		for(int i=0;i<indexIds.length;i++){
			CMSSearchIndex index = this.getIndex(indexIds[i]);
			if(index!= null){
				String indexFilePath = this.getAbsoluteIndexFilePath(index);
				this.releaseIndexLocked(index);
				
					this.deleteFilesAndDirector(new File(indexFilePath));
				
				//}else{
				//	throw new Exception("索引文件已经上锁，暂时无法删除：" + indexFilePath);
				//}
			}
		}
	}
	
	/**
	 * 删除频道索引，为外部提供的接口
	 */
	public void deleteChnlIndex(String chnlId)
	{
		try {
			CMSSearchIndex index = this.getIndexByChnlId(chnlId);
			if(index != null)
			{
				this.deleteIndex(index.getId() + "");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除涉及当前站点的所有索引，为外部提供的接口,weida
	 * ￥￥￥注意：
	 * 本来删除站点时也只关联整站索引，但是删站点索引时必须删除索引所在根目录，否则会不能新建同名站点，
	 * 所以暂时删除站点索引时是删除所有相关索引，并删除根目录
	 */
	public void deleteSiteIndexs(String siteId)
	{
		try {			
			//得到并删除站点下建立的所有索引
			ArrayList index = this.getSiteIndexsBySiteId(siteId);
			if(index != null)
			{
				Object[] indexs = (Object[])index.toArray();
				for(int h=0;h<index.size();h++)
				{
					this.deleteIndex(((CMSSearchIndex)indexs[h]).getId() + "");
				}				
			} 
			
			//删除当前站点的索引所在根目录
			String siteIndexRootPath = this.getAbsoluteIndexRootPath(this.getIndexRootPath());
			Site site = CMSUtil.getSiteCacheManager().getSite(siteId);
			if(site != null)
			{
				siteIndexRootPath = CMSUtil.getPath(siteIndexRootPath,site.getSecondName());
			}
			this.deleteFilesAndDirector(new File(siteIndexRootPath));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 得到此站点的相关索引,weida
	 */
	public ArrayList getSiteIndexsBySiteId(String siteId) throws Exception{
		ArrayList indexs1 = new ArrayList();
//		ArrayList indexs2 = new ArrayList();
		if(!siteId.equals(null))
		{
			//以下是在该站点下建的索引,包括在该站点下建的频道索引、整站索引、站外索引、站群索引和数据库表索引
			String siteSql = "select * from td_cms_site_search where site_id=" + siteId;
//			//以下是频道索引名索引和整站索引
//			String siteSql = "select * from td_cms_site_search where site_id=" + siteId + " and (search_type =" + 0 + " or search_type =" + 2 + ")";
			indexs1 = this.getIndexsBySQL(siteSql);
//			//以下是站群索引
//			String sitesSql = "select * from td_cms_site_search where site_ids like '%" + siteId + "%' and search_type =" + 3;
//			indexs2 = this.getIndexsBySQL(sitesSql);
		}
//		if(!(indexs1 == null))
//		{
//			if(!(indexs2 == null))
//			{
//				for(int i=0;i<indexs2.size();i++)
//				{
//					indexs1.add(indexs2.get(i));
//				}				
//			}
//			return indexs1;
//		}
//		else
//		{
//			if(!(indexs2 == null))
//			{
//				return indexs2;
//			}
//		}
		return indexs1;
	}
	
	/**
	 * 删除涉及当前频道的所有索引，为外部提供的接口
	 */
	public void deleteChnlIndexs(String chnlId)
	{
		try {
//			//得到并删除与此频道直接相关的索引
//			ArrayList index1 = this.getIndexsByChnlId(chnlId);
//			if(index1 != null)
//			{
//				Object[] indexs1 = (Object[])index1.toArray();
//				for(int h=0;h<index1.size();h++)
//				{
//					this.deleteIndex(((CMSSearchIndex)indexs1[h]).getId() + "");
//				}				
//			}
//			//得到并删除此频道所有父频道的相关索引
//			ArrayList index2 = this.getParentIndexsByChnlId(chnlId);
//			if(index2 != null)
//			{
//				Object[] indexs2 = (Object[])index2.toArray();
//				for(int h=0;h<index2.size();h++)
//				{
//					this.deleteIndex(((CMSSearchIndex)indexs2[h]).getId() + "");
//				}				
//			}
			//得到并删除此频道所在站点的整站索引和相关站群索引
			ArrayList index3 = this.getSiteIndexByChnlId(chnlId);
			if(index3 != null)
			{
				Object[] indexs3 = (Object[])index3.toArray();
				for(int h=0;h<index3.size();h++)
				{
					this.deleteIndex(((CMSSearchIndex)indexs3[h]).getId() + "");
				}				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 得到此频道所有父频道的相关索引,weida
	 */
	public ArrayList getParentIndexsByChnlId(String chnlId) throws Exception{
		ArrayList indexs1 = new ArrayList();
		ArrayList indexs2 = new ArrayList();
		if(!chnlId.equals(null))
		{
			ChannelManagerImpl cmi = new ChannelManagerImpl();
			Channel parentChannel = null;
			parentChannel = cmi.getParentChannelInfo(chnlId);
			if(!(parentChannel == null))
			{
				String parentId = parentChannel.getParentChannelId() + "";
				indexs1 = this.getIndexsByChnlId(parentId);
				indexs2 = getParentIndexsByChnlId(parentId);//递归
			}
		}
		if(!(indexs1 == null))
		{
			if(!(indexs2 == null))
			{
				for(int i=0;i<indexs2.size();i++)
				{
					indexs1.add(indexs2.get(i));
				}				
			}
			return indexs1;
		}
		else
		{
			if(!(indexs2 == null))
			{
				return indexs2;
			}
		}
		return null;
	}	
	
	/**
	 * 得到此频道所在站点的相关站群索引,weida
	 */
	public ArrayList getSiteIndexsByChnlId(String chnlId) throws Exception{
		ArrayList indexs1 = new ArrayList();
		ArrayList indexs2 = new ArrayList();
		if(!chnlId.equals(null))
		{
			ChannelManagerImpl cmi = new ChannelManagerImpl();
			Channel thisChn = cmi.getChannelInfo(chnlId);
			String siteId = thisChn.getSiteId() + "";
			//以下是整站索引
			String siteSql = "select * from td_cms_site_search where site_id=" + siteId + " and search_type =" + 2;
			indexs1 = this.getIndexsBySQL(siteSql);
			//以下是站群索引
			String sitesSql = "select * from td_cms_site_search where site_ids like '%" + siteId + "%' and search_type =" + 3;
			indexs2 = this.getIndexsBySQL(sitesSql);
		}
		if(!(indexs1 == null))
		{
			if(!(indexs2 == null))
			{
				for(int i=0;i<indexs2.size();i++)
				{
					indexs1.add(indexs2.get(i));
				}				
			}
			return indexs1;
		}
		else
		{
			if(!(indexs2 == null))
			{
				return indexs2;
			}
		}
		return null;
	}
	
	/*
	 * 由频道id得到此频道所在站点的整站索引
	 * */
	public ArrayList getSiteIndexByChnlId(String chnlId) throws Exception{
		ArrayList indexs1 = new ArrayList();
		if(!chnlId.equals(null))
		{
			ChannelManagerImpl cmi = new ChannelManagerImpl();
			Channel thisChn = cmi.getChannelInfo(chnlId);
			String siteId = thisChn.getSiteId() + "";
			//以下是整站索引
			String siteSql = "select * from td_cms_site_search where site_id=" + siteId + " and search_type =" + 2;
			indexs1 = this.getIndexsBySQL(siteSql);
		}
		return indexs1;
	}
	
	/**
	 * 获取“索引频率”、“索引日期”、“索引时间”的描述（用于显示给用户）
	 * @param lever 	索引频率		
	 * @param day		索引日期
	 * @param time		索引日期
	 * @return 返回：[0]索引频率；[1]索引日期；[2]索引时间
	 */
	public String[] getIndexLDTDes(int lever,int day,int time){
		String[] LDTDes = new String[3];
		String leverDes = "";   
		String dayDes = "";
		String timeDes = "";
		if(lever == 0){
				leverDes = "每周一次";
				switch(day){ 
					case 1:dayDes+="星期日";break;
					case 2:dayDes+="星期一";break;
					case 3:dayDes+="星期二";break;
					case 4:dayDes+="星期三";break;
					case 5:dayDes+="星期四";break;
					case 6:dayDes+="星期五";break;
					case 7:dayDes+="星期六";break;
				}
		}else if(lever == 1){
				leverDes = "每月一次";
				dayDes = "每月" + day + "号";
		}else{
				leverDes = "每天一次";
				dayDes = "当天";		
		}
		timeDes = time + ":00";
		LDTDes[0] = leverDes;
		LDTDes[1] = dayDes;
		LDTDes[2] = timeDes;
		return LDTDes;
	}
	
	/**
	 * 获取频率、日期、时间的描述列表（在editIndex.jsp页面中用到）
	 * @param flag 0-周的日期列表；1-月的日期列表；2-每天；3-时间列表；4-频率列表
	 * @return
	 * @throws FlowManagerException
	 */
	public List getLDTList(int flag) throws FlowManagerException
	{
		List list = new ArrayList();
	    int id = 0;
	    if(flag == 0){
		    for(int i=0;i<7;i++){
		    	List oneRow = new ArrayList();
		    	id = id + 1;
			    oneRow.add(id + "");
			    switch(id)
			    {
			    	case 1:	oneRow.add("星期日");break; 
			    	case 2:	oneRow.add("星期一");break;
			    	case 3:	oneRow.add("星期二");break;
			    	case 4:	oneRow.add("星期三");break;
			    	case 5:	oneRow.add("星期四");break;
			    	case 6:	oneRow.add("星期五");break;
			    	case 7:	oneRow.add("星期六");break;
			    }
			    list.add(oneRow);
		    }
	    }else if(flag == 1){ 
	    	for(int i=0;i<31;i++){
	    		List oneRow = new ArrayList();
	    		id = id + 1;
			    oneRow.add(id + "");
			    oneRow.add("每月" + id + "号");
			    list.add(oneRow);
		    }
	    }else if(flag == 2){
	    	List oneRow = new ArrayList();
	    	oneRow.add("1");
		    oneRow.add("每天");
		    list.add(oneRow);
	    }else if(flag == 3){ 
	    	for(int i=0;i<24;i++){
	    		List oneRow = new ArrayList();
			    oneRow.add(i + "");
			    oneRow.add(i + ":00");
			    list.add(oneRow);
		    }
	    }else if(flag == 4){ 
	    	List oneRow0 = new ArrayList();
			oneRow0.add("0");
			oneRow0.add("每周一次");
			list.add(oneRow0);
			List oneRow1 = new ArrayList();
			oneRow1.add("1");
			oneRow1.add("每月一次");
			list.add(oneRow1);
			List oneRow2 = new ArrayList();
			oneRow2.add("2");
			oneRow2.add("每天一次");
			list.add(oneRow2);
	    }
    	return list;
	}
	/**
	 * 增加一条索引记录
	 * @param searchIndex
	 */
	public boolean addOneIndex(CMSSearchIndex searchIndex) throws Exception{
		
		//判断是否有索引名冲突,weida
		DBUtil db0 = new DBUtil();
		String sql0 = "select * from td_cms_site_search where name ='" + searchIndex.getIndexName() + "'";
		try {
			db0.executeSelect(sql0);
			if(db0.size()>0)
			{
				return false;	//存在索引重名,不执行以下添加代码,返回false,weida
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		
		DBUtil db = new DBUtil();
		int searchType = searchIndex.getSearchType();
		String sql = "";
		int thisSiteId = Integer.parseInt(searchIndex.getSiteId());
		
		long searchId = db.getNextPrimaryKey("td_cms_site_search") ;
		
		if(searchType == CMSSearchManager.SEARCHTYPE_SITECHANNEL){		//站内频道索引
			
			/*
			 *重新得到siteId，由siteName得到
			 *da.wei,200710221121 
			 * */
			String thisSiteName = searchIndex.getSiteName();
			DBUtil dbSiteId = new DBUtil();
			String getSiteId = "select site_id from td_cms_site where second_name ='" + thisSiteName + "'";
			dbSiteId.executeSelect(getSiteId);
			thisSiteId = Integer.parseInt(dbSiteId.getString(0,"site_id"));
			
			sql = "insert into td_cms_site_search" +
					"(id,site_id,chnl_or_domain,lever,day,time,search_type,name) " +
					"values("+ searchId +"," + thisSiteId + ",'" + 
					searchIndex.getChnlId() + "'," + searchIndex.getLever() + "," + 
					searchIndex.getDay() + "," + searchIndex.getTime() + "," + 
					searchType + ",'" + searchIndex.getIndexName()  + "')";
		}else if(searchType == CMSSearchManager.SEARCHTYPE_EXTERNAL){						//站外索引
			//转换所有起始url和域url为http://开头的字符串
			String startUrl = this.getStartUrl(searchIndex.getStartUrl());
			String domainUrl = this.getDomainUrl(searchIndex.getDomainUrl());
			
			sql = "insert into td_cms_site_search" +
					"(id,site_id,chnl_or_domain,lever,day,time,search_type,start_url,name) " +
					"values("+ searchId + "," + thisSiteId + ",'" + 
					domainUrl + "'," + searchIndex.getLever() + "," + 
					searchIndex.getDay() + "," + searchIndex.getTime() + "," + searchType + ",'" + 
					startUrl + "','" + searchIndex.getIndexName()  + "')";
		}else if(searchType == CMSSearchManager.SEARCHTYPE_SITE){				//整站索引
			
			/*
			 *重新得到siteId，由siteName得到
			 *da.wei,200710221121 
			 * */
			String thisSiteName = searchIndex.getSiteName();
			DBUtil dbSiteId = new DBUtil();
			String getSiteId = "select site_id from td_cms_site where second_name ='" + thisSiteName + "'";
			dbSiteId.executeSelect(getSiteId);
			thisSiteId = Integer.parseInt(dbSiteId.getString(0,"site_id"));
			
			sql = "insert into td_cms_site_search" +
						"(id,site_id,lever,day,time,search_type,name) " +
						"values("+ searchId+ "," + thisSiteId + "," + 
						searchIndex.getLever() + "," + 
						searchIndex.getDay() + "," + searchIndex.getTime() + "," + 
						searchType + ",'" + searchIndex.getIndexName()  + "')";
		}
		else if(searchType == CMSSearchManager.SEARCHTYPE_SITES){				//站群索引
			
			//由站点名称(siteNames)得到站点id(site_ids)
			String siteName2 = searchIndex.getSiteNames();
			String[] siteName2s = siteName2.split(",");
			DBUtil db2 = new DBUtil();
			String sql2 = "select site_id from td_cms_site where second_name ='";
			String siteIds = "";
			if(siteName2s.length>0)
			{
				if(!(siteName2s[0].equals("")) && !(siteName2s[0].equals(null)))
				{
					db2.executeSelect(sql2 + siteName2s[0] + "'");
					siteIds = db2.getString(0,"site_id");
				}
				for(int r=1;r<siteName2s.length;r++)
				{
					db2.executeSelect(sql2 + siteName2s[r] + "'");
					siteIds = siteIds + "," + db2.getString(0,"site_id");
				}
			}			
			
			sql = "insert into td_cms_site_search" +
						"(id,site_id,lever,day,time,search_type,name,site_ids) " +
						"values("+ searchId +"," + thisSiteId + "," + 
						searchIndex.getLever() + "," + 
						searchIndex.getDay() + "," + searchIndex.getTime() + "," + 
						searchType + ",'" + searchIndex.getIndexName()  + "','" + siteIds + "')";
		}else if(searchType == CMSSearchManager.SEARCHTYPE_DBTABLE){				//数据库索引
		
			sql = "insert into td_cms_site_search" +
						"(id,site_id,lever,day,time,search_type,name) " +
						"values("+ searchId +"," + thisSiteId + "," + 
						searchIndex.getLever() + "," + 
						searchIndex.getDay() + "," + searchIndex.getTime() + "," + 
						searchType + ",'" + searchIndex.getIndexName()  + "')";
		}
		Object dbtIdObject = null;
		try {
			dbtIdObject = db.executeInsert(sql);
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}		
		
		if(searchType == CMSSearchManager.SEARCHTYPE_DBTABLE){	//数据库表索引
			//String dbtId = dbtIdObject.toString();
			String dbtId = String.valueOf(searchId) ;
			
			PreparedDBUtil dbtDb = new PreparedDBUtil();
			StringBuffer dbtSql = new StringBuffer();
			try{
				dbtSql.append("insert into TD_CMS_DBTSEARCH_DETAIL ")
					.append("(ID,DB_NAME,TABLE_NAME,CONTENT_FIELDS,TITLE_FIELD,")
					.append("DESCRIPTION_FIELD,KEYWORD_FIELD,PUBLISHTIME_FIELD,ACCESS_URL,WHERES,PRIMARYS,content_types,content_path) ")
					.append("values(?,?,?,?,?,?,?,?,?,?,?,?,?)");
				dbtDb.preparedInsert(dbtSql.toString());
				int thisDbtId = Integer.parseInt(dbtId);
				dbtDb.setInt(1,thisDbtId);
				dbtDb.setString(2,searchIndex.getDb_name());
				dbtDb.setString(3,searchIndex.getTable_name());
				dbtDb.setString(4,searchIndex.getContent_fields());
				dbtDb.setString(5,searchIndex.getTitle_field());
				dbtDb.setString(6,searchIndex.getDescription_field());
				dbtDb.setString(7,searchIndex.getKeyword_field());
				dbtDb.setString(8,searchIndex.getPublishtime_field());
				dbtDb.setString(9,searchIndex.getAccess_url());
				dbtDb.setString(10,searchIndex.getWheres());
				dbtDb.setString(11,searchIndex.getPrimarys());
				dbtDb.setString(12,searchIndex.getContent_types());
				dbtDb.setString(13,searchIndex.getContent_path());
				dbtDb.executePrepared();
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				dbtDb.resetPrepare();
			}			
		}
		
		return true;	//顺利执行添加动作,返回ture,weida
	}
	/**
	 * 更新索引
	 * @param searchIndex
	 * @throws Exception
	 */
	public boolean updateOneIndex(CMSSearchIndex searchIndex) throws Exception{
		
		//判断是否有索引名冲突,weida
		DBUtil db0 = new DBUtil();
		String sql0 = "select * from td_cms_site_search where name ='" + searchIndex.getIndexName() + "' and id !='" + searchIndex.getId() + "'";
		try {
			db0.executeSelect(sql0);
			if(db0.size()>0)
			{
				return false;	//存在索引重名,不执行以下添加代码,返回false,weida
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		
		PreparedDBUtil db = new PreparedDBUtil();
		int indexId = searchIndex.getId();
		int searchType = searchIndex.getSearchType();
		String sql = "";
		try {
			if(searchType == CMSSearchManager.SEARCHTYPE_SITECHANNEL){		//站内索引
				/*
				 *重新得到siteId，由siteName得到
				 *da.wei,200710221121 
				 * */
				String thisSiteName = searchIndex.getSiteName();  //获取站点的英文名称
				DBUtil dbSiteId = new DBUtil();
				String getSiteId = "select site_id from td_cms_site where second_name ='" + thisSiteName + "'";
				dbSiteId.executeSelect(getSiteId);
				int thisSiteId = Integer.parseInt(dbSiteId.getString(0,"site_id"));
				
				sql = "update td_cms_site_search set " +
						"site_id=?,chnl_or_domain=?,lever=?,day=?,time=?,search_type=0,name=? where id =?";
				db.preparedUpdate(sql);
//				int thisSiteId = Integer.parseInt(searchIndex.getSiteId());
				db.setInt(1,thisSiteId);
				db.setString(2,searchIndex.getChnlId()+"");
				db.setInt(3,searchIndex.getLever());
				db.setInt(4,searchIndex.getDay());
				db.setInt(5,searchIndex.getTime());
				db.setString(6,searchIndex.getIndexName());
				db.setInt(7,indexId);
			}else if(searchType == CMSSearchManager.SEARCHTYPE_EXTERNAL){						//站外索引
				//转换所有起始url和域url为http://开头的字符串
				String startUrl = this.getStartUrl(searchIndex.getStartUrl());
				String domainUrl = this.getDomainUrl(searchIndex.getDomainUrl());
				
				sql = "update td_cms_site_search set " +
						"site_id=?,chnl_or_domain=?,lever=?,day=?,time=?,search_type=1,start_url=?,name=? where id =?";
				db.preparedUpdate(sql);
				int thisSiteId = Integer.parseInt(searchIndex.getSiteId());
				db.setInt(1,thisSiteId);
				db.setString(2,domainUrl);
				db.setInt(3,searchIndex.getLever());
				db.setInt(4,searchIndex.getDay());
				db.setInt(5,searchIndex.getTime());
				db.setString(6,startUrl);
				db.setString(7,searchIndex.getIndexName());
				db.setInt(8,indexId);
			}else if(searchType == CMSSearchManager.SEARCHTYPE_SITE){	//整站索引
				sql = "update td_cms_site_search set " +
							"site_id=?,lever=?,day=?,time=?,search_type=2,name=? where id =?";
				db.preparedUpdate(sql);
				
				/*
				 *重新得到siteId，由siteName得到
				 *da.wei,200710221121 
				 * */
				String thisSiteName = searchIndex.getSiteName();
				DBUtil dbSiteId = new DBUtil();
				String getSiteId = "select site_id from td_cms_site where second_name ='" + thisSiteName + "'";
				dbSiteId.executeSelect(getSiteId);
				int thisSiteId = Integer.parseInt(dbSiteId.getString(0,"site_id"));
				
//				thisSiteId = Integer.parseInt(searchIndex.getSiteId());
				db.setInt(1,thisSiteId);
				db.setInt(2,searchIndex.getLever());
				db.setInt(3,searchIndex.getDay());
				db.setInt(4,searchIndex.getTime());
				db.setString(5,searchIndex.getIndexName());
				db.setInt(6,indexId);
			}else if(searchType == CMSSearchManager.SEARCHTYPE_SITES){	//站群索引
				sql = "update td_cms_site_search set " +
							"site_id=?,lever=?,day=?,time=?,search_type=3,name=?,site_ids=? where id =?";
				db.preparedUpdate(sql);
				int thisSiteId = Integer.parseInt(searchIndex.getSiteId());
				db.setInt(1,thisSiteId);
				db.setInt(2,searchIndex.getLever());
				db.setInt(3,searchIndex.getDay());
				db.setInt(4,searchIndex.getTime());
				db.setString(5,searchIndex.getIndexName());

				String siteName2 = searchIndex.getSiteNames();
				String[] siteName2s = siteName2.split(",");
				DBUtil db2 = new DBUtil();
				String sql2 = "select site_id from td_cms_site where second_name ='";
				String siteIds = "";
				if(siteName2s.length>0)
				{
					if(!(siteName2s[0].equals("")) && !(siteName2s[0].equals(null)))
					{
						db2.executeSelect(sql2 + siteName2s[0] + "'");
						siteIds = db2.getString(0,"site_id");
					}
					for(int r=1;r<siteName2s.length;r++)
					{
						db2.executeSelect(sql2 + siteName2s[r] + "'");
						siteIds = siteIds + "," + db2.getString(0,"site_id");
					}
				}
				
				db.setString(6,siteIds);
				db.setInt(7,indexId);
			}else if(searchType == CMSSearchManager.SEARCHTYPE_DBTABLE){
				sql = "update td_cms_site_search set " +
				"site_id=?,lever=?,day=?,time=?,search_type=4,name=? where id =?";
				db.preparedUpdate(sql);
				int thisSiteId = Integer.parseInt(searchIndex.getSiteId());
				db.setInt(1,thisSiteId);
				db.setInt(2,searchIndex.getLever());
				db.setInt(3,searchIndex.getDay());
				db.setInt(4,searchIndex.getTime());
				db.setString(5,searchIndex.getIndexName());
				db.setInt(6,indexId);	
			}
			db.executePrepared();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}finally{
			db.resetPrepare();
		}
		
//		更新库表索引详细表
		if(searchType == CMSSearchManager.SEARCHTYPE_DBTABLE){
			PreparedDBUtil dbPDB = new PreparedDBUtil();
			try{
				String dbSql = "update td_cms_dbtsearch_detail set " +
				"db_name=?,table_name=?,content_fields=?,title_field=?,description_field=?,keyword_field=?," +
				"publishtime_field=?,access_url=?,wheres=?,primarys=?,content_types=?,content_path=? where id=?";
				dbPDB.preparedUpdate(dbSql);
				dbPDB.setString(1,searchIndex.getDb_name());
				dbPDB.setString(2,searchIndex.getTable_name());
				dbPDB.setString(3,searchIndex.getContent_fields());
				dbPDB.setString(4,searchIndex.getTitle_field());
				dbPDB.setString(5,searchIndex.getDescription_field());
				dbPDB.setString(6,searchIndex.getKeyword_field());
				dbPDB.setString(7,searchIndex.getPublishtime_field());
				dbPDB.setString(8,searchIndex.getAccess_url());
				dbPDB.setString(9,searchIndex.getWheres());
				dbPDB.setString(10,searchIndex.getPrimarys());
				dbPDB.setString(11,searchIndex.getContent_types());
				dbPDB.setString(12,searchIndex.getContent_path());
				dbPDB.setInt(13,indexId);
				dbPDB.executePrepared();
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				dbPDB.resetPrepare();
			}
		}		
		
		return true;	//顺利执行添加动作,返回ture,weida
	}
	/**
	 * 转换所有起始url为"http://"开头、"/"结尾的字符串
	 * 判断url是否是一个页面如果是页面则需要以"/"结尾的字符串
	 * @param inputStartUrl
	 * @return
	 */
	private String getStartUrl(String inputStartUrl){
		String startUrl = "";
		String startUrls[] = inputStartUrl.split(",");
		for(int i=0; i<startUrls.length;i++){
			String tempStartUrl = "";
			if(startUrls[i].startsWith("http://"))
				tempStartUrl = startUrls[i];
			else 
				tempStartUrl = "http://" + startUrls[i];
			
//			if(!startUrls[i].endsWith("/"))
//				tempStartUrl = tempStartUrl + "/";
//			bug437:增加或修改站外检索，如果域名地址是页面时保存后在地址的后面多加了一个'/',weida
				
			if(i==0)
				startUrl = startUrl + tempStartUrl;
			else
				startUrl = startUrl + "," + tempStartUrl;
		}
		return startUrl;
	}
	/**
	 * 转换所有域url为"http://"开头字符串
	 * @param inputDomainUrl
	 * @return
	 */
	private String getDomainUrl(String inputDomainUrl){
		String domainUrl = "";
		String domainUrls[] = inputDomainUrl.split(",");
		for(int i=0; i<domainUrls.length;i++){
			String tempDomainUrl = "";
			if(domainUrls[i].startsWith("http://"))
				tempDomainUrl = domainUrls[i];
			else
				tempDomainUrl = "http://" + domainUrls[i];

			if(domainUrls[i].endsWith("/"))
				tempDomainUrl = tempDomainUrl.substring(0,tempDomainUrl.length()-1);
			
			if(i==0)
				domainUrl = domainUrl + tempDomainUrl;
			else
				domainUrl = domainUrl + "," + tempDomainUrl;
		}
		return domainUrl;
	}
	/**
	 * 提供检索的接口
	 * @param multiSearcher
	 * @return
	 */
	public HitResult search(CMSMultiSearcher multiSearcher)
	{
		 return multiSearcher.search();
	}
	/**
	 * 将利用lucene查询工具查询返回的Hits集合转化为List
	 * @param hits
	 */
	public static List<CMSSearchHit> getSearchHitList(IndexSearcher searcher, ScoreDoc[] hits) throws Exception
	{
		List<CMSSearchHit> searchHitList = new ArrayList<CMSSearchHit>();
		for(int i=0;i<hits.length;i++){
			
			Document doc = searcher.doc(hits[i].doc);
			//过滤掉文件名前缀不为content的文件
			//如果站点的内容文件命名不是以content为前缀的，则不能如此判断
//			String temp = doc.get("url");
//			if(temp != null){
//				temp = temp.substring(temp.lastIndexOf("/")+1,temp.length());
//				temp = temp.substring(0,7);
//				if(!"content".equals(temp)){
//					continue;
//				}
//			}
			CMSSearchHit searchHit = new CMSSearchHit();
			searchHit.setCategories(doc.get("categories"));
			searchHit.setContent(doc.get("content"));
			searchHit.setContentType(doc.get("contentType"));
			searchHit.setDescription(doc.get("description"));
			String href = doc.get("href");
			
			searchHit.setHref((href != null) ? href : doc.get("url"));
			searchHit.setUri(doc.get("uri"));
			searchHit.setKeywords(doc.get("keyword"));
			IndexableField temp_f = doc.getField("published");
			
			StoredField pfield = ((StoredField)temp_f);
//			long pulished = -1;
//			String pulished = doc.get("published");
			if(null == pfield )
			{
				pfield = ((StoredField)doc.getField("lastModified"));
			}
//				pulished = doc.get("lastModified");
			if(null != pfield)
				searchHit.setPublished(new Date(pfield.numericValue().longValue()));
			
			searchHit.setScore(getFloNum(hits[i].score*100,2));		//相关度计算
			searchHit.setTitle(doc.get("title"));
			searchHit.setUrl(doc.get("url"));
			searchHitList.add(searchHit);
		}
		return searchHitList;
	}
	public static float getFloNum(float num, int n) {
	    int dd = 1;
	    float tempnum;
	    int tnum;
	    for (int i = 0; i < n; i++) {
	      dd *= 10;
	    }
	    tempnum = num * dd;
	    tnum =Math.round(tempnum);
	    return ((float)tnum/dd);
	 }
	/**
	 * 从查询的到的内容里截取供显示的字符串。
	 * @param query  查询关键字
	 * @param contents	查询到的内容
	 * @return
	 */
	public String getInterceptContent(String query,String contents)
    {
    	if( contents==null)
    		return "空";
    
    	String[] s=new String[10];
    	int j=0;
    	StringTokenizer st=new StringTokenizer(query);
    	while(st.hasMoreTokens()){
       	  	s[j]=st.nextToken();
       	  	s[j]=s[j].toLowerCase();
       	  	int DotPos=s[j].indexOf(':',0);
       	  	if (DotPos!=-1)
       	  	s[j]=s[j].substring(DotPos+1,query.length());
           
       	  	//去引号
       	  	int ColPos=s[j].indexOf("\"",0);
       	  	if (ColPos!=-1)
       	  		s[j]=s[j].substring(ColPos+1,s[j].length());
       	  	ColPos=s[j].indexOf("\"",0);
       	  	if (ColPos!=-1)
       	  		s[j]=s[j].substring(ColPos+1,s[j].length());
       	  	if ((!s[j].equals("and"))||(!s[j].equals("or")))
       	  		j++;
       }
       int i=0;
       int StartPos=0;
       while(i<j){
      
    	   StartPos=contents.indexOf(s[i],0);
    	   if (StartPos!=-1){
    		   break;
    	   }
    	   else
    		   i++;
       }
      
       if (StartPos==-1) StartPos=0;
       
       //da.wei20071016，让被搜索关键字前也显示一段文字
       if(StartPos-50>=0){
    	   StartPos = StartPos-50;
       }else{
    	   StartPos = 0;
       }
       
       int EndPos=contents.length()-1;
       if (EndPos==-1) 
    	   return "空";
       
       contents=contents.substring(StartPos,EndPos);
       char[] CharContent=contents.toCharArray();
       int str_length;
       str_length=contents.length();
       if (str_length>100)
    	   str_length=100;
      
       StringBuffer sb = new StringBuffer();
       try{
    	   String temp_c=new String(contents.getBytes(), "GB2312");
    	   char[] pb=temp_c.toCharArray();
      
    	   for(i=0;i<str_length;i++){
        	short b = (short)pb[i];
        	
        	 if (b!=(short)63)
        	   sb.append(CharContent[i]);	
    	   }
      
    	   contents=sb.toString()+"......";
       }
       catch (Exception e) {
    	   log.debug(SimpleStringUtil.exceptionToString(e));
       }
       return contents;
    }
	/**
	 * 判断指定站点的站点索引是否已经存在
	 * 对于任何一个站点，整站索引只有一个
	 * @param siteId
	 * @return 返回索引id
	 */
	public int isAllSiteIndexExist(String siteId){
		DBUtil db = new DBUtil();
		String sql = "select id from td_cms_site_search where site_id = " + siteId + " and search_type = 2";
		try {
			db.executeSelect(sql);
			if(db.size()>0) 
				return db.getInt(0,"id");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	/**
	 * 判断指定索引的索引文件库是否已经上锁（即正在建立索引）
	 * @param cmsIndex
	 */
	public static boolean isIndexLocked(CMSSearchIndex Index){
		try {
			String indexDirectory = getAbsoluteIndexFilePath(Index);
			return isIndexLocked(indexDirectory);
		} catch (Exception e) {
			return true;
		}
	}
	
	/**
	 * 判断指定索引的索引文件库是否已经上锁（即正在建立索引）
	 * @param cmsIndex
	 */
	public static void releaseIndexLocked(CMSSearchIndex Index){
		try {
			String indexDirectory = getAbsoluteIndexFilePath(Index);
			releaseIndexLocked(indexDirectory);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 判断指定索引的索引文件库是否已经上锁（即正在建立索引）
	 * @param cmsIndex
	 */
	public static boolean isIndexLocked(String indexDirectory){
		try {
//			String indexDirectory = getAbsoluteIndexFilePath(Index);
			File f = new File(indexDirectory);
			if(f.exists()){
				Directory dir = null;
				try
				{
					dir = FSDirectory.open(f);
					return IndexWriter.isLocked(dir);
				}
				finally
				{
					if(dir != null)
						dir.close();
				}
			}
			else return false;
		} catch (Exception e) {
			e.printStackTrace();
			return true;
		}
	}
	
	/**
	 * 判断指定索引的索引文件库是否已经上锁（即正在建立索引）
	 * @param cmsIndex
	 */
	public static void releaseIndexLocked(String indexDirectory){
		try {
//			String indexDirectory = getAbsoluteIndexFilePath(Index);
			File f = new File(indexDirectory);
			if(f.exists()){
				Directory dir = null;
				try
				{
					dir = FSDirectory.open(f);
					
					IndexWriter.unlock(dir);
				}
				finally
				{
					if(dir != null)
						dir.close();
				}
			}
			 
		} catch (Exception e) {
			 e.printStackTrace();
		}
	}
	
	/**
	 * 获取文档相关的索引列表，列表的长度最多为2，包括文档所在站点索引和文档所在频道索引
	 * 危达200709111450{文档相关的索引列表包括当前站点的整站索引,文档所在频道的索引名索引.所在索引名索引可能有多个}
	 * @param docId
	 * @param siteId
	 * @return
	 */
	public List getIndexListOfCmsdocument(com.frameworkset.platform.cms.documentmanager.Document doc,String siteId) throws Exception{
		
		int chnlId = doc.getChanel_id();
		String sql = "";
		sql = "select * from td_cms_site_search where (search_type=" + 2 + " and site_id=" + siteId + " ) " +
				"or (search_type=" + 0 + " and site_id=" + siteId + " and (chnl_or_domain='" + chnlId + "' or chnl_or_domain like '%" + chnlId + "," + "%' or chnl_or_domain like '%" + "," + chnlId + "%'))";
		return this.getIndexList(sql);
	}
	
	/**
	 * 获取文档所在站点的站点索引，20070924
	 * @param docId
	 * @param siteId
	 * @return
	 */
	private List getSiteIndex(String siteId) throws Exception{
		DocumentManager dm = new DocumentManagerImpl();
		String sql = "select * from td_cms_site_search where search_type=" + 2 + " and site_id=" + siteId;
		return this.getIndexList(sql);
	}
	public void deleteDocumetFromIndex(HttpServletRequest request,String docId,String siteId) throws Exception{
		com.frameworkset.platform.cms.documentmanager.Document document = new DocumentManagerImpl().getDoc(docId);
		deleteDocumetFromIndex( request, document, siteId);
	}
	
	
	
	public void deleteDocumetFromIndex(HttpServletRequest request,com.frameworkset.platform.cms.documentmanager.Document doc,String siteId) throws Exception{
		List attachmentList = new DocumentManagerImpl().getAllPublishedAttachmentOfDocument(request,doc,siteId);
		//关联整站索引和频道索引
		List indexList = this.getIndexListOfCmsdocument(doc,siteId);
		deleteDocumetFromIndex(request,doc,siteId,attachmentList, indexList  );
	}
	/**
	 * 删除文档在索引库中的记录,主要有文档所在站点索引和文档所在频道索引
	 * @param docId
	 * @param siteId
	 */
	public void deleteDocumetFromIndex(HttpServletRequest request,String docId,String siteId,List attachmentList,List indexList  ) throws Exception{

		
		try{
			if(docId == null || docId.length() <= 0 || siteId == null || siteId.length() <= 0){
				log.debug("参数:文档id和站点id都不能为空！");
				throw new Exception("参数:文档id和站点id都不能为空！"); 
			}
			com.frameworkset.platform.cms.documentmanager.Document doc = CMSUtil.getCMSDriverConfiguration().getCMSService().getDocumentManager().getDocWithNoContent(docId);
			deleteDocumetFromIndex(request,doc,siteId,attachmentList,indexList  ) ;
		}catch(Exception e){}
		
		
	}
	
	
	/**
	 * 删除文档在索引库中的记录,主要有文档所在站点索引和文档所在频道索引
	 * @param docId
	 * @param siteId
	 */
	public void deleteDocumetFromIndex(HttpServletRequest request,com.frameworkset.platform.cms.documentmanager.Document doc,String siteId,List attachmentList,List indexList  ) throws Exception{

		log.debug("正在删除文档索引！");
		try{
			
//			com.frameworkset.platform.cms.documentmanager.Document doc = CMSUtil.getCMSDriverConfiguration().getCMSService().getDocumentManager().getDocWithNoContent(docId);
			String docUrl = this.getPublishedDocUrl(doc,siteId);	
			//获取文档已发布附件的相对路径
//			List attachmentList = new DocumentManagerImpl().getAllPublishedAttachmentOfDocument(request,docId,siteId);
			
			//关联整站索引和频道索引
//			List indexList = this.getIndexListOfCmsdocument(docId,siteId);
			//只关联当前站点的整站索引
			//List indexList = this.getSiteIndex(siteId);
			
			for(int i=0;indexList != null && i<indexList.size();i++){
				//得到索引记录
				CMSSearchIndex  index = (CMSSearchIndex)indexList.get(i);
				String absoluteIndexFilePath = this.getAbsoluteIndexFilePath(index);
				//判断索引库是否存在
				File f = new File(absoluteIndexFilePath);
				IndexWriter writer = null;
				try
				{
					if(f.exists()){
						Directory dir = FSDirectory.open(f);
					      // :Post-Release-Update-Version.LUCENE_XY:
					      Analyzer analyzer = new SmartChineseAnalyzer(Version.LUCENE_47);
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
						//判断索引库是否被锁
						if(!this.isIndexLocked(index)){
							//逻辑删除
							//Term term = new Term("uid","http://localhost:8090/creatorcms/sitepublish/site200/zjcz/content_33925.html");
							Term term = new Term("uid",docUrl.toLowerCase());
							writer.deleteDocuments(term);	
							log.debug("删除索引成功，文档:" + this.getDocPubDestinction(doc,siteId));					
							//对每一个文档附件删除相应的索引记录
							for(int j=0;j<attachmentList.size();j++){
								//获取附件相对于站点发布路径的相对路径，如：zjcz/content_files/20070709024320984.doc
								String relativePath = (String)attachmentList.get(j);
								//获取站点的发布路径
								String sitePubDir = CMSUtil.getSitePubDestinction(siteId);
								//获取附件发布后的绝对路径
								String attachmentPubDir = CMSUtil.getPath(sitePubDir,relativePath);
								//获取附件访问地址
								String attachmentUrl = this.getPublishedFileUrl(attachmentPubDir,siteId);						
								Term attachTerm = new Term("uid",attachmentUrl.toLowerCase());
								writer.deleteDocuments(attachTerm);						
								log.debug("删除索引成功，文档附件:" + attachmentPubDir);
							}					
						}else{
							log.debug(absoluteIndexFilePath + "已被锁，无法删除索引记录！");
							throw new Exception(absoluteIndexFilePath + "已被锁，无法删除索引记录！"); 
						}
					}
				}
				finally
				{
					if(writer != null)
					{
						try {
							writer.close();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}catch(Exception e){}
		
		
	}
	public void addDocumetToIndex(HttpServletRequest request,String docId,String siteId) throws Exception{
		addDocumetToIndex(request,docId,siteId,true); 
	}
	
	/**
	 * 每发布一篇文档，往文档所在站点索引和频道索引的索引库中插入索引记录
	 * @param request 获取文档附件使用
	 * @param docId
	 * @param siteId
	 */
	public void addDocumetToIndex(HttpServletRequest request,String docId,String siteId,boolean deleteIndex) throws Exception{
		
		
			if(docId == null || docId.length() <= 0 || siteId == null || siteId.length() <= 0){
				throw new Exception("参数文档id和站点id都不能为空！"); 
			}
			com.frameworkset.platform.cms.documentmanager.Document doc = CMSUtil.getCMSDriverConfiguration().getCMSService().getDocumentManager().getDocWithNoContent(docId);
			//防止重复追加索引
			
			if(deleteIndex)
			{
				this.deleteDocumetFromIndex(request,doc,siteId);
			}
			addDocumetToIndex(request,doc,siteId,deleteIndex);	
			
			
		
	}
	/**
	 * 每发布一篇文档，往文档所在站点索引和频道索引的索引库中插入索引记录
	 * @param request 获取文档附件使用
	 * @param docId
	 * @param siteId
	 */
	public void addDocumetToIndex(HttpServletRequest request,com.frameworkset.platform.cms.documentmanager.Document doc,String siteId,boolean deleteIndex) throws Exception{
		
		try{
			//获取文档已发布附件的相对路径
			List attachmentList = new DocumentManagerImpl().getAllPublishedAttachmentOfDocument(request,doc,siteId);
			
			//关联整站索引和频道索引
			List indexList = this.getIndexListOfCmsdocument(doc,siteId);
			addDocumetToIndex(request,doc,siteId,attachmentList ,indexList ,deleteIndex);
		}catch(Exception e){}
		
		
	}
	
	/**
	 * 每发布一篇文档，往文档所在站点索引和频道索引的索引库中插入索引记录
	 * @param request 获取文档附件使用
	 * @param docId
	 * @param siteId
	 */
	public void addDocumetToIndex(HttpServletRequest request,com.frameworkset.platform.cms.documentmanager.Document doc,String siteId,List attachmentList ,List indexList ,boolean deleteIndex) throws Exception{
		log.debug("正在添加文档索引！");
		try{
			//防止重复追加索引
			if(deleteIndex)
				this.deleteDocumetFromIndex(request,doc,siteId,attachmentList ,indexList);
			
			//文档发布文件的访问地址
			String docUrl = this.getPublishedDocUrl(doc,siteId);
			
			CMSCrawler crawler = new CMSCrawler();
			long lastModified = new Date().getTime();
			String docContentType = ContentHandler.TEXT_HTML_FILEFOMAT;
			
			ContentHandler docHandler = crawler.handleLocalFile(new File(this.getDocPubDestinction(doc,siteId)),docContentType,null);
			
			
			
			//只关联当前站点的整站索引
			//List indexList = this.getSiteIndex(siteId);
			
			for(int i=0;i<indexList.size();i++){
				//得到索引记录
				CMSSearchIndex  index = (CMSSearchIndex)indexList.get(i);
				String absoluteIndexFilePath = this.getAbsoluteIndexFilePath(index);
				File f = new File(absoluteIndexFilePath);
				//若索引库不存在无需追加索引记录
				if(f.exists()){
					
					releaseIndexLocked(index);
					
					//往现有的索引文件中追加记录
					IndexWriter writer = null;
					Directory dir = null;
					try
					{
//							writer = new IndexWriter(absoluteIndexFilePath,						
//																		new StandardAnalyzer(), false); 
						 dir = FSDirectory.open(f);
					      // :Post-Release-Update-Version.LUCENE_XY:
					      Analyzer analyzer = new SmartChineseAnalyzer(Version.LUCENE_47);
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
						//对文档追加索引记录
						crawler.indexLucene(writer,docHandler,docUrl,docContentType,lastModified);
						
						//对每一个文档附件追加索引记录
						for(int j=0;j<attachmentList.size();j++){
							//获取附件相对于站点发布路径的相对路径，如：zjcz/content_files/20070709024320984.doc
							String relativePath = (String)attachmentList.get(j);
							//获取站点的发布路径
							String sitePubDir = CMSUtil.getSitePubDestinction(siteId);
							//获取附件发布后的绝对路径
							String attachmentPubDir = CMSUtil.getPath(sitePubDir,relativePath);
							//获取附件访问地址
							String attachmentUrl = this.getPublishedFileUrl(attachmentPubDir,siteId);
							
							//判断附件类型，选择文件解析器ContentHandle
							if(attachmentUrl.endsWith(".doc") || attachmentUrl.endsWith(".docx")){
								
								String attachmentContentType = ContentHandler.WORD_FILEFOMAT;
								ContentHandler attachmentHandler = crawler.
																		handleLocalFile(new File(attachmentPubDir),attachmentContentType,attachmentUrl.endsWith(".doc") ?ContentHandler.VERSION_2003:ContentHandler.VERSION_2007);
								
								//追加附件索引记录
								crawler.indexLucene(writer,
										attachmentHandler,attachmentUrl,attachmentContentType,lastModified);
								
							}
							else if(attachmentUrl.endsWith(".xls") || attachmentUrl.endsWith(".xlsx")){
								
								String attachmentContentType = ContentHandler.EXCEL_FILEFOMAT;
								ContentHandler attachmentHandler = crawler.
																		handleLocalFile(new File(attachmentPubDir),attachmentContentType,attachmentUrl.endsWith(".xls") ?ContentHandler.VERSION_2003:ContentHandler.VERSION_2007);
								
								//追加附件索引记录
								crawler.indexLucene(writer,
										attachmentHandler,attachmentUrl,attachmentContentType,lastModified);
								
							}
							else if(attachmentUrl.endsWith(".ppt") || attachmentUrl.endsWith(".pptx")){
								
								String attachmentContentType = ContentHandler.PPT_FILEFOMAT;
								ContentHandler attachmentHandler = crawler.
																		handleLocalFile(new File(attachmentPubDir),attachmentContentType,attachmentUrl.endsWith(".ppt") ?ContentHandler.VERSION_2003:ContentHandler.VERSION_2007);
								
								//追加附件索引记录
								crawler.indexLucene(writer,
										attachmentHandler,attachmentUrl,attachmentContentType,lastModified);
								
							}
							else if(attachmentUrl.endsWith(".pdf")){
								
								String attachmentContentType = ContentHandler.PDF_FILEFOMAT;
								ContentHandler attachmentHandler = crawler.
																		handleLocalFile(new File(attachmentPubDir),attachmentContentType,null);
																		
								//追加附件索引记录
								crawler.indexLucene(writer,
										attachmentHandler,attachmentUrl,attachmentContentType,lastModified);
							}else if(attachmentUrl.endsWith(".html") ||
									attachmentUrl.endsWith(".htm") ||
									attachmentUrl.endsWith(".txt") ){
								
								String attachmentContentType = ContentHandler.TEXT_HTML_FILEFOMAT;
								ContentHandler attachmentHandler = crawler.
																		handleLocalFile(new File(attachmentPubDir),attachmentContentType,null);
																		
								//追加附件索引记录
								crawler.indexLucene(writer,
										attachmentHandler,attachmentUrl,attachmentContentType,lastModified);
							}
						}
						
						
					}
					finally
					{
						try {
							if(writer != null)
								writer.close();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						try {
							if(dir != null)
								dir.close();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}else{
					log.debug(absoluteIndexFilePath + "已被锁，无法追加索引记录！");
					throw new Exception(absoluteIndexFilePath + "已被锁，无法追加索引记录！"); 
				}
				
			}
		}catch(Exception e){}
		
		
	}
	
	/**
	 * 获取发布文件的访问地址,如：http://localhost:8090/creatorcms/sitepublish/site200/zjcz/content_33925.html 
	 * @param filePath  发布文件的绝对路径，如：D:\workspace\cms\creatorcms\sitepublish\site200\zjcz\content_33925.html
	 * @return
	 */
	public static String getPublishedFileUrl(String filePath,String siteId){
		String sitepubdir = CMSUtil.getSitePubDestinction(siteId);
		String filePathTemp = filePath.substring(sitepubdir.length());
		
		return CMSUtil.getPath(CMSUtil.getPublishedSitePath(siteId),filePathTemp).replace('\\','/');
	}
	
	/**
	 * 获取发布文件的访问地址,如：http://localhost:8090/creatorcms/sitepublish/site200/zjcz/content_33925.html 
	 * @param filePath  发布文件的绝对路径，如：D:\workspace\cms\creatorcms\sitepublish\site200\zjcz\content_33925.html
	 * @return
	 */
	public static String getPublishedIndexFileUri(String filePath,String siteId){
		String sitepubdir = CMSUtil.getSitePubDestinction(siteId);
		String filePathTemp = filePath.substring(sitepubdir.length());
		return filePathTemp.replace('\\','/');
		
	}
	
	/**
	 * 获取发布文件的访问地址,如：http://localhost:8090/creatorcms/sitepublish/site200/zjcz/content_33925.html 
	 * @param filePath  发布文件的绝对路径，如：D:\workspace\cms\creatorcms\sitepublish\site200\zjcz\content_33925.html
	 * @return
	 */
	public static String getPublishedIndexFileUrl(String filePath,String siteId,String context){
		String sitepubdir = CMSUtil.getSitePubDestinction(siteId);
		String filePathTemp = filePath.substring(sitepubdir.length());
		
		Site site = CMSUtil.getSite(siteId);
		String domain = site.getWebHttp();
		if(domain == null || domain.equals("") || domain.equals("http://") || domain.equals("https://"))
		{
			domain =context;
		}
		
		return domain + CMSUtil.getPath(CMSUtil.getPublishedSitePath(siteId),filePathTemp).replace('\\','/');
	}
	
	/**
	 * 获取发布文件的相对地址,如：/site200/zjcz/content_33925.html 
	 * @param 
	 * @return
	 * @throws Exception 
	 */
	public String getPublishedFilePathTail(String docId,String siteId) throws Exception{
		try{
			com.frameworkset.platform.cms.documentmanager.Document doc = CMSUtil.getCMSDriverConfiguration().getCMSService().getDocumentManager().getDoc(docId);
			String pathAll = getDocPubDestinction(doc, siteId);
			String siteDir = CMSUtil.getSitePubDestinction(siteId);			
//			siteDir = siteDir.substring(0, siteDir.indexOf("site" + siteId)-1);
			Site site = CMSUtil.getSiteCacheManager().getSite(siteId);
			siteDir = siteDir.substring(0, siteDir.indexOf(site.getSecondName())-1);			
			return pathAll.substring(siteDir.length());
		}catch(Exception e){}				
		return "";
	}
	
	/**
	 * 根据文档id和站点id获取文档最终发布文件的访问地址
	 * 如：http://localhost:8090/creatorcms/sitepublish/site200/zjcz/content_33925.html 
	 * @param docId
	 * @param siteId
	 * @return
	 * @throws Exception
	 */
	private String getPublishedDocUrl(com.frameworkset.platform.cms.documentmanager.Document doc,String siteId) throws Exception{
		//获取文档发布文件的绝对路径
		String docPubDestinction = this.getDocPubDestinction(doc,siteId);
		//获取文档的访问地址
		return this.getPublishedFileUrl(docPubDestinction,siteId);
	}
	/**
	 * 获取文档发布的文件绝对路径，
	 * 如：D:\workspace\cms\creatorcms\sitepublish\site200\zjcz\content_33925.html
	 * @param docId
	 * @param siteId
	 * @return
	 */
	private String getDocPubDestinction(com.frameworkset.platform.cms.documentmanager.Document doc,String siteId) throws Exception
	{
		int chnlId = doc.getChanel_id();
		String channelPubDestinction = CMSUtil.getChannelPubDestinction(siteId,chnlId+"");
		return CMSUtil.getPath(channelPubDestinction,CMSUtil.getContentFileName(siteId,doc));
	}
	/**
	 * 搜索按钮的显示字符串
	 * @param searchType
	 * @return
	 */
	public String getSearchButtonValue(String searchType)
	{
		String searchTypeDes = "搜索";
		if("0".equals(searchType))
		{
			searchTypeDes = "频道搜索";
		}else if("1".equals(searchType))
		{
			searchTypeDes = "站外搜索";
		}else if("2".equals(searchType))
		{
			searchTypeDes = "站内搜索";
		}else if("3".equals(searchType))
		{
			searchTypeDes = "站群搜索";
		}else if("4".equals(searchType))
		{
			searchTypeDes = "库表搜索";
		}
		return searchTypeDes;
	}
	/**
	 * 获取一个站点的所有索引,包括站内的和站外的索引,在高级搜索（advanced_search.jsp）中，供用户选择
	 * @param siteId
	 * @return
	 */
	public List getIndexListOfSite(String siteId) throws Exception{
		String sql = "select * from td_cms_site_search where site_id = " + siteId;
		return this.getIndexList(sql);
	}
	
	private List getIndexList(String sql) throws Exception{
		DBUtil db = new DBUtil();
		DBUtil db1 = new DBUtil();
		DBUtil db2 = new DBUtil();
		DBUtil db3 = new DBUtil();
		DBUtil db4 = new DBUtil();
		List list = new ArrayList();
		try{
			db.executeSelect(sql);
			for(int i=0;i<db.size();i++){
				CMSSearchIndex searchIndex = new CMSSearchIndex();
				searchIndex.setId(db.getInt(i,"id"));				
				String siteId = db.getInt(i,"site_id")+"";
				searchIndex.setSiteId(siteId);
				searchIndex.setSiteName(CMSUtil.getSiteCacheManager().getSite(siteId).getName());		//设置站点名
				int searchType = db.getInt(i,"search_type");
				searchIndex.setSearchType(searchType);				
				searchIndex.setLever(db.getInt(i,"lever"));
				searchIndex.setDay(db.getInt(i,"day"));
				searchIndex.setTime(db.getInt(i,"time"));				
				String indexName = db.getString(i,"name");
				searchIndex.setIndexName(indexName);
				String indexPath = getIndexRootPath();
				searchIndex.setIndexPath(indexPath);				
				if(searchType == 0){   							 //站内频道搜索
					String chnlId = db.getString(i,"chnl_or_domain");
					searchIndex.setChnlId(chnlId);
	            	String[] chnlIds = chnlId.split(",");
	            	String chnlNames = "";	            	
	            	if(chnlIds.length>=1)
	            	{
	            		if( !(chnlIds[0].equals("")) && !(chnlIds[0]==null) )//未预期SQL命令的错误结尾修改,weida
	            		{
	            			
			            	
			            	chnlNames = CMSUtil.getChannelCacheManager(siteId).getChannel(chnlIds[0]).getDisplayName();
	
			            	
	            		}		            			            		
	            		for(int r=1; r<chnlIds.length; r++)
		            	{
			            	
			            	{
			            		chnlNames = chnlNames + "," +CMSUtil.getChannelCacheManager(siteId).getChannel(chnlIds[r]).getDisplayName();
			            	}
		            	}
	            		searchIndex.setChnlName(chnlNames);
	            	}
	            	//频道可以有多个,id改成了字符串,weida
	            	//searchType为0(即站内频道搜索),site_id仅能确定全部这个站点下的已建好索引的频道的搜索	            	
					searchIndex.setAbsoluteIndexPath(
							this.getAbsoluteIndexFilePath(indexPath,"in_" + indexName,siteId+""));
				}else if(searchType == 1){                       //站外搜索
					searchIndex.setDomainUrl(db.getString(i,"chnl_or_domain"));
					searchIndex.setStartUrl(db.getString(i,"start_url"));
					searchIndex.setAbsoluteIndexPath(
							this.getAbsoluteIndexFilePath(indexPath,"out_" + indexName,siteId+""));
				}else if(searchType == 2)	{					//整站检索搜索
					searchIndex.setAbsoluteIndexPath(
							this.getAbsoluteIndexFilePath(indexPath,"site_" + indexName,siteId+""));
				}else if(searchType == 3)	{					//站群检索搜索
					searchIndex.setAbsoluteIndexPath(
							this.getAbsoluteIndexFilePath(indexPath,"sites_" + indexName,siteId+""));
				}else if(searchType == 4)	{					//库表检索搜索
					
					String sql4 = "select * from td_cms_dbtsearch_detail t where t.id =" + db.getInt(i,"id");
					db4.executeSelect(sql4);
					if(db4.size()>0){
						searchIndex.setWheres(db4.getString(0,"WHERES"));
						searchIndex.setAccess_url(db4.getString(0,"ACCESS_URL"));					
						searchIndex.setDb_name(db4.getString(0,"DB_NAME"));
						searchIndex.setTable_name(db4.getString(0,"TABLE_NAME"));
						searchIndex.setPrimarys(db4.getString(0,"PRIMARYS"));
						searchIndex.setTitle_field(db4.getString(0,"TITLE_FIELD"));
						searchIndex.setKeyword_field(db4.getString(0,"KEYWORD_FIELD"));
						searchIndex.setPublishtime_field(db4.getString(0,"PUBLISHTIME_FIELD"));
						searchIndex.setDescription_field(db4.getString(0,"DESCRIPTION_FIELD"));
						searchIndex.setContent_fields(db4.getString(0,"CONTENT_FIELDS"));
						searchIndex.setContent_types(db4.getString(0,"CONTENT_TYPES"));
						searchIndex.setContent_path(db4.getString(0,"CONTENT_PATH"));
					}					
					searchIndex.setAbsoluteIndexPath(
							this.getAbsoluteIndexFilePath(indexPath,"db_" + indexName,siteId+""));
				}
				
				list.add(searchIndex);
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception("获取索引列表时失败！" + e.getMessage());
		}
		return list;
	}
	
}
