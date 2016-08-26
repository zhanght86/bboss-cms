package com.frameworkset.common.tag.html;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import com.frameworkset.common.tag.BaseCellTag;
import com.frameworkset.common.tag.CMSTagUtil;

/**
 * 
 * <p>Title: FlashTag</p>
 * <p>Description: 输出文档</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date 2007-10-15 16:52:57
 * @author biaoping.yin
 * @version 1.0
 */
public class FlashTag extends BaseCellTag{
	
	private String width ="200px";
	
	private String height="100px";

	
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
	
	public int doStartTag() throws JspException {

		super.doStartTag();
		String outStr = CMSTagUtil.getPublishedLinkPath(context, super.getOutStr());
		
		if (outStr != null && !outStr.equals("")) {
			
			
			try {
				out.print(FlashTagShow(outStr).toString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return SKIP_BODY;
	}
	
	
	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}
	
	public void setPageContext(PageContext pageContext){
		super.setPageContext(pageContext);	
	}

	@Override
	public void doFinally() {
		width ="200px";
		
		  height="100px";
		super.doFinally();
	}
}
