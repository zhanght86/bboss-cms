<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<%@ page import="com.frameworkset.platform.sysmgrcore.manager.db.RoleManagerImpl,com.frameworkset.platform.sysmgrcore.manager.RoleManager"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogGetNameById"%>
<%@ page import="com.frameworkset.common.poolman.DBUtil"%>
<%
	
		
		AccessControl control = AccessControl.getInstance();
		control.checkManagerAccess(request,response);
	
		String roleId = request.getParameter("roleId");
		String userId = request.getParameter("userId");
		String orgId = request.getParameter("orgId");
		String tag = request.getParameter("tag"); 
		
		//---------------START--角色管理写操作日志
		String operContent="";        
        String operSource=control.getMachinedID();
        String openModle="角色管理";
        String userName = control.getUserName();
        LogManager logManager = SecurityDatabase.getLogManager(); 	
		String roleName_log = LogGetNameById.getRoleNameByRoleId(roleId);
		operContent=userName+" 对角色  "+ roleName_log+"  对应的用户进行了操作"; 						
		logManager.log(control.getUserAccount() ,operContent,openModle,operSource,"");          
		//---------------END
		
		//if(userId!=null){
		//	String userIds[] =userId.split("\\,");
		//	out.print(RoleManagerAction.storeRoleUserAjax(roleId,userIds,orgId));	
	 	//}
		RoleManager roleManager = new RoleManagerImpl(); 
		String msg = "";
		boolean success = false;
    	if(tag.equals("add")){
    		String[] uids = userId.split(",");
    		try
    		{
    			roleManager.grantRoleToUsers(uids,roleId);
				success = true;
			}
			catch(Exception e){
				msg = "操作失败：" + e.getMessage();
			}
		}
		
		if(tag.equals("delete")){
			String[] uids = userId.split(",");
			try{
				success = roleManager.deleteUsersOfRole(uids,roleId);
				if(!success)
				{
					msg = "操作失败";
				}
				
			}catch(Exception e){
				msg = e.getMessage();
			}
		}
%>
<script>
	<%if(success){%>
		parent.refurbish();
	<%}%>
    window.onload = function prompt(){
    	<%if(!msg.equals(""))
    	{
    	%>
    	
        alert("<%=msg%>");
        <%
        }
        %>
        parent.divProcessing.style.display="none";
    }
</script>
