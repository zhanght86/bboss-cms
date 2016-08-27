<%@page import="java.net.URLDecoder"%>
<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="java.util.List"%>
<%@ page import="com.frameworkset.platform.cms.templatemanager.*" %>
<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@page import="com.frameworkset.platform.security.*"%>

<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);		
	//String siteId = request.getParameter("siteId");
	//com.frameworkset.platform.cms.CMSManager cmsManager  = new com.frameworkset.platform.cms.CMSManager();
 	//cmsManager.init(request,session,response,accesscontroler);
 	//String currentSiteid = cmsManager.getSiteID();
 	String isSite = request.getParameter("isSite");
 	String siteId = request.getParameter("siteId");
 	String channelId = request.getParameter("channelId");
 	String type = request.getParameter("type");
 	String typeName = URLDecoder.decode(request.getParameter("typeName"),"UTF-8");
 	String action = request.getParameter("action");
	String channelName = URLDecoder.decode(request.getParameter("channelName"),"UTF-8");
	String tName = URLDecoder.decode(request.getParameter("tName"),"UTF-8");
	String tId = request.getParameter("tId");
	if(isSite == null || !isSite.equals("is")){
		if(channelName == null || channelName.equals("null")){
			channelName = "新建";
		}
	}else{
		channelName = "";
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
<title>模板查询搜索页面</title>
<script src="../inc/js/func.js"></script>
<script language="JavaScript" src="../../sysmanager/include/pager.js" type="text/javascript"></script>

<style type="text/css">
	body {
		margin-left: 0px;
		margin-top: 5px;
		margin-right: 0px;
		margin-bottom: 0px;
	}
</style>
</head>
<body>
<form id="queryForm" target="templatelist" name="queryForm" method="post" action="channel_templateSet_list.jsp" >
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td width="5" valign="top" bgcolor="#ffffff"><img src="../images/querybox_left.gif" width="5" height="62">
    </td>
    <td style="background:url(../images/querybox_bg.gif) repeat-x top">
    <input type="hidden" name="isSite" id="isSite" value="<%=isSite%>"/>
    <input type="hidden" name="siteId" id="siteId" value="<%=siteId%>"/>
	<input type="hidden" name="type" id="type" value="<%=type%>"/>
	<input type="hidden" name="action" id="action" value="<%=action%>"/>
	<input type="hidden" name="channelId" id="channelId" value="<%=channelId%>">
	<input type="hidden" name="tId" id="tId" value="<%=tId%>">
	<table width="100%" border="0" cellpadding="3" cellspacing="0"  class="query_table" style="margin-top:5px" >
	<tr>
		<td width="15%" height="18" align="right" valign="middle" nowrap>模板名称：</td>
		<td width="22%" height="18" align="left" valign="middle">
			<input name="name" type="text"  class="cms_text" id="name" style="width:120px">		</td>
		<td width="15%" height="18" align="right" valign="middle" nowrap>模板风格：</td>
		<td width="22%" height="18" align="left" valign="middle">
		<select name="templateStyle" class="cms_select" id="templateStyle" width="108">
			<option value="-2">所有风格</option>
			<%
				TemplateStyleManager tsm = new TemplateStyleManagerImpl();
				List list1 = tsm.getAllTemplateStyleIdAndName();
				for(int i=0;i<list1.size();i++){
					TemplateStyleInfo tsi = (TemplateStyleInfo)list1.get(i);
			%>
					<option value="<%= tsi.getStyleId() %>"><%= tsi.getStyleName() %></option>
			<%
				}
			%>
		</select>
		</td>
		<td colspan="2">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input name="search" type="submit" class="cms_button" value="查询" ></td>
		</tr>
	</tr>
	<tr>
		<td height="18" align="right" valign="middle" nowrap>当前站点/频道：</td>
		<td height="18" align="left" valign="middle">
		<%= channelName %>
		</td>
		<td height="18" align="right" valign="middle" nowrap>当前模板：</td>
		<td height="18" align="left" valign="middle">
		<%=tName%>
		</td>
		<td height="18" align="right" valign="middle" nowrap>模版类型：</td>
		<td height="18" align="left" valign="middle">
		<%=typeName%>
		</td>
		</table>
	</td>
    <td width="6" valign="top"><img src="../images/querybox_right.gif" width="6" height="62"></td>
  </tr></table>
</form>
</body>
</html>


