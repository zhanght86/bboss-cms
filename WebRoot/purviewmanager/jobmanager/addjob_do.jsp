<%
/*
 * <p>Title: 岗位新增处理页面</p>
 * <p>Description: 岗位新增处理页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: chinacreator</p>
 * @Date 2008-3-18
 * @author liangbing.tao
 * @version 1.0
 */
%>

<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.util.StringUtil"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.JobManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.exception.ManagerException"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Job"%>
<%@ page import="org.frameworkset.web.servlet.support.RequestContextUtils"%>


<%
			AccessControl control = AccessControl.getInstance();
			control.checkManagerAccess(request,response);
			int curUserId = Integer.parseInt(control.getUserID());
			
			boolean tag = true;
			
			String notice = RequestContextUtils.getI18nMessage("sany.pdp.add.failed", request)+"！\\n";
			
			
			//---------------START--岗位管理写操作日志
			String operContent="";        
	        String operSource=control.getMachinedID();
	        String openModle=RequestContextUtils.getI18nMessage("sany.pdp.job.manage", request);
	        String userName = control.getUserName();
	        String description="";  
	        LogManager logManager = SecurityDatabase.getLogManager();
			//---------------END
			
			
			String jobName = StringUtil.replaceNull(request.getParameter("jobname"));
			String jobDesc = StringUtil.replaceNull(request.getParameter("jobdesc"));
			String jobFunction = StringUtil.replaceNull(request.getParameter("jobFunction"));
			String jobAmount = StringUtil.replaceNull(request.getParameter("jobAmount"));
			String jobNumber = StringUtil.replaceNull(request.getParameter("jobNumber"));
			String jobCondition = StringUtil.replaceNull(request.getParameter("jobCondition"));
			String jobRank = StringUtil.replaceNull(request.getParameter("jobRank"));
			
			boolean b1 = false;
			boolean b2 = false;
			
			JobManager jobMgr = SecurityDatabase.getJobManager();
			
			try
			{
				b1 = jobMgr.isJobExist(jobName.trim());
				b2 = jobMgr.isJobNumber(jobNumber.trim());
			} 
			catch (ManagerException e1) 
			{
				tag  = false;
			}
	
			Job job = new Job();
			
			
			if (b1 == false && b2==false) 
			{
				job.setJobName(jobName);
				job.setJobDesc(jobDesc);
				job.setJobFunction(jobFunction);
				job.setJobAmount(jobAmount);
				job.setJobNumber(jobNumber);
				job.setJobCondition(jobCondition);
				job.setJobRank(jobRank);
				job.setOwner_id(curUserId);
			} 
			else
			{
				if(b1)
				{
					tag = false;
					notice = notice + RequestContextUtils.getI18nMessage("sany.pdp.jobmanage.job.name.exist", request)+"！\\n";
				}
				if(b2)
				{
					tag = false;
					notice = notice +RequestContextUtils.getI18nMessage("sany.pdp.jobmanage.job.number.exist", request)+ "！\\n";
				}
			}
			
			if(tag)
			{
				try {
					jobMgr.saveJob(job);
					
					//--岗位管理写操作日志	
					
					operContent=RequestContextUtils.getI18nMessage("sany.pdp.jobmanage.job.modify", request)+":"+job.getJobName(); 						
					description="";
					logManager.log(control.getUserAccount(),operContent,openModle,operSource,description);       
					//--
					
				} 
				catch (ManagerException e) 
				{
					tag = false;
				}
			}
			
			if(tag)
			{
		%>
				<script>
					var api = parent.frameElement.api, W = api.opener;
					W.$.dialog.alert("<pg:message code='sany.pdp.common.operation.success'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
				</script>
				<%
			}
			else
			{
		%>
				<script>
					var api = parent.frameElement.api, W = api.opener;
					W.$.dialog.alert("<%=notice%>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
				</script>
		<%
			}
		%>
		
<script>
    window.onload = function prompt()
    {
    }
</script>
