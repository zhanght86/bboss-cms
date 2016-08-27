<%@ include file="../include/global1.jsp"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%
String orgId = request.getParameter("orgId");
String parentId = request.getParameter("parentId");
String state = request.getParameter("state");
if(parentId==null || parentId.equals(""))
{
	parentId = "0";
}
%>
<html>
<head>
<title>操作容器工具栏</title>
   <script language="JavaScript" src="../scripts/common.js" type="text/javascript"></script>
	<%@ include file="/include/css.jsp"%>
	<link rel="stylesheet" type="text/css" href="../css/contentpage.css">
	<link rel="stylesheet" type="text/css" href="../css/tab.winclassic.css">
<%@ include file="/include/css.jsp"%>
	<link rel="stylesheet" type="text/css" href="../sysmanager/css/contentpage.css">
	<link rel="stylesheet" type="text/css" href="../sysmanager/css/tab.winclassic.css">
	
	<%
	if(state != null && "false".equals(state)){
	%>
	<script type="text/javascript">
	alert("删除机构失败！该机构或该机构的子机构可能与字典数据外键相关联。");
	getPropertiesContent().location.href ="<%=rootpath%>/sysmanager/orgmanager/organsearch.jsp";
	getNavigatorContent().location.href ="<%=rootpath%>/sysmanager/orgmanager/navigator_content.jsp?anchor=0&expand=0&request_scope=session";
	</script>
	<%	
	}else if(parentId.equals("0"))
	{
	%>
	<script language="JavaScript">
	alert("操作机构信息成功！");
	getPropertiesContent().location.href ="<%=rootpath%>/sysmanager/orgmanager/organsearch.jsp";
	getNavigatorContent().location.href ="<%=rootpath%>/sysmanager/orgmanager/navigator_content.jsp?anchor=0&expand=0&request_scope=session"; 
	</script>
	<%
	}
	else
	{
	%>
	<script language="JavaScript">
	alert("操作机构信息成功！");
	getPropertiesContent().location.href ="<%=rootpath%>/sysmanager/orgmanager/organsearch.jsp";
	getNavigatorContent().location.href ="<%=rootpath%>/sysmanager/orgmanager/navigator_content.jsp?anchor=<%=parentId%>&expand=<%=parentId%>&request_scope=request&selectedNode=<%=orgId%>"; 
	getNavigatorContent().location.href ="<%=rootpath%>/sysmanager/orgmanager/navigator_content.jsp?anchor=<%=parentId%>&collapse=<%=parentId%>&request_scope=request&selectedNode=<%=orgId%>";
	</script>
	<%
	}
	%>
</head>
<body class="toolbarbodymargin" onLoad="">
<!-- <div id="toolbarborder" align="center"> -->
<!-- 操作机构信息成功 -->
<!-- </div> -->
<iframe src="" id="abc" name="abc" style="width:0;height:0">
</body>
</html>
