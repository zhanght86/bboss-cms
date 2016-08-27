<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="java.io.*" %>
<%@ page import="com.frameworkset.platform.cms.*" %>
<%@ page import="com.frameworkset.platform.cms.templatemanager.*" %>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
</head>
<body>
<%
String viewtype = request.getParameter("viewtype"); 
try{
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request, response);
	CMSManager cmsmanager = new CMSManager();
	cmsmanager.init(request,session,response,control);
	String userId = control.getUserID();
	String siteId =  cmsmanager.getSiteID();
	String pathContext = null;	
	String rootPath = "";
	//if(siteId!=null && siteId.trim().length()!=0){
	//	String temp = (new SiteManagerImpl()).getSiteAbsolutePath(siteId);
	//	if(temp!=null && temp.trim().length()!=0){
	//		pathContext = new File(temp,"_template").getAbsolutePath();
	//	}
//	}
	rootPath = request.getRealPath("/");
	rootPath = rootPath.replace('\\','/');
	pathContext = rootPath + "cms/voteManager/images";
	if(pathContext==null || pathContext.trim().length()==0){
%>
		<script type="text/javascript">
			alert("没有提供资源的上下文路径,无法管理资源.");
			top.close();
		</script>
<%
		return;
	}
	String uri = request.getParameter("uri");
	System.out.println(uri);
	String files = request.getParameter("files");
	System.out.print(files);
	if(files==null||files.trim().length()==0){
%>
		<script type="text/javascript">
			alert("请选择你要删除的文件(夹).");
			top.close();
		</script>
<%		
		return;		
	}
	
	String[] fileNames = files.split("<>");
	if(uri==null){
		uri = "";
	}else{
		uri = uri.replace('\\','/');
		if(uri.startsWith("/")){
			uri = uri.substring(1);
		}
		if(!uri.endsWith("/")){
			uri = uri+"/"; 
		}
		if("/".equals(uri.trim())){
			uri = "";
		}
	}
	FileManager fm = new FileManagerImpl();
	for(int i=0;i<fileNames.length;i++){
		fm.deleteFiles(siteId,uri+fileNames[i],userId);
	}	
}catch(Exception e){
%>
	<script type="text/javascript">
		alert("删除文件时发生异常,<%=e.getMessage()%>");
		top.close();
	</script>	
<%	e.printStackTrace();
	return;
}
if(viewtype != null && viewtype.equals("other"))
{
%>
<script type="text/javascript">
	alert("删除文件成功!");
	var urlstr = parent.window.dialogArguments.parent.ImageListFrm.location.href;
	parent.window.dialogArguments.parent.ImageListFrm.location.href = urlstr;
	window.close();
</script>
<%
}
else
{
%>
<script type="text/javascript">
	alert("删除文件成功!");
	window.dialogArguments.reloadView();
	window.dialogArguments.open("navigator_content.jsp","perspective_toolbar");
	top.close();
</script>
<%
}
%>
</body>
</html>