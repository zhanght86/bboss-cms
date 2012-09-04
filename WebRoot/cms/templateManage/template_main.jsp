<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="com.frameworkset.platform.security.*"%>
<%
			AccessControl accesscontroler = AccessControl.getInstance();
			accesscontroler.checkAccess(request, response);
		
			String siteId = request.getParameter("siteId");
%> 
<html>
<head>
<style type="text/css">
<!--
.border{
	border-bottom-width: 1px;
	border-bottom-style: solid;
	border-bottom-color: #cccc;
}
-->
</style>
</head>
<frameset rows="90,*" border=0>
	<frame frameborder="0" id="queryFrame" scrolling="no" name="queryFrame" src="queryFrame.jsp?siteId=<%=siteId%>" class="border">	
	<frame frameborder="0" scrolling="auto" id="TplListFrame" name="TplListFrame" src="template_list.jsp?siteId=<%=siteId%>&action=getAll&name=&creatorUser=&type=">
	
</frameset>
<noframes></noframes>
</html>