package com.sany.workflow.business.entity;

import java.util.List;

/**
 * ProIns.java
 * 
 * 流程页面边界类
 * 
 * @author fudk
 * @company SANY Heavy Industry Co, Ltd
 * @creation date 2013-8-15
 * @version $Revision: 3 $
 */
public class ProIns {

	private String proInsId;// 流程实例Id
	private String nowtaskId;// 当前节点Id
	private String nowTaskKey;// 当前节点Id
	private String title;// 流程标题
	private String operateType;// 操作类型，pass通过，reject驳回，toEnd废弃，turnTo转办
	private String rejectToActId;// 驳回节点key
	private String toActName;// 驳回节点名称
	private int isReturn;// 驳回后提交是否返回
	private String dealOption;// 处理操作 (通过,驳回等)
	private String dealReason;// 处理意见
	private String dealRemak;// 备注(日志记录备注部分)
	private String businessKey;// 业务单据号
	private String businessType;// 业务类型，用于分类查询流程
	private int enableEdit;// 是否可修改
	private List<ActNode> acts;// 审批节点信息列表
	private String userAccount;// 用户登录账号
	private String userName;// 用户名称
	private String delegateUser;// 被转办用户名称
	private String delegateUserName;// 用户名称
	private String proOrderId;// 业务单显示单号

	private String nowTaskFromUser;// 实例当前任务的转办/委托用户
	private String nowTaskToUser;// 实例当前任务的被转办/委托用户
	private String toTaskKey;// 跳转到下个任务节点的key

	public String getDealOption() {
		return dealOption;
	}

	public void setDealOption(String dealOption) {
		this.dealOption = dealOption;
	}

	public String getDealRemak() {
		return dealRemak;
	}

	public void setDealRemak(String dealRemak) {
		this.dealRemak = dealRemak;
	}

	public String getToTaskKey() {
		return toTaskKey;
	}

	public void setToTaskKey(String toTaskKey) {
		this.toTaskKey = toTaskKey;
	}

	public String getNowTaskFromUser() {
		return nowTaskFromUser;
	}

	public void setNowTaskFromUser(String nowTaskFromUser) {
		this.nowTaskFromUser = nowTaskFromUser;
	}

	public String getNowTaskToUser() {
		return nowTaskToUser;
	}

	public void setNowTaskToUser(String nowTaskToUser) {
		this.nowTaskToUser = nowTaskToUser;
	}

	public String getProInsId() {
		return proInsId;
	}

	public void setProInsId(String proInsId) {
		this.proInsId = proInsId;
	}

	public String getNowtaskId() {
		return nowtaskId;
	}

	public void setNowtaskId(String nowtaskId) {
		this.nowtaskId = nowtaskId;
	}

	public String getNowTaskKey() {
		return nowTaskKey;
	}

	public void setNowTaskKey(String nowTaskKey) {
		this.nowTaskKey = nowTaskKey;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

	public String getRejectToActId() {
		return rejectToActId;
	}

	public void setRejectToActId(String rejectToActId) {
		this.rejectToActId = rejectToActId;
	}

	public String getToActName() {
		return toActName;
	}

	public void setToActName(String toActName) {
		this.toActName = toActName;
	}

	public int getIsReturn() {
		return isReturn;
	}

	public void setIsReturn(int isReturn) {
		this.isReturn = isReturn;
	}

	public String getDealReason() {
		return dealReason;
	}

	public void setDealReason(String dealReason) {
		this.dealReason = dealReason;
	}

	public String getBusinessKey() {
		return businessKey;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public int getEnableEdit() {
		return enableEdit;
	}

	public void setEnableEdit(int enableEdit) {
		this.enableEdit = enableEdit;
	}

	public List<ActNode> getActs() {
		return acts;
	}

	public void setActs(List<ActNode> acts) {
		this.acts = acts;
	}

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDelegateUser() {
		return delegateUser;
	}

	public void setDelegateUser(String delegateUser) {
		this.delegateUser = delegateUser;
	}

	public String getDelegateUserName() {
		return delegateUserName;
	}

	public void setDelegateUserName(String delegateUserName) {
		this.delegateUserName = delegateUserName;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getProOrderId() {
		return proOrderId;
	}

	public void setProOrderId(String proOrderId) {
		this.proOrderId = proOrderId;
	}

}
