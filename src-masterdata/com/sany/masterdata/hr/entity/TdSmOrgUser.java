/*
 * @(#)TdSmOrgUser.java
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

/**
 * TdSmOrgUser.java
 * @author caix3
 * @since 2012-3-23 上午9:41:58
 */
public class TdSmOrgUser {

    private String orgId;

    private Integer userId;

    public String getOrgId() {
        return orgId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}
