<%
/*
 * <p>Title: 用户隶属岗位保存页面</p>
 * <p>Description: 用户隶属岗位保存页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: chinacreator</p>
 * @Date 2008-3-22
 * @author liangbing.tao
 * @version 1.0
 */
 %>
 
 
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.util.StringUtil"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogGetNameById"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.*"%>

<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>

<%@ page import="org.frameworkset.web.servlet.support.RequestContextUtils"%>


<%
		AccessControl control = AccessControl.getInstance();
		control.checkManagerAccess(request,response);
		
		String uid2 = request.getParameter("userId");
		Integer uid = Integer.valueOf(uid2);
		String orgId = request.getParameter("orgId");
		String jobId = request.getParameter("jobId");
		String flag = request.getParameter("flag");
		//---------------START--
		String userName_log = LogGetNameById.getUserNameByUserId(uid2);
		String orgName_log = LogGetNameById.getOrgNameByOrgId(orgId);
		String operContent="";        
        String operSource=control.getMachinedID();
        String openModle=RequestContextUtils.getI18nMessage("sany.pdp.groupmanage.user.manage", request);
        String userName = control.getUserName();
        LogManager logManager = SecurityDatabase.getLogManager(); 
 		operContent=RequestContextUtils.getI18nMessage("sany.pdp.userorgmanager.job.change", new Object[] {userName, userName_log, orgName_log}, request);
		logManager.log(control.getUserAccount(),operContent,openModle,operSource,"");          
		//---------------END	
		UserManager userManager = SecurityDatabase.getUserManager();
		String message = "";
		if(jobId!=null){
			String jobIds[] =StringUtil.split(jobId,"\\,");
			if(flag.equals("1")){			    
				message = userManager.storeUserOrgJob(uid,orgId,jobIds);	
			}
			if(flag.equals("0")){		
				message = userManager.deleteUserOrgJob(uid,orgId,jobIds);	
			}
		 }
		 
		 System.out.println("message : " + message);
%>

<script>
    	<%
        	if("fail".equals(message)){
        %>
        	$.dialog.alert("<pg:message code='sany.pdp.common.operation.fail.nocause'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
        <%
    		}else if("success".equals(message)){
        %>
        	$.dialog.alert("<pg:message code='sany.pdp.common.operation.success'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
        <%
        }
        %>
</script>
