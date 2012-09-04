package com.frameworkset.platform.cms.statisticManage.entity;

import java.io.Serializable;
import java.util.Date;

public class DocStatistic implements Serializable {
	//文档标题
	private String title;
	//文档来源
	private String sourceName;
	//文档状态
	private String status;
	//所属频道
	private String channelName;
//	所属频道Id
	private int channelId;
	//撰写时间
	private Date writeTime;
	//作者
	private String author;
	//发稿人
	private String createuserName;
	//字数
	private int wordsNum;
	//文档ID
	private int documentId;
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public String getCreateuserName() {
		return createuserName;
	}
	public void setCreateuserName(String createuserName) {
		this.createuserName = createuserName;
	}
	public String getSourceName() {
		return sourceName;
	}
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getWordsNum() {
		return wordsNum;
	}
	public void setWordsNum(int wordsNum) {
		this.wordsNum = wordsNum;
	}
	public Date getWriteTime() {
		return writeTime;
	}
	public void setWriteTime(Date writeTime) {
		this.writeTime = writeTime;
	}
	public int getDocumentId() {
		return documentId;
	}
	public void setDocumentId(int documentId) {
		this.documentId = documentId;
	}
	public int getChannelId() {
		return channelId;
	}
	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}
}
