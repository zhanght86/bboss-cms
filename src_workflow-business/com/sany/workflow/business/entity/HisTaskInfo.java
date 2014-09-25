package com.sany.workflow.business.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import org.frameworkset.util.annotations.RequestParam;

/**
 * 历史任务
 * 
 * @todo
 * @author tanx
 * @date 2014年8月23日
 * 
 */
public class HisTaskInfo implements Serializable {

	private static final long serialVersionUID = -4473752406812328499L;

	private String ID_;

	private String PROC_INST_ID_;// 流程实例ID

	private String PROC_DEF_ID_;// 流程定义ID

	private String EXECUTION_ID_; // 执行实例ID

	private String ACT_NAME_;// 节点定义名称

	private String DESCRIPTION_;// 节点定义描述

	private String TASK_DEF_KEY_;// 任务定义的ID

	private String OWNER_;// 实际签收人（一般情况下为空，只有在委托时才有值）

	private String OWNER_NAME;// 实际签收人姓名

	private String ASSIGNEE_;// 签收人或委托人
	
	private String DEALUSER;// 处理人（判断撤销的）

	private String ASSIGNEE_NAME;// 签收人姓名

	private String DELEGATION_;// 委托类型，DelegationState分为两种：PENDING，RESOLVED。如无委托则为空

	private String TASK_ID_;// 任务ID

	private String CALL_PROC_INST_ID_;// 调用外部流程的流程实例ID

	@RequestParam(dateformat = "yyyy-MM-dd HH:mm:ss")
	private Timestamp START_TIME_;// 开始时间

	@RequestParam(dateformat = "yyyy-MM-dd HH:mm:ss")
	private Timestamp CLAIM_TIME_;// 提醒时间

	@RequestParam(dateformat = "yyyy-MM-dd HH:mm:ss")
	private Timestamp END_TIME_;// 结束时间

	private String DURATION_;// 耗时(毫秒值)

	private String USER_ID_;// 处理人id (关联其他表的业务字段)

	private String USER_ID_NAME;// 处理人姓名

	private String DURATION_NODE;// 处理工时

	private int IS_CONTAIN_HOLIDAY;// 是否包含节假日（0剔除1包含）

	@RequestParam(dateformat = "yyyy-MM-dd HH:mm:ss")
	private Timestamp ALERTTIME;// 预警时间点

	@RequestParam(dateformat = "yyyy-MM-dd HH:mm:ss")
	private Timestamp OVERTIME;// 预警时间点

	private int advancesend;// 预警发送状态

	private int overtimesend;// 超时发送状态

	private String isOverTime;// 是否超时提醒(0未超时1超时)

	private String isAlertTime;// 是否预警提醒(0未预警1预警)

	private String DELETE_REASON_;// 操作内容

	private int IS_AUTO_COMPLETE;// 是否自动完成

	private String AUTO_HANDLER;// 自动处理类

	private String BUSSINESS_OP;// 业务操作任务方式(通过，驳回，撤销等)

	private String BUSSINESS_REMARK;// 业务处理意见

	public String getDEALUSER() {
		return DEALUSER;
	}

	public void setDEALUSER(String dEALUSER) {
		DEALUSER = dEALUSER;
	}

	public String getBUSSINESS_OP() {
		return BUSSINESS_OP;
	}

	public void setBUSSINESS_OP(String bUSSINESS_OP) {
		BUSSINESS_OP = bUSSINESS_OP;
	}

	public String getBUSSINESS_REMARK() {
		return BUSSINESS_REMARK;
	}

	public void setBUSSINESS_REMARK(String bUSSINESS_REMARK) {
		BUSSINESS_REMARK = bUSSINESS_REMARK;
	}

	public int getIS_AUTO_COMPLETE() {
		return IS_AUTO_COMPLETE;
	}

	public void setIS_AUTO_COMPLETE(int iS_AUTO_COMPLETE) {
		IS_AUTO_COMPLETE = iS_AUTO_COMPLETE;
	}

	public String getAUTO_HANDLER() {
		return AUTO_HANDLER;
	}

	public void setAUTO_HANDLER(String aUTO_HANDLER) {
		AUTO_HANDLER = aUTO_HANDLER;
	}

	public String getDELETE_REASON_() {
		return DELETE_REASON_;
	}

	public void setDELETE_REASON_(String dELETE_REASON_) {
		DELETE_REASON_ = dELETE_REASON_;
	}

	public String getIsOverTime() {
		return isOverTime;
	}

	public void setIsOverTime(String isOverTime) {
		this.isOverTime = isOverTime;
	}

	public String getIsAlertTime() {
		return isAlertTime;
	}

	public void setIsAlertTime(String isAlertTime) {
		this.isAlertTime = isAlertTime;
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

	public String getPROC_DEF_ID_() {
		return PROC_DEF_ID_;
	}

	public void setPROC_DEF_ID_(String pROC_DEF_ID_) {
		PROC_DEF_ID_ = pROC_DEF_ID_;
	}

	public String getEXECUTION_ID_() {
		return EXECUTION_ID_;
	}

	public void setEXECUTION_ID_(String eXECUTION_ID_) {
		EXECUTION_ID_ = eXECUTION_ID_;
	}

	public String getACT_NAME_() {
		return ACT_NAME_;
	}

	public void setACT_NAME_(String aCT_NAME_) {
		ACT_NAME_ = aCT_NAME_;
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

	public String getOWNER_NAME() {
		return OWNER_NAME;
	}

	public void setOWNER_NAME(String oWNER_NAME) {
		OWNER_NAME = oWNER_NAME;
	}

	public String getASSIGNEE_() {
		return ASSIGNEE_;
	}

	public void setASSIGNEE_(String aSSIGNEE_) {
		ASSIGNEE_ = aSSIGNEE_;
	}

	public String getASSIGNEE_NAME() {
		return ASSIGNEE_NAME;
	}

	public void setASSIGNEE_NAME(String aSSIGNEE_NAME) {
		ASSIGNEE_NAME = aSSIGNEE_NAME;
	}

	public String getDELEGATION_() {
		return DELEGATION_;
	}

	public void setDELEGATION_(String dELEGATION_) {
		DELEGATION_ = dELEGATION_;
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

	public Timestamp getSTART_TIME_() {
		return START_TIME_;
	}

	public void setSTART_TIME_(Timestamp sTART_TIME_) {
		START_TIME_ = sTART_TIME_;
	}

	public Timestamp getCLAIM_TIME_() {
		return CLAIM_TIME_;
	}

	public void setCLAIM_TIME_(Timestamp cLAIM_TIME_) {
		CLAIM_TIME_ = cLAIM_TIME_;
	}

	public Timestamp getEND_TIME_() {
		return END_TIME_;
	}

	public void setEND_TIME_(Timestamp eND_TIME_) {
		END_TIME_ = eND_TIME_;
	}

	public String getDURATION_() {
		return DURATION_;
	}

	public void setDURATION_(String dURATION_) {
		DURATION_ = dURATION_;
	}

	public String getUSER_ID_() {
		return USER_ID_;
	}

	public void setUSER_ID_(String uSER_ID_) {
		USER_ID_ = uSER_ID_;
	}

	public String getUSER_ID_NAME() {
		return USER_ID_NAME;
	}

	public void setUSER_ID_NAME(String uSER_ID_NAME) {
		USER_ID_NAME = uSER_ID_NAME;
	}

	public String getDURATION_NODE() {
		return DURATION_NODE;
	}

	public void setDURATION_NODE(String dURATION_NODE) {
		DURATION_NODE = dURATION_NODE;
	}

	public int getIS_CONTAIN_HOLIDAY() {
		return IS_CONTAIN_HOLIDAY;
	}

	public void setIS_CONTAIN_HOLIDAY(int iS_CONTAIN_HOLIDAY) {
		IS_CONTAIN_HOLIDAY = iS_CONTAIN_HOLIDAY;
	}

	public Timestamp getALERTTIME() {
		return ALERTTIME;
	}

	public void setALERTTIME(Timestamp aLERTTIME) {
		ALERTTIME = aLERTTIME;
	}

	public Timestamp getOVERTIME() {
		return OVERTIME;
	}

	public void setOVERTIME(Timestamp oVERTIME) {
		OVERTIME = oVERTIME;
	}

	public int getAdvancesend() {
		return advancesend;
	}

	public void setAdvancesend(int advancesend) {
		this.advancesend = advancesend;
	}

	public int getOvertimesend() {
		return overtimesend;
	}

	public void setOvertimesend(int overtimesend) {
		this.overtimesend = overtimesend;
	}

}
