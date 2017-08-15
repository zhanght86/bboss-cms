/*
  * @(#)BrowserCounter.java
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
package com.frameworkset.platform.sanylog.bean;

import java.sql.Timestamp;

import org.frameworkset.util.annotations.RequestParam;

/**
 * @author gw_hel
 * 浏览器计数
 */
public class BrowserCounter {
	
	/**
	 * 浏览计数器ID 
	 */
	private String browserId;
	
	/**
	 * 文档ID
	 */
	private Long docId;
	
	/**
	 * 文档名称
	 */
	@RequestParam(decodeCharset="UTF-8")
	private String docName;
	
	/**
	 * 站点ID
	 */
	private Integer siteId;
	
	/**
	 * 站点名称
	 */
	@RequestParam(decodeCharset="UTF-8")
	private String siteName;
	
	/**
	 * 频道ID
	 */
	private Integer channelId;
	
	/**
	 * 频道名称
	 */
	@RequestParam(decodeCharset="UTF-8")
	private String channelName;
	
	/**
	 * 默认计数
	 */
	private Integer browserCount;
	
	/**
	 * 浏览IP地址
	 */
	private String browserIp;
	
	/**
	 * 浏览器用户
	 */
	@RequestParam(decodeCharset="UTF-8")
	private String browserUser;
	
	/**
	 * 浏览的页面地址
	 */
	private String pageURL;
	
	/**
	 * 浏览器类型和版本
	 */
	private String browserType;
	
	/**
	 * 浏览时间
	 */
	private Timestamp browserTime;
	
	/**
	 * 浏览的来源地址
	 */
	private String referer;
	
	public String getModuleCode() {
		return moduleCode;
	}

	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}

	public String getModulePath() {
		return modulePath;
	}

	public void setModulePath(String modulePath) {
		this.modulePath = modulePath;
	}
	private String moduleCode;
	@RequestParam(decodeCharset="UTF-8")
	private String modulePath;

	public String getBrowserId() {
		return browserId;
	}

	public void setBrowserId(String browserId) {
		this.browserId = browserId;
	}

	public Long getDocId() {
		return docId;
	}

	public void setDocId(Long docId) {
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

	public Integer getBrowserCount() {
		return browserCount;
	}

	public void setBrowserCount(Integer browserCount) {
		this.browserCount = browserCount;
	}

	public String getBrowserIp() {
		return browserIp;
	}

	public void setBrowserIp(String browserIp) {
		this.browserIp = browserIp;
	}

	public String getBrowserUser() {
		return browserUser;
	}

	public void setBrowserUser(String browserUser) {
		this.browserUser = browserUser;
	}

	public String getPageURL() {
		return pageURL;
	}

	public void setPageURL(String pageURL) {
		this.pageURL = pageURL;
	}

	public String getBrowserType() {
		return browserType;
	}

	public void setBrowserType(String browserType) {
		this.browserType = browserType;
	}

	public Timestamp getBrowserTime() {
		return browserTime;
	}

	public void setBrowserTime(Timestamp browserTime) {
		this.browserTime = browserTime;
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
