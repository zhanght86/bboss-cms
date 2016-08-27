
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ page import="com.frameworkset.platform.security.AccessControl,com.frameworkset.platform.sysmgrcore.manager.*,
com.frameworkset.platform.sysmgrcore.entity.User,com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%

	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request,response);
%>
<html>
<head>
<title>:::::CMS菜单权限设置</title>
<link rel="stylesheet" type="text/css" href="/sysmanager/css/treeview.css">
<%@ include file="/include/css.jsp"%>

</head> 
<frameset name="frame1" cols="30,70" frameborder="no" border="0" framespacing="0" >
  <frame src="resColumnCmsTree.jsp" name="menuTree" id="menuTree" />
  <frame src="default_right.jsp" name="menuList" scrolling="No" noresize="noresize" id="orgList" />
</frameset>
<noframes>
<body>
</body>
</noframes>
</html>

