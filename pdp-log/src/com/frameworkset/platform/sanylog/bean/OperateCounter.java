package com.frameworkset.platform.sanylog.bean;

import java.sql.Timestamp;

import org.frameworkset.util.annotations.RequestParam;

public class OperateCounter {
	//主键
	private String operateId;
	private String moduleCode;
	@RequestParam(decodeCharset="UTF-8")
	private String modulePath;
	public String getOperateId() {
		return operateId;
	}

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

	public void setOperateId(String operateId) {
		this.operateId = operateId;
	}

	//应用ID
	private Integer appId;
	public Integer getAppId() {
		return appId;
	}

	public void setAppId(Integer appId) {
		this.appId = appId;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	//应用名称
	@RequestParam(decodeCharset="UTF-8")
	private String appName;
	//模块ID
	private Integer moduleId;
	//模块名称
	@RequestParam(decodeCharset="UTF-8")
	private String moduleName;
	//页面ID
	private Integer pageId;
	//页面名称
	@RequestParam(decodeCharset="UTF-8")
	private String pageName;
	//访问页面URL
	private String pageURL;
	//来源页面URL
	private String referer;
	//浏览器类型
	private String browserType;
	//IP
	private String operateIp;
	//操作人
	@RequestParam(decodeCharset="UTF-8")
	private String operator;
	//操作内容
    @RequestParam(decodeCharset="UTF-8")
	private String operContent;
	//操作类型
	private String operation;
	//操作时间
	private Timestamp operTime;
	//日志级别
	private Integer logLevel;

	

	public Integer getModuleId() {
		return moduleId;
	}

	public void setModuleId(Integer moduleId) {
		this.moduleId = moduleId;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public Integer getPageId() {
		return pageId;
	}

	public void setPageId(Integer pageId) {
		this.pageId = pageId;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public String getPageURL() {
		return pageURL;
	}

	public void setPageURL(String pageURL) {
		this.pageURL = pageURL;
	}

	public String getReferer() {
		return referer;
	}

	public void setReferer(String referer) {
		this.referer = referer;
	}

	public String getBrowserType() {
		return browserType;
	}

	public void setBrowserType(String browserType) {
		this.browserType = browserType;
	}

	public String getOperateIp() {
		return operateIp;
	}

	public void setOperateIp(String operateIp) {
		this.operateIp = operateIp;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getOperContent() {
		return operContent;
	}

	public void setOperContent(String operContent) {
		this.operContent = operContent;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public Timestamp getOperTime() {
		return operTime;
	}

	public void setOperTime(Timestamp operTime) {
		this.operTime = operTime;
	}

	public Integer getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(Integer logLevel) {
		this.logLevel = logLevel;
	}

}
