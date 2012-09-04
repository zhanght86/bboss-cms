<%
/*
 * <p>Title: 角色授予用户的处理页面</p>
 * <p>Description: 角色授予用户的处理页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: chinacreator</p>
 * @Date 2008-3-24
 * @author liangbing.tao
 * @version 1.0
 */
 %>

<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<%@ page import="com.frameworkset.platform.sysmgrcore.manager.db.RoleManagerImpl,
				com.frameworkset.platform.sysmgrcore.manager.RoleManager"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogGetNameById"%>
<%@ page import="com.frameworkset.common.poolman.DBUtil"%>

<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>

<%@ page import="org.frameworkset.web.servlet.support.RequestContextUtils"%>

<%
	
		
		AccessControl control = AccessControl.getInstance();
		control.checkManagerAccess(request,response);
	
		String roleId = request.getParameter("roleId");
		String userId = request.getParameter("userId");
		String orgId = request.getParameter("orgId");
		String tag = request.getParameter("tag"); 
		
		//---------------START--角色管理写操作日志
		String operContent="";        
        String operSource=control.getMachinedID();
        String openModle=RequestContextUtils.getI18nMessage("sany.pdp.role.manage", request);
        String userName = control.getUserName();
        LogManager logManager = SecurityDatabase.getLogManager(); 	
		String roleName_log = LogGetNameById.getRoleNameByRoleId(roleId);
		operContent=userName+RequestContextUtils.getI18nMessage("sany.pdp.purviewmanager.rolemanager.role.user.operation", new String[] {roleName_log}, request);
		logManager.log(control.getUserAccount() ,operContent,openModle,operSource,"");          
		//---------------END
		
		//if(userId!=null){
		//	String userIds[] =userId.split("\\,");
		//	out.print(RoleManagerAction.storeRoleUserAjax(roleId,userIds,orgId));	
	 	//}
		RoleManager roleManager = new RoleManagerImpl(); 
		String msg = "";
		boolean success = false;
    	if(tag.equals("add")){
    		String[] uids = userId.split(",");
    		try
    		{
    			roleManager.grantRoleToUsers(uids,roleId);
				success = true;
			}
			catch(Exception e){
				msg = RequestContextUtils.getI18nMessage("sany.pdp.common.operation.fail.nocause", request) + "：" + e.getMessage();
			}
		}
		
		if(tag.equals("delete")){
			String[] uids = userId.split(",");
			try{
				success = roleManager.deleteUsersOfRole(uids,roleId);
				if(!success)
				{
					msg = RequestContextUtils.getI18nMessage("sany.pdp.common.operation.fail.nocause", request);
				}
				
			}catch(Exception e){
				msg = e.getMessage();
			}
		}
%>
<script>
	var api = parent.parent.frameElement.api, W = api.opener;
	
	<%
		if(success)
		{
	%>
			W.$.dialog.alert('<pg:message code="sany.pdp.common.operation.success"/>');
	<%
		}
	%>
	
    window.onload = function prompt(){
    	<%
    	if(!msg.equals(""))
    	{
    	%>
	        W.$.dialog.alert("<%=msg%>");
        <%
        }
        %>
       //parent.divProcessing.style.display="none";
       
       //parent.document.all("button1").disabled = false;
	   //parent.document.all("button2").disabled = false;
	   //parent.document.all("button3").disabled = false;
	   //parent.document.all("button4").disabled = false;
	   
	   //parent.document.all("back").disabled = false;
	   
    }
</script>
