<%@ include file="../../sysmanager/include/global1.jsp"%>
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
	//operList_column_ajax.jsp?resTypeId=column
	
	String roleType = (String)session.getAttribute("role_type");
%>
<html>
	<head>
		<title>属性容器</title>
		<script language="JavaScript" src="changeView.js" type="text/javascript"></script>
		<link rel="stylesheet" type="text/css" href="../../sysmanager/css/windows.css">
		<link rel="stylesheet" type="text/css" href="../../sysmanager/css/treeview.css">
	<script language="javascript">
		function grantColumn(){
			var obj=document.getElementsByName("treeid");
			var resIds = "";
			if(obj){
				if(obj.length){
					for(var i = 0; i < obj.length; i++){
						if(obj[i].checked){
							if(resIds==""){
								resIds = obj[i].value;
							}else{
								resIds += ":" + obj[i].value;
							}
						}
					}
				}
			}
			//alert(resIds);
			document.all.resIds.value=resIds;
			document.all.sub.disabled="true";
			document.columnTree.target="hiddenFrame";
			document.columnTree.action="saveGrant.jsp";
			document.columnTree.submit();
		}
	</script>
	</head>
	<body class="contentbodymargin" scroll="no">
		
		<div id="contentborder">
			<form id="columnTree" name="columnTree" action="" method="post" >
			<input name="resTypeId" type="hidden" value="<%=resTypeId%>" />
			<input name="roleType" type="hidden" value="<%=roleType%>" />
			<input name="resIds" type="hidden" />
			<table class="table" width="80%" border="0" cellpadding="0" cellspacing="1">
				<tr class="tr">
					<td class="td" width="40%" align="center">
					</td>
				</tr>
				<tr class="tr">
					<td class="td">

						<tree:tree tree="role_column_tree" 
						 node="role_column_tree.node" 
						 imageFolder="../../sysmanager/images/tree_images/" 
						 collapse="true" 
						 includeRootNode="true" 
						 href="" 
						 target="operList" 
						 mode="static">
						 
						<tree:param name="resTypeId"/>
						<tree:checkbox recursive="true" partuprecursive="true" name="treeid"  />
						
						<tree:treedata treetype="com.frameworkset.platform.menu.ColumnCmsTree" 
						 scope="request" 
						 rootid="0" rootName="菜单树" 
						 expandLevel="1" 
						 showRootHref="false" 
						 needObserver="false" 
						 refreshNode="true"/>
						</tree:tree>
					</td>
				</tr>
				<tr>
					<td align="center"><input class="input" name="sub" type="button" value="确认" onclick="grantColumn()" /></td>
				</tr>
			</table>
			</form>
		</div>
		<iframe name="hiddenFrame" width=0 height=0></iframe>
	</body>
</html>
