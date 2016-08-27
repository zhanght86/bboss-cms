
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.AccessControl" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.db.OrgAdministratorImpl" %>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
%>
<html>
<head>
<title>系统管理</title>


<%@ include file="/include/css.jsp"%>

 
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
    	           node="UserOrg_tre.node"
    	           imageFolder="/sysmanager/images/tree_images/"
    	           collapse="true"
    			   includeRootNode="false"    			   
    			   href="userList.jsp"    			   
    			   target="userList"
    			    mode="static-dynamic"  
    			   >                         
                 

    			   <tree:treedata treetype="com.frameworkset.platform.menu.OrgChargeTree"
    	                   scope="request"
    	                   rootid="0"  
    	                   rootName="机构树"
    	                   expandLevel="1"
    	                   showRootHref="false"
    	                   needObserver="false"
    	                   enablecontextmenu="false"
    	                   />

    	</tree:tree>
         </td></tr>
    </table>
</div>
</body>
</html>

