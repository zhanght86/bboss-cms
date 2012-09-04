package com.frameworkset.platform.cms.driver.publish.impl;

import java.util.List;
import java.util.Set;

import com.frameworkset.platform.cms.channelmanager.Channel;
import com.frameworkset.platform.cms.container.Template;
import com.frameworkset.platform.cms.driver.config.DriverConfigurationException;
import com.frameworkset.platform.cms.driver.context.CMSContext;
import com.frameworkset.platform.cms.driver.context.Context;
import com.frameworkset.platform.cms.driver.context.impl.CMSContextImpl;
import com.frameworkset.platform.cms.driver.jsp.CMSRequestContext;
import com.frameworkset.platform.cms.driver.publish.PublishException;
import com.frameworkset.platform.cms.driver.publish.PublishObject;
import com.frameworkset.platform.cms.driver.publish.RecursivePublishException;
import com.frameworkset.platform.cms.driver.publish.RecursivePublishManager;
import com.frameworkset.platform.cms.sitemanager.SiteManagerException;
import com.frameworkset.platform.cms.util.AttributeKeys;
import com.frameworkset.platform.cms.util.CMSUtil;

/**
 * 
 * <p>Title: com.frameworkset.platform.cms.driver.publish.impl.SitePublishObject.java</p>
 *
 * <p>Description: 站点发布管理对象</p>
 *
 * <p>Copyright (c) 2007</p>
 *
 * <p>Company: iSany</p>
 * @Date 2007-1-29
 * @author biaoping.yin 
 * @version 1.0
 */
public class SitePublishObject extends PublishObject implements java.io.Serializable {
	/**
	 * 构建新的站点发布对象，
	 * @param requestContext 请求上下文
	 * @param publishScope 发布范围
	 * @param params
	 */
	public SitePublishObject(CMSRequestContext requestContext, 
							 int[] publishScope,
							 String[] params,
							 String[] publisher,
							 boolean isRoot,
							 boolean[] local2ndRemote 	,
							 int[] distributeManners ) {
		
		super(requestContext, publishScope, params,
				local2ndRemote,
				 distributeManners,isRoot,publisher);		
		
		
	}
	
	/**
	 * 构建站点的发布上下文
	 * @param requestContext
	 * @param params
	 * @param context
	 */

	public SitePublishObject(CMSRequestContext requestContext, 
							 String[] params,
							Context context ) {
		
		super(requestContext,params,
				context);
		
		
	}

	public SitePublishObject(CMSRequestContext requestContext, String[] params, Context context, int[] publishScope) {
		super(requestContext,params,
				context);
		this.publishScope = publishScope;
	}

	public Context buildContext() {
		return buildCMSContext();		
		
	}
	
	/**
	 * 判断是否需要发布站点
	 * @return
	 */
	protected boolean needPublishSiteIndex()
	{
		for(int i = 0; i < this.publishScope.length; i++)
		{
			if(publishScope[i] == PublishObject.PUBLISH_SITE_INDEX)
				return true;
		}
		return false;
	}
	
	protected boolean needPublishChannel()
	{
		for(int i = 0; i < this.publishScope.length; i++)
		{
			if(publishScope[i] == PublishObject.PUBLISH_CHANNEL_INDEX
				||publishScope[i] == PublishObject.PUBLISH_CHANNEL_RECINDEX
				||publishScope[i] == PublishObject.PUBLISH_CHANNEL_CONTENT
				||publishScope[i] == PublishObject.PUBLISH_CHANNEL_RECCONTENT)
				return true;
		}
		return false;
	}
	
	private CMSContext buildCMSContext()
	{
		if(requestContext != null && this.params == null)
		{
			String siteid = this.requestContext.getRequest().getParameter(AttributeKeys.CMSSITEREQUESTKEY);
			CMSContext cmsContext = new CMSContextImpl(siteid,
													   this.publisher,													   
													   this,
													   monitor);
			parentContext.setEnableRecursive(this.enableRecursive());
			parentContext.setClearFileCache(this.isClearFileCache());
			cmsContext.setMaxPublishDepth(this.getMaxPublishDepth());
			cmsContext.setCurrentPublishDepth(0);
			return cmsContext;
		}
		else 
		{
			CMSContext cmsContext = null;
			if(this.parentContext == null)
			{
				cmsContext = new CMSContextImpl(this.params[0],							   
														   this.publisher,this,
														   monitor);
				cmsContext.setEnableRecursive(this.enableRecursive());
				cmsContext.setClearFileCache(this.isClearFileCache());
			  
				cmsContext.setMaxPublishDepth(this.getMaxPublishDepth());
				cmsContext.setCurrentPublishDepth(0);
			}
			else
			{
				cmsContext = new CMSContextImpl(this.params[0],	
												parentContext,
												this,
												parentContext.getPublishMonitor().createSubPublishMonitor());
				cmsContext.setMaxPublishDepth(parentContext.getMaxPublishDepth());
				cmsContext.setCurrentPublishDepth(parentContext.getCurrentPublishDepth() + 1);
			}
				
			return cmsContext;
		}
	}
	
	public void initScriptlet() throws PublishException{
		Template indexTemplate = ((CMSContext )context).getIndexTemplate();
		this.script = ScriptletUtil.createScriptlet(context,indexTemplate);		
	}
	
	
	
	/**
	 * 递归发布站点的所有频道信息
	 *
	 */
	private void publishChannels() throws SitePublishException
	{
		try {
			/**
			 * 获取站点的第一级频道
			 */
			String siteid = ((CMSContext)this.context).getSiteID();
			List channels = this.context.getDriverConfiguration()
				.getCMSService()
				.getSiteManager().getDirectChannelsOfSite(siteid);
			for(int i = 0; channels != null && i < channels.size(); i ++)
			{
				Channel channel = (Channel) channels.get(i);
				PublishObject channelPublishObject = new ChannelPublishObject(this.requestContext,new String[]{siteid,channel.getChannelId() + ""},context);
				context.getDriverConfiguration()
					   .getPublishEngine()
					   .publish(channelPublishObject);
			}
		} catch (SiteManagerException e) {
			
			e.printStackTrace();
		} catch (DriverConfigurationException e) {
			
			e.printStackTrace();
		} catch (PublishException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void publishSiteIndex() throws SitePublishException
	{
		
		super.runPage();
		
//		JspFile jspFile = script.getJspFile();
//		
////		CMSURL cmsUrl = requestContext.getRequestedCmsURL();
//		//提供action的特殊处理
////		CMSRequestContext jspFileRequestContext = new CMSRequestContext(requestContext,jspFile);
//		String uri = jspFile.getUri();
//		JspletWindow jspWindow = new JspletWindowImpl(context,jspFile);
//		RequestDispatcher dispatcher = requestContext.getRequest().getRequestDispatcher(uri);
//		CMSServletRequest request = new CMSServletRequest(requestContext.getRequest(),requestContext.getPageContext(),jspWindow,this.context);
//		CMSServletResponse response = new CMSServletResponse(requestContext.getResponse(),this.context);
//		
//		
//        try {
//			dispatcher.forward(request, response);
//			StringBuffer out = response.getInternalBuffer().getBuffer(); 
//			out = new StringBuffer(this.parser(out));
//			
//			jspFile.setContent(out);
//			
//			Distribute distribute = new DistributeWraper();
//			
//			distribute.init(context,jspFile);
//			distribute.distribute();
//			
//		} catch (ServletException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (DistributeException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
        
	}
	

	

	/**
	 * 执行发布过程
	 * @throws SitePublishException 
	 *
	 */
	public void doPublish() throws SitePublishException
	{
		if(needPublishChannel())
		{
			publishChannels();
		}
		
//		if(this.publishScope == PUBLISH_PAGE)
//			publishPages();
		if(needPublishSiteIndex())
		{
			publishSiteIndex();
		}
		
		//System.out.println();
		

	}
	
	/**
	 * 发布任务的唯一标识
	 */
	public String getId() {
		if(context == null)
			this.buildContext();
		if(context.isRecursive())
			return new StringBuffer("site." ).append(this.context.getSiteID()).append(".").append(context.isRecursive()).toString();
		else
			return new StringBuffer("site." ).append(this.context.getSiteID()).toString();
//		if(isRoot())
//			return "site." + this.context.getSiteID();
//		return this.parentContext.getID() + ".site." + this.context.getSiteID();
	}


	

	public void recordRecursivePubObj(String refobj, int reftype, String site) {
		super.recordRecursivePublihsObject(this.context.getSite().getSiteId() + "",RecursivePublishManager.PUBOBJECTTYPE_SITE,
											context.getSite().getSecondName(),refobj,reftype,site);
		
	}

	public Set getRecursivePubObject() {
		try {
			Set publishObjects = CMSUtil.getCMSDriverConfiguration()
			 .getCMSService()
			 .getRecursivePublishManager()
			 .getAllPubObjectsOfSite(context,this.context.getSiteID());
			return publishObjects;
		} catch (RecursivePublishException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DriverConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	protected void delteRefObjects() {
		try {
			CMSUtil.getCMSDriverConfiguration()
			 .getCMSService()
			 .getRecursivePublishManager().deleteRefObjectsOfPubobject(context,context.getSiteID(),
					 									RecursivePublishManager.PUBOBJECTTYPE_SITE);
		} catch (RecursivePublishException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DriverConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
