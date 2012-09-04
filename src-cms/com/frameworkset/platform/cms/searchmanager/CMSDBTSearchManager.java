package com.frameworkset.platform.cms.searchmanager;

import java.io.File;

import org.apache.lucene.index.IndexReader;

import com.frameworkset.platform.cms.searchmanager.bean.CMSSearchIndex;
import com.frameworkset.common.poolman.DBUtil;

/*
 * weida，20070929
 * 数据库检索的相关接口
 * */
public class CMSDBTSearchManager implements java.io.Serializable {
	
	/*
	 * 删除数据库，删除这个数据库下所有的库表索引
	 * */
	public void deleteDbIndexs(String db) throws Exception{
		if(db==null || db.equals(null))
			db = "";
		if(!db.equals("")){
			String sql = "select * from TD_CMS_DBTSEARCH_DETAIL where db_name='" + db + "'";
			DBUtil dBUtil = new DBUtil();
			try{
				dBUtil.executeSelect(sql);
				if(dBUtil.size() > 0){
					for(int i=0;i<dBUtil.size();i++){
						String id = dBUtil.getString(i, "id");
						deleteDbIndex(id);						
					}
				}
			}catch(Exception e){e.printStackTrace();}
		}
	}
	
	/*
	 * 删除index对象的索引库文件
	 * */
	private void deleteDbIndexFile(CMSSearchIndex searchIndex) throws Exception{
		try{
			String indexFileName = searchIndex.getIndexName();
			indexFileName = "db_" + indexFileName;
			CMSSearchManager searchManager = new CMSSearchManager();
			String absoluteIndexFilePath = searchManager.getAbsoluteIndexFilePath(searchManager.getIndexRootPath(),indexFileName,searchIndex.getSiteId());
			File absoluteIndexFile = new File(absoluteIndexFilePath);
			if(absoluteIndexFile.exists()){
				if(!IndexReader.isLocked(absoluteIndexFilePath))
					searchManager.deleteFilesAndDirector(absoluteIndexFile);
				else
					throw new Exception("索引文件已经上锁，暂时无法删除：" + absoluteIndexFilePath);
			}
		}catch(Exception e){e.printStackTrace();}
	}
	
	/*
	 * 删除index对象，包括索引库文件、索引记录和详细记录
	 * */
	private void deleteDbIndex(String id) throws Exception{
		CMSSearchIndex searchIndex = getDbIndexById(id);
		deleteDbIndexFile(searchIndex);
		DBUtil dBUtil = new DBUtil();
		String sql1 = "delete from TD_CMS_DBTSEARCH_DETAIL where id = " + searchIndex.getId();
		String sql2 = "delete from td_cms_site_search where id = " + searchIndex.getId();
		try{
			dBUtil.executeDelete(sql1);
			dBUtil.executeDelete(sql2);
		}catch(Exception e){e.printStackTrace();}
	}
	
	/*
	 * 由id得到库表index对象
	 * */
	private CMSSearchIndex getDbIndexById(String id) throws Exception{
		String sql = "select * from TD_CMS_SITE_SEARCH where id='" + id + "'";
		DBUtil dBUtil = new DBUtil();
		try{
			dBUtil.executeSelect(sql);
			if(dBUtil.size() > 0){
				CMSSearchIndex searchIndex = new CMSSearchIndex();
				searchIndex.setId(dBUtil.getInt(0, "id"));
				searchIndex.setIndexName(dBUtil.getString(0, "name"));
				searchIndex.setSearchType(4);
				searchIndex.setSiteId(dBUtil.getString(0, "site_id"));
				return searchIndex;
			}
		}catch(Exception e){e.printStackTrace();}
		return null;
	}
	
	/*
	 * 删除表，删除这个表的所有库表索引
	 * */	
	public void deleteDbTableIndexs(String db, String table) throws Exception{}
	
	/*
	 * 修改表，删除这个表的所有库表索引
	 * */
	public void updateDbTableIndexs() throws Exception{}
	
	/*
	 * 删除数据，去掉这个表的所有库表索引的相关记录
	 * */
	public void deleteDbTableDataIndexs() throws Exception{}
	
	/*
	 * 修改数据，删除再重新添加这个表的所有库表索引的相关记录
	 * */
	public void updateDbTableDataIndexs() throws Exception{}
	
	/*
	 * 添加数据，在这个表的所有库表索引中添加相关纪录
	 * */
	public void addDbTableDataIndexs() throws Exception{}
}
