package com.frameworkset.platform.sysmgrcore.web.struts.form;

import java.io.Serializable;


public class NoticForm implements Serializable{
	private int noticID;

	private int executorID;

	private String beginTime;

	private String endTime;

	private String place;
	
	private String topic;

	private String source;
	
	private int noticPlannerID;
	
	private String noticPlannerName;
	
	private int status;
	
	private String content;

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public int getExecutorID() {
		return executorID;
	}

	public void setExecutorID(int executorID) {
		this.executorID = executorID;
	}

	public int getNoticID() {
		return noticID;
	}

	public void setNoticID(int noticID) {
		this.noticID = noticID;
	}

	public int getNoticPlannerID() {
		return noticPlannerID;
	}

	public void setNoticPlannerID(int noticPlannerID) {
		this.noticPlannerID = noticPlannerID;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getNoticPlannerName() {
		return noticPlannerName;
	}

	public void setNoticPlannerName(String noticPlannerName) {
		this.noticPlannerName = noticPlannerName;
	}
}
