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
 * @todo 个人统计查询条件
 * @author tanx
 * @date 2014年5月8日
 * 
 */
public class PersonalCountCondition {

	private String orgId;// 所属部门

	@RequestParam(dateformat = "yyyy-MM-dd")
	private Timestamp count_start_time;// 统计开始时间

	@RequestParam(dateformat = "yyyy-MM-dd")
	private Timestamp count_end_time;// 统计结束时间

	private String realName;// 姓名

	private String userName;// 用户id

	private int startNum;// 启动流程数

	private int dealNum;// 处理次数

	private int delegateNum;// 转办次数

	private int entrustNum;// 委托次数

	private int entrustedNum;// 被委托次数

	private int rejectNum;// 驳回次数

	private int discardNum;// 废弃次数

	private int cancelNum;// 撤销次数

	public int getEntrustNum() {
		return entrustNum;
	}

	public void setEntrustNum(int entrustNum) {
		this.entrustNum = entrustNum;
	}

	public int getEntrustedNum() {
		return entrustedNum;
	}

	public void setEntrustedNum(int entrustedNum) {
		this.entrustedNum = entrustedNum;
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

	public int getStartNum() {
		return startNum;
	}

	public void setStartNum(int startNum) {
		this.startNum = startNum;
	}

	public int getDealNum() {
		return dealNum;
	}

	public void setDealNum(int dealNum) {
		this.dealNum = dealNum;
	}

	public int getDelegateNum() {
		return delegateNum;
	}

	public void setDelegateNum(int delegateNum) {
		this.delegateNum = delegateNum;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public Timestamp getCount_start_time() {
		return count_start_time;
	}

	public void setCount_start_time(Timestamp count_start_time) {
		this.count_start_time = count_start_time;
	}

	public Timestamp getCount_end_time() {
		return count_end_time;
	}

	public void setCount_end_time(Timestamp count_end_time) {
		this.count_end_time = count_end_time;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

}
