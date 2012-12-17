package com.frameworkset.platform.sysmgrcore.manager.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.RollbackException;

import org.frameworkset.event.Event;
import org.frameworkset.event.EventHandle;
import org.frameworkset.event.EventImpl;
import org.frameworkset.persitent.util.SQLUtil;

import com.frameworkset.platform.security.event.ACLEventType;
import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.platform.sysmgrcore.entity.User;
import com.frameworkset.platform.sysmgrcore.entity.UserJobs;
import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.platform.sysmgrcore.manager.OrgAdministrator;
import com.frameworkset.platform.sysmgrcore.manager.UserManager;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.common.poolman.Record;
import com.frameworkset.common.poolman.SQLExecutor;
import com.frameworkset.common.poolman.handle.NullRowHandler;
import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.util.ListInfo;

/**
 * 机构管理员设置管理类
 * <p>
 * Title: OrgAdministratorImpl
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
 * @Date 2007-11-6 17:12:34
 * @author biaoping.yin
 * @version 1.0
 */

public class OrgAdministratorImpl extends EventHandle implements
		OrgAdministrator {

	
	public static SQLUtil sqlUtilInsert = SQLUtil.getInstance("org/frameworkset/insert.xml");
	/**
	 * 获取机构管理员列表，包含机构的本级管理员的集合
	 */
	public List getAdministorsOfOrg(String orgID) {
//		List list = new ArrayList();

//		String sql = "select u.* from td_sm_user u inner join td_sm_orgmanager org on u.user_id = org.user_id and org.org_id=?";
//		DBUtil dBUtil = new DBUtil();
//		try {
//			dBUtil.executeSelect(sql);
//			if (dBUtil.size() > 0) {
//				for (int i = 0; i < dBUtil.size(); i++) {
//					User user = new User();
//					UserManager userManager = new UserManagerImpl();
//					user = userManager.getUserById(dBUtil.getString(i,
//							"user_id"));
//					list.add(user);
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

//		return list;
		UserManager userManager = new UserManagerImpl();
		try {
			return userManager.getOrgManager(orgID);
		} catch (ManagerException e) {
			e.printStackTrace();
			return  new ArrayList();
		}

	}

	/**
	 * 获取机构管理员列表，包含机构的上级管理员和本及管理员的集合
	 */
	public List getAllAdministorsOfOrg(String orgID) {
		List list = new ArrayList();
		String sql = "select * from td_sm_orgmanager p where p.org_id in"
				+ "("
				+ "select q.org_id from td_sm_organization q start with q.org_id='"
				+ orgID + "' connect by prior q.parent_id = q.org_id" + ")";
		DBUtil dBUtil = new DBUtil();
		try {
			dBUtil.executeSelect(sql);
			if (dBUtil.size() > 0) {
				for (int i = 0; i < dBUtil.size(); i++) {
					User user = new User();
					UserManager userManager = new UserManagerImpl();
					user = userManager.getUserById(dBUtil.getString(i,
							"user_id"));
					list.add(user);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * 获取机构管理员列表，包含机构的上级管理员和本及管理员的集合 用于机构管理显示
	 */
	public ListInfo getAdministorsOfOrg(String orgID, int offset, int maxItem) {
		ListInfo listInfo = new ListInfo();
		DBUtil dbUtil = new DBUtil();
		DBUtil db = new DBUtil();
		// 用户所属的org job
		String sql = "select u.* from td_sm_user u, td_sm_orgmanager p "
				+ "where p.org_id='" + orgID + "' and u.user_id=p.user_id";
		try {
			dbUtil.executeSelect(sql, offset, maxItem);
			List list = new ArrayList();
			UserJobs uj;
			for (int i = 0; i < dbUtil.size(); i++) {
				String orgjob = "";
				String str = "select getuserorgjobinfos('"
						+ dbUtil.getInt(i, "user_id") + "') as org from dual";
				db.executeSelect(str);
				if (db.size() > 0) {
					orgjob = db.getString(0, "org");
					if (orgjob.endsWith("、")) {
						orgjob = orgjob.substring(0, orgjob.length() - 1);
					}
				}
				uj = new UserJobs();
				uj.setUserId(new Integer(dbUtil.getInt(i, "user_id")));
				uj.setUserName(dbUtil.getString(i, "USER_NAME"));
				uj.setUserRealname(dbUtil.getString(i, "USER_REALNAME"));
				uj.setUserMobiletel1(dbUtil.getString(i, "USER_MOBILETEL1"));
				uj.setUserType(dbUtil.getString(i, "USER_TYPE"));
				uj.setUserEmail(dbUtil.getString(i, "USER_EMAIL"));
				uj.setUserSex(dbUtil.getString(i, "USER_SEX "));
				uj.setUser_isvalid(dbUtil.getString(i, "USER_ISVALID"));
				uj.setUser_regdate(dbUtil.getString(i, "USER_REGDATE"));
				uj.setDredge_time(dbUtil.getString(i, "DREDGE_TIME"));
				uj.setOrgName(orgjob);
				uj.setJobName(orgjob);
				uj.setOrg_Name(orgjob);
				list.add(uj);
			}
			listInfo.setDatas(list);
			listInfo.setTotalSize(dbUtil.getTotalSize());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listInfo;
	}

	/**
	 * 给机构添加一个部门管理员 首先要确保用户已有部门管理员角色
	 * */
	public boolean addOrgAdmin(String userId, String orgId) {

		/*
		 * 确保给用户赋予了部门管理员角色和部门管理员模板角色，部门管理员角色的id是3，部门管理员模板角色是4
		 */
		String sql0 = "select e.* from td_sm_userrole e where e.user_id='"
				+ userId + "' and e.role_id='" + 3 + "'";
		DBUtil dBUtil0 = new DBUtil();
		try {
			dBUtil0.executeSelect(sql0);
			if (dBUtil0.size() == 0) {
				sql0 = "insert into td_sm_userrole(user_id,role_id) values('"
						+ userId + "', '" + 3 + "')";
				dBUtil0.addBatch(sql0);
			}
		} catch (Exception e0) {
			e0.printStackTrace();
			return false;
		}

		/*
		 * 确保给用户赋予了部门管理员模板角色，部门管理员模板角色是4
		 */
		String sql1 = "select e.* from td_sm_userrole e where e.user_id='"
				+ userId + "' and e.role_id='" + 4 + "'";
		DBUtil dBUtil1 = new DBUtil();
		try {
			dBUtil1.executeSelect(sql1);
			if (dBUtil1.size() == 0) {
				sql1 = "insert into td_sm_userrole(user_id,role_id) values('"
						+ userId + "', '" + 4 + "')";
				dBUtil0.addBatch(sql1);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			return false;
		}

		/*
		 * 对机构的部门管理员表增加一条记录，避免重复添加
		 */
		String sql = "select e.* from td_sm_orgmanager e where e.user_id='"
				+ userId + "' and e.org_id='" + orgId + "'";
		DBUtil dBUtil = new DBUtil();
		try {
			dBUtil.executeSelect(sql);
			if (dBUtil.size() == 0) {
				sql = "insert into td_sm_orgmanager(user_id,org_id) values('"
						+ userId + "', '" + orgId + "')";
				dBUtil0.addBatch(sql);

				dBUtil0.executeBatch();

				Event event = new EventImpl(userId + "/" + orgId,
						ACLEventType.ORGUNIT_MANAGER_ADD);
				super.change(event, true);

				event = new EventImpl(userId + "/" + orgId,
						ACLEventType.USER_ROLE_INFO_CHANGE);
				super.change(event, true);
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 给机构添加一个部门管理员 首先要确保用户已有部门管理员角色
	 * */
	public boolean addOrgAdmin(String userIds[], String orgId, String curUserId) {

		if (userIds == null || userIds.length == 0 || orgId == null
				|| orgId.equals(""))
			return false;
		/*
		 * 确保给用户赋予了部门管理员角色和部门管理员模板角色，部门管理员角色的id是3，部门管理员模板角色的id是4
		 */

//		DBUtil dBUtil0 = new DBUtil();
//		PreparedDBUtil prepareddbutil1 = new PreparedDBUtil();
//		PreparedDBUtil prepareddbutil3 = new PreparedDBUtil();
//		PreparedDBUtil prepareddbutil5 = new PreparedDBUtil();
		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();
			/*
			 * 对机构的部门管理员表增加一条记录，避免重复添加
			 */
			for (int i = 0; i < userIds.length; i++) {
				// 删除要设置的部门管理员关联岗位，保留在职
				// String delJobSql =
				// "delete td_sm_userjoborg a where a.user_id='"+userIds[i]+"' and org_id='"+orgId+"' and job_id <> '1'";
				// dBUtil0.addBatch(delJobSql);

//				String sql = "insert all when totalsize <= 0 then into td_sm_orgmanager "
//						+ "values('"
//						+ userIds[i]
//						+ "', '"
//						+ orgId
//						+ "') "
//						+ "select count(user_id) as totalsize from td_sm_orgmanager e where e.user_id='"
//						+ userIds[i] + "' and e.org_id='" + orgId + "'";
//				dBUtil0.addBatch(sql);
				String sql1 = sqlUtilInsert.getSQL("orgadministratorimpl_addOrgAdmin");
				int hasset = SQLExecutor.queryObject(int.class, sql1, userIds[i],orgId);
//				prepareddbutil1.preparedSelect(sql1);
//				prepareddbutil1.setString(1, userIds[i]);
//				prepareddbutil1.setString(2,orgId);
//				prepareddbutil1.executePrepared();
				if(hasset<=0)
				{
					String sql2 = sqlUtilInsert.getSQL("orgadministratorimpl_addOrgAdmin2");
					SQLExecutor.insert(sql2, userIds[i],orgId);
//					PreparedDBUtil prepareddbutil2 = new PreparedDBUtil();
//					prepareddbutil2.preparedInsert(sql2);
//					prepareddbutil2.setString(1, userIds[i]);
//					prepareddbutil2.setString(2, orgId);
//					prepareddbutil2.executePrepared();
				}

//				String sql0 = "insert all when totalsize <= 0 then into td_sm_userrole(user_id,role_id,RESOP_ORIGIN_USERID) values('"
//						+ userIds[i]
//						+ "', '"
//						+ 3
//						+ "','"
//						+ curUserId
//						+ "') select count(user_id) as totalsize from td_sm_userrole e where e.user_id='"
//						+ userIds[i] + "' and e.role_id='" + 3 + "'";
//				dBUtil0.addBatch(sql0);
				
				String sql3 = sqlUtilInsert.getSQL("orgadministratorimpl_addOrgAdmin3");
				hasset = SQLExecutor.queryObject(int.class, sql3, userIds[i]);
//				prepareddbutil3.preparedSelect(sql3);
//				prepareddbutil3.setString(1, userIds[i]);
//				prepareddbutil3.executePrepared();
				if(hasset<=0)
				{
					String sql4 = sqlUtilInsert.getSQL("orgadministratorimpl_addOrgAdmin4");
					SQLExecutor.insert(sql4, userIds[i],curUserId);
//					PreparedDBUtil prepareddbutil4 = new PreparedDBUtil();
//					prepareddbutil4.preparedInsert(sql4);
//					prepareddbutil4.setString(1, userIds[i]);
//					prepareddbutil4.setString(2, curUserId);
//					prepareddbutil4.executePrepared();
				}

//				String sql1 = "insert all when totalsize <= 0 then into td_sm_userrole(user_id,role_id,RESOP_ORIGIN_USERID) values('"
//						+ userIds[i]
//						+ "', '"
//						+ 4
//						+ "','"
//						+ curUserId
//						+ "') select count(user_id) as totalsize from td_sm_userrole e where e.user_id='"
//						+ userIds[i] + "' and e.role_id='" + 4 + "'";
//				dBUtil0.addBatch(sql1);
				
				String sql5 = sqlUtilInsert.getSQL("orgadministratorimpl_addOrgAdmin5");
				hasset = SQLExecutor.queryObject(int.class, sql5, userIds[i]);
//				prepareddbutil5.preparedSelect(sql5);
//				prepareddbutil5.setString(1, userIds[i]);
//				prepareddbutil5.executePrepared();
				if(hasset<=0)
				{
					String sql6 =sqlUtilInsert.getSQL("orgadministratorimpl_addOrgAdmin6");
					SQLExecutor.insert(sql6, userIds[i],curUserId);
//					PreparedDBUtil prepareddbutil6 = new PreparedDBUtil();
//					prepareddbutil6.preparedInsert(sql6);
//					prepareddbutil6.setString(1, userIds[i]);
//					prepareddbutil6.setString(2, curUserId);
//					prepareddbutil6.executePrepared();
				}
			}
//			dBUtil0.executeBatch();
			tm.commit();
			Event event = new EventImpl(userIds + "/" + orgId,
					ACLEventType.ORGUNIT_MANAGER_ADD);
			super.change(event, true);

			event = new EventImpl(userIds + "/" + orgId,
					ACLEventType.USER_ROLE_INFO_CHANGE);
			super.change(event, true);
			return true;

		} catch (Throwable e) {
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 在部门管理员用户和机构的映射表td_sm_orgmanager中删除记录 如果用户已不是人和机构的部门管理员，则要删除该用户的部门管理员角色
	 * */
	public boolean deleteOrgAdmin(String userId, String orgId) {

		TransactionManager tm = new TransactionManager();
		/*
		 * 对机构的部门管理员表删除一条记录
		 */
		String sql = "delete from td_sm_orgmanager f where f.user_id='"
				+ userId + "' and f.org_id='" + orgId + "'";
		DBUtil dBUtil = new DBUtil();

		/*
		 * 如果用户不是任何机构的部门管理员，则去掉用户的部门管理员角色和部门管理员模板角色
		 */
		String sql0 = "select * from td_sm_orgmanager t where t.user_id='"
				+ userId + "'";
		DBUtil dBUtil0 = new DBUtil();
		DBUtil dBUtil1 = new DBUtil();
		try {
			tm.begin();
			dBUtil.executeDelete(sql);
			dBUtil0.executeSelect(sql0);
			if (dBUtil0.size() == 0) {
				String sql1 = "delete from td_sm_userrole t where t.user_id='"
						+ userId + "' and t.role_id='3'";
				dBUtil1.addBatch(sql1);

				String sql2 = "delete from td_sm_userrole t where t.user_id='"
						+ userId + "' and t.role_id='4'";
				dBUtil1.addBatch(sql2);
				dBUtil1.executeBatch();

			}
			tm.commit();
			if (dBUtil0.size() == 0) {
				Event event = new EventImpl(userId + "/" + orgId,
						ACLEventType.ORGUNIT_MANAGER_DELETE);
				super.change(event, true);

				event = new EventImpl(userId + "/" + orgId,
						ACLEventType.USER_ROLE_INFO_CHANGE);
				super.change(event, true);
				return true;
			}
		} catch (Exception e0) {
			try {
				tm.rollback();
			} catch (RollbackException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			e0.printStackTrace();
		}

		return false;
	}

	/**
	 * 在部门管理员用户和机构的映射表td_sm_orgmanager中删除记录 如果用户已不是人和机构的部门管理员，则要删除该用户的部门管理员角色
	 * */
	public boolean deleteOrgAdmin(String userIds[], String orgId) {
		if (userIds == null || userIds.length == 0 || orgId == null
				|| orgId.equals(""))
			return false;
		DBUtil dBUtil = new DBUtil();
		DBUtil dBUtil0 = new DBUtil();
		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();
			for (int i = 0; i < userIds.length; i++) {
				String sql = "delete from td_sm_orgmanager where user_id='"
						+ userIds[i] + "' and org_id='" + orgId + "'";
				dBUtil0.executeDelete(sql);

				String sql0 = "select * from td_sm_orgmanager where user_id='"
						+ userIds[i] + "'";
				dBUtil0.executeSelect(sql0);
				if (dBUtil0.size() == 0) {
					String sql1 = "delete from td_sm_userrole  where user_id='"
							+ userIds[i] + "' and role_id='3'";
					dBUtil.addBatch(sql1);

					String sql2 = "delete from td_sm_userrole  where user_id='"
							+ userIds[i] + "' and role_id='4'";
					dBUtil.addBatch(sql2);
				}
			}
			dBUtil.executeBatch();
			tm.commit();

			Event event = new EventImpl(userIds + "/" + orgId,
					ACLEventType.ORGUNIT_MANAGER_DELETE);
			super.change(event, true);

			event = new EventImpl(userIds + "/" + orgId,
					ACLEventType.USER_ROLE_INFO_CHANGE);
			super.change(event, true);

			return true;
		} catch (Exception e) {
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 判断用户是否是机构的部门管理员 当orgId==""时,判断用户是否机构管理员
	 * */
	public boolean isOrgAdmin(String userId, String orgId) {
		if (userId == null || userId.trim().length() == 0) {
			return false;
		}
		String sql = "select * from td_sm_orgmanager t " + "where t.user_id='"
				+ userId + "' ";
		if (!"".equalsIgnoreCase(orgId.trim())) {
			sql += " and t.org_id='" + orgId + "'";
		}

		DBUtil dBUtil = new DBUtil();
		try {
			dBUtil.executeSelect(sql);
			if (dBUtil.size() > 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public static String getParentOrgLevels(String orgId) throws SQLException
	{
		String sql_ = "select org_tree_level from td_sm_organization where org_id = ?";
		PreparedDBUtil prepareddbutil_ = new PreparedDBUtil();
		prepareddbutil_.preparedSelect(sql_);
		prepareddbutil_.setString(1, orgId);
		prepareddbutil_.executePrepared();
		String org_tree_level = prepareddbutil_.getString(0,
				"org_tree_level");
		StringBuffer reals = new StringBuffer();
		String[] levels = org_tree_level.split("\\|");
		if (levels.length == 2) {
			reals.append("'").append(org_tree_level).append("'");
		} else {

			StringBuffer temps = new StringBuffer();

			for (int i = 1; i < levels.length; i++) {
				if (i == 1) {
					temps.append("0|").append(levels[i]);
					reals.append("'").append(temps).append("'");
				} else {
					temps.append("|").append(levels[i]);
					reals.append(",'").append(temps).append("'");
				}

			}

		}
		return reals.toString();
	
	}
	
	public static void main(String[] args) throws SQLException
	{
		System.out.println(getParentOrgLevels("12"));
		System.out.println(getParentOrgLevels("6"));
	}

	/**
	 * 判断用户能否管理机构： 1.用户是当前机构的管理员 2.用户是当前机构的向上路径机构的管理员
	 * */
	public boolean userAdminOrg(String userId, String orgId) {
		if (userId == null || userId.trim().length() == 0) {
			return false;
		}
		// String sql = "select * from td_sm_orgmanager t "
		// +"where t.user_id = '" + userId + "' "
		// +"and "
		// +"t.org_id in "
		// +"( select o.org_id from td_sm_organization o "
		// +"start with o.org_id='" + orgId +
		// "' connect by prior o.parent_id = o.org_id )";
		// System.out.println(sql);
		// String sql =
		// SQLUtil.getInstance("org/frameworkset/insert.xml").getSQL("orgAdministratorImpl_userAdminOrg");
		try {
			
			String sql = " select * from td_sm_orgmanager t "
					+ " where t.user_id = '" + userId + "' " + " and "
					+ "t.org_id in "
					+ "( select o.org_id from td_sm_organization o "
					+ " where o.org_tree_level in (" + getParentOrgLevels(orgId) + "))";

			PreparedDBUtil prepareddbutil = new PreparedDBUtil();
			prepareddbutil.preparedSelect(sql);
			prepareddbutil.setString(1, userId);
			prepareddbutil.executeSelect(sql);
			return prepareddbutil.size() > 0;
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return false;
	}

	/**
	 * 通过用户id获取用户能直接管理的机构列表
	 * */
	public List getManagerOrgsOfUserByID(String userId) {
		List list = new ArrayList();
		String sql = "select org_id from td_sm_orgmanager t where t.user_id = ?";
		
		
		try {
			List<String> orgids = SQLExecutor.queryList(String.class, sql, userId);
			if (orgids.size() > 0) {
				for (int i = 0; i < orgids.size(); i++) {

					Organization organization = OrgCacheManager.getInstance()
							.getOrganization(orgids.get(i));
					if (organization != null) {
						list.add(organization);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		return list;
	}

	/**
	 * 通过用户id递归获取用户能管理的机构列表
	 * */
	public ListInfo getAllManagerOrgsOfUserByID(String userId, String orgName,
			String orgnumber, long offset, int maxPagesize)
			throws ManagerException {
		List list = new ArrayList();
		ListInfo listInfo = new ListInfo();
		/*StringBuffer all_orgs = new StringBuffer()
				.append("select * from td_sm_organization where org_id in (")
				.append(
						"select distinct org.org_id from td_sm_organization org start with org.org_id in(")
				.append(
						"select o.org_id from td_sm_organization o, td_sm_orgmanager om where ")
				.append("o.org_id = om.org_id and om.user_id='").append(userId)
				.append("') ").append(
						" connect by prior org.org_id = org.parent_id) ");*/
		String concat_ = DBUtil.getDBAdapter().concat(" org_tree_level","'|%' ");
		StringBuffer all_orgs = new StringBuffer()
		           .append(" select * from td_sm_organization where org_id in (select distinct t.org_id")
		           .append(" from td_sm_organization t where t.org_tree_level like  (select ").append(concat_).append(" from")
		           .append(" TD_SM_ORGANIZATION c,td_sm_orgmanager om  where c.org_id = om.org_id and om.user_id = '")
		           .append(userId).append("') or t.org_id in (select e.org_id from TD_SM_ORGANIZATION e,td_sm_orgmanager ow where e.org_id=ow.org_id and ow.user_id = '")
		           .append(userId).append("')")
		           .append(")");
//		System.out.println(all_orgs.toString());

		if (orgName != null && !orgName.equals("")) {
			all_orgs.append("and remark5 like '%").append(orgName)
					.append("%' ");
		}
		if (orgnumber != null && !orgnumber.equals("")) {
			all_orgs.append("and ORGNUMBER like '%").append(orgnumber).append(
					"%' ");
		}
//    System.out.println("all_orgs.toString()"+all_orgs.toString());
		DBUtil dBUtil = new DBUtil();
		try {
			dBUtil.executeSelect(all_orgs.toString(), offset, maxPagesize);
			if (dBUtil.size() > 0) {
				for (int i = 0; i < dBUtil.size(); i++) {

					Organization organization = OrgCacheManager.getInstance()
							.getOrganization(dBUtil.getString(i, "org_id"));
					if (organization != null) {
						list.add(organization);
					}
				}
			}
			listInfo.setDatas(list);
			listInfo.setTotalSize(dBUtil.getTotalSize());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listInfo;
	}

	/**
	 * 通过用户帐号（登录名称）获取用户能直接管理的机构列表
	 * */
	public List getManagerOrgsOfUserByAccount(String userAccount) {
		List list = new ArrayList();
		String sql = "select * from td_sm_orgmanager t where t.user_id in"
				+ "(select o.user_id from td_sm_user o where o.user_name='"
				+ userAccount + "')";
		DBUtil dBUtil = new DBUtil();
		try {
			dBUtil.executeSelect(sql);
			if (dBUtil.size() > 0) {
				Organization organization = OrgCacheManager.getInstance()
						.getOrganization(dBUtil.getString(0, "org_id"));
				if (organization != null) {
					list.add(organization);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		return list;
	}

	/**
	 * 根据用户id判断该用户是否为部门管理员
	 * 
	 * @param userId
	 * @return
	 */
	public boolean isOrgManager(String userId) {
		boolean state = false;
		String sql = "select count(1) from td_sm_orgmanager where user_id=?";
		
		try {
			int count = SQLExecutor.queryTField(int.class, sql, userId);
			
			if (count > 0) {
				state = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return state;
	}

}
