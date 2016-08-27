<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../include/global1.jsp"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.web.struts.action.UserOrgManagerAction"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogGetNameById,
com.frameworkset.platform.sysmgrcore.entity.Organization,com.frameworkset.platform.sysmgrcore.manager.OrgManager"%>
<%
	
		
		AccessControl control = AccessControl.getInstance();
		control.checkAccess(request,response);
		String userId = request.getParameter("uid");
		Integer uid = Integer.valueOf(userId);
		String orgIdNames = request.getParameter("orgIdName");
		
		//---------------START--
	
		String operContent="";        
        String operSource=control.getMachinedID();
        String openModle="用户管理";
        String userName = control.getUserName();
        LogManager logManager = SecurityDatabase.getLogManager(); 		
		String userName_log = LogGetNameById.getUserNameByUserId(userId);
		String tmp[] = orgIdNames.split(";");
		OrgManager orgManager = SecurityDatabase.getOrgManager();
		Organization org = orgManager.getOrgById(tmp[0]);			
		operContent=userName+" 调整了用户："+userName_log+" 在机构："+org.getOrgName()+" 下的任职"; 						
		logManager.log(control.getUserAccount() ,operContent,openModle,operSource,"");          
		//--	end	 --	
		if(orgIdNames!=null){
		
			
			out.println(UserOrgManagerAction.storeAndDeleteUserOrg(uid,orgIdNames));	
		 }
    
	
%>

<script language="javascript">
	parent.divProcessing.style.display = "none";
	alert("调出机构成功！");
</script>
