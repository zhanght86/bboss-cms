//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.1/xslt/JavaClass.xsl

package com.frameworkset.platform.sysmgrcore.web.struts.form;

import java.io.Serializable;
import java.util.Collection;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * MyEclipse Struts Creation date: 03-14-2006
 * 
 * XDoclet definition:
 * 
 * @struts:form name="groupManagerForm"
 */
public class GroupListForm implements Serializable{

    /**
     * 在列表中所选择的组
     */
    private String selectGroup;

    /**
     * 构造组列表的标签与值集合
     */
    private Collection groupOfLabelAndValues;

    /**
     * 构造函数
     */
    public GroupListForm() {
        groupOfLabelAndValues = new Vector();
    }

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
     * @return 返回 selectGroup。
     */
    public String getSelectGroup() {
        return selectGroup;
    }

    /**
     * @param selectGroup
     *            要设置的 selectGroup。
     */
    public void setSelectGroup(String selectGroup) {
        this.selectGroup = selectGroup;
    }

    /**
     * @return 返回 groupOfLabelAndValues。
     */
    public Collection getGroupOfLabelAndValues() {
        return groupOfLabelAndValues;
    }

    /**
     * @param groupOfLabelAndValues
     *            要设置的 groupOfLabelAndValues。
     */
    public void setGroupOfLabelAndValues(Collection groupOfLabelAndValues) {
        this.groupOfLabelAndValues = groupOfLabelAndValues;
    }
}