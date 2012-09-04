<%@ page session="false" contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>
<%@page import="com.frameworkset.platform.remote.Utils"%>
<%@page import="org.jgroups.stack.IpAddress"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<html>
  <head>
    <base href="<%=basePath%>">
    <link rel="stylesheet" type="text/css" href="sysmanager/css/windows.css">
    <title>所有服务器状态信息</title>

	<script type="text/javascript" languge="Javascript">
	function flushButton(){
		document.location = document.location;
	}
	
	window.setInterval("flushButton()", 30000);
	</script>

  </head>
  
  <body class="contentbodymargin" onload="" scroll="no">
	<div style="width:100%;height:100%;overflow:auto">
	<form  name="LogForm"  method="post">
	<table width="100%" height="" border="0" cellpadding="0" cellspacing="1" class="thin">
	<caption>监控所有服务器状态监控<div align="right" ><input type="button" value="刷新页面" class="input" onclick="flushButton()"></div></caption>
	<tr>
	<th>服务器名</th>
	<th>服务状态</th>
	</tr>
	<% 
		Vector servers = Utils.getAppservers();
		int serverC = 1;
		for(int i = 0; i < servers.size(); i++){
			IpAddress ipAddress = (IpAddress)servers.get(i);
			String ipPort = ipAddress.toString();
	%>
	<tr>
		<td><strong>server<%=serverC++%>-----<%=ipPort %></strong></td>
		<td>OK</td>
	</tr>
	<% 
		}
	%>
	</table>
	</form>
	</div>
	
	
  </body>
</html>

