<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.ResManager,com.frameworkset.platform.resource.ResourceManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Restype" %>
<%
	String resTypeId = request.getParameter("resTypeId");
	//ResManager resManager = SecurityDatabase.getResourceManager();
	//Restype resType = new Restype();
	//resType.setRestypeId("0");
	//List list = resManager.getChildResTypeList(resType);
	
	ResourceManager resManager = new ResourceManager();
	
	List list = resManager.getResourceInfos();
	if (list == null)
		list = new ArrayList();
	request.setAttribute("resTypeList", list);
%>
<html>
	<head>
		<title>属性容器</title>
		<script language="JavaScript" src="changeView.js" type="text/javascript"></script>
		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../css/treeview.css">
	<body class="contentbodymargin" scroll="no">
		<div id="contentborder">

				
					<table class="table" width="80%" border="0" cellpadding="0" cellspacing="1">
						
						
						<tr class="tr">
							<td class="td" width="40%" align="center">
								
							</td>
						</tr>
						<tr class="tr">
							<td class="td">

								<tree:tree tree="role_column_tree" 
									node="role_column_tree.node" 
									imageFolder="../images/tree_images/" 
									collapse="true" 
									includeRootNode="true" 
									href="/sysmanager/accessmanager/role/operList_column_ajax.jsp" 
									target="operList"
									dynamic="false">
									<tree:param name="resTypeId"/>

									<tree:treedata treetype="com.frameworkset.platform.menu.AuthorColumnTree" scope="request" rootid="0" rootName="栏目树" expandLevel="1" showRootHref="false" 
									needObserver="false" 
									refreshNode="true"
									/>
								</tree:tree>
							</td>
						</tr>
					</table>
		

		</div>
		
	</body>
</html>

