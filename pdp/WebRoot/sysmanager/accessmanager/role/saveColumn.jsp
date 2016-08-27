<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../include/global1.jsp"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.web.struts.action.RoleManagerAction"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogGetNameById"%>
<%
	
		//---------------START--
		AccessControl control = AccessControl.getInstance();
		control.checkManagerAccess(request,response);
		String operContent="";        
        String operSource=control.getMachinedID();
        String openModle="岗位管理";
        String userName = control.getUserName();
        String description="";
        LogManager logManager = SecurityDatabase.getLogManager(); 		
		//---------------END

		String resTypeId = request.getParameter("resTypeId");
		String resId = request.getParameter("resId");
		String opId = request.getParameter("opId");
		String roleId = request.getParameter("roleId");
		String Flag = request.getParameter("flag");
		String isRecursion = request.getParameter("isRecursion");
		String resName=request.getParameter("resName");
		String menuPath  = request.getParameter("menuPath");
		String role_type = request.getParameter("role_type");
		String roleIds[] =roleId.split("\\,");
		
		//--
		//String resName_log = SecurityDatabase.getResourceManager().getRes("resId","").getRestypeName();
		String roleNames_log = LogGetNameById.getRoleNamesByRoleIds(roleIds);
		operContent="资源操作授予-菜单授权,资源名称:"+resName+",角色："+roleNames_log; 						
		description="";
		logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description);          		
		//--	end	 --
			
		out.print(RoleManagerAction.storeColumnAjax(resId,resTypeId,opId,roleIds,Flag,isRecursion,resName,menuPath,role_type));	
	 
%>
