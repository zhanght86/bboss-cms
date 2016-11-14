package com.sany.application.entity;

import java.sql.Timestamp;

public class WfApp {
	
	private String id;
	
	private String system_id;
	
	private String system_name;
	
	private String wf_published_url;
	
	private String wf_manage_url;
	
	private String app_mode_type;
	
	private String app_mode_type_name;
	
	private String app_mode_type_nonexist;
	
	private String creator;
	
	private Timestamp create_date;
	
	private String update_person;
	
	private Timestamp update_date;
	
	private String todo_url;
	
	private String app_url;
	
	private String sso_url;
	
	private String system_secret;
	
	private String system_secret_text;
	
	private String old_system_secret;
	
	private String pending_type;
	
	private String pending_used;
	private int needsign;
	
	private long tickettime;// 票据失效时间
	private String publicKey;
	private String privateKey;

	public String getPending_type() {
		return pending_type;
	}

	public void setPending_type(String pending_type) {
		this.pending_type = pending_type;
	}

	

	public String getPending_used() {
		return pending_used;
	}

	public void setPending_used(String pending_used) {
		this.pending_used = pending_used;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSystem_id() {
		return system_id;
	}

	public void setSystem_id(String system_id) {
		this.system_id = system_id;
	}

	public String getSystem_name() {
		return system_name;
	}

	public void setSystem_name(String system_name) {
		this.system_name = system_name;
	}

	public String getWf_published_url() {
		return wf_published_url;
	}

	public void setWf_published_url(String wf_published_url) {
		this.wf_published_url = wf_published_url;
	}

	public String getWf_manage_url() {
		return wf_manage_url;
	}

	public void setWf_manage_url(String wf_manage_url) {
		this.wf_manage_url = wf_manage_url;
	}

	public String getApp_mode_type() {
		return app_mode_type;
	}

	public void setApp_mode_type(String app_mode_type) {
		this.app_mode_type = app_mode_type;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getUpdate_person() {
		return update_person;
	}

	public void setUpdate_person(String update_person) {
		this.update_person = update_person;
	}
	
	public String getApp_mode_type_name() {
		return app_mode_type_name;
	}

	public void setApp_mode_type_name(String app_mode_type_name) {
		this.app_mode_type_name = app_mode_type_name;
	}
	
	public String getTodo_url() {
		return todo_url;
	}

	public void setTodo_url(String todo_url) {
		this.todo_url = todo_url;
	}

	public String getApp_url() {
		return app_url;
	}

	public void setApp_url(String app_url) {
		this.app_url = app_url;
	}
	
	public Timestamp getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Timestamp create_date) {
		this.create_date = create_date;
	}

	public Timestamp getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(Timestamp update_date) {
		this.update_date = update_date;
	}
	
	public String getSystem_secret() {
		return system_secret;
	}

	public void setSystem_secret(String system_secret) {
		this.system_secret = system_secret;
	}

	public String getSystem_secret_text() {
		return system_secret_text;
	}

	public void setSystem_secret_text(String system_secret_text) {
		this.system_secret_text = system_secret_text;
	}
	
	public String getOld_system_secret() {
		return old_system_secret;
	}

	public void setOld_system_secret(String old_system_secret) {
		this.old_system_secret = old_system_secret;
	}
	
	public String getApp_mode_type_nonexist() {
		return app_mode_type_nonexist;
	}

	public void setApp_mode_type_nonexist(String app_mode_type_nonexist) {
		this.app_mode_type_nonexist = app_mode_type_nonexist;
	}
	
	public String getSso_url() {
		return sso_url;
	}

	public void setSso_url(String sso_url) {
		this.sso_url = sso_url;
	}

	public long getTickettime() {
		return tickettime;
	}

	public void setTickettime(long tickettime) {
		this.tickettime = tickettime;
	}

	public int getNeedsign() {
		return needsign;
	}

	public void setNeedsign(int needsign) {
		this.needsign = needsign;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}




}
