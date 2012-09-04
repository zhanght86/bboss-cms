<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%
String id = request.getParameter("ID");
 %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>应用系统信息</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
<%@ include file="/include/css.jsp"%>
		<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
		<script language="javascript" type="text/javascript">
		function getvalue() {
			var A_APP_ID = document.frm.A_APP_ID.value.replace(/ /g,"");
			var A_USER_NAME = document.frm.A_USER_NAME.value.replace(/ /g,"");
			var B_APP_ID = document.frm.B_APP_ID.value.replace(/ /g,"");
			
			if (A_APP_ID == "" || A_APP_ID == null) {
				alert("请输入应用系统A名称！");
				document.frm.A_APP_ID.focus();
				return false;
			}
			if (A_USER_NAME == "" || A_USER_NAME == null) {
				alert("请输入A系统用户名！");
				document.frm.A_USER_NAME.focus();
				return false;
			}
			if (B_APP_ID == "" || B_APP_ID == null) {
				alert("请输入应用系统B名字！");
				document.frm.B_APP_ID.focus();
				return false;
			}
			frm.action="sso-modify-do.jsp";
			frm.submit();
		}
	</script>
	</head>
	<body>
		<form name="frm" action="">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" class="thin">
				<tr>
					<td align="center" valign="middle" height="50">
						应用系统详细信息
					</td>
				</tr>
			</table>
			<input type="hidden" name="pID" value="<%=id%>" />
			<pg:listdata
				dataInfo="com.liferay.portlet.iframe.action.UserMappingList"
				keyName="UserMappingList" />
			<!--分页显示开始,分页标签初始化-->
			<pg:pager maxPageItems="1" scope="request" data="UserMappingList"
				isList="true">
				<!--list标签循环输出每条记录-->
				<pg:list>
					<table width="100%" border="0" cellspacing="0" cellpadding="0" class="thin">
						<tr>
							<td width="15%" valign="middle" align="right" height="25">
								应用系统A:
							</td>
							<td>
								<input type="text" name="A_APP_ID" value="<pg:cell colName="A_APP_ID" defaultValue="" />" size="100">
							</td>
						</tr>
						<tr>
							<td valign="middle" align="right" height="25">
								A系统用户名:
							</td>
							<td>
								<input type="text" name="A_USER_NAME" value="<pg:cell colName="A_USER_NAME" defaultValue="" />" size="100">
							</td>
						</tr>
						<tr>
							<td valign="middle" align="right" height="25">
								应用系统B:
							</td>
							<td>
								<input type="text" name="B_APP_ID" value="<pg:cell colName="B_APP_ID" defaultValue="" />" size="100">
							</td>
						</tr>
						<tr>
							<td valign="middle" align="right" height="25">
								B用户名:
							</td>
							<td>
								<input type="text" name="B_USER_NAME" value="<pg:cell colName="B_USER_NAME" defaultValue="" />" size="100">
							</td>
						</tr>
						<tr>
							<td valign="middle" align="right" height="25">
								B用户密码:
							</td>
							<td>
								<input type="text" name="B_USER_PASSWORD" value="<pg:cell colName="B_USER_PASSWORD" defaultValue="" />" size="100">
							</td>
						</tr>
						<tr>
							<td valign="middle" align="right" height="25">
								备 注:
							</td>
							<td>
								<input type="text" name="USER_REMARK" value="<pg:cell colName="USER_REMARK" defaultValue="备注未填" />" size="100">
							</td>
						</tr>
					</table>
				</pg:list>
			</pg:pager>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td valign="middle" height="30" align="right" width="45%">
						<input name="save" type="button" onClick="getvalue()"
							class="input" value="保存" >
					</td>
					<td>
						&nbsp;
					</td>
					<td valign="middle" height="30" align="left" width="45%">
						<input name="Cancel" type="button" id="Cancel"
							onClick="history.back();" value="取 消" class="input">
					</td>
				</tr>
			</table>
		</form>
	</body>
</html>

