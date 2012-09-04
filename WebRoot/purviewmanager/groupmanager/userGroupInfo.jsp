<%
/*
 * <p>Title: 用户组信息</p>
 * <p>Description: 查看用户组信息</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: chinacreator</p>
 * @Date 2008-3-18
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
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.GroupManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.User"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Group"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>

<%

	AccessControl accessControl = AccessControl.getInstance();
	accessControl.checkManagerAccess(request,response);

	String groupId = (String) request.getParameter("groupId");
	
	String groupName = "";
	String groupDesc = "";
	String userName="";
	String userRealName="";
	if(groupId != null && !groupId.equals(""))
	{
		//依据组ID获取当前所选组的基本信息
		GroupManager groupManager=SecurityDatabase.getGroupManager();
		UserManager userManager=SecurityDatabase.getUserManager();
		Group group=groupManager.getGroupByID(groupId);
		if(group != null)
		{
			groupName=group.getGroupName();
			groupDesc=group.getGroupDesc();	
			String groupOwnerId=String.valueOf(group.getOwner_id());
			User user=userManager.getUserById(groupOwnerId);
			userName=user.getUserName();
			userRealName=user.getUserRealname();
		}
	}
	else
	{
		groupId="";
	}
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>【<%=groupName%>】用户组基本信息</title>
		<script language="JavaScript" src="../scripts/common.js" type="text/javascript"></script>
		<script language="JavaScript" src="../../include/pager.js" type="text/javascript"></script>
		<script type="text/javascript" src="../../html/js/commontool.js"></script>
		<script language="JavaScript">
			function init()
			{
				document.getElementById("groupName").value = "<%=groupName%>";	
				document.getElementById("groupDesc").value = "<%=groupDesc%>";
			}
		</script>
	</head>
	<body onload="init()">
	<%
		if(groupId.equals(""))
	{
	%>
	 <table>
		 <tr>
	 		<td class="detailtitle"><pg:message code="sany.pdp.groupmanage.choose.group.first"/></td>
	 	</tr> 
	 </table>
	<%
	}
	else
	{
	%>
	<form action="" id="groupForm" method="post">
		<table width="100%" height="25" border="0" cellpadding="2" cellspacing="0" class="table2">
				<tr>
					<td width="16%" height="25" class="detailtitle">
						<P align="center">
							<pg:message code="sany.pdp.groupmanage.group.name"/>：
						</P>
					</td>
					<td width="907" height="25">
						<input name="groupName" id="groupName" type="text" value="<%=groupName%>" style="width=50%" maxlength="100" readonly/>
					</td>
				</tr>
				<tr>
					<td height="25" class="detailtitle">
						<P align="center">
							<pg:message code="sany.pdp.groupmanage.group.description"/>：
						</P>
					</td>
					<td height="25">
						<textarea name="groupDesc" id="groupDesc" rows="4" cols="50" readonly="true" style="TEXT-ALIGN: left" wrap="soft">
						</textarea>
					</td>
				</tr>
				<tr>
					<td width="16%" height="25" class="detailtitle">
						<P align="center">
							<pg:message code="sany.pdp.workflow.creater.username"/>：
						</P>
					</td>
					<td width="907" height="25">
						<input name="creatorName" id="creatorName" type="text" value="<%=userName%>【<%=userRealName%>】" style="width=50%" maxlength="100" readonly/>
					</td>
				</tr>
		</table>
		<br/>
		<div align="center">
			<a class="bt_2" onclick="closeDlg()"><span><pg:message code="sany.pdp.common.operation.exit"/></span></a>
		</div>
	</form>
	<%
	}
	%>	
	</body>
</html>
