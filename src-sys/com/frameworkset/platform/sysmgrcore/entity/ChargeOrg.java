package com.frameworkset.platform.sysmgrcore.entity;

import java.io.Serializable;

public class ChargeOrg implements Serializable{
	//当前用户Id
	private String userId;
	//用户所在机构id
	private String orgId;
//	用户所在机构名称
	private String orgName;
//	用户所在机构的岗位
	private String jobName;
	//主管处室Id
	private String ChargeOrgId;
	//主管处室Name
	private String ChargeOrgName;
//	主管处室级次
	private String layer;
	//主管岗位Id
	private String SatrapJobId;
	//主管岗位名称
	private String SatrapJobName;
	//主管人员id
	private String SatrapId;
	//主管人员帐号
	private String SatrapName;
	//主管人员实名
	private String SatrapRealName;
	

	
	public String getChargeOrgId() {
		return ChargeOrgId;
	}
	public String getSatrapRealName() {
		return SatrapRealName;
	}
	public void setSatrapRealName(String satrapRealName) {
		SatrapRealName = satrapRealName;
	}
	public void setChargeOrgId(String chargeOrgId) {
		ChargeOrgId = chargeOrgId;
	}
	public String getChargeOrgName() {
		return ChargeOrgName;
	}
	public void setChargeOrgName(String chargeOrgName) {
		ChargeOrgName = chargeOrgName;
	}
	public String getLayer() {
		return layer;
	}
	public void setLayer(String layer) {
		this.layer = layer;
	}
	public String getSatrapId() {
		return SatrapId;
	}
	public void setSatrapId(String satrapId) {
		SatrapId = satrapId;
	}
	public String getSatrapName() {
		return SatrapName;
	}
	public void setSatrapName(String satrapName) {
		SatrapName = satrapName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getSatrapJobId() {
		return SatrapJobId;
	}
	public void setSatrapJobId(String satrapJobId) {
		SatrapJobId = satrapJobId;
	}
	public String getSatrapJobName() {
		return SatrapJobName;
	}
	public void setSatrapJobName(String satrapJobName) {
		SatrapJobName = satrapJobName;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	

}
