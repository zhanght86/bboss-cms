<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../include/global1.jsp"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.web.struts.action.OrgJobAction"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogGetNameById"%>
<%
	
		//---------------START--用户组管理写操作日志
		AccessControl control = AccessControl.getInstance();
		control.checkAccess(request,response);
		String operContent="";        
        String operSource=control.getMachinedID();
        String openModle="机构管理";
        String userName = control.getUserName();
        String description="";
        LogManager logManager = SecurityDatabase.getLogManager(); 		
		//---------------END
		String jobId = request.getParameter("jobId");
		String orgId = request.getParameter("orgId");
		String jobSn = request.getParameter("jobSn");
		String auserId = request.getParameter("auserId");
		String userIds = request.getParameter("userId");
		String[] userList = null;
		if(userIds != null && userIds.length() > 0){
			userList = userIds.split(",");
		}
		
		String userNames_log = LogGetNameById.getUserNamesByUserIds(userList);
		String orgName_log = LogGetNameById.getOrgNameByOrgId(orgId);	
		//删除
	
				
		if(userList!= null && userList.length > 0){	
			//--用户组管理写操作日志	
			operContent="从机构组织删除系列用户,机构："+orgName_log+" 用户："+userNames_log; 						
			description="";
			logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description);          		
			//--	end	 --	
			out.print(OrgJobAction.deleteUserOrgJob(orgId, jobId,userList));		
		}
		//添加
		else{
			//--用户组管理写操作日志	
			operContent="添加系列用户到机构组织,机构："+orgName_log+" 用户："+userNames_log; 					
			description="";
			logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description);          		
			//--	end	 --	
			out.print(OrgJobAction.storeUserOrgJob(orgId, jobId,auserId,jobSn));
		}
	
%>

