package com.frameworkset.common.tag.html;

import java.util.List;

import javax.servlet.jsp.JspException;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.Context;

import com.frameworkset.platform.cms.channelmanager.Channel;
import com.frameworkset.platform.cms.documentmanager.Document;
import com.frameworkset.platform.cms.util.CMSUtil;

/**
 * 继承图片新闻标签
 * 图片滚动标签,只是模板和 图片新闻标签不同,参数略微不同
 * <p>Title: ImageRollTag.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date Jun 22, 2007 6:35:01 PM
 * @author ge.tao
 * @version 1.0
 */
public class ImageRollTag extends FlashNewsTag{
	protected String contentWidth = "480"; 
	protected String contentHeight = "180";
	protected String speed = "1";
	/**
	 * 加载top模板
	 * @param second
	 * @return 
	 * ImageNewsTag.java
	 * @author: 陶格
	 */
	protected String loadTopTemplete(int second){
		StringBuffer sb = new StringBuffer();
		Context top = new VelocityContext();
		/* 点击一次的滚动时间 */
		top.put("contentWidth",String.valueOf(this.contentWidth));
		top.put("contentHeight",String.valueOf(this.contentHeight));
		top.put("rollleftImg",dealPath("images/rollleft.gif"));
		sb.append(tagUtil.loadTemplate("publish/imageRoll/content-top.vm",top));
		return sb.toString();
	}
	
	
	/**
	 * 加载loop模板
	 * @param sid
	 * @return 
	 * ImageNewsTag.java
	 * @author: 陶格
	 */
	protected String loadLoopTemplete(String sid){
		StringBuffer sb = new StringBuffer();
		try {
//			List list = null;
//			if("true".equals(this.isLatest)){
//			    list = CMSUtil.getCMSDriverConfiguration().getCMSService().getChannelManager().getLatestPubDocList(sid,getChannel(),this.count);
//			}else{
//				list = CMSUtil.getCMSDriverConfiguration().getCMSService().getChannelManager().getPubDocListOfChannel(sid,getChannel(),this.count);
//			}
			Channel chnl = CMSUtil.getCMSDriverConfiguration().getCMSService().getChannelManager().getChannelInfoByDisplayName(sid,this.channel);
			int j = 0;
			for(int i=0;i<this.size();i++){
				j = i+1;	
				Document document = (Document)this.getOrigineObject(i);
				String title = this.getString(i,"title");
				String pic_path = this.getString(i,"picPath");
				String document_id = this.getString(i,"document_id");
				
				Context loop = new VelocityContext();	
				/*图片路径*/
				loop.put("imagepath",dealPath(pic_path));				
				/*图片点击的链接*/				
				loop.put("linkpath",tagUtil.getPublishedContentPath(context,document));
				/*鼠标经过的ALT文字*/
				loop.put("altstr",title);
				/*弹出窗口位置*/
				loop.put("target",this.target);
				/*设置图片大小*/
				int imgHeight = this.imgHeight>Integer.parseInt(this.contentHeight)?Integer.parseInt(this.contentHeight):this.imgHeight;
				loop.put("imgWidth",String.valueOf(this.imgWidth));
				loop.put("imgHeight",String.valueOf(imgHeight));
				loop.put("rollcenter",dealPath("images/rollcenter.gif"));
				sb.append(tagUtil.loadTemplate("publish/imageRoll/content-loop.vm",loop));					
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return sb.toString();
	}
	
	/**
	 * 加载down模板
	 * @return 
	 * ImageNewsTag.java
	 * @author: 陶格
	 */
	protected String loadDownTemplete(){
		StringBuffer sb = new StringBuffer();
		Context down = new VelocityContext();
		down.put("speed",String.valueOf(this.speed));
		down.put("rollright",dealPath("images/rollright.gif"));
		sb.append(tagUtil.loadTemplate("publish/imageRoll/content-down.vm",down));
		return sb.toString();
	}
	
//	public int doStartTag() throws JspException{
//		if(this.datatype == null || this.datatype.trim().trim().length()==0) {
//			this.datatype = "channelImages";
//		}
//		return super.doStartTag();
//	}
//		
	public String getContentHeight() {
		return contentHeight;
	}
	public void setContentHeight(String contentHeight) {
		this.contentHeight = contentHeight;
	}
	public String getContentWidth() {
		return contentWidth;
	}
	public void setContentWidth(String contentWidth) {
		this.contentWidth = contentWidth;
	}
	public String getSpeed() {
		return speed;
	}
	public void setSpeed(String speed) {
		this.speed = speed;
	}

}
