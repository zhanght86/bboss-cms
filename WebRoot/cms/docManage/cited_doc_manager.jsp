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
	
	String siteid = request.getParameter("siteid");
	String channelid = request.getParameter("channelid");
	String sitename = request.getParameter("sitename");
	String channelname = request.getParameter("channelname");
%>
<html>
<head>
<title>【<%=channelname%>】频道  引用文档管理:::::::::::::::::::::::::::::::::::::::::::::::::::::..</title>
</head>
<frameset rows="20%,*" border=0>
	<frame border=0 scrolling=auto noresize name="citedDocQueryF" src="<%=rootpath%>/cms/docManage/cited_doc_query.jsp?siteid=<%=siteid%>&channelid=<%=channelid%>"></frame>
	<frame border=0 scrolling=auto noresize name="citedDocListF" src="<%=rootpath%>/cms/docManage/cited_doc_list.jsp?siteid=<%=siteid%>&channelid=<%=channelid%>"></frame>		
</frameset>
</html>