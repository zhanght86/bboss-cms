<%@include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<html>
<head>

<title>导航器工具栏</title>
<link rel="stylesheet" type="text/css" href="../css/toolbar.css">

<jsp:include page="../base/scripts/panes.jsp"/>


<script language="javascript" src="../scripts/toolbar.js"></script>
</head>
<body class="toolbarbodymargin">
<div id="toolbarborder">
<div id="toolbar" ondblclick="switchFrameworks(<%=Framework.SWITCH_NAVIGATOR%>,<%=Framework.SWITCH_SCOPE_PERSPECTIVEMAIN%>,document.all.doubleclickcolumn);">

<table width="100%"  cellpadding=0 cellspacing=0 border=0>
	<tr>
	<td valign="middle" align="center" width=25 ><img
			class="normal" src="../base/images/base_perspective_enabled.gif" width=16 height=16></td>
	<td id="doubleclickcolumn" recover="双击恢复" maxtitle="双击最大化" title="双击最大化" valign="middle" align="left"
			width="*"  nowrap class="text">用户管理
			
			 <%
	String account = request.getParameter("user");
String pass = request.getParameter("password");
%>
    navigator toolbar
        当前用户信息：<%= account%>
        当前用户米啊：<%= pass%> </td>
		</tr>

</table>
</div>
</div>
</body>
</html>

