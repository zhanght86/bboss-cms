<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../include/global1.jsp"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogGetNameById"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.GroupManager"%>
<%
		AccessControl control = AccessControl.getInstance();
		control.checkAccess(request,response);
		String userId = request.getParameter("userId");
		String groupId = request.getParameter("groupId");	
		String flag = request.getParameter("flag");
		//用户组管理写操作日志
        String operSource=control.getMachinedID();
        String openModle="用户组管理";
        String userName = control.getUserName();
        String description="";
        LogManager logManager = SecurityDatabase.getLogManager();
        String groupName_Log = LogGetNameById.getGroupNameByGroupId(groupId);
        String operContent=userName +" 对用户组 "+groupName_Log+" 下的隶属用户进行了操作";
        logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description);
		boolean success=false;
		GroupManager groupManager=SecurityDatabase.getGroupManager();
		//对提交方式进行判断
        if(flag.equals("add"))
        {
            String[] userIds=userId.split(",");
			success=groupManager.insertGroupUser(groupId,userIds);
		}
		else
		{
			success=groupManager.deleteGroupUser(groupId,userId);
		}
%>
<script>
    if(<%=success%>)
    {alert("操作成功!");}
    else
    {alert("操作失败!");}
    parent.divProcessing.style.display="none";
</script>

