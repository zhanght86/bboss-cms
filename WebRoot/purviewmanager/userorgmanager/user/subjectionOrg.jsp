<%
/*
 * <p>Title: 隶属机构页面</p>
 * <p>Description: 隶属机构页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-3-22
 * @author liangbing.tao
 * @version 1.0
 */
%>
 
 

<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>

<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.User"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
				
				
				
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkManagerAccess(request,response);
	
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
</html>

