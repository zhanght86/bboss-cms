<%
/**
 * <p>Title: 权限复制</p>
 * <p>Description: 权限复制主页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-3-18
 * @author da.wei
 * @version 1.0
 */
%>

<%     
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0); 
%>

<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.RoleManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Role" %>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>

<%
	AccessControl control = AccessControl.getInstance();
	control.checkManagerAccess(request,response);
	
	String roleId = request.getParameter("roleId");
	RoleManager roleManager = SecurityDatabase.getRoleManager();
	Role role = roleManager.getRoleById(roleId);
	String roleName = role.getRoleName();
	//System.out.println("roleName = " + roleName);
	String copyRestURL = "rolecopy.jsp?roleId="+roleId;
	String copySelfURL = "roleCopySelf.jsp?roleId="+roleId;
%>
<html>
<head>
	<title>角色<%=roleName%>权限复制</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body>
	<div class="mcontent">
	<div style="height: 10px">&nbsp;</div>
	<div class="tabbox" >
		<ul class="tab" id="menu1">
			<li><a href="javascript:void(0)" class="current" onclick="setTab(1,0)"><span><pg:message code="sany.pdp.purviewmanager.rolemanager.role.copy.to.other"/></span></a></li>
			 <li><a href="javascript:void(0)" onclick="setTab(1,1)"><span><pg:message code="sany.pdp.purviewmanager.rolemanager.role.copy.to.this"/></span></a></li>
		</ul>
	</div>
	<div id="main1">
		<ul style="display: block;">
			<jsp:include page="<%=copyRestURL %>"  flush="true"/>
		</ul>
		<ul style="display: none;">
			<iframe id="copySelf" src="<%=copySelfURL %>" frameborder="0" scrolling="no" width="99%" height="540">
		</ul>
	</div>
	
 <iframe name="exeman" width="0" height="0" style="display:none"></iframe>
 
 </div>
</body>
	
</html>

