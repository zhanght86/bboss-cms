<%
/*
 * <p>Title: 角色修改的处理页面</p>
 * <p>Description: 角色修改的处理页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: chinacreator</p>
 * @Date 2008-3-19
 * @author liangbing.tao
 * @version 1.0
 */
 %>



<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.AccessControl,
				com.frameworkset.platform.sysmgrcore.manager.*,
				com.frameworkset.platform.sysmgrcore.entity.Role,
				com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase,
				com.frameworkset.common.poolman.DBUtil"
				%>

<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>

<script language="javascript">
	var api = parent.frameElement.api, W = api.opener;
</script>	
<%
	try
	{
		AccessControl control = AccessControl.getInstance();
		control.checkManagerAccess(request,response);
		
		String roleid = request.getParameter("roleId");
		String rolename = request.getParameter("roleName");
		String roledesc = request.getParameter("roleDesc");
		String roletype = request.getParameter("roleType");
		
		RoleManager roleManager = SecurityDatabase.getRoleManager();
		Role role = new Role();
		DBUtil db = new DBUtil();
		String sql ="select  count(*) from td_sm_role where role_name ='"+ rolename +"' and role_id !='"+roleid+"'";
		db.executeSelect(sql);
		if(db.getInt(0,0)>0)
		{
	%>
		<script language="javascript">
			W.$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.name.exist"/>');		
		</script>	
	<%
		}	
		else
		{	
			role.setRoleId(roleid);		
			role.setRoleDesc(roledesc);
			role.setRoleName(rolename);
			role.setRoleType(roletype);
			roleManager.storeRole(role);
	%>
		<script language="javascript">
			W.$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.modfiy.success"/>', function(){
				api.close();
				W.location.reload();
			}, api);
		</script>
	<%
		}
	}
	catch (Exception e) 
	{
		e.printStackTrace();
	}
%>

