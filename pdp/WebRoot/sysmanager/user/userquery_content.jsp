<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.security.AccessControl,com.frameworkset.common.poolman.DBUtil" %>

<%
 			AccessControl accesscontroler = AccessControl.getInstance();
	        accesscontroler.checkAccess(request, response);
	        String userId = accesscontroler.getUserID();
	        
	        DBUtil db = new DBUtil();
		    db.executeSelect("select * from td_sm_user");
		    String usercount = String.valueOf(db.size());
%>
<html>
	<head>
		<title>属性容器</title>
		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="../css/tab.winclassic.css">
		
<script language="JavaScript" src="common.js" type="text/javascript"></script>
<script language="JavaScript" src="../include/pager.js" type="text/javascript"></script>
<SCRIPT language="javascript">	
var jsAccessControl = new JSAccessControl("#DAE0E9","#F6F8FB","#F6F8FB");

function getUserInfo(e,userId,qstring)
{
	//var qstring = document.forms[0].queryString.value;	
	if (jsAccessControl.setBackColor(e))
	{		
		getPropertiesToolbar().location.href="userquery_toolbar.jsp?userId="+userId+"&qstring="+qstring;
	}
}

function getUserInfo2(e,userId,qstring)
{
	var curruserId = "<%=userId%>";
	if (jsAccessControl.setBackColor(e))
	{		
	    if(userId==curruserId)document.location.href="userInfo2.jsp?userId="+userId+"&qstring="+qstring;
		else document.location.href="userInfo4.jsp?userId="+userId+"&qstring="+qstring;
	}
}
	
function actionOnmouseover(e){	
	e.style.backgroundColor = "#8080FF";
}

function actionOnmouseup(e){
	e.style.backgroundColor = "#BEC0C2";
}


function queryUser()
{	
	if(document.userList.userName.value.length < 1 && document.userList.userRealname.value.length < 1){
		alert("用户名称和用户实名必须输入一个!!!");
		return;
	}
	userList.action="../user/userquery_content.jsp"
	userList.submit();	
}
function advQueryUser()
{	
	document.location.href="../user/advQuery.jsp"	
}

</SCRIPT>
	
			<body class="contentbodymargin" scroll="no">
			<div id="contentborder" >
				<form name="userList" method="post" >
					<table cellspacing="1" cellpadding="0" border="0" width=98% class="thin">
         				<tr valign='top'>
           					<td height='30'valign='middle' colspan="3"><img src='../images/edit.gif' width="16" height="16" border=0 align='absmiddle'>&nbsp;<strong>用户列表&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
           					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
           					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
           					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
           					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
           					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
           					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
           					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
           					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
           					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  
           					&nbsp;总用户数为：<%=usercount%>
           					</strong></td>
         				</tr>
         				<tr >           					
           					<td height='30'valign='middle' align="center">用户名称：<input type="text" name="userName" ></td>
           					<td height='30'valign='middle' align="center">用户实名：<input type="text" name="userRealname" ></td>
           					<td height='30'valign='middle' align="center">
           						<input name="search" type="button" class="input" value="查询" onClick="queryUser()">           						
           						<input name="advsearch" type="button" class="input" value="高级查询" onClick="advQueryUser()">   
           					</td>
         				</tr>
						<pg:listdata dataInfo="UserSearchList" keyName="UserSearchList" />
						<!--分页显示开始,分页标签初始化-->
						<pg:pager maxPageItems="15" scope="request" data="UserSearchList" isList="false">
							<tr>
								<!--设置分页表头-->								
								<td height='30' class="headercolor"  >用户登陆名称	</td>
								<td height='30' class="headercolor"  >用户实名</td>								
								<td height='30' class="headercolor"  >手机号码</td>
							</tr>
							<pg:param name="orgId"/>
							<pg:param name="userName"/>
							<pg:param name="userRealname"/>
							<pg:param name="advQuery" />							
							

							<!--检测当前页面是否有记录-->
							<pg:notify>
								<tr height="18px" class="labeltable_middle_tr_01">
									<td colspan=100 align='center' height='20'>
										暂时没有用户
									</td>
								</tr>
							</pg:notify>

							<!--list标签循环输出每条记录-->
							<pg:list>
								
								<tr onclick=getUserInfo(this,'<pg:cell colName="userId" defaultValue=""/>','<pg:querystring/>') onDBLclick=getUserInfo2(this,'<pg:cell colName="userId" defaultValue=""/>','<pg:querystring encode="true"/>') >
									
									<td height='20' class="tablecells" nowrap="nowrap">
										<pg:cell colName="userName" defaultValue="" />
									</td>
									<td height='20' class="tablecells" nowrap="nowrap">
										<pg:cell colName="userRealname" defaultValue="" />
									</td>									
									<td height='20' class="tablecells" nowrap="nowrap" >
										<pg:notnull colName="userMobiletel1"><pg:cell colName="userMobiletel1" defaultValue=" "/></pg:notnull>
										<pg:null colName="userMobiletel1">没有号码</pg:null>
										<pg:equal colName="userMobiletel1" value="">没有号码</pg:equal>
									</td>
								</tr>
							</pg:list>
							<tr height="18px" class="labeltable_middle_tr_01">
								<td colspan=3 align='center'>
									<pg:index />
								</td></tr>
							<input name="queryString" value="<pg:querystring/>" type="hidden">
						</pg:pager>

				  </table>
				</form>
				</div><%@ include file="../sysMsg.jsp"%>
			</body>
			<center>
</html>

