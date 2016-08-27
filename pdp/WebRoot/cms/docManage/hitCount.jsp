<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.cms.documentmanager.*"%>
<html>
<head><title>文档点击</title></head>

<%
	String docId = request.getParameter("docId");
	DocumentManagerImpl imp = new DocumentManagerImpl();
	imp.addCount(docId);
%>
</html>