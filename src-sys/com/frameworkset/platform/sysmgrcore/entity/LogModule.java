package com.frameworkset.platform.sysmgrcore.entity;

import java.io.Serializable;

public class LogModule implements Serializable{
	  private String id;
	  private String LOGMODULE;
	  private String STATUS;
	  private String MODULE_DESC;
	public String getLOGMODULE() {
		return LOGMODULE;
	}
	public void setLOGMODULE(String logmodule) {
		LOGMODULE = logmodule;
	}
	public String getMODULE_DESC() {
		return MODULE_DESC;
	}
	public void setMODULE_DESC(String module_desc) {
		MODULE_DESC = module_desc;
	}
	public String getSTATUS() {
		return STATUS;
	}
	public void setSTATUS(String status) {
		STATUS = status;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	  
}
