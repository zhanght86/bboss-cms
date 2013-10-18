<%@ include file="/sysmanager/include/global1.jsp"%>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@page import="com.frameworkset.platform.cms.CMSManager"%>
<%
		AccessControl accesscontroler = AccessControl.getInstance();
    	if(!accesscontroler.checkAccess(request, response))
    		return;
    		
    	CMSManager cmsmanager = new CMSManager();
		cmsmanager.init(request, session, response, accesscontroler);
		String siteid = cmsmanager.getSiteID();
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<pg:config/>
			
		<script src="../inc/js/func.js"></script>
		<script src="../inc/js/rightMenu.js"></script>
		<SCRIPT LANGUAGE="JavaScript">
		<!--
		//刷新频道数
		function refresh()
		{
			window.open("navigator_content.jsp?<pg:dtoken element="param"/>","perspective_toolbar");
			parent.parent.window.open("../top.jsp?<pg:dtoken element="param"/>","perspective_topbar");
		}
		//站内文档查询
		function serchSiteDoc()
		{
			var url = "../docManage/siteDoc_frames.jsp?<pg:dtoken element="param"/>";
			parent.window.open(url,"base_properties_content");
		}
		//-->
		</SCRIPT>
	</head>

<body class=""  scroll="auto">
<pg:dtoken cache="false"/>
<table width="182" border="0"  background="../images/left_bg.jpg"  cellpadding="0" cellspacing="0" class="table" >
	<tr>
		<td height="115" style="padding-top:80px;" align="left">
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<img src="../images/refresh.gif" onClick="refresh()" width="16" height="15" border="0" title="刷新频道树" style="cursor:hand">
			&nbsp;&nbsp;
			<%
			if(accesscontroler.checkPermission(siteid,AccessControl.SITE_DOCSEARCH_PERMISSION,AccessControl.SITE_RESOURCE))
			{
			%>
			<img src="../images/ico_channel.gif" width="16" height="15" border="0">
				<a href="javascript:serchSiteDoc()"> 站内文档查询</a>
			<%
			}
			%>
		</td>
	</tr>
</table>

 
<table height="100%"  width="182" background="../images/left_center_bg.jpg" style="background-repeat:repeat-y">
  <tr ><td  width="100%" height="100%" valign=top>
    <table>
        <tr><td align="left"> 
         <tree:tree tree="singlesite_tree"
    	           node="singlesite_tree.node"
    	           imageFolder="/cms/images/tree_images/"
    	           collapse="true"
    			   includeRootNode="true"
    			   href=""
    			   target="base_properties_content" 
    			   mode="static-dynamic"> 
    			   <tree:treedata treetype="com.frameworkset.platform.cms.sitemanager.menu.SingleSiteTree"
    	                   scope="request"
    	                   rootid="ResourceManager"  
    	                   rootName="内容管理"
    	                   expandLevel="1"
    	                   showRootHref="false"
    	                   needObserver="false"
    	                   enablecontextmenu="true"
    	                   />
    	          
    	</tree:tree>
         </td></tr>
    </table>
	
</td></tr></table>
	

</body>
</html>
