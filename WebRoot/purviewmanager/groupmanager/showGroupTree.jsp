<%
/*
 * <p>Title: 显示用户组树</p>
 * <p>Description: 显示用户组树</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: chinacreator</p>
 * @Date 2008-3-18
 * @author liangbing.tao
 * @version 1.0
 */
%>

<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>

<%
	
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkManagerAccess(request,response);
	
	String userId = request.getParameter("userId");

	String href = "userGroupManagerAction.jsp?userId=" + userId;	
%>
<html>
	<head>    
	  <title>属性容器</title>
	  <link rel="stylesheet" type="text/css" href="../css/treeview.css"> 
	</head>
	<body class="contentbodymargin" scroll="no">
		<div id="">
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
						    			   mode="static-dynamic"
						    			   >                         
										   <tree:param name="userId"/>	
						    			   <tree:treedata treetype="GroupTree"
						    	                   scope="request"
						    	                   rootid="0"  
						    	                   rootName="用户组树"
						    	                   rootNameCode="sany.pdp.usergroup.tree.name"
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

