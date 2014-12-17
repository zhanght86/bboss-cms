<%
/*
 * <p>Title: 用户隶属岗位页面</p>
 * <p>Description: 用户隶属岗位页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-3-22
 * @author liangbing.tao
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

<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.JobManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Job"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.User,
				com.frameworkset.platform.sysmgrcore.entity.Organization"%>
<%@ page import="com.frameworkset.platform.security.AccessControl,
				com.frameworkset.platform.sysmgrcore.manager.db.OrgCacheManager,
				java.util.ArrayList,
				java.util.List"%>


<%
			response.setHeader("Cache-Control", "no-cache"); 
			response.setHeader("Pragma", "no-cache"); 
			response.setDateHeader("Expires", -1);  
			response.setDateHeader("max-age", 0); 

			AccessControl accessControl = AccessControl.getInstance();
			accessControl.checkManagerAccess(request,response);
			
			JobManager jobManager = SecurityDatabase.getJobManager();
			Job noJob = jobManager.getJobById("1");
			String jobName = "";
			String jobid = "";
			if (noJob != null) {
				jobid = noJob.getJobId();
				jobName = noJob.getJobName();
			}
			String userId = request.getParameter("userId");
			String orgId = request.getParameter("orgId");
			session.setAttribute("userId", userId);
			UserManager userManager = SecurityDatabase.getUserManager();
			User user = userManager.getUserById(userId);
			Organization org = OrgCacheManager.getInstance().getOrganization(orgId);
				
			List existJob = jobManager.getJobList(org,user);//用户在当前机构下的岗位
			List allJob = jobManager.getJobList(org);
			//权限过滤， 能进行岗位授予的岗位必须是：当前登录用户（包括部门管理员）必须对这些岗位有机构设置的权限。	
			List list1 = new ArrayList();
		    if(existJob != null)
		    {
		    	for(int i=0;i<existJob.size();i++)
		    	{
		    		Job job = (Job)existJob.get(i);
		    		list1.add(job);
		    	}
		    }
		    List list2 = new ArrayList();
		    if(allJob != null)
		    {
		    	for(int i=0;i<allJob.size();i++)
		    	{
		    		Job job = (Job)allJob.get(i);
		    		//用户自己创建的与有权限设置的岗位
		    		if(accessControl.checkPermission(job.getJobId(), "jobset", AccessControl.JOB_RESOURCE))
		    		{
		    			list2.add(job);
		    		}
		    	}
		    }
			request.setAttribute("existJob", list1);
			request.setAttribute("allJob", list2);
			String myOrgId = orgId;
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head> 
		<title>My JSP 'changeJob_ajax.jsp' starting page</title>
		<script language="JavaScript" src="../../scripts/ajax.js" type="text/javascript"></script>
		<script language="javascript" src="../../../include/dragdiv.js"></script>
		<script language="JavaScript" src="../../../include/querySelect.js" type="text/javascript"></script>
		
	<SCRIPT LANGUAGE="JavaScript"> 
	  var api = parent.frameElement.api, W = api.opener;
		function createjob(){
			var orgId = document.all("orgId").value;
			var url = "../jobmanager/A03/addjob.jsp?fromtype=user&orgId=" + orgId;  
			showModalDialog(url,window,"dialogWidth:"+780+"px;dialogHeight:"+580+"px;help:no;scroll:auto;status:no");
		}
		
		var win ;
		function imp()
		{
			var orgId = document.all("orgId").value;
			var url = "lusersys_ajax.jsp?orgId=" + orgId;  
			win = window.showModalDialog(url,window,"dialogWidth:"+660+"px;dialogHeight:"+580+"px;help:no;scroll:auto;status:no;maximize=yes;minimize=0;");
			if(win == "ok"){
			    window.location.href = "userjoborg.jsp?userId=<%=userId%>&orgId=<%=myOrgId%>";
			}
		}
		function addone(name,value,n){
		   for(var i=n;i>=0;i--){
				if(value==document.all("jobId").options[i].value){
				  return;
				}
			}
		   var op=new Option(name,value);
		   document.all("jobId").add(op);
		}
		function addjob(){	
			//已经设置的岗位
			var alreadyJobArr = document.all("jobId");
		   //添加单个岗位
		   var jobIds = "";
		   var n=document.all("jobId").options.length-1;   	
		   var orgId = document.all("orgId").value;
		   var flag = "1";	
		   var alreadyJobs = "";
		   for(var i=0;i<document.all("allist").options.length;i++){
		   	   var op=document.all("allist").options[i];   
			   if(op.selected){
			   		var flag2 = false;
			   		for(var j = 0; j < alreadyJobArr.length; j++){
			   			if(op.text==alreadyJobArr.options[j].text){
			   				flag2 = true;
			   			}
			   		}
			   		if(!flag2){
				   		if(jobIds==""){ 
				   			jobIds = op.value;
				   		}else {
				   			jobIds += "," + op.value;
				   		}
				   		addone(op.text,op.value,n);		
			   		}else{
			   			if(alreadyJobs == ""){
			   				alreadyJobs = op.text;
			   			}else{
			   				alreadyJobs += "\n" + op.text;
			   			}
			   		}   	
			   }	
		   }
		   //每次添加必需要选择一个岗位,weida
		   if(jobIds != ""){
		   		send_request('savaUserOrgJob.jsp?userId=<%=userId%>&orgId='+orgId+'&jobId='+jobIds+'&flag='+flag);  
		   }else if(alreadyJobs != ""){
		   		W.$.dialog.alert("<pg:message code='sany.pdp.yxgwyjsz'/>：\n" + alreadyJobs,function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
	  			return;
		   }else{
		   		W.$.dialog.alert("<pg:message code='sany.pdp.to.choose.jobs'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
		   		return;
		   }	
		}
		
		function addall(){
			//已经设置的岗位
			var alreadyJobArr = document.all("jobId");
			//添加所有岗位
			var jobIds = "";	
			var n=document.all("jobId").options.length-1;
			var p=document.all("allist").options.length-1;
			var alreadyJobs = "";		  
		    for(var i=0;i<document.all("allist").options.length;i++){
			    var op=document.all("allist").options[i];
			    var flag = false;
			    for(var j = 0; j < alreadyJobArr.length; j++){
			    	if(op.text==alreadyJobArr.options[j].text){
			    		flag = true;
			    	}
			    }			
			    if(!flag){
			    	if(jobIds == ""){
			    		jobIds = op.value;	
			    	}else{
						jobIds += "," + op.value;
					}
			    	addone(op.text,op.value,n);
			    }else{
			    	if(alreadyJobs == ""){
			    		alreadyJobs = op.text;
			    	}else{
			    		alreadyJobs += "\n" + op.text;
			    	}
			    }	     
		   }
		   var orgId = document.all("orgId").value;
		   var flag = "1";
		   if(jobIds != "")
		   {
		   		send_request('savaUserOrgJob.jsp?userId=<%=userId%>&orgId='+orgId+'&jobId='+jobIds+'&flag='+flag);	  
		   }else if(alreadyJobs != ""){
		   		W.$.dialog.alert("<pg:message code='sany.pdp.yxgwyjsz'/>：\n" + alreadyJobs,function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
	  			return;
		   }
		   
		}
		function deletejob(){	
			//必须至少保留一个岗位,weida
			//var len=document.all("jobId").options.length;
			//if(len == 1){
			//    alert("必须至少保留一个岗位！");
			//    return;
			//}	
			if(document.all("jobId").options.length < 1){
				W.$.dialog.alert("<pg:message code='sany.pdp.no.job.to.choose'/>！",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
				return;
			}
			if(document.all("jobId").selectedIndex==-1){
				W.$.dialog.alert("<pg:message code='sany.pdp.no.job.to.choose'/>！",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
				return;
			}
			var jobId = "";
			//var selectedCount = 0;
			//for (var m=document.all("jobId").options.length-1;m>=0;m--){
			//    if(document.all("jobId").options[m].selected){
			//        selectedCount = selectedCount + 1;
			//    }
			//}
			//if(selectedCount==len){
			//    alert("必须至少保留一个岗位！");
			//	return;
			//}
			//有权限的岗位数量
			var havePurviewCount = document.all("allist").options.length-1;
			var orgId = document.all("orgId").value;
		    var flag = "0";
		    var jobIds = "";
		    var noPurviewJob = "";
			for (var m=document.all("jobId").options.length-1;m>=0;m--){
				var option = document.all("jobId").options[m];
			    if(document.all("jobId").options[m].selected){
			    	var flag1 = false;
			    	for(var n=havePurviewCount; n>=0; n--){
			    		if(document.all("allist").options[n].value==option.value){
					      	jobId = option.value;
					      	if(jobIds==""){
					      		jobIds = jobId;
					      	}else{
					      		jobIds = jobIds + "," + jobId;
					      	}
					        document.all("jobId").options[m]=null;
					        flag1 = true;
					    }
			        }
			        if(!flag1){
			        	if(noPurviewJob==""){
				    		noPurviewJob = "<pg:message code='sany.pdp.yxgwmyqx'/>：\n" + option.text;
				    	}else{
				    		noPurviewJob += "\n" + option.text;
				    	}
			        }
			    }
		    }
		    if(jobIds != ""){
		    	send_request('savaUserOrgJob.jsp?userId=<%=userId%>&orgId='+orgId+'&jobId='+jobIds+'&flag='+flag);	
		    }else{
			    if(noPurviewJob!=""){
			    	W.$.dialog.alert(noPurviewJob,function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
			    }else{
		    		return ;
		    	}
		    }
		    
		}
		//不允许
		function deleteall(){
			//weida,阻止全部调出岗位
			//alert("要调出全部岗位,请直接在列表中删除用户!");
			//return;
			if(document.all("jobId").options.length < 1){
				W.$.dialog.alert("<pg:message code='sany.pdp.no.job.to.choose'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
			}
			//有权限的岗位数量
			var havePurviewCount = document.all("allist").options.length-1;
			var orgId = document.all("orgId").value;
		    var flag = "0";
		    var jobIds = "";
		    var noPurviewJob = "";
			for (var m=document.all("jobId").options.length-1;m>=0;m--){
				var option = document.all("jobId").options[m];
		    	var flag1 = false;
		    	for(var n=havePurviewCount; n>=0; n--){
		    		if(document.all("allist").options[n].value==option.value){
				      	jobId = option.value;
				      	if(jobIds==""){
				      		jobIds = jobId;
				      	}else{
				      		jobIds = jobIds + "," + jobId;
				      	}
				        document.all("jobId").options[m]=null;
				        flag1 = true;
				    }
		        }
		        if(!flag1){
		        	if(noPurviewJob==""){
		        		W.$.dialog.alert("<pg:message code='sany.pdp.yxgwmyqx'/>：\n" + option.text,function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
			    	}else{
			    		noPurviewJob += "\n" + option.text;
			    	}
		        }
		    }
		    if(jobIds != ""){
		    	send_request('savaUserOrgJob.jsp?userId=<%=userId%>&orgId='+orgId+'&jobId='+jobIds+'&flag='+flag);
		    }else{
			    if(noPurviewJob!=""){
			    	W.$.dialog.alert(noPurviewJob,function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
			    }else{
		    		return ;
		    	}
		    }
			//删除所有岗位		
		}      
	
		
	//---------------------------------------
		var http_request = false;
		function send_request(url){
			document.OrgJobForm.target="hiddenFrame";
			document.OrgJobForm.action = url;
			document.OrgJobForm.submit();
		}
						
		function changebox(){				 
			var len=document.all("jobId").options.length;			  	 	
		    var orgId = document.all("orgId").value;
		    var jobId=new Array(len)
		    for (var i=0;i<len;i++){	      
		        jobId[i]=document.all("jobId").options[i].value;
		     }           		
			send_request('savaUserOrgJob.jsp?userId=<%=userId%>&orgId='+orgId+'&jobId='+jobId);
		}
		
		function closed(){
			parent.window.close();
			parent.window.returnValue="ok";
		}
	</SCRIPT>
	<script type="text/javascript" src="../../../include/jquery-1.4.2.min.js"></script>
		<script type="text/javascript" src="../../../html/js/commontool.js"></script>
		<script type="text/javascript" src="../../../html/js/dialog/lhgdialog.js?self=false"></script>
	<body class="contentbodymargin" scroll="no" onload="loadDragDiv()">
		<div>
			<center>
				<input name="userId" value="<%=userId%>" type="hidden">
				<input name="orgId" value="<%=orgId%>" type="hidden">
				<form name="OrgJobForm" target="hiddenFrame" action="" method="post">
					<table width="80%" border="0" cellpadding="0" cellspacing="1">
						<tr class="tabletop">
							<td width="40%" align="center">
								&nbsp;
							</td>
							<td width="20%" align="center">
								&nbsp;
							</td>
							<td width="40%" align="center">
								&nbsp;
							</td>
						</tr>
						<tr class="tabletop">
							<td width="40%" align="left">
								<pg:message code="sany.pdp.choose.job"/>
							</td>
							<td width="20%" align="center">
								&nbsp;
							</td>
							<td width="40%" align="right">
								<pg:message code="sany.pdp.exist.job"/>
							</td>
						</tr>
						 <tr>
						  	<td width="40%" align="left">
						  	</td>
						    <td width="20%" align="center">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
						    <td width="40%" align="right">
						    	
						    </td>
						  </tr>
						<tr class="tabletop">
							<td width="40%" align="center">
								&nbsp;
							</td>
							<td width="20%" align="center">
								&nbsp;
							</td>
							<td width="40%" align="center">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td align="lrft">
							<div class="win" id="dd_1" align="left" >
								<select name="allist" multiple style="width:98%" onDBLclick="addjob()" size="18" >
									<pg:list requestKey="allJob">
										<option value="<pg:cell colName="jobId"/>">
											<pg:cell colName="jobName" />
										</option>
									</pg:list>
								</select>
								</div>
							</td>

							<td align="center">
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td align="center">
											<a class="bt_2"  onClick="addjob()"><span>&gt;</span></a>
										</td>
									</tr>
									<tr>
										<td align="center">
											&nbsp;
										</td>
									</tr>
									<tr>
										<td align="center">
											<a class="bt_2"  onClick="addall()"><span>&gt;&gt;</span></a>
										</td>
									</tr>
									<tr>
										<td align="center">
											&nbsp;
										</td>
									</tr>
									<tr>
										<td align="center">
											<a class="bt_2"  onClick="deleteall()"><span>&lt;&lt;</span></a>
										</td>
									</tr>
									<tr>
										<td align="center">
											&nbsp;
										</td>
									</tr>
									<tr>
										<td align="center">
											<a class="bt_2"  onClick="deletejob()"><span>&lt;</span></a>
										</td>
									</tr>
									<tr>
										<td align="center">
											&nbsp;
										</td>
									</tr>
								</table>
							</td>
							<td><div class="win" id="dd_2" align="right" >
								<select name="jobId" multiple style="width:98%"  onDBLclick="deletejob()" size="18" >
									<pg:list requestKey="existJob">
										<option value="<pg:cell colName="jobId"/>">
											<pg:cell colName="jobName" />
										</option>
									</pg:list>
								</select>
							</div>
							</td>

						</tr>
						<tr>
							<td>
								<br/>
								<br/>
								<br/>
								<br/>
							</td>
						</tr>
						<tr>
							<td align="center" colspan="4">
								<!--input name="butn" type="button" class="input" value="新增岗位" onClick="createjob()"-->
								<!--<%
								 if (accessControl.checkPermission(myOrgId, "orgjobset", AccessControl.ORGUNIT_RESOURCE))
								 //if(accessControl.isAdmin())
					            {%>
								<a  class="bt_1" href="javascript:void(0)" onclick="imp()" id="addButton"><span><pg:message code="sany.pdp.common.confirm"/></span></a>
								<%}%>
								--><a class="bt_2" href="javascript:void(0)" onclick="parent.closeDlg();"><span><pg:message code="sany.pdp.common.operation.exit"/></span></a>
							</td>
						</tr>
						<tr class="tabletop">

							<td width="40%" align="center">
								&nbsp;
							</td>
						</tr>

					</table>
				</form>
			</center>
		</div>
		<div id=divProcessing style="width:200px;height:30px;position:absolute;left:300px;top:350px;display:none">
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
	<iframe name="hiddenFrame" width=0 height=0></iframe>
</html>

