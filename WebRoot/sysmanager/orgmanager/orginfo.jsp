<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@page import="com.frameworkset.util.StringUtil"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<% 

AccessControl accesscontroler = AccessControl.getInstance();
accesscontroler.checkAccess(request, response);

String action = StringUtil.replaceNull(request.getParameter("action"));
//String orgId = StringUtil.replaceNull(request.getParameter("orgId"));
String parentId = StringUtil.replaceNull(request.getParameter("parentId"));
String orgId=request.getParameter("orgId");
String resName = request.getParameter("resName");

%>
<html>
  <head>
  </head>
  	<%
  		if (orgId != null && !orgId.equals("") && accesscontroler.checkPermission(orgId,
		                            "changeorgadmin", AccessControl.ORGUNIT_RESOURCE))
		{
		%>
		<frameset cols="*" rows="58%,*" framespacing="1" frameborder="yes" border="1" >
			<frame src="<%=request.getContextPath()%>/sysmanager/orgmanager/info.jsp?orgId=<%=orgId%>&action=<%=action%>&parentId=<%=parentId%>" name="info" scrolling="no" id="info">  
			<!-- 机构管理不显示用户列表 -->
			<!-- <frame src="<%=request.getContextPath()%>/sysmanager/orgmanager/orgsubuserlist_tab.jsp?orgId=<%=orgId%>&resName=<%=resName%>" name="orgsubuserlist" id="orgsubuserlist" scrolling="no"> -->
			<frame src="<%=request.getContextPath()%>/sysmanager/orgmanager/orgmanagerlist.jsp?orgId=<%=orgId%>" name="orgmanagerlist" id="orgmanagerlist" scrolling="no">
		</frameset>
		<%
		}
		else
		{
			%>
			<frameset cols="*" rows="100%,*" framespacing="1" frameborder="yes" border="1" >
				<frame src="<%=request.getContextPath()%>/sysmanager/orgmanager/info.jsp?orgId=<%=orgId%>&action=<%=action%>&parentId=<%=parentId%>" name="info" scrolling="no" id="info">  
				<!-- 机构管理不显示用户列表 -->
				<!-- <frame src="<%=request.getContextPath()%>/sysmanager/orgmanager/orgsubuserlist_tab.jsp?orgId=<%=orgId%>&resName=<%=resName%>" name="orgsubuserlist" id="orgsubuserlist" scrolling="no"> -->
				<frame src="" name="orgmanagerlist" id="orgmanagerlist" scrolling="no">
			</frameset>
			<%
		}
  	%>
	
  	<body>
	</body>
</html>
