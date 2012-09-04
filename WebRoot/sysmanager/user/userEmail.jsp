<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.User" %>

<%
	String userName = (String)request.getParameter("userName");
	try {
		UserManager userManager = SecurityDatabase.getUserManager();
		User user = userManager.getUserByName(userName);
		if(user.getUserEmail() != null && user.getUserEmail().length() >0){
			out.println(user.getUserEmail());
		}
		else{
			out.println("该用户暂无邮箱");
		}
	} catch (Exception e) {
		out.println("该用户暂无邮箱");
	}
	
	
%>

