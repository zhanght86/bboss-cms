<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../include/global1.jsp"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.web.struts.action.RoleOperAction"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogGetNameById"%>
<%	  
		AccessControl control = AccessControl.getInstance();
		control.checkAccess(request,response);
	
		String resTypeId = request.getParameter("resTypeId");
		String resId = request.getParameter("resId");
		String opId = request.getParameter("opId");
		String checked = request.getParameter("checked");
		String title = request.getParameter("title");
		String resTypeName = request.getParameter("resTypeName");
		//---------------START--用户组管理写操作日志
	
	    String operSource=control.getMachinedID();
        String openModle="资源管理";
        String userName = control.getUserName();
        String description="";
        LogManager logManager = SecurityDatabase.getLogManager(); 
        String operContent= userName +" 对资源 "+title+" 进行了权限操作";     	
        logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description);   	
		//---------------END
	
	
			
		out.println(RoleOperAction.editRoleOper(resId,resTypeId,opId,checked,title));
%>


