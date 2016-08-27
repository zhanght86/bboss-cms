<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree"%>
<%

			String href = "/sysmanager/schedularmanage/frame_bridge.jsp";
%>
<html>
	<head>
		<title>属性容器</title>
		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../css/treeview.css">
		
	<body class="contentbodymargin" scroll="no">
		<div id="contentborder">

			<form name="OrgJobForm" action="" method="post">
				<table class="table" width="80%" border="0" cellpadding="0" cellspacing="1">
					<tr class="tr">
						<td class="td">

							<tree:tree tree="role_org_tree" node="role_org_tree.node" imageFolder="../images/tree_images/" collapse="true" includeRootNode="false" href="<%=href%>" target="frame_bridge">


								<tree:treedata treetype="GroupOrg_Tree" scope="request" rootid="0" rootName="机构树" expandLevel="1" showRootHref="false" needObserver="false" />
							</tree:tree>
						</td>
					</tr>
				</table>
			</form>

		</div>
	</body>
</html>

