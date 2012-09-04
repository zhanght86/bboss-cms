<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %>
<%
	String userId = request.getParameter("userId");
	String orgId = request.getParameter("orgId");
	String href = "/sysmanager/user/userGroup.do?method=getGroupList&userId="+userId+"&orgId="+orgId ;
%>
<html>
<head>    
  <title>属性容器</title>
  <%@ include file="/include/css.jsp"%>
  <link rel="stylesheet" type="text/css" href="../css/treeview.css"> 
<body class="contentbodymargin" scroll="no">
<div id="contentborder">
<center>
<form name="userGroupForm" action="" method="post" >
<table class="table" width="80%" border="0" cellpadding="0" cellspacing="1"> 
  <tr class="tr" >
     <td  class="td">
     
    <tree:tree tree="group_role_tree"
    	           node="group_role_tree.node"
    	           imageFolder="../images/tree_images/"
    	           collapse="true"
    			   includeRootNode="true"
    			   href="<%=href%>"
    			   target="groupList"
    			   >                         
				   <tree:param name="userId"/>	
    			   <tree:treedata treetype="GroupTree"
    	                   scope="request"
    	                   rootid="0"  
    	                   rootName="组树"
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
</center>
</div>
</body>
</html>

