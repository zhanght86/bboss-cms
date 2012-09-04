<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../include/global1.jsp"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.web.struts.action.MenuAction"%>
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
	String isRecursion = request.getParameter("isRecursion");
	String menuPath = request.getParameter("menuPath");
	String types = "role"; 
	
	   //---------------START--菜单管理写操作日志
		
		String operContent="";        
        String operSource=control.getMachinedID();
        String openModle="菜单管理";
        String userName = control.getUserName();
        LogManager logManager = SecurityDatabase.getLogManager();
        operContent=userName+"：对菜单("+title+")进行了授权"; 	
        logManager.log(control.getUserAccount(),operContent,openModle,operSource,"");	
		//---------------END
		
	out.println(MenuAction.editRoleOper(resId,resTypeId,opId,checked,title,isRecursion,menuPath,types));
%>


