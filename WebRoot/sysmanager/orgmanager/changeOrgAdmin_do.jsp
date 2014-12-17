<%
/**
 * 
 * <p>Title: 机构部门管理员处理页面</p>
 *
 * <p>Description: 机构部门管理员处理页面，记录设置部门管理员的日志</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: bboss</p>
 * @Date 2006-9-15
 * @author gao.tang
 * @version 1.0
 */
 %>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.db.OrgAdministratorImpl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.OrgAdministrator"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.LogManager"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.OrgManager"%>
<%@page import="com.frameworkset.platform.sysmgrcore.entity.Organization,com.frameworkset.platform.sysmgrcore.manager.LogGetNameById"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%
AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
String orgId1 = request.getParameter("orgId1");
String userId = request.getParameter("userId");
String[] userIds = userId.split("\\,");
String tag = request.getParameter("tag");
String exceptionMessage = null;
if(tag.equals("add")){
 	OrgAdministrator orgAdministrator = new OrgAdministratorImpl();
	String operContent="";        
    String operSource=accesscontroler.getMachinedID();
    String openModle="机构管理";
    String userName = accesscontroler.getUserName();
    LogManager logManager = SecurityDatabase.getLogManager(); 		
	String userName_log = "";
	for(int i = 0; i < userIds.length; i++){
		if(!"".equals(userName_log)){
			userName_log += "," + SecurityDatabase.getUserManager().getUserById(userIds[i]).getUserRealname();
		}else{
			userName_log = SecurityDatabase.getUserManager().getUserById(userIds[i]).getUserRealname();
		}
	}
	try{
		orgAdministrator.addOrgAdmin(userIds, orgId1, accesscontroler.getUserID());
		//log start
		OrgManager orgManager = SecurityDatabase.getOrgManager();
		Organization org = orgManager.getOrgById(orgId1);			
		operContent=userName+" 将用户【"+userName_log+"】设置为机构【"+org.getOrgName()+"】的部门管理员"; 				
		logManager.log(accesscontroler.getUserAccount(),operContent,openModle,operSource,"");          
		//--	end	 --
	}catch(Exception e){
		e.printStackTrace();
		exceptionMessage = e.getMessage();
		if(exceptionMessage != null)
        {
        	exceptionMessage = exceptionMessage.replaceAll("\\n","\\\\n");
        	exceptionMessage = exceptionMessage.replaceAll("\\r","\\\\r");
        }
	}	
}
else if(tag.equals("delete")){
	try{
		OrgAdministrator orgAdministrator = new OrgAdministratorImpl();
		//---------------START--
		
			String operContent="";        
	        String operSource=accesscontroler.getMachinedID();
	        String openModle="机构管理";
	        String userName = accesscontroler.getUserName();
	        LogManager logManager = SecurityDatabase.getLogManager(); 		
			String userName_log = "";
			for(int i = 0; i < userIds.length; i++){
				if(!"".equals(userName_log)){
					userName_log += "," + SecurityDatabase.getUserManager().getUserById(userIds[i]).getUserRealname();
				}else{
					userName_log = SecurityDatabase.getUserManager().getUserById(userIds[i]).getUserRealname();
				}
			}
			//String tmp[] = orgIdNames.split(";");
			OrgManager orgManager = SecurityDatabase.getOrgManager();
			Organization org = orgManager.getOrgById(orgId1);			
			operContent=userName+" 取消了用户【"+userName_log+"】在机构【"+org.getOrgName()+"】下的部门管理员角色"; 						
			logManager.log(accesscontroler.getUserAccount(),operContent,openModle,operSource,"");          
		//--	end	 --	
		orgAdministrator.deleteOrgAdmin(userIds, orgId1);
	}catch(Exception e){
		e.printStackTrace();
		exceptionMessage = e.getMessage();
		if(exceptionMessage != null)
        {
        	exceptionMessage = exceptionMessage.replaceAll("\\n","\\\\n");
        	exceptionMessage = exceptionMessage.replaceAll("\\r","\\\\r");
        }
	}
}
%>
<script>
    window.onload = function prompt(){
    	<%if(exceptionMessage == null){%>
        	parent.divProcessing.style.display="none";
        	alert("操作成功！");
        <%}else{%>
        	parent.divProcessing.style.display="none";
        	alert("操作失败！");
        <%}%>
    }
</script>
