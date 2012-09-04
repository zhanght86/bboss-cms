<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">


<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.User"%>
<%@ page
	import="com.frameworkset.platform.sysmgrcore.manager.UserManager"%>
<%@ page
	import="com.frameworkset.platform.sysmgrcore.manager.db.UserManagerImpl"%>

<%
			AccessControl accesscontroler = AccessControl.getInstance();
			accesscontroler.checkManagerAccess(request,response);

			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", -1);
			response.setDateHeader("max-age", 0);
			
			String groupName = request.getParameter("groupName");
			String groupDesc = request.getParameter("groupDesc");
			String groupOwnerName = request.getParameter("groupOwnerName");

			
			groupName = groupName == null ? "" : groupName ;
			groupDesc = groupDesc == null ? "" : groupDesc ;
			groupOwnerName = groupOwnerName == null ? "" : groupOwnerName ;


		%>
<html>
<head>
<%@ include file="/common/jsp/css.jsp"%>
<link href="stylesheet/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/commontool.js"></script>	
		<title>用户组管理</title>
		<style type="text/css">
	.STYLE1 {color: #0000FF}
	.STYLE2 {color: #000099}
	.style3 
	{
		font-size: 14px;
		font-weight: bold;
		color: #3300FF;
	}
	.operStyle
	{
	width:17;
	height:16;
	}
</style>

<SCRIPT LANGUAGE="JavaScript">	
function validateInfo()
{
	var gn = groupForm.groupName.value;
	var gd = groupForm.groupDesc.value;
	var gon = groupForm.groupOwnerName.value;
	var re =  /^[A-Za-z0-9\u4e00-\u9fa5]+$/; 
	
	if((gn == "" || gn.length<1 || gn.replace(/\s/g,"")=="") 
		&& (gd == "" || gd.length<1 || gd.replace(/\s/g,"")=="")
		&& (gon == "" || gon.length<1 || gon.replace(/\s/g,"")==""))
	{
		alert('查询条件不能为空!');
		return false;
	}
	
	if(gn == "" || gn.length<1 || gn.replace(/\s/g,"")=="")
		{
		
		}
	else if(!re.test(gn))
	{
		alert('用户组名称不能有非数字、中文、字母的字符');
		return false; 
	}
	
	if(gd == "" || gd.length<1 || gd.replace(/\s/g,"")=="")
		{
		
		}
	else if(!re.test(gd))
	{
		alert('用户组描述不能有非数字、中文、字母的字符');
		return false; 
	}
	var regUser = /^\w+$/;
	if(!regUser.test(gon))
		{
			alert('创建人中不能有非数字、字母、下划线的字符!');
			return false;
		}
	groupForm.action = "groupInfo.jsp";
	groupForm.submit();
}

function clearInfo()
{
	document.getElementById("groupName").value = '';
	document.getElementById("groupDesc").value = '';
	document.groupForm.groupOwnerName.value="";
}

var tempObj = null;
function changeRowColor(obj) {
   if(obj.flag == "true")
   {
   		obj.flag = "false";
   		obj.style.removeAttribute("backgroundColor");  
	    obj.style.color = 'black';
	    tempObj = null;
	    return ;
   }
   else
   {
   		obj.flag = "true";
   		obj.style.background='#191970';   //把点到的那一行变希望的颜色; 
   		obj.style.color = 'white';
   }
   if(tempObj!=null && tempObj.flag == "true"){
        tempObj.flag = "false";
        tempObj.style.removeAttribute("backgroundColor");  
	    tempObj.style.color = 'black';
   }
   tempObj = obj;
}

function returnUserName(){
	document.groupForm.action="../test/showActivitiNodeEdit.page";
	document.groupForm.submit();
}

function dbClickChoose(obj){
	var groupArr = new Array();
	groupArr = document.getElementById("groups").value.split(",");
	for(i=0;i<groupArr.length;i++){
		if(obj.id==groupArr[i]){
			return;
		}
	}
	if($("#groups").val()!='null'&&$("#groups").val()!=''){
		$("#groups").val($("#groups").val()+","+obj.id);
	}else{
		$("#groups").val(obj.id);
	}
	$("#chooosegrouplistContainer").load("queryCandidateGroupByGroups.page #choosegrouplistContent", {groups:$("#groups").val()}); 
}

function deleteChooseGroup(obj){
	var groups = "";
	var groupArr = new Array();
	groupArr = document.getElementById("groups").value.split(",");
	for(i=0;i<groupArr.length;i++){
		if(obj==groupArr[i]){
		}else{
			if(groups==""){
				groups = groupArr[i];
			}else{
				groups += ","+groupArr[i];
			}
			
		}
	}
	$("#groups").val(groups);
	$("#chooosegrouplistContainer").load("queryCandidateGroupByGroups.page #choosegrouplistContent", {groups:$("#groups").val()});

}

$(document).ready(function() {
	 $("#chooosegrouplistContainer").load("queryCandidateGroupByGroups.page #choosegrouplistContent", {groups:$("#groups").val()}); 
   });

</SCRIPT>
	</head>
	<body class="easyui-layout">
	<div region="north" split="true" title="组织结构"
		style="height: 150px; padding1: 1px; overflow: hidden;">
		<pg:listdata
	dataInfo="com.frameworkset.platform.sysmgrcore.purviewmanager.GroupSearchList"
	keyName="GroupSearchList" />
<!--分页显示开始,分页标签初始化-->
<pg:pager maxPageItems="10" scope="request" data="GroupSearchList"
	isList="false">

		<div id="contentborder" align="center">
			<form name="groupForm" method="post">
				<input type="hidden" value="${groups}" id="groups" name="groups"/>
				<input type="hidden" value="${nodeinfoId}" id="nodeinfoId" name="nodeId"/>
				<table width="100%" border="0" cellpadding="0" cellspacing="0"
					class="stable" id="tb">
					<tr valign='top'>
						<td height='30' valign='middle' colspan="4" nowrap><strong>用户组列表121212
								<div align="right">
									总用户组数为：
									<pg:rowcount />
								</div>
						</strong></td>
					</tr>
					<tr height=30>
						<td width="25%" align="center" nowrap>用户组名称: <input
							type="text" name="groupName" value="<%=groupName%>">
						</td>
						<td width="25%" align="center" nowrap>用户组描述: <input
							type="text" name="groupDesc" value="<%=groupDesc%>">
						</td>
						<td width="25%" align="center" nowrap>创建人: <input type="text"
							name="groupOwnerName" value="<%=groupOwnerName%>">
						</td>
						<td width="25%" align="center" nowrap>
						<a href="javascript:void(0)" class="bt_1" id="queryButton" onclick="validateInfo()"><span>查询</span></a>
						<a href="javascript:void(0)" class="bt_2" id="resetButton" onclick="clearInfo()"><span>清空</span></a>
					</tr>
				</table>

				<hr width="100%">
				<table width="100%" border="0" cellpadding="0" cellspacing="0"
					class="stable" id="tb">

					<pg:param name="groupName" />
					<pg:param name="groupDesc" />

					<pg:header>
							<th>用户组名称121</th>
							<th>用户组描述</th>
							<th>用户组创建人</th>
					</pg:header>
					<pg:param name="groupName" />
					<pg:param name="groupDesc" />
					<pg:param name="groupOwnerName" />
					<!--检测当前页面是否有记录-->
					<pg:notify>
						<tr height='25' class="labeltable_middle_tr_01">
							<td colspan=100 align='center'>暂时没有用户组</td>
						</tr>
					</pg:notify>

					<!--list标签循环输出每条记录-->
					<pg:list>
						<% 
							//只显示这个用户创建的用户组 2008-4-8 baowen.liu
	                          //     String  userId=accesscontroler.getUserID();						
								String data = (String)dataSet.getString("owner_id");
								UserManager userManager=new UserManagerImpl();
								User user=userManager.getUserById(data);
								String userName=user.getUserName();
								String userRealName=user.getUserRealname();
							  //  if(userId.equals(data)){
							%>
						<tr id="<pg:cell colName="groupName" defaultValue="" />"
							class="labeltable_middle_td"
							onmouseover="this.className='mouseover'"
							onclick="changeRowColor(this);"
							onmouseout="this.className= 'mouseout'"
							ondblclick="dbClickChoose(this)" flag="false">
							<td height='20' align=left class="tablecells"><pg:cell
									colName="groupName" defaultValue="没有名称" /></td>
							<td height='20' align=left class="tablecells"><pg:cell
									colName="groupDesc" defaultValue="没有描述" /></td>
							<td height='20' align=left class="tablecells"><%=userName%>【<%=userRealName%>】
							</td>
						</tr>
						<%
							// }
							%>
					</pg:list>
				</table>
					<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="10,20,50,100"/></div>
			</form>
					</div>
</pg:pager>
	</div>
	<div region="center" title="用户列表" split="true"
		style="height: 800px; padding: 10px; background: #efefef;">
		已选取用户组
			<div id="chooosegrouplistContainer">
			</div>
			<table id="table3">
				<tr>
					<td align="right"><a href="javascript:void(0)" class="bt_1" id="addButton" onclick="returnUserName()"><span>确定</span></a></td>
				</tr>
			</table>
		</div>
			
	</body>
</html>
