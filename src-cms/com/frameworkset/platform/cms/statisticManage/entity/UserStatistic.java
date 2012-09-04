package com.frameworkset.platform.cms.statisticManage.entity;

import java.io.Serializable;

public class UserStatistic implements Serializable{
	private int userId;
	private String userName;
	private String userRealName;
	//用户状态
	private int userStatus;
	//所属角色
	private String role;
	//录稿篇数
	private int writeDocs=0;
	//审稿篇数
	private int auditDocs=0;
	//发稿篇数
	private int publishDocs=0;
	//使用率
	private float frequency=0;
	//总字数
	private int totalWords=0;
	//单价(元/千字)
	private float price=0;
	//稿酬
	private float payment=0;
	//机构ID
	private String org_id;
	public float getFrequency() {
		return frequency;
	}
	public void setFrequency(float frequency) {
		this.frequency = frequency;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public int getPublishDocs() {
		return publishDocs;
	}
	public void setPublishDocs(int publishDocs) {
		this.publishDocs = publishDocs;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public int getTotalWords() {
		return totalWords;
	}
	public void setTotalWords(int totalWords) {
		this.totalWords = totalWords;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserRealName() {
		return userRealName;
	}
	public void setUserRealName(String userRealName) {
		this.userRealName = userRealName;
	}
	public int getUserStatus() {
		return userStatus;
	}
	public void setUserStatus(int userStatus) {
		this.userStatus = userStatus;
	}
	public int getWriteDocs() {
		return writeDocs;
	}
	public void setWriteDocs(int writeDocs) {
		this.writeDocs = writeDocs;
	}
	public float getPayment() {
		return payment;
	}
	public void setPayment(float payment) {
		this.payment = payment;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getAuditDocs() {
		return auditDocs;
	}
	public void setAuditDocs(int auditDocs) {
		this.auditDocs = auditDocs;
	}
	public String getOrg_id() {
		return org_id;
	}
	public void setOrg_id(String org_id) {
		this.org_id = org_id;
	}
}
