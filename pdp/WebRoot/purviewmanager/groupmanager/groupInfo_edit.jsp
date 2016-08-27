<%
/*
 * <p>Title: 用户组修改页面</p>
 * <p>Description: 对用户组信息进行修改</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
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
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.GroupManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Group"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.User"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>

<%

	AccessControl accessControl = AccessControl.getInstance();
	accessControl.checkManagerAccess(request,response);


	String groupId = (String) request.getParameter("groupId");
	String groupName="";
	String groupDesc="";
	String parentId="0";
	
	String userName="";
	String userRealName="";

	if(groupId != null && !"".equals(groupId))
	{
		//依据组ID获取当前所选组的基本信息
		GroupManager groupManager=SecurityDatabase.getGroupManager();
		UserManager userManager=SecurityDatabase.getUserManager();
		Group group=groupManager.getGroupByID(groupId);
		if(group != null)
		{
			groupName=group.getGroupName();
			groupDesc=group.getGroupDesc();
			parentId=group.getParentId()+"";
			
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
		<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
		<title>【<%=groupName%>】用户组基本信息</title>
<%@ include file="/include/css.jsp"%>
	   <link rel="stylesheet" type="text/css" href="../css/treeview.css">
<script type="text/javascript" src="../../include/jquery-1.4.2.min.js"></script>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
		<script language="javascript">	 		  
         
		var api = frameElement.api, W = api.opener;
       
		  //保存用户组
		  function saveGroup() 
		  {
		    var submitFlag=0;
			if(groupForm.groupName.value == "" || groupForm.groupName.value.length<1 || groupForm.groupName.value.replace(/\s/g,"")=="" ){
				W.$.dialog.alert("<pg:message code='sany.pdp.groupmanage.group.name.not.null'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
				return false;
			}
			if(groupForm.groupDesc.value == "" || groupForm.groupDesc.value.length<1 || groupForm.groupName.value.replace(/\s/g,"")=="")
			{
				W.$.dialog.alert("<pg:message code='sany.pdp.groupmanage.group.description.not.null'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
				return false;
			}
			var gn = groupForm.groupName.value;
			var gd = groupForm.groupDesc.value;
			if(gn.search(/[\\\/\|:\*\?<>"']/g)!=-1)
			{
				W.$.dialog.alert("<pg:message code='sany.pdp.groupmanage.group.name.invalid'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
				return false;
			}
			if(gd.search(/[\\\/\|:\*\?<>"']/g)!=-1)
			{
				W.$.dialog.alert("<pg:message code='sany.pdp.groupmanage.group.description.invalid'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
				return false;
			}
			if(gn.length>100)
			{
				W.$.dialog.alert("<pg:message code='sany.pdp.groupmanage.group.name.long'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
				return false;
			}
			if(gd.length>100)
			{
				W.$.dialog.alert("<pg:message code='sany.pdp.groupmanage.group.description.long'/>!!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
				return false;
			}
			
			groupForm.action= "submitGroup.jsp?flag=2";
			groupForm.target="submitGroup";
			groupForm.submit(); 
		  } 
		 
		  //窗体初始化处理
          function initial()
          {
            document.all.groupId.value='<%=groupId%>';
            document.all.parentId.value='<%=parentId%>';
          	document.all.groupName.value='<%=groupName%>';
          	document.all.oldGroupName.value='<%=groupName%>';
            document.all.groupDesc.value='<%=groupDesc%>';      	 
          }
          
          function back()
          {
          	window.returnValue = true;
          	window.close();
          }
		</script>
	</head>
	<body onload="initial()" scroll="no">
	        <iframe src="" name="submitGroup" style="display:none"></iframe>
			<form action="" id="groupForm" method="post">
			<div align="center">
				<table width="100%" height="25" border="0" cellpadding="2" cellspacing="0" class="table2">
					<tr>
							<th>
								<pg:message code="sany.pdp.groupmanage.group.name"/>：
							</th>
						<td>
							<input name="groupName" type="text" readonly />
						</td>
					</tr>
					<tr>
						<th>
								<pg:message code="sany.pdp.groupmanage.group.description"/>：
						</th>
						<td>
							<textarea name="groupDesc" style="width:350"></textarea>
						</td>
					</tr>
					<tr>
					<th>
							<pg:message code="sany.pdp.workflow.creater.username"/>：
					</th>
					<td>
						<input name="creatorName" id="creatorName" type="text" value="<%=userName%>【<%=userRealName%>】"  disabled=true/>
					</td>
				</tr>
			</table>
			</div>
			<table width="100%" border="0" cellspacing="0" cellpadding="2" >
					<tr>
						<td>
							<table width="60%" border="0" align="center" cellpadding="0" cellspacing="0">
								<tr>
									<td align="center">
									 <br>
										<a class="bt_1" onclick="saveGroup();" ><span><pg:message code="sany.pdp.common.operation.save"/></span></a>
										&nbsp;&nbsp;
										<a class="bt_2" onclick="closeDlg()"><span><pg:message code="sany.pdp.common.operation.exit"/></span></a>
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
</html>
