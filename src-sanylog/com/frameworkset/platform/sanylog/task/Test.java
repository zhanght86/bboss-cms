package com.frameworkset.platform.sanylog.task;

import java.util.Calendar;

import org.apache.tools.ant.util.DateUtils;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Calendar today = Calendar.getInstance();
		
		String todayTime = DateUtils.format(today.getTime(), DateUtils.ISO8601_DATE_PATTERN);
		today.add(Calendar.DAY_OF_MONTH, -1);
		String yesterdayTime = DateUtils.format(today.getTime(), DateUtils.ISO8601_DATE_PATTERN);
		System.out.println("todayTime-------"+todayTime);
	
	System.out.println("yesterdayTime-------"+yesterdayTime);
	
	}

}
