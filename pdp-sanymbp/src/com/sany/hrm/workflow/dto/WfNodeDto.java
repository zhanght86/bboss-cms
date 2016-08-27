package com.sany.hrm.workflow.dto;

import java.util.Date;

/**
 * 流程节点
 * 
 * @author gw_yaoht
 * @version 2012-8-17
 **/
public class WfNodeDto implements java.io.Serializable {
	private static final long serialVersionUID = 4158080450266962621L;
	/**
     * 
     */
	private String candidateGroups;
	private String candidateGroupsName;
	private String candidateUsers;
	private String candidateUsersName;
	private String createPerson;
	private Date createTime;
	private String deploymentCode;
	private String describing;
	private String name;
	private Integer taxisNum;
	private String type;
	private String typeName;
	private String updatePerson;
	private Date updateTime;
	private String url;
	private String wfNodeCode;

	public String getCandidateGroups() {
		return this.candidateGroups;
	}

	public String getCandidateGroupsName() {
		return this.candidateGroupsName;
	}

	public String getCandidateUsers() {
		return this.candidateUsers;
	}

	public String getCandidateUsersName() {
		return this.candidateUsersName;
	}

	public String getCreatePerson() {
		return this.createPerson;
	}

	// Constructors

	public Date getCreateTime() {
		return this.createTime;
	}

	public String getDeploymentCode() {
		return this.deploymentCode;
	}

	public String getDescribing() {
		return this.describing;
	}

	public String getName() {
		return this.name;
	}

	public Integer getTaxisNum() {
		return this.taxisNum;
	}

	public String getType() {
		return this.type;
	}

	public String getTypeName() {
		return this.typeName;
	}

	public String getUpdatePerson() {
		return this.updatePerson;
	}

	public Date getUpdateTime() {
		return this.updateTime;
	}

	public String getUrl() {
		return this.url;
	}

	public String getWfNodeCode() {
		return this.wfNodeCode;
	}

	public void setCandidateGroups(String candidateGroups) {
		this.candidateGroups = candidateGroups;
	}

	public void setCandidateGroupsName(String candidateGroupsName) {
		this.candidateGroupsName = candidateGroupsName;
	}

	public void setCandidateUsers(String candidateUsers) {
		this.candidateUsers = candidateUsers;
	}

	public void setCandidateUsersName(String candidateUsersName) {
		this.candidateUsersName = candidateUsersName;
	}

	public void setCreatePerson(String createPerson) {
		this.createPerson = createPerson;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setDeploymentCode(String deploymentCode) {
		this.deploymentCode = deploymentCode;
	}

	public void setDescribing(String describing) {
		this.describing = describing;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTaxisNum(Integer taxisNum) {
		this.taxisNum = taxisNum;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public void setUpdatePerson(String updatePerson) {
		this.updatePerson = updatePerson;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setWfNodeCode(String wfNodeCode) {
		this.wfNodeCode = wfNodeCode;
	}
}