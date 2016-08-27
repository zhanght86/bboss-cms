
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ include file="../include/global1.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.JobManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Job" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.User" %>

<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>
<%
	String reFlush = "false";
	if (request.getAttribute("reFlush") != null) {
		reFlush = "true";
	}
	Integer currUserId = (Integer)session.getAttribute("currUserId");
	if(currUserId == null){
		currUserId = Integer.valueOf("-1");
	}
	String desc = (String)request.getParameter("pager.desc");
	String curOrgId = (String)session.getAttribute("orgId");
%>
<html >
      

<head>
	<%@ include file="/include/css.jsp"%>
	<link rel="stylesheet" type="text/css" href="../css/contentpage.css">
	<link rel="stylesheet" type="text/css" href="../css/tab.winclassic.css">
					
	<tab:tabConfig/>
	<script language="JavaScript" src="common.js" type="text/javascript"></script>
	<script language="JavaScript" src="../include/pager.js" type="text/javascript"></script>
	
	<script language="javascript">
	var jsAccessControl = new JSAccessControl("#DAE0E9","#ffffff","#F6F8FB");
	var reFlush = "<%=reFlush%>";
	//if ( reFlush == "true" && parent.userInfo.userInfo != null)
	//{
	//	parent.userInfo.userInfo.action="../user/userManager.do?method=newUser";	
	//	parent.userInfo.userInfo.submit();	
	//}
	
	function getUserInfo(e,userId)
	{
		if (jsAccessControl.setBackColor(e))
		{
			//parent.userInfo.userInfo.action="../user/userManager.do?method=getUser"+"&userId="+userId;
			//parent.userInfo.userInfo.submit();	
			getPropertiesToolbar().location.href="properties_toolbar.jsp?userId="+userId+"&orgId=<%=curOrgId%>";
		}
	}
	
	function dealRecord(dealType) {
	    var isSelect = false;
	    var outMsg;
		var userList = document.userList;    
	    for (var i=0;i<userList.elements.length;i++) {
			var e = userList.elements[i];
				
			if (e.name == 'checkBoxOne'){
				if (e.checked){
		       		isSelect=true;
		       		break;
			    }
			}
	    }
	    if (isSelect){
	    	if (dealType==1){
	    		outMsg = "你确定要删除吗？(删除后是不可以再恢复的)。";
	        	if (confirm(outMsg)){
					userList.action="../user/userManager.do?method=deleteUser";
					userList.submit();
		 			return true;
				}
			} 
	    }else{
	    	alert("至少要选择一条记录！");
	    	return false;
	    }
		return false;
	}
		
	function sortBy(byName){
		var desc = "false";
	
		if ( "<%=desc%>"  == desc ) desc = "true";
		userList.action="../user/userList.jsp?orgId=<%=curOrgId%>&pager.offset=0&pager.sortKey="+byName+"&pager.desc="+desc;
		userList.submit();
	}
		
	function actionOnmouseover(e){	
		e.style.backgroundColor = "#8080FF";
	}
	
	function actionOnmouseup(e){
		e.style.backgroundColor = "#BEC0C2";
	}
	
	function newUser()
	{
		document.location.href="../user/userManager.do?method=newUser";	
	}
	function queryUser()
	{	
		var userList = document.userList;
		//if(document.userList.userName.value.length < 1 && document.userList.userRealname.value.length < 1){
		//	alert("用户名称和用户实名必须输入一个!!!");
		//	return;
		//}
		userList.action="../user/changeJob_tab.jsp?orgId=<%=curOrgId%>"
		userList.submit();	
	}
	function advQueryUser()
	{	
		document.location.href="../user/advQuery.jsp?orgId=<%=curOrgId%>"	
	}
			
	var jsAccessControl = new JSAccessControl("#DAE0E9","#ffffff","#F6F8FB");
	var thisUserId = "<%=currUserId.toString()%>";
	var userList = document.userList;
	if(userList)
	    for (var i=0;i<userList.elements.length;i++) {
			var e = userList.elements[i];
	
			if (e.name == "checkBoxOne"){
				if (e.value == thisUserId ){
					var obj = e.parentElement;
					var tableObj = obj.parentElement;
					tableObj.style.backgroundColor = jsAccessControl.heightLightColor;
					break;
			    }
			}
	    }		
		
		
	//查看岗位信息的------------------------------------------------------------------	
		function queryUserjob()
		{	
			//if(document.jobList.uName.value.length < 1 && document.jobList.jobname.value.length < 1){
			//	alert("用户姓名和所属岗位必须输入一个!!!");
			//	return;
			//}
			jobList.action="../user/changeJob_tab.jsp"
			jobList.submit();	
		}
		function queryUserall()
		{	
			jobList.action="../user/changeJob_tab.jsp"
			jobList.submit();	
		}
		function queryUserHistory()
		{	
			//if(document.userHistoryList.user_Name.value.length < 1  && document.userHistoryList.job_name.value.length < 1){
			//	alert("用户姓名和所属岗位必须输入一个!!!");
			//	return;
			//}
			userHistoryList.action="../user/changeJob_tab.jsp"
			userHistoryList.submit();	
		}
		function queryUserHistoryall()
		{	
			userHistoryList.action="../user/changeJob_tab.jsp"
			userHistoryList.submit();	
		}	
			
	</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></head>
<body>
<tab:tabContainer id="user-list-container" selectedTabPaneId="user-tab">
	<tab:tabPane id="user-tab" tabTitle="用户信息">
		
		<form name="userList" method="post" >
					<input name="orgId" value="<%=curOrgId%>" type="hidden">
					<table cellspacing="1" cellpadding="0" border="0" bordercolor="#EEEEEE" width=98% class="thin">
         				<tr valign='top'>
           					<td height='30'valign='middle' colspan="5"><img src='../images/edit.gif' width="16" height="16" border=0 align='absmiddle'>&nbsp;<strong>用户列表</strong></td>
         				</tr>
         				<tr >
           					
           					<td colspan="2" height='30'valign='middle' align="center">用户名称：<input type="text" name="userName" ></td>
           					<td height='30'valign='middle' align="center">用户实名：<input type="text" name="userRealname" ></td>
           					<td height='30'valign='middle' align="center">
           						<input name="search" type="button" class="input" value="查询" onClick="queryUser()">           						
           						
           					</td><td  height='30'valign='middle' align="center"><input name="newuser" type="button" class="input" value="新增" onClick="newUser()">	</td>
         				</tr>
						<pg:listdata dataInfo="UserListSn" keyName="UserListSn" />
						<!--分页显示开始,分页标签初始化-->
						<pg:pager maxPageItems="15" scope="request" data="UserListSn" isList="false">
							<tr>
								<!--设置分页表头-->
								<td class="headercolor" width="10">
									<input type="checkBox" name="checkBoxAll" onClick="checkAll('checkBoxAll','checkBoxOne')" width="10">
								</td>
								<td class="headercolor" onclick="sortBy('userName')" >用户登陆名称	</td>
								<td class="headercolor" onclick="sortBy('userName')" >用户实名</td>
								<td class="headercolor" onclick="sortBy('userName')"  >用户类型</td>
								<td class="headercolor" onclick="sortBy('userName')"  >邮箱地址</td>								
								
							</tr>
							<pg:param name="orgId" />
							<pg:param name="userName" />
							<pg:param name="userRealname" />

							<!--检测当前页面是否有记录-->
							<pg:notify>
								<tr height="18px" class="labeltable_middle_tr_01">
									<td colspan=100 align='center'>
										暂时没有用户
									</td>
								</tr>
							</pg:notify>

							<!--list标签循环输出每条记录-->
							<pg:list>
								<%
								     //String backColor="";
								     //if (  )
								     //	backColor="bgcolor=#ff0000";
								     //}
								%>
								<tr onMouseOver="this.className='mousestyle1'" onMouseOut="this.className= 'mousestyle2'" onclick="getUserInfo(this,'<pg:cell colName="userId" defaultValue=""/>')" >
									<td class="tablecells" nowrap="nowrap">
										<input type="checkBox" name="checkBoxOne" onClick="checkOne('checkBoxAll','checkBoxOne')" value="<pg:cell colName="userId" defaultValue=""/>" width="10">
									</td>
									<td class="tablecells" nowrap="nowrap">
										<pg:cell colName="userName" defaultValue="" />
									</td>
									<td class="tablecells" nowrap="nowrap">
										<pg:cell colName="userRealname" defaultValue="" />
									</td>									
									<td class="tablecells" nowrap="nowrap" >
									<%
										String userType = dataSet.getString(Integer.parseInt(rowid),"userType");
									%>
									<dict:itemname type="userType" itemValue="<%=userType%>" />
									</td>
									<td class="tablecells" nowrap="nowrap">
										<pg:notnull colName="userEmail"><pg:cell colName="userEmail" defaultValue=" "/></pg:notnull>
										<pg:null colName="userEmail">没有邮箱</pg:null>
										<pg:equal colName="userEmail" value="">没有邮箱</pg:equal>
									</td>	
								</tr>
							</pg:list>
							<tr height="18px" class="labeltable_middle_tr_01">
								<td colspan=4 align='center'>
									<pg:index />
								</td><td><input type="submit" value="删除" class="input" onclick="javascript:dealRecord(1); return false;"></td>							</tr>
							<input name="queryString" value="<pg:querystring/>" type="hidden">
						</pg:pager>

		  </table>
	</form>
		
	</tab:tabPane>
	<!-- ------------------------------------------------------------------------------------------- -->
	<tab:tabPane id="job-tab" tabTitle="岗位情况">
		<form name="jobList" method="post" >
					<input name="orgId" value="<%=curOrgId%>" type="hidden">
					<table cellspacing="1" cellpadding="0" border="0" bordercolor="#EEEEEE" width=98% class="thin">
         				<tr valign='top'>
           					<td height='30'valign='middle' colspan="5">&nbsp;<strong>用户岗位列表</strong></td>
         				</tr>
         				<tr >
           					
           					<td height='30'valign='middle' align="center">用户姓名：</td>
           					<td height='30'valign='middle' align="center"><input type="text" name="uName" ></td>
           					<td height='30'valign='middle' align="center">所属岗位：</td>
           					<td height='30'valign='middle' align="center"><input type="text" name="jobname" ></td>
           					<td height='30'valign='middle' align="center">
           						<input name="search" type="button" class="input" value="查询" onClick="queryUserjob()">
           						<input name="search1" type="button" class="input" value="查看所有" onClick="queryUserall()">
           				   </td>
           				 
           				</tr>
						<pg:listdata dataInfo="OrgjobSeeList" keyName="OrgjobSeeList" />
<!--						分页显示开始,分页标签初始化-->
						<pg:pager maxPageItems="15" scope="request" data="OrgjobSeeList" isList="false">
							<tr>
<!--								设置分页表头-->
								
								<td class="headercolor" height='30'>用户姓名</td>
								<td class="headercolor">所属岗位</td>
								<td class="headercolor">所在机构</td>
								<td class="headercolor">上岗时间</td>								
								<td class="headercolor">岗位状态</td>
							</tr>
							<pg:param name="orgname" />
							<pg:param name="userName" />
							<pg:param name="jobname" />
							<pg:param name="orgId" />

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
								
							</pg:list>							<input name="queryString" value="<pg:querystring/>" type="hidden">
							
						</pg:pager>

		  </table>
	</form>
	</tab:tabPane>
	<!-- --------------------------------------------------------------------------------------------------------- -->
	<tab:tabPane id="bar-tab" tabTitle="离退人员信息">
		<form name="userHistoryList" method="post" >
					<input name="orgId" value="<%=curOrgId%>" type="hidden">
					<table cellspacing="1" cellpadding="0" border="0" width=98%  class="thin">
         				<tr valign='top'>
           					<td height='30'valign='middle' colspan="6">&nbsp;<strong>用户岗位历史列表</strong></td>
         				</tr>
         				<tr >
           					
           					<td height='30'valign='middle' align="center">用户姓名：</td>
           					<td height='30'valign='middle' align="center"><input type="text" name="user_Name" ></td>
<!--           					<td height='30'valign='middle' align="center">所在机构：<input type="text" name="org_name" ></td>-->
           					<td height='30'valign='middle' align="center" colspan="2">所属岗位：</td>
           				    <td height='30'valign='middle' align="center"><input type="text" name="job_name" ></td>
           					<td height='30'valign='middle' align="center" >
           						<input name="search" type="button" class="input" value="查询" onClick="queryUserHistory()">
								<input name="search1" type="button" class="input" value="查看所有" onClick="queryUserHistoryall()">           					
           					</td>
           				</tr>
						<pg:listdata dataInfo="OrgjobSee_HistoryList" keyName="OrgjobSee_HistoryList" />
						<!--分页显示开始,分页标签初始化-->
						<pg:pager maxPageItems="15" scope="request" data="OrgjobSee_HistoryList" isList="false">
							<tr>
								<!--设置分页表头-->
								
								<td class="headercolor" height='30'>用户姓名</td>
								<td class="headercolor">所属岗位</td>
								<td class="headercolor">所在机构</td>
								<td class="headercolor">上岗时间</td>	
								<td class="headercolor">离岗时间</td>								
								<td class="headercolor">岗位状态</td>
							</tr>
							<pg:param name="org_name" />
							<pg:param name="user_Name" />
							<pg:param name="job_name" />
							<pg:param name="orgId" />
							<!--检测当前页面是否有记录-->
							<pg:notify>
								<tr height="18px" class="labeltable_middle_tr_01">
									<td colspan=100 align='center'>
											没有历史用户
									</td>
								</tr>
							</pg:notify>

							<!--list标签循环输出每条记录-->
							<pg:list>								
								<tr onmouseover="this.className='mouseover'" onmouseout="this.className= 'mouseout'" onclick="" >
									
									<td class="tablecells" nowrap="nowrap" height='20'>
										<pg:cell colName="userRealname" defaultValue="" />
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
								<td colspan=6 align='center'>
									<pg:index />
								</td>						
							</tr>
							<input name="queryString" value="<pg:querystring/>" type="hidden">
						</pg:pager>

		  </table>
	</form>
	</tab:tabPane>
</tab:tabContainer>
<%@ include file="../sysMsg.jsp"%>
</body>
</html>

