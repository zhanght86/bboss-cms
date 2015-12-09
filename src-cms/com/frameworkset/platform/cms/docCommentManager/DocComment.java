package com.frameworkset.platform.cms.docCommentManager;

import java.util.Date;

import com.frameworkset.orm.annotation.Column;

public class DocComment implements java.io.Serializable {
	private int commentId;              //评论ID
	private int docId;					//文档ID
	private String docTitle;			//文档标题，与前面统一，都用文档子标题
	private String docComment;				//评论内容
	private String userName;			//评论发表人名
	private Date subTime;				//评论发表时间
	@Column(editor="com.frameworkset.platform.util.DateformatEditor")
	private String str_subTime;
	private String userIP;				//评论发表人IP
	private int srcCommentId;			//源评论ID
	private int status;                 //评论状态，0－未审；1－通过；2－删除 
	private int alarm;					//标志此评论是否含有需报警的词，0表示没含，1表示报警
	private int impeachFlag;				//标志此评论是否有举报信息，0表示没有，1表示有
	
	private String docUrl;
	

	public String getDocUrl() {
		return docUrl;
	}
	public void setDocUrl(String docUrl) {
		this.docUrl = docUrl;
	}
	public int getCommentId() {
		return commentId;
	}
	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}
	public int getDocId() {
		return docId;
	}
	public void setDocId(int docId) {
		this.docId = docId;
	}
	public int getSrcCommentId() {
		return srcCommentId;
	}
	public void setSrcCommentId(int srcCommentId) {
		this.srcCommentId = srcCommentId;
	}
	public Date getSubTime() {
		return subTime;
	}
	public void setSubTime(Date subTime) {
		this.subTime = subTime;
	}
	public String getUserIP() {
		return userIP;
	}
	public void setUserIP(String userIP) {
		this.userIP = userIP;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getDocTitle() {
		return docTitle;
	}
	public void setDocTitle(String docTitle) {
		this.docTitle = docTitle;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getAlarm() {
		return alarm;
	}
	public void setAlarm(int alarm) {
		this.alarm = alarm;
	}
	public int getImpeachFlag() {
		return impeachFlag;
	}
	public void setImpeachFlag(int impeachFlag) {
		this.impeachFlag = impeachFlag;
	}
	public String getDocComment() {
		return docComment;
	}
	public void setDocComment(String docComment) {
		this.docComment = docComment;
	}
	public String getStr_subTime() {
		return str_subTime;
	}
	public void setStr_subTime(String str_subTime) {
		this.str_subTime = str_subTime;
	}
	
}
