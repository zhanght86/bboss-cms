package com.frameworkset.platform.ldap;

import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 资源文件读取
 * @author 朱杰
 *2005年11月10日
 */
public class PropertiesReader implements java.io.Serializable {
	static Log log = LogFactory.getLog(PropertiesReader.class);

	/**
	 * 资源文件读取
	 * @param filename
	 * @param key
	 * @return		未找到文件或不存在对应的key时都返回""
	 */
	public static String read(String filename,String key){
		String ret = "";
		ResourceBundle messages = ResourceBundle.getBundle(filename);
		if(messages==null){
			log.debug("配置文件：" + filename + "未找到！");
			return "";
		}
		try{
			ret = messages.getString(key).trim();
		}catch(Exception e){
			log.debug("在配置文件：" + filename + "未找到key：" + key + "！");
			return "";
		}
		return ret;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
			System.out.println(read("sinomail","mail.host"));
	}

}
