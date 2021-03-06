package com.sany.activiti.demo.pojo;

import java.sql.Timestamp;

public class ActivitiNode {

	private String id;
	
	private String process_key;
	
	private String node_id;
	
	private String node_name;
	
	private String candidate_groups_id;
	
	private String candidate_groups_name;
	
	private String candidate_users_id;
	
	private String candidate_users_name;
	
	private Timestamp create_date;
	
	private String create_person_id;
	
	private String create_person_name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProcess_key() {
		return process_key;
	}

	public void setProcess_key(String process_key) {
		this.process_key = process_key;
	}

	public String getNode_id() {
		return node_id;
	}

	public void setNode_id(String node_id) {
		this.node_id = node_id;
	}

	public String getNode_name() {
		return node_name;
	}

	public void setNode_name(String node_name) {
		this.node_name = node_name;
	}


	public String getCandidate_groups_id() {
		return candidate_groups_id;
	}

	public void setCandidate_groups_id(String candidate_groups_id) {
		this.candidate_groups_id = candidate_groups_id;
	}

	public String getCandidate_groups_name() {
		return candidate_groups_name;
	}

	public void setCandidate_groups_name(String candidate_groups_name) {
		this.candidate_groups_name = candidate_groups_name;
	}

	public String getCandidate_users_id() {
		return candidate_users_id;
	}

	public void setCandidate_users_id(String candidate_users_id) {
		this.candidate_users_id = candidate_users_id;
	}

	public String getCandidate_users_name() {
		return candidate_users_name;
	}

	public void setCandidate_users_name(String candidate_users_name) {
		this.candidate_users_name = candidate_users_name;
	}

	public Timestamp getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Timestamp create_date) {
		this.create_date = create_date;
	}

	public String getCreate_person_id() {
		return create_person_id;
	}

	public void setCreate_person_id(String create_person_id) {
		this.create_person_id = create_person_id;
	}

	public String getCreate_person_name() {
		return create_person_name;
	}

	public void setCreate_person_name(String create_person_name) {
		this.create_person_name = create_person_name;
	}
	
}
