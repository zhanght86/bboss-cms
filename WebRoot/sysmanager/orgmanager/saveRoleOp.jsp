<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../include/global1.jsp"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.web.struts.action.OrgManAction"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogManager,com.frameworkset.platform.sysmgrcore.manager.OrgManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase,com.frameworkset.platform.sysmgrcore.entity.Organization"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogGetNameById"%>
<%	  
		
		AccessControl control = AccessControl.getInstance();
		control.checkAccess(request,response);
	
		String resTypeId = request.getParameter("resTypeId");
		String resId = request.getParameter("resId");
		String opId = request.getParameter("opId");
	
		String checked = request.getParameter("checked");
		String title = request.getParameter("title");
		String isRecursion = request.getParameter("isRecursion");
		//---------------START--机构管理写操作日志
		OrgManager orgManager = SecurityDatabase.getOrgManager();
		Organization  org = orgManager.getOrgById(resId);
		String operContent=control.getUserName()+" 对机构: "+ org.getOrgName()+" 进行了授权";        
        String operSource=control.getMachinedID();
        String openModle="机构管理";
        String userName = control.getUserName();
        LogManager logManager = SecurityDatabase.getLogManager();
        logManager.log(control.getUserAccount() ,operContent,openModle,operSource,"");		
		//---------------END
	
		out.println(OrgManAction.editRoleOper(resId,resTypeId,opId,checked,title,isRecursion));
%>


