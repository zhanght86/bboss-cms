<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkAccess(request, response);
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

								<tree:tree tree="res_role_tree" 
								node="res_role_tree.node" 
								imageFolder="../images/tree_images/" 
								collapse="true" 
								includeRootNode="true" 
								href="/sysmanager/accessmanager/role/operList_role_ajax.jsp?resTypeId=role" 
								target="operList"
								mode="static-dynamic">
									<tree:param name="resTypeId"/>

									<tree:treedata treetype="Role_Tree" scope="request" rootid="0" rootName="角色" expandLevel="1" showRootHref="false" 
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

