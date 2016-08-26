/*
  * @(#)DocumentCondition.java
 * 
 * Copyright @ 2001-2012 SANY Group Co.,Ltd.
 * All right reserved.
 * 
 * 这个软件是属于三一集团有限公司机密的和私有信息，不得泄露。
 * 并且只能由三一集团有限公司内部员工在得到许可的情况下才允许使用。
 * This software is the confidential and proprietary information
 * of SANY Group Co, Ltd. You shall not disclose such
 * Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with
 * SANY Group Co, Ltd.
 */
package com.frameworkset.platform.cms.documentmanager.bean;

/**
 * @author gw_hel
 * 文档查询条件
 */
public class DocumentCondition {
	
	/**
	 * 文档ID
	 */
	private int documentId;
	
	/**
	 * 站点ID
	 */
	private int siteId;
	
	/**
	 * 频道ID
	 */
	private int channelId;
	
	/**
	 * 开始时间
	 */
	private String startTime;
	
	/**
	 * 结束时间
	 */
	private String endTime;
	
	/**
	 * 关键字
	 */
	private String keywords;
	
	/**
	 * 按照排序时间排序
	 */
	private String orderByTime;
	
	/**
	 * 按照播放次数排序
	 */
	private String orderByPlay;
	
	/**
	 * 按照评论数量排序
	 */
	private String orderByComment;

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public int getSiteId() {
		return siteId;
	}

	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}

	public int getChannelId() {
		return channelId;
	}

	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}

	public String getOrderByTime() {
		return orderByTime;
	}

	public void setOrderByTime(String orderByTime) {
		this.orderByTime = orderByTime;
	}

	public String getOrderByPlay() {
		return orderByPlay;
	}

	public void setOrderByPlay(String orderByPlay) {
		this.orderByPlay = orderByPlay;
	}

	public String getOrderByComment() {
		return orderByComment;
	}

	public void setOrderByComment(String orderByComment) {
		this.orderByComment = orderByComment;
	}

	public int getDocumentId() {
		return documentId;
	}

	public void setDocumentId(int documentId) {
		this.documentId = documentId;
	}
}
