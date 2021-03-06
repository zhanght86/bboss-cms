package com.sany.workflow.business.entity;

import java.io.Serializable;

/**
 * ActNode.java
 * 
 * 节点描述实体类
 * 
 * @author fudk
 * @company SANY Heavy Industry Co, Ltd
 * @creation date 2013-8-15
 * @version $Revision: 3 $
 */
public class ActNode implements Serializable {

	private static final long serialVersionUID = -4473752406812328499L;
	private String actId;// 节点ID 如：usertask1，usertask2
	private String actName;// 节点名称
	private String candidateName;// 审批人，工号或登录名，多人用“，”分割
	private String candidateCNName;// 审批人中文名
	private String realName;// 审批人+部门名称
	private String candidateOrgId;// 抄送部门
	private String candidateOrgName;// 抄送部门名称
	private String nodeDescribe;// 节点描述
	private String approveType;// 审批类型
	private double nodeWorkTime;// 节点工时（小时）
	private int isValid;// 是否有效0否1是
	private int isEdit;// 本节点能否被修改，0否 1可以
	private int isEditAfter;// 是否能修改后续节点 0 不能1能
	private int isAuto;// 自动审批节点 0不是1是
	private int isAutoAfter;// 后续节点自动审批 0不是1是
	private int isRecall;// 可撤回 0不能1能
	private int isCancel;// 可驳回 0 不能 1 能
	private int isDiscard;// 可废弃 0 不能 1 能
	private int isDiscarded;// 可被废弃 0 不能 1 能
	private int isCopy;// 可抄送 0 不能 1 能
	private int isMulti;// 多实例 0 单实例 1 多实例
	private int isMultiDefault;// 默认多实例 0 不是 1 是
	private int isSequential;// 串行 0 并行 1 串行
	private String orgId;// 节点组织
	private String taskUrl;// 待办URL
	private String bussinessControlClass;// 业务控制类

	public int getIsDiscarded() {
		return isDiscarded;
	}

	public void setIsDiscarded(int isDiscarded) {
		this.isDiscarded = isDiscarded;
	}

	public int getIsMultiDefault() {
		return isMultiDefault;
	}

	public void setIsMultiDefault(int isMultiDefault) {
		this.isMultiDefault = isMultiDefault;
	}

	public int getIsMulti() {
		return isMulti;
	}

	public void setIsMulti(int isMulti) {
		this.isMulti = isMulti;
	}

	public int getIsSequential() {
		return isSequential;
	}

	public void setIsSequential(int isSequential) {
		this.isSequential = isSequential;
	}

	public String getActId() {
		return actId;
	}

	public void setActId(String actId) {
		this.actId = actId;
	}

	public String getActName() {
		return actName;
	}

	public void setActName(String actName) {
		this.actName = actName;
	}

	public String getCandidateName() {
		return candidateName;
	}

	public void setCandidateName(String candidateName) {
		this.candidateName = candidateName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getNodeDescribe() {
		return nodeDescribe;
	}

	public void setNodeDescribe(String nodeDescribe) {
		this.nodeDescribe = nodeDescribe;
	}

	public String getApproveType() {
		return approveType;
	}

	public void setApproveType(String approveType) {
		this.approveType = approveType;
	}

	public double getNodeWorkTime() {
		return nodeWorkTime;
	}

	public void setNodeWorkTime(double nodeWorkTime) {
		this.nodeWorkTime = nodeWorkTime;
	}

	public int getIsValid() {
		return isValid;
	}

	public void setIsValid(int isValid) {
		this.isValid = isValid;
	}

	public int getIsEdit() {
		return isEdit;
	}

	public void setIsEdit(int isEdit) {
		this.isEdit = isEdit;
	}

	public int getIsEditAfter() {
		return isEditAfter;
	}

	public void setIsEditAfter(int isEditAfter) {
		this.isEditAfter = isEditAfter;
	}

	public int getIsAuto() {
		return isAuto;
	}

	public void setIsAuto(int isAuto) {
		this.isAuto = isAuto;
	}

	public int getIsAutoAfter() {
		return isAutoAfter;
	}

	public void setIsAutoAfter(int isAutoAfter) {
		this.isAutoAfter = isAutoAfter;
	}

	public int getIsRecall() {
		return isRecall;
	}

	public void setIsRecall(int isRecall) {
		this.isRecall = isRecall;
	}

	public int getIsCancel() {
		return isCancel;
	}

	public void setIsCancel(int isCancel) {
		this.isCancel = isCancel;
	}

	public int getIsDiscard() {
		return isDiscard;
	}

	public void setIsDiscard(int isDiscard) {
		this.isDiscard = isDiscard;
	}

	public int getIsCopy() {
		return isCopy;
	}

	public void setIsCopy(int isCopy) {
		this.isCopy = isCopy;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getTaskUrl() {
		return taskUrl;
	}

	public void setTaskUrl(String taskUrl) {
		this.taskUrl = taskUrl;
	}

	public String getBussinessControlClass() {
		return bussinessControlClass;
	}

	public void setBussinessControlClass(String bussinessControlClass) {
		this.bussinessControlClass = bussinessControlClass;
	}

	public String getCandidateOrgName() {
		return candidateOrgName;
	}

	public void setCandidateOrgName(String candidateOrgName) {
		this.candidateOrgName = candidateOrgName;
	}

	public String getCandidateCNName() {
		return candidateCNName;
	}

	public void setCandidateCNName(String candidateCNName) {
		this.candidateCNName = candidateCNName;
	}

	public String getCandidateOrgId() {
		return candidateOrgId;
	}

	public void setCandidateOrgId(String candidateOrgId) {
		this.candidateOrgId = candidateOrgId;
	}

}
