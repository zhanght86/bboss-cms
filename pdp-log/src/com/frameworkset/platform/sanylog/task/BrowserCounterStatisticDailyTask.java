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

import java.util.Calendar;

import org.apache.tools.ant.util.DateUtils;

import com.frameworkset.platform.sanylog.service.CounterManager;

/**
 * @author gw_hel
 * 浏览计数统计每日任务
 *
 */
public class BrowserCounterStatisticDailyTask {

	private CounterManager counterManager;

	/**
	 * 浏览计数统计每日任务
	 * @throws Exception
	 */
	public void statisticBrowserCounterDailyJob() throws Exception {
        System.out.println("----------------定时任务被触发-----------------");
		Calendar today = Calendar.getInstance();
		today.add(Calendar.DAY_OF_MONTH, -1);

		String yesterdayTime = DateUtils.format(today.getTime(), DateUtils.ISO8601_DATE_PATTERN);

		// 浏览访问计数统计任务
		counterManager.statisticBrowserCounter(yesterdayTime, yesterdayTime, null);
		// 浏览器类型统计任务
		counterManager.statisticBrowserType(yesterdayTime, yesterdayTime, null);
		// 浏览器IP地址统计任务
		counterManager.statisticBrowserIP(yesterdayTime, yesterdayTime, null);
		
	}
}
