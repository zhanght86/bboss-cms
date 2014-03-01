<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<%
	com.frameworkset.platform.security.AccessControl accesscontroler = com.frameworkset.platform.security.AccessControl.getAccessControl();
	//com.frameworkset.platform.security.AccessControl accesscontroler = com.frameworkset.platform.security.AccessControl.getInstance();
	
	//if (!accesscontroler.checkAccess(request, response)){
	//	return;
	//}
	boolean hasaddpermission = accesscontroler.checkPermission("testid","add","testresource");
	boolean hasupdatepermission = accesscontroler.checkPermission("testid","write","testresource");
	boolean hasdeletepermission = accesscontroler.checkPermission("testid","delete","testresource");
	boolean hasreadpermission = accesscontroler.checkPermission("testid","read","testresource");
	boolean hasglobaltestreadpermission = accesscontroler.checkPermission("globaltest","read","testresource");
	boolean hasglobaltestdeletepermission = accesscontroler.checkPermission("globaltest","delete","testresource");
	
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
							has add testid permission：<%=hasaddpermission %>
						</th>
						
					</tr>
					
					<tr>
						
						
						<th>
						has update testid permission：<%=hasupdatepermission %>
						</th>
					</tr>
		
					<tr>
						
						<td colspan="7">
						has delete testid permission：<%=hasdeletepermission %>
						</td>
					</tr>
				    <tr>
						
						<td colspan="7">
							has read testid permission：<%=hasreadpermission %> <a href="<%=request.getContextPath() %>/test/testresopurlpermissionread.jsp" target="_blank">测试url控制</a>
						</td>
					</tr>
					<tr>
						
						<td colspan="7">
							has globaltest read permission：<%=hasglobaltestreadpermission %> <a href="<%=request.getContextPath() %>/test/testopurlpermissionread.jsp" target="_blank">测试url控制</a>
						</td>
					</tr>
				    <tr>
						
						<td colspan="7">
							has globaltest delete permission：<%=hasglobaltestdeletepermission %> <a href="<%=request.getContextPath() %>/test/testopurlpermissiondelete.jsp"  target="_blank">测试参数动态url权限控制（不传orgCode参数）</a>
							<a href="<%=request.getContextPath() %>/test/testopurlpermissiondelete.jsp?orgCode=globaltest"  target="_blank">测试参数动态url权限控制（传orgCode=globaltest参数）</a>
						</td>
					</tr>
				</table>
			</fieldset>
			</form>
		</div>
	</body>