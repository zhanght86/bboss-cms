<%@page import="org.jboss.netty.handler.codec.http.HttpRequest"%>
<%@ page session="true" language="java"
	contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/sanymbp/common/jsp/taglibs.jsp"%>

<!DOCTYPE html>
<html>
<head>
<title>移动应用平台</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<cm:request setAttribute="pagePath" value="/sanymbp/login/login"/>
 
<%@ include file="/sanymbp/common/jsp/head.jsp"%>
<script src="${ctx}/sanymbp/common/js/jquery.validate.js"></script>
<script src="${ctx}/sanymbp/common/js/jquery.metadata.js"></script>
<script src="${ctx}/sanymbp/common/js/messages_zh_CN.js"></script>
<script src="${ctx}/sanymbp/common/js/urchin.js"></script>

</head>

<body>
	<div data-role="page" data-theme="c">
		<h1 class="logo">
			<img src="${ctx}/sanymbp/common/images/logo.jpg" class="logo"><span
				class="logotext">移动应用平台</span>
		</h1>
		<div data-role="content">
			<form data-ajax="false" method="post" id="loginform"
				action="${ctx}/sanymbp/login.page">
				<input type="hidden" name="subsystem_id" id="subsystem_id"
					value="mbp" />
				<div data-role="fieldcontain" class="pt30">
					<label for="userName">用户名：</label> <input type="text"
						name="userName" id="userName" value="" placeholder="用户名"
						class="required" />
				</div>
				<div data-role="fieldcontain" class="pt30">
					<label for="password">密&nbsp;&nbsp;码:</label> <input id="password"
						name="password" value="" type="password" class="required" />
				</div>
				<%
					if (null != (String) request.getAttribute("message")) {
				%>
				<p><%=(String) request.getAttribute("message")%></p>
				<%
					}
				%>
				<div class="pt30 tcenter">
					<a data-role="button" id="loginsub" data-inline="true"
						data-transition="fade" data-theme="b">登 录</a> <a
						data-role="button" data-inline="true" data-transition="fade"
						data-theme="c" href="page1">取 消</a>
				</div>
			</form>
		</div>
		
		<div data-theme="c" data-role="footer" data-position="fixed">
			<h1 style="text-align: right">
				<img src="${ctx}/sanymbp/common/images/isany.gif">
			</h1>
		</div>
	</div>

</body>
</html>

