
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>


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
    			   mode="static-dynamic"  
    			   >                         
                 

    			   <tree:treedata treetype="com.sany.workflow.util.AllOrgTree"
    	                   scope="request"
    	                   rootid="0"  
    	                   rootName="机构树"
    	                   rootNameCode="sany.pdp.organization.tree.name"
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

