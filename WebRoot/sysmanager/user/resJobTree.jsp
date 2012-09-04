<%@ include file="../include/global1.jsp"%>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %>

<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.resource.ResourceManager"%>
<%
AccessControl accessControl = AccessControl.getInstance();
accessControl.checkAccess(pageContext);
String resTypeId = request.getParameter("resTypeId");
%>
<html>
<head>
<title>导航器内容</title>
<link rel="stylesheet" type="text/css" href="../css/treeview.css">
<%@ include file="/include/css.jsp"%>
<link rel="stylesheet" type="text/css" href="../css/contentpage.css">
<link rel="stylesheet" type="text/css" href="../css/tab.winclassic.css">
</head>

<body class="contentbodymargin" scroll="no">
<div id="contentborder" align="left">
<form name="navform">
	<table >
        <tr><td>
         <tree:tree tree="job_tree"
    	           node="test_tree.node"
    	           imageFolder="../images/tree_images/"
    	           collapse="true"
    			   includeRootNode="true"
    			   href="/sysmanager/accessmanager/role/operList_job_ajax.jsp?resTypeId=job"
    			   target="operList"
    			   dynamic="false"
    			   >                         
                   <tree:param name="resTypeId"/>
                   <tree:param name="resName"/>

    			   <tree:treedata treetype="com.frameworkset.platform.menu.ResJobTree"
    	                   scope="request"
    	                   rootid="0"  
    	                   rootName="岗位列表"
    	                   expandLevel="1"
    	                   showRootHref="false"
    	                   needObserver="false"
    	                   
    	                   />

    	</tree:tree>
         </td></tr>
    </table>
    </form>
</div>
</body>
</html>
