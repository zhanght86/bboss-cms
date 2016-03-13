package com.frameworkset.common.tag.html;

import javax.servlet.jsp.JspException;

import com.frameworkset.common.ecs.A;
import com.frameworkset.common.tag.BaseCellTag;
import com.frameworkset.platform.cms.channelmanager.Channel;
import com.frameworkset.platform.cms.driver.context.Context;
import com.frameworkset.platform.cms.util.CMSUtil;
/**
 * 在概览标签中使用,可以获取文档和频道混合列表
 * <p>Title: ChannelIndexTag.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date Jun 6, 2005 10:24:42 PM
 * @author ge.tao -- modify
 * @version 1.0
 */
public class ChannelIndexTag extends BaseCellTag {

	String style = "";

	String type = "channelindex";
	
	String mouseOverEvent = "";
	
	String property;

	public int doStartTag() throws JspException {
		super.doStartTag();
        
		if(property == null){//没有指定, 就显示频道名称
			property = "name";
		}
		try {
			if (super.dataSet != null) {
			
			
				if ("channelindex".equals(type)) {
					A a = new A();
//					StringBuilder a = new StringBuilder();
//					a.append("<a");
//					a.append(" href=\"").append(getPublishedChannelPath(context)).append("\"");
//					a.append(" style=\"").append(style).append("\"");
//					a.append(" onMouseOver=\"").append(mouseOverEvent).append("\">");
//					a.append(getSimpleText(super.dataSet
//							.getString(property))).append("</a>");
					a.setHref(getPublishedChannelPath(context));
					a.setTagText(getSimpleText(super.dataSet
									.getString(property)));
					a.setStyle(style);
					a.setOnMouseOver(mouseOverEvent);
					out.print(a.toString());
				}
				if ("refchannel".equals(type)) {
					
					Channel _channel = CMSUtil.getCMSDriverConfiguration()
											  .getCMSService()
											  .getChannelManager()
											  .getChannelOfDocument(this.dataSet.getString("document_id"));
					if (!CMSUtil.getCMSDriverConfiguration()
							  .getCMSService()
							  .getChannelManager()
							  .doesHaveHomePageOfChnl(_channel))
						return this.SKIP_BODY;
					
					A a = new A();
					a.setHref(CMSUtil.getPublishedChannelPath(context,_channel));
					a.setTagText(getSimpleText(_channel.getName()));
					a.setStyle(style);
					a.setOnMouseOver(mouseOverEvent);
					
//					StringBuilder a = new StringBuilder();
//					a.append("<a");
//					a.append(" href=\"").append(CMSUtil.getPublishedChannelPath(context,_channel)).append("\"");
//					a.append(" style=\"").append(style).append("\"");
//					a.append(" onMouseOver=\"").append(mouseOverEvent).append("\">");
//					a.append(getSimpleText(_channel.getName())).append("</a>");
					out.print(a.toString());
				}
			
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return this.SKIP_BODY;

	}

	public String getStyle() {
		return style;
	}
	
//	public String getContentPath(String channedir,String documentid)
//	{
//		return CMSUtil.getPublishedContentPath(context,channedir,documentid);
//	}

	public String getPublishedChannelPath(Context context) {
//		String channelpath = "";
		Channel channel = (Channel)dataSet.getOrigineObject();
		return CMSUtil.getPublishedChannelPath(context,channel);
//		if (dataSet.getInt("pageflag") == 0) // 模版首页模版方式
//		{
//			channelpath = CMSUtil.getPath(dataSet.getString("channelPath"),
//					dataSet.getString("pubFileName"));
//		} else if (dataSet.getInt("pageflag") == 1) // 指定页面为首页
//		{
//			channelpath = dataSet.getString("indexpagepath");
//			if(CMSUtil.isExternalUrl(channelpath))
//				return channelpath;
//		} else // 指定文档细览页面为首页
//		{
//			channelpath = CMSUtil.getContentPath(context,dataSet.getString("channelPath"),
//												dataSet
//														.getString("indexpagepath"));
//			if(CMSUtil.isExternalUrl(channelpath))
//				return channelpath;
//		}
//		if (context instanceof DefaultContextImpl) {
//			return ((DefaultContextImpl) context)
//					.getPublishedLinkPath(channelpath);
//		} else {
//			if(channelpath.toLowerCase().startsWith("http://") 
//					|| channelpath.toLowerCase().startsWith("https://"))
//				return channelpath;
//			String currentPublishPath = CMSTagUtil
//					.getCurrentPublishPath(context);
//
//			return CMSUtil.getSimplePathFromfullPath(currentPublishPath,
//					channelpath);
//		}
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMouseOverEvent() {
		return mouseOverEvent;
	}

	public void setMouseOverEvent(String mouseOverEvent) {
		this.mouseOverEvent = mouseOverEvent;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	@Override
	public void doFinally() {
		  style = "";

		  type = "channelindex";
		
		  mouseOverEvent = "";
		
		  property = null;
		super.doFinally();
	}

}
