package com.frameworkset.platform.cms.votemanager;

public class Item {
	private int id;

	private int qid;

	private String options;

	private int count;
	private String score;

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	private double percentCount;
	public double getPercentCount() {
		return percentCount;
	}

	public void setPercentCount(double percentCount) {
		this.percentCount = percentCount;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}

	public int getQid() {
		return qid;
	}

	public void setQid(int qid) {
		this.qid = qid;
	}

}
