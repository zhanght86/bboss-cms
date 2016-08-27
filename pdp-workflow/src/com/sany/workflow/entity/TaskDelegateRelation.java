package com.sany.workflow.entity;

import java.sql.Timestamp;

import org.frameworkset.util.annotations.RequestParam;

/**
 * 任务转办/委托关系
 * 
 * @todo
 * @author tanx
 * @date 2014年7月15日
 * 
 */
public class TaskDelegateRelation {

	private String TASKRELATION;// 1转办关系 2 委托关系

	private String TASKID;

	private String PROCESS_KEY;

	private String PROCESS_ID;

	private String FROM_USER;// 转办/委托人

	private String FROM_USER_NAME;

	private String TO_USER;// 被转办/委托人

	private String TO_USER_NAME;

	@RequestParam(dateformat = "yyyy-MM-dd HH:mm:ss")
	private Timestamp CHANGETIME;// 转办/委托时间

	public String getTASKRELATION() {
		return TASKRELATION;
	}

	public void setTASKRELATION(String tASKRELATION) {
		TASKRELATION = tASKRELATION;
	}

	public String getFROM_USER_NAME() {
		return FROM_USER_NAME;
	}

	public void setFROM_USER_NAME(String fROM_USER_NAME) {
		FROM_USER_NAME = fROM_USER_NAME;
	}

	public String getTO_USER_NAME() {
		return TO_USER_NAME;
	}

	public void setTO_USER_NAME(String tO_USER_NAME) {
		TO_USER_NAME = tO_USER_NAME;
	}

	public String getTASKID() {
		return TASKID;
	}

	public void setTASKID(String tASKID) {
		TASKID = tASKID;
	}

	public String getPROCESS_KEY() {
		return PROCESS_KEY;
	}

	public void setPROCESS_KEY(String pROCESS_KEY) {
		PROCESS_KEY = pROCESS_KEY;
	}

	public String getPROCESS_ID() {
		return PROCESS_ID;
	}

	public void setPROCESS_ID(String pROCESS_ID) {
		PROCESS_ID = pROCESS_ID;
	}

	public String getFROM_USER() {
		return FROM_USER;
	}

	public void setFROM_USER(String fROM_USER) {
		FROM_USER = fROM_USER;
	}

	public String getTO_USER() {
		return TO_USER;
	}

	public void setTO_USER(String tO_USER) {
		TO_USER = tO_USER;
	}

	public Timestamp getCHANGETIME() {
		return CHANGETIME;
	}

	public void setCHANGETIME(Timestamp cHANGETIME) {
		CHANGETIME = cHANGETIME;
	}

}
