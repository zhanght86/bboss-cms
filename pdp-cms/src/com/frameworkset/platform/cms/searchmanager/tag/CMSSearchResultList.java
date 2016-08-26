package com.frameworkset.platform.cms.searchmanager.tag;

import java.util.List;

import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

public class CMSSearchResultList extends DataInfoImpl implements java.io.Serializable {

	protected ListInfo getDataList(String sortKey, boolean desc, long offSet,
			int pageItemsize) {
		
		List searchhitList = (List)session.getAttribute("searchhitList");
		return super.pagerList(searchhitList,(int)offSet,pageItemsize);
		
	}

	protected ListInfo getDataList(String sortKey, boolean desc) {
		// TODO Auto-generated method stub
		return null;
	}

}
