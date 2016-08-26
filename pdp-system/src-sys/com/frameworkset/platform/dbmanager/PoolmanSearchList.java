package com.frameworkset.platform.dbmanager;

import java.util.ArrayList;
import java.util.List;

import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

public class PoolmanSearchList extends DataInfoImpl {

	protected ListInfo getDataList(String sortKey, boolean desc, long offset, int maxItem) {
		
		ListInfo listInfo = new ListInfo();
	
		
//		ParsedPoolman poolman = PoolmanHelper.getParsedPoolman();
//		
//		//每页只显示一条poolman连接池对象
//		ParsedDataSource  dataSource = (ParsedDataSource)poolman.getDataSourceList().get((int)offset);
		
//		List list = new ArrayList();
//		list.add(dataSource);
//		
//		listInfo.setDatas(list);
//		listInfo.setTotalSize(poolman.getDataSourceList().size());
		
		return listInfo;
	}

	protected ListInfo getDataList(String sortKey, boolean desc) {
		
		return null;
	}
}
