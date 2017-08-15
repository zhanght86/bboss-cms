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

import java.util.List;

/**
 * @todo 个人统计实体
 * @author tanx
 * @date 2014年5月8日
 * 
 */
public class PersonalCounter {

	private String orgId;// 部门id

	private int userId;// 用户id

	private String userName;// 登录账号

	private String realName;// 真实姓名

	private String orgName;// 部门全称

	private int startNum;// 启动流程数

	private int dealNum;// 处理次数

	private int delegateNum;// 转办次数

	private int delegatedNum;// 被转办次数

	private int entrustNum;// 委托次数

	private int entrustedNum;// 委托次数

	private int rejectNum;// 驳回次数

	private int discardNum;// 废弃次数

	private int cancelNum;// 撤销次数

	private String startEff;// 启动效率

	private String dealEff;// 处理效率

	private String delegateEff;// 转办效率

	private String rejectEff;// 驳回效率

	private String discardEff;// 废弃效率

	private String cancelEff;// 撤销效率

	private String entrustEff;// 委托效率

	private String entrustedEff;// 被委托效率

	private String processId;// 流程实例Id

	private String processName;// 流程名称

	private String businessKey;// 业务主题

	public String getEntrustEff() {
		return entrustEff;
	}

	public void setEntrustEff(String entrustEff) {
		this.entrustEff = entrustEff;
	}

	public String getEntrustedEff() {
		return entrustedEff;
	}

	public void setEntrustedEff(String entrustedEff) {
		this.entrustedEff = entrustedEff;
	}

	private List<PersonalDealTask> dealTaskList;// 个人处理任务信息

	public int getEntrustedNum() {
		return entrustedNum;
	}

	public void setEntrustedNum(int entrustedNum) {
		this.entrustedNum = entrustedNum;
	}

	public List<PersonalDealTask> getDealTaskList() {
		return dealTaskList;
	}

	public void setDealTaskList(List<PersonalDealTask> dealTaskList) {
		this.dealTaskList = dealTaskList;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getProcessName() {
		return processName;
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

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getBusinessKey() {
		return businessKey;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public String getRejectEff() {
		return rejectEff;
	}

	public void setRejectEff(String rejectEff) {
		this.rejectEff = rejectEff;
	}

	public String getDiscardEff() {
		return discardEff;
	}

	public void setDiscardEff(String discardEff) {
		this.discardEff = discardEff;
	}

	public String getCancelEff() {
		return cancelEff;
	}

	public void setCancelEff(String cancelEff) {
		this.cancelEff = cancelEff;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
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

	public int getDelegatedNum() {
		return delegatedNum;
	}

	public void setDelegatedNum(int delegatedNum) {
		this.delegatedNum = delegatedNum;
	}

	public int getEntrustNum() {
		return entrustNum;
	}

	public void setEntrustNum(int entrustNum) {
		this.entrustNum = entrustNum;
	}

	public String getStartEff() {
		return startEff;
	}

	public void setStartEff(String startEff) {
		this.startEff = startEff;
	}

	public String getDealEff() {
		return dealEff;
	}

	public void setDealEff(String dealEff) {
		this.dealEff = dealEff;
	}

	public String getDelegateEff() {
		return delegateEff;
	}

	public void setDelegateEff(String delegateEff) {
		this.delegateEff = delegateEff;
	}

}
