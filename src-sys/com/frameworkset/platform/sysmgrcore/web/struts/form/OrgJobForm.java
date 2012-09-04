/*
 * Created on 2006-3-9
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.frameworkset.platform.sysmgrcore.web.struts.form;

import java.io.Serializable;

import org.apache.struts.action.ActionForm;

/**
 * @author ok
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class OrgJobForm  implements Serializable {
	
	private String id;

	private String[] jobId;

	private String orgId;

	private String[] allist;

	private String[] userList;
	
	private String curJobId;
	
	private String jobSn;
	
	private String[] roleId;  
	
	public String getJobSn() {
		return jobSn;
	}

	public void setJobSn(String jobSn) {
		this.jobSn = jobSn;
	}

	/**
	 * @return Returns the id.
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            The id to set.
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return Returns the jobId.
	 */
	public String[] getJobId() {
		return jobId;
	}

	/**
	 * @param jobId
	 *            The jobId to set.
	 */
	public void setJobId(String[] jobId) {
		this.jobId = jobId;
	}

	public String getOrgId() {
		return orgId;
	}

	/**
	 * @param orgId
	 *            The orgId to set.
	 */
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String[] getUserList() {
		return userList;
	}

	public void setUserList(String[] userList) {
		this.userList = userList;
	}

	public String[] getAllist() {
		return allist;
	}

	public void setAllist(String[] allist) {
		this.allist = allist;
	}

	public String getCurJobId() {
		return curJobId;
	}

	public void setCurJobId(String curJobId) {
		this.curJobId = curJobId;
	}
	
	public String[] getRoleId() {
		return roleId;
	}

	/**
	 * @param roleId
	 *            The roleId to set.
	 */
	public void setRoleId(String[] roleId) {
		this.roleId = roleId;
	}	
}
