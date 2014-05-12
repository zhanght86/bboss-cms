package com.sany.workflow.entrust.entity;

import java.sql.Timestamp;

public class WfEntrust {
	
	private String id;
	
	private String entrust_user;
	
	private Timestamp start_date;
	
	private Timestamp end_date;
	
	private Timestamp create_date;
	
	private String create_user;
	
	private String sts;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEntrust_user() {
		return entrust_user;
	}

	public void setEntrust_user(String entrust_user) {
		this.entrust_user = entrust_user;
	}

	public Timestamp getStart_date() {
		return start_date;
	}

	public void setStart_date(Timestamp start_date) {
		this.start_date = start_date;
	}

	public Timestamp getEnd_date() {
		return end_date;
	}

	public void setEnd_date(Timestamp end_date) {
		this.end_date = end_date;
	}

	public Timestamp getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Timestamp create_date) {
		this.create_date = create_date;
	}

	public String getCreate_user() {
		return create_user;
	}

	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}

	public String getSts() {
		return sts;
	}

	public void setSts(String sts) {
		this.sts = sts;
	}

	
}





