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
<%@ page import="java.util.List"%>

<%
	AccessControl accessControl = AccessControl.getInstance();
	accessControl.checkManagerAccess(request,response);
	
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
				if (groupManager.isContainChildGroup(group1)) 
				{
					allGroup = groupManager
							.getGroupList("select * from td_sm_group o where o.PARENT_ID='"
									+ groupId + "' or o.GROUP_ID='" + groupId + "'");
				}
				else 
				{
					allGroup = groupManager
							.getGroupList("select * from td_sm_group o where o.GROUP_ID='"
									+ groupId + "'");
				}
				
				List existGroup = groupManager.getGroupList(user);
				
				request.setAttribute("allGroup", allGroup);
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
