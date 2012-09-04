<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ page import="com.frameworkset.platform.security.AccessControl,com.frameworkset.platform.sysmgrcore.manager.*,
com.frameworkset.platform.sysmgrcore.entity.Role,com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%

	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request,response);	
	String roleId = request.getParameter("roleId");
	
	RoleManager roleManager = SecurityDatabase.getRoleManager();
	Role role = roleManager.getRoleById( roleId);
	String rolename = role.getRoleName();
%>
<html>
<head>
<title>:::::角色(<%=rolename%>)权限设置</title>
<link rel="stylesheet" type="text/css" href="../../sysmanager/css/treeview.css">
<link rel="stylesheet" type="text/css" href="../../sysmanager/css/windows.css">

</head> 
<frameset name="frame1" cols="30,70" frameborder="no" border="0" framespacing="0" >
  <frame src="resdefault.jsp?roleId=<%=roleId%>" name="orgTree" id="orgTree" />
  <frameset name="frame2" rows="26,74" frameborder="yes" border="1" framespacing="1" >
    <frame src="operList_global.jsp" name="globalOperList" id="globalOperList" scrolling="No" />
    <frame src="operdefault.jsp" name="operList" scrolling="No" id="orgList" />
  </frameset>
</frameset>
<noframes>
<body>
</body>
</noframes>
</html>
