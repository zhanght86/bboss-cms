package com.frameworkset.platform.sanylog.task;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import org.apache.tools.ant.util.DateUtils;

import com.frameworkset.platform.sanylog.service.CounterManager;

public class OperCounterStatisticDailyTask {
	private CounterManager counterManager;
	public void statisticOperCounterDailyJob() throws SQLException{
        Calendar today = Calendar.getInstance();
		
		String todayTime = DateUtils.format(today.getTime(), DateUtils.ISO8601_DATE_PATTERN);
		
		/*List<String> siteList = counterManager.getSiteList();
		for(int i=0;i<siteList.size();i++){
			
		}*/
		//按天统计
		counterManager.staticOperCounterByDay(todayTime);
		
		//按月统计
		counterManager.deleteOperCounterByMonth(todayTime.substring(0, 7));
		counterManager.staticOperCounterByMonth(todayTime.substring(0, 7));
		
		//按年统计
		
		counterManager.deleteOperCounterByYear(todayTime.substring(0, 4));
		counterManager.staticOperCounterByYear(todayTime.substring(0, 4));
		
		
		
	}

}
