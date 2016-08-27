package com.sany.workflow.demo.entity;

import java.io.Serializable;
import java.util.Date;

import org.frameworkset.util.annotations.RequestParam;

/**
 * 演示示例实体
 * 
 * @todo
 * @author tanx
 * @date 2014年8月27日
 * 
 */
public class DemoEntity implements Serializable {

	private static final long serialVersionUID = -4473752406812328499L;

	private int businessState;// 单据状态 0暂存 1运行中 2 结束

	private String title;// 标题

	private String sender;// 流程发起人

	private String senderName;// 流程发起人姓名

	@RequestParam(dateformat = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;// 创建时间

	@RequestParam(dateformat = "yyyy-MM-dd HH:mm:ss")
	private Date endTime;// 创建时间

	private String businessKey;// 业务主题key

	private String instanceId;// 流程实例id

	private String processKey;// 流程key

	private String taskId;// 任务id

	private String formData;// 暂存表单数据

	private String nodeName;// 节点名称

	private String assignee;// 处理人

	private String assigneeName;// 处理人名字

	public int getBusinessState() {
		return businessState;
	}

	public void setBusinessState(int businessState) {
		this.businessState = businessState;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public String getAssigneeName() {
		return assigneeName;
	}

	public void setAssigneeName(String assigneeName) {
		this.assigneeName = assigneeName;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
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

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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

	public String getProcessKey() {
		return processKey;
	}

	public void setProcessKey(String processKey) {
		this.processKey = processKey;
	}

	public String getFormData() {
		return formData;
	}

	public void setFormData(String formData) {
		this.formData = formData;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

}
