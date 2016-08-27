<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ page import="com.frameworkset.platform.security.*"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
%>
<frameset rows="20%,*" border=0>
	<frame src = "pigeonholeQueryFrame.jsp?operate=pigeonholeQuery" id="pigeonholeQueryFrame" name="pigeonholeQueryFrame" scrolling="auto" noresize border=0></frame>
	<frame src = "pigeonholeListFrame.jsp?operate=pigeonholeQuery" id="pigeonholeListFrame" name="pigeonholeListFrame" scrolling="auto" noresize border=0></frame>
</framset>