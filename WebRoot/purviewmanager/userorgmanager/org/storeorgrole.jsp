<%
/*
 * <p>Title: 机构角色隶属的后台保存处理页面</p>
 * <p>Description:机构角色隶属的后台保存处理页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-3-26
 * @author liangbing.tao
 * @version 1.0
 */
 %>


<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogGetNameById"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.*"%>

<%@ page import="org.frameworkset.web.servlet.support.RequestContextUtils"%>

<%
	
		//---------------START--机构管理写操作日志
		AccessControl control = AccessControl.getInstance();
		control.checkManagerAccess(request,response);
		
		String operContent="";        
        String operSource=control.getMachinedID();
        String openModle=RequestContextUtils.getI18nMessage("sany.pdp.organization.manage", request);
        String userName = control.getUserName();
        String description="";
        LogManager logManager = SecurityDatabase.getLogManager(); 		
        
		//---------------END
		
		String orgId = request.getParameter("orgId");
		String roleIds = request.getParameter("roleId");
		String[] arrrole = roleIds.split(",");
		String flag = request.getParameter("flag");
		
		for(int i=0;i<arrrole.length;i++)
		{
		    String roleId = arrrole[i];
			//--机构管理写操作日志	
			String orgName_log = LogGetNameById.getOrgNameByOrgId(orgId);
			String roleName_log = LogGetNameById.getRoleNameByRoleId(roleId);
			operContent=RequestContextUtils.getI18nMessage("sany.pdp.userorgmanager.org.role.save.log", new Object[] {orgName_log, roleName_log}, request);
		    description="";
			logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description);       
			//--
			
		}
		OrgManager orgManager = SecurityDatabase.getOrgManager();
		boolean tag = orgManager.storeOrgRole(orgId,arrrole,flag);

%>
<script>
    window.onload = function prompt()
    {
    	if("<%=tag%>")
    	{
    		$.dialog.alert("<pg:message code='sany.pdp.common.operation.success'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
    	}
    	else
    	{
    		$.dialog.alert("<pg:message code='sany.pdp.common.operation.fail.nocause'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
    	}
       // parent.divProcessing.style.display="none";
        
      //  parent.document.all("button1").disabled = false;
       // parent.document.all("button2").disabled = false;
      //  parent.document.all("button3").disabled = false;
      //  parent.document.all("button4").disabled = false;
        //parent.document.all("back").disabled = false;
    }
</script>

