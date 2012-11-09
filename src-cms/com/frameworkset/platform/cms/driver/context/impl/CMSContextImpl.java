package com.frameworkset.platform.cms.driver.context.impl;

import com.frameworkset.platform.cms.container.Template;
import com.frameworkset.platform.cms.driver.config.DriverConfigurationException;
import com.frameworkset.platform.cms.driver.context.CMSContext;
import com.frameworkset.platform.cms.driver.context.Context;
import com.frameworkset.platform.cms.driver.context.ContextException;
import com.frameworkset.platform.cms.driver.publish.PublishObject;
import com.frameworkset.platform.cms.driver.publish.impl.PublishMonitor;
import com.frameworkset.platform.cms.sitemanager.SiteManagerException;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.platform.cms.util.FileUtil;

public class CMSContextImpl extends BaseContextImpl implements CMSContext {

	private void evalFileName()
	{
		
		this.tempFileName = fileName;
		if(indexTemplate != null)
			this.jspFileName = CMSUtil.getJspFileName(this.fileName, this.indexTemplate.getTemplateId() + "");
	}
	
	/**
	 * 站点首页模版
	 */
	private Template indexTemplate;

	/**
	 * 站点发布的上下文
	 * @param siteID 发布站点id
	 * @param driverConfiguration 内容管理驱动配置
	 * @param publisher 发布者信息
	 * @param isRemote 是否远程发布
	 * @param distributeManners 发布结果分发方式
	 * @param publishObject 发布任务对象
	 * @param monitor 发布任务监控器
	 */

	public CMSContextImpl(String siteID,
			String[] publisher, 
			PublishObject publishObject, 
			PublishMonitor monitor) {
		super(siteID,null,publishObject,monitor);
//		this.driverConfiguration = driverConfiguration;
		this.publisher = publisher;
		
		
		try {
			this.indexTemplate = this.getDriverConfiguration().getCMSService().getSiteManager().getIndexTemplate(siteID);
//			this.indexTemplate = this.testindexTemplate;
			
			this.fileName = site.getIndexFileName();
			if(fileName == null || fileName.equals(""))
				fileName = "default.htm";
//			this.tempFileName = fileName;
			evalFileName();
			this.fileExt = site.getIndexFileSuffix();
			
			if(fileExt == null) 
				fileExt = FileUtil.getFileExtByFileName(fileName);
			this.mimeType = CMSUtil.getMimeType(fileExt);
			this.publishPath = "";
			
			
			/**
			 * 动态和静态模式
			 */
			this.publishMode = CMSUtil.getPublishMode(new Integer(site
					.getPublishMode()));
			try
			{
				// 获取站点允许的发布状态
				this.enablePublishStatus = this.getDriverConfiguration().getCMSService()
						.getSiteManager().getEnablePublishStatus(siteID);
			}
			catch(Exception e)
			{
				//e.printStackTrace();
			}
			
			
			
			
			
			
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SiteManagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DriverConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ContextException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	public CMSContextImpl(String siteid, Context parentContext, PublishObject publishObject, PublishMonitor monitor) {
		super(siteid,parentContext,publishObject,monitor);
//		this.driverConfiguration = driverConfiguration;
		
		
		
		try {

			
			this.indexTemplate = this.getDriverConfiguration().getCMSService().getSiteManager().getIndexTemplate(siteID);
//			this.indexTemplate = this.testindexTemplate;
			
			this.fileName = site.getIndexFileName();
			if(fileName == null || fileName.equals(""))
				fileName = "default.htm";
//			this.tempFileName = fileName;
			evalFileName();
			this.fileExt = site.getIndexFileSuffix();
			this.localPublishDestination = site.getLocalPublishPath();
			if(fileExt == null) 
				fileExt = FileUtil.getFileExtByFileName(fileName);
			this.mimeType = CMSUtil.getMimeType(fileExt);
			this.publishPath = "";
			
			;
			/**
			 * 动态和静态模式
			 */
			this.publishMode = CMSUtil.getPublishMode(new Integer(site
					.getPublishMode()));
			try
			{
				// 获取站点允许的发布状态
				this.enablePublishStatus = this.getDriverConfiguration().getCMSService()
						.getSiteManager().getEnablePublishStatus(siteID);
			}
			catch(Exception e)
			{
				//e.printStackTrace();
			}

			
		
			
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SiteManagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DriverConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ContextException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String toString() {
		StringBuffer buffer = new StringBuffer();

		return buffer.append("Context:[siteid=").append(this.getSiteID())
				.append(",").append("dbname=").append(this.getDBName()).append(
						"]").toString();

	}

//	public String getDomain() {
//
//		return site.getWebHttp();
//	}

//	public FTPConfig getFtpConfig() {
//
//		return this.FTPConfig;
//	}
//
//	public String getSiteDir() {
//		return this.siteDir;
//	}
//
//	public Site getSite() {
//		return site;
//	}

	
	public Template getIndexTemplate() {
		return indexTemplate;
	}
	public boolean haveIndexTemplate() {
		// TODO Auto-generated method stub
		return this.indexTemplate != null;
	}



}
