package com.frameworkset.common.tag.html;

import javax.servlet.jsp.JspException;

import com.frameworkset.common.tag.CMSBaseTag;
import com.frameworkset.common.tag.CMSTagUtil;
import com.frameworkset.platform.cms.channelmanager.Channel;
import com.frameworkset.platform.cms.channelmanager.ChannelCacheManager;
import com.frameworkset.platform.cms.channelmanager.ChannelManagerImpl;
import com.frameworkset.platform.cms.documentmanager.Document;
import com.frameworkset.platform.cms.documentmanager.DocumentManagerImpl;
import com.frameworkset.platform.cms.sitemanager.Site;
import com.frameworkset.platform.cms.util.CMSUtil;
//import com.frameworkset.util.ValueObjectUtil;

/**
 * 
 * <p>Title: CurrentPosTag</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: bbossgroups</p> 
 * @Date 2007-4-16 9:43:20
 * @author kai.hu
 * @version 1.0
 */
public class CurrentPosTag extends CMSBaseTag {
	
	protected String content = null;
	
	protected String target="";
//	protected String style = "cursor:hand;color:#595959;";
	protected String style = "";
	protected String currentPosPrompt = "当前位置： ";
	protected String indexPrompt = "首页";
	/**
	 * 不显示的导航位置，从0开始，从末频道开始计算，用";隔开"
	*/
	protected String notDisplayIds = null;

	private void channelPos(StringBuffer outputStrng,String channel){
		try{
//			ChannelManagerImpl chnlMgr = new ChannelManagerImpl();
			ChannelCacheManager chcache = CMSUtil.getChannelCacheManager(context.getSiteID());
			Channel chnl = chcache.getChannelByDisplayName(this.getChannel());
			//chnlMgr.getChannelInfoByDisplayName(context.getSiteID(),channel);
			
			String href = CMSTagUtil.getPublishedChannelPath(context,chnl);
			 
//			SiteManagerImpl siteMngr = new SiteManagerImpl();
			Site site = context.getSite();//siteMngr.getSiteInfo(String.valueOf(chnl.getSiteId()));
			
			if(notDisplayIds != null && !notDisplayIds.trim().equals(""))
			{
				notDisplayIds = ";" + notDisplayIds.trim() + ";";
				if(notDisplayIds.indexOf(";0;") == -1)
					outputStrng.append("<a style=\""+style+" \"  href =\""+href+"\">"+chnl.getName()+"</a>");
				else
					outputStrng.append("");
				chnl = chcache.getChannel(String.valueOf(chnl.getParentChannelId()));//chnlMgr.getParentChannelInfoByDisplayName(context.getSiteID(),String.valueOf(channel));
				int tmp_i = 1;
				while(chnl != null){
					if(chnl.getChannelId() == 0)
						break;
//					chnl = chnlMgr.getChannelInfo(String.valueOf(chnl.getChannelId()));
					href = CMSTagUtil.getPublishedChannelPath(context,chnl);
					if(notDisplayIds.indexOf(";" + tmp_i + ";") == -1)
						outputStrng.insert(0,"<a style=\""+style+"\"  href =\""+href+"\">"+chnl.getName()+"</a>>");
					chnl = chcache.getChannel(String.valueOf(chnl.getParentChannelId()));//chnlMgr.getParentChannelInfo(String.valueOf(chnl.getChannelId()));
					if(chnl.getChannelId() == 0)
						break;
					tmp_i ++;
				}
			}
			else
			{  
				outputStrng.append("<a style=\""+style+" \"  href =\""+href+"\">"+chnl.getName()+"</a>");
				chnl = chcache.getChannel(String.valueOf(chnl.getParentChannelId()));//chnlMgr.getParentChannelInfoByDisplayName(context.getSiteID(),String.valueOf(channel));
				while(chnl != null){
					if(chnl.getChannelId() == 0)
						break;
//					chnl = chnlMgr.getChannelInfo(String.valueOf(chnl.getChannelId()));
					href = CMSTagUtil.getPublishedChannelPath(context,chnl);
					outputStrng.insert(0,"<a style=\""+style+"\"  href =\""+href+"\">"+chnl.getName()+"</a>>");
					chnl = chcache.getChannel(String.valueOf(chnl.getParentChannelId()));//chnlMgr.getParentChannelInfo(String.valueOf(chnl.getChannelId()));
				}
			}
			
			outputStrng.insert(0,getSitePosition(site));
			
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String getSitePosition(Site site)
	{
		
		if(site == null)
			return "";
		return "<a style=\""+style+"\" href =\""+CMSTagUtil.getPublishedSitePath(context,site.getIndexFileName())+"\" >"+this.indexPrompt+"</a>>";
	}
	
	private void docPos(StringBuffer outputStrng,String docID){
		DocumentManagerImpl docMngr = new DocumentManagerImpl();
		try{
			ChannelCacheManager chcache = CMSUtil.getChannelCacheManager(context.getSiteID());
			//get info about this doc
//			
			if(content != null){
				Document doc = docMngr.getDoc(docID);
				docID = String.valueOf(doc.getChanel_id());
			}
			
//			outputStrng = "";6718353
//			int chnlID = 0;
			
			//get channel id for this doc
//			chnlID = Integer.parseInt(docID);
//			
//			ChannelManagerImpl chnlMgr = new ChannelManagerImpl();
			
			//get channel related to doc
			Channel chnl = chcache.getChannel(docID);//chnlMgr.getChannelInfo(String.valueOf(chnlID));
			
			channelPos(outputStrng,chnl.getDisplayName());
			
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public int doStartTag() throws JspException{
		try {
			super.doStartTag();
			StringBuffer outputStrng = new StringBuffer();
			if (channel == null && this.content == null){
				Channel chnl = CMSTagUtil.getCurrentChannel(context);
				if(chnl == null)
				{
					outputStrng.append(getSitePosition(context.getSite()));
				}
				else
				{
					channel = String.valueOf(chnl.getDisplayName());
					channelPos(outputStrng,channel);
				}
//				ValueObjectUtil.getValue(channel,"");
				
			}
			else if(content != null){
				docPos(outputStrng,content);
			}
			else if(channel != null){
				channelPos(outputStrng,channel);
			}
			out.print(this.currentPosPrompt + outputStrng);		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SKIP_BODY;
	}
//	public int doEndTag() throws JspException{		
//		try {
//			if (channel == null && this.content == null){
//				Channel chnl = CMSTagUtil.getCurrentChannel(context);
//				channel = String.valueOf(chnl.getChannelId());
//				channelPos(channel);
//			}
//			else if(content != null){
//				docPos(content);
//			}
//			else if(channel != null){
//				channelPos(channel);
//			}
//			out.println("当前位置： "+outputStrng);		
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return super.doEndTag();
//	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	
	
	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getCurrentPosPrompt() {
		return currentPosPrompt;
	}

	public void setCurrentPosPrompt(String currentPosPrompt) {
		this.currentPosPrompt = currentPosPrompt;
	}

	public String getIndexPrompt() {
		return indexPrompt;
	}

	public void setIndexPrompt(String indexPrompt) {
		this.indexPrompt = indexPrompt;
	}

	public String getNotDisplayIds() {
		return notDisplayIds;
	}

	public void setNotDisplayIds(String notDisplayIds) {
		this.notDisplayIds = notDisplayIds;
	}

	@Override
	public int doEndTag() throws JspException {
		
		return super.doEndTag();
	}

	@Override
	public void doFinally() {
		content = null;
		
		target="";
		style = "";
		currentPosPrompt = "当前位置： ";
		indexPrompt = "首页";
		super.doFinally();
	}


//	public String getDocID() {
//		return docID;
//	}
//
//	public void setDocID(String docID) {
//		this.docID = docID;
//	}


//	public String getPosType() {
//		return posType;
//	}
//
//
//	public void setPosType(String posType) {
//		this.posType = posType;
//	}

}

/*
<%
	String path = "您的当前位置： ";

	String positionID = "3397";
	
	String posType = "doc";
	
	int chnlID = Integer.parseInt(positionID);
	
	if ("doc".equals(posType)){
		DocumentManagerImpl docMngr = new DocumentManagerImpl();
		Document doc = docMngr.getDoc(positionID);
		if (doc == null)
			out.println("此文档ID不存在！");
				
		path = " " + doc.getTitle();
		
		try{
			chnlID = (int)(doc.getChanel_id());
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
%>
<%
	ChannelManagerImpl chnlMgr = new ChannelManagerImpl();
	
	Channel chnl = chnlMgr.getChannelInfo(String.valueOf(chnlID));
	
	SiteManagerImpl siteMngr = new SiteManagerImpl();
	
	String href = request.getContextPath()+"/cms/siteResource/";
	
	if (siteMngr.siteIsExist(String.valueOf(chnl.getSiteId()))){
		Site site = siteMngr.getSiteInfo(String.valueOf(chnl.getSiteId()));
		href += site.getSiteDir() + "/_webprj/";
	}
	href += chnl.getChannelPath()+"/"+chnl.getPubFileName()+"."+chnl.getPubFileNameSuffix();
	out.println(href);
	path = "<a style=\"cursor:hand \" href =\""+href+"\">"+chnl.getDisplayName()+"</a>>>" + path;
	
	chnl = chnlMgr.getParentChannelInfo(String.valueOf(chnlID));
	
	while(chnl != null){
		chnl = chnlMgr.getChannelInfo(String.valueOf(chnl.getChannelId()));
		href = request.getContextPath()+"/cms/siteResource/";
		if (siteMngr.siteIsExist(String.valueOf(chnl.getSiteId()))){
			Site site = siteMngr.getSiteInfo(String.valueOf(chnl.getSiteId()));
			href += site.getSiteDir() + "/_webprj/";
		}

		href += chnl.getChannelPath()+"/"+chnl.getPubFileName()+"."+chnl.getPubFileNameSuffix();
		out.println(href);
	
		path = "<a style=\"cursor:hand\"  href =\""+href+"\">"+chnl.getDisplayName()+"</a>>>" + path;
		chnl = chnlMgr.getParentChannelInfo(String.valueOf(chnl.getChannelId()));
	}
	
	out.println(path);
%>
*/
