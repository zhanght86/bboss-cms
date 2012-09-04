package com.frameworkset.common.tag.html;

import javax.servlet.jsp.JspException;

import com.frameworkset.common.tag.CMSBaseTag;

/**
 * 生成留言表单标签
 * @author huiqiong.zeng
 *
 */
public class DocCommentLeavingTag extends CMSBaseTag {
	private String width;
	private String height;
	
	public int doStartTag() throws JspException
	{	
		super.doStartTag();
		try{
			out.println(commentTagShow());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SKIP_BODY;
	}
	private String commentTagShow(){
		String docId = request.getParameter("docId");
		///////////////////////////////////
		docId = docId==null?"33430":docId;    //测试用
		///////////////////////////////////
		String path=request.getContextPath();
		
		String outputString = "";
		outputString = outputString + 
			"<link href='" + path + "/cms/inc/css/cms.css' rel='stylesheet' type='text/css'>";
		
		outputString = outputString + 
			"<div>" +
			"<form method='post' target='operIframe' name='myForm'> " +
				"<table align='center' style='width:" + width + ";'>" +
					"<tr>" +
						"<td width='12%' align='right'>用户名:</td>" +
						"<td width='16%'><input type='text' name='commenterName' size='16'></td>" +
						"<td width='11%' align='right'>密码:</td>" +
						"<td width='25%'><input type='password' name='psword' size='16'></td>" +
						"<td width='36%'><input type='checkbox' name='userhidebox' checked='checked' onClick='changeBoxVluae()'>匿名发表</td>" +
						"<input type='hidden' name='userhide' checked='checked' value='1'>" + 
					"</tr>" +
					"<tr>" + 
					"<td colspan='5' height='70' align='left'><textarea style='width:80%; height:70;' name='docComment'></textarea></td>" +
					"</tr>" +
					"<tr>" +
						"<td colspan='4'></td>" +
						"<td colspan='4' align='left'><input type='button' name='button' onClick='sub()'  class = 'cms_button' value='提交留言'></td>" +
					"</tr>" +
		       "</table>" +
		   "</form>" +
		   "</div>";
		//System.out.println(outputString);
		outputString = outputString + 
			"<script language='javascript'>" +
				"function sub(){" +
					"if(myForm.docComment.value==''){" +
						"alert('留言不能为空');" +
						"return;" +
					"}" +
					"myForm.action='" + path + "/cms/docManage/addCommentHandleDefault.jsp?docId=" + docId + "';"+
					"myForm.submit();" +
				"}" +
				"function changeBoxVluae(){" +
					"if(myForm.userhidebox.checked)" +
						"myForm.userhide.value = '1';" +
					"else myForm.userhide.value = '0';" +
				"}" +
			"</script>";
		outputString = outputString + 
			"<div style='display:none'><iframe name='operIframe'></iframe></div>"; 
		
		return outputString;
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
}
