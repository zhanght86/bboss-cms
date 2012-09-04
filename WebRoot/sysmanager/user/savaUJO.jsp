<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../include/global1.jsp"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.web.struts.action.UserManagerAction,com.frameworkset.util.StringUtil"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.web.struts.action.UserJobManagerAction"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogGetNameById"%>

<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager" %>
<%

		//---------------START--
		AccessControl control = AccessControl.getInstance();
		control.checkAccess(request,response);
		String operContent="";        
        String operSource=control.getMachinedID();
        String openModle="用户管理";
        String userName = control.getUserName();
        String description="";
        LogManager logManager = SecurityDatabase.getLogManager(); 		
		//---------------END
		
		String uid = request.getParameter("uid");
		String orgId = request.getParameter("orgId1");
		String jobId = request.getParameter("jobId");
		String flag = request.getParameter("flag");
		if(jobId!=null){
			String jobIds[] =StringUtil.split(jobId,"\\,");
			String orgName_log = LogGetNameById.getOrgNameByOrgId(orgId);
			String userName_log = LogGetNameById.getUserNameByUserId(uid);
			String jobNames_log = LogGetNameById.getJobNamesByJobIds(jobIds);
			UserManager userManager = SecurityDatabase.getUserManager();
			if(flag.equals("1")){
				//--
				operContent="添加人员: "+userName_log+"岗位: "+jobNames_log+" 和机构: "+orgName_log+"的关系"; 
				description="";
				logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description);          
				//--	end	 --
				//UserJobManagerAction.storeUJOAjax(uid,jobIds,orgId);		
				
				userManager.storeUJOAjax(uid, jobIds, orgId);					
			}	
			if(flag.equals("0")){
				//--
				operContent="删除人员："+userName_log+"岗位: "+jobNames_log+" 和机构: "+orgName_log+"的关系"; 
				description="";
				logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description);          
				//--	end	 --
				//UserJobManagerAction.deleteUJOAjax(uid,jobIds,orgId);
				//userManager.storeUJOAjax(uid, jobIds, orgId);					
				userManager.deleteUJOAjax(uid,jobIds,orgId);
			
			}
		}
	
    

%>

<script language="javascript">
	parent.divProcessing.style.display = "none";
</script>
