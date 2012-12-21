package com.frameworkset.platform.sysmgrcore.web.struts.form;

import java.io.Serializable;

import org.apache.struts.action.ActionForm;

/**
 * 资源管理＝＝资源操作授予＝＝角色操作的保存
 * @author 
 * @file RoleOperForm.java
 * Created on: Apr 14, 2006
 */
public class RoleOperForm implements Serializable{
	/**
	 * 操作列表
	 */
	String[] opId;
	
	/**
	 * 资源id
	 */
	String resId;
	
	/**
	 * 资源类型
	 */
	String resTypeId;
	
	public String[] getOpId() {
		return opId;
	}
	public void setOpId(String[] opId) {
		this.opId = opId;
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
}
