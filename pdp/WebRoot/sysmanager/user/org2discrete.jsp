<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.web.struts.form.UserOrgManagerForm" %>
<html>
<head>
<title>用户调入机构</title>
<link rel="stylesheet" type="text/css" href="../css/treeview.css">
<%@ include file="/include/css.jsp"%>
<%
	//String uid=request.getParameter("uid");
	String uid=request.getParameter("userId");
	request.setAttribute("userId",uid);  
%>
</head> 
<frameset name="userId" value="<%=uid%>" cols="30,70" frameborder="no" border="0" framespacing="0" >
  <frame src="orgTree2.jsp?uid=<%=uid%>" name="orgTree" id="orgTree" />
  <frame src="noOrg.jsp" name="orgList" scrolling="No" noresize="noresize" id="orgList" />
</frameset>
<noframes>
<body>
</body>
</noframes>
</html>

