package com.frameworkset.platform.sysmgrcore.entity;

import java.io.Serializable;


public class RoleResCopy implements Serializable{
	
   private String roleName;
   private String roleId;
   private String roleTypeName;
 
	
   public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleTypeName() {
		return roleTypeName;
	}

	public void setRoleTypeName(String roleTypeName) {
		this.roleTypeName = roleTypeName;
	}

	
	
}
