<%
/*
 * <p>Title: 岗位机构树显示框架页面</p>
 * <p>Description: 岗位机构树显示框架页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: chinacreator</p>
 * @Date 2008-3-25
 * @author baowen.liu
 * @version 1.0
 */
%>
<%     
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0); 
%>

<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page
	import="com.frameworkset.platform.security.AccessControl,
	com.frameworkset.platform.sysmgrcore.entity.Job,
	com.frameworkset.platform.sysmgrcore.manager.JobManager,
	com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
	
<%
	String jobId=request.getParameter("jobId");
	
	/*UserOrgManagerForm s = (UserOrgManagerForm)request.getAttribute("userOrgForm");	
	String oid = "";
	String uid = "";
	if(s != null){
    	oid =  s.getOrgId();
    	uid = s.getUserId();
    }   
    session.setAttribute("roleTabId", "6"); */
    JobManager jobManager = SecurityDatabase.getJobManager();
	Job job = jobManager.getJobById(jobId);
	String jobName = job.getJobName();
      
%>	
<html>
<head>
<title>岗位【<%=jobName %>】授予机构</title>
<link rel="stylesheet" type="text/css" href="../css/treeview.css">
<%@ include file="/include/css.jsp"%>

</head> 

	<frameset name="frame1" cols="30,70" frameborder="no" border="0" framespacing="0" >
	  <frame src="orgTree1.jsp?jobId=<%=jobId%>" name="orgTree" id="orgTree" />
	  <frame src="../groupmanager/noGroup.jsp" name="orgList" scrolling="No" noresize="noresize" id="orgList" />
	</frameset>
	<input type="hidden" name="jobId" value ="<%=jobId%>">

</html>

