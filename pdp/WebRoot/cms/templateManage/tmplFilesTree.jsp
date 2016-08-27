<%@ include file="/sysmanager/include/global1.jsp"%>
<jsp:include page="/sysmanager/base/scripts/panes.jsp"/>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="com.frameworkset.platform.security.AccessControl,com.frameworkset.platform.cms.CMSManager"%>
<%
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request, response);

	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);

	CMSManager cmsmanager = new CMSManager();
	cmsmanager.init(request,session,response,control);	
	//String urlstr = "template_import.jsp";
	String rootName = "压缩包模板文件";
	
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
<style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}
-->
</style></head>
<body class=""  scroll="auto">
	
<table height="100%"  width="100%" background="../images/left_center_bg.jpg">
	<tr>
		<td width="100%" height="100%" valign=top>
			<table>
				<tr>
					<td align="center"> 
						<tree:tree tree="TemplateupzipFolderTree"
						node="filesystem_tree.node"
						imageFolder="/cms/images/tree_images/"
						collapse="true"
						includeRootNode="true"
						href=""
						target="ImageListFrm" 
						dynamic="true"> 
						
						<tree:param name="rootPath"/>
							<tree:treedata treetype="TemplateupzipFolderTree"
								scope="request"
								rootid="/"  
								rootName="<%=rootName%>"
								expandLevel="1"
								showRootHref="true"
								needObserver="false"
								enablecontextmenu="true"
								/>      
						</tree:tree>
					</td>	
				</tr>
			</table>	
		</td>
	</tr>
</table>
</body>
</html>