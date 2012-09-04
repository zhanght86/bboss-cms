<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="org.frameworkset.web.servlet.support.RequestContext" %>

<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib uri="/WEB-INF/commontag.tld" prefix="common"%>

<%
	//当前应用上下文，如/creatoresb
	String webAppPath = request.getContextPath();

	//当前应用URL上下文，如http://localhost:8082/creatoresb
	String webAppUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+webAppPath;
	
	pageContext.setAttribute("webAppPath", webAppPath);
	pageContext.setAttribute("webAppUrl", webAppUrl);
%>
