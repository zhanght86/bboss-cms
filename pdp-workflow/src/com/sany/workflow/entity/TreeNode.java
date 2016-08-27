package com.sany.workflow.entity;

public class TreeNode {
	private String id;

	private String text;

	private String state;

	public String getState() {
		return "closed";
	}

	public void setState(String state) {
		this.state = "closed";
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
