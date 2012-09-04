package com.frameworkset.platform.ldap;

public class AuthUser implements java.io.Serializable {
	private String uid = "";
	private String username = "";
	private String userpsw = "";
	private String personid = "";
	private String roleid = "";
	private String userremark = "";
	public String getPersonid() {
		return personid;
	}
	public void setPersonid(String personid) {
		this.personid = personid;
	}
	public String getRoleid() {
		return roleid;
	}
	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUserpsw() {
		return userpsw;
	}
	public void setUserpsw(String userpsw) {
		this.userpsw = userpsw;
	}
	public String getUserremark() {
		return userremark;
	}
	public void setUserremark(String userremark) {
		this.userremark = userremark;
	}
}
