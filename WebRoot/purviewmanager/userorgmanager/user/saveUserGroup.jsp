<%
/*
 * <p>Title: 保存用户隶属用户组的页面</p>
 * <p>Description: 保存用户隶属用户组的页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-3-18
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
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>

<%@ page import="org.frameworkset.web.servlet.support.RequestContextUtils"%>

<%
	
		//---------------START--
		AccessControl control = AccessControl.getInstance();
		control.checkManagerAccess(request,response);
		String operContent="";        
        String operSource=control.getMachinedID();
        String openModle=RequestContextUtils.getI18nMessage("sany.pdp.groupmanage.user.manage", request);
        String userName = control.getUserName();
        String description="";
        LogManager logManager = SecurityDatabase.getLogManager(); 		
		//---------------END
		
		
		//Integer uid = (Integer) session.getAttribute("currUserId");
		String userId = request.getParameter("userId");
		Integer uid = Integer.valueOf(userId);
		String groupId = request.getParameter("groupId");
		String flag = request.getParameter("flag");
		
		if(groupId!=null)
		{
		    UserManager userManager = SecurityDatabase.getUserManager();
			String groupIds[] =groupId.split("\\,") ;
			String userName_log = LogGetNameById.getUserNameByUserId(userId);
			String groupNames_Log = LogGetNameById.getGroupNamesByGroupIds(groupIds);
			if(flag.equals("1"))
			{
				//--
				operContent=RequestContextUtils.getI18nMessage("sany.pdp.groupmanage.add.user", request)+"："+userName_log+RequestContextUtils.getI18nMessage("sany.pdp.groupmanage.to.group", request)+" ："+groupNames_Log;					
				description="";
				logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description);          
				//--	end	 --
				userManager.addUsergroup(uid,groupIds);		
			}
			
			if(flag.equals("0"))
			{
				//--
				operContent=RequestContextUtils.getI18nMessage("sany.pdp.groupmanage.delte.user", request) + "："+userName_log+RequestContextUtils.getI18nMessage("sany.pdp.groupmanage.from.group", request) + " ："+groupNames_Log;					
				description="";
				logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description);          
				//--	end	 --
				userManager.deleteUsergroup(uid,groupIds);	

			}
		 }	
%>

<script>
var api = parent.parent.frameElement.api, W = api.opener;

    window.onload = function prompt()
    {
        W.$.dialog.alert('<pg:message code="sany.pdp.common.operation.success"/>');
       // parent.divProcessing.style.display="none";
        
       // parent.document.all("button1").disabled = false;
       // parent.document.all("button2").disabled = false;
      //  parent.document.all("button3").disabled = false;
      //  parent.document.all("button4").disabled = false;
      //  parent.document.all("back").disabled = false;
    }
</script>
