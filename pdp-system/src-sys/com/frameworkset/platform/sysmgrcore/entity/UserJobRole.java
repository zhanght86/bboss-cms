package com.frameworkset.platform.sysmgrcore.entity;

import java.io.Serializable;

/**
 * 用户对应的岗位角色对象
 * @author gao.tang
 *
 */

public class UserJobRole  implements Serializable{

	private String roleId = null;
	
	private String roleName = null;
	
	private String orgName = null;
	
	private String jobName = null;
	
	private String roleType = null;

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	
}
