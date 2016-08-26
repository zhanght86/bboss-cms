package com.frameworkset.platform.cms.votemanager;



public class TimeCtrl {

	private int id;

	private int titleId;

	private String timeStart;

	private String timeEnd;

	public TimeCtrl() {
		super();
		// TODO 自动生成构造函数存根
	}

	public int getId() {
		return id;
	}

	public String getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(String timeEnd) {
		this.timeEnd = timeEnd;
	}

	public String getTimeStart() {
		return timeStart;
	}

	public void setTimeStart(String timeStart) {
		this.timeStart = timeStart;
	}

	public void setId(int id) {
		this.id = id;
	}



	

	public int getTitleId() {
		return titleId;
	}

	public void setTitleId(int titleId) {
		this.titleId = titleId;
	}

}
