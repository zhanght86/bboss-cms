package com.sany.workflow.business.entity;

import com.frameworkset.orm.annotation.Column;

/**
 * 流程表单暂存实体
 * 
 * @todo
 * @author tanx
 * @date 2014年9月23日
 * 
 */
public class FormCache {

	private String businessKey;// 业务单据号

	private String processKey;// 流程key

	@Column(type = "clob")
	private String formdata;// 表单暂存信息

	public String getBusinessKey() {
		return businessKey;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public String getProcessKey() {
		return processKey;
	}

	public void setProcessKey(String processKey) {
		this.processKey = processKey;
	}

	public String getFormdata() {
		return formdata;
	}

	public void setFormdata(String formdata) {
		this.formdata = formdata;
	}

}
