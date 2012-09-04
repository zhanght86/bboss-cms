
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.resource.ResourceManager"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	String showhref = "" + accesscontroler.checkPermission("addrootgroup",
							"addgroup", AccessControl.GROUP_RESOURCE);
%>
<html>
<head>    
  <title>属性容器</title>
  <%@ include file="/include/css.jsp"%>
  <link rel="stylesheet" type="text/css" href="../css/treeview.css"> 
<body class="contentbodymargin" scroll="no">
<div id="contentborder">
<center>
<form name="OrgJobForm" action="" method="post" >
<table class="table" width="80%" border="0" cellpadding="0" cellspacing="1"> 
  <tr class="tr" >
     <td  class="td" align="center">
     <%
     ResourceManager resManager = new ResourceManager();
	 String resId = null;
	 try{
	 	resId = resManager.getGlobalResourceid(AccessControl.GROUP_RESOURCE);
	 }catch(Exception e){
	 
	 }
	 if (resId == null || !resId.equals("") || accesscontroler.checkPermission(resId,
	 				"addgroup", AccessControl.GROUP_RESOURCE))
	 {
	 %>
     <a href="groupInfo_add.jsp" target="base_properties_content">新增一级用户组</a>
     <%}%>
	</td>				  
  </tr>  
</table>
<table class="table" width="80%" border="0" cellpadding="0" cellspacing="1"> 
  <tr class="tr" >
     <td  class="td">     
    <tree:tree tree="group_role_tree"
    	           node="group_role_tree.node"
    	           imageFolder="../images/tree_images/"
    	           collapse="true"
    			   includeRootNode="true"
    			   href="/sysmanager/groupmanager/properties_toolbar.jsp"
    			   target="base_properties_toolbar"
    			   dynamic="false"
    			   >    
    			   <tree:treedata treetype="GroupTree"
    	                   scope="request"
    	                   rootid="0"  
    	                   rootName="组树"
    	                   expandLevel="1"
    	                   showRootHref="<%=showhref%>"
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

