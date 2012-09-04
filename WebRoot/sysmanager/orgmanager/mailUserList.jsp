<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%	
	String desc = (String)request.getParameter("pager.desc");
	String orgId = request.getParameter("orgId");
	if (orgId == null) {
		orgId = (String) request.getAttribute("orgId");
	}
	if (orgId == null) {
		orgId = "";
	}
%>
<html>
	<head>
		<title>属性容器</title>
		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="sysmanager/css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="sysmanager/css/tab.winclassic.css">
		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="../css/tab.winclassic.css">
		<script language="JavaScript" src="../include/pager.js" type="text/javascript"></script>
		<script language="JavaScript" src="sysmanager/orgmanager/common.js" type="text/javascript"></script>
		<script language="JavaScript" src="../orgmanager/common.js" type="text/javascript"></script>
		<SCRIPT language="javascript">	
var jsAccessControl = new JSAccessControl("#DAE0E9","#F6F8FB","#F6F8FB");

function getUserInfo(e,userId)
{
	if (jsAccessControl.setBackColor(e))
	{
		//parent.userInfo.userInfo.action="../user/userManager.do?method=getUser"+"&userId="+userId;
		//parent.userInfo.userInfo.submit();	
		
	}
}

function selmail(){
	document.forms[0].action="createMail.do?method=createMailByUser";
	document.forms[0].submit();
}

function markUser(){
	document.forms[0].action="createMail.do?method=markUser";
	document.forms[0].submit();
}

function markAll(){
	document.forms[0].action="createMail.do?method=markAllUser";
	document.forms[0].submit();
}

function allmail(){
	document.forms[0].action="createMail.do?method=createMailByOrg";
	document.forms[0].submit();
}

</SCRIPT>		
			<body>
				<form name="userList" method="post" >
					<table cellspacing="1" cellpadding="0" border="0" bordercolor="#EEEEEE" width="100%" class="thin" >
         				<tr valign='top'>
           					<td height='30'valign='middle' colspan="5"><strong>用户列表</strong></td>
         				</tr>
         				<tr >
         					<td height='30' colspan="3" valign='middle' align="center">用户类型：<dict:select type="userType" name="userType" /></td>
           					<td height='30'valign='middle' align="center">  <input name="b1" value="标识选中用户" type="button"  onClick="markUser()" class="input"></td>
           					<td height='30'valign='middle' align="center">  <input name="b2" value="标识所有用户" type="button"  onClick="markAll()" class="input"></td>
         				</tr>
         				<tr >
           					<td colspan="3" height='30'valign='middle' align="center">邮箱后缀名：<dict:select type="MailPostfix" name="mailPostfix" /></td>
           					<td height='30'valign='middle' align="center">  <input name="b1" value="生成选中用户" type="button"  onClick="selmail()" class="input"></td>
           					<td height='30'valign='middle' align="center">  <input name="b2" value="生成所有人" type="button"  onClick="allmail()" class="input"></td>
         				</tr>
						<pg:listdata dataInfo="MailUserList" keyName="UserList" />
						<!--分页显示开始,分页标签初始化-->
						<pg:pager maxPageItems="15" scope="request" data="UserList" isList="false">
							<tr>
								<!--设置分页表头-->
								<td class="headercolor" width="10">
									<input type="checkBox" name="checkBoxAll" onClick="checkAll('checkBoxAll','userIds')" width="10">
								</td>
								<td class="headercolor"  >用户名称	</td>
								<td class="headercolor"  >用户实名</td>
								
								<td class="headercolor"  >用户类型</td>
								<td class="headercolor"  >邮箱地址</td>
								
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
								<tr onmouseover="this.className='mouseover'" onmouseout="this.className= 'mouseout'" >
									<td class="tablecells" nowrap="nowrap">
										<input type="checkBox" name="userIds" onClick="checkOne('checkBoxAll','userIds')" value="<pg:cell colName="userId" defaultValue=""/>" width="10">
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
									<dict:itemName type="userType" itemValue="<%=userType%>" />
									
									</td>
									<td class="tablecells" nowrap="nowrap">
										<pg:cell colName="userEmail" defaultValue="暂时没有邮箱" />
									</td>	
								</tr>
							</pg:list>
							
							<tr height="18px" class="labeltable_middle_tr_01">
								<td colspan=5 align='center'>
									<pg:index />
									<!-- 
									<input type="submit" value="删除" class="input" onclick="javascript:dealRecord(1); return false;">
									-->
								</td>							
							</tr>
							
							<input name="queryString" value="<pg:querystring/>" type="hidden">
							<input name="orgId" value="<%=orgId%>" type="hidden">
						</pg:pager>

					</table>
				</form>
<%@ include file="../sysMsg.jsp"%>
			</body>
			<center>
</html>

