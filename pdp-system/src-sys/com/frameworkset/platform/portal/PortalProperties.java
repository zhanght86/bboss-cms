package com.frameworkset.platform.portal;

import java.io.File;

import org.frameworkset.spi.BaseSPIManager2;

/** 
 * <p>类说明:portal相关的配置属性</p>
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: bbossgroups</p>
 * @author  gao.tang 
 * @version V1.0  创建时间：Oct 19, 2009 9:42:17 AM 
 */
public class PortalProperties {
	
	/**
	 * 应用部署的目录
	 */
	public static final String APPROOT = BaseSPIManager2.getProperty("approot"); 
	
	/**
	 * portal iframe模板文件发布目录路径
	 */
	public static final String PORTAL_ISSUEPATH_WAR = BaseSPIManager2.getProperty("portal.issuepath.war", 
			APPROOT+"/portal/issue_war");
	
	static{
		File file = new File(PORTAL_ISSUEPATH_WAR);
		if(!file.exists()){
			file.mkdirs();
		}
	}

}
