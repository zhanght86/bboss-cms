/*
 * @(#)ProcessDefCondition.java
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
 * @todo 对应activiti中的流程实例表中的数据
 * @author tanx
 * @date 2014年5月8日
 * 
 */
public class ProcessInst {

	private String ID_;
	private String PROC_INST_ID_;//流程实例ID
	private String BUSINESS_KEY_;
	private String PROC_DEF_ID_;//流程定义ID
	private String START_TIME_;//流程开启时间
	private String END_TIME_;//流程开启时间
	private String DURATION_;
	private String START_USER_ID_;//流程开启人
	private String START_ACT_ID_;
	private String END_ACT_ID_;
	private String SUPER_PROCESS_INSTANCE_ID_;
	private String DELETE_REASON_;//删除原因
	
	private String NAME_;//当前节点名称
	private String ASSIGNEE_;//当前节点处理人
	private String REMARK;//备注
	private String VERSION_;//版本

	public String getVERSION_() {
		return VERSION_;
	}

	public void setVERSION_(String vERSION_) {
		VERSION_ = vERSION_;
	}

	public String getREMARK() {
		return REMARK;
	}

	public void setREMARK(String rEMARK) {
		REMARK = rEMARK;
	}

	public String getNAME_() {
		return NAME_;
	}

	public void setNAME_(String nAME_) {
		NAME_ = nAME_;
	}

	public String getASSIGNEE_() {
		return ASSIGNEE_;
	}

	public void setASSIGNEE_(String aSSIGNEE_) {
		ASSIGNEE_ = aSSIGNEE_;
	}

	public String getID_() {
		return ID_;
	}

	public void setID_(String iD_) {
		ID_ = iD_;
	}

	public String getPROC_INST_ID_() {
		return PROC_INST_ID_;
	}

	public void setPROC_INST_ID_(String pROC_INST_ID_) {
		PROC_INST_ID_ = pROC_INST_ID_;
	}

	public String getBUSINESS_KEY_() {
		return BUSINESS_KEY_;
	}

	public void setBUSINESS_KEY_(String bUSINESS_KEY_) {
		BUSINESS_KEY_ = bUSINESS_KEY_;
	}

	public String getPROC_DEF_ID_() {
		return PROC_DEF_ID_;
	}

	public void setPROC_DEF_ID_(String pROC_DEF_ID_) {
		PROC_DEF_ID_ = pROC_DEF_ID_;
	}

	public String getSTART_TIME_() {
		return START_TIME_;
	}

	public void setSTART_TIME_(String sTART_TIME_) {
		START_TIME_ = sTART_TIME_;
	}

	public String getEND_TIME_() {
		return END_TIME_;
	}

	public void setEND_TIME_(String eND_TIME_) {
		END_TIME_ = eND_TIME_;
	}

	public String getDURATION_() {
		return DURATION_;
	}

	public void setDURATION_(String dURATION_) {
		DURATION_ = dURATION_;
	}

	public String getSTART_USER_ID_() {
		return START_USER_ID_;
	}

	public void setSTART_USER_ID_(String sTART_USER_ID_) {
		START_USER_ID_ = sTART_USER_ID_;
	}

	public String getSTART_ACT_ID_() {
		return START_ACT_ID_;
	}

	public void setSTART_ACT_ID_(String sTART_ACT_ID_) {
		START_ACT_ID_ = sTART_ACT_ID_;
	}

	public String getEND_ACT_ID_() {
		return END_ACT_ID_;
	}

	public void setEND_ACT_ID_(String eND_ACT_ID_) {
		END_ACT_ID_ = eND_ACT_ID_;
	}

	public String getSUPER_PROCESS_INSTANCE_ID_() {
		return SUPER_PROCESS_INSTANCE_ID_;
	}

	public void setSUPER_PROCESS_INSTANCE_ID_(String sUPER_PROCESS_INSTANCE_ID_) {
		SUPER_PROCESS_INSTANCE_ID_ = sUPER_PROCESS_INSTANCE_ID_;
	}

	public String getDELETE_REASON_() {
		return DELETE_REASON_;
	}

	public void setDELETE_REASON_(String dELETE_REASON_) {
		DELETE_REASON_ = dELETE_REASON_;
	}

}
