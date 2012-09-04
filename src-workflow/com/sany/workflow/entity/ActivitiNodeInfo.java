package com.sany.workflow.entity;

public class ActivitiNodeInfo {

	private String id;
	
	private String process_key;
	
	private String node_key;
	
	private String node_name;
	
	private int order_num;

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
}
