<%     
  response.setHeader("Cache-Control", "no-cache"); 
  response.setHeader("Pragma", "no-cache"); 
  response.setDateHeader("Expires", -1);  
  response.setDateHeader("max-age", 0); 
%>
<%@ include file="../../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../base/scripts/panes.jsp"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.web.struts.form.UserOrgManagerForm" %>
<%
	
%>
<html>
<head>
<title>系统管理</title>
<link rel="stylesheet" type="text/css" href="../../css/treeview.css">
<%@ include file="/include/css.jsp"%>

</head> 
<frameset name="frame1" cols="26,74" frameborder="no" border="0" framespacing="0" >
  <frame src="resdefault.jsp" name="orgTree" id="orgTree" />
  <frameset name="frame2" rows="26,74" frameborder="yes" border="1" framespacing="1" >
    <frame src="operList_global.jsp" name="globalOperList" id="globalOperList" scrolling="No" />
    <frame src="operdefault.jsp" name="operList" scrolling="No" id="orgList" />
  </frameset>
</frameset>
<noframes>
<body>
</body>
</noframes>
</html>
