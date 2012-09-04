package com.frameworkset.platform.ldap;

public class AuthRole implements java.io.Serializable {
	private String roleid = "";
	private String rolename = "";
	private String rolesireid = "";
	private String rolediscribe = "";
	public String getRolediscribe() {
		return rolediscribe;
	}
	public void setRolediscribe(String rolediscribe) {
		this.rolediscribe = rolediscribe;
	}
	public String getRoleid() {
		return roleid;
	}
	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}
	public String getRolename() {
		return rolename;
	}
	public void setRolename(String rolename) {
		this.rolename = rolename;
	}
	public String getRolesireid() {
		return rolesireid;
	}
	public void setRolesireid(String rolesireid) {
		this.rolesireid = rolesireid;
	}
}
