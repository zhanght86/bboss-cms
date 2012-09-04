<%@ include file="../../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ page import="com.frameworkset.common.tag.contextmenu.ContextMenuTag"%>
<%@ page import="com.frameworkset.common.tag.contextmenu.ContextMenu"%>
<%@ page import="com.frameworkset.common.tag.contextmenu.ContextMenuImpl"%>
<%@ page import="com.frameworkset.common.tag.contextmenu.Menu"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%
	String roleName = ""; 
	roleName = request.getParameter("roleName");
	if(roleName == null)
	{
		roleName = "";
	}
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
			//if(document.userList.userName.value.length < 1 && document.userList.userRealname.value.length < 1){
			//	alert("用户名称和用户实名必须输入一个!!!");
			//	return;
			//}
			userList.action="roleQuery.jsp"
			userList.submit();	
		}
		
		function roleResList(roleId){
			window.showModalDialog("<%=request.getContextPath()%>/sysmanager/accessmanager/role/roleres_frame.jsp?roleId="+ roleId,window,"dialogWidth:"+(800)+"px;dialogHeight:"+(600)+"px;help:no;scroll:auto;status:no");
		}

		</SCRIPT>		
			<body>
				<form name="userList" method="post" >
					<table cellspacing="1" cellpadding="0" border="0" bordercolor="#EEEEEE" width=100% class="thin">
         				<tr valign='top'>
           					<td height='30'valign='middle' colspan="5"><img src='../../images/edit.gif' width="16" height="16" border=0 align='absmiddle'>&nbsp;<strong>角色列表</strong></td>
         				</tr>
         				<tr >
           					
           					<td colspan=2 height='30'valign='middle' align="center">角色名称：<input type="text" name="roleName" value="<%=roleName%>"></td>
           					
           					<td height='30'valign='middle' align=left>
           						<input name="search" type="button" class="input" value="查询" onClick="queryUser()">           						
           				
           					</td>
         				</tr>
						<pg:listdata dataInfo="RoleSearchList" keyName="RoleSearchList" />
						<!--分页显示开始,分页标签初始化-->
						<pg:pager maxPageItems="12" scope="request" data="RoleSearchList" isList="false">
							<tr>
								<!--设置分页表头-->
								<td height='30' class="headercolor" width="30%">角色名称</td>
								<td height='30' class="headercolor" width="40%">角色描述</td>
								<td height='30' class="headercolor" width="30%">角色类型</td>							
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
							
							<!--  ----2007.11.27     gao.tang   ---------------->
							<%
								ContextMenu contextmenu = new ContextMenuImpl();
					 		%>
					 		
							<!--list标签循环输出每条记录-->
							<pg:list>
							<%
								String roleId = dataSet.getString("roleId");
								
								Menu menu = new Menu();
								menu.setIdentity("oprole_"+roleId);
								
								//角色资源权限查询
								Menu.ContextMenuItem menuitem1 = new Menu.ContextMenuItem();
								menuitem1.setName("角色资源权限查询");
								menuitem1.setLink("javascript:roleResList("+roleId+")");
								menuitem1.setIcon(request.getContextPath() +"/sysmanager/images/rightMemu/doc_fbyl.gif");
								menu.addContextMenuItem(menuitem1);
								
								contextmenu.addContextMenu(menu);
								
							%>
							
								<tr >
									<td id="oprole_<%=roleId%>" class="tablecells" width="30%" >
										<pg:cell colName="roleName" defaultValue="" />
									</td>
									<td class="tablecells"  width="40%" >
										<pg:cell colName="roleDesc" defaultValue="" />
									</td>
									<td class="tablecells"  width="30%" >
										<pg:cell colName="roleType" defaultValue="" />
									</td>
								</tr>
							</pg:list>
							<%
							request.setAttribute("oprole",contextmenu); 
							%>
							<pg:contextmenu enablecontextmenu="true" context="oprole" scope="request"/>
							<tr height="18px" class="labeltable_middle_tr_01">
								<td colspan=4 align='center'>
									<pg:index />
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
