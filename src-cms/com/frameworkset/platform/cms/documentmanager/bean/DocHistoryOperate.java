package com.frameworkset.platform.cms.documentmanager.bean;

public class DocHistoryOperate implements java.io.Serializable{
	private String userName;          //执行此历史操作的用户名
	private int userid;				  //执行此历史操作的用户id
	private String docSubtitle;		  //历史操作的文档标题
	private int docid;				  //历史操作的文档id
	private String operateName;		  //此历史操作的操作名
	private int operid;				  //此历史操作的操作id
	private int docStatusTranid;	  //此历史操作的导致的状态迁移id	
	private String hisOperTime;		  //此历史操作的执行时间
	private int hisOperid;			  //历史操作id
	private String hisOperContent;    //此历史操作的内容
	public int getDocid() {
		return docid;
	}
	public void setDocid(int docid) {
		this.docid = docid;
	}
	public int getDocStatusTranid() {
		return docStatusTranid;
	}
	public void setDocStatusTranid(int docStatusTranid) {
		this.docStatusTranid = docStatusTranid;
	}
	public String getDocSubtitle() {
		return docSubtitle;
	}
	public void setDocSubtitle(String docSubtitle) {
		this.docSubtitle = docSubtitle;
	}
	public int getHisOperid() {
		return hisOperid;
	}
	public void setHisOperid(int hisOperid) {
		this.hisOperid = hisOperid;
	}
	public String getHisOperTime() {
		return hisOperTime;
	}
	public void setHisOperTime(String hisOperTime) {
		this.hisOperTime = hisOperTime;
	}
	public String getOperateName() {
		return operateName;
	}
	public void setOperateName(String operateName) {
		this.operateName = operateName;
	}
	public int getOperid() {
		return operid;
	}
	public void setOperid(int operid) {
		this.operid = operid;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getHisOperContent() {
		return hisOperContent;
	}
	public void setHisOperContent(String hisOperContent) {
		this.hisOperContent = hisOperContent;
	}
	
}
