
<%@page import="com.frameworkset.platform.sysmgrcore.manager.OrgAdministrator"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.db.OrgAdministratorImpl"%>
<%@page import="java.util.List"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.OrgManager"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.UserManager"%><%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>
<%@ include file="../../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.security.AccessControl,com.frameworkset.common.poolman.DBUtil" %>
<%
 			AccessControl accesscontroler = AccessControl.getInstance();
	        accesscontroler.checkManagerAccess(request,response);
	        String curUserId = accesscontroler.getUserID();	
	        String curOrgId = accesscontroler.getChargeOrgId();
	        String roletype = ""; 
			roletype = request.getParameter("roletype");
			if(roletype == null)
			{
				roletype = "";
			}        
			OrgAdministrator orgAdministrator = new OrgAdministratorImpl();
			List list = orgAdministrator.getManagerOrgsOfUserByID(curUserId);
			boolean isAdminOrOrgManager = false;//当前登陆用户是否具有部门管理员角色或者超级管理员角色
			if(list.size() > 0 || accesscontroler.isAdmin()){//判断是否是部门管理员和拥有超级管理员角色
		      	  isAdminOrOrgManager = true;
			}  
			
			OrgManager orgManager = SecurityDatabase.getOrgManager();
			UserManager userManager = SecurityDatabase.getUserManager(); 
			
			boolean isAdmin = accesscontroler.isAdmin();
%>

<html >
<head>				
	<tab:tabConfig/>
		<title>属性容器</title>
		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../../css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="../../css/tab.winclassic.css">
		
	<script language="JavaScript" src="common.js" type="text/javascript"></script>
	<script language="JavaScript" src="../../include/pager.js" type="text/javascript"></script>
	<script language="JavaScript" src="func.js" type="text/javascript"></script>
	<SCRIPT language="javascript">
	function queryRoleType()
	{
		searchForm.action='roletype_list.jsp';
		searchForm.submit();
	}
	function newRoleType()
	{
		var url = "roletype_add.jsp";
		window.showModalDialog(url,window,"dialogWidth:"+(500)+"px;dialogHeight:"+(440)+"px;help:no;scroll:auto;status:no");
	}
	
	function delRoleType()
	{
		var all = document.getElementsByName("ID");
		var docidStr="";
		for(var i=0;i<all.length;i++)
		{
			if(all[i].checked == true)
			{
				if(all[i].value==1)
				{
					alert("通用角色类型不能被删除！");
					return false;			
				}
				docidStr = docidStr + all[i].value + ";";
			}
		}
		if(haveSelect('ID'))
		{		
			if(confirm('您确定要删除所选的角色类型?删除前请对该类型下的角色进行处理！'))
			{
			RoleTypeList.action="roletype_delete.jsp?roletype_id=" + docidStr;
		    RoleTypeList.target="";
			RoleTypeList.submit();			
			}
			else
			{
			  return false;
			}
		
		}else{
	    	alert("请选择要求删除的记录！");
	    	return false;
	   	}	
    }	
	</SCRIPT>	
</head>
<body>
	<%if(isAdminOrOrgManager){ %>
	<form name='searchForm' method='post'>
		<table cellspacing="1" cellpadding="0" border="0" width="100%" class="thin">
        	<tr valign='top'>
           		<td height='30'valign='middle' colspan="6"><img src='../../images/edit.gif' width="16" height="16" border=0 align='middle'>&nbsp;<strong>角色类型列表</strong></td>
         	</tr>
         				<tr >           					
           					<td height="30" colspan="5" align="left">
	           					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;角色类型：<input type="text" name="roletype" value="<%=roletype%>">
           					</td>         					
           					<td height='30' valign='middle' align="center">
           						<input name="search" type="button" class="input" value="查询" onClick='queryRoleType()'>           						          						
           					</td>
           					</tr>
    </form>     				
         <form name="RoleTypeList" method="post" >
						<pg:listdata dataInfo="com.frameworkset.platform.sysmgrcore.web.tag.RoleTypeList" keyName="RoleTypeList" />
						<!--分页显示开始,分页标签初始化-->
						<pg:pager maxPageItems="15" id="RoleTypeList" scope="request" data="RoleTypeList" isList="false">
							<tr>
								<!--设置分页表头-->
								<td class="headercolor" width="3%">
								<input type="checkbox" name="checkBoxAll" onclick="checkAll('checkBoxAll','ID')">							
								</td>
								<td height='30' width="25%" class="headercolor"  >角色类型名称</td>
								<td height='30' width="*" class="headercolor" colspan="2" >角色类型描述</td>	
								<td height='30' width="*" class="headercolor" >类型所属机构</td>		
								<td height='30' width="*" class="headercolor" >类型创建人</td>						
							</tr>                       
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
							<% 
								String orgId = dataSet.getString("creatorOrgId");
								String userId = dataSet.getString("creatorUserId");
							%>
								<tr onmouseover="this.className='mouseover'" onmouseout="this.className= 'mouseout'">
									<td class="tablecells" nowrap="true">
										<P align="left">
											<input type="checkbox" name="ID" onclick="checkOne('checkBoxAll','ID')" value='<pg:cell colName="roleTypeID" defaultValue=""/>' 
											<%
												if(!isAdmin && !orgId.equals(curOrgId)){
													out.print(" disabled ");
												} 
											%>
											>						
										</P>
									</td>
									<td height='20' class="tablecells" nowrap="nowrap">
										<pg:cell colName="typeName" defaultValue="" />
									</td>		
									<td height='20' class="tablecells" nowrap="nowrap" colspan="2" >
										<pg:cell colName="typeDesc" defaultValue="" />
									</td>		
									<td height='20' class="tablecells" nowrap="nowrap" >
										<% 
											if(orgManager.getOrgById(orgId) != null){
												out.print(orgId + " " + orgManager.getOrgById(orgId).getRemark5());
											}else{
												out.print("未知");
											}
										%>
									</td>		
									<td height='20' class="tablecells" nowrap="nowrap" >
										<% 
											if(userManager.getUserById(userId) != null){
												out.print(userManager.getUserById(userId).getUserName() + "(" + userManager.getUserById(userId).getUserRealname() + ")");
											}else{
												out.print("未知");
											}
										%>
									</td>							
								
										
								</tr>
							</pg:list>
							<tr height="18px" class="labeltable_middle_tr_01">
								<td colspan="4" align='center'>
									<pg:index />
								</td>
								<td colspan="2" align='center'>													
								<input name="newroletype" type="button" class="input" value="新增" onClick="newRoleType()">
								<input type="submit" value="删除" class="input" onclick="javascript:delRoleType(); return false;">			
								</td>										
							</tr>
						</pg:pager>

				  </table>
				</form>
				<%}else{ %>
				<div align="center">没有权限！请与系统管理员联系</div>
				<%} %>
</body>
</html>

