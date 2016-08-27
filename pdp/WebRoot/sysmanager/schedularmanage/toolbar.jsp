<%@ include file="../include/global1.jsp"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.User" %>
<%
String userId = request.getParameter("userId");

//点击了用户，置session
if(userId != null){
	session.setAttribute("currUserId",Integer.valueOf(userId));
}
if(userId == null){
	session.setAttribute("currUserId",null);
	userId = "";
}
%>
<html>
<head>
<title>操作容器工具栏</title>
<link rel="stylesheet" type="text/css" href="../css/toolbar.css">
<%

%>

  <%@ include file="/include/css.jsp"%>
<script language="javascript" src="../scripts/toolbar.js"></script>
<script language="javascript">
var uId = "<%=userId%>";
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

	function bussinessAction(action){	 
			if (action == 0 ) {
				getPropertiesContent().location.href="../schedularmanage/successfulSchedular.jsp" ;	
			}
			if (action == 1 ) {
				getPropertiesContent().location.href="../schedularmanage/arrangeSDList.jsp" ;	
			}
			if (action == 2 ) {
				getPropertiesContent().location.href="../schedularmanage/auditingSDList.jsp" ;	
			}
			if (action == 3 ) {
				getPropertiesContent().location.href="../schedularmanage/noPassList.jsp";	
			}
			if (action == 4 ) {
				getPropertiesContent().location.href="../schedularmanage/refuseArrangeSDList.jsp";	
			}
			if (action == 5 ) {
				getPropertiesContent().location.href="../schedularmanage/noticList.jsp";	
			}
	}
</script>
<style type="text/css">
<!--
.style4 {color: #FF00FF}
.style6 {
    font-size: 12px;
	font-family: Tahoma;
	color: #0C3866;
	text-align: left;
}
-->
</style>
</head>
<body class="toolbarbodymargin" scrolling="no">
<div id="toolbarborder">
<div id="toolbar" ondblclick="switchFrameworks(<%=Framework.SWITCH_WORKSPACE%>,<%=Framework.SWITCH_SCOPE_PERSPECTIVEMAIN%>,document.all.doubleclickcolumn);">
<table width="100%" height=25 cellpadding=0 cellspacing=0 border=0>
	<tr>
		<td valign="middle" align="center" width=25 height=25><img
			class="normal" src="../images/actions.gif"></td>
		<td valign="middle"
			align="left" height=25 nowrap class="text">日程通知</td>
		<td id="doubleclickcolumn" recover="双击恢复" maxtitle="双击最大化" title="双击最大化" valign="middle"
			align="right" width="*" height=25 nowrap class="text">
		<table height=25 cellpadding=0 cellspacing=0 border=0>
			<td class=normal name="tool1" id="tools1" title="待安排的日程" valign=middle align=center onmousedown=mousedown(this) onmouseout=mouseout(this) onmouseover=mouseover(this) onmouseup=mouseup(this) onclick="bussinessAction(1)" height=25>
			<table cellSpacing=0 cellPadding=0 border=0><tr><td valign=center class="style4">&nbsp;<img src="../images/new.gif" alt="待安排的日程" width=16 height=16 border=0 class="normal"></td><td valign=center>&nbsp;<span class="style6">待安排的日程&nbsp;</span></td></tr></table></td>
			<td class=normal name="tool1" id="tools1" title="拒绝安排的日程" valign=middle align=center onmousedown=mousedown(this) onmouseout=mouseout(this) onmouseover=mouseover(this) onmouseup=mouseup(this) onclick="bussinessAction(4)" height=25>
			<table cellSpacing=0 cellPadding=0 border=0><tr><td valign=center class="style4">&nbsp;<img src="../images/new.gif" alt="拒绝安排的日程" width=16 height=16 border=0 class="normal"></td><td valign=center>&nbsp;<span class="style6">拒绝安排的日程&nbsp;</span></td></tr></table></td>
			<td class=normal name="tool1" id="tools1" title="待同意的日程" valign=middle align=center onmousedown=mousedown(this) onmouseout=mouseout(this) onmouseover=mouseover(this) onmouseup=mouseup(this) onclick="bussinessAction(2)" height=25>
			<table cellSpacing=0 cellPadding=0 border=0><tr><td valign=center class="style4">&nbsp;<img src="../images/new.gif" alt="待同意的日程" width=16 height=16 border=0 class="normal"></td><td valign=center>&nbsp;<span class="style6">待同意的日程</span>&nbsp;</td></tr></table></td>
			<td class=normal name="tool1" id="tools1" title="审批未通过的日程" valign=middle align=center onmousedown=mousedown(this) onmouseout=mouseout(this) onmouseover=mouseover(this) onmouseup=mouseup(this) onclick="bussinessAction(3)" height=25>
			<table cellSpacing=0 cellPadding=0 border=0><tr><td valign=center class="style4">&nbsp;<img src="../images/new.gif" alt="审批未通过的日程" width=16 height=16 border=0 class="normal"></td><td valign=center>&nbsp;<span class="style6">审批未通过的日程</span>&nbsp;</td></tr></table></td>
			<td class=normal name="tool1" id="tools1" title="会议或其他通知" valign=middle align=center onmousedown=mousedown(this) onmouseout=mouseout(this) onmouseover=mouseover(this) onmouseup=mouseup(this) onclick="bussinessAction(5)" height=25>
			<table cellSpacing=0 cellPadding=0 border=0><tr><td valign=center class="style4">&nbsp;<img src="../images/new.gif" alt="会议或其他通知" width=16 height=16 border=0 class="normal"></td><td valign=center>&nbsp;<span class="style6">会议或其他通知&nbsp;</span></td></tr></table></td>
			
			
		</table>
		</td>
	</tr>
</table>
</div>
</div>
</body>
</html>

