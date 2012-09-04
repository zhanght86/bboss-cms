/*
 * 创建日期 2005-7-6
 *
 */
package com.frameworkset.platform.smtp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.Properties;
public class Constants implements Serializable{
	private static Properties configProperties=System.getProperties();
	static {
		System.out.println("come constants");
		int pos = 0;
		URL url = Constants.class.getResource("Constants.class");

		String pkgName = Constants.class.getPackage().getName();

		do {
			pos = pkgName.indexOf(".");
			if (pos > 0) {
				pkgName = pkgName.substring(0, pos) + "/" + pkgName.substring(pos + 1);
			}
		} while (pos > 0);

		String filePath = url.getFile();
		String webinfoPath = filePath.substring(0, filePath.indexOf("/classes/" + pkgName));
		String configPath=webinfoPath+"/classes/config";
		try {
			configProperties.load(new FileInputStream(configPath + "/mail.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    public static final String MAIL_USER = "user";
    public static final String popServer=configProperties.getProperty("popServer");// "localhost";
	public static final int popPort=Integer.parseInt(configProperties.getProperty("popPort","110"));
	public static final String damin =configProperties.getProperty("damin");// "@localhost";
	public static final String mailroot=configProperties.getProperty("mailroot","maildata");//"maildata";// 存放邮件的根目录
	public static final String path="mailpath";
	public static final String smtpServer=configProperties.getProperty("smtpServer");//"localhost";
	public static final long  fileSize=Long.parseLong(configProperties.getProperty("upfileSize","1048576"));
        //
        public static final long  onefileSize=Long.parseLong(configProperties.getProperty("onefileSize","1048576"));
	//群发默认Email，密码，是否需要认证
	public static final String oa_mail=configProperties.getProperty("oa_mail","oa"+damin);
	public static final String oa_mail_password=configProperties.getProperty("oa_mail_password","");
	public static final boolean auth=Boolean.valueOf(configProperties.getProperty("auth","false")).booleanValue();
	public static final String maxmailboxsize=configProperties.getProperty("maxmailboxsize");
       public static final String downloadpath=configProperties.getProperty("oa_downloadpath");

}
