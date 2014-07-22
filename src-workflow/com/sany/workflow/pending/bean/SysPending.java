package com.sany.workflow.pending.bean;

import java.text.SimpleDateFormat;

import com.sany.greatwall.domain.ToDoPro;
import com.sany.workflow.entity.NoHandleTask;

/**
*@author qingl2
*
*/
public class SysPending {
private String businessKey;//业务ID
private String createTime;//创建时间
private String sender;//发起人姓名
private String title;//流程标题
private String url;//链接地址
private String userAccount;//发起人工号
private String taskId;//	待办任务id
private String taskDefKey;//	任务节点定义
private String instanceId;//	流程实例ID
private String defId;//	流程定义id
private String processkey;//流程key
private String suspensionState;//流程状态
private String taskState;//任务签收状态




public String getProcesskey() {
	return processkey;
}

public void setProcesskey(String processkey) {
	this.processkey = processkey;
}

public String getSuspensionState() {
	return suspensionState;
}

public void setSuspensionState(String suspensionState) {
	this.suspensionState = suspensionState;
}

public String getTaskState() {
	return taskState;
}

public void setTaskState(String taskState) {
	this.taskState = taskState;
}

public SysPending(NoHandleTask to){
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	setTaskId(to.getTaskId()) ;
	 setTaskDefKey(to.getTaskDefKey());
	setInstanceId(to.getInstanceId());
	 setDefId(to.getDefId());
	 setBusinessKey(to.getBusinessKey());
	 setCreateTime(df.format(to.getCreateTime()));
	 setSender(to.getSender());
	 setTitle(to.getTitle()+"("+to.getBusinessKey()+")");
	 setUrl(to.getUrl());
	 setUserAccount(to.getUserAccount());
}

public SysPending(ToDoPro to){
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	setTaskId(to.getTaskId()) ;
	 setTaskDefKey(to.getTaskDefKey());
	setInstanceId(to.getInstanceId());
	 setDefId(to.getDefId());
	 setBusinessKey(to.getBusinessKey());
	 setCreateTime(df.format(to.getCreateTime()));
	 setSender(to.getSender());
	 setTitle(to.getTitle());
	 setUrl("http://uimweb.sany.com.cn"+to.getUrl());
	 setUserAccount(to.getUserAccount());
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
public String getBusinessKey() {
	return businessKey;
}
public void setBusinessKey(String businessKey) {
	this.businessKey = businessKey;
}
public String getCreateTime() {
	return createTime;
}
public void setCreateTime(String createTime) {
	this.createTime = createTime;
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
public String getUserAccount() {
	return userAccount;
}
public void setUserAccount(String userAccount) {
	this.userAccount = userAccount;
}

}
