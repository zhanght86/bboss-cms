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

	//CMSManager cmsmanager = new CMSManager();
	//cmsmanager.init(request,session,response,control);
	//String fileFlag = request.getParameter("fileFlag");
	//String rootName = "选择图片";
	//if(fileFlag!=null && fileFlag.equals("1"))
	//{
	//	rootName = "选择首页文件";
	//}
	//if(fileFlag!=null && fileFlag.equals("media"))
	//{
	//	rootName = "选择多媒体文件";
	//}
	//if(fileFlag!=null && fileFlag.equals("file"))
	//{
	//	rootName = "选择文件";
	//}
	//session.removeAttribute("fileFlag");
	//session.putValue("fileFlag",fileFlag);
	//String urlstr = "chooseImageList.jsp?fileFlag=" + fileFlag;

	String path_= request.getContextPath();
    String basePath = request.getScheme() + "://"	+ request.getServerName() + ":" +
    request.getServerPort() + path_+ "/"; 

%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	
	<link rel="stylesheet" type="text/css" href="/sysmanager/css/treeview.css">
	<link rel="stylesheet" type="text/css" href="/sysmanager/css/windows.css">
	<link rel="stylesheet" type="text/css" href="/sysmanager/css/contentpage.css">
	<link rel="stylesheet" type="text/css" href="/sysmanager/css/tab.winclassic.css">
	
	<script src="/cms/inc/js/func.js"></script>
	<script src="/cms/inc/js/rightMenu.js"></script>
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
<table height="100%"  width="100%" background="/cms/images/left_center_bg.jpg">
	<tr>
		<td width="100%" height="100%" valign=top>
			<table>
				<tr>
					<td align="left"> 
						<img src="/cms/voteManager/images/site.gif"/><%=basePath%>cms/voteManager/images
					</td>	
				</tr>
			</table>	
		</td>
	</tr>
</table>
</body>
</html>