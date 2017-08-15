/*
 * @(#)ProcessDefCondition.java
 * 
 * Copyright @ 2001-2012 SANY Group Co.,Ltd.
 * All right reserved.
 * 
 * 这个软件是属于bbossgroups有限公司机密的和私有信息，不得泄露。
 * 并且只能由bbossgroups有限公司内部员工在得到许可的情况下才允许使用。
 * This software is the confidential and proprietary information
 * of SANY Group Co, Ltd. You shall not disclose such
 * Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with
 * SANY Group Co, Ltd.
 */
package com.sany.workflow.entity.statistics;

import java.sql.Timestamp;

import org.frameworkset.util.annotations.RequestParam;

/**
 * @todo 个人处理任务记录
 * @author tanx
 * @date 2014年5月8日
 * 
 */
public class PersonalDealTask {

	private String nodeName;// 节点名称

	private String durationNode;// 节点工时

	private String duration;// 耗时

	@RequestParam(dateformat = "yyyy-MM-dd HH:mm:ss")
	private Timestamp alertTime;// 预警时间

	@RequestParam(dateformat = "yyyy-MM-dd HH:mm:ss")
	private Timestamp overTime;// 超时时间

	@RequestParam(dateformat = "yyyy-MM-dd HH:mm:ss")
	private Timestamp startTime;// 开始时间

	@RequestParam(dateformat = "yyyy-MM-dd HH:mm:ss")
	private Timestamp endTime;// 结束时间

	private String isOverTime;// 是否超时提醒(0未超时1超时)

	private String isAlertTime;// 是否预警提醒(0未预警1预警)

	private String remark;// 备注

	public String getIsOverTime() {
		return isOverTime;
	}

	public void setIsOverTime(String isOverTime) {
		this.isOverTime = isOverTime;
	}

	public String getIsAlertTime() {
		return isAlertTime;
	}

	public void setIsAlertTime(String isAlertTime) {
		this.isAlertTime = isAlertTime;
	}

	public Timestamp getAlertTime() {
		return alertTime;
	}

	public void setAlertTime(Timestamp alertTime) {
		this.alertTime = alertTime;
	}

	public Timestamp getOverTime() {
		return overTime;
	}

	public void setOverTime(Timestamp overTime) {
		this.overTime = overTime;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getDurationNode() {
		return durationNode;
	}

	public void setDurationNode(String durationNode) {
		this.durationNode = durationNode;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

}
