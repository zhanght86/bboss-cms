/*
 * @(#)BrowserCounterStatisticDailyTask.java
 * 
 * Copyright @ 2001-2012 SANY Group Co.,Ltd.
 * All right reserved.
 * 
 * 这个软件是属于三一集团有限公司机密的和私有信息，不得泄露。
 * 并且只能由三一集团有限公司内部员工在得到许可的情况下才允许使用。
 * This software is the confidential and proprietary information
 * of SANY Group Co, Ltd. You shall not disclose such
 * Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with
 * SANY Group Co, Ltd.
 */
package com.frameworkset.platform.sanylog.task;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import org.apache.tools.ant.util.DateUtils;

import com.frameworkset.platform.sanylog.bean.App;
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
				List<SpentTime> sysSpentTime = functionListManager.staticSpentTimeByAppName(appName,appId);
				if(null!=sysSpentTime&&sysSpentTime.size()>0){
					functionListManager.deleteSysSpentTime(appId);
					functionListManager.insertSysSpentTime(sysSpentTime);
				}
				
			}
			System.out.println("统计成功");
		}catch(SQLException e){
			System.out.println("统计失败"+e.getMessage());
		}
		
	}
}
