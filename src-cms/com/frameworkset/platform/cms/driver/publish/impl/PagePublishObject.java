package com.frameworkset.platform.cms.driver.publish.impl;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Set;

import com.frameworkset.platform.cms.driver.config.DriverConfigurationException;
import com.frameworkset.platform.cms.driver.context.Context;
import com.frameworkset.platform.cms.driver.context.PageContext;
import com.frameworkset.platform.cms.driver.context.impl.CMSContextImpl;
import com.frameworkset.platform.cms.driver.context.impl.PageContextImpl;
import com.frameworkset.platform.cms.driver.distribute.Distribute;
import com.frameworkset.platform.cms.driver.distribute.DistributeException;
import com.frameworkset.platform.cms.driver.distribute.DistributeWraper;
import com.frameworkset.platform.cms.driver.htmlconverter.CmsLinkProcessor.CMSLink;
import com.frameworkset.platform.cms.driver.jsp.CMSRequestContext;
import com.frameworkset.platform.cms.driver.jsp.JspFile;
import com.frameworkset.platform.cms.driver.publish.PublishCallBack;
import com.frameworkset.platform.cms.driver.publish.PublishException;
import com.frameworkset.platform.cms.driver.publish.PublishObject;
import com.frameworkset.platform.cms.driver.publish.RecursivePublishException;
import com.frameworkset.platform.cms.driver.publish.RecursivePublishManager;
import com.frameworkset.platform.cms.util.AttributeKeys;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.platform.cms.util.FileUtil;
import com.frameworkset.util.SimpleStringUtil;

/**
 * 页面的发布需要判断不同类型页面的发布
  * <p>Title: PagePublishObject</p>
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
public class PagePublishObject extends PublishObject implements java.io.Serializable {
	
	private String pageType; 
	private PageContext pagecontext ;
	
	/**
	 * 页面请求的基本参数
	 */

	public PagePublishObject(CMSRequestContext requestContext, 
							 int[] publishScope,
							 String[] params,
							 String[] publisher,
							 boolean isRoot,
								boolean[] local2ndRemote,
								int[] distributeManners) {
		super(requestContext, publishScope, params,
				local2ndRemote,
				 distributeManners,isRoot,publisher);
		
	}
	
	public PagePublishObject(CMSRequestContext requestContext,
			 String[] params,
			 Context parentContext
			 ) {
		super(requestContext,  params,parentContext);
	}
	

	public PagePublishObject(CMSRequestContext requestContext, String[] params, Context parentContext, int[] publishScope) {
		super(requestContext,  params,parentContext);
		this.publishScope = publishScope;
	}

	public Context buildContext() {
		return pagecontext = this.buildPageContext();

	}
	
	
	protected PageContext buildPageContext()
	{
		if(this.params == null)
		{
			String siteid = this.requestContext.getRequest().getParameter(AttributeKeys.CMSSITEREQUESTKEY);
			String pagePath  = this.requestContext.getRequest().getParameter(AttributeKeys.PAGEREQUESTKEY);
			pageType = this.requestContext.getRequest().getParameter(AttributeKeys.PAGEREQUESTKEY_TYPE);
			if(pageType == null || pageType.trim().equals(""))
				pageType = CMSUtil.getPageType(pagePath ) + "";
				
			if(this.parentContext == null)
			{
				parentContext = new CMSContextImpl(siteid,publisher,this,null);
				parentContext.setEnableRecursive(this.enableRecursive());
				parentContext.setClearFileCache(this.isClearFileCache());
				pagecontext = new PageContextImpl(siteid,pagePath,pageType,parentContext,this,monitor);
				pagecontext.setMaxPublishDepth(this.getMaxPublishDepth());
				pagecontext.setCurrentPublishDepth(1);
			}
			else
			{
				pagecontext = new PageContextImpl(siteid,pagePath,pageType,parentContext,this,parentContext.getPublishMonitor().createSubPublishMonitor());
				pagecontext.setMaxPublishDepth(parentContext.getMaxPublishDepth());
				pagecontext.setCurrentPublishDepth(parentContext.getCurrentPublishDepth());				
			}
		}
		else
		{
			/*
			 * 如果是单独执行页面的发布，则直接发布页面即可
			 */
			if(this.parentContext == null)
			{
				this.parentContext = new CMSContextImpl(params[0],publisher,this,null);
				parentContext.setEnableRecursive(this.enableRecursive());
				parentContext.setClearFileCache(this.isClearFileCache());
				if(params.length == 3)
					this.pageType = params[2];
				else
				{
					pageType = CMSUtil.getPageType(params[1]) + "";
				}
				pagecontext = new PageContextImpl(params[0],params[1],params[2],parentContext,this,PublishMonitor.createPublishMonitor());
				pagecontext.setMaxPublishDepth(this.getMaxPublishDepth());
				pagecontext.setCurrentPublishDepth(1);				
				
			}
			else
			{
				if(params.length == 3)
					this.pageType = params[2];		
				else
				{
					pageType = CMSUtil.getPageType(params[1]) + "";
				}
				pagecontext = new PageContextImpl(params[0],params[1],parentContext,this,parentContext.getPublishMonitor().createSubPublishMonitor());
				pagecontext.setMaxPublishDepth(parentContext.getMaxPublishDepth());
				pagecontext.setCurrentPublishDepth(parentContext.getCurrentPublishDepth());
			}
		}
		return pagecontext;
	}
	
	public void initScriptlet() throws PublishException {
		this.script = ScriptletUtil.createScriptlet(
				(PageContext)context);
	}
	private void publishDirectory(String directory)
	{
		//发布子目录
		//发布binary文件
		//发布html文件
		//忽略模版的发布
		//发布动态文件
	}
	
	public void doPublish() {
		if(this.pageType.equals( CMSLink.TYPE_STATIC_PAGE + "")
				|| this.pageType.equals( CMSLink.TYPE_DYNAMIC_PAGE+ ""))
		{
			runPage();
			/**
			 * 执行后续页面的发布功能
			 */
			if(this.pagecontext.isPagintion())
			{
				//自动翻到下一页
				pagecontext.next();
				
				for(int i = 1; i < pagecontext.getTotalPages(); i ++)
				{
				    pagecontext.getPublishMonitor().setPublishStatus(PublishMonitor.SCRIPT_INITED);//发布下一页时，操作没有执行
					runPage();
					//自动翻到下一页
					pagecontext.next();
				}
			}
		}
		else if(this.pageType.equals( CMSLink.TYPE_DIRECTORY + ""))
		{
			this.publishDirectory(this.params[0]);
			
		}
		else if(this.pageType.equals(CMSLink.TYPE_BINARY_PAGE + ""))
		{
			String htmlpath_temp = new StringBuffer(this.context.getPublishTemppath())
			 .append("/")
			 .append(context.getSiteDir())
			 .append("/")
			 .append(((PageContext)context).getPagePath())
			 .toString();
			try {
				FileUtil.fileCopy(this.script.getJspFile().getAbsolutePath(),htmlpath_temp);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else 
		{
			
		}
		
	}
	
	public void publishPages()
	{
		
		if(this.pageType.equals( CMSLink.TYPE_STATIC_PAGE + "")
				|| this.pageType.equals( CMSLink.TYPE_DYNAMIC_PAGE+ ""))
			super.publishPages();
	}
	
	/**
	 * 判断文件是否是二进制文件
	 * @param file
	 * @return
	 */
	public boolean isBinaryFile(File file)
	{
		return CMSUtil.isBinaryFile(file.getName());
	}
	
	protected void runPage()
	{
		
		JspFile jspFile = script.getJspFile();
		if(!this.context.getPublishMonitor().isScriptInited())
			return ;
		
		//页面中包含jsp标签，需要做特殊处理
		if(jspFile.containJspTag())
			super.runPage();
		else
		{
			if(script.getContent() != null)
			{
//				StringBuffer out = new StringBuffer(this.script.getOutput()) ;
//				script.setContent(out);	
				try {
					this.saveHtml();
					
//					this.context.getPublishMonitor().setPublishStatus(PublishMonitor.PAGE_GENERATED);
					if(!this.pagecontext.isPagintion())
						this.pagecontext.getPublishMonitor().setPublishStatus(PublishMonitor.PAGE_GENERATED);
					else
					{
						if(this.pagecontext.isPublishAllPage())
							this.context.getPublishMonitor().setPublishStatus(PublishMonitor.PAGE_GENERATED);
					}
					this.context.getPublishMonitor().addSuccessMessage(new StringBuffer(context.toString()).append("生成页面[")
							.append(this.pagecontext.getPagePath())
							.append("]成功").toString(),context.getPublisher());
				} catch (Exception e) {
					context.getPublishMonitor().setPublishStatus(PublishMonitor.PUBLISH_FAILED);
					this.context.getPublishMonitor().addFailedMessage(new StringBuffer(context.toString()).append("生成页面[")
							.append(this.pagecontext.getPagePath())
							.append("]失败:").append(SimpleStringUtil.formatBRException(e)).toString(),context.getPublisher());
					e.printStackTrace();
				}
			}
		}
	}
	
//	protected void saveHtml() throws Exception {
//		String htmlpath_temp = "";
//		if(context.getActionType() == PublishObject.ACTIONTYPE_PUBLISH)
//		{
////			if(context instanceof PageContext)
//				htmlpath_temp = new StringBuffer(this.context.getPublishTemppath())
//										 .append("/")
//										 .append(context.getSiteDir())
//										 .append("/")
//										 .append(((PageContext)context).getPagePath())
//										 .toString();
////			else
////			{
////				String t_fileName = this.context.getFileName();
////				htmlpath_temp = new StringBuffer(this.context.getPublishTemppath())
////										 .append("/")
////										 .append(context.getSiteDir())
////										 .append("/")
////							 			 .append(this.context.getRendPath())
////										 .append("/")
////										 .append(t_fileName)
////										 .toString();
////			}
//		}
//		else
//		{
////			if(context instanceof PageContext)
//				htmlpath_temp = new StringBuffer(this.context.getPreviewRootPath())
//										 .append("/")
//										 .append(((PageContext)context).getPagePath())
//						.toString();
////			else
////			{ 
////				String t_fileName = this.context.getFileName();
////				htmlpath_temp = new StringBuffer(this.context.getPreviewRootPath())
////				 .append("/")
////				 .append(this.context.getRendPath())
////				 .append("/")
////				 .append(t_fileName)
////				 .toString();
////			}
//		}
//	
//		/**
//		 * 保存到临时目录
//		 */
////		saveHtml(htmlpath_temp);
//		File html = FileUtil.createNewFile(htmlpath_temp);
//		FileWriter fileWriter = null;
//		try {
//			fileWriter = new FileWriter(html);
//
//			Template template = VelocityUtil
//					.getTemplate("publish/html_generator.vm");
//			org.apache.velocity.context.Context vcontext = new VelocityContext();
//			vcontext.put("htmlcontent", script.getJspFile().getContent());
//			template.merge(vcontext, fileWriter);
//
//		} catch (ResourceNotFoundException e) {
//
//			e.printStackTrace();
//		} catch (ParseErrorException e) {
//
//			e.printStackTrace();
//		} catch (MethodInvocationException e) {
//
//			e.printStackTrace();
//		} catch (IOException e1) {
//			throw e1;
//		} catch (Exception e) {
//
//			throw e;
//		} finally {
//			try {
//				if (fileWriter != null)
//					fileWriter.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//
//	}
	
	/**
	 * 判断是否需要执行发布任务
	 */
	public boolean needPublish(PublishCallBack callBack)
	{
		boolean flag = true;
		try {
			init();
		} catch (PublishException e) {
			flag = false;
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!flag)
		{
			callBack.getPublishMonitor().addFailedMessage("初始化页面发布上下文失败：" + this.context,new Date(),this.publisher);
			return false;
		}
		boolean ret = true;
		if(params != null)
		{
			if(params.length == 2)
				ret = this.context.getPublishMonitor().isPageTemplate(context,this.params[0]);
			else
				ret = this.context.getPublishMonitor().isPageTemplate(context,this.params[1]);
			if(ret)
				callBack.getPublishMonitor().addFailedMessage("" + this.context + "已经存在，或者无需发布！",new Date(),this.publisher);
			return !ret;
				
		}
		return true;
	}
	
	public String getId() {
		
		return ((PageContext)context).getPagePath();
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
			
			if(this.script != null)
			{
				JspFile jspFile = this.script.getJspFile();
				distribute.init(context, jspFile);
			}
			else
			{
				distribute.init(context, null);
				System.out.println("disttribute():script=null" );
			}
				
			distribute.before();
			/**
			 * 如果页面已经生成，则分发页面和页面相关的附件到临时目录
			 */
			if(this.pageType.equals(CMSLink.TYPE_STATIC_PAGE + "") 
					&& this.context.getPublishMonitor().isPageGenerated())
			{
				distribute.distribute();
			}
			else if(pageType.equals(CMSLink.TYPE_DIRECTORY + "") )
			{
				distribute.distribute();
			}
				
			distribute.after();
			context.getPublishMonitor().setPublishStatus(PublishMonitor.PUBLISH_COMPLETED);
		}
	    catch (DistributeException e) {	    	
			context.getPublishMonitor().setPublishStatus(PublishMonitor.DISTRIBUTE_FAILED);
		}
		finally
		{
			distribute.dofinally();
		}
		
	}

	


	/**
	 * 记录当前发布页面和页面上相关元素之间的关系,有具体的元素进行回调
	 */
	public void recordRecursivePubObj(String refobj, int reftype, String site) {		
		super.recordRecursivePublihsObject( this.pagecontext.getPagePath(),
											RecursivePublishManager.PUBOBJECTTYPE_PAGE, 
											this.context.getSite().getSecondName(),
											refobj,  
											reftype,  
											site);
	}

	public Set getRecursivePubObject() {
		try {
			Set publishObjects = CMSUtil.getCMSDriverConfiguration()
			 .getCMSService()
			 .getRecursivePublishManager()
			 .getAllPubObjectsOfPage(context,this.pagecontext.getPagePath());
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
			 .getRecursivePublishManager().deleteRefObjectsOfPubobject(context, this.pagecontext.getPagePath(),
												RecursivePublishManager.PUBOBJECTTYPE_PAGE);
		} catch (RecursivePublishException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DriverConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
