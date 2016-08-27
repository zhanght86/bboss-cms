<%@include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<title>操作容器工具栏</title>
<link rel="stylesheet" type="text/css" href="../css/toolbar.css">
<%
if (pageContext.getAttribute("formAction", 2)==null)
pageContext.setAttribute("formAction", request.getContextPath() + "/base/actions/ResizeBaseFramesActionJSP.jsp", 2);
if (pageContext.getAttribute("formFrameName", 2)==null)
pageContext.setAttribute("formFrameName", "base_status_container", 2);
%>
<jsp:include page="../base/scripts/framesets.jsp" />
<script language="javascript" src="../scripts/toolbar.js"></script>
</head>
<body class="toolbarbodymargin">
<div id="toolbarborder">
<div id="toolbar" ondblclick="processFramesetSizes(document.forms[0])">
<jsp:include page="../base/forms/ProcessFramesetsForm.jsp" />
<table width="100%" height=25 cellpadding=0 cellspacing=0 border=0>
	<tr>
		<td valign="middle" align="center" width=25 height=25><img
			class="normal" src="../images/status.gif" width=16 height=16></td>
		<td id="doubleclickcolumn" recover="双击恢复" maxtitle="双击最大化" title="双击最大化" valign="middle" align="left"
			width="*" height=25 nowrap class="text">操作信息</td>
		<td valign="middle" align="center" width=25 height=25><a
			href="<%=request.getContextPath()%>/ReloadPerspectiveActionJSP.jsp"
			target="perspective_workarea"><img class="normal"
			src="../images/refresh_enabled.gif"
			onMouseOver="src='../images/refresh_highlighted.gif';mouseover(this)"
			onMouseOut="src='../images/refresh_enabled.gif';mouseout(this)"
			onMouseDown="src='../images/refresh_highlighted.gif';mousedown(this)"
			onMouseUp="src='../images/refresh_enabled.gif';mouseup(this)"
			alt="刷新" title="刷新"></a></td>
	</tr>
</table>
</div>
</div>
</body>
</html>

