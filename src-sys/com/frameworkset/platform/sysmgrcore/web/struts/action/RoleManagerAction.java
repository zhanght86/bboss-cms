package com.frameworkset.platform.sysmgrcore.web.struts.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.frameworkset.platform.framework.Framework;
import com.frameworkset.platform.framework.Item;
import com.frameworkset.platform.framework.ItemQueue;
import com.frameworkset.platform.framework.Module;
import com.frameworkset.platform.framework.ModuleQueue;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.sysmgrcore.entity.Group;
import com.frameworkset.platform.sysmgrcore.entity.Grouprole;
import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.platform.sysmgrcore.entity.Orgrole;
import com.frameworkset.platform.sysmgrcore.entity.Role;
import com.frameworkset.platform.sysmgrcore.entity.Roleresop;
import com.frameworkset.platform.sysmgrcore.manager.GroupManager;
import com.frameworkset.platform.sysmgrcore.manager.OrgManager;
import com.frameworkset.platform.sysmgrcore.manager.RoleManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
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
public class RoleManagerAction   {
	public RoleManagerAction() {
	}

	private static Logger log = Logger.getLogger(RoleManagerAction.class
			.getName());

	  

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
	
	  
}
