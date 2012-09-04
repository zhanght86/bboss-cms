<%
/*
 * <p>Title: 岗位修改页面</p>
 * <p>Description: 岗位修改页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: chinacreator</p>
 * @Date 2008-3-18
 * @author liangbing.tao
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
	
	//旧的岗位名称与岗位编号，修改的岗位名称与岗位编号不能相同
	String oldJobName = job.getJobName();
	String oldJobNumber = job.getJobNumber(); 
	
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
		<title>岗位信息修改</title>
		<SCRIPT language="JavaScript" SRC="../include/validateForm.js"></SCRIPT>
		<script type="text/javascript" src="../../include/jquery-1.4.2.min.js"></script>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
				<script language="JavaScript">
				var api = frameElement.api, W = api.opener;
			 function trim(string)
			 {
			  var temp="";
			  string = ''+string;
			  splitstring = string.split(" ");
			  for(i=0;i<splitstring.length;i++)
			  {
			    temp += splitstring[i];
			  } 
			  return temp;
			 }
		  
			function updatejob()
			{
				var form = document.forms[0];
				var jobName= document.forms[0].jobName.value;
				var jobNumber=form.jobNumber.value;
				var jobRank=form.jobRank.value;
				var jobAmount=form.jobAmount.value;
				var jobFunction=form.jobFunction.value;
				var jobDesc=form.jobDesc.value;
				var jobCondition=form.jobCondition.value;
				
				if (trim(jobName).length == 0 )
				{
					W.$.dialog.alert("<pg:message code='sany.pdp.jobmnage.input.job.name'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
				    return false;
			    }
			    
			    //if (trim(jobNumber).length == 0 )
			    //{
				//    alert("请录入岗位编号！"); 
				//    return false;
			    //}
			      
			    if(jobName.search(/[\\\/\|:\*\?<>"']/g)!=-1)
			    {
			    	W.$.dialog.alert("<pg:message code='sany.pdp.jobmanage.job.name.invalid'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
					form.jobName.focus();
					return;
				}
				if(jobNumber.search(/\W/g)!=-1 )
				{
					W.$.dialog.alert("<pg:message code='sany.pdp.jobmanage.job.number.invalid'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
					form.jobNumber.focus();
					return false;
				}
				if(jobRank.search(/[\\\/\|:\*\?<>"']/g)!=-1)
				{
					W.$.dialog.alert("<pg:message code='sany.pdp.jobmanage.job.level.invali'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
					form.jobRank.focus();
					return;
				}
				if(jobAmount.search(/[\\\/\|:\*\?<>"']/g)!=-1)
				{
					W.$.dialog.alert("<pg:message code='sany.pdp.jobmanage.job.establishment.invalid'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
					form.jobAmount.focus();
					return;
				} 
		
				if(jobName.length>100)
				{
					W.$.dialog.alert("<pg:message code='sany.pdp.jobmanage.job.name.long'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
					return;
				}
				if(jobNumber.length>100)
				{
					W.$.dialog.alert("<pg:message code='sany.pdp.jobmanage.job.number.long'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
					return;
				}
				if(jobRank.length>100)
				{
					W.$.dialog.alert("<pg:message code='sany.pdp.jobmange.job.level.long'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
					return;
				}
				if(jobAmount.length>100)
				{
					W.$.dialog.alert("<pg:message code='sany.pdp.jobmanage.job.establishment.long'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
					return;
				}
				if(jobFunction.length>200)
				{
					W.$.dialog.alert("<pg:message code='sany.pdp.jobmanage.job.duty.long'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
					return;
				}
				if(jobDesc.length>200)
				{
					W.$.dialog.alert("<pg:message code='sany.pdp.jobmanage.job.description.long'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
					return;
				}
				if(jobCondition.length>200)
				{
					W.$.dialog.alert("<pg:message code='sany.pdp.jobmanage.job.condition.long'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
					return;
				}
		    
			  	form.action = "modifyjob_do.jsp";
				form.target = "hiddenFrame";
				form.submit();
			}
						
			function back()
			{
				window.returnValue = true;
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
	</head>
	<body class="contentbodymargin" onLoad="init();" scroll="no">
		<div id="" align="center">
			
			<form action="form1" method="post">
				<pg:beaninfo requestKey="Job">
					<input name="jobId" type="hidden" value="<pg:cell colName="jobId" defaultValue=""/>">
					<table width="100%" height="85" border="0" cellpadding="0" cellspacing="1" class="table2">
					<input name="oldJobName" type="hidden" value="<%=oldJobName %>" />
					<input name="oldJobNumber" type="hidden" value="<%=oldJobNumber %>" />
						<tr>
							<th>
								<pg:message code="sany.pdp.jobmanage.job.name"/>：
							</th>
							<td class="detailcontent">
								<input name="jobName" type="text" size="40" value='<pg:cell colName="jobName"  defaultValue=""/>' maxlength="20">
							</td>
						</tr>
						<tr>
							<th>
								<pg:message code="sany.pdp.jobmanage.job.number"/>：
							</th>
							<td class="detailcontent">
								<input name="jobNumber"  type="text" size="40" value='<pg:cell colName="jobNumber"  defaultValue=""/>' maxlength="20">
							</td>
						</tr>
						<tr>
							<th>
								<pg:message code="sany.pdp.jobmanage.job.level"/>：
							</th>
							<td class="detailcontent">
								<input name="jobRank" type="text" size="40" value='<pg:cell colName="jobRank"  defaultValue=""/>' maxlength="20">
							</td>
						</tr>
						<tr>
							<th>
								<pg:message code="sany.pdp.jobmanage.job.establishment"/>：
							</th>
							<td class="detailcontent">
								<input name="jobAmount" type="text" size="40" value='<pg:cell colName="jobAmount"  defaultValue=""/>' maxlength="20">
							</td>
						</tr>
						<tr>
							<th>
								<pg:message code="sany.pdp.jobmanage.job.duty"/>：
							</th>
							<td class="detailcontent">
								<textarea name="jobFunction" cols="50" rows="4">

								</textarea>
							</td>
						</tr>

						<tr>
							<th>
								<pg:message code="sany.pdp.jobmanage.job.description"/>：
							</th>
							<td class="detailcontent">
								<textarea name="jobDesc" cols="50" rows="4">
								</textarea>
							</td>
						</tr>

						<tr>
							<th>
								<pg:message code="sany.pdp.jobmanage.job.condition"/>：
							</th>
							<td class="detailcontent">
								<textarea name="jobCondition" cols="50" rows="4">
								</textarea>
							</td>
						</tr>
						<tr>
							<th>
								<pg:message code="sany.pdp.workflow.creater.username"/>：
							</th>
							<td class="detailcontent">
								<input disabled=true name="creatorName" type="text" size="40" value='<%=userName%>【<%=userRealName%>】'>
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
											<div align="center">
												<a class="bt_1" onclick="updatejob()"><span><pg:message code="sany.pdp.common.operation.save"/></span></a>
											</div>
										</td>
										<%
												}
										%>
										<td>
											<div align="center">
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
		<div id=divProcessing style="width:200px;height:30px;position:absolute;left:200px;top:260px;display:none">
			<table border=0 cellpadding=0 cellspacing=1 bgcolor="#000000" width="100%" height="100%">
				<tr>
					<td bgcolor=#3A6EA5>
						<marquee align="middle" behavior="alternate" scrollamount="5">
							<font color=#FFFFFF>...处理中...请等待...</font>
						</marquee>
					</td>
				</tr>
			</table>
		</div>
	</body>
	<iframe name="hiddenFrame" width="0" height="0"></iframe>
</html>

