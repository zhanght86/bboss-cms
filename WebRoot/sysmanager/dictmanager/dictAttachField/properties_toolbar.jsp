<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../include/global1.jsp"%>
<%@ include file="../../base/scripts/panes.jsp"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Dicttype" %>
<%@ page import="com.frameworkset.platform.dictionary.DictManager" %>
<%@ page import="com.frameworkset.platform.dictionary.DictManagerImpl" %>
<% 
	String dicttypeId = request.getParameter("dicttypeId");
	dicttypeId = dicttypeId==null?"":dicttypeId;
	String dictTabId = (String)session.getAttribute("dictTabId");
	if(dictTabId == null){
		dictTabId = "1";
	}
	
%>
<html>
<head>
<title>操作容器工具栏</title>
<link rel="stylesheet" type="text/css" href="../../css/toolbar.css">
<script language="javascript" src="../../scripts/toolbar.js"></script>
<script language="javascript">
	
	
</script>
<style type="text/css">
<!--
.style1 {color: #000000}
-->
</style>
</head>
<body class="toolbarbodymargin">
<div id="toolbarborder">
<div id="toolbar" ondblclick="switchFrameworks(<%=Framework.SWITCH_WORKSPACE%>,<%=Framework.SWITCH_SCOPE_PERSPECTIVEMAIN%>,document.all.doubleclickcolumn);">

<table width="100%"  cellpadding=0 cellspacing=0 border=0>
	<tr>
		<td valign="middle" align="center" width=25 ><img
			class="normal" src="../../images/actions.gif"></td>
		<td valign="middle"
			align="left"  nowrap class="text">字典高级字段配置&nbsp;
	    </td>
		<td id="doubleclickcolumn" recover="双击恢复" maxtitle="双击最大化" title="双击最大化" valign="middle"
			align="right" width="*"  nowrap class="text">
		
		</td>
	</tr>
</table>
</div>
</div>
</body>
</html>
