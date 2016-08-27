<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../include/global1.jsp"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.web.struts.action.RoleManagerAction"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogGetNameById"%>
<%
	
		//---------------START--角色管理写操作日志
		AccessControl control = AccessControl.getInstance();
		control.checkManagerAccess(request,response);
		String operContent="";        
        String operSource=control.getMachinedID();
        String openModle="角色管理";
        String userName = control.getUserName();
        String description="";
        LogManager logManager = SecurityDatabase.getLogManager(); 		
		//---------------END
		String orgId = request.getParameter("orgId");
		String resId = request.getParameter("resId");
		String roleId = request.getParameter("roleId");
		String opId = request.getParameter("opId");
		String Flag = request.getParameter("flag");
		String role_type = request.getParameter("role_type");
		//--
		String orgName_log = LogGetNameById.getOrgNameByOrgId(orgId);
		String resName_log = LogGetNameById.getResNameByResId(resId);
		String roleName_log = LogGetNameById.getRoleNameByRoleId(roleId);		
		operContent="保存角色操作信息,角色："+roleName_log+" 资源："+resName_log+" 机构："+orgName_log;
		description="";
		logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description);          
		//--	end	 --
		out.print(RoleManagerAction.saveURRO(orgId,resId,roleId,opId,Flag,role_type));	
%>
