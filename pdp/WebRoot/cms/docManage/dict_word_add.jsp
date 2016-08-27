
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@page import="com.frameworkset.platform.cms.sitemanager.*"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@page import="com.frameworkset.platform.cms.customform.*"%>
<%@ page import="java.util.*"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>

<%
	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkAccess(request, response);
	
	String action = request.getParameter("action");
	String wordId = request.getParameter("wordId");
	
	String word="";
	String description="";
	String siteId="";
	
	if("edit".equals(action)){
		word = request.getParameter("word");
		description = request.getParameter("description");
		siteId = request.getParameter("siteId");
	}		
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
<script src="../inc/js/func.js"></script>
<tab:tabConfig/>
<script language="javascript" src="../inc/js/selectTime.js"></script>
<style type="text/css">
<!--
.STYLE7 {color:#003398; font-size: 12px;}
#Layer1 {
	position:absolute;
	left:0px;
	top:0px;
	width:113px;
	height:127px;
	z-index:1;
}
.STYLE8 {color: #0000FF}

-->
</style>
<SCRIPT LANGUAGE="JavaScript">
	//
	function subform()
	{
		if(dictWordForm.word.value=="")
		{
			alert("请填写词汇！");
			dictWordForm.word.focus();
			return false;
		}
		dictWordForm.action="dictWordHandle.jsp?action=<%=action%>";
		dictWordForm.submit();
	}
</SCRIPT>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>字典词汇信息</title>
</head>

<body background="../images/grayline_bg.jpg">
<form  method="post"  name="dictWordForm" target="dictWordFrame" id="dictWordForm">
	<input type="hidden" name="wordId" value="<%=wordId%>"/>		
	<table width="98%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF" align="center">
		<tr >
			<td width="4%" height="20" align="right">
				<img src="../images/ico_point.gif" width="25" height="25">
			</td>
			<td height="20" class="cms_title_blue">
				字典词汇信息
			</td>
		</tr>
		<tr>
			<td colspan="2" style="height:9px; background-color:#3266B1"></td>
		</tr>
		<tr>
			<td colspan="2">
				<table width="100%" border="1" cellpadding="0" cellspacing="0" bordercolor="#f0f0f0">
					<tr>
						<td width="100" height="24" align="right" nowrap>词汇名称:</td>
						<td height="24">
							<input type="text" id="word" name="word" class="cms_text" style="width:180px"  maxlength="20" value="<%=word%>"/>
							<span style="color: #FF0000">*</span>
						</td>
					</tr>
					<tr>
						<td width="100" height="24" align="right" nowrap>所属站点:</td>
						<td height='30' align="left">
							<select name="siteId" class="cms_select">
								<option value="0">通用</option>
								<%
									SiteManagerImpl dmi = new SiteManagerImpl();
									List allSiteList = null;
									allSiteList = dmi.getSiteList();
									request.setAttribute("allSiteList",allSiteList);
								%>
								<pg:list requestKey="allSiteList">
								<option value="<pg:cell colName="siteId"/>">
								<pg:cell colName="name"/>
								</pg:list>
							</select>		
							<span style="color: #FF0000">*</span>
						</td>
					</tr>
					
					<tr>
						<td width="100" height="24" align="right" nowrap>词汇描述:</td>
						<td height="24">
							<textarea name="description" cols="28" rows="5" class="cms_textarea"><%=description%></textarea>	
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	<br>
	<div style="text-align:center">
		<INPUT name="button" type="button" class="cms_button" onClick="subform()"  value="保存">
		&nbsp;&nbsp;&nbsp;
		<INPUT name="button" type="button" class="cms_button" onClick="window.close()"  value="关闭">
	</div>
</form>
<iframe name="dictWordFrame" width="0" height="0" style="display:none"></iframe>

</body>
</html>