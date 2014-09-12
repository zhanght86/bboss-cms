package com.sany.workflow.business.entity;

import java.io.Serializable;
import java.util.Date;

import org.frameworkset.util.annotations.RequestParam;

/**
 * 节点任务信息
 * 
 * @todo
 * @author tanx
 * @date 2014年8月27日
 * 
 */
public class TaskInfo implements Serializable {

	private static final long serialVersionUID = -4473752406812328499L;

	private String taskState;// 任务状态 1 未签收2 签收

	private String title;// 标题

	private String sender;// 流程发起人

	private String senderName;// 流程发起人姓名

	@RequestParam(dateformat = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;// 创建时间

	private String taskId;// 任务id

	private String taskDefKey;// 任务key

	private String businessKey;// 业务主题key

	private String instanceId;// 流程实例id

	private String defId;// 流程定义id

	private String processKey;// 流程key

	private String suspensionState;// 流程状态 1 运行中2 挂起

	private String taskUrl;// 链接地址

	private int taskType;// 任务类型 0自己任务1委托任务2转办任务

	private String fromUser;// 转办/委托人

	private String fromUserName;// 转办/委托人姓名
	
	private int isRecall;// 可撤回 0不能1能
	private int isCancel;// 可驳回 0 不能 1 能
	private int isDiscard;// 可废弃 0 不能 1 能

	public int getIsRecall() {
		return isRecall;
	}

	public void setIsRecall(int isRecall) {
		this.isRecall = isRecall;
	}

	public int getIsCancel() {
		return isCancel;
	}

	public void setIsCancel(int isCancel) {
		this.isCancel = isCancel;
	}

	public int getIsDiscard() {
		return isDiscard;
	}

	public void setIsDiscard(int isDiscard) {
		this.isDiscard = isDiscard;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getTaskState() {
		return taskState;
	}

	public void setTaskState(String taskState) {
		this.taskState = taskState;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
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

	public String getTaskUrl() {
		return taskUrl;
	}

	public void setTaskUrl(String taskUrl) {
		this.taskUrl = taskUrl;
	}

	public int getTaskType() {
		return taskType;
	}

	public void setTaskType(int taskType) {
		this.taskType = taskType;
	}

	public String getFromUser() {
		return fromUser;
	}

	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

}
