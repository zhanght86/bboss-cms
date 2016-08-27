<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.cms.*"%>
<%
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request, response);

	CMSManager cmsmanager = new CMSManager();
	cmsmanager.init(request,session,response,control);
	String siteId =  cmsmanager.getSiteID();

	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);

	//fileFlag为"1"表示首页文件的选择，为null或""表示为图片的选择
	String fileFlag = request.getParameter("fileFlag");
	fileFlag = fileFlag == null?"pic":fileFlag;//默认为选择图片
	String uploadTitle = "上传图片文件";
	if("1".equals(fileFlag))
		uploadTitle = "上传首页文件";
	if("media".equals(fileFlag))
		uploadTitle = "上传多媒体文件";
	if("file".equals(fileFlag))
		uploadTitle = "上传文件";
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
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
<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
</head>
<script src="<%=request.getContextPath()%>/cms/inc/js/func.js"></script>
<script language="javascript">
var fileFlag = "<%=fileFlag%>";
function showUpload(){
	document.all.showUploadDIV.style.display = "none";
	document.all.uploadFileDIV.style.display = "inline";
}

function cancel(){
	document.all.showUploadDIV.style.display = "inline";
	document.all.uploadFileDIV.style.display = "none";
}


function send(){
    var pattern=/\.(bmp|gif|jpeg|jpg)$/i;
	var filename = document.all.form1.file.value;
	if(fileFlag == "" || fileFlag == "pic")
	{
		if(filename.search(/\S/)==-1){
			alert("请选择一个图片文件!");
			return;
		}
		if(!pattern.test(filename)){
			alert("这里只能上传后缀名为.gif,.bmp,.jpeg,.jpg图片!");
			return;
		}
	}
	document.all.form1.action="uploadImageFile_do.jsp?uri="+parent.ImageListFrm.uri;
	document.all.form1.submit();
}
var selectedFileName;
//选择文件
function sub(){
	var alertvalue = "请选择一个图片！！";
	if(fileFlag == "1")
		alertvalue = "请选择一个首页文件！！";
	if(fileFlag == "media")
		alertvalue = "请选择一个多媒体文件！！";
	if(fileFlag == "file")
		alertvalue = "请选择一个文件！！";

	if(selectedFileName==undefined){
		alert(alertvalue);
		return;
	}
	parent.ImageListFrm.setImage(selectedFileName);
}
//删除文件
function del()
{
	var alertvalue = "请选择一个图片！！";
	if(fileFlag == "1")
		alertvalue = "请选择一个首页文件！！";
	if(fileFlag == "media")
		alertvalue = "请选择一个多媒体文件！！";
	if(fileFlag == "file")
		alertvalue = "请选择一个文件！！";

	if(selectedFileName==undefined){
		alert(alertvalue);
		return;
	}
	var url = "";
	if(parent.ImageListFrm.uri != undefined)
	{
		url = parent.ImageListFrm.uri;
		alert(selectedFileName);
		alert(url);
	}
	if(confirm('您确定要删除所选文件?')){
		var urlstr = "deleteFiles_do.jsp?viewtype=other&uri="+url+"&files="+selectedFileName;
		aler(urlstr);
		openWin(urlstr,300,150);
	}
	return;
}
//重命名
function rename()
{
	var alertvalue = "请选择一个图片！！";
	if(fileFlag == "1")
		alertvalue = "请选择一个首页文件！！";
	if(fileFlag == "media")
		alertvalue = "请选择一个多媒体文件！！";
	if(fileFlag == "file")
		alertvalue = "请选择一个文件！！";

	if(selectedFileName==undefined){
		alert(alertvalue);
		return;
	}
	var url = "";
	if(parent.ImageListFrm.uri != undefined)
	{
		url = parent.ImageListFrm.uri;
	}
	var urlstr = "reName.jsp?viewtype=other&uri="+url+"&tempuri="+selectedFileName;
	openWin(urlstr,450,350);
}
</script>
<body>
<form action="" method="post" enctype="multipart/form-data" name="form1" target="hiddenFrm">
<span id="showUploadDIV">
	<input type="button" name="Submit" value="<%=uploadTitle%>" onClick="showUpload()">
</span>
<span id="uploadFileDIV" style="display:none">	
	<input type="file" name="file" id="file">
	<input type="button" name="Submit2" value="上传" onclick="send()">
	<input type="button" value="取消" onClick="cancel()">
</span>
<input type="button" value="确定" onClick="sub()">
<%
if (control.checkPermission(
			siteId,AccessControl.SITE_TPLDELFILE_PERMISSION,AccessControl.SITEFILE_RESOURCE)){
%>
<!-- <input type="button" value="删除" onClick="del()">-->
<%
}
%>
<!-- <input type="button" value="重命名" onClick="rename()">-->
<input type="button" value="返回" onClick="top.close()">
</form>
<iframe id="hiddenFrm" name="hiddenFrm" style="display:none"></iframe>
</body>
</html>
