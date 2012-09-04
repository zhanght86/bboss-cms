<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl,com.frameworkset.platform.sysmgrcore.manager.*,
com.frameworkset.platform.sysmgrcore.entity.Role,com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%
try {
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request, response);
	String[] id = request.getParameterValues("ID");
	if (id != null && id.length > 0) {
	        RoleManager roleManager = SecurityDatabase.getRoleManager();
	        roleManager.deleteRoles(id);
	        
			
	}
	
%>
<script language="javascript">
	alert("删除角色成功!");
	parent.document.location.href ="../userManage/main.jsp";
	//parent.window.returnValue = true;
	//window.close();
</script>
<%
} catch (Exception e) {
	e.printStackTrace();


}
%>
