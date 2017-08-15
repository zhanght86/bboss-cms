package com.frameworkset.platform.security;

import java.util.Date;

/**
 * 
 * 
 * <p>Title: LineContext.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: bbossgroups</p>
 * @Date May 7, 2008 6:43:38 PM
 * @author biaoping.yin,尹标平
 * @version 1.0
 * @@org.jboss.cache.aop.AopMarker
 */
public class LineContext implements Comparable,java.io.Serializable{
	
	String ipaddress;

	String userName;

	Date loginTime;
	String sessionId;
	/**
	 * 客户端的mac地址
	 */
	String macaddr ;
	String DNSName;
//	HttpSession session;
	/**
	 * 判断用户是否是有效的用户
	 */
//	private HttpSession session;
	/**
	 * 服务器ip地址
	 */
	String serverIp ;
	/**
	 * 服务器端口
	 */
	String serverport;
	

	public LineContext(String sessionId,String userName, 
						String ipaddress, Date loginTime,
						String macaddr,String DNSName,
						String serverIp,String serverport) {
		this.loginTime = loginTime;
		this.ipaddress = ipaddress;
		this.userName = userName;
		this.sessionId = sessionId;
		this.macaddr = macaddr;
		this.DNSName = DNSName;
		this.serverIp = serverIp;
		this.serverport = serverport;
		
	}
	public LineContext()
	{
		
	}
	
	public String getSessionId()
	{
		return sessionId;
	}

	public String getIpaddress() {
		return ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public boolean isValid()
	{
//		//System.out.println(userName + " session :" + session);
//		if(session == null)
//			return false;
//		else
			return true;
	}
	
	public boolean equals(Object obj)
	{
		if(obj == null)
			return false;
		if(obj instanceof LineContext)
		{
			LineContext temp = (LineContext)obj;
			return temp.ipaddress.equals(this.ipaddress) && sessionId.equals(temp.getSessionId())&& this.userName.equals(temp.userName);
				
			
		}
		else
			return false;
	}

	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		if(this.equals(o))
			return 0;
		else
			return 1;
	}
	public String getMacaddr() {
		return macaddr;
	}
	public String getDNSName() {
		return DNSName;
	}
	public String getServerIp() {
		return serverIp;
	}
	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}
	public String getServerport() {
		return serverport;
	}
	public void setServerport(String serverport) {
		this.serverport = serverport;
	}

}