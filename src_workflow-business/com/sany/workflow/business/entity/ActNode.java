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
	private String realName;// 审批人姓名，多人用“，”分割
	private String nodeDescribe;// 节点描述
	private String approveType;// 审批类型
	private String editAfter;// 能修改后续节点,0 否 1可以
	private String canEdit;// 本节点能否被修改，0否 1可以
	private String autoApprove;// 自动处理，0 否1可以
	private String canRecall;// 能否撤回 ，0否1可以
	private double nodeWorkTime;// 节点工时（小时）
	private String isValid;// 是否有效0否1是
	private String orgId;// 节点组织

	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
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

	public String getEditAfter() {
		return editAfter;
	}

	public void setEditAfter(String editAfter) {
		this.editAfter = editAfter;
	}

	public String getCanEdit() {
		return canEdit;
	}

	public void setCanEdit(String canEdit) {
		this.canEdit = canEdit;
	}

	public String getAutoApprove() {
		return autoApprove;
	}

	public void setAutoApprove(String autoApprove) {
		this.autoApprove = autoApprove;
	}

	public String getCanRecall() {
		return canRecall;
	}

	public void setCanRecall(String canRecall) {
		this.canRecall = canRecall;
	}

	public double getNodeWorkTime() {
		return nodeWorkTime;
	}

	public void setNodeWorkTime(double nodeWorkTime) {
		this.nodeWorkTime = nodeWorkTime;
	}

}
