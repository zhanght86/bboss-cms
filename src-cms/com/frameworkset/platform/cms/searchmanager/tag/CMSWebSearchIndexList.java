package com.frameworkset.platform.cms.searchmanager.tag;

import com.frameworkset.platform.cms.searchmanager.CMSSearchManager;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

/**
 * 站外索引列表
 * */
public class CMSWebSearchIndexList extends DataInfoImpl implements java.io.Serializable {

	protected ListInfo getDataList(String sortKey, boolean desc, long offSet,
			int pageItemsize) {
		String currentSiteid = (String)session.getAttribute("currentSiteid");
		String sql = "select a.*,b.name as siteName " +
				"from td_cms_site_search a " +
				"inner join td_cms_site b on a.site_id = b.site_id " +
				"where a.search_type = 1 and a.site_id = " + currentSiteid + " order by a.id";
		ListInfo listInfo=null;
		try {
			listInfo = new CMSSearchManager().getWebSearchIndexList(sql,(int)offSet,pageItemsize);
		} catch (Exception e) {
			e.printStackTrace();
		}			
		return listInfo;
	}

	protected ListInfo getDataList(String sortKey, boolean desc) {
		// TODO Auto-generated method stub
		return null;
	}

}
