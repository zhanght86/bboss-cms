<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.AccessControl" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.db.RoleManagerImpl" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.RoleManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.User" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Role" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.GroupManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.OrgManager" %>
<%@ page import="java.util.Set" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Group" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Organization" %>
<%@ page import="java.util.HashSet" %>

<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%
	AccessControl accessControl = AccessControl.getInstance();
			accessControl.checkAccess(request,response);
			 String uid2 = request.getParameter("userId");
			 if(null == uid2 || "".equals(uid2))
			 	uid2 = accessControl.getUserID();
			 request.setAttribute("userId",uid2);
			 Integer uid = Integer.valueOf(uid2);
			 String orgId = request.getParameter("orgId");
			 request.setAttribute("orgId",orgId);
			if (uid == null) {
				return ;
			}
			String orgid = request.getParameter("orgId");
			//UserRoleManagerForm userRoleForm = (UserRoleManagerForm) form;
			//userRoleForm.setOrgId(orgid);
			//userRoleForm.setUserId(uid.toString());
			RoleManager roleManager = SecurityDatabase.getRoleManager();
			User user = new User();
			user.setUserId(uid);
			List existRole = roleManager.getRoleListByUserRole(user);
			String sql ="";
			if("1".equals(accessControl.getUserID())){
				sql = "select * from td_sm_role t where t.role_id not in('2','3') order by t.role_name";
			}else{
				sql = "select * from td_sm_role t where t.role_id not in('1','2','3') order by t.role_name";
			}
			List list = roleManager.getRoleList(sql);
			List allRole = null;
			// 角色列表加权限
			for (int i = 0; list != null && i < list.size(); i++) {
				Role role = (Role) list.get(i);
				if (accessControl.checkPermission(role.getRoleId(), "userset", AccessControl.ROLE_RESOURCE)) {
					if (allRole == null)
						allRole = new ArrayList();
					if(AccessControl.isAdministratorRole(role.getRoleName()))
					{
							allRole.add(role);
					}
					else
					{
						if(!role.getRoleName().equals(AccessControl.getEveryonegrantedRoleName()))
						{
							allRole.add(role);
						}
					}
				}
			}
//			得到用户所属组得到用户所属角色
			GroupManager groupManager = SecurityDatabase.getGroupManager();
			OrgManager orgManager = SecurityDatabase.getOrgManager();
			List groupList = groupManager.getGroupList(user);
			List orgList = orgManager.getOrgList(user);
			Set userGroupRole =new HashSet();
			Set userOrgRole = new HashSet();
			for(int i=0;groupList!=null&&i<groupList.size();i++){
				Group group = (Group)groupList.get(i);
				List role = roleManager.getRoleList(group);
				userGroupRole.addAll(role);
			}
			for(int i=0;orgList!=null&&i<orgList.size();i++){
				Organization org = (Organization)orgList.get(i);
				List role = roleManager.getRoleList(org);
				userOrgRole.addAll(role);
			}
			//得到用户岗位对应的角色
			//List jobRole = roleManager.getJobRoleByList(uid2);
			
			request.setAttribute("orgRole",new ArrayList(userOrgRole));
			request.setAttribute("groupRole",new ArrayList(userGroupRole));
			request.setAttribute("allRole", allRole);
			request.setAttribute("existRole", existRole);
			//request.setAttribute("userRoleForm", userRoleForm);
			//request.setAttribute("jobRole", jobRole);
%>
<jsp:forward page="userrole_list.jsp"></jsp:forward>
