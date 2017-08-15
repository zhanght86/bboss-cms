/*
 * @(#)TdSmOrgJob.java
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
 * TdSmOrgJob.java
 * @author caix3
 * @since 2012-3-22
 */
public class TdSmOrgJob {

    private String orgId;

    private String jobId;

    private Long jobSn;

    public String getJobId() {
        return jobId;
    }

    public Long getJobSn() {
        return jobSn;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public void setJobSn(Long jobSn) {
        this.jobSn = jobSn;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

}
