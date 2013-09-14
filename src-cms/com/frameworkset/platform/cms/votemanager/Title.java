package com.frameworkset.platform.cms.votemanager;

import java.util.List;

public class Title {

	private int id;

	private String content;

	private String name;

	private int siteid;

	private int ipRepeat;
	
	private String state;

	private int active;
	
	private int isTop;

	private int timeGap;

	private int founderID;
	private String foundDate;

	private String foundername;

	private String channelID;
	
	private String channelName;
	
	private List questions;

	private List ipCtrls;
	
	private List timeCtrls;
	private String picpath;
	private String depart_id;
	private int islook;
	private String depart_name;
	private String endTime;
	
	
	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public int getIslook() {
		return islook;
	}

	public void setIslook(int islook) {
		this.islook = islook;
	}

	public String getPicpath() {
		return picpath;
	}

	public void setPicpath(String picpath) {
		this.picpath = picpath;
	}

	public List getIpCtrls() {
		return ipCtrls;
	}

	public void setIpCtrls(List ipCtrls) {
		this.ipCtrls = ipCtrls;
	}

	public List getQuestions() {
		return questions;
	}

	public void setQuestions(List questions) {
		this.questions = questions;
	}

	public List getTimeCtrls() {
		return timeCtrls;
	}

	public void setTimeCtrls(List timeCtrls) {
		this.timeCtrls = timeCtrls;
	}

	public Title() {
		super();
		// TODO 自动生成构造函数存根
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}



	public int getIpRepeat() {
		return ipRepeat;
	}

	public void setIpRepeat(int ipRepeat) {
		this.ipRepeat = ipRepeat;
	}

	public int getTimeGap() {
		return timeGap;
	}

	public void setTimeGap(int timeGap) {
		this.timeGap = timeGap;
	}

	public int getSiteid() {
		return siteid;
	}

	public void setSiteid(int siteid) {
		this.siteid = siteid;
	}

	public int getFounderID() {
		return founderID;
	}

	public void setFounderID(int founderID) {
		this.founderID = founderID;
	}

	public String getFoundername() {
		return foundername;
	}

	public void setFoundername(String foundername) {
		this.foundername = foundername;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public String getChannelID() {
		return channelID;
	}

	public void setChannelID(String channelID) {
		this.channelID = channelID;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getFoundDate() {
		return foundDate;
	}

	public void setFoundDate(String foundDate) {
		this.foundDate = foundDate;
	}
	public int getIsTop() {
		return isTop;
	}

	public void setIsTop(int isTop) {
		this.isTop = isTop;
	}

	public String getDepart_id() {
		return depart_id;
	}

	public void setDepart_id(String depart_id) {
		this.depart_id = depart_id;
	}

	public String getDepart_name() {
		return depart_name;
	}

	public void setDepart_name(String depart_name) {
		this.depart_name = depart_name;
	}


	
	

}
