package com.frameworkset.platform.cms.votemanager;



public class Answer {

	private int answerID;

	private String answer;

	
	private int qid;

	private int type;
	private int itemId;
	private String whoIp;
	
	private String when;
	private String qtitle;
	private int isBigTitle;
	private int state;

	
	public int getState() {
		return state;
	}


	public void setState(int state) {
		this.state = state;
	}


	public Answer() {
		super();
		// TODO 自动生成构造函数存根
	}


	public String getAnswer() {
		return answer;
	}


	public void setAnswer(String answer) {
		this.answer = answer;
	}


	public int getAnswerID() {
		return answerID;
	}


	public void setAnswerID(int answerID) {
		this.answerID = answerID;
	}


	public int getIsBigTitle() {
		return isBigTitle;
	}


	public void setIsBigTitle(int isBigTitle) {
		this.isBigTitle = isBigTitle;
	}


	public int getQid() {
		return qid;
	}


	public void setQid(int qid) {
		this.qid = qid;
	}


	public int getType() {
		return type;
	}


	public void setType(int type) {
		this.type = type;
	}


	public int getItemId() {
		return itemId;
	}


	public void setItemId(int itemId) {
		this.itemId = itemId;
	}


	public String getWhoIp() {
		return whoIp;
	}


	public void setWhoIp(String whoIp) {
		this.whoIp = whoIp;
	}


	public String getWhen() {
		return when;
	}


	public void setWhen(String when) {
		this.when = when;
	}


	public String getQtitle() {
		return qtitle;
	}


	public void setQtitle(String qtitle) {
		this.qtitle = qtitle;
	}


	

	

}
