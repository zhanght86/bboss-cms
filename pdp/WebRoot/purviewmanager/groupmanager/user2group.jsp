<%
/*
 * <p>Title: 用户隶属组界面</p>
 * <p>Description: 用户隶属组界面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-3-18
 * @author liangbing.tao
 * @version 1.0
 */
%>

<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.User" %>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%     
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0); 
%>

<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkManagerAccess(request,response);
	
	String userId=request.getParameter("userId");
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
  		<frame src="showGroupTree.jsp?userId=<%=userId%>" name="groupTree" id="groupTree" />
  		<frame src="noGroup.jsp" name="groupList"  id="groupList" />
	</frameset>
</html>

