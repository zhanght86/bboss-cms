<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.ResManager,com.frameworkset.platform.resource.ResourceManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Restype" %>
<%
	String roleId = (String)session.getAttribute("currRoleId");
	String restypeId=request.getParameter("restypeId");
	//out.println(roleId);
	
%>
<html>
	<head>
		<title>属性容器</title>
		<script language="JavaScript" src="../scripts/changeView.js" type="text/javascript"></script>
<%@ include file="/include/css.jsp"%>
  		 <link rel="stylesheet" type="text/css" href="../css/treeview.css"> 
	<body class="contentbodymargin" scroll="no">
		<div id="contentborder">

				<form name="OrgJobForm" action="" method="post">
					<table class="table" width="80%" border="0" cellpadding="0" cellspacing="1">
						
						<tr class="tr">
							<td class="td">

								<tree:tree tree="res_tree1" 
				        		node="res_tree1.node" imageFolder="../images/tree_images/" 
				        		collapse="true" 
				        		includeRootNode="true" 
				        		href="rolereslist.jsp"    
				        		target="roleresList">
												
				
							
								<tree:treedata treetype="ResourceType1Tree" 
												scope="request" 
												rootid="0" 
												rootName="资源树" 
												expandLevel="1" 
												showRootHref="false" 
												needObserver="false" 
												refreshNode="false"
												/>
								</tree:tree>
							</td>
						</tr>
					</table>
				</form>

		</div>
	</body>
</html>

