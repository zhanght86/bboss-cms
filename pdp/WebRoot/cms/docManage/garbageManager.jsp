<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ page import="com.frameworkset.platform.security.*"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
%>
<frameset rows="20%,*" border=0>
	<frame src = "garbageQueryFrame.jsp" id="garbageQueryFrame" name="garbageQueryFrame" scrolling="auto" noresize border=0></frame>
	<frame src = "garbageListFrame.jsp" id="garbageListFrame" name="garbageListFrame" scrolling="auto" noresize border=0></frame>
</framset>
