package com.sany.workflow.webservice.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 统一代办任务响应实体
 * 
 * @todo
 * @author tanx
 * @date 2014年8月5日
 * 
 */
public class NoHandTaskResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private String resultCode;
	private String resultMess;
	private List<NoHandTaskInfo> dataList;

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

	public List<NoHandTaskInfo> getDataList() {
		return dataList;
	}

	public void setDataList(List<NoHandTaskInfo> dataList) {
		this.dataList = dataList;
	}

}
