package com.sany.mail;

import java.util.Properties;

import org.frameworkset.spi.InitializingBean;

/**
 * <p>
 * Title: MailConf.java
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
 * Company: bbossgroups
 * </p>
 * 
 * @Date 2013-5-20 下午4:57:05
 * @author biaoping.yin
 * @version 1.0.0
 */
public class MailConf implements InitializingBean{
	private String mailServerHost = "smtp.sany.com.cn";   
	private String mailServerPort = "25";
	private boolean validate = false;
	private Properties mailProperties;
	//邮件发送者的地址      
	private String fromAddress;   
	
	// 登陆邮件发送服务器的用户名和密码 
	private String userName;   
	private String password;   
	
	/**   
	 * 获得邮件会话属性 
	 */  
	private Properties getProperties() {   
		Properties p = new Properties();   
		p.put("mail.smtp.host", this.mailServerHost);   
		p.put("mail.smtp.port", this.mailServerPort);   
		p.put("mail.smtp.auth", validate ? "true" : "false");   
		//p.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");//gmail��Ҫ    
		return p;   
	}   
	public String getMailServerHost() {
		return mailServerHost;
	}
	public void setMailServerHost(String mailServerHost) {
		this.mailServerHost = mailServerHost;
	}
	public String getMailServerPort() {
		return mailServerPort;
	}
	public void setMailServerPort(String mailServerPort) {
		this.mailServerPort = mailServerPort;
	}
	public boolean isValidate() {
		return validate;
	}
	public void setValidate(boolean validate) {
		this.validate = validate;
	}
	@Override
	public void afterPropertiesSet() throws Exception {
		mailProperties = this.getProperties();
		
	}
	public Properties getMailProperties() {
		return mailProperties;
	}
	public String getFromAddress() {
		return fromAddress;
	}
	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}   

}
