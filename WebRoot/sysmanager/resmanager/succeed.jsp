<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ include file="../include/global1.jsp"%>
<%@page import="com.frameworkset.util.StringUtil"%>
<%
String restypeId = StringUtil.replaceNull(request.getParameter("restypeId"));
String restypeName = StringUtil.replaceNull(request.getParameter("restypeName"));
%>   
<html>
<head>
<script language="JavaScript">
function refreshtree(){
	getNavigatorContent().location.href ="<%=rootpath%>/sysmanager/resmanager/navigator_content.jsp?anchor=0&expand=0&request_scope=session&selectedNode=<%=restypeId%>";
}
</script>
</head>
<body class="contentbodymargin" onLoad="refreshtree()" scroll="no">
</body>
</html>
<script language="JavaScript">
alert("操作资源信息成功！");
getNavigatorContent().location.href ="../sysmanager/resmanager/navigator_content.jsp?expand=0&request_scope=session&selectedNode=<%=restypeId%>";
location.href ="../sysmanager/resmanager/reslist.jsp?restypeId=<%=restypeId%>&restypeName=<%=restypeName%>";
</script>
