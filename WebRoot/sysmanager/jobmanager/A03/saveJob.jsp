/**
 * 
 * <p>Title: 保存岗位操作</p>
 *
 * <p>Description: 岗位保存处理页面</p>
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
    String jobName = request.getParameter("jobName");
    String jobNumber = request.getParameter("jobNumber");
    String jobRank = request.getParameter("jobRank");
    String jobAmount = request.getParameter("jobAmount");
    String jobDesc = request.getParameter("jobDesc");
    String jobCondition = request.getParameter("jobCondition");
    String jobFunction = request.getParameter("jobFunction");
    
    //reauest end
    
    Job job = new Job();
    boolean jobNameState = jobManager.isJobExist(jobName.trim());
    boolean jobNumberState = jobManager.isJobNumber(jobNumber.trim());
    if(!jobNameState && !jobNumberState){
    	job.setJobName(jobName);
		job.setJobDesc(jobDesc);
		job.setJobFunction(jobFunction);
		job.setJobAmount(jobAmount);
		job.setJobNumber(jobNumber);
		job.setJobCondition(jobCondition);
		job.setJobRank(jobRank);
		job.setOwner_id(Integer.parseInt(control.getUserID()));
		
		jobManager.saveJob(job);
		//--岗位管理写操作日志	
		operContent="添加岗位："+job.getJobName(); 						
		description="";
		logManager.log(control.getUserAccount()+":"+userName,operContent,openModle,operSource,description);
%>
	<script language="javascript">
		alert("添加岗位成功！");
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
