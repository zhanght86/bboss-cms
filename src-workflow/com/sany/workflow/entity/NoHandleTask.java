package com.sany.workflow.entity;

import java.util.Date;

/**
 * 统一待办取待办任务实体bean
 * 
 * @todo
 * @author tanx
 * @date 2014年7月3日
 * 
 */
public class NoHandleTask {

	private String taskState;// 任务状态 1 未签收2 签收

	private String userAccount;// 发起人工号

	private Date createTime;// 创建时间

	private String taskId;// 任务id

	private String taskDefKey;// 任务key

	private String businessKey;// 业务主题key

	private String instanceId;// 流程实例id

	private String defId;// 流程定义id

	private String sender;// 发起人姓名

	private String title;// 流程标题

	private String processKey;// 流程key

	private String suspensionState;// 流程状态 1 运行中2 挂起

	private String url;// 链接地址

	public String getTaskState() {
		return taskState;
	}

	public void setTaskState(String taskState) {
		this.taskState = taskState;
	}

	public String getProcessKey() {
		return processKey;
	}

	public void setProcessKey(String processKey) {
		this.processKey = processKey;
	}

	public String getSuspensionState() {
		return suspensionState;
	}

	public void setSuspensionState(String suspensionState) {
		this.suspensionState = suspensionState;
	}

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTaskDefKey() {
		return taskDefKey;
	}

	public void setTaskDefKey(String taskDefKey) {
		this.taskDefKey = taskDefKey;
	}

	public String getBusinessKey() {
		return businessKey;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getDefId() {
		return defId;
	}

	public void setDefId(String defId) {
		this.defId = defId;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
