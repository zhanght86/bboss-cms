<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../include/global1.jsp"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.web.struts.action.RoleManagerAction"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%
	
		

		String resTypeId = request.getParameter("resTypeId");
		String resId = request.getParameter("resId");
		String opId = request.getParameter("opId");
		String roleId = request.getParameter("roleId");
		String Flag = request.getParameter("flag");
		String isRecursion = request.getParameter("isRecursion");
		String resName=request.getParameter("resName");
		String role_type = (String)session.getAttribute("role_type");
		String roleIds[] =roleId.split("\\,");
		
		//System.out.println("resTypeId..........."+resTypeId);
		//System.out.println("resId..........."+resId);
		//System.out.println("roleId..........."+roleId);
		//System.out.println("resName..........."+resName);
		//System.out.println("role_type..........."+role_type);
		//System.out.println("opId..........."+opId);
					
		out.print(RoleManagerAction.storeChannelAjax(resId,resTypeId,opId,roleIds,Flag,isRecursion,resName,role_type));	
%>
