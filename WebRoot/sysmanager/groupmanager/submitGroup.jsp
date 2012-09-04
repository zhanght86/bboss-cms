<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../include/global1.jsp"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogGetNameById"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.db.GroupManagerImpl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.GroupManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Group"%>


<%
		AccessControl control = AccessControl.getInstance();
		control.checkAccess(request,response);
		
		int userId = Integer.parseInt(control.getUserID());
		
		int flag = Integer.parseInt(request.getParameter("flag"));
		String parentId = request.getParameter("parentId");	
		if(parentId=="")parentId="0";
		String groupId = request.getParameter("groupId");	
		String groupName = request.getParameter("groupName");
		String oldGroupName = request.getParameter("oldGroupName");
		String groupDesc = request.getParameter("groupDesc");
		GroupManager groupManager=SecurityDatabase.getGroupManager();		
		Group group=new Group();	
		String info="操作失败";	
		//对操作状态进行判断
		switch(flag)
		{
			//新增
			case 1:
			group.setOwner_id(userId);
			group.setParentId(Integer.parseInt(parentId));			
			group.setGroupName(groupName);
			group.setGroupDesc(groupDesc);	
			info=groupManager.insertGroup(group,control.getUserID());	
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
			if(groupManager.deleteGroup(group))info="操作成功";	
			break;
		}
%>
<script language="javascript">
   	alert("<%=info%>");
   	if(<%=request.getParameter("son")%>=="1"){
	   	parent.getNavigatorContent().location.href ="groupTree.jsp?collapse=<%=parentId%>&request_scope=request&selectedNode=<%=groupId%>";
	   	parent.getNavigatorContent().location.href ="groupTree.jsp?expand=<%=parentId%>&request_scope=request&selectedNode=<%=groupId%>";
   	}else{
   		parent.getNavigatorContent().window.location.reload();
   	}
</script>


