

<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>

<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkManagerAccess(request,response);
	
	String userId = request.getParameter("userId");
	String orgId = request.getParameter("orgId");
	
	String siteUrl = "siteTree.jsp?resTypeId=template&currRoleId="+userId+"&currOrgId="+orgId+"&role_type=user";

%>
<html>
<frameset name="templateFrame" cols="30,70" frameborder="no" border="0" framespacing="0" >
  	<frame src="<%=siteUrl%>" name="globalOperList" id="globalOperList" scrolling="No" noresize="noresize" />
  	<frame src="operdefault.jsp" name="operList" scrolling="No" noresize="noresize" id="orgList" />
</frameset>
<noframes>
<body>
</body>
</noframes>
</html>
