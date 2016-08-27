<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.db.OrgAdministratorImpl"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree"%>
<%
	AccessControl accessControl = AccessControl.getInstance();
	accessControl.checkAccess(pageContext);
	//当前用户是否拥有超级管理员权限与部门管理员权限
	boolean isAdminOrOrgManager = false;
	//是否是管理员
	boolean isOrgManager = new OrgAdministratorImpl().isOrgManager(accessControl.getUserID());
	boolean isAdmin = accessControl.isAdmin();
	if(isAdmin || isOrgManager){
		isAdminOrOrgManager = true;
	}	
%>
<html>
	<head>
		<title>属性容器</title>
		<script language="JavaScript" src="changeView.js" type="text/javascript"></script>
	<body class="contentbodymargin" scroll="auto">
	<%if(isAdminOrOrgManager){ %>
		<div id="contentborder">

				
					<table class="table" width="80%" border="0" cellpadding="0" cellspacing="1">
						
						
						<tr class="tr">
							<td class="td" width="40%" align="center">
								
							</td>
						</tr>
						<tr class="tr">
							<td class="td">

								<tree:tree tree="role_column_tree" node="role_column_tree.node" 
								imageFolder="../images/tree_images/" collapse="true" includeRootNode="false" 
								href="viewMenuInfo.page" 
								dynamic="false"
								target="base_properties_content">
									<tree:param name="resTypeId"/>

									<tree:treedata treetype="ColumnTree" scope="request" 
									rootid="0" rootName="菜单管理" expandLevel="1" 
									showRootHref="false" 
									sortable="false"
									needObserver="false" refreshNode="false" enablecontextmenu="ture"/>
									
								</tree:tree>
							</td>
						</tr>
					</table>
		

		</div>
		<%}else{ %>
		<div align="center">没有权限！</div>
		<%} %>
	</body>
</html>

