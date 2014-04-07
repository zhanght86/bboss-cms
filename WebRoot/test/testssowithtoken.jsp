<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page session="true" language="java"
	contentType="text/html; charset=utf-8"%>

<%


out.println("isGuest:"+AccessControl.getAccessControl().isGuest());
out.println("userAccount:"+AccessControl.getAccessControl().getUserAccount());

 %>
<html>
<body>
<table>
<tr><td>账号登录</td></tr>
<tr><td><a target="_blank" href="<%=request.getContextPath() %>/sso/ssowithtoken.page?userId=ab09a48ac5c29d58&loginType=1&loginMenu=appbommanagermodule&subsystem_id=module">创建领料单</a></td></tr>
<tr><td><a target="_blank" href="<%=request.getContextPath() %>/sso/ssowithtoken.page?userId=3329b804a8149fc1&loginType=1&loginMenu=xxx&subsystem_id=module">创建退料单</a></td></tr>
<tr><td><a target="_blank" href="<%=request.getContextPath() %>/sso/ssowithtoken.page?userId=3329b804a8149fc1&loginType=1&loginMenu=ccc&subsystem_id=module">工单管理</a></td></tr>
<tr><td><a target="_blank" href="<%=request.getContextPath() %>/sso/ssowithtoken.page?userId=3329b804a8149fc1&loginType=1&loginMenu=cccvvv&subsystem_id=module">台账查询</a></td></tr>
</table>
<table>
<tr><td>账号登录-直接跳转到指定页面</td></tr>
<tr><td><a target="_blank" href="<%=request.getContextPath() %>/sso/ssowithtoken.page?userId=ab09a48ac5c29d58&loginType=1&successRedirect=<%=URLEncoder.encode("/appbom/aaa.page?a=b&c=d") %>&subsystem_id=module">创建领料单</a></td></tr>
<tr><td><a target="_blank" href="<%=request.getContextPath() %>/sso/ssowithtoken.page?userId=3329b804a8149fc1&loginType=1&successRedirect=<%=URLEncoder.encode("/appbom/aaa.page?a=b&c=d") %>&subsystem_id=module">创建退料单</a></td></tr>
<tr><td><a target="_blank" href="<%=request.getContextPath() %>/sso/ssowithtoken.page?userId=3329b804a8149fc1&loginType=1&successRedirect=<%=URLEncoder.encode("/appbom/aaa.page?a=b&c=d") %>&subsystem_id=module">工单管理</a></td></tr>
<tr><td><a target="_blank" href="<%=request.getContextPath() %>/sso/ssowithtoken.page?userId=3329b804a8149fc1&loginType=1&successRedirect=<%=URLEncoder.encode("/appbom/aaa.page?a=b&c=d") %>&subsystem_id=module">台账查询</a></td></tr>
</table>
<table>
<tr><td>工号登录</td></tr>
<tr><td><a target="_blank" href="<%=request.getContextPath() %>/sso/ssowithtoken.page?userId=bbcf7e801cdb8a4b05c04cf6bda98b74&loginType=2&loginMenu=fff&subsystem_id=module">创建领料单</a></td></tr>
<tr><td><a target="_blank" href="<%=request.getContextPath() %>/sso/ssowithtoken.page?userId=bbcf7e801cdb8a4b05c04cf6bda98b74&loginType=2&loginMenu=vvbb&subsystem_id=module">创建退料单</a></td></tr>
<tr><td><a target="_blank" href="<%=request.getContextPath() %>/sso/ssowithtoken.page?userId=bbcf7e801cdb8a4b05c04cf6bda98b74&loginType=2&loginMenu=bbb&subsystem_id=module">工单管理</a></td></tr>
<tr><td><a target="_blank" href="<%=request.getContextPath() %>/sso/ssowithtoken.page?userId=bbcf7e801cdb8a4b05c04cf6bda98b74&loginType=2&loginMenu=ddddd&subsystem_id=module">台账查询</a></td></tr>
</table>

<table>
<tr><td>工号登录-直接跳转到指定页面</td></tr>
<tr><td><a target="_blank" href="<%=request.getContextPath() %>/sso/ssowithtoken.page?userId=bbcf7e801cdb8a4b05c04cf6bda98b74&loginType=2&successRedirect=<%=URLEncoder.encode("/appbom/aaa.page?a=b&c=d") %>&subsystem_id=module">创建领料单</a></td></tr>
<tr><td><a target="_blank" href="<%=request.getContextPath() %>/sso/ssowithtoken.page?userId=bbcf7e801cdb8a4b05c04cf6bda98b74&loginType=2&successRedirect=<%=URLEncoder.encode("/appbom/aaa.page?a=b&c=d") %>&subsystem_id=module">创建退料单</a></td></tr>
<tr><td><a target="_blank" href="<%=request.getContextPath() %>/sso/ssowithtoken.page?userId=bbcf7e801cdb8a4b05c04cf6bda98b74&loginType=2&successRedirect=<%=URLEncoder.encode("/appbom/aaa.page?a=b&c=d") %>&subsystem_id=module">工单管理</a></td></tr>
<tr><td><a target="_blank" href="<%=request.getContextPath() %>/sso/ssowithtoken.page?userId=bbcf7e801cdb8a4b05c04cf6bda98b74&loginType=2&successRedirect=<%=URLEncoder.encode("/appbom/aaa.page?a=b&c=d") %>&subsystem_id=module">台账查询</a></td></tr>
</table>
</body>
</html>