<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="java.io.*" %>
<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ page import="com.frameworkset.platform.security.AccessControl" %>
<%@ page import="com.frameworkset.platform.cms.*"%>
<%@ page import="com.frameworkset.platform.cms.util.*"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*" %>
<%@ page import="com.frameworkset.platform.cms.templatemanager.*" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
<title></title>
<%
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request, response);
	
	String uri = request.getParameter("uri");

	CMSManager cmsmanager = new CMSManager();
	cmsmanager.init(request,session,response,control);

	String siteId =  cmsmanager.getSiteID();
	String siteSecondName = cmsmanager.getCurrentSite().getSiteDir();
	String pathContext = null;
	if(siteId!=null && siteId.trim().length()!=0){
		String temp = (new SiteManagerImpl()).getSiteAbsolutePath(siteId);
		if(temp!=null && temp.trim().length()!=0){
			pathContext = new File(temp,"_template").getAbsolutePath();
		}
	}
	if(pathContext==null || pathContext.trim().length()==0){
%>
		<script language="javascript">
			alert("没有找到站点所在路径.");
		</script>
<% 
		return;
	}
%>
<style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}
-->
</style>
<script type="text/javascript">
var uri = "";
<%
if(uri!=null && uri.trim().length()!=0){
	out.println("uri = '"+uri+"';");
}
%>
function setImage(name){
	var url = uri.replace("\\","/");
	
	if(url.charAt(0) == '/'){
		url = url.substr(1); 
	}
	if(url.charAt(url.length-1) != '/' && url.search(/\S+/img)!=-1){
		url += "/";
	}
	
	url += name;

	parent.parent.theOpener.setImage(url);
	
	top.close();
}
function preview(fileName,abstractPth){
	var uri = "<%=uri==null?"/":uri%>";
	var url = uri.replace("\\","/");
	if(url.charAt(0) == '/'){
		url = url.substr(1); 
	}
	if(url.charAt(url.length-1) != '/' && url.search(/\S+/img)!=-1){
		url += "/";
	}
	window.open("<%=rootpath%>/cms/siteResource/<%=siteSecondName%>/_template/"+ url + fileName);
}
function high(ob,fileName){
	var tds = document.getElementsByName("picName");
	for(var i=0;i<tds.length;i++){
		unhigh(tds[i]);
	}
	ob.bgColor="#C3D2E5";
	parent.ToolsFrm.selectedFileName=fileName;
}
function unhigh(ob){
	ob.bgColor="";
}
</script>
</head>
<body>
<table width="100%" border="1" cellspacing="0" cellpadding="0" bordercolordark="#FFFFFF" bordercolorlight="#eeeeee">
<tr class="cms_report_tr">
	<td width="20%">
		文件类型
	</td>
    <td width="60%">
		文件名
	</td>
    <td width="20%">
		操作
	</td>
</tr>
<%
	FileFilter fileFilter = new IndexPageFilter(siteId,uri);   
	File[] files = FileUtil.getSubFiles(pathContext,uri,fileFilter);
	for(int i=0;files!=null&&i<files.length;i++){
		String fileName = files[i].getName();
		String icon = "../images/doc_html.gif";
		if(fileName.endsWith("jsp"))
			icon = "../images/doc_jsp.gif";
%>
	<tr onselectstart="return false" style="cursor:hand;height:25px"  class="cms_data_tr">
		<td width="20%" align="center">
			<img src="<%=icon%>">
		</td>
		<td width="60%" onclick="high(this,'<%=fileName%>')" name="picName" id = "picName" width="80%">
			<%=fileName%>
		</td>
		<td width="20%" align="center">
				<a style="cursor:hand" onclick="preview('<%=fileName%>')">预览</a>
		</td>
	</tr>
<%		
	}
%>	
</table>
</body>
</html>
