
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ page import="com.frameworkset.platform.security.AccessControl,
				 com.frameworkset.platform.sysmgrcore.manager.*,
				 com.frameworkset.platform.sysmgrcore.entity.User,
				 com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%

	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request,response);	
	String userId = request.getParameter("userId");
	
	UserManager userManager = SecurityDatabase.getUserManager();
	User user = userManager.getUserById(userId);
	String username = user.getUserName();
%>

<html>
	<head>
	<title>:::::用户(<%=username%>)权限设置</title>
	<link rel="stylesheet" type="text/css" href="../../sysmanager/css/treeview.css">
	<link rel="stylesheet" type="text/css" href="../../sysmanager/css/windows.css">
	
	</head> 
	<frameset name="frame1" cols="25%,*" frameborder="no" border="0" framespacing="0" >
	  <frame src="userResdefault.jsp?userId=<%=userId%>" name="orgTree" id="orgTree" scrolling="yes"/>
	  <frameset name="frame2" rows="26%,*" frameborder="yes" border="1" framespacing="1" >
	    <frame src="operList_global.jsp" name="globalOperList" id="globalOperList" scrolling="yes" />
	    <frame src="operdefault.jsp" name="operList" scrolling="yes" id="orgList" />
	  </frameset>
	</frameset>
	<noframes>
		<body>
		</body>
	</noframes>
</html>
