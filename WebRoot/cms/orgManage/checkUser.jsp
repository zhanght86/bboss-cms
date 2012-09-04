<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.User"%>

<%
	String userName = request.getParameter("userName");	
	UserManager userManager = SecurityDatabase.getUserManager();
	User user = userManager.getUserByName(userName);
	if(user == null){
		out.println("1");
	}
	else{
		out.println("0");
	}
%>

