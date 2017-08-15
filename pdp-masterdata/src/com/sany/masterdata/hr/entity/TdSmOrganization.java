/*
 * @(#)TdSmOrganization.java
 * 
 * Copyright @ 2001-2012 SANY Group Co.,Ltd.
 * All right reserved.
 * 
 * 这个软件是属于bbossgroups有限公司机密的和私有信息，不得泄露。
 * 并且只能由bbossgroups有限公司内部员工在得到许可的情况下才允许使用。
 * This software is the confidential and proprietary information
 * of SANY Group Co, Ltd. You shall not disclose such
 * Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with
 * SANY Group Co, Ltd.
 */
package com.sany.masterdata.hr.entity;

/**
 * Organization info entity
 * @author caix3
 * @since 2012-3-21
 */
public class TdSmOrganization {

    private String orgId;

    private Integer orgSn;

    private String orgName;

    private String orgnumber;

    private String parentId;

    private String chargeorgid;

    private String orgdesc;

    private String remark3;

    private String remark5;
    
    private String orgTreeLevel;
    
    private String orgLevel;
    
    private String orgXzqm;
    
    public String getChargeorgid() {
        return chargeorgid;
    }

    public String getOrgdesc() {
        return orgdesc;
    }

    public String getOrgId() {
        return orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public String getOrgnumber() {
        return orgnumber;
    }

    public Integer getOrgSn() {
        return orgSn;
    }

    public String getParentId() {
        return parentId;
    }

    public String getRemark3() {
        return remark3;
    }

    public void setChargeorgid(String chargeorgid) {
        this.chargeorgid = chargeorgid;
    }

    public void setOrgdesc(String orgdesc) {
        this.orgdesc = orgdesc;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public void setOrgnumber(String orgnumber) {
        this.orgnumber = orgnumber;
    }

    public void setOrgSn(Integer orgSn) {
        this.orgSn = orgSn;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public void setRemark3(String remark3) {
        this.remark3 = remark3;
    }

    public String getRemark5() {
        return remark5;
    }

    public void setRemark5(String remark5) {
        this.remark5 = remark5;
    }

    public String getOrgTreeLevel() {
        return orgTreeLevel;
    }

    public void setOrgTreeLevel(String orgTreeLevel) {
        this.orgTreeLevel = orgTreeLevel;
    }

    public String getOrgXzqm() {
        return orgXzqm;
    }

    public void setOrgXzqm(String orgXzqm) {
        this.orgXzqm = orgXzqm;
    }

    public String getOrgLevel() {
        return orgLevel;
    }

    public void setOrgLevel(String orgLevel) {
        this.orgLevel = orgLevel;
    }

}
