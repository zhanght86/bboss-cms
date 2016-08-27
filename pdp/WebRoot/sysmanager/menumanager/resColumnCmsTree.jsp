
<%@ page contentType="text/html; charset=UTF-8"%>

<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.ResManager,com.frameworkset.platform.resource.ResourceManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Restype" %>
<%
	
%>
<html>
	<head>
		<title>属性容器</title>
		<script language="JavaScript" src="changeView.js" type="text/javascript"></script>
		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../css/treeview.css">
	<body class="contentbodymargin" scroll="no">
		<div id="contentborder">

				
					<table class="table" width="80%" border="0" cellpadding="0" cellspacing="1">
						
						
						<tr class="tr">
							<td class="td" width="40%" align="center">
								
							</td>
						</tr>
						<tr class="tr">
							<td class="td">

								<tree:tree tree="role_column_tree" node="role_column_tree.node" 
								imageFolder="../images/tree_images/" collapse="true" includeRootNode="true" 
								href="/sysmanager/menumanager/resMenu_tab.jsp" 
								dynamic="false"
								target="menuList">
									<tree:param name="resTypeId"/>

									<tree:treedata treetype="ColumnCmsTree" scope="request" 
									rootid="0" rootName="菜单管理" expandLevel="1" 
									showRootHref="false" 
									sortable="false"
									needObserver="false" refreshNode="false" enablecontextmenu="false"/>
									
								</tree:tree>
							</td>
						</tr>
					</table>
		

		</div>
	</body>
</html>

