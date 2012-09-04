/*
 * Created Wed Feb 08 15:36:32 CST 2006 by MyEclipse Hibernate Tool.
 */
package com.frameworkset.platform.sysmgrcore.entity;

import java.io.Serializable;
import java.util.List;

/**
 * A class that represents a row in the 'td_sm_organization' table. 
 * This class may be customized as it is never re-generated 
 * after being created.
 */
public class Organization
    extends AbstractOrganization
    implements Serializable
{
	/**
	 * 机构管理人员列表，系统管理扩展
	 */
	private List orgAdmins; 
	
	private String orgtreelevel;
	
	
    public String getOrgtreelevel() {
		return orgtreelevel;
	}

	public void setOrgtreelevel(String orgtreelevel) {
		this.orgtreelevel = orgtreelevel;
	}

	/**
     * Simple constructor of Organization instances.
     */
    public Organization()
    {
    	
    }

    /**
     * Constructor of Organization instances given a simple primary key.
     * @param orgId
     */
    public Organization(String orgId)
    {
        super(orgId);
    }

	public List getOrgAdmins() {
		return orgAdmins;
	}

	public void setOrgAdmins(List orgAdmins) {
		this.orgAdmins = orgAdmins;
	}

	
    /* Add customized code below */

}
