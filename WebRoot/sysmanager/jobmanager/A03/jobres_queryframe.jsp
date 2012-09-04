<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.List"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*,com.frameworkset.platform.cms.documentmanager.*"%>

<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.security.*"%>
<%@ page import="com.frameworkset.platform.cms.channelmanager.*"%>
<%@ page import="com.frameworkset.platform.cms.customform.*,
	com.frameworkset.platform.sysmgrcore.entity.Job,
	com.frameworkset.platform.sysmgrcore.manager.JobManager,
	com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase,
	com.frameworkset.platform.sysmgrcore.manager.ResManager,
	org.frameworkset.spi.SPIException"%>
<%
AccessControl accesscontroler = AccessControl.getInstance();
accesscontroler.checkAccess(request, response);

String jobId = (String)request.getParameter("jobId");
JobManager jm = SecurityDatabase.getJobManager();
String jobName = null;
if(jobId != null && !jobId.equals("")){
	Job job = jm.getJobById(jobId);
	jobName = job.getJobName();
}
%>
<HTML>
 <HEAD>
   <title>岗位[<%=jobName%>]资源权限查询</title>
 
 </HEAD>
  <frameset rows="30,*" border=0>
	<frame frameborder=0  noResize scrolling="no" marginWidth=0 name="forQuery" src="jobres_query.jsp?jobId=<%=jobId%>"></frame>		
	<frame frameborder=0  noResize scrolling="auto" marginWidth=0 name="forDocList" src="jobres_querylist.jsp?jobId=<%=jobId%>"></frame>
	</frameset><noframes></noframes>
</HTML>

