<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.AccessControl,
				com.frameworkset.platform.framework.MenuHelper"%>
<%@page import="com.frameworkset.platform.portal.PortalUtil"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request,response);
	String contextPath = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+contextPath+"/";
	String i = request.getParameter("i");
	String areaType = request.getParameter("issueArea"+i);
	String menuPath = request.getParameter("appModuleSrc"+i);
	String moduleName = request.getParameter("moduleName"+i);
	String url = PortalUtil.getRealMenuPath(contextPath, menuPath, areaType);
	System.out.println(areaType + " = " + url);
%>

<html>

<head><title>模块区域预览</title></head>

<body>
<form action="" method="post">
<table>

</table>
<table >
<tr>
<td>
模块[<%=moduleName %>]的[<%=areaType %>]区域预览
</td>
</tr>
<tr>

<td>
<iframe src="<%=url %>" height="600" width="1024"></iframe>
</td>
</tr>
</table>
</form>
</body>

</html>
