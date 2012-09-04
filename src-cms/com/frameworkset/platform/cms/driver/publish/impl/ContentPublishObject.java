 package com.frameworkset.platform.cms.driver.publish.impl;

import java.io.IOException;
import java.util.Date;
import java.util.Set;

import javax.servlet.RequestDispatcher;

import org.apache.log4j.Logger;
import org.htmlparser.util.ParserException;

import com.frameworkset.platform.cms.container.Template;
import com.frameworkset.platform.cms.documentmanager.Document;
import com.frameworkset.platform.cms.driver.config.DocumentStatus;
import com.frameworkset.platform.cms.driver.config.DriverConfigurationException;
import com.frameworkset.platform.cms.driver.context.ContentContext;
import com.frameworkset.platform.cms.driver.context.Context;
import com.frameworkset.platform.cms.driver.context.impl.CMSContextImpl;
import com.frameworkset.platform.cms.driver.context.impl.ContentContextImpl;
import com.frameworkset.platform.cms.driver.distribute.IndexObject;
import com.frameworkset.platform.cms.driver.htmlconverter.CmsLinkProcessor;
import com.frameworkset.platform.cms.driver.jsp.CMSException;
import com.frameworkset.platform.cms.driver.jsp.CMSRequestContext;
import com.frameworkset.platform.cms.driver.jsp.CMSRequestDispatcher;
import com.frameworkset.platform.cms.driver.jsp.CMSRequestDispatcherImpl;
import com.frameworkset.platform.cms.driver.jsp.CMSServletRequest;
import com.frameworkset.platform.cms.driver.jsp.CMSServletResponse;
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
import com.frameworkset.util.StringUtil;

/**
 * 
 * <p>
 * Title: com.frameworkset.platform.cms.driver.publish.impl.ContentPublishObject.java
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright (c) 2007
 * </p>
 * 
 * <p>
 * Company: iSany
 * </p>
 * 
 * @Date 2007-1-26
 * @author biaoping.yin
 * @version 1.0 
 */
public class ContentPublishObject extends PublishObject {
	
	private static final Logger log = Logger.getLogger(ContentPublishObject.class);
	/**
	 * 预览文档内容时的
	 */
	private Document viewDocument;
	ContentContext contentContext = null;
	

	public ContentPublishObject(CMSRequestContext requestContext,
			int[] publishScope, String[] params, String[] publisher,
			boolean isRoot,
			boolean[] local2ndRemote,
			int[] distributeManners) {
		super(requestContext, publishScope, params,
				local2ndRemote,
				 distributeManners,isRoot,publisher);
//		this.publisher = publisher;
//		this.isRoot = isRoot;
	}
	
	public ContentPublishObject(CMSRequestContext requestContext,
			int[] publishScope, String[] params, String[] publisher,
			boolean isRoot,
			boolean[] local2ndRemote,
			int[] distributeManners,Document viewDocument) {
		super(requestContext, publishScope, params,
				local2ndRemote,
				 distributeManners,isRoot,publisher);
		this.viewDocument = viewDocument;
//		this.publisher = publisher;
//		this.isRoot = isRoot;
	}

	public ContentPublishObject(CMSRequestContext requestContext,
								String[] params, 
								Context parentContext) {
		super(requestContext,  params,parentContext);
		
//		this.parentContext = parentContext;
//		this.publisher = parentContext.getPublisher();
//		this.isRoot = false;

	}

	public Context buildContext() {
		return buildContentContext();
	}

	/**
	 * 根据具体情况，还需要做进一步的扩展
	 * 
	 * @return
	 */
	public ContentContext buildContentContext() {
		if (this.params == null) {
			String siteid = this.requestContext.getRequest().getParameter(
					AttributeKeys.CMSSITEREQUESTKEY);
			String channelid = this.requestContext.getRequest().getParameter(
					AttributeKeys.CMSCHANNELREQUESTKEY);
			String contentid = this.requestContext.getRequest().getParameter(
					AttributeKeys.CMSSITEREQUESTKEY);
			if (this.parentContext == null)
			{
				this.parentContext = new CMSContextImpl(siteid,
						
						publisher, this,null);
				parentContext.setEnableRecursive(this.enableRecursive());
				parentContext.setClearFileCache(this.isClearFileCache());
			}
			if(this.viewDocument == null)
				return contentContext = new ContentContextImpl(contentid, channelid, siteid,parentContext,
					this, monitor);
			else
				return contentContext = new ContentContextImpl(viewDocument, channelid, siteid,parentContext,
						this, monitor,true);
		} else {
			if (this.parentContext == null)
			{
				
				parentContext = new CMSContextImpl(params[0],
						publisher, this,null);
				parentContext.setEnableRecursive(this.enableRecursive());
				parentContext.setClearFileCache(this.isClearFileCache());
				if(params.length == 3)
				{
					if(this.viewDocument == null)
						return contentContext = new ContentContextImpl(params[2], params[1], params[0],parentContext,
													  this, monitor);
					else
						return contentContext = new ContentContextImpl(viewDocument, params[1], params[0],parentContext,
								this, monitor,true);
				}
				else
				{
					if(this.viewDocument == null)
						return contentContext = new ContentContextImpl(params[1], parentContext,
													  this, monitor,params[0]);
					else
						return contentContext = new ContentContextImpl(viewDocument, viewDocument.getChanel_id()+"",params[0],parentContext,
								this, monitor,true);
				}
			}
			else
			{
				if(params.length == 3)
				{
					if(this.viewDocument == null)
						return contentContext = new ContentContextImpl(params[2], params[1],params[0], parentContext,
								  this, parentContext.getPublishMonitor().createSubPublishMonitor());
					else
						return contentContext = new ContentContextImpl(viewDocument, params[1], params[0],parentContext,
								this, monitor,true);
				}
				else
				{
					if(this.viewDocument == null)
						return contentContext = new ContentContextImpl(params[1], parentContext,
								  this, parentContext.getPublishMonitor().createSubPublishMonitor(),params[0]);
					else
						return contentContext = new ContentContextImpl(viewDocument, viewDocument.getChanel_id() + "",params[0], parentContext,
								this, monitor,true);
				}
			}
			
			
		}

	}

	/**
	 * 初始化文档细览脚本
	 */
	public void initScriptlet() throws PublishException {
		
//		ContentContext contentContext = (ContentContext) this.context;
		if(contentContext.isAggregation())
		{
			this.context.getPublishMonitor().setPublishStatus(PublishMonitor.SCRIPT_INITFAILED);
			context.getPublishMonitor().addFailedMessage("文档[" + contentContext + "]为聚合文档，发布任务忽略。",new Date(),context.getPublisher());
			return;
		}
		Template detailTemplate = contentContext.getDetailTemplate();
		this.script = ScriptletUtil.createScriptlet(context,detailTemplate);
	}

	/**
	 * 发布文档细览页面
	 * 
	 * @throws PublishException
	 */
	private void publishDetailPage() throws PublishException {
		if(this.contentContext.getPublishMode() == PublishMode.MODE_NO_ACTION)
		{
			contentContext.getPublishMonitor().setPublishStatus(PublishMonitor.DISTRIBUTE_COMPLETED);
			return;
		}
		/**
		 * 如果系统只发布文档附件，无需发布文档的细览页面，则无需生成细览页面
		 */
		if(this.contentContext.getPublishMode() != PublishMode.MODE_ONLY_ATTACHMENT)
		{
			/**
			 * 判断发布模式是否为动态发布，对于动态发布无需生成文档的静态页面，只需将模板转换为动态jsp页面发布
			 * 到相应的频道目录下即可,这样文档的概览列表将会以传递当前文档的id，文档频道的id，文档对应的站点
			 */
			if(this.contentContext.getPublishMode() == PublishMode.MODE_DYNAMIC_PROTECTED 
					|| this.contentContext.getPublishMode() == PublishMode.MODE_DYNAMIC_UNPROTECTED)
			{
				if(contentContext.hasDetailTemplate()) 
				{

						if(context.getPublishMonitor()
							   .containTempFileOfPublishObject(contentContext.getDetailTemplate().getTemplateId() + "",
									   							"channeldetail:" + contentContext.getChannelid(),false))
						{
							prehandleContent(contentContext);
							contentContext.getPublishMonitor().setPublishStatus(PublishMonitor.PAGE_GENERATED);
							return;
						}
						else
						{
							prehandleContent(contentContext);
							generateDynamicPage();
						}
				}
				
			}
			else
			{
//				预处理待发布的文档,如果是分页文档,要进行特殊的处理
				prehandleContent(contentContext);
				if(!this.contentContext.isPagintion())
				{
					super.runPage();
				}
				else
				{
					for(int i = 0; i < contentContext.getTotalPages(); i ++)
					{				
						runPage();	
						contentContext.next();
					}
				}
			}
		}
		else
		{
			this.prehandleContent(contentContext);
			contentContext.getPublishMonitor().setPublishStatus(PublishMonitor.PAGE_GENERATED);
			
		}
	}
	
	protected void generateDynamicPage()
	{
		script.getJspFile();
		if(!this.context.getPublishMonitor().isScriptInited())
			return ;
		this.context.getPublishMonitor().setPublishStatus(PublishMonitor.PAGE_GENERATED);
	}
	
	
	protected void runPage()  {
		JspFile jspFile = script.getJspFile();
		if(!this.context.getPublishMonitor().isScriptInited())
			return ;
		String uri = jspFile.getUri();
		JspletWindow jspWindow = new JspletWindowImpl(context, jspFile);
		RequestDispatcher dispatcher = requestContext.getRequest()
				.getRequestDispatcher(uri);
		CMSRequestDispatcher cmsDispatcher = new CMSRequestDispatcherImpl(dispatcher);
		CMSServletRequest request = new CMSServletRequest(requestContext
				.getRequest(), requestContext.getPageContext(),jspWindow,this.context);
		CMSServletResponse response = new CMSServletResponse(requestContext
				.getResponse(),context);

		try {
			cmsDispatcher.include(request, response);
			StringBuffer out = response.getInternalBuffer().getBuffer();
			out = new StringBuffer(this.parser(out));
			script.setContent(out);			
			String[] path_info = this.saveHtml();
			
			
			/**
			 * 记录生成的
			 */
			if(CMSUtil.enableIndex())
			{
				IndexObject indexObject = new IndexObject(this.contentContext.getSiteID() + "||" + contentContext.getChannelid() + "||" +  this.contentContext.getContentid(),
														  IndexObject.DOCUMENT,
														  path_info,contentContext.getDocument(),contentContext.getContentOrigineTemplateLinkTable());
				context.addIndexObject(indexObject);
			}
			
			if(!this.contentContext.isPagintion())
				this.context.getPublishMonitor().setPublishStatus(PublishMonitor.PAGE_GENERATED);
			else
			{
				if(this.contentContext.isPublishAllPage())
					this.context.getPublishMonitor().setPublishStatus(PublishMonitor.PAGE_GENERATED);
			}
			this.context.getPublishMonitor().addSuccessMessage(new StringBuffer(context.toString()).append("生成页面[")
					.append(context.getRendURI())
					.append("]成功").toString(),context.getPublisher());
			
		}

		catch (IOException e) {
			log.debug(this.getClass().getName() + "发布报错：[" + uri +"]" + e.getMessage(),e);
			System.out.println(this.getClass().getName() + "发布报错：[" + uri +"]" + e.getMessage());
			e.printStackTrace();
			context.getPublishMonitor().setPublishStatus(PublishMonitor.PUBLISH_FAILED);
			this.context.getPublishMonitor().addFailedMessage(new StringBuffer(context.toString()).append("生成页面[")
					.append(context.getRendURI())
					.append("]失败:").append(e.getMessage()).toString(),context.getPublisher());
		} catch (CMSException e) {
			
			e.printStackTrace();
			context.getPublishMonitor().setPublishStatus(PublishMonitor.PUBLISH_FAILED);
			this.context.getPublishMonitor().addFailedMessage(new StringBuffer(context.toString()).append("生成页面[")
					.append(jspFile.getUri())
					.append("]失败:").append(e.getMessage()).toString(),context.getPublisher());
		} catch (Exception e) {
			
			e.printStackTrace();
			context.getPublishMonitor().setPublishStatus(PublishMonitor.PUBLISH_FAILED);
			this.context.getPublishMonitor().addFailedMessage(new StringBuffer(context.toString()).append("生成页面[")
					.append(context.getRendURI())
					.append("]失败:").append(e.getMessage()).toString(),context.getPublisher());
		}
	}
	
	
	
	

	public void doPublish() throws PublishException {
		/*
		 * 聚合文档不需要发布
		 * 外部链接不需要发布
		 */
//		ContentContext contentContext = (ContentContext) this.context;
		if(contentContext.isAggregation() 
				|| contentContext.getDocument().getDoctype() == Document.DOCUMENT_OUTLINK)
		{
			contentContext.getPublishMonitor().setPublishStatus(PublishMonitor.DISTRIBUTE_COMPLETED);
			executeRecusive();
			return;
		}
		
		this.publishDetailPage();		
		executeRecusive();
	}
	/**
	 * 执行文档发布动作时，如果允许递归发布并且当前的发布任务不是由递归发布任务引起的
	 * 则文档发布后，需要对所有引用了本文档的相关站点首页、频道首页进行递归发布,在执行递归发布的时候要
	 * 将当前文档排出掉
	 */
	private void executeRecusive()
	{
//		执行递归发布
		try
		{
			/**
			 * 执行文档发布动作时，如果允许递归发布并且当前的发布任务不是由递归发布任务引起的
			 * 则文档发布后，需要对所有引用了本文档的相关站点首页、频道首页进行递归发布,在执行递归发布的时候要
			 * 将当前文档排出掉
			 */
			
			if(!this.context.isRecursive() && context.enableRecursive() 
					&& getActionType() == PublishObject.ACTIONTYPE_PUBLISH 
					&& 
					(!(this.contentContext.getPreStatusOfContent().getStatus() + "")
								.equals(DocumentStatus.PUBLISHED.getStatus())
							|| this.contentContext.forceStatusPublished()))
			{
				Set temp_ = getRecursivePubObject();
				if(temp_ != null && temp_.size() > 0)
				
//					this.recursivePubObjects.addAll(temp_);
				/**
				 * 如果是根任务，执行所有与本次任务中所有子任务相关对象的发布
				 */
//				if(context.isRootContext())
				{				
//					recursivePublish(temp_); 解决递归发布不正确的问题，先将所有需要递归发布的对象放到上下文中，当根任务
//                  发布动作完成后  待续
					this.context.addRecursivePubObjToContext(temp_);
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	

	/**
	 * 对文档正文进行预处理:
	 * 如果是分页新闻,则发布多条新闻
	 */
	private void prehandleContent(ContentContext contentContext)
	{
		String content = contentContext.getDocument().getContent();
		String encoding = contentContext.getSite().getEncoding();
		
		CmsLinkProcessor processor = new CmsLinkProcessor(contentContext,
														  CmsLinkProcessor.REPLACE_LINKS,
														  encoding);
		processor.setHandletype(CmsLinkProcessor.PROCESS_CONTENT);
		try {
			content = processor.process(content,encoding);
			this.contentContext.setContentOrigineTemplateLinkTable(processor.getOrigineTemplateLinkTable());
			if(processor.containSeparatorToken())
			{	
				String[] segments = StringUtil.split(content,CmsLinkProcessor.PAGE_TAG);
				if(segments.length <= 1)
					contentContext.getDocument().setContent(content);
				else
				{
//					contentContext.getDocument().setContent(segments[0]);
					
					contentContext.setSegments(segments);
					contentContext.getDocument().setContent(segments[0]);
				}
			}
			else
			{
				contentContext.getDocument().setContent(content);
			}
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * 获取当前发布对象的全局id
	 */
	public String getId() {
		StringBuffer buffer = new StringBuffer();
		if(params.length == 3)
		{
			
			buffer.append("contentid.").append(this.params[0]).append(".")
												 .append(params[1]).append(".")
												 .append(params[2])
										     .toString();
			
		}
		else if(params.length == 2)
		{
			buffer.append("contentid.").append(this.params[0]).append(".")
			 .append(params[1])
			 .toString();
		}
		
		else if(params.length == 1)
		{
			buffer.append("contentid.").append(this.params[0])
			 .toString();
		}
		if(buffer.length() > 0 && this.context.isRecursive())
		{
			buffer.append(".").append(this.context.isRecursive());
		}
		return buffer.toString();
			
	}

	public Document getViewDocument() {
		return viewDocument;
	}

	public void setViewDocument(Document viewDocument) {
		this.viewDocument = viewDocument;
	}



	public void recordRecursivePubObj(String refobj, int reftype, String site) {
		if(reftype == RecursivePublishManager.REFOBJECTTYPE_CHANNEL_ANSESTOR)
		{
//			super.recordRecursivePublihsObject(this.contentContext.getContentid(),
//					RecursivePublishManager.PUBOBJECTTYPE_DOCUMENT,
//					context.getSite().getSecondName(),refobj,reftype,site);
			
		}
		else if(reftype == RecursivePublishManager.REFOBJECTTYPE_PAGE)
		{
			
		}
		else if(reftype == RecursivePublishManager.REFOBJECTTYPE_CHANNELDOCUMENT)
		{
			//频道概览和当前频道所属的频频道一致
			if(this.contentContext.getChannelid().equals(refobj))
			{
				if(this.contentContext.getDetailTemplate() != null ){
					/**
					 * 记录相关的文档频道，引用元素被应用于频道下的继承频道细览模板的其他模板，那么需要记录引用元素与文档对应频道的关系
					 */
					if(this.contentContext.getDocument().getDetailtemplate_id() == 
						this.contentContext.getChannel().getDetailTemplateId())
					{
						super.recordRecursivePublihsObject(this.contentContext.getChannelid(),
															RecursivePublishManager.PUBOBJECTTYPE_CHANNELDOCUMENT,
															context.getSite().getSecondName(),refobj,reftype,site);
					}
					else
					{
						super.recordRecursivePublihsObject(this.contentContext.getContentid(),
								RecursivePublishManager.PUBOBJECTTYPE_DOCUMENT,
								context.getSite().getSecondName(),refobj,reftype,site);
					}
				}
			}
			else //如果不一致
			{
				if(this.contentContext.getDetailTemplate() != null ){
					/**
					 * 记录相关的文档频道，引用元素被应用于频道下的继承频道细览模板的其他模板，那么需要记录引用元素与文档对应频道的关系
					 */
					if(this.contentContext.getDocument().getDetailtemplate_id() == 
						this.contentContext.getChannel().getDetailTemplateId())
					{
						super.recordRecursivePublihsObject(this.contentContext.getChannelid(),
															RecursivePublishManager.PUBOBJECTTYPE_CHANNELDOCUMENT,
															context.getSite().getSecondName(),refobj,reftype,site);
					}
					else
					{
						super.recordRecursivePublihsObject(this.contentContext.getContentid(),
								RecursivePublishManager.PUBOBJECTTYPE_DOCUMENT,
								context.getSite().getSecondName(),refobj,reftype,site);
					}
				}
			}
		}
		
		
	}

	public Set getRecursivePubObject() {
		
		try {
			Set publishObjects = CMSUtil.getCMSDriverConfiguration()
			 .getCMSService()
			 .getRecursivePublishManager()
			 .getAllPubObjectsOfDocument(context,this.contentContext.getContentid());
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
		/**
		 * 删除文档本身相关的数据
		 */
		try {
			CMSUtil.getCMSDriverConfiguration()
			.getCMSService()
			.getRecursivePublishManager().deleteRefObjectsOfPubobject(context,this.contentContext.getContentid(),RecursivePublishManager.PUBOBJECTTYPE_DOCUMENT);
		} catch (RecursivePublishException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DriverConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/**
		 * 删除文档频道相关的数据
		 */
		
	}

}
