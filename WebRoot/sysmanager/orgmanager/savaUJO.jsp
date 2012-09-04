<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../include/global1.jsp"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.web.struts.action.OrgManAction,com.frameworkset.util.StringUtil" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.web.struts.action.OrgManAction"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogGetNameById"%>
<%
		AccessControl control = AccessControl.getInstance();
		control.checkAccess(request,response);
		
		String jobId = request.getParameter("jobId");
		String userId = request.getParameter("userId");
		String orgId = request.getParameter("orgId");
		String flag = request.getParameter("flag");
		
		//---------------START--机构管理写操作日志	
		String jobName_log = LogGetNameById.getJobNameByJobId(jobId);
		String orgName_log = LogGetNameById.getOrgNameByOrgId(orgId);			
		String operContent="";        
        String operSource=control.getMachinedID();
        String openModle="机构管理";
        String userName = control.getUserName();
        LogManager logManager = SecurityDatabase.getLogManager();
    	operContent=userName+" 对机构： "+orgName_log+" 的岗位： "+jobName_log+" 进行了人员调动"; 	
	    logManager.log(control.getUserAccount() ,operContent,openModle,operSource,"");       
		//---------------END
	
	if(userId!=null){
		String userIds[] = StringUtil.split(userId,"\\,");
		if(flag.equals("1")){
			out.print(OrgManAction.storeUJOAjax(jobId,userIds,orgId));	
		}
		if(flag.equals("0")){
			out.print(OrgManAction.deleteUJOAjax(jobId,userIds,orgId));
	
		}
	}
	
    
	
%>
