<%
/*
 * <p>Title: 隶属组的处理</p>
 * <p>Description: 隶属组的处理</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-3-18
 * @author liangbing.tao
 * @version 1.0
 */
%>

<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.GroupManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.User"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Group"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="java.util.List,
				java.util.ArrayList"%>

<%
	AccessControl accessControl = AccessControl.getInstance();
	accessControl.checkManagerAccess(request,response);
	String curUserId = accessControl.getUserID(); 
	
	String userId = request.getParameter("userId");
	
		if(userId == null)
		{
			request.getRequestDispatcher("noUser.jsp").forward(request,response);
		}
		else
		{
			try
			{
				String groupId = request.getParameter("groupId");
				
				request.setAttribute("userId",userId);
				
				
				GroupManager groupManager = SecurityDatabase.getGroupManager();
				User user = new User();
				user.setUserId(Integer.valueOf(userId));
				
				Group group1 = groupManager.getGroupByID(groupId);
				List allGroup = null;
				List existGroup = null;
				if (groupManager.isContainChildGroup(group1)){
					allGroup = groupManager
							.getGroupList("select * from td_sm_group o where o.PARENT_ID='"
									+ groupId + "' or o.GROUP_ID='" + groupId + "'");
					existGroup = groupManager
							.getGroupList("select * from td_sm_group o,td_sm_usergroup b where (o.PARENT_ID='"
									+ groupId + "' or o.GROUP_ID='" + groupId + "') and o.group_id=b.group_id"
									+ " and b.user_id='"+userId+"'");
				}
				else {
					allGroup = groupManager
							.getGroupList("select * from td_sm_group o where o.GROUP_ID='"
									+ groupId + "'");
					existGroup = groupManager
							.getGroupList("select * from td_sm_group o,td_sm_usergroup b where o.GROUP_ID='"
									+ groupId + "' and o.group_id=b.group_id and b.user_id='"+userId+"'");									
				}
				
				//List existGroup = groupManager.getGroupList(user);
				
				List usergroupsetGroup = null;
				if(allGroup.size() > 0){
					usergroupsetGroup = new ArrayList();
					for(int i = 0; i < allGroup.size(); i++){
						Group setGroup = (Group)allGroup.get(i);
						String groupIdSelf = String.valueOf(setGroup.getGroupId()); 
						if(curUserId.equals(String.valueOf(setGroup.getOwner_id()))){//只能为用户添加到自己创建的组
							usergroupsetGroup.add(setGroup);
						}
					}
				}
				
				if(accessControl.isAdmin()){
					request.setAttribute("allGroup", allGroup);
				}else{
					request.setAttribute("allGroup", usergroupsetGroup);
				}
				request.setAttribute("existGroup", existGroup);
				
				request.getRequestDispatcher("changeGroup_ajax.jsp").forward(request,response);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				request.getRequestDispatcher("fail.jsp").forward(request,response);
			}
		}
%>
