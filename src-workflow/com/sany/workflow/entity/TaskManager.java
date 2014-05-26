package com.sany.workflow.entity;

/**
 * @todo 平台任务实体
 * @author tanx
 * @date 2014年5月14日
 * 
 */
public class TaskManager {

	private String ID_;

	private String EXECUTION_ID_; // 执行实例ID

	private String PROC_INST_ID_;// 流程实例ID

	private String PROC_DEF_ID_;// 流程定义ID

	private String NAME_;// 节点定义名称

	private String PARENT_TASK_ID_;// 父节点实例ID

	private String DESCRIPTION_;// 节点定义描述

	private String TASK_DEF_KEY_;// 任务定义的ID

	private String OWNER_;// 实际签收人（一般情况下为空，只有在委托时才有值）

	private String ASSIGNEE_;// 签收人或委托人

	private String DELEGATION_;// 委托类型，DelegationState分为两种：PENDING，RESOLVED。如无委托则为空

	private String PRIORITY_;// 优先级别，默认为：50

	private String CREATE_TIME_;// 创建时间

	private String SUSPENSION_STATE_;// 是否挂起 1代表激活 2代表挂起
	
	private String ACT_ID_;// 节点定义ID
	
	private String TASK_ID_;// 任务ID
	
	private String CALL_PROC_INST_ID_;// 调用外部流程的流程实例ID

	private String ACT_NAME_;// 节点定义名称

	private String ACT_TYPE_;// 父节点实例ID

	private String START_TIME_;// 开始时间

	private String CLAIM_TIME_;// 提醒时间

	private String END_TIME_;// 结束时间
	
	private String DURATION_;// 耗时(毫秒值)
	
	private String DELETE_REASON_;//备注
	
	public String getDELETE_REASON_() {
		return DELETE_REASON_;
	}

	public void setDELETE_REASON_(String dELETE_REASON_) {
		DELETE_REASON_ = dELETE_REASON_;
	}

	private String state;// 签收状态 0所有任务 1 未签收 2已签收(关联其他表的业务字段)
	
	private String USER_ID_;// 处理人id (关联其他表的业务字段)
	
	private String GROUP_ID;// 处理组id

	public String getID_() {
		return ID_;
	}

	public void setID_(String iD_) {
		ID_ = iD_;
	}

	public String getEXECUTION_ID_() {
		return EXECUTION_ID_;
	}

	public void setEXECUTION_ID_(String eXECUTION_ID_) {
		EXECUTION_ID_ = eXECUTION_ID_;
	}

	public String getPROC_INST_ID_() {
		return PROC_INST_ID_;
	}

	public void setPROC_INST_ID_(String pROC_INST_ID_) {
		PROC_INST_ID_ = pROC_INST_ID_;
	}

	public String getPROC_DEF_ID_() {
		return PROC_DEF_ID_;
	}

	public void setPROC_DEF_ID_(String pROC_DEF_ID_) {
		PROC_DEF_ID_ = pROC_DEF_ID_;
	}

	public String getNAME_() {
		return NAME_;
	}

	public void setNAME_(String nAME_) {
		NAME_ = nAME_;
	}

	public String getPARENT_TASK_ID_() {
		return PARENT_TASK_ID_;
	}

	public void setPARENT_TASK_ID_(String pARENT_TASK_ID_) {
		PARENT_TASK_ID_ = pARENT_TASK_ID_;
	}

	public String getDESCRIPTION_() {
		return DESCRIPTION_;
	}

	public void setDESCRIPTION_(String dESCRIPTION_) {
		DESCRIPTION_ = dESCRIPTION_;
	}

	public String getTASK_DEF_KEY_() {
		return TASK_DEF_KEY_;
	}

	public void setTASK_DEF_KEY_(String tASK_DEF_KEY_) {
		TASK_DEF_KEY_ = tASK_DEF_KEY_;
	}

	public String getOWNER_() {
		return OWNER_;
	}

	public void setOWNER_(String oWNER_) {
		OWNER_ = oWNER_;
	}

	public String getASSIGNEE_() {
		return ASSIGNEE_;
	}

	public void setASSIGNEE_(String aSSIGNEE_) {
		ASSIGNEE_ = aSSIGNEE_;
	}

	public String getDELEGATION_() {
		return DELEGATION_;
	}

	public void setDELEGATION_(String dELEGATION_) {
		DELEGATION_ = dELEGATION_;
	}

	public String getPRIORITY_() {
		return PRIORITY_;
	}

	public void setPRIORITY_(String pRIORITY_) {
		PRIORITY_ = pRIORITY_;
	}

	public String getCREATE_TIME_() {
		return CREATE_TIME_;
	}

	public void setCREATE_TIME_(String cREATE_TIME_) {
		CREATE_TIME_ = cREATE_TIME_;
	}

	public String getSUSPENSION_STATE_() {
		return SUSPENSION_STATE_;
	}

	public void setSUSPENSION_STATE_(String sUSPENSION_STATE_) {
		SUSPENSION_STATE_ = sUSPENSION_STATE_;
	}

	public String getACT_ID_() {
		return ACT_ID_;
	}

	public void setACT_ID_(String aCT_ID_) {
		ACT_ID_ = aCT_ID_;
	}

	public String getTASK_ID_() {
		return TASK_ID_;
	}

	public void setTASK_ID_(String tASK_ID_) {
		TASK_ID_ = tASK_ID_;
	}

	public String getCALL_PROC_INST_ID_() {
		return CALL_PROC_INST_ID_;
	}

	public void setCALL_PROC_INST_ID_(String cALL_PROC_INST_ID_) {
		CALL_PROC_INST_ID_ = cALL_PROC_INST_ID_;
	}

	public String getACT_NAME_() {
		return ACT_NAME_;
	}

	public void setACT_NAME_(String aCT_NAME_) {
		ACT_NAME_ = aCT_NAME_;
	}

	public String getACT_TYPE_() {
		return ACT_TYPE_;
	}

	public void setACT_TYPE_(String aCT_TYPE_) {
		ACT_TYPE_ = aCT_TYPE_;
	}

	public String getSTART_TIME_() {
		return START_TIME_;
	}

	public void setSTART_TIME_(String sTART_TIME_) {
		START_TIME_ = sTART_TIME_;
	}

	public String getCLAIM_TIME_() {
		return CLAIM_TIME_;
	}

	public void setCLAIM_TIME_(String cLAIM_TIME_) {
		CLAIM_TIME_ = cLAIM_TIME_;
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getUSER_ID_() {
		return USER_ID_;
	}

	public void setUSER_ID_(String uSER_ID_) {
		USER_ID_ = uSER_ID_;
	}

	public String getGROUP_ID() {
		return GROUP_ID;
	}

	public void setGROUP_ID(String gROUP_ID) {
		GROUP_ID = gROUP_ID;
	}


}
