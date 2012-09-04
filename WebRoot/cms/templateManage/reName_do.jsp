<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.cms.templatemanager.*" %>
<%@ page import="com.frameworkset.platform.cms.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
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
	
	String uri = request.getParameter("uri");
	String oldFileName = request.getParameter("oldFileName");
	String fileName = request.getParameter("fileName");
    FileManager fm = new FileManagerImpl();
	fm.reName(siteId,uri,userId,oldFileName,fileName);
	
	
}catch(Exception e){
%>
	<script type="text/javascript">
		alert("给文件(文件夹)改名时发生异常,<%=e.getMessage()%>");
		//parent.win.parent.location = parent.win.parent.location;
		//刷新右边的树的语句
		//parent.win.open("navigator_content.jsp","perspective_toolbar");
		top.close();
	</script>	
<%	e.printStackTrace();
	return;
}
if(viewtype != null && viewtype.equals("other"))
{
%>
<script type="text/javascript">
	alert("给文件(文件夹)改名成功!");
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
	alert("给文件(文件夹)改名成功!");
	parent.win.parent.location = parent.win.parent.location;
	//刷新右边的树的语句
	parent.win.open("navigator_content.jsp","perspective_toolbar");
	top.close();
</script>
<%
}
%>
</body>
</html>