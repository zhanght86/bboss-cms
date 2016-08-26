package com.frameworkset.platform.cms.driver.publish.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import com.frameworkset.platform.cms.driver.jsp.CMSRequestContext;
import com.frameworkset.platform.security.AccessControl;

public class WEBPublish extends APPPublish {
//	private HttpServletRequest request;
//	private HttpServletResponse response;
	
	public void init(HttpServletRequest request,
					 HttpServletResponse response,
					 PageContext pageContext,
					 AccessControl accessControl)
	{
//		this.request = request;
//		this.response = response;
		this.requestContext = new CMSRequestContext(pageContext,
													request,
													response);
		super.accessControl = accessControl;
		
	}
	
	public void init(HttpServletRequest request,
			 HttpServletResponse response,
			 PageContext pageContext)
{
//this.request = request;
//this.response = response;
this.requestContext = new CMSRequestContext(pageContext,
											request,
											response);


}
	
	
	

}
