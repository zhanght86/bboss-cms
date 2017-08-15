package com.frameworkset.platform.sysmgrcore.web.struts.form;

import java.io.Serializable;

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
public class RoleManagerForm  implements Serializable{
    public RoleManagerForm() {
    }

    private String roleType;
    private String roleId;
    private String roleDesc;
    private String roleName;
    private String userId;
    private String userName;
    private String[] userIds;
    private String[] alloper;
    private String[] groupId;    
    private String orgId;
    private int action; 

    public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getRoleDesc() {
        return roleDesc;
    }

    public String getRoleId() {
        return roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public String[] getAlloper() {
		return alloper;
	}

	public void setAlloper(String[] alloper) {
		this.alloper = alloper;
	}

	public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String[] getUserIds() {
        return userIds;
    }
    
    public String[] getGroupId() {
        return groupId;
    }
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserIds(String[] userIds) {
        this.userIds = userIds;
    }
    
    public void setGroupId(String[] groupId) {
        this.groupId = groupId;
    }

	public String getRoleType()
	{
		return roleType;
	}

	public void setRoleType(String roletype)
	{
		this.roleType = roletype;
	}    
}
