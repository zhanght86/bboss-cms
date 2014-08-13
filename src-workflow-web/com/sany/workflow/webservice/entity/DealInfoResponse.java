package com.sany.workflow.webservice.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 处理记录
 * 
 * @todo
 * @author tanx
 * @date 2014年8月5日
 * 
 */
public class DealInfoResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private String resultCode;
	private String resultMess;
	private List<NodeTaskInfo> dataList;

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

	public List<NodeTaskInfo> getDataList() {
		return dataList;
	}

	public void setDataList(List<NodeTaskInfo> dataList) {
		this.dataList = dataList;
	}

}
