/**
 * 
 */
package com.frameworkset.platform.sysmgrcore.entity;

import java.io.Serializable;

/**
 * <p>Title: ResOpOriginObj.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date 2008-1-2 11:47:47
 * @author ge.tao
 * @version 1.0
 */
public class ResOpOriginObj implements Serializable{
	
	
	
	private String opId;
	
	private String resId;
	
	private String resTypeId;
	
	private String roleId;
	
	private String roleType;
	
	private String origineId;
	
	private String origineType;
		
	

	public String getOrigineType() {
		return origineType;
	}

	public void setOrigineType(String origineType) {
		this.origineType = origineType;
	}

	

	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	public String getOpId() {
		return opId;
	}

	public void setOpId(String opId) {
		this.opId = opId;
	}

	public String getOrigineId() {
		return origineId;
	}

	public void setOrigineId(String origineId) {
		this.origineId = origineId;
	}

	public String getResId() {
		return resId;
	}

	public void setResId(String resId) {
		this.resId = resId;
	}

	public String getResTypeId() {
		return resTypeId;
	}

	public void setResTypeId(String resTypeId) {
		this.resTypeId = resTypeId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	

}
