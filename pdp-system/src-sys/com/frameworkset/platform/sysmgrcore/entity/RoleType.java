package com.frameworkset.platform.sysmgrcore.entity;

import java.io.Serializable;

public class RoleType implements Serializable
{
	private String roleTypeID;
	private String typeName;
	private String typeDesc;
	private String creatorUserId;
	private String creatorOrgId;
	public String getCreatorUserId() {
		if(creatorUserId == null || "".equals(creatorUserId)){
			return "1";
		}
		return creatorUserId;
	}
	public void setCreatorUserId(String creatorUserId) {
		this.creatorUserId = creatorUserId;
	}
	public String getCreatorOrgId() {
		if(creatorOrgId == null || "".equals(creatorOrgId)){
			return "1";
		}
		return creatorOrgId;
	}
	public void setCreatorOrgId(String creatorOrgId) {
		this.creatorOrgId = creatorOrgId;
	}
	public String getTypeDesc()
	{
		return typeDesc;
	}
	public void setTypeDesc(String typeDesc)
	{
		this.typeDesc = typeDesc;
	}
	public String getTypeName()
	{
		return typeName;
	}
	public void setTypeName(String typeName)
	{
		this.typeName = typeName;
	}
	public String getRoleTypeID()
	{
		return roleTypeID;
	}
	public void setRoleTypeID(String roleTypeID)
	{
		this.roleTypeID = roleTypeID;
	}
}
