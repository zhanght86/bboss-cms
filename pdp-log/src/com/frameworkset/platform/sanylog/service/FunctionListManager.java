package com.frameworkset.platform.sanylog.service;

import java.sql.SQLException;
import java.util.List;

import com.frameworkset.platform.sanylog.bean.App;
import com.frameworkset.platform.sanylog.bean.FunctionList;
import com.frameworkset.platform.sanylog.bean.SpentTime;
import com.frameworkset.util.ListInfo;

public interface FunctionListManager {
	public ListInfo showFunctionList(String appId,String  functionCode,String functionName,int offset,int pagesize)throws SQLException;
	public List<App> getAllApp()throws SQLException;
	public void deleteRecord(String id)throws SQLException;
	List<FunctionList> getSingleFunctionList(String id)throws SQLException;
	public void functionListSingleModify(String id,int estimateOper,int estimateUser)throws SQLException;
	List<String> getAppId(String appName)throws SQLException;
	public List<String> getMaxAppId()throws SQLException;
	public void functionListSingleIncrement(String id,String appId,String appName,String functionName,String functionCode,int estimateOper,int estimateUser)throws SQLException;
	public void testJira()throws SQLException;
	List<FunctionList> staticSpentTimeByAppName(String appName,String appId)throws SQLException;
	public void deleteSysSpentTime(String appId)throws SQLException;
	public void insertSysSpentTime(List<SpentTime> sysSpentTime)throws SQLException;
	public List<FunctionList> getFunctionListInDB()throws SQLException;
	public void batchInsertFunctionList(List<FunctionList> excelDatas,List<FunctionList> DBDatas)throws SQLException;
	public void insertApp(String autoId,String appId,String appName)throws SQLException;
	public List<FunctionList> getFunctionList(String appId)throws SQLException;
	public void insertSysTime(List<FunctionList> sysSpentTime)throws SQLException;
	public void updateSysTime(List<FunctionList> datasForUpdate)throws SQLException;

	//测试存储过程
	public void testBatchInsertFunctionList(List<FunctionList> excelDatas )throws SQLException;

}
