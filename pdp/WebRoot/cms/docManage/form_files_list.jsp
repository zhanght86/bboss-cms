<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<jsp:include page="/sysmanager/base/scripts/panes.jsp"/>
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*"%>
<%@ page import="com.frameworkset.platform.cms.templatemanager.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.cms.util.*"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<% 
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request, response);

	//String siteId = request.getParameter("siteId");
	String uri = request.getParameter("uri");
	if(uri == null || uri.equals(""))
		uri = "/";
	
	
	String pathContext = CMSUtil.getAppRootPath();// + CMSUtil.getCMSContextPath() + "/customForm";
	//设置这个属性供上传文件,新建文件等
	//如果不设置这个属性,要传递参数;难得统一管理
	session.setAttribute("pathContext",pathContext);	
	
%>

<style type="text/css">
<!--
a:link {
	color: #000000;
	text-decoration: none;
}
a:visited {
	text-decoration: none;
	color: #000000;
}
a:hover {
	text-decoration: none;
	color: #000000;
}
a:active {
	text-decoration: none;
	color: #000000;
}
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}
.list {
	font-family: Verdana, "宋体";
	font-size: 12px;
	font-weight: lighter;
}
-->
</style>
<script type="text/javascript">
function onMsEnter(obj){
	obj.style.backgroundColor = "#eeeeee";
	obj.style.cursor="hand";
}
function onMsOut(obj){
	if(obj == lastClickObj) return;
	obj.style.backgroundColor = "#ffffff";
	obj.style.cursor="default";
}


var lastClickObj;
function onMsClick(obj,name,isFolder){
	if(lastClickObj!=null){
		lastClickObj.style.backgroundColor = "#ffffff";
	}
	obj.style.backgroundColor = "#eeeeee";
	lastClickObj = obj;
	var type = parent.document.URL;
	type = type.substring(type.lastIndexOf("=")+1);
	window.dialogArguments.document.all(type).value = "<%=uri%>" + name;
	window.close();
}

function onDBClick(obj,name,isFolder){
	
}

function goBack(){
	
}
function init(){
	
}
</script>
</head>
<body onload="init()">
<table width="100%" border="1" cellspacing="0" cellpadding="0" bordercolordark="#FFFFFF" bordercolorlight="#eeeeee">

<%
	List fileresources = com.frameworkset.platform.cms.documentmanager.tag.FormFilesTree.getFiles(pathContext,uri);
	System.out.println(fileresources.size());
	for(int i=0;i<fileresources.size();i++){
		FileResource fr = (FileResource)fileresources.get(i);
		if(!fr.isDirectory()){
%>
	<tr onmouseenter="onMsEnter(this)" onmouseout="onMsOut(this)" onclick="onMsClick(this,'<%=fr.getName()%>',<%=fr.isDirectory()%>)" ondblclick="onDBClick(this,'<%=fr.getName()%>',<%=fr.isDirectory()%>)" onselectstart="return false">
		<td height="25" class="list" style="padding-left:5px"><%=fr.getName()%></td>
		
	</tr>
<%
		}
	}
%>	
</table>
</body>
</html>