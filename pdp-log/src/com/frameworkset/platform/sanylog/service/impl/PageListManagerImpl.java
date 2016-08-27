package com.frameworkset.platform.sanylog.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.frameworkset.util.ClassWrapper;
 

import com.frameworkset.common.poolman.CallableDBUtil;
import com.frameworkset.common.poolman.ConfigSQLExecutor;
import com.frameworkset.common.poolman.SQLExecutor;
import com.frameworkset.platform.sanylog.bean.App;
import com.frameworkset.platform.sanylog.bean.FunctionList;
import com.frameworkset.platform.sanylog.bean.PageCounter;
import com.frameworkset.platform.sanylog.bean.PageList;
import com.frameworkset.platform.sanylog.service.PageListManager;
import com.frameworkset.util.ListInfo;

public class PageListManagerImpl implements PageListManager{
	private ConfigSQLExecutor executor;

	@Override
	public ListInfo showFunctionList(String appId, String functionCode,
			String functionName, int offset, int pagesize) throws SQLException {

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("appId", appId);
		paramMap.put("functionCode", functionCode);
		paramMap.put("functionName", functionName);

		ListInfo datas = executor.queryListInfoBean(PageList.class, "showPageList", offset, pagesize, paramMap);
		return datas;
	}

	@Override
	public List<App> getAllApp() throws SQLException {
		List<App> datas = executor.queryList(App.class, "getAllApp");
		return datas;
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
	public void deleteAllPageList() throws SQLException {
		executor.delete("deleteAllPageList");
	}

	@Override
	public void staticAllPageList() throws SQLException {
		List<PageList> datasInList = executor.queryList(PageList.class, "getPageList");//1.获得表TD_LOG_PAGE_SPENTTIME中的所有数据
		List<PageList> datasInCounts = executor.queryList(PageList.class, "staticAllPageList");//统计TD_LOG_BROWSER_COUNTS中的所有数据
		List<PageList> datasForInsert = new ArrayList<PageList> ();
		if(datasInCounts.size()>0){
			executor.delete("deleteAllPageList");//删除数据库TD_LOG_PAGE_SPENTTIME中的数据，准备插入新值
			for(PageList bean:datasInCounts){//遍历从基础表中读取的所有数据
				String appIdInCounts = (bean.getAppId()==null||"".equals(bean.getAppId()))?"":bean.getAppId();
				String appNameInCounts = (bean.getAppName()==null||"".equals(bean.getAppName()))?"":bean.getAppName();
				String functionNameInCounts = (bean.getFunctionName()==null||"".equals(bean.getFunctionName()))?"":bean.getFunctionName();
				boolean duplicate = false;
				if(datasInList.size()>0){
					for(PageList bean_InList:datasInList){//遍历pagelist表中读取的所有数据
						String appIdInList = (bean_InList.getAppId()==null||"".equals(bean_InList.getAppId()))?"":bean_InList.getAppId();
						String appNameInList = (bean_InList.getAppName()==null||"".equals(bean_InList.getAppName()))?"":bean_InList.getAppName();
						String functionNameInList = (bean_InList.getFunctionName()==null||"".equals(bean_InList.getFunctionName()))?"":bean_InList.getFunctionName();
						String functionCodeInList = (bean_InList.getFunctionCode()==null||"".equals(bean_InList.getFunctionCode()))?"":bean_InList.getFunctionCode();
						if(appIdInCounts.equals(appIdInList)&&appNameInCounts.equals(appNameInList)&&functionNameInCounts.equals(functionNameInList)){
							duplicate = true;//即存在基础表中，也存在pagelist表中
							
							if(!"".equals(appNameInList)&&!"".equals(functionCodeInList)){//原来存在TD_LOG_PAGE_SPENTTIME表中的数据，去jira里面获取工时（只有它才有功能编码）
								Map<String, Object> paramMap = new HashMap<String, Object>();
								paramMap.put("appName", appNameInList);
								paramMap.put("functionCode", functionCodeInList);
								List<Float> spentTime = executor.queryListBeanWithDBName(Float.class, "jira","getSpentTime", bean);
								float spent = 0;
								if(spentTime.size()>0){
									for(int i=0;i<spentTime.size();i++){
										spent = spent + spentTime.get(i);
									}
								}
								bean_InList.setTimeSpent(spent);//同步工时放入bean中
							}
							
							datasForInsert.add(bean_InList);//放入待插入的表中
							datasInList.remove(bean_InList);//从数据库中的读取的数据移除
							break;
						}
						
					}
				}
				if(!duplicate){//本条数据不在page_list中，但是td_log_browser_counts中有
					bean.setId(UUID.randomUUID().toString());
					datasForInsert.add(bean);
				}
			}
		}
		if(datasInList.size()>0){//本条数据在page_list中，但是td_log_browser_counts中没有
			executor.insertBeans("insertPageList", datasInList);	
		}
		
		if(datasForInsert.size()>0){//插入值
		      executor.insertBeans("insertPageList", datasForInsert);			
		}
		
	}

	@Override
	public List<PageList> getSinglePageList(String id) throws SQLException {
		List<PageList> datas = executor.queryList(PageList.class, "getSinglePageList", id);
		return datas;
	}

	@Override
	public void updatePageList(String id, String functionCode)
			throws SQLException {
		PageList bean = new PageList();
		bean.setId(id);
		bean.setFunctionCode(functionCode);
		executor.updateBean("updatePageList", bean);
	}

	@Override
	public List<PageList> getExcelDataByAppId(String appId) throws SQLException {
		List<PageList> datas = executor.queryList(PageList.class, "getExcelDataByAppId", appId);
		return datas;
	}

	@Override
	public List<String> getAppNameById(String appId)  throws SQLException {
		List<String> datas = executor.queryList(String.class, "getAppNameById", appId);
		return datas;
	}

	@Override
	public void updateBatchPageList(List<PageList> dataGroupList, String appId)
			throws SQLException {
		for(PageList bean:dataGroupList){
			bean.setAppId(appId);
		}
		executor.updateBeans("updateBatchPageList", dataGroupList);
		
	}

	@Override
	public List<PageCounter> showPageCounterList(String startTime, String endTime) throws SQLException {
		List<App> apps = executor.queryList(App.class, "getAllApp");
		Map<String, Object> paramMapPages = new HashMap<String, Object>();
		paramMapPages.put("startTime", startTime);
		paramMapPages.put("endTime", endTime);
		List<PageCounter> pages =  executor.queryListBean(PageCounter.class, "getPages", paramMapPages);
		List<PageCounter> datas = new ArrayList<PageCounter> ();
		if(apps.size()<=0){
			return null;
		}
		for(App app:apps){
			String appName = app.getAppName()==null?"":app.getAppName();
			PageCounter bean = new PageCounter();
			bean.setAppName(appName);
			boolean exist = false;
			for(PageCounter page:pages){
				//System.out.println("appName----"+appName+"----page.getAppName()------"+page.getAppName());
				if(appName.equals(page.getAppName())){
					String pagesBean = page.getPages()==null ||"".equals(page.getPages())?"0":page.getPages();
					String pagesActBean = page.getPagesActual()==null ||"".equals(page.getPagesActual())?"0":page.getPagesActual();
					bean.setPages(pagesBean);
					bean.setPagesActual(pagesActBean);
					exist = true;
				}
				if(exist){
					break;
				}
			}
			datas.add(bean);
		}
        String [] scopes = {"0-1","2-5","6-10","11-20","21-35","36-50","51-1000"};
        String [] params = {"scope1","scope2","scope3","scope4","scope5","scope6","scope7"};
        for(int i=0;i<scopes.length;i++){
        	Map<String, Object> paramMap = new HashMap<String, Object>();
    		paramMap.put("startTime", startTime);
    		paramMap.put("endTime", endTime);
    		paramMap.put("start", scopes[i].split("-")[0]);
    		paramMap.put("end", scopes[i].split("-")[1]);
    		List<PageCounter> temp = executor.queryListBean(PageCounter.class, "showPageCounterList", paramMap);
    		for(PageCounter bean :datas){
    			String appName = bean.getAppName();
    			boolean exist = false ; 
    			if(temp.size()>0){
    				for(PageCounter tempBean :temp){
            			String appNameTemp = tempBean.getAppName();
            			if(appName.equals(appNameTemp)){
            				ClassWrapper beanWrapper = new ClassWrapper(bean);
            				beanWrapper.setPropertyValue(params[i],tempBean.getScope());
            				exist = true;
            			}
            		}
    			}
    			if(!exist){
    				ClassWrapper beanWrapper = new ClassWrapper(bean);
    				beanWrapper.setPropertyValue(params[i],"0");
    			}
    		}
        }
		

		
		return datas;
	}

	@Override
	public List<PageCounter> getMaintainPage() throws SQLException {
		executor.insert("insertMaintainPage");
		List<PageCounter> datas = executor.queryList(PageCounter.class, "getMaintainPage");
		return datas;
	}

	@Override
	public void savePages(String appName, String pages) throws SQLException {
		PageCounter bean = new PageCounter();
		bean.setAppName(appName);
		bean.setPages(pages);
		executor.updateBean("savePages", bean);
		
	}

	  

		
	

}
