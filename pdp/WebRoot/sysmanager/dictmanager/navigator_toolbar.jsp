<%@include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<html>
<head>
<%
    String dicttype_type = request.getParameter("dicttype_type")==null?"字典管理":request.getParameter("dicttype_type");
    if("0".equals(dicttype_type)){
        dicttype_type = "基础字典数据采集";
    }else if("1".equals(dicttype_type)){
        dicttype_type = "通用业务数据采集";
    }else if("2".equals(dicttype_type)){
        dicttype_type = "授权业务数据采集";
    } 
    //给properties_content_toolbar.jsp
    session.removeAttribute("dicttype_type");
    session.setAttribute("dicttype_type",dicttype_type);
%>

<title>导航器工具栏</title>
<link rel="stylesheet" type="text/css" href="../css/toolbar.css">

<jsp:include page="../base/scripts/panes.jsp" />
<script language="javascript" src="../scripts/toolbar.js"></script>
<style type="text/css">
<!--
.style1 {color: #000000}
-->
</style>
</head>
<body class="toolbarbodymargin">
<div id="toolbarborder">
<div id="toolbar" ondblclick="switchFrameworks(<%=Framework.SWITCH_NAVIGATOR%>,<%=Framework.SWITCH_SCOPE_PERSPECTIVEMAIN%>,document.all.doubleclickcolumn);">
<table width="100%"  cellpadding=0 cellspacing=0 border=0>
	<tr>
	<td valign="middle" align="center" width=25 ><img
			class="normal" src="../base/images/base_perspective_enabled.gif" width=16 height=16></td>
	<td
			width="*"  align="left" valign="middle" nowrap class="text" id="doubleclickcolumn" recover="双击恢复" maxtitle="双击最大化" title="双击最大化"><%=dicttype_type%></td>
	<td valign="middle" align="center" width=25 ><a
       
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

