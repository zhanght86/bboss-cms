package com.sany.workflow.entity;

import java.sql.Timestamp;

public class ActivitiNodeCandidate {

	private String id;

	private String node_id;

	private String candidate_groups_id;

	private String candidate_groups_name;

	private String candidate_users_id;

	private String candidate_users_name;

	private String business_id;

	private String business_type;

	private Timestamp create_date;

	private String create_person_id;

	private String create_person_name;

	private String candidate_orgs_id;

	private String candidate_orgs_name;

	private String candidate_jobs_id;

	private String candidate_jobs_name;

	private String candidate_roles_id;

	private String candidate_roles_name;

	private int is_valid;

	private int is_edit_candidate;

	private String node_key;
	
	private String node_type;

	private String node_name;

	private String process_key;

	private String org_name;

	private String messagetempleid; // 短信模板id

	private String messagetempletitle;// 短息模板名称

	private String emailtempleid;// 邮件模板id

	private String emailtempletitle;// 邮件模板名称

	private String duration_node;// 节点处理工时

	private int noticenum;// 提醒次数

	private String isMulti;// 0不是多实例1串行多实例2并行多实例

	public String getNode_type() {
		return node_type;
	}

	public void setNode_type(String node_type) {
		this.node_type = node_type;
	}

	public String getIsMulti() {
		return isMulti;
	}

	public void setIsMulti(String isMulti) {
		this.isMulti = isMulti;
	}

	public int getNoticenum() {
		return noticenum;
	}

	public void setNoticenum(int noticenum) {
		this.noticenum = noticenum;
	}

	public String getDuration_node() {
		return duration_node;
	}

	public void setDuration_node(String duration_node) {
		this.duration_node = duration_node;
	}

	public String getMessagetempletitle() {
		return messagetempletitle;
	}

	public void setMessagetempletitle(String messagetempletitle) {
		this.messagetempletitle = messagetempletitle;
	}

	public String getEmailtempletitle() {
		return emailtempletitle;
	}

	public void setEmailtempletitle(String emailtempletitle) {
		this.emailtempletitle = emailtempletitle;
	}

	public String getMessagetempleid() {
		return messagetempleid;
	}

	public void setMessagetempleid(String messagetempleid) {
		this.messagetempleid = messagetempleid;
	}

	public String getEmailtempleid() {
		return emailtempleid;
	}

	public void setEmailtempleid(String emailtempleid) {
		this.emailtempleid = emailtempleid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNode_id() {
		return node_id;
	}

	public void setNode_id(String node_id) {
		this.node_id = node_id;
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

	public String getBusiness_id() {
		return business_id;
	}

	public void setBusiness_id(String business_id) {
		this.business_id = business_id;
	}

	public String getBusiness_type() {
		return business_type;
	}

	public void setBusiness_type(String business_type) {
		this.business_type = business_type;
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

	public String getNode_key() {
		return node_key;
	}

	public void setNode_key(String node_key) {
		this.node_key = node_key;
	}

	public String getProcess_key() {
		return process_key;
	}

	public void setProcess_key(String process_key) {
		this.process_key = process_key;
	}

	public String getOrg_name() {
		return org_name;
	}

	public void setOrg_name(String org_name) {
		this.org_name = org_name;
	}

	public String getNode_name() {
		return node_name;
	}

	public void setNode_name(String node_name) {
		this.node_name = node_name;
	}

	public String getCandidate_orgs_id() {
		return candidate_orgs_id;
	}

	public void setCandidate_orgs_id(String candidate_orgs_id) {
		this.candidate_orgs_id = candidate_orgs_id;
	}

	public String getCandidate_orgs_name() {
		return candidate_orgs_name;
	}

	public void setCandidate_orgs_name(String candidate_orgs_name) {
		this.candidate_orgs_name = candidate_orgs_name;
	}

	public String getCandidate_jobs_id() {
		return candidate_jobs_id;
	}

	public void setCandidate_jobs_id(String candidate_jobs_id) {
		this.candidate_jobs_id = candidate_jobs_id;
	}

	public String getCandidate_jobs_name() {
		return candidate_jobs_name;
	}

	public void setCandidate_jobs_name(String candidate_jobs_name) {
		this.candidate_jobs_name = candidate_jobs_name;
	}

	public String getCandidate_roles_id() {
		return candidate_roles_id;
	}

	public void setCandidate_roles_id(String candidate_roles_id) {
		this.candidate_roles_id = candidate_roles_id;
	}

	public String getCandidate_roles_name() {
		return candidate_roles_name;
	}

	public void setCandidate_roles_name(String candidate_roles_name) {
		this.candidate_roles_name = candidate_roles_name;
	}

	public int getIs_valid() {
		return is_valid;
	}

	public void setIs_valid(int is_valid) {
		this.is_valid = is_valid;
	}

	public int getIs_edit_candidate() {
		return is_edit_candidate;
	}

	public void setIs_edit_candidate(int is_edit_candidate) {
		this.is_edit_candidate = is_edit_candidate;
	}

}
