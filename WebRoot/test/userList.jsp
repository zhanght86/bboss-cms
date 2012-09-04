<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page import="com.frameworkset.platform.cms.customform.*"%>
<%@ page import="com.frameworkset.platform.config.model.AuthorTableInfo,
	com.frameworkset.platform.security.AccessControl,
	com.frameworkset.platform.security.authorization.AuthRole,
	com.frameworkset.platform.sysmgrcore.authorization.AppAuthorizationTable,
	com.frameworkset.common.poolman.DBUtil,
	com.frameworkset.platform.config.ConfigManager"%>
<%@ page import="com.frameworkset.platform.resource.ResourceManager"%>

<%			
			AccessControl accesscontroler = AccessControl.getInstance();
			accesscontroler.checkAccess(request, response);
			String uId = accesscontroler.getUserID();
			ResourceManager resManager = new ResourceManager();
			String resId = resManager
					.getGlobalResourceid(AccessControl.ORGUNIT_RESOURCE);
			String curOrgId = request.getParameter("orgId");
			if (curOrgId == null)
				curOrgId = (String) request.getAttribute("orgId");
			String reFlush = "false";
			if (request.getAttribute("reFlush") != null) {
				reFlush = "true";
			}

			Integer currUserId = (Integer) session.getAttribute("currUserId");
			if (currUserId == null) {
				currUserId = Integer.valueOf("-1");
			}
			//String curOrgId = (String)session.getAttribute("orgId");
			String desc = (String) request.getParameter("pager.desc");
			String intervalType = (String) request.getParameter("intervalType");
			String taxmanager = (String) request.getParameter("taxmanager");
			String ischecked = "";
			if ((String) request.getAttribute("ischecked") == null) {
				ischecked = "";
			} else {
				ischecked = (String) request.getAttribute("ischecked");
			}

			String QueryuserName = null;
			String QueryuserRealname = null;
			String usernames = null;
			String nodeinfoId = null;
			if (request.getParameter("userName") == null) {
				QueryuserName = "";
			} else {
				QueryuserName = request.getParameter("userName");
			}
			if (request.getParameter("userRealname") == null) {
				QueryuserRealname = "";
			} else {
				QueryuserRealname = request.getParameter("userRealname");
			}
			if(request.getParameter("usernames")!=null){
				usernames = request.getParameter("usernames");
			}
			if(request.getParameter("nodeinfoId")!=null){
				nodeinfoId = request.getParameter("nodeinfoId");
			}
%>
<html>
<head>
<title>属性容器</title>
<%@ include file="/common/jsp/css.jsp"%>
<script language="JavaScript" src="common.js" type="text/javascript"></script>
<SCRIPT language="javascript">
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
function dbClickChoose(obj){
	var usernames = window.parent.document.getElementById("usernames").value;
	$("#usernames").val(usernames);
	var usernamesArr = new Array();
	usernamesArr = document.getElementById("usernames").value.split(",");
	for(i=0;i<usernamesArr.length;i++){
		if(obj.id==usernamesArr[i]){
			return;
		}
	}
	if($("#usernames").val()!='null'&&$("#usernames").val()!=''){
		$("#usernames").val(usernames+","+obj.id);
		window.parent.document.getElementById("usernames").value = $("#usernames").val();
	}else{
		$("#usernames").val(obj.id);
		window.parent.document.getElementById("usernames").value = $("#usernames").val();
	}
	$("#choooseuserlistContainer").load("queryCandidateUserByNames.page #chooseuserlistContent", {usernames:$("#usernames").val()});
}
function returnUserName(){
	window.parent.document.submitForm.action="../test/showActivitiNodeEdit.page";
	window.parent.document.submitForm.submit();
}
function queryUser()
{
	var intervalType = document.all("intervalType").value;
	userList.action="userList.jsp?orgId=<%=curOrgId%>&intervalType="+intervalType;
	userList.submit();	
}

function deleteChooseUser(obj){
	var usernames = "";
	var usernamesArr = new Array();
	usernamesArr = document.getElementById("usernames").value.split(",");
	for(i=0;i<usernamesArr.length;i++){
		if(obj==usernamesArr[i]){
		}else{
			if(usernames==""){
				usernames = usernamesArr[i];
			}else{
				usernames += ","+usernamesArr[i];
			}
			
		}
	}
	$("#usernames").val(usernames);
	window.parent.document.getElementById("usernames").value = $("#usernames").val();
	$("#choooseuserlistContainer").load("queryCandidateUserByNames.page #chooseuserlistContent", {usernames:$("#usernames").val()});
}

$(document).ready(function() {
	var names = $(window.parent.document).find("#usernames").val();
	 $("#choooseuserlistContainer").load("queryCandidateUserByNames.page #chooseuserlistContent", {usernames:names}); 
   });
</SCRIPT>
<body>
<div id="contentborder" align="center">
<form name="userList" target="userList" method="post"><input
	type="hidden" name="orgId" value="<%=curOrgId%>" />
	<input type="hidden" value="<%=usernames %>" name="usernames" id="usernames"/>
	<input type="hidden" value="<%=nodeinfoId %>" name="nodeId" id="nodeId"/>
		<table width="100%" border="0" cellpadding="0" cellspacing="0"
			class="stable" id="tb"><tr>
			<td height='30' valign='middle' align="left">用户名称：</td>
			<td><input type="text" name="userName" value="" size="15"></td>
			<td>用户实名：</td><td><input type="text" name="userRealname" value="" size="15"></td>
			<td><select name="intervalType" id="intervalType" class="select" onchange="">
				<option value="0">不递归机构查询</option>
				<option value="1">递归机构查询</option>
			</select>
			</td>
			<td>
			<a href="javascript:void(0)" class="bt_1" id="addButton" onclick="queryUser()"><span>查询</span></a>
			</td>
		</tr></table><br>
	<pg:listdata dataInfo="com.frameworkset.platform.sysmgrcore.web.tag.UserListSn"
		keyName="UserListSn" />
	<!--分页显示开始,分页标签初始化-->
	<pg:pager maxPageItems="15" scope="request" data="UserListSn"
		isList="false">
		<table width="100%" border="0" cellpadding="0" cellspacing="0"
			class="stable" id="tb">
		<pg:header>
				<th>登录名</th>
				<th>用户实名</th>
				<th>用户类别</th>
				<th>隶属机构</th>
				<th>邮箱地址</th>
			</pg:header>
		<pg:param name="orgId" />
		<pg:param name="userName" />
		<pg:param name="userRealname" />
		<pg:param name="intervalType" />
		<pg:param name="istaxmanager" />
		<!--检测当前页面是否有记录-->
		<pg:notify>
			<tr>
					<td colspan=15 style="text-align: center;"><img
						src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>" /></td>
				</tr>
		</pg:notify>
		<!--list标签循环输出每条记录-->
		<pg:list>
			<tr id="<pg:cell colName="userName" defaultValue="" />"
				onclick="changeRowColor(this);" ondblclick="dbClickChoose(this)" flag="false"
				onmouseover="this.className='mouseover'"
				onmouseout="this.className= 'mouseout'">
				<td class="tablecells" nowrap="nowrap"><pg:cell
					colName="userName" defaultValue="" /></td>
				<td class="tablecells" nowrap="nowrap"><pg:cell
					colName="userRealname" defaultValue="" /></td>
				<td class="tablecells" nowrap="nowrap"><dict:itemname
					type="userType" expression="{userType}" /></td>
				<td class="tablecells"><pg:cell colName="org_Name" defaultValue="" />
				</td>
				<td class="tablecells" nowrap="nowrap"><pg:notnull
					colName="userEmail">
					<pg:cell colName="userEmail" defaultValue=" " />
				</pg:notnull> <pg:null colName="userEmail">没有邮箱</pg:null> <pg:equal
					colName="userEmail" value="">没有邮箱</pg:equal></td>
			</tr>
		</pg:list>
	</table>
	<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="10,20,50,100"/></div>
	</pg:pager>
	<br />
	<br />
已选取用户
<div id="choooseuserlistContainer">
</div>
<table id="table3">
	<tr>
		<td align="right"><a href="javascript:void(0)" class="bt_1" id="addButton" onclick="returnUserName()"><span>确定</span></a></td>
	</tr>
</table>
</form>
</div>
</body>
</html>

