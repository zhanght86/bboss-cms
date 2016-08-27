<%@ include file="../../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ page
	import="com.frameworkset.platform.security.AccessControl,
	com.frameworkset.platform.sysmgrcore.entity.Job,
	com.frameworkset.platform.sysmgrcore.manager.JobManager,
	com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>
<%
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request,response);
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
	<%@ include file="/include/css.jsp"%>
	<link rel="stylesheet" type="text/css" href="../../css/contentpage.css">
	<link rel="stylesheet" type="text/css" href="../../css/tab.winclassic.css">
<%@ include file="/include/css.jsp"%>
	<link rel="stylesheet" type="text/css" href="../sysmanager/css/contentpage.css">
	<link rel="stylesheet" type="text/css" href="../sysmanager/css/tab.winclassic.css">
					
	<tab:tabConfig/>
	<script language="JavaScript" src="common.js" type="text/javascript"></script>
	<script language="JavaScript" src="../../include/pager.js" type="text/javascript"></script>
	
	<script language="javascript">
		
			
		
		
		
		
		function queryUser()
		{	
			if(document.userList.userName.value.length < 1 && document.userList.orgname.value.length < 1 ){
				alert("用户姓名和所在机构必须输入一个!!!");
				return;
			}
			userList.action="../../jobmanager/A03/jobSee_tab.jsp"
			userList.submit();	
		}
		function queryUserall()
		{	
			document.userList.userName.value="";
			document.userList.orgname.value="";
			userList.action="../../jobmanager/A03/jobSee_tab.jsp"
			userList.submit();	
		}
		function queryUserHistory()
		{	
			if(document.userHistoryList.user_Name.value.length < 1 && document.userHistoryList.org_name.value.length < 1 ){
				alert("用户姓名和所在机构必须输入一个!!!");
				return;
			}
			userHistoryList.action="../../jobmanager/A03/jobSee_tab.jsp"
			userHistoryList.submit();	
		}
		function queryUserHistoryall()
		{	
			document.userHistoryList.user_Name.value="";
			document.userHistoryList.org_name.value="";
			userHistoryList.action="../../jobmanager/A03/jobSee_tab.jsp"
			userHistoryList.submit();	
		}	
			
	</script>
</head>
<body>
<tab:tabContainer id="foo-job-container" selectedTabPaneId="job_foo">
	<tab:tabPane id="job_foo" tabTitle="在职查看">
		<form name="userList" method="post" >
					<input name="jobId" value="<%=jobId%>" type="hidden">
					<table width=98% border="0" cellpadding="0" cellspacing="1" class="thin">
         				<tr valign='top'>
           					<td height='30'valign='middle' colspan="6">&nbsp;<strong><%=jobName%>:在职情况列表</strong></td>
         				</tr>
         				<tr >
           					
           					<td height='30'valign='middle' align="left" colspan="5">
           					用户姓名：<input type="text" name="userName" value="<%=queryUserName%>" >&nbsp;&nbsp;&nbsp;&nbsp;
           					所在机构：<input type="text" name="orgname" value="<%=queryOrgName%>" >
           					</td>
<!--           					<td height='30'valign='middle' align="center" colspan="2">所属岗位：<input type="text" name="jobname" >-->
<!--           				    </td>-->
           					<td height='30' valign='middle' align="left" >
           						<input name="search" type="button" class="input" value="查询" onClick="queryUser()">
           						<input name="search1" type="button" class="input" value="查看所有" onClick="queryUserall()">
           				   </td>
           				 
           				</tr>
						<pg:listdata dataInfo="JobSeeList" keyName="JobSeeList" />
<!--						分页显示开始,分页标签初始化-->
						<pg:pager maxPageItems="13" id="JobSeeList" scope="request" data="JobSeeList" isList="false">
							<tr>
<!--								设置分页表头-->
								
								<td class="headercolor" height='29'>用户姓名</td>
								<td class="headercolor">用户登录名</td>
								<td class="headercolor">所属岗位</td>
								<td class="headercolor">所在机构</td>
								<td class="headercolor">上岗时间</td>								
								<td class="headercolor">岗位状态</td>
							</tr>
							<pg:param name="orgname" />
							<pg:param name="userName" />
							<pg:param name="jobname" />
							<pg:param name="jobId" />
<!--							检测当前页面是否有记录-->
							<pg:notify>
								<tr height="18px" class="labeltable_middle_tr_01">
									<td colspan=100 align='center'>
										职位暂缺
									</td>
								</tr>
							</pg:notify>

<!--							list标签循环输出每条记录-->
							<pg:list>								
								<tr onmouseover="this.className='mouseover'" onmouseout="this.className= 'mouseout'" onclick="" >
									
									<td class="tablecells" nowrap="nowrap" height='20'>
										<pg:cell colName="userRealname" defaultValue="" />
									</td>
									<td class="tablecells" nowrap="nowrap" height='20'>
										<pg:cell colName="userName" defaultValue="" />
									</td>
									<td class="tablecells" nowrap="nowrap">
										<pg:cell colName="jobName" defaultValue="" />
									</td>									
									<td class="tablecells" nowrap="nowrap" >
										<pg:cell colName="orgName" defaultValue="" />
									</td>
									<td class="tablecells" nowrap="nowrap">
										<pg:cell colName="jobStartTime" defaultValue="未知" dateformat="yyyy-MM-dd HH:mm:ss"/>
									</td>
									
									<td class="tablecells" nowrap="nowrap">
										
										<pg:equal colName="fettle" value="1">在岗</pg:equal>
					  					<pg:equal colName="fettle" value="0">未知</pg:equal>																										    					    							    				    		
					  					<pg:equal colName="fettle" value=" ">未知</pg:equal>
									</td>	
								</tr>
							</pg:list>
							<tr height="18px" class="labeltable_middle_tr_01">
								<td colspan=6 align='center'>
									 <pg:index />
								</td>						
							</tr>
							<input name="queryString" value="<pg:querystring/>" type="hidden">
						</pg:pager>

		  </table>
	</form>
	</tab:tabPane>
	<!-- ---------------------------------------------------------------------------------------------------------- -->
	<tab:tabPane id="job_bar" tabTitle="历史职位查看">
		<form name="userHistoryList" method="post" >
					<input name="jobId" value="<%=jobId%>" type="hidden">
					<table cellspacing="1" cellpadding="0" border="0" width=98%  class="thin">
         				<tr valign='top'>
           					<td height='30'valign='middle' colspan="7">&nbsp;<strong><%=jobName%>:历史任职列表</strong></td>
         				</tr>
         				<tr >
           					
           					<td height='30'valign='middle' align="left" colspan="5" >
           					    用户姓名：<input type="text" name="user_Name" value="<%=queryUser_Name%>" >&nbsp;&nbsp;&nbsp;&nbsp;
							    所在机构：<input type="text" name="org_name" value="<%=queryOrg_Name%>" >
							</td>
							<td height='30'valign='middle' align="left" colspan="2">
           						<input name="search" type="button" class="input" value="查询" onClick="queryUserHistory()">
								<input name="search1" type="button" class="input" value="查看所有" onClick="queryUserHistoryall()">           					
           					</td>
           				</tr>
						<pg:listdata dataInfo="JobHistoryList" keyName="JobHistoryList" />
						<!--分页显示开始,分页标签初始化-->
						<pg:pager maxPageItems="13" id="JobHistoryList" scope="request" data="JobHistoryList" isList="false">
							<tr>
								<!--设置分页表头-->
								
								<td class="headercolor" height='30'>用户姓名</td>
								<td class="headercolor">用户登录名</td>
								<td class="headercolor">所属岗位</td>
								<td class="headercolor">所在机构</td>
								<td class="headercolor">上岗时间</td>	
								<td class="headercolor">离岗时间</td>								
								<td class="headercolor">岗位状态</td>
							</tr>
							<pg:param name="org_name" />
							<pg:param name="user_Name" />
							<pg:param name="job_name" />
							<pg:param name="jobId" />
							<!--检测当前页面是否有记录-->
							<pg:notify>
								<tr height="18px" class="labeltable_middle_tr_01">
									<td colspan=100 align='center'>
										没有历史记录
									</td>
								</tr>
							</pg:notify>

							<!--list标签循环输出每条记录-->
							<pg:list>								
								<tr onmouseover="this.className='mouseover'" onmouseout="this.className= 'mouseout'" onclick="" >
									
									<td class="tablecells" nowrap="nowrap" height='20'>
										<pg:cell colName="userRealname" defaultValue="" />
									</td>
									<td class="tablecells" nowrap="nowrap" height='20'>
										<pg:cell colName="userName" defaultValue="" />
									</td>
									<td class="tablecells" nowrap="nowrap">
										<pg:cell colName="jobName" defaultValue="" />
									</td>									
									<td class="tablecells" nowrap="nowrap" >
										<pg:cell colName="orgName" defaultValue="" />
									</td>
									<td class="tablecells" nowrap="nowrap">
										<pg:cell colName="jobStartTime" defaultValue="未知" dateformat="yyyy-MM-dd HH:mm:ss"/>
									</td>
									<td class="tablecells" nowrap="nowrap">
										<pg:cell colName="quashTime" defaultValue="未知" dateformat="yyyy-MM-dd HH:mm:ss"/>
									</td>
									
									<td class="tablecells" nowrap="nowrap">
										
										<pg:equal colName="fettle" value="1">在岗</pg:equal>
					  					<pg:equal colName="fettle" value="0">离岗</pg:equal>																										    					    							    				    		
					  					<pg:equal colName="fettle" value=" ">未知</pg:equal>
									</td>	
								</tr>
							</pg:list>
							<tr height="18px" class="labeltable_middle_tr_01">
								<td colspan=7 align='center'>
									 <pg:index />
								</td>						
							</tr>
							<input name="queryString" value="<pg:querystring/>" type="hidden">
						</pg:pager>

					</table>
	</form>
	</tab:tabPane>
</tab:tabContainer>
<%@ include file="../../sysMsg.jsp"%>
</body>
</html>

