package com.frameworkset.platform.sysmgrcore.entity;

import java.io.Serializable;

public class MenuSee implements Serializable{
	private String opid;
	
	private String resTypeName;
	
	private String resName;
	
	private String roleName;

	public String getOpid() {
		return opid;
	}

	public void setOpid(String opid) {
		this.opid = opid;
	}

	public String getResName() {
		return resName;
	}

	public void setResName(String resName) {
		this.resName = resName;
	}

	public String getResTypeName() {
		return resTypeName;
	}

	public void setResTypeName(String resTypeName) {
		this.resTypeName = resTypeName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	
	
}
