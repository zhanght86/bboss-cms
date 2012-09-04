/*
 * @(#)DownLoadCounter.java
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
package com.frameworkset.platform.cms.countermanager.bean;

import java.sql.Timestamp;

/**
 * @author gw_hel
 * 下载计数器
 */
public class DownLoadCounter {

	/**
	 * 下载计数器ID
	 */
	private String countId;
	
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
	 * 下载技术
	 */
	private Integer downloadCount;
	
	/**
	 * 附件地址
	 */
	private String attachPath;

	/**
	 * 附件ID
	 */
	private Long attachId;

	/**
	 * 下载IP
	 */
	private String downloadIP;
	
	/**
	 * 下载时间
	 */
	private Timestamp downloadTime;

	public String getCountId() {
		return countId;
	}

	public void setCountId(String countId) {
		this.countId = countId;
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

	public Integer getDownloadCount() {
		return downloadCount;
	}

	public void setDownloadCount(Integer downloadCount) {
		this.downloadCount = downloadCount;
	}

	public String getAttachPath() {
		return attachPath;
	}

	public void setAttachPath(String attachPath) {
		this.attachPath = attachPath;
	}

	public Long getAttachId() {
		return attachId;
	}

	public void setAttachId(Long attachId) {
		this.attachId = attachId;
	}

	public String getDownloadIP() {
		return downloadIP;
	}

	public void setDownloadIP(String downloadIP) {
		this.downloadIP = downloadIP;
	}

	public Timestamp getDownloadTime() {
		return downloadTime;
	}

	public void setDownloadTime(Timestamp downloadTime) {
		this.downloadTime = downloadTime;
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

	public void setDocId(Integer docId) {
		this.docId = docId;
	}

	public Integer getDocId() {
		return docId;
	}

}
