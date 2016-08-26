package com.frameworkset.common.tag.html;

import java.util.List;

import javax.servlet.jsp.JspException;

import com.frameworkset.platform.cms.channelmanager.Channel;
import com.frameworkset.platform.cms.channelmanager.ChannelManagerImpl;
import com.frameworkset.platform.cms.sitemanager.SiteManagerImpl;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.common.tag.CMSBaseTag;
import com.frameworkset.common.tag.CMSTagUtil;

public class SiteNaviMapTag extends CMSBaseTag {

	String site = "";
	String channel = null;
	String width = "";
	String border = "";
	String align = "";
	String style = "";
	String classname = "";
	String outputStrng = "";
	
	public SiteNaviMapTag() {
		super();
		// TODO Auto-generated constructor stub
	}

	private boolean doExistSubNaviChnl(String parentChnlID,boolean doParent){
		ChannelManagerImpl chnlMgr = new ChannelManagerImpl();
		try{
			List chnlList = chnlMgr.getDirectSubChannels(parentChnlID);
			if (chnlList.size()>0 && doParent)
				return true;
			
			for(int i=0;i<chnlList.size();i++){
				Channel chnl  = (Channel)chnlList.get(i);
				if (!chnl.isNavigator()){
					continue;
				}
				if(doExistSubNaviChnl(String.valueOf(chnl.getChannelId()),true)){
					return true;
				}
			}
			return false;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	private boolean drwIfNoSubNaviChnl(String parantChnlID){
		try{
			ChannelManagerImpl chnlMgr = new ChannelManagerImpl();
			List chnlList = chnlMgr.getDirectSubChannels(parantChnlID);
			
			for(int i=0;i<chnlList.size();i++){
				Channel chnl  = (Channel)chnlList.get(i);
				if (!chnl.isNavigator()){
					continue;
				}
				String href = CMSTagUtil.getPublishedChannelPath(context,chnl);
				outputStrng += "<a href=\""+href+"\" style=\""+style+"\" class=\""+classname+"\">"+chnl.getName()+"</a>"+" ";
			}
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	private boolean drawSubChnlTbl(String chnlID,boolean doParent){
		ChannelManagerImpl chnlMgr = new ChannelManagerImpl();
		try{
			if (!doExistSubNaviChnl(chnlID, false)){
				drwIfNoSubNaviChnl(chnlID);
				return true;
			}
			List chnlList = chnlMgr.getDirectSubChannels(chnlID);
			if (doParent)
				outputStrng += "\n<table cellspacing=\"0\" cellpadding=\"0\" border=\""+border+"\">";
			
			for(int i=0;i<chnlList.size();i++){
				Channel chnl  = (Channel)chnlList.get(i);
				if (!chnl.isNavigator()){
					drawSubChnlTbl(String.valueOf(chnl.getChannelId()),false);
					continue;
				}
				String href = CMSTagUtil.getPublishedChannelPath(context,chnl);
				outputStrng += "<tr><td width=\""+width+"\" align=\""+align+"\">";
				outputStrng += "<a href=\""+href+"\" style=\""+style+"\" class=\""+classname+"\">"+chnl.getName()+"</a>";
				outputStrng += "</td><td>";
				drawSubChnlTbl(String.valueOf(chnl.getChannelId()),true);
				outputStrng += "</td></tr>";
			}
			if (doParent)
				outputStrng += "</table>";
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	public int doStartTag() throws JspException{

		if (border==null || "".equals(border))
			border = "0";
		
		try {
			
			if(channel!=null && !"".equals(channel)){
				ChannelManagerImpl chnlMgr = new ChannelManagerImpl();
				Channel chnl = chnlMgr.getChannelInfoByDisplayName(context.getSiteID(),channel);
				
				outputStrng = "\n<table cellspacing=\"0\" cellpadding=\"0\" border=\""+border+"\">";
				String href = CMSTagUtil.getPublishedChannelPath(context,chnl);
				outputStrng += "<tr><td width=\""+width+"\" align=\""+align+"\">";
				outputStrng += "<a href=\""+href+"\" style=\""+style+"\" class=\""+classname+"\">"+chnl.getName()+"</a>";
				outputStrng += "</td><td>";
				drawSubChnlTbl(String.valueOf(chnl.getChannelId()),true);
				outputStrng += "</td></tr></table>";
			}
			else if (site!=null && !"".equals(site)){
				site = String.valueOf(CMSUtil.getSiteCacheManager().getSiteByEname(site).getSiteId());
				SiteManagerImpl siteMngr = new SiteManagerImpl();
				List chnlList = siteMngr.getDirectChannelsOfSite(site);
				outputStrng = "\n<table cellspacing=\"0\" cellpadding=\"0\" border=\""+border+"\">";
			
				for(int i=0;i<chnlList.size();i++){
					Channel chnl  = (Channel)chnlList.get(i);
					if (!chnl.isNavigator()){
						drawSubChnlTbl(String.valueOf(chnl.getChannelId()),false);
						continue;
					}
					String href = CMSTagUtil.getPublishedChannelPath(context,chnl);
					outputStrng += "<tr><td align=\""+align+"\">";
					outputStrng += "<a href=\""+href+"\"  style=\""+style+"\" class=\""+classname+"\">"+chnl.getName()+"</a>";
					outputStrng += "</td><td>";
					drawSubChnlTbl(String.valueOf(chnl.getChannelId()),true);
					outputStrng += "</td></tr>";
				}
				outputStrng += "</table>";
			}
			else{
				ChannelManagerImpl chnlMgr = new ChannelManagerImpl();
				Channel chnl = CMSTagUtil.getCurrentChannel(context);
				
				outputStrng = "\n<table  border=\""+border+"\" cellspacing=\"0\" cellpadding=\"0\" >";
				String href = CMSTagUtil.getPublishedChannelPath(context,chnl);
				outputStrng += "<tr><td width=\""+width+"\" align=\""+align+"\">";
				outputStrng += "<a href=\""+href+"\" style=\""+style+"\" class=\""+classname+"\">"+chnl.getName()+"</a>";
				outputStrng += "</td><td>";
				drawSubChnlTbl(String.valueOf(chnl.getChannelId()),true);
				outputStrng += "</td></tr></table>";
			}
			
			pageContext.getOut().println(outputStrng);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return super.doStartTag();
	}

	public int doEndTag() throws JspException{
		outputStrng = "";
		return super.doEndTag();
	}
	public String getAlign() {
		return align;
	}

	public void setAlign(String align) {
		this.align = align;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
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

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getBorder() {
		return border;
	}

	public void setBorder(String border) {
		this.border = border;
	}

	@Override
	public void doFinally() {
		site = "";
		  channel = null;
		  width = "";
		  border = "";
		  align = "";
		  style = "";
		  classname = "";
		  outputStrng = "";
		super.doFinally();
	}
}
