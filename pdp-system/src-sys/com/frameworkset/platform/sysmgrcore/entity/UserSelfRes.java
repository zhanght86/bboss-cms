package com.frameworkset.platform.sysmgrcore.entity;

import java.io.Serializable;

public class UserSelfRes implements Serializable{
	private String opId;
	private String opName;
	
	private String resName;
	
	private String userName;
	
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
