<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../include/global1.jsp"%>
<%@ page import="com.frameworkset.util.StringUtil"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogGetNameById"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.*"%>
<%
		AccessControl control = AccessControl.getInstance();
		control.checkAccess(request,response);
		
		String uid2 = request.getParameter("userId");
		Integer uid = Integer.valueOf(uid2);
		String orgId = request.getParameter("orgId");
		String jobId = request.getParameter("jobId");
		String flag = request.getParameter("flag");
		//---------------START--
		String userName_log = LogGetNameById.getUserNameByUserId(uid2);
		String orgName_log = LogGetNameById.getOrgNameByOrgId(orgId);
		String operContent="";        
        String operSource=control.getMachinedID();
        String openModle="用户管理";
        String userName = control.getUserName();
        LogManager logManager = SecurityDatabase.getLogManager(); 
 		operContent=userName+" 调整了用户 "+userName_log+" 在机构 "+ orgName_log+" 下的岗位"; 						
		logManager.log(control.getUserAccount(),operContent,openModle,operSource,"");          
		//---------------END	
		UserManager userManager = SecurityDatabase.getUserManager();
		String message = "";
		if(jobId!=null){
			String jobIds[] =StringUtil.split(jobId,"\\,");
			if(flag.equals("1")){			    
				message = userManager.storeUserOrgJob(uid,orgId,jobIds);	
			}
			if(flag.equals("0")){
					
				message = userManager.deleteUserOrgJob(uid,orgId,jobIds);	
			}
		 }
%>

<script>
    window.onload = function prompt(){
        //alert("操作成功!");
        <%
        	if("fail".equals(message)){
        		out.print("alert('操作失败！')");
        	}else if("success".equals(message)){
        		out.print("alert('操作成功！')");
        	}
        %>
        parent.divProcessing.style.display="none";
        //parent.refreshPage();
        
    }
</script>
