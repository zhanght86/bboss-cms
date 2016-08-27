<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.web.struts.form.UserOrgManagerForm" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.User" %>
<%@page import="com.frameworkset.common.poolman.DBUtil"%>

<%
	String userId=request.getParameter("userId");
	String orgId = request.getParameter("orgId");
	UserManager userManager = SecurityDatabase.getUserManager();
	User user = userManager.getUserById(userId);	
	String userName = user.getUserRealname();
	
	String sql ="select count(*) from td_sm_userrole where user_id ="+ userId +" and role_id='1'";
	try{
		DBUtil db = new DBUtil();
		db.executeSelect(sql);
		if(db.getInt(0,0)>0){
%>
	<SCRIPT LANGUAGE="JavaScript">
		alert("此用户是系统管理员，不能把权限复制给其他用户！"); 
		window.close();
		//getPropertiesContent().location.href="../user/userManager.do?method=getUserList&orgId=<%=orgId%>";
	</script>
<%}else{%>

<html>
<head>
<title>用户【<%=userName%>】权限复制</title>
<link rel="stylesheet" type="text/css" href="../css/treeview.css">
<%@ include file="/include/css.jsp"%>

</head> 
<frameset  name="userId" value="<%=userId%>" cols="30,70,0" frameborder="no" border="0" framespacing="0" >
  <frame src="orgTreecopy.jsp?userId=<%=userId%>&orgId=<%=orgId%>" name="orgTree" id="orgTree" />
  <frame src="noOrg.jsp" name="userList" scrolling="No" noresize="noresize" id="orgList" />
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

