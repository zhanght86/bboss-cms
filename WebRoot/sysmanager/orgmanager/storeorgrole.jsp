<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../include/global1.jsp"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.web.struts.action.OrgManAction"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogGetNameById"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.*"%>
<%
	
		//---------------START--机构管理写操作日志
		AccessControl control = AccessControl.getInstance();
		control.checkAccess(request,response);
		String operContent="";        
        String operSource=control.getMachinedID();
        String openModle="机构管理";
        String userName = control.getUserName();
        String description="";
        LogManager logManager = SecurityDatabase.getLogManager(); 		
		//---------------END
		String orgId = request.getParameter("orgId");
		String roleIds = request.getParameter("roleId");
		String[] arrrole = roleIds.split(",");
		String flag = request.getParameter("flag");
		for(int i=0;i<arrrole.length;i++){
		    String roleId = arrrole[i];
			//--机构管理写操作日志	
			String orgName_log = LogGetNameById.getOrgNameByOrgId(orgId);
			String roleName_log = LogGetNameById.getRoleNameByRoleId(roleId);
			operContent="保存机构角色,机构："+orgName_log+" 角色:"+roleName_log; 	
		    description="";
			logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description);       
			//--
			
		}
		OrgManager orgManager = SecurityDatabase.getOrgManager();
		orgManager.storeOrgRole(orgId,arrrole,flag);
	
%>
<script>
    window.onload = function prompt(){
        //alert("操作成功!");
        parent.divProcessing.style.display="none";
    }
</script>

