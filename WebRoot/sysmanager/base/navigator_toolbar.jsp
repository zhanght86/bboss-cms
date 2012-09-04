<%@include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<html>
<head>

<title>导航器工具栏</title>
<link rel="stylesheet" type="text/css" href="../css/toolbar.css">
<%
if (pageContext.getAttribute("formAction", 2)==null)
pageContext.setAttribute("formAction", request.getContextPath() + "/sysmanager/base/actions/ResizeBaseFramesActionJSP.jsp", 2);
if (pageContext.getAttribute("formFrameName", 2)==null)
pageContext.setAttribute("formFrameName", "base_navigator_container", 2);
%>
<!--jsp:include page="/base/scripts/framesets.jsp" /-->
<script language="javascript">
  var perspectiveWorkArea = top.frames["perspective_workarea"];
  var perspectiveToolbar = top.frames["perspective_toolbar"];
  var perspectiveContent = top.frames["perspective_content"];
  //var perspectiveContent = top.frames["perspective_content"];
  function getPerspectiveContentFrameset()
  {
    return perspectiveContent.document.getElementsByTagName("frameset").item(0);
  }

  function toggleDoubleClickColumnTitle()
  {
    var doubleClickColumn = document.getElementById("doubleclickcolumn");
    if (doubleClickColumn == null)
      return;

    if (doubleClickColumn.title == "双击恢复")
      doubleClickColumn.title = "双击最大化";
    else
      doubleClickColumn.title = "双击恢复";
  }
</script>
<script language="javascript">
  function getActionsContainerFrameset()
  {
    return perspectiveContent.frames["base_actions_container"].document.getElementsByTagName("frameset").item(0);
  }

  function processFramesetSizes(framesetSizesForm)
  {
    toggleDoubleClickColumnTitle();
    var perspectiveContentFrameset = getPerspectiveContentFrameset();
    var actionsContainerFrameset = getActionsContainerFrameset();
    framesetSizesForm.framesetColsPerspectiveContent.value = perspectiveContentFrameset.cols;
    framesetSizesForm.framesetRowsActionsContainer.value = actionsContainerFrameset.rows;
    //alert(framesetSizesForm.action);
    framesetSizesForm.submit();
  }
</script>
<script language="javascript" src="../scripts/toolbar.js"></script>
</head>
<body class="toolbarbodymargin">
<div id="toolbarborder">
<div id="toolbar" ondblclick="processFramesetSizes(document.forms[0])">
<jsp:include page="forms/ProcessFramesetsForm.jsp" />
<!--FORM style="MARGIN-TOP: 0px; MARGIN-BOTTOM: 0px"
action=/wapsite/base/actions/ResizeBaseFramesActionJSP.jsp method=post
target=perspective_workarea encType=multipart/form-data><INPUT type=hidden
value=base_navigator_container name=frameName> <INPUT type=hidden value=30%,*
name=framesetColsPerspectiveContent> <INPUT type=hidden value=75%,*
name=framesetRowsActionsContainer></FORM-->
<table width="100%" height=25 cellpadding=0 cellspacing=0 border=0>
	<tr>
	<td valign="middle" align="center" width=25 height=25><img
			class="normal" src="../../images/base_perspective_enabled.gif" width=16 height=16></td>
	<td id="doubleclickcolumn" recover="双击恢复" maxtitle="双击最大化" title="双击最大化" valign="middle" align="left"
			width="*" height=25 nowrap class="text">系统视图</td>
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

