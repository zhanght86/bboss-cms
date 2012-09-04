package com.frameworkset.common.tag;

import com.frameworkset.platform.cms.util.CMSUtil;

public class CMSTagUtil extends CMSUtil{
	
//	public static String getTemplatePath(Context context)
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
//	
//	/**
//	 * 获取当前发布的目的地地址
//	 * @return
//	 */
//	public static String getCurrentPublishPath(Context context)
//	{
//		if(context == null)
//			return "";
//		return context.getPublishPath();
//	}
//	
//	/**
//	 * 获取文档的原始页面路径
//	 * @param channeldir
//	 * @param contentid
//	 * @return
//	 */
//	public static String getContentPath(Context context,String channeldir,String contentid)
//	{
//		String fileName = CMSUtil.getContentFileName(contentid);
//		
//		return CMSUtil.getPath(channeldir,fileName);
//	}
//	
//	
//	/**
//	 * 获取文档的原始路径
//	 * @param channeldir
//	 * @param contentid
//	 * @return
//	 */
//	public static String getPublishedContentPath(Context context,String channeldir,String contentid)
//	{
//		String originePath = getContentPath(context,channeldir,contentid);
//		
//		if(context instanceof DefaultContextImpl)
//		{
//			return ((DefaultContextImpl)context).getPublishedLinkPath(originePath);
//		}
//		else
//		{
//			String currentPublishPath = getCurrentPublishPath(context);
//			
//			return CMSUtil.getSimplePathFromfullPath(currentPublishPath,originePath);
//		}
//		
//	}
//	
//	
//	/**
//	 * 获取频道首页的路径
//	 * @param channeldir
//	 * @param contentid
//	 * @return
//	 */
//	public static String getPublishedChannelPath(Context context,String channelpath)
//	{
//		if(context instanceof DefaultContextImpl)
//		{
//			return ((DefaultContextImpl)context).getPublishedLinkPath(channelpath);
//		}
//		else
//		{
//			String currentPublishPath = getCurrentPublishPath(context);
//			
//			return CMSUtil.getSimplePathFromfullPath(currentPublishPath,channelpath);
//		}
//	}
//	
//	/**
//	 * 获取频道首页的路径
//	 * @param channeldir
//	 * @param contentid
//	 * @return
//	 */
//	public static String getPublishedSitePath(Context context,String sitePath)
//	{
//		if(context instanceof DefaultContextImpl)
//		{
//			return ((DefaultContextImpl)context).getPublishedLinkPath(sitePath);
//		}
//		else
//		{
//			String currentPublishPath = "";
//			
//			return CMSUtil.getSimplePathFromfullPath(currentPublishPath,sitePath);
//		}
//	}
//	
//	/**
//	 * 获取链接文件发布后的路径
//	 * @param templatePath
//	 * @param linkPath
//	 * @return
//	 */
//	public static String getPublishedLinkPath(Context context,String templatePath,String linkPath)
//	{
//		if(context == null)
//			return linkPath;
//		if(context instanceof DefaultContextImpl)
//		{
//			return ((DefaultContextImpl)context).getPublishedLinkPath(linkPath);
//		}
//		else
//		{
//			CmsTagLinkProcessor processor = new CmsTagLinkProcessor(context,templatePath);
//			processor.setHandletype(CmsTagLinkProcessor.PROCESS_TEMPLATE);
//			CMSLink templink = processor.processHref(linkPath);	
//			return templink.getHref();
//		}
//	}
//	
//	/**
//	 * 获取原始的频道路径链接
//	 * @param context
//	 * @return
//	 */
//	public static String getChannelPath(Context context)
//	{
//		if(context instanceof ContentContext)
//		{
//			ContentContext contentContext = (ContentContext)context;
//		
//			String dir = contentContext.getChannel().getChannelPath();
//			dir += "/"+contentContext.getChannel().getPubFileName() + "." + contentContext.getChannel().getPubFileNameSuffix();
//			return dir;
//		}
//		else if(context instanceof com.frameworkset.platform.cms.driver.context.PageContext)
//		{
//			return "";
//		}
//		else if(context instanceof CMSContext)
//		{
//			return "";
//		}
//		else if(context instanceof ChannelContext)
//		{
//			String dir = ((ChannelContext)context).getChannelPath();
//			dir += "/" + ((ChannelContext)context).getChannel().getPubFileName() + "." + ((ChannelContext)context).getChannel().getPubFileNameSuffix();
//			return dir;
//			
//		}
//		else
//		{
//			return "";
//		}
//			
//	}
//	
//	
//	/**
//	 * 获取原始的频道路径链接
//	 * @param context
//	 * @return
//	 */
//	public static String getChannelDispalyName(Context context)
//	{
//
//		if(context instanceof ContentContext)
//		{
//			ContentContext contentContext = (ContentContext)context;
//
//			return contentContext.getChannel().getDisplayName();
//		}
//		else if(context instanceof com.frameworkset.platform.cms.driver.context.PageContext)
//		{
//			return "";
//		}
//		else if(context instanceof CMSContext)
//		{
//			return "";
//		}
//		else if(context instanceof ChannelContext)
//		{
//			
//			return ((ChannelContext)context).getChannel().getDisplayName();
//			
//		}
//		else
//		{
//			return "";
//		}
//			
//	}
//	
//	
//	/**
//	 * 获取链接文件发布后的路径,并且记录需要发布的链接附件
//	 * @param linkPath
//	 * @return
//	 */
//	public static String getPublishedLinkPath(Context context,String linkPath)
//	{
//		
//		return getPublishedLinkPath(context,"",linkPath);
//	}
//	
//	
//	/**
//	 * 获取内容管理概览缺省的数据获取接口
//	 * @return
//	 */
//	public static CMSBaseListData getCMSBaseListData()
//	{
//		return new CMSDefaultListData();
//	}
//	
//	/**
//	 * 获取当前发布任务所属的频道
//	 * @param context
//	 * @return
//	 */
//	public static Channel getCurrentChannel(Context context)
//	{
//		if(context instanceof ContentContext)
//		{
//			ContentContext contentContext = (ContentContext)context;
//
//			return contentContext.getChannel();
//		}
//		else if(context instanceof com.frameworkset.platform.cms.driver.context.PageContext)
//		{
//			return null;
//		}
//		else if(context instanceof CMSContext)
//		{
//			return null;
//		}
//		else if(context instanceof ChannelContext)
//		{
//			
//			return ((ChannelContext)context).getChannel();
//			
//		}
//		else
//		{
//			return null;
//		}
//	}
//	
//	/**
//	 * 加载模板
//	 * @param templateUrl 模板路径
//	 * @param vcontext
//	 */
//	public String loadTemplate(String templateUrl,org.apache.velocity.context.Context vcontext){
//		StringWriter writer = new StringWriter();
//		Template template = VelocityUtil.getTemplate(templateUrl);
//		try {
//		    template.merge(vcontext,writer);		    
//		} catch (ResourceNotFoundException e) {
//		     e.printStackTrace();
//		} catch (ParseErrorException e) {
//		     e.printStackTrace();
//		} catch (MethodInvocationException e) {
//		     e.printStackTrace();
//		} catch (Exception e) {
//		     e.printStackTrace();
//		}
//		return writer.toString();
//	}
	
	

}
