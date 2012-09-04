<%@ include file="/sysmanager/include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.chinaspis.base.perspective.*,com.chinaspis.explorer.perspective.*"%>
<%Perspective perspective = controller.getBasePerspective();%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>²ڗ��ࠀ¸</title>
<link rel="stylesheet" type="text/css" href="../css/toolbar.css">
<%
//if (pageContext.getAttribute("formAction", 2) == null)
pageContext.setAttribute(
	"formAction",
	request.getContextPath() + "/sysmanager/base/actions/ResizeBaseFramesActionJSP.jsp",
	2);
if (pageContext.getAttribute("formFrameName", 2) == null)
	pageContext.setAttribute("formFrameName", "base_properties_container", 2);
%>
<jsp:include page="/sysmanager/base/scripts/framesets.jsp" />
<script language="javascript" src="../scripts/toolbar.js"></script>
<script language="javascript">
	function getElementByName(eName){
		var elements = document.body.all;
		for(i = 0;i< elements.length;i++){
           if (elements[i].name==eName) return elements[i];
		}
		return null;
	}
	function selectTool(toolName) {
	    var tool=getElementByName(toolName);
		if (tool) {
		    tool.onclick();
		}
	}
</script>
</head>
<body class="toolbarbodymargin">
<div id="toolbarborder">
<div id="toolbar" ondblclick="processFramesetSizes(document.forms[0])">
<jsp:include page="/sysmanager/base/forms/ProcessFramesetsForm.jsp" />
<table width="100%" height=25 cellpadding=0 cellspacing=0 border=0>
	<tr>
		<td valign="middle" align="center" width=25 height=25><img
			class="normal" src="../images/actions.gif"></td>
		<td valign="middle"
			align="left" width="60" height=25 nowrap class="text">Śɝ¼°²ڗ�d>
		<td id="doubleclickcolumn" title="˫»�»¯" valign="middle"
			align="right" width="*" height=25 nowrap class="text">
		<table height=25 cellpadding=0 cellspacing=0 border=0>
			<tr><td class=normal name="tool1" title="т½¨¿ͻ§хϢ" valign=middle align=center onmousedown=mousedown(this) onmouseout=mouseout(this) onmouseover=mouseover(this) onmouseup=mouseup(this) onclick="top.perspective_workarea.location='/future3/base/actions/SelectBaseToolActionJSP.jsp?nodeId=5&toolId=1'" height=25><table cellSpacing=0 cellPadding=0 border=0><tr><td valign=center>&nbsp;<img src="../images/new.gif" alt="т½¨¿ͻ§хϢ" width=16 height=16 border=0 class="normal"></td><td valign=center>&nbsp;т½¨&nbsp;</td></tr></table></td>
				<%--NodeManager nodemanager = perspective.getNodeManager();
Node node = nodemanager.getSelectedNode();
if (node != null) {
	ToolManager toolmanager;
	if (node.getViewId() == -1)
		toolmanager = node.getToolManager();
	else
		toolmanager = node.getViewToolManager();
	for (int i = 0; i < toolmanager.getNumberOfTools(); i++) {
		Tool tool = toolmanager.getTool(i);
		out.print(tool.renderTool(response, controller));
	}
}--%><td width="5"><td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</div>
</div>
</body>
</html>

