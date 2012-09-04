package com.frameworkset.platform.cms.driver.context;

/**
 * 
 * <p>Title: com.frameworkset.platform.cms.driver.context.FTPConfig.java</p>
 *
 * <p>Description: 站点的ftp远程发布信息</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: iSany</p>
 * @Date 2007-1-20
 * @author biaoping.yin
 * @version 1.0
 */
public class FTPConfig implements java.io.Serializable{
	
	private String ftpip;
	private String port;
	private String ftpFolder;
	private String username;
	private String password;
	public String getFtpip() {
		return ftpip;
	}
	public FTPConfig( String ftpip,
						String port,
						String ftpFolder,
						String username,
						String password)
	{
		this.ftpip = ftpip;
		this.port = port;
		this.username = username;
		this.password = password;
		this.ftpFolder = ftpFolder;
	}
	public void setFtpip(String ftpip) {
		this.ftpip = ftpip;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getFtpFolder() {
		return ftpFolder;
	}
	public void setFtpFolder(String ftpFolder) {
		this.ftpFolder = ftpFolder;
	}

}
