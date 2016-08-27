/*
 * @(#)TdSmUserJobOrg.java
 * 
 * Copyright @ 2001-2012 SANY Group Co.,Ltd.
 * All right reserved.
 * 
 * 这个软件是属于三一集团有限公司机密的和私有信息，不得泄露。
 * 并且只能由三一集团有限公司内部员工在得到许可的情况下才允许使用。
 * This software is the confidential and proprietary information
 * of SANY Group Co, Ltd. You shall not disclose such
 * Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with
 * SANY Group Co, Ltd.
 */
package com.sany.masterdata.hr.entity;

import java.util.Date;

/**
 * TdSmUserJobOrg.java
 * @author caix3
 * @since 2012-3-23 上午10:05:34
 */
public class TdSmUserJobOrg {

    private Integer userId;

    private String jobId;

    private String orgId;

    private Date jobStartTime;

    public String getJobId() {
        return jobId;
    }

    public Date getJobStartTime() {
        return jobStartTime;
    }

    public String getOrgId() {
        return orgId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public void setJobStartTime(Date jobStartTime) {
        this.jobStartTime = jobStartTime;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}
