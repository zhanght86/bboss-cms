package com.frameworkset.common.tag;

import javax.servlet.jsp.JspException;

import com.frameworkset.common.tag.html.CMSListTag;
import com.frameworkset.common.tag.pager.tags.CellTag;
import com.frameworkset.platform.cms.channelmanager.Channel;
import com.frameworkset.platform.cms.channelmanager.ChannelManagerException;
import com.frameworkset.platform.cms.documentmanager.Document;
import com.frameworkset.platform.cms.driver.config.DriverConfigurationException;
import com.frameworkset.platform.cms.driver.context.ChannelContext;
import com.frameworkset.platform.cms.driver.context.Context;
import com.frameworkset.platform.cms.driver.context.impl.DefaultContextImpl;
import com.frameworkset.platform.cms.driver.jsp.CMSServletRequest;
import com.frameworkset.platform.cms.driver.jsp.InternalImplConverter;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.util.StringUtil;

/**
 * 单元格标签的公共基础类
 * <p>Title: BaseCellTag</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date 2007-4-11 8:31:33
 * @author biaoping.yin
 * @version 1.0
 */
public abstract class BaseCellTag extends CellTag {
	protected Context context = null;

	protected CMSListTag listTag;
//	protected boolean isCurrentChanel()
//	{
//		if(context instanceof ChannelContext)
//		{
//			
//		}
//		else
//		{
//			return false;
//		}
//	}
	
	protected String getCurrentChannelDir()
	{
		try {
			String currentChannelDir = CMSUtil.getChannelCacheManager(context.getSiteID()).getChannelByDisplayName(this.listTag.getChannel()).getChannelPath();
			return currentChannelDir;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new IllegalArgumentException("获取[站点："+context.getSite().getName()+",频道："+this.listTag.getChannel()+"]当前文档隶属频道路径失败："+StringUtil.formatException(e));
		}
		
	}
	
	private void setContext()
	{
		if(context == null)
		{
		
			CMSServletRequest cmsrequest = InternalImplConverter.getInternalRequest(this.request);
			if(cmsrequest != null)
			{
				this.context = (Context)cmsrequest.getContext();
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
	}
	
	
//	public void setPageContext(PageContext pageContext)
//	{
//		super.setPageContext(pageContext);
//		cmsrequest = InternalImplConverter.getInternalRequest(this.request);
//		cmsresponse = InternalImplConverter.getInternalResponse(this.response);
//		if(cmsrequest != null)
//		{
//			this.context = this.cmsrequest.getContext();
//		}
//		
//		/**
//		 * 如果context 不存在，表示当前运行的环境不是系统发布的环境，则采用缺省的上下文环境以便
//		 * 系统能够正常的运转
//		 */
//		if(this.context == null)
//		{
//			
//			context = new DefaultContextImpl(this.request,response);
//		}
//		
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
//	
	public int doStartTag() throws JspException 
	{
		
			
		init();
		if(this.dataSet != null )
		{
			if(dataSet instanceof CMSListTag)
			{
				listTag = (CMSListTag)this.dataSet;
				context = listTag.getContext();
			}
			else
			{
				setContext();
				
			}
			
			
//			if(this.context instanceof DefaultContextImpl)
//			{
//				((DefaultContextImpl)context).setSite(listTag.getSite());
//			}
		}
		else
		{
			setContext();
		}
		
		
		
		
		
		
			
		
		return SKIP_BODY;
	}
	
	protected String getSimpleText(String origine)
	{
		if(this.maxlength > 0 && origine != null && origine.length() > maxlength)
		{
			origine = origine.substring(0,this.maxlength);
			if(replace != null)
				origine += replace;
		}
		return origine;
	}
	
	/**
	 * 获取文档发布到页面上的链接地址
	 * @param context
	 * @return
	 */
	protected String getContentPath(Context context)
	{
		Document document = (Document)dataSet.getOrigineObject();

		
		
		try {
			

			return CMSTagUtil.getPublishedContentPath(context,document);
		} catch (Exception e) {
			
			e.printStackTrace();
		} 
			
		return "#";
		
	}
	
	
	/**
	 * 根据文档id和文档所属的频道获取文档的发布页面地址
	 * @param context
	 * @return
	 */
	protected String getContentPath(String content_id,String channel_id)
	{
//		String content_id = dataSet.getString("document_id");
//		
//		String t_channel = dataSet.getString("chanel_id");
		
		
		try {
			
			Channel channel = CMSUtil.getCMSDriverConfiguration()
									.getCMSService()
									.getChannelManager()
									.getChannelInfo(channel_id);
			
			String t_channeldir = channel.getChannelPath();
			
			return CMSTagUtil.getPublishedContentPath(context,t_channeldir,content_id);
		} catch (DriverConfigurationException e) {
			
			e.printStackTrace();
		} catch (ChannelManagerException e) {
			
			e.printStackTrace();
		}
			
		return "#";
		
	}
	
	public int doEndTag() throws JspException
	{
		int ret = super.doEndTag();
		context = null;
		
//		cmsrequest = null;
//		cmsresponse = null;
//		channeldir = null ;
//		channel = null ;
		listTag = null;
		dataSet = null;
		return ret;
	}

	
}
