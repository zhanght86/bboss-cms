<%@ include file="../include/global1.jsp"%>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>

<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<%
	AccessControl accessControl = AccessControl.getInstance();
	accessControl.checkManagerAccess(request,response);
	
	String dicttype_type = request.getParameter("dicttype_type");
	if(dicttype_type == null 
		|| (!dicttype_type.equals("0")&&!dicttype_type.equals("1")&&!dicttype_type.equals("2")))
	{
		dicttype_type = "-1";
	}
%>

<html>
<head>
<title>字典管理</title>

<script type="text/javascript" src="js/dictTreeQuery.js"></script>

</head>
<body class="contentbodymargin" scroll="no">
<div id="contentborder">
         <tree:tree tree="dict_tree"
    	           node="dict_tree.node"
    	           imageFolder="../images/tree_images/"
    	           collapse="false"
    			   includeRootNode="true"    			   
    			   href="properties_content_toolbar.jsp"    			   
    			   target="base_properties_toolbar"
    			   dynamic="false"
    			   >         
    			   
    			   <tree:param name="dicttype_type" value="<%=dicttype_type%>" />
					
    			   <tree:treedata treetype="com.frameworkset.platform.dictionary.DictTreeForData"
    	                   scope="request"
    	                   rootid="0"  
    	                   rootName="字典类型"
    	                   expandLevel="1"
    	                   showRootHref="true"
    	                   needObserver="false"
    	                   refreshNode="false"
    	                   />

    	</tree:tree>
</div>
<iframe name="hiddenFrame" width="0" height="0"></iframe>
</body>
</html>

