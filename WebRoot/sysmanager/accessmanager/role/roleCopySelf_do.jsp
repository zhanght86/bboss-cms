<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.RoleManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Role" %>
<%@ page import="com.frameworkset.platform.security.AccessControl,
				com.frameworkset.platform.sysmgrcore.manager.LogManager"%>

<%
	AccessControl control = AccessControl.getInstance();
	control.checkManagerAccess(request,response);
	String operContent="";        
    String operSource=control.getMachinedID();
    String openModle="角色管理";
    String userName = control.getUserName();
    String description="";
    LogManager logManager = SecurityDatabase.getLogManager(); 		
	//---------------END
    
	String rolecopyId = request.getParameter("rolecopyId");
	String checks = request.getParameter("checks");
	String roleName = request.getParameter("roleName");
	String[] id = checks.split(",");
	request.setAttribute("roleId", rolecopyId);
	RoleManager roleManager = SecurityDatabase.getRoleManager();
	boolean state = roleManager.copyResOfRoleSelf(rolecopyId,id);
	//--角色管理写操作日志	
	operContent="复制角色: "+	roleName;			
	description="";
	logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description);
%>

<script language="javascript">
<%if(state){%>
	alert("复制权限成功！");
	parent.window.location.href=parent.window.location.href;
<%}else{%>
	alert("复制权限失败！");
<%}%>
</script>
