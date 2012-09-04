<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.purviewmanager.PurviewManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.purviewmanager.db.PurviewManagerImpl"%>
<%@ page import="com.frameworkset.platform.security.AccessControl, java.util.*"%>
<html>
<body></body>
<script language="javascript">
<%
    AccessControl control = AccessControl.getInstance();
   	control.checkManagerAccess(request,response);
    
    String orgId = request.getParameter("orgId")==null?"":request.getParameter("orgId");
    //public parameter
    String directOrgRes = request.getParameter("directOrgRes")==null?"":request.getParameter("directOrgRes");
    String orgRoleRes = request.getParameter("orgRoleRes")==null?"":request.getParameter("orgRoleRes");    
    String directRes = request.getParameter("directRes")==null?"":request.getParameter("directRes");
    String userRoleRes = request.getParameter("userRoleRes")==null?"":request.getParameter("userRoleRes");
    String userOrgJobRes = request.getParameter("userOrgJobRes")==null?"":request.getParameter("userOrgJobRes");
    String isRecursion_ = request.getParameter("isRecursion")==null?"":request.getParameter("isRecursion");
    String isOrgRecursion_ = request.getParameter("isOrgRecursion")==null?"":request.getParameter("isOrgRecursion");
    //是否选中用户的用户组权限进行回收
    String userGroupRes = request.getParameter("userGroupRes")==null?"":request.getParameter("userGroupRes");
    
    PurviewManager manager = new PurviewManagerImpl();
    boolean isReclaimOrgDirectRes = "".equals(directOrgRes)?false:true;
    boolean isReclaimOrgRoleRes = "".equals(orgRoleRes)?false:true;
    boolean isReclaimDirectRes = "".equals(directRes)?false:true;
    boolean isReclaimUserRoles = "".equals(userRoleRes)?false:true;
    boolean isReclaimUserJobs = "".equals(userOrgJobRes)?false:true;
    boolean isRecursion = "".equals(isRecursion_)?false:true;
    boolean isOrgRecursion = "".equals(isOrgRecursion_)?false:true;
    boolean isReclaimUserGroupRes = "".equals(userGroupRes)?false:true;
    //回收机构的资源
    manager.reclaimOrgResources(orgId, isReclaimOrgDirectRes,isReclaimOrgRoleRes,isOrgRecursion);
    //回收机构下用户的资源
    List optFailedList = manager.reclaimOrgUsersResources(control,orgId,isReclaimDirectRes,isReclaimUserRoles,isReclaimUserJobs,isReclaimUserGroupRes,isRecursion);    
    
    String promptStr = "";    
    for(int i=0;i<optFailedList.size();i++){
        //格式 admin:业务1,业务2,
        if("".equals(promptStr)){
            promptStr = "以下用户资源回收失败:\\n";
            promptStr += String.valueOf(optFailedList.get(i)) + "\\n";
        }else{
            promptStr += String.valueOf(optFailedList.get(i)) + "\\n";
        }        
    }
    if(!"".equals(promptStr)){
%>
        alert("<%=promptStr%>");
<%        
    }else{
%>
        alert("操作成功!");
        window.returnValue = "";
        window.close();
<%
    }
%>
</script>
