package com.frameworkset.platform.sanylog.service;

import java.sql.SQLException;
import java.util.List;

import com.frameworkset.platform.sanylog.bean.App;
import com.frameworkset.platform.sanylog.bean.FunctionList;
import com.frameworkset.platform.sanylog.bean.PageCounter;
import com.frameworkset.platform.sanylog.bean.PageList;
import com.frameworkset.platform.sanylog.bean.SpentTime;
import com.frameworkset.util.ListInfo;

public interface PageListManager {
	public ListInfo showFunctionList(String appId,String  functionCode,String functionName,int offset,int pagesize)throws SQLException;
	public List<App> getAllApp()throws SQLException;
	public void deleteAllPageList()throws SQLException;
	public void staticAllPageList()throws SQLException;
	public List<PageList> getSinglePageList(String id)throws SQLException;
	public void updatePageList(String id,String functionCode)throws SQLException;
	public List<PageList>getExcelDataByAppId(String appId)throws SQLException;
	public List<String> getAppNameById(String appId)throws SQLException;
	public List<PageCounter> showPageCounterList(String  startTime, String endTime)throws SQLException;
	public List<PageCounter> getMaintainPage()throws SQLException;
	public void savePages(String appName,String  pages)throws SQLException;
	//测试存储过程
	public void testBatchInsertFunctionList(List<FunctionList> excelDatas )throws SQLException;
	public void updateBatchPageList(List<PageList> dataGroupList, String appId)throws SQLException;

}
