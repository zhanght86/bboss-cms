<%@page contentType="text/html; charset=UTF-8"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@page import="java.util.*" %>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>

<%@ include file="/common/jsp/importtaglib.jsp"%>
<%@ page import="com.frameworkset.platform.config.ConfigManager,
				com.frameworkset.platform.sysmgrcore.manager.db.OrgAdministratorImpl,
				com.frameworkset.platform.sysmgrcore.manager.OrgAdministrator"%>
<%
	AccessControl control = AccessControl.getInstance();
	control.checkManagerAccess(request,response);
		
	
	String curSystem = control.getCurrentSystem();
	
	OrgAdministrator orgAdministrator = new OrgAdministratorImpl();
	String curUserId = control.getUserID();
	List list = orgAdministrator.getManagerOrgsOfUserByID(curUserId);
	boolean isAdminOrOrgManager = false;//当前登陆用户是否具有部门管理员角色或者超级管理员角色
	if(list.size() > 0 || control.isAdmin()){//判断是否是部门管理员和拥有超级管理员角色
      	  isAdminOrOrgManager = true;
	}
%>
<HTML>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
	<%@ include file="/common/jsp/css.jsp"%>	
	<tab:tabConfig/>
	<title>权限管理</title>
</head>
<body >
<sany:menupath menuid="purviewmanager"/>
			<% 
			if(isAdminOrOrgManager){
			%>
			
				<tab:tabContainer id="user-manage-update" selectedTabPaneId="org-manage" skin="sany">
					<tab:tabPane id="org-manage"  tabTitleCode="sany.pdp.user.organization.manage" tabTitle="用户和机构管理"  lazeload="true">
						<tab:iframe id="orgmanage" src="userorgmanager/org/org_main.jsp" frameborder="0" scrolling="no" width="100%" height="95%">
						</tab:iframe>
					</tab:tabPane>
					<tab:tabPane id="role-manage" tabTitleCode="sany.pdp.role.manage" tabTitle="角色管理" lazeload="true">
						<tab:iframe id="rolemanage" src="rolemanager/role.jsp" frameborder="0" scrolling="no" width="99%" height="95%">
						</tab:iframe>
					</tab:tabPane>
					
					<!-- 岗位管理开关   2008-4-2 baowen.liu -->
					<%
						if(ConfigManager.getInstance().getConfigBooleanValue("enablejobfunction", false))
						{
					%>
					<tab:tabPane id="job-manage" tabTitleCode="sany.pdp.job.manage" tabTitle="岗位管理" lazeload="true">
						<tab:iframe id="jobmanage" src="jobmanager/jobinfo.jsp" frameborder="0" scrolling="no" width="99%" height="95%">
						</tab:iframe>
					</tab:tabPane>
					<%
						}
					%>
					
					<!-- 用户组开关-->
					<%
						if(ConfigManager.getInstance().getConfigBooleanValue("enablergrouprole", false))
						{   
					%>
					<tab:tabPane id="group-manage" tabTitleCode="sany.pdp.user.group.manage" tabTitle="用户组管理" lazeload="true">
						<tab:iframe id="groupmanager" src="groupmanager/group_main.jsp" frameborder="0" scrolling="no" width="99%" height="95%">
						</tab:iframe>
					</tab:tabPane>
					<%
						}
					%>
					
					<!-- 资源管理开关 -->
					<% 
						if(ConfigManager.getInstance().getConfigBooleanValue("enablerresmanaer", false))
						{
					%>
					<tab:tabPane id="res-manage" tabTitleCode="sany.pdp.resource.manage" tabTitle="资源管理" lazeload="true">
						<tab:iframe id="resmanage" src="resmanager/res_main.jsp" frameborder="0" scrolling="no" width="99%" height="95%" >
						</tab:iframe>
					</tab:tabPane>
					<% 
						}
					%>
					</tab:tabContainer>		
				
			<% 
			}else{
			%>
			<div align="center">没有权限！请与系统管理员联系</div>
			<% 
			}	
			%>	
			
		
<iframe name="exeman" width="0" height="0" style="display:none"></iframe>
</body>
</html>

