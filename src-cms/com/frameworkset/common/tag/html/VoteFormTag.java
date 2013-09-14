/**
 * 
 */
package com.frameworkset.common.tag.html;

import javax.servlet.jsp.JspException;

import com.frameworkset.common.tag.CMSBaseTag;

/**
 *  
 * <p>Title: VoteFormTag.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date 2007 3:10:50 PM
 * @author da.wei
 * @version 1.0
 */

public class VoteFormTag extends CMSBaseTag {

	/**
	 * 问卷id
	 */
	String id = "";
	/**
	 * 频道显示名称，用来指定展示的问卷所属的频道
	 */
	String channel = "";

	public int doStartTag() throws JspException {
		super.doStartTag();
		String str = "<form name='form1' method='post' action='"+request.getContextPath()+"/cms/voteManager/voteaction.jsp'>" +
				"<script language='javascript'>var questionids='-1';</script>";
		
		try{
			 out.print(str);
		 } catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
		 
		return EVAL_BODY_AGAIN;
	}
	
	public int doEndTag() throws JspException {
		String str = "</form>";
		
		try{
			 out.print(str);
		 } catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
		int ret = super.doEndTag();
		
		 
		return ret;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}
}
