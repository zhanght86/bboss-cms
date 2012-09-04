<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../include/global1.jsp"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.web.struts.action.OrgJobAction"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogGetNameById"%>
<%
    //---------------START--用户组管理写操作日志
    AccessControl control = AccessControl.getInstance();
    control.checkAccess(request,response);
    //记录日志
    String operContent="";        
    String operSource=control.getMachinedID();
    String openModle="用户管理";
    String userName = control.getUserName();
    String description="";
    LogManager logManager = SecurityDatabase.getLogManager();   
    
    String userId3 = request.getParameter("userId");
    String orgId = request.getParameter("orgId");
    String[] userId = null;
    if(userId3 != null && userId3.length() >0){
        userId = userId3.split(",");
    }
    if(userId != null && userId.length > 0){
        String orgName_log = LogGetNameById.getOrgNameByOrgId(orgId);
        operContent="调整机构下用户的排序：机构名称"+orgName_log;                         
        description="";
        logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description);
        OrgJobAction.storeOrgUserOrder(orgId,userId);        
    }else{
        out.print("获取机构下的用户异常!");
    }
%>
<script>
	window.onload = function prompt(){
        //alert("操作成功!");
        parent.divProcessing.style.display="none";
        parent.document.all.button1.disabled = false;
        parent.document.all.button2.disabled = false;
        parent.document.all.button3.disabled = false;
        parent.document.all.button4.disabled = false;
        parent.document.all.b1new.disabled = false;
    }
</script>
