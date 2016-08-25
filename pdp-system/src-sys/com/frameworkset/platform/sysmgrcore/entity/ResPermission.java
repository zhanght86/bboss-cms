package com.frameworkset.platform.sysmgrcore.entity;

import java.util.Date;

public class ResPermission {

	private String remark5 = null;
	
	private String userName = null;
	
	private String userId = null;
	
	private String userRealname = null;
	
	private String opId = null;
	
	private String opName = null;
	
	private String resResource = null;
	
	private Date sDate = null;
	
	private String roleName = null;
	
	private String roleTypeName = null;
	
	private String jobName = null;
	
	private String auto = "0";

	public String getAuto() {
		return auto;
	}

	public void setAuto(String auto) {
		this.auto = auto;
	}

	public String getRemark5() {
		return remark5;
	}

	public void setRemark5(String remark5) {
		this.remark5 = remark5;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserRealname() {
		return userRealname;
	}

	public void setUserRealname(String userRealname) {
		this.userRealname = userRealname;
	}

	public String getOpId() {
		return opId;
	}

	public void setOpId(String opId) {
		this.opId = opId;
	}

	public String getOpName() {
		return opName;
	}

	public void setOpName(String opName) {
		this.opName = opName;
	}

	public String getResResource() {
		return resResource;
	}

	public void setResResource(String resResource) {
		this.resResource = resResource;
	}

	public Date getSDate() {
		return sDate;
	}

	public void setSDate(Date date) {
		sDate = date;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleTypeName() {
		return roleTypeName;
	}

	public void setRoleTypeName(String roleTypeName) {
		this.roleTypeName = roleTypeName;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
