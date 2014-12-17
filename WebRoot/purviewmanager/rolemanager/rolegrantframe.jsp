
<%@page import="com.frameworkset.platform.config.ConfigManager"%><%
/*
 * <p>Title: 角色授予查询操作框架</p>
 * <p>Description: 角色授予查询操作框架</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-4-14
 * @author liangbing.tao
 * @version 1.0
 */
 %>
 

<%     
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0); 
%>

<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.AccessControl,
				com.frameworkset.platform.sysmgrcore.manager.RoleManager,
				com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase,
				com.frameworkset.platform.sysmgrcore.entity.Role"%>
				
<%@ page import="org.frameworkset.web.servlet.support.RequestContextUtils"%>

<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>	

<%
	request.setAttribute("userList", RequestContextUtils.getI18nMessage("sany.pdp.purviewmanager.rolemanager.role.authorize.to.user.list", request));
	request.setAttribute("orgList", RequestContextUtils.getI18nMessage("sany.pdp.purviewmanager.rolemanager.role.authorize.to.org.list", request));
	request.setAttribute("usergroupList", RequestContextUtils.getI18nMessage("sany.pdp.purviewmanager.rolemanager.role.authorize.to.usergroup.list", request));
	request.setAttribute("postList", RequestContextUtils.getI18nMessage("sany.pdp.purviewmanager.rolemanager.role.authorize.to.post.list", request));
%>

<%
	AccessControl accessControl = AccessControl.getInstance();
	accessControl.checkManagerAccess(request,response);
	
	String roleId = (String) request.getParameter("roleId");
	RoleManager roleManager = SecurityDatabase.getRoleManager();
	Role role = roleManager.getRoleById(roleId);
	String roleName = role.getRoleName();
	
	
	//角色用户查询列表
	String roleuserquery = "roleuserquery.jsp?roleId=" + roleId ;
	
	//角色机构查询列表
	String roleorgquery = "roleorgquery.jsp?roleId=" + roleId ;
	
	//角色用户组查询列表
	String rolegroupquery = "rolegroupquery.jsp?roleId=" + roleId;
	
	//角色机构岗位查询列表
	String roleOrgJobQuery = "roleOrgJobQuery.jsp?roleId=" + roleId;
%>

<html>
	<head>
		<title>授予角色【<%=roleName %>】查询</title>
	</head> 
	<tab:tabConfig/>
	<body>
		<table width="100%" border="0" cellpadding="0" cellspacing="0" align="center">
			<tr>
				<td colspan="1">	
					<tab:tabContainer id="role-grant" selectedTabPaneId="user-grant" skin="sany">
						<tab:tabPane id="user-grant" tabTitle="${userList}" lazeload="true">
							<tab:iframe id="usergrant" src="<%=roleuserquery%>" frameborder="0" scrolling="no" width="100%" height="540">
							</tab:iframe>
						</tab:tabPane>
						<% 
							if(ConfigManager.getInstance().getConfigBooleanValue("enableorgrole",false)){
						%>
						<tab:tabPane id="org-grant" tabTitle="${orgList}" lazeload="true">
							<tab:iframe id="orggrant" src="<%=roleorgquery%>" frameborder="0" scrolling="no" width="100%" height="540">
							</tab:iframe>
						</tab:tabPane>
						<% 
							}
						%>
						<%
							if(ConfigManager.getInstance().getConfigBooleanValue("enablergrouprole", false)){   
						%>
						<tab:tabPane id="group-grant" tabTitle="${usergroupList}" lazeload="true">
							<tab:iframe id="groupgrant" src="<%=rolegroupquery%>" frameborder="0" scrolling="no" width="100%" height="540">
							</tab:iframe>
						</tab:tabPane>
						<% 
							}
						%>
						<%
							if(ConfigManager.getInstance().getConfigBooleanValue("enablejobfunction", false)){
						%>
						<tab:tabPane id="orgjob-grant" tabTitle="${postList}" lazeload="true">
							<tab:iframe id="orgjobgrant" src="<%=roleOrgJobQuery%>" frameborder="0" scrolling="no" width="100%" height="540">
							</tab:iframe>
						</tab:tabPane>
						<% 
							}
						%>
						
					</tab:tabContainer>
				</td>
			</tr>
		</table>
	</body>
</html>

