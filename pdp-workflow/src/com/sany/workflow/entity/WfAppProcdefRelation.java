package com.sany.workflow.entity;

public class WfAppProcdefRelation {
	
	private String id;
	
	private String wf_app_id;
	
	private String procdef_id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
