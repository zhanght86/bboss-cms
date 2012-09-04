<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.web.struts.form.UserOrgManagerForm" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.User" %>

<%
	String userId=request.getParameter("userId");
	String orgId = request.getParameter("orgId");
	UserManager userManager = SecurityDatabase.getUserManager();
	User user = userManager.getUserById(userId);	
	String userName = user.getUserRealname();
      
%>

<html>
<head>
<title>用户【<%=userName%>】隶属组设置</title>
<link rel="stylesheet" type="text/css" href="../css/treeview.css">
<%@ include file="/include/css.jsp"%>

</head> 
<frameset name="userId" value="<%=userId%>" cols="30,70,0" frameborder="no" border="0" framespacing="0" >
  <frame src="groupTree.jsp?userId=<%=userId%>&orgId=<%=orgId%>" name="groupTree" id="groupTree" />
  <frame src="noGroup.jsp" name="groupList" scrolling="No" noresize="noresize" id="groupList" />
  <frame src="#" name="orgId" value="<%=orgId%>" />
</frameset>
<noframes >

</noframes>
</html>

