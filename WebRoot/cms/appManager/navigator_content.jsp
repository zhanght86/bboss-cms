<%@ include file="/sysmanager/include/global1.jsp"%>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%
		AccessControl accesscontroler = AccessControl.getInstance();
    	if(!accesscontroler.checkAccess(request, response))
    		return;
    	String viewPath = "cms::menu://sysmenu$root/appManager$module";
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
		<script language="JavaScript" src="common.js" type="text/javascript"></script>
		<script src="../inc/js/func.js"></script>
		<script language="JavaScript" src="<%=rootpath%>/sysmanager/resmanager/common.js" type="text/javascript"></script>
		<script language="JavaScript" src="<%=rootpath%>/sysmanager/include/pager.js" type="text/javascript"></script>
		<SCRIPT LANGUAGE="JavaScript">
		<!--
		function refresh()
		{
			window.open("navigator_content.jsp","perspective_toolbar");
			parent.parent.window.open("../top.jsp","perspective_topbar");
		}
		/**
		 * type:用户、机构、角色
		 * resid:资源id，对应当前站点id，所有的应用属于站点，所以通过站点id来作为所有应用的资源标识
		 * restype:资源类型，对应菜单的id，所有应用菜单作为一类型的资源
		 * resname：资源名称，对应菜单的名称
		 */
		function dispatch(type,resid,restype,resname){
		    if(type==0){
		        openWin('../resManager/user_iframe.jsp?resId2='+resid+'&resTypeId2='+restype+'&resTypeName='+resname+'&title='+resname,600,500);
		    }else if(type==1){
		        openWin('../resManager/role_iframe.jsp?resId2='+resid+'&resTypeId2='+restype+'&resTypeName='+resname+'&title='+resname,600,500);
		    }else if(type==2){
		        openWin('../resManager/org_iframe.jsp?resId2='+resid+'&resTypeId2='+restype+'&resTypeName='+resname+'&title='+resname,600,500);
		    }
		}
		//-->
		</SCRIPT>
	</head>

<body class=""  scroll="auto">

<table width="100%" border="0"  background="../images/left_bg.jpg"  cellpadding="0" cellspacing="0" class="table" >
	<tr>
		<td height="115" style="padding-top:80px;" align="left">
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<img src="../images/refresh.gif" onClick="refresh()" width="16" height="15" border="0" title="刷新频道树" style="cursor:hand">		</td>
	</tr>
</table>

 
<table height="100%"  width="100%" background="../images/left_center_bg.jpg"><tr ><td  width="100%" height="100%" valign=top>
    <table>
        <tr><td align="left"> 
        		<tree:tree tree="dispatch_column_tree" node="dispatch_column_tree.node" 
								imageFolder="../images/tree_images/" collapse="true" includeRootNode="true" 
								href="" 
								target="base_properties_content" mode="static-dynamic">
				

				<tree:treedata treetype="com.frameworkset.platform.cms.appmanager.menu.SiteApp" scope="request" 
									rootid="<%=viewPath%>" rootName="应用项管理" expandLevel="1" 
				showRootHref="false" 
				sortable="false"
				needObserver="false" refreshNode="false" enablecontextmenu="true"/>
				
			</tree:tree>
         </td></tr>
    </table>
	
	</td></tr></table>
	

</body>
</html>
