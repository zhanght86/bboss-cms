/*
 *
 * Title:
 *
 * Copyright: Copyright (c) 2004
 *
 * Company: iSany Co., Ltd
 *
 * All right reserved.
 *
 * Created on 2004-6-10
 *
 * JDK version used		:1.4.1
 *
 * Modification history:
 *
 */

package com.frameworkset.platform.sysmgrcore.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 许可的数据封装。
 * 权限信息封装
 * @author gao.tang,chunqiu.zhao
 * @since 2007.10.21
 */
public class Permission implements Serializable {
	
	
	private static final long serialVersionUID = -1738252642751791198L;
	
	private String resTypeId = null;
	
	private String opId = null;

	private String resName = null;//资源名称
	
	private String resId = null;//资源标识
	
	private String resTypeName = null;//资源类型
	
	private String opName = null;//资源操作
	
	private String roleName = null;//角色名称
	
	private String roleId = null;
	
	private String userName = null;//用户名称
	
	private String types = null;
	
	private String jobName = null;//岗位名称
	
	private String name = null;
	
	private String type = null;
	
	private String id = null;
	
	private String special = null;

	private String resResource = null;
	
	private Date sDate = null;
	
	private Date eDate = null;
	
	private int auto = 0;//是否是系统资源

	public String getSpecial() {
		return special;
	}

	public void setSpecial(String special) {
		this.special = special;
	}

	public String getOpName() {
		return opName;
	}

	public void setOpName(String opName) {
		this.opName = opName;
	}

	public String getResId() {
		return resId;
	}

	public void setResId(String resId) {
		this.resId = resId;
	}

	public String getResName() {
		return resName;
	}

	public void setResName(String resName) {
		this.resName = resName;
	}

	public String getResTypeName() {
		return resTypeName;
	}

	public void setResTypeName(String resTypeName) {
		this.resTypeName = resTypeName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getOpId() {
		return opId;
	}

	public void setOpId(String opId) {
		this.opId = opId;
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

	public String getTypes() {
		return types;
	}

	public void setTypes(String types) {
		this.types = types;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getResResource() {
		return resResource;
	}

	public void setResResource(String resResource) {
		this.resResource = resResource;
	}

	public Date getSDate() {
		return sDate;
	}

	public void setSDate(Date date) {
		sDate = date;
	}

	public Date getEDate() {
		return eDate;
	}

	public void setEDate(Date date) {
		eDate = date;
	}

	public int getAuto() {
		return auto;
	}

	public void setAuto(int auto) {
		this.auto = auto;
	}

}
