package com.frameworkset.platform.cms.driver.jsp;

import com.frameworkset.platform.cms.driver.publish.Scriptlet;

public class CMSJspLoader implements java.io.Serializable {
	CMSRequestContext requestContext;
	public CMSJspLoader(CMSServletRequest request,CMSServletResponse reponse,Scriptlet scriptlet)
	{
		this.requestContext = requestContext;
	}
	
	

}
