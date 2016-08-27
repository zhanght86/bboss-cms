<%@page contentType="text/html; charset=UTF-8"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.RoleManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Role" %>
<%
	AccessControl control = AccessControl.getInstance();
	control.checkManagerAccess(request,response);
	
	String roleId = request.getParameter("roleId");
	RoleManager roleManager = SecurityDatabase.getRoleManager();
	Role role = roleManager.getRoleById(roleId);
	String roleName = role.getRoleName();
	//System.out.println("roleName = " + roleName);
%>
<html>
<head>
	<title>角色<%=roleName%>权限复制</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<tab:tabConfig/>
</head>
<body  bgcolor="#F7F8FC">
	<table width="100%" border="0" cellpadding="0" cellspacing="0" align="center">
		<tr>
			<td colspan="2">
				<tab:tabContainer id="purviewCopy" selectedTabPaneId="purview-copy">
					<tab:tabPane id="copy-rest" tabTitle="该角色权限复制给其他角色" lazeload="true">
						<tab:iframe id="copyRest" src="" frameborder="0" scrolling="yes" width="99%" height="540">
						</tab:iframe>
					</tab:tabPane>
					<tab:tabPane id="copy-self" tabTitle="其他角色的权限复制给该角色" lazeload="true">
						<tab:iframe id="copySelf" src="" frameborder="0" scrolling="yes" width="99%" height="540">
						</tab:iframe>
					</tab:tabPane>
				</tab:tabContainer>			
			</td>
		</tr>
  </table>	

<iframe name="exeman" width="0" height="0" style="display:none"></iframe>
</body>
<script language="javascript">
    window.onload=function init(){
        var path = "rolecopy.jsp?roleId=<%=roleId%>";
        document.all("copyRest").src = path;
        var path = "roleCopySelf.jsp?roleId=<%=roleId%>";
        document.all("copySelf").src = path;
    }
</script>
</html>

