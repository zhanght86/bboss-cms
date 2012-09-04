<%
/*
 * <p>Title: 角色授予用户主框架</p>
 * <p>Description: 角色授予用户主框架</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: chinacreator</p>
 * @Date 2008-3-24
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

<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>

<%
	AccessControl accessControl = AccessControl.getInstance();
	accessControl.checkManagerAccess(request,response);
	
	String roleId = (String) request.getParameter("roleId");
	
	RoleManager roleManager = SecurityDatabase.getRoleManager();
	Role role = roleManager.getRoleById(roleId);
	String roleName = role.getRoleName();
%>
<html>
<head>
<title><pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.user.param" arguments="<%=roleName %>"/></title>

</head> 
	<frameset name="frame1" cols="30,70" frameborder="no" border="0" framespacing="0">
  		<frame src="orgTree.jsp?roleId=<%=roleId%>" name="orgTree" id="orgTree" />  
  		<frame src="userList_ajax.jsp?roleId=<%=roleId%>" name="userList" scrolling="No" noresize="noresize" id="orgList" />
	</frameset>
	<noframes>
		<body>
		</body>
	</noframes>
</html>

