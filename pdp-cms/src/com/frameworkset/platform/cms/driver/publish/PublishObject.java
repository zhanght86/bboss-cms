package com.frameworkset.platform.cms.driver.publish;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;

import org.htmlparser.util.ParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.platform.cms.driver.config.DriverConfigurationException;
import com.frameworkset.platform.cms.driver.context.ContentContext;
import com.frameworkset.platform.cms.driver.context.Context;
import com.frameworkset.platform.cms.driver.context.PageContext;
import com.frameworkset.platform.cms.driver.distribute.Distribute;
import com.frameworkset.platform.cms.driver.distribute.DistributeException;
import com.frameworkset.platform.cms.driver.distribute.DistributeWraper;
import com.frameworkset.platform.cms.driver.distribute.IndexObject;
import com.frameworkset.platform.cms.driver.htmlconverter.CmsHtmlConverter;
import com.frameworkset.platform.cms.driver.htmlconverter.CmsLinkProcessor;
import com.frameworkset.platform.cms.driver.htmlconverter.CmsLinkProcessor.CMSLink;
import com.frameworkset.platform.cms.driver.htmlconverter.CmsLinkTable;
import com.frameworkset.platform.cms.driver.i18n.CmsEncoder;
import com.frameworkset.platform.cms.driver.jsp.CMSException;
import com.frameworkset.platform.cms.driver.jsp.CMSRequestContext;
import com.frameworkset.platform.cms.driver.jsp.CMSRequestDispatcher;
import com.frameworkset.platform.cms.driver.jsp.CMSRequestDispatcherImpl;
import com.frameworkset.platform.cms.driver.jsp.CMSServletRequestImpl;
import com.frameworkset.platform.cms.driver.jsp.CMSServletResponse;
import com.frameworkset.platform.cms.driver.jsp.CMSServletResponseImpl;
import com.frameworkset.platform.cms.driver.jsp.JspFile;
import com.frameworkset.platform.cms.driver.jsp.JspletWindow;
import com.frameworkset.platform.cms.driver.jsp.JspletWindowImpl;
import com.frameworkset.platform.cms.driver.publish.impl.ChannelPublishObject;
import com.frameworkset.platform.cms.driver.publish.impl.ContentPublishObject;
import com.frameworkset.platform.cms.driver.publish.impl.PagePublishObject;
import com.frameworkset.platform.cms.driver.publish.impl.PublishMonitor;
import com.frameworkset.platform.cms.driver.publish.impl.SitePublishObject;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.platform.cms.util.FileUtil;
import com.frameworkset.util.SimpleStringUtil;
import com.frameworkset.util.VelocityUtil;

import bboss.org.apache.velocity.Template;
import bboss.org.apache.velocity.VelocityContext;
import bboss.org.apache.velocity.exception.MethodInvocationException;
import bboss.org.apache.velocity.exception.ParseErrorException;
import bboss.org.apache.velocity.exception.ResourceNotFoundException;

/**
 * 
  * <p>Title: PublishObject</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: bbossgroups</p>
 *
 * @author biaoping.yin
 * @version 1.0
 */
public abstract class PublishObject implements java.io.Serializable

{
	
	private static final Logger log = LoggerFactory.getLogger(PublishObject.class);
	protected boolean synchronize = true;
	
	
	public static final boolean PUBLISH_LOCAL = true;
	public static final boolean PUBLISH_REMOTE = false;
	
	
	
	
	public static final boolean[] DEFAULT_PUBLISH_LOCAL = new boolean [] {PUBLISH_LOCAL,PUBLISH_REMOTE};
	
	/**
	 * 发布对象类型定义
	 */
	public static final int OBJECT_SITE = 0;
	public static final int OBJECT_CHANNEL = 1;
	public static final int OBJECT_DOCUMENT = 2;
	public static final int OBJECT_PAGE = 3;
	

	/**
	 * 标识发布对象是否是当前发布任务的起始发布对象
	 */
	protected boolean isRoot = false;
	
	/**
	 * 标识当前发布对象是否是联动（递归）发布对象
	 * true：表示当前正在执行联动（递归）发布操作
	 * false:表示当前正在执行直接发布操作，缺省值 
	 */
	protected boolean isRecursive = false;
	/**
	 * 发布执行的动作
	 */
	public static final int ACTIONTYPE_PUBLISH = 0;
	public static final int ACTIONTYPE_VIEW = 1;
	public static final int ACTIONTYPE_DESIGN = 2;
	
	private int actionType = ACTIONTYPE_PUBLISH;
	
	
	
	/**
	 * 站点首页
	 */
	public static final int  PUBLISH_SITE_INDEX = 1;
	
	/**
	 * 频道首页
	 */
	public static final int  PUBLISH_CHANNEL_INDEX = 2;
	
	/**
	 * 频道内容
	 */
	public static final int  PUBLISH_CHANNEL_CONTENT = 3;
	
	/**
	 * 发布文档
	 */
	public static final int  PUBLISH_DOCUMENT = 4;

//	
	/**
	 * 发布频道、子频道下的内容
	 */
	public static final int  PUBLISH_CHANNEL_RECCONTENT = 5;
	

	
	/**
	 * 发布单个页面
	 */
	public static final int  PUBLISH_PAGE = 6;
	
	/**
	 * 发布频道首页及子频道首页
	 */
	public static final int  PUBLISH_CHANNEL_RECINDEX = 7;

	
	/**
	 * 重发频道中已经发布过的文档，并且频道的文档模板必须和频道的模板保持一致
	 * added by biaoping.yin on 2007-06-26
	 */
	public static final int PUBLISH_CHANNEL_CONTENT_PUBLISHED = 8;
	
	/**
	 * 发布文档时是否联动发布文档相关的网站首页、频道首页、文档页面、普通页面等等
	 */
	public static final boolean ENABLE_PUBLISH_CONTENT_RECURSIVE = true;
	
	/**
	 * 发布文档时是否联动发布文档相关的网站首页、频道首页、文档页面、普通页面等等
	 */
	public static final boolean DISABLE_PUBLISH_CONTENT_RECURSIVE = false;
	
	/**
	 * 发布过程中清除缓存资源，操作员可以在系统界面上进行设置
	 */
	public static final int CLEAR_PUBLISH_RESOURCECACHE = 0;
	/**
	 * 发布过程中使用缓存资源，操作员可以在系统界面上进行设置
	 */
	public static final int USE_PUBLISH_RESOURCECACHE = 1;
	
	
	
	/**
	 * 标识当前的发布是增量发布还是完全发布，
	 * 本变量只针对频道和站点的发布有用
	 * false-全部发布
	 * true-部分发布
	 */
	private boolean isIncreament = false;
	
	/**
	 * 记录发布人员信息
	 */
	protected String[] publisher;
	protected Context parentContext;
	

	/**
	 * 发布范围定义
	 * 		增量式－适用于站点、频道
	 * 		全部－适用于站点、频道
	 * 		首页－适用于站点
	 * 		概览首页－使用于频道
	 *      单片文档-适用于文档
	 */



	
	
	/**
	 * 当前发布模式,缺省为动态与静态结合的模式
	 */
	protected PublishMode publishMode = PublishMode.MODE_STATIC_UNPROTECTED;
	
	/**
	 * 发布范围，缺省为发布站点首页
	 */
	protected int[] publishScope = new int[]{PUBLISH_SITE_INDEX};
	
//	protected Template template;
	/**
	 * 发布的脚本
	 */
	protected Scriptlet script;


	
	protected CMSRequestContext requestContext;
	protected Context context;
	protected boolean[] local2ndRemote;
	protected int[] distributeManners;
	protected PublishMonitor monitor;


	protected int currentPublishDepth = 0;


	protected int maxPublishDepth = -1;
	
	
	
//	protected PublishMonitor monitor;
//	
//	/**
//	 * 设置发布监控器
//	 * @param monitor
//	 */
//	public void setPublishMonitor(PublishMonitor monitor)
//	{
//		this.monitor = monitor;
//	}
	/**
	 * 模版脚本的缓冲器
	 */
	protected Map scriptlets = Collections.synchronizedMap(new HashMap());
	protected String[] params;

	/**
	 * 控制是否允许递归发布
	 */
	protected boolean enableRecursive = true;
	/**
	 * 发布时是否清除系统发布文件缓存
	 */
	protected boolean clearFileCache = false;
	
	protected String scopeToUIID = "";


	private boolean forceStatusPublished = false;


	
	public PublishObject()
	{
		
	}

	
	
//	public PublishObject(CMSRequestContext requestContext)
//	{
//		this.requestContext = requestContext;
//	}
	
	/**
	 * 构造函数 对于站点发布/频道发布
	 * modify by ge.tao
	 * date 2007-07-23
	 */
	public PublishObject(CMSRequestContext requestContext,
			  int[] publishScope,String[] params,
			  boolean[] local2ndRemote 	,
			  int[] distributeManners,
			  boolean isRoot,
			  String publisher[])
	{
		this.params = params;
		this.requestContext = requestContext;
		this.publishScope = publishScope;
		this.distributeManners = distributeManners;
		this.local2ndRemote = local2ndRemote;
		this.isRoot = isRoot;
		if(isRoot)
			monitor = PublishMonitor.createPublishMonitor();
		this.publisher = publisher;
		
	}
	
	
	
	
	public PublishObject(CMSRequestContext requestContext,
			  int[] publishScope,
			  boolean[] local2ndRemote 	,
			  int[] distributeManners,
			  boolean isRoot,
			  String publisher[])
	{
		this.params = null;
		this.requestContext = requestContext;
		this.publishScope = publishScope;
		this.distributeManners = distributeManners;
		this.local2ndRemote = local2ndRemote;
		this.isRoot = isRoot;
		if(isRoot)
			monitor = PublishMonitor.createPublishMonitor();
		this.publisher = publisher;
		
	}
	
	
	public PublishObject(CMSRequestContext requestContext,String[] params,Context parentContext)
	{
		this.params = params;
		this.parentContext = parentContext;
		this.requestContext = requestContext;
		this.publishScope = parentContext.getPublishScope();
		this.distributeManners = parentContext.getDistributeManners();
		this.local2ndRemote = parentContext.getLocal2ndRemote();


		this.isRoot = false;
		
	}
	
	public Context getContext()
	{
		checkContext();
		return this.context;
	}
	
	public PublishObject(CMSRequestContext requestContext,Context parentContext)
	{
//		this.params = params;
		this.parentContext = parentContext;
		this.requestContext = requestContext;
		this.publishScope = parentContext.getPublishScope();
		this.distributeManners = parentContext.getDistributeManners();
		this.local2ndRemote = parentContext.getLocal2ndRemote();
		this.isRoot = false;
	}
	
	/**
	 * 判断当前的发布任务是否可发布
	 * @return
	 */
	public boolean needPublish(PublishCallBack callBack)
	{
		return true;
	}
	
//	private boolean needAction()
//	{
//		if(context instanceof ChannelContext)
//		{
//			if(!((ChannelContext)context).haveOutlineTemplate())
//			{
//				this.context.getPublishMonitor().setPublishStatus(PublishMonitor.SCRIPT_TEMPLATE_NOEXIST);
//				this.context.getPublishMonitor().addFailedMessage("频道首页未设置",new Date(),this.publisher);
//				return false;
//			}
//			
//			
//			
//		}
//		if(context instanceof CMSContext)
//		{
//			if(!((CMSContext)context).haveIndexTemplate())
//			{
//				this.context.getPublishMonitor().setPublishStatus(PublishMonitor.SCRIPT_TEMPLATE_NOEXIST);
//				this.context.getPublishMonitor().addFailedMessage("站点首页未设置",new Date(),this.publisher);
//				
//				return false;
//			}
//			
//		}
//		
//		if(context instanceof PageContext)
//		{
//			return !this.context.getPublishMonitor().isTemplateFileNoexist();
//		}
//		
////		if(!this.context.getPublishMonitor().isScriptInited())
////			return false;
//		
//		return true;
//	}
	protected void setVariable(CMSServletRequestImpl request) throws Exception
	{
		
		request.setAttribute("cur_site", context.getSite());
	}
	/**
	 * 运行发布过程当中产生的临时jsp页面
	 * 并且生成最终发布页面
	 * 
	 * @throws PublishException
	 */
	protected void runPage()  {
		
		
//		if(!(context instanceof PageContext))
		 
//			if(!needAction())
//				return;
			JspFile jspFile = script.getJspFile();
			
			
			if(!this.context.getPublishMonitor().isScriptInited())
				return ;
			
			String uri = jspFile.getUri();
//			System.out.println(this.getClass().getName() +":" + uri);
			JspletWindow jspWindow = new JspletWindowImpl(context, jspFile);
			RequestDispatcher dispatcher = null;
			CMSRequestDispatcher cmsDispatcher = null;
			CMSServletRequestImpl request = null;
			CMSServletResponse response = null;
			
			
			try {
				request = new CMSServletRequestImpl(requestContext
						.getRequest(), requestContext.getPageContext(),jspWindow,this.context);
				response = new CMSServletResponseImpl(requestContext
						.getResponse(),context);
				dispatcher = request.getRequestDispatcher(uri);
				cmsDispatcher = new CMSRequestDispatcherImpl(dispatcher);
				setVariable(request);
				log.debug("common publishobject 开始:" + uri + ":" + this.getId());
				cmsDispatcher.include(request, response);
				StringBuffer out = response.getInternalBuffer().getBuffer();
				//out.append("中文");
				//System.out.println("out:" +out);
				
//				System.out.println(out);
//				long time = System.currentTimeMillis();
//				out = new StringBuffer(this.parser(out));
//				long end = System.currentTimeMillis();
//				System.out.println("custom time:" + (end - time));
				
				script.setContent(out);
				String[] path_info = this.saveHtml();
				
				this.context.getPublishMonitor().setPublishStatus(PublishMonitor.PAGE_GENERATED);
				this.context.getPublishMonitor().addSuccessMessage(new StringBuffer(context.toString()).append("生成页面[")
															.append(context.getRendURI())
															.append("]成功").toString(),context.getPublisher());
				System.err.println("common publishobject 结束:" + uri + ":" + this.getId());
				/**
				 * 记录生成的
				 */
				if(CMSUtil.enableIndex())
				{
					if(context instanceof ContentContext)
					{
						ContentContext contentContext = (ContentContext)context;
						IndexObject indexObject = new IndexObject(contentContext.getSiteID() + "||" + contentContext.getChannelid() + "||" +  contentContext.getContentid(),
																  IndexObject.DOCUMENT,
																  path_info,contentContext.getDocument(),contentContext.getContentOrigineTemplateLinkTable());
						context.addIndexObject(indexObject);
					}
				}
			}
	
			catch (IOException e) {
				e.printStackTrace();
				context.getPublishMonitor().setPublishStatus(PublishMonitor.PUBLISH_FAILED);
				this.context.getPublishMonitor().addFailedMessage(new StringBuffer(context.toString()).append("生成页面[")
						.append(context.getRendURI())
						.append("]失败:").append(SimpleStringUtil.formatBRException(e)).toString(),context.getPublisher());
				
			} catch (CMSException e) {
				e.printStackTrace();
//				e.printStackTrace();
				context.getPublishMonitor().setPublishStatus(PublishMonitor.PUBLISH_FAILED);
				this.context.getPublishMonitor().addFailedMessage(new StringBuffer(context.toString()).append("生成页面[")
						.append(context.getRendURI())
						.append("]失败:").append(SimpleStringUtil.formatBRException(e)).toString(),context.getPublisher());
			} catch (Exception e) {
				e.printStackTrace();
				context.getPublishMonitor().setPublishStatus(PublishMonitor.PUBLISH_FAILED);
				this.context.getPublishMonitor().addFailedMessage(new StringBuffer(context.toString()).append("生成页面[")
						.append(context.getRendURI())
						.append("]失败:").append(SimpleStringUtil.formatBRException(e)).toString(),context.getPublisher());
//				e.printStackTrace();
			}
			finally
			{
				if(request != null)
					request.clear();
			}
		 
		
	}
	
	/**
	 * 返回生成的临时文件的路径
	 * @return 生成的临时文件的相对路径 added by biaoping.yin on 2007.10.13 
	 * @throws Exception
	 */
	protected String[] saveHtml() throws Exception {
		String htmlpath_temp = "";
		String relative_path = "";
		String path_pre = this.context.getPublishTemppath()+ "/" + context.getSiteDir();
		if(context.getActionType() == PublishObject.ACTIONTYPE_PUBLISH)
		{
			if(context instanceof PageContext)
			{
				relative_path = ((PageContext)context).getPagePath();
				htmlpath_temp = new StringBuffer(path_pre)
										 .append("/")
										 .append(relative_path)
										 .toString();
				
			}
			else
			{
				String t_fileName = this.context.getFileName();
				relative_path = this.context.getRendPath() + "/" + t_fileName;
				htmlpath_temp = new StringBuffer(path_pre)
										 .append("/")
							 			 .append(relative_path)
										 .toString();
			}
		}
		else
		{
			if(context instanceof PageContext)
			{
				relative_path = ((PageContext)context).getPagePath();
				htmlpath_temp = new StringBuffer(this.context.getPreviewRootPath())
										 .append("/")
										 .append(relative_path)
						.toString();
			}
			else
			{ 
				String t_fileName = this.context.getFileName();
				relative_path = this.context.getRendPath() + "/" + t_fileName;
				htmlpath_temp = new StringBuffer(this.context.getPreviewRootPath())
				 .append("/")
				 .append(relative_path)
				 .toString();
			}
		}
	
		/**
		 * 保存到临时目录
		 */
//		saveHtml(htmlpath_temp);
		File html = FileUtil.createNewFile(htmlpath_temp);
		Writer fileWriter = null;
		try {
			fileWriter = new OutputStreamWriter( 
					new FileOutputStream(html),context.getCharset());//new FileWriter(html);

			Template template = VelocityUtil
					.getTemplate("publish/html_generator.vm");
			bboss.org.apache.velocity.context.Context vcontext = new VelocityContext();
			vcontext.put("htmlcontent", script.getContent());
			template.merge(vcontext, fileWriter);
			fileWriter.flush();
			fileWriter.close();

		} catch (ResourceNotFoundException e) {

			e.printStackTrace();
		} catch (ParseErrorException e) {

			e.printStackTrace();
		} catch (MethodInvocationException e) {

			e.printStackTrace();
		} catch (IOException e1) {
			throw e1;
		} catch (Exception e) {

			throw e;
		} finally {
			try {
				if (fileWriter != null )
					fileWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return new String[] {path_pre,relative_path};

	}
	
//	/**
//	 * 判断当前的页面地址是否是页面模版
//	 * @param pathUrl
//	 * @return
//	 */
//	protected boolean isPageTemplate(String pathUrl)
//	{
//		
//	}
	
	/**
	 * 发布模版中的静态页面
	 */
	private void publishTemplatePages()
	{
		/*
		 * 发布模版中包含的普通页面和动态页面
		 */
		JspFile jspFile = this.script.getJspFile();
//		if(!this.context.getPublishMonitor().isScriptInited())
//			return ;
		CmsLinkTable staticpages = jspFile.getOrigineStaticPageLinkTable();
		if(staticpages != null)
		{
			Iterator stitr = staticpages.iterator();
			while(stitr.hasNext())
			{
				CMSLink link = (CMSLink)stitr.next();
	//			if(link.getRelativeFilePath().equals("top.htm"))
	//				System.out.println("link.getRelativeFilePath():"+link.getRelativeFilePath());
	//			
	//			if(link.getRelativeFilePath().equals("index.htm"))
	//				System.out.println("link.getRelativeFilePath():"+link.getRelativeFilePath());
				/**
				 * 页面是否已经发布过
				 * 
				 */
				if( this.context.getPublishMonitor().containDistributePage(context.getSiteID(),link.getRelativeFilePath())
						|| this.context.getPublishMonitor().isPageTemplate(context,link.getRelativeFilePath())
						)
					continue;
				
				
				try {
					PagePublishObject pagePublishObject = new PagePublishObject(this.requestContext,new String[] {context.getSiteID(),link.getRelativeFilePath(),
							  link.getRelativeFilePathType() + ""},
							  this.context);
					this.context.getDriverConfiguration().getPublishEngine().publish(pagePublishObject);
				} catch (PublishException e) {
					
					e.printStackTrace();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			
		} 
		
		
		/**
		 * 发布动态页面
		 */
		
		CmsLinkTable dynamicpages = jspFile.getOrigineDynamicPageLinkTable();
		if(dynamicpages != null)
		{
			Iterator dtitr = dynamicpages.iterator();
			
			while(dtitr.hasNext())
			{
				CMSLink link = (CMSLink)dtitr.next();
				if(this.context.getPublishMonitor().containDistributePage(context.getSiteID(),link.getRelativeFilePath())
						|| this.context.getPublishMonitor().isPageTemplate(context,link.getRelativeFilePath()))
					continue;
				PagePublishObject pagePublishObject = new PagePublishObject(this.requestContext,new String[] {context.getSiteID(),link.getRelativeFilePath(),link.getRelativeFilePathType() + ""},this.context);
				try {
					this.context.getDriverConfiguration().getPublishEngine().publish(pagePublishObject);
				} catch (PublishException e) {
					
					e.printStackTrace();
				}
				
			}
		}
	}
	
	
	/**
	 * 发布发布过程中涉及的页面
	 *
	 */
	private void publishContextePages()
	{
		/*
		 * 发布发布过程中涉及的页面
		 */
		
		CmsLinkTable staticpages = context.getStaticPageLinkTable();
		if(staticpages != null)
		{
			Iterator stitr = staticpages.iterator();
			while(stitr.hasNext())
			{
				CMSLink link = (CMSLink)stitr.next();
	//			if(link.getRelativeFilePath().equals("top.htm"))
	//				System.out.println("link.getRelativeFilePath():"+link.getRelativeFilePath());
	//			
	//			if(link.getRelativeFilePath().equals("index.htm"))
	//				System.out.println("link.getRelativeFilePath():"+link.getRelativeFilePath());
				/**
				 * 页面是否已经发布过
				 * 
				 */
				if( this.context.getPublishMonitor().containDistributePage(context.getSiteID(),link.getRelativeFilePath())
						|| this.context.getPublishMonitor().isPageTemplate(context,link.getRelativeFilePath())
						)
					continue;
				
				
				try {
					PagePublishObject pagePublishObject = new PagePublishObject(this.requestContext,new String[] {context.getSiteID(),link.getRelativeFilePath(),
							  link.getRelativeFilePathType() + ""},
							  this.context);
					this.context.getDriverConfiguration().getPublishEngine().publish(pagePublishObject);
				} catch (PublishException e) {
					
					e.printStackTrace();
				}
			}
			
		} 
		
		
		/**
		 * 发布动态页面
		 */
		
		CmsLinkTable dynamicpages = context.getDynamicPageLinkTable();
		if(dynamicpages != null)
		{
			Iterator dtitr = dynamicpages.iterator();
			
			while(dtitr.hasNext())
			{
				CMSLink link = (CMSLink)dtitr.next();
				if(this.context.getPublishMonitor().containDistributePage(context.getSiteID(),link.getRelativeFilePath())
						|| this.context.getPublishMonitor().isPageTemplate(context,link.getRelativeFilePath()))
					continue;
				PagePublishObject pagePublishObject = new PagePublishObject(this.requestContext,
																			new String[] {context.getSiteID(),link.getRelativeFilePath(),link.getRelativeFilePathType() + ""},
																			this.context);
				try {
					this.context.getDriverConfiguration().getPublishEngine().publish(pagePublishObject);
				} catch (PublishException e) {
					
					e.printStackTrace();
				}
				
			}
		}
	}
	
	/**
	 * 发布模版中包含的普通页面和动态页面
	 * 发布模版中特定标签中包含的普通页面和动态页面
	 */
	protected void publishPages()
	{
		/*
		 * 发布静态页面,如果script == null
		 * 直接返回
		 */
		if(script == null )
		{
			System.out.println("publishPages: script=null");
			return;
		}
		
		if(script.getJspFile() == null)
		{
			System.out.println("publishPages: script.jspfile=null");
			return;
		}
		/**
		 * 如果是完全发布才发布包含的页面
		 */
//		if(!this.isIncreament())
		{
			publishTemplatePages();
			
			publishContextePages();
		}
		
	}
	
	
	
	/**
	 * 分发文件
	 * @param jspFile
	 */
	protected void disttribute()
	{
		Distribute distribute = new DistributeWraper();
		try
		{
			
			
			try
			{
				if(!this.isBatchPublish() && this.script != null && !this.context.getPublishMonitor().isDistributeCompleted())
				{
					JspFile jspFile = this.script.getJspFile();
					distribute.init(context, jspFile);
				}
				else
				{
					distribute.init(context, null);
					System.out.println("disttribute():script=null,分发批量发布生成的文件，或者外部连接文件。" );
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}			
			
			distribute.before();
			/**
			 * 如果页面已经生成，则分发页面和页面相关的附件到临时目录
			 */
			try
			{
				if(this.context.getPublishMonitor().isPageGenerated())
					distribute.distribute();
			}
			catch(Exception e)
			{
				
			}
			try
			{
				distribute.after();
			}
			catch(Exception e)
			{
				
			}
			
			context.getPublishMonitor().setPublishStatus(PublishMonitor.PUBLISH_COMPLETED);
		}
	    catch (DistributeException e) {
//	    	e.printStackTrace();
			context.getPublishMonitor().setPublishStatus(PublishMonitor.DISTRIBUTE_FAILED);
		}
		finally
		{
			distribute.dofinally();
		}
		
	}
	/**
	 * 解析生成的html脚本的过程
	 *
	 */
	public String parser(StringBuffer content)
	{
		CmsLinkProcessor processor = new CmsLinkProcessor(this.context,CmsLinkProcessor.REPLACE_LINKS,CmsEncoder.ENCODING_ISO_8859_1);
		processor.setHandletype(CmsLinkProcessor.PROCESS_PUBLISHRESULT);
		try {
			String content_t = processor.process(content.toString(),CmsEncoder.ENCODING_UTF_8);
			if(context.isRootContext() && context.autoCorrectHtml())
			{
				CmsHtmlConverter converter = new CmsHtmlConverter();
				try {
					
					content_t = converter.convertToString(content.toString());
				} catch (UnsupportedEncodingException e) {
					
					e.printStackTrace();
				}
			}
			return content_t;
		} catch (ParserException e) {
			if(context.isRootContext() && context.autoCorrectHtml())
			{
				CmsHtmlConverter converter = new CmsHtmlConverter();
				String content_t;
				
					try {
						content_t = converter.convertToString(content.toString());
						return content_t;
					} catch (UnsupportedEncodingException e1) {
						
						e1.printStackTrace();
					}
					
				
				
			}
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
		return content.toString();
	}
	
	public abstract void doPublish() throws PublishException;
	
	
	protected void doBatchPublish() throws PublishException
	{
		
	}
	
	protected void init() throws PublishException
	{
		if(this.context == null)
			this.context = this.buildContext();
	}
	
	
	
	/**
	 *  抽象方法，具体的发布对象子类去实现
	 * 添加与当前任务相关的发布对象到上下文中
	 */
	public abstract Set getRecursivePubObject() ;
	
	
	/**
	 * 抽象方法，具体的发布对象子类去实现
	 * 记录当前发布对象与相关发布对象中包含的元素之间的引用关系，当相关的元素发生变化时
	 * 本方法在发布的过程中由页面的相关元素进行回调
	 */
	public abstract void recordRecursivePubObj(String refobj,int reftype,String site);
	
	/**
	 * 执行发布过程
	 *
	 */
	public void publish() throws PublishException
	{
		
		init();
		if(this.context.getPublishMonitor().isTemplateNoexist() //模板不存在时 ，这个条件在这里永远不会成立
				|| 
				this.context.getPublishMonitor().isPageNotExist())//如果是在发布页面，并且页面不存在时忽略发布的执行
			return;
		try
		{
			//初始化模板文件，生成临时文件
			initScriptlet();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		try
		{
			doPublish();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		if(!context.getPublishMonitor().isDistributeCompleted() 
				&& !context.getPublishMonitor().isPublishFailed())
		{
			if(!reachMaxdeth())
				try
				{
					publishPages();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
		}
		
		/**
		 * 如果允许递归发布，将该发布任务所影响的其他发布对象添加到上下文中
		 * 如果当前发布任务是根任务时，就会执行整个发布过程中记录的所有发布对象 
		 * 2007.11.15日之前屏蔽，之后打开，因为之前只考虑了文档的递归发布，但是还有页面和频道的
		 */
		if(context.enableRecursive() && context.getPublishObject().getActionType() == PublishObject.ACTIONTYPE_PUBLISH)
		{
//			addRecursivePubObjToContext();
			/**
			 * 如果是根任务，执行所有与本次任务中所有子任务相关对象的发布
			 */
			if(this.isRoot)
			{				
				recursivePublish();
			}
		}
		disttribute();
		
		
		if(context.needRecordRefObject() && 
				context.getPublishObject().getActionType() == PublishObject.ACTIONTYPE_PUBLISH)
		{
			// 存储当前发布任务对应的发布对象
			if (context.getPublishMonitor().isPublishCompleted() 
					|| context.getPublishMonitor().isDistributeCompleted())
			{
				delteRefObjects();	
				storeRefObjects();
			}
		}
		
		
	}
	
	/**
	 * 存储最新的引用元素
	 *
	 */
	protected void storeRefObjects()
	{
		
		try {
//			CMSUtil.getCMSDriverConfiguration().getCMSService().getRecursivePublishManager().deleteRefObjectsOfPubobject(context,);
			CMSUtil.getCMSDriverConfiguration().getCMSService().getRecursivePublishManager().trace(context,context.getRefObjects());
		} catch (RecursivePublishException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DriverConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 删除与发布对象相关的所有相关元素
	 *
	 */
	protected abstract void delteRefObjects();
	
	
	/**
	 * 执行所有与本次任务中所有子任务相关对象的发布
	 * 2007.
	 */
	public void recursivePublish()
	{
		Set recursivePubObjects = this.context.getRecursivePubObjects();
		Iterator  recursivePubObjects_ = recursivePubObjects.iterator();
		for(; recursivePubObjects_.hasNext(); )
		{
			PubObjectReference ref = (PubObjectReference)recursivePubObjects_.next();
			
			
			/**
			 * 相关的站点发布
			 */
			if(ref.getPubobjectType() == RecursivePublishManager.PUBOBJECTTYPE_SITE)
			{
				
				PublishObject publishObject = new SitePublishObject(requestContext,
																	new String[] {ref.getPubobject()},
																	this.context,
																	new int[] {PublishObject.PUBLISH_SITE_INDEX});
				
				publishObject.setMaxPublishDepth(1);
				publishObject.setCurrentPublishDepth(1);
				publishObject.setRecursive(true);
				

				try {
					context.getDriverConfiguration().getPublishEngine().publish(publishObject);
				} catch (PublishException e) {
					
					e.printStackTrace();
				}
								
			}			
			else if(ref.getPubobjectType() == RecursivePublishManager.PUBOBJECTTYPE_CHANNEL)
			{
				try
				{
					PublishObject channelPublishObject = new ChannelPublishObject(requestContext,
																new String[]{CMSUtil.getSiteCacheManager().getSiteByEname(ref.getPubSite()).getSiteId() + "",ref.getPubobject()},
																this.context ,
																new int[] {PublishObject.PUBLISH_CHANNEL_INDEX});
					channelPublishObject.setMaxPublishDepth(1);
					channelPublishObject.setCurrentPublishDepth(1);
					channelPublishObject.setRecursive(true);
					context.getDriverConfiguration()
							.getPublishEngine()
							.publish(channelPublishObject);
				} catch (PublishException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			else if(ref.getPubobjectType() == RecursivePublishManager.PUBOBJECTTYPE_CHANNELDOCUMENT)
			{
				try {
					
					ChannelPublishObject channelPublishObject = new ChannelPublishObject(this.requestContext,
																				  new String[]{CMSUtil.getSiteCacheManager().getSiteByEname(ref.getPubSite()).getSiteId() + "",
																					ref.getPubobject()},context,new int[] {PublishObject.PUBLISH_CHANNEL_CONTENT_PUBLISHED});
					channelPublishObject.setMaxPublishDepth(1);
					channelPublishObject.setCurrentPublishDepth(1);
					channelPublishObject.setRecursive(true);
					/**
					 * 如果当前发布的文档和属于引用对象ref.getPubobject()对应的频道的文档，
					 * 在联动发布本频道下的文档时，需要排除文档自身被重复发布
					 * channelPublishObject已经考虑这种情况，特注释本代码段
					 */
//					if(context instanceof ContentContext)
//					{
//						String docid = ((ContentContext)context).getDocument().getDocument_id() + "";
//						String channelid = ((ContentContext)context).getDocument().getChanel_id() + "";
//						if(channelid.equals(ref.getPubobject()))
//						{
//							channelPublishObject.setExcluedDocument(docid);
//						}
//					}
					context.getDriverConfiguration()
							.getPublishEngine()
							.publish(channelPublishObject);
				} catch (PublishException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			else if(ref.getPubobjectType() == RecursivePublishManager.PUBOBJECTTYPE_PAGE)
			{
				try {
					PublishObject pagePublishObject = new PagePublishObject(this.requestContext,
																			  new String[]{CMSUtil.getSiteCacheManager().getSiteByEname(ref.getPubSite()).getSiteId() + "",
																				ref.getPubobject()},context,new int[] {PublishObject.PUBLISH_CHANNEL_CONTENT});
					pagePublishObject.setMaxPublishDepth(1);
					pagePublishObject.setCurrentPublishDepth(1);
					pagePublishObject.setRecursive(true);
					context.getDriverConfiguration()
							.getPublishEngine()
							.publish(pagePublishObject);
				} catch (PublishException e) {
					
					e.printStackTrace();
				} catch (Exception e) {
					
					e.printStackTrace();
				}				
			}
			else if(ref.getPubobjectType() == RecursivePublishManager.PUBOBJECTTYPE_DOCUMENT)
			{
				try {
					/**
					 * 文档联动发布时，判断发布的当前文档和相关对象是不是一样，如果是一样的话跳过该相关对象的发布
					 * 
					 */
					if(context instanceof ContentContext)
					{
						try
						{
							 String docid = ((ContentContext)context).getDocument().getDocument_id() + "";
							 if(docid.equals(ref.getPubobject()))
							 {
								 context.getPublishMonitor().addSuccessMessage("忽略文档[" 
										 		+ docid + "]引用的相关文档对象[" 
										 		+ ref.getPubobject() 
										 		+ "]的发布操作：引用对象和发布对象为同一对象。",context.getPublisher());
								 continue;
							 }
						}
						catch(Exception e)
						{
							e.printStackTrace();
						}
						 
					}
					PublishObject docPublishObject = new ContentPublishObject(this.requestContext,
						  new String[]{CMSUtil.getSiteCacheManager().getSiteByEname(ref.getPubSite()).getSiteId() + "",
							ref.getPubobject()},context);
					docPublishObject.setMaxPublishDepth(1);
					docPublishObject.setCurrentPublishDepth(1);
					docPublishObject.setRecursive(true);
					this.context.getDriverConfiguration()
						.getPublishEngine()
						.publish(docPublishObject);
				} catch (PublishException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//待扩展的其他相关的发布对象
			else if(ref.getPubobjectType() == RecursivePublishManager.PUBOBJECTTYPE_OTHER)
			{
//				PublishObject channelPublishObject = new ChannelPublishObject(this.requestContext,
//																			  new String[]{siteid,
//																				ref.getPubobject()},context,new int[] {PublishObject.PUBLISH_CHANNEL_CONTENT});
//				context.getDriverConfiguration()
//						.getPublishEngine()
//						.publish(channelPublishObject);
				
			}
		}
	}
	
	/**
	 * 判断当前发布的任务是否已经到达最大深度
	 * @return
	 */
	protected boolean reachMaxdeth()
	{
		checkContext();
		if(context.getMaxPublishDepth() <= 0)
			return false;
		return this.context.getCurrentPublishDepth() >= this.context.getMaxPublishDepth();
	}
	
	
	
	public void checkContext()
	{
		if(this.context == null)
		{
			TransactionManager tm = new TransactionManager (); 
			try {	
				tm.begin();
				context = this.buildContext();
				tm.commit();
			} catch (PublishException e) {
				throw new NestedPublishException(e);
			}
			catch (NestedPublishException e) {
				
				throw e;
			}
			catch (RuntimeException e) {
				
				throw new NestedPublishException(e);
			}
			catch (Exception e) {
				
				throw new NestedPublishException(e);
			}
			finally
			{
				tm.release();
			}
		}	
			
			
	}

	public abstract Context buildContext()  throws PublishException;
	
	
	public abstract void initScriptlet() throws PublishException;
	
	/**
	 * 当前发布任务的监控器索引，以发布任务的唯一id为索引
	 * 发布站点任务：siteid
	 * 发布频道任务：channelid + ":" + siteid
	 * 发布文档任务：docuemntid + ":" + siteid
	 * 当发布任务完成后，将当前任务的监控器删除，
	 * 任务开始时创建任务的监控器，并且缓冲到索引器中
	 * 如果需要发布的任务在监控器中存在，那么将提示用户发布任务重复，新的发布任务不允许启动
	 * 
	 */
//	private static Map monitors = new ConcurrentHashMap();
	protected abstract String getId();
	
	/**
	 * 获取发布对象的标识
	 */
	public String getPublishObjectID()
	{
		checkContext();
		if(this.context == null)
		{
			return "";
		}
		else			
			return getId();
		
	}
	
	public PublishMonitor getPublishMonitor()
	{
		checkContext();
		return this.context.getPublishMonitor();
	}
	
	public PublishMonitor getInnerPublishMonitor()
	{
		return this.monitor;
	}
	
	public boolean isRoot()
	{
		return this.isRoot;
	}


	public int[] getDistributeManners() {
		return distributeManners;
	}


	public void setDistributeManners(int[] distributeManners) {
		this.distributeManners = distributeManners;
	}


	public boolean[] getLocal2ndRemote() {
		return local2ndRemote;
	}
	
	/**
	 * 判断发布的文档是否时批处理发布
	 * @return
	 */
	public boolean isBatchPublish()
	{
		return false;
	}


	
	
//	/**
//	 * 合并关键字到系统的头文件中
//	 * @param header
//	 * @param keywords
//	 * @return
//	 */
//	protected String appendKeywords(String header,String keywords)
//	{
//		
//		if(keywords == null)
//			keywords = "";
//		StringTemplate str_template = new StringTemplate(header);
//		Map params = new HashMap();
//		params.put("keywords",keywords);
//		String r_header = str_template.apply(params);	
//		return r_header;
//	}


	public CMSRequestContext getRequestContext() {
		return requestContext;
	}


	public String[] getPublisher() {
		//this.checkContext();
		return publisher;
	}


	public boolean isSynchronized() {
		return this.synchronize;
	}


	public void setSynchronized(boolean synchronize) {
		this.synchronize = synchronize;
	}

	/**
	 * 返回发布生成的主页面地址
	 * @return
	 */
	public String getPublishPageUrl() {
		checkContext();
		return this.context.getPublishedPageUrl();
	}
	
	/**
	 * 返回生成的预览页面地址
	 * @return
	 */
	public String getPublishViewPageUrl()
	{
		checkContext();
		return this.context.getPreviewPageUrl();
	}


	public int getActionType() {
		return actionType;
	}


	public void setActionType(int actionType) {
		this.actionType = actionType;
	}


	public boolean isIncreament() {
		return isIncreament;
	}


	public void setIncreament(boolean isIncreament) {
		this.isIncreament = isIncreament;
	}

	public int[] getPublishScope() {
		return publishScope;
	}
	
	/**
	 * 获取发布的深度
	 * -1，0标识不受限制
	 * 其他情况每发布一个层次，depth 自动加1，
	 * 达到最深允许的深度时，如果还存在下级子任务，不执行直接返回
	 */
	public int getCurrentPublishDepth()
	{
		return this.currentPublishDepth;
	}
	/**
	 * 设置发布的深度
	 * -1，0标识不受限制
	 * 其他情况每发布一个层次，depth 自动加1，
	 * 达到最深允许的深度时，如果还存在下级子任务，不执行直接返回
	 */
	public void setCurrentPublishDepth(int currentdepth)
	{
		this.currentPublishDepth = currentdepth;
	}
	
	/**
	 * 获取发布的最大允许深度
	 * -1，0标识不受限制
	 * 其他情况每发布一个层次，depth 自动加1，
	 * 达到最深允许的深度时，如果还存在下级子任务，不执行直接返回
	 */
	public int getMaxPublishDepth()
	{
		return this.maxPublishDepth;
	}
	
	/**
	 * 设置发布的最大深度
	 * @param maxPublishDepth
	 */
	public void setMaxPublishDepth(int maxPublishDepth)
	{
		this.maxPublishDepth = maxPublishDepth;
	}


	/**
	 * 是否允许递归发布
	 * @return
	 */
	public boolean enableRecursive() {
		return enableRecursive;
	}



	public void setEnableRecursive(boolean enableRecursive) {
		this.enableRecursive = enableRecursive;
	}

	
	/**
	 * 记录发布对象和引用元素之间的关系
	 * @param pagePath
	 * @param string
	 * @param site
	 * @param refobj
	 * @param reftype
	 * @param site2
	 */
	protected void recordRecursivePublihsObject(String pubobj, 
			int pubtype, String pubsite, String refobj, int reftype, String refsite) {
//		try {
			
			PubObjectReference object = new PubObjectReference();
			object.setPubobject(pubobj);
			object.setPubobjectType(pubtype);
			object.setPubSite(pubsite);
			object.setReferenceObject(refobj);
			object.setRefobjectType(reftype);
			object.setRefSite(refsite);		
			this.context.addRefObject(object);
//			CMSUtil.getCMSDriverConfiguration().getCMSService()
//						.getRecursivePublishManager().trace(context,object);
//		} catch (RecursivePublishException e) {
//			 
//			e.printStackTrace();
//		} catch (DriverConfigurationException e) {
//			
//			e.printStackTrace();
//		}
		
		
		
		
	}
	
	/**
	 * 将发布范围转换为字符串标识，以逗号分隔
	 * @return
	 */
	protected String changeScopeToUIID()
	{
		if(scopeToUIID.equals(""))
		{
			publishScope = CMSUtil.sort(publishScope);
			if(this.publishScope.length > 0)
			{
				StringBuffer buffer = new StringBuffer();
				for(int i = 0; i < this.publishScope.length; i ++)
				{
					if(i == 0)
						buffer.append("" + this.publishScope[i]);
					else
						buffer.append("." + this.publishScope[i]);
				}
				scopeToUIID = buffer.toString();
			}
		}
		
		return scopeToUIID;
		
			
	}
	
	/**
	 * 设置强制执行已发布文档的递归发布标识
	 * 为true时将执行已经发布文档的递归发布操作
	 * 为false时不执行，缺省为false
	 * @param forceStatusPublished
	 */
	public void forceStatusPublished(boolean forceStatusPublished) {
		this.forceStatusPublished   = forceStatusPublished;
		
	}



	public boolean isForceStatusPublished() {
		return this.forceStatusPublished;
		
	}


    /**
     * 从外部注入PublishMonitor monitor
     * @param monitor 
     * PublishObject.java
     * @author: ge.tao
     */
	public  void setMonitor(PublishMonitor monitor) {
		this.monitor = monitor;
	}
	
	/**
	 * 当发布任务执行完成后，检测发布的页面是否已经设置共全文检索进行检索的meta标记，如果没有设置
	 * 则需要对内容进行重新分析和处理并设置相关的meta数据
	 */
	protected void addMetaData()
	{
		
	}



	public boolean isRecursive() {
		return isRecursive;
	}



	public void setRecursive(boolean isRecursive) {
		this.isRecursive = isRecursive;
	}
	
	public void destroy()
	{
		this.context.destroy();
	}



	public void setClearFileCache(boolean clearFileCache) {
		this.clearFileCache = clearFileCache;
		
	}



	public boolean isClearFileCache() {
		return clearFileCache;
	}
	
}
