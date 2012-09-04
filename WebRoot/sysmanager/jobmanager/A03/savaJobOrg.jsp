<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../include/global1.jsp"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.web.struts.action.JobManagerAction"%>
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
        String openModle="岗位管理";
        String userName = control.getUserName();
        String description="";
        LogManager logManager = SecurityDatabase.getLogManager(); 		
		//---------------END
		String jobId = request.getParameter("jobId");
		String orgId = request.getParameter("orgId");
		
		if(orgId!=null){
			String orgIds[] =orgId.split("\\,");
			//--用户组管理写操作日志	
			String orgNames_log = LogGetNameById.getOrgNamesByOrgIds(orgIds);
			String jobName_log = LogGetNameById.getJobNameByJobId(jobId);
			System.out.println("----------------岗位编号："+jobId);
			operContent="修改岗位机构,岗位："+jobName_log+" 机构："+orgNames_log; 						
			description="";
			logManager.log(control.getUserAccount(),operContent,openModle,operSource,description);       
			//--	end	 --
			out.print(JobManagerAction.storeJobOrgAjax(jobId,orgIds));	
		 }
	
    
	
%>
