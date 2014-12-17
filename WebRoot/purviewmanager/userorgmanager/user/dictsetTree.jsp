<%
/**
 * 
 * <p>Title: 字典树页面</p>
 *
 * <p>Description: 字典定义树页面</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: bboss</p>
 * @Date 2006-9-15
 * @author gao.tang
 * @version 1.0
 */
 %>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkManagerAccess(request,response);
    String currRoleId = request.getParameter("currRoleId");
	String currOrgId = request.getParameter("currOrgId");
	String role_type = request.getParameter("role_type");
    String url = "operList_dict_ajax.jsp?resTypeId=dict&currRoleId="+currRoleId+"&currOrgId="+currOrgId+"&role_type="+role_type;
%>
<html>
<head>
<title>字典管理</title>

<link rel="stylesheet" type="text/css" href="../../css/treeview.css">
<%@ include file="/include/css.jsp"%>

</head>
<body class="contentbodymargin" scroll="no">
<div id="contentborder">
    <table >
    	
    	
        <tr><td>
         <tree:tree tree="dict_tree"
    	           node="dict_tree.node"
    	           imageFolder="../../images/tree_images/"
    	           collapse="true"
    			   includeRootNode="true"    			   
    			   href="<%=url%>"    			   
    			   target="operList"
    			   dynamic="false"
    			   >                                            
					<tree:param name="resTypeId"/>
					<tree:param name="currRoleId"/>
					<tree:param name="role_type"/>

    			   <tree:treedata treetype="com.frameworkset.platform.dictionary.UserGrantResDictTree"
    	                   scope="request"
    	                   rootid="0"  
    	                   rootName="字典类型"
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

