<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<%
	String userName = request.getParameter("userName");
%>
<html>
<head>
<%@ include file="/include/css.jsp"%>
</head>
<table width="100%" height="25" border="0" cellpadding="0" cellspacing="1" class="thin">
<tr>
<td height="25" class="detailtitle" width="23%">自动生成的登陆名为：<%=userName %></td>
</tr>
<tr>
<td align="right"><input name="co" value="关闭" type="button" onclick="window.close();" class="input"/></td>
</tr>
</table>
</html>
