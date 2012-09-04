<%
/*
 * <p>Title: 隶属机构处理页面</p>
 * <p>Description: 隶属机构处理页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: chinacreator</p>
 * @Date 2008-3-24
 * @author liangbing.tao
 * @version 1.0
 */
 %>
 
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.web.struts.action.UserOrgManagerAction"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogGetNameById"%>



<%
		AccessControl control = AccessControl.getInstance();
		control.checkManagerAccess(request,response);

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

