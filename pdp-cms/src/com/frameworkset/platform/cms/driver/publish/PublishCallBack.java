package com.frameworkset.platform.cms.driver.publish;

import java.util.List;

import com.frameworkset.platform.cms.driver.publish.impl.PublishMonitor;
/**
 * 
  * <p>Title: PublishCallBack</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 *
 * @author biaoping.yin
 * @version 1.0
 */
public interface PublishCallBack extends java.io.Serializable {
	/**
	 * 获取发布引擎接受任务情况信息
	 * @return
	 */
	public Object getMessage();
	
	/**
	 * 获取当前任务所产生预览页面地址
	 * @return
	 */
	public String getViewUrl();
	
	/**
	 * 获取当前任务产生的发布页面地址
	 * @return
	 */
	public String getPageUrl();
	
	/**
	 * 获取全部的监控信息
	 * @return
	 */
	public List getAllMonitorMessages();
	
	/**
	 * 获取所有成功执行的任务监控信息
	 * @return
	 */
	public List getAllSuccessMessages();
	
	
	/**
	 * 获取所有失败的任务监控信息
	 * @return
	 */
	public List getAllFailedMessages();
	
	/**
	 * 获取最新的任务监控信息
	 * @return
	 */
	public List getNewestMessages();
	
	public PublishMonitor getPublishMonitor();
	
	public void setPublishMonitor(PublishMonitor monitor);	

}
