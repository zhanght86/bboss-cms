
<%
/**
 * <p>Title: 添加角色处理页面</p>
 * <p>Description: 添加角色处理页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: chinacreator</p>
 * @Date 2008-3-19
 * @author baowen.liu
 * @version 1.0
 **/
 %>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.AccessControl,com.frameworkset.platform.sysmgrcore.manager.*,
com.frameworkset.platform.sysmgrcore.entity.Role,com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase,
com.frameworkset.common.poolman.DBUtil"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>

<script language="javascript">
	var api = parent.frameElement.api, W = api.opener;
</script>	
<%
try {
	AccessControl control = AccessControl.getInstance();
	control.checkManagerAccess(request,response);
	String rolename = request.getParameter("roleName");
	String roledesc = request.getParameter("roleDesc");
	String roletype = request.getParameter("roleType");
	
	RoleManager roleManager = SecurityDatabase.getRoleManager();
	Role role = new Role();
	DBUtil db = new DBUtil();
	String sql ="select  count(*) from td_sm_role where role_name ='"+ rolename +"'";
	db.executeSelect(sql);
	if(db.getInt(0,0)>0){
	%>
		<script language="javascript">
		W.$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.name.exist"/>');
		
		</script>	
	<%}	else{			
		role.setRoleDesc(roledesc);
		role.setRoleName(rolename);
		role.setOwner_id(Integer.parseInt(control.getUserID()));
		role.setRoleType(roletype);
		roleManager.insertRole(role);
	%>
<script language="javascript">
	W.$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.add.success"/>', function(){
		W.location.reload();
		api.close();
	}, api);
	
</script>
<%
	}
	} catch (Exception e) {
		e.printStackTrace();
	}
%>
<script>
    window.onload = function prompt()
    {        
    	//parent.location.reload();
    	//parent.document.all.update.disabled=false;
				//parent.document.all.calc[0].disabled=false;
				//parent.document.all.calc[1].disabled=false;
        //parent.divProcessing.style.display="none";
    }
</script>

