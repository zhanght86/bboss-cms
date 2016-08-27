<%
/*
 * <p>Title: 修改用户组角色</p>
 * <p>Description: 修改用户组角色</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-3-18
 * @author liangbing.tao
 * @version 1.0
 */
%>


<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogGetNameById"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.GroupManager"%>
<%@ page import="org.frameworkset.web.servlet.support.RequestContextUtils"%>

<%	
		AccessControl control = AccessControl.getInstance();
		control.checkManagerAccess(request,response);
		
		String groupId = request.getParameter("groupId");
		String roleId = request.getParameter("roleId");
		String flag = request.getParameter("flag");
		
		//用户组管理写操作日志
        String operSource=control.getMachinedID();
        String openModle=RequestContextUtils.getI18nMessage("sany.pdp.groupmanage", request);
        String userName = control.getUserName();
        String description="";
        LogManager logManager = SecurityDatabase.getLogManager();
        String groupName_Log = LogGetNameById.getGroupNameByGroupId(groupId);
        String operContent=userName+RequestContextUtils.getI18nMessage("sany.pdp.groupmanage.group.user.operation", new String[] {groupName_Log}, request);
        logManager.log(control.getUserAccount(),operContent,openModle,operSource,description);
        boolean success=false;
        GroupManager groupManager=SecurityDatabase.getGroupManager();
        //对提交方式进行判断
        
        if(flag.equals("add"))
        {
            //对当前组下的角色进行新增
            String[] roleIds=roleId.split(",");
			success=groupManager.insertGroupRole(groupId,roleIds);
		}
		else
		{
			//对当前组下的角色进行删除
			success=groupManager.deleteGroupRole(groupId,roleId);
		}
%>
<script language="javascript">
	
	var api = parent.frameElement.api, W = api.opener;
    if(<%=success%>)
    {
    	W.$.dialog.alert("<pg:message code='sany.pdp.common.operation.success'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
    }
    else
    {
    	W.$.dialog.alert("<pg:message code='sany.pdp.common.operation.fail.nocause'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
    }
    
</script>

