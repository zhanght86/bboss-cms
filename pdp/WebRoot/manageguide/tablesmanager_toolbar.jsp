<%@ include file="../sysmanager/include/global1.jsp"%>
<%@ include file="..//sysmanager/base/scripts/panes.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>

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
<link rel="stylesheet" type="text/css" href="../sysmanager/css/toolbar.css">
<%

%>


<script language="javascript" src="../sysmanager/scripts/toolbar.js"></script>
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
<div id="toolbar" onDblClick="switchFrameworks(<%=Framework.SWITCH_WORKSPACE%>,<%=Framework.SWITCH_SCOPE_PERSPECTIVEMAIN%>,document.all.doubleclickcolumn);">
<table width="100%" cellpadding=0 cellspacing=0 border=0>
	<tr>
		<td valign="middle" align="center" width=25 ><img
			class="normal" src="../sysmanager/images/actions.gif"></td>
		<td align="left" valign="middle" nowrap class="text">管控指标</td>
		<td id="doubleclickcolumn" recover="双击恢复" maxtitle="双击最大化" title="双击最大化" valign="middle"
			align="right" width="*" nowrap class="text">
		<table cellpadding=0 cellspacing=0 border=0>
			
		</table>
		</td>
	</tr>
</table>
</div>
</div>
</body>
</html>

