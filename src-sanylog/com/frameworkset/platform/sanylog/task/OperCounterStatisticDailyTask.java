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
        String week = String.valueOf(today.get(Calendar.WEEK_OF_YEAR));
		String todayTime = DateUtils.format(today.getTime(), DateUtils.ISO8601_DATE_PATTERN);
		week = todayTime.substring(0, 4)+"-"+week;
		Calendar startDate = Calendar.getInstance();
		int offset = startDate.get(Calendar.DAY_OF_WEEK);
		startDate.add(Calendar.DAY_OF_MONTH, offset - (offset * 2 - 1));
		String startTime =  DateUtils.format(startDate.getTime(), DateUtils.ISO8601_DATE_PATTERN);
		//按周统计
		counterManager.deleteOperCounterByWeek(week);
		counterManager.staticOperCounterByWeek(startTime,todayTime,week);
		//按天统计
		counterManager.deleteOperCounterByDay(todayTime);
		counterManager.staticOperCounterByDay(todayTime);
		
		//按月统计
		counterManager.deleteOperCounterByMonth(todayTime.substring(0, 7));
		counterManager.staticOperCounterByMonth(todayTime.substring(0, 7));
		
		//按年统计
		
		counterManager.deleteOperCounterByYear(todayTime.substring(0, 4));
		counterManager.staticOperCounterByYear(todayTime.substring(0, 4));
		
		
		
	}

}
