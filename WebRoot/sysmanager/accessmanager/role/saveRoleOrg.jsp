<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../include/global1.jsp"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.web.struts.action.RoleManagerAction"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogGetNameById"%>
<%@ page import="com.frameworkset.common.poolman.DBUtil,com.frameworkset.util.StringUtil"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.*"%>
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
		String[] roleids = StringUtil.split(roleId,",");
		String orgids[] = StringUtil.split(orgId,",");
			         
	
		OrgManager orgManager = SecurityDatabase.getOrgManager();
		if((tag == "add") || (tag.equals("add"))){
		
			orgManager.storeOrgRole(orgids,roleids);
		}
		if((tag == "delete") || (tag.equals("delete"))){
			orgManager.deleteOrgrole(orgids, roleids);
		}
	
%>

<script>
    window.onload = function prompt(){
        //alert("操作成功!");
        parent.divProcessing.style.display="none";
    }
</script>
