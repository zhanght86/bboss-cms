<%@include file="../../sysmanager/include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<html>
	<head>

		<title>导航器工具栏</title>
		<link rel="stylesheet" type="text/css" href="../../sysmanager/css/toolbar.css">

		<jsp:include page="../../sysmanager/base/scripts/panes.jsp" />

		<script language="javascript" src="../../sysmanager/scripts/toolbar.js"></script>	
	</head>
	<body class="toolbarbodymargin">
			<div id="toolbar" ondblclick="switchFrameworks(<%=Framework.SWITCH_NAVIGATOR%>,<%=Framework.SWITCH_SCOPE_PERSPECTIVEMAIN%>,document.all.doubleclickcolumn);">				
				<table width="100%"  cellpadding=0 cellspacing=0 border=0>
					<tr>
						<td valign="middle" align="center" width=25 >
							<img class="normal" src="../../sysmanager/base/images/base_perspective_enabled.gif" width=16 height=16>
						</td>
						<td width="*"  align="left" valign="middle" nowrap class="text" id="doubleclickcolumn" recover="双击恢复" maxtitle="双击最大化" title="双击最大化">
							选择菜单模块
						</td>
						<td valign="middle" align="center" width=25 >
							<a  target="perspective_workarea"><img class="normal" src="../../sysmanager/images/refresh_enabled.gif" onMouseOver="src='../../sysmanager/images/refresh_highlighted.gif';mouseover(this)"
									onMouseOut="src='../../sysmanager/images/refresh_enabled.gif';mouseout(this)" onMouseDown="src='../../sysmanager/images/refresh_highlighted.gif';mousedown(this)" onMouseUp="src='../../sysmanager/images/refresh_enabled.gif';mouseup(this)" alt="刷新" title="刷新"></a>
						</td>

					</tr>

				</table>
			</div>
	</body>
</html>
