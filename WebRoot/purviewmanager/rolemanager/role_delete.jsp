<%
/**
 * <p>Title: 角色删除页面</p>
 * <p>Description: 角色删除</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: chinacreator</p>
 * @Date 2008-3-19
 * @author baowen.liu
 * @version 1.0
 **/
 %>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.RoleTypeManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.*
				,com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
				
<%@ page import="org.frameworkset.web.servlet.support.RequestContextUtils"%>
<html>
<head>
<title>角色删除</title>

<style type="text/css">
<!--
.STYLE2 {	color: #263F77;
	font-weight: bold;
}
.STYLE3 {color: #FF0000}
-->
</style>
</head>
<body>
<%			
			AccessControl accessControl = AccessControl.getInstance();
			accessControl.checkManagerAccess(request,response); //页面登录保护

			boolean tag = true;
			String notice = "";

			//删除操作
			RoleTypeManager rtm = new RoleTypeManager();
			String id = request.getParameter("role_id");			
			
			if ((id != null) && (id.length() > 0)) {
				String[] ids = id.split(";");
					
				RoleManager roleManager = SecurityDatabase.getRoleManager();
				
				//--角色管理写操作日志	
				String operSource=accessControl.getMachinedID();
    			String openModle=RequestContextUtils.getI18nMessage("sany.pdp.role.manage", request);
				String operContent=RequestContextUtils.getI18nMessage("sany.pdp.purviewmanager.rolemanager.role.delete.log.begin", request)+"： " + ids; 						
				String description="";
				LogManager logManager = SecurityDatabase.getLogManager(); 
				logManager.log(accessControl.getUserAccount(),operContent,openModle,operSource,description);       
				//--
					
				tag = roleManager.deleteRoles(ids);
				if(tag)
				{
					notice = RequestContextUtils.getI18nMessage("sany.pdp.purviewmanager.rolemanager.role.type.remove.success", request);
				}
				else
				{
					notice= RequestContextUtils.getI18nMessage("sany.pdp.purviewmanager.rolemanager.role.type.remove.fail", request);
				}
			}

		%>
</body>
</html>
<script language="javascript">
    	$.dialog.alert("<%=notice%>", function() {
    		parent.document.getElementById("rolemanage").src="rolemanager/role.jsp";
    	});
</script>

