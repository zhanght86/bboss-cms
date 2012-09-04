//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.1/xslt/JavaClass.xsl

package com.frameworkset.platform.sysmgrcore.web.struts.form;

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

/** 
 * MyEclipse Struts
 * Creation date: 03-08-2006
 * 
 * XDoclet definition:
 * @struts:form name="userManagerForm"
 */
public class UserManagerForm  implements Serializable{

	// --------------------------------------------------------- Instance Variables
    private String orgId = null; //机构ID
    private List userList = null; //用户列表
	// --------------------------------------------------------- Methods

	/** 
	 * Method validate
	 * @param mapping
	 * @param request
	 * @return ActionErrors
	 */
	public ActionErrors validate(
		ActionMapping mapping,
		HttpServletRequest request) {

        ActionErrors errors = new ActionErrors();

        if ((orgId == null) || (orgId.length() < 1))
            errors.add("orgId", new ActionMessage("机构ID不能为NULL"));

        return errors;
	}

	/** 
	 * Method reset
	 * @param mapping
	 * @param request
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {

		this.orgId = null;
	}

	/**
	 * @param string
	 * @return
	 */
	public String getOrgId() {
		return this.orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	/**
	 * @return 返回 userList。
	 */
	public List getUserList() {
		return userList;
	}
	/**
	 * @param userList 要设置的 userList。
	 */
	public void setUserList(List userList) {
		this.userList = userList;
	}
}