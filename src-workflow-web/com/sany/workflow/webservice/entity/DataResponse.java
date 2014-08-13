package com.sany.workflow.webservice.entity;

import java.io.Serializable;

public class DataResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	private String resultCode;// 返回代码
	private String resultData;// 返回数据
	private String resultMess;// 返回提示信息

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultData() {
		return resultData;
	}

	public void setResultData(String resultData) {
		this.resultData = resultData;
	}

	public String getResultMess() {
		return resultMess;
	}

	public void setResultMess(String resultMess) {
		this.resultMess = resultMess;
	}

}
