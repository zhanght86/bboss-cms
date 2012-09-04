<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<%
	com.frameworkset.platform.security.AccessControl accesscontroler = com.frameworkset.platform.security.AccessControl.getAccessControl();
	
	if (!accesscontroler.checkAccess(request, response)){
		return;
	}
	boolean hasaddpermission = accesscontroler.checkPermission("test","add","testresource");
	boolean hasupdatepermission = accesscontroler.checkPermission("test","write","testresource");
	boolean hasdeletepermission = accesscontroler.checkPermission("test","delete","testresource");
	boolean hasauditpermission = accesscontroler.checkPermission("test","read","testresource");
	
%>

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>权限测试页面</title>
		<%@ include file="/common/jsp/css.jsp"%>
	</head>
	<body>
		<div class="form_box">
			<form id="addForm" name="addForm" method="post">
			<!--  class="collapsible"  收缩 -->
			<fieldset>
				<legend>自定义资源测试</legend>
				<table border="0" cellpadding="0" cellspacing="0" class="table4">
					
					<tr>
						<th>
							hasaddpermission：<%=hasaddpermission %>
						</th>
						
					</tr>
					
					<tr>
						
						
						<th>
							hasupdatepermission：<%=hasupdatepermission %>
						</th>
					</tr>
		
					<tr>
						
						<td colspan="7">
							hasdeletepermission：<%=hasdeletepermission %>
						</td>
					</tr>
				    <tr>
						
						<td colspan="7">
							hasauditpermission：<%=hasauditpermission %>
						</td>
					</tr>
				</table>
			</fieldset>
			</form>
		</div>
	</body>