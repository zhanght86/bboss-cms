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
 * <p>Company: 三一集团</p>
 *
 * @author feng.jing
 * @version 1.0
 */
public class UserGroupManagerForm  implements Serializable {
    public UserGroupManagerForm() {
    }

    private String userId = "";
    private List allGroup = null;
    private List existGroup = null;
    private String[] groupId;
    private String orgId;

    public List getAllGroup() {
        return allGroup;
    }

    public List getExistGroup() {
        return existGroup;
    }

    public String[] getGroupId() {
        return groupId;
    }

    public String getOrgId() {
        return orgId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public void setGroupId(String[] groupId) {
        this.groupId = groupId;
    }

    public void setExistGroup(List existGroup) {
        this.existGroup = existGroup;
    }

    public void setAllGroup(List allGroup) {
        this.allGroup = allGroup;
    }


}
