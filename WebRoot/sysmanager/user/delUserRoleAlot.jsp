<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../include/global1.jsp"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>


<%
	
    String roleId = request.getParameter("roleDelId");	
    String id = request.getParameter("id");	
   // System.out.println("id" + id);
	//System.out.println("roleId" + roleId);
	if(roleId!=null){
		String roleIds[] =roleId.split("\\,") ;
		String idso[] =id.split("\\,") ;
		//System.out.println("liumeiyu" + roleIds[0]);
		UserManager userManager = SecurityDatabase.getUserManager();
		userManager.delAlotUserRole(idso,roleIds);
		
	 }
    
	
%>
