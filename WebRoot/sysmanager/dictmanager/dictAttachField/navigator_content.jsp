<%@ include file="../../include/global1.jsp"%>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%
	AccessControl accessControl = AccessControl.getInstance();
	accessControl.checkManagerAccess(request,response);
%>

<html>
<head>
<title>字典高级字段配置</title>

<link rel="stylesheet" type="text/css" href="../../css/treeview.css">
<%@ include file="/include/css.jsp"%>
<link rel="stylesheet" type="text/css" href="../../css/contentpage.css">
<link rel="stylesheet" type="text/css" href="../../css/tab.winclassic.css">
<script type="text/javascript" src="../js/dictTreeQuery.js"></script>
</head>
<body class="contentbodymargin" scroll="no">
<form action="" target="hiddenFrame" name="dictForm">
<div id="contentborder">
    <table>
   <tr>
			<td align="left">
			<input name="blurryValue" type="text" onpropertychange="onBlurryQueryChange()" onkeydown="enterKeydowngo()"/>
			
			</td>
			
			
		</tr>
		<tr>
		<td align="center">
			<input type="button" name="queryBtn" value="查询" onclick="blurryQuary()"  class="input">
			<input style="display:none" name="nextBtn" type="button" value="查找下一个" onclick="nextNodes()" class="input">
			</td>
		</tr>
        <tr><td>
         <tree:tree tree="dict_tree"
    	           node="dict_tree.node"
    	           imageFolder="../../images/tree_images/"
    	           collapse="true"
    			   includeRootNode="true"    			   
    			   href="advanceFieldList.jsp"    			   
    			   target="base_properties_content"
    			   dynamic="false"
    			   >                                            

    			   <tree:treedata treetype="com.frameworkset.platform.dictionary.AttachDictTypeTree"
    	                   scope="request"
    	                   rootid="0"  
    	                   rootName="字典类型"
    	                   expandLevel="1"
    	                   showRootHref="true" 
    	                   needObserver="false"
    	                   refreshNode="false"
    	                   />

    	</tree:tree>
         </td></tr>
    </table>
</div>
</form>
<iframe name="hiddenFrame" width="0" height="0"></iframe>
</body>
</html>

