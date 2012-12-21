package com.frameworkset.platform.sysmgrcore.web.struts.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.platform.framework.Framework;
import com.frameworkset.platform.framework.Item;
import com.frameworkset.platform.framework.ItemQueue;
import com.frameworkset.platform.framework.MenuHelper;
import com.frameworkset.platform.framework.Module;
import com.frameworkset.platform.framework.ModuleQueue;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.sysmgrcore.entity.CSMenuModel;
import com.frameworkset.platform.sysmgrcore.entity.Group;
import com.frameworkset.platform.sysmgrcore.entity.Grouprole;
import com.frameworkset.platform.sysmgrcore.entity.Job;
import com.frameworkset.platform.sysmgrcore.entity.Operation;
import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.platform.sysmgrcore.entity.Orgrole;
import com.frameworkset.platform.sysmgrcore.entity.Role;
import com.frameworkset.platform.sysmgrcore.entity.Roleresop;
import com.frameworkset.platform.sysmgrcore.entity.User;
import com.frameworkset.platform.sysmgrcore.entity.Userjoborg;
import com.frameworkset.platform.sysmgrcore.manager.CSMenuManager;
import com.frameworkset.platform.sysmgrcore.manager.GroupManager;
import com.frameworkset.platform.sysmgrcore.manager.JobManager;
import com.frameworkset.platform.sysmgrcore.manager.LogGetNameById;
import com.frameworkset.platform.sysmgrcore.manager.LogManager;
import com.frameworkset.platform.sysmgrcore.manager.OrgManager;
import com.frameworkset.platform.sysmgrcore.manager.RoleManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.platform.sysmgrcore.manager.UserManager;
import com.frameworkset.platform.sysmgrcore.manager.db.CSMenuManagerImpl;
import com.frameworkset.platform.sysmgrcore.web.struts.form.RoleManagerForm;
import com.frameworkset.platform.sysmgrcore.web.struts.form.RoleOrgForm;
/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:角色管理模块的action
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: 三一集团
 * </p>
 * 
 * @author feng.jing
 * @version 1.0
 */
public class RoleManagerAction extends DispatchAction implements Serializable{
	public RoleManagerAction() {
	}

	private static Logger log = Logger.getLogger(RoleManagerAction.class
			.getName());

	/**
	 * 根据角色id获取角色信息
	 * 
	 * @param mapping
	 *            ActionMapping
	 * @param form
	 *            ActionForm
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */

	public ActionForward getRoleById(ActionMapping mapping, RoleManagerForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		String roleId = request.getParameter("roleId");
		if (roleId == null) {
			roleId = (String) session.getAttribute("currRoleId");
		}
		if (roleId == null) {
			return mapping.findForward("noRole");
		}

		RoleManager roleManager = SecurityDatabase.getRoleManager();
		Role role = roleManager.getRoleById( roleId);

		session.setAttribute("currRoleId", roleId);
		session.setAttribute("roleTabId", "1");
		request.setAttribute("role", role);
		RoleManagerForm roleForm = (RoleManagerForm) form;
		if (role != null) {
			roleForm.setRoleId(role.getRoleId());
			roleForm.setRoleDesc(role.getRoleDesc());
			roleForm.setRoleName(role.getRoleName());
		}
		return mapping.findForward("roledetail");
	}

	/**
	 * 保存新角色 废弃
	 * 
	 * @param mapping
	 *            ActionMapping
	 * @param form
	 *            ActionForm
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward saveRole(ActionMapping mapping, RoleManagerForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		//---------------START--角色管理写操作日志
		AccessControl control = AccessControl.getInstance();
		control.checkAccess(request,response);
		String operContent="";        
        String operSource=control.getMachinedID();//request.getRemoteAddr();
        String openModle="角色管理";
        String userName = control.getUserName();
        String description="";
        LogManager logManager = SecurityDatabase.getLogManager(); 		
		//---------------END
		
		RoleManager roleManager = SecurityDatabase.getRoleManager();
		RoleManagerForm roleForm = (RoleManagerForm) form;
		String roleDesc = roleForm.getRoleDesc();
		String roleName = roleForm.getRoleName();

		if (roleDesc != null && roleName != null) {
			Role role = new Role();
			role.setRoleDesc(roleDesc);
			role.setRoleName(roleName);
			roleManager.storeRole(role);
			
			//--角色管理写操作日志	
			operContent="修改角色: "+role.getRoleName(); 						
			description="";
			logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description);       
			//--
			
			Role r = roleManager.getRoleByName(roleName);
			request.setAttribute("role", r);
			return mapping.findForward("roledetail");
		} else {
			return mapping.findForward("fail");
		}
	}

	/**
	 * update role 废弃
	 * 
	 * @param mapping
	 *            ActionMapping
	 * @param form
	 *            ActionForm
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward updateRole(ActionMapping mapping, RoleManagerForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		//---------------START--角色管理写操作日志
		AccessControl control = AccessControl.getInstance();
		control.checkAccess(request,response);
		String operContent="";        
        String operSource=control.getMachinedID();
        String openModle="角色管理";
        String userName = control.getUserName();
        String description="";
        LogManager logManager = SecurityDatabase.getLogManager(); 		
		//---------------END
        
		RoleManager roleManager = SecurityDatabase.getRoleManager();
		RoleManagerForm roleForm = (RoleManagerForm) form;
		String roleDesc = roleForm.getRoleDesc();
		String roleName = roleForm.getRoleName();
		String roleId = roleForm.getRoleId();
		if (roleDesc != null && roleName != null && roleId != null) {
			Role role = new Role();
			role.setRoleDesc(roleDesc);
			role.setRoleName(roleName);
			role.setRoleId(roleId);
			roleManager.storeRole(role);
			
			//--角色管理写操作日志	
			operContent="更新角色: "+roleName;						
			description="";
			logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description);       
			//--
			
			return mapping.findForward("success");
		} else {
			return mapping.findForward("fail");
		}
	}

	/**
	 * delete role 废弃
	 * 
	 * @param mapping
	 *            ActionMapping
	 * @param form
	 *            ActionForm
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	
	public ActionForward deleteRole(ActionMapping mapping, RoleManagerForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		//---------------START--角色管理写操作日志
		AccessControl control = AccessControl.getInstance();
		control.checkAccess(request,response);
		String operContent="";        
        String operSource=control.getMachinedID();
        String openModle="角色管理";
        String userName = control.getUserName();
        String description="";
        LogManager logManager = SecurityDatabase.getLogManager(); 		
		//---------------END
        
		RoleManager roleManager = SecurityDatabase.getRoleManager();
		RoleManagerForm roleForm = (RoleManagerForm) form;
		String roleId = roleForm.getRoleId();
		String rid = request.getParameter("rid");
		if (rid != null) {
			roleId = rid;
		}
		Role role = roleManager.getRoleById( roleId);

		if (role != null) {
			if (role.getRoleName().equalsIgnoreCase(
					AccessControl.getAdministratorRoleName())) {
				return mapping.findForward("fail");
			}
			
			//--角色管理写操作日志	
			operContent="删除角色,角色："+role.getRoleName(); 
			description="";
			logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description);       
			//--
			
			roleManager.deleteRole(role);

			return mapping.findForward("success");
		} else {
			return mapping.findForward("fail");
		}

	}

	public ActionForward roleEdit(ActionMapping mapping, RoleManagerForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		//---------------START--角色管理写操作日志
		AccessControl control = AccessControl.getInstance();
		control.checkAccess(request,response);		
		int userId = Integer.parseInt(control.getUserID());		
		String operContent="";        
        String operSource=control.getMachinedID();
        String openModle="角色管理";
        String userName = control.getUserName();
        String description="";
        LogManager logManager = SecurityDatabase.getLogManager(); 		
		//---------------END
        
        
		RoleManager roleManager = SecurityDatabase.getRoleManager();
		
		RoleManagerForm roleForm = (RoleManagerForm) form;
		String roleId = roleForm.getRoleId();
		if(roleId == null)
		{
			roleId = "";
		}
		
		Role role = null;
		HttpSession session = request.getSession();
		boolean newTag = false;
		try {
			switch (roleForm.getAction()) {
			case 0:
				// 取组信息的操作
				String roleId1 = request.getParameter("roleId");
				if (roleId1 != null && roleId1.length() > 0) {
					role = roleManager.getRoleById(roleId1);
					if (role != null) {
						roleForm.setRoleId(role.getRoleId());
						roleForm.setRoleDesc(role.getRoleDesc());
						roleForm.setRoleName(role.getRoleName());
						session.setAttribute("currRoleId", role.getRoleId());
					}
				}
				break;

			case 1:
				// 新增、修改角色信息的操作
				role = new Role();
				role.setOwner_id(userId);
				role.setRoleDesc(roleForm.getRoleDesc());
				role.setRoleName(roleForm.getRoleName());
				role.setRoleType(roleForm.getRoleType());
				role.setRoleId(roleId);
				DBUtil db = new DBUtil();
				
				if(roleId.equals(""))//新增角色时判断角色名的冲突
				{
					String sql ="select count(*) from td_sm_role where role_name ='"+ roleForm.getRoleName() +"'";
					db.executeSelect(sql);
					if(db.getInt(0,0)>0){
						request.setAttribute("isRoleExist","true");
						return mapping.findForward("newrole");
					}
				}
				
				boolean tag = false;
				if(!(roleId.equals("")))
				{
					//修改处理
					roleManager.storeRole(role);
					tag = true;
				}
				else
				{
					//新增处理
					newTag = true;
					roleManager.insertRole(role);
					roleId = roleManager.getRoleByName(role.getRoleName()).getRoleId();
					tag = true;
				}

				if (tag) {
					roleForm.setRoleId(role.getRoleId());						
					//--角色管理写操作日志	
					operContent="新增（更新）角色: "+role.getRoleName(); 						
					description="";
					logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description);       
					//新增角色时给角色添加人赋权
					if(roleId.equals(""))
					{
						AccessControl accesscontroler = AccessControl.getInstance();
				        accesscontroler.checkAccess(request, response);
				        String roleid = accesscontroler.getUserID();
				        String resid = roleId;
				        String resname = role.getRoleName();
				        String opid1 = "write";
				        String opid2 = "read";
				        String resTypeid ="role";
				        roleManager.storeRoleresop(opid1,resid,roleid,resTypeid,resname,"user");
				    	roleManager.storeRoleresop(opid2,resid,roleid,resTypeid,resname,"user");
					}						
				}
				// 告诉页面已经保存
				request.setAttribute("iss", "1");
				break;

			case 2:
				// 删除角色信息的操作
				
				//--角色管理写操作日志	
				operContent="删除角色,角色： " + roleForm.getRoleName(); 						
				description="";
				logManager.log(control.getUserAccount()+":"+userName,operContent,openModle,operSource,description);       
				//--
				
				
				if(roleManager.deleteRoles(new String[] {roleId}))
				{
					session.setAttribute("deleteRole", "1");
					return mapping.findForward("noRole");
				}
				
				break;
			}
			if(newTag)
			{
				//add by ge.tao
				//date 2008-01-25
				//新增角色, 角色类别没有回写的问题
				session.removeAttribute("roleTypeid");
				session.setAttribute("roleTypeid",roleForm.getRoleType());
				//--end
				return mapping.findForward("newrole");
			}
			return mapping.findForward("roledetail");
		} catch (Exception e) {
			log.error(e);
			return mapping.findForward("fail");
		}
	}

	/**
	 * 角色查询
	 * 
	 * @param mapping
	 *            ActionMapping
	 * @param form
	 *            ActionForm
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward roleQuery(ActionMapping mapping, RoleManagerForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		RoleManagerForm roleForm = (RoleManagerForm) form;
		String roleName = roleForm.getRoleName();
		// 没有关键字
		if (roleName == null) {
			request.setAttribute("hasKey", "0");
			return mapping.findForward("roleQueryResult");
		} else {
			request.setAttribute("hasKey", "1");
			request.setAttribute("roleName", roleName);
			return mapping.findForward("roleQueryResult");
		}

	}

	/**
	 * query user
	 * 
	 * @param mapping
	 *            ActionMapping
	 * @param form
	 *            ActionForm
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward userQuery(ActionMapping mapping, RoleManagerForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		RoleManagerForm roleForm = (RoleManagerForm) form;
		String userName = roleForm.getUserName();
		// 没有关键字
		if (userName == null) {
			request.setAttribute("hasKey", "0");
			return mapping.findForward("userQueryResult");
		} else {
			request.setAttribute("hasKey", "1");
			request.setAttribute("userName", userName);
			return mapping.findForward("userQueryResult");
		}

	}

	/**
	 * 得到某一机构的用户列表和该机构下拥有角色的用户列表
	 * 
	 * @param mapping
	 *            ActionMapping
	 * @param form
	 *            ActionForm
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward getRoleList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		String roleId = (String) session.getAttribute("currRoleId");

		request.setAttribute("roleId", roleId);
		if (roleId == null) {
			return mapping.findForward("noRole");
		}
		String orgid = request.getParameter("orgId");
		
		UserManager userManager = SecurityDatabase.getUserManager();

		if (roleId != null && !roleId.equals("") && orgid != null && !orgid.equals("")) {
			
			//获取机构下的用户列表，并且安照机构/岗位序号和机构/岗位/人员序号排序
			 List allUser = userManager.getOrgUserList(orgid);
			 

//			String str ="select * from td_sm_user where user_id in " +
//					" (select user_id from td_sm_userrole where role_id='"+ roleId +"' and user_id in" +
//					" (select user_id from td_sm_userjoborg where org_id ='"+ orgid+"'))";
//			 根据角色得用户列表(当前机构下面)
			
//			List existUser = userManager.getUserList(str);
			List existUser = userManager.getUsersListOfRole(roleId);
			if (allUser == null) {
				allUser = new ArrayList();
			}
			if (existUser == null) {
				existUser = new ArrayList();
			}
			request.setAttribute("allUser", allUser);
			request.setAttribute("existUser", existUser);
		}
		// return mapping.findForward("roleUserList");
		return mapping.findForward("roleUserajax");
	}

//	/**
//	 * 保存用户的角色(userList.jsp右边的用户列表)
//	 * 
//	 * @param mapping
//	 *            ActionMapping
//	 * @param form
//	 *            ActionForm
//	 * @param request
//	 *            HttpServletRequest
//	 * @param response
//	 *            HttpServletResponse
//	 * @return ActionForward
//	 * @throws Exception
//	 */
//	public ActionForward storeUserList(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response)
//			throws Exception {
//		//---------------START--角色管理写操作日志
//		AccessControl control = AccessControl.getInstance();
//		control.checkAccess(request,response);
//		String operContent="";        
//        String operSource=control.getMachinedID();
//        String openModle="角色管理";
//        String userName = control.getUserName();
//        String description="";
//        LogManager logManager = SecurityDatabase.getLogManager(); 		
//		//---------------END
//		HttpSession session = request.getSession();
//		String roleId = (String) session.getAttribute("currRoleId");
//		if (roleId == null) {
//			return mapping.findForward("noRole");
//		}
//		RoleManagerForm roleForm = (RoleManagerForm) form;
//		RoleManager roleManager = SecurityDatabase.getRoleManager();
//		UserManager userManager = SecurityDatabase.getUserManager();
//		OrgManager orgManager = SecurityDatabase.getOrgManager();
//		String orgid = roleForm.getOrgId();
//		Organization org = orgManager.getOrgById( orgid);
//		Role role = roleManager.getRole("roleId", roleId);
//		String[] userList = roleForm.getUserIds();
//		String userids_log ="";
//		try {
//			if (role != null && org != null) {
//				userManager.deleteUserrole(org, role);
//				if (userList != null) {
//					for (int i = 0; i < userList.length; i++) {
//						User user = userManager.getUserById(userList[i]);
//						if (user != null) {
//							Userrole userrole = new Userrole();
//							userrole.setUser(user);
//							userrole.setRole(role);
//							userids_log +=LogGetNameById.getUserNameByUserId(userList[i])+" ";
//							userManager.storeUserrole(userrole);
//						}
//					}
//					//--角色管理写操作日志	
//					operContent="添加用户角色，用户："+userids_log+",角色:"+role.getRoleName();						
//					description="";
//					logManager.log(control.getUserAccount()+":"+userName,operContent,openModle,operSource,description);       
//					//--
//				}
//			}
//			List allUser = userManager.getUserList(org);
//			List existUser = userManager.getUserList(org, role);
//			request.setAttribute("allUser", allUser);
//			request.setAttribute("existUser", existUser);
//			return mapping.findForward("roleUserList");
//		} catch (Exception e) {
//			log.error(e);
//			return mapping.findForward("fail");
//		}
//	}

	

	/**
	 * 获取组织列表
	 * 
	 * @param mapping
	 *            ActionMapping
	 * @param form
	 *            ActionForm
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward getGroupListByRoleId(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			HttpSession session = request.getSession();
			String roleId = request.getParameter("roleId");
			String groupId = request.getParameter("groupId");
			// System.out.println("roleId........"+roleId);
			// System.out.println("groupId........"+groupId);

			// if (roleId == null) {
			// roleId = (String) session.getAttribute("currRoleId");
			// }
			// if (roleId == null) {
			// return mapping.findForward("noRole");
			// }
			//
			// session.setAttribute("currRoleId", roleId);
			// session.setAttribute("roleTabId", "4");

			Role role = new Role();
			role.setRoleId(roleId);
			GroupManager groupManager = SecurityDatabase.getGroupManager();
			Group group = groupManager.getGroupByID(groupId);
			List allGroup;
			if (groupManager.isContainChildGroup(group)) {
				allGroup = groupManager
						.getGroupList("select * from td_sm_Group o where o.PARENT_ID='"
								+ groupId + "' or o.GROUP_ID='" + groupId + "'");
			} else {
				allGroup = groupManager
						.getGroupList("select * from td_sm_Group o where o.GROUP_ID='"
								+ groupId + "'");
			}
			List existGroup = groupManager.getGroupList(role);

			request.setAttribute("allGroup", allGroup);
			request.setAttribute("existGroup", existGroup);

		} catch (Exception e) {
			// return mapping.findForward("fail");
		}

		return mapping.findForward("groupajax");

	}

	/**
	 * 存储变更后的组织
	 * 
	 * @param mapping
	 *            ActionMapping
	 * @param form
	 *            ActionForm
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward storeRoleGroup(ActionMapping mapping, RoleManagerForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		//---------------START--角色管理写操作日志
		AccessControl control = AccessControl.getInstance();
		control.checkAccess(request,response);
		String operContent="";        
        String operSource=control.getMachinedID();
        String openModle="角色管理";
        String userName = control.getUserName();
        String description="";
        LogManager logManager = SecurityDatabase.getLogManager(); 		
		//---------------END
		
		HttpSession session = request.getSession();
		String roleId = request.getParameter("roleId");

		if (roleId == null) {
			roleId = (String) session.getAttribute("currRoleId");
		}
		if (roleId == null) {
			return mapping.findForward("noRole");
		}

		RoleManager roleManager = SecurityDatabase.getRoleManager();
		Role role = roleManager.getRoleById(roleId);

		session.setAttribute("currRoleId", roleId);
		session.setAttribute("roleTabId", "4");

		GroupManager groupManager = SecurityDatabase.getGroupManager();
		try {
			if (role != null) {
				groupManager.deleteGrouprole(role);
				RoleManagerForm roleManagerForm = (RoleManagerForm) form;
				String[] groupid = roleManagerForm.getGroupId();

				for (int i = 0; (groupid != null) && (i < groupid.length); i++) {
					Group group = groupManager.getGroupByID(groupid[i]);

					if (group != null) {
						Grouprole grouprole = new Grouprole();
						grouprole.setRole(role);
						grouprole.setGroup(group);
						groupManager.storeGrouprole(grouprole);
						
						
						//--角色管理写操作日志	
						operContent="添加组角色实例: "+ group.getGroupName();						
						description="";
						logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description);       
						//--
					}
				}
			}
			// no selected role
			else {
				return mapping.findForward("noRole");
			}

			List existGroup = groupManager.getGroupList(role);
			List allGroup = groupManager.getGroupList();
			request.setAttribute("allGroup", allGroup);
			request.setAttribute("existGroup", existGroup);
			return mapping.findForward("changeGroup");
		} catch (Exception e) {
			log.error(e);
			return mapping.findForward("fail");
		}
	}

	/**
	 * 保存角色操作信息。orgjobuserList.jsp---没用的方法
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward storeRoleResOpList(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		
		//---------------START--角色管理写操作日志
//		AccessControl control = AccessControl.getInstance();
//		control.checkAccess(request,response);
//		String operContent="";        
//        String operSource=control.getMachinedID();
//        String openModle="角色管理";
//        String userName = control.getUserName();
//        String description="";
//        LogManager logManager = SecurityDatabase.getLogManager(); 		
//		//---------------END
//		
//		HttpSession session = request.getSession();
//		String roleId = (String) session.getAttribute("currRoleId");
//		String resTypeId = request.getParameter("resTypeId");
//		String resId = request.getParameter("resId");
//		String orgId = request.getParameter("orgId");
//		// System.out.println("............." + roleId);
//
//		if (roleId == null) {
//			return mapping.findForward("noRole");
//		}
//		RoleManagerForm roleForm = (RoleManagerForm) form;
//		String[] userList = roleForm.getUserIds();
//		String[] alloper = roleForm.getAlloper();
//
//		RoleManager roleManager = SecurityDatabase.getRoleManager();
//		// UserManager userManager = SecurityDatabase.getUserManager();
//		Role role = roleManager.getRoleById(roleId);
//		try {
//			if (role != null) {
//				//
//				for (int i = 0; userList != null && i < userList.length; i++) {
//					// User user = userManager.getUser("userId", userList[i]);
//					roleManager.deleteRoleresop(resId + ":" + userList[i],
//							roleId, "user");
//					for (int j = 0; alloper != null && j < alloper.length; j++) {
////						Roleresop o = new Roleresop();
////
////						o.getId().setOpId(alloper[j]);
////						o.getId().setResId(resId + ":" + userList[i]);
////						o.getId().setRestypeId("user");
////						o.getId().setRoleId(roleId);
////						roleManager.storeRoleresop(o);
//						roleManager.storeRoleresop(alloper[j],resId + ":" + userList[i],roleId,"user","","role");
//						//--角色管理写操作日志	
//						operContent="添加角色资源操作对象，角色编号： "+LogGetNameById.getRoleNameByRoleId(roleId);						
//						description="";
//						logManager.log(control.getUserAccount()+":"+userName,operContent,openModle,operSource,description);       
//						//--
//						
//					}
//
//				}
//			}
//			request.setAttribute("resTypeId", resTypeId);
//			request.setAttribute("resId", resId);
//			request.setAttribute("orgId", orgId);
//			request.setAttribute("flag", "1");
//			return mapping.findForward("orgjobuserlist");
//		} catch (Exception e) {
//			log.error(e);
//			return mapping.findForward("fail");
//		}
		return null;
	}

	

	/**
	 * 得到机构列表和该角色下拥有的机构列表 2006-04-20
	 * 
	 * @param mapping
	 *            ActionMapping
	 * @param form
	 *            ActionForm
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward getOrgList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		String roleId = (String) session.getAttribute("currRoleId");
		request.setAttribute("roleId", roleId);

		String orgId = request.getParameter("orgId");
		// System.out.println("orgId........"+orgId);
		if (orgId == null)
			orgId = "0";
		OrgManager orgManager = SecurityDatabase.getOrgManager();

		Role role = new Role();
		role.setRoleId(roleId);
		List existOrg = orgManager.getOrgList(role);// 该角色关联机构
		List allOrg;
		Organization org = new Organization();
		org.setOrgId(orgId);
//		orgManager.isContainChildOrg(org);// 检查指定的机构是否包含子机构
//		if (orgManager.isContainChildOrg(org)) {
//			allOrg = orgManager
//					.getOrgList("from Organization o where o.parentId='"
//							+ orgId + "'");
//		} else {
//			allOrg = orgManager
//					.getOrgList("from Organization o where o.orgId='" + orgId
//							+ "'");
//		}
		allOrg = orgManager.getOrgList("select * from td_sm_organization o where " +
				" o.parentId='" + orgId + "' or o.orgId='" + orgId +"'");
		request.setAttribute("existOrg", existOrg);
		request.setAttribute("allOrg", allOrg);

		// return mapping.findForward("roleorg"); //button
		return mapping.findForward("orgajax"); // ajax
	}

	public ActionForward storeRoleOrg(ActionMapping mapping, RoleOrgForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//---------------START--角色管理写操作日志
		AccessControl control = AccessControl.getInstance();
		control.checkAccess(request,response);
		String operContent="";        
        String operSource=control.getMachinedID();
        String openModle="角色管理";
        String userName = control.getUserName();
        String description="";
        LogManager logManager = SecurityDatabase.getLogManager(); 		
		//---------------END
		
		String roleid = request.getParameter("roleId");
		RoleManager roleManager = SecurityDatabase.getRoleManager();
		OrgManager orgManager = SecurityDatabase.getOrgManager();

		RoleOrgForm roleOrgForm = (RoleOrgForm) form;
		Role role = roleManager.getRoleById(roleid);

		roleManager.deleteOrgrole(role);

		String[] orgid = roleOrgForm.getOrgId();

		for (int i = 0; (orgid != null) && (i < orgid.length); i++) {
			Organization org = orgManager.getOrgById(orgid[i]);

			if (org != null) {
				Orgrole orgrole = new Orgrole();
				orgrole.setOrganization(org);
				orgrole.setRole(role);
				orgManager.storeOrgrole(orgrole);
				 
				
				//--角色管理写操作日志	
				operContent="添加机构组对象: "+org.getOrgName();		
				description="";
				logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description);       
				//--
			}
		}

		List existOrg = orgManager.getOrgList(role);
		List allOrg = orgManager.getOrgList();
		request.setAttribute("existOrg", existOrg);
		request.setAttribute("allOrg", allOrg);

		return mapping.findForward("roleorg");

	}

	/**
	 * 存储变更后的组织--ajax 2006-04-20
	 */
	public static String storeRoleGroupAjax(String roleId, String[] groupIds) {

		try {
			GroupManager groupManager = SecurityDatabase.getGroupManager();
			RoleManager roleManager = SecurityDatabase.getRoleManager();
			// System.out.println("...."+roleId);
			// System.out.println("...."+groupIds);

			Role role = roleManager.getRoleById(roleId);
			groupManager.deleteGrouprole(role);

			for (int i = 0; (groupIds != null) && (i < groupIds.length); i++) {
				Group group = groupManager.getGroupByID(groupIds[i]);
				if (group != null) {
					Grouprole gr = new Grouprole();
					gr.setGroup(group);
					gr.setRole(role);
					groupManager.storeGrouprole(gr);

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
		return "success";
	}
	
	/**
	 * 角色赋予用户组，更改原来的先删后存的方式
	 * 这是存的接口
	 * 危达
	 * 200711071917
	 * */
	public static boolean addRoleGroup(String roleId, String groupId){
		return false;
	}
	
	/**
	 * 角色赋予用户组，更改原来的先删后存的方式
	 * 这是删的接口
	 * 危达
	 * 200711071920
	 * */
	public static boolean deleteRoleGroup(String roleId, String groupId){
		return false;
	}
	

	/**
	 * 存储变更后的机构--ajax 2006-04-21
	 */
	public static String storeRoleOrgAjax(String roleId, String[] orgIds) {

		try {
			OrgManager orgManager = SecurityDatabase.getOrgManager();
			RoleManager roleManager = SecurityDatabase.getRoleManager();

			Role role = roleManager.getRoleById(roleId);
			roleManager.deleteOrgrole(role);

			for (int i = 0; (orgIds != null) && (i < orgIds.length); i++) {
				Organization org = orgManager.getOrgById(orgIds[i]);
				if (org != null) {
					Orgrole or = new Orgrole();
					or.setOrganization(org);
					or.setRole(role);
					orgManager.storeOrgrole(or);

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
		return "success";
	}

	/**
	 * 存储变更后的用户--ajax 2006-04-21
	 */
	public static String storeRoleUserAjax(String roleId, String[] userIds,
			String orgId) {

		try {
			RoleManager roleManager = SecurityDatabase.getRoleManager();
			roleManager.deleteUserOfRole(roleId);
			roleManager.grantRoleToUsers(userIds,roleId);
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
		return "success";
	}

	/**
	 * 存储变更后的角色资源操作--ajax 2006-04-21
	 */
	public static String storeOperListAjax(String resId, String resTypeId,
			String opId, String roleId, String Flag, String resName,
			String role_type) {
			try {
			RoleManager roleManager = SecurityDatabase.getRoleManager();
			
//
//			Roleresop o = new Roleresop();
//			o.getId().setRoleId(roleId);
//			o.getId().setOpId(opId);
//			o.getId().setResId(resId);
//			o.getId().setRestypeId(resTypeId);
//			o.setResName(resName);
//			o.setTypes(role_type);
//			Roleresop s = operManager.getRoleresop(o);
			if (Flag != null && Flag.equals("1")) {
				roleManager.storeRoleresop(opId,resId,roleId,resTypeId,resName,role_type);
			} else if (Flag != null && Flag.equals("0")) {
				roleManager.deletePermissionOfRole(resId,resTypeId,roleId,role_type);
			}
			return "success";

		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}

	}

	/**
	 * 获得资源对应的所有角色及操作列表
	 *没有被使用的方法 
	 * @param resId
	 * @param restypeId
	 * @return
	 */
	
//	public static List getRoleOperList(String resId, String restypeId) {
//		List list = null;
//		try {
//			OperManager om = SecurityDatabase.getOperManager();
//			list = om.getRoleOperList(resId, restypeId);
//		} catch (Exception e) {
//			log.error(e);
//			list = null;
//		}
//		return list;
//	}

	/**
	 * 保存角色操作信息。orgjobuserList.jsp ajax
	 */
	public static String saveURRO(String orgId, String resId, String roleId,
		String opId, String Flag, String role_type) {
		String[] tmp = opId.split(";");
		String resid = resId + ":" + tmp[0];
		String opid = tmp[1];
		String resname = tmp[2];
		if (tmp != null && tmp.length == 3) {

			try {
				RoleManager roleManager = SecurityDatabase.getRoleManager();
				

//				Roleresop o = new Roleresop();
//				o.getId().setRoleId(roleId);
//				o.getId().setOpId(tmp[1]);
//				o.getId().setResId(resId + ":" + tmp[0]);
//				o.getId().setRestypeId("user");
//				o.setResName(tmp[2]);
//				o.setTypes(role_type);
//				Roleresop s = operManager.getRoleresop(o);
				if (Flag != null && Flag.equals("1") ) {
					roleManager.storeRoleresop(opid,resId,roleId,"user",resname,role_type);
				} else if (Flag != null && Flag.equals("0")) {
					roleManager.deletePermissionOfRole(resId,"user",roleId,role_type);
				}
				return "success";

			} catch (Exception e) {
				e.printStackTrace();
				return "fail";
			}
		} else
			return "error";
	}

	/**
	 * 角色管理＝资源操作授予＝机构授权，包括是否递归 add 
	 * 没有确定按钮
	 * @param resId
	 * @param resTypeId
	 * @param opId
	 * @param roleId
	 * @param Flag
	 * @param isRecursion
	 * @return
	 */
	public static String storeRoleOrgAjax(String resId, String resTypeId,
			String opId, String[] roleId, String Flag, String isRecursion,
			String resName, String role_type) {
		// Role role = roleManager.getRole("roleId", roleId);
		// System.out.println("resTypeId=" +resTypeId);
		// System.out.println("resId=" +resId);
		// System.out.println("opId=" +opId);
		// System.out.println(roleId);
		// System.out.println(resName);
		
		try {
			if (roleId != null) {
				for (int k = 0; k < roleId.length; k++){
					RoleManager roleManager = SecurityDatabase.getRoleManager();
					
					OrgManager orgManager = SecurityDatabase.getOrgManager();
					Roleresop o = new Roleresop();
//					o.getId().setRoleId(roleId[k]);
//					o.getId().setOpId(opId);
//					o.getId().setResId(resId);
//					o.getId().setRestypeId(resTypeId);
//					o.setResName(resName);
//
//					o.setTypes(role_type);
//					// 检查rro对象是否存在
//					Roleresop s = operManager.getRoleresop(o);
//					// 给子机构授权时，父机构自动拥有可见的权限

					
					// 授权当前机构
					if (Flag != null && Flag.equals("1")) {
						roleManager.storeRoleresop(opId,resId,roleId[k],resTypeId,resName,role_type);
					} else if (Flag != null && Flag.equals("0")) {
						roleManager.deletePermissionOfRole(resId,resTypeId,roleId[k],role_type);
					}
					// 递归授权机构的权限
					if (isRecursion.equals("1")) {
						Organization org = orgManager.getOrgById(resId);
						if (org != null) {
							List suborg = orgManager.getChildOrgList(org, true);
							for (int i = 0; suborg != null && i < suborg.size(); i++) {
								Organization so = (Organization) suborg.get(i);
//								Roleresop rro = new Roleresop();
//								rro.getId().setRoleId(roleId[k]);
//								rro.getId().setOpId(opId);
//								rro.getId().setResId(so.getOrgId());
//								rro.getId().setRestypeId(resTypeId);
//								rro.setResName(so.getOrgName());
//								rro.setTypes(role_type);
//								Roleresop rs = operManager.getRoleresop(rro);
								// 授权当前机构
								if (Flag != null && Flag.equals("1")) {
									roleManager.storeRoleresop(opId,so.getOrgId(),roleId[k],resTypeId,so.getOrgName(),role_type);
								} else if (Flag != null && Flag.equals("0")) {
									roleManager.deletePermissionOfRole(so.getOrgId(),resTypeId,roleId[k],role_type);
								}
							}
						  }
						}
					}
				}
			
			return "success";

		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}

	}

	/**
	 * 角色管理＝资源操作授予＝菜单授权，包括是否递归 add 王卓
	 * 
	 * @param resId
	 * @param resTypeId
	 * @param opId
	 * @param roleId
	 * @param Flag
	 * @param isRecursion
	 * @return
	 */
	public static String storeColumnAjax(String resId, String resTypeId,
			String opId, String[] roleId, String Flag, String isRecursion,
			String resName, String menuPath, String role_type) {
		AccessControl control = AccessControl.getAccessControl(); 
		HttpServletRequest request = control.getRequest();
		try {
			if (roleId != null) {
				for (int k = 0; k < roleId.length; k++){
					RoleManager roleManager = SecurityDatabase.getRoleManager();
					
					// 授权当前菜单
					if (Flag != null && Flag.equals("1")) {
						roleManager.storeRoleresop(opId,resId,roleId[k],resTypeId,resName,role_type);
					} else if (Flag != null && Flag.equals("0")) {
						roleManager.deletePermissionOfRole(resId,resTypeId,roleId[k],role_type);
					}

//					 递归授权菜单的权限
					if (isRecursion.equals("1")) {
						ModuleQueue modules = Framework.getInstance().getSubModules(
								menuPath);
						ItemQueue items = Framework.getInstance().getSubItems(menuPath);
						for (int i = 0; items != null && i < items.size(); i++) {
							Item item = items.getItem(i);
							String resname = item.getName(request);
						
							// 子栏目授权
							storeColumnAjax(item.getId(), resTypeId, opId,roleId, 
									Flag, isRecursion,resname,item.getPath(),role_type);
						
							}

						for (int i = 0; modules != null && i < modules.size(); i++) {
							Module module = modules.getModule(i);
							module.getId();
							String resname = module.getName(request);
						
							// 子模块授权
							storeColumnAjax(module.getId(), resTypeId, opId,roleId, 
									Flag, isRecursion,resname,module.getPath(),role_type);
							
						}
					}
				}
			}
		

			return "success";

		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}

	}

	/**
	 * 机构管理岗位设置中调入，保存用户机构岗位的关系
	 * 
	 */
	public ActionForward storeuserList(ActionMapping mapping, RoleManagerForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		//---------------START--角色管理写操作日志
		AccessControl control = AccessControl.getInstance();
		control.checkAccess(request,response);
		String operContent="";        
        String operSource=control.getMachinedID();
        String openModle="角色管理";
        String userName = control.getUserName();
        String description="";
        LogManager logManager = SecurityDatabase.getLogManager(); 		
		//---------------END
        
		String orgId1 = request.getParameter("orgId1");
		String orgId = request.getParameter("orgId");
		String jobId = request.getParameter("jobId");
		// System.out.println("orgId1........"+orgId1);
		// System.out.println("jobId........"+jobId);
		// System.out.println("orgId........"+orgId);
		RoleManagerForm roleForm = (RoleManagerForm) form;
		JobManager jobManager = SecurityDatabase.getJobManager();
		UserManager userManager = SecurityDatabase.getUserManager();
		OrgManager orgManager = SecurityDatabase.getOrgManager();

		Organization org = orgManager.getOrgById(orgId1);
//		Job job = jobManager.getJob("jobId", jobId);
		Job job = jobManager.getJobById(jobId);
//		Job job1 = jobManager.getJob("jobId", "1");
		Job job1 = jobManager.getJobById("1");
		userManager.deleteUserjoborg(job1, org);
		userManager.deleteUserjoborg(job, org);

		String[] userList = roleForm.getUserIds();
		if (userList != null) {
			for (int i = 0; i < userList.length; i++) {

				User user = userManager.getUserById(userList[i]);
				if (user != null) {
					Userjoborg ujo = new Userjoborg();
					ujo.setUser(user);
					ujo.setOrg(org);
					ujo.setJob(job);
					ujo.setJobSn(new Integer(0));
					ujo.setSameJobUserSn(new Integer(0));
					// userManager.deleteUserjoborg(job,org);
					userManager.storeUserjoborg(ujo);
				}
			}
			//--角色管理写操作日志	
			operContent="添加用户机构岗位的关系，用户："+LogGetNameById.getUserNamesByUserIds(userList)+",机构:"+LogGetNameById.getOrgNameByOrgId(orgId1)+",岗位:"+LogGetNameById.getJobNameByJobId(jobId);						
			description="";
			logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description);       
			//--
		}
		return null;
	}

	/**
	 * 用户管理隶属机构调入，保存用户机构岗位的关系
	 * 
	 */
	public ActionForward storeUserOrg(ActionMapping mapping, RoleOrgForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		//---------------START--角色管理写操作日志
		AccessControl control = AccessControl.getInstance();
		control.checkAccess(request,response);
		String operContent="";        
        String operSource=control.getMachinedID();
        String openModle="角色管理";
        String userName = control.getUserName();
        String description="";
        LogManager logManager = SecurityDatabase.getLogManager(); 		
		//---------------END
        
		String userId = request.getParameter("uid");
		// System.out.println("userId........."+userId);

		OrgManager orgManager = SecurityDatabase.getOrgManager();
		UserManager userManager = SecurityDatabase.getUserManager();
		JobManager jobManager = SecurityDatabase.getJobManager();
//		Job job = jobManager.getJob("jobId", "1");
		Job job = jobManager.getJobById("1");
		User user = userManager.getUserById(userId);
		RoleOrgForm roleOrgForm = (RoleOrgForm) form;

		// userManager.deleteUserjoborg(user);

		String[] orgid = roleOrgForm.getOrgId();
		String orgids = "";


		for (int i = 0; (orgid != null) && (i < orgid.length); i++) {
			// System.out.println("orgId......"+orgid[i]);
			Organization org = orgManager.getOrgById(orgid[i]);

			if (org != null) {
				Userjoborg ujo = new Userjoborg();
				ujo.setOrg(org);
				ujo.setJob(job);
				ujo.setUser(user);
				ujo.setJobSn(new Integer(0));
				ujo.setSameJobUserSn(new Integer(0));
				userManager.storeUserjoborg(ujo);
				orgids +=","+LogGetNameById.getOrgNameByOrgId(orgid[i]);
			}
		}
		//--角色管理写操作日志	
		if(orgid.length > 0)
		{
		operContent="添加用户机构岗位的关系,用户，"+user.getUserName()+",机构编号："+orgids+" ,岗位:"+job.getJobName();						
		description="";
		logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description);    
		}
		//--

		return null;

	}

	public ActionForward saveCopy(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//---------------START--角色管理写操作日志
		AccessControl control = AccessControl.getInstance();
		control.checkAccess(request,response);
		String operContent="";        
        String operSource=control.getMachinedID();
        String openModle="角色管理";
        String userName = control.getUserName();
        String description="";
        LogManager logManager = SecurityDatabase.getLogManager(); 		
		//---------------END
	    
		String rolecopyId = request.getParameter("rolecopyId");
		String[] id = request.getParameterValues("checkBoxOne");
		request.setAttribute("roleId", rolecopyId);
		RoleManager roleManager = SecurityDatabase.getRoleManager();
		roleManager.copyResOfRole(rolecopyId,id);
		//--角色管理写操作日志	
		operContent="复制角色: "+	LogGetNameById.getRoleNameByRoleId(rolecopyId);			
		description="";
		logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description);       
		//--
		
		return mapping.findForward("rolecopy");
	}
	
	/**
	 * 角色管理＝资源操作授予＝机构授权，包括是否递归 add 王卓
	 * 有确定按钮
	 */
	public ActionForward storeAuthorization(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			String[] opid = request.getParameterValues("alloper");
			String isRecursion = request.getParameter("isRecursion");
			
			String resTypeId = request.getParameter("resTypeId");
			String resid = request.getParameter("resid");
			String roleid = request.getParameter("roleid");
			String[] tmp = roleid.split(",");
			String role_type = request.getParameter("role_type");
			request.setAttribute("resTypeId",resTypeId);
			request.setAttribute("resId",resid);
			request.setAttribute("isOk","1");
			HttpSession session = request.getSession();
			session.setAttribute("currRoleId", roleid);
			session.setAttribute("role_type", role_type);
			DBUtil db = new DBUtil();
			OrgManager orgManager = SecurityDatabase.getOrgManager();
			RoleManager roleManager = SecurityDatabase.getRoleManager();
			if(resid.equals("lisan")){
				for(int k=0;k<tmp.length;k++){
					if(opid!=null && isRecursion.equals("0")){
						roleManager.deletePermissionOfRole(resid,resTypeId,tmp[k],role_type);
						roleManager.storeRoleresop(opid,resid,tmp[k],resTypeId,"lisan",role_type);
						
					}
				}
			}
			else if(resid.equals("00"))
			{
				for(int k=0;k<tmp.length;k++){
					//根据机构id取机构名称
					Organization root = new Organization();
					root.setOrgId("0");
				
					String resname = "添加一级机构";
					request.setAttribute("resName","添加一级机构");
					//操作和递归为空的话删除当前机构所有操作
					if(opid==null && isRecursion.equals("0")){
						roleManager.deletePermissionOfRole(resid,resTypeId,tmp[k],role_type);
						
					}
					//操作不为空和递归为空的话授权当前机构相关操作（先删后存）
					if(opid!=null && isRecursion.equals("0")){
						roleManager.deletePermissionOfRole(resid,resTypeId,tmp[k],role_type);
						roleManager.storeRoleresop(opid,resid,tmp[k],resTypeId,resname,role_type);						
					}
					
				}
			}
			else
			{
				for(int k=0;k<tmp.length;k++){
					//根据机构id取机构名称					
				
					Organization org = orgManager.getOrgById(resid);
					String resname = org.getOrgName();
					request.setAttribute("resName",resname);
					//操作和递归为空的话删除当前机构所有操作
					if(opid==null && isRecursion.equals("0")){
						roleManager.deletePermissionOfRole(resid,resTypeId,tmp[k],role_type);
						
					}
					//操作为空和递归不为空的话删除当前机构和子机构所有操作
					//opid==null && 去掉
					//modify by ge.tao
					//2007-09-17
					//对于所有的操作, 先删除当前的授权情况, 然后加载新的授权情况.
					if(isRecursion.equals("1")){
						roleManager.deletePermissionOfRole(resid,resTypeId,tmp[k],role_type);
						List subsite = orgManager.getChildOrgList(org,true);
						for (int i = 0; subsite != null && i < subsite.size(); i++) {
							Organization org1 = (Organization) subsite.get(i);
							
							roleManager.deletePermissionOfRole(""+org1.getOrgId(),resTypeId,tmp[k],role_type);
						
						}
					}
					//操作不为空和递归为空的话授权当前机构相关操作（先删后存）
					if(opid!=null && isRecursion.equals("0")){
						roleManager.deletePermissionOfRole(resid,resTypeId,tmp[k],role_type);
						roleManager.storeRoleresop(opid,resid,tmp[k],resTypeId,resname,role_type);
						//递归给该机构的父机构授只读权限
						String str="SELECT t.org_id FROM TD_SM_ORGANIZATION t START WITH "+
						" t.org_id='"+resid+"' CONNECT BY PRIOR t.PARENT_ID=t.org_id";
						db.executeSelect(str);
						if(db.size()>1){
							for(int j=0;j<db.size();j++)
							{
								String parentid = db.getString(j,"org_id");
								
								roleManager.storeRoleresop("read",parentid,tmp[k],resTypeId,resname,role_type);
							
							} 
						}
						
					}
					//操作不为空和递归不为空的话授权当前机构及子机构相关操作（先删后存）
					if(opid!=null && isRecursion.equals("1")){
					
						List subsite = orgManager.getChildOrgList(org,true);
						for (int i = 0; subsite != null && i < subsite.size(); i++) {
							Organization org1 = (Organization) subsite.get(i);
							String sql ="select count(*) as num from td_sm_roleresop where " +
									"res_id='"+""+org1.getOrgId()+"' and restype_id='"+ resTypeId +"'" +
									" and role_id ='"+ tmp[k] +"' and types='"+ role_type +"'";
							db.executeSelect(sql);
							if(db.getInt(0,"num")==0){
								roleManager.storeRoleresop(opid,""+org1.getOrgId(),tmp[k],resTypeId,org1.getOrgName(),role_type);
							}
							
						}
							roleManager.deletePermissionOfRole(resid,resTypeId,tmp[k],role_type);
							roleManager.storeRoleresop(opid,resid,tmp[k],resTypeId,resname,role_type);
							//递归给该机构的父机构授只读权限
							String str="SELECT t.org_id FROM TD_SM_ORGANIZATION t START WITH "+
							" t.org_id='"+resid+"' CONNECT BY PRIOR t.PARENT_ID=t.org_id";
							db.executeSelect(str);
							if(db.size()>1){
								for(int j=0;j<db.size();j++)
								{
									String parentid = db.getString(j,"org_id");
									roleManager.storeRoleresop("read",parentid,tmp[k],resTypeId,resname,role_type);
								
								}
							}
					}
					
				}
			}
	
		
			return mapping.findForward("oporg");
	}
	
	/**
	 * 角色管理-->资源操作授予 选择多个机构的保存 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 * RoleManagerAction.java
	 * @author: ge.tao
	 */
	public ActionForward storeAuthorizationMutiOrgId(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		    //多个机构
			String orgIds = request.getParameter("orgIds");
			String orgIdArr[] = orgIds.split(",");
		    
			String[] opid = request.getParameterValues("alloper");
			String isRecursion = request.getParameter("isRecursion");			
			String resTypeId = request.getParameter("resTypeId");
			String roleid = request.getParameter("roleid");
			String[] tmp = roleid.split(",");
			String role_type = request.getParameter("role_type");
			request.setAttribute("resTypeId",resTypeId);			
			request.setAttribute("isOk","1");
			request.setAttribute("resId",orgIds);
			
			HttpSession session = request.getSession();
			session.setAttribute("currRoleId", roleid);
			session.setAttribute("role_type", role_type);
			
			DBUtil db = new DBUtil();
			OrgManager orgManager = SecurityDatabase.getOrgManager();
			RoleManager roleManager = SecurityDatabase.getRoleManager();
			
			for(int o=0;o<orgIdArr.length;o++){
				//机构ID 单个机构的ID
				String resid = orgIdArr[o];
				if(resid.equals("lisan")){
					for(int k=0;k<tmp.length;k++){
						if(opid!=null && isRecursion.equals("0")){
							roleManager.deletePermissionOfRole(resid,resTypeId,tmp[k],role_type);
							roleManager.storeRoleresop(opid,resid,tmp[k],resTypeId,"lisan",role_type);
							
						}
					}
				}
				else if(resid.equals("00"))
				{
					for(int k=0;k<tmp.length;k++){
						//根据机构id取机构名称
						Organization root = new Organization();
						root.setOrgId("0");
					
						String resname = "添加一级机构";
						request.setAttribute("resName","添加一级机构");
						//操作和递归为空的话删除当前机构所有操作
						if(opid==null && isRecursion.equals("0")){
							roleManager.deletePermissionOfRole(resid,resTypeId,tmp[k],role_type);
							
						}
						//操作不为空和递归为空的话授权当前机构相关操作（先删后存）
						if(opid!=null && isRecursion.equals("0")){
							roleManager.deletePermissionOfRole(resid,resTypeId,tmp[k],role_type);
							roleManager.storeRoleresop(opid,resid,tmp[k],resTypeId,resname,role_type);						
						}
						
					}
				}
				else
				{
					for(int k=0;k<tmp.length;k++){
						//根据机构id取机构名称					
					
						Organization org = orgManager.getOrgById(resid);
						String resname = org.getOrgName();
						request.setAttribute("resName",resname);
						//操作和递归为空的话删除当前机构所有操作
						if(opid==null && isRecursion.equals("0")){
							roleManager.deletePermissionOfRole(resid,resTypeId,tmp[k],role_type);
							
						}
						//操作为空和递归不为空的话删除当前机构和子机构所有操作
						//opid==null && 去掉
						//modify by ge.tao
						//2007-09-17
						//对于所有的操作, 先删除当前的授权情况, 然后加载新的授权情况.
						if(isRecursion.equals("1")){
							roleManager.deletePermissionOfRole(resid,resTypeId,tmp[k],role_type);
							List subsite = orgManager.getChildOrgList(org,true);
							for (int i = 0; subsite != null && i < subsite.size(); i++) {
								Organization org1 = (Organization) subsite.get(i);
								
								roleManager.deletePermissionOfRole(""+org1.getOrgId(),resTypeId,tmp[k],role_type);
							
							}
						}
						//操作不为空和递归为空的话授权当前机构相关操作（先删后存）
						if(opid!=null && isRecursion.equals("0")){
							roleManager.deletePermissionOfRole(resid,resTypeId,tmp[k],role_type);
							roleManager.storeRoleresop(opid,resid,tmp[k],resTypeId,resname,role_type);
							//递归给该机构的父机构授只读权限
							String str="SELECT t.org_id,t.org_name,t.remark5 FROM TD_SM_ORGANIZATION t START WITH "+
							" t.org_id='"+resid+"' CONNECT BY PRIOR t.PARENT_ID=t.org_id";
							db.executeSelect(str);
							if(db.size()>1){
								for(int j=0;j<db.size();j++)
								{
									String parentid = db.getString(j,"org_id");
									String parentresname = null;
									if(db.getString(j, "remark5") != null || !"".equals(db.getString(j, "remark5"))){
										parentresname = db.getString(j, "remark5");
									}else{
										parentresname = db.getString(j, "org_name");
									}
									roleManager.storeRoleresop("read",parentid,tmp[k],resTypeId,parentresname,role_type);
								
								}
							}
							
						}
						//操作不为空和递归不为空的话授权当前机构及子机构相关操作（先删后存）
						if(opid!=null && isRecursion.equals("1")){
						
							List subsite = orgManager.getChildOrgList(org,true);
							for (int i = 0; subsite != null && i < subsite.size(); i++) {
								Organization org1 = (Organization) subsite.get(i);
								String sql ="select count(*) as num from td_sm_roleresop where " +
										"res_id='"+""+org1.getOrgId()+"' and restype_id='"+ resTypeId +"'" +
										" and role_id ='"+ tmp[k] +"' and types='"+ role_type +"'";
								db.executeSelect(sql);
								if(db.getInt(0,"num")==0){
									roleManager.storeRoleresop(opid,""+org1.getOrgId(),tmp[k],resTypeId,org1.getOrgName(),role_type);
								}
								
							}
								roleManager.deletePermissionOfRole(resid,resTypeId,tmp[k],role_type);
								roleManager.storeRoleresop(opid,resid,tmp[k],resTypeId,resname,role_type);
								//递归给该机构的父机构授只读权限
								String str="SELECT t.org_id,t.org_name,t.remark5 FROM TD_SM_ORGANIZATION t START WITH "+
								" t.org_id='"+resid+"' CONNECT BY PRIOR t.PARENT_ID=t.org_id";
								db.executeSelect(str);
								if(db.size()>1){
									for(int j=0;j<db.size();j++)
									{
										String parentid = db.getString(j,"org_id");
										String parentresname = null;
										if(db.getString(j, "remark5") != null || !"".equals(db.getString(j, "remark5"))){
											parentresname = db.getString(j, "remark5");
										}else{
											parentresname = db.getString(j, "org_name");
										}
										roleManager.storeRoleresop("read",parentid,tmp[k],resTypeId,parentresname,role_type);
									
									}
								}
						}
						
					}
				}
	        
			}
		
			return mapping.findForward("oporg");
	}
	
	/**
	 * 实现多个角色和多个操作的直接复权
	 * 先删后存--以后优化----------2007.12.10
	 * 角色管理＝资源操作授予＝菜单授权，包括是否递归 add 王卓
	 * 有确定按钮
	 */
	public ActionForward menuAuthorization(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			String[] opid = request.getParameterValues("alloper");
			/**
			 * 0标识不递归
			 * 1标识递归
			 */
			String isRecursion = request.getParameter("isRecursion");
			
			AccessControl accessControl = AccessControl.getInstance();
			accessControl.checkAccess(request,response);
			String resTypeId = request.getParameter("resTypeId");
//			String resTypeId2 = request.getParameter("resTypeId2");
			String resid = request.getParameter("resid");
			String roleid = request.getParameter("roleid");
			String resName = request.getParameter("resName");
			System.out.println("resName:"+resName);
			/**
			 * 角色id
			 */
			String[] tmp = roleid.split(",");
			String role_type = request.getParameter("role_type");
			String menuPath =  request.getParameter("menuPath");
		
			request.setAttribute("resTypeId",resTypeId);
			request.setAttribute("resId",resid);
			request.setAttribute("isOk","1");
			HttpSession session = request.getSession();
			session.setAttribute("currRoleId", roleid);
			session.setAttribute("role_type", role_type);
			
			
			RoleManager roleManager = SecurityDatabase.getRoleManager();
			List resids = new ArrayList();
			
//				操作和递归为空的话删除当前菜单所有操作,没有选择操作
			if(opid==null || opid.length == 0){
				if(isRecursion.equals("0"))
				{
					
					resids.add(new String[]{resid ,resName});
//						roleManager.deletePermissionOfRole(resids,resTypeId,tmp,role_type);
				}
				else if(isRecursion.equals("1"))
				{
					 //add by ge.tao
				    //2007-10-19
				    //CS菜单
					if("cs_column".equalsIgnoreCase(resTypeId)){
//							List resids = new ArrayList();
						resids.add(new String[]{resid ,resName});
						CSMenuManager csmenu = new CSMenuManagerImpl();
//							roleManager.deletePermissionOfRole(resid,resTypeId,tmp[k],role_type);
						List modelList = csmenu.getCSMenuPathList(resid);
						if((modelList != null) || (modelList.size() > 0))
						{
							for(int m=0;m<modelList.size();m++)
							{
								CSMenuModel myModel = (CSMenuModel)modelList.get(m);
								resids.add(new String[]{myModel.getId(), myModel.getTitle()});
//									roleManager.storeRoleresop(opid,myModel.getId(),tmp[k],resTypeId,myModel.getTitle(),role_type);
							}
						}
						
					    List csmenuModels =  csmenu.getRecursionCSMenuItems(resid);
					    for(int i=0;i<csmenuModels.size();i++){
					    	CSMenuModel model = (CSMenuModel)csmenuModels.get(i);
					        //CS子菜单授权
					    	resids.add(new String[]{model.getId(), model.getTitle()});
					    						    	
					    }		
//						    roleManager.deletePermissionOfRole(resids,resTypeId,tmp,role_type);	
					    
					}else{
//							List resids = new ArrayList();
						resids.add(new String[]{resid , resName});
//							roleManager.deletePermissionOfRole(resid,resTypeId,tmp[k],role_type);
						String subsystem = Framework.getSubsystemFromPath(menuPath);
						MenuHelper helper = new MenuHelper(subsystem,accessControl);
						ModuleQueue modules = helper.getSubModules(menuPath);
						ItemQueue items = helper.getSubItems(menuPath);
						for (int i = 0; items != null && i < items.size(); i++) {
							Item item = items.getItem(i);	
							resids.add(new String[]{item.getId() , item.getName(request)});
							// 子栏目授权							
//								roleManager.deletePermissionOfRole(item.getId(),resTypeId,tmp[k],role_type);
						}

						for (int i = 0; modules != null && i < modules.size(); i++) {
							Module module = modules.getModule(i);
//								module.getId();	
							resids.add(new String[]{module.getId(),module.getName(request)});
							// 子模块授权
//								roleManager.deletePermissionOfRole(module.getId(),resTypeId,tmp[k],role_type);
							recursionMenuids(resids,modules.getModule(i));
						}
//							roleManager.deletePermissionOfRole(resids,resTypeId,tmp,role_type);
					}
				}
				roleManager.deletePermissionOfRole(resids,resTypeId,tmp,role_type);					

			}
			
			//操作不为空和递归为空的话授权当前菜单相关操作（先删后存）
			else if(opid!=null ){
				if(isRecursion.equals("0"))
				{
					if(resTypeId.equals("cs_column")){
	//						List resids = new ArrayList();
						resids.add(new String[]{resid ,resName});
	//						roleManager.deletePermissionOfRole(resids,resTypeId,tmp,role_type);
						
	//						roleManager.storeRoleresop(opid,resids,tmp,resTypeId,resid,role_type);
						try{
							/*
							 * 递归附上父节点路径的权限
							 * */
							CSMenuManager csmenu = new CSMenuManagerImpl();
							List modelList = csmenu.getCSMenuPathList(resid);
							if((modelList != null) || (modelList.size() > 0))
							{
								for(int m=0;m<modelList.size();m++)
								{
									CSMenuModel myModel = (CSMenuModel)modelList.get(m);
									resids.add(new String[]{myModel.getId() ,myModel.getTitle()});
	//									roleManager.storeRoleresop(opid,myModel.getId(),tmp[k],resTypeId,myModel.getTitle(),role_type);
								}
							}
						}catch(Exception e){e.printStackTrace();}
						
					}else{
	//						List resids = new ArrayList();
						resids.add(new String[]{resid , resName});
	//						roleManager.deletePermissionOfRole(resids,resTypeId,tmp,role_type);
	//						roleManager.storeRoleresop(opid,resids,tmp,resTypeId,resid,role_type);
					}
					
				}
			
				//操作不为空和递归不为空的话授权当前菜单及子站点相关操作（先删后存）
				else if(isRecursion.equals("1")){
					//add by ge.tao
				    //2007-10-19
				    //CS菜单
					if("cs_column".equalsIgnoreCase(resTypeId)){
	//						List resids = new ArrayList();
						resids.add(new String[]{resid , resName});
						CSMenuManager csmenu = new CSMenuManagerImpl();
						/*
						 * 递归附上父节点路径的权限
						 * */
						
						List modelList = csmenu.getCSMenuPathList(resid);
						
						for(int m=0;modelList != null && m<modelList.size();m++)
						{
							CSMenuModel myModel = (CSMenuModel)modelList.get(m);
							
							resids.add(new String[]{myModel.getId() , myModel.getTitle()});
	//								roleManager.storeRoleresop(opid,myModel.getId(),tmp[k],resTypeId,myModel.getTitle(),role_type);
						}
						
	//						roleManager.deletePermissionOfRole(resid,resTypeId,tmp[k],role_type);
	//						roleManager.storeRoleresop(opid,resid,tmp[k],resTypeId,resid,role_type);
						/*
						 * 递归附上子节点的权限
						 * */
						
					    List csmenuModels =  csmenu.getRecursionCSMenuItems(resid);
					    for(int i=0;i<csmenuModels.size();i++){
					    	CSMenuModel model = (CSMenuModel)csmenuModels.get(i);
					    	// CS子栏目授权
					    	resids.add(new String[]{model.getId() , model.getTitle()});
	//							roleManager.deletePermissionOfRole(model.getId(),resTypeId,tmp[k],role_type);
	//							
	//							roleManager.storeRoleresop(opid,model.getId(),tmp[k],resTypeId,model.getTitle(),role_type);	
						
							
					    }
					}
					else
					{
						resids.add(new String[]{resid , resName});
	//						roleManager.deletePermissionOfRole(resid,resTypeId,tmp[k],role_type);
	//						roleManager.storeRoleresop(opid,resid,tmp[k],resTypeId,resid,role_type);
						try{
							String subsystem = Framework.getSubsystemFromPath(menuPath);
							MenuHelper helper = new MenuHelper(subsystem,accessControl);
							ModuleQueue modules = helper.getSubModules(menuPath);
							ItemQueue items = helper.getSubItems(menuPath);
							for (int i = 0; items != null && i < items.size(); i++) {
								Item item = items.getItem(i);
								
								String resname = item.getName(request);	
								resids.add(new String[]{item.getId() , resname});
								// 子栏目授权
	//							roleManager.deletePermissionOfRole(item.getId(),resTypeId,tmp[k],role_type);
	//							roleManager.storeRoleresop(opid,item.getId(),tmp[k],resTypeId,resname,role_type);						
							}
							for (int i = 0; modules != null && i < modules.size(); i++) {
								Module module = modules.getModule(i);
	//								module.getId();
								
								String resname = module.getName(request);
								resids.add(new String[]{module.getId() ,resname});
								// 子模块授权
	//								roleManager.deletePermissionOfRole(module.getId(),resTypeId,tmp[k],role_type);
	//								roleManager.storeRoleresop(opid,module.getId(),tmp[k],resTypeId,resname,role_type);
								recursionMenuids(resids,modules.getModule(i));
							}
						}catch(Exception e){
							e.printStackTrace();
						}						
					}
				}

				roleManager.restoreRoleresop(opid,resids,tmp,resTypeId,role_type);
			}
			
			
			return mapping.findForward("opmenu");
	}
	
	/**
	 * 
	 * @param roleManager
	 * @param module
	 * @param opid
	 * @param roleid
	 * @param resTypeId
	 * @param roleType
	 * @throws Exception
	 */
	public static void recursionMenu(RoleManager roleManager,Module module,
			String[] opid,String roleid,String resTypeId,String roleType,boolean isdel) throws Exception
	{
		AccessControl control = AccessControl.getAccessControl(); 
		HttpServletRequest request = control.getRequest();
		ModuleQueue modules = module.getSubModules();
		ItemQueue items = module.getItems();
		Item item = null;
		Module submodule = null;
		for (int i = 0; items != null && i < items.size(); i++) 
		{
			item = items.getItem(i);
			//String resname = item.getName();
		
			// 子栏目授权
			roleManager.deletePermissionOfRole(item.getId(),resTypeId,roleid,roleType);
			if(!isdel)
			{
				roleManager.storeRoleresop(opid,item.getId(),roleid,resTypeId,item.getName(request),roleType);
			}
		
		}
		for (int i = 0; modules != null && i < modules.size(); i++)
		{
			submodule = modules.getModule(i);
			
			roleManager.deletePermissionOfRole(submodule.getId(),resTypeId,roleid,roleType);
			if(!isdel)
			{
				roleManager.storeRoleresop(opid,submodule.getId(),roleid,resTypeId,submodule.getName(request),roleType);
			}
			
			recursionMenu(roleManager,submodule,opid,roleid,resTypeId,roleType,isdel);
		}
	}
	
	/**
	 * 
	 * @param roleManager
	 * @param module
	 * @param opid
	 * @param roleid
	 * @param resTypeId
	 * @param roleType
	 * @throws Exception
	 */
	public static void recursionMenuids(List resids,Module module) throws Exception
	{
		ModuleQueue modules = module.getSubModules();
		ItemQueue items = module.getItems();
		Item item = null;
		Module submodule = null;
		AccessControl control = AccessControl.getAccessControl(); 
		HttpServletRequest request = control.getRequest();
		for (int i = 0; items != null && i < items.size(); i++) 
		{
			item = items.getItem(i);
			//String resname = item.getName();
			resids.add(new String[]{item.getId() , item.getName(request)});
			// 子栏目授权
//			roleManager.deletePermissionOfRole(item.getId(),resTypeId,roleid,roleType);
//			if(!isdel)
//			{
//				roleManager.storeRoleresop(opid,item.getId(),roleid,resTypeId,item.getName(),roleType);
//			}
		
		}
		for (int i = 0; modules != null && i < modules.size(); i++)
		{
			submodule = modules.getModule(i);
			
			resids.add(new String[]{submodule.getId() , submodule.getName(request)});
//			roleManager.deletePermissionOfRole(submodule.getId(),resTypeId,roleid,roleType);
//			if(!isdel)
//			{
//				roleManager.storeRoleresop(opid,submodule.getId(),roleid,resTypeId,submodule.getName(),roleType);
//			}
			
			recursionMenuids(resids,submodule);
		}
	}
	
	/**
	 * 角色管理＝资源操作授予＝tab授权， add 王卓
	 * 有确定按钮
	 */
	public ActionForward tabAuthorization(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			String[] opid = request.getParameterValues("alloper");
			String resTypeId = request.getParameter("resTypeId");
			String resid = request.getParameter("resid");
			String roleid = request.getParameter("roleid");
			String[] tmp = roleid.split(",");
			String role_type = request.getParameter("role_type");
			String resName = request.getParameter("resName");
			request.setAttribute("resTypeId",resTypeId);
			request.setAttribute("resId",resid);
			request.setAttribute("isOk","1");
			HttpSession session = request.getSession();
			session.setAttribute("currRoleId", roleid);
			session.setAttribute("role_type", role_type);
		
			RoleManager roleManager = SecurityDatabase.getRoleManager();
		
			for(int k=0;k<tmp.length;k++){
				//操作为空的话删除当前tab资源所有操作
				if(opid==null){
					roleManager.deletePermissionOfRole(resName,resTypeId,tmp[k],role_type);
					
				}

				//操作不为空的话授权当前tab资源相关操作（先删后存）
				if(opid!=null){
					roleManager.deletePermissionOfRole(resName,resTypeId,tmp[k],role_type);
					roleManager.storeRoleresop(opid,resName,tmp[k],resTypeId,resName,role_type);
					
				}
			}
		
			return mapping.findForward("optab");
	}
	/**
	 * 角色管理＝资源操作授予＝用户组授权， add 王卓
	 * 有确定按钮
	 */
	public ActionForward groupAuthorization(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			String[] opid = request.getParameterValues("alloper");
			String isRecursion = request.getParameter("isRecursion");
			
			
			String resTypeId = request.getParameter("resTypeId");
			String resid = request.getParameter("resid");
			String roleid = request.getParameter("roleid");
			String[] tmp = roleid.split(",");
			String role_type = request.getParameter("role_type");
			request.setAttribute("resTypeId",resTypeId);
			request.setAttribute("resId",resid);
			request.setAttribute("isOk","1");
			HttpSession session = request.getSession();
			session.setAttribute("currRoleId", roleid);
			session.setAttribute("role_type", role_type);
			String resname = "";
			DBUtil db = new DBUtil();
			GroupManager groupManager = SecurityDatabase.getGroupManager();
			RoleManager roleManager = SecurityDatabase.getRoleManager();
			if(!resid.equals("addrootgroup"))
			{
			//根据组id取组名称
				
				Group group = groupManager.getGroupByID(resid);
				resname = group.getGroupName();
			}
			else
			{
				resname="添加一级组";
			}
			request.setAttribute("resName",resname);
			for(int k=0;k<tmp.length;k++){
//				操作和递归为空的话删除当前组所有操作
				if(opid==null && isRecursion.equals("0")){
					roleManager.deletePermissionOfRole(resid,resTypeId,tmp[k],role_type);
					
				}
				//操作为空和递归不为空的话删除当前组和子组所有操作
				if(opid==null && isRecursion.equals("1")){
					
					
					roleManager.deletePermissionOfRole(resid,resTypeId,tmp[k],role_type);
					if(!resid.equals("addrootgroup"))
					{
						List subgroup = groupManager.getChildGroupList(resid);
						for (int i = 0; subgroup != null && i < subgroup.size(); i++) {
							Group group1 = (Group) subgroup.get(i);
							
							roleManager.deletePermissionOfRole(""+group1.getGroupId(),resTypeId,tmp[k],role_type);
						
						}
					}
					
				}
				//操作不为空和递归不为空的话授权当前组相关操作（先删后存）
				if(opid!=null && isRecursion.equals("0")){
					roleManager.deletePermissionOfRole(resid,resTypeId,tmp[k],role_type);
					roleManager.storeRoleresop(opid,resid,tmp[k],resTypeId,resname,role_type);
					//递归给该用户组的父组授只读权限
					if(!resid.equals("addrootgroup"))
					{
						String str="SELECT t.group_id FROM TD_SM_GROUP t START WITH "+
							" t.group_id="+ resid +" CONNECT BY PRIOR t.PARENT_ID=t.group_id";
						db.executeSelect(str);
						if(db.size()>1){
							for(int j=0;j<db.size();j++)
							{
								String parentid = db.getInt(j,"group_id")+"";
								roleManager.storeRoleresop("read",parentid,tmp[k],resTypeId,resname,role_type);
							
							}
						}
					}
				}
				//操作不为空和递归不为空的话授权当前组及子组相关操作（先删后存），之前已经删除了
				if(opid!=null && isRecursion.equals("1")){
					try
					{
						roleManager.storeRoleresop(opid,resid,tmp[k],resTypeId,resname,role_type);
					}
					catch(Exception e )
					{
						e.printStackTrace();
					}
					if(!resid.equals("addrootgroup"))
					{
						
						List subgroups = groupManager.getChildGroupList(resid);
						for (int i = 0; subgroups != null && i < subgroups.size(); i++) {
							Group group1 = (Group) subgroups.get(i);
							
							//roleManager.deletePermissionOfRole(""+group1.getGroupId(),resTypeId,tmp[k],role_type);
							try
							{
								roleManager.storeRoleresop(opid,""+group1.getGroupId(),tmp[k],resTypeId,group1.getGroupName(),role_type);
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
							
						}
						//递归给该用户组的父组授只读权限
						String str="SELECT t.group_id FROM TD_SM_GROUP t START WITH "+
							" t.group_id="+ resid +" CONNECT BY PRIOR t.PARENT_ID=t.group_id";
						db.executeSelect(str);
						if(db.size()>1){
							for(int j=0;j<db.size();j++)
							{
								String parentid = db.getInt(j,"group_id")+"";
								try{
									roleManager.storeRoleresop("read",parentid,tmp[k],resTypeId,resname,role_type);
								}
								catch(Exception e)
								{
									
								}
							
							}
						}
					}
					
					
				}
			}
			
			return mapping.findForward("opgroup");
	}
	/**
	 * 通用资源授权页面，没有递归操作
	 * 有确定按钮
	 */
	public ActionForward roleAuthorization(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			String[] opid = request.getParameterValues("alloper");
			
			String resTypeId = request.getParameter("resTypeId");
			String resid = request.getParameter("resid");
			String resname = request.getParameter("resName");
			String roleid = request.getParameter("roleid");
			String[] tmp = roleid.split(",");
			String role_type = request.getParameter("role_type");
			request.setAttribute("resTypeId",resTypeId);
			request.setAttribute("resId",resid);
			request.setAttribute("isOk","1");
			HttpSession session = request.getSession();
			session.setAttribute("currRoleId", roleid);
			session.setAttribute("role_type", role_type);
			//根据角色id取角色名称
		
			RoleManager roleManager = SecurityDatabase.getRoleManager();
//			Role role = roleManager.getRole("roleId",resid);
//			String resname = role.getRoleName();
			request.setAttribute("resName",resname);
			for(int k=0;k<tmp.length;k++){
				//操作为空的话删除当前角色所有操作
				if(opid==null ){
					roleManager.deletePermissionOfRole(resid,resTypeId,tmp[k],role_type);
					
				}
			
				//操作不为空的话授权当前角色相关操作（先删后存）
				if(opid!=null ){
					roleManager.deletePermissionOfRole(resid,resTypeId,tmp[k],role_type);
					roleManager.storeRoleresop(opid,resid,tmp[k],resTypeId,resname,role_type);
					
				}
				
			}
			
			return mapping.findForward("oprole");
	}
}
