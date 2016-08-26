package com.frameworkset.common.tag.html;

import java.io.IOException;

import javax.servlet.jsp.JspException;

import org.apache.log4j.Logger;

import bboss.org.apache.velocity.VelocityContext;
import bboss.org.apache.velocity.context.Context;

import com.frameworkset.common.tag.CMSTagUtil;
import com.frameworkset.platform.cms.channelmanager.Channel;
import com.frameworkset.platform.cms.documentmanager.Document;
import com.frameworkset.platform.cms.sitemanager.Site;
import com.frameworkset.platform.cms.sitemanager.SiteManagerException;
import com.frameworkset.platform.cms.sitemanager.SiteManagerImpl;
import com.frameworkset.platform.cms.util.CMSUtil;
/**
 * 被FlashNewsTag.java取代
 * <p>Title: ImageNewsTag.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date 2007-9-12 18:45:50
 * @author ge.tao
 * @version 1.0
 */
public class ImageNewsTag extends CMSListTag{
	protected Logger log = Logger.getLogger(ImageNewsTag.class);
	protected CMSTagUtil tagUtil = new CMSTagUtil();
	protected SiteManagerImpl siteUtil = new SiteManagerImpl();
	protected String cssVmpath = "css/imageNews.css";
	protected String target = "_self";
	protected String isMarquee ;
	protected int m_second = 0;	
	protected int imgWidth = 240;
	protected int imgHeight=180;
	/**
	 * 
	 * true:   获得图片新闻频道下所有最近发布图片
	 * false:  获得普通频道下所有发布的带主题图片的文档图片
	 */
	protected String isLatest = "true" ;

	public int getImgHeight() {
		return imgHeight;
	}

	public void setImgHeight(int imgHeight) {
		this.imgHeight = imgHeight;
	}

	public int getM_second() {
		return m_second;
	}

	public void setM_second(int m_second) {
		this.m_second = m_second;
	}	

	public String getCssVmpath() {
		return cssVmpath;
	}

	public void setCssVmpath(String cssVmpath) {
		this.cssVmpath = cssVmpath;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getIsMarquee() {
		return isMarquee;
	}

	public void setIsMarquee(String isMarquee) {
		this.isMarquee = isMarquee;
	}

	public int getImgWidth() {
		return imgWidth;
	}

	public void setImgWidth(int imgWidth) {
		this.imgWidth = imgWidth;
	}

	public String getIsLatest() {
		return isLatest;
	}

	public void setIsLatest(String isLatest) {
		this.isLatest = isLatest;
	}
	
	/**
	 * 处理链接地址或者文件
	 * @param path 原路径
	 * @return String 处理后的路径
	 */
	public String dealPath(String path){
		return CMSTagUtil.getPublishedLinkPath(context,path);
	}
	
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
		top.put("second",second+"");
		top.put("imgWidth",this.imgWidth+"");
		top.put("imgHeight",this.imgHeight+"");
		sb.append(tagUtil.loadTemplate("publish/imageNews/content-top.vm",top));
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
		down.put("target",this.target);
		sb.append(tagUtil.loadTemplate("publish/imageNews/content-down.vm",down));
		return sb.toString();
	}
	
//	/**
//	 * 加载loop模板
//	 * @param sid
//	 * @return 
//	 * ImageNewsTag.java
//	 * @author: 陶格
//	 */
//	protected String loadLoopTemplete(String sid){
//		StringBuffer sb = new StringBuffer();
//		try {
//			List list = null;
//			if("true".equals(this.isLatest)){
//			    list = CMSUtil.getCMSDriverConfiguration().getCMSService().getChannelManager().getLatestPubDocList(sid,getChannel(),this.count);
//			}else{
//				list = CMSUtil.getCMSDriverConfiguration().getCMSService().getChannelManager().getPubDocListOfChannel(sid,getChannel(),this.count);
//			}
//			Channel chnl = CMSUtil.getCMSDriverConfiguration().getCMSService().getChannelManager().getChannelInfoByDisplayName(sid,this.channel);
//			int j = 0;
//			for(int i=0;i<list.size();i++){
//				j = i+1;	
//				Document doc = (Document)list.get(i);
//				String title = doc.getTitle();
//				String pic_path = doc.getPicPath();
//				
//				Context loop = new VelocityContext();		
//				loop.put("num",j+"");
//				/*图片路径*/
//				loop.put("param1",dealPath(pic_path));
//				/*依此为:图片文字(右上角)*/
//				loop.put("param2",title);
//				/*图片下面的滚动文字点击的链接*/
//				loop.put("param3",title);
//				/*和图片下面的滚动文字*/
//				loop.put("param4",title);
//				/*图片点击的链接*/
//				
//				loop.put("param5",tagUtil.getPublishedContentPath(context,chnl.getChannelPath(),doc.getDocument_id() + ""));
//				/*鼠标经过的ALT文字*/
//				loop.put("param6",title);
//				loop.put("target",this.target);
//				loop.put("isMarquee",this.isMarquee+"");
//				sb.append(tagUtil.loadTemplate("publish/imageNews/content-loop.vm",loop));					
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}	
//		return sb.toString();
//	}
//	
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
			for(int i=0;i<size();i++){
				j = i+1;	
				Document document = (Document)this.getOrigineObject(i);
				String title = this.getString(i,"title");
				String pic_path = this.getString(i,"picPath");
				String document_id = this.getString(i,"document_id");
				
				Context loop = new VelocityContext();		
				loop.put("num",j+"");
				/*图片路径*/
				loop.put("param1",dealPath(pic_path));
				/*依此为:图片文字(右上角)*/
				loop.put("param2",title);
				/*图片下面的滚动文字点击的链接*/
				loop.put("param3",title);
				/*和图片下面的滚动文字*/
				loop.put("param4",title);
				/*图片点击的链接*/
				
				loop.put("param5",tagUtil.getPublishedContentPath(context,document));
				/*鼠标经过的ALT文字*/
				loop.put("param6",title);
				loop.put("target",this.target);
				loop.put("isMarquee",this.isMarquee+"");
				sb.append(tagUtil.loadTemplate("publish/imageNews/content-loop.vm",loop));					
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return sb.toString();
	}
	
	/**
     * 生成图片新闻标签的HTML代码.
     * 加载外部的模板.vm文件,辅助生成HTML
     * 根据isLatest的取值,true:获得图片新闻频道的图片
     *                  false:获得制定频道里面的文档图片
     * @return StringBuffer
     * @throws Exception
     */
	public StringBuffer Img_newsTag(){
		int second = getM_second()==0?7600:getM_second();
		StringBuffer sb = new StringBuffer();
		//分块加载
		sb.append(this.loadTopTemplete(second));
		String siteid = "";  
		Site siteob = null;
		/*指定siteid ;其次指定sitename 最后从上下文获取context.getSiteID()*/
		if(this.getSite()!=null && this.getSite().length()>0){
			try {
				siteob = CMSUtil.getSiteCacheManager().getSiteByEname(this.getSite());
				siteid = String.valueOf(siteob.getSiteId());
			} catch (SiteManagerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			if(context!=null) siteid = context.getSiteID();			
		}
		sb.append(this.loadLoopTemplete(siteid));
		sb.append(this.loadDownTemplete());
	    return sb;
	}
	
	
	public int doStartTag() throws JspException{
		if(this.datatype == null || "".equals(this.datatype)) this.datatype = "imageChannel";
		return super.doStartTag();
	}
	
	public int doEndTag() throws JspException{
		StringBuffer buffer = Img_newsTag();
	    try {
	        pageContext.getOut().println(buffer.toString());
	    }catch (IOException ioe) {
	        ioe.printStackTrace();
	    }
		return super.doEndTag();
	}

	@Override
	public void doFinally() {
		cssVmpath = "css/imageNews.css";
		 target = "_self";
		 isMarquee = null;
		 m_second = 0;	
		 imgWidth = 240;
		 imgHeight=180;
		/**
		 * 
		 * true:   获得图片新闻频道下所有最近发布图片
		 * false:  获得普通频道下所有发布的带主题图片的文档图片
		 */
		 isLatest = "true" ;
		super.doFinally();
	}

}
