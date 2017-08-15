package com.frameworkset.platform.sysmgrcore.web.struts.form;

import java.io.Serializable;
import java.util.List;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: bbossgroups</p>
 *
 * @author feng.jing
 * @version 1.0
 */
public class UserOrgManagerForm  implements Serializable{
    public UserOrgManagerForm() {
    }

    private String userId = "";
    private List allOrg = null;
    private List existOrg = null;
    private String orgId;
    private String[] orgList;
    private String orgName;
    private String[] orgIdName;
    private String remark5;

    public String[] getOrgIdName() {
		return orgIdName;
	}

	public void setOrgIdName(String[] orgIdName) {
		this.orgIdName = orgIdName;
	}

	public List getAllOrg() {
        return allOrg;
    }

    public List getExistOrg() {
        return existOrg;
    }

    public String getOrgId() {
        return orgId;
    }

    public String getUserId() {
        return userId;
    }

    public String[] getOrgList() {
        return orgList;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public void setExistOrg(List existOrg) {
        this.existOrg = existOrg;
    }

    public void setAllOrg(List allOrg) {
        this.allOrg = allOrg;
    }

    public void setOrgList(String[] orgList) {
        this.orgList = orgList;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

	public String getRemark5() {
		return remark5;
	}

	public void setRemark5(String remark5) {
		this.remark5 = remark5;
	}


}
