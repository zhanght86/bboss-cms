<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="com.frameworkset.platform.security.*"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);

	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);

	String resId2 = request.getParameter("resId2");
	String resTypeId2 = request.getParameter("resTypeId2");
	String resTypeName = request.getParameter("resTypeName");
	String title = request.getParameter("title");
	String isGlobal=request.getParameter("isGlobal");
	if(isGlobal == null) isGlobal = "false";
%>
<html>
<head>
<title>授予角色</title>
</head>
<iframe src="../../sysmanager/resmanager/hasPower_ajax.jsp?isGlobal=<%=isGlobal%>&resId2=<%=resId2%>&resTypeId2=<%=resTypeId2%>&resTypeName=<%=resTypeName%>&title=<%=title%>"  border=0 scrolling="no" id="docVerListFrame" name="docVerListFrame" height="100%" width="100%"></iframe>
</html>