<%@ include file="../../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%
	
	
%>
<html>
	<head>
		<title>属性容器</title>
		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../../css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="../../css/tab.winclassic.css">
		
<script language="JavaScript" src="common.js" type="text/javascript"></script>
<script language="JavaScript" src="../../include/pager.js" type="text/javascript"></script>
<SCRIPT language="javascript">	


function queryUser()
{	
	userList.action="roleQuery.jsp"
	userList.submit();	
}


</SCRIPT>		
			<body>
				<form name="userList" method="post" >
					<table cellspacing="1" cellpadding="0" border="0" bordercolor="#EEEEEE" width=98% class="thin">
         				<tr valign='top'>
           					<td height='30'valign='middle' colspan="5"><img src='../../images/edit.gif' width="16" height="16" border=0 align='absmiddle'>&nbsp;<strong>角色列表</strong></td>
         				</tr>
         				<tr >
           					
           					<td height='30'valign='middle' align="left">角色名称：<input type="text" name="roleName" ></td>
           					
           					<td height='30'valign='middle' align="center">
           						<input name="search" type="button" class="input" value="查询" onClick="queryUser()">           						
           				
           					</td>
         				</tr>
						<pg:listdata dataInfo="RoleSearchList" keyName="RoleSearchList" />
						<!--分页显示开始,分页标签初始化-->
						<pg:pager maxPageItems="12" scope="request" data="RoleSearchList" isList="false">
							<tr>
								<!--设置分页表头-->
								<td height='30' class="headercolor" onclick="sortBy('userName')" >角色名称</td>
								<td height='30' class="headercolor" onclick="sortBy('userName')" >角色描述</td>
								
								
							</tr>
							<pg:param name="roleId" />
							<pg:param name="roleName" />
							

							<!--检测当前页面是否有记录-->
							<pg:notify>
								<tr height="18px" class="labeltable_middle_tr_01">
									<td colspan=100 align='center'>
										暂时没有角色
									</td>
								</tr>
							</pg:notify>

							<!--list标签循环输出每条记录-->
							<pg:list>
								
								<tr >
									<td height='25' class="tablecells" nowrap="nowrap">
										<pg:cell colName="roleName" defaultValue="" />
									</td>
									<td height='25' class="tablecells" nowrap="nowrap">
										<pg:cell colName="roleDesc" defaultValue="" />
									</td>									
									
								</tr>
							</pg:list>
							<tr height="18px" class="labeltable_middle_tr_01">
								<td colspan=4 align='center'>
									
									<pg:index />
									<input name="search" type="button" class="input" value="确定" onClick="parent.window.close();">
									<input name="search" type="button" class="input" value="返回" onClick="parent.window.close();">
								</td>
								
							</tr>
							<input name="queryString" value="<pg:querystring/>" type="hidden">
						</pg:pager>

				  </table>
				</form>
				<%@ include file="../../sysMsg.jsp"%>
		</body>
		<center>
</html>

