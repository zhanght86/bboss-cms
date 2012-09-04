package com.frameworkset.platform.cms.searchmanager.tag;


import com.frameworkset.platform.cms.searchmanager.CMSSearchManager;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

/**
 * 站内索引列表
 * */
	public class CMSSiteSearchIndexList extends DataInfoImpl implements java.io.Serializable {

		protected ListInfo getDataList(String sortKey, boolean desc, long offset,
	            int maxPagesize){				
			String currentSiteid = (String)session.getAttribute("currentSiteid");
			String sql = "select a.*,b.name as siteName " +
			"from td_cms_site_search a " +
			"inner join td_cms_site b on a.site_id = b.site_id " +
			"where (a.search_type = 0 or (a.search_type = 2)) order by a.id";
//			String sql = "select a.*,b.name as siteName " +
//					"from td_cms_site_search a " +
//					"inner join td_cms_site b on a.site_id = b.site_id " +
//					"where (a.search_type = 0 or (a.search_type = 2))and a.site_id = " + currentSiteid + " order by a.id";
			ListInfo listInfo=null;
			try {
				listInfo = new CMSSearchManager().getLocalSearchIndexList(sql,(int)offset,maxPagesize);
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
