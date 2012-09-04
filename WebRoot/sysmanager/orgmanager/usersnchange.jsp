<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../include/global1.jsp"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.db.UserManagerImpl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.web.struts.action.OrgJobAction"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogGetNameById"%>
<%
	
		//---------------START--用户组管理写操作日志
		AccessControl control = AccessControl.getInstance();
		control.checkAccess(request,response);
        UserManagerImpl uimpl = new UserManagerImpl();
        
		String operContent="";        
        String operSource=control.getMachinedID();
        String openModle="机构管理";
        String userName = control.getUserName();
        String description="";
        LogManager logManager = SecurityDatabase.getLogManager(); 		
		//---------------END
		String jobId = request.getParameter("jobId");
		String userId1 = request.getParameter("userId1");
		String userId2 = request.getParameter("userId2");
		String orgId = request.getParameter("orgId");
		String jobSn = request.getParameter("jobSn");
		String userSn = request.getParameter("userSn");
	
		//upall or downall
		String userId3 = request.getParameter("userId");		
		String[] userId = null;
		if(userId3 != null && userId3.length() >0){
			userId = userId3.split(",");
		}
		String orgName_log = LogGetNameById.getOrgNameByOrgId(orgId);
		String userNames_log = LogGetNameById.getUserNamesByUserIds(userId);
		
		if(userId != null && userId.length > 0)
		{
			//--用户组管理写操作日志	
			operContent="添加系列用户到机构组织,机构："+orgName_log+" 用户："+userNames_log; 						
			description="";
			logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description);          		
			//--	end	 --	
			out.print(uimpl.storeAllUserSnJobOrg(orgId,jobId,jobSn,userId));
		}else{
			//--用户组管理写操作日志	
			operContent="修改机构下岗位的排序,机构编号："+orgName_log; 						
			description="";
			logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description);          		
			//--	end	 --	
			out.print(OrgJobAction.storeUserSnJobOrg(orgId, jobId, jobSn,userId1, userId2, userSn));
		}
%>

