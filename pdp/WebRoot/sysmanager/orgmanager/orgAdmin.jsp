<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase,
		com.frameworkset.platform.sysmgrcore.manager.OrgManager,
		com.frameworkset.platform.sysmgrcore.entity.Organization" %>
<%
String orgId1=request.getParameter("orgId");//获取需要设置机构管理员的机构id
OrgManager orgManager = SecurityDatabase.getOrgManager();
Organization org = orgManager.getOrgById(orgId1);
String orgName = "";
if(org.getRemark5() != null && !"".equals(org.getRemark5())){
	orgName = org.getRemark5();
}else{
	orgName = org.getOrgName();
}
%>
<html>
<head>
<title>为部门【<%=orgName%>】设置部门管理员</title>
</head> 
<frameset name="frame1" cols="30%,*" frameborder="no" border="0" framespacing="0" >
  <frame src="orgAdminTree.jsp?orgId1=<%=orgId1%>" name="orgTree" id="orgTree" />
  <frame src="changeOrgAdmin.jsp?orgId1=<%=orgId1%>" name="userList" scrolling="No" noresize="noresize" id="orgList" />
</frameset>
<noframes>
<body>
</body>
</noframes>
<input type="hidden" name="orgId1" value="<%=orgId1%>" />
</html>

