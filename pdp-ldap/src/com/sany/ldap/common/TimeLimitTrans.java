/*
 * @(#)TimeLimitTrans.java
 * 
 * Copyright @ 2001-2011 SANY Group Co.,Ltd.
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
package com.sany.ldap.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * 解析AD中时间表示方式
 * @author caix3
 * @since 2011-12-30
 */
public class TimeLimitTrans {

	private Logger logger = Logger.getLogger(this.getClass());
	
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * 根据入参换算成AD中时间表示方式，即从1601年开始的1/10000毫秒数
	 * @param date
	 * @return
	 */
	public String getAccountExpires(String date) {
    	
    	Date limitDate = null;
		Date baseDate = null;
		try {
			limitDate = simpleDateFormat.parse(date);
			baseDate = simpleDateFormat.parse("1600-12-31 08:00:00");
		} catch (ParseException e) {
			logger.error("Format date error", e);
		}
		
		return (limitDate.getTime() - baseDate.getTime()) * 10000 + "";
    }
	
	/**
	 * 解析AD时间表示形式成JAVA DATE
	 * @param temp
	 * @return
	 */
	public String parseADtime(String temp) {
		
		if (temp == null || temp.length() < 14) return "";
		
		String dateString = "";
		try {
			if (temp.contains("0Z")) {
				//第一种AD时间表示模式
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
				Calendar curr = Calendar.getInstance();
				curr.setTime(df.parse(temp.substring(0, temp.length() - 3)));
				//东8区加8个小时
				curr.set(Calendar.HOUR, curr.get(Calendar.HOUR) + 8);
				Date date = curr.getTime();
				dateString = simpleDateFormat.format(date);
			} else {
				//第二种AD时间表示模式
				Long dateLong = Long.parseLong(temp);
				dateLong = dateLong/10000 + simpleDateFormat.parse("1601-01-01 08:00:00").getTime();
				Date date = new Date(dateLong);
				dateString = simpleDateFormat.format(date);
			}
		} catch (ParseException e) {
			logger.error("parseADtime error", e);
		}
		return dateString;
	}
}
