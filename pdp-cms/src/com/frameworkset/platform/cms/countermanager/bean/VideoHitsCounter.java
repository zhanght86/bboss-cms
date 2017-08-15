/*
  * @(#)VideoHitsCounter.java
 * 
 * Copyright @ 2001-2012 SANY Group Co.,Ltd.
 * All right reserved.
 * 
 * 这个软件是属于bbossgroups有限公司机密的和私有信息，不得泄露。
 * 并且只能由bbossgroups有限公司内部员工在得到许可的情况下才允许使用。
 * This software is the confidential and proprietary information
 * of SANY Group Co, Ltd. You shall not disclose such
 * Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with
 * SANY Group Co, Ltd.
 */
package com.frameworkset.platform.cms.countermanager.bean;

import java.sql.Timestamp;

/**
 * @author gw_hel
 * 视频点播计数器
 */
public class VideoHitsCounter {
	
	/**
	 * 计数器ID
	 */
	private String hitId;
	
	/**
	 * 文档ID
	 */
	private Integer docId;
	
	/**
	 * 文档名称
	 */
	private String docName;
	
	/**
	 * 站点ID
	 */
	private Integer siteId;
	
	/**
	 * 站点名称
	 */
	private String siteName;
	
	/**
	 * 频道ID
	 */
	private Integer channelId;
	
	/**
	 * 频道名称
	 */
	private String channelName;
	
	/**
	 * 播放计数
	 */
	private Integer hitCount;

	/**
	 * 点击的用户
	 */
	private String hitUser;
	
	/**
	 * 点击IP地址
	 */
	private String hitIP;
	
	/**
	 * 视频地址
	 */
	private String videoPath;
	
	/**
	 * 点击时间
	 */
	private Timestamp hitTime;
	
	/**
	 * 浏览的来源地址
	 */
	private String referer;

	public String getHitId() {
		return hitId;
	}

	public void setHitId(String hitId) {
		this.hitId = hitId;
	}

	public Integer getDocId() {
		return docId;
	}

	public void setDocId(Integer docId) {
		this.docId = docId;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public Integer getChannelId() {
		return channelId;
	}

	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}

	public Integer getHitCount() {
		return hitCount;
	}

	public void setHitCount(Integer hitCount) {
		this.hitCount = hitCount;
	}

	public String getHitUser() {
		return hitUser;
	}

	public void setHitUser(String hitUser) {
		this.hitUser = hitUser;
	}

	public String getVideoPath() {
		return videoPath;
	}

	public void setVideoPath(String videoPath) {
		this.videoPath = videoPath;
	}

	public Timestamp getHitTime() {
		return hitTime;
	}

	public void setHitTime(Timestamp hitTime) {
		this.hitTime = hitTime;
	}

	public String getHitIP() {
		return hitIP;
	}

	public void setHitIP(String hitIP) {
		this.hitIP = hitIP;
	}

	public String getReferer() {
		return referer;
	}

	public void setReferer(String referer) {
		this.referer = referer;
	}

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
}
