
<%@page import="com.frameworkset.platform.sysmgrcore.manager.db.OrgAdministratorImpl"%><%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ page import="com.frameworkset.platform.security.AccessControl" %>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	
	String idString = request.getParameter("idString");
	String type = request.getParameter("type");
	
	//当前用户是否拥有超级管理员权限与部门管理员权限
	boolean isAdminOrOrgManager = false;
	//是否是管理员
	boolean isOrgManager = new OrgAdministratorImpl().isOrgManager(accesscontroler.getUserID());
	boolean isAdmin = accesscontroler.isAdmin();
	if(isAdmin || isOrgManager){
		isAdminOrOrgManager = true;
	}
%>
<html>
<head>
<title>资源与机构可见操作授予</title>
<link rel="stylesheet" type="text/css" href="../css/treeview.css">
<%@ include file="/include/css.jsp"%>

</head> 
<%if(isAdminOrOrgManager){ %>
<frameset name="urFrame" id="urFrame" value="" cols="50%,*" border="1" framespacing="2" >
	<frameset name="saveOrgFrame" rows="11%,*" border="0" framespacing="0">
		<frame src="saveOrgGrant.jsp" name="saveOrgGrant" id="saveOrgGrant" marginWidth=0 marginHeight=0 />
		<frame src="orgChooseTree.jsp?idString=<%=idString%>&type=<%=type%>" name="orgChooseTree" id="orgChooseTree" marginWidth=0 marginHeight=0 />
	</frameset>
	<frameset name="saveMenuFrame" rows="11%,*" border="0" framespacing="0">
		<frame src="saveMenuGrant.jsp" name="saveMenuGrant" id="saveMenuGrant" marginWidth=0 marginHeight=0 />
		<frame src="menuTree.jsp?idString=<%=idString%>&type=<%=type%>" name="menuTree" id="menuTree" marginWidth=0 marginHeight=0 />
	</frameset>
</frameset>
<noframes>
<body>
</body>
</noframes>
<%}else{ %>
<body>
<div align="center">没有权限！请与系统管理员联系</div>
</body>
<%} %>
</html>

