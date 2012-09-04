<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../include/global1.jsp"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase,com.frameworkset.platform.sysmgrcore.web.struts.action.UserOrgManagerAction"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogGetNameById"%>
<%
		AccessControl control = AccessControl.getInstance();
		control.checkAccess(request,response);
	
		
		String jobId = request.getParameter("jobId");	
		String id = request.getParameter("id");
		String orgId = request.getParameter("orgId");	
	
		if(jobId!=null){
			String jobIds[] = jobId.split("\\,") ;
			String idso[] = id.split("\\,") ;
			
			//---------------START--
			String operContent="";        
	        String operSource=control.getMachinedID();
	        String openModle="用户管理";
	        String userName = control.getUserName();
	        String description="";
	        LogManager logManager = SecurityDatabase.getLogManager(); 		
			String userNames_log = LogGetNameById.getUserNamesByUserIds(idso);
			String orgName_log = LogGetNameById.getOrgNameByOrgId(orgId);
			
			operContent=userName+" 对机构 "+orgName_log+" 下的用户进行了批量操作"; 						
			logManager.log(control.getUserAccount() ,operContent,openModle,operSource,"");          
			//--end--			
			
			UserOrgManagerAction uma = new UserOrgManagerAction();
			uma.delAlotUserOrg(idso,jobIds,orgId);
			
		 }
    
	
%>
