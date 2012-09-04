package com.frameworkset.platform.sysmgrcore.web.struts.form;

import java.io.Serializable;
import java.util.List;

import org.apache.struts.action.ActionForm;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 *
 * @author feng.jing
 * @version 1.0
 */
public class UserRoleManagerForm  implements Serializable {
    public UserRoleManagerForm() {
    }

    private String userId = "";
    private List allRole = null;
    private List existRole = null;
    private String[] roleId;
    private String orgId;

    public List getAllRole() {
        return allRole;
    }

    public List getExistRole() {
        return existRole;
    }

    public String getOrgId() {
        return orgId;
    }

    public String[] getRoleId() {
        return roleId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setRoleId(String[] roleId) {
        this.roleId = roleId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public void setExistRole(List existRole) {
        this.existRole = existRole;
    }

    public void setAllRole(List allRole) {
        this.allRole = allRole;
    }
}
