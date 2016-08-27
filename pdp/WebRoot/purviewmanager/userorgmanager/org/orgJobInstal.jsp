<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
	<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/include/css.jsp"%>
<link rel="stylesheet" type="text/css" href="../../css/treeview.css">
<script language="javascript" src="../../scripts/dragdiv.js" type="text/javascript"></script>
<script language="JavaScript" src="../../../include/querySelect.js"	type="text/javascript"></script>
<title>orgJobInstal</title>
</head>
<body>
	<table>
		<tr>
			<td width="30%"><fieldset>
				<legend>可选岗位</legend>
				<select name="allist" multiple style="width:98%" onDBLclick="addjob()">
					<pg:list autosort="false">
						
					</pg:list>
				</select>
			</fieldset></td>
			<td width="20%">中间按钮</td>
			<td width="30%"><fieldset><legend>可选岗位</legend></fieldset></td>
			<td width="20%">右边按钮</td>
		</tr>
	</table>
</body>
</html>