package com.frameworkset.platform.security.service.entity;

import java.io.Serializable;

public class Result implements Serializable{
	public static final String ok = "success";
	public static final String fail = "failed";
	/**
	 * success
	 * fail
	 */
	private String code;
	private String errormessage;
	private CommonUser user; 
	private CommonOrganization org; 
	public CommonOrganization getOrg() {
		return org;
	}

	public void setOrg(CommonOrganization org) {
		this.org = org;
	}

	private String operationData;

	public Result() {
		// TODO Auto-generated constructor stub
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getErrormessage() {
		return errormessage;
	}

	public void setErrormessage(String errormessage) {
		this.errormessage = errormessage;
	}

	public CommonUser getUser() {
		return user;
	}

	public void setUser(CommonUser user) {
		this.user = user;
	}

	 

	public String getOperationData() {
		return operationData;
	}

	public void setOperationData(String operationData) {
		this.operationData = operationData;
	}

	public void appendErrormessage(String errormessage) {
		if(this.errormessage == null)
			this.errormessage = errormessage;
		else
			this.errormessage = this.errormessage +"\n"+ errormessage;
	}

	 

	 

}
