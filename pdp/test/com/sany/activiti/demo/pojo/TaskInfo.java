package com.sany.activiti.demo.pojo;

import java.sql.Timestamp;
import java.util.List;

public class TaskInfo {

	private String id;
	
	private String process_instance_id;
	
	private String task_id;
	
	private String deal_user;
	
	private Timestamp deal_time;
	
	private String deal_opinion;
	
	private String is_pass;
	
	private String task_name;
	
	private List<MaterielTest> materielList;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProcess_instance_id() {
		return process_instance_id;
	}

	public void setProcess_instance_id(String process_instance_id) {
		this.process_instance_id = process_instance_id;
	}

	public String getTask_id() {
		return task_id;
	}

	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}

	public String getDeal_user() {
		return deal_user;
	}

	public void setDeal_user(String deal_user) {
		this.deal_user = deal_user;
	}



	public String getDeal_opinion() {
		return deal_opinion;
	}


	public Timestamp getDeal_time() {
		return deal_time;
	}

	public void setDeal_time(Timestamp deal_time) {
		this.deal_time = deal_time;
	}

	public void setDeal_opinion(String deal_opinion) {
		this.deal_opinion = deal_opinion;
	}

	public String getIs_pass() {
		return is_pass;
	}

	public void setIs_pass(String is_pass) {
		this.is_pass = is_pass;
	}

	public String getTask_name() {
		return task_name;
	}

	public void setTask_name(String task_name) {
		this.task_name = task_name;
	}

	public List<MaterielTest> getMaterielList() {
		return materielList;
	}

	public void setMaterielList(List<MaterielTest> materielList) {
		this.materielList = materielList;
	}
	
	
}
