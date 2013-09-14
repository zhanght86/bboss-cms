package com.frameworkset.common.tag.html;

import java.io.IOException;

import javax.servlet.jsp.JspException;

import org.apache.ecs.html.A;
import org.apache.ecs.html.IMG;

import com.frameworkset.platform.cms.channelmanager.Channel;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.common.tag.CMSBaseTag;
import com.frameworkset.common.tag.CMSTagUtil;
import com.frameworkset.util.ValueObjectUtil;

public class ChannelInfoTag extends CMSBaseTag {

	protected String property = null;

	protected String displayname = null;

	// protected String site = null;
	protected String linktype = null;

	protected String imagestyle = null;
	/**
	 * 图片扩展脚步
	 */
	protected String imageextend;

	public String getImageextend() {
		return imageextend;
	}

	public void setImageextend(String imageextend) {
		this.imageextend = imageextend;
	}

	protected String target = "_self";

	public ChannelInfoTag() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CMSListTag findCMSListTag() {
		CMSListTag parent = (CMSListTag) findAncestorWithClass(this,
				CMSListTag.class);

		return parent;
	}

	public int doStartTag() throws JspException {
		super.doStartTag();
		try {

			if (site == null || "".equals(site)) {
				site = context.getSiteID();
			} else {
				// Site st = CMSUtil.getSiteCacheManager().getSiteByEname(site);
				// site = String.valueOf(st.getSiteId());
			}

			Channel chnl = getChannelInfo();

			if (chnl == null)
				return SKIP_BODY;

			StringBuffer outputStrng = new StringBuffer();

			if ("image".equals(linktype)) {
				if (property == null)
					property = "outlinepicture";
				Object v = ValueObjectUtil
						.getValue(chnl, property);
				String value =  v == null?null:String.valueOf(v);
				if (value == null)
					value = "频道的属性[" + property + "]未指定";
				outputStrng = new StringBuffer(value);
				if (CMSTagUtil.getPublishedLinkPath(context,
						outputStrng.toString()).endsWith(".swf")
						|| CMSTagUtil.getPublishedLinkPath(context,
								outputStrng.toString()).endsWith(".SWF")) {/*
																			 * 判断是图片还是flash
																			 * 判断是flash
																			 */
					try {
						outputStrng = flashTagShow(CMSTagUtil
								.getPublishedLinkPath(context, outputStrng
										.toString()));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					IMG img = new IMG();
					img.setSrc(CMSTagUtil.getPublishedLinkPath(context,
							outputStrng.toString()));
					if(imageextend != null)
						img.setExtend(imageextend);
					if (imagestyle != null)
						img.setStyle(imagestyle);
					outputStrng = new StringBuffer(img.toString());
				}
			} else if ("homepage".equals(linktype)) {
				if (property == null)
					property = "name";
				Object v = ValueObjectUtil
						.getValue(chnl, property);
				String value =  v == null?null:String.valueOf(v);
				if (value == null)
					value = "频道的属性[" + property + "]未指定";
				outputStrng = new StringBuffer(value);
				A a = new A();
				/* set target */
				if (target != null)
					a.setTarget(this.target);
				a.setHref(CMSTagUtil.getPublishedChannelPath(context, chnl));
				a.setTagText(outputStrng.toString());
				outputStrng = new StringBuffer(a.toString());
			} else if ("imagehomepage".equals(linktype)) {
				if (property == null)
					property = "outlinepicture";
				Object v = ValueObjectUtil
						.getValue(chnl, property);
				String value =  v == null?null:String.valueOf(v);
				if (value == null)
					value = "频道的属性[" + property + "]未指定";
				outputStrng = new StringBuffer(value);
				String swfLink = CMSUtil.getPublishedLinkPath(context,outputStrng.toString());
				if (swfLink.endsWith(".swf")
						|| swfLink.endsWith(".SWF")) {/*
													 * 判断是图片还是flash
													 * 判断是flash
													 */
					try {
						outputStrng = flashTagShow(swfLink);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					A a = new A();
					/* set target */
					a.setTarget(this.target);
					a
							.setHref(CMSTagUtil.getPublishedChannelPath(
									context, chnl));
					IMG img = new IMG();
					img.setSrc(CMSTagUtil.getPublishedLinkPath(context,
							outputStrng.toString()));
					if(imageextend != null)
						img.setExtend(imageextend);
					if (imagestyle != null)
						img.setStyle(imagestyle);
					a.setTagText(img.toString());
					outputStrng = new StringBuffer(a.toString());
				}
			} else {
				Object v = ValueObjectUtil
						.getValue(chnl, property);
				String value =  v == null?null:String.valueOf(v);
				if (value == null)
					value = "频道的属性[" + property + "]未指定";

				outputStrng = new StringBuffer(value);
			}

			out.print(outputStrng);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SKIP_BODY;
	}

	public String getDisplayname() {
		return displayname;
	}

	public void setDisplayname(String displayname) {
		this.displayname = displayname;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getImagestyle() {
		return imagestyle;
	}

	public void setImagestyle(String imagestyle) {
		this.imagestyle = imagestyle;
	}

	public String getLinktype() {
		return linktype;
	}

	public void setLinktype(String linktype) {
		this.linktype = linktype;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public int doEndTag() throws JspException {
		int ret = super.doEndTag();
		property = null;
		displayname = null;
		site = null;
		linktype = null;
		imagestyle = null;
		imageextend = null;
		return ret;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	/**
	 * 
	 * @param src
	 * @return
	 * @throws Exception
	 */
	public StringBuffer flashTagShow(String src) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb
				.append("<object classid=\"clsid:D27CDB6E-AE6D-11cf-96B8-444553540000\" \n");
		sb
				.append("codebase=\"http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,29,0\" \n");
		sb.append("style=\"" + imagestyle + "\" > \n");
		sb.append("<param name=\"movie\" value=\"" + src + "\"> \n");
		sb.append("<param name=\"quality\" value=\"high\"> \n");
		sb.append("<embed src=\"" + src + "\" style=\"" + imagestyle
				+ "\"  quality=\"high\" \n");
		sb
				.append("pluginspage=\"http://www.macromedia.com/go/getflashplayer\" type=\"application/x-shockwave-flash\"> \n");
		sb.append("</embed> \n");
		sb.append("</object> \n");

		return sb;
	}

	protected Channel getChannelInfo() {
		Channel chnl = null;
		try {

			if (displayname != null && !"".equals(displayname))

				chnl = CMSUtil.getChannelCacheManager(context.getSiteID())
						.getChannelByDisplayName(displayname);

			else {
				CMSListTag parent = this.findCMSListTag();
				if (parent == null) {
					chnl = CMSTagUtil.getCurrentChannel(context);
				} else {
					chnl = CMSUtil.getChannelCacheManager(context.getSiteID())
							.getChannelByDisplayName(parent.getChannel());
				}
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		return chnl;
	}

}
