<%@ include file="../include/global1.jsp"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.User" %>
<%
String userId = request.getParameter("userId");
String qstring = request.getParameter("qstring");
String qqstring = qstring;
if(qstring != null){
	qstring = java.net.URLEncoder.encode(qstring);
}


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


<script language="javascript" src="../scripts/toolbar.js"></script>
<script language="javascript">
var uId = "<%=userId%>";
var qstring="<%=qstring%>";
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
	    if(action == 1 && getPropertiesContent()){
			getPropertiesContent().location.href="../user/userquery_content.jsp?<%=qqstring%>";
	    }
	    else if(action == 2&&getPropertiesContent()){
			getPropertiesContent().location.href="../user/userManager.do?method=getUser&advQuery=1&qstring=<%=qstring%>";
	    }
	}
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
<table width="100%" cellpadding=0 cellspacing=0 border=0>
	<tr>
		<td valign="middle" align="center" width=25 ><img
			class="normal" src="../images/actions.gif"></td>
		<td align="left" valign="middle" nowrap class="text">用户查询</td>
		<td id="doubleclickcolumn" recover="双击恢复" maxtitle="双击最大化" title="双击最大化" valign="middle"
			align="right" width="*" nowrap class="text">
		<table cellpadding=0 cellspacing=0 border=0>
			<!--<td class=normal name="tool1" id="tools1" title="用户查询" valign=middle align=center onmousedown=mousedown(this) onmouseout=mouseout(this) onmouseover=mouseover(this) onmouseup=mouseup(this) onclick="bussinessAction(1)" ><table cellSpacing=0 cellPadding=0 border=0><tr><td valign=center>&nbsp;<img src="../images/new.gif" alt="用户列表" width=16 height=16 border=0 class="normal"></td><td valign=center>&nbsp;用户列表&nbsp;</td></tr></table></td>
			<td class=normal name="tool1" id="tools1" title="用户基本信息" valign=middle align=center onmousedown=mousedown(this) onmouseout=mouseout(this) onmouseover=mouseover(this) onmouseup=mouseup(this) onclick="bussinessAction(2)" ><table cellSpacing=0 cellPadding=0 border=0><tr><td valign=center>&nbsp;<img src="../images/new.gif" alt="用户基本信息" width=16 height=16 border=0 class="normal"></td><td valign=center>&nbsp;用户基本信息&nbsp;</td></tr></table></td>
			-->
		
		</table>
		</td>
	</tr>
</table>
</div>
</div>
</body>
</html>

