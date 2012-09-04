<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.ResManager,com.frameworkset.platform.resource.ResourceManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Restype" %>
<%
	String resTypeId = request.getParameter("resTypeId");
	
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
							<td class="td">

								<tree:tree tree="res_groupTree_tree" 
								node="res_groupTree_tree.node" 
								imageFolder="../images/tree_images/" 
								collapse="true" includeRootNode="true" 
								href="/sysmanager/accessmanager/role/operList_group_ajax.jsp?resTypeId=group" 
								target="operList">
								<tree:param name="resTypeId"/>

								<tree:treedata treetype="GroupTree" 
								scope="request" 
								rootid="0" 
								rootName="用户组" 
								expandLevel="1" 
								showRootHref="false" 
								needObserver="false" 
								refreshNode="false"
								/>
								</tree:tree>
							</td>
						</tr>
					</table>
				

		</div>
	</body>
</html>

