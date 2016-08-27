<%
/**
  * 选择自定义表单
  * 
  */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.*"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*"%>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %>
<%@ include file="/sysmanager/include/global1.jsp"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>自定义表单的选择</title>
<link rel="stylesheet" type="text/css" href="<%=rootpath%>/sysmanager/css/treeview.css">
<link rel="stylesheet" type="text/css" href="<%=rootpath%>/sysmanager/css/windows.css">
</head>
<body class="contentbodymargin" scrolling="no">
<div id="contentborder">
    <table >
        <tr><td>
        <tree:tree tree="filesystem_tree"
						node="filesystem_tree.node"
						imageFolder="/cms/images/tree_images/"
						collapse="true"
						includeRootNode="true"
						href="form_files_list.jsp"
						target="formfilelist" 
						dynamic="false">
		<tree:param name="siteid"/>
		<tree:treedata treetype="FormFilesTree"
			scope="request"
			rootid="/"  
			rootName="文件管理"
			expandLevel="1"
			showRootHref="true"
			needObserver="false"
			enablecontextmenu="true"
			/>      
		</tree:tree>
         </td></tr>
    </table>
</div>
</body>
</html>