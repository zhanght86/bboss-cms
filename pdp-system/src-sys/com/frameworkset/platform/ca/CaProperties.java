package com.frameworkset.platform.ca;

import org.frameworkset.spi.BaseSPIManager2;

/** 
 * <p>类说明:
 * 认证中心使用到的开关属性配置
 * </p>
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: bbossgroups</p>
 * @author  gao.tang 
 * @version V1.0  创建时间：Oct 23, 2009 11:45:18 AM 
 */
public class CaProperties {
	
	/**
	 * 是否使用数字认证中心服务
	 * 默认为false不使用
	 */
	public static final boolean CA_LOGIN_SERVER = BaseSPIManager2.getBooleanProperty("ca.login.server",false);
	
	public static final String RETURN_SPLIT = "^@^";
	public static final String RETURN_SPLIT_ = "\\^@\\^";

	public static final String PRINCIPAL_SPLIT = "^_^";
	public static final String PRINCIPAL_SPLIT_ = "\\^_\\^";

	public static final String IDENTITY_SPLIT = "#$#";
	public static final String IDENTITY_SPLIT_ = "#\\$#";

	public static final String PRINCIPAL_CREDENTIAL_SPLIT = "^|^";
	public static final String PRINCIPAL_CREDENTIAL_SPLIT_ = "\\^\\|\\^";
	
	public static void main(String[] args){
		String str = "1"+PRINCIPAL_CREDENTIAL_SPLIT+"2";
		System.out.println(str);
		System.out.println(str.split(PRINCIPAL_CREDENTIAL_SPLIT_).length);
	}

}
