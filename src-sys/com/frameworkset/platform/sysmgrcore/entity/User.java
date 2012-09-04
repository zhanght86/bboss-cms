/*
 * Created Tue Feb 14 11:47:28 CST 2006 by MyEclipse Hibernate Tool.
 */
package com.frameworkset.platform.sysmgrcore.entity;

import java.io.Serializable;

/**
 * A class that represents a row in the 'td_sm_user' table. 
 * This class may be customized as it is never re-generated 
 * after being created.
 */
public class User
    extends AbstractUser
    implements Serializable
{
	private String orgName;
	
	private String job_name;
	
	private String orgs;
    
	/**
	 * 保存主机构
	 */
	protected String mainOrg;
	
	/**
     * Simple constructor of User instances.
     */
    public User()
    {
    }

    /**
     * Constructor of User instances given a simple primary key.
     * @param userId
     */
    public User(Integer userId)
    {
        super(userId);
    }

	public String getOrgs() {
		return orgs;
	}

	public void setOrgs(String orgs) {
		this.orgs = orgs;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getJob_name() {
		return job_name;
	}

	public void setJob_name(String job_name) {
		this.job_name = job_name;
	}

	public String getMainOrg() {
		return mainOrg;
	}

	public void setMainOrg(String mainOrg) {
		this.mainOrg = mainOrg;
	}

	public String toString() {
		StringBuffer userinfo = new StringBuffer();
		userinfo.append("[user_id=").append(getUserId()).append(",user_name=")
			.append(getUserName()).append(",").append("USER_SN=").append(getUserSn())
			.append(",USER_PASSWORD=").append(getUserPassword())
			.append(",USER_REALNAME=").append(this.getUserRealname())
			.append(",USER_PINYIN=").append(this.getUserPinyin())
			.append(",USER_SEX=").append(this.getUserSex())
			.append(",USER_HOMETEL=").append(this.getUserHometel())
			.append(",USER_WORKTEL=").append(this.getUserWorktel())
			.append(",USER_TYPE=").append(this.getUserType())
			.append("]");
		return userinfo.toString();
	}

	
    /* Add customized code below */

}
