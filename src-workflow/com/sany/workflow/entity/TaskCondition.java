package com.sany.workflow.entity;

/**
 * @todo 任务查询条件
 * @author tanx
 * @date 2014年5月14日
 * 
 */
public class TaskCondition {

	private String taskId;// 任务id

	private String processKey;// 流程定义Key

	private String processIntsId;// 流程实例id

	private String processDefId;// 流程定义id

	private String startTime;// 任务开始时间

	private String endTime;// 任务结束时间

	private String taskState;// 任务状态 0 所有任务 1 未签收 2 已签收

	private String assignee;// 签收任务人id

	private String taskName;// 任务名称

	private boolean isAdmin;// 是否管理员

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getProcessKey() {
		return processKey;
	}

	public void setProcessKey(String processKey) {
		this.processKey = processKey;
	}

	public String getProcessIntsId() {
		return processIntsId;
	}

	public void setProcessIntsId(String processIntsId) {
		this.processIntsId = processIntsId;
	}

	public String getProcessDefId() {
		return processDefId;
	}

	public void setProcessDefId(String processDefId) {
		this.processDefId = processDefId;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getTaskState() {
		return taskState;
	}

	public void setTaskState(String taskState) {
		this.taskState = taskState;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

}
