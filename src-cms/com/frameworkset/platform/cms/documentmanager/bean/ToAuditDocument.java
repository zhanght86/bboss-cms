package com.frameworkset.platform.cms.documentmanager.bean;

public class ToAuditDocument implements java.io.Serializable {
	private int docid;					  //待审核文档id 
	private String docName;               //待审核文档名称
	private String submitTime;            //文档提交时间
	private int submitUserid;             //呈送人id
	private String submitUserName;		  //呈送人姓名
	private int docChannelid;             //文档所处频道id
	private String docChannelName;		  //文档所处频道名
	public int getDocChannelid() {
		return docChannelid;
	}
	public void setDocChannelid(int docChannelid) {
		this.docChannelid = docChannelid;
	}
	public String getDocChannelName() {
		return docChannelName;
	}
	public void setDocChannelName(String docChannelName) {
		this.docChannelName = docChannelName;
	}
	public int getDocid() {
		return docid;
	}
	public void setDocid(int docid) {
		this.docid = docid;
	}
	public String getDocName() {
		return docName;
	}
	public void setDocName(String docName) {
		this.docName = docName;
	}
	public String getSubmitTime() {
		return submitTime;
	}
	public void setSubmitTime(String submitTime) {
		this.submitTime = submitTime;
	}
	public int getSubmitUserid() {
		return submitUserid;
	}
	public void setSubmitUserid(int submitUserid) {
		this.submitUserid = submitUserid;
	}
	public String getSubmitUserName() {
		return submitUserName;
	}
	public void setSubmitUserName(String submitUserName) {
		this.submitUserName = submitUserName;
	}
	
}
