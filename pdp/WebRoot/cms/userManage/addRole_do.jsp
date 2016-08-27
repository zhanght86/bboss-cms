<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl,com.frameworkset.platform.sysmgrcore.manager.*,
com.frameworkset.platform.sysmgrcore.entity.Role,com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase,
com.frameworkset.common.poolman.DBUtil"%>
<%
try {
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request, response);
	String rolename = request.getParameter("roleName").trim();
	String roledesc = request.getParameter("roleDesc");
	String roletype = request.getParameter("roleType");
	
	System.out.println("..........."+rolename);
	System.out.println("..........."+roledesc);
	RoleManager roleManager = SecurityDatabase.getRoleManager();
	Role role = new Role();
	DBUtil db = new DBUtil();
	String sql ="select  count(*) from td_sm_role where role_name ='"+ rolename +"'";
	//System.out.println("..........."+sql);
	db.executeSelect(sql);
	if(db.getInt(0,0)>0){
	%>
		<script language="javascript">
		alert("角色名称已存在，请另外取名!");
		
		</script>	
	<%}	else{			
		role.setRoleDesc(roledesc);
		role.setRoleName(rolename);
		role.setOwner_id(Integer.parseInt(control.getUserID()));
		role.setRoleType(roletype);
		roleManager.insertRole(role);
	%>
<script language="javascript">
	alert("新增角色成功!");
	window.dialogArguments.location.href ="../userManage/main.jsp";
	//parent.window.returnValue = true;
	window.close();
</script>
<%
}
	} catch (Exception e) {
	e.printStackTrace();


}
%>
