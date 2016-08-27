package com.sany.sap.test;

import java.util.Date;

public class ClearAccount {

   private String id;
   private String logid;

   private String docno;          //清账凭证    
   private String compcode;       //公司代码  
   private String accountyear;      //会计年度  
   private String reason;         //冲销原因 
   private Date accountdate ;   //记帐日期 
   private String func;           //执行功能          01: 只重置  02：重置并冲销
   
   private String refDocNo;  //sap返回参考凭证
   private String msgType;  //sap返回消息文本：消息类型: S 成功,E 错误,W警告,I 信息,A 中断
   private String errormessages;   //如果是校验错误的记录就记录错误，否则放返回sap的信息
   
	public String getRefDocNo() {
	    return refDocNo;
	}
	public void setRefDocNo(String refDocNo) {
		this.refDocNo = refDocNo;
	}
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	public String getErrormessages() {
	return errormessages;
	}
	public void setErrormessages(String errormessages) {
		this.errormessages = errormessages;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLogid() {
		return logid;
	}
	public void setLogid(String logid) {
		this.logid = logid;
	}
	public String getDocno() {
		return docno;
	}
	public void setDocno(String docno) {
		this.docno = docno;
	}
	public String getCompcode() {
		return compcode;
	}
	public void setCompcode(String compcode) {
		this.compcode = compcode;
	}

	public String getAccountyear() {
		return accountyear;
	}
	public void setAccountyear(String accountyear) {
		this.accountyear = accountyear;
	}
	public Date getAccountdate() {
		return accountdate;
	}
	public void setAccountdate(Date accountdate) {
		this.accountdate = accountdate;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getFunc() {
		return func;
	}
	public void setFunc(String func) {
		this.func = func;
	}
	
	   
}
