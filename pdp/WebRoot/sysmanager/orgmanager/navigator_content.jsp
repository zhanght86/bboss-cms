<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.db.OrgAdministratorImpl"%>
<%@ include file="../include/global1.jsp"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.resource.ResourceManager"%>
<%
	AccessControl accessControl = AccessControl.getInstance();
	accessControl.checkAccess(request,response);
	
	//当前用户是否拥有超级管理员权限与部门管理员权限
	boolean isAdminOrOrgManager = false;
	//是否是管理员
	boolean isOrgManager = new OrgAdministratorImpl().isOrgManager(accessControl.getUserID());
	boolean isAdmin = accessControl.isAdmin();
	if(isAdmin || isOrgManager){
		isAdminOrOrgManager = true;
	}
	                         
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>   </title>
<script language="javascript">
function action11(){
	getPropertiesContent().location.href="<%=rootpath%>/sysmanager/orgmanager/organsearch.jsp";   
}
function openWin(url,swidth,sheight)
{
	var w = showModalDialog(url,window,"dialogWidth:"+swidth+"px;dialogHeight:"+sheight+"px;help:no;scroll:auto;status:no");
	return w;
}
function changeOrgAdmin(orgId)
{
	var okay = openWin("orgAdmin.jsp?orgId=" + orgId,600,500);
	if(okay == "okay")
	{
		getPropertiesContent().location.reload();
	}
}
function sortOrg(orgId,orgName)
{
	openWin("sortOrg.jsp?orgId=" + orgId + "&orgName=" + orgName,500,500);
}
//reclaim user's resource under organization
//ge.tao
function reclaimOrgUserRes(orgId){
    openWin("../../purviewmanager/reclaimManager/reclaimOrgUserRes.jsp?orgId=" + orgId,500,400);
}
</script>
<link rel="stylesheet" type="text/css" href="../css/treeview.css">
<%@ include file="/include/css.jsp"%>
<link rel="stylesheet" type="text/css" href="../css/contentpage.css">
<link rel="stylesheet" type="text/css" href="../css/tab.winclassic.css">

</head>
<body class="contentbodymargin" scroll="auto">
<%if(isAdminOrOrgManager){  %>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table" >
  <tr>
    <td align="center">
    <a href="<%=rootpath%>/sysmanager/orgmanager/organsearch.jsp" target="base_properties_content">机构查询</a>
    <%
	//ResourceManager resManager = new ResourceManager();
	//String resId = resManager.getGlobalResourceid(AccessControl.ORGUNIT_RESOURCE);
	//if (resId != null && !resId.equals("") && accessControl.checkPermission(resId,
	//				"addorg", AccessControl.ORGUNIT_RESOURCE))
	//{
	if(accessControl.isAdmin()){
	%>
	&nbsp;&nbsp;
    <a href="<%=rootpath%>/sysmanager/orgmanager/new_org.jsp" target="base_properties_content">新增一级机构</a>
    <%}%>
    </td>   
  </tr>
</table>
    <table >
        <tr><td align="left">
         <tree:tree tree="org_tree"
    	           node="org_tree.node"
    	           imageFolder="../images/tree_images/"
    	           collapse="true"
    			   includeRootNode="true"
    			   href="properties_toolbar.jsp" 
    			   target="base_properties_toolbar" 
    			   dynamic="false"
    			   > 
    			    
    			   <tree:treedata treetype="OrgChargeTree"
    	                   scope="request"
    	                   rootid="0"  
    	                   rootName="机构树"
    	                   expandLevel="1"
    	                   showRootHref="false"
    	                   needObserver="false"
    	                   enablecontextmenu="true" 
    	                   />
					
    	</tree:tree>
         </td></tr>
    </table>
    <%}else{%>
<div align="center">没有权限！</div>
<%}%>
<script language="javascript">

</script>

</body>
</html>

