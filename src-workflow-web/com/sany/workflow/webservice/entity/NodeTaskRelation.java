package com.sany.workflow.webservice.entity;

import java.io.Serializable;

/**
 * 节点任务关系
 * 
 * @todo
 * @author tanx
 * @date 2014年8月5日
 * 
 */
public class NodeTaskRelation implements Serializable {

	private static final long serialVersionUID = 1L;

	private String taskRelation;// 1转办关系 2 委托关系
	private String fromUserName;// 转办/委托人姓名
	private String toUserName;// 被 转办/委托人姓名
	private String changeTime;// 转办/委托 时间

	public String getTaskRelation() {
		return taskRelation;
	}

	public void setTaskRelation(String taskRelation) {
		this.taskRelation = taskRelation;
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	public String getChangeTime() {
		return changeTime;
	}

	public void setChangeTime(String changeTime) {
		this.changeTime = changeTime;
	}

}
