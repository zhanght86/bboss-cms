<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.User" %>
<%@page import="com.frameworkset.common.poolman.DBUtil"%>
<%
	String userId = request.getParameter("userId");
	String orgId = request.getParameter("orgId");
	
	UserManager userManager = SecurityDatabase.getUserManager();
	User user = userManager.getUserById(userId);
	String userName = user.getUserRealname();
	DBUtil db = new DBUtil();
	
	String sql ="select count(*) from td_sm_userrole where user_id ="+ userId +" and role_id='1'";
	try{
		db.executeSelect(sql);
		if(db.getInt(0,0)>0)
		{
	
%>
	<SCRIPT LANGUAGE="JavaScript">
		alert("此用户是系统管理员，不需要对他授予资源权限！"); 
		window.close();
		//getPropertiesContent().location.href="../user/userManager.do?method=getUserList&orgId=<%=orgId%>";
	</script>
<%}else{%>
<html>
<head>
<title>用户【<%=userName%>】资源操作授予</title>
<link rel="stylesheet" type="text/css" href="../css/treeview.css">
<%@ include file="/include/css.jsp"%>

</head> 
<frameset name="userId" value="<%=userId%>" cols="30,70,0" frameborder="no" border="0" framespacing="0" >
  <frame src="user_resdefault.jsp?userId=<%=userId%>&orgId=<%=orgId%>" name="orgTree" id="orgTree" />
  <frameset name="userId" value="<%=userId%>" rows="30%,70%" frameborder="yes" border="1" framespacing="1" >
  	<frame src="../accessmanager/role/operList_global.jsp" name="globalOperList" id="globalOperList" scrolling="No" noresize="noresize" />
  	<frame src="user_operdefault.jsp?userId=<%=userId%>" name="operList" scrolling="No" noresize="noresize" id="orgList" />
  </frameset>
  <frame src="#" name="orgId" value="<%=orgId%>" />
</frameset>
<noframes>
<body>
</body>
</noframes>
</html>
<%
		}
	}
	catch(Exception e)
	{}	
%>


