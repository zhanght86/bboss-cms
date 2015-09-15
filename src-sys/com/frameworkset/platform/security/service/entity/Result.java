package com.frameworkset.platform.security.service.entity;

import java.io.Serializable;

public class Result<T> implements Serializable{
	public static final String ok = "success";
	public static final String fail = "failed";
	/**
	 * success
	 * fail
	 */
	private String code;
	private String errormessage;
	private Serializable data; 

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

	 

	public T getData() {
		return (T)data;
	}

	public void setData(Serializable data) {
		this.data = data;
	}

}
