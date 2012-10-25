package com.sany.activiti.demo.pojo;

import java.sql.Timestamp;

public class MaterielTest {

	private String id;
	
	private String apply_name;
	
	private Timestamp apply_time;
	
	private String materiel_name;
	
	private Integer materiel_num;

	private String title;
	
	private String process_name;
	
	private String process_instance_id;
	
	private String task_id;//临时变量
	
	private String task_name;
	
	private String act_name;//当前所处环节名称
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getApply_name() {
		return apply_name;
	}

	public void setApply_name(String apply_name) {
		this.apply_name = apply_name;
	}


	public Timestamp getApply_time() {
		return apply_time;
	}

	public void setApply_time(Timestamp apply_time) {
		this.apply_time = apply_time;
	}

	public String getTask_name() {
		return task_name;
	}

	public void setTask_name(String task_name) {
		this.task_name = task_name;
	}

	public String getMateriel_name() {
		return materiel_name;
	}

	public void setMateriel_name(String materiel_name) {
		this.materiel_name = materiel_name;
	}

	public Integer getMateriel_num() {
		return materiel_num;
	}

	public void setMateriel_num(Integer materiel_num) {
		this.materiel_num = materiel_num;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getProcess_name() {
		return process_name;
	}

	public void setProcess_name(String process_name) {
		this.process_name = process_name;
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


	public String getAct_name() {
		return act_name;
	}

	public void setAct_name(String act_name) {
		this.act_name = act_name;
	}




}
