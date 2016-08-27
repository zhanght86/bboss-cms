<%@ page contentType="text/html;charset=UTF-8" language="java" import="java.util.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%@ include file="../../sysmanager/include/global1.jsp"%>
<%
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);
	
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	String docId = request.getParameter("docId");
%>
<html>
<head>
<title>.::::::::::视频播放记录:::::::::::::::::::::::::::::::::::::::::::::::::::::..</title>
</head>
<frameset rows="20%,*" border=0>
	<frame border=0 scrolling=auto noresize name="docPlayedQueryF" src="doc_played_query.jsp?docId=<%=docId%>"></frame>
	<frame border=0 scrolling=auto noresize name="docPlayedDocListF" src="doc_played_list.jsp?docId=<%=docId%>"></frame>
</frameset>
</html>