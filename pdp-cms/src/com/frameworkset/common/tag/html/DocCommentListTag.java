package com.frameworkset.common.tag.html;

import javax.servlet.jsp.JspException;

import com.frameworkset.common.ecs.IFrame;
import com.frameworkset.common.tag.CMSBaseTag;

/**
 * 文档评论列表标签
 * @author Administrator
 */
public class DocCommentListTag extends CMSBaseTag {
	private int frameBorder ;
	private String width;
	private String height;
	private String style;
	private String scrolling = "auto";
	public int doStartTag() throws JspException
	{	
		super.doStartTag();
		String docId = request.getParameter("docId");
		///////////////////////////////////
		docId = docId==null?"33430":docId;    //测试用
		/////////////////////////////////// 
		String path=request.getContextPath();
		try{
			IFrame iframe = new IFrame();
			iframe.setSrc(path + "/cms/docManage/docCommentsListTag.jsp?docId=" + docId);
			iframe.setWidth(width);
			iframe.setHeight(height);
			if(style!=null){
				iframe.setStyle(style);
			}
			if(frameBorder>0)
				iframe.setFrameBorder(true);
			else iframe.setFrameBorder(false);
			iframe.setScrolling(scrolling);
			out.print(iframe.toString()); 
		} catch (Exception e) {  
			e.printStackTrace();
		}
		return SKIP_BODY;
	}
	public int getFrameBorder() {
		return frameBorder;
	}
	public void setFrameBorder(int frameBorder) {
		this.frameBorder = frameBorder;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getScrolling() {
		return scrolling;
	}
	public void setScrolling(String scrolling) {
		this.scrolling = scrolling;
	}
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	@Override
	public void doFinally() {
		 frameBorder = 0;
			  width = null;
			  height = null;
			  style = null;
			  scrolling = "auto";
		super.doFinally();
	}
}
