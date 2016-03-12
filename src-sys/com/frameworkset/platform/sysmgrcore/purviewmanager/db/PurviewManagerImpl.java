/**
 * 
 */
package com.frameworkset.platform.sysmgrcore.purviewmanager.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.transaction.RollbackException;

import org.frameworkset.event.Event;
import org.frameworkset.event.EventHandle;
import org.frameworkset.event.EventImpl;
import org.frameworkset.persitent.util.SQLUtil;

import com.frameworkset.common.poolman.ConfigSQLExecutor;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.common.poolman.SQLParams;
import com.frameworkset.orm.transaction.TransactionException;
import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.platform.config.ConfigManager;
import com.frameworkset.platform.config.model.ResourceInfo;
import com.frameworkset.platform.resource.ResourceManager;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.security.event.ACLEventType;
import com.frameworkset.platform.sysmgrcore.entity.Role;
import com.frameworkset.platform.sysmgrcore.entity.User;
import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.platform.sysmgrcore.manager.LogManager;
import com.frameworkset.platform.sysmgrcore.manager.RoleManager;
import com.frameworkset.platform.sysmgrcore.manager.db.LogManagerImpl;
import com.frameworkset.platform.sysmgrcore.manager.db.OrgCacheManager;
import com.frameworkset.platform.sysmgrcore.manager.db.RoleManagerImpl;
import com.frameworkset.platform.sysmgrcore.manager.db.UserManagerImpl;
import com.frameworkset.platform.sysmgrcore.purviewmanager.BussinessCheck;
import com.frameworkset.platform.sysmgrcore.purviewmanager.DefaultBussinessCheckImpl;
import com.frameworkset.platform.sysmgrcore.purviewmanager.PurviewManager;

/**
 * <p>
 * Title: PurviewManagerImpl.java
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
 * @Date 2008-3-6 9:34:20
 * @author ge.tao
 * @version 1.0
 */
public class PurviewManagerImpl extends EventHandle implements PurviewManager {
	private static ConfigSQLExecutor executor = new ConfigSQLExecutor("com/frameworkset/platform/sysmgrcore/manager/db/user.xml"); 
	private static BussinessCheck bussinessCheck;
	private SQLUtil sqlUtilInsert = SQLUtil
			.getInstance("org/frameworkset/insert.xml");
	static {
		if (bussinessCheck == null) {
			String bussinessCheck_ = ConfigManager
					.getInstance()
					.getConfigValue(
							"com.frameworkset.platform.sysmgrcore.purviewmanager.bussinesscheck",
							"com.frameworkset.platform.hnds.sysManager.BussinessCheckImpl");
			try {
				bussinessCheck = (BussinessCheck) Class
						.forName(bussinessCheck_).newInstance();
			} catch (InstantiationException e) {
				bussinessCheck = new DefaultBussinessCheckImpl();
			} catch (IllegalAccessException e) {
				bussinessCheck = new DefaultBussinessCheckImpl();
			} catch (ClassNotFoundException e) {
				bussinessCheck = new DefaultBussinessCheckImpl();
			}
		}
	}

	public static BussinessCheck getBussinessCheck() {
		return bussinessCheck;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.frameworkset.platform.sysmgrcore.purviewmanager.PurviewManager#
	 * reclaimUserDirectResources(java.lang.String)
	 */
	public String reclaimUserDirectResources(String userId) {
		if (strIsNull(userId))
			return "";
		return "delete from TD_SM_ROLERESOP where TYPES='user' and ROLE_ID='"
				+ userId + "' ";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.frameworkset.platform.sysmgrcore.purviewmanager.PurviewManager#reclaimUserRoles
	 * (java.lang.String)
	 */
	public String reclaimUserRoles(String userId) {
		if (strIsNull(userId))
			return "";
		return "delete from TD_SM_USERROLE where USER_ID=" + userId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.frameworkset.platform.sysmgrcore.purviewmanager.PurviewManager#reclaimUserJobs
	 * (java.lang.String)
	 */
	public String reclaimUserJobs(String userId) {
		if (strIsNull(userId))
			return "";
		return "delete from TD_SM_USERJOBORG  where USER_ID=" + userId
				+ " and JOB_ID !='" + EVERYONE_JOBID + "'";
	}

	/**
	 * 
	 * @param userId
	 * @return
	 */
	public String reclaimUserGroups(String userId) {
		if (strIsNull(userId))
			return "";
		return "delete from td_sm_usergroup  where user_id='" + userId + "'";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.frameworkset.platform.sysmgrcore.purviewmanager.PurviewManager#
	 * reclaimUserResources(java.lang.String)
	 */
	public String reclaimUserResources(AccessControl control, String userId,
			boolean isReclaimDirectRes, boolean isReclaimUserRoles,
			boolean isReclaimUserJobs, boolean isReclaimUserGroupRes) {
		// String restr = "";
		// if (strIsNull(userId))
		// return restr;
		// if (!isReclaimDirectRes && !isReclaimUserRoles && !isReclaimUserJobs
		// && !isReclaimUserGroupRes)
		// return restr;
		// List checkInfos = bussinessCheck.userReclaimCheck(control, userId);
		//
		// // 返回信息, 说明不能被删除或者调离, 直接返回信息.
		// if (checkInfos.size() > 0) {
		// UserManagerImpl userImpl = new UserManagerImpl();
		// User user = null;
		// try {
		// user = userImpl.getUserById(userId);
		// restr = user.getUserRealname() + "(" + user.getUserName() + ")"
		// + ":";
		// } catch (ManagerException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		// for (int i = 0; i < checkInfos.size(); i++) {
		// restr += String.valueOf(checkInfos.get(i));
		// }
		// // 格式 admin:业务1,业务2,...
		// return restr;
		// }
		//
		// DBUtil db = new DBUtil();
		// try {
		// if (isReclaimDirectRes) {
		// String reclaimUserDirectResources_sql =
		// reclaimUserDirectResources(userId);
		// if (!"".equalsIgnoreCase(reclaimUserDirectResources_sql))
		// ;
		// db.addBatch(reclaimUserDirectResources_sql);
		// }
		// if (isReclaimUserRoles) {
		// String reclaimUserRoles_sql = reclaimUserRoles(userId);
		// if (!"".equalsIgnoreCase(reclaimUserRoles_sql))
		// db.addBatch(reclaimUserRoles_sql);
		// }
		// if (isReclaimUserJobs) {
		// String reclaimUserJobs_sql = reclaimUserJobs(userId);
		// if (!"".equalsIgnoreCase(reclaimUserJobs_sql))
		// db.addBatch(reclaimUserJobs_sql);
		// }
		// if (isReclaimUserGroupRes) {
		// String reclaimUserGroupRes_sql = reclaimUserGroups(userId);
		// if (!"".equals(reclaimUserGroupRes_sql)) {
		// db.addBatch(reclaimUserGroupRes_sql);
		// }
		// }
		// db.executeBatch();
		// Event event = new EventImpl("",
		// ACLEventType.RESOURCE_ROLE_INFO_CHANGE);
		// super.change(event);
		// Event eventRole = new EventImpl("",
		// ACLEventType.USER_ROLE_INFO_CHANGE);
		// super.change(eventRole);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		return reclaimUserResources(control, userId, isReclaimDirectRes,
				isReclaimUserRoles, isReclaimUserJobs, isReclaimUserGroupRes,
				true);
	}

	public String reclaimUserResources(AccessControl control, String userId,
			boolean isReclaimDirectRes, boolean isReclaimUserRoles,
			boolean isReclaimUserJobs, boolean isReclaimUserGroupRes,
			boolean sendEvent) {
		String restr = "";
		if (strIsNull(userId))
			return restr;
		if (!isReclaimDirectRes && !isReclaimUserRoles && !isReclaimUserJobs
				&& !isReclaimUserGroupRes)
			return restr;
		List checkInfos = bussinessCheck.userReclaimCheck(control, userId);

		// 返回信息, 说明不能被删除或者调离, 直接返回信息.
		if (checkInfos.size() > 0) {
			UserManagerImpl userImpl = new UserManagerImpl();
			User user = null;
			try {
				user = userImpl.getUserById(userId);
				restr = user.getUserRealname() + "(" + user.getUserName() + ")"
						+ ":";
			} catch (ManagerException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			for (int i = 0; i < checkInfos.size(); i++) {
				restr += String.valueOf(checkInfos.get(i));
			}
			// 格式 admin:业务1,业务2,...
			return restr;
		}

		DBUtil db = new DBUtil();
		try {
			if (isReclaimDirectRes) {
				String reclaimUserDirectResources_sql = reclaimUserDirectResources(userId);
				if (!"".equalsIgnoreCase(reclaimUserDirectResources_sql))
					;
				db.addBatch(reclaimUserDirectResources_sql);
			}
			if (isReclaimUserRoles) {
				String reclaimUserRoles_sql = reclaimUserRoles(userId);
				if (!"".equalsIgnoreCase(reclaimUserRoles_sql))
					db.addBatch(reclaimUserRoles_sql);
			}
			if (isReclaimUserJobs) {
				String reclaimUserJobs_sql = reclaimUserJobs(userId);
				if (!"".equalsIgnoreCase(reclaimUserJobs_sql))
					db.addBatch(reclaimUserJobs_sql);
			}
			if (isReclaimUserGroupRes) {
				String reclaimUserGroupRes_sql = reclaimUserGroups(userId);
				if (!"".equals(reclaimUserGroupRes_sql)) {
					db.addBatch(reclaimUserGroupRes_sql);
				}
			}
			db.executeBatch();
			if (sendEvent) {
				Event event = new EventImpl("",
						ACLEventType.RESOURCE_ROLE_INFO_CHANGE);
				super.change(event);
				Event eventRole = new EventImpl("",
						ACLEventType.USER_ROLE_INFO_CHANGE);
				super.change(eventRole);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return restr;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.frameworkset.platform.sysmgrcore.purviewmanager.PurviewManager#
	 * reclaimUserResources(com.frameworkset.platform.security.AccessControl,
	 * java.util.List, boolean, boolean, boolean)
	 */
	public List reclaimUsersResources(AccessControl control, String[] userIds,
			boolean isReclaimDirectRes, boolean isReclaimUserRoles,
			boolean isReclaimUserJobs, boolean isReclaimUserGroupRes) {
		List optFailedList = new ArrayList();
		int count = 0;
		for (int i = 0; i < userIds.length; i++) {
			String infos = reclaimUserResources(control, String
					.valueOf(userIds[i]), isReclaimDirectRes,
					isReclaimUserRoles, isReclaimUserJobs,
					isReclaimUserGroupRes, false);
			if (!strIsNull(infos)) {
				optFailedList.add(infos);
			}
			count++;
		}
		if (count > 0) {
			if (isReclaimDirectRes) {
				Event event = new EventImpl("",
						ACLEventType.RESOURCE_ROLE_INFO_CHANGE);
				super.change(event);
			}
			if (isReclaimUserRoles || isReclaimUserJobs
					|| isReclaimUserGroupRes) {
				Event eventRole = new EventImpl("",
						ACLEventType.USER_ROLE_INFO_CHANGE);
				super.change(eventRole);
			}
		}

		return optFailedList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.frameworkset.platform.sysmgrcore.purviewmanager.PurviewManager#
	 * reclaimOrgUsersResources(java.lang.String, boolean)
	 */
	public List reclaimOrgUsersResources(AccessControl control, String orgId,
			boolean isReclaimDirectRes, boolean isReclaimUserRoles,
			boolean isReclaimUserJobs, boolean isReclaimUserGroupRes,
			boolean isRecursion) {
		List optFailedList = new ArrayList();
		String sqlstr = "select a.USER_ID from TD_SM_USERJOBORG a where a.ORG_ID= '"
				+ orgId + "' ";
		if (isRecursion) {// 递归回收
		// sqlstr =
		// "select a.USER_ID from TD_SM_USERJOBORG a where a.ORG_ID in "
		// + "(select o.ORG_ID from TD_SM_ORGANIZATION o start with o.ORG_ID='"
		// + orgId + "' connect by prior o.ORG_ID=o.PARENT_ID )";
		//			
			String concat_ = DBUtil.getDBAdapter().concat(" org_tree_level","'|%' ");
			sqlstr = "select USER_ID from TD_SM_USERJOBORG  where ORG_ID in"
					+ "(select t.org_id from TD_SM_ORGANIZATION t where t.org_tree_level like (select "
					+ concat_ 
					+ " from TD_SM_ORGANIZATION c  where c.org_id ='"
					+ orgId + "') or t.org_id in ('" + orgId + "'))";

		}
		DBUtil db = new DBUtil();
		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();
			db.executeSelect(sqlstr);
			
			for (int i = 0; i < db.size(); i++) {
				String userId = db.getString(i, "USER_ID");
				String infos = reclaimUserResources(control, userId,
						isReclaimDirectRes, isReclaimUserRoles,
						isReclaimUserJobs, isReclaimUserGroupRes, false);
				if (this.strIsNull(infos))
					continue;
				optFailedList.add(infos);
			}
			tm.commit();
			Event event = new EventImpl("",
					ACLEventType.RESOURCE_ROLE_INFO_CHANGE);
			super.change(event);
			Event eventRole = new EventImpl("",
					ACLEventType.USER_ROLE_INFO_CHANGE);
			super.change(eventRole);

		} catch (SQLException e) {
			e.printStackTrace();
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (TransactionException e) {
			e.printStackTrace();
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return optFailedList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.frameworkset.platform.sysmgrcore.purviewmanager.PurviewManager#
	 * reclaimRoleResources(java.lang.String)
	 */
	public List reclaimRoleResources(String[] roleIds) {
		List optFailedList = null;
		if (roleIds == null)
			return optFailedList;
		optFailedList = new ArrayList();
		DBUtil db = new DBUtil();
		RoleManager roleManager = new RoleManagerImpl();
		try {
			for (int i = 0; i < roleIds.length; i++) {
				String roleId = String.valueOf(roleIds[i]);
				if (strIsNull(roleId))
					continue;
				// 检测角色 是否可以 资源回收
				// 不能回收的角色, 记录到List中
				String checkInfos = bussinessCheck.roleReclaimCheck(roleId);
				if (!"".equalsIgnoreCase(checkInfos)) {
					Role role = roleManager.getRoleById(roleId);
					optFailedList.add(role.getRoleName() + ":" + checkInfos);
					continue;
				}
				String sqlstr = "delete from TD_SM_ROLERESOP where TYPES='role' and ROLE_ID='"
						+ roleId + "' ";
				db.addBatch(sqlstr);
			}
			db.executeBatch();
			Event event = new EventImpl("",
					ACLEventType.RESOURCE_ROLE_INFO_CHANGE);
			super.change(event);
			return optFailedList;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return optFailedList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.frameworkset.platform.sysmgrcore.purviewmanager.PurviewManager#
	 * reclaimOrgDirectResources(java.lang.String)
	 */
	public String reclaimOrgDirectResources(String orgId) {
		if (strIsNull(orgId))
			return "";
		return "delete from TD_SM_ROLERESOP where TYPES='organization' and ROLE_ID='"
				+ orgId + "' ";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.frameworkset.platform.sysmgrcore.purviewmanager.PurviewManager#reclaimOrgRoles
	 * (java.lang.String)
	 */
	public String reclaimOrgRoles(String orgId) {
		if (strIsNull(orgId))
			return "";
		return "delete from TD_SM_ORGROLE where  ORG_ID='" + orgId + "' ";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.frameworkset.platform.sysmgrcore.purviewmanager.PurviewManager#reclaimOrgResources
	 * (java.lang.String)
	 */
	public boolean reclaimOrgResources(String orgId,
			boolean isReclaimOrgDirectRes, boolean isReclaimOrgRoleRes,
			boolean isRecursion) {
		DBUtil db = new DBUtil();
		DBUtil batch = new DBUtil();
		String org_sql = "select org_id from TD_SM_ORGANIZATION t where t.org_id='"
				+ orgId + "'";
		if (isRecursion) {
			// org_sql =
			// "select org_id from TD_SM_ORGANIZATION t start with t.org_id='"
			// + orgId + "' " + "connect by prior t.ORG_ID=t.PARENT_ID ";
			String concat_ = DBUtil.getDBAdapter().concat(" org_tree_level","'|%' ");
			org_sql = "select t.org_id from TD_SM_ORGANIZATION t where t.org_tree_level like (select "
				    + concat_
					+ " from TD_SM_ORGANIZATION c  where c.org_id ='"
					+ orgId + "') or  t.org_id =" + orgId ;
		}
		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();
			db.executeSelect(org_sql);
			
			for (int i = 0; i < db.size(); i++) {
				String org_id = db.getString(i, "org_id");
				if (isReclaimOrgDirectRes) {
					String reclaimOrgDirectResources_sql = reclaimOrgDirectResources(org_id);
					if (!"".equalsIgnoreCase(reclaimOrgDirectResources_sql))
						;
					batch.addBatch(reclaimOrgDirectResources_sql);
				}
				if (isReclaimOrgRoleRes) {
					String reclaimOrgRoles_sql = reclaimOrgRoles(org_id);
					if (!"".equalsIgnoreCase(reclaimOrgRoles_sql))
						batch.addBatch(reclaimOrgRoles_sql);
				}
			}
			batch.executeBatch();
			tm.commit();
			Event event = new EventImpl("",
					ACLEventType.RESOURCE_ROLE_INFO_CHANGE);
			super.change(event);
			Event eventRole = new EventImpl("",
					ACLEventType.USER_ROLE_INFO_CHANGE);
			super.change(eventRole);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				e1.printStackTrace();
			}
		}
		return false;
	}

	private boolean strIsNull(String str) {
		if (str == null || "".equalsIgnoreCase(str) || str.trim().length() == 0) {
			return true;
		}
		return false;
	}

	public boolean userResCopy(String userId, String[] checkUserIds,
			String userSelf, String userRole, String userJob, String curOrgId) {
		boolean state = false;
		TransactionManager tm = new TransactionManager();
		DBUtil dbSelf = new DBUtil();
		DBUtil dbRole = new DBUtil();
		DBUtil dbJob = new DBUtil();
		if (checkUserIds == null) {
			return false;
		}
		StringBuffer sqlSelf = new StringBuffer();
		StringBuffer sqlRole = new StringBuffer();
		StringBuffer sqlJob = new StringBuffer();
		try {
			tm.begin();
			for (int i = 0; i < checkUserIds.length; i++) {
				// 复制用户自身权限

				if ("self".equals(userSelf)) {
					sqlSelf
							.append(
									"insert into td_sm_roleresop(op_id,res_id,res_name,restype_id,types,role_id) ")
							.append(
									" select b.op_id,b.res_id,b.res_name,b.restype_id,b.types,'")
							.append(checkUserIds[i])
							.append("' from ")
							.append(
									" td_sm_roleresop b where b.types = 'user' and b.role_id = '")
							.append(userId)
							.append(
									"'and not exists( select c.op_id, c.res_id, c.res_name, c.restype_id, c.types, c.role_id")
							.append(
									" from td_sm_roleresop c where c.types = 'user' and c.op_id=b.op_id  and c.res_name=b.res_name")
							.append(
									" and c.restype_id=b.restype_id and c.role_id = '")
							.append(checkUserIds[i]).append(" ')");
					// .append("' minus select ")
					// .append(" c.op_id,c.res_id,c.res_name,c.restype_id,c.types,c.role_id from td_sm_roleresop c ")
					// .append(" where c.role_id='").append(checkUserIds[i]).append(
					// "' and c.types = 'user'");
					
					dbSelf.addBatch(sqlSelf.toString());
					// System.out.println(sqlSelf.toString());
					sqlSelf.setLength(0);
				}
				// 复制用户的角色给选定用户
				if ("role".equals(userRole)) {
					sqlRole
							.append(
									"insert into td_sm_userrole(user_id, role_id, RESOP_ORIGIN_USERID) select ")
							.append(checkUserIds[i])
							.append(
									",a.role_id,a.RESOP_ORIGIN_USERID from td_sm_userrole a ")
							.append("where a.user_id='")
							.append(userId)
							.append("' and a.role_id in(")
							.append(
									"select t.role_id from td_sm_userrole t where t.user_id = '")
							.append(userId)
							.append(
									" ' and not exists ( select tt.role_id from td_sm_userrole tt where tt.user_id = '")
							.append(checkUserIds[i]).append(
									" ' and tt.role_id=t.role_id ))");
					// .append("' minus select tt.role_id from td_sm_userrole tt where tt.user_id = '")
					// .append(checkUserIds[i]).append("') ");
					dbRole.addBatch(sqlRole.toString());
					// System.out.println(sqlRole.toString());
					sqlRole.setLength(0);
				}
				// 复制用户的岗位资源给选定用户
				if ("job".equals(userJob)) {
					sqlJob
							.append(
									"insert into td_sm_userjoborg(user_id,job_id,org_id) select ")
							.append(checkUserIds[i])
							.append(
									",a.job_id,a.org_id from td_sm_userjoborg a ")
							.append("where a.user_id=")
							.append(userId)
							.append(" and a.org_id='")
							.append(curOrgId)
							.append(
									" 'and not exists ( select b.user_id, b.job_id, b.org_id from td_sm_userjoborg b  where b.user_id = '")
							.append(checkUserIds[i]).append(
									" ' and b.org_id = '").append(curOrgId)
							.append(" ' and b.job_id=a.job_id)");

					// .append("' minus select b.user_id,b.job_id,b.org_id from td_sm_userjoborg b where b.user_id=")
					// .append(checkUserIds[i]).append(" and b.org_id='").append(curOrgId).append("'");
					// System.out.println(sqlJob.toString());
					dbJob.addBatch(sqlJob.toString());
					sqlJob.setLength(0);
				}
			}
			if ("self".equals(userSelf)) {
				dbSelf.executeBatch();
			}
			if ("role".equals(userRole)) {
				dbRole.executeBatch();
			}
			if ("job".equals(userJob)) {
				dbJob.executeBatch();
			}
			tm.commit();
			Event event = new EventImpl("",
					ACLEventType.RESOURCE_ROLE_INFO_CHANGE);
			super.change(event);
			state = true;
		} catch (Exception e) {
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			sqlSelf = null;
			sqlRole = null;
			sqlJob = null;
		}
		return state;
	}

	public boolean userResCopySelf(String userId, String[] checkUserIds,
			String userSelf, String userRole, String userJob, String curOrgId) {
		boolean state = false;
		TransactionManager tm = new TransactionManager();
		DBUtil dbSelf = new DBUtil();
		DBUtil dbRole = new DBUtil();
		DBUtil dbJob = new DBUtil();
		if (checkUserIds == null) {
			return false;
		}
		StringBuffer sqlSelf = new StringBuffer();
		StringBuffer sqlRole = new StringBuffer();
		StringBuffer sqlJob = new StringBuffer();
		try {
			tm.begin();
			for (int i = 0; i < checkUserIds.length; i++) {
				// 复制用户自身权限

				if ("self".equals(userSelf)) {
					sqlSelf
							.append(
									"insert into td_sm_roleresop(op_id,res_id,res_name,restype_id,types,role_id) ")
							.append(
									" select b.op_id,b.res_id,b.res_name,b.restype_id,b.types,'")
							.append(userId)
							.append("' from ")
							.append(
									" td_sm_roleresop b where b.types = 'user' and b.role_id = '")
							.append(checkUserIds[i])
							.append("'  and not exists (  select  ")
							.append(
									" c.op_id,c.res_id,c.res_name,c.restype_id,c.types,c.role_id from td_sm_roleresop c ")
							.append(" where c.role_id='")
							.append(userId)
							.append(
									"' and c.types = 'user' and  c.op_id=b.op_id and c.res_name=b.res_name and c.restype_id=b.restype_id)");

					// .append("' minus select ")
					// .append(
					// " c.op_id,c.res_id,c.res_name,c.restype_id,c.types,c.role_id from td_sm_roleresop c ")
					// .append(" where c.role_id='").append(userId)
					// .append("' and c.types = 'user'");
				
					dbSelf.addBatch(sqlSelf.toString());
					
					// System.out.println(sqlSelf.toString());
					sqlSelf.setLength(0);
				}
				// 复制用户的角色给选定用户
				if ("role".equals(userRole)) {
					sqlRole
							.append(
									"insert into td_sm_userrole(user_id, role_id, RESOP_ORIGIN_USERID) select ")
							.append(userId)
							.append(
									",a.role_id,a.RESOP_ORIGIN_USERID from td_sm_userrole a ")
							.append("where a.user_id='")
							.append(checkUserIds[i])
							.append("' and a.role_id in(")
							.append(
									"select t.role_id from td_sm_userrole t where t.user_id = '")
							.append(checkUserIds[i])
							.append(
									"' and not exists ( select tt.role_id from td_sm_userrole tt where tt.user_id = '")
							.append(userId).append("' ").append(
									" and  tt.role_id=t.role_id ))");
					// .append("' minus select tt.role_id from td_sm_userrole tt where tt.user_id = '")
					// .append(userId).append("') ");

					dbRole.addBatch(sqlRole.toString());
					sqlRole.setLength(0);
				}
				// 复制用户的岗位资源给选定用户
				if ("job".equals(userJob)) {
					sqlJob
							.append(
									"insert into td_sm_userjoborg(user_id,job_id,org_id) select ")
							.append(userId)
							.append(
									",a.job_id,a.org_id from td_sm_userjoborg a ")
							.append("where a.user_id=")
							.append(checkUserIds[i])
							.append(" and a.org_id='")
							.append(curOrgId)
							.append(
									"'  and not exists ( select b.user_id,b.job_id,b.org_id from td_sm_userjoborg b where b.user_id=")
							.append(userId).append(" and b.org_id='").append(
									curOrgId).append(
									"' and  b.job_id=a.job_id)");
					// .append("' minus select b.user_id,b.job_id,b.org_id from td_sm_userjoborg b where b.user_id=")
					// .append(userId).append(" and b.org_id='").append(curOrgId).append("'");
					// System.out.println(sqlJob.toString());
					dbJob.addBatch(sqlJob.toString());
					sqlJob.setLength(0);
				}
			}
			if ("self".equals(userSelf)) {
				dbSelf.executeBatch();
			}
			if ("role".equals(userRole)) {
				dbRole.executeBatch();
			}
			if ("job".equals(userJob)) {
				dbJob.executeBatch();
			}
			tm.commit();
			Event event = new EventImpl("",
					ACLEventType.RESOURCE_ROLE_INFO_CHANGE);
			super.change(event);
			state = true;
		} catch (Exception e) {
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			sqlSelf = null;
			sqlRole = null;
			sqlJob = null;
		}
		return state;
	}

	public boolean isAdminOrOrgmanager(String curOrgId, String userId) {
		boolean state = false;
		DBUtil db = new DBUtil();
		StringBuffer sql = new StringBuffer();
		sql
				.append(
						"select count(*) from (select a.user_id from td_sm_userrole a ")
				.append("where a.user_id = '").append(userId).append("' and ")
				.append("a.role_id = '1' union select b.user_id from ").append(
						"td_sm_orgmanager b where b.user_id = '")
				.append(userId).append("' and b.org_id = '").append(curOrgId)
				.append("') c ");
		// System.out.println(sql.toString());
		try {
			db.executeSelect(sql.toString());
			if (db.getInt(0, 0) > 0) {
				state = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return state;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.frameworkset.platform.sysmgrcore.purviewmanager.PurviewManager#getOrgNamesByOrgIds
	 * (java.lang.String)
	 */
	public String getOrgNameByOrgId(String orgId) {
		if (this.strIsNull(orgId))
			return "";
		String orgName = "";
		try {
			orgName = OrgCacheManager.getInstance().getOrganization(orgId)
					.getRemark5();
		} catch (ManagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return orgName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.frameworkset.platform.sysmgrcore.purviewmanager.PurviewManager#
	 * getUserNamesByUserIds(java.lang.String)
	 */
	public String getUserNamesByUserIds(String userIds) {
		if (this.strIsNull(userIds))
			return "";
		String userNames = "";
		String sqlstr = "select t.USER_NAME,t.USER_REALNAME from td_sm_user t where t.USER_ID in("
				+ userIds + ")";
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(sqlstr);
			for (int i = 0; i < db.size(); i++) {
				if ("".equalsIgnoreCase(userNames)) {
					userNames = db.getString(i, "USER_REALNAME") + "("
							+ db.getString(i, "USER_NAME") + ")";
				} else {
					userNames += "," + db.getString(i, "USER_REALNAME") + "("
							+ db.getString(i, "USER_NAME") + ")";
				}
			}
			return userNames;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userNames;
	}

	/**
	 * 保存树 权限授予资源
	 * 
	 * @param opId
	 *            操作ID
	 * @param checkValues
	 *            res_id^#restype_id^#res_name 选中复选框的值
	 * @param restypeId
	 *            资源类型ID（菜单retype_id包括'column','cs_column','report_column'）
	 * @param roleId
	 * @param types
	 *            授予类型类别
	 * @param isColumn
	 *            是否是菜单资源
	 * @return
	 */
	public boolean saveTreeRoleresop(String opId, String restypeId,
			String[] checkValues, String roleId, String types, String isColumn) {
		return this.saveTreeRoleresop(opId, restypeId, checkValues, roleId,
				types, isColumn, null);
	}

	/**
	 * 保存树 权限授予资源
	 * 
	 * @param opId
	 *            操作ID
	 * @param checkValues
	 *            res_id^#restype_id^#res_name 选中复选框的值
	 * @param restypeId
	 *            资源类型ID（菜单retype_id包括'column','cs_column','report_column'）
	 * @param roleId
	 * @param types
	 *            授予类型类别
	 * @param isColumn
	 *            是否是菜单资源
	 * @return
	 */
	public boolean saveTreeRoleresop(String opId, String restypeId,
			String[] checkValues, String roleId, String types, String isColumn,
			String[] unselectValues) {
		DBUtil del_db = new DBUtil();
		DBUtil add_db = new DBUtil();
		boolean state = false;
		int arrLength = 0;
		if (checkValues == null) {

		} else {
			arrLength = checkValues.length;
		}
		String[] resIds = new String[arrLength];
		String[] resNames = new String[arrLength];
		String[] resTypeIds = new String[arrLength];
		String resId = "";
		for (int i = 0; checkValues != null && i < checkValues.length; i++) {
			String[] checkValue = checkValues[i].split("#");
			if (checkValue.length >= 3) {
				resIds[i] = checkValue[0];
				if (resId.equals("")) {
					resId = "'" + checkValue[0] + "'";
				} else {
					resId += "," + "'" + checkValue[0] + "'";
				}
				resTypeIds[i] = checkValue[1];
				resNames[i] = checkValue[2];
			}
		}
		// 可删除的资源
		// String[] unresIds = new String[unarrLength];
		String unresId = "";
		for (int i = 0; unselectValues != null && !"".equals(unselectValues[0])
				&& i < unselectValues.length; i++) {
			String[] checkValue = unselectValues[i].split("#");
			if (checkValue.length >= 3) {
				// unresIds[i] = unselectValues[0];
				if (unresId.equals("")) {
					unresId = "'" + checkValue[0] + "'";
				} else {
					unresId += "," + "'" + checkValue[0] + "'";
				}
			}
		}
		// 创建事务
		TransactionManager tm = new TransactionManager();
		// 删除未选中的复选框的相应资源
		StringBuffer del_sql = new StringBuffer().append(
				"delete from td_sm_roleresop ").append("where op_id='").append(
				opId).append("' ");
		if ("column".equals(isColumn)) {// 如果是菜单资源则restype_id有三个
			del_sql
					.append(" and restype_id in('column','cs_column','report_column') ");
		} else {
			del_sql.append(" and restype_id='").append(restypeId).append("' ");
		}
		del_sql.append(" and role_id='").append(roleId).append("' and types='")
				.append(types).append("'");
		if (!unresId.equals("")) {
			del_sql.append(" and res_id in (").append(unresId).append(")");
		}
		// System.out.println("del_sql = " + del_sql.toString());

		try {
			tm.begin();
			del_db.executeDelete(del_sql.toString());

			// 添加选中项资源
			String sql = sqlUtilInsert.getSQL("purviewmanagerimpl_saveTreeRoleresop");
			String sql_ = sqlUtilInsert.getSQL("purviewmanagerimpl_saveTreeRoleresop_");;
			PreparedDBUtil pe = new PreparedDBUtil();
			PreparedDBUtil pe_ = new PreparedDBUtil();
			if (resIds.length > 0) {
				for (int j = 0; resIds != null && j < resIds.length; j++) {
					
					

//					add_sql_1.append(
//							"select count(op_id)  from td_sm_roleresop where ")
//							.append(" op_id='").append(opId).append("' ")
//							.append(" and res_id='").append(resIds[j]).append(
//									"' ").append(" and role_id='").append(
//									roleId).append("' ").append(
//									" and restype_id='").append(resTypeIds[j])
//							.append("' ").append(" and types='").append(types)
//							.append("' ");
					
					int id;
					
					pe.preparedSelect(sql);
					pe.setString(1, opId);
					pe.setString(2, resIds[j]);
					pe.setString(3, roleId);
					pe.setString(4, resTypeIds[j]);
					pe.setString(5, types);
					pe.executePrepared();
					id = pe.getInt(0, 0);
					

					if (id <= 0) {
						
						

//						add_sql
//								.append("insert  into ")
//								.append(
//										"td_sm_roleresop(OP_ID,RES_ID,ROLE_ID,RESTYPE_ID,RES_NAME,TYPES) ")
//								.append("values('").append(opId).append("','")
//								.append(resIds[j]).append("','").append(roleId)
//								.append("','").append(resTypeIds[j]).append(
//										"','").append(resNames[j])
//								.append("','").append(types).append("')");

						// select count(op_id) totalsize from td_sm_roleresop
						// where ")
						// .append(" op_id='").append(opId).append("' ")
						// .append(" and res_id='").append(resIds[j]).append(
						// "' ").append(" and role_id='").append(
						// roleId).append("' ").append(
						// " and restype_id='").append(resTypeIds[j])
						// .append("' ")
						// // .append(" and
						// // res_name='").append(resNames[j]).append("' ")
						// .append(" and types='").append(types).append("' ");
						pe_.preparedInsert(sql_);
						pe_.setString(1, opId);
						pe_.setString(2, resIds[j]);
						pe_.setString(3, roleId);
						pe_.setString(4, resTypeIds[j]);
						pe_.setString(5, resNames[j]);
						pe_.setString(6, types);
						pe_.executePrepared();
						 
						
					}
					
				}

			}
			state = true;
			tm.commit();
			// begin log
			if (opId != null && !"".equals(opId)) {
				ResourceManager resourceManager = new ResourceManager();
				StringBuffer opidstr = new StringBuffer();
				if (resourceManager.getOperation(restypeId, opId) != null) {
					opidstr.append(resourceManager
							.getOperation(restypeId, opId).getName());
				}
				LogManager logManager = new LogManagerImpl();

				ResourceInfo resourceInfo = resourceManager
						.getResourceInfoByType(restypeId);
				String resourceName = "";
				if (resourceInfo != null) {
					resourceName = resourceInfo.getName();
				}
				StringBuffer operContent = new StringBuffer();
				if (types.equals("user")) {
					operContent.append("为用户(用户ID)");
				} else if (types.equals("role")) {
					operContent.append("为角色(角色ID)");
				} else if (types.equals("organization")) {
					operContent.append("为机构(机构ID)");
				}
				operContent.append("【").append(roleId).append("】授予").append(
						resourceName).append("中的资源操作项【").append(opidstr)
						.append("】");
				logManager.log(operContent.toString(), "权限管理");
			}
			// end log
			Event event = new EventImpl("",
					ACLEventType.RESOURCE_ROLE_INFO_CHANGE);
			super.change(event);
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				e1.printStackTrace();
			}
		} catch (TransactionException e) {
			e.printStackTrace();
			try {
				// 回滚
				tm.rollback();
			} catch (RollbackException e1) {
				e1.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				e1.printStackTrace();
			}
		}
		// System.out.println("state = " + state);
		return state;
	}

	/**
	 * 保存角色授予权限
	 * 
	 * @param opId
	 *            操作ID
	 * @param resType_id
	 *            资源
	 * @param checkValues
	 *            res_id#res_name 选中复选框的值 选中项
	 * @param un_checkValues
	 *            res_id#res_name 选中复选框的值 未选中项
	 * @param role_id
	 * @param types
	 *            类型
	 * @return
	 */
	public boolean saveRoleListRoleresop(String opId, String resType_id,
			String[] checkValues, String[] un_checkValues, String role_id,
			String types) {
//		DBUtil del_db = new DBUtil();
//		DBUtil add_db = new DBUtil();
		boolean state = false;
		int arrlength = 0;
		int un_arrlength = 0;
		if (checkValues != null) {
			arrlength = checkValues.length;
		}
		if (un_checkValues != null) {
			un_arrlength = un_checkValues.length;
		}
		// 选中项
		String[] resId = new String[arrlength];
		String[] resName = new String[arrlength];
		for (int i = 0; i < arrlength; i++) {
			String checkValue = checkValues[i];
			String[] arr = checkValue.split("#");
			resId[i] = arr[0];
			resName[i] = arr[1];
		}
		// 未选中项删除操作
		String[] un_resId = new String[un_arrlength];
		for (int i = 0; i < un_arrlength; i++) {
			String un_checkValue = un_checkValues[i];
			String[] un_arr = un_checkValue.split("#");
			un_resId[i] = un_arr[0];
//			if ("".equals(un_resId)) {
//				un_resId = "'" + un_arr[0] + "'";
//			} else {
//				un_resId += "," + "'" + un_arr[0] + "'";
//			}
		}
		// 创建事务
		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();
			if (un_resId.length > 0) {
				SQLParams params = new SQLParams();
				params.addSQLParam("role_id", role_id, SQLParams.STRING);
				params.addSQLParam("opId", opId, SQLParams.STRING);
				params.addSQLParam("resType_id", resType_id, SQLParams.STRING);
				params.addSQLParam("types", types, SQLParams.STRING);
				params.addSQLParam("un_resId", un_resId, SQLParams.OBJECT);
				executor.deleteBean("saveRoleListRoleresop_delete", params);
//				StringBuffer del_sql = new StringBuffer().append(
//						"delete from td_sm_roleresop where role_id='").append(
//						role_id).append("' ").append("and op_id='")
//						.append(opId).append("' and restype_id='").append(
//								resType_id).append("' and types='").append(
//								types).append("' and res_id in(").append(
//								un_resId).append(")");
//
//				del_db.executeDelete(del_sql.toString());
			}
			// 添加选中资源
			StringBuffer add_sql = new StringBuffer();
			PreparedDBUtil prepareddbutil = new PreparedDBUtil();
			PreparedDBUtil prepareddbutil_ = new PreparedDBUtil();
			if (resId.length > 0) {
				for (int j = 0; j < resId.length; j++) {

					String sql = sqlUtilInsert
							.getSQL("purviewmanagerimpl_saveRoleListRoleresop");
					prepareddbutil.preparedSelect(sql);
					prepareddbutil.setString(1, opId);
					prepareddbutil.setString(2, resId[j]);
					prepareddbutil.setString(3, role_id);
					prepareddbutil.setString(4, resType_id);
					prepareddbutil.setString(5, types);
					prepareddbutil.executePrepared();

					if (prepareddbutil.getInt(0, 0) <= 0) {
						String sql_ = sqlUtilInsert
								.getSQL("purviewmanagerimpl_saveRoleListRoleresop_");
						prepareddbutil_.preparedInsert(sql_);
						prepareddbutil_.setString(1, opId);
						prepareddbutil_.setString(2, resId[j]);
						prepareddbutil_.setString(3, role_id);
						prepareddbutil_.setString(4, resType_id);
						prepareddbutil_.setString(5, resName[j]);
						prepareddbutil_.setString(6, types);
						prepareddbutil_.executePrepared();
						sql_ = "";
					}
					sql = "";

					// add_sql
					// .append("insert all when totalsize <= 0 then into ")
					// .append(
					// "td_sm_roleresop(OP_ID,RES_ID,ROLE_ID,RESTYPE_ID,RES_NAME,TYPES) ")
					// .append("values('")
					// .append(opId)
					// .append("','")
					// .append(resId[j])
					// .append("','")
					// .append(role_id)
					// .append("','")
					// .append(resType_id)
					// .append("','")
					// .append(resName[j])
					// .append("','")
					// .append(types)
					// .append(
					// "') select count(op_id) totalsize from td_sm_roleresop where ")
					// .append(" op_id='").append(opId).append("' ")
					// .append(" and res_id='").append(resId[j]).append(
					// "' ").append(" and role_id='").append(
					// role_id).append("' ").append(
					// " and restype_id='").append(resType_id)
					// .append("' ")
					// // .append(" and
					// // res_name='").append(resName[j]).append("' ")
					// .append(" and types='").append(types).append("' ");
					// add_db.addBatch(add_sql.toString());
					// add_sql.setLength(0);
				}
				// add_db.executeBatch();
			}

			tm.commit();
			// begin log
			if (opId != null && !"".equals(opId)) {
				ResourceManager resourceManager = new ResourceManager();
				StringBuffer opidstr = new StringBuffer();
				if (resourceManager.getOperation(resType_id, opId) != null) {
					opidstr.append(resourceManager.getOperation(resType_id,
							opId).getName());
				}
				LogManager logManager = new LogManagerImpl();

				ResourceInfo resourceInfo = resourceManager
						.getResourceInfoByType(resType_id);
				String resourceName = "";
				if (resourceInfo != null) {
					resourceName = resourceInfo.getName();
				}
				StringBuffer operContent = new StringBuffer();
				if (types.equals("user")) {
					operContent.append("为用户(用户ID)");
				} else if (types.equals("role")) {
					operContent.append("为角色(角色ID)");
				} else if (types.equals("organization")) {
					operContent.append("为机构(机构ID)");
				}
				operContent.append("【").append(role_id).append("】授予").append(
						resourceName).append("中的资源操作项【").append(opidstr)
						.append("】");
				logManager.log(operContent.toString(), "权限管理");
			}
			// end log
			state = true;
			Event event = new EventImpl("",
					ACLEventType.RESOURCE_ROLE_INFO_CHANGE);
			super.change(event);
		} catch (SQLException e) {
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} catch (TransactionException e) {
			e.printStackTrace();
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				e1.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				e1.printStackTrace();
			}
		}
		return state;
	}

	public boolean batchSaveTreeRoleresop(String opId, String restype_id,
			String[] checkValues, String roleId, String types) {
		// DBUtil add_db = new DBUtil();
		boolean state = false;
		int arrLength = 0;
		if (checkValues == null) {

		} else {
			arrLength = checkValues.length;
		}
		String[] resIds = new String[arrLength];
		String[] resNames = new String[arrLength];
		String[] resTypeIds = new String[arrLength];
		String resId = "";
		for (int i = 0; checkValues != null && i < checkValues.length; i++) {
			String[] checkValue = checkValues[i].split("#");
			if (checkValue.length >= 3) {
				resIds[i] = checkValue[0];
				if (resId.equals("")) {
					resId = "'" + checkValue[0] + "'";
				} else {
					resId += "," + "'" + checkValue[0] + "'";
				}
				resTypeIds[i] = checkValue[1];
				resNames[i] = checkValue[2];
			}
		}
		String[] roleIds = roleId.split(",");
		// 创建事务
		TransactionManager tm = new TransactionManager();

		PreparedDBUtil preparedDBUtilour = new PreparedDBUtil();
		PreparedDBUtil tempDBUtil = new PreparedDBUtil();
		try {
			tm.begin();
			// 添加选中项资源
			StringBuffer add_sql = new StringBuffer();
			// String temp =
			// "select count(op_id) totalsize from td_sm_roleresop where  op_id='readorgname'  and res_id='7'  and role_id='9'  and restype_id='orgunit'  and types='role'";

			// System.out.println("s"+totalsize.toString());
			StringBuffer sql1 = new StringBuffer();
			sql1
					.append(sqlUtilInsert
							.getSQL("purviewmanagerimpl_batchSaveTreeRoleresop"));
			StringBuffer sql2 = new StringBuffer();
			sql2.append(sqlUtilInsert
					.getSQL("roleManagerImpl_grantRoleresop2"));
			if (resIds.length > 0) {
				for (int i = 0; i < roleIds.length; i++) {
					for (int j = 0; resIds != null && j < resIds.length; j++) {
						
						preparedDBUtilour.preparedSelect(sql1.toString());
						preparedDBUtilour.setString(1, opId);
						preparedDBUtilour.setString(2, resIds[j]);
						preparedDBUtilour.setString(3, roleIds[i]);
						preparedDBUtilour.setString(5, resTypeIds[j]);
						preparedDBUtilour.setString(4, types);
						preparedDBUtilour.executePrepared();
						int count = preparedDBUtilour.getInt(0, 0);
						if (count <= 0) {

							
							tempDBUtil.preparedInsert(sql2.toString());
							tempDBUtil.setString(1, opId);
							tempDBUtil.setString(2, resIds[j]);
							tempDBUtil.setString(3, roleIds[i]);
							tempDBUtil.setString(4, resTypeIds[j]);
							tempDBUtil.setString(5, resNames[j]);
							tempDBUtil.setString(6, types);
							tempDBUtil.executePrepared();

//							sql2.setLength(0);
//							sql1.setLength(0);
							//							
							// add_sql.append(
							// "insert into td_sm_roleresop(OP_ID,RES_ID,ROLE_ID,RESTYPE_ID,RES_NAME,TYPES) ")
							// .append("values('")
							// .append(opId)
							// .append("','")
							// .append(resIds[j])
							// .append("','")
							// .append(roleIds[i])
							// .append("','")
							// .append(resTypeIds[j])
							// .append("','")
							// .append(resNames[j])
							// .append("','")
							// .append(types)
							// .append("')");
							// "') where totalsieze<=0 (select count(op_id) totalsize from td_sm_roleresop where ")
							// .append(" op_id='").append(opId).append("' ")
							// .append(" and res_id='").append(resIds[j])
							// .append("' ").append(" and role_id='").append(
							// roleIds[i]).append("' ").append(
							// " and restype_id='").append(
							// resTypeIds[j]).append("' ")
							// // .append(" and
							// // res_name='").append(resNames[j]).append("' ")
							// .append(" and types='").append(types).append(
							// "' )");
							// System.out.println("lengthis:"+add_sql.length());
							// System.out.println("string:"+add_sql.toString());
							// add_db.addBatch(add_sql.toString());
							// // System.out.println("sql = " +
							// add_sql.toString());
							// add_sql.setLength(0);
						}
					}
				}
				// add_db.executeBatch();
			}

			state = true;
			tm.commit();
			// begin log
			if (opId != null && !"".equals(opId)) {
				ResourceManager resourceManager = new ResourceManager();
				StringBuffer opidstr = new StringBuffer();
				if (resourceManager.getOperation(restype_id, opId) != null) {
					opidstr.append(resourceManager.getOperation(restype_id,
							opId).getName());
				}
				LogManager logManager = new LogManagerImpl();

				ResourceInfo resourceInfo = resourceManager
						.getResourceInfoByType(restype_id);
				String resourceName = "";
				if (resourceInfo != null) {
					resourceName = resourceInfo.getName();
				}
				StringBuffer operContent = new StringBuffer();
				if (types.equals("user")) {
					operContent.append("批量为用户(用户ID)");
				} else if (types.equals("role")) {
					operContent.append("批量为角色(角色ID)");
				} else if (types.equals("organization")) {
					operContent.append("批量为机构(机构ID)");
				}
				operContent.append("【").append(roleId).append("】授予").append(
						resourceName).append("中的资源操作项【").append(opidstr)
						.append("】");
				logManager.log(operContent.toString(), "权限管理");
			}
			Event event = new EventImpl("",
					ACLEventType.RESOURCE_ROLE_INFO_CHANGE);
			super.change(event);
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				e1.printStackTrace();
			}
		} catch (TransactionException e) {
			e.printStackTrace();
			try {
				// 回滚
				tm.rollback();
			} catch (RollbackException e1) {
				e1.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				e1.printStackTrace();
			}
		}
		return state;
	}

	public boolean batchSaveRoleListRoleresop(String opId, String resType_id,
			String[] checkValues, String role_id, String types) {
		DBUtil add_db = new DBUtil();
		boolean state = false;
		int arrlength = 0;
		if (checkValues != null) {
			arrlength = checkValues.length;
		}
		// 选中项
		String[] resId = new String[arrlength];
		String[] resName = new String[arrlength];
		for (int i = 0; i < arrlength; i++) {
			String checkValue = checkValues[i];
			String[] arr = checkValue.split("#");
			resId[i] = arr[0];
			resName[i] = arr[1];
		}
		String[] roleIds = role_id.split(",");
		// 创建事务
		TransactionManager tm = new TransactionManager();
		PreparedDBUtil preparedDBUtilour = new PreparedDBUtil();
		PreparedDBUtil tempDBUtil = new PreparedDBUtil();
		try {
			tm.begin();

			// 添加选中资源
			// StringBuffer add_sql = new StringBuffer();
			if (resId.length > 0) {
				for (int i = 0; i < roleIds.length; i++) {
					for (int j = 0; j < resId.length; j++) {

						StringBuffer sql1 = new StringBuffer();
						sql1
								.append(sqlUtilInsert
										.getSQL("purviewmanagerimpl_batchSaveRoleListRoleresop"));
						preparedDBUtilour.preparedSelect(sql1.toString());
						preparedDBUtilour.setString(1, opId);
						preparedDBUtilour.setString(2, resId[j]);
						preparedDBUtilour.setString(3, roleIds[i]);
						preparedDBUtilour.setString(4, resType_id);
						preparedDBUtilour.setString(5, types);
						//						
						preparedDBUtilour.executePrepared();
						int count = preparedDBUtilour.getInt(0, 0);
						if (count <= 0) {

							StringBuffer sql2 = new StringBuffer();
							sql2
									.append(sqlUtilInsert
											.getSQL("purviewmanagerimpl_batchSaveRoleListRoleresop2"));
							tempDBUtil.preparedInsert(sql2.toString());
							tempDBUtil.setString(1, opId);
							tempDBUtil.setString(2, resId[j]);
							tempDBUtil.setString(3, roleIds[i]);
							tempDBUtil.setString(4, resType_id);
							tempDBUtil.setString(5, resName[j]);
							tempDBUtil.setString(6, types);
							tempDBUtil.executePrepared();

							sql2.setLength(0);
							sql1.setLength(0);
						}
						// add_sql
						// .append(
						// "insert all when totalsize <= 0 then into ")
						// .append(
						// "td_sm_roleresop(OP_ID,RES_ID,ROLE_ID,RESTYPE_ID,RES_NAME,TYPES) ")
						// .append("values('")
						// .append(opId)
						// .append("','")
						// .append(resId[j])
						// .append("','")
						// .append(roleIds[i])
						// .append("','")
						// .append(resType_id)
						// .append("','")
						// .append(resName[j])
						// .append("','")
						// .append(types)
						// .append(
						// "') select count(op_id) totalsize from td_sm_roleresop where ")
						// .append(" op_id='").append(opId).append("' ")
						// .append(" and res_id='").append(resId[j])
						// .append("' ").append(" and role_id='").append(
						// roleIds[i]).append("' ").append(
						// " and restype_id='").append(resType_id)
						// .append("' ")
						// // .append(" and
						// // res_name='").append(resName[j]).append("' ")
						// .append(" and types='").append(types).append(
						// "' ");
						// add_db.addBatch(add_sql.toString());
						// add_sql.setLength(0);
					}
				}
				// add_db.executeBatch();
			}

			tm.commit();
			// begin log
			if (opId != null && !"".equals(opId)) {
				ResourceManager resourceManager = new ResourceManager();
				StringBuffer opidstr = new StringBuffer();
				if (resourceManager.getOperation(resType_id, opId) != null) {
					opidstr.append(resourceManager.getOperation(resType_id,
							opId).getName());
				}
				LogManager logManager = new LogManagerImpl();

				ResourceInfo resourceInfo = resourceManager
						.getResourceInfoByType(resType_id);
				String resourceName = "";
				if (resourceInfo != null) {
					resourceName = resourceInfo.getName();
				}
				StringBuffer operContent = new StringBuffer();
				if (types.equals("user")) {
					operContent.append("批量为用户(用户ID)");
				} else if (types.equals("role")) {
					operContent.append("批量为角色(角色ID)");
				} else if (types.equals("organization")) {
					operContent.append("批量为机构(机构ID)");
				}
				operContent.append("【").append(role_id).append("】授予").append(
						resourceName).append("中的资源操作项【").append(opidstr)
						.append("】");
				logManager.log(operContent.toString(), "权限管理");
			}
			state = true;
			Event event = new EventImpl("",
					ACLEventType.RESOURCE_ROLE_INFO_CHANGE);
			super.change(event);

		} catch (SQLException e) {
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} catch (TransactionException e) {
			e.printStackTrace();
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				e1.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				e1.printStackTrace();
			}
		}
		return state;
	}

	public static String buildMessage(List list) {
		String checkMessage = "";
		if (list == null || list.size() == 0) {
			return "";
		}
		for (int i = 0; i < list.size(); i++) {
			if ("".equals(checkMessage)) {
				checkMessage = (String) list.get(i);
			} else {
				checkMessage += "," + (String) list.get(i);
			}
		}
		return checkMessage;
	}

	/**
	 * 老板系统可访问税务机关保存操作~~~特例
	 * 
	 * @param orgIds
	 *            可访问机构ID对应资源ID
	 * @param restypeId
	 *            资源类型ID
	 * @param roleid
	 *            角色ID或用户ID，数组大于1时说明是批量可访问税务机关授予
	 * @param role_type
	 *            设置给资源的类型包括user，role
	 * @param isSave
	 *            保存资源还是删除资源
	 * @return
	 */
	public boolean saveReadOrgmanager(String[] orgIds, String[] roleid,
			String restypeId, String role_type, boolean isSave) {
		boolean state = false;
		DBUtil db = new DBUtil();
		if (roleid == null) {
			return false;
		}
		try {
			if (roleid.length > 1) {// 批量权限授予
				if (isSave) {// 如果为true,可访问税务机关权限勾中
					int count = 0;
					StringBuffer add_sql = new StringBuffer();
					StringBuffer add_sql_1 = new StringBuffer();
					for (int j = 0; roleid != null && j < roleid.length; j++) {
						for (int i = 0; orgIds != null && i < orgIds.length; i++) {

							add_sql_1
									.append(
											"select count(op_id)  from td_sm_roleresop where ")
									.append(" op_id='readorgname' ").append(
											" and res_id='").append(orgIds[i])
									.append("' ").append(" and role_id='")
									.append(roleid[j]).append("' ").append(
											" and restype_id='").append(
											restypeId).append("' ").append(
											" and types='").append(role_type)
									.append("' ");

							System.out.println(add_sql_1.toString());

							HashMap[] s = db.executeSql(add_sql_1.toString());
							HashMap s1 = s[0];
							Collection keys = s1.values();
							Iterator it = keys.iterator();
							Object totalsize = it.next();

							if (Integer.parseInt(totalsize.toString()) <= 0) {
								add_sql
										.append("insert  into ")
										.append(
												"td_sm_roleresop(OP_ID,RES_ID,ROLE_ID,RESTYPE_ID,RES_NAME,TYPES) ")
										.append("values('readorgname','")
										.append(orgIds[i])
										.append("','")
										.append(roleid[j])
										.append("','")
										.append(restypeId)
										.append(
												"',(SELECT remark5 FROM td_sm_organization WHERE org_id = '")
										.append(orgIds[i]).append("'),'")
										.append(role_type).append("')");
								// select count(op_id) totalsize from
								// td_sm_roleresop where ")
								// .append(" op_id='readorgname' ").append(
								// " and res_id='").append(orgIds[i])
								// .append("' ").append(" and role_id='")
								// .append(roleid[j]).append("' ").append(
								// " and restype_id='").append(
								// restypeId).append("' ").append(
								// " and types='").append(role_type)
								// .append("' ");
								System.out.println(add_sql.toString());
								db.addBatch(add_sql.toString());
								add_sql.setLength(0);
								count++;
								if (count > 900) {
									db.executeBatch();
									count = 0;
								}
							}
						}
					}
					db.executeBatch();
					state = true;
				} else {
					state = true;
				}
			} else {
				if (isSave) {// 如果为true,可访问税务机关权限勾中
					StringBuffer add_sql = new StringBuffer();
					int count = 0;
					for (int i = 0; orgIds != null && i < orgIds.length; i++) {
						add_sql
								.append(
										"insert all when totalsize <= 0 then into ")
								.append(
										"td_sm_roleresop(OP_ID,RES_ID,ROLE_ID,RESTYPE_ID,RES_NAME,TYPES) ")
								.append("values('readorgname','")
								.append(orgIds[i])
								.append("','")
								.append(roleid[0])
								.append("','")
								.append(restypeId)
								.append(
										"',(SELECT remark5 FROM td_sm_organization WHERE org_id = '")
								.append(orgIds[i])
								.append("'),'")
								.append(role_type)
								.append(
										"') select count(op_id) totalsize from td_sm_roleresop where ")
								.append(" op_id='readorgname' ").append(
										" and res_id='").append(orgIds[i])
								.append("' ").append(" and role_id='").append(
										roleid[0]).append("' ").append(
										" and restype_id='").append(restypeId)
								.append("' ").append(" and types='").append(
										role_type).append("' ");
						db.addBatch(add_sql.toString());
						add_sql.setLength(0);
						count++;
						if (count > 900) {
							db.executeBatch();
							count = 0;
						}
					}
					db.executeBatch();
					state = true;
				} else {// 否则删除可访问机关权限
					StringBuffer del_sql = new StringBuffer();
					String un_resId = "";
					for (int i = 0; orgIds != null && i < orgIds.length; i++) {
						if ("".equals(un_resId)) {
							un_resId = "'" + orgIds[i] + "'";
						} else {
							un_resId += ",'" + orgIds[i] + "'";
						}
					}
					if (!"".equals(un_resId)) {
						del_sql
								.append(
										"delete from td_sm_roleresop where role_id='")
								.append(roleid[0])
								.append("' ")
								.append(
										"and op_id='readorgname' and restype_id='")
								.append(restypeId).append("' and types='")
								.append(role_type).append("' and res_id in(")
								.append(un_resId).append(")");
						db.executeDelete(del_sql.toString());
						state = true;
					}
				}
			}
			Event event = new EventImpl("",
					ACLEventType.RESOURCE_ROLE_INFO_CHANGE);
			super.change(event);
		} catch (SQLException e) {
			db.resetBatch();
			e.printStackTrace();
		} catch (Exception e) {
			db.resetBatch();
			e.printStackTrace();
		}
		return state;
	}

	public boolean saveSelfDefineRoleresop(String[] opids, String[] roleIds,
			String[] un_opids, String[] un_roleIds, String[] resIds,
			String types, String restypeId, String[] resName, boolean isBatch) {
		TransactionManager tm = new TransactionManager();
		DBUtil dbInsert = new DBUtil();
		DBUtil dbDelete = new DBUtil();
		
		String selectSql = new String();
		String insertSql = new String();
		StringBuffer deleteSql = new StringBuffer();
		
		selectSql = sqlUtilInsert.getSQL("purviewmanagerimpl_saveSelfDefineRoleresop_1");
		insertSql = sqlUtilInsert.getSQL("purviewmanagerimpl_saveSelfDefineRoleresop_2");
		
		PreparedDBUtil pe = new PreparedDBUtil();
//		
//		int insertCount = 0;
		int deleteCount = 0;
		try {
			tm.begin();
			for (int i = 0; i < resIds.length; i++) {
				// 添加自定义权限
				for (int j = 0; opids != null && j < opids.length; j++) {
					
					pe.preparedSelect(selectSql);
					pe.setString(1, opids[j]);
					pe.setString(2, resIds[i]);
					pe.setString(3, roleIds[j]);
					pe.setString(4, types);
					pe.setString(5, restypeId);
					pe.executePrepared();
					
					if(pe.getInt(0, 0)<=0)
					{
						pe.preparedInsert(insertSql);
						pe.setString(1, opids[j]);
						pe.setString(2, resIds[i]);
						pe.setString(3,roleIds[j]);
						pe.setString(4, restypeId);
						pe.setString(5, resName[i]);
						pe.setString(6, types);
						pe.executePrepared();
						
					}
					
//					insertSql
//							.append(
//									"insert all when totalsize <= 0 then into td_sm_roleresop(OP_ID,RES_ID,ROLE_ID,RESTYPE_ID,RES_NAME,TYPES,auto)")
//							.append(" values('")
//							.append(opids[j])
//							.append("',")
//							.append("'")
//							.append(resIds[i])
//							.append("',")
//							.append("'")
//							.append(roleIds[j])
//							.append("',")
//							.append("'")
//							.append(restypeId)
//							.append("',")
//							.append("'")
//							.append(resName[i])
//							.append("',")
//							.append("'")
//							.append(types)
//							.append("','1') ")
//							.append(
//									" select count(OP_ID) totalsize from td_sm_roleresop where OP_ID='")
//							.append(opids[j]).append("' and RES_ID='").append(
//									resIds[i]).append("' and ROLE_ID='")
//							.append(roleIds[j]).append("' and TYPES='").append(
//									types).append("' and RESTYPE_ID='").append(
//									restypeId).append("' and auto='1' ");
//					dbInsert.addBatch(insertSql.toString());
//					// System.out.println("insert_sql = " +
//					// insertSql.toString());
//					insertSql.setLength(0);
//					insertCount++;
//					if (insertCount > 900) {
//						dbInsert.executeBatch();
//						insertCount = 0;
//					}
				}
				// 删除自定义权限
				if (!isBatch) {
					for (int z = 0; un_opids != null && z < un_opids.length; z++) {
						deleteSql.append(
								"delete from td_sm_roleresop where OP_ID='")
								.append(un_opids[z]).append("' and RES_ID='")
								.append(resIds[i]).append("' and ROLE_ID='")
								.append(un_roleIds[z]).append("' and TYPES='")
								.append(types).append("' and RESTYPE_ID='")
								.append(restypeId).append("' and auto='1' ");
						dbDelete.addBatch(deleteSql.toString());
						deleteSql.setLength(0);
						deleteCount++;
						if (deleteCount > 900) {
							dbDelete.executeBatch();
							deleteCount = 0;
						}
					}
				}
			}
			
			if (!isBatch && deleteCount > 0) {
				dbDelete.executeBatch();
			}
			tm.commit();
			Event event = new EventImpl("",
					ACLEventType.RESOURCE_ROLE_INFO_CHANGE);
			super.change(event);
			return true;
		} catch (TransactionException e) {
			e.printStackTrace();
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				e1.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				e1.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				e1.printStackTrace();
			}
		}

		return false;
	}

	public boolean saveSelfDefineBatchOrgRoleresop(String[] opids,
			String orgId, String[] un_opids, String[] resIds, String types,
			String restypeId, String[] resName, boolean isRecursion) {
		return saveSelfDefineOrgRoleresop(opids, orgId, un_opids, resIds,
				types, restypeId, resName, isRecursion, true);
	}

	public boolean saveSelfDefineOrgRoleresop(String[] opids, String orgId,
			String[] un_opids, String[] resIds, String types, String restypeId,
			String[] resName, boolean isRecursion) {
		return saveSelfDefineOrgRoleresop(opids, orgId, un_opids, resIds,
				types, restypeId, resName, isRecursion, false);
	}

	private boolean saveSelfDefineOrgRoleresop(String[] opids, String orgId,
			String[] un_opids, String[] resIds, String types, String restypeId,
			String[] resNames, boolean isRecursion, boolean isBatch) {
		TransactionManager tm = new TransactionManager();
		PreparedDBUtil dbInsert = new PreparedDBUtil();
		PreparedDBUtil dbDelete = new PreparedDBUtil();
		String insertSql = new String();
		String deleteSql = new String();
//		int insertCount = 0;
//		int deleteCount = 0;
//		String sunorg_sql = "select org_id from td_sm_organization start with org_id='"
//				+ orgId + "' connect by prior org_id=parent_id";
		String concat_ = DBUtil.getDBAdapter().concat(" org_tree_level","'|%' ");
		String sunorg_sql = "select t.org_id from TD_SM_ORGANIZATION t where t.org_id='"+orgId+"' or "+ "t.org_tree_level like"
			+"(select " + concat_ + " from TD_SM_ORGANIZATION c  where c.org_id ='" 
			+orgId+"')";
		
//		System.out.println("sunorg_sql:"+sunorg_sql);
		
		String sql_count = sqlUtilInsert.getSQL("purviewmanagerimpl_saveSelfDefineOrgRoleresop");
		
//		System.out.println("sql_count:"+sql_count);
		
		PreparedDBUtil pe = new PreparedDBUtil();
		
		insertSql = sqlUtilInsert.getSQL("purviewmanagerimpl_saveSelfDefineOrgRoleresop_");
		deleteSql = sqlUtilInsert.getSQL("purviewmanagerimpl_saveSelfDefineOrgRoleresop_del");
					
		try {
			tm.begin();
			
			
			for (int i = 0; i < resIds.length; i++) {
				// 添加自定义权限
				for (int j = 0; opids != null && j < opids.length; j++) {
					if (isRecursion) {// 如果是递归授予子机构
						DBUtil sunorg_db = new DBUtil();
						sunorg_db.executeSelect(sunorg_sql);
						for (int orgCount = 0; orgCount < sunorg_db.size(); orgCount++) {
							String orgid = sunorg_db.getString(orgCount,"org_id");
							
							pe.preparedSelect(sql_count);
							pe.setString(1, opids[j]);
							pe.setString(2, resIds[i]);
							pe.setString(3, orgid);
							pe.setString(4, types);
							pe.setString(5, restypeId);
							pe.executePrepared();
							
							if(pe.getInt(0, 0)<=0)
							{
								dbInsert.preparedInsert(insertSql);
								dbInsert.setString(1, opids[j]);
								dbInsert.setString(2, resIds[i]);
								dbInsert.setString(3, orgid);
								dbInsert.setString(4, restypeId);
								dbInsert.setString(5, resNames[i]);
								dbInsert.setString(6, types);
								dbInsert.executePrepared();
							}
//							insertSql
//									.append(
//											"insert all when totalsize <= 0 then into td_sm_roleresop(OP_ID,RES_ID,ROLE_ID,RESTYPE_ID,RES_NAME,TYPES,AUTO)")
//									.append(" values('")
//									.append(opids[j])
//									.append("',")
//									.append("'")
//									.append(resIds[i])
//									.append("',")
//									.append("'")
//									.append(orgid)
//									.append("',")
//									.append("'")
//									.append(restypeId)
//									.append("',")
//									.append("'")
//									.append(resNames[i])
//									.append("',")
//									.append("'")
//									.append(types)
//									.append("','1') ")
//									.append(
//											" select count(OP_ID) totalsize from td_sm_roleresop where OP_ID='")
//									.append(opids[j]).append("' and RES_ID='")
//									.append(resIds[i])
//									.append("' and ROLE_ID='").append(orgid)
//									.append("' and TYPES='").append(types)
//									.append("' and RESTYPE_ID='").append(
//											restypeId).append("' and auto='1'");
//							dbInsert.addBatch(insertSql.toString());
//							insertSql.setLength(0);
//							insertCount++;
//							if (insertCount > 900) {
//								dbInsert.executeBatch();
//								insertCount = 0;
//							}
						}
					} else {
						
						pe.preparedSelect(sql_count);
						pe.setString(1, opids[j]);
						pe.setString(2, resIds[i]);
						pe.setString(3, orgId);
						pe.setString(4, types);
						pe.setString(5, restypeId);
						pe.executePrepared();
						
						if(pe.getInt(0, 0)<=0)
						{
							dbInsert.preparedInsert(insertSql);
							dbInsert.setString(1, opids[j]);
							dbInsert.setString(2, resIds[i]);
							dbInsert.setString(3, orgId);
							dbInsert.setString(4, restypeId);
							dbInsert.setString(5, resNames[i]);
							dbInsert.setString(6, types);
							dbInsert.executePrepared();
						}
//						insertSql
//								.append(
//										"insert all when totalsize <= 0 then into td_sm_roleresop(OP_ID,RES_ID,ROLE_ID,RESTYPE_ID,RES_NAME,TYPES,AUTO)")
//								.append(" values('")
//								.append(opids[j])
//								.append("',")
//								.append("'")
//								.append(resIds[i])
//								.append("',")
//								.append("'")
//								.append(orgId)
//								.append("',")
//								.append("'")
//								.append(restypeId)
//								.append("',")
//								.append("'")
//								.append(resNames[i])
//								.append("',")
//								.append("'")
//								.append(types)
//								.append("','1') ")
//								.append(
//										" select count(OP_ID) totalsize from td_sm_roleresop where OP_ID='")
//								.append(opids[j]).append("' and RES_ID='")
//								.append(resIds[i]).append("' and ROLE_ID='")
//								.append(orgId).append("' and TYPES='").append(
//										types).append("' and RESTYPE_ID='")
//								.append(restypeId).append("' and AUTO = '1'");
//						dbInsert.addBatch(insertSql.toString());
//						insertSql.setLength(0);
//						insertCount++;
//						if (insertCount > 900) {
//							dbInsert.executeBatch();
//							insertCount = 0;
//						}
					}
					// System.out.println("insert_sql = " +
					// insertSql.toString());

				}
				// 删除自定义权限
				if (!isBatch) {
					for (int z = 0; un_opids != null && z < un_opids.length; z++) {
						if (isRecursion) {
							DBUtil sunorg_db = new DBUtil();
							sunorg_db.executeSelect(sunorg_sql);
							for (int orgCount = 0; orgCount < sunorg_db.size(); orgCount++) {
								String orgid = sunorg_db.getString(orgCount,"org_id");
								
								dbDelete.preparedDelete(deleteSql);
								dbDelete.setString(1, un_opids[z]);
								dbDelete.setString(2, resIds[i]);
								dbDelete.setString(3, orgid);
								dbDelete.setString(4, types);
								dbDelete.setString(5, restypeId);
								dbDelete.executePrepared();
//								deleteSql
//										.append(
//												"delete from td_sm_roleresop where OP_ID='")
//										.append(un_opids[z]).append(
//												"' and RES_ID='").append(
//												resIds[i]).append(
//												"' and ROLE_ID='")
//										.append(orgid).append("' and TYPES='")
//										.append(types).append(
//												"' and RESTYPE_ID='").append(
//												restypeId).append(
//												"' and AUTO='1'");
//								dbDelete.addBatch(deleteSql.toString());
//								deleteSql.setLength(0);
//								deleteCount++;
//								if (deleteCount > 900) {
//									dbDelete.executeBatch();
//									deleteCount = 0;
//								}
							}
						} else {
							
							dbDelete.preparedDelete(deleteSql);
							dbDelete.setString(1, un_opids[z]);
							dbDelete.setString(2, resIds[i]);
							dbDelete.setString(3, orgId);
							dbDelete.setString(4, types);
							dbDelete.setString(5, restypeId);
							dbDelete.executePrepared();
							
//							deleteSql
//									.append(
//											"delete from td_sm_roleresop where OP_ID='")
//									.append(un_opids[z]).append(
//											"' and RES_ID='").append(resIds[i])
//									.append("' and ROLE_ID='").append(orgId)
//									.append("' and TYPES='").append(types)
//									.append("' and RESTYPE_ID='").append(
//											restypeId).append(
//											"' and AUTO = '1' ");
//							dbDelete.addBatch(deleteSql.toString());
//							deleteSql.setLength(0);
//							deleteCount++;
//							if (deleteCount > 900) {
//								dbDelete.executeBatch();
//								deleteCount = 0;
//							}
						}
					}
				}
			}
//			if (insertCount > 0) {
//				dbInsert.executeBatch();
//			}
//			if (!isBatch && deleteCount > 0) {
//				dbDelete.executeBatch();
//			}
			tm.commit();

			Event event = new EventImpl("",
					ACLEventType.RESOURCE_ROLE_INFO_CHANGE);
			super.change(event);
			return true;
		} catch (TransactionException e) {
			e.printStackTrace();
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				e1.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				e1.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				e1.printStackTrace();
			}
		}

		return false;
	}

}
