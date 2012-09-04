<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.GroupManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Group" %>
<%@ page import="java.util.List"%>
<%
	String groupId = (String)request.getSession().getAttribute("curGroupId");
	String orgid = request.getParameter("orgId");
	GroupManager groupManager=SecurityDatabase.getGroupManager();
	List userList=groupManager.getUserList(groupId,orgid,null);
	Group group = groupManager.getGroupByID(groupId);
	UserManager userManager = SecurityDatabase.getUserManager();
	List existUser = userManager.getUserList(group);
	request.setAttribute("existUser",existUser);
	request.setAttribute("allUser",userList);
%>
<html>
	<head>
		<title>属性容器</title>
		<script language="javascript" src="<%=request.getContextPath()%>/include/dragdiv.js"></script>
		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/sysmanager/css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/sysmanager/css/tab.winclassic.css">
		<script language="JavaScript">
  		var allowSubmit=false; 
		//添加选中的角色
		function addUser()
		{	
   			var selectUserIds="";
   			var selectedDrop=document.all("userIds");
   			var count=document.all("allist").options.length;
   			for(var i=0;i<count;i++)
   			{
   				//
   				var op=document.all("allist").options[i];
   				if(op.selected)
   				{
   					//	
					addone(op.text,op.value);
					//
					if(allowSubmit)selectUserIds+=","+op.value;
				}
   			}
   			if(selectUserIds.length>0)send_request(selectUserIds,'add');
		}
		//添加所有的角色
		function addall()
		{
    		var selectUserIds="";
			var count=document.all("allist").options.length;	  
    		for(var i=0;i<count;i++)
    		{
     			var op=document.all("allist").options[i];
     			addone(op.text,op.value);  
     			if(allowSubmit)selectUserIds+=","+op.value;
    		}
   			if(selectUserIds.length>0)send_request(selectUserIds,'add');
		}
		//将列表框中的选项进行追加
		function addone(name,value)
		{
   			allowSubmit=true;
   			for(var i=0;i<document.all("userIds").options.length;i++)
  			{
	 			if(value==document.all("userIds").options[i].value)
	 			{
	 				allowSubmit=false;
	 				break;
	 			}
  			}
   			if(allowSubmit)document.all("userIds").add(new Option(name,value));
		}
		//删除选中的角色
		function deleteUser()
		{
    		var selectUserIds="";
 			for(var m=document.all("userIds").options.length-1;m>=0;m--)
 			{
	    		if(document.all("userIds").options[m].selected)
	    		{
	    			selectUserIds+=","+document.all("userIds").options[m].value;
	    			document.all("userIds").options.remove(m);
	    		}
			}
			if(selectUserIds.length>0)send_request(selectUserIds,'delete');
		}
		//删除所有的角色
		function deleteall()
		{
    		var selectUserIds="";
    		var count=document.all("userIds").options.length;
			for (var m=0;m<count;m++)
			{
	    		selectUserIds+=","+document.all("userIds").options[m].value;
			}
			if(selectUserIds.length>0)
			{
				document.all("userIds").innerHTML="";
    			send_request(selectUserIds,'delete');
   			}	
		}
		//将参数提交至隐藏帧进行处理
		function send_request(selectUserIds,flag)
		{
    		selectUserIds=selectUserIds.substring(1,selectUserIds.length);
			document.all.divProcessing.style.display = "block";
			document.OrgJobForm.action = '<%=rootpath%>/sysmanager/groupmanager/editgroupuser.jsp?userId='+selectUserIds+'&groupId='+<%=groupId%>+'&flag='+flag;
			document.OrgJobForm.target = "hiddenFrame";
			document.OrgJobForm.submit();
		}
		</script>
	<body class="contentbodymargin" onload="loadDragDiv()">
		<div id="contentborder" align="center">
			<center>
				<form name="OrgJobForm" action="" method="post">
					<table width="80%" border="0" cellpadding="0" cellspacing="1" class="table">
						<tr class="tabletop">
						    <td width="40%" align="center">&nbsp;</td>
						    <td width="20%" align="center">&nbsp;</td>
						    <td width="40%" align="center">&nbsp;</td>
						  </tr>
						<tr class="tr">
							<td class="td" width="40%" align="center">
								可选用户
							</td>
							<td width="20%" class="td" align="center">
								&nbsp;
							</td>
							<td width="40%" align="center" class="td">
								已授予用户
							</td>
						</tr>
						<tr class="tabletop">
						    <td width="40%" align="center">&nbsp;</td>
						    <td width="20%" align="center">&nbsp;</td>
						    <td width="40%" align="center">&nbsp;</td>
						  </tr>
						<tr class="tr">
							<td class="td" align="right"><div class="win" id="dd_1" align="left">
								<select class="select" name="allist" multiple style="width:98%" onDBLclick="addRole()" size="18">
									<pg:list requestKey="allUser" needClear="false">
										<option value="<pg:cell colName="userId"/>">
											<pg:cell colName="userRealname" />(<pg:cell colName="userName" />)
										</option>
									</pg:list>
								</select></div>
							</td>

							<td align="center" class="td">
								<table class="table" width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr class="tr">
										<td align="center" class="td">
											<input name="button1" type="button" class="input" value="&gt;" onclick="addUser()">
										</td>
									</tr>
									<tr class="tr">
										<td align="center" class="td">
											&nbsp;
										</td>
									</tr>
									<tr class="tr">
										<td align="center" class="td">
											<input name="button2" type="button" class="input" value="&gt;&gt;" onclick="addall()">
										</td>
									</tr>
									<tr class="tr">
										<td align="center" class="td">
											&nbsp;
										</td>
									</tr>
									<tr class="tr">
										<td align="center" class="td">
											<input name="button3" type="button" class="input" value="&lt;&lt;" onclick="deleteall()">
										</td>
									</tr>
									<tr class="tr">
										<td align="center" class="td">
											&nbsp;
										</td>
									</tr>
									<tr class="tr">
										<td align="center" class="td">
											<input name="button4" type="button" class="input" value="&lt;" onclick="deleteUser()">
										</td>
									</tr>
									<tr class="tr">
										<td align="center" class="td">
											&nbsp;
										</td>
									</tr>
								</table>
							</td>
							<td class="td"><div class="win" id="dd_2" align="left">
								<select class="select" name="userIds" multiple style="width:98%" onDBLclick="deleterole()" size="18">
									<pg:list requestKey="existUser" needClear="false">
										<option value="<pg:cell colName="userId"/>">
											<pg:cell colName="userRealname" />(<pg:cell colName="userName" />)
										</option>
									</pg:list>
								</select></div>
							</td>
						</tr>						
					</table>
					<input type="hidden" name="orgId" value="<%=orgid%>" />
				</form>
			</center>
		</div>
		<div id=divProcessing style="width:200px;height:30px;position:absolute;left:150px;top:450px;display:none">
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

