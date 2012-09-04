<%
/*
 * <p>Title: 角色复制处理页面</p>
 * <p>Description: 角色复制处理页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: chinacreator</p>
 * @Date 2008-3-19
 * @author liangbing.tao
 * @version 1.0
 */
 %>



<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.AccessControl,
				com.frameworkset.platform.sysmgrcore.manager.RoleManager,
				com.frameworkset.platform.sysmgrcore.manager.LogManager,
				com.frameworkset.platform.sysmgrcore.entity.Role,
				com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase,
				com.frameworkset.platform.sysmgrcore.manager.LogGetNameById"%>
				
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>

<%

				
		boolean tag = false ;
		//---------------START--角色管理写操作日志
		AccessControl control = AccessControl.getInstance();
		control.checkManagerAccess(request,response);
		
		
		String operContent="";        
        String operSource=control.getMachinedID();
        String openModle="角色管理";
        String userName = control.getUserName();
        String description="";
        LogManager logManager = SecurityDatabase.getLogManager(); 		
		//---------------END
	    
		String rolecopyId = request.getParameter("rolecopyId");
		String[] id = request.getParameterValues("checkBoxOne");
		request.setAttribute("roleId", rolecopyId);
		
		RoleManager roleManager = SecurityDatabase.getRoleManager();
		try
		{
			
			roleManager.copyResOfRole(rolecopyId,id);
			tag = true ;
		}
		catch(Exception e)
		{
			tag = false ;
			e.printStackTrace() ;
		}
		
		
		//--角色管理写操作日志	
		operContent="复制角色: "+	LogGetNameById.getRoleNameByRoleId(rolecopyId);			
		description="";
		logManager.log(control.getUserAccount(),operContent,openModle,operSource,description);  
	
%>	

<script language="javascript">
	var api = parent.frameElement.api, W = api.opener;
</script>

<script language="javascript">
	if(<%=tag%>)
	{
		W.$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.copy.success" />');
	}
	else
	{
		W.$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.copy.fail" />');
	}
	
	//parent.document.getElementById("copy").disabled = false ;
	//parent.document.getElementById("back").disabled = false ;
	//parent.document.all.divProcessing.style.display = "none";
</script>
	

	
