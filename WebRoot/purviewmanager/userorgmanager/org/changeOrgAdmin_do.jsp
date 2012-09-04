<%
/**
 * <p>Title: 机构管理员设置处理页面</p>
 * <p>Description: 机构管理员设置处理页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: chinacreator</p>
 * @Date 2008-3-17
 * @author da.wei
 * @version 1.0
 **/
 %>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.db.OrgAdministratorImpl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.OrgAdministrator"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.LogManager"%>
<%@page import="com.frameworkset.platform.sysmgrcore.entity.Organization"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.OrgManager"%>

<%@ page import="org.frameworkset.web.servlet.support.RequestContextUtils"%>

<%
AccessControl accesscontroler = AccessControl.getInstance();
accesscontroler.checkManagerAccess(request,response);
String orgId1 = request.getParameter("orgId");
String userId = request.getParameter("userId");
String[] userIds = userId.split("\\,");
String tag = request.getParameter("tag");
String exceptionMessage = null;

OrgAdministrator orgAdministrator = new OrgAdministratorImpl();
String operContent="";        
String operSource=accesscontroler.getMachinedID();
String openModle=RequestContextUtils.getI18nMessage("sany.pdp.organization.manage", request);
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

if(tag.equals("add")){
	try{
 		orgAdministrator.addOrgAdmin(userIds, orgId1, accesscontroler.getUserID());
 		//log start
		OrgManager orgManager = SecurityDatabase.getOrgManager();
		Organization org = orgManager.getOrgById(orgId1);	
		
		operContent=RequestContextUtils.getI18nMessage("sany.pdp.userorgmanager.org.user.admin.set.log", new Object[] {userName, userName_log, org.getOrgName()}, request);
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
		OrgManager orgManager = SecurityDatabase.getOrgManager();
		Organization org = orgManager.getOrgById(orgId1);			
		operContent=RequestContextUtils.getI18nMessage("sany.pdp.userorgmanager.org.user.admin.cancel.log", new Object[] {userName, userName_log, org.getOrgName()}, request);
		logManager.log(accesscontroler.getUserAccount(),operContent,openModle,operSource,"");         
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

