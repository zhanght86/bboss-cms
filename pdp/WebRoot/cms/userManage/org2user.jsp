
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>

<html>
<head>
<title>::::::::::添加用户</title>

<%
	
	String roleId = request.getParameter("roleId");
  	
%>
</head> 
<frameset name="frame1" cols="30,70" frameborder="no" border="0" framespacing="0" >
  <frame src="orguserTree.jsp?roleId=<%=roleId%>" name="orgTree" id="orgTree" />
  <frame src="userList_ajax.jsp?roleId=<%=roleId%>" name="userList" scrolling="No" noresize="noresize" id="orgList" />
</frameset>
<noframes>
<body>
</body>
</noframes>
</html>
