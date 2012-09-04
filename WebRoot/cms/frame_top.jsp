<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.frameworkset.platform.security.AccessControl,java.util.List"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.SiteManager,
com.frameworkset.platform.cms.sitemanager.SiteManagerImpl,com.frameworkset.platform.cms.sitemanager.Site"%>
<%
	 AccessControl accesscontroler = AccessControl.getInstance();
     accesscontroler.checkAccess(request,response);
	 String modulePath = request.getParameter("path");
	 String temp[]=modulePath.split("=");
	
	 request.setAttribute("modulePath",temp[1]);
%>
    <script language="javascript">
   		window.parent.location.reload();
     </script>

