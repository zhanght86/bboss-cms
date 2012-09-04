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
		String orgId = request.getParameter("orgId");
		String tag = request.getParameter("tag");		
		
		//---------------START--角色管理写操作日志
		String operContent="";        
        String operSource=control.getMachinedID();
        String openModle="角色管理";
        String userName = control.getUserName();
        LogManager logManager = SecurityDatabase.getLogManager(); 	
		String roleName_log = LogGetNameById.getRoleNameByRoleId(roleId);
		operContent=userName+" 对角色  "+ roleName_log+"  对应的机构进行了操作"; 						
		logManager.log(control.getUserAccount() ,operContent,openModle,operSource,"");          
		//---------------END
		
		//if(orgId!=null){
		//	String orgIds[] =orgId.split("\\,");
		//
		//	out.print(RoleManagerAction.storeRoleOrgAjax(roleId,orgIds));	
		//}
		
		if((tag == "add") || (tag.equals("add"))){
			String sql = "select e.* from td_sm_orgrole e where e.org_id='" + orgId + "' and e.role_id='" + roleId + "'";
			DBUtil dBUtil = new DBUtil();
			try{
				dBUtil.executeSelect(sql);
				if(dBUtil.size() == 0){
					sql = "insert into td_sm_orgrole values('" + orgId + "', '" + roleId + "')";
					dBUtil.executeInsert(sql);
				}
			}catch(Exception e){}
		}
		if((tag == "delete") || (tag.equals("delete"))){
			String sql = "delete from td_sm_orgrole f where f.org_id='" + orgId + "' and f.role_id='" + roleId + "'";
			DBUtil dBUtil = new DBUtil();
			try{
				dBUtil.executeDelete(sql);
			}catch(Exception e){}
		}
	
%>

<script>
    window.onload = function prompt(){
        //alert("操作成功!");
        parent.divProcessing.style.display="none";
    }
</script>
