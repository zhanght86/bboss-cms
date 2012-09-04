<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.purviewmanager.PurviewManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.purviewmanager.db.PurviewManagerImpl"%>
<%@ page import="com.frameworkset.platform.security.AccessControl, java.util.*"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<html>
<body></body>
<script language="javascript">
<%
    AccessControl control = AccessControl.getInstance();
   	control.checkManagerAccess(request,response);
    String[] roleIds = null;
    if(request.getParameterValues("ID")!=null){ 
        roleIds = request.getParameterValues("ID");
    }else if(request.getParameter("roleId") != null){
        roleIds = new String[1];
        roleIds[0] = request.getParameter("roleId");
    }
    PurviewManager manager = new PurviewManagerImpl();
    List optFailedList = manager.reclaimRoleResources(roleIds);
    String fialedInfos = "";
    for(int i=0;optFailedList!=null && i<optFailedList.size();i++){
        if("".equals(fialedInfos)){
            fialedInfos = "<pg:message code='sany.pdp.purviewmanager.rolemanager.role.resource.recycle.fail'/>"+"\\n";
            fialedInfos += String.valueOf(optFailedList.get(i)) + "\\n"; 
        }else{
            fialedInfos += String.valueOf(optFailedList.get(i)) + "\\n";
        }
    }
    if("".equals(fialedInfos)){
%>
    $.dialog.alert('<pg:message code="sany.pdp.common.operation.success"/>', function() {
    	window.parent.location.href=parent.window.location.href;
    });
<%        
    }else{
%>
	$.dialog.alert("<%=fialedInfos%>");
<%        
    }
%>
</script>
