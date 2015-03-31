package com.frameworkset.common.tag.html;

import java.io.IOException;

import javax.servlet.jsp.JspException;

import org.apache.ecs.html.A;
import org.apache.ecs.html.IMG;

import bboss.org.apache.velocity.VelocityContext;
import bboss.org.apache.velocity.context.Context;

import com.frameworkset.common.tag.BaseCellTag;
import com.frameworkset.common.tag.CMSTagUtil;
import com.frameworkset.platform.cms.channelmanager.Channel;
import com.frameworkset.platform.cms.documentmanager.Document;
import com.frameworkset.platform.cms.util.CMSUtil;

/**
 * 图片新闻链接标签，针对主题图片的链接标签,如果是外部链接，直接输出外部链接和主题图片，如果是普通
 * 文档，输出文档链接和图片,用做概览标签的子标签，无需自定自己的站点属性从概览标签继承
 * 
 * 设置图片的 alt 属性
 * 如果 imgalt指定的值, 是dataSet的属性, 
 * 类似{title}
 * 获取该属性的值,设置为图片的 alt属性
 * 否则 直接把imgalt设置为图片的 alt属性
 * <p>Title: CMSImageLinkTag</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date 2007-5-21 14:59:52
 * @author biaoping.yin
 * @version 1.0
 */
public class CMSImageLinkTag extends BaseCellTag {
	private int width = -1;
	private int height = -1;
	private String astyle;
	private String imgstyle;
	private String imgalt;
	private int border = 0;
	private String target = null;
	private String aclassname = null;	
	private String imgclassname = null;
	private String isZoom = "false"; 
	public int doStartTag() throws JspException
	{
		int ret = super.doStartTag();
		Object object = dataSet.getOrigineObject();
		String imageurl = "";
		String extHTML = "";
		if(object instanceof Document){
			int doctype = dataSet.getInt("doctype");		
			if(doctype != Document.DOCUMENT_CHANNEL){
				if(this.getColName() == null)
				{
					imageurl = this.dataSet.getString("picPath");
					
				}	
				else
				{
					imageurl = this.getOutStr();
				}
				IMG image = new IMG();
				/*图片缩放功能*/
				if("true".equals(this.isZoom)){
					Context content = new VelocityContext();
					extHTML = CMSTagUtil.loadTemplate("publish/imageZoom.vm",	content);
					image.addAttribute("onmousewheel","return zoomimg(this)");				
				}
				imageurl  = CMSUtil.getPublishedLinkPath(this.context,"",imageurl);
				
				if(imageurl.endsWith(".swf") || imageurl.endsWith(".SWF")){/*判断是图片还是flash 判断是flash*/
					try {
						out.print(FlashTagShow(imageurl).toString());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{/*判断是图片*/
					image.setSrc(imageurl);
					if(imgstyle != null)
						image.setStyle(imgstyle);
					if(imgclassname != null)
						image.setClass(imgclassname);
					
					if(width != -1)
						image.setWidth(width);
					if(height != -1)
						image.setHeight(height);
					image.setBorder(border);
					/**
					 * 设置图片的 alt 属性
					 * 如果 imgalt指定的值, 是dataSet的属性, 
					 * 类似{title}
					 * 获取该属性的值,设置为图片的 alt属性
					 * 否则 直接把imgalt设置为图片的 alt属性
					 */
					if(imgalt != null ){
						Object imgaltStr = imgaltStr = super.getFormulaValueWithAttribute(imgalt); 
						
						if(imgaltStr == null || imgaltStr.equals(""))
						{
							imgaltStr = imgalt;
						}
						image.setAlt(imgaltStr.toString());
					}
					
					String docType = this.dataSet.getString("doctype");
					String url = "";
					A a = new A();
					if(astyle != null)
						a.setStyle(astyle);
					if(aclassname != null)
						a.setClass(aclassname);
					
					if(docType.equals("0")) //普通文档
					{
						url = super.getContentPath(context);
					}
					else if(docType.equals("1")) //外部链接
					{
						url = this.dataSet.getString("linkfile");
						
					}		
					
					a.setHref(url);
					if(target != null )
						a.setTarget(target);
					else
					{
						String target_ = this.dataSet.getString("linktarget");
						if(target_ != null)
							a.setTarget(target_);
					}
					a.setTagText(image.toString());
					try {
						out.print(extHTML);
						out.print(a.toString());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			
			}else{/* 频道 */
				Channel channel = (Channel)dataSet.getValue("refChannel");
				imageurl = channel.getOutlinepicture();
				IMG image = new IMG();
				/*图片缩放功能*/
				if("true".equals(this.isZoom)) {
					Context content = new VelocityContext();
					extHTML = CMSTagUtil.loadTemplate("publish/imageZoom.vm",	content);
					image.addAttribute("onmousewheel","return zoomimg(this)");
				}
				A a = new A();
				String url = CMSUtil.getPublishedChannelPath(context,channel);
				imageurl  = CMSUtil.getPublishedLinkPath(this.context,imageurl);
				if(imageurl.endsWith(".swf") || imageurl.endsWith(".SWF")){/*判断是图片还是flash 判断是flash*/
					try {
						out.print(FlashTagShow(imageurl).toString());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{/*判断是图片*/
					/* 图片 */
					image.setSrc(imageurl);
					if(imgstyle != null)
						image.setStyle(imgstyle);
					if(imgclassname != null)
						image.setClass(imgclassname);			
					if(width != -1)
						image.setWidth(width);
					if(height != -1)
						image.setHeight(height);
					image.setBorder(border);
					/**
					 * 设置图片的 alt 属性
					 * 如果 imgalt指定的值, 是dataSet的属性, 
					 * 类似{title}
					 * 获取该属性的值,设置为图片的 alt属性
					 * 否则 直接把imgalt设置为图片的 alt属性
					 */
					if(imgalt != null ){
						Object imgaltStr = imgaltStr = super.getFormulaValueWithAttribute(imgalt); 
						
						if(imgaltStr == null || imgaltStr.equals(""))
						{
							imgaltStr = imgalt;
						}
						image.setAlt(imgaltStr.toString());
					}
					/* 链接 */
					if(astyle != null)
						a.setStyle(astyle);
					if(aclassname != null)
						a.setClass(aclassname);
					if(target != null )
						a.setTarget(target);
					
					a.setTagText(image.toString());
					a.setHref(url);
					try {
						out.print(extHTML);
						out.print(a.toString());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
		    }
		}else if(object instanceof Channel){
			Channel channel = (Channel)object;
			imageurl = channel.getOutlinepicture();
			IMG image = new IMG();
			/*图片缩放功能*/
			if("true".equals(this.isZoom)) {
				Context content = new VelocityContext();
				extHTML = CMSTagUtil.loadTemplate("publish/imageZoom.vm",	content);
				image.addAttribute("onmousewheel","return zoomimg(this)");
			}
			A a = new A();
			String url = CMSUtil.getPublishedChannelPath(context,channel);
			imageurl  = CMSUtil.getPublishedLinkPath(this.context,imageurl);
			if(imageurl.endsWith(".swf") || imageurl.endsWith(".SWF")){/*判断是图片还是flash 判断是flash*/
				try {
					out.print(FlashTagShow(imageurl).toString());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{/*判断是图片*/
			/* 图片 */
				image.setSrc(imageurl);
				if(imgstyle != null)
					image.setStyle(imgstyle);
				if(imgclassname != null)
					image.setClass(imgclassname);			
				if(width != -1)
					image.setWidth(width);
				if(height != -1)
					image.setHeight(height);
				image.setBorder(border);
				/**
				 * 设置图片的 alt 属性
				 * 如果 imgalt指定的值, 是dataSet的属性, 
				 * 类似{title}
				 * 获取该属性的值,设置为图片的 alt属性
				 * 否则 直接把imgalt设置为图片的 alt属性
				 */
				if(imgalt != null ){
					Object imgaltStr = imgaltStr = super.getFormulaValueWithAttribute(imgalt); 
					
					if(imgaltStr == null || imgaltStr.equals(""))
					{
						imgaltStr = imgalt;
					}
					image.setAlt(imgaltStr.toString());
				}
				/* 链接 */
				if(astyle != null)
					a.setStyle(astyle);
				if(aclassname != null)
					a.setClass(aclassname);
				if(target != null )
					a.setTarget(target);
				
				a.setTagText(image.toString());
				a.setHref(url);
				try {
					out.print(extHTML);
					out.print(a.toString());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return ret;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}

	public int getBorder() {
		return border;
	}
	public void setBorder(int border) {
		this.border = border;
	}
	
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	
	public String getAclassname() {
		return aclassname;
	}
	public void setAclassname(String aclassname) {
		this.aclassname = aclassname;
	}
	public String getAstyle() {
		return astyle;
	}
	public void setAstyle(String astyle) {
		this.astyle = astyle;
	}
	public String getImgalt() {
		return imgalt;
	}
	public void setImgalt(String imgalt) {
		this.imgalt = imgalt;
	}
	public String getImgclassname() {
		return imgclassname;
	}
	public void setImgclassname(String imgclassname) {
		this.imgclassname = imgclassname;
	}
	public String getImgstyle() {
		return imgstyle;
	}
	public void setImgstyle(String imgstyle) {
		this.imgstyle = imgstyle;
	}
	public String getIsZoom() {
		return isZoom;
	}
	public void setIsZoom(String isZoom) {
		this.isZoom = isZoom;
	}
	
	public StringBuffer FlashTagShow(String src) throws Exception{		
		StringBuffer sb = new StringBuffer();
		sb.append("<object classid=\"clsid:D27CDB6E-AE6D-11cf-96B8-444553540000\" \n");
		sb.append("codebase=\"http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,29,0\" \n");
		sb.append("width=\""+ width +"\" height=\""+ height +"\"> \n");
		sb.append("<param name=\"movie\" value=\""+ src +"\"> \n");
		sb.append("<param name=\"quality\" value=\"high\"> \n");
		sb.append("<embed src=\""+ src +"\" width=\""+ width +"\" height=\""+ height +"\" quality=\"high\" \n");
		sb.append("pluginspage=\"http://www.macromedia.com/go/getflashplayer\" type=\"application/x-shockwave-flash\"> \n");
		sb.append("</embed> \n");
		sb.append("</object> \n");		
	
		return sb;
	}
	@Override
	public void doFinally() {
		  width = -1;
		  height = -1;
		  astyle = null;
		  imgstyle= null;
		  imgalt= null;
		  border = 0;
		  target = null;
		  aclassname = null;	
		  imgclassname = null;
		  isZoom = "false"; 
		super.doFinally();
	}

	

}
