package com.frameworkset.platform.cms.searchmanager.tag;

import com.frameworkset.platform.cms.searchmanager.CMSSearchManager;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

/**
 * 库表索引列表
 * */
public class CMSDBTSearchIndexList extends DataInfoImpl implements java.io.Serializable {
	
	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
            int maxPagesize){				
		String currentSiteid = (String)session.getAttribute("currentSiteid");
//		String sql = "select a.*,b.db_name as dbName,b.table_name as tableName from td_cms_site_search a "
//				+ "inner join td_cms_dbtsearch_detail b on a.id = b.id" 
//				+ " where a.search_type = 4 "
//				+ "and a.site_id = " + currentSiteid + " order by a.id";
		String sql = "select a.*,b.db_name as dbName,b.table_name as tableName from td_cms_site_search a "
			+ "inner join td_cms_dbtsearch_detail b on a.id = b.id" 
			+ " where a.search_type = 4 order by a.id";
		ListInfo listInfo=null;
		try {
			listInfo = new CMSSearchManager().getDBTSearchIndexList(sql,(int)offset,maxPagesize);
		} catch (Exception e) {
			e.printStackTrace();
		}			
		return listInfo;
	}

	protected ListInfo getDataList(String arg0, boolean arg1) {
		// TODO Auto-generated method stub
		return null;
	}
}
