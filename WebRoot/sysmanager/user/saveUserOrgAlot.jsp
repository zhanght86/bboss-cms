<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../include/global1.jsp"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase,com.frameworkset.platform.sysmgrcore.web.struts.action.UserOrgManagerAction"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogGetNameById"%>
<%
	
		//---------------START--角色管理写操作日志
		AccessControl control = AccessControl.getInstance();
		control.checkAccess(request,response);
		String operContent="";        
        String operSource=control.getMachinedID();
        String openModle="用户管理";
        String userName = control.getUserName();
        String description="";
        LogManager logManager = SecurityDatabase.getLogManager(); 		
		//---------------END
	
		String jobId = request.getParameter("jobId");
		String orgId = request.getParameter("orgId");		
		String id = request.getParameter("id");	
		// System.out.println("id" + id);
		//System.out.println("roleId" + roleId);
		if(jobId!=null){
			String jobIds[] =jobId.split("\\,") ;
			String idso[] =id.split("\\,") ;
			//--
			String userNames_log = LogGetNameById.getUserNamesByUserIds(idso);
			String jobNames_log = LogGetNameById.getJobNamesByJobIds(jobIds);
			String orgName_log = LogGetNameById.getOrgNameByOrgId(orgId);
			operContent="批量用户加入机构,用户："+userNames_log+" 机构："+orgName_log+" 岗位："+jobNames_log; 						
			description="";
			logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description);          
			//--	end	 --
			UserOrgManagerAction uma = new UserOrgManagerAction();
			uma.storeAlotUserOrg(idso,jobIds,orgId);
			//userManager.storeAlotUserJob(idso,roleIds,orgId);
			
		 }
    
	
%>
