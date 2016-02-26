/*
 * WARNING: DO NOT EDIT THIS FILE. This is a generated file that is synchronized
 * by MyEclipse Hibernate tool integration.
 *
 * Created Wed Feb 08 15:36:32 CST 2006 by MyEclipse Hibernate Tool.
 */
package com.frameworkset.platform.sysmgrcore.entity;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * A class that represents a row in the td_sm_organization table. You can
 * customize the behavior of this class by editing the class,
 * {@link Organization()}. WARNING: DO NOT EDIT THIS FILE. This is a generated
 * file that is synchronized * by MyEclipse Hibernate tool integration.
 * @@org.jboss.cache.aop.AopMarker
 */
public abstract class AbstractOrganization  implements
		Serializable {
 

	/** The composite primary key value. */
	private String orgId;

	/** The value of the simple orgSn property. */
	private String orgSn;

	/** The value of the simple orgName property. */
	private String orgName;

	/** The value of the simple parentId property. */
	private String parentId;

	/** The value of the simple path property. */
	private String path;

	/** The value of the simple layer property. */
	private String layer;

	/** The value of the simple children property. */
	private String children;

	/** The value of the simple code property. */
	private String code;

	/** The value of the simple jp property. */
	private String jp;

	/** The value of the simple qp property. */
	private String qp;

	/** The value of the simple creatingtime property. */
	private Date creatingtime;

	/** The value of the simple creator property. */
	private String creator;

	/** The value of the simple remark1 property. */
	private String remark1;

	/** The value of the simple remark2 property. */
	private String remark2;

    /**
     * 机构类型
     */
	private String remark3;
	
	/**
	 * add by ge.tao
	 * 2007-11-07
	 * 表示机构是否是业务部门 0-是 1-不是 默认为1
	 */
    private String ispartybussiness;
    
    
	
	private String remark4;

	/**
	 * 机构别名
	 */
	private String remark5;

	/**
	 * 机构编号
	 */
	private String orgnumber;
	

	/**
	 * 机构描述
	 */
	private String orgdesc;
	//主管处室id
	private String chargeOrgId;
	//主管岗位id
	private String satrapJobId;
	/**
	 * 子机构列表
	 */
	protected List<Organization> suborgs;

	/**
	 * 机构实体所关联的岗位
	 */
	private Set jobs = null;

	/**
	 * 用户、岗位、机构实体集合
	 */
	private Set userjoborgSet = null;

	/**
	 * 机构和岗位实体集合
	 */
	private Set orgjobSet = null;

	/**
	 * 机构和组实体集合
	 */
	private Set orggroupSet = null;

	/**
	 * 当前机构的父机构
	 */
	protected Organization parentOrg = null;
	
	/**
	 * 当前机构的父机构
	 */
	private String org_level;
	

	/**
	 * 机构区码
	 */
	private String org_xzqm;
	
	/**
	 * 是否直属局
	 * */
	private String isdirectlyparty = "0";
	
	/**
	 * 是否涉外局
	 * */
	private String isforeignparty = "0";
	
	/**
	 * 是否稽查局
	 * */
	private String isjichaparty = "0";
	
	/**
	 * 是否直接管户单位
	 * */
	private String isdirectguanhu = "0";
	
	/**
	 * 获取机构的id拼接的路径，例如：
	 * 1/2/3
	 * @return
	 */	
	public String getUniqPath()
	{
		if(parentOrg == null 
				|| this.parentId == "0" 
				|| this.parentId.equals("0"))
		{
			return this.orgId;
		}
		
		return this.parentOrg.getUniqPath() + "/" +this.orgId;
	}

	/**
	 * @return 返回 orggroupSet。
	 */
	public Set getOrggroupSet() {
		return orggroupSet;
	}

	/**
	 * @param orggroupSet
	 *            要设置的 orggroupSet。
	 */
	public void setOrggroupSet(Set orggroupSet) {
		this.orggroupSet = orggroupSet;
	}

	/**
	 * @return 返回 orgjobSet。
	 */
	public Set getOrgjobSet() {
		return orgjobSet;
	}

	/**
	 * @param orgjobSet
	 *            要设置的 orgjobSet。
	 */
	public void setOrgjobSet(Set orgjobSet) {
		this.orgjobSet = orgjobSet;
	}

	/**
	 * @return 返回 userjoborgSet。
	 */
	public Set getUserjoborgSet() {
		return userjoborgSet;
	}

	/**
	 * @param userjoborgSet
	 *            要设置的 userjoborgSet。
	 */
	public void setUserjoborgSet(Set userjoborgSet) {
		this.userjoborgSet = userjoborgSet;
	}

	/**
	 * @return 返回 jobs。
	 */
	public Set getJobs() {
		return jobs;
	}

	/**
	 * @param jobs
	 *            要设置的 jobs。
	 */
	public void setJobs(Set jobs) {
		this.jobs = jobs;
	}

	/**
	 * Simple constructor of AbstractOrganization instances.
	 */
	public AbstractOrganization() {
	}

	/**
	 * Constructor of AbstractOrganization instances given a simple primary key.
	 * 
	 * @param orgId
	 */
	public AbstractOrganization(String orgId) {
		this.setOrgId(orgId);
	}

	/**
	 * Return the simple primary key value that identifies this object.
	 * 
	 * @return String
	 */
	public String getOrgId() {
		return orgId;
	}

	/**
	 * Set the simple primary key value that identifies this object.
	 * 
	 * @param orgId
	 */
	public void setOrgId(String orgId) {
		 
		this.orgId = orgId;
	}

	/**
	 * Return the value of the ORG_SN column.
	 * 
	 * @return Integer
	 */
	public String getOrgSn() {
		return this.orgSn;
	}

	/**
	 * Set the value of the ORG_SN column.
	 * 
	 * @param orgSn
	 */
	public void setOrgSn(String orgSn) {
		this.orgSn = orgSn;
	}

	/**
	 * Return the value of the ORG_NAME column.
	 * 
	 * @return String
	 */
	public String getOrgName() {
		return this.orgName;
	}

	/**
	 * Set the value of the ORG_NAME column.
	 * 
	 * @param orgName
	 */
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	/**
	 * Return the value of the PARENT_ID column.
	 * 
	 * @return String
	 */
	public String getParentId() {
		return this.parentId;
	}

	/**
	 * Set the value of the PARENT_ID column.
	 * 
	 * @param parentId
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	/**
	 * Return the value of the PATH column.
	 * 
	 * @return String
	 */
	public String getPath() {
		return this.path;
	}

	/**
	 * Set the value of the PATH column.
	 * 
	 * @param path
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * Return the value of the LAYER column.
	 * 
	 * @return String
	 */
	public String getLayer() {
		return this.layer;
	}

	/**
	 * Set the value of the LAYER column.
	 * 
	 * @param layer
	 */
	public void setLayer(String layer) {
		this.layer = layer;
	}

	/**
	 * Return the value of the CHILDREN column.
	 * 
	 * @return String
	 */
	public String getChildren() {
		return this.children;
	}

	/**
	 * Set the value of the CHILDREN column.
	 * 
	 * @param children
	 */
	public void setChildren(String children) {
		this.children = children;
	}

	/**
	 * Return the value of the CODE column.
	 * 
	 * @return String
	 */
	public String getCode() {
		return this.code;
	}

	/**
	 * Set the value of the CODE column.
	 * 
	 * @param code
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * Return the value of the JP column.
	 * 
	 * @return String
	 */
	public String getJp() {
		return this.jp;
	}

	/**
	 * Set the value of the JP column.
	 * 
	 * @param jp
	 */
	public void setJp(String jp) {
		this.jp = jp;
	}

	/**
	 * Return the value of the QP column.
	 * 
	 * @return String
	 */
	public String getQp() {
		return this.qp;
	}

	/**
	 * Set the value of the QP column.
	 * 
	 * @param qp
	 */
	public void setQp(String qp) {
		this.qp = qp;
	}

	/**
	 * Return the value of the CREATINGTIME column.
	 * 
	 * @return java.util.Date
	 */
	public Date getCreatingtime() {
		return this.creatingtime;
	}

	/**
	 * Set the value of the CREATINGTIME column.
	 * 
	 * @param creatingtime
	 */
	public void setCreatingtime(Date creatingtime) {
		this.creatingtime = creatingtime;
	}

	/**
	 * Return the value of the CREATOR column.
	 * 
	 * @return String
	 */
	public String getCreator() {
		if(this.creator == null || "".equals(this.creator)){
			return "1";
		}
		return this.creator;
	}

	/**
	 * Set the value of the CREATOR column.
	 * 
	 * @param creator
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}

	/**
	 * Return the value of the REMARK1 column.
	 * 
	 * @return String
	 */
	public String getRemark1() {
		return this.remark1;
	}

	/**
	 * Set the value of the REMARK1 column.
	 * 
	 * @param remark1
	 */
	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}

	/**
	 * Return the value of the REMARK2 column.
	 * 
	 * @return String
	 */
	public String getRemark2() {
		return this.remark2;
	}

	/**
	 * Set the value of the REMARK2 column.
	 * 
	 * @param remark2
	 */
	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}

	/**
	 * Return the value of the REMARK3 column.
	 * 
	 * @return String
	 */
	public String getRemark3() {
		return this.remark3;
	}

	/**
	 * Set the value of the REMARK3 column.
	 * 
	 * @param remark3
	 */
	public void setRemark3(String remark3) {
		this.remark3 = remark3;
	}

	/**
	 * Implementation of the equals comparison on the basis of equality of the
	 * primary key values.
	 * 
	 * @param rhs
	 * @return boolean
	 */
	public boolean equals(Object rhs) {
		if (rhs == null)
			return false;
		if (!(rhs instanceof Organization))
			return false;
		Organization that = (Organization) rhs;
		if (this.getOrgId() != null && that.getOrgId() != null) {
			if (!this.getOrgId().equals(that.getOrgId())) {
				return false;
			}
		}
		return true;
	}

	 

	public String getOrgdesc() {
		return orgdesc;
	}

	public void setOrgdesc(String orgdesc) {
		this.orgdesc = orgdesc;
	}

	public String getOrgnumber() {
		return orgnumber;
	}

	public void setOrgnumber(String orgnumber) {
		this.orgnumber = orgnumber;
	}

	public String getRemark4() {
		return remark4;
	}

	public void setRemark4(String remark4) {
		this.remark4 = remark4;
	}

	public String getRemark5() {
		return remark5;
	}

	public void setRemark5(String remark5) {
		this.remark5 = remark5;
	}

	public Organization getParentOrg() {
		return parentOrg;
	}

	public void setParentOrg(Organization parentOrg) {
		this.parentOrg = parentOrg;
	}

	public String getChargeOrgId() {
		return chargeOrgId;
	}

	public void setChargeOrgId(String chargeOrgId) {
		this.chargeOrgId = chargeOrgId;
	}

	public String getSatrapJobId() {
		return satrapJobId;
	}

	public void setSatrapJobId(String satrapJobId) {
		this.satrapJobId = satrapJobId;
	}

	public List<Organization> getSuborgs() {
		return suborgs;
	}

	public void setSuborgs(List<Organization> suborgs) {
		this.suborgs = suborgs;
	}
	
	public void addSubOrg(Organization org) {
		if(this.suborgs == null)
		{
			suborgs = new ArrayList<Organization>();			
		}
		suborgs.add(org);
		//org.setParentOrg((Organization)this);
			
		
	}
	
	public void deleteSubOrg(Organization org) {
		if(this.suborgs == null)
		{
			return;			
		}
		suborgs.remove(org);
		
		//org.setParentOrg((Organization)this);
			
		
	}

	public String getIspartybussiness() {
		return ispartybussiness;
	}

	public void setIspartybussiness(String ispartybussiness) {
		this.ispartybussiness = ispartybussiness;
	}

	public String getOrg_level() {
		return org_level;
	}

	public void setOrg_level(String org_level) {
		this.org_level = org_level;
	}

	public String getOrg_xzqm() {
		return org_xzqm;
	}

	public void setOrg_xzqm(String org_xzqm) {
		this.org_xzqm = org_xzqm;
	}

	public String getIsdirectguanhu() {
		return isdirectguanhu;
	}

	public void setIsdirectguanhu(String isdirectguanhu) {
		this.isdirectguanhu = isdirectguanhu;
	}

	public String getIsdirectlyparty() {
		return isdirectlyparty;
	}

	public void setIsdirectlyparty(String isdirectlyparty) {
		this.isdirectlyparty = isdirectlyparty;
	}

	public String getIsforeignparty() {
		return isforeignparty;
	}

	public void setIsforeignparty(String isforeignparty) {
		this.isforeignparty = isforeignparty;
	}

	public String getIsjichaparty() {
		return isjichaparty;
	}

	public void setIsjichaparty(String isjichaparty) {
		this.isjichaparty = isjichaparty;
	}

	
}
