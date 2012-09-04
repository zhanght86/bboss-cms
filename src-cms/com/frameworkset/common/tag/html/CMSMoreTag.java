package com.frameworkset.common.tag.html;

import java.io.IOException;

import javax.servlet.jsp.JspException;

import org.apache.ecs.html.A;
import org.apache.ecs.html.IMG;

import com.frameworkset.platform.cms.channelmanager.Channel;
import com.frameworkset.platform.cms.channelmanager.ChannelCacheManager;
import com.frameworkset.platform.cms.driver.context.Context;
import com.frameworkset.platform.cms.driver.context.impl.DefaultContextImpl;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.common.tag.CMSSupportTag;
import com.frameworkset.common.tag.CMSTagUtil;

/**
 * 更多文本标签
 * 单独使用more标签
 * 可以指定site（站点英文名称），也可以不指定
 * 不指定的话就从发布上下中获取
 * 如果指定了，有两种情况，
 * 		一种是在模板中使用，
 *          一种是发布的上下文site==指定site
 *          一种是发布的上下文site!=指定site
 * 		一种是在动态页面中使用.
 * <p>Title: CMSMoreTag</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date 2007-4-11 20:30:23
 * @author biaoping.yin
 * @version 1.0
 */
public class CMSMoreTag extends CMSSupportTag {
	/**
	 * 文本方式的more标记
	 */
	protected String moretext;
	/**
	 * 手动设置的more链接
	 */
	protected String morelink;
	
	/**
	 * more链接弹出的窗口
	 */
	protected String target;
	/**
	 * 图片样式的more标签
	 */
	protected String moreimage;
	
	/**
	 * 手动指定的频道名称
	 */
	protected String channel;
	
	
	/**
	 * 定义样式
	 */
	protected String style;
	/**
	 * 定义样式类
	 */
	protected String classname;
	
	/**
	 * 手动指定的频道名称
	 */
	protected String a;
	
	protected String site;
	
	
	
	public int doStartTag() throws JspException
	{
		super.doStartTag();
		if(this.listTag != null && (this.channel==null || "".equals(this.channel)) )
		{
			
			if(!listTag.moreSeted())
			{
				
				this.listTag.setMoreTag(this);
				//把more标签放到outline标签的上下文
				this.context = listTag.getContext();
			}
			
		}
		else
		{
			try {
				if(site != null){
				    if(site.equals(context.getSite().getSecondName())){
				    	out.print(this.generatorMoreScript());
				    }else{
				    	Context _old = this.context;
				    	this.context = new DefaultContextImpl(request,response);
				    	((DefaultContextImpl)context).setSite(site);
				    	((DefaultContextImpl)context).setOldContext(_old);
				    	out.print(this.generatorMoreScript());
				    }
				}else{
					out.print(this.generatorMoreScript());
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return SKIP_BODY;
	}
	
	public int doEndTag()throws JspException{
		if(this.listTag != null){
			//在listTag里面执行
			//在回调完more.generatorMoreScript()后
			//执行more.clearParameter()
		}else{
			this.clearParameter();
		}
		return super.doEndTag();
	}
	
	public void clearParameter(){
		this.a = null;
		this.channel = null;
		this.channeldir = null;
		this.classname = null;
		this.moreimage = null;
		this.morelink = null;
		this.moretext = null;
		this.target = null;
		this.style = null;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getMoreimage() {
		return moreimage;
	}

	public void setMoreimage(String moreimage) {
		this.moreimage = moreimage;
	}

	public String getMorelink() {
		return morelink;
	}

	public void setMorelink(String morelink) {
		this.morelink = morelink;
	}

	public String getMoretext() {
		return moretext == null ? "更多":moretext;
	}

	public void setMoretext(String moretext) {
		this.moretext = moretext;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}
	
	
	/**
	 * 生成more链接脚本
	 * @param more
	 * @return
	 */
	public String generatorMoreScript()
	{
		String result = "";
		
		/*
		 * 如果是外部指定的链接，需要记录地址，并且将外部地址设置为more链接地址
		 */
		if(getMorelink() != null)
		{
			A moreA = new A();
			
			if(this.context != null)
			{
				String templatePath = CMSTagUtil.getTemplatePath(context);
//				String templatePath = "";
				moreA.setHref(CMSTagUtil.getPublishedLinkPath(context,templatePath,getMorelink()) );
				if(getTarget() != null)
					moreA.setTarget(getTarget());
				if(getMoreimage() == null)
				{
					moreA.setTagText(getMoretext());
				}
				else
				{
					IMG image = new IMG();
					String imgSrc = CMSTagUtil.getPublishedLinkPath(context,templatePath,getMoreimage());
					image.setSrc(imgSrc);
					image.setBorder(0);
					moreA.setTagText(image.toString());
				}
			}	
			else
			{
				moreA.setHref(getMorelink());
				if(getMoreimage() == null)
				{
					moreA.setTagText(getMoretext());
				}
				else
				{
					IMG image = new IMG();
					String imgSrc = CMSTagUtil.getPublishedLinkPath(context,"",getMoreimage());
					image.setSrc(imgSrc);
					image.setBorder(0);
					moreA.setTagText(image.toString());
				}
			}
				if (style!=null && !"".equals(style))
					moreA.setStyle(style);
				if (classname!=null && !"".equals(classname))
					moreA.setClass(classname);
			result = moreA.toString();
		}
		/**
		 * 将频道首页设置为more链接地址
		 */
		else if(getChannel() != null)
		{
			try {
				Channel channel = null;
				
//				channel = CMSUtil.getCMSDriverConfiguration().getCMSService().getChannelManager().getChannelInfoByDisplayName(context.getSiteID(),getChannel());
				
				channel = CMSUtil.getChannelCacheManager(context.getSiteID()).getChannelByDisplayName(this.getChannel());
				
				if(channel == null)
				{
					System.out.println("站点['" + context.getSiteID() + "']中显示名称为['" + getChannel() + "']的频道不存在。");
					return "";
				}
				String channelLink = CMSTagUtil.getPublishedChannelPath(context,channel);
				A moreA = new A();
				
				moreA.setHref(channelLink);
				if(getMoreimage() == null)
				{
					moreA.setTagText(getMoretext());
				}
				else
				{
					String templatePath = CMSTagUtil.getTemplatePath(context);
					IMG image = new IMG();
					String imgSrc = CMSTagUtil.getPublishedLinkPath(context,templatePath,getMoreimage());
					image.setSrc(imgSrc);
					image.setBorder(0);
					moreA.setTagText(image.toString());
				}
				if(getTarget() != null)
					moreA.setTarget(getTarget());

				if (style!=null && !"".equals(style))
					moreA.setStyle(style);
				if (classname!=null && !"".equals(classname))
					moreA.setClass(classname);
				result = moreA.toString();
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		/**
		 * 从概览设置的频道中获取频道首页链接
		 */
		else if(this.listTag != null 
				&& listTag.getChannel() != null)
		{
			String channelName = listTag.getChannel();
			try {
				
//				Channel channel = CMSUtil.getCMSDriverConfiguration()
//										 .getCMSService()
//										 .getChannelManager()
//										 .getChannelInfoByDisplayName(context.getSiteID(),channelName);
				
				String site_id = listTag.getContext().getSiteID();
				ChannelCacheManager ccm = CMSUtil.getChannelCacheManager(site_id);
				Channel channel = ccm.getChannelByDisplayName(channelName);
				if(channel == null)
				{
					System.out.println("站点['" + listTag.getContext().getSiteID() + "']中显示名称为['" + getChannel() + "']的频道不存在。");
					return "";
				}
				String channelLink  = CMSTagUtil.getPublishedChannelPath(listTag.getContext(),channel);
				A moreA = new A();
				
				moreA.setHref(channelLink);
				if(getMoreimage() == null)
				{
					moreA.setTagText(getMoretext());
				}
				else
				{
					String templatePath = CMSTagUtil.getTemplatePath(listTag.getContext());
					IMG image = new IMG();
					String imgSrc = CMSTagUtil.getPublishedLinkPath(listTag.getContext(),templatePath,getMoreimage());
					image.setSrc(imgSrc);
					image.setBorder(0);
					moreA.setTagText(image.toString());
				}
				if(getTarget() != null)
					moreA.setTarget(getTarget());

				if (style!=null && !"".equals(style))
					moreA.setStyle(style);
				if (classname!=null && !"".equals(classname))
					moreA.setClass(classname);
				
				result = moreA.toString();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
			
		}
		else  //从当前页面环境中中获取频道
		{
			
			String dir = CMSTagUtil.getPublishedChannelPath(context,CMSTagUtil.getCurrentChannel(context));
			A moreA = new A();
			moreA.setHref(dir);
			if(getMoreimage() == null)
			{
				moreA.setTagText(getMoretext());
			}
			else
			{
				String templatePath = CMSTagUtil.getTemplatePath(context);
				IMG image = new IMG();
				String imgSrc = CMSTagUtil.getPublishedLinkPath(context,templatePath,getMoreimage());
				image.setSrc(imgSrc);
				image.setBorder(0);
				moreA.setTagText(image.toString());
			}
			if(getTarget() != null)
				moreA.setTarget(getTarget());
			
			if (style!=null && !"".equals(style))
				moreA.setStyle(style);
			if (classname!=null && !"".equals(classname))
				moreA.setClass(classname);
			result = moreA.toString();
		}
		return result;
	}

	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}
	
}
