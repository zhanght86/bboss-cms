<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="com.frameworkset.platform.cms.templatemanager.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title></title>
</head>
<body>
<%
String siteId = request.getParameter("siteId");
String uri = request.getParameter("uri");
String action = request.getParameter("action");
try{
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request, response);
	String userId = ""+control.getUserID();
	FileManager fm = new FileManagerImpl();
	if("checkout".equals(action)){
		fm.checkOutFile(siteId,uri,userId);
	}else if("checkin".equals(action)){
		fm.checkInFile(siteId,uri,userId);
	}else{%>
		<script language="javascript">
			alert("请提供操作类型!");
		</script>
	<%
		return;
	}%>
	<script language="javascript">
		parent.location.reload();
	</script>
<%}catch(Exception e){
	out.println("<script language=\"javascript\">");
	out.println("alert('"+e.getMessage()+"');");
	out.println("parent.location.reload();");
	out.println("</script>");
	e.printStackTrace();
}
%>
</body>
</html>
