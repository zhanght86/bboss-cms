package com.sany.workflow.entity;

/** 驳回日志记录实体
 * @todo 
 * @author tanx
 * @date 2014年9月9日
 * 
 */
public class RejectLog {
	
	private String NEWTASKID;// 当前任务id
	
	private String REJECTTASKID;// 驳回任务ID
	
	private String REJECTNODE;// 驳回任务key

	public String getNEWTASKID() {
		return NEWTASKID;
	}

	public void setNEWTASKID(String nEWTASKID) {
		NEWTASKID = nEWTASKID;
	}

	public String getREJECTTASKID() {
		return REJECTTASKID;
	}

	public void setREJECTTASKID(String rEJECTTASKID) {
		REJECTTASKID = rEJECTTASKID;
	}

	public String getREJECTNODE() {
		return REJECTNODE;
	}

	public void setREJECTNODE(String rEJECTNODE) {
		REJECTNODE = rEJECTNODE;
	}
	

}
