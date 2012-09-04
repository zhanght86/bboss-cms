<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl,com.frameworkset.platform.sysmgrcore.manager.*,
com.frameworkset.platform.sysmgrcore.entity.User,com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%
try {
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request, response);
	String roleId = request.getParameter("roleId");
	//System.out.println("======="+roleId);
	String[] id = request.getParameterValues("ID");
	if (id != null) {
			for (int i = 0; i < id.length; i++) {
				UserManager userManager = SecurityDatabase.getUserManager();
				if(!id[i].equals("1"))
				{
					User user= userManager.getUserById(id[i]);
					userManager.deleteUserrole(user);
					
				}
			}
	}
	
%>
<script language="javascript">
	alert("删除用户成功!");
	parent.document.location.href ="../userManage/selectuser.jsp?roleId=<%=roleId%>";
	
</script>
<%
} catch (Exception e) {
	e.printStackTrace();


}
%>
