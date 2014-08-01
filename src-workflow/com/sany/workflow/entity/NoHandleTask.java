package com.sany.workflow.entity;

import java.util.Date;

import org.frameworkset.util.annotations.RequestParam;

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

	@RequestParam(dateformat = "yyyy-MM-dd HH:mm:ss")
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

	private int taskType;// 任务类型 0自己任务1委托任务2转办任务

	private String fromUser;// 转办/委托人

	private String fromUserName;// 转办/委托人姓名

	private String dealButtionName;// 处理记录按钮名称
	
	private String appUrl;// 应用url

	public String getAppUrl() {
		return appUrl;
	}

	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}

	public String getDealButtionName() {
		return dealButtionName;
	}

	public void setDealButtionName(String dealButtionName) {
		this.dealButtionName = dealButtionName;
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public String getFromUser() {
		return fromUser;
	}

	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}

	public int getTaskType() {
		return taskType;
	}

	public void setTaskType(int taskType) {
		this.taskType = taskType;
	}

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
