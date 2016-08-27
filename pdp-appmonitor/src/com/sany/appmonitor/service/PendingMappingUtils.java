package com.sany.appmonitor.service;

import java.util.Properties;

/**
 * 读取应用系统代办地址配置文件
 * @author caix3
 * @since 2011-12-02
 */
public class PendingMappingUtils {

	private static final String SEARCH_URL_MAPPING_PROPERTIES = "pending.properties";
	private static Properties props = new Properties();
	
	static {

		try {
			props.load(PendingMappingUtils.class.getResourceAsStream(SEARCH_URL_MAPPING_PROPERTIES));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getUrl(String targetingSys) {

		return props.getProperty(targetingSys);
	}

}
