<%
/*
 * <p>Title: 角色隶属机构的后台处理页面</p>
 * <p>Description: 角色隶属机构的后台处理页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
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
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.*"%>
<%@ page import="com.frameworkset.util.StringUtil"%>

<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>

<%@ page import="org.frameworkset.web.servlet.support.RequestContextUtils"%>

<%		
		AccessControl control = AccessControl.getInstance();
		control.checkManagerAccess(request,response);
	
		String roleId = request.getParameter("roleId");
		String orgId = request.getParameter("orgids");
		String tag = request.getParameter("tag");		
		
		//---------------START--角色管理写操作日志
		String operContent="";        
        String operSource=control.getMachinedID();
        String openModle=RequestContextUtils.getI18nMessage("sany.pdp.role.manage", request);
        String userName = control.getUserName();
        LogManager logManager = SecurityDatabase.getLogManager(); 	
		String roleName_log = LogGetNameById.getRoleNameByRoleId(roleId);
		operContent=userName+RequestContextUtils.getI18nMessage("sany.pdp.purviewmanager.rolemanager.role.org.operation", new String[] {roleName_log}, request);
		logManager.log(control.getUserAccount() ,operContent,openModle,operSource,""); 
		
		String[] roleids = StringUtil.split(roleId,",");
		String orgids[] = StringUtil.split(orgId,",");
			         
	
		boolean flag = false;
		
		OrgManager orgManager = SecurityDatabase.getOrgManager();
		
		if(tag != null  && !tag.equals("")  && tag.equals("add"))
		{
			flag = orgManager.storeOrgRole(orgids,roleids);
		}
		
		if(tag != null && !tag.equals("") && tag.equals("delete"))
		{
			flag = orgManager.deleteOrgrole(orgids, roleids);
		}
	
%>


<script>
	var api = parent.parent.frameElement.api, W = api.opener;
	
    window.onload = function prompt()
    {    
		if(<%=flag%>)
		{
			W.$.dialog.alert('<pg:message code="sany.pdp.common.operation.success"/>');
			parent.refreshAllGrantedOrgs();
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
