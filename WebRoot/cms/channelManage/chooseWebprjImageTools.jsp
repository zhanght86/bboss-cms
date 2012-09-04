<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.cms.*"%>
<%
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request, response);

	CMSManager cmsmanager = new CMSManager();
	cmsmanager.init(request,session,response,control);
	String siteId =  cmsmanager.getSiteID();
	String viewertype = (String)session.getAttribute("viewertype");

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
	if("flash".equals(fileFlag))
		uploadTitle = "上传Flash动画文件";
	if("picOrFlash".equals(fileFlag))
	    uploadTitle = "上传图片或flash动画";
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
    if(fileFlag == "pic" ){/* 上传图片 */
        var urlstr = "chooseWebprjImage.jsp?uri="+parent.ImageListFrm.uri;
        var returnVal;
        returnVal = openWin(urlstr,320,310);
        if(returnVal!= ""){
            //var param = window.location.search;
            
            parent.ImageListFrm.location.href = parent.ImageListFrm.location +"&filename="+returnVal;
        }
        
    }else{
	    document.all.showUploadDIV.style.display = "none";
	    document.all.uploadFileDIV.style.display = "inline";
    }
}

function cancel(){
	document.all.showUploadDIV.style.display = "inline";
	document.all.uploadFileDIV.style.display = "none";
}


function send(){
    var pattern=/\.(bmp|gif|jpeg|jpg)$/i;
   
	var filename = document.all.form1.file.value;
	if(fileFlag == "" || fileFlag == "pic")//图片
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
	if(fileFlag == "" || fileFlag == "media")//多媒体文件
	{
		pattern=/\.(rm|mp3|wav|mid|midi|ra|avi|mpg|mpeg|asf|asx|wma|mov|wmv|rmvb)$/i;
		if(filename.search(/\S/)==-1){
			alert("请选择一个多媒体文件!");
			return;
		}
		if(!pattern.test(filename)){
			alert("这里只能上传后缀名为rm|mp3|wav|mid|midi|ra|avi|mpg|mpeg|asf|asx|wma|mov|wmv|rmvb的多媒体文件!");
			return;
		}
	}
	if(fileFlag == "" || fileFlag == "1")//首页文件
	{
		pattern=/\.(html|htm|jsp)$/i;
		if(filename.search(/\S/)==-1){
			alert("请选择一个首页文件!");
			return;
		}
		if(!pattern.test(filename)){
			alert("这里只能上传后缀名为html|htm|jsp的首页文件!");
			return;
		}
	}
	if(fileFlag == "" || fileFlag == "flash")//Flash动画文件
	{
		pattern=/\.(swf)$/i;
		if(filename.search(/\S/)==-1){
			alert("请选择一个Flash动画文件!");
			return;
		}
		if(!pattern.test(filename)){
			alert("这里只能上传后缀名为swf的Flash动画文件!");
			return;
		}
	}
	
	if(fileFlag == "" || fileFlag == "picOrFlash")//图片或Flash动画文件
	{
		var pattern1=/\.(bmp|gif|jpeg|jpg)$/i;
		var pattern2=/\.(swf)$/i;
		
		if(filename.search(/\S/)==-1){
			alert("请选择一个图片或Flash动画文件!");
			return;
		}
		if(!pattern1.test(filename)&&!pattern2.test(filename)){
			alert("这里只能上传后缀名为.gif,.bmp,.jpeg,.jpg图片或后缀名为swf的Flash动画文件!");
			return;
		}
	}
	parent.ImageListFrm.document.getElementById("divProcessing").style.display="block";
	document.all.form1.action="uploadImageFile_do.jsp?coverflag="+document.all.coverFlag.checked+"&uri="+parent.ImageListFrm.uri;
	document.all.form1.submit();
}
var selectedFileName = null;
//选择文件
function sub(){
	
	var alertvalue = "请选择一个图片！！";
	if(fileFlag == "1")
		alertvalue = "请选择一个首页文件！！";
	if(fileFlag == "media")
		alertvalue = "请选择一个多媒体文件！！";
	if(fileFlag == "file")
		alertvalue = "请选择一个文件！！";
	if(fileFlag == "flash")
		alertvalue = "请选择一个Flash动画文件！！";
    if(fileFlag == "picOrFlash")
        alertvalue = "请选择一个图片或flash动画";
        
	if(selectedFileName==undefined){
		alert(alertvalue);
		return;
	}
	parent.ImageListFrm.setImage(selectedFileName);
}
//清空选项
function cleaner()
{
	parent.ImageListFrm.setImage("");
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
	if(fileFlag == "flash")
		alertvalue = "请选择一个Flash动画文件！！";
    if(fileFlag == "picOrFlash")
        alertvalue = "请选择一个图片或flash动画";
        
	if(selectedFileName==undefined){
		alert(alertvalue);
		return;
	}
	var url = "";
	if(parent.ImageListFrm.uri != undefined)
	{
		url = parent.ImageListFrm.uri;
	}
	if(confirm('您确定要删除所选文件?')){
		var urlstr = "../templateManage/deleteWebprjFiles_do.jsp?viewtype=other&uri="+url+"&files="+selectedFileName;
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
	if(fileFlag == "flash")
		alertvalue = "请选择一个Flash动画文件！！";
    if(fileFlag == "picOrFlash")
        alertvalue = "请选择一个图片或flash动画";
        
	if(selectedFileName==undefined){
		alert(alertvalue);
		return;
	}
	var url = "";
	if(parent.ImageListFrm.uri != undefined)
	{
		url = parent.ImageListFrm.uri;
	}
	var urlstr = "../templateManage/reName.jsp?fileFlag=<%=fileFlag%>&viewtype=other&uri="+url+"&tempuri="+selectedFileName;
	openWin(urlstr,450,350);
}
//图片浏览视图：list，列表；ppt，幻灯片；thumbnail，缩略图
function change()
{	
	var viewertype = document.all("viewtype").value;
	if(viewertype == "")
		return false;
	document.forms[0].action = "chooseImageSetViewerType.jsp?viewertype=" + viewertype;
	document.forms[0].target = "hiddenFrm";
	document.forms[0].submit();
}
</script>
<body>
<form action="" method="post" enctype="multipart/form-data" name="form1" target="hiddenFrm">
<input type="hidden" name="selectedFileName" id="selectedFileName" value=""/>
<span id="showUploadDIV">
	<input type="button" name="Submit" value="<%=uploadTitle%>" onClick="showUpload()">
</span>
<span id="uploadFileDIV" style="display:none">	
	<input type="file" size="10" name="file" id="file"><input type="checkbox" id="coverFlag" name="coverFlag">&nbsp;覆盖重名文件
	<input type="button" name="Submit2" value="上传" onclick="send()">
	<input type="button" value="取消" onClick="cancel()">
</span>
<input type="button" value="确定" onClick="sub()">
<%
if (control.checkPermission(
			siteId,AccessControl.SITE_TPLDELFILE_PERMISSION,AccessControl.SITEFILE_RESOURCE)){
%>
<input type="button" value="删除" onClick="del()">
<%
}
%>
<!-- 
	 <input type="button" value="重命名" onClick="rename()">
-->

<input type="button" value="返回" onClick="top.close()">
<input type="button" value="清空选项" onClick="cleaner()">
<%
if(fileFlag != null && (fileFlag.equals("pic")||fileFlag.equals("picOrFlash")))
{
%>
<select name="viewtype" class="cms_select" onchange="change();" style="width:80px">
	<option value="">--查看--</option>
	<option value="ppt" <%if("ppt".equals(viewertype)){out.print("selected");}%>>幻灯片</option>
	<option value="thumbnail" <%if("thumbnail".equals(viewertype)){out.print("selected");}%>>缩略图</option>
	<option value="list" <%if(viewertype == null || "list".equals(viewertype)){out.print("selected");}%>>列表</option>
</select>
<%
}
%>
</form>
<iframe id="hiddenFrm" name="hiddenFrm" style="display:none"></iframe>
</body>
</html>
