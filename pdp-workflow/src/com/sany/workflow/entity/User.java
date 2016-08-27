package com.sany.workflow.entity;

public class User {

	private String user_id;
	
	private String user_name;
	
	private String user_realname;
	
	private String user_worknumber;
	
	private String org_id;
	
	private String org_name;
	
	private String job_name;
	private boolean alluser;

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getUser_realname() {
		return user_realname;
	}

	public void setUser_realname(String user_realname) {
		this.user_realname = user_realname;
	}

	public String getUser_worknumber() {
		return user_worknumber;
	}

	public void setUser_worknumber(String user_worknumber) {
		this.user_worknumber = user_worknumber;
	}

	public String getOrg_id() {
		return org_id;
	}

	public void setOrg_id(String org_id) {
		this.org_id = org_id;
	}

	public String getOrg_name() {
		return org_name;
	}

	public void setOrg_name(String org_name) {
		this.org_name = org_name;
	}

	public String getJob_name() {
		return job_name;
	}

	public void setJob_name(String job_name) {
		this.job_name = job_name;
	}

	public boolean isAlluser() {
		return alluser;
	}

	public void setAlluser(boolean alluser) {
		this.alluser = alluser;
	}
}
