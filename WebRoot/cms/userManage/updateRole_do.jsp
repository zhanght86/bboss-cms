<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl,com.frameworkset.platform.sysmgrcore.manager.*,
com.frameworkset.platform.sysmgrcore.entity.Role,com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase,
com.frameworkset.common.poolman.DBUtil"%>
<%
try {
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request, response);
	String roleid = request.getParameter("roleId");
	System.out.println(roleid);
	String rolename = request.getParameter("roleName");
	String roledesc = request.getParameter("roleDesc");
	String roletype = request.getParameter("roleType");
	DBUtil db=new DBUtil();
	String sql ="select  *  from td_sm_role where role_name ='"+ rolename +"' and role_id <>'"+roleid+"'";
	db.executeSelect(sql);
	if(db.size() > 0)
	{
	%>
	<script language="javascript">
			alert("角色名称已存在，请另外取名!");		
	</script>	
	
	<%
	} else {
	
	RoleManager roleManager = SecurityDatabase.getRoleManager();
	Role role = roleManager.getRoleById(roleid);
	   
		
	role.setRoleName(rolename);
	role.setRoleDesc(roledesc);
	role.setRoleType(roletype);
	roleManager.storeRole(role);
	%>
		<script language="javascript">
			alert("修改角色信息成功!");
			window.dialogArguments.location.href ="../userManage/main.jsp";
			window.close();
		</script>
  <%
      }
	
	} catch (Exception e) {
				e.printStackTrace();

       }
  %>
