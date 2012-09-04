<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../include/global1.jsp"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.web.struts.action.RoleManagerAction"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogGetNameById"%>
<%@ page import="com.frameworkset.common.poolman.DBUtil"%>

<%
		AccessControl control = AccessControl.getInstance();
		control.checkManagerAccess(request,response);
		String roleId = request.getParameter("roleId");
		String groupId = request.getParameter("groupId");
		String tag = request.getParameter("tag");
		
		//---------------START--角色管理写操作日志
		String operContent="";        
        String operSource=control.getMachinedID();
        String openModle="角色管理";
        String userName = control.getUserName();
        LogManager logManager = SecurityDatabase.getLogManager(); 	
		String roleName_log = LogGetNameById.getRoleNameByRoleId(roleId);
		operContent=userName+" 对角色  "+ roleName_log+"  对应的用户组进行了操作"; 						
		logManager.log(control.getUserAccount() ,operContent,openModle,operSource,"");          
		//---------------END
		
		//if(groupId!=null){
		//	String groupIds[] =groupId.split("\\,");
		//}
		if((tag == "add") || (tag.equals("add"))){
			String sql = "select e.* from td_sm_grouprole e where e.group_id='" + groupId + "' and e.role_id='" + roleId + "'";
			DBUtil dBUtil = new DBUtil();
			try{
				dBUtil.executeSelect(sql);
				if(dBUtil.size() == 0){
					sql = "insert into td_sm_grouprole values('" + groupId + "', '" + roleId + "')";
					dBUtil.executeInsert(sql);
				}
			}catch(Exception e){}
		}
		if((tag == "delete") || (tag.equals("delete"))){
			String sql = "delete from td_sm_grouprole f where f.group_id='" + groupId + "' and f.role_id='" + roleId + "'";
			DBUtil dBUtil = new DBUtil();
			try{
				dBUtil.executeDelete(sql);
			}catch(Exception e){}
		}
			//out.print(RoleManagerAction.storeRoleGroupAjax(roleId,groupIds));	
		
%>
<script>
    window.onload = function prompt(){
        //alert("操作成功!");
        parent.divProcessing.style.display="none";
    }
</script>
