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
		String resName=request.getParameter("resName");
		String role_type=request.getParameter("role_type");
		//--
		String resName_log = LogGetNameById.getResNameByResId(resId);
		String roleName_log = LogGetNameById.getRoleNameByRoleId(roleId);
		operContent="修改角色资源,资源："+resName_log+" 角色："+ roleName_log;						
		description="";
		logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description);          
		//--	end	 --
		out.print(RoleManagerAction.storeOperListAjax(resId,resTypeId,opId,roleId,Flag,resName,role_type));	
	 
%>
