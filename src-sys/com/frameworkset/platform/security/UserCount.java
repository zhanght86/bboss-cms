package com.frameworkset.platform.security;

/**
 * 
 * 
 * <p>Title: UserCount.java</p>
 *
 * <p>Description: 用户统计计数器</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: 三一集团</p>
 * @Date May 11, 2008 5:20:17 PM
 * @author biaoping.yin,尹标平
 * @version 1.0
 * @@org.jboss.cache.aop.InstanceOfAopMarker
 */
public class UserCount implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1639094353355716793L;
	/**
	 * 计数器
	 */
	private int onlineuserCount = 0;
	public int getOnlineuserCount() {
		return onlineuserCount;
	}
	public void setOnlineuserCount(int onlineuserCount) {
		this.onlineuserCount = onlineuserCount;
	}
	
	

}
