package com.frameworkset.platform.security.service.entity;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 常用字段：
 * 
 * orgId,
 * orgName,
 * parentId,
 * code,
 * creatingtime,
 * orgnumber,
 * orgdesc,
 * remark5, 显示名称
 * orgTreeLevel,部门层级，自动运算
 * orgleader 部门主管
 * @author yinbp
 *
 */
public class CommonOrganization implements Serializable {

	public CommonOrganization() {
		// TODO Auto-generated constructor stub
	}
	
	private String orgId;
	private String chargeorgid;
	private String children;
	private String code;
	private Timestamp creatingtime;
	private String creator;
	private int isdirectguanhu;
	private int isdirectlyparty;
	private int isforeignparty;
	private int isjichaparty;
	private int ispartybussiness;
	private String jp;
	private String layer;
	private String orgLevel;
	private String orgName;
	private int orgSn;
	private String orgTreeLevel;
	private String orgXzqm;
	private String orgdesc;
	private String orgleader;
	private String orgnumber;
	private String parentId;
	private String path;
	private String qp;
	private String remark1;
	private String remark2;
	private String remark3;
	private String remark4;
	private String remark5;
	private String satrapjobid;
 
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setChargeorgid(String chargeorgid) {
		this.chargeorgid = chargeorgid;
	}

	public String getChargeorgid() {
		return chargeorgid;
	}

	public void setChildren(String children) {
		this.children = children;
	}

	public String getChildren() {
		return children;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCreatingtime(Timestamp creatingtime) {
		this.creatingtime = creatingtime;
	}

	public Timestamp getCreatingtime() {
		return creatingtime;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreator() {
		return creator;
	}

	public void setIsdirectguanhu(int isdirectguanhu) {
		this.isdirectguanhu = isdirectguanhu;
	}

	public int getIsdirectguanhu() {
		return isdirectguanhu;
	}

	public void setIsdirectlyparty(int isdirectlyparty) {
		this.isdirectlyparty = isdirectlyparty;
	}

	public int getIsdirectlyparty() {
		return isdirectlyparty;
	}

	public void setIsforeignparty(int isforeignparty) {
		this.isforeignparty = isforeignparty;
	}

	public int getIsforeignparty() {
		return isforeignparty;
	}

	public void setIsjichaparty(int isjichaparty) {
		this.isjichaparty = isjichaparty;
	}

	public long getIsjichaparty() {
		return isjichaparty;
	}

	public void setIspartybussiness(int ispartybussiness) {
		this.ispartybussiness = ispartybussiness;
	}

	public int getIspartybussiness() {
		return ispartybussiness;
	}

	public void setJp(String jp) {
		this.jp = jp;
	}

	public String getJp() {
		return jp;
	}

	public void setLayer(String layer) {
		this.layer = layer;
	}

	public String getLayer() {
		return layer;
	}

	public void setOrgLevel(String orgLevel) {
		this.orgLevel = orgLevel;
	}

	public String getOrgLevel() {
		return orgLevel;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgSn(int orgSn) {
		this.orgSn = orgSn;
	}

	public int getOrgSn() {
		return orgSn;
	}

	public void setOrgTreeLevel(String orgTreeLevel) {
		this.orgTreeLevel = orgTreeLevel;
	}

	public String getOrgTreeLevel() {
		return orgTreeLevel;
	}

	public void setOrgXzqm(String orgXzqm) {
		this.orgXzqm = orgXzqm;
	}

	public String getOrgXzqm() {
		return orgXzqm;
	}

	public void setOrgdesc(String orgdesc) {
		this.orgdesc = orgdesc;
	}

	public String getOrgdesc() {
		return orgdesc;
	}

	public void setOrgleader(String orgleader) {
		this.orgleader = orgleader;
	}

	public String getOrgleader() {
		return orgleader;
	}

	public void setOrgnumber(String orgnumber) {
		this.orgnumber = orgnumber;
	}

	public String getOrgnumber() {
		return orgnumber;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getParentId() {
		return parentId;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	public void setQp(String qp) {
		this.qp = qp;
	}

	public String getQp() {
		return qp;
	}

	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}

	public String getRemark1() {
		return remark1;
	}

	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}

	public String getRemark2() {
		return remark2;
	}

	public void setRemark3(String remark3) {
		this.remark3 = remark3;
	}

	public String getRemark3() {
		return remark3;
	}

	public void setRemark4(String remark4) {
		this.remark4 = remark4;
	}

	public String getRemark4() {
		return remark4;
	}

	public void setRemark5(String remark5) {
		this.remark5 = remark5;
	}

	public String getRemark5() {
		return remark5;
	}

	public void setSatrapjobid(String satrapjobid) {
		this.satrapjobid = satrapjobid;
	}

	public String getSatrapjobid() {
		return satrapjobid;
	}

}
