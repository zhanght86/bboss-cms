<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="java.io.*" %>
<%@ include file="/sysmanager/include/global1.jsp"%>
<%@ include file="/sysmanager/base/scripts/panes.jsp"%>
<%@ page import="com.frameworkset.platform.security.AccessControl" %>
<%@ page import="com.frameworkset.platform.cms.*"%>
<%@ page import="com.frameworkset.platform.cms.util.*"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*" %>
<%@ page import="com.frameworkset.platform.cms.templatemanager.*" %>
<html>
<head>
<%
String path_= request.getContextPath();
    String basePath = request.getScheme() + "://"	+ request.getServerName() + ":" +
    request.getServerPort() + path_+ "/"; 
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="<%=basePath%>cms/inc/css/cms.css" rel="stylesheet" type="text/css">
<title></title>
<%
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request, response);

	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);
	
	String uri = request.getParameter("uri");
	
	//fileFlag为"1"表示首页文件的选择，为null或""表示为图片的选择
	String fileFlag = request.getParameter("fileFlag");
	//fileFlag = fileFlag == null?"pic":fileFlag;//默认为选择图片

	CMSManager cmsmanager = new CMSManager();
	cmsmanager.init(request,session,response,control);

	String siteId =  cmsmanager.getSiteID();

	String pathContext = null;
	String rootPath = "";
	//if(siteId!=null && siteId.trim().length()!=0){
	//	String temp = (new SiteManagerImpl()).getSiteAbsolutePath(siteId);
	//	if(temp!=null && temp.trim().length()!=0){
	//		pathContext = new File(temp,"_template").getAbsolutePath();
	//	}
	//}
	rootPath = request.getRealPath("\\");
	rootPath = rootPath.replace('\\','/');
	pathContext = rootPath + "cms/voteManager/images";
	if(pathContext==null || pathContext.trim().length()==0){
%>
		<script language="javascript">
			alert("没有找到站点所在路径.");
		</script>
<% 
		return;
	}
%>
<%
response.setContentType("text/html;charset=UTF-8");
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
   window.open("<%=pathContext%>/"+ url + fileName);
}
function preview1(fileName,abstractPth){
   var uri = "<%=uri==null?"/":uri%>";
	var url = uri.replace("\\","/");
	if(url.charAt(0) == '/'){
		url = url.substr(1); 
	}
	if(url.charAt(url.length-1) != '/' && url.search(/\S+/img)!=-1){
		url += "/";
	}
     var path="<%=pathContext%>/"+ url + fileName;
     alert(path);
     window.open("showimg.jsp?filepath="+path);
	
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
function test(o)
{
var uri = "<%=uri==null?"/":uri%>";
	var url = uri.replace("\\","/");
	if(url.charAt(0) == '/'){
		url = url.substr(1); 
	}
	if(url.charAt(url.length-1) != '/' && url.search(/\S+/img)!=-1){
		url += "/";
	}
	
     var path="<%=basePath%>cms/voteManager/images/"+ url + o.filename;
     window.open(path);
}
</script>
</head>
<body>
<table width="100%" border="1" cellspacing="0" cellpadding="0" bordercolordark="#FFFFFF" bordercolorlight="#eeeeee">
<%
	FileFilter fileFilter = null;//System.out.println(fileFlag);
	if(fileFlag != null && fileFlag.equals("media"))
	{
		fileFilter = new MediaFileFilter();
	}
	if(fileFlag != null && fileFlag.equals("pic"))
	{
		fileFilter = new ImageFileFilter();
	}
	if(fileFlag != null && fileFlag.equals("1"))
	{
       fileFilter = new IndexPageFilter(siteId,uri);
	}
	if(fileFlag != null && fileFlag.equals("file"))
	{
       fileFilter = new ResourceFilter();
	}
	File[] files = FileUtil.getSubFiles(pathContext,uri,fileFilter);
	for(int i=0;files!=null&&i<files.length;i++){
%>
	<tr onselectstart="return false" style="cursor:hand;height:25px"  class="cms_data_tr">
		<td  onclick="high(this,'<%=files[i].getName()%>')" name="picName" id = "picName" width="80%">
		<%=files[i].getName()%>
		</td>
		<%
			if(fileFlag != null && fileFlag.equals("pic"))//图片
			{
		%>
			<td width="20%">
			<script type="text/javascript">
            
           
			var uri = "<%=uri==null?"/":uri%>";
	        var url = uri.replace("\\","/");
	       if(url.charAt(0) == '/')
	         {
		        url = url.substr(1); 
	         }
	       if(url.charAt(url.length-1) != '/' && url.search(/\S+/img)!=-1)
	          {
		         url += "/";
	          }
              var path="<%=basePath%>cms/voteManager/images/" + "<%=files[i].getName()%>";
             var filename="<%=files[i].getName()%>";
             var imgstr='<image src="'+path+'" filename="'+filename+'" style=\"cursor:hand\" onclick=\"test(this)\" name=\"previewPic\" width=\"40\" height=\"30\" alt=\"预览\"></image>';
             document.write(imgstr);
			</script>
				
				
			</td>
		<%
			}
			if(fileFlag != null && fileFlag.equals("1"))//首页
			{
		%>
			<td width="20%">
				<a style="cursor:hand" onclick="preview('<%=files[i].getName()%>')">预览</a>
			</td>
		<%
			}
		%>
	</tr>
<%		
	}
%>	
</table>
</body>
</html>
