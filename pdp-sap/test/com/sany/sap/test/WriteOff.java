package com.sany.sap.test;

import java.util.Date;

/**
 * 冲销的bean
 */
public class WriteOff {
	/**
	 * 主键
	 */
	private String id;
	private String logId;//日志ID
	private String docNo;//凭证编号
	private String compCode;//公司代码
	private String accountYear;//会计年份
	private String reason;//冲销原因
	private Date docDate;//记账日期
	private String fisPeriod;//备用
	private String errorMsg;//校验错误信息
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLogId() {
		return logId;
	}
	public void setLogId(String logId) {
		this.logId = logId;
	}
	public String getDocNo() {
		return docNo;
	}
	public void setDocNo(String docNo) {
		this.docNo = docNo;
	}
	public String getCompCode() {
		return compCode;
	}
	public void setCompCode(String compCode) {
		this.compCode = compCode;
	}
	public String getAccountYear() {
		return accountYear;
	}
	public void setAccountYear(String accountYear) {
		this.accountYear = accountYear;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public Date getDocDate() {
		return docDate;
	}
	public void setDocDate(Date docDate) {
		this.docDate = docDate;
	}
	public String getFisPeriod() {
		return fisPeriod;
	}
	public void setFisPeriod(String fisPeriod) {
		this.fisPeriod = fisPeriod;
	}
	
}
