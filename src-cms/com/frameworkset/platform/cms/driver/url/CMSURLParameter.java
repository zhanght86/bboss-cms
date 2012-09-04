package com.frameworkset.platform.cms.driver.url;

public class CMSURLParameter implements java.io.Serializable {
	String windowId;
	String paramName;
	String[] values;
	public CMSURLParameter(String windowId, String paramName, String[] paramValues) {
		this.windowId = windowId;
		this.paramName = paramName;
		this.values = paramValues;
	}

	public String[] getValues() {
		
		return this.values;
	}

	public String getName() {
		
		return this.paramName;
	}

	public String getWindowId() {
		
		return this.windowId;
	}

}
