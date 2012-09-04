package com.frameworkset.platform.cms.statisticManage.entity;

import java.io.Serializable;

public class ChannelStatistic implements Serializable{
	//站点名称
	private String siteName;
	//频道名称
	private String channelName;
	//录入篇数
	private int writeNum;
	//审稿篇数
	private int auditNum;
	//发稿篇数
	private int publishNum;
	//使用率(%)
	private float frequency;
	//总字数
	private int totalWords;
	//频道ID
	private int channelId;
	public int getChannelId() {
		return channelId;
	}
	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public float getFrequency() {
		return frequency;
	}
	public void setFrequency(float frequency) {
		this.frequency = frequency;
	}
	public int getPublishNum() {
		return publishNum;
	}
	public void setPublishNum(int publishNum) {
		this.publishNum = publishNum;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public int getTotalWords() {
		return totalWords;
	}
	public void setTotalWords(int totalWords) {
		this.totalWords = totalWords;
	}
	public int getWriteNum() {
		return writeNum;
	}
	public void setWriteNum(int writeNum) {
		this.writeNum = writeNum;
	}
	public int getAuditNum() {
		return auditNum;
	}
	public void setAuditNum(int auditNum) {
		this.auditNum = auditNum;
	}
}
