<%@ include file="/sysmanager/include/global1.jsp"%>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);
%>

<html>
<head>
<title>资源管理</title>
<script language="javascript">
function action11(){
	getPropertiesContent().location.href="<%=rootpath%>/sysmanager/orgmanager/organsearch.jsp";
}
</script>
<link rel="stylesheet" type="text/css" href="<%=rootpath%>/sysmanager/css/treeview.css">
<link rel="stylesheet" type="text/css" href="<%=rootpath%>/sysmanager/css/windows.css">
</head>
<body scroll="no">
<table >
	<tr><td>
	<tree:tree tree="res_tree" 
			node="res_tree.node" imageFolder="/sysmanager/images/tree_images/" 
			collapse="true" includeRootNode="true" 
			href="orgres_list.jsp" target="operList">
							

			<tree:treedata treetype="ResourceTypeCMSTree" scope="session" rootid="0" rootName="资源分类" expandLevel="1" 
			showRootHref="false" 
			needObserver="false" 
			
			/>
		</tree:tree>
	 </td></tr>
</table>
</body>
</html>
