<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>

<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %>

<%
	String currRoleId = request.getParameter("currRoleId");
	String currOrgId = request.getParameter("currOrgId");
	String role_type = request.getParameter("role_type");
	String resTypeId = request.getParameter("resTypeId");
	
	String url = "operList_site_ajax.jsp?currRoleId="
		+currRoleId +"&currOrgId="+currOrgId+"&role_type="+role_type+"&resTypeId="+resTypeId;
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
	  <tr class="tr" >
	     <td  class="td">
		    <tree:tree tree="role_org_tree"
		    	           node="role_org_tree.node"
		    	           imageFolder="../../sysmanager/images/tree_images/"
		    	           collapse="true"
		    			   includeRootNode="true"
		    			   href="<%=url%>"
		    			   target="operList"
		    			   dynamic="false"
		    			   >                         
		                   <tree:param name="resTypeId"/>
		                   <tree:param name="currRoleId"/>
		                   <tree:param name="role_type"/>
		                   <tree:param name="currOrgId"/>
		
		    			   <tree:treedata treetype="SitePurviewTree"
		    	                   scope="request"
		    	                   rootid="0"  
		    	                   rootName="站点树"
		    	                   expandLevel="1"
		    	                   showRootHref="false"
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

