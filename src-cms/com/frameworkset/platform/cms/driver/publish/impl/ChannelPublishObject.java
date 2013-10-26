package com.frameworkset.platform.cms.driver.publish.impl;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.servlet.RequestDispatcher;

import org.apache.log4j.Logger;

import com.frameworkset.platform.cms.channelmanager.Channel;
import com.frameworkset.platform.cms.channelmanager.ChannelManagerException;
import com.frameworkset.platform.cms.container.Template;
import com.frameworkset.platform.cms.documentmanager.Document;
import com.frameworkset.platform.cms.documentmanager.DocumentManagerException;
import com.frameworkset.platform.cms.driver.config.DriverConfigurationException;
import com.frameworkset.platform.cms.driver.context.ChannelContext;
import com.frameworkset.platform.cms.driver.context.ContentContext;
import com.frameworkset.platform.cms.driver.context.Context;
import com.frameworkset.platform.cms.driver.context.impl.CMSContextImpl;
import com.frameworkset.platform.cms.driver.context.impl.ChannelContextImpl;
import com.frameworkset.platform.cms.driver.jsp.CMSException;
import com.frameworkset.platform.cms.driver.jsp.CMSRequestContext;
import com.frameworkset.platform.cms.driver.jsp.CMSRequestDispatcher;
import com.frameworkset.platform.cms.driver.jsp.CMSRequestDispatcherImpl;
import com.frameworkset.platform.cms.driver.jsp.CMSServletRequest;
import com.frameworkset.platform.cms.driver.jsp.CMSServletRequestImpl;
import com.frameworkset.platform.cms.driver.jsp.CMSServletResponse;
import com.frameworkset.platform.cms.driver.jsp.CMSServletResponseImpl;
import com.frameworkset.platform.cms.driver.jsp.JspFile;
import com.frameworkset.platform.cms.driver.jsp.JspletWindow;
import com.frameworkset.platform.cms.driver.jsp.JspletWindowImpl;
import com.frameworkset.platform.cms.driver.publish.PublishException;
import com.frameworkset.platform.cms.driver.publish.PublishMode;
import com.frameworkset.platform.cms.driver.publish.PublishObject;
import com.frameworkset.platform.cms.driver.publish.RecursivePublishException;
import com.frameworkset.platform.cms.driver.publish.RecursivePublishManager;
import com.frameworkset.platform.cms.util.AttributeKeys;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.platform.cms.util.StringUtil;

/**
 * 
 * <p>Title: ChannelPublishObject</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 *
 * @author biaoping.yin
 * @version 1.0 
 */
public class ChannelPublishObject extends PublishObject implements java.io.Serializable {
	private static final Logger log = Logger.getLogger(ChannelPublishObject.class);
	
	ChannelContext channelContext ; 

	public ChannelPublishObject(CMSRequestContext requestContext, 
								int[] publishScope,
								String[] params,
								String[] publisher,
								boolean isRoot,
								boolean[] local2ndRemote,
								int[] distributeManners ) {
		super(requestContext, publishScope,params, local2ndRemote 	,
				   distributeManners ,isRoot,publisher);
//		this.publisher = publisher;
//		this.isRoot = isRoot;
		
	}

	public ChannelPublishObject(CMSRequestContext requestcontext, String[] params, Context context,int[] publishScope) {
		super(requestcontext,params,context);
		this.publishScope = publishScope;
		
	}
	
	public ChannelPublishObject(CMSRequestContext requestcontext, String[] params, Context context) {
		super(requestcontext,params,context);
		
		
	}

	public Context buildContext() {
		return buildChannelContext();
	}
	
	/**
	 * 构建当前的频道上下文
	 * @return
	 */
	
	private ChannelContext buildChannelContext()
	{
		if(this.params == null)
		{
			String siteid = this.requestContext.getRequest().getParameter(AttributeKeys.CMSSITEREQUESTKEY);
			String channelid = this.requestContext.getRequest().getParameter(AttributeKeys.CMSCHANNELREQUESTKEY);
			if(this.parentContext == null)
			{
				/**
				 * 构建频道的父上下文
				 */
				this.parentContext = new CMSContextImpl(siteid,publisher,this,null);
				parentContext.setEnableRecursive(this.enableRecursive());
				parentContext.setClearFileCache(this.isClearFileCache());
				channelContext = new ChannelContextImpl(siteid,channelid,parentContext,this,monitor);
				channelContext.setMaxPublishDepth(this.getMaxPublishDepth());
				channelContext.setCurrentPublishDepth(1);
				
			}
			else
			{
				channelContext =  new ChannelContextImpl(siteid,channelid,this.parentContext,this,parentContext.getPublishMonitor().createSubPublishMonitor());
				channelContext.setMaxPublishDepth(parentContext.getMaxPublishDepth());
				channelContext.setCurrentPublishDepth(parentContext.getCurrentPublishDepth() + 1);
			}
		}
		else
		{
			/**
			 * 如果是发布任务的起点，parentContext为null，需要构建内容管理系统的上下文
			 */
			if(this.parentContext == null)
			{
				parentContext = new CMSContextImpl(this.params[0],publisher,this,null);
				parentContext.setEnableRecursive(this.enableRecursive());
				parentContext.setClearFileCache(this.isClearFileCache());
				
				channelContext =  new ChannelContextImpl(this.params[0],this.params[1],parentContext,this,monitor);
				
				/**
				 * 设置发布的深度
				 */
				channelContext.setMaxPublishDepth(this.getMaxPublishDepth());
				channelContext.setCurrentPublishDepth(1);
			}
			else
			{
				channelContext =  new  ChannelContextImpl(this.params[0],this.params[1],
						this.parentContext,
						this,parentContext.getPublishMonitor().createSubPublishMonitor());
				channelContext.setMaxPublishDepth(parentContext.getMaxPublishDepth());
				channelContext.setCurrentPublishDepth(parentContext.getCurrentPublishDepth() + 1);
			}
		}
		
		return channelContext ;
	}
	
	/**
	 * 初始化模版脚本对应于频道的概览模版
	 */
	public void initScriptlet() throws PublishException {
		ChannelContext channelContext = (ChannelContext)this.context;
		if( channelContext.isCustomPageType() 
				|| channelContext.isRefchannelType() 
				|| channelContext.isDocDetailPageType())
			return ;
		Template indexTemplate = channelContext.getOutlineTemplate();
		if(indexTemplate == null )
			return;
		this.script = ScriptletUtil.createScriptlet(context,indexTemplate);
		
		
	}
	
	/**
	 * 发布子频道
	 * @throws ChannelPublishException 
	 * 
	 */
	public void publishSubChannels() throws ChannelPublishException
	{
//		ChannelContext channelContext = (ChannelContext)this.context;
		try {
			List subChannels = this.requestContext.getDriverConfiguration().getCMSService().getChannelManager().getDirectSubChannels(channelContext.getChannelID() + "");
			for(int i = 0; subChannels != null && i < subChannels.size(); i ++)
			{
				Channel channel = (Channel) subChannels.get(i);
				PublishObject channelPublishObject = new ChannelPublishObject(this.requestContext,
																			  new String[]{channelContext.getSiteID(),
																			  channel.getChannelId() + ""},context);
				context.getDriverConfiguration()
					   .getPublishEngine()
					   .publish(channelPublishObject);
			}
		} catch (ChannelManagerException e) {
			throw new ChannelPublishException(e.getMessage());
		} catch (DriverConfigurationException e) {
			throw new ChannelPublishException(e.getMessage());
		} catch (PublishException e) {
			throw new ChannelPublishException(e.getMessage());
		}
	}
	
	/**
	 * 发布频道的所有内容
	 * @throws PublishException
	 */
	public void publishContents() throws PublishException
	{
//		ChannelContext channelContext = (ChannelContext)this.context;
		
		if(this.channelContext.getDocumentPublishMode() != PublishMode.MODE_NO_ACTION)
		{

			List unpublishDocuments = null;
			
			
			try {
				/**
				 * 如果执行的是递归发布，则只需要发布已经发布的文档，并且这些已经发布的文档的细览模板和频道的概览模板要保持一致才执行
				 * 
				 */
				if(!this.context.isRecursive())
				{
				
					/*
					 * 增量发布则获取所有待发布的文档
					 * 全部发布则发布所以待发布和已经发布过的文档
					 */
					if(this.isIncreament())	
					{
						unpublishDocuments = this.requestContext.getDriverConfiguration()
																.getCMSService()
																.getChannelManager()
																.getIncCanPubDocsOfChnl(channelContext.getChannelID());
					}
					else
					{
						unpublishDocuments = this.requestContext.getDriverConfiguration()
																.getCMSService()
																.getChannelManager()
																.getAllCanPubDocsOfChnl(channelContext.getChannelID());
					}
				}
				else 
				{
					if(channelContext.getParentContext() instanceof ContentContext)
					{
						//发布单条文档时发布文档递归的的频道下的文档
						unpublishDocuments = this.requestContext.getDriverConfiguration()
											.getCMSService()
											.getChannelManager().getPubDocWithSameTplOfChannel(channelContext.getChannelID(),
													new String[] {((ContentContext)channelContext.getParentContext()).getContentid()});
					}
					else
					{
//						发布单条文档时发布文档递归的的频道下的文档
						unpublishDocuments = this.requestContext.getDriverConfiguration()
											.getCMSService()
											.getChannelManager().getPubDocWithSameTplOfChannel(channelContext.getChannelID(),
													null);
					}
				}
				
				/**
				 * 发布频道包含的所有未发布文档
				 */
				for(int i = 0; unpublishDocuments != null && i < unpublishDocuments.size(); i ++)
				{
					
					Document document = (Document)unpublishDocuments.get(i);
	//				if(document.getDoctype()  1 )//外部链接无需发布
	//					continue;
					publishDocument(document);
				}
				
				
			} catch (DriverConfigurationException e) {
				
				throw new ChannelPublishException(e.getMessage());
			} catch (ChannelManagerException e) {
				throw new ChannelPublishException(e.getMessage());
			}

			
		}
		else
		{
			try {
				CMSUtil.getCMSDriverConfiguration().getCMSService().getChannelManager().publishUnpublishDocs(this.channelContext.getChannel().getDisplayName());
			} catch (ChannelManagerException e) {
				this.channelContext.getPublishMonitor().addFailedMessage("发布频道文档时，改变文档状态失败：" + e.getMessage(),context.getPublisher());
				e.printStackTrace();
			} catch (DriverConfigurationException e) {
				this.channelContext.getPublishMonitor().addFailedMessage("发布频道文档时，改变文档状态失败：" + e.getMessage(),context.getPublisher());
				e.printStackTrace();
			}
		}
		
	}
	
//	private void publishDocumentAttachment(Document document) {
//		PublishObject contentPublishObject = new ContentPublishObject(this.requestContext,
//				  new String[] {channelContext.getSiteID(),
//								document.getChanel_id() + "",
//				  				document.getDocument_id() + ""},
//				  this.context);
//		try {
//			this.requestContext.getDriverConfiguration()
//			.getPublishEngine()
//			.publish(contentPublishObject);
//		} catch (PublishException e) {
//			
//		}
//		
//	}

	/**
	 * 发布单片文档,待优化
	 * @param document
	 * @throws PublishException 
	 */
	private void publishDocument(Document document) 
	{
//		ChannelContext channelContext = (ChannelContext)this.context;		
		PublishObject contentPublishObject = new ContentPublishObject(this.requestContext,
																	  new String[] {channelContext.getSiteID(),
																					document.getChanel_id() + "",
																	  				document.getDocument_id() + ""},
																	  this.context);
		try {
			this.requestContext.getDriverConfiguration()
							   .getPublishEngine()
							   .publish(contentPublishObject);
		} catch (PublishException e) {
			
		}
		
	}
	
	/**
	 * 发布频道的概览模版
	 * @throws PublishException
	 */
	private void publishOutlineTemplate() throws PublishException 
	{
		/*
		 * 判断是否需要发布概览模版，如果频道没有设置概览模版则跳过
		 */
//		ChannelContext channelContext = (ChannelContext)this.context;
		if(channelContext.haveOutlineTemplate())
		{
			/**
			 * 先发布一页，看模板中是否包含分页标签，如果包含则根据总页数的多收
			 * 需要发布后续的页面。
			 */
			runPage();
			
			/**
			 * 执行后续页面的发布功能
			 */
			if(channelContext.isPagintion())
			{
				//自动翻到下一页
				channelContext.next();
				for(int i = 1; i < channelContext.getTotalPages(); i ++)
				{
					runPage();
					//自动翻到下一页
					channelContext.next();
				}
			}
		}
	}
	
	
	/**
	 * 运行发布过程当中产生的临时jsp页面
	 * 并且生成最终发布页面
	 * 过载父类中提供的方法
	 * @throws PublishException
	 */
	protected void runPage()  {
		
//		if(!(context instanceof PageContext))
		{
//			if(!needAction())
//				return;
			JspFile jspFile = script.getJspFile();
			
			if(!this.context.getPublishMonitor().isScriptInited())
				return ;
			String uri = jspFile.getUri();
			//uri = "asdbce";
//			System.out.println("channel 开始:" + uri);
			JspletWindow jspWindow = new JspletWindowImpl(context, jspFile);
			RequestDispatcher dispatcher = requestContext.getRequest()
					.getRequestDispatcher(uri);
			CMSRequestDispatcher cmsDispatcher = new CMSRequestDispatcherImpl(dispatcher);
			CMSServletRequest request = new CMSServletRequestImpl(requestContext
					.getRequest(), requestContext.getPageContext(),jspWindow,this.context);
			CMSServletResponse response = new CMSServletResponseImpl(requestContext
					.getResponse(),context);
	
			try {
				System.err.println("channel 开始:" + uri +":" + this.getId());
				cmsDispatcher.include(request, response);
				StringBuffer out = response.getInternalBuffer().getBuffer();
			//	System.out.println("gengercontent:" + out);
//				long time = System.currentTimeMillis();
//				out = new StringBuffer(this.parser(out));
//				long end = System.currentTimeMillis();
//				System.out.println("custom time:" + (end - time));
				
				script.setContent(out);
				this.saveHtml();
				if(!this.channelContext.isPagintion())
					this.context.getPublishMonitor().setPublishStatus(PublishMonitor.PAGE_GENERATED);
				else
				{
					if(this.channelContext.isPublishAllPage())
						this.context.getPublishMonitor().setPublishStatus(PublishMonitor.PAGE_GENERATED);
				}
				//this.context.getPublishMonitor().setPublishStatus(PublishMonitor.PAGE_GENERATED);
				this.context.getPublishMonitor().addSuccessMessage(new StringBuffer(context.toString()).append("生成页面[")
						.append(context.getRendURI())
						.append("]成功").toString(),context.getPublisher());
				System.err.println("channel 完成:" + uri+":" + this.getId());
//				System.out.println()
			}
	
			catch (IOException e) {
				log.debug(this.getClass().getName() + "发布报错：[" + uri +"]" + e.getMessage(),e);
				System.out.println(this.getClass().getName() + "发布报错：[" + uri +"]" + e.getMessage());
				System.err.println("common publishobject 结束0:" + uri + ":" + this.getId());
				e.printStackTrace();
				System.err.println("common publishobject 结束1:" + uri + ":" + this.getId());
				context.getPublishMonitor().setPublishStatus(PublishMonitor.PUBLISH_FAILED);
				this.context.getPublishMonitor().addFailedMessage(new StringBuffer(context.toString()).append("生成页面[")
						.append(context.getRendURI())
						.append("]失败:").append(e.getMessage()).toString(),context.getPublisher());
			} catch (CMSException e) {
				
				System.err.println("common publishobject 结束0:" + uri + ":" + this.getId());
				e.printStackTrace();
				System.err.println("common publishobject 结束1:" + uri + ":" + this.getId());
				context.getPublishMonitor().setPublishStatus(PublishMonitor.PUBLISH_FAILED);
				this.context.getPublishMonitor().addFailedMessage(new StringBuffer(context.toString()).append("生成页面[")
						.append(context.getRendURI())
						.append("]失败:").append(e.getMessage()).toString(),context.getPublisher());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.err.println("common publishobject 结束0:" + uri + ":" + this.getId());
				e.printStackTrace();
				System.err.println("common publishobject 结束1:" + uri + ":" + this.getId());
				context.getPublishMonitor().setPublishStatus(PublishMonitor.PUBLISH_FAILED);
				this.context.getPublishMonitor().addFailedMessage(new StringBuffer(context.toString()).append("生成页面[")
						.append(context.getRendURI())
						.append("]失败:").append(e.getMessage()).toString(),context.getPublisher());
			}
		}
		
	}
	
	
	
	public boolean needPublishContent()
	{
		
		for(int i = 0;  i < this.publishScope.length; i ++)
		{
			if(publishScope[i] == PublishObject.PUBLISH_CHANNEL_CONTENT
					|| publishScope[i] == PublishObject.PUBLISH_CHANNEL_RECCONTENT 
					|| publishScope[i] == PublishObject.PUBLISH_CHANNEL_CONTENT_PUBLISHED)
				return true;
			
		}
		return false;
	}
	
	public boolean needPublishSubChannel()
	{
		for(int i = 0;  i < this.publishScope.length; i ++)
		{
			if(publishScope[i] == PublishObject.PUBLISH_CHANNEL_RECINDEX
					|| publishScope[i] == PublishObject.PUBLISH_CHANNEL_RECCONTENT)
				return true;
			
		}
		return false;
	}
	
	public boolean needPublishChannelIndex()
	{
		if(this.channelContext.getPublishMode() != PublishMode.MODE_NO_ACTION)
		{
			for(int i = 0;  i < this.publishScope.length; i ++)
			{
				if(publishScope[i] == PublishObject.PUBLISH_CHANNEL_RECINDEX
						|| publishScope[i] == PublishObject.PUBLISH_CHANNEL_INDEX)
				{
					return true;
					
				}
				
			}
		}
		return false;
	}
	
	
	
	/**
	 * 执行频道的发布处理动作，发布的内容包括
	 * 频道首页
	 * 频道文档
	 * 子频道首页
	 * 子频道文档 
	 */
	public void doPublish() throws PublishException {
		boolean flag = false;
		if(needPublishContent())
		{
			publishContents();
			flag = true;
		}
		
		if(needPublishSubChannel())
		{
			publishSubChannels();
		}
		  
		if(needPublishChannelIndex())
		{
			ChannelContext channelContext = (ChannelContext)this.context;
			/*
			 * 频道首页发布,根据前台的设置，可分为3种情况：
			 * 1.频道概览模版生成首页
			 * 2.指定自定义的页面生成首页
			 * 3.指定文档的细览页面作为频道的首页
			 */
			if(channelContext.isTemplateType())   
			{
				publishOutlineTemplate();
			}
			else if(channelContext.isCustomPageType())
			{
				if(!CMSUtil.isOtherDomain(channelContext.getChannel().getIndexpagepath(), requestContext.getRequest()))
				{
	//				CMSUtil.getPublishedLinkPath(this.context,channelContext.getChannel().getIndexpagepath());
					PagePublishObject pagePublishObject = new PagePublishObject(this.requestContext,new String[] {channelContext.getChannel().getIndexpagepath(),
							  CMSUtil.getPageType(channelContext.getChannel().getIndexpagepath()) + ""},
							  this.context);
					this.context.getDriverConfiguration().getPublishEngine().publish(pagePublishObject);
				}
			}
			else if(channelContext.isDocDetailPageType())
			{
				try {
					/*
					 * 判断是否需要发布对应的文档，因为指定的文档已经发布后就不需要再发布对应的文档，否则需要
					 */
					if(!flag && CMSUtil.getCMSDriverConfiguration()
							.getCMSService()
							.getChannelManager().canPublishDocument(channelContext.getChannel().getIndexpagepath(),this.isIncreament()))
					{
						
						Document doc = CMSUtil.getCMSDriverConfiguration()
												.getCMSService()
												.getDocumentManager()
												.getDoc(channelContext.getChannel().getIndexpagepath());
						
						this.publishDocument(doc);
						
					}
				} catch (DocumentManagerException e) {
					context.getPublishMonitor().addFailedMessage("文档[" + channelContext.getChannel().getIndexpagepath() + "]不存在",new java.util.Date(),channelContext.getPublisher());
					this.context.getPublishMonitor().setPublishStatus(PublishMonitor.PUBLISH_FAILED);
//					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (DriverConfigurationException e) {
//					// TODO Auto-generated catch block
					context.getPublishMonitor().addFailedMessage(e.getMessage(),new java.util.Date(),channelContext.getPublisher());
					this.context.getPublishMonitor().setPublishStatus(PublishMonitor.PUBLISH_FAILED);
					e.printStackTrace();
				}
			}
			else if(channelContext.isRefchannelType()) //通过其他频道首页生成当前的页面:站点id：频道id
			{
//				String channelinfo = channelContext.getChannel().getIndexpagepath();
////				String[] infos = channelinfo.split(":");				
			}
			if(!StringUtil.isEmpty(this.channelContext.getChannel().getOutlinepicture()))
			{
				CMSUtil.addPublishLink(channelContext, "", this.channelContext.getChannel().getOutlinepicture());
			}
		}
		

	}
	
	 
	
	
	
	
	/**
	 * 发布对象的标识
	 */
	public String getId() {
//		if(this.isRoot())
//			return "channel." + ((ChannelContext)this.context).getChannelID();
//		
//		return   this.parentContext.getID() + ".channel." + ((ChannelContext)this.context).getChannelID();
		
		if(this.context.isRecursive())
			return new StringBuffer("channel.").
					append(((ChannelContext)this.context).getChannelID())
					.append(".")
					.append(this.context.isRecursive())
					.append(".").append(this.changeScopeToUIID())
					.toString() ;
		else
			return new StringBuffer("channel.")
						.append(((ChannelContext)this.context).getChannelID()).append(".").append(this.changeScopeToUIID())
						.toString() ;
	}

	public Set getRecursivePubObject() {
		try {
			Set publishObjects = CMSUtil.getCMSDriverConfiguration()
										.getCMSService()
										.getRecursivePublishManager()
										.getAllPubObjectsOfChannel(context,this.channelContext.getChannelID());
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

	public void recordRecursivePubObj(String refobj, int reftype, String site) {		
		super.recordRecursivePublihsObject(this.channelContext.getChannelID(),
											RecursivePublishManager.PUBOBJECTTYPE_CHANNEL,
											context.getSite().getSecondName(),
											refobj,reftype,site
											);
		
	}

	protected void delteRefObjects() {
		try {
			CMSUtil.getCMSDriverConfiguration()
			.getCMSService()
			.getRecursivePublishManager().deleteRefObjectsOfPubobject(context,this.channelContext.getChannelID(),RecursivePublishManager.PUBOBJECTTYPE_CHANNEL);
		} catch (RecursivePublishException e) {
			
			e.printStackTrace();
		} catch (DriverConfigurationException e) {
			
			e.printStackTrace();
		}catch (Exception e) {
			
			e.printStackTrace();
		}
	}

//	/**已经的发布文档的方法中处理了
//	 * 如果当前发布的文档和属于引用对象ref.getPubobject()对应的频道的文档，
//	 * 在联动发布本频道下的文档时，需要排除文档自身被重复发布
//	 */
//	public void setExcluedDocument(String excluedDocument) {
//		this.excluedDocument = excluedDocument;
//		
//	}
//	/**
//	 * 如果当前发布的文档和属于引用对象ref.getPubobject()对应的频道的文档，
//	 * 在联动发布本频道下的文档时，需要排除文档自身被重复发布
//	 */
//	private String excluedDocument;
//	
	

}
