package com.frameworkset.platform.cms.searchmanager.tag;

import com.frameworkset.platform.cms.searchmanager.CMSSearchManager;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

/**
 * 站群索引列表
 * */
	public class CMSSitesSearchIndexList extends DataInfoImpl implements java.io.Serializable {

		protected ListInfo getDataList(String sortKey, boolean desc, long offset,
	            int maxPagesize){				
			String currentSiteid = (String)session.getAttribute("currentSiteid");
//			String sql = "select * from td_cms_site_search where search_type = 3 and site_id = " + currentSiteid + " order by id";
			String sql = "select * from td_cms_site_search where search_type = 3 order by id";
			ListInfo listInfo=null;
			try {
				listInfo = new CMSSearchManager().getSitesSearchIndexList(sql,(int)offset,maxPagesize);
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
