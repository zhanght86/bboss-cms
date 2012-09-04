<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%
/**
 * 
 * <p>Title: 角色选择页面</p>
 *
 * <p>Description: 角色设置中由于角色过多可视化效果很差，在角色设置中增加角色查询功能，该页面给用户选择角色</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: chinacreator</p>
 * @Date 2008-6-17
 * @author gao.tang
 * @version 1.0
 */
 %>
<% 
	AccessControl accessControl = AccessControl.getInstance();
	accessControl.checkManagerAccess(request,response);
%> 
<html>

  <head>
    <title>选择角色</title>
  </head>
  
  <body class="contentbodymargin" scroll="no">
  
    <div id="contentborder" style="width:100%;height:530;overflow:auto">
    
      <form action="" name="Form1">
      
		<table>
		  <tr>
		    <td>
		      <tree:tree tree="selectrole_tree"
	   	           node="selectrole_tree.node"
	   	           imageFolder="../../images/tree_images/"
	   	           collapse="true"
	   			   includeRootNode="true"    			   
	   			   href=""    			   
	   			   target="base_properties_toolbar"
	   			   mode="static-dynamic"
   			   >  				
   			 <tree:treedata treetype="com.frameworkset.platform.sysmgrcore.purviewmanager.menu.RoleTree"
 	                   scope="request"
 	                   rootid="0"  
 	                   rootName="角色"
 	                   expandLevel="1"
 	                   showRootHref="false"
 	                   needObserver="false"
 	                   />
   			 </tree:tree>
		    </td>
		  </tr>
		</table>
		
      </form>
      
    </div>
    
  </body>
  
</html>
