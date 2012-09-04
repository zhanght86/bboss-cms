package com.frameworkset.platform.cms.driver.jsp;

import com.frameworkset.platform.cms.driver.context.Context;
import com.frameworkset.platform.cms.driver.publish.PublishMode;

public interface JspletWindow extends java.io.Serializable {
	/**
	 * 获取当前jsp页面的上下文
	 * @return
	 */
	public String getContextPath();
	/**
	 * 获取jsp的唯一窗口标识，以便和内容管理系统交互
	 * @return
	 */
	public JspletWindowID getJspletWindowID();
	
	/**
	 * 获取jsp的名称
	 * @return
	 */
	public String getJspName();
	
	/**
	 * 获取jsp的发布模式
	 * @return
	 */
	public PublishMode getPublishMode();
	

	public Context getContext() ;

	public JspFile getJspFile();

}
