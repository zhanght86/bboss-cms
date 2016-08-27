<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="java.io.*" %>
<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.AccessControl" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
<title></title>
<%
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request, response);

    response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);
	
	String viewertype = request.getParameter("viewertype");
    if(viewertype != null)
	{
		session.removeAttribute("viewertype");
		session.setAttribute("viewertype",viewertype);
	}
	else
	{
		session.removeAttribute("viewertype");
		//session.setAttribute("viewertype","list");
	}
%>
<SCRIPT LANGUAGE="JavaScript">
	parent.parent.ImageListFrm.location.href = parent.parent.ImageListFrm.location.href;
</SCRIPT>
</html>
