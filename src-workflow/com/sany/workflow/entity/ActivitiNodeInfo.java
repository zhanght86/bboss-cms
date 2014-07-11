package com.sany.workflow.entity;

public class ActivitiNodeInfo {

	private String id;

	private String process_key;

	private String node_key;

	private String node_name;

	private int order_num;

	private String node_type;

	private String node_groups_id;

	private String node_groups_name;

	private String node_users_id;

	private String node_users_name;

	private String node_param_id;

	private String node_param_value;

	private String isMulti;// 0不是多实例1串行多实例2并行多实例

	public String getIsMulti() {
		return isMulti;
	}

	public void setIsMulti(String isMulti) {
		this.isMulti = isMulti;
	}

	public String getNode_param_id() {
		return node_param_id;
	}

	public void setNode_param_id(String node_param_id) {
		this.node_param_id = node_param_id;
	}

	public String getNode_param_value() {
		return node_param_value;
	}

	public void setNode_param_value(String node_param_value) {
		this.node_param_value = node_param_value;
	}

	public String getNode_groups_id() {
		return node_groups_id;
	}

	public void setNode_groups_id(String node_groups_id) {
		this.node_groups_id = node_groups_id;
	}

	public String getNode_groups_name() {
		return node_groups_name;
	}

	public void setNode_groups_name(String node_groups_name) {
		this.node_groups_name = node_groups_name;
	}

	public String getNode_users_id() {
		return node_users_id;
	}

	public void setNode_users_id(String node_users_id) {
		this.node_users_id = node_users_id;
	}

	public String getNode_users_name() {
		return node_users_name;
	}

	public void setNode_users_name(String node_users_name) {
		this.node_users_name = node_users_name;
	}

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

	public String getNode_key() {
		return node_key;
	}

	public void setNode_key(String node_key) {
		this.node_key = node_key;
	}

	public String getNode_name() {
		return node_name;
	}

	public void setNode_name(String node_name) {
		this.node_name = node_name;
	}

	public int getOrder_num() {
		return order_num;
	}

	public void setOrder_num(int order_num) {
		this.order_num = order_num;
	}

	public String getNode_type() {
		return node_type;
	}

	public void setNode_type(String node_type) {
		this.node_type = node_type;
	}
}
