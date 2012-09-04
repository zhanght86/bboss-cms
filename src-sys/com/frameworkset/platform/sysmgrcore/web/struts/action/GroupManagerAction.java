package com.frameworkset.platform.sysmgrcore.web.struts.action;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
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
import com.frameworkset.platform.sysmgrcore.entity.Grouprole;
import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.platform.sysmgrcore.entity.Role;
import com.frameworkset.platform.sysmgrcore.entity.User;
import com.frameworkset.platform.sysmgrcore.entity.Usergroup;
import com.frameworkset.platform.sysmgrcore.manager.GroupManager;
import com.frameworkset.platform.sysmgrcore.manager.LogGetNameById;
import com.frameworkset.platform.sysmgrcore.manager.LogManager;
import com.frameworkset.platform.sysmgrcore.manager.OrgManager;
import com.frameworkset.platform.sysmgrcore.manager.RoleManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.platform.sysmgrcore.manager.UserManager;
import com.frameworkset.platform.sysmgrcore.web.struts.form.GroupManagerForm;
import com.frameworkset.common.poolman.DBUtil;


/**
 * 
 * @author feng.jing
 * @file GroupManagerAction.java Created on: Mar 20, 2006
 */
public class GroupManagerAction extends DispatchAction implements Serializable {
	private List userList;
	public GroupManagerAction() {
	}

	private static Logger log = Logger.getLogger(GroupManagerAction.class
			.getName());

	/**
	 * 得到一个用户组对应的角色列表和所有角色列表（groupmanager/changeRole.jsp）
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward getGroupRoleList(ActionMapping mapping,
			GroupManagerForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		AccessControl accessControl = AccessControl.getInstance();
		accessControl.checkAccess(request, response);
		String groupId = request.getParameter("groupId");
		GroupManagerForm groupForm = (GroupManagerForm) form;
		groupForm.setGroupId(groupId);
		GroupManager groupManager = SecurityDatabase.getGroupManager();
		RoleManager roleManager = SecurityDatabase.getRoleManager();
		Group g = groupManager.loadAssociatedSet(groupId,
				GroupManager.ASSOCIATED_GROUPROLESET);
		List existRole = null;
		if (g != null) {
			Iterator iterator = g.getGrouproleSet().iterator();
			existRole = new ArrayList();
			while (iterator.hasNext()) {
				Grouprole gr = (Grouprole) iterator.next();
				Role r = gr.getRole();
				existRole.add(r);
			}
		}
		List list = roleManager.getRoleList("select * from td_sm_Role role order by role.role_Name");
		List allRole = null;
		//角色列表加权限
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
		request.setAttribute("existRole", existRole);
		request.setAttribute("groupForm", groupForm);
		return mapping.findForward("roleList");
	}

	/**
	 * 保存一个用户组对应的角色列表（groupmanager/changeRole.jsp右边的角色列表）
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward storeGroupRole(ActionMapping mapping, GroupManagerForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//---------------START--
		AccessControl control = AccessControl.getInstance();
		control.checkAccess(request,response);
		String operContent="";        
		String operSource=control.getMachinedID();//request.getRemoteAddr();
		String openModle="用户组管理";
		String userName = control.getUserName();
		String description="";
		LogManager logManager = SecurityDatabase.getLogManager(); 		
		//---------------END

		GroupManager groupManager = SecurityDatabase.getGroupManager();
		
		RoleManager roleManager = SecurityDatabase.getRoleManager();
		try {
			GroupManagerForm groupForm = (GroupManagerForm) form;
			String[] roleId = groupForm.getRoleId();
			String groupId = groupForm.getGroupId();
			Group group = groupManager.getGroupByID(groupId);
			if (group != null) {
				Grouprole grr = new Grouprole();
				grr.setGroup(group);
				// 删除该用户组与所有角色的关系
				groupManager.deleteGrouprole(group);
				String roleNames = "";
				for (int i = 0; (roleId != null) && (i < roleId.length); i++) {

					Role role = roleManager.getRoleById(roleId[i]);
					if (role != null) {
						Grouprole gr = new Grouprole();
						gr.setGroup(group);
						gr.setRole(role);
						roleNames +=LogGetNameById.getRoleNameByRoleId(roleId[i])+" ";
						groupManager.storeGrouprole(gr);
					}
				}
			
				
				//--	
				operContent="修改用户组角色，用户组："+LogGetNameById.getGroupNameByGroupId(groupId)+" 角色编号："+roleNames;				
				description="";
				logManager.log(control.getUserAccount(),operContent,openModle,operSource,description);       
				//--
			}
			groupForm.setGroupId(groupId);
			List existRole = null;
			Group g = groupManager.loadAssociatedSet(groupId,
					GroupManager.ASSOCIATED_GROUPROLESET);
			if (g != null) {
				Iterator iterator = g.getGrouproleSet().iterator();
				existRole = new ArrayList();
				while (iterator.hasNext()) {
					Grouprole gr = (Grouprole) iterator.next();
					Role r = gr.getRole();
					existRole.add(r);
				}
			}
			List allRole = roleManager.getRoleList();
			request.setAttribute("allRole", allRole);
			request.setAttribute("existRole", existRole);
			request.setAttribute("groupForm", groupForm);
			return mapping.findForward("roleList");
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			return mapping.findForward("fail");
		}
	}

	/**
	 * 得到某一机构的用户列表和隶属某一用户组的用户列表
	 * 
	 * 修改：用户列表变为用户实名+工号
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
	public ActionForward getUserList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		String groupId = (String) session.getAttribute("currGroupId");
		if (groupId == null) {
			return mapping.findForward("noGroup");
		}
		String orgid = request.getParameter("orgId");
		String isRecursive = request.getParameter("isRecursive");
		isRecursive = isRecursive==null?"":isRecursive;
		GroupManager groupManager = SecurityDatabase.getGroupManager();
		UserManager userManager = SecurityDatabase.getUserManager();
		OrgManager orgManager = SecurityDatabase.getOrgManager();
		Organization org = orgManager.getOrgById(orgid);
		Group group = groupManager.getGroupByID(groupId);
		if (group != null && org != null) {
			List allUser = new ArrayList();
			if("true".equalsIgnoreCase(isRecursive)){
			//if(true){
				userList = new ArrayList();
				allUser = this.sortUser(orgid);
			}else{ 
				DBUtil db = new DBUtil();				
				String sql="select a.USER_ID,a.USER_SN,a.USER_NAME,a.USER_PASSWORD,a.USER_REALNAME,a.USER_WORKNUMBER,"+
		           "a.USER_PINYIN,a.USER_SEX,a.USER_HOMETEL,a.USER_WORKTEL,a.USER_MOBILETEL1,a.USER_MOBILETEL2,"+
		           "a.USER_FAX,a.USER_OICQ,a.USER_BIRTHDAY,a.USER_EMAIL,a.USER_ADDRESS,a.USER_POSTALCODE,a.USER_IDCARD,"+
		           "a.USER_ISVALID,a.USER_REGDATE,a.USER_LOGINCOUNT,a.USER_TYPE,a.REMARK1,a.REMARK2,a.REMARK3,a.REMARK4,"+
		           "a.REMARK5,min(b.same_job_user_sn) aa,min(b.job_sn) bb "+
		            " from td_sm_user a, td_sm_userjoborg b "+
		            "where a.user_id = b.user_id and b.org_id ='"+orgid+"' "+
		            "group by a.USER_ID,a.USER_SN,a.USER_NAME,a.USER_PASSWORD,a.USER_REALNAME,a.USER_WORKNUMBER,"+
		            "a.USER_PINYIN,a.USER_SEX,a.USER_HOMETEL,a.USER_WORKTEL,a.USER_MOBILETEL1,a.USER_MOBILETEL2,"+
		            "a.USER_FAX,a.USER_OICQ,a.USER_BIRTHDAY,a.USER_EMAIL,a.USER_ADDRESS,a.USER_POSTALCODE,a.USER_IDCARD,"+
		            "a.USER_ISVALID,a.USER_REGDATE,a.USER_LOGINCOUNT,a.USER_TYPE,a.REMARK1,"+
		            "a.REMARK2,a.REMARK3,a.REMARK4,a.REMARK5 "+
		            " order by bb asc,aa asc";  
				 
				 db.executeSelect(sql);
				 for(int i=0;i<db.size();i++){
					 User user=new User();
					 int userid=db.getInt(i,"user_id");
					 user.setUserId(new Integer(userid));
					 user.setUserName(db.getString(i,"USER_NAME"));
					 user.setUserRealname(db.getString(i,"USER_REALNAME"));
					 allUser.add(user);
	
				 }
			}
			List existUser = userManager.getUserList(group);
			if (allUser == null) {
				allUser = new ArrayList();
			}
			if (existUser == null) {
				existUser = new ArrayList();
			}
			request.setAttribute("allUser", allUser);
			request.setAttribute("existUser", existUser);
		}
		return mapping.findForward("userList");
	}
	
	private List sortUser(String parent_id) {
		DBUtil db_org = new DBUtil();
		
		try {
			// 取出直接下级机构，按jorg_sn,orgnumber排序
			db_org.executeSelect("select org_id from TD_SM_ORGANIZATION where parent_id='"
				+ parent_id + "' order by org_sn,orgnumber");

			for (int i = 0; i < db_org.size(); i++) {
				//
				String org_id = db_org.getString(i, "org_id");
				DBUtil db_user = new DBUtil();
				StringBuffer sb_user = new StringBuffer();
				sb_user.append("select b.user_id, b.user_name, b.user_realname, b.USER_HOMETEL,");
				sb_user.append("b.USER_WORKTEL, b.USER_MOBILETEL1, ");
				sb_user.append("b.USER_MOBILETEL2, b.USER_EMAIL from v_user_one_org_one_job a ");
				sb_user.append("inner join td_sm_user b on a.user_id = b.user_id ");
				sb_user.append("where org_id ='" + org_id + "' ");
				sb_user.append("order by job_sn,SAME_JOB_USER_SN");

				// 取出一个机构的用户，按job_sn,SAME_JOB_USER_SN排序
				db_user.executeSelect(sb_user.toString());
				//
				for (int j = 0; j < db_user.size(); j++) {
					User user = new User();
					user.setUserId(new Integer(db_user.getInt(j, "user_id")));
					user.setUserName(db_user.getString(j, "user_name"));
					user.setUserRealname(db_user.getString(j, "user_realname"));
					user.setUserHometel(db_user.getString(j, "USER_HOMETEL"));
					user.setUserWorktel(db_user.getString(j, "USER_WORKTEL"));
					user.setUserMobiletel1(db_user.getString(j,
							"USER_MOBILETEL1"));
					user.setUserMobiletel2(db_user.getString(j,
							"USER_MOBILETEL2"));
					user.setUserEmail(db_user.getString(j, "USER_EMAIL"));
					userList.add(user);
				}
				// 递归调用
				sortUser(org_id);
			}
		} catch (SQLException e) {
			return null;
		}
		return userList;
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
	public ActionForward storeUserList(ActionMapping mapping, GroupManagerForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//---------------START--
		AccessControl control = AccessControl.getInstance();
		control.checkAccess(request,response);
		String operContent="";        
		String operSource=control.getMachinedID();
		String openModle="用户组管理";
		String userName = control.getUserName();
		String description="";
		LogManager logManager = SecurityDatabase.getLogManager(); 		
		//---------------END
		HttpSession session = request.getSession();
		String groupId = (String) session.getAttribute("currGroupId");
		if (groupId == null) {
			return mapping.findForward("noGroup");
		}
		GroupManager groupManager = SecurityDatabase.getGroupManager();
		UserManager userManager = SecurityDatabase.getUserManager();
		try {
			GroupManagerForm groupForm = (GroupManagerForm) form;
			String[] userId = groupForm.getUserIds();
			String orgid = groupForm.getOrgId();
			Group group = groupManager.getGroupByID(groupId);
			// 根据用户组取对应的角色列表 王卓添加
			/*RoleManager roleManager = SecurityDatabase.getRoleManager();
			List rolelist = roleManager.getRoleList(group);
			// System.out.println("......."+rolelist.size());
			// 保存角色和用户的关系
			if (group != null) {
				for (int j = 0; rolelist != null && j < rolelist.size(); j++) {

					Role role = (Role) rolelist.get(j);

					// 删除角色与用户的关系
					userManager.deleteUserrole(role, group);
				}
				for (int j = 0; j < rolelist.size(); j++) {
					for (int i = 0; (userId != null) && (i < userId.length); i++) {
						User user = userManager.getUser("userId", userId[i]);

						Role role = (Role) rolelist.get(j);

						if (user != null && rolelist.size() > 0) {
							Userrole ur = new Userrole();
							ur.setUser(user);
							ur.setRole(role);
							userManager.storeUserrole(ur);

						}
					}
				}

			}
			// 添加结束
			 
			 */
			if (group != null) {
				Grouprole grr = new Grouprole();
				grr.setGroup(group);
				// 删除该用户组与所有角色的关系
				userManager.deleteUsergroup(group);
				String userNames = "";
				for (int i = 0; (userId != null) && (i < userId.length); i++) {
					User user = userManager.getUserById(userId[i]);
					if (user != null) {
						Usergroup usergroup = new Usergroup();
						usergroup.setUser(user);
						usergroup.setGroup(group);
						userNames += LogGetNameById.getUserNameByUserId(userId[i])+" ";
						userManager.storeUsergroup(usergroup);
					}
				}
				//--	
				operContent="修改用户组，用户组："+LogGetNameById.getGroupNameByGroupId(groupId)+" 用户："+userNames;				
				description="";
				logManager.log(control.getUserAccount(),operContent,openModle,operSource,description);       
				//--
			}

			List existUser = userManager.getUserList(group);
			OrgManager orgManager = SecurityDatabase.getOrgManager();
			Organization org = orgManager.getOrgById(orgid);
			List allUser = userManager.getUserList(org);
			request.setAttribute("allUser", allUser);
			request.setAttribute("existUser", existUser);
			return mapping.findForward("userList");
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			return mapping.findForward("fail");
		}
	}

	/**
	 * 当点击用户组上面的隶属用户的时候执行该action。 将已有的用户取出来，
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward getExistList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		String groupId = (String) session.getAttribute("currGroupId");
		if (groupId == null) {
			return mapping.findForward("noGroup");
		}

		GroupManager groupManager = SecurityDatabase.getGroupManager();
		UserManager userManager = SecurityDatabase.getUserManager();

		Group group = groupManager.getGroupByID(groupId);
		if (group != null) {
			List existUser = userManager.getUserList(group);
			if (existUser == null) {
				existUser = new ArrayList();
			}
			request.setAttribute("existUser", existUser);
		}
		return mapping.findForward("groupFrame");
	}

	/**
	 * 保存一个用户组对应的角色（groupmanager/changeRole_ajax.jsp右边的角色列表）
	 * 
	 * @param groupId
	 * @param roleId
	 * @return
	 * @throws Exception
	 */
	public static String storeGroupRole(String groupId, String roleId)
			throws Exception {
		GroupManager groupManager = SecurityDatabase.getGroupManager();
		RoleManager roleManager = SecurityDatabase.getRoleManager();
		try {
			if(roleId.equals("")||roleId==null)
			{
				Group group = groupManager.getGroupByID(groupId);
				groupManager.deleteGrouprole(group);
			}
			else
			{
				String roleIds[] =roleId.split("\\,");
				DBUtil db = new DBUtil();
			
				for (int i = 0; (roleIds != null) && (i < roleIds.length); i++) {
					String sql ="select count(*) from td_sm_grouprole where " +
							" role_id ='"+ roleIds[i]+"' and group_id="+ groupId ;
					db.executeSelect(sql);
					if(db.getInt(0,0)==0){
						
						Group group = groupManager.getGroupByID(groupId);
					
							Grouprole grr = new Grouprole();
							grr.setGroup(group);
							Role role = roleManager.getRoleById( roleIds[i]);
							if (role != null) {
								Grouprole gr = new Grouprole();
								gr.setGroup(group);
								gr.setRole(role);
								groupManager.storeGrouprole(gr);
							}
					
					}
				}
			}
		

			return "success";
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			return "fail";
		}
	}

	/**
	 * 删除一个用户组对应的角色（groupmanager/changeRole_ajax.jsp右边的角色列表）
	 * 
	 * @param groupId
	 * @param roleId
	 * @return
	 * @throws Exception
	 */
	public static String deleteGroupRole(String groupId, String roleId)
			throws Exception {
		try {
			GroupManager groupManager = SecurityDatabase.getGroupManager();			
			RoleManager roleManager = SecurityDatabase.getRoleManager();
			Group group = groupManager.getGroupByID(groupId);
			if (group != null) {
				Grouprole grr = new Grouprole();
				grr.setGroup(group);
				Role role = roleManager.getRoleById( roleId);
				if (role != null) {
					Grouprole gr = new Grouprole();
					gr.setGroup(group);
					gr.setRole(role);
					groupManager.deleteGrouprole(gr);
				}
			}
			return "success";
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			return "fail";
		}
	}

	/**
	 * 保存用户组下的用户－ajax方式。景峰
	 * 
	 * @param groupId
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public static String storeGroupUser(String groupId, String userId)
			throws Exception {
		try {
			GroupManager groupManager = SecurityDatabase.getGroupManager();
			UserManager userManager = SecurityDatabase.getUserManager();
			Group group = groupManager.getGroupByID(groupId);
			// 根据用户组取对应的角色列表
			RoleManager roleManager = SecurityDatabase.getRoleManager();
			List rolelist = roleManager.getRoleList(group);
			User user = userManager.getUserById(userId);
			/*
			// 保存角色和用户的关系
			if (group != null) {
				for (int j = 0; rolelist != null && j < rolelist.size(); j++) {
					Role role = (Role) rolelist.get(j);
					if (user != null) {
						Userrole ur = new Userrole();
						ur.setUser(user);
						ur.setRole(role);
						if (!userManager.isUserroleExist(ur))
							userManager.storeUserrole(ur);
					}
				}
			}*/
			
			if (group != null) {
				Grouprole grr = new Grouprole();
				grr.setGroup(group);
				if (user != null) {
					Usergroup usergroup = new Usergroup();
					usergroup.setUser(user);
					usergroup.setGroup(group);
					userManager.storeUsergroup(usergroup);
				}
			}
			return "success";
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			return "fail";
		}
	}
	
	/**
	 * 删除用户组下的用户－ajax方式。景峰
	 * 
	 * @param groupId
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public static String deleteGroupUser(String groupId, String userId)
			throws Exception {
		try {
			GroupManager groupManager = SecurityDatabase.getGroupManager();
			UserManager userManager = SecurityDatabase.getUserManager();
			Group group = groupManager.getGroupByID(groupId);
			User user = userManager.getUserById(userId);
			if (group != null) {
				Grouprole grr = new Grouprole();
				grr.setGroup(group);
				if (user != null) {
					Usergroup usergroup = new Usergroup();
					usergroup.setUser(user);
					usergroup.setGroup(group);
					userManager.deleteUsergroup(usergroup);
				}
			}
			return "success";
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			return "fail";
		}
	}
}
