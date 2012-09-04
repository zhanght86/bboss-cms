<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../include/global1.jsp"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.web.struts.action.UserManagerAction,com.frameworkset.util.StringUtil"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.web.struts.action.UserJobManagerAction"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogGetNameById"%>
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
			UserJobManagerAction ujm = new UserJobManagerAction();
			String jobIds[] =StringUtil.split(jobId,"\\,");
			String idso[] = StringUtil.split(uid,"\\,");	
			String orgName_log = LogGetNameById.getOrgNameByOrgId(orgId);
			String jobNames_log = LogGetNameById.getJobNamesByJobIds(jobIds);
			String userNames_log = LogGetNameById.getUserNamesByUserIds(idso);
			if(flag.equals("1")){
				//--
				operContent="添加用户到机构、岗位,用户:"+userNames_log+"机构："+orgName_log+",岗位;"+ jobNames_log;
				description="";
				logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description);          
				//--	end	 --
				
				ujm.storeUJOAjax_batch(idso,jobIds,orgId);
			}	
			if(flag.equals("0")){
				//--
				operContent="从机构、岗位删除用户,用户:"+userNames_log+"机构："+orgName_log+",岗位;"+ jobNames_log;			
				description="";
				logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description);          
				//--	end	 --
				ujm.deleteUJOAjax_batch(idso,jobIds,orgId);	
						
			}
		}
		
    
	
%>
