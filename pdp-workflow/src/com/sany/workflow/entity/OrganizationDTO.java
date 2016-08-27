package com.sany.workflow.entity;

import java.io.Serializable;

public class OrganizationDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6302521066118237755L;
	
	private long id;
	
	private String text;
	
	private String state;


	public String getState() {
		return "closed";
	}

	public void setState(String state) {
		this.state = "closed";
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}


	

}
