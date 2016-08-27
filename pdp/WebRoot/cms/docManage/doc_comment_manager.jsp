<%@ page contentType="text/html;charset=UTF-8" language="java" import="java.util.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.cms.documentmanager.*"%>
 
<%
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);
	
	
	String docId = request.getParameter("docId");
%>
<html>
<head>
<title>.::::::::::文档评论管理:::::::::::::::::::::::::::::::::::::::::::::::::::::..</title>
</head>
<frameset rows="20%,*" border=0>
	<frame border=0 scrolling=auto noresize name="docCommentQueryF" src="doc_comment_query.jsp?docId=<%=docId%>"></frame>
	<frame border=0 scrolling=auto noresize name="docCommentDocListF" src="doc_comment_list.jsp?docId=<%=docId%>"></frame>		
</frameset>
</html>