<%
/**
 * <p>Title: 机构岗位下的角色设置处理页面</p>
 * <p>Description: 机构岗位下的角色设置处理页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: chinacreator</p>
 * @Date 2008-3-18
 * @author da.wei
 * @version 1.0
 **/
 %>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.web.struts.action.JobManagerAction"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.*"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Job"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Role"%>
<%
	 
		//---------------START--用户组管理写操作日志
		AccessControl control = AccessControl.getInstance();
		control.checkManagerAccess(request,response);
		String currentUserId = control.getUserID();
		String operContent="";        
        String operSource=control.getMachinedID();
        String openModle="岗位管理";
        String userName = control.getUserName();
        String description="";
        LogManager logManager = SecurityDatabase.getLogManager(); 	
        boolean flag=false;
        	
		//---------------END
		String jobId = request.getParameter("jobId");
		String roleId = request.getParameter("roleId");
		String orgId = request.getParameter("orgId");
	
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
			  try{
				jobManager.deleteJobroleByJobId(jobId, orgId);
				if(roleIds[0] != null && !roleIds[0].equals("")){
					//jobManager.addJobroleMap(jobId, orgId, roleIds);
					//记录递归授予关系
					jobManager.addJobroleMap(jobId, orgId, roleIds);	                
				}
				flag=true;
				
				}catch(Exception e){
				e.printStackTrace();
				flag=false;
				}
			}
			//System.out.println(JobManagerAction.storeAndDeleteJobRoleAjax(jobId,roleIds));	
		 }
%>

<script>
    window.onload = function prompt(){
    <%
       if(flag){
    %>
    parent.parent.$.dialog.alert("操作成功!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
    <%
       }else {
    %>
    parent.parent.$.dialog.alert("操作失败!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
     <%
     }
     %>
   
    }
</script>

