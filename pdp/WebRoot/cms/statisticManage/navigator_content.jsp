<%@ include file="/sysmanager/include/global1.jsp"%>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);
	
	AccessControl accesscontroler = AccessControl.getInstance();
	if(!accesscontroler.checkAccess(request, response))
		return;
	String viewPath = "cms::menu://sysmenu$root/statisticManage$module";
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
			window.open("navigator_content.jsp","perspective_toolbar");
			parent.parent.window.open("../top.jsp","perspective_topbar");
		}
		//-->
		</SCRIPT>
	</head>

<body class=""  scroll="auto">

<table width="100%" border="0"  background="../images/left_bg.jpg"  cellpadding="0" cellspacing="0" class="table" >
	<tr>
		<td height="115" style="padding-top:80px;" align="left">
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<img src="../images/refresh.gif" onClick="refresh()" width="16" height="15" border="0" title="刷新频道树" style="cursor:hand">		</td>
	</tr>
</table>

 
<table height="100%"  width="100%" background="../images/left_center_bg.jpg"><tr ><td  width="100%" height="100%" valign=top>
    <table>
        <tr><td align="left"> 
        		<tree:tree tree="dispatch_column_tree" node="dispatch_column_tree.node" 
								imageFolder="../images/tree_images/" collapse="true" includeRootNode="true" 
								href="" 
								target="base_properties_content" dynamic="false">
				

				<tree:treedata treetype="CustomColumnTree" scope="request" 
									rootid="<%=viewPath%>" rootName="统计功能" expandLevel="1" 
				showRootHref="false" 
				sortable="false"
				needObserver="false" refreshNode="false" enablecontextmenu="false"/>
				<tree:contextmenu/>
			</tree:tree>
         </td></tr>
    </table>
	
	</td></tr></table>
	

</body>
</html>
