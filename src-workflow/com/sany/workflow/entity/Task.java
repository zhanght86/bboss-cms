package com.sany.workflow.entity;

public class Task {
	
	private String id_;
	
	private String execution_id_;
	
	private String proc_inst_id_;
	
	private String proc_def_id_;
	
	private String name_;
	
	private String task_def_key_;

	public String getId_() {
		return id_;
	}

	public void setId_(String id_) {
		this.id_ = id_;
	}

	public String getExecution_id_() {
		return execution_id_;
	}

	public void setExecution_id_(String execution_id_) {
		this.execution_id_ = execution_id_;
	}

	public String getProc_inst_id_() {
		return proc_inst_id_;
	}

	public void setProc_inst_id_(String proc_inst_id_) {
		this.proc_inst_id_ = proc_inst_id_;
	}

	public String getProc_def_id_() {
		return proc_def_id_;
	}

	public void setProc_def_id_(String proc_def_id_) {
		this.proc_def_id_ = proc_def_id_;
	}

	public String getName_() {
		return name_;
	}

	public void setName_(String name_) {
		this.name_ = name_;
	}

	public String getTask_def_key_() {
		return task_def_key_;
	}

	public void setTask_def_key_(String task_def_key_) {
		this.task_def_key_ = task_def_key_;
	}

}
