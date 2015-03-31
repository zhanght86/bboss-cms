package com.frameworkset.common.tag.html;

import java.io.IOException;

import javax.servlet.jsp.JspException;

import org.apache.ecs.html.A;
import org.apache.ecs.html.IMG;

import com.frameworkset.platform.cms.channelmanager.Channel;
import com.frameworkset.platform.cms.sitemanager.SiteManagerImpl;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.common.tag.CMSSupportTag;
import com.frameworkset.common.tag.CMSTagUtil;

/**
 * RSS 链接标签
 * <p>Title: RssTag.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date Jul 5, 2007 5:54:42 PM
 * @author ge.tao
 * @version 1.0
 */
public class RssTag extends CMSSupportTag {
	protected String channel;
	
	/**
	 * 站点的英文名称
	 */
	protected String site;

	protected String rsslink;

	protected String rssimage;

	protected String target;

	protected String style;

	protected String classname;

	public int doStartTag() throws JspException {
		super.doStartTag();
		if (this.listTag != null && (this.channel == null || "".equals(this.channel))){// 放置在outline标签里面		
			if (!listTag.rssSeted()) {
				this.listTag.setRssTag(this);
			}
		} else {// 放置在outline标签外面
			try {
				out.print(this.generatorRssScript());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return SKIP_BODY;
	}

	public String generatorRssScript() {
		String result = "";
		/*
		 * 如果是外部指定的链接，需要记录地址，并且将外部地址设置为RSS链接地址
		 */
		if (getRsslink() != null) {
			A rssA = new A();
			if (this.context != null) {
				String templatePath = CMSTagUtil.getTemplatePath(context);
				rssA.setHref(CMSTagUtil.getPublishedLinkPath(context,templatePath, getRsslink()));
				if (getTarget() != null)
					rssA.setTarget(getTarget());
				if (getRssimage() == null) {
					rssA.setTagText("RSS" + "  " +rssA.getAttribute("href"));
				} else {
					IMG image = new IMG();
					String imgSrc = CMSTagUtil.getPublishedLinkPath(context,templatePath, getRssimage());
					image.setSrc(imgSrc);
					image.setBorder(0);
					rssA.setTagText(image.toString() + "  " + rssA.getAttribute("href"));
				}
			} else {
				rssA.setHref(getRsslink());
				if (getRssimage() == null) {
					rssA.setTagText("RSS" + "  " + rssA.getAttribute("href"));
				} else {
					IMG image = new IMG();
					String imgSrc = CMSTagUtil.getPublishedLinkPath(context,"", getRssimage());
					image.setSrc(imgSrc);
					image.setBorder(0);
					rssA.setTagText(image.toString() + "  " + rssA.getAttribute("href"));
				}
			}
			if (style != null && !"".equals(style))
				rssA.setStyle(style);
			if (classname != null && !"".equals(classname))
				rssA.setClass(classname);
			result = rssA.toString();
		}
		/**
		 * 指定了频道
		 */
		else if (getChannel() != null) {
			try {
				Channel channel = null;

				channel = CMSUtil.getChannelCacheManager(context.getSiteID()).getChannelByDisplayName(this.getChannel());

				if (channel == null) {
					System.out.println("站点['" + context.getSiteID()+ "']中显示名称为['" + getChannel() + "']的频道不存在。");
					return "";
				}
				/* 频道RSS种子路径 */
				String rssLink = "";
				String siteid = String.valueOf(new SiteManagerImpl().getSiteInfoBySiteName(this.site).getSiteId());
				//String tmp = CMSUtil.getChannelPublishTempPath(context, siteid, channel);
				String tmp = CMSUtil.getPublishedChannelDir(context,channel);
				rssLink = tmp + "/" + channel.getChannelId() + ".xml";
				CMSTagUtil.getPublishedChannelPath(context, channel);

				A rssA = new A();

				rssA.setHref(rssLink);
				if (getRssimage() == null) {
					rssA.setTagText("RSS" + "  " + rssA.getAttribute("href"));
				} else {
					String templatePath = CMSTagUtil.getTemplatePath(context);
					IMG image = new IMG();
					String imgSrc = CMSTagUtil.getPublishedLinkPath(context,templatePath, getRssimage());
					image.setSrc(imgSrc);
					image.setBorder(0);
					rssA.setTagText(image.toString() + "  " + rssA.getAttribute("href"));
				}
				if (getTarget() != null)
					rssA.setTarget(getTarget());

				if (style != null && !"".equals(style))
					rssA.setStyle(style);
				if (classname != null && !"".equals(classname))
					rssA.setClass(classname);
				result = rssA.toString();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		/**
		 * 从概览设置的频道中获取频道首页链接 概览标签外部指定了频道/从当前页面环境中中获取频道 放概览标签里面
		 */
		else {
//			String channelName = listTag.getChannel();
			try {
//				Channel channel = CMSUtil.getChannelCacheManager(context.getSiteID()).getChannelByDisplayName(channelName);
//				if (channel == null) {
//					System.out.println("站点['" + context.getSiteID()+ "']中显示名称为['" + getChannel() + "']的频道不存在。");
//					return "";
//				}
				/* listTag doStartTag时 保存RSS路径 */
				String rssLink = this.listTag.getRssPath();
				A rssA = new A();
				rssA.setHref(rssLink);

				if (getRssimage() == null) {
					rssA.setTagText("RSS" + "  " + rssA.getAttribute("href"));
				} else {
					String templatePath = CMSTagUtil.getTemplatePath(context);
					IMG image = new IMG();
					String imgSrc = CMSTagUtil.getPublishedLinkPath(context,templatePath, getRssimage());
					image.setSrc(imgSrc);
					image.setBorder(0);
					rssA.setTagText(image.toString() + "  " + rssA.getAttribute("href"));
				}
				if (getTarget() != null)
					rssA.setTarget(getTarget());

				if (style != null && !"".equals(style))
					rssA.setStyle(style);
				if (classname != null && !"".equals(classname)) 
					rssA.setClass(classname);

				result = rssA.toString();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
//		else{ // 概览标签从当前页面环境中中获取频道
//			String rssLink = this.listTag.getRssPath();
//			A rssA = new A();
//			rssA.setHref(rssLink);
//			if (getRssimage() == null) {
//				rssA.setTagText("RSS");
//			} else {
//				String templatePath = CMSTagUtil.getTemplatePath(context);
//				IMG image = new IMG();
//				String imgSrc = CMSTagUtil.getPublishedLinkPath(context,templatePath, getRssimage());
//				image.setSrc(imgSrc);
//				image.setBorder(0);
//				rssA.setTagText(image.toString());
//			}
//			if (getTarget() != null)
//				rssA.setTarget(getTarget());
//			if (style != null && !"".equals(style))
//				rssA.setStyle(style);
//			if (classname != null && !"".equals(classname))
//				rssA.setClass(classname);
//			result = rssA.toString();
//		}
		return result;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getRsslink() {
		return rsslink;
	}

	public void setRsslink(String rsslink) {
		this.rsslink = rsslink;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getRssimage() {
		return rssimage;
	}

	public void setRssimage(String rssimage) {
		this.rssimage = rssimage;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	@Override
	public void doFinally() {
		channel = null;
		
		/**
		 * 站点的英文名称
		 */
		 site = null;

		 rsslink = null;

		 rssimage = null;

		 target = null;

		 style = null;

		 classname = null;
		super.doFinally();
	}

}
