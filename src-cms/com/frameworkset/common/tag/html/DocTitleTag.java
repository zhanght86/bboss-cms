
package com.frameworkset.common.tag.html;

import javax.servlet.jsp.JspException;

import com.frameworkset.platform.cms.documentmanager.DocumentManager;
import com.frameworkset.platform.cms.documentmanager.DocumentManagerImpl;
import com.frameworkset.common.tag.CMSBaseTag;
/**
 * 评论查看页面中文档标题标签
 * @author Administrator
 *
 */
public class DocTitleTag extends CMSBaseTag {

	private String style = "";
	private String color;
	private String size;
	
	public int doStartTag() throws JspException
	{	
		super.doStartTag();
		String docId = request.getParameter("docId");
		String returnUrl = request.getParameter("returnUrl");
		DocumentManager dmi = new DocumentManagerImpl();
		try {
			String title = dmi.getDoc(docId).getSubtitle();
			String a = "";
			a = "<a href='" + returnUrl +"' style='" + style + "'>" +
					"<font color='" + color + "' size='" + size + "'>" + title +"</font>" +
				"</a>";
			out.println(a);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SKIP_BODY;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}
}
