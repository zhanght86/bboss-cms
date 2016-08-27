<%
/*
 * <p>Title: 用户组的后台处理页面</p>
 * <p>Description: 对用户组的添、删和改操作</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-3-18
 * @author liangbing.tao
 * @version 1.0
 */
%>

<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.GroupManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Group"%>
<%@ page import="org.frameworkset.web.servlet.support.RequestContextUtils"%>

<%
		AccessControl control = AccessControl.getInstance();
		control.checkManagerAccess(request,response);
		
		int userId = Integer.parseInt(control.getUserID());
		
		//状态
		int flag = Integer.parseInt(request.getParameter("flag"));
		
		String parentId = request.getParameter("parentId");	
		
		//对新增子组是否是一级组还是子组,进行不同的操作
		String son = request.getParameter("son");
		son = son == null ? "" : son;
		
						
		if(parentId == "" || parentId == null)
		{
			parentId="0";
		}
		
		String groupId = request.getParameter("groupId");	
		
		String groupName = request.getParameter("groupName");
		
		String oldGroupName = request.getParameter("oldGroupName");
		
		String groupDesc = request.getParameter("groupDesc");
		
		
		GroupManager groupManager=SecurityDatabase.getGroupManager();	
		
		
		Group group=new Group();	
		String info=RequestContextUtils.getI18nMessage("sany.pdp.common.operation.success", request);	
		
		//对操作状态进行判断
		switch(flag)
		{
			//新增
			case 1:
			group.setOwner_id(userId);
			group.setParentId(Integer.parseInt(parentId));			
			group.setGroupName(groupName);
			group.setGroupDesc(groupDesc);
			info=groupManager.saveGroup(group,control.getUserID());		
			groupId=group.getGroupId() + "";	
			
			break;
			
			//修改
			case 2:
			group.setGroupId(Integer.parseInt(groupId));
			group.setGroupName(groupName);
			group.setGroupDesc(groupDesc);	
			info=groupManager.updateGroup(group,oldGroupName);			
			break;
			
			//删除
			case 3:
			group.setGroupId(Integer.parseInt(groupId));	
			if(groupManager.deleteGroup(group))
			{
				info=RequestContextUtils.getI18nMessage("sany.pdp.common.operation.fail.nocause", request);
			}		
			break;
		}
%>

<script language="javascript">
	var api = parent.frameElement.api ;
	if(api){
		W = api.opener;
		W.location.reload();
	}else{
		//parent.parent.parent.parent.parent.parent.parent.parent.parent.$.dialog.alert("<%=info%>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
		parent.parent.location.reload();
	}
</script>


