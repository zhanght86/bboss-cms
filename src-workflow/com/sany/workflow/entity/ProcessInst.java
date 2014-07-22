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

import java.sql.Timestamp;
import java.util.List;

/**
 * @todo 对应activiti中的流程实例表中的数据
 * @author tanx
 * @date 2014年5月8日
 * 
 */
public class ProcessInst {

	private String ID_;
	private String PROC_INST_ID_;// 流程实例ID
	private String BUSINESS_KEY_;
	private String PROC_DEF_ID_;// 流程定义ID
	private Timestamp START_TIME_;// 流程开启时间
	private Timestamp END_TIME_;// 流程结束时间
	private String DURATION_;
	private String START_USER_ID_;// 流程开启人
	private String START_USER_ID_NAME;
	private String START_ACT_ID_;
	private String END_ACT_ID_;
	private String SUPER_PROCESS_INSTANCE_ID_;// 父流程实例id（ACT_HI_PROCINST表中字段）
	private String DELETE_REASON_;// 删除原因

	private String VERSION_;// 版本
	private String KEY_;// 流程KEY
	private String BUSINESS_NAME;// 业务类型

	private String NAME_;// 流程名称
	private String SUSPENSION_STATE_;// 流程状态 1激活， 2挂起，空->流程结束

	private List<TaskManager> taskList;// 任务集合

	private String SUPER_SUSPENSION_STATE_;// 父流程实例状态
	private String SUPER_START_USER_ID_;// 父流程开启人
	private String SUPER_START_USER_ID_NAME;
	private Timestamp SUPER_START_TIME_;// 父流程开启时间
	private Timestamp SUPER_END_TIME_;// 父流程结束时间
	private String PARENT_ID_;// 父流程实例id （ACT_RU_EXECUTION表中字段）

	private List<ActivitiVariable> variableList;// 变量参数集合
	
	public String getSTART_USER_ID_NAME() {
		return START_USER_ID_NAME;
	}

	public void setSTART_USER_ID_NAME(String sTART_USER_ID_NAME) {
		START_USER_ID_NAME = sTART_USER_ID_NAME;
	}

	public String getSUPER_START_USER_ID_NAME() {
		return SUPER_START_USER_ID_NAME;
	}

	public void setSUPER_START_USER_ID_NAME(String sUPER_START_USER_ID_NAME) {
		SUPER_START_USER_ID_NAME = sUPER_START_USER_ID_NAME;
	}

	public String getBUSINESS_NAME() {
		return BUSINESS_NAME;
	}

	public void setBUSINESS_NAME(String bUSINESS_NAME) {
		BUSINESS_NAME = bUSINESS_NAME;
	}

	public List<ActivitiVariable> getVariableList() {
		return variableList;
	}

	public void setVariableList(List<ActivitiVariable> variableList) {
		this.variableList = variableList;
	}

	public String getPARENT_ID_() {
		return PARENT_ID_;
	}

	public void setPARENT_ID_(String pARENT_ID_) {
		PARENT_ID_ = pARENT_ID_;
	}

	public String getSUPER_SUSPENSION_STATE_() {
		return SUPER_SUSPENSION_STATE_;
	}

	public void setSUPER_SUSPENSION_STATE_(String sUPER_SUSPENSION_STATE_) {
		SUPER_SUSPENSION_STATE_ = sUPER_SUSPENSION_STATE_;
	}

	public String getSUPER_START_USER_ID_() {
		return SUPER_START_USER_ID_;
	}

	public void setSUPER_START_USER_ID_(String sUPER_START_USER_ID_) {
		SUPER_START_USER_ID_ = sUPER_START_USER_ID_;
	}

	public Timestamp getSUPER_START_TIME_() {
		return SUPER_START_TIME_;
	}

	public void setSUPER_START_TIME_(Timestamp sUPER_START_TIME_) {
		SUPER_START_TIME_ = sUPER_START_TIME_;
	}

	public Timestamp getSUPER_END_TIME_() {
		return SUPER_END_TIME_;
	}

	public void setSUPER_END_TIME_(Timestamp sUPER_END_TIME_) {
		SUPER_END_TIME_ = sUPER_END_TIME_;
	}

	public void setSTART_TIME_(Timestamp sTART_TIME_) {
		START_TIME_ = sTART_TIME_;
	}

	public void setEND_TIME_(Timestamp eND_TIME_) {
		END_TIME_ = eND_TIME_;
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

	public Timestamp getSTART_TIME_() {
		return START_TIME_;
	}

	public Timestamp getEND_TIME_() {
		return END_TIME_;
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

	public String getVERSION_() {
		return VERSION_;
	}

	public void setVERSION_(String vERSION_) {
		VERSION_ = vERSION_;
	}

	public String getKEY_() {
		return KEY_;
	}

	public void setKEY_(String kEY_) {
		KEY_ = kEY_;
	}

	public String getNAME_() {
		return NAME_;
	}

	public void setNAME_(String nAME_) {
		NAME_ = nAME_;
	}

	public String getSUSPENSION_STATE_() {
		return SUSPENSION_STATE_;
	}

	public void setSUSPENSION_STATE_(String sUSPENSION_STATE_) {
		SUSPENSION_STATE_ = sUSPENSION_STATE_;
	}

	public List<TaskManager> getTaskList() {
		return taskList;
	}

	public void setTaskList(List<TaskManager> taskList) {
		this.taskList = taskList;
	}

}
