<%
/**
 * <p>Title: 机构管理员设置树</p>
 * <p>Description: 机构管理员设置树</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: chinacreator</p>
 * @Date 2008-3-17
 * @author da.wei
 * @version 1.0
 **/
 %>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %>
<%@ page import="com.frameworkset.platform.security.AccessControl" %>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkManagerAccess(request,response);
	String orgId1 = request.getParameter("orgId1");//设置机构管理员的机构
	String href = "changeOrgAdmin.jsp?orgId1=" + orgId1;
%>
<html>
<head>    
  <title>属性容器</title>
  <link rel="stylesheet" type="text/css" href="../../css/treeview.css">
<body class="contentbodymargin" scroll="no">
<div id="">
<form name="OrgJobForm" action="" method="post" >
<table class="table" width="80%" border="0" cellpadding="0" cellspacing="1"> 
  <tr class="tr" >
     <td  class="td">
    <tree:tree tree="role_org_tree"
    	           node="role_org_tree.node"
    	           imageFolder="../../images/tree_images/"
    	           collapse="true"
    			   includeRootNode="false"
    			   href="<%=href%>"
    			   target="userList"
    			   mode="static-dynamic"
    			   >                         
                   <tree:param name="uid"/>
                   <tree:param name="orgId"/>
                   <tree:param name="jobId"/>
                   <tree:param name="orgId1" value="<%=orgId1%>" />

    			   <tree:treedata treetype="com.frameworkset.platform.sysmgrcore.purviewmanager.menu.OrgAdminSelectTree"
    	                   scope="request"
    	                   rootid="0"  
    	                   rootName="机构树"
    	                   rootNameCode="sany.pdp.organization.tree.name"
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

