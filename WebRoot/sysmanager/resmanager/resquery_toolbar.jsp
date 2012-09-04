
<%@page import="com.frameworkset.platform.sysmgrcore.manager.db.OrgAdministratorImpl"%><%@ include file="../include/global1.jsp"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request,response);
	
	//当前用户是否拥有超级管理员权限与部门管理员权限
	boolean isAdminOrOrgManager = false;
	//是否是管理员
	boolean isOrgManager = new OrgAdministratorImpl().isOrgManager(control.getUserID());
	boolean isAdmin = control.isAdmin();
	if(isAdmin || isOrgManager){
		isAdminOrOrgManager = true;
	}
%>
<html>
<head>
<title>操作容器工具栏</title>
<link rel="stylesheet" type="text/css" href="../css/toolbar.css">
<script language="javascript" src="../scripts/toolbar.js"></script>
<script language="javascript">
	function bussinessAction(action){
		if(action == 3&&getPropertiesContent()){
			getPropertiesContent().location.href="../resmanager/special_Frame.jsp";
	    }
		else if(action == 4&&getPropertiesContent()){
			getPropertiesContent().location.href="../resmanager/res_queryframe.jsp";
	    }
	    else if(action == 5&&getPropertiesContent()){
	    	getPropertiesContent().location.href="../resmanager/far-outerquery/res_queryframe.jsp";
	    }
	}
</script>
<style type="text/css">
<!--
.style1 {color: #000000}
-->
</style>
</head>
<%if(isAdminOrOrgManager){ %>
<body class="toolbarbodymargin" onload="bussinessAction(4)">

<div id="toolbarborder">
<div id="toolbar" ondblclick="switchFrameworks(<%=Framework.SWITCH_WORKSPACE%>,<%=Framework.SWITCH_SCOPE_PERSPECTIVEMAIN%>,document.all.doubleclickcolumn);">
<table width="100%" cellpadding=0 cellspacing=0 border=0>
	<tr>
		<td valign="middle" align="center" width=25 ><img
			class="normal" src="../images/actions.gif"></td>
		<td align="left" valign="middle" nowrap class="text">权限查询</td>
		<td id="doubleclickcolumn" recover="双击恢复" maxtitle="双击最大化" title="双击最大化" valign="middle"
			align="right" width="*" nowrap class="text">
		<table cellpadding=0 cellspacing=0 border=0>
			<tr>
			<td class=normal name="tool2" id="tools2" title="特殊资源查询" valign=middle align=center onmousedown=mousedown(this) onmouseout=mouseout(this) onmouseover=mouseover(this) onmouseup=mouseup(this) onclick="bussinessAction(3)"><table border=0 cellPadding=0 cellSpacing=0 class="text">
			  <tr><td valign=center>&nbsp;<img src="../images/new.gif" alt="特殊资源查询" width=16 height=16 border=0 class="normal"></td><td valign=center>&nbsp;特殊资源查询&nbsp;</td></tr></table>
			</td>
			<td class=normal name="tool2" id="tools2" title="普通资源查询" valign=middle align=center onmousedown=mousedown(this) onmouseout=mouseout(this) onmouseover=mouseover(this) onmouseup=mouseup(this) onclick="bussinessAction(4)"><table border=0 cellPadding=0 cellSpacing=0 class="text">
			  <tr><td valign=center>&nbsp;<img src="../images/new.gif" alt="普通资源查询" width=16 height=16 border=0 class="normal"></td><td valign=center>&nbsp;普通资源查询&nbsp;</td></tr></table>
			</td>
			<td class=normal name="tool3" id="tools3" title="资源授予情况查询" valign=middle align=center onmousedown=mousedown(this) onmouseout=mouseout(this) onmouseover=mouseover(this) onmouseup=mouseup(this) onclick="bussinessAction(5)"><table border=0 cellPadding=0 cellSpacing=0 class="text">
			  <tr><td valign=center>&nbsp;<img src="../images/new.gif" alt="资源授予情况查询" width=16 height=16 border=0 class="normal"></td><td valign=center>&nbsp;资源授予情况查询&nbsp;</td></tr></table>
			</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</div>
</div>

</body>
<%}else{ %>
<div align="center">没有权限！请与系统管理员联系</dir>
<%} %>
</html>
