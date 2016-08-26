package com.frameworkset.platform.cms.driver.services.impl.resource;

import javax.servlet.ServletContext;

import com.frameworkset.platform.cms.channelmanager.ChannelManager;
import com.frameworkset.platform.cms.channelmanager.ChannelManagerImpl;
import com.frameworkset.platform.cms.documentmanager.DocumentManager;
import com.frameworkset.platform.cms.documentmanager.DocumentManagerImpl;
import com.frameworkset.platform.cms.driver.config.DriverConfigurationException;
import com.frameworkset.platform.cms.driver.publish.RecursivePublishManager;
import com.frameworkset.platform.cms.driver.publish.impl.RecursivePublishManagerImpl;
import com.frameworkset.platform.cms.driver.services.CMSService;
import com.frameworkset.platform.cms.sitemanager.SiteManager;
import com.frameworkset.platform.cms.sitemanager.SiteManagerImpl;
import com.frameworkset.platform.cms.templatemanager.TemplateManager;
import com.frameworkset.platform.cms.templatemanager.TemplateManagerImpl;

public class CMSServiceImpl implements CMSService{
	private ServletContext ctx = null;
	private DocumentManager documentManager;
	private ChannelManager channelManager;
	private SiteManager siteManager; 
	private TemplateManager templateManager;
	private RecursivePublishManager recursivePublishManager;
	public DocumentManager getDocumentManager()
	{
		if(documentManager == null)
			documentManager = new DocumentManagerImpl();
		return documentManager;
	}
	public ChannelManager getChannelManager()
	{
		if(channelManager == null)
			channelManager = new ChannelManagerImpl();
		return channelManager;
	}
	public SiteManager getSiteManager()
	{
		if(siteManager == null)
			siteManager = new SiteManagerImpl();
		return siteManager;
	}
	public TemplateManager getTemplateManager()
	{
		if(templateManager == null)
			templateManager = new TemplateManagerImpl();
		return templateManager;
	}
	public void init(ServletContext ctx) throws DriverConfigurationException {
		this.ctx = ctx;
//		this.getChannelManager().init(ctx);
//		this.getSiteManager().init(ctx);
//		this.getDocumentManager().init(ctx);
//		this.templateManager.init(ctx);
	}
	public void destroy() throws DriverConfigurationException {
		this.channelManager = null;
		this.ctx = null;
		
	}
	
	public RecursivePublishManager getRecursivePublishManager() {
		if(recursivePublishManager == null)
			recursivePublishManager = new RecursivePublishManagerImpl();
		
		return recursivePublishManager;
	}

}
