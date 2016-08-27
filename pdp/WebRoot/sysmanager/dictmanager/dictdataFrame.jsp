<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.User" %>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	String did = request.getParameter("did");
%>
	
<html>
	<head>
	<title>数据字典数据项</title>
	
	</head> 
	<frameset name="dictdata"  cols="25,75" frameborder="no" border="0" framespacing="0" >
	  <frame src="dictdataTree.jsp?did=<%=did%>" name="dictdataTree" id="dictdataTree" />
	  <frame src="newDictdata.jsp?did=<%=did%>" name="newDictdata" scrolling="yes" noresize="noresize" id="newDictdata" />
	</frameset>
	<noframes>
	<body>
	</body>
	</noframes>
</html>


