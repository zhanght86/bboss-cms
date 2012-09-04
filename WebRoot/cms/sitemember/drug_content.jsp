<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree"%>


<html>
	<head>
		<title>属性容器</title>
		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../../sysmanager/css/treeview.css">		
		<link rel="stylesheet" type="text/css" href="../../sysmanager/css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="../../sysmanager/css/tab.winclassic.css">
	</head>
	<body class="contentbodymargin" scroll="no">
		<div id="contentborder">
			<center>
				<form name="OrgJobForm" action="" method="post">
					<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table">
								
						<tr class="tr">
							<td class="td" colspan="2">
								<tree:tree tree="role_tree"
				    	           node="role_tree.node"
				    	           imageFolder="../../sysmanager/images/tree_images/"
				    	           collapse="true"
				    			   includeRootNode="true"    			   
				    			   href="../sitemember/properties_toolbar.jsp"    			   
				    			   target="base_properties_toolbar"
				    			   dynamic="false"
				    			   >                   
				
				    			   <tree:treedata treetype="RoleDrugTree"
				    	                   scope="request"
				    	                   rootid="0"  
				    	                   rootName="医药网角色类型"
				    	                   expandLevel="1"
				    	                   showRootHref="false"
				    	                   needObserver="false"
				    	                  
				    	                   />
				
				    			</tree:tree>
							</td>
						</tr>
					</table>					
				</form>
			</center>
		</div>
	</body>
</html>
