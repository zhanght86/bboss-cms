<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.cms.templatemanager.*"%>
<%@ page import="com.frameworkset.platform.cms.container.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="java.util.*"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 00px;
	margin-right: 0px;
	margin-bottom: 0px;
}
body,td,th {
	font-family: Verdana, 宋体;
	font-size: 12px;
}
-->
</style></head>
<body>
<form name="form1" id="form1" action="">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td width="80" align="right" valign="middle">
			 文件名:
		</td>
		<td>
			<input name="fileName" type="text" class="cms_text" id="fileName" value="qw" readonly="true">
		</td>
	</tr>
	<tr>
		<td width="80" align="right" valign="middle">
			文件路径:
		</td>
		<td>
			<input name="uri" type="text" class="cms_text" id="uri" value="qq" readonly="true">
		</td>
	</tr>
	<tr>
		<td width="80" align="right" valign="middle">
			模板名称:
		</td>
		<td>
			<input name="templateName" type="text" class="cms_text" id="templateName" size="20" maxlength="40">
			<span class="red_star">*</span>
		</td>
	</tr>
	<tr>
		<td width="80" align="right" valign="middle">
			模板类别:
		</td>
		<td><select name="templateType" class="cms_select" id="templateType">
				<option value="0">首页</option>
				<option value="1" selected>概览</option>
				<option value="2">细览</option>
			</select>
		</td>
	</tr>
	<tr>
		<td width="80" align="right" valign="middle">
			模板描述:
		</td>
		<td>
			<input name="templateDesc" type="text" class="cms_text" id="templateDesc" size="30" maxlength="100">
			<span class="red_star">*</span>
		</td>
	</tr>
</table>
</form>
</body>
</html>