package org.frameworkset.spi.task.entity;

public class TaskEngine {
	private String taskEngineID;
	private String taskEngineName;
	private String configfile;
	public String getTaskEngineID() {
		return taskEngineID;
	}
	public void setTaskEngineID(String taskEngineID) {
		this.taskEngineID = taskEngineID;
	}
	public String getTaskEngineName() {
		return taskEngineName;
	}
	public void setTaskEngineName(String taskEngineName) {
		this.taskEngineName = taskEngineName;
	}
	public String getConfigfile() {
		return configfile;
	}
	public void setConfigfile(String configfile) {
		this.configfile = configfile;
	}

}
