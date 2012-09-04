<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.db.OrgAdministratorImpl"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree"%>
<%
AccessControl control = AccessControl.getInstance();
control.checkAccess(request,response);
//当前用户是否拥有超级管理员权限与部门管理员权限
boolean isAdminOrOrgManager = false;
//是否是管理员
boolean isOrgManager = new OrgAdministratorImpl().isOrgManager(control.getUserID());
boolean isAdmin = control.isAdmin();
if(isAdmin || isOrgManager){
	isAdminOrOrgManager = true;
}
String path = request.getContextPath();
String imageFolder = path + "/sysmanager/images/tree_images/";
//System.out.println(pageContext.getServletContext().getRealPath("portal/template/web_iframe_plugin.zip"));
//String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    
    <title>My JSP 'columntree.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

    <link href="<%=path%>/purviewmanager/css/windows.css" rel="stylesheet"
			type="text/css">
    <script type="text/javascript" language="Javascript">
		function subColumnModule(){
			var count = 0;
			if(document.all.columnModuleValue){
				var obj = document.getElementsByName("columnModuleValue");
				var size = obj.length;
				for(var i = 0; i < size; i++){
					if(obj[i].checked){
						count++;	
					}
					if(count > 0)
						break;
				}
				if(count > 0){
					document.form1.action="templateIssue_configure.jsp";
					document.form1.target="base_properties_content";
					document.form1.submit();
				}else{
					alert("请至少选择一个菜单模块！");
				}
			}
			else
			{
				alert("请选择菜单模块！");
			}
		}
    </script>
  </head>
  
  <body class="contentbodymargin" scroll="no">
    <form action="" method="post" target="hiddenFrame" name="form1">
      <%if(isAdminOrOrgManager){ %>
		<div id="contentborder">
				
			<table class="table" width="100%" border="0" cellpadding="0" cellspacing="1">
				
				<tr class="tr">
					<td class="td" width="40%" align="left">
						<input type="button" value="确认" onclick="subColumnModule()" class="input">
					</td>
				</tr>
				<tr class="tr">
					<td class="td">

						<tree:tree tree="portal_column_tree" node="portal_column_tree.node" 
							imageFolder="<%=imageFolder %>" 
							collapse="true" 
							includeRootNode="false" 
							href="" 
							mode="static-dynamic"
							target="">
							
							<tree:checkbox name="columnModuleValue"/>

							<tree:treedata treetype="com.frameworkset.platform.portal.menu.ColumnTreePortal" 
								scope="request" 
								rootid="0" 
								rootName="菜单管理" 
								expandLevel="1" 
								showRootHref="false" 
								sortable="false"
								needObserver="false" 
								refreshNode="false" 
								enablecontextmenu="false"/>
							
						</tree:tree>
					</td>
				</tr>
			</table>
		</div>
		<%}else{ %>
		<div align="center">没有权限！</div>
		<%} %>
    </form>
    
    <iframe name="hiddenFrame" width="0" height="0"></iframe>
  </body>
</html>

