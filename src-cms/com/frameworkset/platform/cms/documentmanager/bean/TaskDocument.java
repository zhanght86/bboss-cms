package com.frameworkset.platform.cms.documentmanager.bean;

import java.util.Date;

public class TaskDocument implements java.io.Serializable {
	private int taskid;                   //任务id
	private int docid;					  //任务文档id 
	private String docName;               //任务文档名称
	private Date submitTime;            //文档提交时间
	private int submitUserid;             //任务提交人id
	private String submitUserName;		  //任务提交人人姓名
	private int docChannelid;             //文档所处频道id
	private String docChannelName;		  //文档所处频道名
	private int docTpye;                  //任务文档类型
	private int docSiteid;                //任务文档所处站点id  
	private String docSiteName;		      //任务文档所处站点名
	private int docFlowid;                //文档流程id
	private String docFlowName;           //文档流程名
	private String taskOpinion;           //当前任务的意见
	private String preTaskOpinion;               //前一个任务的意见，主要是返工任务的审核意见。
	public String getDocFlowName() {
		return docFlowName;
	}
	public void setDocFlowName(String docFlowName) {
		this.docFlowName = docFlowName;
	}
	public String getDocSiteName() {
		return docSiteName;
	}
	public void setDocSiteName(String docSiteName) {
		this.docSiteName = docSiteName;
	}
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
	public Date getSubmitTime() {
		return submitTime;
	}
	public void setSubmitTime(Date submitTime) {
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
	public int getTaskid() {
		return taskid;
	}
	public void setTaskid(int taskid) {
		this.taskid = taskid;
	}
	public int getDocTpye() {
		return docTpye;
	}
	public void setDocTpye(int docTpye) {
		this.docTpye = docTpye;
	}
	public int getDocSiteid() {
		return docSiteid;
	}
	public void setDocSiteid(int docSiteid) {
		this.docSiteid = docSiteid;
	}
	public int getDocFlowid() {
		return docFlowid;
	}
	public void setDocFlowid(int docFlowid) {
		this.docFlowid = docFlowid;
	}
	public String getPreTaskOpinion() {
		return preTaskOpinion;
	}
	public void setPreTaskOpinion(String preTaskOpinion) {
		this.preTaskOpinion = preTaskOpinion;
	}
	public String getTaskOpinion() {
		return taskOpinion;
	}
	public void setTaskOpinion(String taskOpinion) {
		this.taskOpinion = taskOpinion;
	}
	
}
