/*
 * @(#)UserIdProduced.java
 * 
 * Copyright @ 2001-2011 SANY Group Co.,Ltd.
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
package com.frameworkset.platform.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class UserIdProduced {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * 中文转化为拼音
	 * @param displayName
	 * @return
	 */
	public String[][] getUserId(String displayName) {
		
		if (displayName == null || displayName.equals("")) {
			logger.info("Null input");
			return null;
		}
		
		//汉语拼音格式输出类，输出设置，大小写，音标方式等
		HanyuPinyinOutputFormat hanYuPinOutputFormat = new HanyuPinyinOutputFormat();
		hanYuPinOutputFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		hanYuPinOutputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		hanYuPinOutputFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
		
		char[] displayNameChar = displayName.toCharArray();
		String[][] userIdChar = new String[displayName.length()][];   
			
		for (int i = 0; i < displayNameChar.length; i ++) {
				
			char temp = displayNameChar[i];
			if (String.valueOf(temp).matches("[\\u4E00-\\u9FA5]+")) {
				
				try {
					userIdChar[i] = PinyinHelper.toHanyuPinyinStringArray(temp, hanYuPinOutputFormat);
				} catch(BadHanyuPinyinOutputFormatCombination e) {
					logger.error("UserID format error", e);
				}	
				
			} else if (((int) temp >= 65 && (int) temp <= 90)
					|| ((int) temp >= 97 && (int) temp <= 122)) {
				userIdChar[i] = new String[] { String.valueOf(temp) };
			} else {
				userIdChar[i] = new String[] { "" };
			}
		}
		
		return userIdChar;   
	}
}
