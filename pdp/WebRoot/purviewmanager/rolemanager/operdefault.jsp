<%
/*
 * <p>Title: 信息提示页面</p>
 * <p>Description: 信息提示页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-3-19
 * @author liangbing.tao
 * @version 1.0
 */
%>

<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>

<%
	AccessControl control = AccessControl.getInstance();
	control.checkManagerAccess(request,response);
%>

<html>
	<head>
		<title>属性容器</title>
<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../css/treeview.css">
	</head>
	<body class="contentbodymargin">
		<div id="contentborder">
			<center>
				<br>
				<table>
					<tr>
						<td class="td">
							请在左边选择相应资源进行操作
						<td>
					<tr>
				</table>
			</center>
		</div>
	</body>
</html>

