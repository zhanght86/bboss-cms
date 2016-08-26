package com.frameworkset.platform.cms.votemanager;

import java.util.List;


public class Question {
	private int id;
	
	private int titleID;

	private String title;

	private int style;

	private int votecount;

	private int active;
	private int isTop;
	
	private List items;
	
	private List answers;
	
	private String surveyName;

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getStyle() {
		return style;
	}

	public void setStyle(int style) {
		this.style = style;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getVotecount() {
		return votecount;
	}

	public void setVotecount(int votecount) {
		this.votecount = votecount;
	}

	public List getAnswers() {
		return answers;
	}

	public void setAnswers(List answers) {
		this.answers = answers;
	}

	public List getItems() {
		return items;
	}

	public void setItems(List items) {
		this.items = items;
	}

	public int getTitleID() {
		return titleID;
	}

	public void setTitleID(int titleID) {
		this.titleID = titleID;
	}

	public String getSurveyName() {
		return surveyName;
	}

	public void setSurveyName(String surveyName) {
		this.surveyName = surveyName;
	}

	public int getIsTop() {
		return isTop;
	}

	public void setIsTop(int isTop) {
		this.isTop = isTop;
	}

	

}
