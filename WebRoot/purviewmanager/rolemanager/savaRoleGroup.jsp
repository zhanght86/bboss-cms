<%
/*
 * <p>Title: 角色授予用户组的保存数据页面</p>
 * <p>Description: 角色授予用户组的保存数据页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: chinacreator</p>
 * @Date 2008-3-25
 * @author liangbing.tao
 * @version 1.0
 */
%>


<%@ page language="java" contentType="text/html; charset=UTF-8"%>
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
		
		String userId = control.getUserID();
		
		String roleId = request.getParameter("roleId");
		
		String groupId = request.getParameter("groupId");
		String[] groupIds = null ;
		
		if(groupId != null && !groupId.equals(""))
		{
			groupIds = groupId.split(",");
		}
		
		String tag = request.getParameter("tag");
		
		
		//---------------START--角色管理写操作日志
		String operContent="";        
        String operSource=control.getMachinedID();
        String openModle=RequestContextUtils.getI18nMessage("sany.pdp.role.manage", request);
        String userName = control.getUserName();
        LogManager logManager = SecurityDatabase.getLogManager(); 	
		String roleName_log = LogGetNameById.getRoleNameByRoleId(roleId);
		operContent=userName+RequestContextUtils.getI18nMessage("sany.pdp.purviewmanager.rolemanager.role.usergroup.operation", new String[] {roleName_log}, request);						
		logManager.log(control.getUserAccount(),operContent,openModle,operSource,"");          
		//---------------END
		
		
		boolean flag = false;
		
		if(tag !=null && !tag.equals("") && (tag.equals("add")))
		{
			DBUtil dBUtil = new DBUtil();
			try
			{
				for(int i=0; i< groupIds.length; i++)
				{
					String sql = "insert into td_sm_grouprole values('" + groupIds[i] + "', '" + roleId + "','" + userId + "')";
					dBUtil.executeInsert(sql);				
				}
				
				flag = true;
			}
			catch(Exception e)
			{
				flag = false;
				e.printStackTrace();
			}
		}
		
		if(tag !=null  && !tag.equals("") &&  tag.equals("delete"))
		{
			
			DBUtil dBUtil = new DBUtil();
			
			try
			{
				for(int i=0 ; i<groupIds.length ; i++)
				{
					String sql = "delete from td_sm_grouprole  where group_id='" + groupIds[i] + "' and role_id='" + roleId + "'";
					dBUtil.executeDelete(sql);			
				}
				flag = true;
			}
			catch(Exception e)
			{
				e.printStackTrace();
				flag = false;
			}
		}	
		
%>
<script>
	var api = parent.parent.frameElement.api, W = api.opener;
	
    window.onload = function prompt()
    {
    	if(<%=flag%>)
    	{
    		W.$.dialog.alert('<pg:message code="sany.pdp.common.operation.success"/>');
    	}
        else
        {
        	W.$.dialog.alert('<pg:message code="sany.pdp.common.operation.fail.nocause"/>');
        }
        //parent.divProcessing.style.display="none";
        
        //parent.document.all("button1").disabled = false;
		//parent.document.all("button2").disabled = false;
		//parent.document.all("button3").disabled = false;
		//parent.document.all("button4").disabled = false;
		//parent.document.all("back").disabled = false;
    }
</script>
