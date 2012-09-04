package com.frameworkset.platform.dbmanager;

import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

public class TableInfoSearchList extends DataInfoImpl {

	protected ListInfo getDataList(String sortKey, boolean desc, long offset, int maxItem) {
		
		
		String tableName = request.getParameter("tableName");
		String keyName = request.getParameter("keyName");
		String keyType = request.getParameter("keyType");
		String dbName = request.getParameter("db");
		
		//默认数据源名称是: bspf
		if(dbName == null || dbName.equals(""))
		{
			dbName = "bspf";
		}

		StringBuffer sb = new StringBuffer("select * from TABLEINFO a ");
		
		boolean flag = false ;
		
		if(tableName != null && !tableName.equals("")){
			sb.append("where a.TABLE_NAME like '%").append(tableName).append("%' ");
			flag = true ;
		}
		
		if(flag){
			if(keyName != null && !keyName.equals("")){
				sb.append("and a.TABLE_ID_Name like '%").append(keyName).append("%' ");
			}
			
			if(keyType != null && !keyType.equals("")){
				sb.append("and a.TABLE_ID_TYPE like '%").append(keyType).append("%' ");
			}
		}
		else{
			if(keyName != null && !keyName.equals("")){
				sb.append("where a.TABLE_ID_Name like '%").append(keyName).append("%' ");
				
				if(keyType != null && !keyType.equals("")){
					sb.append("and a.TABLE_ID_TYPE like '%").append(keyType).append("%' ");
				}
				
			}
			else{
				if(keyType != null && !keyType.equals("")){
					sb.append("where a.TABLE_ID_TYPE like '%").append(keyType).append("%' ");
				}
			}
		}
		
		TableInfoManager tableInfoEntiyManger = new TableInfoManagerImpl();
		ListInfo listInfo = null ;
		
		try {
			listInfo = tableInfoEntiyManger
							.getTableInfoEntityList(dbName,sb.toString(),(int)offset,maxItem);
		} catch (TableInfoManagerException e) {
			e.printStackTrace();
		}
		
		return listInfo;
		
	}

	protected ListInfo getDataList(String sortKey, boolean desc) {
		
		return null;
	}
}
