<%@ taglib uri="../../WEB-INF/treetag.tld" prefix="tree" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.config.ConfigManager"%>
<%
	String oid=request.getParameter("oid");
	
	String href = "userList_ajax.jsp?oid=" + oid;
	String state = "true";
	if(!ConfigManager.getInstance().getConfigBooleanValue("sys.user.enablemutiorg", true)){
		state = "false";
	}
%>
<html>
<head>
<title>系统管理</title>

<link rel="stylesheet" type="text/css" href="../../sysmanager/css/treeview.css">
<link rel="stylesheet" type="text/css" href="../../sysmanager/css/windows.css">
<link rel="stylesheet" type="text/css" href="../../sysmanager/css/contentpage.css">
<link rel="stylesheet" type="text/css" href="../../sysmanager/css/tab.winclassic.css">
 
</head>
<body class="contentbodymargin" scroll="no">
<div id="contentborder">
    <table >
    	<tr class="tr">
			<td class="td" align="center">				
					<!--<a href="discreteUser.jsp" target="base_properties_toolbar">离散用户管理</a>-->
				</td>			
		</tr>
    	
        <tr><td>
         <tree:tree tree="UserOrg_tree"
    	           node="UserOrg_tree.node"
    	           imageFolder="../../sysmanager/images/tree_images/"
    	           collapse="true"
    			   includeRootNode="<%=state%>"				   
    			   href="<%=href%>"    			   
    			   target="userList"
    			   dynamic="false"
    			   >                         
                   <tree:param name="oid"/>
    			   <tree:treedata treetype="com.frameworkset.platform.menu.OrgUserTree"
    	                   scope="request"
    	                   rootid="0"
    	                   rootName="机构树"
    	                   expandLevel="1"
    	                   showRootHref="true"
    	                   needObserver="false"
    	                   refreshNode="false"
    	                   />

    	</tree:tree>
         </td></tr>
    </table>
</div>
</body>
</html>
