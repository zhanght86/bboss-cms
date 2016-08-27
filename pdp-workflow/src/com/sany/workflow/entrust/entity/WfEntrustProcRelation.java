package com.sany.workflow.entrust.entity;

public class WfEntrustProcRelation {

	private String id;
	
	private String entrust_id;
	
	private String procdef_id;
	
	private String procdef_name;
	
	private String business_name;
	
	private String wf_app_name;
	
	private String entrust_type;
	
	private String entrust_desc;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEntrust_id() {
		return entrust_id;
	}

	public void setEntrust_id(String entrust_id) {
		this.entrust_id = entrust_id;
	}

	public String getProcdef_id() {
		return procdef_id;
	}

	public void setProcdef_id(String procdef_id) {
		this.procdef_id = procdef_id;
	}

	public String getEntrust_type() {
		return entrust_type;
	}

	public void setEntrust_type(String entrust_type) {
		this.entrust_type = entrust_type;
	}

	public String getEntrust_desc() {
		return entrust_desc;
	}

	public void setEntrust_desc(String entrust_desc) {
		this.entrust_desc = entrust_desc;
	}
	
	public String getProcdef_name() {
		return procdef_name;
	}

	public void setProcdef_name(String procdef_name) {
		this.procdef_name = procdef_name;
	}

	public String getBusiness_name() {
		return business_name;
	}

	public void setBusiness_name(String business_name) {
		this.business_name = business_name;
	}

	public String getWf_app_name() {
		return wf_app_name;
	}

	public void setWf_app_name(String wf_app_name) {
		this.wf_app_name = wf_app_name;
	}
	
	
}


