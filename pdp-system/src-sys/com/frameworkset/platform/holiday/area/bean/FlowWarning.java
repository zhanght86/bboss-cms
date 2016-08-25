package com.frameworkset.platform.holiday.area.bean;

import java.sql.Timestamp;
import java.util.Date;

public class FlowWarning {
	
	private String userAccount;//用户账号
	private Date createTime;//任务开始时间
	private int dicipline;//节假日规则
	private long interval;//工时
	private int warnPercent;//预警百分率
	private Timestamp completeTime;//任务超时时间
	private Timestamp warnTime;//任务预警时间
	private String errorCode;
	
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getUserAccount() {
		return userAccount;
	}
	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public int getDicipline() {
		return dicipline;
	}
	public void setDicipline(int dicipline) {
		this.dicipline = dicipline;
	}
	public long getInterval() {
		return interval;
	}
	public void setInterval(long interval) {
		this.interval = interval;
	}
	
	public Timestamp getCompleteTime() {
		return completeTime;
	}
	public void setCompleteTime(Timestamp completeTime) {
		this.completeTime = completeTime;
	}
	public Timestamp getWarnTime() {
		return warnTime;
	}
	public void setWarnTime(Timestamp warnTime) {
		this.warnTime = warnTime;
	}
	public int getWarnPercent() {
		return warnPercent;
	}
	public void setWarnPercent(int warnPercent) {
		this.warnPercent = warnPercent;
	}
	
	
}
