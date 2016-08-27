<%
/*
 * <p>Title: 用户排序后台处理页面</p>
 * <p>Description: 用户排序后台的处理页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-3-20
 * @author liangbing.tao
 * @version 1.0
 */
 %>
 <%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.web.struts.action.OrgJobAction"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogGetNameById"%>


<%
    //---------------START--用户组管理写操作日志
    AccessControl control = AccessControl.getInstance();
    control.checkManagerAccess(request,response);
    
    //记录日志
    String operContent="";        
    String operSource=control.getMachinedID();
    String userName = control.getUserName();
    String description="";
    LogManager logManager = SecurityDatabase.getLogManager();   
    
    String userId3 = request.getParameter("userId");
    String orgId = request.getParameter("orgId");
    String[] userId = null;
    boolean isSuccess = false;
    if(userId3 != null && userId3.length() >0){
        userId = userId3.split(",");
    }
    
    if(userId != null && userId.length > 0){
        String orgName_log = LogGetNameById.getOrgNameByOrgId(orgId);
        description="";
       // logManager.log(control.getUserAccount() ,operContent,"",operSource,description);
        try{
             isSuccess = "success".equals(OrgJobAction.storeOrgUserOrder(orgId,userId));
        }catch(Exception e){
             e.printStackTrace();
        }
    }
%>
<script>
	var api = parent.frameElement.api, W = api.opener;
	window.onload = function prompt(){
	    if(<%=isSuccess %>){
	   W.$.dialog.alert("<pg:message code='sany.pdp.common.operation.success'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
        }else{
        	W.$.dialog.alert("<pg:message code='sany.pdp.common.operation.failed'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
        }
    }
</script>
