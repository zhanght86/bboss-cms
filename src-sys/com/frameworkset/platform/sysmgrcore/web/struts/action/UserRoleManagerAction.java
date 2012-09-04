package com.frameworkset.platform.sysmgrcore.web.struts.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.frameworkset.platform.config.ConfigManager;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.sysmgrcore.entity.Group;
import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.platform.sysmgrcore.entity.Role;
import com.frameworkset.platform.sysmgrcore.entity.User;
import com.frameworkset.platform.sysmgrcore.entity.Userrole;
import com.frameworkset.platform.sysmgrcore.manager.GroupManager;
import com.frameworkset.platform.sysmgrcore.manager.LogGetNameById;
import com.frameworkset.platform.sysmgrcore.manager.LogManager;
import com.frameworkset.platform.sysmgrcore.manager.OrgManager;
import com.frameworkset.platform.sysmgrcore.manager.RoleManager;
import com.frameworkset.platform.sysmgrcore.manager.RoleTypeManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.platform.sysmgrcore.manager.UserManager;
import com.frameworkset.platform.sysmgrcore.web.struts.form.UserRoleManagerForm;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:
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
public class UserRoleManagerAction extends DispatchAction implements Serializable {
	public UserRoleManagerAction() {
	}

	private static Logger log = Logger.getLogger(UserRoleManagerAction.class
			.getName());

	public ActionForward getRoleList(ActionMapping mapping, UserRoleManagerForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			AccessControl accessControl = AccessControl.getInstance();
			accessControl.checkAccess(request, response);
			 String uid2 = request.getParameter("userId");
			 request.setAttribute("userId",uid2);
			 Integer uid = Integer.valueOf(uid2);
			 String orgId = request.getParameter("orgId");
			 request.setAttribute("orgId",orgId);
			if (uid == null) {
				return mapping.findForward("noUser");
			}
			String orgid = request.getParameter("orgId");
			UserRoleManagerForm userRoleForm = (UserRoleManagerForm) form;
			userRoleForm.setOrgId(orgid);
			userRoleForm.setUserId(uid.toString());
			RoleManager roleManager = SecurityDatabase.getRoleManager();
			User user = new User();
			user.setUserId(uid);
			List existRole = roleManager.getRoleListByUserRole(user);
			//角色类别Map
			Map roleTypeMap = new RoleTypeManager().getRoleTypeMap();
			
			String sql ="";
			if("1".equals(accessControl.getUserID())){
				sql = "select * from td_sm_role t where t.role_id not in('2','3','4') order by t.ROLE_TYPE,t.role_name";
			}else{
				sql = "select * from td_sm_role t where t.role_id not in('1','2','3','4') order by t.ROLE_TYPE,t.role_name";
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
			
			
			
			
			//得到用户岗位对应的角色
			List jobRole = roleManager.getJobRoleByList(uid2);
			if(ConfigManager.getInstance().getConfigBooleanValue("enableorgrole",true))
			{
				Set userOrgRole = new HashSet();
				for(int i=0;orgList!=null&&i<orgList.size();i++){
					Organization org = (Organization)orgList.get(i);
					List role = roleManager.getRoleList(org);
					userOrgRole.addAll(role);
				}
				request.setAttribute("orgRole",new ArrayList(userOrgRole));
			}
			
			if(ConfigManager.getInstance().getConfigBooleanValue("enablergrouprole",true))
			{
				Set userGroupRole =new HashSet();
				for(int i=0;groupList!=null&&i<groupList.size();i++){
					Group group = (Group)groupList.get(i);
					List role = roleManager.getRoleList(group);
					userGroupRole.addAll(role);
				}
				request.setAttribute("groupRole",new ArrayList(userGroupRole));
			}
			request.setAttribute("allRole", allRole);
			request.setAttribute("existRole", existRole);
			request.setAttribute("userRoleForm", userRoleForm);
			request.setAttribute("jobRole", jobRole);
			request.setAttribute("roleTypeMap", roleTypeMap);
		} catch (Exception e) {
			return mapping.findForward("fail");
		}
		return mapping.findForward("roleDetail");
	}
	
	public ActionForward getAllRoleList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			String[] id = request.getParameterValues("checkBoxOne");
			AccessControl accessControl = AccessControl.getInstance();
			accessControl.checkAccess(request, response);
			RoleManager roleManager = SecurityDatabase.getRoleManager();
			String sql = "";
			if("1".equals(accessControl.getUserID())){
				sql = "select * from td_sm_role where role_id not in('2','3') order by role_name";
			}else{
				sql = "select * from td_sm_role where role_id not in('1','2','3') order by role_name";
			}
		    List list = roleManager.getRoleList(sql);
			List allRole = null;
			// 角色列表加权限
			for (int i = 0; list != null && i < list.size(); i++) {
				Role role = (Role) list.get(i);
				if (accessControl.checkPermission(role.getRoleId(),
						AccessControl.WRITE_PERMISSION,
						AccessControl.ROLE_RESOURCE)
						|| accessControl.checkPermission(role.getRoleId(),
								AccessControl.READ_PERMISSION,
								AccessControl.ROLE_RESOURCE)) {
					if (allRole == null)
						allRole = new ArrayList();
					allRole.add(role);
				}
			}
         request.setAttribute("allRole", allRole);
         request.setAttribute("id", id);

		} catch (Exception e) {
			return mapping.findForward("fail");
		}
		return mapping.findForward("roleuseralot");
		
	}



	public ActionForward storeUserRole(ActionMapping mapping, UserRoleManagerForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//---------------START--
		AccessControl control = AccessControl.getInstance();
		control.checkAccess(request,response);
		String operContent="";        
        String operSource=control.getMachinedID();//request.getRemoteAddr();
        String openModle="用户管理";
        String userName = control.getUserName();
        String description="";
        LogManager logManager = SecurityDatabase.getLogManager(); 		
		//---------------END
		HttpSession session = request.getSession();
		Integer uid = (Integer) session.getAttribute("currUserId");
		if (uid == null) {
			return mapping.findForward("noUser");
		}
		String orgid = request.getParameter("orgId");
		UserManager userManager = SecurityDatabase.getUserManager();
		User user = userManager.getUserById(uid.toString());

		RoleManager roleManager = SecurityDatabase.getRoleManager();
		
		try {
			if (user != null) {
				// delete all job of this user
				userManager.deleteUserrole(user);
				UserRoleManagerForm userRoleForm = (UserRoleManagerForm) form;
				String[] roleid = userRoleForm.getRoleId();
				String roleids ="";
				for (int i = 0; (roleid != null) && (i < roleid.length); i++) {
					Role role = roleManager.getRoleById(roleid[i]);
					if (role != null) {
						Userrole userrole = new Userrole();
						userrole.setUser(user);
						userrole.setRole(role);
						roleids +=LogGetNameById.getRoleNameByRoleId(roleid[i])+" ";
						userManager.storeUserrole(userrole);
					}
				}
				
				//--	
				operContent="添加用户角色,用户:"+user.getUserName()+" 角色编号："+roleids;
				
				description="";
		        logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description);       
				//--
			} else {
				return mapping.findForward("noUser");
			}
			UserRoleManagerForm userRoleForm = (UserRoleManagerForm) form;
			userRoleForm.setOrgId(orgid);
			userRoleForm.setUserId(uid.toString());
			List existRole = null;
			if (user != null) {
				existRole = roleManager.getRoleList(user);
			}
			
			List allRole = roleManager.getRoleList();
			request.setAttribute("allRole", allRole);
			request.setAttribute("existRole", existRole);
			request.setAttribute("userRoleForm", userRoleForm);
			return mapping.findForward("roleDetail");
		} catch (Exception e) {
			log.error(e);
			return mapping.findForward("fail");
		}
	}

	/**
	 * add by hongyu.deng to offer a fridenly page to user, when store and
	 * delete userRole relation the jsp page is not refresh
	 * 
	 * @param uid
	 * @param orgid
	 * @param roleid
	 * @return
	 */
	public static String storeAndDeleteUserRole(Integer uid, String orgid,
			String[] roleid) throws Exception {
		UserManager userManager = SecurityDatabase.getUserManager();
		RoleManager roleManager = SecurityDatabase.getRoleManager();
		User user = userManager.getUserById(uid.toString());
		if (user != null) {
			// delete all job of this user
			userManager.deleteUserrole(user);
			for (int i = 0; (roleid != null) && (i < roleid.length); i++) {
				Role role = roleManager.getRoleById( roleid[i]);
				if (role != null) {
					Userrole userrole = new Userrole();
					userrole.setUser(user);
					userrole.setRole(role);
					userManager.storeUserrole(userrole);
				}
			}
		} else {
			return "fail";
		}
		return "success";

	}
	
	public ActionForward getAllOperList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			String[] id = request.getParameterValues("checkBoxOne");
			request.setAttribute("id", id);

		} catch (Exception e) {
			return mapping.findForward("fail");
		}
		
		return mapping.findForward("batch2Oper");
		
	}
	

}
