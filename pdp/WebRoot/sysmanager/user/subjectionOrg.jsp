<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager" %>
<%@page import="com.frameworkset.platform.sysmgrcore.entity.*,com.frameworkset.platform.sysmgrcore.web.struts.form.*"%>
<%
	String userId = request.getParameter("userId");
	String orgId = request.getParameter("orgId");
	UserManager userManager = SecurityDatabase.getUserManager();
	User user = userManager.getUserById(userId);
    String userName= user.getUserRealname();
%>
<html>
  <head>
  <title>用户【<%=userName%>】隶属机构</title>
  </head>
<frameset rows="*" cols="100%" framespacing="3" frameborder="yes" border="5" >
<frame src="changeOrg_ajax.jsp?userId=<%=userId%>&orgId=<%=orgId%>" name="subjection" id="subjection">
</frameset>
<body>
</body>
</html>

