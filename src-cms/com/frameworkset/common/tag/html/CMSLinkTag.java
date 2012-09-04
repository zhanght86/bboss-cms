package com.frameworkset.common.tag.html;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.jsp.JspException;

import org.apache.ecs.html.A;
import org.apache.ecs.html.Font;
import org.apache.ecs.html.IMG;

import com.frameworkset.platform.cms.channelmanager.Channel;
import com.frameworkset.platform.cms.documentmanager.Document;
import com.frameworkset.platform.cms.documentmanager.DocumentManager;
import com.frameworkset.platform.cms.documentmanager.DocumentManagerException;
import com.frameworkset.platform.cms.documentmanager.DocumentManagerImpl;
import com.frameworkset.platform.cms.documentmanager.bean.DocAggregation;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.common.tag.BaseCellTag;
import com.frameworkset.common.tag.CMSTagUtil;
;

/**
 * 在一个<a>中添加两个样式 如:<a class="class1 class2">...</a>
 * 内容管理系统概览标签中内嵌的链接标签，无需指定站点的属性
 * 生成外部指定链接的标签，可以指定站点属性，如果指定的话有两种情况（不建议使用，请使用a标签）：
 *  1.link标签单独置于动态页面上
 *  2.link标签引用了另外一个站点的链接
 * <p>Title: CMSLinkTag</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date 2007-4-11 8:38:59
 * @author biaoping.yin
 * @version 1.0
 */
public class CMSLinkTag extends BaseCellTag {
	/**
	 * 外部指定的链接地址
	 */
	private String link;
	private String target;
	private boolean setcolor = false;
	/**
	 * 定义样式
	 */ 
	protected String style;
	/**
	 * 定义样式类 this.classname + linkTagClass 本来的样式
	 */
	protected String classname;
	/**
	 * 显示title 用样式的方式
	 * 配合使用
	 */
	private boolean useTitle = false;
    
	private String addTitleContent(String altStr){
		String content = "<span>"+altStr+"</span>";
		
		return content;
	}

	public int doStartTag() throws JspException {		
	    super.doStartTag();    
		Object object = dataSet.getOrigineObject();
		if(object instanceof Document)
		{
			/**
			 * 获取聚合文档标识
			 */
		    int aggregation = dataSet.getInt("doctype");
			if(aggregation != Document.DOCUMENT_CHANNEL)
			{
				String outStr = super.getOutStr();
				String altStr = outStr;
				if(outStr != null)
				{
					if(getEncode() != null && getEncode().equals("true"))
						outStr = URLEncoder.encode(outStr);
					if(getDecode() != null && getDecode().equals("true"))
						outStr = URLDecoder.decode(outStr);
				}		
				
				try { 
					if(this.maxlength > 0 && outStr != null && outStr.length() > maxlength)
					{
						outStr = outStr.substring(0,this.maxlength);
						if(replace != null)
							outStr += replace;
					}
					
					if(this.isSetcolor())
					{
						String color = this.dataSet.getString("titlecolor");
						if(color != null)
						{
							Font font = new Font();
							font.setColor(color);
							font.setTagText(outStr);
							outStr = font.toString();
						}
					}
					if(aggregation != 3) //非聚合文档
					{
						A linkA = new A();
						linkA.setTagText(outStr);
						/*增加title属性*/
						if(this.useTitle){
							linkA.addElement(addTitleContent(altStr));
						}else{
							linkA.setTitle(altStr);
						}
						/*
						 * 标签解析时，与之相关的路径有：
						 * 当前模版所在的路径－从context中获取,这里设置为"",因为内容管理系统中维护的图片地址都是基于_template目录的根开始的
						 * 当前发布的目的地－从context中获取
						 * 标签本身的链接 － 可从标签变量获取，如果没有设置标签变量则通过本频道当前的文档对应页面地址来获取
						 * 
						 */
						if(link == null)  
						{
							String href = this.getContentPath(context);
							linkA.setHref(href);
							if(target != null)
								linkA.setTarget(this.target);
						}
						else
						{
							if(this.context != null)
							{
			//					String templatePath = getTemplatePath(context);
								String templatePath = "";
								linkA.setHref(CMSTagUtil.getPublishedLinkPath(context,templatePath,link));
								if(target != null)
									linkA.setTarget(this.target);
								
							}	
							else
							{
								
								linkA.setHref(link);
							}
						}
						if (style!=null && !"".equals(style))
						linkA.setStyle(style);
						if (classname!=null && !"".equals(classname))
						{
							linkA.setClass("linkTagClass "+classname);
						}
						else
						{
							linkA.setClass("linkTagClass ");
						}
						if(this.dataSet.getInt("isNew") == 1 && !"".equals(this.dataSet.getString("newPicPath")))
						{
							IMG img = new IMG();
							
							String newPicPath = this.dataSet.getStringWithDefault("newPicPath","image/new.gif");
							String src = CMSTagUtil.getPublishedLinkPath(context,"",newPicPath);
							img.setSrc(src);
							out.print(linkA.toString() + img.toString());
						}
						else
						{
							out.print(linkA.toString());
						}
					}
					else //聚合文档处理
					{
						if(this.dataSet.getInt("isNew") == 1 && !"".equals(this.dataSet.getString("newPicPath")))
						{
							IMG img = new IMG();
							
							String newPicPath = this.dataSet.getStringWithDefault("newPicPath","image/new.gif");
							String src = CMSTagUtil.getPublishedLinkPath(context,"",newPicPath);
							img.setSrc(src);
							//out.print(linkA.toString() + img.toString());
							outStr = "<b>" + outStr + img.toString() + "</b>&nbsp;" + getAggregationScript();
						}
						else
						{
							outStr = "<b>" + outStr + "</b>&nbsp;" + getAggregationScript();
						}
		//				if(this.maxlength > 0 && outStr != null && outStr.length() > maxlength)
		//				{
		//					outStr = outStr.substring(0,this.maxlength);
		//					if(replace != null)
		//						outStr += replace;
		//				}
						
						out.print(outStr);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
					try {
						out.print(outStr);
					} catch (IOException e1) {
					
					}
		//			throw new JspException(e.getMessage());
				}
			}
			else
			{
				Channel channel = (Channel)dataSet.getValue("refChannel");
				A linkA = new A();
				String href = CMSUtil.getPublishedChannelPath(context,channel);
				String tagText = this.getOutStr();
				linkA.setHref(href);
				linkA.setTagText(tagText);
				if (style!=null && !"".equals(style))
				    linkA.setStyle(style);
				if (classname!=null && !"".equals(classname)) linkA.setClass("linkTagClass "+classname);
				else linkA.setClass("linkTagClass ");
				if(target != null)
					linkA.setTarget(this.target);
				try {
					/*增加title属性*/
					if(this.useTitle){
						linkA.addElement(addTitleContent(tagText));
					}else{
						linkA.setTitle(tagText);
					}
					out.print(linkA.toString());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else if(object instanceof Channel)
		{
			Channel channel = (Channel)object;
			A linkA = new A();
			String href = CMSUtil.getPublishedChannelPath(context,channel);
			String tagText = this.getOutStr();
			linkA.setHref(href);
			linkA.setTagText(tagText);
			if (style!=null && !"".equals(style))
			    linkA.setStyle(style);
			if (classname!=null && !"".equals(classname)) linkA.setClass("linkTagClass "+classname);
			else linkA.setClass("linkTagClass ");
			if(target != null)
				linkA.setTarget(this.target);
			try {
				/*增加title属性*/
				if(this.useTitle){
					linkA.addElement(addTitleContent(tagText));
				}else{
					linkA.setTitle(tagText);
				}
				out.print(linkA.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}  
			
		return SKIP_BODY;
	}
	
	/**
	 * 获取聚合文档脚本
	 * @return
	 */
	protected String getAggregationScript()
	{
		StringBuffer str = new StringBuffer();
		DocumentManager dm = new DocumentManagerImpl();
		
		try {
//			str.append(dataSet.getString("subtitle") + "&nbsp;");
			String docId = String.valueOf(dataSet.getInt("document_id"));
			
			List list = dm.getPubAggrDocList(docId);
			
			A alink;
			DocAggregation docAggregation;;
			
			for(int i =0;i<list.size();i++)
			{
				alink = new A();

				if (style!=null && !"".equals(style))
				alink.setStyle(style);
				if (classname!=null && !"".equals(classname)) alink.setClass("linkTagClass "+classname);
				else alink.setClass("linkTagClass ");
				
				docAggregation = (DocAggregation)list.get(i);
				String title = docAggregation.getTitle();
				if(this.maxlength > 0 && title != null && title.length() > maxlength)
					{
						title = title.substring(0,this.maxlength);
						if(replace != null)
							title += replace;
					}
				alink.setTagText(title);
				/*增加title属性*/
				if(this.useTitle){
					alink.addElement(addTitleContent(docAggregation.getTitle()));
				}else{
					alink.setTitle(docAggregation.getTitle());
				}				
				
				alink.setHref(
						getContentPath(String.valueOf(docAggregation.getIdbyaggr()),docAggregation.getChlId()));
				if(target != null)
					alink.setTarget(this.target);
				if (style!=null && !"".equals(style))
					alink.setStyle(style);
				if (classname!=null && !"".equals(classname)) 
					alink.setClass("linkTagClass "+classname);
				else 
					alink.setClass("linkTagClass ");
				
				
				if(docAggregation.getIsNew() == 1 && !"".equals(docAggregation.getNewPicPath()))
				{
					IMG img = new IMG();
					String newPicPath = docAggregation.getNewPicPath();
					String src = CMSTagUtil.getPublishedLinkPath(context,"",newPicPath);
					img.setSrc(src);
					if(i != list.size()-1)
					{
						str.append(alink.toString() + img.toString() + "&nbsp;");
					}
					else
					{
						str.append(alink.toString() + img.toString());
					}
				}
				else
				{
					if(i != list.size()-1)
					{
						str.append(alink.toString() + "&nbsp;");
					}
					else
					{
						str.append(alink.toString());
					}
				}
			}
		} catch (DocumentManagerException e) {
			e.printStackTrace();
		}
		
		return str.toString();
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
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

	public boolean isSetcolor() {
		return setcolor;
	}

	public void setSetcolor(boolean setcolor) {
		this.setcolor = setcolor;
	}

	public boolean isUseTitle() {
		return useTitle;
	}

	public void setUseTitle(boolean useTitle) {
		this.useTitle = useTitle;
	}

	@Override
	public int doEndTag() throws JspException {
		link = null;
		target = null;
		setcolor = false;
		 
		style = null;
		classname = null;
		/**
		 * 显示title 用样式的方式
		 * 配合使用
		 */
		useTitle = false;
		return super.doEndTag();
	}
}
