<%
/*
 * <p>Title: 岗位修改处理页面</p>
 * <p>Description: 岗位修改处理页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-3-18
 * @author liangbing.tao
 * @version 1.0
 */
%>

<%@ page language="java" contentType="text/html; charset=UTF-8"%>	
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>			
<%@ page import="com.frameworkset.util.StringUtil"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.JobManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Job"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="org.frameworkset.web.servlet.support.RequestContextUtils"%>				
<%
			AccessControl control = AccessControl.getInstance();
			control.checkManagerAccess(request,response);
			boolean tag = true;
			String notice = RequestContextUtils.getI18nMessage("sany.pdp.common.operation.fail.nocause", request)+"\\n";
			
			String oldJobName = request.getParameter("oldJobName");
			String oldJobNumber = request.getParameter("oldJobNumber")==null?"":request.getParameter("oldJobNumber");
			
			//---------------START--岗位管理写操作日志
			
			String operContent="";        
	        String operSource=control.getMachinedID();
	        String openModle=RequestContextUtils.getI18nMessage("sany.pdp.jobmanage", request);
	        String userName = control.getUserName();
	        String description="";
	        LogManager logManager = SecurityDatabase.getLogManager(); 		
			//---------------END
			
			
			String jobName = StringUtil.replaceNull(request.getParameter("jobName"));
			String jobDesc = StringUtil.replaceNull(request.getParameter("jobDesc"));
			String jobFunction = StringUtil.replaceNull(request.getParameter("jobFunction"));
			String jobAmount = StringUtil.replaceNull(request.getParameter("jobAmount"));
			String jobNumber = StringUtil.replaceNull(request.getParameter("jobNumber"));
			String jobCondition = StringUtil.replaceNull(request.getParameter("jobCondition"));
			String jobRank = StringUtil.replaceNull(request.getParameter("jobRank"));
			String jobId = StringUtil.replaceNull(request.getParameter("jobId"));
			
			JobManager jobManager = SecurityDatabase.getJobManager();	
			
			if(!"".equals(oldJobNumber)){
				if(!jobNumber.equals(oldJobNumber)){
					boolean isNumber = jobManager.isJobNumber(jobNumber);
					if(isNumber){
						tag = false;
						notice += RequestContextUtils.getI18nMessage("sany.pdp.jobmanage.same.job.number", request)+"！\\n";
					}
				}
			}
			if(!oldJobName.equals(jobName)){
				boolean isJob = jobManager.isJobExist(jobName);
				if(isJob){
					tag = false;
					notice += RequestContextUtils.getI18nMessage("sany.pdp.jobmanage.same.job.name", request)+"！\\n";
				}
			}
			
			if(tag){
				try
				{
					Job job = new Job();
					job.setJobName(jobName);
					job.setJobDesc(jobDesc);
					job.setJobFunction(jobFunction);
					job.setJobAmount(jobAmount);
					job.setJobNumber(jobNumber);
					job.setJobCondition(jobCondition);
					job.setJobRank(jobRank);
					job.setJobId(jobId);
					
					jobManager.storeJob(job);
					
					//--岗位管理写操作日志	
					operContent=RequestContextUtils.getI18nMessage("sany.pdp.jobmanage.job.modify", request)+job.getJobName(); 						
					description="";
					logManager.log(control.getUserAccount(),operContent,openModle,operSource,description);       
					//--
				}
				catch(Exception e)
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
					W.$.dialog.alert("<%=notice%>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
				</script>
		<%
			}
%>

	<script>
	    window.onload = function prompt()
	    {
	    }
	</script>
