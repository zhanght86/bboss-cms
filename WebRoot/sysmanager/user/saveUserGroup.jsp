<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../include/global1.jsp"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.web.struts.action.UserGroupManagerAction"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogGetNameById"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.*"%>
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
		//Integer uid = (Integer) session.getAttribute("currUserId");
		String userId = request.getParameter("userId");
		Integer uid = Integer.valueOf(userId);
		String groupId = request.getParameter("groupId");
		String flag = request.getParameter("flag");
		if(groupId!=null){
		    UserManager userManager = SecurityDatabase.getUserManager();
			String groupIds[] =groupId.split("\\,") ;
			String userName_log = LogGetNameById.getUserNameByUserId(userId);
			String groupNames_Log = LogGetNameById.getGroupNamesByGroupIds(groupIds);
			if(flag.equals("1")){
				//--
				operContent="添加用户："+userName_log+" 到组："+groupNames_Log;					
				description="";
				logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description);          
				//--	end	 --
				userManager.addUsergroup(uid,groupIds);		
			}
			if(flag.equals("0")){
				//--
				operContent="删除用户："+userName_log+" 从组："+groupNames_Log;					
				description="";
				logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description);          
				//--	end	 --
				userManager.deleteUsergroup(uid,groupIds);	

			}
		 }	
%>

<script>
    window.onload = function prompt(){
        //alert("操作成功!");
        parent.divProcessing.style.display="none";
    }
</script>
