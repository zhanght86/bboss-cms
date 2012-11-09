package com.frameworkset.platform.sanylog.action;

import java.net.URLDecoder;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.frameworkset.util.annotations.ResponseBody;

import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.platform.sanylog.bean.BrowserCounter;
import com.frameworkset.platform.sanylog.bean.Module;
import com.frameworkset.platform.sanylog.bean.OperateCounter;
import com.frameworkset.platform.sanylog.service.ConfigManager;
import com.frameworkset.platform.sanylog.service.CounterManager;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.util.StringUtil;

public class InterfaceController {
	private final String[] browserTypeSet = { "MSIE", "Firefox", "Safari", "Chrome" };
    private ConfigManager  configManager;
	private CounterManager counterManager;
	private Logger log = Logger.getLogger(InterfaceController.class);
	public @ResponseBody(datatype = "jsonp")
	long operateCounter(OperateCounter paramCounter, boolean enable, HttpServletRequest request) throws Exception {

		paramCounter.setOperateId(UUID.randomUUID().toString());
       
		String appName = paramCounter.getAppName();
		String moduleName = paramCounter.getModuleName();
		String moduleCode = paramCounter.getModuleCode();
		String modulePath = paramCounter.getModulePath();
		
		String pageName = paramCounter.getPageName();
		String operContent = paramCounter.getOperContent();
		String operator = paramCounter.getOperator();
		if(null!=operator&&!"".equals(operator)){
			paramCounter.setOperator(operator);
		}else{
			paramCounter.setOperator("guest");
		}
		paramCounter.setModulePath(StringUtil.isEmpty(modulePath) ? null : modulePath);
		paramCounter.setAppName(StringUtil.isEmpty(appName) ? null : appName);
		paramCounter.setModuleName(StringUtil.isEmpty(moduleName) ? null : moduleName);
		paramCounter.setPageName(StringUtil.isEmpty(pageName) ? null : pageName);
		paramCounter.setOperContent(StringUtil.isEmpty(operContent) ? null : operContent);
		System.out.println(paramCounter.getAppName()+"---------------------------"+paramCounter.getModuleName());
		Module module = configManager.checkAppModule(paramCounter.getAppName(), paramCounter.getModuleName(),paramCounter.getModuleCode(),paramCounter.getModulePath());//检查 appId  moduleId
		paramCounter.setAppId(Integer.parseInt(module.getAppId()));
		paramCounter.setModuleId(Integer.parseInt(module.getModuleId()));
		paramCounter.setOperateIp(request.getRemoteAddr());
		paramCounter.setPageURL(paramCounter.getPageURL());
//设置浏览器类型
		String userAgent = request.getHeader("User-Agent");
		for (String agent : userAgent.split(";")) {
			for (String browser : browserTypeSet) {
				if (agent.indexOf(browser) > 0) {
					paramCounter.setBrowserType(agent.substring(agent.indexOf(browser)).replaceAll("/", " "));
					break;
				}
			}
		}
		//设置访问来源的url
		paramCounter.setReferer(request.getHeader("Referer"));
//事务提交代码
		TransactionManager tm = new TransactionManager();
		try {
			tm.begin(TransactionManager.RW_TRANSACTION);
			counterManager.incrementOperateCounter(paramCounter);//新增一条记录在基础计数表中
			long ret = 0;
			/*if (enable) {
				ret = counterManager.getBrowserCount(paramCounter.getSiteId());
			}*/
			tm.commit();
			return ret;
		} finally {
			tm.release();
		}
	}
	/**
	 * 浏览器计数
	 * @param paramCounter 计数器参数
	 * @param enable 是否启用
	 * @param request HttpServletRequest
	 * @return long 浏览计数 
	 * @throws Exception
	 */
	public @ResponseBody(datatype = "jsonp")
	long browserCounter(BrowserCounter paramCounter, boolean enable, HttpServletRequest request) throws Exception {
       log.info("------------------here-------------------");
		paramCounter.setBrowserId(UUID.randomUUID().toString());

		String siteName = paramCounter.getSiteName();
		String channelName = paramCounter.getChannelName();
		String docName = paramCounter.getDocName();
		String moduleCode = paramCounter.getModuleCode();
		String modulePath = paramCounter.getModulePath();
		String browserUser = paramCounter.getBrowserUser();
		if(null!=browserUser&&!"".equals(browserUser)){
			paramCounter.setBrowserUser(browserUser);
		}else{
			paramCounter.setBrowserUser("guest");
		}
		
		paramCounter.setModuleCode(StringUtil.isEmpty(moduleCode) ? null : moduleCode);
		paramCounter.setModulePath(StringUtil.isEmpty(modulePath) ? null : modulePath);

		paramCounter.setSiteName(StringUtil.isEmpty(siteName) ? null : siteName);
		paramCounter.setChannelName(StringUtil.isEmpty(channelName) ? null : channelName);
		paramCounter.setDocName(StringUtil.isEmpty(docName) ? null : docName);
		System.out.println(paramCounter.getSiteName()+"---------------------------"+paramCounter.getChannelName());
		Module module = configManager.checkAppModule(paramCounter.getSiteName(), paramCounter.getChannelName(),paramCounter.getModuleCode(),paramCounter.getModulePath());//检查 appId  moduleId
		
		paramCounter.setSiteId(Integer.parseInt(module.getAppId()));
		paramCounter.setChannelId(Integer.parseInt(module.getModuleId()));
		paramCounter.setBrowserIp(request.getRemoteAddr());
		paramCounter.setPageURL(paramCounter.getPageURL());
//设置浏览器类型
		String userAgent = request.getHeader("User-Agent");
		for (String agent : userAgent.split(";")) {
			for (String browser : browserTypeSet) {
				if (agent.indexOf(browser) > 0) {
					paramCounter.setBrowserType(agent.substring(agent.indexOf(browser)).replaceAll("/", " "));
					break;
				}
			}
		}
		//设置访问来源的url
		paramCounter.setReferer(request.getHeader("Referer"));
//事务提交代码
		TransactionManager tm = new TransactionManager();
		try {
			tm.begin(TransactionManager.RW_TRANSACTION);
			counterManager.incrementBrowserCounter(paramCounter);//新增一条记录在基础计数表中
			long ret = 0;
			if (enable) {
				ret = counterManager.getBrowserCount(paramCounter.getSiteId());
			}
			tm.commit();
			return ret;
		} finally {
			tm.release();
		}
	}
	
}
