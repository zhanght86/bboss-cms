package com.frameworkset.common.tag.html;

import javax.servlet.jsp.JspException;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.Context;

import com.frameworkset.platform.cms.channelmanager.Channel;
import com.frameworkset.platform.cms.channelmanager.ChannelManagerException;
import com.frameworkset.platform.cms.channelmanager.ChannelManagerImpl;
import com.frameworkset.platform.cms.driver.context.ChannelContext;
import com.frameworkset.platform.cms.driver.context.impl.DefaultContextImpl;
import com.frameworkset.platform.cms.driver.publish.RecursivePublishManager;
import com.frameworkset.platform.cms.sitemanager.Site;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.common.tag.CMSBaseTag;
import com.frameworkset.common.tag.CMSTagUtil;

public class CalendarTag extends CMSBaseTag {
	// private CMSTagUtil tagUtil = new CMSTagUtil();
	private ChannelManagerImpl impl = new ChannelManagerImpl();

	private String channel = "";

	private String yearWidth = "35";

	private String monthWidth = "20";

	private String YMHeight = "15";

	private String tdHeight = "18";

	private String totalHeight = "200";

	private String fontsize = "9";

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	/**
	 * 生成日历标签的HTML代码. 加载外部的模板.vm文件,辅助生成HTML
	 * 
	 * @return String
	 * @throws ChannelManagerException
	 *             String CalendarTag.java 陶格
	 */
	public String calendarShow() throws ChannelManagerException {
		String rootPath = "";
		Channel channelObj = null;
		Site site = context.getSite();

		String site_id = String.valueOf(site.getSiteId());
		Context content = new VelocityContext();
		rootPath = super.request.getContextPath();
		content.put("site_id", site_id);
		content.put("rootPath", rootPath);
		content.put("channel", this.channel);
		content.put("publishPath", "czxwlb/");
		if (this.channel.length() > 0) {
			try {
				channelObj = CMSUtil.getChannelCacheManager(site_id)
						.getChannelByDisplayName(this.channel);
				String publishPath = CMSUtil.getPublishedChannelDir(context,channelObj);
				content.put("publishPath", publishPath);
			} catch (Exception e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
			}

		}
		/* 外观设置 */
		content.put("yearWidth", this.yearWidth);
		content.put("monthWidth", this.monthWidth);
		content.put("YMHeight", this.YMHeight);
		content.put("tdHeight", this.tdHeight);
		content.put("totalHeight", this.totalHeight);
		content.put("fontsize", this.fontsize);
		return CMSTagUtil.loadTemplate("publish/calendar/otherElement.vm",	content);
	}

	public int doStartTag() throws JspException {
		/* 注册频道 */
		if (context != null) {
//			if(context instanceof DefaultContextImpl){}
//			else{
//			
//						context.recordRecursivePubObj(this.getChannel(),
//								                      RecursivePublishManager.REFOBJECTTYPE_CHANNEL_ANSESTOR, 
//						                      context.getSite().getSecondName());
//			
//			}
		}
		return super.doStartTag();
	}

	public int doEndTag() throws JspException {
		try {
			pageContext.getOut().println(calendarShow());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return super.doEndTag();
	}

	public String getFontsize() {
		return fontsize;
	}

	public void setFontsize(String fontsize) {
		this.fontsize = fontsize;
	}

	public String getMonthWidth() {
		return monthWidth;
	}

	public void setMonthWidth(String monthWidth) {
		this.monthWidth = monthWidth;
	}

	public String getTdHeight() {
		return tdHeight;
	}

	public void setTdHeight(String tdHeight) {
		this.tdHeight = tdHeight;
	}

	public String getTotalHeight() {
		return totalHeight;
	}

	public void setTotalHeight(String totalHeight) {
		this.totalHeight = totalHeight;
	}

	public String getYearWidth() {
		return yearWidth;
	}

	public void setYearWidth(String yearWidth) {
		this.yearWidth = yearWidth;
	}

	public String getYMHeight() {
		return YMHeight;
	}

	public void setYMHeight(String height) {
		YMHeight = height;
	}

}
