/*
 * Created Wed Feb 08 15:36:32 CST 2006 by MyEclipse Hibernate Tool.
 */
package com.frameworkset.platform.sysmgrcore.entity;

import java.io.Serializable;
import java.util.List;

import com.frameworkset.platform.sysmgrcore.manager.db.OrgCacheCallback;

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
	private transient List orgAdmins;
	private transient OrgCacheCallback orgCacheCallback;
	
	
	
	

	private String orgtreelevel;
	private transient boolean loadfather = false;
	private transient boolean loadsubs = false;
//	private transient boolean loadfathers = false;
	public boolean loadsubs()
	{
		return loadsubs;
	}
	
	public boolean putloadsubs(boolean loadsubs)
	{
		this.loadsubs = loadsubs;
		return loadsubs;
	}
	public boolean loadfather()
	{
		return loadfather;
	}
	
	public boolean putloadfather(boolean loadfather)
	{
		this.loadfather = loadfather;
		return loadfather;
	}
//	public boolean loadfathers()
//	{
//		return loadfathers;
//	}
//	
//	public boolean putloadfathers(boolean loadfathers)
//	{
//		this.loadfathers = loadfathers;
//		return loadfathers;
//	}
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

	@Override
	public Organization getParentOrg() {
		if(orgCacheCallback != null )
			orgCacheCallback.loadfather(this);
		return this.parentOrg;
	}

	public void orgCacheCallback(OrgCacheCallback orgCacheCallback) {
		this.orgCacheCallback = orgCacheCallback;
	}

	@Override
	public List getSuborgs() {
		if(orgCacheCallback != null )
			orgCacheCallback.loadsubs(this);
		return this.suborgs;
	}
    /* Add customized code below */

	public boolean containSubOrg(String orgId) {
		if(this.suborgs == null || this.suborgs.size() == 0)
			return false;
		for(int i = 0; i < this.suborgs.size(); i ++)
		{
			Organization org = this.suborgs.get(i);
			if(org.getOrgId().equals(orgId))
				return true;
		}
		return false;
	}

}
