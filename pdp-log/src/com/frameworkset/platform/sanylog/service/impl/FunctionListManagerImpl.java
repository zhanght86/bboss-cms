package com.frameworkset.platform.sanylog.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

 

import com.frameworkset.common.poolman.CallableDBUtil;
import com.frameworkset.common.poolman.ConfigSQLExecutor;
import com.frameworkset.common.poolman.SQLExecutor;
import com.frameworkset.platform.sanylog.bean.App;
import com.frameworkset.platform.sanylog.bean.FunctionList;
import com.frameworkset.platform.sanylog.bean.SpentTime;
import com.frameworkset.platform.sanylog.service.FunctionListManager;
import com.frameworkset.util.ListInfo;

public class FunctionListManagerImpl implements FunctionListManager{
	private ConfigSQLExecutor executor;

	@Override
	public ListInfo showFunctionList(String appId, String functionCode,
			String functionName, int offset, int pagesize) throws SQLException {

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("appId", appId);
		paramMap.put("functionCode", functionCode);
		paramMap.put("functionName", functionName);

		ListInfo datas = executor.queryListInfoBean(FunctionList.class, "showFunctionList", offset, pagesize, paramMap);
		return datas;
	}

	@Override
	public List<App> getAllApp() throws SQLException {
		List<App> datas = executor.queryList(App.class, "getAllApp");
		return datas;
	}

	@Override
	public void deleteRecord(String id) throws SQLException {
		executor.delete("deleteRecord", id);
	}

	@Override
	public List<FunctionList> getSingleFunctionList(String id)
			throws SQLException {
		List<FunctionList> datas = executor.queryList(FunctionList.class, "getSingleFunctionList",id);
		return datas;
	}

	@Override
	public void functionListSingleModify(String id, int estimateOper,
			int estimateUser) throws SQLException {
		FunctionList bean = new FunctionList();
		bean.setId(id);
		bean.setEstimateOper(estimateOper);
		bean.setEstimateUser(estimateUser);
		executor.updateBean("functionListSingleModify", bean);
		
	}

	@Override
	public List<String> getAppId(String appName) throws SQLException {
		List<String> datas = executor.queryList(String.class, "getAppId", appName);
		return datas;
	}

	@Override
	public List<String> getMaxAppId() throws SQLException {
		List<String> datas = executor.queryList(String.class, "getMaxAppId");
		return datas;
	}

	@Override
	public void functionListSingleIncrement(String id, String appId,
			String appName, String functionName, String functionCode,
			int estimateOper, int estimateUser) throws SQLException {
		FunctionList bean = new FunctionList();
		bean.setId(id);
		bean.setEstimateOper(estimateOper);
		bean.setEstimateUser(estimateUser);
		bean.setAppId(appId);
		bean.setAppName(appName);
		bean.setFunctionCode(functionCode);
		bean.setFunctionName(functionName);
		executor.insertBean("functionListSingleIncrement", bean);
		
	}

	 
	public void testJira()throws SQLException {
		//List<String> datas = executor.queryListWithDBName(String.class, "jira", "jiratest");
		   
        try  
        {              
            List<String> datas = SQLExecutor.queryListWithDBName(String.class, "jira", "select pname from issuetype");
            System.out.println();
      
         
        }   
        catch(Exception e)   
        {   
            
        }   
    }

	@Override
	public List<FunctionList> staticSpentTimeByAppName(String appName,String appId)
			throws SQLException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("appName", appName);
		paramMap.put("appId", appId);
		List<FunctionList> datas = executor.queryListBeanWithDBName(FunctionList.class, "jira", "staticSpentTimeByAppName", paramMap);
		return datas;
	}

	@Override
	public void deleteSysSpentTime(String appId) throws SQLException {
		executor.delete("deleteSysSpentTime", appId);
		
	}

	@Override
	public void insertSysSpentTime(List<SpentTime> sysSpentTime)
			throws SQLException {
		executor.insertBeans("insertSysSpentTime", sysSpentTime);
	}

	@Override
	public List<FunctionList> getFunctionListInDB() throws SQLException {
		List<FunctionList> datas = executor.queryList(FunctionList.class, "getFunctionListInDB");
		return datas;
	}

	@Override
	public void batchInsertFunctionList(List<FunctionList> excelDatas,List<FunctionList> DBDatas)throws SQLException {
		/**1.找到List中的所有项目名称
		 * 如果数据库中，有相应的appId，则放到map中，如果没有就生成新的appId
		*/
		Map<String,String> appNameIdMap = new HashMap<String,String> ();
		for(FunctionList bean:excelDatas){
			if(null==appNameIdMap||appNameIdMap.size()==0){
				appNameIdMap.put(bean.getAppName(), "") ;
			}else{
				boolean duplicate = false;
				if(appNameIdMap.containsKey(bean.getAppName())){
					duplicate = true;
				}
				if(!duplicate){
					appNameIdMap.put(bean.getAppName(), "") ;
				}
			}
			
		}
		//获得DB中最大的appId
		List<String> datas = executor.queryList(String.class, "getMaxAppId");
		if(null == datas ||datas.size()==0){
			int i=1;
			Set<String> appNameSet = appNameIdMap.keySet();
			Iterator<String> ite = appNameSet.iterator();
			while(ite.hasNext()){
				String appName = ite.next();
				appNameIdMap.put(appName, String.valueOf(i));
				insertApp(String.valueOf(i),String.valueOf(i),appName);
				i++;
				
			}
		}else{
			int i = Integer.parseInt(datas.get(0));
			Set<String> appNameSet = appNameIdMap.keySet();
			Iterator<String> ite = appNameSet.iterator();
			for(FunctionList bean:DBDatas){
				if(appNameIdMap.containsKey(bean.getAppName())){
					appNameIdMap.put(bean.getAppName(), bean.getAppId());
				}
			}
			while(ite.hasNext()){
				String appName = ite.next();
				String appId = appNameIdMap.get(appName);
				if(null == appId||"".equals(appId)){
					i++;
					appNameIdMap.put(appName, String.valueOf(i));
					insertApp(String.valueOf(i),String.valueOf(i),appName);
					
				}
				
			}
		}
		//往EXCEL数据中插入appId
		for(FunctionList bean:excelDatas){
			bean.setId(UUID.randomUUID().toString());
			bean.setAppId(appNameIdMap.get(bean.getAppName()));
		}
		executor.insertBeans("batchInsertFunctionList", excelDatas);
	}
	@Override
	public void testBatchInsertFunctionList(List<FunctionList> excelDatas)
			throws SQLException {
		executor.insertBeans("batchInsertFunctionList", excelDatas);
        CallableDBUtil callableUtil = new CallableDBUtil();
        callableUtil.prepareCallable("call clearfunctionlist_proc()");
        /*callableUtil.setString(1, expScope.getDeptId());
        callableUtil.setString(2, expScope.getExpType());
        callableUtil.setString(3, expScope.getYearMonth());
        callableUtil.registerOutParameter(4, java.sql.Types.INTEGER); */  
        callableUtil.executeCallable();
        int result = callableUtil.getInt(4);
	} 
	@Override
	public void insertApp(String autoId, String appId, String appName)
			throws SQLException {
		App bean = new App();
		bean.setAutoId(autoId);
		bean.setAppId(appId);
		bean.setAppName(appName);
		executor.insertBean("insertApp", bean);
	}

	@Override
	public List<FunctionList> getFunctionList(String appId) throws SQLException {
		List<FunctionList> datas = executor.queryList(FunctionList.class, "getFunctionList",appId);
		return datas;
	}

	@Override
	public void insertSysTime(List<FunctionList> sysSpentTime)
			throws SQLException {
executor.insertBeans("insertSysTime", sysSpentTime)	;	
	}

	@Override
	public void updateSysTime(List<FunctionList> datasForUpdate)
			throws SQLException {
		executor.updateBeans("updateSysTime", datasForUpdate);
	}

	  

		
	

}
