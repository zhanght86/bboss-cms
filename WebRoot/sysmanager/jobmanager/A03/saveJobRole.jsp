<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../include/global1.jsp"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.*"%>
<%
	  
		//---------------START--用户组管理写操作日志
		AccessControl control = AccessControl.getInstance();
		control.checkAccess(request,response);
		String currentUserId = control.getUserID();
		String operContent="";        
        String operSource=control.getMachinedID();
        String openModle="岗位管理";
        String userName = control.getUserName();
        String description="";
        LogManager logManager = SecurityDatabase.getLogManager(); 		
		//---------------END
		String jobId = request.getParameter("jobId");
		String roleId = request.getParameter("roleId");
		String orgId = request.getParameter("orgId");
		boolean state = false;
		if(roleId!=null){
			String roleIds[] =roleId.split("\\,");
			//--用户组管理写操作日志	
			String orgNames_log = LogGetNameById.getOrgNamesByOrgIds(roleIds);
			String jobName_log = LogGetNameById.getJobNameByJobId(jobId);
			//System.out.println("----------------岗位编号："+jobId);
			operContent="修改岗位角色,岗位："+jobName_log+" 角色："+orgNames_log; 						
			description="";
			logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description);       
			//--	end	 --
			
			
			JobManager jobManager = SecurityDatabase.getJobManager();
			
			if(jobId != null && !jobId.equals("")){
				//jobManager.deleteJobroleByJobId(jobId, orgId);
				//递归回收
				state = jobManager.deleteJobroleByJobId(jobId, orgId);
				if(roleIds[0] != null && !roleIds[0].equals("")){
					//jobManager.addJobroleMap(jobId, orgId, roleIds);
					//记录递归授予关系
					state = jobManager.addJobroleMap(jobId, orgId, roleIds);
				}
			}
			//System.out.println(JobManagerAction.storeAndDeleteJobRoleAjax(jobId,roleIds));	
		 }
	
    
	
%>
<script language="Javascript">
alert("ddd");
parent.document.all.divProcessing.style.display="none";
</script>
