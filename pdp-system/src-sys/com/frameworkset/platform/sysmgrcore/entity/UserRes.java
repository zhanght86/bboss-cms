package com.frameworkset.platform.sysmgrcore.entity;

import java.io.Serializable;

public class UserRes implements Serializable{
	private String opId;
	private String opName;
	
	private String resName;
	
	private String roleName;
	
	private String resTypeName;
	
	
	public String getOpId() {
		return opId;
	}

	public void setOpId(String opId) {
		this.opId = opId;
	}



	public String getResName() {
		return resName;
	}

	public void setResName(String resName) {
		this.resName = resName;
	}



	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	public String getResTypeName() {
		return resTypeName;
	}

	public void setResTypeName(String resTypeName) {
		this.resTypeName = resTypeName;
	}

	public String getOpName() {
		return opName;
	}

	public void setOpName(String opName) {
		this.opName = opName;
	}
}
