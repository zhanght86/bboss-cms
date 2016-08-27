package com.sany.workflow.entity;

import java.sql.Timestamp;

import org.frameworkset.util.annotations.RequestParam;

/**
 * 转派任务实体
 * 
 * @todo
 * @author tanx
 * @date 2014年10月23日
 * 
 */
public class DelegateTaskLog {

	private String taskId;// 任务id

	private String processKey;// 流程key

	private String processId;// 流程实例id

	private String fromUser;// 转派人

	private String toUser;// 被转派人

	@RequestParam(dateformat = "yyyy-MM-dd HH:mm:ss")
	private Timestamp delegateTime;// 转派时间

	@RequestParam(dateformat = "yyyy-MM-dd HH:mm:ss")
	private Timestamp delegateTime_from;// 转派时间

	@RequestParam(dateformat = "yyyy-MM-dd HH:mm:ss")
	private Timestamp delegateTime_to;// 转派时间

	private String delegateRemark;// 转派日志

	public Timestamp getDelegateTime_from() {
		return delegateTime_from;
	}

	public void setDelegateTime_from(Timestamp delegateTime_from) {
		this.delegateTime_from = delegateTime_from;
	}

	public Timestamp getDelegateTime_to() {
		return delegateTime_to;
	}

	public void setDelegateTime_to(Timestamp delegateTime_to) {
		this.delegateTime_to = delegateTime_to;
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

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getFromUser() {
		return fromUser;
	}

	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}

	public String getToUser() {
		return toUser;
	}

	public void setToUser(String toUser) {
		this.toUser = toUser;
	}

	public Timestamp getDelegateTime() {
		return delegateTime;
	}

	public void setDelegateTime(Timestamp delegateTime) {
		this.delegateTime = delegateTime;
	}

	public String getDelegateRemark() {
		return delegateRemark;
	}

	public void setDelegateRemark(String delegateRemark) {
		this.delegateRemark = delegateRemark;
	}

}
