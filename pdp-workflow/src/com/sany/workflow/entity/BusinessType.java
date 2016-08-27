/*
 * @(#)BussinessType.java
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
package com.sany.workflow.entity;

/**
 * 业务类型实体
 * @author caix3 
 * @version 2012-4-16,v1.0 
 */
public class BusinessType {

    private String businessId;
    
    private String businessCode;
    
    private String businessName;
    
    private String parentId;
    
    private String useFlag;
    
    private String remark;
    
    private String parent_name;

    
    public String getBusinessId() {
        return businessId;
    }

    
    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    
    public String getBusinessCode() {
        return businessCode;
    }

    
    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    
    public String getBusinessName() {
        return businessName;
    }

    
    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    
    public String getParentId() {
        return parentId;
    }

    
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    
    public String getUseFlag() {
        return useFlag;
    }

    
    public void setUseFlag(String useFlag) {
        this.useFlag = useFlag;
    }

    
    public String getRemark() {
        return remark;
    }

    
    public void setRemark(String remark) {
        this.remark = remark;
    }


	public String getParent_name() {
		return parent_name;
	}


	public void setParent_name(String parent_name) {
		this.parent_name = parent_name;
	}

    
}
