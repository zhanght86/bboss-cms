<%@include file="../../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<html>
<head>

<title>导航器工具栏</title>
<link rel="stylesheet" type="text/css" href="../../css/toolbar.css">

<jsp:include page="../../base/scripts/panes.jsp" />
<script language="javascript" src="../../scripts/toolbar.js"></script>
</head>
<body class="toolbarbodymargin">
<div id="toolbarborder">
<div id="toolbar" ondblclick="switchFrameworks(<%=Framework.SWITCH_NAVIGATOR%>,<%=Framework.SWITCH_SCOPE_PERSPECTIVEMAIN%>,document.all.doubleclickcolumn);">
<table width="100%"  cellpadding=0 cellspacing=0 border=0 class="table">
	<tr class="tr">
	<td class="td" valign="middle" align="center" width=25 ><img
			class="normal" src="../../base/images/base_perspective_enabled.gif" width=16 height=16></td>
	<td id="doubleclickcolumn" title="双击最大化" recover="双击恢复" maxtitle="双击最大化" valign="middle" align="left"width="*"  nowrap class="text">角色管理</td>
	<td class="td" valign="middle" align="center" width=25 ><a
       
        target="perspective_workarea"><img class="normal" 
        src="../../images/refresh_enabled.gif"
        onMouseOver="src='../../images/refresh_highlighted.gif';mouseover(this)"
        onMouseOut="src='../../images/refresh_enabled.gif';mouseout(this)"
        onMouseDown="src='../../images/refresh_highlighted.gif';mousedown(this)"
        onMouseUp="src='../../images/refresh_enabled.gif';mouseup(this)"
        alt="刷新" title="刷新"></a></td>
        
	</tr>
	
</table>
</div>
</div>
</body>
</html>

