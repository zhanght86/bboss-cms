/*
 * @(#)ProcessDefCondition.java
 * 
 * Copyright @ 2001-2012 SANY Group Co.,Ltd.
 * All right reserved.
 * 
 * 这个软件是属于三一集团有限公司机密的和私有信息，不得泄露。
 * 并且只能由三一集团有限公司内部员工在得到许可的情况下才允许使用。
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
 * @todo 流程统计实体
 * @author tanx
 * @date 2014年5月8日
 * 
 */
public class ProcessCounter {

	private String processKey;// 流程key

	private String processName;// 流程名称

	private int processNum;// 流程总数

	private int passNum;// 通过总数（结束流程-废弃总数）

	private int waitNum;// 待审总数（所有流程-结束的流程）

	private int rejectNum;// 驳回总数(逻辑删除，DELETE_REASON_不为空)

	private int discardNum;// 废弃总数

	private int cancelNum;// 撤销总数

	private String startUserId;// 流程发起人

	private String startUserName;// 流程发起人姓名

	private String businessKey;// 业务主题

	@RequestParam(dateformat = "yyyy-MM-dd HH:mm:ss")
	private Timestamp startTime;// 流程开始时间

	@RequestParam(dateformat = "yyyy-MM-dd HH:mm:ss")
	private Timestamp endTime;// 流程完成时间

	private String duration;// 耗时

	private int delegateNum;// 转办次数

	private int entrustNum;// 委托次数

	private String avgWorkTime;// 平均办结工时

	private double avgPassNum;// 平均办理人次

	private double avgDelegateNum;// 平均转办次数

	private double avgDiscardNum;// 平均废弃次数

	private double avgCancelNum;// 平均撤销次数

	private double avgRejectNum;// 平均驳回次数

	public double getAvgDiscardNum() {
		return avgDiscardNum;
	}

	public void setAvgDiscardNum(double avgDiscardNum) {
		this.avgDiscardNum = avgDiscardNum;
	}

	public double getAvgCancelNum() {
		return avgCancelNum;
	}

	public void setAvgCancelNum(double avgCancelNum) {
		this.avgCancelNum = avgCancelNum;
	}

	public double getAvgRejectNum() {
		return avgRejectNum;
	}

	public void setAvgRejectNum(double avgRejectNum) {
		this.avgRejectNum = avgRejectNum;
	}

	public String getProcessKey() {
		return processKey;
	}

	public void setProcessKey(String processKey) {
		this.processKey = processKey;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public int getProcessNum() {
		return processNum;
	}

	public void setProcessNum(int processNum) {
		this.processNum = processNum;
	}

	public int getPassNum() {
		return passNum;
	}

	public void setPassNum(int passNum) {
		this.passNum = passNum;
	}

	public int getWaitNum() {
		return waitNum;
	}

	public void setWaitNum(int waitNum) {
		this.waitNum = waitNum;
	}

	public int getRejectNum() {
		return rejectNum;
	}

	public void setRejectNum(int rejectNum) {
		this.rejectNum = rejectNum;
	}

	public int getDiscardNum() {
		return discardNum;
	}

	public void setDiscardNum(int discardNum) {
		this.discardNum = discardNum;
	}

	public int getCancelNum() {
		return cancelNum;
	}

	public void setCancelNum(int cancelNum) {
		this.cancelNum = cancelNum;
	}

	public String getStartUserId() {
		return startUserId;
	}

	public void setStartUserId(String startUserId) {
		this.startUserId = startUserId;
	}

	public String getStartUserName() {
		return startUserName;
	}

	public void setStartUserName(String startUserName) {
		this.startUserName = startUserName;
	}

	public String getBusinessKey() {
		return businessKey;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
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

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public int getDelegateNum() {
		return delegateNum;
	}

	public void setDelegateNum(int delegateNum) {
		this.delegateNum = delegateNum;
	}

	public int getEntrustNum() {
		return entrustNum;
	}

	public void setEntrustNum(int entrustNum) {
		this.entrustNum = entrustNum;
	}

	public String getAvgWorkTime() {
		return avgWorkTime;
	}

	public void setAvgWorkTime(String avgWorkTime) {
		this.avgWorkTime = avgWorkTime;
	}

	public double getAvgPassNum() {
		return avgPassNum;
	}

	public void setAvgPassNum(double avgPassNum) {
		this.avgPassNum = avgPassNum;
	}

	public double getAvgDelegateNum() {
		return avgDelegateNum;
	}

	public void setAvgDelegateNum(double avgDelegateNum) {
		this.avgDelegateNum = avgDelegateNum;
	}

}
