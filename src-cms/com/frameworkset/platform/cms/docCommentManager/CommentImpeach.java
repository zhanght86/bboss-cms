package com.frameworkset.platform.cms.docCommentManager;

import java.util.Date;

public class CommentImpeach {
	private int id;
	private int commentId;
	private String impeacher;
	private String email;
	private int reason;
	private String description;
	private Date comtime;
	private int repled;      //引用类型标志，0－引用文档；1－引用频道
	public int getRepled() {
		return repled;
	}
	public void setRepled(int repled) {
		this.repled = repled;
	}
	public int getCommentId() {
		return commentId;
	}
	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}
	public Date getComtime() {
		return comtime;
	}
	public void setComtime(Date comtime) {
		this.comtime = comtime;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getImpeacher() {
		return impeacher;
	}
	public void setImpeacher(String impeacher) {
		this.impeacher = impeacher;
	}
	public int getReason() {
		return reason;
	}
	public void setReason(int reason) {
		this.reason = reason;
	}
	
}
