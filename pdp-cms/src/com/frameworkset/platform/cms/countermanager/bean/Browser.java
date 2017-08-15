package com.frameworkset.platform.cms.countermanager.bean;
/**
 * <p>
 * Title: Browser.java
 * </p>
 * 
 * <p>
 * Description:浏览统计计数器
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
 * @Date 2012-9-12 下午4:50:45
 * @author biaoping.yin
 * @version 1.0.0
 */
public class Browser {
	/**
	 * 总访问量
	 */
	private long total;
	/**
	 * 今日总访问量
	 */
	private long today;
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public long getToday() {
		return today;
	}
	public void setToday(long today) {
		this.today = today;
	}
	

}
