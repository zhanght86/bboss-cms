package com.frameworkset.platform.security.authentication;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.frameworkset.platform.security.authentication.CheckCallBack.Attribute;

public class CheckCallBackWrapper {
	private CheckCallBack checkCallBack;
	private HttpServletRequest request;
	private HttpServletResponse response;
	public CheckCallBackWrapper(CheckCallBack checkCallBack,HttpServletRequest request,HttpServletResponse response) {
		this.checkCallBack = checkCallBack;
		this.request = request;this.response = response;
	}
	public HttpServletRequest getRequest() {
		return request;
	}
	public HttpServletResponse getResponse() {
		return response;
	}
	
	 public Object getUserAttribute(String userAttribute)
	    {
	    	
	        return checkCallBack.getUserAttribute(userAttribute);
	    }
	    public Map<String,Attribute> getCallBacks()
	    {
	    	return checkCallBack.getCallBacks();
	    }
	    public void setUserAttribute(String userAttribute,Object value)
	    {
	    	checkCallBack.setUserAttribute(userAttribute,value);
	    }

//	    public AttributeQueue getAttributeQueue()
//	    {
//	        return this.list;
//	    }

	    public String getLoginModule() {
	        return checkCallBack.getLoginModule();
	    }

}
