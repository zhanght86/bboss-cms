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
	
	private String node_orgs_id;

	private String node_orgs_name;

	private String node_param_id;

	private String node_param_value;

	private String isMulti;// 0不是多实例1串行多实例2并行多实例

	private int IS_MULTI_DEFAULT;// 默认多实例 0 否 1 是

	private String duration_node;// 处理工时

	private String node_describe;// 节点描述

	private String taskUrl;// 待办URL

	private String nodeTypeName;// 类型名称
	
	private int is_copy;//是否抄送节点
	
	private String copyusers;// 抄送用户

	private String copyorgs;// 抄送部门

	private String copyerscnname;// 抄送用户+部门名称
	
	public String getDuration_node() {
		return duration_node;
	}

	public void setDuration_node(String duration_node) {
		this.duration_node = duration_node;
	}

	public int getIS_MULTI_DEFAULT() {
		return IS_MULTI_DEFAULT;
	}

	public void setIS_MULTI_DEFAULT(int iS_MULTI_DEFAULT) {
		IS_MULTI_DEFAULT = iS_MULTI_DEFAULT;
	}

	public String getNodeTypeName() {
		return nodeTypeName;
	}

	public void setNodeTypeName(String nodeTypeName) {
		this.nodeTypeName = nodeTypeName;
	}

	public String getTaskUrl() {
		return taskUrl;
	}

	public void setTaskUrl(String taskUrl) {
		this.taskUrl = taskUrl;
	}

	public String getNode_describe() {
		return node_describe;
	}

	public void setNode_describe(String node_describe) {
		this.node_describe = node_describe;
	}

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

	public int getIs_copy() {
		return is_copy;
	}

	public void setIs_copy(int is_copy) {
		this.is_copy = is_copy;
	}

	public String getNode_orgs_id() {
		return node_orgs_id;
	}

	public void setNode_orgs_id(String node_orgs_id) {
		this.node_orgs_id = node_orgs_id;
	}

	public String getNode_orgs_name() {
		return node_orgs_name;
	}

	public void setNode_orgs_name(String node_orgs_name) {
		this.node_orgs_name = node_orgs_name;
	}

	public String getCopyusers() {
		return copyusers;
	}

	public void setCopyusers(String copyusers) {
		this.copyusers = copyusers;
	}

	public String getCopyorgs() {
		return copyorgs;
	}

	public void setCopyorgs(String copyorgs) {
		this.copyorgs = copyorgs;
	}

	public String getCopyerscnname() {
		return copyerscnname;
	}

	public void setCopyerscnname(String copyerscnname) {
		this.copyerscnname = copyerscnname;
	}
}
