package com.sany.workflow.entity;

import java.sql.Timestamp;
import java.util.List;

import org.frameworkset.util.annotations.RequestParam;

import com.sany.workflow.entrust.entity.WfEntrust;

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

	@RequestParam(dateformat = "yyyy-MM-dd HH:mm:ss")
	private Timestamp startTime;// 任务开始时间

	@RequestParam(dateformat = "yyyy-MM-dd HH:mm:ss")
	private Timestamp endTime;// 任务结束时间

	private String taskState;// 任务状态 0 所有任务 1 未签收 2 已签收

	private String assignee;// 签收任务人id

	private String taskName;// 任务名称

	private boolean isAdmin;// 是否管理员

	private String businessKey;// 业务主题

	private String businessTypeId;// 业务类型id

	private String taskDefKey;// 任务key

	private String completeReason;// 处理结果

	private List<WfEntrust> entrustList;// 委托代办关系集合数据

	private String createUser;// 委托人

	private String entrustUser;// 被委托人

	private String currentUser;// 当前用户

	private String changeUserId; // 转办人id

	private String appName;// 应用名称

	private String rejectToActId;// 驳回到哪个节点

	private String toActName;// 驳回节点名称

	public String getToActName() {
		return toActName;
	}

	public void setToActName(String toActName) {
		this.toActName = toActName;
	}

	public String getRejectToActId() {
		return rejectToActId;
	}

	public void setRejectToActId(String rejectToActId) {
		this.rejectToActId = rejectToActId;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getChangeUserId() {
		return changeUserId;
	}

	public void setChangeUserId(String changeUserId) {
		this.changeUserId = changeUserId;
	}

	public String getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(String currentUser) {
		this.currentUser = currentUser;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getEntrustUser() {
		return entrustUser;
	}

	public void setEntrustUser(String entrustUser) {
		this.entrustUser = entrustUser;
	}

	public List<WfEntrust> getEntrustList() {
		return entrustList;
	}

	public void setEntrustList(List<WfEntrust> entrustList) {
		this.entrustList = entrustList;
	}

	public String getTaskDefKey() {
		return taskDefKey;
	}

	public void setTaskDefKey(String taskDefKey) {
		this.taskDefKey = taskDefKey;
	}

	public String getCompleteReason() {
		return completeReason;
	}

	public void setCompleteReason(String completeReason) {
		this.completeReason = completeReason;
	}

	public String getBusinessKey() {
		return businessKey;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public String getBusinessTypeId() {
		return businessTypeId;
	}

	public void setBusinessTypeId(String businessTypeId) {
		this.businessTypeId = businessTypeId;
	}

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

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
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
