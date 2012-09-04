/**
 * 
 * <p>Title: 修改岗位操作</p>
 *
 * <p>Description: 岗位修改处理页面</p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: chinacreator</p>
 * @Date 2008-3-17
 * @author gao.tang
 * @version 1.0
 */ 
 <%@ include file="../../base/scripts/panes.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.AccessControl,
				com.frameworkset.platform.sysmgrcore.entity.Job,
				com.frameworkset.platform.sysmgrcore.manager.LogManager,
				com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase,
				com.frameworkset.platform.sysmgrcore.manager.JobManager" %>

<%
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(pageContext);
	String operContent="";        
    String operSource=control.getMachinedID();
    String openModle="岗位管理";
    String userName = control.getUserName();
    String description="";  
    LogManager logManager = SecurityDatabase.getLogManager();
    
    //request start
    JobManager jobManager = SecurityDatabase.getJobManager();
    //旧岗位名称
    String oldjobname = request.getParameter("oldjobname");
    //旧岗位编号
    String oldjobnumber = request.getParameter("oldjobnumber");
    
    String jobId = request.getParameter("jobId");
    String jobName = request.getParameter("jobName").trim();
    String jobNumber = request.getParameter("jobNumber").trim()==null?"":request.getParameter("jobNumber").trim();
    String jobRank = request.getParameter("jobRank").trim()==null?"":request.getParameter("jobRank").trim();
    String jobAmount = request.getParameter("jobAmount").trim()==null?"":request.getParameter("jobAmount").trim();
    String jobDesc = request.getParameter("jobDesc").trim()==null?"":request.getParameter("jobDesc").trim();
    String jobCondition = request.getParameter("jobCondition").trim()==null?"":request.getParameter("jobCondition").trim();
    String jobFunction = request.getParameter("jobFunction").trim()==null?"":request.getParameter("jobFunction").trim();
    
    //reauest end
    
    Job job = new Job();
    boolean jobNameState = false;
    boolean jobNumberState = false;
    if(!oldjobname.equals(jobName)){
    	jobNameState = jobManager.isJobExist(jobName.trim());
    }
    if(!oldjobnumber.equals(jobNumber)){
    	jobNumberState = jobManager.isJobNumber(jobNumber.trim());
    }
    if(!jobNameState && !jobNumberState){
    	job.setJobId(jobId);
    	job.setJobName(jobName);
		job.setJobDesc(jobDesc);
		job.setJobFunction(jobFunction);
		job.setJobAmount(jobAmount);
		job.setJobNumber(jobNumber);
		job.setJobCondition(jobCondition);
		job.setJobRank(jobRank);
		job.setOwner_id(Integer.parseInt(control.getUserID()));
		
		jobManager.updateJob(job);
		//--岗位管理写操作日志	
		operContent="修改岗位："+job.getJobName(); 						
		description="";
		logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description);
%>
	<script language="Javascript">
		alert("修改岗位成功！");
		var content  = getNavigatorContent();
		if(content)
		{
			getNavigatorContent().location.href = "../navigator_content.jsp?selected=<%=job.getJobId()%>";
		}
	</script>
<%
	}else{
%>
	<script language="Javascript">
		alert("岗位名称或岗位编号已存在，请重新输入!");
	</script>
<%		
	}
%>
