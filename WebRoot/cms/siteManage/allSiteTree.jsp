<%@ include file="/sysmanager/include/global1.jsp"%>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%
		AccessControl accesscontroler = AccessControl.getInstance();
    	if(!accesscontroler.checkAccess(request, response))
    		return;
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		
		<link rel="stylesheet" type="text/css" href="<%=rootpath%>/sysmanager/css/treeview.css">
		<link rel="stylesheet" type="text/css" href="<%=rootpath%>/sysmanager/css/windows.css">
		<link rel="stylesheet" type="text/css" href="<%=rootpath%>/sysmanager/css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="<%=rootpath%>/sysmanager/css/tab.winclassic.css">
		
		<script src="../inc/js/func.js"></script>
		<script src="../inc/js/rightMenu.js"></script>
		<SCRIPT LANGUAGE="JavaScript">
		<!--
		function refresh()
		{
			window.open("allSiteTree.jsp","perspective_toolbar");
			parent.parent.window.open("../top.jsp","perspective_topbar");
		}
		//-->
		</SCRIPT>
	</head>

<body class=""  scroll="auto">

<table width="100%" border="0"  background="../images/left_bg.jpg"  cellpadding="0" cellspacing="0" class="table" >
<tr>
<%
		
		if (accesscontroler.checkPermission("siteaddlink", AccessControl.WRITE_PERMISSION,
										AccessControl.SITE_RESOURCE)) {
%>
	<td height="78" style="padding-top:80px;" align="left">
	&nbsp;&nbsp;&nbsp;
	<img src="../images/refresh.gif" onclick="refresh()" width="16" height="15" border="0" title="刷新站点树" style="cursor:hand">
	&nbsp;&nbsp;
	<img src="../images/ico_channel.gif" width="16" height="15" border="0">
		<a href="javascript:addRootSite()"> 新建站点</a>
	</td>
<%}%>

   
</tr>
</table>

 
<table height="100%"  width="100%" background="../images/left_center_bg.jpg"><tr ><td  width="100%" height="100%" valign=top>
    <table>
        <tr><td align="left"> 
         <tree:tree tree="allsite_tree"
    	           node="allsite_tree.node"
    	           imageFolder="/cms/images/tree_images/"
    	           collapse="true"
    			   includeRootNode="true"
    			   href="site_edit.jsp"
    			   target="base_properties_content" 
    			   dynamic="false"> 
    			   <tree:treedata treetype="AllSiteTree"
    	                   scope="request"
    	                   rootid="ResourceManager"  
    	                   rootName="站点管理"
    	                   expandLevel="1"
    	                   showRootHref="false"
    	                   needObserver="false"
    	                   enablecontextmenu="true"
    	                   />
    	          
    	</tree:tree>
         </td></tr>
    </table>
	
	</td></tr></table>
	

</body>
</html>
