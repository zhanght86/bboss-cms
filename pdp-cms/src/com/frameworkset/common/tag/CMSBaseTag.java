package com.frameworkset.common.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import com.frameworkset.platform.cms.channelmanager.Channel;
import com.frameworkset.platform.cms.driver.context.Context;
import com.frameworkset.platform.cms.driver.context.impl.DefaultContextImpl;
import com.frameworkset.platform.cms.driver.jsp.CMSServletRequest;
import com.frameworkset.platform.cms.driver.jsp.CMSServletResponse;
import com.frameworkset.platform.cms.driver.jsp.InternalImplConverter;
import com.frameworkset.platform.cms.sitemanager.Site;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.util.StringUtil;


/**
 * 内容管理标签库的基类
 * <p>Title: CMSBaseTag</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: bbossgroups</p>
 * @Date 2007-4-10 15:30:44
 * @author biaoping.yin
 * @version 1.0
 */
public class CMSBaseTag extends BaseTag  {
	
	protected Context context = null;
	protected CMSServletRequest cmsrequest;
	protected CMSServletResponse cmsresponse;
	protected String channeldir ;
	protected String channel ;
	protected String site;
	
	
	
	public void setPageContext(PageContext pageContext)
	{
		super.setPageContext(pageContext);
		cmsrequest = InternalImplConverter.getInternalRequest(this.request);
		cmsresponse = InternalImplConverter.getInternalResponse(this.response);
		if(cmsrequest != null)
		{
			this.context = (Context)this.cmsrequest.getContext();
		}
		

		/**
		 * 如果context 不存在，表示当前运行的环境不是系统发布的环境，则采用缺省的上下文环境以便
		 * 系统能够正常的运转
		 */
		if(this.context == null)
		{
			context = new DefaultContextImpl(request,response);
			
		}
	}

	public int doStartTag() throws JspException
	{
		
		if(context instanceof DefaultContextImpl)
		{
//			if(site == null || site.equals(""))
//				throw new JspException("没有指定站点名称，请检查对应的模板和文件是否设置了site属性。");
			
			if(site != null)
				((DefaultContextImpl)context).setSite(site);
			else
			{
				String siteId = request.getParameter("siteId");
				if(!StringUtil.isEmpty(siteId))
				{
					try {
						Site  site_ = CMSUtil.getSiteCacheManager().getSite(siteId);
						site = site_.getSecondName();
						((DefaultContextImpl)context).setSite(site);
					} catch (Exception e) {
						
						e.printStackTrace();
					}
				}
				
			}
			
			if(channel == null)
			{
				String channelid = request.getParameter("channelId");
				if(!StringUtil.isEmpty(channelid))
				{
					try {
						Channel  channel_ = CMSUtil.getChannelCacheManager(context.getSiteID()).getChannel(channelid);
						channel = channel_.getDisplayName();
					} catch (Exception e) {
						
						e.printStackTrace();
					}
				}
			}
		}
		
		return EVAL_BODY_INCLUDE;
	}

	public String getSite() {
		return site;
	}



	public void setSite(String site) {
		this.site = site;
	}
	
	public int doEndTag() throws JspException{
		
		return super.doEndTag();
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	@Override
	public void doFinally() {
		context = null;
		cmsrequest = null;
		cmsresponse = null;
		channeldir = null;
		channel = null;
		site = null;
		super.doFinally();
	}
	
//	/**
//	 * 获取当前发布任务的目的地地址
//	 * @return
//	 */
//	public String getCurrentPublishPath()
//	{
//		if(this.context == null)
//			return "";
//		return context.getPublishPath();
//	}
//	
//	/**
//	 * 获取文档的原始路径
//	 * @param channeldir
//	 * @param contentid
//	 * @return
//	 */
//	public String getContentPath(String channeldir,String contentid)
//	{
//		String fileName = CMSUtil.getContentFileName(contentid);
//		
//		return CMSUtil.getPath(channeldir,fileName);
//	}
//	
	
	
	
//	/**
//	 * 获取文档的发布页面路径，无需记录文档发布页面地址
//	 * @param channeldir
//	 * @param contentid
//	 * @return
//	 */
//	public String getPublishedContentPath(String channeldir,String contentid)
//	{
//		String originePath = getContentPath(channeldir,contentid);
//		String currentPublishPath = this.getCurrentPublishPath();
//		
//		return CMSUtil.getSimplePathFromfullPath(currentPublishPath,originePath);
//	}
	
//	/**
//	 * 获取频道首页的路径，无需记录频道路径
//	 * @param channeldir
//	 * @param contentid
//	 * @return
//	 */
//	public String getPublishedChannelPath(String channelpath)
//	{
//		
//		String currentPublishPath = this.getCurrentPublishPath();
//		
//		return CMSUtil.getSimplePathFromfullPath(currentPublishPath,channelpath);
//	}
	
//	/**
//	 * 获取链接文件发布后的路径,并且记录需要发布的链接附件
//	 * @param templatePath
//	 * @param linkPath
//	 * @return
//	 */
//	public String getPublishedLinkPath(String templatePath,String linkPath)
//	{
//		if(context == null)
//			return linkPath;
//		CmsTagLinkProcessor processor = new CmsTagLinkProcessor(context,templatePath);
//		processor.setHandletype(CmsTagLinkProcessor.PROCESS_TEMPLATE);
//		CMSLink templink = processor.processHref(linkPath);	
//		return templink.getHref();
//	}
	
//	/**
//	 * 获取链接文件发布后的路径,并且记录需要发布的链接附件
//	 * @param linkPath
//	 * @return
//	 */
//	public String getPublishedLinkPath(String linkPath)
//	{
//		
//		return getPublishedLinkPath("",linkPath);
//	}
	
	
//	/**
//	 * 处理页面上的超链接地址 
//	 * @param href
//	 * @return
//	 */
//	protected String handleHref(String href)
//	{
//		return href;
//	}
//	
//	/**
//	 * 处理页面上图片地址
//	 * @param imageUrl
//	 * @return
//	 */
//	public String handleImage(String imageUrl) {
//		
//		return imageUrl;
//	}
//	
//	/**
//	 * 处理页面中的link标签地址
//	 * @param linkUrl
//	 * @return
//	 */
//	public String handleLink(String linkUrl) {
//		
//		return linkUrl;
//	}
//	
//	/**
//	 * 处理页面中的javascript文件地址
//	 * @param scriptUrl
//	 * @return
//	 */
//	public String handleScript(String scriptUrl) {
//		
//		return scriptUrl;
//	}
	
//	/**
//	 * 获取当前发布任务的模版信息
//	 * @param context
//	 * @return
//	 */
//	protected String getTemplatePath(Context context)
//	{
//		if(context instanceof ContentContext)
//		{
//			return ((ContentContext)context).getDetailTemplate().getTemplatePath();
//		}
//		else if(context instanceof com.frameworkset.platform.cms.driver.context.PageContext)
//		{
//			return ((com.frameworkset.platform.cms.driver.context.PageContext)context).getPageDir();
//		}
//		else if(context instanceof CMSContext)
//		{
//			return ((CMSContext)context).getIndexTemplate().getTemplatePath();
//		}
//		else if(context instanceof ChannelContext)
//		{
//			return ((ChannelContext)context).getOutlineTemplate().getTemplatePath();
//		}
//		else
//		{
//			return "";
//		}
//			
//	}
	

	
	
}
