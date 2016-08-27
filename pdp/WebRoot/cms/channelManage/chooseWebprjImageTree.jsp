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
	String fileFlag = request.getParameter("fileFlag");
	//图片浏览视图：list，列表；ppt，幻灯片；thumbnail，缩略图
	//String viewertype = request.getParameter("viewertype");
	String rootName = "选择图片";
	if(fileFlag!=null && fileFlag.equals("1"))
	{
		rootName = "选择首页文件";
	}
	if(fileFlag!=null && fileFlag.equals("media"))
	{
		rootName = "选择多媒体文件";
	}
	if(fileFlag!=null && fileFlag.equals("file"))
	{
		rootName = "选择文件";
	}
	if(fileFlag!=null && fileFlag.equals("picOrFlash"))
	{
		rootName = "选择图片或Flash动画";
	}
	//session.removeAttribute("fileFlag");
	//session.putValue("fileFlag",fileFlag);
	String urlstr = "chooseWebprjImageList.jsp?fileFlag=" + fileFlag;
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
					<td align="left"> 
						<tree:tree tree="filesystem_tree"
						node="filesystem_tree.node"
						imageFolder="/cms/images/tree_images/"
						collapse="true"
						includeRootNode="true"
						href="<%=urlstr%>"
						target="ImageListFrm" 
						dynamic="false"> 
						
						<tree:param name="fileFlag"/>

							<tree:treedata treetype="com.frameworkset.platform.cms.templatemanager.WebprojectFolderTree"
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