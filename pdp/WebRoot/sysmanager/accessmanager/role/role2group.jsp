<%@ include file="../../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../base/scripts/panes.jsp"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.web.struts.form.UserOrgManagerForm" %>
<html>
<head>
<title>系统管理</title>
<link rel="stylesheet" type="text/css" href="../../css/treeview.css">
<%@ include file="/include/css.jsp"%>
<%
	String roleId=request.getParameter("roleId");
	
      
%>
</head> 
<frameset name="frame1" cols="30,70" frameborder="no" border="0" framespacing="0" >
  <frame src="groupTree.jsp?roleId=<%=roleId%>" name="groupTree" id="groupTree" />
  <frame src="changeGroup_ajax.jsp?roleId=<%=roleId%>" name="groupList" scrolling="No" noresize="noresize" id="groupList" />
</frameset>
<noframes>
<body>
</body>
</noframes>
</html>

