<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.web.struts.form.UserOrgManagerForm
				,com.frameworkset.platform.config.ConfigManager" %>
<html>
<head>
<title>调入人员</title>
<link rel="stylesheet" type="text/css" href="../css/treeview.css">
<%@ include file="/include/css.jsp"%>
<%
	String orgId=request.getParameter("orgId");
	String jobId=request.getParameter("jobId");
	
  	//加判断：如果开关允许调入其他机构的用户，则显示全部的树，否则只显示当前机构其他岗位的和离散用户
  	//危达，200711141134
  	boolean tag = ConfigManager.getInstance().getConfigBooleanValue("org.job.user.all",true);
%>
</head> 
<frameset name="frame1" cols="30,70" frameborder="no" border="0" framespacing="0" >
<%
	if(tag){
%>
  <frame src="orguserTree.jsp?orgId=<%=orgId%>&jobId=<%=jobId%>" name="orgTree" id="orgTree" />
<%
	}
	else
	{
%>
  <frame src="orguserTree.jsp?orgId=<%=orgId%>&jobId=<%=jobId%>" name="orgTree" id="orgTree" />
<%
	}
%>
  <frame src="userList_ajax.jsp?orgId=<%=orgId%>&jobId=<%=jobId%>" name="userList" scrolling="No" noresize="noresize" id="orgList" />
</frameset>
<noframes>
<body>
</body>
</noframes>
</html>

