package com.frameworkset.platform.cms.driver.services;

import com.frameworkset.platform.cms.channelmanager.ChannelManager;
import com.frameworkset.platform.cms.documentmanager.DocumentManager;
import com.frameworkset.platform.cms.driver.publish.RecursivePublishManager;
import com.frameworkset.platform.cms.sitemanager.SiteManager;
import com.frameworkset.platform.cms.templatemanager.TemplateManager;

public interface CMSService extends DriverConfigurationService,java.io.Serializable{
	DocumentManager getDocumentManager()  ;
	ChannelManager getChannelManager();
	SiteManager getSiteManager();
	TemplateManager getTemplateManager();
	/**
	 * 获取递归发布的接口
	 * @return
	 */
	RecursivePublishManager getRecursivePublishManager();
	

}
