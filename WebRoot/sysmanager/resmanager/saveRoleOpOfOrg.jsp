<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../include/global1.jsp"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.web.struts.action.RoleOperAction"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogGetNameById"%>
<%	  
		//---------------START--用户组管理写操作日志
		AccessControl control = AccessControl.getInstance();
		control.checkAccess(request,response);
		String operContent="";        
        String operSource=control.getMachinedID();
        String openModle="资源管理";
        String userName = control.getUserName();
        String description="";
        LogManager logManager = SecurityDatabase.getLogManager(); 		
		//---------------END
		//--用户组管理写操作日志
	
		String resTypeId = request.getParameter("resTypeId");
		String resId = request.getParameter("resId");
		String opId = request.getParameter("opId");
		String checked = request.getParameter("checked");
		String title = request.getParameter("title");
		String resTypeName = request.getParameter("resTypeName");
		
			//--用户组管理写操作日志	
		String[] opIds=opId.split(";");
		String roleId = "";
		String opration = "";		
		if(opIds.length>1)
		{
			roleId = opIds[0];
		    opration = opIds[1];
		}
		String resName_log = LogGetNameById.getResNameByResId(resId);
		String roleName_log = LogGetNameById.getRoleNameByRoleId(roleId);
		String oprationName = "";
		if(opration.equals("read")){ oprationName="可见不可操作"; }
		if(opration.equals("write")){ oprationName="可操作"; }		

		if(checked.equals("1"))
		{
		operContent="建立:资源名称为"+resTypeName+">"+title+" 的角色及操作权限 "+roleName_log+" 操作:"+oprationName;  
		}else{
		operContent="取消:资源名称为"+resTypeName+">"+title+" 的角色及操作权限 "+roleName_log+" 操作:"+oprationName; 
		}	
		logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description);       
		//--
			logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description);       
			//--		
		out.println(RoleOperAction.editRoleOperOfOrg(resId,resTypeId,opId,checked,title));
%>


