<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.db.ResManagerImpl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.ResManager"%>
<%
AccessControl accesscontroler = AccessControl.getInstance();
accesscontroler.checkAccess(request, response);
%>

<%
ResManager resMan = new ResManagerImpl();
String type = (String)request.getParameter("type");
   
	 resMan.delRedundance(type);
	
%>

