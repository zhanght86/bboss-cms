package com.sany.sms;
/**
 * <p>
 * Title: Config.java
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: 三一集团
 * </p>
 * 
 * @Date 2013-1-25 下午2:56:19
 * @author biaoping.yin
 * @version 1.0.0
 */
public class Config {

	public Config() {
		// TODO Auto-generated constructor stub
	}
	private String user;
	private String password;
	private String smsurl;
	private String ORGEH_CODE;
	private String SMS_SUBCODE;
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSmsurl() {
		return smsurl;
	}
	public void setSmsurl(String smsurl) {
		this.smsurl = smsurl;
	}
	public String getORGEH_CODE() {
		return ORGEH_CODE;
	}
	public void setORGEH_CODE(String oRGEH_CODE) {
		ORGEH_CODE = oRGEH_CODE;
	}
	public String getSMS_SUBCODE() {
		return SMS_SUBCODE;
	}
	public void setSMS_SUBCODE(String sMS_SUBCODE) {
		SMS_SUBCODE = sMS_SUBCODE;
	}

}
