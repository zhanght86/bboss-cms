<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="com.frameworkset.platform.framework.Framework"%>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %>  
<%
    String path = "menu://sysmenu$root";
    //com.frameworkset.platform.framework.Framework.SUPER_MENU
    String contextPath = request.getContextPath();
    com.frameworkset.platform.security.AccessControl accesscontroler_panes = com.frameworkset.platform.security.AccessControl.getInstance();
  	accesscontroler_panes.checkAccess(request,response);
    String currentSystem = accesscontroler_panes.getCurrentSystem();
    String name = Framework.getSubFrameworkName(currentSystem,request);
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>

<link rel="stylesheet" type="text/css" href="sysmanager/css/treeview.css">
<link rel="stylesheet" type="text/css" href="sysmanager/css/windows.css">
</head>
<body class="contentbodymargin" scrolling="no">
<div id="contentborder">
    <table >
        <tr><td>
         <tree:tree tree="basecolumn_tree"
    	           node="basecolumn_tree.node"
    	           imageFolder="sysmanager/images/tree_images/"
    	           collapse="true"
    			   includeRootNode="true"
    			   nowrap="true"
    			   target="perspective_content"
    			   mode="static"
    			   >


    			   <tree:treedata treetype="BaseColumnTree"
    	                   scope="session"
    	                   rootid="menu://sysmenu$root"
    	                   rootName="<%=name %>"
    	                   expandLevel="2"
    	                   showRootHref="false"
    	                   needObserver="false"
                           refreshNode="false"
    	                   />

    	</tree:tree>
         </td></tr>
    </table>
</div>
</body>
</html>

