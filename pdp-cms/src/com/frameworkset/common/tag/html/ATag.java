package com.frameworkset.common.tag.html;

import java.io.IOException;

import javax.servlet.jsp.JspException;

import com.frameworkset.common.ecs.A;
import com.frameworkset.common.tag.BaseCellTag;
import com.frameworkset.common.tag.CMSTagUtil;
import com.frameworkset.platform.cms.driver.context.impl.DefaultContextImpl;

/**
 * 外部链接标签，可以用于概览标签的内容标签，也可以单独使用来生成一个可用的链接
 * 缺省根据文档的linkfile属性来生成url链接元素（<a href="..."></a>）
 * 也可以指定文档中的其他属性来生成
 * 
 * 单独用来生成一个可用的链接，必须指定href属性
 * 可以指定site（站点英文名称），也可以不指定
 * 不指定的话就从发布上下中获取
 * 如果指定了，有两种情况，
 * 		一种是在模板中使用，标签解析生成的是一个其带他站点域名链接
 *          一种是发布的上下文site==指定site
 *          一种是发布的上下文site!=指定site
 * 		一种是在动态页面中使用，标签解析生成的是一个带站点域名链接
 * 
 * 
 * 
 * <p>
 * Title: ATag
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: bbossgroups
 * </p>
 * 
 * @Date 2007-4-14 23:02:08
 * @author biaoping.yin
 * @version 1.0
 */
public class ATag extends BaseCellTag {

	private String target;
	/**
	 * 定义样式
	 */
	protected String style;
	
	/**
	 * 指定超链接文本字段
	 */
	protected String textColname;
	/**
	 * 定义样式类
	 */
	protected String classname;
	
	/**
	 * 指定站点英文名称
	 */
	protected String site;
	
	/**
	 * 指定链接地址
	 */
	protected String href ;
	
	/**
	 * 指定链接文字
	 */
	protected String text;

	
	public int doStartTag() throws JspException {
		
		if(this.getColName() == null || this.getColName().equals(""))
			this.setColName("linkfile");
		super.doStartTag();
		//链接路径
		String outStr = "" ; 
		//指定了链接地址
		if(this.href != null){
			if(this.context == null){//在动态页面中使用, 构造上下文,必须指定site属性
			    this.context = new DefaultContextImpl(request,response);
			    if(this.site != null)
			        ((DefaultContextImpl)context).setSite(site);
			}
			if(context instanceof DefaultContextImpl)
			{
				
				outStr = CMSTagUtil.getPublishedLinkPath(context,"",href);
			}
			else if(site == null || site.equals("") || site.equals(context.getSite().getSecondName()))
			{
				String templatePath = CMSTagUtil.getTemplatePath(context);
				if(templatePath == null)
					templatePath = "";
				outStr = CMSTagUtil.getPublishedLinkPath(context,templatePath,href);
			}
			else if(!site.equals(context.getSite().getSecondName()))
			{
				 this.context = new DefaultContextImpl(request,response);
				    if(this.site != null)
				        ((DefaultContextImpl)context).setSite(site);
				
				outStr = CMSTagUtil.getPublishedLinkPath(context,"",href);
			}
		}else{//未指定链接地址
		    outStr = super.getOutStr();
		}
		
		
		if(text == null )
		{
			if(textColname != null)
			{
				text = dataSet.getString(textColname);
			}
			else
			{
				text = dataSet.getString("title");
			}
		}
		else
		{
			
		}
		/**
		 * 根据指定的长度和替换字符对text进行处理
		 */
		text = getSimpleText(text);
		A a = new A();
//		StringBuilder a = new StringBuilder();
//		a.append("<a");
//		a.setTagText(text);
		a.setTagText(text);

		if (outStr != null && !outStr.equals("")) {
			a.setHref(outStr);
//			a.append(" href=\"").append(outStr).append("\"");

			if (null != target && !"".equals(target))
			{
//				a.append(" target=\"").append(target).append("\"");
				a.setTarget(target);
			}
			else
			{
				String temp = this.dataSet.getString("linktarget");
				if (null != temp && !"".equals(temp))
				{
					a.setTarget(temp);
//					a.append(" target=\"").append(target).append("\"");
				}
			}

			if (style!=null && !"".equals(style))
			{
				a.setStyle(style);
//				a.append(" style=\"").append(style).append("\"");
			}
			if (classname!=null && !"".equals(classname))
			{
				a.setClass(classname);
//				a.append(" class=\"").append(classname).append("\"");
			}
//			a.append(">").append(text).append("</a>");

			try {
				out.print(a.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return SKIP_BODY;
	}

	public int doEndTag() throws JspException {

		
		
		
		return super.doEndTag();
	}
	@Override
	public void doFinally() {
		// TODO Auto-generated method stub
		/**
		 * 重置标签属性，避免属性的值被保留下来，影响标签的正常功能
		 */
		this.classname = null;
		this.defaultValue = null;
		this.style = null;
		this.text = null;
		this.site = null;
		this.target = null;
		this.href = null;
		this.textColname = null;
		this.maxlength = -1;
		super.doFinally();
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

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getTextColname() {
		return textColname;
	}

	public void setTextColname(String textColname) {
		this.textColname = textColname;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	

}
