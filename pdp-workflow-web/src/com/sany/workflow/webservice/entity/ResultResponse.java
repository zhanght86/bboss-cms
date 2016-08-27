package com.sany.workflow.webservice.entity;

import java.io.Serializable;

/**
 * 结果响应
 * 
 * @todo
 * @author tanx
 * @date 2014年8月6日
 * 
 */
public class ResultResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	private String resultCode;// 返回代码
	private String resultMess;// 返回提示信息

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMess() {
		return resultMess;
	}

	public void setResultMess(String resultMess) {
		this.resultMess = resultMess;
	}

}
