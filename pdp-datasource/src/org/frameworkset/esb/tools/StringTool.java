package org.frameworkset.esb.tools;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * 字符串工具类
 *<p>
 * Title:StringTool.java
 * </p>
 *<p>
 * Description:
 * </p>
 *<p>
 * Copyright:Copyright (c) 2010
 * </p>
 *<p>
 * Company:湖南科创
 * </p>
 * 
 * @author 刘剑峰
 *@version 1.0 2011-4-13
 */
public class StringTool {

	
	/**
	 * 构建模糊查询字符串，如果字符串有内容，则在前后加上%
	 * @param str
	 * @param upper 是否转换成大写
	 * @return
	 */
	public static String buildFuzzySearchString(String str, boolean upper){
		if (StringTool.isStrNull(str)) {
			return str;
		} else {
			StringBuilder stringBuilder = new StringBuilder();
			str = str.trim();
			if (upper){
				str = str.toUpperCase();
			}
			stringBuilder.append("%").append(str).append("%");
			return stringBuilder.toString();
		}
	}		

	/**
	 * 判断字符串是否没有内容
	 * @param str
	 * @return
	 */
	public static boolean isStrNull(String str) {
		return str == null || str.trim().length() == 0;
	}

	/**
	 * 判断字符串数组是否有元素
	 * @param strArr
	 * @return
	 */
	public static boolean isStrArrayNull(String[] strArr) {
		return strArr == null || strArr.length == 0;
	}

	/**
	 * 判断Map是否有元素
	 * @param map
	 * @return
	 */
	public static boolean isMapNull(Map map) {
		return map == null || map.isEmpty();
	}

	/**
	 * 判断List是否有元素
	 * @param list
	 * @return
	 */
	public static boolean isListNull(List list) {
		return list == null || list.isEmpty();
	}

	/**
	 * 替换掉非法字符成空
	 * 
	 * @param string
	 *            待替换的字符串
	 * @return 替换后的字符串
	 */
	public static String replaceHTMLTag(String string) {
		if (string == null || string.trim().equals(""))
			return string;
		return string.replaceAll("[<>\\'()&*\\/]", "").replaceAll("or", "")
				.replaceAll("and", "").replaceAll("delete", "").replaceAll(
						"<script>", "").replaceAll("</script>", "").replaceAll(
						"from", "").replaceAll("update", "").replaceAll("drop",
						"").replaceAll("replace", "").replaceAll("alter", "")
				.replaceAll("create", "");
	}

	/**
	 * 字符串编码转换，如果转码失败则返回原字符串
	 * 
	 * @param str
	 *            待转码的字符串
	 * @param oldCharasetCode
	 *            旧字符编码
	 * @param newCharasetCode
	 *            新字符编码
	 * @return 返回转码后的字符串
	 */
	public static String charactorCodeConversion(String str,
			String oldCharasetCode, String newCharasetCode) {
		String tempStr;
		try {
			tempStr = new String(str.getBytes(oldCharasetCode), newCharasetCode);
		} catch (UnsupportedEncodingException e) {
			tempStr = str;
		}
		return tempStr;
	}

	/**
	 * 将Null替换成空字符串
	 * 
	 * @param str
	 * @return
	 */
	public static String replaceNull(String str) {
		return str == null ? "" : str;
	}

}
