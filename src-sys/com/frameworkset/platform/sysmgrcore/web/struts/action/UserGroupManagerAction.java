package com.frameworkset.platform.sysmgrcore.web.struts.action;

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.sysmgrcore.entity.Group;
import com.frameworkset.platform.sysmgrcore.entity.User;
import com.frameworkset.platform.sysmgrcore.entity.Usergroup;
import com.frameworkset.platform.sysmgrcore.manager.GroupManager;
import com.frameworkset.platform.sysmgrcore.manager.LogManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.platform.sysmgrcore.manager.UserManager;
import com.frameworkset.platform.sysmgrcore.web.struts.form.UserGroupManagerForm;
import com.frameworkset.common.poolman.DBUtil;




/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description: 组织管理action
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

public class UserGroupManagerAction extends DispatchAction implements Serializable {

	public UserGroupManagerAction() {
	}

	private static Logger log = Logger.getLogger(UserGroupManagerAction.class
			.getName());

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
	public ActionForward getGroupList(ActionMapping mapping, UserGroupManagerForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			//HttpSession session = request.getSession();
			AccessControl accessControl = AccessControl.getInstance();
			accessControl.checkAccess(request,response);
			//Integer uid = (Integer) session.getAttribute("currUserId");
			String uid2 = request.getParameter("userId");
			request.setAttribute("userId",uid2);
			Integer uid = Integer.valueOf(uid2);
			
		
			String groupId = request.getParameter("groupId");
//			System.out.println("userId..........."+uid);
//			
			if (uid == null) {
				return mapping.findForward("noUser");
			}
			String orgid = request.getParameter("orgId");
			request.setAttribute("orgId",orgid);
//			System.out.println("groupId..........."+orgid);
			UserGroupManagerForm userGroupForm = (UserGroupManagerForm) form;
			userGroupForm.setOrgId(orgid);
			userGroupForm.setUserId(uid.toString());
			GroupManager groupManager = SecurityDatabase.getGroupManager();
			User user = new User();
			user.setUserId(uid);
		
			Group group1 = groupManager.getGroupByID(groupId);
			List allGroup = null;
			if (groupManager.isContainChildGroup(group1)) {
				allGroup = groupManager
						.getGroupList("select * from td_sm_group o where o.PARENT_ID='"
								+ groupId + "' or o.GROUP_ID='" + groupId + "'");
			} else {
				allGroup = groupManager
						.getGroupList("select * from td_sm_group o where o.GROUP_ID='"
								+ groupId + "'");
			}
			List existGroup = groupManager.getGroupList(user);
//			List list = groupManager.getGroupList();
////			 角色列表加权限
//			
//			for (int i = 0; list != null && i < list.size(); i++) {
//				Group group = (Group) list.get(i);
//				if (accessControl.checkPermission(group.getGroupId().toString(),
//						AccessControl.WRITE_PERMISSION,
//						AccessControl.GROUP_RESOURCE)
//						|| accessControl.checkPermission(group.getGroupId().toString(),
//								AccessControl.READ_PERMISSION,
//								AccessControl.GROUP_RESOURCE)) {
//					if (allGroup == null)
//						allGroup = new ArrayList();
//					allGroup.add(group);
//				}
//			}
			request.setAttribute("allGroup", allGroup);
			request.setAttribute("existGroup", existGroup);
			request.setAttribute("userGroupForm", userGroupForm);
		} catch (Exception e) {
			return mapping.findForward("fail");
		}
		return mapping.findForward("groupDetail");
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
	public ActionForward storeUserGroup(ActionMapping mapping, UserGroupManagerForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		//---------------START--用户组管理写操作日志
		AccessControl control = AccessControl.getInstance();
		control.checkAccess(request,response);
		String operContent="";        
        String operSource=control.getMachinedID();//request.getRemoteAddr();
        String openModle="用户组管理";
        String userName = control.getUserName();
        String description="";
        LogManager logManager = SecurityDatabase.getLogManager(); 		
		//---------------END
        
		HttpSession session = request.getSession(false);
		Integer uid = (Integer) session.getAttribute("currUserId");
		if (uid == null) {
			return mapping.findForward("noUser");
		}

		UserManager userManager = SecurityDatabase.getUserManager();
		User user = userManager.getUserById(uid.toString());
		String orgid = request.getParameter("oid");
		GroupManager groupManager = SecurityDatabase.getGroupManager();
		try {
			if (user != null) {
				userManager.deleteUsergroup(user);
				UserGroupManagerForm userGroupForm = (UserGroupManagerForm) form;
				String[] groupid = userGroupForm.getGroupId();

				for (int i = 0; (groupid != null) && (i < groupid.length); i++) {
					Group group = groupManager.getGroupByID(groupid[i]);

					if (group != null) {
						Usergroup usergroup = new Usergroup();
						usergroup.setUser(user);
						usergroup.setGroup(group);
						userManager.storeUsergroup(usergroup);	
						
						//--资源管理写操作日志	
						operContent="存储用户组: "+group.getGroupName(); 
						 description="";
				        logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description);       
						//--
					}
				}
			}
			// no selected user
			else {
				return mapping.findForward("noUser");
			}
			UserGroupManagerForm userGroupForm = (UserGroupManagerForm) form;
			userGroupForm.setOrgId(orgid);
			userGroupForm.setUserId(uid.toString());
			List existGroup = groupManager.getGroupList(user);
			List allGroup = groupManager.getGroupList();
			request.setAttribute("allGroup", allGroup);
			request.setAttribute("existGroup", existGroup);
			request.setAttribute("userGroupForm", userGroupForm);
			return mapping.findForward("groupDetail");
		} catch (Exception e) {
			log.error(e);
			return mapping.findForward("fail");
		}
	}
/**
 * add the method by hongyu.deng when store and delete userGroup the reference jsp page is not refresh
 * @param uid
 * @param groupid
 * @return
 */
	public static String storeAndDeleteUserGroup(Integer uid, 
			String[] groupid) {
		try {
			UserManager userManager = SecurityDatabase.getUserManager();
			GroupManager groupManager = SecurityDatabase.getGroupManager();
			User user = userManager.getUserById(uid.toString());
			if (user != null) {
				//userManager.deleteUsergroup(user);
				for (int i = 0; (groupid != null) && (i < groupid.length); i++) {
					userManager.deleteUsergroup(uid.toString(),groupid[i]);
					Group group = groupManager.getGroupByID(groupid[i]);
					if (group != null) {
						Usergroup usergroup = new Usergroup();
						usergroup.setUser(user);
						usergroup.setGroup(group);
						userManager.storeUsergroup(usergroup);
					}
				}
			}
			return "success";

		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
	}
	
	public static String DeleteUserGroup(Integer uid, String[] groupid) {        
		try {
			UserManager userManager = SecurityDatabase.getUserManager();
			GroupManager groupManager = SecurityDatabase.getGroupManager();
			User user = userManager.getUserById(uid.toString());
			if (user != null) {
				
				for (int i = 0; (groupid != null) && (i < groupid.length); i++) {
					userManager.deleteUsergroup(uid.toString(),groupid[i]);
					

				}
			}
			return "success";

		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
	}
	public static String UserGroup(Integer uid, String[] groupid) {
		try {
			UserManager userManager = SecurityDatabase.getUserManager();
			GroupManager groupManager = SecurityDatabase.getGroupManager();
			User user = userManager.getUserById(uid.toString());
			if (user != null) {
				
				for (int i = 0; (groupid != null) && (i < groupid.length); i++) {
				
						DBUtil db = new  DBUtil();
						String sql ="select * from td_sm_usergroup where " +
								" user_id ="+ uid.toString()+" and group_id="+ groupid[i] +"";
						
						db.executeSelect(sql);
						if(db.size()>0){
							continue;
						}else{
							Group group = groupManager.getGroupByID(groupid[i]);
							if (group != null) {
								Usergroup usergroup = new Usergroup();
								usergroup.setUser(user);
								usergroup.setGroup(group);
								userManager.storeUsergroup(usergroup);
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
}
