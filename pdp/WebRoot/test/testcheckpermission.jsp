<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>

<%
	com.frameworkset.platform.security.AccessControl accesscontroler = com.frameworkset.platform.security.AccessControl.getAccessControl();	//获取平台当前用户会话对象
	boolean hasaddpermission = accesscontroler.checkPermission("testid",//资源id
																"add",//资源操作
																"testresource"//资源类型
																);
	boolean hasupdatepermission = accesscontroler.checkPermission("testid","write","testresource");
	boolean hasdeletepermission = accesscontroler.checkPermission("testid","delete","testresource");
	boolean hasreadpermission = accesscontroler.checkPermission("testid","read","testresource");
	boolean hasglobaltestreadpermission = accesscontroler.checkPermission("globaltest","read","testresource");
	boolean hasglobaltestdeletepermission = accesscontroler.checkPermission("globaltest","delete","testresource");
	boolean isadmin = accesscontroler.isAdmin();//判断用户是否是超级管理员
	java.util.Map<String,java.util.List<String>> permissions = accesscontroler.getResourcePermissions(accesscontroler, "testresource");
	out.println(permissions);
%>
<pg:true actual="<%=hasaddpermission %>">
	true do something here...
</pg:true>

<pg:false actual="<%=hasaddpermission %>">
	false do something here...
</pg:false>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>权限测试页面</title>
		<%@ include file="/common/jsp/css-lhgdialog.jsp"%>
		<script language="javascript">
		function selectUser()
		{
			var url="<%=request.getContextPath()%>/purviewmanager/common/selectuser.jsp?loginName=userName";
			$.dialog({title:'<pg:message code="sany.pdp.personcenter.person.select"/>',width:1150,height:650, content:'url:'+url,currentwindow:this}); 
		}
		</script>
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
						<pg:true actual="<%=hasdeletepermission %>">
							<a href="javascript:void(0)"  onclick="delteUse();">删除用户</a>
						</pg:true>
						<pg:false actual="<%=hasdeletepermission %>">
							没有删除用户的权限
						</pg:false>
						    has delete testid permission：<%=hasdeletepermission %>   code parameter value:null<a href="<%=request.getContextPath() %>/test/testresopurlpermissionread.jsp?opCode=testid" target="_blank">测试url控制1</a><br>
							has write testid permission：<%=hasupdatepermission %>   code parameter value:bbb<a href="<%=request.getContextPath() %>/test/testresopurlpermissionread.jsp?opCode=testid&code=bbb" target="_blank">测试url控制2</a><br>
							has add testid permission：<%=hasaddpermission %>   code parameter value:ccc<a href="<%=request.getContextPath() %>/test/testresopurlpermissionread.jsp?opCode=testid&code=ccc" target="_blank">测试url控制3</a><br>
							has read testid permission：<%=hasreadpermission %>   code parameter value:aaa<a href="<%=request.getContextPath() %>/test/testresopurlpermissionread.jsp?opCode=testid&code=aaa" target="_blank">测试url控制4</a>
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
					
					<tr>
						
						<td colspan="7">
							选择用户：<input type="text" value="yinbp" name="userName" id="userName"> 
							<a href="javascript:void(0)"  onclick="selectUser();">选择</a>
						</td>
					</tr>
					
					<tr>
						
						<td colspan="7">
							
							<a href="<%=request.getContextPath() %>/test/testssowithtoken.jsp"  target="_blank""  >token单点登录测试</a>
						</td>
					</tr>
					<tr>
						
						<td colspan="7">
							
							<a href="<%=request.getContextPath() %>/test/sessiontest.jsp"  target="_blank""  >session共享测试</a>
						</td>
					</tr>
					
					<tr>
						
						<td >							
							<a href="http://testpdp.sany.com.cn:8080/WebRoot/sanydesktop/frame.page?sany_menupath=module::menu://sysmenu$root/sysmanager$module/sessioncontrol$item"  target="frame_blank""  >session共享单点登录测试(不带top)</a>
						</td>
						<td >							
							<a href="http://testpdp.sany.com.cn:8080/WebRoot/sanydesktop/index.page?sany_menupath=module::menu://sysmenu$root/sysmanager$module/sessioncontrol$item"  target="top_blank""  >session共享单点登录测试(带top)</a>
						</td>
						
						<td >							
							<a href="http://test.sany.com.cn:8080/sanydesktop/indexcommon.page"  target="top_blank""  >session共享单点登录测试(无上下文)</a>
						</td>
						
						<td >							
							<a href="<%=request.getContextPath() %>/sanydesktop/index.page?sany_menupath=module::menu://sysmenu$root/sysmanager$module/sessioncontrol$item"  target="top_blank""  >session共享单点登录测试(带top，本机)</a>
						</td>
						<td >							
							<a href="<%=request.getContextPath() %>test/svg/wf.html"  target="top_blank""  >wf.html</a>
						</td>
						
						
						
						
					</tr>
					
					
						<tr>
						
						<td >							
							字典标签国际化测试
						</td>
						
						<td colspan="7">							
							<dict:select type="sex" name="userSex" textValueCode="sany.pdp.common.operation.select" textNAN="-1"  />
						</td>
						
					</tr>
					
					<tr>
						
						<td >							
							in标签类型匹配测试 int i = 10; in 1,2,10,20
							<%request.setAttribute("i", 10); %>
						</td>
						
						<td colspan="7">							
							<pg:in requestKey="i" scope="1,2,10,20">in标签类型匹配测试 int i = 10; in 1,2,10,20: true</pg:in>
							<pg:notin requestKey="i" scope="1,2,10,20">in标签类型匹配测试 int i = 10; in 1,2,10,20: false</pg:notin>
						</td>
						
					</tr>
					
				</table>
			</fieldset>
			</form>
		</div>
	</body>