package com.frameworkset.common.tag.html;

import java.io.IOException;

import javax.servlet.jsp.JspException;

import com.frameworkset.common.tag.CMSBaseCellTag;
import com.frameworkset.common.tag.CMSTagUtil;
import com.frameworkset.util.StringUtil;



/**
 * 对模版页面中直接引用的链接进行处理
 * <p>
 * Title: CMSURITag
 * </p>
 * 
 * <p>
 * Description:直接输出模板目录下的对应用url地址,可以通过link属性直接指定对应的url地址，也可以通过读取colName属性找到
 * 对应的url地址
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: 三一集团
 * </p>
 * 
 * @Date 2007-4-14 23:02:08
 * @author biaoping.yin
 * @version 1.0
 */
public class CMSURITag	extends CMSBaseCellTag
//	extends CMSBaseTag 
{
	private String base = null;
	private String link="";
	
		
	public int doStartTag() throws JspException {		
		super.doStartTag();
		String temp = null;
		if(StringUtil.isEmpty(this.getColName()))
		{
			if(this.link!=null &&this.link.length()>0)
//				try {
//					String templatePath = CMSTagUtil.getTemplatePath(context);
//					if(templatePath == null)
//						templatePath = "";
//					out.print(CMSTagUtil.getPublishedLinkPath(context,templatePath,this.link));
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				temp = this.link;
		}
		else
		{
			temp =this.getOutStr();
		}
		if(temp!=null && temp.length()>0)
		{
			
			try {
				if(base == null)
				{
					String templatePath = CMSTagUtil.getTemplatePath(context);
					if(templatePath == null)
						templatePath = "";
					String url = CMSTagUtil.getPublishedLinkPath(context,templatePath,temp); 
					out.print(url);
				}
				else
				{
					String url = CMSTagUtil.getPublishedLinkPath(context,base,temp); 
					out.print(url);
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return SKIP_BODY;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
	
	public static void main(String[] args){
		
	}

	@Override
	public int doEndTag() throws JspException {
		
		return super.doEndTag();
	}

	@Override
	public void doFinally() {
		link = "";
		this.base = null;
		super.doFinally();
	}

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}
	
	
	



}
