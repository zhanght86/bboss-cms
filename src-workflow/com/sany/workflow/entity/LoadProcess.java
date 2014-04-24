package com.sany.workflow.entity;

public class LoadProcess {
	private String processKey;
	private String processName;
	private String businessType;
	private String wf_app_id;
	private String procdef_id;
	
	public String getProcessKey() {
		return processKey;
	}
	public void setProcessKey(String processKey) {
		this.processKey = processKey;
	}
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	public String getBusinessType() {
		return businessType;
	}
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	public String getWf_app_id() {
		return wf_app_id;
	}
	public void setWf_app_id(String wf_app_id) {
		this.wf_app_id = wf_app_id;
	}
	public String getProcdef_id() {
		return procdef_id;
	}
	public void setProcdef_id(String procdef_id) {
		this.procdef_id = procdef_id;
	}

}
