/*
 * @(#)BrowserCounterStatisticDailyTask.java
 * 
 * Copyright @ 2001-2012 SANY Group Co.,Ltd.
 * All right reserved.
 * 
 * 这个软件是属于bbossgroups有限公司机密的和私有信息，不得泄露。
 * 并且只能由bbossgroups有限公司内部员工在得到许可的情况下才允许使用。
 * This software is the confidential and proprietary information
 * of SANY Group Co, Ltd. You shall not disclose such
 * Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with
 * SANY Group Co, Ltd.
 */
package com.frameworkset.platform.sanylog.task;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import org.apache.tools.ant.util.DateUtils;

import com.frameworkset.platform.sanylog.bean.App;
import com.frameworkset.platform.sanylog.bean.FunctionList;
import com.frameworkset.platform.sanylog.bean.SpentTime;
import com.frameworkset.platform.sanylog.service.CounterManager;
import com.frameworkset.platform.sanylog.service.FunctionListManager;

/**
 * @author gw_hel
 * 浏览计数统计每日任务
 *
 */
public class SpentTimeStaticTask {

	private FunctionListManager functionListManager;

	/**
	 * 浏览计数统计每日任务
	 * @throws Exception
	 */
	public void autoStaticSpentTimeJob() throws Exception {

		try{
			/**
			 * 1.在日志系统库中找到所有的项目名称
			 * 2.用相应的项目名称在jira里面统计工时
			 * 3.结果插入日志系统的表
			*/
			List<App> datas = functionListManager.getAllApp();
			for(App bean:datas){
				String appName = bean.getAppName();
				String appId = bean.getAppId();
				List<FunctionList> sysSpentTime = functionListManager.staticSpentTimeByAppName(appName,appId);
				if(null!=sysSpentTime&&sysSpentTime.size()>0){
					//functionListManager.deleteSysSpentTime(appId);
					//functionListManager.insertSysSpentTime(sysSpentTime);
					for(FunctionList sysTime:sysSpentTime){
						sysTime.setId(UUID.randomUUID().toString());
					}
					List<FunctionList> datasInDB = functionListManager.getFunctionList(appId);
					if(datasInDB.size()>0){
						List<FunctionList> datasForInsert = new ArrayList<FunctionList> ();//存放td_log_functionlist表里面没有的
						List<FunctionList> datasForUpdate = new ArrayList<FunctionList> ();//存放td_log_functionlist表里面有，但是时间更新了的
						for(FunctionList sysTime:sysSpentTime){
							String status = "notequal";
							for(FunctionList beanInDB:datasInDB){
								String result = sysTime.equal(beanInDB);
								if("notequal".equals(result)){
									
								}else if("timediff".equals(result)){
									 datasForUpdate.add(beanInDB);
									 status = "timediff";
									 break;
								}else if("equal".equals(result)){
									status = "equal";
									 break;
								}
							}
							if("notequal".equals(status)){
								datasForInsert.add(sysTime);
							}
						}
						functionListManager.insertSysTime(datasForInsert);
						functionListManager.updateSysTime(datasForUpdate);
					}else{
						functionListManager.insertSysTime(sysSpentTime);
					}
				}
				
			}
			System.out.println("统计成功");
		}catch(SQLException e){
			System.out.println("统计失败"+e.getMessage());
		}
		
	
	}
}
/*
		try{
			
			List<App> datas = functionListManager.getAllApp();
			for(App bean:datas){
				String appName = bean.getAppName();
				String appId = bean.getAppId();
				List<FunctionList> sysSpentTime = functionListManager.staticSpentTimeByAppName(appName,appId);
				if(null!=sysSpentTime&&sysSpentTime.size()>0){
					//functionListManager.deleteSysSpentTime(appId);
					//functionListManager.insertSysSpentTime(sysSpentTime);
					for(FunctionList sysTime:sysSpentTime){
						sysTime.setId(UUID.randomUUID().toString());
					}
					List<FunctionList> datasInDB = functionListManager.getFunctionList(appId);
					if(datasInDB.size()>0){
						List<FunctionList> datasForInsert = new ArrayList<FunctionList> ();//存放td_log_functionlist表里面没有的
						List<FunctionList> datasForUpdate = new ArrayList<FunctionList> ();//存放td_log_functionlist表里面有，但是时间更新了的
						for(FunctionList sysTime:sysSpentTime){
							for(FunctionList beanInDB:datasInDB){
								String result = sysTime.equal(beanInDB);
								if("notequal".equals(result)){
									datasForInsert.add(sysTime);
								}else if("timediff".equals(result)){
									 datasForUpdate.add(beanInDB);
								}
							}
						}
						functionListManager.insertSysTime(datasForInsert);
						functionListManager.updateSysTime(datasForUpdate);
					}else{
						functionListManager.insertSysTime(sysSpentTime);
					}
				}
				
			}
			System.out.println("统计成功");
		}catch(SQLException e){
			System.out.println("统计失败"+e.getMessage());
		}
		
	
*/