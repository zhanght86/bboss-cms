package com.sany.workflow.business.entity;


public class SysUser extends com.frameworkset.platform.sysmgrcore.entity.User {

	private static final long serialVersionUID = -5700709320134727999L;
	/**
	 * 园区ID
	 */
	private Integer areaId;
	/**
	 * 园区名称
	 */
	private String areaName;
	/**
	 * 组织ID
	 */
	private String orgId; 
	
	
	public Integer getAreaId() {
		return areaId;
	}
	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
}
