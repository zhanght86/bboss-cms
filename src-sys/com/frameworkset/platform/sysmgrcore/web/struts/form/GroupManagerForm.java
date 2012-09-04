package com.frameworkset.platform.sysmgrcore.web.struts.form;

import java.io.Serializable;
import java.util.List;

import org.apache.struts.action.ActionForm;

/**
 * @author feng.jing
 * GroupManagerForm.java
 * Created on: Mar 20, 2006
 */
public class GroupManagerForm implements Serializable{
	public GroupManagerForm() {
    }
	private List allRole = null;
    private List existRole = null;
    private String[] roleId;
    private String groupId;
    private String userName;
    private String[] userIds;
    private String orgId;
    
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public List getAllRole() {
		return allRole;
	}
	public void setAllRole(List allRole) {
		this.allRole = allRole;
	}
	public List getExistRole() {
		return existRole;
	}
	public void setExistRole(List existRole) {
		this.existRole = existRole;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String[] getRoleId() {
		return roleId;
	}
	public void setRoleId(String[] roleId) {
		this.roleId = roleId;
	}
	public String[] getUserIds() {
		return userIds;
	}
	public void setUserIds(String[] userIds) {
		this.userIds = userIds;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
}
