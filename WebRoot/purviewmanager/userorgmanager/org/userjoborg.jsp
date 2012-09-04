<%
/**
 * <p>Title: 机构岗位设置主页面</p>
 * <p>Description: 机构岗位设置主页面</p>
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
<%@ page import="com.frameworkset.platform.sysmgrcore.web.struts.form.OrgJobForm"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Orgjob"%>
<%@ page import="java.util.List"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Organization"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.JobManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%
		String orgId = request.getParameter("orgId");
		String orgName = request.getParameter("orgName");
		
		
		JobManager jobManager = SecurityDatabase.getJobManager();
		Orgjob orgjob = new Orgjob();
		//List joblist = jobManager.getJobList();
		Organization org = new Organization();
		org.setOrgId(orgId);

		//List orgjoblist = jobManager.getJobList(org);
		//session.setAttribute("joblist", joblist);
		//session.setAttribute("orgjoblist", orgjoblist);
		
%>
<html>
	<head>
	<title>【<%=orgName%>】机构下岗位的设置</title>
<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../../css/treeview.css">
	</head>
	<frameset rows="*" cols="750, *" framespacing="1" frameborder="yes" border="1" >
		<frame src="lusersys_ajax.jsp?orgId=<%=orgId %>" name="lusersys" id="lusersys"> 
		<frame src="rusersys_ajax.jsp?orgId=<%=orgId %>" name="rusersys" id="rusersys">
	</frameset>
	<body>
	</body>
</html>

