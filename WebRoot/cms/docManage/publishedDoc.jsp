<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ page import="com.frameworkset.platform.security.*"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
%>
<html>
<frameset rows="28%,*" border=0>
	<frame src="pigeonholeQueryFrame.jsp?operate=toPigeonhole" id="pigeonholeQueryFrame" name="pigeonholeQueryFrame" scrolling="auto" noresize  marginwidth="0" frameborder=0></frame>
	<frame src = "publishedDocListFrame.jsp?operate=toPigeonhole" id="pigeonholeListFrame" name="pigeonholeListFrame" scrolling="auto" noresize marginwidth="0" frameborder=0></frame>
</frameset><noframes></noframes>
</html>