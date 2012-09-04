package com.frameworkset.platform.cms.driver.publish.impl;


public class PublishCallBackImpl extends  BasePublishCallback implements java.io.Serializable {
	
	public Object getMessage() {
		return message;
	}
	
	public String getViewUrl()
	{
		return this.viewUrl;
	}
	
	public String getPageUrl()
	{
		return this.pageUrl;
	}
	
	

}
