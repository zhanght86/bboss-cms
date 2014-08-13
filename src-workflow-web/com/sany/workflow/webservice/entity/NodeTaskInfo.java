package com.sany.workflow.webservice.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 统一代办任务
 * 
 * @todo
 * @author tanx
 * @date 2014年8月5日
 * 
 */
public class NodeTaskInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String nodeName;// 节点名称
	private String startTime;// 任务到达时间
	private String claimTime;// 任务签收时间
	private String endTime;// 任务处理时间
	private String durationNode;// 任务处理工时
	private int isContainHoliday;// 工时规则
	private String duration;// 耗时
	private String alertTime;// 预警时间点
	private String isAlertTime;// 是否已到预警时间点
	private int advanceSend;// 预警提示返回结果
	private String overTime;// 超时时间点
	private String isOverTime;// 是否已到超时时间点
	private int overtimeSend;// 超时提醒返回结果
	private String userName;// 处理人
	private List<NodeTaskRelation> delegateTaskList;// 任务关系(转办/委托)

	private String assigneeName;// 签收人
	private String remark;// 备注

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getClaimTime() {
		return claimTime;
	}

	public void setClaimTime(String claimTime) {
		this.claimTime = claimTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getDurationNode() {
		return durationNode;
	}

	public void setDurationNode(String durationNode) {
		this.durationNode = durationNode;
	}

	public int getIsContainHoliday() {
		return isContainHoliday;
	}

	public void setIsContainHoliday(int isContainHoliday) {
		this.isContainHoliday = isContainHoliday;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getAlertTime() {
		return alertTime;
	}

	public void setAlertTime(String alertTime) {
		this.alertTime = alertTime;
	}

	public String getIsAlertTime() {
		return isAlertTime;
	}

	public void setIsAlertTime(String isAlertTime) {
		this.isAlertTime = isAlertTime;
	}

	public int getAdvanceSend() {
		return advanceSend;
	}

	public void setAdvanceSend(int advanceSend) {
		this.advanceSend = advanceSend;
	}

	public String getOverTime() {
		return overTime;
	}

	public void setOverTime(String overTime) {
		this.overTime = overTime;
	}

	public String getIsOverTime() {
		return isOverTime;
	}

	public void setIsOverTime(String isOverTime) {
		this.isOverTime = isOverTime;
	}

	public int getOvertimeSend() {
		return overtimeSend;
	}

	public void setOvertimeSend(int overtimeSend) {
		this.overtimeSend = overtimeSend;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<NodeTaskRelation> getDelegateTaskList() {
		return delegateTaskList;
	}

	public void setDelegateTaskList(List<NodeTaskRelation> delegateTaskList) {
		this.delegateTaskList = delegateTaskList;
	}

	public String getAssigneeName() {
		return assigneeName;
	}

	public void setAssigneeName(String assigneeName) {
		this.assigneeName = assigneeName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
