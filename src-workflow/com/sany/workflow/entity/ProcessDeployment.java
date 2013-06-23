/*
 * @(#)ProcessDeployment.java
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

import org.activiti.engine.repository.DeploymentBuilder;
import org.frameworkset.web.multipart.MultipartFile;

/**
 * 流程部署信息
 * @author yinbp
 * @since 2012-3-22 下午7:42:10
 */
public class ProcessDeployment {
	private String ID_;
	private String NAME_;
	private MultipartFile processDef;
	private java.sql.Timestamp DEPLOY_TIME_;
	private MultipartFile paramFile;
	private int upgradepolicy = DeploymentBuilder.Deploy_policy_default;
	
	/**
	 * @return the upgradepolicy
	 */
	public int getUpgradepolicy() {
		return upgradepolicy;
	}

	/**
	 * @param upgradepolicy the upgradepolicy to set
	 */
	public void setUpgradepolicy(int upgradepolicy) {
		this.upgradepolicy = upgradepolicy;
	}

	private String businessTypeId;
	public String getID_() {
		return ID_;
	}

	public void setID_(String iD_) {
		ID_ = iD_;
	}

	public String getNAME_() {
		return NAME_;
	}

	public void setNAME_(String nAME_) {
		NAME_ = nAME_;
	}

	public java.sql.Timestamp getDEPLOY_TIME_() {
		return DEPLOY_TIME_;
	}

	public void setDEPLOY_TIME_(java.sql.Timestamp dEPLOY_TIME_) {
		DEPLOY_TIME_ = dEPLOY_TIME_;
	}

	public MultipartFile getProcessDef() {
		return processDef;
	}

	public void setProcessDef(MultipartFile processDef) {
		this.processDef = processDef;
	}

	public MultipartFile getParamFile() {
		return paramFile;
	}

	public void setParamFile(MultipartFile paramFile) {
		this.paramFile = paramFile;
	}

	public String getBusinessTypeId() {
		return businessTypeId;
	}

	public void setBusinessTypeId(String businessTypeId) {
		this.businessTypeId = businessTypeId;
	}

	
}
