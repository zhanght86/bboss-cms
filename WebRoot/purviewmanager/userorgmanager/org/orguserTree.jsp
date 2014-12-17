<%
/*
 * <p>Title: 用户调入操作的用户机构树</p>
 * <p>Description: 用户调入操作的用户机构树</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-3-21
 * @author liangbing.tao
 * @version 1.0
 */
 %>

 <%     
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0); 
%>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.config.ConfigManager"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%
	AccessControl control = AccessControl.getInstance();
	control.checkManagerAccess(request,response);
	String oid=request.getParameter("oid");
	
	String href = "userList_ajax.jsp?oid=" + oid;
	
//	String state = "true";
//	if(!ConfigManager.getInstance().getConfigBooleanValue("sys.user.enablemutiorg", true)){
//		state = "false";
//	}
%>
<html>
	<head>
		<title>系统管理</title>
		<link rel="stylesheet" type="text/css" href="../../css/treeview.css">
	</head>
	<body class="contentbodymargin" scroll="auto">
		<div>
		    <table >
		    	<tr class="tr">
					<td class="td" align="center">				
							<!--<a href="discreteUser.jsp" target="base_properties_toolbar">离散用户管理</a>-->
						</td>			
				</tr>
		    	
		        <tr><td>
		         <tree:tree tree="UserOrg_tree"
		    	           node="UserOrg_tree.node"
		    	           imageFolder="../../images/tree_images/"
		    	           collapse="true"
		    			   includeRootNode="true"			   
		    			   href="<%=href%>"    			   
		    			   target="userList"
		    			   mode="static-dynamic"
		    			   >                         
		                   <tree:param name="oid"/>
		    			   <tree:treedata treetype="com.frameworkset.platform.sysmgrcore.purviewmanager.menu.OrgUserTree"
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
		         </td></tr>
		    </table>
		</div>
	</body>
</html>

