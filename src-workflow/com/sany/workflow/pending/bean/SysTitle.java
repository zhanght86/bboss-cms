package com.sany.workflow.pending.bean;
public class SysTitle {
private String id;
private String nameEN;
private String nameCH;
private String pendingUsed;
private String pendingSubscribe;
private String sysDesc;
private String  pendingType;
private String pendingNum;
private String pendingUrl;
private String appUrl;
private String picName;

public String getPicName() {
	return picName;
}

public void setPicName(String picName) {
	this.picName = picName;
}

public String getAppUrl() {
	return appUrl;
}

public void setAppUrl(String appUrl) {
	this.appUrl = appUrl;
}

public String getPendingUrl() {
	return pendingUrl;
}

public void setPendingUrl(String pendingUrl) {
	this.pendingUrl = pendingUrl;
}

public String getPendingSubscribe() {
	return pendingSubscribe;
}

public void setPendingSubscribe(String pendingSubscribe) {
	this.pendingSubscribe = pendingSubscribe;
}

public String getId() {
	return id;
}

public String getPendingUsed() {
	return pendingUsed;
}

public void setPendingUsed(String pendingUsed) {
	this.pendingUsed = pendingUsed;
}

public void setId(String id) {
	this.id = id;
}
public String getNameEN() {
	return nameEN;
}
public void setNameEN(String nameEN) {
	this.nameEN = nameEN;
}
public String getNameCH() {
	return nameCH;
}
public void setNameCH(String nameCH) {
	this.nameCH = nameCH;
}
public String getSysDesc() {
	return sysDesc;
}
public String getPendingType() {
	return pendingType;
}

public void setPendingType(String pendingType) {
	this.pendingType = pendingType;
}

public void setSysDesc(String sysDesc) {
	this.sysDesc = sysDesc;
}

public String getPendingNum() {
	return pendingNum;
}
public void setPendingNum(String pendingNum) {
	this.pendingNum = pendingNum;
}
}
