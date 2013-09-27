<%@ include file="/sysmanager/include/global1.jsp"%>
<jsp:include page="/sysmanager/base/scripts/panes.jsp"/>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="com.frameworkset.platform.security.AccessControl,com.frameworkset.platform.cms.CMSManager"%>
<%
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request, response);
	CMSManager cmsmanager = new CMSManager();
	cmsmanager.init(request,session,response,control);
	String rootName = "模版管理[" +cmsmanager.getCurrentSite().getName()+ "]" ;
	String siteId = cmsmanager.getSiteID();
	
	if (!control.checkPermission(
			siteId,AccessControl.SITE_TEMPLATEVIEW_PERMISSION,AccessControl.SITETPL_RESOURCE)
			&& !control.checkPermission(
			siteId,AccessControl.SITE_FILEVIEW_PERMISSION,AccessControl.SITEFILE_RESOURCE)) {%>
			<script language="javascript">
			alert("对不起，您没有权限，请联系站点管理员!");
			</script>			
						
<%
	}
%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	
	<link rel="stylesheet" type="text/css" href="<%=rootpath%>/sysmanager/css/treeview.css">
	<link rel="stylesheet" type="text/css" href="<%=rootpath%>/sysmanager/css/windows.css">
	<link rel="stylesheet" type="text/css" href="<%=rootpath%>/sysmanager/css/contentpage.css">
	<link rel="stylesheet" type="text/css" href="<%=rootpath%>/sysmanager/css/tab.winclassic.css">
	
	<script src="../inc/js/func.js"></script>
	<script src="../inc/js/rightMenu.js"></script>
</head>
<body class=""  scroll="auto">
<table height="100%"  width="100%" background="../images/left_center_bg.jpg">
	<tr>
		<td width="100%" height="100%" valign=top>
			<table>			   
				<tr>
					<td align="left"> 
						<tree:tree tree="filesystem_tree"
						node="filesystem_tree.node"
						imageFolder="/cms/images/tree_images/"
						collapse="true"
						includeRootNode="false"
						href="file_ListFrameset.jsp"
						target="base_properties_content" 
						mode="static-dynamic"> 
							<tree:treedata treetype="TemplateFolderTree"
								scope="request"
								rootid="//theRoot"  
								rootName="<%=rootName%>"
								expandLevel="1"
								showRootHref="false"
								needObserver="false"
								enablecontextmenu="false"
								/>      
						</tree:tree>					
					</td>
				</tr>
				
			</table>	
		</td>
	</tr>
</table>
</body>
</html>