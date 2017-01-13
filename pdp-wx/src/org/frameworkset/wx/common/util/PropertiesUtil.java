/**
 * 
 */
package org.frameworkset.wx.common.util;

import java.util.Properties;

/**
 * @author suwei
 * @date 2017年1月6日
 *
 */
public class PropertiesUtil {
	private static final String SEARCH_URL_MAPPING_PROPERTIES = "weixin.properties";
	private static Properties props = new Properties();

	static {

		try {
			props.load(PropertiesUtil.class.getResourceAsStream(SEARCH_URL_MAPPING_PROPERTIES));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getUrl(String targetingSys) {
		return props.getProperty(targetingSys);
	}

}
