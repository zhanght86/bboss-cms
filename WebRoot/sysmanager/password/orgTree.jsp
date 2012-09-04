<%@ include file="../include/global1.jsp"%>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.AccessControl" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.db.OrgAdministratorImpl" %>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
%>
<%@ page import="org.frameworkset.web.servlet.support.RequestContextUtils"%>

<%
	request.setAttribute("rootName", RequestContextUtils.getI18nMessage("sany.pdp.organization.tree.name", request));
%>

<html>
<head>
<title>系统管理</title>

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
    	           imageFolder="../images/tree_images/"
    	           collapse="true"
    			   includeRootNode="true"    			   
    			   href="userList.jsp"    			   
    			   target="userList"
    			   mode="static"  
    			   >                         
                 

    			   <tree:treedata treetype="com.frameworkset.platform.menu.OrgChargeTree"
    	                   scope="request"
    	                   rootid="0"  
    	                   rootName="${rootName}"
    	                   expandLevel="1"
    	                   showRootHref="false"
    	                   needObserver="false"
    	                  
    	                   />

    	</tree:tree>
         </td></tr>
    </table>
</div>
</body>
</html>

