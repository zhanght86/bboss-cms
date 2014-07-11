package com.sany.workflow.entrust.entity;

import java.sql.Timestamp;
import java.util.List;

import org.frameworkset.util.annotations.RequestParam;

public class WfEntrust {

	private String id;

	private String entrust_user;

	private String entrust_user_name;

	@RequestParam(dateformat = "yyyy-MM-dd")
	private Timestamp start_date;

	@RequestParam(dateformat = "yyyy-MM-dd")
	private Timestamp end_date;

	@RequestParam(dateformat = "yyyy-MM-dd")
	private Timestamp create_date;

	private String create_user;

	private String create_user_name;

	private String sts;

	private String wf_entrust_type;

	private List<String> wfProcdefIdList;

	private List<String> wfEntrustIdList;

	public List<String> getWfEntrustIdList() {
		return wfEntrustIdList;
	}

	public void setWfEntrustIdList(List<String> wfEntrustIdList) {
		this.wfEntrustIdList = wfEntrustIdList;
	}

	public String getWf_entrust_type() {
		return wf_entrust_type;
	}

	public void setWf_entrust_type(String wf_entrust_type) {
		this.wf_entrust_type = wf_entrust_type;
	}

	public List<String> getWfProcdefIdList() {
		return wfProcdefIdList;
	}

	public void setWfProcdefIdList(List<String> wfProcdefIdList) {
		this.wfProcdefIdList = wfProcdefIdList;
	}

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

	public String getEntrust_user_name() {
		return entrust_user_name;
	}

	public void setEntrust_user_name(String entrust_user_name) {
		this.entrust_user_name = entrust_user_name;
	}

	public String getCreate_user_name() {
		return create_user_name;
	}

	public void setCreate_user_name(String create_user_name) {
		this.create_user_name = create_user_name;
	}

}
