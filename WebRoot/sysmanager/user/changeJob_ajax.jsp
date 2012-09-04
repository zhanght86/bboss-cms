<%     
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0); 
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ include file="../include/global1.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.JobManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Job"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.User"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%
			AccessControl accessControl = AccessControl.getInstance();
			accessControl.checkAccess(pageContext);
			
			JobManager jobManager = SecurityDatabase.getJobManager();
			Job noJob = jobManager.getJobById("1");
			String jobName = "";
			String jobid = "";
			if (noJob != null) {
				jobid = noJob.getJobId();
				jobName = noJob.getJobName();
			}
			//Integer userId = (Integer) session.getAttribute("currUserId");
			String userId = request.getParameter("userId");
			//String userId = (String) session.getAttribute("userId");
			String orgId = request.getParameter("orgId");
			session.setAttribute("userId", userId);
			UserManager userManager = SecurityDatabase.getUserManager();
			User user = userManager.getUserById(userId);
			
			String myOrgId = orgId;
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head> 
		<title>My JSP 'changeJob_ajax.jsp' starting page</title>
		<script language="JavaScript" src="../scripts/ajax.js" type="text/javascript"></script>
		<script language="javascript" src="<%=request.getContextPath()%>/include/dragdiv.js"></script>
		<script language="javascript" src="<%=request.getContextPath()%>/include/querySelect.js"></script>
<SCRIPT LANGUAGE="JavaScript"> 
  
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
		win = window.showModalDialog(url,window,"dialogWidth:"+560+"px;dialogHeight:"+480+"px;help:no;scroll:auto;status:no");
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
	   //添加单个岗位
	   var jobId = "";
	   var n=document.all("jobId").options.length-1;   	
	   var count = 0;
	   var orgId = document.all("orgId").value;
	   var flag = "1";	
	   var jobIds = "";
	   for(var i=0;i<document.all("allist").options.length;i++){
	   	   var op=document.all("allist").options[i];   
		   if(op.selected){
		   		jobId = op.value;
		   		if(jobIds=="") jobIds = jobIds + jobId;
		      	else jobIds = jobIds + "," + jobId;
		   		addone(op.text,op.value,n);		   		
		   		count++;
		   }	
	   }
	   if(count==0){
	  	   alert("请选择一个要添加的岗位!");
	  	   return;
	   }//每次添加必需要选择一个岗位,weida
	   send_request('savaUserOrgJob.jsp?userId=<%=userId%>&orgId='+orgId+'&jobId='+jobIds+'&flag='+flag);  
	}
	
	function addall(){
		//添加所有岗位
		var jobIds = "";
	    var flag1 = false;		
		var n=document.all("jobId").options.length-1;
		var p=document.all("allist").options.length-1;		  
	    for(var i=0;i<document.all("allist").options.length;i++){
		    var op=document.all("allist").options[i];
		    if(flag1){				
				jobIds += "," + op.value;
			}else{
				jobIds += op.value;
				flag1 = true;
			}
		    addone(op.text,op.value,n);	     
	   }
	   var orgId = document.all("orgId").value;
	   var flag = "1";
	   send_request('savaUserOrgJob.jsp?userId=<%=userId%>&orgId='+orgId+'&jobId='+jobIds+'&flag='+flag);	  
	}
	function deletejob(){	
		//必须至少保留一个岗位,weida
		var len=document.all("jobId").options.length;
		//if(len == 1){
		//    alert("必须至少保留一个岗位！");
		//    return;
		//}	
		if(document.all("jobId").options.length < 1){
			alert("没有可选的岗位！");
			return;
		}
		if(document.all("jobId").selectedIndex==-1){
			alert("请选择要删除的岗位！");
			return;
		}
		var jobId = "";
		var flag1 = false;
		var selectedCount = 0;
		for (var m=document.all("jobId").options.length-1;m>=0;m--){
		    if(document.all("jobId").options[m].selected){
		        selectedCount = selectedCount + 1;
		    }
		}
		//if(selectedCount==len){
		//    alert("必须至少保留一个岗位！");
		//	return;
		//}
		var orgId = document.all("orgId").value;
	    var flag = "0";
	    var jobIds = "";
		for (var m=document.all("jobId").options.length-1;m>=0;m--){
			var option = document.all("jobId").options[m];
		    if(document.all("jobId").options[m].selected){
		      	jobId = option.value;
		      	if(jobIds=="") jobIds = jobIds + jobId;
		      	else jobIds = jobIds + "," + jobId;
		        document.all("jobId").options[m]=null;
		        flag1 = true;
		    }
	    }
	    send_request('savaUserOrgJob.jsp?userId=<%=userId%>&orgId='+orgId+'&jobId='+jobIds+'&flag='+flag);
	}
	//不允许
	function deleteall(){
		//weida,阻止全部调出岗位
		//alert("要调出全部岗位,请直接在列表中删除用户!");
		//return;
		if(document.all("jobId").options.length < 1){
			alert("没有可选的岗位！");
		}
		var orgId = document.all("orgId").value;
	    var flag = "0";
	    var jobIds = "";
		for (var m=document.all("jobId").options.length-1;m>=0;m--){
			var option = document.all("jobId").options[m];
	      	jobId = option.value;
	      	if(jobIds==""){
				jobIds = jobIds + jobId;
	      	}
	      	else{ 
	      		jobIds = jobIds + "," + jobId;
	      	}
	        document.all("jobId").options[m]=null;
	    }
	    send_request('savaUserOrgJob.jsp?userId=<%=userId%>&orgId='+orgId+'&jobId='+jobIds+'&flag='+flag);
		//删除所有岗位		
	}      

	//显示右边框架的用户列表
	function showUserList(){	
		var curjobId;		
		var len=document.all("jobId").options.length;	
		var isnew = 0;
		var orgId = document.all("orgId").value;
		var jobSn=0;
		for (var i=0;i<len;i++){
	      	if(document.all("jobId").options[i].selected){      		
	        	curjobId = document.all("jobId").options[i].value;
	        	jobSn = i;
	        	var strs = curjobId.split(";"); 
	        	//没有保存的岗位
	        	if(strs.length == 2){
	        		isnew = "1";
	        		//alert(parent.frames.length);
	        		if(parent.frames[1])
	        		{
		        		parent.frames[1].document.forms[0].b1new.disabled=true;
		        		parent.frames[1].document.forms[0].b2save.disabled=true;
		        		parent.frames[1].document.forms[0].b3del.disabled=true;
		        		parent.frames[1].document.forms[0].button1.disabled=true;
		        		parent.frames[1].document.forms[0].button2.disabled=true;
		        		parent.frames[1].document.forms[0].button3.disabled=true;
		        		parent.frames[1].document.forms[0].button4.disabled=true;
	        		
		        		//for (var n=parent.frames[1].document.all("userList").options.length-1;n>=0;n--)
		    				//parent.frames[1].document.all("userList").options[n]=null;
	        			alert("请先保存");
	        			return;
	        		}
	        	}
	        }
	    }
	    if(isnew == 0 && parent.frames[1]){
			parent.frames[1].location.href="<%=rootpath%>/orgmanager/orgoperjob.do?method=getUserList&curjobid="+curjobId+"&orgId="+orgId+"&jobSn="+jobSn;
		}
		
	}


	function okadd(){
		var len=document.all("jobId").options.length;
		for (var i=0;i<len;i++){	      
	         document.all("jobId").options[i].selected="true";
	         }
	         document.forms[0].action="../user/userJob.do?method=storeUserJob";
	         document.forms[0].submit();
	}
//---------------------------------------
	var http_request = false;
	function send_request(url){
		document.all.divProcessing.style.display = "block";
		document.OrgJobForm.target="hiddenFrame";
		document.OrgJobForm.action = url;
		document.OrgJobForm.submit();
	}
	function refreshPage(){
	    location.href = "../user/userJob.do?method=getJobList&userId=<%=userId%>&orgId="+document.all("orgId").value;
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
	<%@ include file="/include/css.jsp"%>
	<body class="contentbodymargin" scroll="no" onload="loadDragDiv()">
		<div id="contentborder">
			<center>
				<input name="userId" value="<%=userId%>" type="hidden">
				<input name="orgId" value="<%=orgId%>" type="hidden">
				<form name="OrgJobForm" action="" target="hiddenFrame" method="post">
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
								机构已设岗位
							</td>
							<td width="20%" align="center">
								&nbsp;
							</td>
							<td width="40%" align="right">
								<%=user.getUserRealname()%>
								(
								<%=user.getUserName()%>
								)在该机构下已授予岗位
							</td>
						</tr>
						  <tr>
						  	<td width="40%" align="left">
						  		<input name="selectRoles" type="text" value="" onkeydown="enterKeydowngo()" onpropertychange="onBlurryQueryChange()" ondblclick=""  />
						  		<input name="que" type="button" value="查询" onclick="blurryQuary()" class="input" />
						  		<input name="nextQue" style="display:none" type="button" value="查找下一个" onclick="nextQuery()" class="input" />
						  	</td>
						    <td width="20%" align="center">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
						    <td width="40%" align="left">
						    	
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
							<td align="left">
							<div class="win" id="dd_1" align="left" title="下拉选择框的右边可以左右拖动">
								<select name="allist" multiple style="width:98%" onDBLclick="addjob()" size="18" title="下拉选择框的右边可以左右拖动">
									<pg:list sessionKey="allJob">
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
											<input name="button1" type="button" class="input" value="&gt;" onClick="addjob()">
										</td>
									</tr>
									<tr>
										<td align="center">
											&nbsp;
										</td>
									</tr>
									<tr>
										<td align="center">
											<input name="button2" type="button" class="input" value="&gt;&gt;" onClick="addall()">
										</td>
									</tr>
									<tr>
										<td align="center">
											&nbsp;
										</td>
									</tr>
									<tr>
										<td align="center">
											<input name="button3" type="button" class="input" value="&lt;&lt;" onClick="deleteall()">
										</td>
									</tr>
									<tr>
										<td align="center">
											&nbsp;
										</td>
									</tr>
									<tr>
										<td align="center">
											<input name="button4" type="button" class="input" value="&lt;" onClick="deletejob()">
										</td>
									</tr>
									<tr>
										<td align="center">
											&nbsp;
										</td>
									</tr>
								</table>
							</td>
							<td align="right"><div class="win" id="dd_2" align="left" title="下拉选择框的右边可以左右拖动">
								<select name="jobId" multiple style="width:98%" onChange="showUserList()" onDBLclick="deletejob()" size="18" title="下拉选择框的右边可以左右拖动">
									<pg:list sessionKey="existJob">
										<option value="<pg:cell colName="jobId"/>">
											<pg:cell colName="jobName" />
										</option>
									</pg:list>
								</select>
							</div>
							</td>

						</tr>
						<tr>
							<td align="center" colspan="4">
								<!--input name="butn" type="button" class="input" value="新增岗位" onClick="createjob()"-->
								<%
								 if (accessControl.checkPermission(myOrgId, "orgjobset", AccessControl.ORGUNIT_RESOURCE))
								 //if(accessControl.isAdmin())
					            {%>
								<input name="butn" type="button" class="input" value="设置机构岗位" onClick="imp()">
								<%}%>
								<input name="butn2" type="button" class="input" value="关闭" onclick="closed();">
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

