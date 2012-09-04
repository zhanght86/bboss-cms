<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.cms.*"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
<title></title>
<% 
	AccessControl accesscontrol = AccessControl.getInstance();
	accesscontrol.checkAccess(request, response);
	CMSManager cmsmanager = new CMSManager();
	cmsmanager.init(request,session,response,accesscontrol);
	String siteId =  cmsmanager.getSiteID();	
	
%>
<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
<style type="text/css">
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}
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
.menu {
	font-size: 14px;
	color: #0F62A3;
	text-align: center;
	padding-left: 5px;
	cursor:hand;
}
.list {
	font-family: Verdana, "宋体";
	font-size: 12px;
	font-weight: lighter;
}
</style>
<script language="javascript">
<%
	String uri = request.getParameter("uri");
%>
var uri = "<%=(uri==null?"":uri)%>";
function createFile(type){
	var url = "createFile.jsp?uri="+uri+"&type="+type;
	myOpen(url);
}


//如果有checkboxId,将删除checkbox所在的那一行,
//如果没有checkboxId这个参数,将删除所有选中的checkbox所在的行
function deleteFiles(checkboxId){
	if(checkboxId){
	  if(confirm('您确定要删除所选文件?')){
			var Obj = eval("parent.fileList.document.form1."+checkboxId);
			var url = "deleteFiles_do.jsp?uri="+uri+"&files="+Obj.value;
			myOpen(url);
		}
		return;

	}
	var files = parent.fileList.document.all.form1.elements("multiFiles");
	var hasChoosed = false;
	var filesStr = "";
	if(!files){
		alert("这个目录下没有文件!");
		return;
	}
	if(!files.length){
		if(files.checked){
			hasChoosed = true;
			filesStr += files.value+"<>";
		}	
	}else{
		for(var i=0;i<files.length;i++){
			if(files[i].checked){
				hasChoosed = true;
				filesStr += files[i].value+"<>"
			}
		}
	}
	if(hasChoosed){
		filesStr = filesStr.substring(0,filesStr.length-2);
	}else{
		alert("请选中一个或多个文件(夹)");
		return;
	}
	var url = "deleteFiles_do.jsp?uri="+uri+"&files="+filesStr;
	if(confirm('您确定要删除所选文件?')){
		myOpen(url);
	}
}

function uploadFile(){
	var url = "uploadFile.jsp?uri="+uri;
	myOpen(url);
}

function uploadZipFile(){
	var url = "uploadZipFile.jsp?uri="+uri;
	myOpen(url);
}

function reloadView(){
	parent.fileList.location.reload();	
}

function myOpen(url){
	window.showModalDialog(url,window,"dialogWidth:450px;dialogHeight:250px;center:yes;resizable:no;scroll:no");
}	
</script>
</head>
<body>
<table width="100%" height="36" border="0" cellpadding="0" cellspacing="1" bgcolor="#9DB3C4">
	<tr style="background-image:url(../images/Template_FileView_topmenu_Bg.gif)">
		<%
		if (accesscontrol.checkPermission(
				   		siteId,AccessControl.SITE_TPLCREATORFILE_PERMISSION,AccessControl.SITEFILE_RESOURCE)){
		%>
			<td height="27" class="menu" onClick="createFile(1)"><img src="../images/Template_adfileDot1.gif" hspace="3" align="absmiddle">新建文件</td>

		<%
			}if (accesscontrol.checkPermission(
				   		siteId,AccessControl.SITE_TPLCREATORDIRECTORY_PERMISSION,AccessControl.SITEFILE_RESOURCE)){
		%>
		<td height="27" class="menu" onClick="createFile(2)"><img src="../images/Template_adDirectryDot1.gif"  hspace="3" align="absmiddle">新建目录</td>
		<%
		}if (accesscontrol.checkPermission(
			   		siteId,AccessControl.SITE_TPLDELFILE_PERMISSION,AccessControl.SITEFILE_RESOURCE)){
		%>
		<td height="27" class="menu" onClick="deleteFiles()"><img src="../images/Template_DellCfile.gif"  hspace="3" align="absmiddle">删除选定文件</td>
		<%
		}if (accesscontrol.checkPermission(
			   		siteId,AccessControl.SITE_TPLSENDFILE_PERMISSION,AccessControl.SITEFILE_RESOURCE)){
		%>
		<td height="27" class="menu" onClick="uploadFile()"><img src="../images/Template_uploadfileDot1.gif"  hspace="3" align="absmiddle">上传文件</td>
		<%
		}if (accesscontrol.checkPermission(
			   		siteId,AccessControl.SITE_TPLSENDBAG_PERMISSION,AccessControl.SITEFILE_RESOURCE)){
		%>
		<td height="27" class="menu" onClick="uploadZipFile()"><img src="../images/Template_uploadZipfile1.gif" hspace="3" align="absmiddle">上传压缩包</td>
		<%
			}
		%>
	</tr>
</table>

</body>
</html>
