



<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.purviewmanager.PurviewManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.purviewmanager.db.PurviewManagerImpl"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="org.frameworkset.web.servlet.support.RequestContextUtils"%>
<%@ page import="java.util.*"%>
<html>
<body></body>
<script language="javascript">

<%
    AccessControl control = AccessControl.getInstance();
   	control.checkManagerAccess(request,response);
    
    String userIds = request.getParameter("userIds") == null ? "":request.getParameter("userIds");
    String[] userIdList = userIds.split(",");
    //public parameter
    String directRes = request.getParameter("directRes") == null ? "":request.getParameter("directRes");
    String userRoleRes = request.getParameter("userRoleRes") ==null ? "":request.getParameter("userRoleRes");
    String userOrgJobRes = request.getParameter("userOrgJobRes") == null ? "":request.getParameter("userOrgJobRes");
    String userGroupRes = request.getParameter("userGroupRes") == null ? "":request.getParameter("userGroupRes");
    
    PurviewManager manager = new PurviewManagerImpl();
    boolean isReclaimDirectRes = "".equals(directRes) ? false:true;
    boolean isReclaimUserRoles = "".equals(userRoleRes) ? false:true;
    boolean isReclaimUserJobs = "".equals(userOrgJobRes) ? false:true;
    boolean isReclaimUserGroups = "".equals(userGroupRes)?false:true;
    
    List optFailedList = manager.reclaimUsersResources(control,userIdList,isReclaimDirectRes,isReclaimUserRoles,isReclaimUserJobs,isReclaimUserGroups);
    
    String promptStr = "";    
    for(int i=0;i<optFailedList.size();i++){
        //格式 admin:业务1,业务2,
        if("".equals(promptStr)){
            promptStr = RequestContextUtils.getI18nMessage("sany.pdp.purviewmanager.rolemanager.user.resource.recycle.fail", request)+"\\n";
            promptStr += String.valueOf(optFailedList.get(i)) + "\\n";
        }else{
            promptStr += String.valueOf(optFailedList.get(i)) + "\\n";
        }        
    }
    if(!"".equals(promptStr)){
%>
		
		var api = parent.frameElement.api, W = api.opener;
        W.$.dialog.alert("<%=promptStr%>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
<%        
    }else{
%>
			
		var api = parent.frameElement.api, W = api.opener;
		 W.$.dialog.alert("<pg:message code='sany.pdp.common.operation.success'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
       // window.returnValue = "";
       // window.close();
<%
    }
%>
</script>
