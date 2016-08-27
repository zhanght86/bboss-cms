<%
/*
 * <p>Title: 岗位信息查看页面</p>
 * <p>Description: 岗位信息查看页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-3-24
 * @author baowen.liu
 * @version 1.0
 */
%>

<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="com.frameworkset.util.StringUtil"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.JobManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Job"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.User"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>

<%
	AccessControl accessControl = AccessControl.getInstance();
	accessControl.checkManagerAccess(request,response);
	

	String jobId = StringUtil.replaceNull(request.getParameter("jobId"));	
	JobManager jobManager = SecurityDatabase.getJobManager();
	UserManager userManager=SecurityDatabase.getUserManager();
	Job job = jobManager.getJobById(jobId);
	request.setAttribute("Job", job);

	String jobFunction="";
	String jobDesc="";
	String jobCondition=""; 
	String userName="";
	String userRealName="";
	
	
	    
	if(job != null){
		jobFunction = job.getJobFunction() ;
		jobFunction = jobFunction == null ? "" : jobFunction.trim() ;
		
		jobDesc = job.getJobDesc();
		jobDesc = jobDesc  == null ? "": jobDesc.trim() ;
		
		jobCondition = job.getJobCondition();
		jobCondition = jobCondition == null ? "" : jobCondition.trim();	
		
	    String roleOwnerId=String.valueOf(job.getOwner_id());
		User user=userManager.getUserById(roleOwnerId);
		userName=user.getUserName();
	    userRealName=user.getUserRealname();	
				
  	}
  	
  	
  	
%>
<html>
	<head>
		<title>岗位信息查看</title>
		<script language="JavaScript">
			 					
			function back()
			{
				
				window.close();
			}
			
			function init()
			{
				document.getElementsByName("jobFunction")[0].value="<%=jobFunction%>";
				document.getElementsByName("jobDesc")[0].value="<%=jobDesc%>";
				document.getElementsByName("jobCondition")[0].value="<%=jobCondition%>";
			}
	</script>
	<style type="text/css">
			<!--
			.style1 {color: #CC0000}
			-->
		</style>
		
<script type="text/javascript" src="../../include/jquery-1.4.2.min.js"></script>
<link href="../../html/stylesheet/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="../../html/js/commontool.js"></script>
<script type="text/javascript" src="../../html/js/dialog/lhgdialog.js?self=false"></script>	
	</head>
	<body class="contentbodymargin" onLoad="init();" scroll="no">
		<div id="" align="center">

			<form action="form1" method="post">
				<pg:beaninfo requestKey="Job">
					
					<table width="100%" height="85" border="0" cellpadding="0" cellspacing="1" class="table2">
						<tr>
							<th>
								<pg:message code="sany.pdp.jobmanage.job.name"/>：
							</th>
							<td class="detailcontent">
								<input   readonly="readonly" name="jobName" type="text" size="40" value='<pg:cell colName="jobName"  defaultValue=""/>'>
							</td>
						</tr>
						<tr>
							<th>
								<pg:message code="sany.pdp.jobmanage.job.number"/>： 
							</th>
							<td class="detailcontent">
								<input readonly="readonly" name="jobNumber" type="text" size="40" value='<pg:cell colName="jobNumber"  defaultValue=""/>'>
							</td>
						</tr>
						<tr>
							<th>
								<pg:message code="sany.pdp.jobmanage.job.level"/>： 
							</th>
							<td class="detailcontent">
								<input readonly="readonly" name="jobRank" type="text" size="40" value='<pg:cell colName="jobRank"  defaultValue=""/>'>
							</td>
						</tr>
						<tr>
							<th>
								<pg:message code="sany.pdp.jobmanage.job.establishment"/>： 
							</th>
							<td class="detailcontent">
								<input  readonly="readonly" name="jobAmount" type="text" size="40" value='<pg:cell colName="jobAmount"  defaultValue=""/>'>
							</td>
						</tr>
						<tr>
							<th>
								 <pg:message code="sany.pdp.jobmanage.job.duty"/>： 
							</th>
							<td class="detailcontent">
								<textarea readonly="readonly" name="jobFunction" cols="50" rows="4">

								</textarea>
							</td>
						</tr>

						<tr>
							<th>
								<pg:message code="sany.pdp.jobmanage.job.description"/>： 
							</th>
							<td class="detailcontent">
								<textarea  readonly="readonly" name="jobDesc" cols="50" rows="4">
								</textarea>
							</td>
						</tr>

						<tr>
							<th>
								<pg:message code="sany.pdp.jobmanage.job.condition"/>： 
							</th>
							<td class="detailcontent">
								<textarea readonly="readonly" name="jobCondition" cols="50" rows="4">
								</textarea>
							</td>
						</tr>
						<tr>
							<th>
								<pg:message code="sany.pdp.workflow.creater.username"/>： 
							</th>
							<td class="detailcontent">
								<input readonly="readonly" name="creatorName" type="text" size="40" value='<%=userName%>【<%=userRealName%>】'>
							</td>
						</tr>
					</table>

					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td>
								&nbsp;
							</td>
							<td>
								<table width="26%" border="0" align="center" cellpadding="0" cellspacing="0">

									<tr>
										<%if (!jobId.equals("0")) 
											{
										%>
										<%
												if (accessControl.checkPermission(jobId, "edit",AccessControl.JOB_RESOURCE)) 
												{
										%>
										<td>
											
										</td>
										<%
												}
										%>
										<td>
											<div align="center">
												<a class="bt_2" onclick="closeDlg()"><span><pg:message code="sany.pdp.common.operation.exit"/></span></a>
											</div>
										</td>
										<%
										}
										%>
									</tr>

								</table>
							</td>
						</tr>
					</table>
				</pg:beaninfo>
			</form>
		</div>
		
	</body>
	
</html>

