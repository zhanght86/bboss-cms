<%@ include file="../include/global1.jsp"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.db.GroupManagerImpl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.GroupManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Group"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%

	AccessControl accessControl = AccessControl.getInstance();
	accessControl.checkAccess(pageContext);

	//String parentId = (String) request.getAttribute("parentId");
	String groupId = (String) request.getParameter("groupId");
	String groupName="",groupDesc="",parentId="0";
	if(groupId!=null)
	{
		//依据组ID获取当前所选组的基本信息
		GroupManager groupManager=SecurityDatabase.getGroupManager();
		Group group=groupManager.getGroupByID(groupId);
		if(group!=null)
		{
			groupName=group.getGroupName();
			groupDesc=group.getGroupDesc();
			parentId=group.getParentId()+"";
		}
	}
	else
	{
		groupId="-1";
	}
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>用户组基本信息</title>
		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="../css/tab.winclassic.css">
		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="sysmanager/css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="sysmanager/css/tab.winclassic.css">

		<script language="javascript">	 		  
          //if("<%=groupId%>"!=""){
          	//getNavigatorContent().location.href ="groupTree.jsp?collapse=<%=parentId%>&request_scope=request&selectedNode=<%=groupId%>";
          //}
          //新建用户组
		  function editGroup() 
		  {
		  	getPropertiesContent().location.href="groupInfo_edit.jsp?groupId=" + <%=groupId%>;
		  }
		  //保存用户组
		  function saveGroup() 
		  {
		    var submitFlag=0;
			if(groupForm.groupName.value == "" || groupForm.groupName.value.length<1 || groupForm.groupName.value.replace(/\s/g,"")=="" || groupForm.groupDesc.value == "" || groupForm.groupDesc.value.length<1 || groupForm.groupName.value.replace(/\s/g,"")=="")
			{
				alert("带*为必填项");
				return false;
			}
			if(groupForm.parentId.value == "null" || groupForm.parentId.value.length<1)
			{
				alert("请先在左边选择组");
				return false;
			}
			var gn = groupForm.groupName.value;
			var gd = groupForm.groupDesc.value;
			if(gn.search(/[\\\/\|:\*\?<>"']/g)!=-1)
			{
				alert("组名称不能有\\/|:*?<>\"'!等特殊字符");
				return false;
			}
			if(gd.search(/[\\\/\|:\*\?<>"']/g)!=-1)
			{
				alert("组描述不能有\\/|:*?<>\"'!等特殊字符");
				return false;
			}
			if(gn.length>100)
			{
				alert("组名称过长,限制在100字符以内!");
				return false;
			}
			if(gd.length>100)
			{
				alert("组描述过长,限制在100字符以内!");
				return false;
			}
			if(groupForm.groupId.value !="" && groupForm.groupId.value !="0")
			{
				submitFlag=2;
			}
			else
			{
				submitFlag=1;
			}
			groupForm.action= "submitGroup.jsp?flag="+submitFlag;
			groupForm.target="submitGroup";
			groupForm.submit(); 
		  } 
		  //删除用户组
		  function delGroup() 
		  {
			if (groupForm.groupId.value != "") 
			{			
				if (window.confirm("删除将连带删除子组并且不可恢复，你确认要删除这个用户组吗？")) 
				{				
					groupForm.action= "submitGroup.jsp?flag=3";
					groupForm.target="submitGroup";
					groupForm.submit();
				}
			} 
			else 
			{
				alert("请先在左边的用户组列表中选择需要删除的组!");
			}
		  }
		  //窗体初始化处理
          function initial()
          {
            document.all.groupId.value="<%=groupId%>";
            document.all.parentId.value="<%=parentId%>";
          	document.all.groupName.value="<%=groupName%>";
          	document.all.oldGroupName.value="<%=groupName%>";
            document.all.groupDesc.value="<%=groupDesc%>";      	 
          }
          
          function addsongroup()
          {
          		document.location = "groupInfo_addson.jsp?groupId="+document.all.groupId.value;
          }
		</script>
	</head>
	<body onload="initial()" >
	<%if(groupId.equals("")){
	%>
	 <table>
	 <tr>
	 <td class="detailtitle">
	 请选择左边的组再进行操作
	 </td>
	 </tr> 
	<%
	}else{%>
	        <iframe src="" name="submitGroup" style="display:none"></iframe>
			<form action="" id="groupForm" method="post">
				<table width="100%" height="25" border="0" cellpadding="2" cellspacing="0" class="thin">
					<tr>
					<td colspan="2"><strong>&nbsp;用户组基本信息</strong></td>
					</tr>
					<tr>
						<td width="16%" height="25" class="detailtitle">
							<P align="center">
								用户组名称：
							</P>
						</td>
						<td width="907" height="25">
							<input name="groupName" type="text" style="width=50%" maxlength="100" readonly/>
						</td>
					</tr>
					<tr>
						<td height="25" class="detailtitle">
							<P align="center">
								用户组说明：
							</P>
						</td>
						<td height="25">
							<textarea name="groupDesc" rows="4" cols="50" readonly></textarea>
						</td>
					</tr>
			</table>
				<table width="100%" border="0" cellspacing="0" cellpadding="2">
					<tr>
						<td>
							<table width="60%" border="0" align="right" cellpadding="0" cellspacing="0">
								<tr>
									<td align="right">
										<%
										if(groupId!="")
										{
										if (accessControl.checkPermission(groupId,
							                       "editgroup", AccessControl.GROUP_RESOURCE))
							            {
										%>
										<input name="newButton" type="button" class="input" value="修改" onclick="editGroup();" />
										<%
										 }
										 if (accessControl.checkPermission(groupId,
							                       "deletegroup", AccessControl.GROUP_RESOURCE))
							            {%>
										<input type="button" class="input" value="删除" onclick="delGroup();" />
										<%
										}
										if (accessControl.checkPermission(groupId,
							                       "addsongroup", AccessControl.GROUP_RESOURCE))
							            {
										%>
										<input type="button" class="input" value="新增子组" onclick="addsongroup();" />
										<%
										}
										}
										%>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
				<input type="hidden" name="groupId" />
				<input type="hidden" name="parentId" />
				<input type="hidden" name="oldGroupName" />
			</form>
		<%}%>	
	</body>
</html>
