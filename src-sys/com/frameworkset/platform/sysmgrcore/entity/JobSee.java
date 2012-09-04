package com.frameworkset.platform.sysmgrcore.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class JobSee implements Serializable {
	private String userRealname;
	
	private String userName;
	
	private String jobName;
	
	private String orgName;
	
	private Timestamp jobStartTime;
	
	private Timestamp quashTime;
	
	private int fettle;

	public int getFettle() {
		return fettle;
	}

	public void setFettle(int fettle) {
		this.fettle = fettle;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public Timestamp getJobStartTime() {
		return jobStartTime;
	}

	public void setJobStartTime(Timestamp jobStartTime) {
		this.jobStartTime = jobStartTime;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public Timestamp getQuashTime() {
		return quashTime;
	}

	public void setQuashTime(Timestamp quashTime) {
		this.quashTime = quashTime;
	}

	public String getUserRealname() {
		return userRealname;
	}

	public void setUserRealname(String userRealname) {
		this.userRealname = userRealname;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
