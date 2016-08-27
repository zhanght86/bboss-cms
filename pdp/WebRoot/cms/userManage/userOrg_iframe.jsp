<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%
String userId = request.getParameter("userId");
AccessControl control = AccessControl.getInstance();
control.checkAccess(request,response);	
%>
<html>
<head>
<title>用户隶属机构</title>
</head>
<iframe src="../../sysmanager/user/userOrg.do?method=getOrgList&userId=<%=userId%>"  border=0 scrolling="no" id="docVerListFrame" name="docVerListFrame" height="100%" width="100%"></iframe>
</html>