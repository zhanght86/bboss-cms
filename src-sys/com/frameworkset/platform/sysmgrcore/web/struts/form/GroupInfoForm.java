//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_4.1.0/xslt/JavaClass.xsl

package com.frameworkset.platform.sysmgrcore.web.struts.form;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * MyEclipse Struts Creation date: 03-15-2006
 * 
 * XDoclet definition:
 * 
 * @struts.form name="groupInfoForm"
 */
public class GroupInfoForm implements Serializable{

	// --------------------------------------------------------- Instance
	// Variables

	/**
	 * 针对组的描述
	 */
	private String groupDesc;

	/**
	 * 组的名称
	 */
	private String groupName;

	/**
	 * 组的ID
	 */
	private String groupId;
	
	private String createflag;
	
	/**
	 * 父节点ID
	 */
	private String parentId;

	/**
	 * 需要 GroupInfo 执行的操作
	 */
	private int action;

	// --------------------------------------------------------- Methods

	/**
	 * Method validate
	 * 
	 * @param mapping
	 * @param request
	 * @return ActionErrors
	 */
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		return null;
	}

	/**
	 * Method reset
	 * 
	 * @param mapping
	 * @param request
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
	}

	/**
	 * Returns the groupDesc.
	 * 
	 * @return String
	 */
	public String getGroupDesc() {
		return groupDesc;
	}

	/**
	 * Set the groupDesc.
	 * 
	 * @param groupDesc
	 *            The groupDesc to set
	 */
	public void setGroupDesc(String groupDesc) {
		this.groupDesc = groupDesc;
	}

	/**
	 * Returns the groupName.
	 * 
	 * @return String
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * Set the groupName.
	 * 
	 * @param groupName
	 *            The groupName to set
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	/**
	 * Returns the groupId.
	 * 
	 * @return
	 */
	public String getGroupId() {
		return groupId;
	}

	/**
	 * Set the groupId.
	 * 
	 * @param groupId
	 */
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getCreateflag() {
		return createflag;
	}

	public void setCreateflag(String createflag) {
		this.createflag = createflag;
	}

}
