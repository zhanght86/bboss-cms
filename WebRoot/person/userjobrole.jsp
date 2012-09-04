<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="../test/include/MyJsp.jsp" %>

<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ page import="java.util.List" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.UserJobRole" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.RoleManager" %>
<%@ page import="com.frameworkset.platform.security.AccessControl" %>
<%	
	AccessControl accessControl = AccessControl.getInstance();
    accessControl.checkAccess(request,response);
    
    String uid = accessControl.getUserID();
%>
<html>
<head>    
<title></title>
<%@ include file="/include/css.jsp"%>
	<link rel="stylesheet" type="text/css" href="../sysmanager/css/contentpage.css">
	<link rel="stylesheet" type="text/css" href="../sysmanager/css/tab.winclassic.css">
  <script language="javascript" src="<%=request.getContextPath()%>/include/dragdiv.js"></script>
  <link rel="stylesheet" type="text/css" href="../css/tab.winclassic.css">
</head>
<body class="contentbodymargin" scroll="yes">
<%
	RoleManager roleManager = SecurityDatabase.getRoleManager();
    	//用户岗位所对应的角色
		List jobRoles = roleManager.getJobRoleByList(uid);
    %>
    <table width="100%" border="0" height="100%" cellpadding="0" cellspacing="1" class="thin">
	    <tr class="labeltable_middle_td">
			<td height='20' class="headercolor" nowrap width="10%">角色</td>
			<td height='20' class="headercolor" nowrap width="10%">角色所隶属的机构-岗位</td>
		</tr>
	<%
		if(jobRoles == null || jobRoles.size() <= 0){
	%>
		<tr>
			<td colspan="2" align="center">用户没有任何岗位所对应的角色</td>
		</tr>
	<%		
		}else{
		for(int i=0;i<jobRoles.size();i++){
			UserJobRole ujr = (UserJobRole)jobRoles.get(i);
			List orgJobs = roleManager.getOrgJobList(uid, ujr.getRoleId());
			String strRoleName = ujr.getRoleName();
			System.out.println(i);
			for(int j=0;j<orgJobs.size();j++){
				System.out.println(j);
				ujr = (UserJobRole)orgJobs.get(i);
				String strOrgJob = ujr.getOrgName()+"-"+ujr.getJobName();
	%>
		<tr>
			<td><%=strRoleName%></td>
			<td><%=strOrgJob%></td>
		</tr>
	<%
			}
		}
	}
    %>
    </table>
</body>
</html>
