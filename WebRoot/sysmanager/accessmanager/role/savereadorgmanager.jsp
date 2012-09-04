<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<%@ page import="com.frameworkset.platform.security.AccessControl" %>
<%@page import="com.frameworkset.platform.sysmgrcore.purviewmanager.db.PurviewManagerImpl"%>

<%
	AccessControl accessControl = AccessControl.getInstance();
	accessControl.checkManagerAccess(request,response);
	
	String resTypeId = request.getParameter("resTypeId");
	String resid = request.getParameter("resid");
	String roleid = request.getParameter("roleid");
	String role_type = request.getParameter("role_type");
	
	String orgIds = request.getParameter("orgIds");
	
	String[] alloper = request.getParameterValues("alloper");
	
	boolean state = false;
	if(alloper == null){
		state = new PurviewManagerImpl().saveReadOrgmanager(orgIds.split(","),roleid.split(","),resTypeId,role_type,false);
	}else{
		state = new PurviewManagerImpl().saveReadOrgmanager(orgIds.split(","),roleid.split(","),resTypeId,role_type,true);
	}
	
	if(state){
%>
<script language="JavaScript">
	alert("操作授予成功！");
	//parent.parent.orgTree.resource_bridge.location = parent.parent.orgTree.resource_bridge.location;
</script>
<% }else{%>
<script language="JavaScript">
	alert("操作授予失败！");
</script>
<%}%>
