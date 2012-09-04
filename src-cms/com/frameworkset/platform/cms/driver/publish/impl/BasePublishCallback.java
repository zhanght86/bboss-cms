package com.frameworkset.platform.cms.driver.publish.impl;

import java.util.List;

import com.frameworkset.platform.cms.driver.publish.PublishCallBack;

public abstract class BasePublishCallback implements PublishCallBack,java.io.Serializable{
	/**
	 * 发布反馈信息
	 */
	protected String message;
	
	/**
	 * 发布页面访问地址
	 */
	protected String pageUrl;
	
	private PublishMonitor monitor;
	
	/**
	 * 发布预览页面地址
	 */
	protected String viewUrl;
	
	protected void setMessage(Object message)
	{
		this.message = message.toString();
	}
	
	
	protected void setPageUrl(String pageUrl)
	{
		this.pageUrl = pageUrl;
	}
	
	protected void setViewUrl(String viewUrl)
	{
		this.viewUrl = viewUrl;
	}
	
	public void setPublishMonitor(PublishMonitor monitor)
	{
		this.monitor = monitor;
	}


	public PublishMonitor getPublishMonitor()
	{
//		monitor.isPublishCompleted();
//		monitor.isPublishEnded();
		
		return this.monitor;
	}


	public List getAllMonitorMessages() {
		
		return this.monitor.getAllMessages();
	}


	public List getAllSuccessMessages() {
		
		return this.monitor.getAllSuccessMessages();
	}


	public List getAllFailedMessages() {
		
		return this.monitor.getAllFailedMessages();
	}


	public List getNewestMessages() {
		
		return this.monitor.getNewestMessages();
	}	

}
