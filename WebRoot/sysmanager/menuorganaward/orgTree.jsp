<%@ include file="../include/global1.jsp"%>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%
/**
 * 项目：系统管理 
 * 描述：实现用户管理树型结构
 * 版本：1.0 
 * 日期：2007.11.29
 * 公司：三一集团信息
 * @author ge.tao
 */
%>
<html>
<head>
<title>系统管理</title>

<link rel="stylesheet" type="text/css" href="../css/treeview.css">
<%@ include file="/include/css.jsp"%>
<link rel="stylesheet" type="text/css" href="../css/contentpage.css">
<link rel="stylesheet" type="text/css" href="../css/tab.winclassic.css">
 
</head>
<body class="contentbodymargin" scroll="no">
<div id="contentborder">
    <table >
    	<tr class="tr">
			<td class="td" align="center">				
					<!--<a href="discreteUser.jsp" target="base_properties_toolbar">离散用户管理</a>-->
				</td>			
		</tr>
    	
        <tr><td>
         <tree:tree tree="UserOrg_tree"
    	           node="UserOrg_tre.node"
    	           imageFolder="../images/tree_images/"
    	           collapse="true"
    			   includeRootNode="true"    			   
    			   href="userList.jsp"    			   
    			   target="userList"
    			   mode="static-dynamic"  
    			   >                         
                 

    			   <tree:treedata treetype="com.frameworkset.platform.menu.OrgByUserTree"
    	                   scope="request"
    	                   rootid="0"  
    	                   rootName="机构树"
    	                   expandLevel="1"
    	                   showRootHref="false"
    	                   needObserver="false"
    	                  
    	                   />

    	</tree:tree>
         </td></tr>
    </table>
</div>
</body>
</html>

