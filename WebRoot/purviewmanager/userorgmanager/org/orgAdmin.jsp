<%
/**
 * <p>Title: 机构管理员设置主页面</p>
 * <p>Description: 机构管理员设置主页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: chinacreator</p>
 * @Date 2008-3-17
 * @author da.wei
 * @version 1.0
 **/
 %>
 <%     
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0); 
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase,
		com.frameworkset.platform.sysmgrcore.manager.OrgManager,
		com.frameworkset.platform.sysmgrcore.entity.Organization,com.frameworkset.platform.security.AccessControl" %>
<%
AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkManagerAccess(request,response);
String orgId1=request.getParameter("orgId");//获取需要设置机构管理员的机构id
OrgManager orgManager = SecurityDatabase.getOrgManager();
Organization org = orgManager.getOrgById(orgId1);
//String PAGE_QUERY_STRING = request.getParameter("PAGE_QUERY_STRING");
//System.out.println("PAGE_QUERY_STRING = " + PAGE_QUERY_STRING);
String orgName = "";
if(org.getRemark5() != null && !"".equals(org.getRemark5())){
	orgName = org.getRemark5();
}else{
	orgName = org.getOrgName();
}
%>
<html>
<head>
<title>为机构【<%=orgName%>】设置管理员</title>
<%@ include file="/include/css.jsp"%>
<link rel="stylesheet" type="text/css" href="../../css/treeview.css">
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

