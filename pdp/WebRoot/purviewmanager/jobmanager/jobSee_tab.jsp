<%
/*
 * <p>Title: 用户任职人员查看页面</p>
 * <p>Description: 用户任职查看页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-3-26
 * @author baowen.liu
 * @version 1.0
 */
%>


<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ page import="com.frameworkset.platform.security.AccessControl,
				 com.frameworkset.platform.sysmgrcore.entity.Job,
				 com.frameworkset.platform.sysmgrcore.manager.JobManager,
				 com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>
<%
	AccessControl control = AccessControl.getInstance();
	control.checkManagerAccess(request,response);
	String jobId = request.getParameter("jobId");
	JobManager jobManager = SecurityDatabase.getJobManager();
	Job job = jobManager.getJobById(jobId);
	String jobName = job.getJobName();
	
	//在职查看
	String queryUserName = "";
	if(request.getParameter("userName")!=null){
		queryUserName = request.getParameter("userName");
	}
	String queryOrgName = "";
	if(request.getParameter("orgname") != null){
		queryOrgName = request.getParameter("orgname");
	}
	//历史查看
	String queryUser_Name = "";
	if(request.getParameter("user_Name") != null){
		queryUser_Name = request.getParameter("user_Name");
	}
	String queryOrg_Name = "";
	if(request.getParameter("org_name") != null){
		queryOrg_Name = request.getParameter("org_name");
	}
	
%>
<html >
<head>
   <title>用户任职情况</title>
	<tab:tabConfig/>
	<script language="JavaScript" src="../scripts/common.js" type="text/javascript"></script>
	<script language="JavaScript" src="../pager.js" type="text/javascript"></script>
	<link href="../../html/stylesheet/common.css" rel="stylesheet" type="text/css" />
	<script language="javascript">
		
		//查询在职人员信息		
		function queryUser()
		{	
		    var username= document.userList.userName.value;
		    var orgname1=document.userList.orgname.value;
			var tablesFrame= document.getElementsByName("currentjob");
			//alert(tablesFrame[0].src);
			//if(document.userList.userName.value.length < 1 && document.userList.orgname.value.length < 1 ){
			//	alert("用户姓名和所在机构必须输入一个!!!");
			//	return;
			//}
			tablesFrame[0].src= "currentjob.jsp?jobId=<%=jobId%>&userName=" + username + "&orgname=" + orgname1;
			
		}
		
		//查询所有在职人员信息
		//function queryUserall()
		//{	
		 //   var tablesFrame= document.getElementsByName("currentjob");
			
		//	tablesFrame[0].src="currentjob.jsp?jobId=<%=jobId%>"
				
		//}
		
		//查询历史人员信息
		function queryUserHistory()
		{	
		    var username= document.userHistoryList.user_Name.value;
		    var orgname=document.userHistoryList.org_name.value;
		    var tablesFrame= document.getElementsByName("historyjob");
			//if(document.userHistoryList.user_Name.value.length < 1 && document.userHistoryList.org_name.value.length < 1 ){
			//	alert("用户姓名和所在机构必须输入一个!!!");
			//	return;
			//}
			tablesFrame[0].src= "historyjob.jsp?jobId=<%=jobId%>&user_Name=" + username + "&org_name=" + orgname;
		}
		
		//查询所有历史人员信息
		//function queryUserHistoryall()
		//{	
		//	var tablesFrame= document.getElementsByName("historyjob");
		//	tablesFrame[0].src="historyjob.jsp?jobId=<%=jobId%>"
		//}	
		function resetUserListQuery()
			{
				document.userList.userName.value="";
				document.userList.orgname.value="";
			}
		function resetUserHistoryListQuery()
			{
				document.userHistoryList.user_Name.value="";
				document.userHistoryList.org_name.value="";
			}
	</script>
</head>
<body>
<div style="height: 10px">&nbsp;</div>
  <tab:tabContainer id="foo-job-container" selectedTabPaneId="job_foo" skin="sany">
	<tab:tabPane id="job_foo" tabTitleCode="sany.pdp.jobmanage.take.office.record"  >
	<div id="searchblock">
				<div class="search_top">
					<div class="right_top"></div>
					<div class="left_top"></div>
				</div>
				<div class="search_box">
		<form name="userList" method="post" >
					<input name="jobId" value="<%=jobId%>" type="hidden">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td class="left_box"></td>
								<td>
									<table width="100%" border="0" cellpadding="0" cellspacing="0"
										class="table2">
         				<tr >
           					
           					<th>
           					<pg:message code="sany.pdp.workflow.user.username"/>：
           					</th>
           					<td><input type="text" name="userName" value="<%=queryUserName%>" ></td>
           					<th><pg:message code="sany.pdp.workflow.organization"/>：
           					</th>
           					<td>
           					<input type="text" name="orgname" value="<%=queryOrgName%>" >
           					</td>
           					<td height='30' valign='middle' align="left" >
           						<a class="bt_1" onClick="queryUser()"><span><pg:message code="sany.pdp.common.operation.search"/></span></a>
           						<a class="bt_2" onclick="resetUserListQuery()"><span><pg:message code="sany.pdp.common.operation.reset"/></span></a>
           				   </td>
           				 
           				</tr>
           				</table>
								</td>
								<td class="right_box"></td>
							</tr>
				</table>
			</div>
			<div class="search_bottom">
					<div class="right_bottom"></div>
					<div class="left_bottom"></div>
				</div>
			</div>

		  
			<iframe name="currentjob" src="currentjob.jsp?jobId=<%=jobId%>" style="width:102%" height="70%" frameborder="0" marginwidth="1" marginheight="1"></iframe>
			
		  
	</form>
	</tab:tabPane>
	<!-- ---------------------------------------------------------------------------------------------------------- -->
	<tab:tabPane id="job_bar" tabTitleCode="sany.pdp.jobmanage.his.office.record" tabTitle="历史职位查看">
	<div id="searchblock">
				<div class="search_top">
					<div class="right_top"></div>
					<div class="left_top"></div>
				</div>
				<div class="search_box">
		<form name="userHistoryList" method="post" >
					<input name="jobId" value="<%=jobId%>" type="hidden">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td class="left_box"></td>
								<td>
									<table width="100%" border="0" cellpadding="0" cellspacing="0"
										class="table2">
         				<tr >
           					
           					<th>
           					    <pg:message code="sany.pdp.workflow.user.username"/>：
           					 </th>
           					 <td>
           					 <input type="text" name="user_Name" value="<%=queryUser_Name%>" >
							</td>
							<th>
							   <pg:message code="sany.pdp.workflow.organization"/>：
							 </th>
							 <td>
							 <input type="text" name="org_name" value="<%=queryOrg_Name%>" >
							</td>
							<td height='30'valign='middle' align="left" colspan="2">
           						<a class="bt_1" onClick="queryUserHistory()"><span><pg:message code="sany.pdp.common.operation.search"/></span></a>
								<a class="bt_2" onclick="resetUserHistoryListQuery()"><span><pg:message code="sany.pdp.common.operation.reset"/></span></a>           					
           					</td>
           				</tr>						
					</table>
								</td>
								<td class="right_box"></td>
							</tr>
				</table>
			</div>
			<div class="search_bottom">
					<div class="right_bottom"></div>
					<div class="left_bottom"></div>
				</div>
			</div>
				  
					  	<iframe name="historyjob" src="historyjob.jsp?jobId=<%=jobId%>" style="width:102%" height="70%" frameborder="0" marginwidth="1" marginheight="1"></iframe>
					
	  </form>
	</tab:tabPane>
</tab:tabContainer>

</body>
</html>

