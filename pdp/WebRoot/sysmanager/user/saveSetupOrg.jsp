<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../include/global1.jsp"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.web.struts.action.UserOrgManagerAction"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogGetNameById"%>


<%
		AccessControl control = AccessControl.getInstance();
		control.checkAccess(request,response);

		String uid = request.getParameter("userId");
		String orgIdNames = request.getParameter("orgIdName");
		//---------------START--
		String operContent="";        
        String operSource=control.getMachinedID();
        String openModle="用户管理";
        String userName = control.getUserName();
        LogManager logManager = SecurityDatabase.getLogManager(); 
        String userName_log = LogGetNameById.getUserNameByUserId(uid);
		operContent=userName+" 设置了用户："+userName_log+" 的主单位"; 						
		logManager.log(control.getUserAccount() ,operContent,openModle,operSource,"");          
		//---------------END		
		if(orgIdNames!=null){
			
			out.println(UserOrgManagerAction.storeSetupOrg(Integer.valueOf(uid),orgIdNames));	
		 }
    
	
%>

