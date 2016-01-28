package com.frameworkset.platform.sysmgrcore.manager.db;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.RollbackException;

import org.apache.log4j.Logger;
import org.frameworkset.event.Event;
import org.frameworkset.event.EventHandle;
import org.frameworkset.event.EventImpl;
import org.frameworkset.persitent.util.SQLUtil;
import org.frameworkset.spi.SPIException;
import org.frameworkset.util.MoreListInfo;

import com.frameworkset.common.poolman.ConfigSQLExecutor;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.common.poolman.Record;
import com.frameworkset.common.poolman.SQLExecutor;
import com.frameworkset.common.poolman.handle.NullRowHandler;
import com.frameworkset.common.poolman.handle.RowHandler;
import com.frameworkset.orm.annotation.TransactionType;
import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.platform.config.ConfigManager;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.security.authentication.EncrpyPwd;
import com.frameworkset.platform.security.event.ACLEventType;
import com.frameworkset.platform.sysmgrcore.entity.Accredit;
import com.frameworkset.platform.sysmgrcore.entity.Dictdata;
import com.frameworkset.platform.sysmgrcore.entity.Group;
import com.frameworkset.platform.sysmgrcore.entity.Job;
import com.frameworkset.platform.sysmgrcore.entity.Log;
import com.frameworkset.platform.sysmgrcore.entity.Operation;
import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.platform.sysmgrcore.entity.Orgjob;
import com.frameworkset.platform.sysmgrcore.entity.Res;
import com.frameworkset.platform.sysmgrcore.entity.Role;
import com.frameworkset.platform.sysmgrcore.entity.Tempaccredit;
import com.frameworkset.platform.sysmgrcore.entity.User;
import com.frameworkset.platform.sysmgrcore.entity.UserJobs;
import com.frameworkset.platform.sysmgrcore.entity.Userattr;
import com.frameworkset.platform.sysmgrcore.entity.Usergroup;
import com.frameworkset.platform.sysmgrcore.entity.Userjoborg;
import com.frameworkset.platform.sysmgrcore.entity.Userresop;
import com.frameworkset.platform.sysmgrcore.entity.Userrole;
import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.platform.sysmgrcore.manager.LogManager;
import com.frameworkset.platform.sysmgrcore.manager.OrgManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.platform.sysmgrcore.manager.UserManager;
import com.frameworkset.platform.sysmgrcore.purviewmanager.db.FunctionDB;
import com.frameworkset.platform.sysmgrcore.purviewmanager.db.PurviewManagerImpl;
import com.frameworkset.platform.sysmgrcore.purviewmanager.db.UserOrgParamManager;
import com.frameworkset.platform.util.EventUtil;
import com.frameworkset.util.ListInfo;
import com.frameworkset.util.StringUtil;

/**
 * 项目：SysMgrCore <br>
 * 描述：用户管理(DB实现类) <br> 
 * 版本：1.0 <br>
 * 
 * @author 
 */
public class UserManagerImpl extends EventHandle implements UserManager {
	
	private SQLUtil sqlUtil = SQLUtil.getInstance("org/frameworkset/insert.xml");
	private static ConfigSQLExecutor executor = new ConfigSQLExecutor("com/frameworkset/platform/sysmgrcore/manager/db/user.xml"); 

	private UserOrgParamManager userOrgParamManager = new UserOrgParamManager();

	private static Logger logger = Logger.getLogger(UserManagerImpl.class
			.getName());

	public class UserComparator implements Comparator {

		public int compare(Object arg0, Object arg1) {
			Object[] o1 = (Object[]) arg0;
			Object[] o2 = (Object[]) arg1;

			if (o1[1] != null && o2[1] != null){
				return ((Integer) o1[1]).intValue()
						- ((Integer) o2[1]).intValue();
			}else{
				return 0;
			}
		}
	}
	
	public List<User> getOrgManager(String org_id) throws ManagerException
	{
		String sql = "select u.* from td_sm_user u inner join td_sm_orgmanager org on u.user_id = org.user_id and org.org_id=?";
		TransactionManager tm = new TransactionManager(); 
		try {
			tm.begin(TransactionType.RW_TRANSACTION);
			List<User> orgmanagers = SQLExecutor.queryListByRowHandler(new RowHandler<User>(){

				@Override
				public void handleRow(User rowValue, Record record)
						throws Exception {
					getUser( rowValue,record) ;
					String orgjob = FunctionDB.getUserorgjobinfos(rowValue.getUserId() );
					if(orgjob.endsWith("、"))
					{
						orgjob = orgjob.substring(0, orgjob.length() - 1);
					}
					rowValue.setUserRealname(rowValue.getUserRealname() + "[" + orgjob + "]");
					
				}
				
			}, User.class, sql, org_id);
			tm.commit();
			return orgmanagers;
		} catch (Throwable e) {
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			throw new ManagerException(e);
		}
		
	}
	/**
	 * 没有被使用
	 * @param user
	 * @return
	 * @throws ManagerException
	 * @deprecated 不推荐使用该方法保存用户，已经被addUser(User)方法替代
	 */
	public boolean storeUser(User user) throws ManagerException {
		// boolean r = false;
		// if (user != null) {
		// try {
		// // 检查当前用户实例是否来自LDAP，因为LDAP中的用户ID(UID)是采用用户名标识其唯一性
		// if (user.getUserId() != null
		// && user.getUserId().intValue() == -1) {
		// User oldUser = getUser("userName", user.getUserName());
		// if (oldUser == null)
		// user.setUserId(null);
		// else {
		//
		// user.setUserId(oldUser.getUserId());
		// String oldpassword = StringUtil.replaceNull(oldUser
		// .getUserPassword());
		// String newpassword = StringUtil.replaceNull(user
		// .getUserPassword());
		// if (!oldpassword.equals(EncrpyPwd
		// .encodePassword(newpassword)))
		// user.setUserPassword(EncrpyPwd
		// .encodePassword(newpassword));
		//
		// }
		//
		// } else {
		// String password = StringUtil.replaceNull(user
		// .getUserPassword());
		// user.setUserPassword(EncrpyPwd.encodePassword(password));
		// }
		// Parameter p = new Parameter();
		//
		// // 保存
		// p.setCommand(Parameter.COMMAND_STORE);
		// p.setObject(user);
		// cb.execute(p);
		//
		// r = true;
		// Event event = new EventImpl("", ACLEventType.USER_INFO_CHANGE);
		// super.change(event);
		// } catch (ControlException e) {
		// e.printStackTrace();
		// }
		// }
		System.out
				.println("hibernate->使用了UserManagerImpl类下的storeUser(User user)方法。。。");
		return false;
	}

	/**
	 * 没有被使用
	 * @deprecated 不推荐使用该方法，系统中没用使用该方法
	 */
	public boolean storeUser(User user, String propName, String value)
			throws ManagerException {
		boolean r = false;

//		if (user != null) {
//			try {
//				// 检查数据库中是否存在与属性以及属性值相同的记录，有则用 user 对象实例中的值
//				// 更新该记录否则插入新的记录
//				User oldUser = getUser(propName, value);
//				if (oldUser == null)
//					user.setUserId(null);
//				else
//					user.setUserId(oldUser.getUserId());
//
//				Parameter p = new Parameter();
//
//				// 保存
//				p.setCommand(Parameter.COMMAND_STORE);
//				p.setObject(user);
//				cb.execute(p);
//				Event event = new EventImpl("", ACLEventType.USER_INFO_ADD);
//				super.change(event);
//				r = true;
//			} catch (ControlException e) {
//				throw new ManagerException(e.getMessage());
//			}
//		}

		return r;
	}

	public boolean storeLogincount(String userName)
			throws ManagerException {
		boolean r = false;
		DBUtil db = new DBUtil();
		String sql = "update td_sm_user set  USER_LOGINCOUNT=USER_LOGINCOUNT+1"
				+ " where  USER_NAME='" + userName + "'";
		try {
			db.executeUpdate(sql);
			r = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return r;

	}

	/**
	 * 无效的方法
	 * @deprecated 无效的方法，方法实现已经删除
	 */
	public boolean storeUserattr(Userattr userattr) throws ManagerException {
		boolean r = false;

//		 if (userattr != null) {
//		 try {
//		 Parameter p = new Parameter();
		
//		 // 先查找数据库中当前用户是否存在同名的属性，如果有则取该属性的ID
//		 p.setCommand(Parameter.COMMAND_GET);
//		 p.setObject("from Userattr ua where ua.userattrName='"
//		 + userattr.getUserattrName() + "' and ua.user.userId='"
//		 + userattr.getUserId() + "'");
//		 List list = (List) cb.execute(p);
//		 if (list != null && !list.isEmpty()) {
//		 Userattr oldUserattr = (Userattr) list.get(0);
//		 userattr.setUserattrId(oldUserattr.getUserattrId());
//		 }
//		
//		 // 保存
//		 p.setCommand(Parameter.COMMAND_STORE);
//		 p.setObject(userattr);
//		 cb.execute(p);
//		 Event event = new EventImpl("", ACLEventType.USER_INFO_CHANGE);
//		 super.change(event);
//		 r = true;
//		 } catch (ControlException e) {
//		 throw new ManagerException(e.getMessage());
//		 }
//		 }
		System.out.println("hibernate->使用了UserManagerImpl类下的storeUserattr(Userattr userattr)方法。。。");
		return r;
	}

	public boolean storeUserjoborg(Userjoborg userjoborg)
			throws ManagerException {
		boolean r = false;

		if (userjoborg != null) {
			DBUtil dbUtil = new DBUtil();
			try {
//				Parameter p = new Parameter();
//
//				p.setCommand(Parameter.COMMAND_STORE);
//				p.setObject(userjoborg);
//				cb.execute(p);
				StringBuffer sql = new StringBuffer();
				sql.append("insert into TD_SM_USERJOBORG(USER_ID, JOB_ID, ORG_ID, SAME_JOB_USER_SN, JOB_SN, JOB_STARTTIME, JOB_FETTLE) ")
				   .append("values('").append(userjoborg.getUser().getUserId()).append("', '")
				   .append(userjoborg.getJob().getJobId()).append("', '")
				   .append(userjoborg.getOrg().getOrgId()).append("', '")
				   .append(userjoborg.getSameJobUserSn()).append("', '")
				   .append(userjoborg.getJobSn()).append("', ")
				   .append(DBUtil.getDBAdapter().to_date(new Date()))
				   .append(", '").append(userjoborg.getFettle()).append("')");
				dbUtil.executeInsert(sql.toString());
				Event event = new EventImpl(String.valueOf(userjoborg.getUser().getUserId()),
						ACLEventType.USER_ROLE_INFO_CHANGE);
				super.change(event);
				r = true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return r;
	}

	/**
	 * 去掉hibernate后的方法
	 */
	public boolean storeUserrole(Userrole userrole) throws ManagerException {
		boolean r = false;

//		if (userrole != null) {
//			try {
//				Parameter p = new Parameter();
//
//				p.setCommand(Parameter.COMMAND_STORE);
//				p.setObject(userrole);
//				cb.execute(p);
//
//				r = true;
//				Event event = new EventImpl("",
//						ACLEventType.USER_ROLE_INFO_CHANGE);
//				super.change(event);
//			} catch (ControlException e) {
//				throw new ManagerException(e.getMessage());
//			}
//		}
		String saveUserRole = "insert into td_sm_userrole(user_id,role_id) values('"+userrole.getUser().getUserId()
			+ "','"+userrole.getRole().getRoleId()+"')";
		DBUtil db = new DBUtil();
		try {
			db.executeInsert(saveUserRole);
			Event event = new EventImpl("",
					ACLEventType.USER_ROLE_INFO_CHANGE);
			super.change(event);
			r = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return r;
	}

	/**
	 * 去掉hibernate后的方法
	 */
	public boolean storeUsergroup(Usergroup usergroup) throws ManagerException {
		boolean r = false;

//		if (usergroup != null) {
//			try {
//				Parameter p = new Parameter();
//
//				p.setCommand(Parameter.COMMAND_STORE);
//				p.setObject(usergroup);
//				cb.execute(p);
//
//				// Event event = new EventImpl(usergroup,
//				// ACLEventType.GROUP_INFO_ADD);
//				// super.change(event);
//
//				r = true;
//				Event event = new EventImpl("",
//						ACLEventType.USER_GROUP_INFO_CHANGE);
//				super.change(event);
//			} catch (ControlException e) {
//				throw new ManagerException(e.getMessage());
//			}
//		}
		String saveUserGroup = "insert into td_sm_usergroup(group_id,user_id) values('"+usergroup.getGroup().getGroupId()
			+ "','" + usergroup.getUser().getUserId() + "')";
		DBUtil db = new DBUtil();
		try {
			db.executeInsert(saveUserGroup);
			Event event = new EventImpl("",
			ACLEventType.USER_GROUP_INFO_CHANGE);
			super.change(event);
			r = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return r;
	}

	/**
	 * 保存用户 用户组关系
	 * 
	 * @param usergroup
	 * @return
	 * @throws ManagerException
	 *             UserManagerImpl.java
	 * @author: ge.tao
	 */
	public boolean addUsergroup(Integer userid, String[] groupid)
			throws ManagerException {
		boolean r = false;
		DBUtil db = new DBUtil();
//		TransactionManager tm = new TransactionManager();
//		PreparedDBUtil preparedDBUtil = new PreparedDBUtil();
//		StringBuffer sql = new StringBuffer();
		try {
//			tm.begin();
			for (int i = 0; (groupid != null) && (i < groupid.length); i++) {
//				sql.append("insert into td_sm_usergroup(user_id,group_id) ")
//					.append("(select ? as user_id,? as group_id from dual ")
//					.append(" where not exists (select user_id,group_id from td_sm_usergroup where ")
//					.append(" user_id = ? and group_id=?))");
//				preparedDBUtil.preparedInsert(sql.toString());
//				preparedDBUtil.setString(1, String.valueOf(userid));
//				preparedDBUtil.setString(2, groupid[i]);
//				preparedDBUtil.setString(3, String.valueOf(userid));
//				preparedDBUtil.setString(4, groupid[i]);
//				
//				preparedDBUtil.addPreparedBatch();
				String sql = "insert into td_sm_usergroup(user_id,group_id) "
						+ "(select "
						+ String.valueOf(userid)
						+ " as user_id ,"
						+ groupid[i]
						+ " as group_id "
						+ " from dual where not exists (select * from td_sm_usergroup where "
						+ " user_id =" + String.valueOf(userid)
						+ " and group_id=" + groupid[i] + "))";
				db.addBatch(sql);
				logger.warn(sql);
			}
			db.executeBatch();
//			preparedDBUtil.executePreparedBatch();
			r = true;
//			tm.commit();
			Event event = new EventImpl("", ACLEventType.USER_GROUP_INFO_CHANGE);
			super.change(event);
		} catch (Exception e) {
//			try {
//				tm.rollback();
//			} catch (RollbackException e1) {
//				e1.printStackTrace();
//			}
			e.printStackTrace();
			throw new ManagerException(e.getMessage());
		}

		return r;
	}

	/**
	 * 没有被使用的方法
	 */
	public boolean storeUserresop(Userresop userresop) throws ManagerException {
		boolean r = false;

//		if (userresop != null) {
//			try {
//				Parameter p = new Parameter();
//
//				p.setCommand(Parameter.COMMAND_STORE);
//				p.setObject(userresop);
//				cb.execute(p);
//
//				r = true;
//				Event event = new EventImpl("", ACLEventType.USER_INFO_CHANGE);
//				super.change(event);
//			} catch (ControlException e) {
//				throw new ManagerException(e.getMessage());
//			}
//		}

		return r;
	}

	/**
	 * 没有被使用的方法
	 */
	public boolean storeTempaccredit(Tempaccredit tempaccredit)
			throws ManagerException {
		boolean r = false;

//		if (tempaccredit != null) {
//			try {
//				Parameter p = new Parameter();
//				p.setCommand(Parameter.COMMAND_STORE);
//				p.setObject(tempaccredit);
//
//				cb.execute(p);
//
//				r = true;
//			} catch (ControlException e) {
//				throw new ManagerException(e.getMessage());
//			}
//		}

		return r;
	}

	/**
	 * 没有被使用的方法
	 */
	public boolean storeAccredit(Accredit accredit) throws ManagerException {
		boolean r = false;

//		if (accredit != null) {
//			try {
//				Parameter p = new Parameter();
//				p.setCommand(Parameter.COMMAND_STORE);
//				p.setObject(accredit);
//				cb.execute(p);
//
//				r = true;
//				Event event = new EventImpl("", ACLEventType.USER_INFO_CHANGE);
//				super.change(event);
//			} catch (ControlException e) {
//				throw new ManagerException(e.getMessage());
//			}
//		}

		return r;
	}
    /**
     * 删除用户所有资源
     */
	public boolean deleteUserRes(User user) throws ManagerException {
		boolean r = false;
		DBUtil db = new DBUtil();
		// String sql = "delete from TD_SM_USER_ADDONS where user_id ="
		// + user.getUserId() + "";
		// TD_SM_USER_ADDONS 不存在
		
		//删除用户的主机构 
		String sql1 = "delete from TD_SM_ORGUSER where user_id ="
				+ user.getUserId() + "";
		//判断是否有这个表 有的话 再删除.		
		String sql2 = "delete from TD_cms_siteuser where user_id ="
				+ user.getUserId() + "";
		//删除用户自身资源
		String sql3 = "delete TD_SM_roleresop a where" + " a.role_id='"
				+ user.getUserId() + "' and a.types='user'";
		//删除用户对应的机构管理员td_sm_orgmanager关系表---add gao.tang 2007.11.19
		String sql4 = "delete from TD_SM_ORGMANAGER where USER_ID = '" + user.getUserId() + "' ";
		//删除用户的用户组所关联的 Usergroup 对象---add gao.tang 2007.11.19
		String sql5 = "delete from TD_SM_USERGROUP where USER_ID = '" + user.getUserId() + "' ";
		//删除用户机构岗位关系表---add gao.tang 2007.11.19
		String sql6 = "delete from TD_SM_USERJOBORG where USER_ID = '" + user.getUserId() +"' ";
		//删除用户角色资源---add gao.tang 2007.11.19
		String sql7 = "delete from TD_SM_USERROLE where USER_ID = '" + user.getUserId() + "' ";
		TransactionManager tm = new TransactionManager();
		if (user != null) {
			try {
				tm.begin();
				// 批处理操作
				db.addBatch(sql1);
				if(db.getTableMetaData("TD_cms_siteuser")!=null){
					db.addBatch(sql2);
				}
				db.addBatch(sql3);
				db.addBatch(sql4);
				db.addBatch(sql5);
				db.addBatch(sql6);
				if(!"1".equals(String.valueOf(user.getUserId()))){//删除机构时机构中存在admin超级管理员，不删除超级管理员与较色的关系
					db.addBatch(sql7);
				}
				db.executeBatch();
				tm.commit();
				
				r = true;
				Event event = new EventImpl("", ACLEventType.USER_INFO_CHANGE);
				super.change(event);
			} catch (SQLException e2) {
				e2.printStackTrace();
				try {
					tm.rollback();
				} catch (RollbackException e) {
					e.printStackTrace();
				}
			} catch (Exception e) {
				e.printStackTrace();
				try {
					tm.rollback();
				} catch (RollbackException e1) {
					e1.printStackTrace();
				}
			}
		}
//			try {

//				cb.setAutoCommit(false);
//
//				SchedularManagerImpl schImpl = new SchedularManagerImpl();
//				schImpl.deleteAllSchTableByUserId(user.getUserId().intValue());
//
//				// 删除当前用户的所关联的 Userjoborg 对象
//				Parameter p = new Parameter();
//				p.setCommand(Parameter.COMMAND_DELETE);
//				p.setObject("from Userjoborg ujo where ujo.id.userId = "
//						+ user.getUserId());
//				cb.execute(p);
//
//				// 删除当前用户的所关联的 Userrole 对象
//				p.setObject("from Userrole ur where ur.id.userId = "
//						+ user.getUserId());
//				cb.execute(p);
//
//				// 删除当前用户的所关联的 Usergroup 对象
//				p.setObject("from Usergroup ug where ug.id.userId = "
//						+ user.getUserId());
//				cb.execute(p);
//
//				// 删除指定的用户实例
//				p.setObject("from User u where u.userId = " + user.getUserId());
//				cb.execute(p);
//				cb.commit(true);
//				r = true;
//				// 触发删除缓冲中用户的事件
//				Event event = new EventImpl(user.getUserId().toString(),
//						ACLEventType.USER_INFO_DELETE);
//				super.change(event);
//			} catch (ControlException e) {
//
//				try {
//					cb.rollback(true);
//				} catch (ControlException e1) {
//					logger.error(e1);
//				}
//
//				throw new ManagerException(e.getMessage());
//			}
//		}

		return r;
	}

	public boolean deleteUser(User user) throws ManagerException {
		boolean r = false;
		DBUtil db = new DBUtil();
		// String sql = "delete from TD_SM_USER_ADDONS where user_id ="
		// + user.getUserId() + "";
		// TD_SM_USER_ADDONS 不存在
		//删除用户的主机构 
		
		//		删除用户
		String sql = "delete from TD_SM_USER where user_id ="
				+ user.getUserId();
		
		String sql1 = "delete from TD_SM_ORGUSER where user_id ="
				+ user.getUserId() + "";
		//111
		String sql2 = "delete from TD_cms_siteuser where user_id ="
				+ user.getUserId() + "";
		//删除用户自身资源
		String sql3 = "delete from TD_SM_roleresop  where" + " role_id='"
				+ user.getUserId() + "' and types='user'";
		//删除用户对应的机构管理员td_sm_orgmanager关系表---add gao.tang 2007.11.19
		String sql4 = "delete from TD_SM_ORGMANAGER where USER_ID = '" + user.getUserId() + "' ";
		//删除用户的用户组所关联的 Usergroup 对象---add gao.tang 2007.11.19
		String sql5 = "delete from TD_SM_USERGROUP where USER_ID = '" + user.getUserId() + "' ";
		//删除用户机构岗位关系表---add gao.tang 2007.11.19
		String sql6 = "delete from TD_SM_USERJOBORG where USER_ID = '" + user.getUserId() +"' ";
		//删除用户角色资源---add gao.tang 2007.11.19
		String sql7 = "delete from TD_SM_USERROLE where USER_ID = '" + user.getUserId() + "' ";
		
		//删除用户历史任职表记录 gao.tang add by 2008.09.09
		String sql8 = "delete from td_sm_userjoborg_history where user_id = " + user.getUserId();
		
		
		
		TransactionManager tm = new TransactionManager();
		if (user != null) {
			try {
				// 批处理操作
				tm.begin();
				db.addBatch(sql1);
				if(db.getTableMetaData("td_cms_siteuser")!=null){
					db.addBatch(sql2);
				}
				db.addBatch(sql3);
				db.addBatch(sql4);
				db.addBatch(sql5);
				db.addBatch(sql6);
				db.addBatch(sql7);
				db.addBatch(sql8);
				db.addBatch(sql);
				db.executeBatch();
				
				tm.commit();
				r = true;
				Event event = new EventImpl("", ACLEventType.USER_INFO_CHANGE);
				super.change(event);
			} catch (SQLException e2) {
				e2.printStackTrace();
				try {
					tm.rollback();
				} catch (RollbackException e) {
					e.printStackTrace();
				}
			} catch (Exception e) {
				e.printStackTrace();
				try {
					tm.rollback();
				} catch (RollbackException e1) {
					e1.printStackTrace();
				}
			}
		}
		return r;
	}
	public boolean deleteBatchUser(String users[]) throws ManagerException {
		return deleteBatchUser(  users,  true);
	}
	/**
	 * 批量删除用户时改为批出理，gao.tang 修改
	 */
	public boolean deleteBatchUser(String users[],boolean sendevent) throws ManagerException {
		boolean r = false;
		TransactionManager tm = new TransactionManager();
		if (users != null) {
			try {
				tm.begin();
//				cb.setAutoCommit(false);
				PreparedDBUtil dbUtil = new PreparedDBUtil();
				for (int i = 0; i < users.length; i++) {
//					User user = getUser("userId", userIds[i]);

					// 删除当前用户的所关联的 Userjoborg 对象
//					Parameter p = new Parameter();
//					p.setCommand(Parameter.COMMAND_DELETE);
//					p.setObject("from Userjoborg ujo where ujo.id.userId = '"
//							+ userIds[i] + "'");
//					cb.execute(p);
					String userId = (users[i]);
					int iuid = Integer.parseInt(userId);
					String sql7 = "delete from TD_SM_ORGUSER where user_id =?";
					dbUtil.preparedDelete(sql7);
					dbUtil.setInt(1, iuid);
					dbUtil.addPreparedBatch();
					
					String sql1 = "delete from TD_SM_USERJOBORG where USER_ID = ?";
					dbUtil.preparedDelete(sql1);
					dbUtil.setInt(1, iuid);
					dbUtil.addPreparedBatch();
					// 删除当前用户的所关联的 Userrole 对象
//					p.setObject("from Userrole ur where ur.id.userId = '"
//							+ userIds[i] + "'");
//					cb.execute(p);
					
					String sql2 = "delete from TD_SM_USERROLE where USER_ID = ? ";
					if(!"1".equals(userId)){
						dbUtil.preparedDelete(sql2);
						dbUtil.setInt(1, iuid);
						dbUtil.addPreparedBatch();
					}
					// 删除当前用户的所关联的 Userresop 对象
//					p.setObject("from Userresop uro where uro.id.userId = '"
//							+ userIds[i] + "'");
//					cb.execute(p);
					String sql3 = "delete from TD_SM_ROLERESOP where ROLE_ID = ? and types='user' ";
					dbUtil.preparedDelete(sql3);
					dbUtil.setString(1, userId);
					dbUtil.addPreparedBatch();

					// 删除当前用户的所关联的 Usergroup 对象
//					p.setObject("from Usergroup ug where ug.id.userId = '"
//							+ userIds[i] + "'");
//					cb.execute(p);
					String sql4 = "delete from TD_SM_USERGROUP where USER_ID = ? ";
					dbUtil.preparedDelete(sql4);
					dbUtil.setInt(1, iuid);
					dbUtil.addPreparedBatch();

					// 删除当前用户的所关联的 Tempaccredit 对象
//					p.setObject("from Tempaccredit ta where ta.id.userId = '"
//							+ userIds[i] + "'");
//					cb.execute(p);
					//删除用户对应的机构管理员td_sm_orgmanager关系表
					String sql5 = "delete from TD_SM_ORGMANAGER where USER_ID = ? ";
					dbUtil.preparedDelete(sql5);
					dbUtil.setInt(1, iuid);
					dbUtil.addPreparedBatch();
					// 删除指定的用户实例
//					p.setObject("from User u where u.userId = '" + userIds[i]
//							+ "'");
//					cb.execute(p);
					String sql6 = "delete from TD_SM_USER where USER_ID = ? ";
					dbUtil.preparedDelete(sql6);
					dbUtil.setInt(1, iuid);
					dbUtil.addPreparedBatch();
					
					

				}
				dbUtil.executePreparedBatch();
				tm.commit();
//				 触发删除缓冲中用户的事件
				if(sendevent)
				{
					EventUtil.sendUSER_INFO_DELETEEvent(users);
				}
//				Event event = new EventImpl("",
//						ACLEventType.USER_INFO_DELETE);
//				super.change(event);
				r = true;
				
			} catch (SQLException e) {
				throw new ManagerException(e);
			} catch (Exception e) {
				throw new ManagerException(e);
			}
			finally
			{
				tm.release();
			}
		}
		return r;
	}
	public boolean deleteBatchUser(User[] users) throws ManagerException 
	{
		return deleteBatchUser(users,true);
	}
	public boolean deleteBatchUser(User[] users,boolean sendevent) throws ManagerException {
		boolean r = false;
		 
		TransactionManager tm = new TransactionManager();
		if (users != null) {
			try {
				tm.begin();
				String[] ids = new String[users.length];
				PreparedDBUtil dbUtil = new PreparedDBUtil();
				for (int i = 0; i < users.length; i++) {
//					User user = getUser("userId", userIds[i]);

					// 删除当前用户的所关联的 Userjoborg 对象
//					Parameter p = new Parameter();
//					p.setCommand(Parameter.COMMAND_DELETE);
//					p.setObject("from Userjoborg ujo where ujo.id.userId = '"
//							+ userIds[i] + "'");
//					cb.execute(p);
					
					int iuid = users[i].getUserId();
					
					String userId = iuid+"";
					ids[i] = userId;
					String sql7 = "delete from TD_SM_ORGUSER where user_id =?";
					dbUtil.preparedDelete(sql7);
					dbUtil.setInt(1, iuid);
					dbUtil.addPreparedBatch();
					
					String sql1 = "delete from TD_SM_USERJOBORG where USER_ID = ?";
					dbUtil.preparedDelete(sql1);
					dbUtil.setInt(1, iuid);
					dbUtil.addPreparedBatch();
					// 删除当前用户的所关联的 Userrole 对象
//					p.setObject("from Userrole ur where ur.id.userId = '"
//							+ userIds[i] + "'");
//					cb.execute(p);
					
					String sql2 = "delete from TD_SM_USERROLE where USER_ID = ? ";
					if(!"1".equals(userId)){
						dbUtil.preparedDelete(sql2);
						dbUtil.setInt(1, iuid);
						dbUtil.addPreparedBatch();
					}
					// 删除当前用户的所关联的 Userresop 对象
//					p.setObject("from Userresop uro where uro.id.userId = '"
//							+ userIds[i] + "'");
//					cb.execute(p);
					String sql3 = "delete from TD_SM_ROLERESOP where ROLE_ID = ? and types='user' ";
					dbUtil.preparedDelete(sql3);
					dbUtil.setString(1, userId);
					dbUtil.addPreparedBatch();

					// 删除当前用户的所关联的 Usergroup 对象
//					p.setObject("from Usergroup ug where ug.id.userId = '"
//							+ userIds[i] + "'");
//					cb.execute(p);
					String sql4 = "delete from TD_SM_USERGROUP where USER_ID = ? ";
					dbUtil.preparedDelete(sql4);
					dbUtil.setInt(1, iuid);
					dbUtil.addPreparedBatch();

					// 删除当前用户的所关联的 Tempaccredit 对象
//					p.setObject("from Tempaccredit ta where ta.id.userId = '"
//							+ userIds[i] + "'");
//					cb.execute(p);
					//删除用户对应的机构管理员td_sm_orgmanager关系表
					String sql5 = "delete from TD_SM_ORGMANAGER where USER_ID = ? ";
					dbUtil.preparedDelete(sql5);
					dbUtil.setInt(1, iuid);
					dbUtil.addPreparedBatch();
					// 删除指定的用户实例
//					p.setObject("from User u where u.userId = '" + userIds[i]
//							+ "'");
//					cb.execute(p);
					String sql6 = "delete from TD_SM_USER where USER_ID = ? ";
					dbUtil.preparedDelete(sql6);
					dbUtil.setInt(1, iuid);
					dbUtil.addPreparedBatch();
					
					

				}
				dbUtil.executePreparedBatch();
				tm.commit();
//				 触发删除缓冲中用户的事件
				if(sendevent)
				{
					EventUtil.sendUSER_INFO_DELETEEvent(ids);
				}
//				Event event = new EventImpl("",
//						ACLEventType.USER_INFO_DELETE);
//				super.change(event);
				r = true;
				
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
		}
		return r;
	}
	
	/**
	 * 批量删除用户资源时改为批出理，gao.tang 修改
	 */
	public boolean deleteBatchUserRes(String userIds[]) throws ManagerException {
		return deleteBatchUserRes(userIds,true);
	}
	
	/**
	 * 批量删除用户资源时改为批出理，gao.tang 修改
	 */
	public boolean deleteBatchUserRes(String userIds[],boolean broadcastevent) throws ManagerException {
		boolean r = false;
		TransactionManager tm = new TransactionManager();
		if (userIds != null) {
			try {
				tm.begin();
				int count = 0;
				PreparedDBUtil dbUtil = new PreparedDBUtil();
				for (int i = 0; i < userIds.length; i++) {

					// 删除当前用户的所关联的 Userjoborg 对象
					String userid = userIds[i];
					int uid = Integer.parseInt(userid);
					String sql7 = "delete from TD_SM_ORGUSER where user_id =?";
					dbUtil.preparedDelete(sql7);
					dbUtil.setInt(1, uid);
					dbUtil.addPreparedBatch();
					
					String sql1 = "delete from TD_SM_USERJOBORG where USER_ID = ?";
					dbUtil.preparedDelete(sql1);
					dbUtil.setInt(1, uid);
					dbUtil.addPreparedBatch();
					
					// 删除当前用户的所关联的 Userrole 对象
					String sql2 = "delete from TD_SM_USERROLE where USER_ID = ?";
					//如果删除的用户是admin不删除admin与角色的关系。删除机构时会出现下面现象
					if(!userIds[i].equals("1")){
						
								dbUtil.preparedDelete(sql2);
								dbUtil.setInt(1, uid);
								dbUtil.addPreparedBatch();
					}
					// 删除当前用户的所关联的 Userresop 对象
					String sql3 = "delete from TD_SM_ROLERESOP  where ROLE_ID = ? and types='user' ";
					dbUtil.preparedDelete(sql3);
					dbUtil.setString(1, userid);
					dbUtil.addPreparedBatch();

					String sql4 = "delete from TD_SM_USERGROUP where USER_ID = ? ";
					dbUtil.preparedDelete(sql4);
					dbUtil.setInt(1, uid);
					dbUtil.addPreparedBatch();
					//删除用户对应的机构管理员td_sm_orgmanager关系表
					String sql5 = "delete from TD_SM_ORGMANAGER where USER_ID = ?";
					dbUtil.preparedDelete(sql5);
					dbUtil.setInt(1, uid);
					dbUtil.addPreparedBatch();
					count ++;
					if(count > 900){
						dbUtil.executePreparedBatch();
						count = 0;
					}

				}
				if(count > 0)
					dbUtil.executePreparedBatch();
				tm.commit();
//				 触发删除缓冲中用户的事件
				if(broadcastevent)
				{
					EventUtil.sendUSER_INFO_DELETEEvent(userIds);
				}
				r = true;
				
			} catch (SQLException e) {
				
				throw new ManagerException(e);
			} catch (Exception e) {
				
				throw new ManagerException(e);
			}
			finally
			{
				tm.release();
			}
		}

		return r;
	}

	/**
	 * 没有被使用的方法
	 */
	public boolean deleteUserattr(Userattr userattr) throws ManagerException {
		boolean r = false;

//		if (userattr != null) {
//			try {
//				// 先删除当前用户的所有 Userjoborg 对象
//				Parameter p = new Parameter();
//				p.setCommand(Parameter.COMMAND_DELETE);
//				p.setObject(userattr);
//				cb.execute(p);
//
//				r = true;
//			} catch (ControlException e) {
//				throw new ManagerException(e.getMessage());
//			}
//		}

		return r;
	}

	public boolean deleteUserjoborg(Userjoborg userjoborg)
			throws ManagerException {
		boolean r = false;

		if (userjoborg != null) {
			try {
				DBUtil dbUtil = new DBUtil();
//				Parameter p = new Parameter();
//				p.setCommand(Parameter.COMMAND_DELETE);
//				p.setObject(userjoborg);
//				cb.execute(p);
				StringBuffer sql = new StringBuffer(); 
				sql.append("delete from TD_SM_USERJOBORG where USER_ID='")
				   .append(userjoborg.getUser().getUserId()).append("' and ")
				   .append("JOB_ID='").append(userjoborg.getJob().getJobId()).append("' and ")
				   .append("ORG_ID='").append(userjoborg.getOrg().getOrgId()).append("' ");
				dbUtil.executeDelete(sql.toString());
				Event event = new EventImpl(String.valueOf(userjoborg.getUser().getUserId()),
						ACLEventType.USER_ROLE_INFO_CHANGE);
				super.change(event);
				r = true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return r;
	}

	/**
	 *  去掉hibernate后的方法
	 */
	public boolean deleteUserjoborg(User user) throws ManagerException {
		boolean r = false;

//		if (user != null) {
//			try {
//				Parameter p = new Parameter();
//				p.setCommand(Parameter.COMMAND_DELETE);
//				p.setObject("from Userjoborg ujo where ujo.id.userId = '"
//						+ user.getUserId() + "'");
//				cb.execute(p);
//				Event event = new EventImpl(String.valueOf(user.getUserId()),
//						ACLEventType.USER_ROLE_INFO_CHANGE);
//				super.change(event);
//				r = true;
//			} catch (ControlException e) {
//				throw new ManagerException(e.getMessage());
//			}
//		}
		if(user != null){
			String delUserJobOrgsql = "delete from td_sm_userjoborg where user_id='" + user.getUserId() + "'";
			DBUtil db = new DBUtil();
			try {
				db.executeDelete(delUserJobOrgsql);
				Event event = new EventImpl(String.valueOf(user.getUserId()),
				ACLEventType.USER_ROLE_INFO_CHANGE);
				super.change(event);
				r = true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return r;
	}

	public boolean deleteUserjoborg(Job job, Organization org)
			throws ManagerException {
		boolean r = false;

		if (job != null && org != null) {
			DBUtil dbUtil = new DBUtil();
			String sql = "delete from TD_SM_USERJOBORG where JOB_ID ='"
					+ job.getJobId() + "' and" + " ORG_ID = '" + org.getOrgId()
					+ "'";
			try {
				dbUtil.executeDelete(sql);
				r = true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
			// try {
			// Parameter p = new Parameter();
			// p.setCommand(Parameter.COMMAND_DELETE);
			// p.setObject("from Userjoborg ujo where ujo.id.orgId = '"
			// + org.getOrgId() + "' and ujo.id.jobId ='"
			// + job.getJobId() + "'");
			// cb.execute(p);
			// r = true;
			// } catch (ControlException e) {
			// throw new ManagerException(e.getMessage());
			// }
		}

		return r;

	}

	public boolean deleteUserjoborg(Job job, User user) throws ManagerException {
		boolean r = false;

		if (job != null && user != null) {
			try {
				DBUtil dbUtil = new DBUtil();
//				Parameter p = new Parameter();
//				p.setCommand(Parameter.COMMAND_DELETE);
//				p.setObject("from Userjoborg ujo where ujo.id.userId = '"
//						+ user.getUserId() + "' and ujo.id.jobId ='"
//						+ job.getJobId() + "'");
//				cb.execute(p);
				StringBuffer sql = new StringBuffer();
				sql.append("delete from TD_SM_USERJOBORG where USER_ID='")
				   .append(user.getUserId()).append("' and ")
				   .append("JOB_ID='").append(job.getJobId()).append("' ");
				dbUtil.executeDelete(sql.toString());
				r = true;
				Event event = new EventImpl(String.valueOf(user.getUserId()),
						ACLEventType.USER_ROLE_INFO_CHANGE);
				super.change(event);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return r;

	}

	public boolean deleteUserjoborg(Organization org, User user)
			throws ManagerException {
		boolean r = false;
		DBUtil dbUtil = null;
		TransactionManager tm = new TransactionManager();
		if (org != null) {
			try {
				tm.begin();
				dbUtil = new DBUtil();
//				Parameter p = new Parameter();
//				p.setCommand(Parameter.COMMAND_DELETE);
//				p.setObject("from Userjoborg ujo where ujo.id.orgId = '"
//						+ org.getOrgId() + "' and ujo.id.userId="
//						+ user.getUserId() + "");
//				cb.execute(p);
				//删除TD_SM_USERJOBORG表中该用户在该机构下的所有岗位的记录
				StringBuffer sql = new StringBuffer();
				sql.append("delete from TD_SM_USERJOBORG where USER_ID='")
				   .append(user.getUserId()).append("' and ORG_ID='")
				   .append(org.getOrgId()).append("' ");
			
				//删除TD_SM_ORGMANAGER表中该用户与该机构管理员的关系记录
				StringBuffer sql1 = new StringBuffer();
//				sql1.append("delete from td_sm_orgmanager where USER_ID='")
//					.append(user.getUserId()).append("' and ORG_ID='")
//					.append(org.getOrgId()).append("' ");
				sql1.append("delete from td_sm_orgmanager where USER_ID='")
				.append(user.getUserId()).append("' ");		
			
				dbUtil.addBatch(sql.toString());
				dbUtil.addBatch(sql1.toString());
				dbUtil.executeBatch();
				tm.commit();
				Event event = new EventImpl(String.valueOf(user.getUserId()),
						ACLEventType.USER_ROLE_INFO_CHANGE);
				super.change(event);
				r = true;
				
			} catch (SQLException e) {
				try {
					tm.rollback();
				} catch (RollbackException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				throw new ManagerException(e.getMessage());
			} catch (Exception e) {
				try {
					tm.rollback();
				} catch (RollbackException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				throw new ManagerException(e.getMessage());
			}finally{
				dbUtil.resetBatch();
			}
		}
		return r;
	}

	public boolean deleteUsergroup(Usergroup usergroup) throws ManagerException {
		boolean r = false;

//		if (usergroup != null) {
//			try {
//				List userList = getUserList(usergroup.getGroup());
//				String src[][] = new String[1][userList.size() + 1];
//
//				Parameter p = new Parameter();
//				p.setCommand(Parameter.COMMAND_DELETE);
//				p.setObject(usergroup);
//				cb.execute(p);
//				r = true;
//
//				// 触发事件
//				src[0][0] = usergroup.getGroup().getGroupName();
//				for (int i = 0; i < userList.size(); i++) {
//					User user = (User) userList.get(i);
//					src[0][i + 1] = user.getUserName();
//				}
//
//				Event event = new EventImpl("",
//						ACLEventType.USER_GROUP_INFO_CHANGE);
//				super.change(event);
//			} catch (ControlException e) {
//				throw new ManagerException(e.getMessage());
//			}
//		}
		if(usergroup != null){
			String deleteUsergroupSql = "delete from td_sm_usergroup where user_id='" + usergroup.getUser().getUserId()
				+  "' and group_id='" + usergroup.getGroup().getGroupId() + "'";
			DBUtil db = new DBUtil();
			try {
				db.executeDelete(deleteUsergroupSql);
				Event event = new EventImpl("",
				ACLEventType.USER_GROUP_INFO_CHANGE);
				super.change(event);
				r = true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return r;
	}

	/**
	 * 删除用户,用户组关系
	 * 
	 * @param userid
	 * @param groupid
	 * @return
	 * @throws ManagerException
	 *             UserManagerImpl.java
	 * @author: ge.tao
	 */
	public boolean deleteUsergroup(Integer userid, String[] groupids)
			throws ManagerException {
		boolean r = false;
		DBUtil db = new DBUtil();
		try {
			for (int i = 0; (groupids != null) && (i < groupids.length); i++) {
				String groupid = groupids[i];
				String sql = "delete from td_sm_usergroup where GROUP_ID=" + groupid
						+ " and USER_ID=" + String.valueOf(userid);
				db.addBatch(sql);
			}
			db.executeBatch();
			r = true;
			Event event = new EventImpl(String.valueOf(userid), ACLEventType.USER_GROUP_INFO_CHANGE);
			super.change(event);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ManagerException(e.getMessage());
		}finally{
			db.resetBatch();
		}
		return r;
	}

	/**
	 * 去掉hibernate后的方法
	 */
	public boolean deleteUsergroup(User user) throws ManagerException {
		boolean r = false;

//		if (user != null) {
//			try {
//				Parameter p = new Parameter();
//				p.setCommand(Parameter.COMMAND_DELETE);
//				p.setObject("from Usergroup ug where ug.id.userId = '"
//						+ user.getUserId() + "'");
//				cb.execute(p);
//				Event event = new EventImpl(String.valueOf(user.getUserId()),
//						ACLEventType.USER_GROUP_INFO_CHANGE);
//				super.change(event);
//				r = true;
//
//			} catch (ControlException e) {
//				throw new ManagerException(e.getMessage());
//			}
//		}
		if(user != null){
			String deleteUsergroupSql = "delete from td_sm_usergroup where user_id='" + user.getUserId() + "'";
			DBUtil db = new DBUtil();
			try {
				db.executeDelete(deleteUsergroupSql);
				Event event = new EventImpl(String.valueOf(user.getUserId()),
						ACLEventType.USER_GROUP_INFO_CHANGE);
				super.change(event);
				r = true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return r;
	}

	/**
	 * 去掉hibernate后的方法
	 */
	public boolean deleteUsergroup(Group group) throws ManagerException {
		boolean r = false;

//		if (group != null) {
//			try {
//				Parameter p = new Parameter();
//				p.setCommand(Parameter.COMMAND_DELETE);
//				p.setObject("from Usergroup ug where ug.id.groupId = '"
//						+ group.getGroupId() + "'");
//				cb.execute(p);
//
//				r = true;
//				Event event = new EventImpl("",
//						ACLEventType.USER_GROUP_INFO_CHANGE);
//				super.change(event);
//
//			} catch (ControlException e) {
//				throw new ManagerException(e.getMessage());
//			}
//		}
		if(group != null){
			String deleteUsergroupSql = "delete from td_sm_usergroup where group_id='" + group.getGroupId() + "'";
			DBUtil db = new DBUtil();
			try {
				db.executeDelete(deleteUsergroupSql);
				r = true;
				Event event = new EventImpl("",
						ACLEventType.USER_GROUP_INFO_CHANGE);
				super.change(event);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return r;
	}

	/**
	 * 没有被使用的方法
	 */
	public boolean deleteUserrole(Userrole userrole) throws ManagerException {
		boolean r = false;

//		if (userrole != null) {
//			try {
//				Parameter p = new Parameter();
//				p.setCommand(Parameter.COMMAND_DELETE);
//				p.setObject(userrole);
//				cb.execute(p);
//
//				r = true;
//
//				// 触发事件
//				String src[] = new String[] { userrole.getUser().getUserName(),
//						userrole.getRole().getRoleName() };
//				Event event = new EventImpl(src,
//						ACLEventType.USER_ROLE_INFO_CHANGE);
//				super.change(event);
//			} catch (ControlException e) {
//				throw new ManagerException(e.getMessage());
//			}
//		}

		return r;
	}

	/**
	 * 没有被使用的方法
	 */
	public boolean deleteUserresop(Userresop userresop) throws ManagerException {
		boolean r = false;

//		if (userresop != null) {
//			try {
//				Parameter p = new Parameter();
//				p.setCommand(Parameter.COMMAND_DELETE);
//				p.setObject(userresop);
//				cb.execute(p);
//
//				r = true;
//				Event event = new EventImpl(String.valueOf(userresop.getUser().getUserId()),
//						ACLEventType.USER_ROLE_INFO_CHANGE);
//				super.change(event);
//			} catch (ControlException e) {
//				throw new ManagerException(e.getMessage());
//			}
//		}

		return r;
	}

	/**
	 * 没有被使用的方法
	 */
	public boolean deleteAccredit(Accredit accredit) throws ManagerException {
		boolean r = false;

//		if (accredit != null) {
//			try {
//				Parameter p = new Parameter();
//				p.setCommand(Parameter.COMMAND_DELETE);
//				p.setObject(accredit);
//				cb.execute(p);
//
//				r = true;
//				Event event = new EventImpl("", ACLEventType.USER_INFO_CHANGE);
//				super.change(event);
//			} catch (ControlException e) {
//				throw new ManagerException(e.getMessage());
//			}
//		}

		return r;
	}

	/**
	 *没有被使用的方法
	 */
	public boolean deleteTempaccredit(Tempaccredit tempaccredit)
			throws ManagerException {
		boolean r = false;

//		if (tempaccredit != null) {
//			try {
//				Parameter p = new Parameter();
//				p.setCommand(Parameter.COMMAND_DELETE);
//				p.setObject(tempaccredit);
//				cb.execute(p);
//
//				r = true;
//				Event event = new EventImpl("", ACLEventType.USER_INFO_CHANGE);
//				super.change(event);
//			} catch (ControlException e) {
//				throw new ManagerException(e.getMessage());
//			}
//		}

		return r;
	}

	/**
	 * 修改为sql
	 */
	public boolean deleteUserrole(User user) throws ManagerException {
		boolean r = false;

//		if (user != null) {
//			try {
//				Parameter p = new Parameter();
//				p.setCommand(Parameter.COMMAND_DELETE);
//				p.setObject("from Userrole ur where ur.id.userId = '"
//						+ user.getUserId() + "'");
//				cb.execute(p);
//
//				r = true;
//				Event event = new EventImpl(String.valueOf(user.getUserId()),
//						ACLEventType.USER_ROLE_INFO_CHANGE);
//				super.change(event);
//			} catch (ControlException e) {
//				throw new ManagerException(e.getMessage());
//			}
//		}
		if(user != null){
			String deleteUserroleSql = "delete from td_sm_userrole where user_id='"
				+ user.getUserId() + "'";
			DBUtil db = new DBUtil();
			try {
				db.executeDelete(deleteUserroleSql);
				r = true;
				Event event = new EventImpl(String.valueOf(user.getUserId()),
						ACLEventType.USER_ROLE_INFO_CHANGE);
				super.change(event);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return r;
	}

	/**
	 * 没有被使用的方法
	 */
	public boolean deleteUserrole(Organization org, Role role)
			throws ManagerException {
		boolean r = false;

//		if (role != null) {
//			try {
//				Parameter p = new Parameter();
//				p.setCommand(Parameter.COMMAND_DELETE);
//				p.setObject("from Userrole ur where ur.id.roleId = '"
//						+ role.getRoleId()
//						+ "' and ur.id.userId in (select ujo.id.userId from "
//						+ "Userjoborg ujo where ujo.id.orgId = '"
//						+ org.getOrgId() + "')");
//				cb.execute(p);
//				r = true;
//				Event event = new EventImpl("",
//						ACLEventType.USER_ROLE_INFO_CHANGE);
//				super.change(event);
//			} catch (ControlException e) {
//				throw new ManagerException(e.getMessage());
//			}
//		}

		return r;
	}

	private List getUsers(DBUtil dbUtil) throws ManagerException {
		List list = new ArrayList();
		for (int i = 0; i < dbUtil.size(); i++) {

			try {
				User user = new User();
				user.setUserId(new Integer(dbUtil.getInt(i, "user_id")));
				user.setUserSn(new Integer(dbUtil.getInt(i, "user_sn")));
				user.setUserName(dbUtil.getString(i, "user_name"));
				user.setUserPassword(dbUtil.getString(i, "USER_PASSWORD"));
				user.setUserRealname(dbUtil.getString(i, "USER_REALNAME"));
				user.setUserPinyin(dbUtil.getString(i, "USER_PINYIN"));
				user.setUserSex(dbUtil.getString(i, "USER_SEX"));
				user.setUserHometel(dbUtil.getString(i, "USER_HOMETEL"));
				user.setUserWorktel(dbUtil.getString(i, "USER_WORKTEL"));
				user.setUserWorknumber(dbUtil.getString(i, "USER_WORKNUMBER"));
				user.setUserMobiletel1(dbUtil.getString(i, "USER_MOBILETEL1"));
				user.setUserMobiletel2(dbUtil.getString(i, "USER_MOBILETEL2"));
				user.setUserFax(dbUtil.getString(i, "USER_FAX"));
				user.setUserOicq(dbUtil.getString(i, "USER_OICQ"));
				
				//生日日期
				if(String.valueOf(dbUtil.getTimestamp(i, "USER_BIRTHDAY")) != "null"){
					user.setUserBirthday(new java.sql.Date(dbUtil.getTimestamp(i,"USER_BIRTHDAY").getTime()));
				}else{
					user.setUserBirthday((java.sql.Date)dbUtil.getDate(i, "USER_BIRTHDAY"));
				}
				user.setPasswordDualedTime(dbUtil.getInt(i, "Password_DualTime"));
				user.setPasswordUpdatetime(dbUtil.getTimestamp(i, "password_updatetime"));
				user.setPasswordExpiredTime((Timestamp)this.getPasswordExpiredTime(user.getPasswordUpdatetime(),user.getPasswordDualedTime()));
				
				user.setUserEmail(dbUtil.getString(i, "USER_EMAIL"));
				user.setUserAddress(dbUtil.getString(i, "USER_ADDRESS"));
				user.setUserPostalcode(dbUtil.getString(i, "USER_POSTALCODE"));
				user.setUserIdcard(dbUtil.getString(i, "USER_IDCARD"));
				user.setUserIsvalid(new Integer(dbUtil.getInt(i, "USER_ISVALID")));
				
				//注册日期
				if(String.valueOf(dbUtil.getTimestamp(i, "USER_REGDATE")) != "null"){
					user.setUserRegdate(new java.sql.Date(dbUtil.getTimestamp(i,"USER_REGDATE").getTime()));
				}else{
					user.setUserRegdate((java.sql.Date)dbUtil.getDate(i, "USER_REGDATE"));
				}
				
				user.setUserLogincount(new Integer(dbUtil.getInt(i,"USER_LOGINCOUNT")));
				user.setUserType(dbUtil.getString(i, "USER_TYPE"));
				user.setRemark1(dbUtil.getString(i, "REMARK1"));
				user.setRemark2(dbUtil.getString(i, "REMARK2"));
				user.setRemark3(dbUtil.getString(i, "REMARK3"));
				user.setRemark4(dbUtil.getString(i, "REMARK4"));
				user.setRemark5(dbUtil.getString(i, "REMARK5"));
				
				//过期日期
				if(String.valueOf(dbUtil.getTimestamp(i, "PAST_TIME")) != "null"){
					user.setPastTime(new java.sql.Date(dbUtil.getTimestamp(i, "PAST_TIME").getTime()));
				}else{
					user.setPastTime((java.sql.Date)dbUtil.getDate(i, "PAST_TIME"));
				}
				
				user.setDredgeTime(dbUtil.getString(i, "DREDGE_TIME"));
				
				//用户最后登陆时间
				if(String.valueOf(dbUtil.getTimestamp(i, "LASTLOGIN_DATE")) != "null"){
					user.setLastlogindate(new java.sql.Date(dbUtil.getTimestamp(i,"LASTLOGIN_DATE").getTime()));
				}else{
					user.setLastlogindate((java.sql.Date)dbUtil.getDate(i, "LASTLOGIN_DATE"));
				}
				
				user.setWorklength(dbUtil.getString(i, "WORKLENGTH"));
				user.setPolitics(dbUtil.getString(i, "POLITICS"));
				user.setIstaxmanager(dbUtil.getInt(i, "ISTAXMANAGER"));
				list.add(user);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	private User getUser(DBUtil dbUtil) throws ManagerException {
		User user = new User();
		try {
			user.setUserId(new Integer(dbUtil.getInt(0, "user_id")));
			user.setUserSn(new Integer(dbUtil.getInt(0, "user_sn")));
			user.setUserName(dbUtil.getString(0, "user_name"));
			user.setUserPassword(dbUtil.getString(0, "USER_PASSWORD"));
			user.setUserRealname(dbUtil.getString(0, "USER_REALNAME"));
			user.setUserPinyin(dbUtil.getString(0, "USER_PINYIN"));
			user.setUserSex(dbUtil.getString(0, "USER_SEX"));
			user.setUserHometel(dbUtil.getString(0, "USER_HOMETEL"));
			user.setUserWorktel(dbUtil.getString(0, "USER_WORKTEL"));
			user.setUserWorknumber(dbUtil.getString(0, "USER_WORKNUMBER"));
			user.setUserMobiletel1(dbUtil.getString(0, "USER_MOBILETEL1"));
			user.setUserMobiletel2(dbUtil.getString(0, "USER_MOBILETEL2"));
			user.setUserFax(dbUtil.getString(0, "USER_FAX"));
			user.setUserOicq(dbUtil.getString(0, "USER_OICQ"));
			
			//生日日期
			if(String.valueOf(dbUtil.getTimestamp(0, "USER_BIRTHDAY")) != "null"){
				user.setUserBirthday(new java.sql.Date(dbUtil.getTimestamp(0,"USER_BIRTHDAY").getTime()));
			}else{
				user.setUserBirthday((java.sql.Date)dbUtil.getDate(0, "USER_BIRTHDAY"));
			}
			
			user.setUserEmail(dbUtil.getString(0, "USER_EMAIL"));
			user.setUserAddress(dbUtil.getString(0, "USER_ADDRESS"));
			user.setUserPostalcode(dbUtil.getString(0, "USER_POSTALCODE"));
			user.setUserIdcard(dbUtil.getString(0, "USER_IDCARD"));
			user.setUserIsvalid(new Integer(dbUtil.getInt(0, "USER_ISVALID")));
			
			//注册日期
			if(String.valueOf(dbUtil.getTimestamp(0, "USER_REGDATE")) != "null"){
				user.setUserRegdate(new java.sql.Date(dbUtil.getTimestamp(0,"USER_REGDATE").getTime()));
			}else{
				user.setUserRegdate((java.sql.Date)dbUtil.getDate(0, "USER_REGDATE"));
			}
			user.setPasswordDualedTime(dbUtil.getInt(0, "Password_DualTime"));
			user.setPasswordUpdatetime(dbUtil.getTimestamp(0, "password_updatetime"));
			user.setPasswordExpiredTime((Timestamp)this.getPasswordExpiredTime(user.getPasswordUpdatetime(),user.getPasswordDualedTime()));	
			user.setUserLogincount(new Integer(dbUtil.getInt(0,"USER_LOGINCOUNT")));
			user.setUserType(dbUtil.getString(0, "USER_TYPE"));
			user.setRemark1(dbUtil.getString(0, "REMARK1"));
			user.setRemark2(dbUtil.getString(0, "REMARK2"));
			user.setRemark3(dbUtil.getString(0, "REMARK3"));
			user.setRemark4(dbUtil.getString(0, "REMARK4"));
			user.setRemark5(dbUtil.getString(0, "REMARK5"));
			
			
			//过期日期
			if(String.valueOf(dbUtil.getTimestamp(0, "PAST_TIME")) != "null"){
				user.setPastTime(new java.sql.Date(dbUtil.getTimestamp(0, "PAST_TIME").getTime()));
			}else{
				user.setPastTime((java.sql.Date)dbUtil.getDate(0, "PAST_TIME"));
			}
			
			user.setDredgeTime(dbUtil.getString(0, "DREDGE_TIME"));
			
			//用户最后登陆时间
			if(String.valueOf(dbUtil.getTimestamp(0, "LASTLOGIN_DATE")) != "null"){
				user.setLastlogindate(new java.sql.Date(dbUtil.getTimestamp(0,"LASTLOGIN_DATE").getTime()));
			}else{
				user.setLastlogindate((java.sql.Date)dbUtil.getDate(0, "LASTLOGIN_DATE"));
			}
			
			user.setWorklength(dbUtil.getString(0, "WORKLENGTH"));
			user.setPolitics(dbUtil.getString(0, "POLITICS"));
			user.setIstaxmanager(dbUtil.getInt(0, "ISTAXMANAGER"));
		} catch (SQLException e) {
			 throw new ManagerException(e.getMessage());
		}
		catch (Exception e) {
			 throw new ManagerException( e.getMessage());
		}
		return user;
	}
	private User getUser(User user,Record dbUtil) throws ManagerException {
//		User user = new User();
		try {
			user.setUserId(new Integer(dbUtil.getInt( "user_id")));
			user.setUserSn(new Integer(dbUtil.getInt( "user_sn")));
			user.setUserName(dbUtil.getString( "user_name"));
			user.setUserPassword(dbUtil.getString( "USER_PASSWORD"));
			user.setUserRealname(dbUtil.getString( "USER_REALNAME"));
			user.setUserPinyin(dbUtil.getString( "USER_PINYIN"));
			user.setUserSex(dbUtil.getString( "USER_SEX"));
			user.setUserHometel(dbUtil.getString( "USER_HOMETEL"));
			user.setUserWorktel(dbUtil.getString( "USER_WORKTEL"));
			user.setUserWorknumber(dbUtil.getString( "USER_WORKNUMBER"));
			user.setUserMobiletel1(dbUtil.getString( "USER_MOBILETEL1"));
			user.setUserMobiletel2(dbUtil.getString( "USER_MOBILETEL2"));
			user.setUserFax(dbUtil.getString( "USER_FAX"));
			user.setUserOicq(dbUtil.getString( "USER_OICQ"));
			
			//生日日期
			if(String.valueOf(dbUtil.getTimestamp( "USER_BIRTHDAY")) != "null"){
				user.setUserBirthday(new java.sql.Date(dbUtil.getTimestamp("USER_BIRTHDAY").getTime()));
			}else{
				user.setUserBirthday((java.sql.Date)dbUtil.getDate( "USER_BIRTHDAY"));
			}
			
			user.setUserEmail(dbUtil.getString( "USER_EMAIL"));
			user.setUserAddress(dbUtil.getString( "USER_ADDRESS"));
			user.setUserPostalcode(dbUtil.getString( "USER_POSTALCODE"));
			user.setUserIdcard(dbUtil.getString( "USER_IDCARD"));
			user.setUserIsvalid(new Integer(dbUtil.getInt( "USER_ISVALID")));
			
			//注册日期
			if(String.valueOf(dbUtil.getTimestamp( "USER_REGDATE")) != "null"){
				user.setUserRegdate(new java.sql.Date(dbUtil.getTimestamp("USER_REGDATE").getTime()));
			}else{
				user.setUserRegdate((java.sql.Date)dbUtil.getDate( "USER_REGDATE"));
			}
			user.setPasswordDualedTime(dbUtil.getInt( "Password_DualTime"));
			user.setPasswordUpdatetime(dbUtil.getTimestamp("password_updatetime"));
			user.setPasswordExpiredTime((Timestamp)this.getPasswordExpiredTime(user.getPasswordUpdatetime(),user.getPasswordDualedTime()));
			
			user.setUserLogincount(new Integer(dbUtil.getInt("USER_LOGINCOUNT")));
			user.setUserType(dbUtil.getString( "USER_TYPE"));
			user.setRemark1(dbUtil.getString( "REMARK1"));
			user.setRemark2(dbUtil.getString( "REMARK2"));
			user.setRemark3(dbUtil.getString( "REMARK3"));
			user.setRemark4(dbUtil.getString( "REMARK4"));
			user.setRemark5(dbUtil.getString( "REMARK5"));
			
			//过期日期
			if(String.valueOf(dbUtil.getTimestamp( "PAST_TIME")) != "null"){
				user.setPastTime(new java.sql.Date(dbUtil.getTimestamp( "PAST_TIME").getTime()));
			}else{
				user.setPastTime((java.sql.Date)dbUtil.getDate( "PAST_TIME"));
			}
			
			user.setDredgeTime(dbUtil.getString( "DREDGE_TIME"));
			
			//用户最后登陆时间
			if(String.valueOf(dbUtil.getTimestamp( "LASTLOGIN_DATE")) != "null"){
				user.setLastlogindate(new java.sql.Date(dbUtil.getTimestamp("LASTLOGIN_DATE").getTime()));
			}else{
				user.setLastlogindate((java.sql.Date)dbUtil.getDate( "LASTLOGIN_DATE"));
			}
			
			user.setWorklength(dbUtil.getString( "WORKLENGTH"));
			user.setPolitics(dbUtil.getString( "POLITICS"));
			user.setIstaxmanager(dbUtil.getInt( "ISTAXMANAGER"));
		} catch (SQLException e) {
			 throw new ManagerException(e);
		}
		catch (Exception e) {
			 throw new ManagerException( e);
		}
		return user;
	}

	public User getUser(String propName, String value) throws ManagerException {
		User user = null;
		try {

			DBUtil db = new DBUtil();

			if (propName == null || value == null || propName.trim().length() == 0
					|| value.trim().length() == 0) {
				return user;
			} else {
				if (propName.equalsIgnoreCase("userId"))
					propName = "user_id";
				if (propName.equalsIgnoreCase("username"))
					propName = "user_name";
			}

//			String sql = "select * from td_sm_user where " + propName + "='"
//					+ value + "'";
			StringBuffer sql = new StringBuffer();
			sql.append("select USER_ID, USER_SN, USER_NAME, USER_PASSWORD, USER_REALNAME, USER_PINYIN, ")
			   .append("USER_SEX, USER_HOMETEL, USER_WORKTEL, USER_WORKNUMBER, USER_MOBILETEL1, ")
			   .append("USER_MOBILETEL2, USER_FAX, USER_OICQ, USER_BIRTHDAY, USER_EMAIL, USER_ADDRESS, ")
			   .append("USER_POSTALCODE, USER_IDCARD, USER_ISVALID, USER_REGDATE, USER_LOGINCOUNT, ")
			   .append("USER_TYPE, REMARK1, REMARK2, REMARK3, REMARK4, REMARK5, PAST_TIME, DREDGE_TIME, ")
			   .append("LASTLOGIN_DATE, WORKLENGTH, POLITICS, ISTAXMANAGER,password_updatetime,Password_DualTime from TD_SM_USER ")
			   .append("where ").append(propName).append(" = '").append(value).append("' ");
			db.executeSelect(sql.toString());
			if (db.size() > 0) {
				user = this.getUser(db);
			}
			// Parameter p = new Parameter();
			// p.setCommand(Parameter.COMMAND_GET);
			// p.setObject("from User user where user." + propName + "='" +
			// value
			// + "'");
			// List list = (List) cb.execute(p);
			//
			// if (list != null && !list.isEmpty())
			// user = (User) list.get(0);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}
	
	public User getUserById(String userId)  throws ManagerException {
		if(StringUtil.isEmpty(userId))
			return null;
		User user = null;
		try {
			PreparedDBUtil db = new PreparedDBUtil();
			StringBuffer sql = new StringBuffer();
			sql.append("select USER_ID, USER_SN, USER_NAME, USER_PASSWORD, USER_REALNAME, USER_PINYIN, ")
			   .append("USER_SEX, USER_HOMETEL, USER_WORKTEL, USER_WORKNUMBER, USER_MOBILETEL1, ")
			   .append("USER_MOBILETEL2, USER_FAX, USER_OICQ, USER_BIRTHDAY, USER_EMAIL, USER_ADDRESS, ")
			   .append("USER_POSTALCODE, USER_IDCARD, USER_ISVALID, USER_REGDATE, USER_LOGINCOUNT, ")
			   .append("USER_TYPE, REMARK1, REMARK2, REMARK3, REMARK4, REMARK5, PAST_TIME, DREDGE_TIME, ")
			   .append("LASTLOGIN_DATE, WORKLENGTH, POLITICS, ISTAXMANAGER,password_updatetime,Password_DualTime from TD_SM_USER ")
			   .append("where USER_ID = ? ");
		
			db.preparedSelect(sql.toString());
			db.setInt(1, Integer.parseInt(userId));
			db.executePrepared();
		
			if (db.size() > 0) {
				user = this.getUser(db);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ManagerException e) {
			e.printStackTrace();
		}
		return user;
	}
	
	/** 根据用户工号或域账号查询用户信息
	 * @param worknumber
	 * @return
	 * @throws ManagerException
	 * 2014年8月22日
	 */
	public User getUserByWorknumberOrUsername(String worknumberOrUsername)  throws ManagerException {
		if(StringUtil.isEmpty(worknumberOrUsername))
			return null;
		User user = null;
		try {
			PreparedDBUtil db = new PreparedDBUtil();
			StringBuffer sql = new StringBuffer();
			sql.append("select USER_ID, USER_SN, USER_NAME, USER_PASSWORD, USER_REALNAME, USER_PINYIN, ")
			   .append("USER_SEX, USER_HOMETEL, USER_WORKTEL, USER_WORKNUMBER, USER_MOBILETEL1, ")
			   .append("USER_MOBILETEL2, USER_FAX, USER_OICQ, USER_BIRTHDAY, USER_EMAIL, USER_ADDRESS, ")
			   .append("USER_POSTALCODE, USER_IDCARD, USER_ISVALID, USER_REGDATE, USER_LOGINCOUNT, ")
			   .append("USER_TYPE, REMARK1, REMARK2, REMARK3, REMARK4, REMARK5, PAST_TIME, DREDGE_TIME, ")
			   .append("LASTLOGIN_DATE, WORKLENGTH, POLITICS, ISTAXMANAGER,password_updatetime,Password_DualTime from TD_SM_USER ")
			   .append("where user_worknumber = ? or user_name= ? ");
		
			db.preparedSelect(sql.toString());
			db.setString(1, worknumberOrUsername);
			db.setString(2, worknumberOrUsername);
			db.executePrepared();
		
			if (db.size() > 0) {
				user = this.getUser(db);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ManagerException e) {
			e.printStackTrace();
		}
		return user;
	}
	/**
	 * 根据传入的名称对用户安装用户账号，用户工号，用户真实名称进行组合
	 * @param username
	 * @return
	 * @throws Exception
	 */
	public List<User> getUsers(String username) throws Exception
	{
		if(username == null || username.equals(""))
		{
			return new ArrayList<User>();
		}
		username = username + "%";
		
		TransactionManager tm = new TransactionManager();
		try
		{
			tm.begin();
			List<User> users = SQLExecutor.queryListByRowHandler(new RowHandler<User>(){
	
				@Override
				public void handleRow(User rowValue, Record record)
						throws Exception {
					
					getUser( rowValue,record) ;
				}
				
			}, User.class, "select * from td_sm_user where USER_NAME like ? or USER_REALNAME like ? or USER_WORKNUMBER like ? order by USER_NAME", username ,username,"%"+username);
			for(User user :users)
			{
				String orgjob = FunctionDB.getUserorgjobinfos(user.getUserId());
				if(orgjob.endsWith(","))
				{
					orgjob = orgjob.substring(0, orgjob.length() - 1);
				}
				user.setOrgName(orgjob);
			}
			tm.commit();
			return users;
		}
		catch(Exception e)
		{
			
		}
		finally
		{
			tm.release();
		}
		return new ArrayList<User>();
		
		
	}
	
	@Override
	public MoreListInfo getMoreUsers(String username, long offset, int pagesize)
			throws Exception {
		if(username == null || username.equals(""))
		{
			return new MoreListInfo();
		}
		username = username + "%";
		TransactionManager tm = new TransactionManager();
		try
		{
			tm.begin();
			
			ListInfo listInfo = SQLExecutor.moreListInfoByRowHandler(new RowHandler<User>(){
	
				@Override
				public void handleRow(User rowValue, Record record)
						throws Exception {
					
					getUser( rowValue,record) ;
				}
				
			}, User.class, "select * from td_sm_user where USER_NAME like ? or USER_REALNAME like ? or USER_WORKNUMBER like ? order by USER_NAME desc", offset,pagesize,username ,username,"%"+username);
			List<User> users = listInfo.getDatas();
			if(users != null && users.size() > 0)
			{
				for(User user :users)
				{
					String orgjob = FunctionDB.getUserorgjobinfos(user.getUserId());
					if(orgjob.endsWith(","))
					{
						orgjob = orgjob.substring(0, orgjob.length() - 1);
					}
					user.setOrgName(orgjob);
				}
			}
			tm.commit();
			MoreListInfo moredata = listInfo.getMoreListInfo();
			return moredata;
		}
		catch(Exception e)
		{
			
		}
		finally
		{
			tm.release();
		}
		return new MoreListInfo();
	}
	public User getUserByName(String userName) throws ManagerException {
		User user = null;
		try {
//			PreparedDBUtil db = new PreparedDBUtil();
			StringBuffer sql = new StringBuffer();
			sql.append("select USER_ID, USER_SN, USER_NAME, USER_PASSWORD, USER_REALNAME, USER_PINYIN, ")
			   .append("USER_SEX, USER_HOMETEL, USER_WORKTEL, USER_WORKNUMBER, USER_MOBILETEL1, ")
			   .append("USER_MOBILETEL2, USER_FAX, USER_OICQ, USER_BIRTHDAY, USER_EMAIL, USER_ADDRESS, ")
			   .append("USER_POSTALCODE, USER_IDCARD, USER_ISVALID, USER_REGDATE, USER_LOGINCOUNT, ")
			   .append("USER_TYPE, REMARK1, REMARK2, REMARK3, REMARK4, REMARK5, PAST_TIME, DREDGE_TIME, ")
			   .append("LASTLOGIN_DATE, WORKLENGTH, POLITICS, ISTAXMANAGER,password_updatetime,Password_DualTime from TD_SM_USER ")
			   .append("where USER_NAME = ? ");
		
//			db.preparedSelect(sql.toString());
//			db.setString(1, userName);
//			db.executePrepared();
			user = SQLExecutor.queryObjectByRowHandler(new RowHandler<User>(){

				@Override
				public void handleRow(User rowValue, Record record)
						throws Exception {
					
					getUser( rowValue,record) ;
				}
				
			}, User.class, sql.toString(), userName);
//			db.executeSelect(sql.toString());
//			user = this.getUser(db);
//			if (db.size() > 0) {
//				user = this.getUser(db);
//			}
		} catch (SQLException e) {			
			throw new ManagerException("通过用户帐号["+ userName +"]获取用户信息失败：" + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ManagerException("通过用户帐号["+ userName +"]获取用户信息失败" + e.getMessage());
		}
		return user;
	} 
	
	/**
	 * 没有被使用的方法
	 */
	public User getUser(String hsql) throws ManagerException {
		User user = null;
//		// DBUtil db = new DBUtil();
//		try {
//			Parameter p = new Parameter();
//			p.setCommand(Parameter.COMMAND_GET);
//			p.setObject(hsql);
//			List list = (List) cb.execute(p);
//			// List list = db.execute;
//			if (list != null && !list.isEmpty())
//				user = (User) list.get(0);
//		} catch (ControlException e) {
//			e.printStackTrace();
//			throw new ManagerException(e.getMessage());
//		}

		return user;
	}

	public List getUserList(String propName, String value, boolean isLike)
			throws ManagerException {

		List list = null;
		DBUtil dbUtil = new DBUtil();
		try {
//			Parameter p = new Parameter();
//			p.setCommand(Parameter.COMMAND_GET);
//			if (!isLike)
//				p.setObject("from User u where u." + propName + " = '" + value
//						+ "'");
//			else
//				p.setObject("from User u where u." + propName + " like '"
//						+ value + "'");
//
//			list = (List) cb.execute(p);
			StringBuffer sql = new StringBuffer();
			if(!isLike){
				sql.append("select USER_ID, USER_SN, USER_NAME, USER_PASSWORD, USER_REALNAME, USER_PINYIN, ")
				   .append("USER_SEX, USER_HOMETEL, USER_WORKTEL, USER_WORKNUMBER, USER_MOBILETEL1, ")
				   .append("USER_MOBILETEL2, USER_FAX, USER_OICQ, USER_BIRTHDAY, USER_EMAIL, USER_ADDRESS, ")
				   .append("USER_POSTALCODE, USER_IDCARD, USER_ISVALID, USER_REGDATE, USER_LOGINCOUNT, ")
				   .append("USER_TYPE, REMARK1, REMARK2, REMARK3, REMARK4, REMARK5, PAST_TIME, DREDGE_TIME, ")
				   .append("LASTLOGIN_DATE, WORKLENGTH, POLITICS, ISTAXMANAGER,password_updatetime,Password_DualTime from TD_SM_USER ")
				   .append("where ").append(propName).append(" = '").append(value).append("' ");
				dbUtil.executeSelect(sql.toString());
				if(dbUtil.size() > 0){
					list = this.getUsers(dbUtil);
				}
			}else{
				sql.append("select USER_ID, USER_SN, USER_NAME, USER_PASSWORD, USER_REALNAME, USER_PINYIN, ")
				   .append("USER_SEX, USER_HOMETEL, USER_WORKTEL, USER_WORKNUMBER, USER_MOBILETEL1, ")
				   .append("USER_MOBILETEL2, USER_FAX, USER_OICQ, USER_BIRTHDAY, USER_EMAIL, USER_ADDRESS, ")
				   .append("USER_POSTALCODE, USER_IDCARD, USER_ISVALID, USER_REGDATE, USER_LOGINCOUNT, ")
				   .append("USER_TYPE, REMARK1, REMARK2, REMARK3, REMARK4, REMARK5, PAST_TIME, DREDGE_TIME, ")
				   .append("LASTLOGIN_DATE, WORKLENGTH, POLITICS, ISTAXMANAGER,password_updatetime,Password_DualTime from TD_SM_USER ")
				   .append("where ").append(propName).append(" like '%").append(value).append("%' ");
				dbUtil.executeSelect(sql.toString());
				if(dbUtil.size() > 0){
					list = this.getUsers(dbUtil);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * 去掉hibernate后的方法
	 */
	public List getUserList(Group group) throws ManagerException {
		List list = null;

//		if (group != null) {
//			try {
//				Parameter p = new Parameter();
//				p.setCommand(Parameter.COMMAND_GET);
//				p
//						.setObject("from User user where user.userId in ("
//								+ "select ug.id.userId from Usergroup ug where ug.id.groupId = '"
//								+ group.getGroupId()
//								+ "') order by user.userRealname");
//				list = (List) cb.execute(p);
//			} catch (ControlException e) {
//				throw new ManagerException(e.getMessage());
//			}
//		}
		if(group != null){
			String getUserListSql = "select * from td_sm_user where user_id in("
				+ "select user_id from td_sm_usergroup where group_id='"
				+ group.getGroupId() + "') order by user_realname";
			DBUtil db = new DBUtil();
			try {
				db.executeSelect(getUserListSql);
				list = getUsers(db);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return list;
	}

	/**
	 * 没有被使用的方法
	 */
	public List getUserList(Job job) throws ManagerException {
		List list = null;

//		if (job != null) {
//			try {
//				Parameter p = new Parameter();
//				p.setCommand(Parameter.COMMAND_GET);
//				p
//						.setObject("from User user where user.userId in ("
//								+ "select ujo.id.userId from Userjoborg ujo where ujo.id.jobId = '"
//								+ job.getJobId() + "')");
//				list = (List) cb.execute(p);
//			} catch (ControlException e) {
//				throw new ManagerException(e.getMessage());
//			}
//		}

		return list;
	}

	/**
	 * 没有被使用的方法
	 */
	public List getUserList(Operation oper) throws ManagerException {
		List list = null;

//		if (oper != null) {
//			try {
//				Parameter p = new Parameter();
//				p.setCommand(Parameter.COMMAND_GET);
//				p
//						.setObject("from User user where user.userId in ("
//								+ "select uro.id.userId from Userresop uro where uro.id.opId = '"
//								+ oper.getOpId() + "')");
//				list = (List) cb.execute(p);
//			} catch (ControlException e) {
//				throw new ManagerException(e.getMessage());
//			}
//		}

		return list;
	}

	/**
	 * 没有被使用的方法
	 */
	public List getUserList(Res res) throws ManagerException {
		List list = null;

//		if (res != null) {
//			try {
//				Parameter p = new Parameter();
//				p.setCommand(Parameter.COMMAND_GET);
//				p
//						.setObject("from User user where user.userId in ("
//								+ "select uro.id.userId from Userresop uro where uro.id.resId = '"
//								+ res.getResId() + "')");
//				list = (List) cb.execute(p);
//			} catch (ControlException e) {
//				throw new ManagerException(e.getMessage());
//			}
//		}

		return list;
	}

	/**
	 * 通过机构id获取机构下隶属的用户列表 added by biaoping.yin on 2007.5.26
	 * 
	 * @param orgid
	 * @return
	 */
	public List getOrgUserList(String orgid) throws ManagerException {
		List allUser = new ArrayList();
		DBUtil db = new DBUtil();
		String sql = "select a.USER_ID,a.USER_SN,a.USER_NAME,a.USER_PASSWORD,a.USER_REALNAME,a.USER_WORKNUMBER,"
				+ "a.USER_PINYIN,a.USER_SEX,a.USER_HOMETEL,a.USER_WORKTEL,a.USER_MOBILETEL1,a.USER_MOBILETEL2,"
				+ "a.USER_FAX,a.USER_OICQ,a.USER_BIRTHDAY,a.USER_EMAIL,a.USER_ADDRESS,a.USER_POSTALCODE,a.USER_IDCARD,"
				+ "a.USER_ISVALID,a.USER_REGDATE,a.USER_LOGINCOUNT,a.USER_TYPE,a.REMARK1,a.REMARK2,a.REMARK3,a.REMARK4,"
				+ "a.REMARK5,max(b.same_job_user_sn) aa,max(b.job_sn) bb "
				+ " from td_sm_user a, td_sm_userjoborg b "
				+ "where a.user_id = b.user_id and b.org_id='"
				+ orgid
				+ "' "
				+ "group by a.USER_ID,a.USER_SN,a.USER_NAME,a.USER_PASSWORD,a.USER_REALNAME,a.USER_WORKNUMBER,"
				+ "a.USER_PINYIN,a.USER_SEX,a.USER_HOMETEL,a.USER_WORKTEL,a.USER_MOBILETEL1,a.USER_MOBILETEL2,"
				+ "a.USER_FAX,a.USER_OICQ,a.USER_BIRTHDAY,a.USER_EMAIL,a.USER_ADDRESS,a.USER_POSTALCODE,a.USER_IDCARD,"
				+ "a.USER_ISVALID,a.USER_REGDATE,a.USER_LOGINCOUNT,a.USER_TYPE,a.REMARK1,"
				+ "a.REMARK2,a.REMARK3,a.REMARK4,a.REMARK5 "
				+ " order by bb asc,aa asc";

		try {
			db.executeSelect(sql);
			for (int i = 0; i < db.size(); i++) {
				User user = new User();
				int userid = db.getInt(i, "user_id");
				user.setUserId(new Integer(userid));
				user.setUserName(db.getString(i, "USER_NAME"));
				user.setUserRealname(db.getString(i, "USER_REALNAME"));
				allUser.add(user);

			}
			return allUser;
		} catch (SQLException e) {

			e.printStackTrace();
			throw new ManagerException("获取机构[" + orgid + "]的用户失败:"
					+ e.getMessage());
		}

	}
	
	/**
	 * 通过机构id获取机构下隶属的用户列表,如果不是系统管理员将不出现admin
	 * @param orgid
	 * @param userId
	 * @return
	 * @throws ManagerException
	 */
	public List getOrgUserList(String orgid, String userId) throws ManagerException {
		List allUser = new ArrayList();
		DBUtil db = new DBUtil();
		String sql = "select a.USER_ID,a.USER_SN,a.USER_NAME,a.USER_PASSWORD,a.USER_REALNAME,a.USER_WORKNUMBER,"
				+ "a.USER_PINYIN,a.USER_SEX,a.USER_HOMETEL,a.USER_WORKTEL,a.USER_MOBILETEL1,a.USER_MOBILETEL2,"
				+ "a.USER_FAX,a.USER_OICQ,a.USER_BIRTHDAY,a.USER_EMAIL,a.USER_ADDRESS,a.USER_POSTALCODE,a.USER_IDCARD,"
				+ "a.USER_ISVALID,a.USER_REGDATE,a.USER_LOGINCOUNT,a.USER_TYPE,a.REMARK1,a.REMARK2,a.REMARK3,a.REMARK4,"
				+ "a.REMARK5,max(b.same_job_user_sn) aa,max(b.job_sn) bb "
				+ " from td_sm_user a, td_sm_userjoborg b "
				+ "where a.user_id = b.user_id and b.org_id='"
				+ orgid
				+ "' "
				+ "group by a.USER_ID,a.USER_SN,a.USER_NAME,a.USER_PASSWORD,a.USER_REALNAME,a.USER_WORKNUMBER,"
				+ "a.USER_PINYIN,a.USER_SEX,a.USER_HOMETEL,a.USER_WORKTEL,a.USER_MOBILETEL1,a.USER_MOBILETEL2,"
				+ "a.USER_FAX,a.USER_OICQ,a.USER_BIRTHDAY,a.USER_EMAIL,a.USER_ADDRESS,a.USER_POSTALCODE,a.USER_IDCARD,"
				+ "a.USER_ISVALID,a.USER_REGDATE,a.USER_LOGINCOUNT,a.USER_TYPE,a.REMARK1,"
				+ "a.REMARK2,a.REMARK3,a.REMARK4,a.REMARK5 "
				+ " order by bb asc,aa asc";

		try {
			db.executeSelect(sql);
			for (int i = 0; i < db.size(); i++) {
				User user = new User();
				int userid = db.getInt(i, "user_id");
				//如果当前用户不是超级管理员并且userid为超级管理员的ID则屏蔽掉超级管理员
				if(!"1".equals(userId) && userid==1){
					continue;
				}
				//如果当前用户不是超级管理员并且userid的值与userId相等则过滤此用户
//				if(!"1".equals(userId) && String.valueOf(userid).equals(userId)){
//					continue;
//				}
				user.setUserId(new Integer(userid));
				user.setUserName(db.getString(i, "USER_NAME"));
				user.setUserRealname(db.getString(i, "USER_REALNAME"));
				allUser.add(user);
				
			}
			return allUser;
		} catch (SQLException e) {

			e.printStackTrace();
			throw new ManagerException("获取机构[" + orgid + "]的用户失败:"
					+ e.getMessage());
		}

	}

	public List getUserList(Organization org) throws ManagerException {
		List list = new ArrayList();

//		if (org != null) {
//			try {
//				Parameter p = new Parameter();
//				p.setCommand(Parameter.COMMAND_GET);
//				// 修改：实现排序
//				// p
//				// .setObject("from User user where user.userId in ("
//				// + "select ujo.id.userId from Userjoborg ujo where
//				// ujo.id.orgId = '"
//				// + org.getOrgId() + "')");
//				// list = (List) cb.execute(p);
//
//				p.setObject("select ujo.id.userId,ujo.jobSn from Userjoborg ujo where ujo.id.orgId = '"
//								+ org.getOrgId() + "'");
//				List ujos = (List) cb.execute(p);
//				// 排序列表中的记录（目前仅实现按岗位排序）
//				Collections.sort(ujos, new UserComparator());
//
//				list = new ArrayList();
//				// 将排序后的用户添加到列表中
//				for (int i = 0; i < ujos.size(); i++) {
//					Object[] uj = (Object[]) ujos.get(i);
//					User user = getUser("userId", String.valueOf(uj[0]));
//					if (user != null) {
//						if (!list.contains(user))
//							list.add(user);
//					}
//				}
//
//				// 修改结束
//			} catch (ControlException e) {
//				throw new ManagerException(e.getMessage());
//			}
//		}
		DBUtil dbUtil = new DBUtil();
		StringBuffer sql = new StringBuffer()
			.append("select ujo.user_id, ujo.job_id, ujo.job_sn, ujo.same_job_user_sn, u.* ")
			.append("from td_sm_userjoborg ujo, td_sm_user u ")
			.append("where ujo.org_id = '").append(org.getOrgId()).append("' ")
			.append("and ujo.user_id = u.user_id and ujo.job_id='1' ")
			.append("order by ujo.same_job_user_sn");
		try {
			dbUtil.executeSelect(sql.toString());
			list = this.getUsers(dbUtil);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 去掉hibernate后的方法
	 */
	public List getUserList(Role role) throws ManagerException {
		List list = null;

//		if (role != null) {
//			try {
//				Parameter p = new Parameter();
//				p.setCommand(Parameter.COMMAND_GET);
//				p.setObject("from User user where user.userId in ("
//								+ "select ur.id.userId from Userrole ur where ur.id.roleId = '"
//								+ role.getRoleId() + "')");
//				list = (List) cb.execute(p);
//			} catch (ControlException e) {
//				throw new ManagerException(e.getMessage());
//			}
//		}
		String getUserListSql = "select * from td_sm_user where user_id in(select user_id "
			+ " from td_sm_userrole where role_id='"+role.getRoleId() +"')";
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(getUserListSql);
			list = getUsers(db);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * 获取角色对应的用户列表
	 * 
	 * @param role
	 * @return
	 * @throws ManagerException
	 */
	public List getUsersListOfRole(String roleid) throws ManagerException {
		List list = new ArrayList();

		if (roleid != null && roleid.length() > 0) {
			String userssql = "select a.* from td_sm_user a inner join td_sm_userrole b on a.user_id = b.user_id and b.role_id='"
					+ roleid + "'";
			DBUtil dbUtil = new DBUtil();
			try {
				dbUtil.executeSelect(userssql);
				for (int i = 0; i < dbUtil.size(); i++) {
					int userid = dbUtil.getInt(i, "user_id");
					int isvalid = dbUtil.getInt(i, "USER_ISVALID");

					User user = new User();
					user.setUserId(new Integer(userid));
					user.setUserName(dbUtil.getString(i, "USER_NAME"));
					user.setUserRealname(dbUtil.getString(i, "USER_REALNAME"));
					user.setUserPassword(dbUtil.getString(i, " USER_PASSWORD"));
					user.setUserSex(dbUtil.getString(i, "USER_SEX"));
					user.setUserMobiletel1(dbUtil.getString(i,
							"USER_MOBILETEL1"));
					user.setUserMobiletel2(dbUtil.getString(i,
							"USER_MOBILETEL2"));
					user.setUserIsvalid(new Integer(isvalid));
					user.setUserEmail(dbUtil.getString(i, "USER_EMAIL"));
					user.setUserType(dbUtil.getString(i, "USER_TYPE"));
					user.setPasswordDualedTime(dbUtil.getInt(i, "Password_DualTime"));
					user.setPasswordUpdatetime(dbUtil.getTimestamp(i, "password_updatetime"));
					user.setPasswordExpiredTime((Timestamp)this.getPasswordExpiredTime(user.getPasswordUpdatetime(),user.getPasswordDualedTime()));
					
					list.add(user);
				}
			} catch (SQLException e) {
				throw new ManagerException("获取角色的用户列表失败[roleid=" + roleid
						+ "]：" + e.getMessage());
			}
		}

		return list;
	}
	
	/**
	 * 获取角色对应的用户列表
	 * 
	 * @param role
	 * @return
	 * @throws ManagerException
	 */
	public ListInfo getUsersListInfoOfRole(String roleid,long offset,int pagesize) throws ManagerException {
		List list = new ArrayList();

		if (roleid != null && roleid.length() > 0) {
			String userssql = "select a.* from td_sm_user a inner join td_sm_userrole b on a.user_id = b.user_id and b.role_id=?";
			
			PreparedDBUtil dbUtil = new PreparedDBUtil();
			
			try {
				dbUtil.preparedSelect(userssql, offset, pagesize);
				dbUtil.setString(1, roleid);
				list = dbUtil.executePreparedForList(UserJobs.class,new RowHandler<UserJobs>(){

					@Override
					public void handleRow(UserJobs user, Record dbUtil)
							throws Exception {
						int userid = dbUtil.getInt(  "user_id");
						int isvalid = dbUtil.getInt(  "USER_ISVALID");

//						User user = new User();
						user.setUserId(new Integer(userid));
						user.setUserName(dbUtil.getString(  "USER_NAME"));
						user.setUserRealname(dbUtil.getString(  "USER_REALNAME"));
						user.setUserSex(dbUtil.getString(  "USER_SEX"));
						user.setWorkNumber(dbUtil.getString(  "USER_WORKNUMBER"));
						user.setUserMobiletel1(dbUtil.getString( 
								"USER_MOBILETEL1"));
						
						user.setUserEmail(dbUtil.getString(  "USER_EMAIL"));
						user.setUserType(dbUtil.getString(  "USER_TYPE"));
					
						String orgjob = FunctionDB.getUserorgjobinfos(userid);
						user.setJobName(orgjob);
//						user.setPasswordExpiredTime((Timestamp)this.getPasswordExpiredTime(user.getPasswordUpdatetime(),user.getPasswordDualedTime()));
						
					}
					
				});
				
				ListInfo ret = new ListInfo();
				ret.setDatas(list);
				ret.setTotalSize(dbUtil.getLongTotalSize());
				return ret;
			} catch (SQLException e) {
				throw new ManagerException("获取角色的用户列表失败[roleid=" + roleid
						+ "]：" + e.getMessage());
			}
		}

		return  null;
	}
	
	/**
	 * 获取当前机构的当前角色对应的用户列表
	 * 
	 * @param role
	 * @return
	 * @throws ManagerException
	 */
	public List getUsersListOfRoleInOrg(String roleid, String orgId) throws ManagerException {
		List list = new ArrayList();
		if(orgId == null)
			return list;
		if (roleid != null && roleid.length() > 0) {
			String userssql = 
					"select distinct a.* from td_sm_user a " +
					"inner join td_sm_userrole b on a.user_id = b.user_id and b.role_id=? inner join td_sm_userjoborg c on a.user_id = c.user_id and c.org_id=?";
			PreparedDBUtil dbUtil = new PreparedDBUtil();
			try {
				dbUtil.preparedSelect(userssql);
				dbUtil.setString(1, roleid);
				dbUtil.setString(2, orgId);
				list = dbUtil.executePreparedForList(User.class, new RowHandler<User>(){

					@Override
					public void handleRow(User user, Record dbUtil)
							throws Exception {
						int userid = dbUtil.getInt(  "user_id");
						int isvalid = dbUtil.getInt(  "USER_ISVALID");

//						User user = new User();
						user.setUserId(new Integer(userid));
						user.setUserName(dbUtil.getString(  "USER_NAME"));
						user.setUserRealname(dbUtil.getString(  "USER_REALNAME"));
						user.setUserPassword(dbUtil.getString(  " USER_PASSWORD"));
						user.setUserSex(dbUtil.getString(  "USER_SEX"));
						user.setUserMobiletel1(dbUtil.getString( 
								"USER_MOBILETEL1"));
						user.setUserMobiletel2(dbUtil.getString( 
								"USER_MOBILETEL2"));
						user.setUserIsvalid(new Integer(isvalid));
						user.setUserEmail(dbUtil.getString(  "USER_EMAIL"));
						user.setUserType(dbUtil.getString(  "USER_TYPE"));
						user.setPasswordUpdatetime(dbUtil.getTimestamp(  "password_updatetime"));
						user.setPasswordDualedTime(dbUtil.getInt(  "Password_DualTime"));
//						user.setPasswordExpiredTime((Timestamp)getPasswordExpiredTime(user.getPasswordUpdatetime(),user.getPasswordDualedTime()));
						
						
					}
					
				});
//				for (int i = 0; i < dbUtil.size(); i++) {
//					int userid = dbUtil.getInt(i, "user_id");
//					int isvalid = dbUtil.getInt(i, "USER_ISVALID");
//
//					User user = new User();
//					user.setUserId(new Integer(userid));
//					user.setUserName(dbUtil.getString(i, "USER_NAME"));
//					user.setUserRealname(dbUtil.getString(i, "USER_REALNAME"));
//					user.setUserPassword(dbUtil.getString(i, " USER_PASSWORD"));
//					user.setUserSex(dbUtil.getString(i, "USER_SEX"));
//					user.setUserMobiletel1(dbUtil.getString(i,
//							"USER_MOBILETEL1"));
//					user.setUserMobiletel2(dbUtil.getString(i,
//							"USER_MOBILETEL2"));
//					user.setUserIsvalid(new Integer(isvalid));
//					user.setUserEmail(dbUtil.getString(i, "USER_EMAIL"));
//					user.setUserType(dbUtil.getString(i, "USER_TYPE"));
//					user.setPasswordUpdatetime(dbUtil.getTimestamp(i, "password_updatetime"));
//					user.setPasswordDualedTime(dbUtil.getInt(i, "Password_DualTime"));
//					user.setPasswordExpiredTime((Timestamp)this.getPasswordExpiredTime(user.getPasswordUpdatetime(),user.getPasswordDualedTime()));
//					
//					list.add(user);
//				}
			} catch (SQLException e) {
				throw new ManagerException("获取当前机构的当前角色的用户列表失败[roleid=" + roleid
						+ "]：" + e.getMessage());
			}
		}
		return list;
	}

	/**
	 * 没有被使用的方法
	 */
	public List getUserList(Organization org, Role role)
			throws ManagerException {
		List list = null;

//		if (role != null) {
//			try {
//				Parameter p = new Parameter();
//				p.setCommand(Parameter.COMMAND_GET);
//				p
//						.setObject("from User user where user.userId in ("
//								+ "select ur.id.userId from Userrole ur where ur.id.roleId = '"
//								+ role.getRoleId()
//								+ "' and ur.id.userId in (select ujo.id.userId from Userjoborg ujo where ujo.id.orgId = '"
//								+ org.getOrgId() + "'))");
//				list = (List) cb.execute(p);
//			} catch (ControlException e) {
//				throw new ManagerException(e.getMessage());
//			}
//		}

		return list;
	}

	/**
	 * 根据orgid 和 jobid 获得在该org下但岗位不是jobid的用户列表 add by 
	 */
	public List getUserList(Orgjob orgjob) throws ManagerException {
		List list = null;
//
//		if (orgjob != null) {
//			try {
//				Parameter p = new Parameter();
//				p.setCommand(Parameter.COMMAND_GET);
//				p
//						.setObject("select distinct ujo.user.userId, ujo.user.userName from UserJoborg ujo where ujo.org.orgId = '"
//								+ orgjob.getId().getOrgId()
//								+ "' and ujo.job.jobId != '"
//								+ orgjob.getJob().getJobId() + "'))");
//				list = (List) cb.execute(p);
//			} catch (ControlException e) {
//				throw new ManagerException(e.getMessage());
//			}
//		}

		return list;
	}

	/**
	 * 获取机构相应岗位下的用户信息
	 * 
	 * 没有被使用的方法
	 * 
	 * @param orgid
	 * @param jobid
	 * @return java.util.List<User>
	 * @throws ManagerException
	 */
	public List getUserList(String orgid, String jobid) throws ManagerException {
		List list = null;
//		try {
//			Parameter p = new Parameter();
//			p.setCommand(Parameter.COMMAND_GET);
//			p
//					.setObject("from User user where user.userId in ("
//							+ "select distinct ujo.id.userId from Userjoborg ujo where ujo.id.orgId = '"
//							+ orgid + "' and ujo.id.jobId = '" + jobid + "')");
//			list = (List) cb.execute(p);
//		} catch (ControlException e) {
//			throw new ManagerException(e.getMessage());
//		}

		return list;
	}

	/**
	 * 去掉hibernate后的方法---20080509
	 */
	public List getUserList(Organization org, Job job) throws ManagerException {
		List list = null;
		if(job != null){
			String getUserListSql = "select * from td_sm_user where user_id in ("
				+ "select user_id from td_sm_userjoborg where job_id='" + job.getJobId()
				+ "' and org_id='" + org.getOrgId() + "')";
			DBUtil db = new DBUtil();
			try {
				db.executeSelect(getUserListSql);
				list = getUsers(db);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
//		if (job != null) {
//			try {
//				Parameter p = new Parameter();
//				p.setCommand(Parameter.COMMAND_GET);
//				// 修改：实现同一机构下同一岗位的用户排序
//				// p
//				// .setObject("from User user where user.userId in ("
//				// + "select ujo.id.userId from Userjoborg ujo where
//				// ujo.id.jobId = '"
//				// + job.getJobId() + "' and ujo.id.orgId = '"
//				// + org.getOrgId() + "')");
//
//				p.setObject("from User user where user.userId in (select ujo.id.userId from Userjoborg ujo where "
//								+ "ujo.id.jobId = '"
//								+ job.getJobId()
//								+ "' and ujo.id.orgId = '"
//								+ org.getOrgId()
//								+ "')");
//
//				List users = (List) cb.execute(p);
//				list = new ArrayList();
//
//				// for (int i = 0; i < users.size(); i++) {
//				// Object objs[] = (Object[]) users.get(i);
//				// list.add(objs[0]);
//				// }
//				return users;
//
//				// 修改结束
//			} catch (ControlException e) {
//				throw new ManagerException(e.getMessage());
//			}
//		}
//		list = new ArrayList();
		return list;
	}

	// public List getUserList(String hsql) throws ManagerException {
	// List list = null;
	//
	// try {
	// Parameter p = new Parameter();
	// p.setCommand(Parameter.COMMAND_GET);
	// p.setObject(hsql);
	//		
	// list = (List) cb.execute(p);
	// } catch (ControlException e) {
	// throw new ManagerException(e.getMessage());
	// }
	//
	// return list;
	// }

	public boolean getUserSnList(String orgId, String jobId, int jobSn)
			throws ManagerException {
		boolean r = false;
		DBUtil db = new DBUtil();
		// DBUtil db1 = new DBUtil();
		// int jsn;
		// int usn;
		// int userId;
		String sql = "update TD_SM_USERJOBORG set  JOB_SN =" + jobSn
				+ " where  JOB_ID ='" + jobId + "'" + "and ORG_ID ='" + orgId
				+ "'";

		try {
			db.executeUpdate(sql);
			// String str ="select JOB_SN,SAME_JOB_USER_SN,user_id from
			// TD_SM_USERJOBORG " +
			// "where JOB_ID ='" + jobId + "'" + "and ORG_ID ='" + orgId +"'";
			// db1.executeSelect(str);
			// if (db1 != null && db1.size() > 0) {
			// jsn = db1.getInt(0,"JOB_SN");
			//				
			//				
			// for(int i=0;i<db1.size();i++){
			// usn = db1.getInt(i,"SAME_JOB_USER_SN");
			// String SN = String.valueOf(jsn)+String.valueOf(usn);
			// userId = db1.getInt(i,"user_id");
			// String sqlstr = "update TB_Employee set FD_EMPLOYEE_POSITION = "+
			// SN +""
			// + "where FD_Employee_ID=" + String.valueOf(userId) + "";
			// db.executeUpdate(sqlstr);
			//	
			// }
			// }
			r = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return r;

	}
	public void loadUsers(final UserCacheManager userCache) throws ManagerException
	{
		String sql = "select u.*,ou.org_id from td_sm_user u left join td_sm_orguser ou on u.user_id = ou.user_id";

		try {
			
			SQLExecutor.queryByNullRowHandler(new NullRowHandler<User>(){

				

				@Override
				public void handleRow(Record record) throws Exception {
					int userid = record.getInt("user_id");
					User user = new User();
                    getUser( user,record) ;

					String orgjob = FunctionDB.getUserorgjobinfos(userid);
					if(orgjob.endsWith(","))
					{
						orgjob = orgjob.substring(0, orgjob.length() - 1);
					}
					
					
					
//				try{
//					orgjob = dbUtil.getString(i, "org_job");
//				}catch(Exception e){
//					orgjob = "";
//				}
					user.setOrgName(orgjob);
					
					//System.out.println("orgId = " + dbUtil.getString(i, "org_id"));
					user.setMainOrg(record.getString( "org_id"));
					userCache.addUser(user);
				}
				
			}, sql);
		} catch (SQLException e) {
			throw new ManagerException(e);
		}

	}
	/**
	 * 去掉hibernate后的方法
	 * @throws SQLException 
	 */
	public List getUserList() throws ManagerException {
		List list = null;

//		try {
//			Parameter p = new Parameter();
//			p.setCommand(Parameter.COMMAND_GET);
//			p.setObject("from User");
//
//			list = (List) cb.execute(p);
//		} catch (ControlException e) {
//			throw new ManagerException(e.getMessage());
//		}
		String sql = "select u.*,ou.org_id from td_sm_user u left join td_sm_orguser ou on u.user_id = ou.user_id";
//		DBUtil db = new DBUtil();
//		try {
//			db.executeSelect(sql);
//			list = getUsers(db);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		
		try {
			list = SQLExecutor.queryListByRowHandler(new RowHandler<User>(){

				@Override
				public void handleRow(User user, Record record)
						throws Exception {
					int userid = record.getInt("user_id");
                    getUser( user,record) ;

					String orgjob = FunctionDB.getUserorgjobinfos(userid);
					if(orgjob.endsWith(","))
					{
						orgjob = orgjob.substring(0, orgjob.length() - 1);
					}
					
					
					
//				try{
//					orgjob = dbUtil.getString(i, "org_job");
//				}catch(Exception e){
//					orgjob = "";
//				}
					user.setOrgName(orgjob);
					
					//System.out.println("orgId = " + dbUtil.getString(i, "org_id"));
					user.setMainOrg(record.getString( "org_id"));
					
				}
				
			},User.class, sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * 无效的方法
	 */
	public List getAccreditList(String userId) throws ManagerException {
		List list =  new ArrayList();

//		if (userId != null) {
//			try {
//				Parameter p = new Parameter();
//				p.setCommand(Parameter.COMMAND_GET);
//				p
//						.setObject("from Accredit accredit where accredit.user.userId = '"
//								+ userId + "'");
//				list = (List) cb.execute(p);
//			} catch (ControlException e) {
//				throw new ManagerException(e.getMessage());
//			}
//		}

		return list;
	}

	/**
	 * 无效的方法
	 */
	public List getTempaccredit(String userId) throws ManagerException {
		List list = new ArrayList();
//
//		if (userId != null) {
//			try {
//				Parameter p = new Parameter();
//				p.setCommand(Parameter.COMMAND_GET);
//				p.setObject("from Tempaccredit temp where temp.id.userId = '"
//						+ userId + "'");
//				list = (List) cb.execute(p);
//			} catch (ControlException e) {
//				throw new ManagerException(e.getMessage());
//			}
//		}

		return list;
	}

	/**
	 * 去掉hibernate后的方法
	 */
	public boolean isUserExist(User user) throws ManagerException {
		boolean r = false;

//		try {
//			Parameter p = new Parameter();
//			p.setCommand(Parameter.COMMAND_GET);
//			p.setObject("from User user where user.userName='"
//					+ user.getUserName() + "'");
//			List list = (List) cb.execute(p);
//
//			if (list != null && list.size() > 0)
//				r = true;
//		} catch (ControlException e) {
//			throw new ManagerException(e.getMessage());
//		}
		String sql = "select count(1) from td_sm_user where user_name='" + 
			user.getUserName() + "'";
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(sql);
			if(db.getInt(0, 0) > 0){
				r = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return r;
	}

	/**
	 * 没有被使用的方法
	 */
	public boolean isUserroleExist(Userrole userrole) throws ManagerException {
		boolean r = false;

//		try {
//			Parameter p = new Parameter();
//			p.setCommand(Parameter.COMMAND_GET);
//			p.setObject("from Userrole ur where ur.id.userId='"
//					+ userrole.getUser().getUserId() + "' and ur.id.roleId='"
//					+ userrole.getRole().getRoleId() + "'");
//			List list = (List) cb.execute(p);
//
//			if (list != null && list.size() > 0)
//				r = true;
//		} catch (ControlException e) {
//			throw new ManagerException(e.getMessage());
//		}

		return r;
	}

	/**
	 * 没有被使用的方法
	 */
	public boolean isUserjoborgExist(Userjoborg userjoborg)
			throws ManagerException {
		boolean r = false;

//		try {
//			Parameter p = new Parameter();
//			p.setCommand(Parameter.COMMAND_GET);
//			p.setObject("from Userjoborg ujo where ujo.id.userId='"
//					+ userjoborg.getId().getUserId() + "' and ujo.id.jobId='"
//					+ userjoborg.getId().getJobId() + "' and ujo.id.orgId='"
//					+ userjoborg.getId().getOrgId());
//			List list = (List) cb.execute(p);
//
//			if (list != null && list.size() > 0)
//				r = true;
//		} catch (ControlException e) {
//			throw new ManagerException(e.getMessage());
//		}

		return r;
	}

	/**
	 * 去掉hibernate后的方法
	 */
	public User loadAssociatedSet(String userId, String associated)
			throws ManagerException {
		User returnUser = new User();

//		try {
//			DataControl cb = DataControl
//					.getInstance(DataControl.CONTROL_INSTANCE_DB);
//			Parameter par = new Parameter();
//			par.setCommand(Parameter.COMMAND_GET);
//			par.setObject("from User u left join fetch u." + associated
//					+ " where u.userId = '" + userId + "'");
//
//			List list = (List) cb.execute(par);
//			if (list != null && !list.isEmpty()) {
//				userRel = (User) list.get(0);
//			}
//		} catch (ControlException e) {
//			throw new ManagerException(e.getMessage());
//		}
		String sql = "select * from td_sm_userjoborg where user_id='" + userId + "'";
		DBUtil db = new DBUtil();
		
		Userjoborg userjoborg = null;
		Organization organization = null;
		User user = null;
		Job job = null;
		Set set = new HashSet();
		try {
			db.executeSelect(sql);
			for(int i = 0; i < db.size(); i++){
				user = new User();
				job = new Job();
				organization = new Organization();
				userjoborg = new Userjoborg();
				organization = new OrgManagerImpl().getOrgById(db.getString(i, "org_id"));
				user = getUserById(db.getString(i, "user_id"));
				job = new JobManagerImpl().getJobById(db.getString(i, "job_id"));
				userjoborg.setOrg(organization);
				userjoborg.setJob(job);
				userjoborg.setUser(user);
				set.add(userjoborg);
			}
			returnUser.setUserjoborgSet(set);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return returnUser;
	}


	// 王卓添加
	/**
	 * 没有被使用
	 */
	public boolean deleteUserrole(Role role, Group group)
			throws ManagerException {
		boolean r = false;

//		if (role != null) {
//			try {
//				Parameter p = new Parameter();
//				p.setCommand(Parameter.COMMAND_DELETE);
//				p
//						.setObject("from Userrole ur where ur.id.roleId = '"
//								+ role.getRoleId()
//								+ "' and ur.id.userId in(select u.id.userId from Usergroup u where u.id.groupId='"
//								+ group.getGroupId() + "')");
//				cb.execute(p);
//
//				r = true;
//				Event event = new EventImpl("",
//						ACLEventType.USER_ROLE_INFO_CHANGE);
//				super.change(event);
//			} catch (ControlException e) {
//				throw new ManagerException(e.getMessage());
//			}
//		}

		return r;
	}

	/**
	 * modify by ge.tao 2007-09-07 新增了 函数 getUserorginfos 用户管理 列表
	 */
	public ListInfo getUserList(String sql, int offset, int maxItem)
			throws ManagerException {
		DBUtil dbUtil = new DBUtil();
		DBUtil db = new DBUtil();
		String orgjob = "";// 用户所属的org job
		TransactionManager tm = new TransactionManager();
		try {
			tm.begin(TransactionManager.RW_TRANSACTION);
			ListInfo listInfo = null;
			if(offset < 0)
			{
				listInfo = SQLExecutor.queryListInfoByRowHandler(new RowHandler<UserJobs>(){

					@Override
					public void handleRow(UserJobs uj, Record record)
							throws Exception {
						int userid = record.getInt("user_id");
//						String str = "select getuserorgjobinfos('" + userid
//								+ "') as org from dual";
//						db.executeSelect(str);
//						if (db.size() > 0) {
//							orgjob = db.getString(0, "org");
//							if (orgjob.endsWith("、")) {
//								orgjob = orgjob.substring(0, orgjob.length() - 1);
//							}
//						}
//						String orgjob = "";
						String orgjob = FunctionDB.getUserorgjobinfos(userid);
						if(orgjob.endsWith("、"))
						{
							orgjob = orgjob.substring(0, orgjob.length() - 1);
						}
						
						
						uj.setUserId(new Integer(userid));
						uj.setUserName(record.getString( "USER_NAME"));
						uj.setUserRealname(record.getString( "USER_REALNAME"));
						uj.setUserMobiletel1(record.getString( "USER_MOBILETEL1"));
						uj.setUserType(record.getString("USER_TYPE"));
						uj.setUserEmail(record.getString( "USER_EMAIL"));
						uj.setUserSex(record.getString( "USER_SEX"));
						uj.setUser_isvalid(record.getString( "USER_ISVALID"));
						uj.setUser_regdate(record.getString("USER_REGDATE"));
						uj.setDredge_time(record.getString( "DREDGE_TIME"));
						uj.setIstaxmanager(new Integer(record.getInt( "ISTAXMANAGER")));
						uj.setPasswordUpdatetime(record.getTimestamp("password_updatetime"));
						uj.setUSER_IDCARD(record.getString("USER_IDCARD"));
						uj.setWorkNumber(record.getString("USER_WORKNUMBER"));
						uj.setPasswordDualedTime(record.getInt("Password_DualTime"));
						uj.setPasswordExpiredTime((Timestamp)getPasswordExpiredTime(uj.getPasswordUpdatetime(),uj.getPasswordDualedTime()));
//						try{
//							orgjob = dbUtil.getString(i, "org_job");
//						}catch(Exception e){
//							orgjob = "";
//						}
						uj.setOrgName(orgjob);
						uj.setJobName(orgjob);
						uj.setOrg_Name(orgjob);
						//System.out.println("orgId = " + dbUtil.getString(i, "org_id"));
						uj.setOrgId(record.getString( "org_id"));
						
					}
					
				}, UserJobs.class, sql,0,100);
//				listInfo = new ListInfo();
//				listInfo.setDatas(list);
//				listInfo.setTotalSize(list.size());
//				dbUtil.executeSelect(sql, offset, maxItem);
			}
			else
			{
				listInfo = SQLExecutor.queryListInfoByRowHandler(new RowHandler<UserJobs>(){

					@Override
					public void handleRow(UserJobs uj, Record record)
							throws Exception {
						int userid = record.getInt("user_id");
//						String str = "select getuserorgjobinfos('" + userid
//								+ "') as org from dual";
//						db.executeSelect(str);
//						if (db.size() > 0) {
//							orgjob = db.getString(0, "org");
//							if (orgjob.endsWith("、")) {
//								orgjob = orgjob.substring(0, orgjob.length() - 1);
//							}
//						}

						String orgjob = FunctionDB.getUserorgjobinfos(userid);
						if(orgjob.endsWith("、"))
						{
							orgjob = orgjob.substring(0, orgjob.length() - 1);
						}
						
						
						uj.setUserId(new Integer(userid));
						uj.setUserName(record.getString( "USER_NAME"));
						uj.setUserRealname(record.getString( "USER_REALNAME"));
						uj.setUserMobiletel1(record.getString( "USER_MOBILETEL1"));
						uj.setUserType(record.getString("USER_TYPE"));
						uj.setUserEmail(record.getString( "USER_EMAIL"));
						uj.setUserSex(record.getString( "USER_SEX"));
						uj.setUser_isvalid(record.getString( "USER_ISVALID"));
						uj.setUser_regdate(record.getString("USER_REGDATE"));
						uj.setDredge_time(record.getString( "DREDGE_TIME"));
						uj.setIstaxmanager(new Integer(record.getInt( "ISTAXMANAGER")));
						uj.setPasswordUpdatetime(record.getTimestamp("password_updatetime"));
						uj.setPasswordDualedTime(record.getInt("Password_DualTime"));
						uj.setUSER_IDCARD(record.getString("USER_IDCARD"));
						uj.setWorkNumber(record.getString("USER_WORKNUMBER"));
						uj.setPasswordExpiredTime((Timestamp)getPasswordExpiredTime(uj.getPasswordUpdatetime(),uj.getPasswordDualedTime()));
//						try{
//							orgjob = dbUtil.getString(i, "org_job");
//						}catch(Exception e){
//							orgjob = "";
//						}
						uj.setOrgName(orgjob);
						uj.setJobName(orgjob);
						uj.setOrg_Name(orgjob);
						//System.out.println("orgId = " + dbUtil.getString(i, "org_id"));
						uj.setOrgId(record.getString( "org_id"));
						
					}
					
				},UserJobs.class, sql, offset, maxItem);
//				dbUtil.executeSelect(sql, offset, maxItem);
			}
				
//			ListInfo listInfo = new ListInfo();
//			List list = new ArrayList();
//			UserJobs uj;
//			for (int i = 0; i < dbUtil.size(); i++) {
//
//				int userid = dbUtil.getInt(i, "user_id");
////				String str = "select getuserorgjobinfos('" + userid
////						+ "') as org from dual";
////				db.executeSelect(str);
////				if (db.size() > 0) {
////					orgjob = db.getString(0, "org");
////					if (orgjob.endsWith("、")) {
////						orgjob = orgjob.substring(0, orgjob.length() - 1);
////					}
////				}
//
//				orgjob = FunctionDB.getUserorgjobinfos(userid);
//				if(orgjob.endsWith("、"))
//				{
//					orgjob = orgjob.substring(0, orgjob.length() - 1);
//				}
//				
//				uj = new UserJobs();
//				uj.setUserId(new Integer(userid));
//				uj.setUserName(dbUtil.getString(i, "USER_NAME"));
//				uj.setUserRealname(dbUtil.getString(i, "USER_REALNAME"));
//				uj.setUserMobiletel1(dbUtil.getString(i, "USER_MOBILETEL1"));
//				uj.setUserType(dbUtil.getString(i, "USER_TYPE"));
//				uj.setUserEmail(dbUtil.getString(i, "USER_EMAIL"));
//				uj.setUserSex(dbUtil.getString(i, "USER_SEX"));
//				uj.setUser_isvalid(dbUtil.getString(i, "USER_ISVALID"));
//				uj.setUser_regdate(dbUtil.getString(i, "USER_REGDATE"));
//				uj.setDredge_time(dbUtil.getString(i, "DREDGE_TIME"));
//				uj.setIstaxmanager(new Integer(dbUtil.getInt(i, "ISTAXMANAGER")));
////				try{
////					orgjob = dbUtil.getString(i, "org_job");
////				}catch(Exception e){
////					orgjob = "";
////				}
//				uj.setOrgName(orgjob);
//				uj.setJobName(orgjob);
//				uj.setOrg_Name(orgjob);
//				//System.out.println("orgId = " + dbUtil.getString(i, "org_id"));
//				uj.setOrgId(dbUtil.getString(i, "org_id"));
//				list.add(uj);
//				
//			}
			
			tm.commit();
			return listInfo;
		} catch (Throwable e) {
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			throw new ManagerException(e.getMessage());

		}

	}

	/**
	 * modify by ge.tao 2007-09-07 新增了 函数 getUserorginfos 机构管理 列表
	 */
	public ListInfo getUserInfoList(String sql, int offset, int maxItem)
			throws ManagerException {
		DBUtil dbUtil = new DBUtil();
		DBUtil db = new DBUtil();
		String orgjob = "";// 用户所属的org job
		try {
			dbUtil.executeSelect(sql, offset, maxItem);
			ListInfo listInfo = new ListInfo();
			List list = new ArrayList();
			UserJobs uj;
			for (int i = 0; i < dbUtil.size(); i++) {

				int userid = dbUtil.getInt(i, "user_id");
//				String str = "select getUserorgjobinfos(" + userid
//						+ " || '') as org from dual";
//				db.executeSelect(str);
//				if (db.size() > 0) {
//					orgjob = db.getString(0, "org");
//					if (orgjob.endsWith("、")) {
//						orgjob = orgjob.substring(0, orgjob.length() - 1);
//					}
//				}
				
				orgjob = FunctionDB.getUserorgjobinfos(userid);
				if(orgjob.endsWith("、"))
				{
					orgjob = orgjob.substring(0, orgjob.length() - 1);
				}

				uj = new UserJobs();
				uj.setUserId(new Integer(userid));
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
				// if (dbUtil.getDate(i, "JOB_STARTTIME") != null)
				// uj.setJobStartTime(dbUtil.getDate(i, "JOB_STARTTIME"));
				// if (dbUtil.getString(i, "JOB_FETTLE") != null)
				// uj.setFettle(dbUtil.getString(i, "JOB_FETTLE"));
				list.add(uj);
			}
			listInfo.setDatas(list);
			listInfo.setTotalSize(dbUtil.getTotalSize());
			return listInfo;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ManagerException(e.getMessage());

		}

	}

	public List getUserList(String sql) throws ManagerException {
		DBUtil dbUtil = new DBUtil();
		try {
			dbUtil.executeSelect(sql);
			// ListInfo listInfo = new ListInfo();
			List list = new ArrayList();
			for (int i = 0; i < dbUtil.size(); i++) {

				int userid = dbUtil.getInt(i, "user_id");
				int isvalid = dbUtil.getInt(i, "USER_ISVALID");

				User user = new User();
				user.setUserId(new Integer(userid));
				user.setUserName(dbUtil.getString(i, "USER_NAME"));
				user.setUserRealname(dbUtil.getString(i, "USER_REALNAME"));
				user.setUserPassword(dbUtil.getString(i, " USER_PASSWORD"));
				user.setUserSex(dbUtil.getString(i, "USER_SEX"));
				user.setUserMobiletel1(dbUtil.getString(i, "USER_MOBILETEL1"));
				user.setUserMobiletel2(dbUtil.getString(i, "USER_MOBILETEL2"));
				user.setUserIsvalid(new Integer(isvalid));
				user.setUserEmail(dbUtil.getString(i, "USER_EMAIL"));
				user.setUserType(dbUtil.getString(i, "USER_TYPE"));

				list.add(user);
			}

			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ManagerException(e.getMessage());

		}
	}

	public List getUserjoborgList(String userId, String orgId)
			throws ManagerException {
		DBUtil dbUtil = new DBUtil();
		try {
			String sql = "select * from td_sm_userjoborg where " + "USER_ID="
					+ userId + " and ORG_ID='" + orgId + "'";
			dbUtil.executeSelect(sql);

			List list = new ArrayList();
			for (int i = 0; i < dbUtil.size(); i++) {

				int userid = dbUtil.getInt(i, "user_id");

				Userjoborg ujo = new Userjoborg();
				ujo.getId().setUserId(new Integer(userid));

				list.add(ujo);
			}

			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ManagerException(e.getMessage());

		}
	}

	/**
	 * 得离散用户列表
	 **/
	public List getDicList() throws ManagerException {		
		List list = new ArrayList();
		DBUtil db = new DBUtil();			
		String sql = "select t.* from td_sm_user t " +
					"where t.user_id not in " +
					"(select u.user_id from td_sm_userjoborg u) " +
					"order by t.user_realname";
		try
		{
			db.executeSelect(sql);
			list = this.getUsers(db);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * modify by ge.tao 2007-08-31 调入岗位时,如果在该机构下已有岗位, 保持SAME_JOB_USER_SN与原来一致
	 * 如果在该机构下无岗位, SAME_JOB_USER_SN是该机构下最大的 添加 ,boolean needevent参数设置是否需要事件处理
	 */
	public boolean storeUserjoborg(String userId, String jobId, String orgId,
			boolean needevent) throws ManagerException {
		boolean b = false;
		int samejobsn = 1;
		int jobsn = 1;
//		DBUtil db = new DBUtil();
		PreparedDBUtil pe = new PreparedDBUtil();
		PreparedDBUtil pe_ = new PreparedDBUtil();
		try {
			// DBUtil db1 = new DBUtil();
			DBUtil db2 = new DBUtil();
			// String str1 ="select * from td_sm_userjoborg where org_id ='"+
			// orgId +"' " +
			// "and job_id ='1' and user_id ="+ userId +"";
			// String str2 ="delete from td_sm_userjoborg where org_id ='"+
			// orgId +"' " +
			// "and job_id ='1' and user_id ="+ userId +"";
			// db.executeSelect(str1);
			// if(db.size()>0){
			// db1.executeDelete(str2);
			// }

			/* 初始化数据 TD_SM_USERJOBORG 里有job_sn 和same_job_user_sn 为null的数据 */
			// DBUtil initdb = new DBUtil();
			// String initData = "update TD_SM_USERJOBORG set job_sn=1 ,
			// same_job_user_sn=1 where same_job_user_sn is null ";
			// initdb.executeUpdate(initData);
			/* 机构下改用户原来就有岗位 新调入的岗位SAME_JOB_USER_SN与原来的保持一致 */
			DBUtil addorgjob = new DBUtil();
			String minSN = "select min(SAME_JOB_USER_SN) as minsn from TD_SM_USERJOBORG where user_id ="
					+ userId
					+ " and"
					+ " org_id ='"
					+ orgId
					+ "' and same_job_user_sn is not null ";
			addorgjob.executeSelect(minSN);

			/* 用户新调入机构 SAME_JOB_USER_SN为改机构下最大的 */
			DBUtil neworgjob = new DBUtil();
			String maxSN = " select max(same_job_user_sn) as maxsn from td_sm_userjoborg where "
					+ " org_id ='"
					+ orgId
					+ "' and same_job_user_sn is not null ";
			neworgjob.executeSelect(maxSN);

			if (addorgjob != null && addorgjob.size() > 0) {
				samejobsn = addorgjob.getInt(0, "minsn");
			} else {
				if (neworgjob != null && neworgjob.size() > 0) {
					samejobsn = neworgjob.getInt(0, "maxsn") + 1;
				}
			}

			/* 获取JOB_SN */
			String jobSN = "select JOB_SN from TD_SM_ORGJOB where job_id ='"
					+ jobId + "' and" + " org_id ='" + orgId + "'";
			db2.executeSelect(jobSN);
			if (db2 != null && db2.size() > 0) {
				jobsn = db2.getInt(0, "JOB_SN");
			}

			/* 插入三元关系表 */
//			StringBuffer sql = new StringBuffer();
//			sql.append("insert all when totalsize <= 0 then into TD_SM_USERJOBORG(USER_ID,JOB_ID,ORG_ID,SAME_JOB_USER_SN, ")
//					.append("JOB_SN,JOB_STARTTIME,JOB_FETTLE)values (").append(userId).append(",'").append(jobId).append("','")
//					.append(orgId).append("','").append(samejobsn)
//					.append("','").append(jobsn).append("',").append(
//			DBUtil.getDBAdapter().sysdate()).append(",1) select count(1) as totalsize from TD_SM_USERJOBORG where ")
//			.append("USER_ID='").append(userId).append("' and JOB_ID='").append(jobId).append("' and ")
//			.append("ORG_ID='").append(orgId).append("'");
			
			String sql = sqlUtil.getSQL("userManagerImpl_storeUserjoborg");
			pe.preparedSelect(sql);
			pe.setString(1, userId);
			pe.setString(2, jobId);
			pe.setString(3, orgId);
			pe.executePrepared();
			
			logger.warn(sql.toString());
			
			if(pe.getInt(0, 0)<=0)
			{
				String sql_ = sqlUtil.getSQL("userManagerImpl_storeUserjoborg_");
				pe_.preparedInsert(sql_);
				pe_.setString(1, userId);
				pe_.setString(2, jobId);
				pe_.setString(3, orgId);
				pe_.setInt(4, samejobsn);
				pe_.setInt(5, jobsn);
				pe_.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
				pe_.executePrepared();
				logger.warn(sql_.toString());
			}
			
			
			
//			db.executeInsert(sql.toString());

			b = true;
			if (needevent) {
				Event event = new EventImpl("",
						ACLEventType.USER_ROLE_INFO_CHANGE);
				super.change(event);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ManagerException(e.getMessage());
		}

		return b;

	}

	/**
	 * modify by ge.tao 2007-09-02
	 * sysmanager/user/userManager.do?method=getOrgList2 机构下的用户调入调出
	 * 
	 */
	public boolean storeUserjoborg(String userId, String jobId[], String orgId)
			throws ManagerException {
		boolean b = false;
		if (userId == null || userId.trim().length() <= 0)
			return b;
		int userSN = 1;
		int jobsn = 1;
		DBUtil batchdb = new DBUtil();
		try {
			
			for (int i = 0; (jobId != null) && (i < jobId.length); i++) {
				DBUtil db = new DBUtil();
				String existsql = "select *  from TD_SM_USERJOBORG where job_id ='"
						+ jobId[i]
						+ "' and"
						+ " org_id ='"
						+ orgId
						+ "' and user_id =" + userId + "";
				db.executeSelect(existsql);
				// 如果记录已有，不进行操作
				if (db.size() > 0) {
					continue;
				} else {
					DBUtil db1 = new DBUtil();
					DBUtil db2 = new DBUtil();

					String maxUser = "select max(SAME_JOB_USER_SN) as Sn from TD_SM_USERJOBORG where "
							+ " org_id ='" + orgId + "'";
					db1.executeSelect(maxUser);
					if (db1 != null && db1.size() > 0) {
						userSN = db1.getInt(0, "Sn") + 1;
					}

					String jobSN = "select JOB_SN from TD_SM_ORGJOB where job_id ='"
							+ jobId[i] + "' and" + " org_id ='" + orgId + "'";
					db2.executeSelect(jobSN);

					if (db2 != null && db2.size() > 0) {
						jobsn = db2.getInt(0, "JOB_SN");
					}

					String sql = "insert into TD_SM_USERJOBORG(USER_ID,JOB_ID,ORG_ID,SAME_JOB_USER_SN,JOB_SN,JOB_STARTTIME,JOB_FETTLE) "
							+ "values ("
							+ userId
							+ ","
							+ "'"
							+ jobId[i]
							+ "','"
							+ orgId
							+ "',"
							+ userSN
							+ ","
							+ jobsn
							+ ","
							+ DBUtil.getDBAdapter().to_date(new Date()) + ",1)";

					batchdb.addBatch(sql);
				}
			}
			batchdb.executeBatch();
			b = true;
			Event event = new EventImpl("", ACLEventType.USER_ROLE_INFO_CHANGE);
			super.change(event);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ManagerException(e.getMessage());
		} catch (Exception e) {

			e.printStackTrace();
			throw new ManagerException(e.getMessage());
		}finally{
			batchdb.resetBatch();
		}

		return b;

	}

	public boolean deleteUserjoborg(String userId, String jobId, String orgId)
			throws ManagerException {
//		boolean b = false;
//		DBUtil db = new DBUtil();
//		String sql = "delete from TD_SM_USERJOBORG where job_id ='" + jobId
//				+ "' and" + " org_id ='" + orgId + "' and user_id =" + userId
//				+ "";
//		try {
//			db.executeDelete(sql);
//			b = true;
//			Event event = new EventImpl("", ACLEventType.USER_ROLE_INFO_CHANGE);
//			super.change(event);
//		} catch (SQLException e) {
//			e.printStackTrace();
//			throw new ManagerException(e.getMessage());
//		}
		return deleteUserjoborg(userId, jobId, orgId, true);

	}
	
	public boolean deleteUserjoborg(String userId, String jobId, String orgId,boolean sendEvent) throws ManagerException {
		boolean b = false;
		DBUtil db = new DBUtil();
		String sql = "delete from TD_SM_USERJOBORG where job_id ='" + jobId
				+ "' and" + " org_id ='" + orgId + "' and user_id =" + userId
				+ "";
		try {
			db.executeDelete(sql);
			b = true;
			if(sendEvent){
				Event event = new EventImpl("", ACLEventType.USER_ROLE_INFO_CHANGE);
				super.change(event);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ManagerException(e.getMessage());
		}
		return b;
		
		}

	public boolean userResCopy(String userId, String[] userid)
			throws ManagerException {
		boolean b = false;
		DBUtil db = new DBUtil();
		DBUtil db1 = new DBUtil();
		TransactionManager tm = new TransactionManager();
		try {
			
			if (userid != null) {
				tm.begin();
				for (int i = 0; i < userid.length; i++) {
					String sql = "insert into td_sm_roleresop(op_id,res_id,res_name,restype_id,types,role_id) "
							+ " select b.op_id,b.res_id,b.res_name,b.restype_id,b.types,'"
							+ userid[i]
							+ "' from "
							+ " td_sm_roleresop b  where b.role_id ='"
							+ userId
							+ "' minus select "
							+ " c.op_id,c.res_id,c.res_name,c.restype_id,c.types,c.role_id from td_sm_roleresop c "
							+ " where c.role_id='" + userid[i] + "'";

					db.addBatch(sql);
					// 复制用户的角色给选定用户
					String sqlrole = "select role_id from td_sm_userrole where user_id ="
							+ userId;
					db1.executeSelect(sqlrole);
					if (db1.size() > 0) {
						for (int j = 0; j < db1.size(); j++) {
							String sqlselect = "select count(*) from td_sm_userrole where "
									+ " user_id="
									+ userid[i]
									+ " and role_id='"
									+ db1.getString(j, "role_id") + "'";
							String str = "insert into td_sm_userrole(user_id ,role_id) "
									+ " values("
									+ userid[i]
									+ ",'"
									+ db1.getString(j, "role_id") + "')";
							db1.executeSelect(sqlselect);
							if (db1.getInt(0, 0) == 0) {
								db1.executeInsert(str);
							}
						}
					}
				}
				db.executeBatch();
				tm.commit();
				b = true;
				Event event = new EventImpl("",
						ACLEventType.RESOURCE_ROLE_INFO_CHANGE);
				super.change(event);
			}

		} catch (Exception e) {
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			throw new ManagerException(e.getMessage());
		}finally{
			db.resetBatch();
		}
		return b;

	}

	public boolean storeAlotUserRole(String[] ids, String[] roleid)
			throws ManagerException {
		boolean b = false;
//		DBUtil db = new DBUtil();
		PreparedDBUtil preparedbutil = new PreparedDBUtil();
		PreparedDBUtil preparedbutil_ = new PreparedDBUtil();
		try {
			if (ids != null && roleid != null && ids.length > 0 && roleid.length > 0) {
				for (int i = 0; i < ids.length; i++)
				{
					int id = Integer.parseInt(ids[i]);
					for (int j = 0; j < roleid.length; j++) {
						
						String sql = sqlUtil.getSQL("userManagerImpl_storeAlotUserRole");
						preparedbutil.preparedSelect(sql);
						preparedbutil.setInt(1,  id);
						preparedbutil.setString(2,  roleid[j]);
						preparedbutil.executePrepared();
						
						if(preparedbutil.getInt(0, 0)<=0)
						{
							preparedbutil_.preparedInsert(sqlUtil.getSQL("userManagerImpl_storeAlotUserRole_"));
							preparedbutil_.setInt(1, id);
							preparedbutil_.setString(2, roleid[j]);
							preparedbutil_.executePrepared();
						}
//						String sqlins = "insert all when totalsize <= 0 then into td_sm_userrole(user_id,role_id) values("
//								+ id + ",'" + roleid[j] + "') select count(role_id ) totalsize from td_sm_userrole where user_id = "
//							+ id + " and role_id = '" + roleid[j] + "'";
//
//						db.addBatch(sqlins);
						
					}
				}
//				db.executeBatch();
				b = true;
				Event event = new EventImpl("",
						ACLEventType.USER_ROLE_INFO_CHANGE);
				super.change(event);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new ManagerException(e.getMessage());
		}finally{
//			db.resetBatch();
		
		}
		return b;

	}

	public boolean delAlotUserRole(String[] ids, String[] roleid)
			throws ManagerException {
		boolean b = false;
		PreparedDBUtil dbl = new PreparedDBUtil();
		try {
			if (ids != null && roleid != null && roleid.length > 0 && ids.length > 0) {
				String sql = "delete from td_sm_userrole where user_id = ? and role_id = ?";
				dbl.preparedDelete(sql);
				
				for (int i = 0; i < ids.length; i++)
				{
					int id = Integer.parseInt(ids[i]);
					for (int j = 0; j < roleid.length; j++) {
						

//						String sql = "delete from td_sm_userrole where user_id = "
//								+ id + " and role_id = '" + roleid[j] + "'";
						dbl.setInt(1, id);
						dbl.setString(2, roleid[j]);
						
						dbl.addPreparedBatch();
					}
				}
				dbl.executePreparedBatch();
				b = true;
				Event event = new EventImpl("",
						ACLEventType.USER_ROLE_INFO_CHANGE);
				super.change(event);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new ManagerException(e.getMessage());
		}finally{
			dbl.resetBatch();
		}
		return b;

	}

	public List getUserList(String[][] orgjobs) throws ManagerException {
		List list = new ArrayList();
		try {
			StringBuffer sql = new StringBuffer(
					"select distinct * from td_sm_user where user_id in (");
			boolean flag = false;
			for (int i = 0; orgjobs != null && i < orgjobs.length; i++) {
				if (flag)
					sql.append(" union select user_id from td_sm_userjoborg where org_id = '"
									+ orgjobs[i][0]
									+ "' and job_id = '"
									+ orgjobs[i][1] + "'");
				else {
					sql.append("select user_id from td_sm_userjoborg where org_id = '"
									+ orgjobs[i][0]
									+ "' and job_id = '"
									+ orgjobs[i][1] + "'");
					flag = true;

				}
			}
			if (flag)
				sql.append(")");
			else
				return new ArrayList();
			DBUtil dbUtil = new DBUtil();
			dbUtil.executeSelect(sql.toString());
			for (int i = 0; i < dbUtil.size(); i++) {

				int userid = dbUtil.getInt(i, "user_id");
				int isvalid = dbUtil.getInt(i, "USER_ISVALID");

				User user = new User();
				user.setUserId(new Integer(userid));
				user.setUserName(dbUtil.getString(i, "USER_NAME"));
				user.setUserRealname(dbUtil.getString(i, "USER_REALNAME"));
				user.setUserPassword(dbUtil.getString(i, " USER_PASSWORD"));
				user.setUserSex(dbUtil.getString(i, "USER_SEX"));
				user.setUserMobiletel1(dbUtil.getString(i, "USER_MOBILETEL1"));
				user.setUserMobiletel2(dbUtil.getString(i, "USER_MOBILETEL2"));
				user.setUserIsvalid(new Integer(isvalid));
				user.setUserEmail(dbUtil.getString(i, "USER_EMAIL"));
				user.setUserType(dbUtil.getString(i, "USER_TYPE"));
				user.setPasswordDualedTime(dbUtil.getInt(i, "Password_DualTime"));
				user.setPasswordUpdatetime(dbUtil.getTimestamp(i, "password_updatetime"));
				user.setPasswordExpiredTime((Timestamp)this.getPasswordExpiredTime(user.getPasswordUpdatetime(),user.getPasswordDualedTime()));
			
				list.add(user);
			}

		} catch (SQLException e) {
			throw new ManagerException(e.getMessage());
		}

		return list;
	}

	public boolean deleteUsergroup(String userId, String groupId)
			throws ManagerException {
		DBUtil db = new DBUtil();
		String sql = "delete from td_sm_usergroup where GROUP_ID =" + groupId
				+ " and USER_ID=" + userId + "";
		try {
			db.executeDelete(sql);
			Event event = new EventImpl("", ACLEventType.USER_GROUP_INFO_CHANGE);
			super.change(event);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;

	}

	public boolean storeUserjoborg(String userId, String jobId, String orgId,
			String jobuserSn, String jobSn, boolean needevent)
			throws ManagerException {
		boolean b = false;

		DBUtil db = new DBUtil();
		String sql = "insert into TD_SM_USERJOBORG(USER_ID,JOB_ID,ORG_ID,SAME_JOB_USER_SN,JOB_SN,JOB_STARTTIME,JOB_FETTLE) "
				+ "values ("
				+ userId
				+ ","
				+ "'"
				+ jobId
				+ "','"
				+ orgId
				+ "',"
				+ jobuserSn
				+ ","
				+ jobSn
				+ ","
				+ DBUtil.getDBAdapter().to_date(new Date()) + ",1)";
		try {
			db.executeInsert(sql);
			b = true;
			if (needevent) {
				Event event = new EventImpl("",
						ACLEventType.USER_ROLE_INFO_CHANGE);
				super.change(event);
			}
		} catch (SQLException e) {
			throw new ManagerException(e.getMessage());

		}

		return b;

	}

	/**
	 * 获取机构用户排序号，如果用户在机构下已经存在，则保持用户的排序号不变 如果不存在，则将本机构下最大的用户排序号加1返回
	 * 
	 * @param orgid
	 * @param userid
	 * @return
	 */
	public int getUserSN(String orgid, String userid) {
		try {
			String sqlsn = "select same_job_user_sn as sn from  td_sm_userjoborg"
					+ " where  org_id ='"
					+ orgid
					+ "' and user_id="
					+ userid
					+ " ";
			DBUtil dbutil = new DBUtil();
			dbutil.executeSelect(sqlsn);
			int samesn = 0;
			if (dbutil.size() > 0) {
				sqlsn = "select min(same_job_user_sn) as sn from  td_sm_userjoborg"
						+ " where  org_id ='"
						+ orgid
						+ "' and user_id="
						+ userid + " ";

				dbutil.executeSelect(sqlsn);
				samesn = dbutil.getInt(0, 0);
			} else {
				DBUtil dbutil1 = new DBUtil();
				sqlsn = "select max(same_job_user_sn) as sn from  td_sm_userjoborg"
						+ " where  org_id ='" + orgid + "'";
				dbutil1.executeSelect(sqlsn);
				if (dbutil1.size() > 0)
					samesn = dbutil1.getInt(0, 0) + 1;
				else
					samesn = 0;

			}
			return samesn;
		} catch (Exception e) {

		}
		return 0;
	}

	/**
	 * 保存多个用户在同一个机构下的多个岗位关系
	 */
	public boolean storeAlotUserJob(String[] ids, String[] jobid, String orgid)
			throws ManagerException {

		boolean b = false;

		try {

			DBUtil dbl = new DBUtil();

			if (ids != null && jobid != null) {
				for (int i = 0; i < ids.length; i++) {
					String id = ids[i];
					int samesn = this.getUserSN(orgid, id);
					for (int j = 0; j < jobid.length; j++) {
						String sql = "select * from td_sm_userjoborg where "
								+ "user_id = " + id + " and job_id = '"
								+ jobid[j] + "' and org_id ='" + orgid + "'";
						dbl.executeSelect(sql);
						if (dbl.size() == 0) {

							saveUser(id, jobid[j], orgid, samesn);
						}
					}

				}
				b = true;
				Event event = new EventImpl("", ACLEventType.USER_INFO_CHANGE);
				super.change(event);
			}

		} catch (Exception e) {
			throw new ManagerException(e.getMessage());
		}
		return b;
	}

	private void saveUser(String userId, String jobId, String orgId, int samesn) {
		try {
			String jobsnsql = "select c.job_sn from td_sm_orgjob c where c.org_id ='"
					+ orgId + "' and c.job_id='" + jobId + "'";
			DBUtil temp = new DBUtil();
			temp.executeSelect(jobsnsql);

			String jobsn = "1";
			if (temp.size() > 0)
				jobsn = temp.getInt(0, 0) + "";

			DBUtil db = new DBUtil();

			String sql = "insert into td_sm_userjoborg"
					+ " (user_id,job_id,org_id,JOB_SN,SAME_JOB_USER_SN,JOB_STARTTIME,JOB_FETTLE)"
					+ " values(" + userId + ",'" + jobId + "','" + orgId + "',"
					+ jobsn + "," + samesn + "," + DBUtil.getDBAdapter().to_date(new Date())
					+ ",1)";
			db.executeInsert(sql);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除多个用户在同一个机构下的多个岗位，
	 * 同时如果用户在机构下没有任何岗位时并且本机构是用户的主机构，需删除本机构和用户的主机构关系，并且重新设置用户的主机构
	 * 
	 */
	public boolean delAlotJobRole(String[] userids, String[] jobids, String orgId)
			throws ManagerException {
		boolean b = false;
		TransactionManager tm = new TransactionManager();
		PreparedDBUtil pd = new PreparedDBUtil();
		final PreparedDBUtil pdbatch = new PreparedDBUtil();
		
		Map<String, String> variablevalues = new HashMap<String, String>();
        variablevalues.put("date", DBUtil.getDBAdapter().to_date(new Date()));
        
		StringBuilder selectSql = new StringBuilder();
		selectSql.append("select a.user_id, a.JOB_ID, a.ORG_ID,a.JOB_STARTTIME, b.job_name, c.org_name ")
			.append("  from TD_SM_USERJOBORG a, td_sm_job b, td_sm_organization c ")
			.append(" where a.job_id = b.job_id ")
			.append("   and a.org_id = c.org_id and a.job_id = ? and ")
			.append(" a.org_id = ?  and a.user_id = ? ");
		StringBuilder delSql = new StringBuilder();
		delSql.append("delete from td_sm_userjoborg where user_id = ? ")
			.append(" and job_id = ?  and org_id = ? ");
		String Sql = "insert into TD_SM_USERJOBORG_HISTORY(USER_ID,JOB_ID,ORG_ID,JOB_STARTTIME,JOB_QUASHTIME,JOB_FETTLE,JOB_NAME,ORG_NAME) values(?,?,?,?,${date},0,?,?)";
		final String insertSql = sqlUtil.evaluateSQL("inserttdsmlog", Sql, variablevalues);
		try {
			tm.begin();
			
			//如果用户ID和岗位ID不为空
			if(userids != null && jobids != null)
			{
				
				//遍历用户ID
				for(String userid : userids)
				{
					//遍历岗位ID
					for(String jobid : jobids)
					{
						
						pd.preparedSelect(selectSql.toString());
						pd.setString(1, jobid);
						pd.setString(2, orgId);
						pd.setString(3, userid);
						pd.executePreparedWithRowHandler(new NullRowHandler()
						{
							@Override
							public void handleRow(Record origine)
									throws Exception {
								pdbatch.preparedInsert(insertSql);
								pdbatch.setInt(1, origine.getInt("user_id"));
								pdbatch.setString(2, origine.getString("JOB_ID"));
								pdbatch.setString(3, origine.getString("ORG_ID"));
								pdbatch.setDate(4, origine.getDate("JOB_STARTTIME"));
								//pdbatch.setDate(5,new Date());
								pdbatch.setString(5, origine.getString("JOB_NAME"));
								pdbatch.setString(6, origine.getString("ORG_NAME"));
								pdbatch.addPreparedBatch();
							}
							
						});
						pdbatch.preparedDelete(delSql.toString());
						pdbatch.setString(1, userid);
						//pdbatch.setInt(1,Integer.parseInt(userid));
						pdbatch.setString(2, jobid);
						pdbatch.setString(3, orgId);
						pdbatch.addPreparedBatch();
						//pdbatch.executePreparedBatch();
					}
					resetUserMainOrg(userid, orgId);
				}
				pdbatch.executePreparedBatch();
				tm.commit();
				b = true;
				Event event = new EventImpl("",ACLEventType.USER_ROLE_INFO_CHANGE);
				super.change(event);
			}
		} catch (Exception e) {
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				e1.printStackTrace();
			}
			throw new ManagerException(e.getMessage());
		}
		return b;
	}

	/**
	 * 更改用户的主机构，所有取消用户岗位的方法在处理完后需要调用本方法进行检测，并进行相应的主机构处理
	 * 
	 * @param id
	 */
	public void resetUserMainOrg(String userid, String oldmainorg) {
		String user_sql = "select count(user_id) from td_sm_userjoborg where user_id="
				+ userid + " and org_id='" + oldmainorg + "'";
		DBUtil dbUtil = new DBUtil();
		TransactionManager tm = new TransactionManager();
		try {
			dbUtil.executeSelect(user_sql);
			
			if (dbUtil.getInt(0, 0) == 0)// 用户在oldmainorg没有任职的情况处理,并且检测用户的主机构是否是oldmainorg，如果是则删除用户的主机构信息，并且重新为用户设置一个主机构
			{
				tm.begin();
				String mainsql = "select user_id,org_id from td_sm_orguser where user_id = "
						+ userid;
				dbUtil.executeSelect(mainsql);
				if (dbUtil.size() != 0) {
					String t_org_id = dbUtil.getString(0, "org_id");
					if (t_org_id.equals(oldmainorg)) {
						String slq = "delete from td_sm_orguser where user_id="
								+ userid + " and org_id='" + oldmainorg + "'";
						dbUtil.executeDelete(slq);
						user_sql = "select user_id,org_id from td_sm_userjoborg where user_id="
								+ userid + " order by job_starttime asc";
						dbUtil.executeSelect(user_sql);
						if (dbUtil.size() > 0) {
							user_sql = "insert into td_sm_orguser(user_id,org_id) values("
									+ userid
									+ ","
									+ dbUtil.getString(0, "org_id") + ")";
						}
					}
				}
				tm.commit();

			} else {

			}
			Event event = new EventImpl("", ACLEventType.USER_INFO_CHANGE);
			super.change(event);
		} catch (Exception e) {

		}

	}

	public List getmemberTypeList(String typeid) throws ManagerException {
		String sql = "select DICTDATA_VALUE,DICTDATA_NAME from TD_SM_DICTDATA where DICTTYPE_ID='"
				+ typeid + "'";
		List list = null;

		try {
			DBUtil db1 = new DBUtil();
			db1.executeSelect(sql);
			list = new ArrayList();
			for (int i = 0; i < db1.size(); i++) {
				Dictdata ddata = new Dictdata();
				ddata.setDictdataValue(db1.getString(i, "DICTDATA_VALUE"));
				ddata.setDictdataName(db1.getString(i, "DICTDATA_NAME"));
				list.add(ddata);

			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ManagerException(e.toString());
		}

		return list;
	}

	public void storeUserrole(String userId, String roleId)
			throws ManagerException {

		DBUtil db = new DBUtil();
		DBUtil dbl = new DBUtil();
		try {

			String sql = "select count(*) from td_sm_userrole where "
					+ " user_id = " + userId + " and role_id ='" + roleId + "'";

			dbl.executeSelect(sql);
			if (dbl.getInt(0, 0) == 0) {
				String sqlins = "insert into td_sm_userrole(user_id,role_id) "
						+ " values(" + userId + ",'" + roleId + "')";
				db.executeInsert(sqlins);
			}

			Event event = new EventImpl("",
					ACLEventType.RESOURCE_ROLE_INFO_CHANGE);
			super.change(event);

		} catch (Exception e) {
			e.printStackTrace();
			throw new ManagerException(e.getMessage());
		}

	}

	/**
	 * 保存用户 角色
	 * 
	 * @param userId
	 * @param roleIds
	 * @throws ManagerException
	 *             UserManagerImpl.java
	 * @author: ge.tao
	 */
	public void addUserrole(String userId, String[] roleIds)
			throws ManagerException {
		DBUtil db = new DBUtil();
		try {
			for (int i = 0; i < roleIds.length; i++) {
				String roleId = roleIds[i];
				String sql = "insert into td_sm_userrole(user_id,role_id) (select "
						+ userId
						+ " as user_id ,'"
						+ roleId
						+ "' as role_id from dual where "
						+ " not exists (select * from td_sm_userrole where user_id= "
						+ userId + " and " + " role_id = '" + roleId + "'))";
				db.addBatch(sql);
			}
			db.executeBatch();
			Event event = new EventImpl("", ACLEventType.USER_ROLE_INFO_CHANGE);
			super.change(event);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ManagerException(e.getMessage());
		}finally{
			db.resetBatch();
		}

	}
	
	/**
	 * 保存用户 角色
	 * @param userId
	 * @param roleIds
	 * @param currentUserId
	 * @throws ManagerException UserManagerImpl.java
	 * @author: ge.tao
	 */
	public void addUserrole(String userId, String[] roleIds,  String currentUserId)
			throws ManagerException {
		DBUtil db = new DBUtil();
		PreparedDBUtil pe = new PreparedDBUtil();
		PreparedDBUtil pe_ = new PreparedDBUtil();
		try {
			int userId_i = Integer.parseInt(userId);
//			StringBuffer sql = new StringBuffer();
			for (int i = 0; i < roleIds.length; i++) {
//				String roleId = roleIds[i];
//				String sql = "insert into td_sm_userrole(user_id,role_id,resop_origin_userid) (select "
//						+ userId
//						+ " as user_id ,'"
//						+ roleId
//						+ "' as role_id, '" 
//						+ currentUserId 
//						+ "' as resop_origin_userid from dual where "
//						+ " not exists (select * from td_sm_userrole where user_id= "
//						+ userId + " and " + " role_id = '" + roleId + "'))";
				//update 20080722 gao.tang 
//				sql.append("insert all when totalsize <= 0 then into td_sm_userrole(user_id,role_id,resop_origin_userid) ")
//					.append(" values('")
//					.append(userId).append("','").append(roleIds[i]).append("','")
//					.append(currentUserId).append("') ")
//					.append("select count(1) as totalsize from td_sm_userrole ")
//					.append("where user_id='").append(userId).append("' ")
//					.append(" and role_id='").append(roleIds[i]).append("'");
//				db.addBatch(sql.toString());
//				sql.setLength(0);
				
				String sql = sqlUtil.getSQL("userManagerImpl_addUserrole");
				pe.preparedSelect(sql);
				pe.setInt(1, userId_i);
				pe.setString(2, roleIds[i]);
				pe.executePrepared();
				
				if(pe.getInt(0, 0)<=0)
				{
					String sql_ = sqlUtil.getSQL("userManagerImpl_addUserrole_");
					pe_.preparedInsert(sql_);
					pe_.setInt(1, userId_i);
					pe_.setString(2, roleIds[i]);
					pe_.setString(3, currentUserId);
					pe_.executePrepared();
				}
			}
			
//			db.executeBatch();
			Event event = new EventImpl("", ACLEventType.USER_ROLE_INFO_CHANGE);
			super.change(event);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ManagerException(e.getMessage());
		}finally{
//			db.resetBatch();
			pe.resetPrepare();
			pe_.resetPrepare();
		}

	}

	/**
	 * 删除用户 角色
	 * 
	 * @param userId
	 * @param roleIds
	 * @throws ManagerException
	 *             UserManagerImpl.java
	 * @author: ge.tao
	 */
	public void deleteUserrole(String userId, String[] roleIds)
			throws ManagerException {

		DBUtil db = new DBUtil();
		try {
			StringBuffer sql = new StringBuffer(
					"delete td_sm_userrole where user_id = ").append(userId)
					.append(" and role_id in(");
			for (int i = 0; i < roleIds.length; i++) {
				if (i == 0)
					sql.append(roleIds[i]);
				else
					sql.append(",").append(roleIds[i]);
			}
			sql.append(")");
			db.executeDelete(sql.toString());
			sql.setLength(0);
			Event event = new EventImpl("", ACLEventType.USER_ROLE_INFO_CHANGE);
			super.change(event);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ManagerException(e.getMessage());
		}

	}
	
	/**
	 * 删除用户 角色
	 * @param userId
	 * @param roleIds
	 * @param roleTypes
	 * @throws ManagerException
	 *             UserManagerImpl.java
	 * @author: ge.tao
	 */
	public void deleteUserrole(String userId, String[] roleIds,String roleTypes)throws ManagerException {
		DBUtil db = new DBUtil();
		if(roleIds == null || roleIds.length == 0)
		{
			return ;
		}
		try {
//			StringBuffer sql = new StringBuffer(
//					"delete from td_sm_userrole where user_id = ").append(userId)
//					.append(" and role_id in(");
//			for (int i = 0; i < roleIds.length; i++) {
//				if (i == 0)
//					sql.append(roleIds[i]);
//				else
//					sql.append(",").append(roleIds[i]);
//			}
//			sql.append(")");
//			db.executeDelete(sql.toString());
//			sql.setLength(0);
			Map params = new HashMap();
			params.put("userId", userId);
			params.put("roleIds", roleIds);
			executor.deleteBean("deleteUserrole", params);
			//递归回收
//			ResManager resManager = new ResManagerImpl();
//			for(int i=0;i<roleIds.length;i++){
//				String roleId = roleIds[i];
//				resManager.reclaimUserRole(roleId, userId, roleTypes);
//			}			
			Event event = new EventImpl("", ACLEventType.USER_ROLE_INFO_CHANGE);
			super.change(event);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ManagerException(e.getMessage());
		}

	}

	public void creatorUser(User user, String orgId, String jobId)
			throws ManagerException {
		TransactionManager tm = new TransactionManager();
		if (user != null) {
			/**
			 * 把主机构set到user对象里面 ge.tao 2007-09-10
			 */
			user.setMainOrg(orgId);
			int samesn = 1;
			try {
				tm.begin();
				// 检查当前用户实例是否来自LDAP，因为LDAP中的用户ID(UID)是采用用户名标识其唯一性
//				if (user.getUserId() != null
//						&& user.getUserId().intValue() == -1) {
//					User oldUser = getUser("userName", user.getUserName());
//					if (oldUser == null)
//						user.setUserId(null);
//					else {
//
//						user.setUserId(oldUser.getUserId());
//						String oldpassword = StringUtil.replaceNull(oldUser
//								.getUserPassword());
//						String newpassword = StringUtil.replaceNull(user
//								.getUserPassword());
//						if (!oldpassword.equals(EncrpyPwd
//								.encodePassword(newpassword)))
//							user.setUserPassword(EncrpyPwd
//									.encodePassword(newpassword));
//
//					}
//
//				} else {
					String password = StringUtil.replaceNull(user
							.getUserPassword());
					user.setUserPassword(EncrpyPwd.encodePassword(password));
//				}

				// （1）保存到用户表
				String userId = null;
				// 判断user是否是新增的用户
//				if (user.getUserId() == null) {
					userId = SecurityDatabase.getUserManager().addUser(user,false);
//				} 
//				else {
//					// 修改用户直接跳出
//					SecurityDatabase.getUserManager().updateUser(user);
//					return;
//				}
				// （2）增加用户主要所属单位到td_sm_orguser表
				OrgManager orgmanager = SecurityDatabase.getOrgManager();
				// String userId = user.getUserId().toString();

				orgmanager.addMainOrgnazitionOfUser(userId, orgId);
				// （3）往td_sm_userjoborg表中插入记录
				String sqlsn = "select max(same_job_user_sn) as sn from  td_sm_userjoborg"
						+ " where  org_id ='" + orgId + "'";
				DBUtil dbutil = new DBUtil();
				dbutil.executeSelect(sqlsn);
				DBUtil db = new DBUtil();
				if (dbutil != null && dbutil.getInt(0, 0) > 0) {
					samesn = dbutil.getInt(0, "sn") + 1;
					String sql = "insert into td_sm_userjoborg"
							+ " (user_id,job_id,org_id,JOB_SN,SAME_JOB_USER_SN,JOB_STARTTIME,JOB_FETTLE)"
							+ " values(" + userId + ",'" + jobId + "','"
							+ orgId + "'," + 999 + "," + samesn + ","+DBUtil.getDBAdapter().to_date(new Date())+",1)";
					db.executeInsert(sql);
				} else {
					samesn = 1;
					String sql = "insert into td_sm_userjoborg"
							+ "(user_id,job_id,org_id,JOB_SN,SAME_JOB_USER_SN,JOB_STARTTIME,JOB_FETTLE)"
							+ " values(" + userId + ",'" + jobId + "','"
							+ orgId + "'," + 999 + "," + samesn + ","+DBUtil.getDBAdapter().to_date(new Date())+",1)";
					db.executeInsert(sql);
				}
				/**
				 * 把排序号set到user对象里面 ge.tao 2007-09-10
				 */
				user.setUserSn(new Integer(samesn));
				user.setUserId(new Integer(userId));
				AccessControl control = AccessControl.getAccessControl();
				String operContent="";        
		        String operSource = control.getMachinedID();//control.getRemoteAddr();
		        String openModle="用户管理";
		        String userName = control.getUserName();
		        LogManager logManager = SecurityDatabase.getLogManager(); 
				operContent=userName +" 新增了用户: " + user.getUserName() + "["+user.getUserRealname()+"]" ;
//		        logManager.log(control.getUserAccount(),operContent,openModle,operSource,"");
//				String operUser,String operOrg,String logModule, String visitorial,
//				String oper ,String remark1, int operType
				
				tm.commit();
				logManager.log(control.getUserAccount(), control.getChargeOrgId(),
						openModle, operSource, operContent,
						"", Log.INSERT_OPER_TYPE);
				Event event = new EventImpl("", ACLEventType.USER_INFO_ADD);
				super.change(event);
				
				
			} catch (Exception e) {
				
				throw new ManagerException(e.getMessage());
			}
			finally
			{
				tm.release();
			}

		}

	}

	/**
	 * 保存用户的排序
	 * 
	 * @param orgId
	 * @param jobId
	 * @param jobSn
	 * @param userId
	 * @return
	 * @throws Exception
	 *             UserManager.java
	 * @author: ge.tao
	 */
	public String storeAllUserSnJobOrg(String orgId, String jobId,
			String jobSn, String[] userId) throws Exception {
		DBUtil db = new DBUtil();
		try {
			UserManager userManager = SecurityDatabase.getUserManager();

			// 修改：需要完整用户对象信息
			// Organization org = new Organization();
			// org.setOrgId(orgId);
			Organization org = OrgCacheManager.getInstance().getOrganization(
					orgId);
			// Organization org = orgMgr.getOrg("org_id", orgId);
			// 修改结束
			Job job = new Job();
			job.setJobId(jobId);
			// 修改：需要完成用户对象信息
			// User user = new User();
			// user.setUserId(Integer.valueOf(userList[i]));

			// 在保存之前先删除该机构下改岗位的所有用户.然后再添加
			// userManager.deleteUserjoborg(job, org);
			
			// String str = "delete from td_sm_userjoborg where " + " org_id ='"
			// + orgId + "' and job_id='" + jobId + "'";
			// db.executeDelete(str);

			for (int i = 0; userId != null && i < userId.length; i++) {
				String sql = "update TD_SM_USERJOBORG set SAME_JOB_USER_SN='"
						+ (i + 1) + "',JOB_SN='"
						+ (Integer.parseInt(jobSn) + 1) + "',JOB_STARTTIME="
						+ DBUtil.getDBAdapter().to_date(new Date())
						+ ",JOB_FETTLE=1 where USER_ID=" + userId[i]
						+ "and JOB_ID='" + jobId + "' and ORG_ID='" + orgId
						+ "'";
				db.addBatch(sql);
				// storeUserjoborg(userId[i],jobId,orgId,(i + 1) +
				// "",(Integer.parseInt(jobSn) + 1) + "",false);
			}
			db.executeBatch();
			Event event = new EventImpl("", ACLEventType.USER_ROLE_INFO_CHANGE);
			super.change(event);

		} catch (Exception e) {
			return "fail";
		}finally{
			db.resetBatch();
		}

		return "success";
	}

	/**
	 * 保存机构下的用户排序
	 * 
	 * @param orgId
	 * @param userId
	 * @throws Exception
	 *             OrgJobAction.java
	 * @author: ge.tao
	 */
	public void storeOrgUserOrder(String orgId, String[] userId)
			throws Exception {
		if (userId == null || userId.length <= 0)
			return;
		PreparedDBUtil db = new PreparedDBUtil();
		String sql = ("update td_sm_userjoborg t set t.same_job_user_sn=? where t.user_id=? and org_id=?");
		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();
			db.preparedUpdate(sql.toString());
			for (int i = 0; i < userId.length; i++) {
				db.setInt(1, i);
				db.setInt(2, Integer.parseInt(userId[i]));
				db.setString(3, orgId);
				db.addPreparedBatch();
			}
			db.executePreparedBatch();
			tm.commit();
			//Event event = new EventImpl("", ACLEventType.USER_ROLE_INFO_CHANGE);
			//super.change(event);
		} catch (Exception e) {
			throw e;
		}finally{
			tm.release();
		}

	}

	/**
	 * 删除人员岗位和机构的关系，用户管理隶属机构中的调入 修改人:gao.tang,biaoping.yin
	 */
	public String deleteUJOAjax(String uid, String[] jobIds, String orgId)
			throws Exception {
		if (uid == null || uid.trim().length() <= 0)
			return "fail";
		DBUtil batchUtil = new DBUtil();
		TransactionManager tm = new TransactionManager();
		try {
			
			tm.begin();
			for (int i = 0; (jobIds != null) && (i < jobIds.length); i++) {
				// UserManager userManager = SecurityDatabase.getUserManager();
				DBUtil dbutile = new DBUtil();
				DBUtil db = new DBUtil();
				String sql1 = "select * from TD_SM_USERJOBORG where job_id ='"
						+ jobIds[i] + "' and" + " org_id ='" + orgId
						+ "' and user_id =" + uid + "";
				db.executeSelect(sql1);
				// -----如果该用户的主要单位为该机构，删除用户和该机构关系
				OrgManager orgManager = SecurityDatabase.getOrgManager();
				String strsql = "select count(*) from td_sm_orguser where org_id ='"
						+ orgId + "' and user_id =" + uid + "";
				dbutile.executeSelect(strsql);
				if (dbutile.getInt(0, 0) > 0) {
					orgManager.deleteMainOrgnazitionOfUser(uid);
				}
				// ------------------------------------------------------

				// 存数据到历史表TD_SM_USERJOBORG_HISTORY

				int userid = db.getInt(0, "user_id");
				String jid = db.getString(0, "JOB_ID");
				String oid = db.getString(0, "ORG_ID");
				Date starttime = db.getDate(0, "JOB_STARTTIME");
				String sql2 = "insert into TD_SM_USERJOBORG_HISTORY values("
						+ userid + ",'" + jid + "'," + "'" + oid + "',"
						+ DBUtil.getDBAdapter().to_date(new Date()) + "," + ""
						+ DBUtil.getDBAdapter().to_date(new Date()) + ",0)";

				// db.executeInsert(sql2);
				batchUtil.addBatch(sql2);
				String sql = "delete from TD_SM_USERJOBORG where job_id ='"
						+ jobIds[i] + "' and" + " org_id ='" + orgId
						+ "' and user_id =" + uid + "";
				batchUtil.addBatch(sql);
				// userManager.deleteUserjoborg(uid, jobIds[i], orgId);

			}
			batchUtil.executeBatch();
			tm.commit();
			Event event = new EventImpl("", ACLEventType.USER_ROLE_INFO_CHANGE);
			super.change(event);
		} catch (Exception e) {
			tm.rollback();
			e.printStackTrace();
			return "fail";
		}finally{
			batchUtil.resetBatch();
		}

		return "success";
	}

	public void storeUJOAjax_batch(String[] ids, String[] jobid, String orgid) {

		try {
			DBUtil db = new DBUtil();
			DBUtil dbl = new DBUtil();
			OrgManager orgManager = SecurityDatabase.getOrgManager();

			if (ids != null && jobid != null) {
				for (int i = 0; i < ids.length; i++) {

					String userid = ids[i];
					// 获得same_job_user_sn和JOB_SN,weida
					String org_id = orgid;
					String sjus = String.valueOf(getUserSN(org_id, userid));
					for (int j = 0; j < jobid.length; j++) {
						int id = Integer.parseInt(ids[i]);
						String job_id = jobid[j];
						String js = "";

						String jsSql = "select JOB_SN from TD_SM_ORGJOB where job_id ='"
								+ job_id
								+ "' and"
								+ " org_id ='"
								+ org_id
								+ "'";
						DBUtil jsDb = new DBUtil();
						jsDb.executeSelect(jsSql);
						if (jsDb != null && jsDb.size() > 0) {
							js = String.valueOf(jsDb.getInt(0, "JOB_SN"));
						}

						String sql = "select * from td_sm_userjoborg where "
								+ "user_id = " + id + " and job_id = '"
								+ jobid[j] + "' and org_id ='" + orgid + "'";
						dbl.executeSelect(sql);
						if (dbl.size() == 0) {

							// 插入same_job_user_sn和JOB_SN
							String sqlins = "insert into td_sm_userjoborg(user_id,job_id,org_id,SAME_JOB_USER_SN,JOB_SN,JOB_STARTTIME,JOB_FETTLE) "
									+ "values("
									+ id
									+ ",'"
									+ jobid[j]
									+ "','"
									+ orgid
									+ "','"
									+ sjus
									+ "','"
									+ js
									+ "',"
									+ DBUtil.getDBAdapter().to_date(new Date()) + ",1)";
							// String sqlins ="insert into
							// td_sm_userjoborg(user_id,job_id,org_id,JOB_STARTTIME,JOB_FETTLE)
							// " +
							// "values("+ id +",'"+ jobid[j] +"','"+ orgid
							// +"',"+ DBUtil.getDBAdapter().sysdate()+",1)";

							db.executeInsert(sqlins);
							// 批量用户加入机构，设置主机构
							DBUtil db1 = new DBUtil();
							String str = "select count(*)  from TD_SM_ORGUSER where "
									+ " user_id ="
									+ id
									+ " and org_id ='"
									+ orgid + "'";
							db1.executeSelect(str);
							if (db1.getInt(0, 0) == 0) {
								orgManager.addMainOrgnazitionOfUser(userid,
										orgid);
							}

						}
					}
				}

			}
			Event event = new EventImpl("", ACLEventType.USER_ROLE_INFO_CHANGE);
			super.change(event);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void deleteUJOAjax_batch(String[] ids, String[] jobid, String orgId)
			throws ManagerException {

		DBUtil dbl = new DBUtil();
		DBUtil db2 = new DBUtil();
		TransactionManager tm = new TransactionManager();
		try {
			if (ids != null && jobid != null) {
				tm.begin();
				for (int i = 0; i < ids.length; i++)
					for (int j = 0; j < jobid.length; j++) {
						int id = Integer.parseInt(ids[i]);
						String sqlselect = "select * from TD_SM_USERJOBORG where job_id ='"
								+ jobid[j]
								+ "' and"
								+ " org_id ='"
								+ orgId
								+ "' and user_id =" + id + "";
						String sql = "delete from td_sm_userjoborg where user_id = "
								+ id
								+ " and job_id = '"
								+ jobid[j]
								+ "' and org_id ='" + orgId + "'";
						db2.executeSelect(sqlselect);
						for (int k = 0; k < db2.size(); k++) {
							int userid = db2.getInt(k, "user_id");
							String jid = db2.getString(k, "JOB_ID");
							String oid = db2.getString(k, "ORG_ID");
							Date starttime = db2.getDate(k, "JOB_STARTTIME");
							String sql2 = "insert into TD_SM_USERJOBORG_HISTORY values("
									+ userid
									+ ",'"
									+ jid
									+ "',"
									+ "'"
									+ oid
									+ "',"
									+ DBUtil.getDBDate(starttime)
									+ ","
									+ "" + DBUtil.getDBAdapter().to_date(new Date()) + ",0)";

							db2.executeInsert(sql2);
						}

						dbl.executeDelete(sql);
					}
				tm.commit();

			}
			Event event = new EventImpl("", ACLEventType.USER_ROLE_INFO_CHANGE);
			super.change(event);
		} catch (Exception e) {
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}

	}

	public String deleteUserOrgJob(Integer uid, String orgId, String[] jobid) {

		// System.out.println("uid..........."+uid);
		// System.out.println("orgId............"+orgId);
		TransactionManager tm = new TransactionManager();
		StringBuffer sql1 = new StringBuffer();
		StringBuffer sql2 = new StringBuffer();
		DBUtil db = new DBUtil();
		DBUtil insert_db = new DBUtil();
		try {
			
			tm.begin();
			for (int i = 0; (jobid != null) && (i < jobid.length); i++) {
				
				UserManager userManager = SecurityDatabase.getUserManager();
//				String sql = "delete from TD_SM_USERJOBORG where job_id ='"
//						+ jobid[i] + "' and" + " org_id ='" + orgId
//						+ "' and user_id =" + uid + "";
//				String sql1 = "select * from TD_SM_USERJOBORG where job_id ='"
//						+ jobid[i] + "' and" + " org_id ='" + orgId
//						+ "' and user_id =" + uid + "";
				sql1.append("SELECT a.*, b.job_name as jobname, o.remark5 as remark5 ")
					.append("FROM td_sm_userjoborg a LEFT JOIN td_sm_job b ON a.job_id = b.job_id ")
					.append("LEFT JOIN td_sm_organization o ON a.org_id = o.org_id where a.job_id <> '1' and a.job_id = '")
					.append(jobid[i]).append("' and a.org_id = '").append(orgId).append("' and a.user_id =")
					.append(uid);
				db.executeSelect(sql1.toString());
				// db.executeDelete(sql);
				String userId = uid.toString();
				userManager.deleteUserjoborg(userId, jobid[i], orgId,false);
				sql1.setLength(0);
				// 存数据到历史表TD_SM_USERJOBORG_HISTORY
				for (int j = 0; j < db.size(); j++) {
					int userid = db.getInt(j, "user_id");
					String jid = db.getString(j, "JOB_ID");
					String oid = db.getString(j, "ORG_ID");
					Date starttime = db.getDate(j, "JOB_STARTTIME");
					String jobName = db.getString(j, "jobname");
					String orgName = db.getString(j, "remark5");

					sql2.append("insert into TD_SM_USERJOBORG_HISTORY(USER_ID,JOB_ID,job_name,org_id,")
						.append("org_name,JOB_STARTTIME,JOB_QUASHTIME,JOB_FETTLE) values(")
						.append(userid).append(",'").append(jid).append("','").append(jobName).append("','")
						.append(oid).append("','").append(orgName).append("',").append(DBUtil.getDBDate(starttime))
						.append(",").append(DBUtil.getDBAdapter().to_date(new Date())).append(",0)");
					// System.out.println(sql2);
					insert_db.executeInsert(sql2.toString());
					sql2.setLength(0);
				}
			}
			tm.commit();
			Event event = new EventImpl("", ACLEventType.USER_ROLE_INFO_CHANGE);
			super.change(event);
		} catch (Exception e) {
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			return "fail";
		}finally{
			sql1 = null;
			sql2 = null;
		}
		return "success";
	}

	/**
	 * 存储人员岗位和机构的关系，用户管理隶属机构中的调入,如果用户没有设置主机构， 同时设置主机构
	 */
	public String storeUJOAjax(String uid, String[] jobIds, String orgId) {
		if (uid == null || uid.trim().length() <= 0)
			return "fail";
		UserManager userManager;
		try {
			userManager = SecurityDatabase.getUserManager();
			OrgManager orgManager = SecurityDatabase.getOrgManager();
			try {
				userManager.storeUserjoborg(uid, jobIds, orgId);

				DBUtil db1 = new DBUtil();
				String str = "select count(*)  from TD_SM_ORGUSER where "
						+ " user_id =" + uid;
				// +" and org_id ='"+ orgId +"'";

				db1.executeSelect(str);

				if (db1.getInt(0, 0) == 0) {
					orgManager.addMainOrgnazitionOfUser(uid, orgId);
				}
				// 执行userManager.storeUserjoborg(uid, jobIds,
				// orgId);方法时已经发出用户角色变化的事件,此处不需要再发
				// Event event = new EventImpl("",
				// ACLEventType.USER_INFO_CHANGE);
				// super.change(event);
			} catch (ManagerException e) {

				e.printStackTrace();
			}
		} catch (Exception e) {

			e.printStackTrace();
		}

		return "success";
	}

	/**
	 * 隶属岗位－－保存用户岗位机构的关系 store and delete userOrgJob by ajax the reference page
	 * is refresh auto (hongyu.deng)
	 * 
	 * @param uid
	 * @param orgId
	 * @param jobid
	 * @return
	 */
	public String storeUserOrgJob(Integer uid, String orgId, String[] jobid) {

		// System.out.println("uid..........."+uid);
		// System.out.println("orgId............"+orgId);

		try {

			for (int i = 0; (jobid != null) && (i < jobid.length); i++) {
				DBUtil db = new DBUtil();
				String sql = "select *  from TD_SM_USERJOBORG where job_id ='"
						+ jobid[i] + "' and" + " org_id ='" + orgId
						+ "' and user_id =" + uid + "";
				db.executeSelect(sql);
				// 如果记录已有，不进行操作
				if (db.size() > 0) {
					continue;
				} else {
					String userId = uid.toString();
					UserManager userManager = SecurityDatabase.getUserManager();
					userManager.storeUserjoborg(userId, jobid[i], orgId, false);
					// String sql1 ="insert into TD_SM_USERJOBORG values ("+ uid
					// +"," +
					// "'"+ jobid[i] +"','"+ orgId +"',0,0," +
					// DBUtil.getDBAdapter().sysdate()+ ",1)";
					//
					// //System.out.println(sql1);
					// db.executeInsert(sql1);
				}
			}
			Event event = new EventImpl("", ACLEventType.USER_ROLE_INFO_CHANGE);
			super.change(event);
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}

		return "success";

	}

	/**
	 * 批量用户加入机构
	 * 
	 * @param ids
	 * @param jobid
	 * @param orgid
	 * @throws ManagerException
	 * @throws SPIException
	 */
	public void storeAlotUserOrg(String[] ids, String[] jobids, String orgid)
			throws ManagerException, SPIException {
		// System.out.println("...................."+orgid);

		DBUtil db = new DBUtil();
		DBUtil dbl = new DBUtil();

		try {
			if (ids != null && jobids != null) {
				for (int i = 0; i < ids.length; i++) {
					String userid = String.valueOf(ids[i]);
					String org_id = orgid;
					String sjus = String.valueOf(getUserSN(org_id, userid));
					for (int j = 0; j < jobids.length; j++) {
						int id = Integer.parseInt(ids[i]);

						// 获得same_job_user_sn和JOB_SN,weida

						String job_id = jobids[j];

						String js = "";
						String jsSql = "select JOB_SN from TD_SM_ORGJOB where job_id ='"
								+ job_id
								+ "' and"
								+ " org_id ='"
								+ org_id
								+ "'";
						DBUtil jsDb = new DBUtil();
						jsDb.executeSelect(jsSql);
						if (jsDb != null && jsDb.size() > 0) {
							js = String.valueOf(jsDb.getInt(0, "JOB_SN"));
						}

						String sql = "select * from td_sm_userjoborg where "
								+ "user_id = " + id + " and job_id = '"
								+ jobids[j] + "' and org_id ='" + orgid + "'";
						dbl.executeSelect(sql);
						if (dbl.size() == 0) {

							String sqlins = "insert into td_sm_userjoborg(user_id,job_id,org_id,SAME_JOB_USER_SN,JOB_SN,JOB_STARTTIME,JOB_FETTLE) "
									+ "values("
									+ id
									+ ",'"
									+ jobids[j]
									+ "','"
									+ orgid
									+ "','"
									+ sjus
									+ "','"
									+ js
									+ "',"
									+ DBUtil.getDBAdapter().to_date(new Date()) + ",1)";

							db.executeInsert(sqlins);
						}
					}

				}
			}
			Event event = new EventImpl("", ACLEventType.USER_ROLE_INFO_CHANGE);
			super.change(event);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 批量用户移出机构
	 * 
	 * @param ids
	 * @param jobid
	 * @param orgId
	 * @throws ManagerException
	 */
	public void delAlotUserOrg(String[] ids, String[] jobids, String orgId)
			throws ManagerException {

		DBUtil dbl = new DBUtil();
		DBUtil db2 = new DBUtil();
		TransactionManager tm = new TransactionManager();
		try {
			if (ids != null && jobids != null) {
				tm.begin();
				for (int i = 0; i < ids.length; i++)
					for (int j = 0; j < jobids.length; j++) {
						int id = Integer.parseInt(ids[i]);
						String sqlselect = "select * from TD_SM_USERJOBORG where job_id ='"
								+ jobids[j]
								+ "' and"
								+ " org_id ='"
								+ orgId
								+ "' and user_id =" + id + "";
						String sql = "delete from td_sm_userjoborg where user_id = "
								+ id
								+ " and job_id = '"
								+ jobids[j]
								+ "' and org_id ='" + orgId + "'";
						db2.executeSelect(sqlselect);
						for (int k = 0; k < db2.size(); k++) {
							int userid = db2.getInt(k, "user_id");
							String jid = db2.getString(k, "JOB_ID");
							String oid = db2.getString(k, "ORG_ID");
							Date starttime = db2.getDate(k, "JOB_STARTTIME");
							String sql2 = "insert into TD_SM_USERJOBORG_HISTORY values("
									+ userid
									+ ",'"
									+ jid
									+ "',"
									+ "'"
									+ oid
									+ "',"
									+ DBUtil.getDBDate(starttime)
									+ ","
									+ "" + DBUtil.getDBAdapter().to_date(new Date()) + ",0)";

							db2.executeInsert(sql2);
						}

						dbl.executeDelete(sql);
					}
				tm.commit();

			}

			Event event = new EventImpl("", ACLEventType.USER_ROLE_INFO_CHANGE);
			super.change(event);

		} catch (Exception e) {
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}

	}

	public String addUser(User user) throws ManagerException {
		return addUser(user,true);
	}
	public String addUser(User user,boolean isEvent) throws ManagerException {
//		DBUtil db = new DBUtil();
		PreparedDBUtil preparedDBUtil = new PreparedDBUtil();
		TransactionManager tm = new TransactionManager();
		String userId = null;
		if (user != null) {
			StringBuffer hsql = new StringBuffer();
			try {
				tm.begin();
				userId = String.valueOf(preparedDBUtil.getNextPrimaryKey("td_sm_user"));
				hsql.append("insert into TD_SM_USER(USER_ID,USER_SN, USER_NAME, USER_PASSWORD, ")
					.append("USER_REALNAME, USER_PINYIN, USER_SEX, USER_HOMETEL, USER_WORKTEL, ")
					.append("USER_WORKNUMBER, USER_MOBILETEL1, USER_MOBILETEL2, USER_FAX, ")
					.append("USER_OICQ, USER_BIRTHDAY, USER_EMAIL, USER_ADDRESS, USER_POSTALCODE, ")
					.append("USER_IDCARD, USER_ISVALID, USER_REGDATE, USER_LOGINCOUNT, USER_TYPE, ")
					.append("REMARK1, REMARK2, REMARK3, REMARK4, REMARK5, PAST_TIME, DREDGE_TIME, ISTAXMANAGER, WORKLENGTH, POLITICS,password_updatetime,Password_DualTime) ")
					.append(" values(?,?,?,?,")
					.append("?,?,?,?,?,")
					.append("?,?,?,?,")
					.append("?,?,?,?,?,")
					.append("?,?,?,?,?,")
					.append("?,?,?,?,?,?,?,?,?,?,?,?)");
				preparedDBUtil.preparedInsert(hsql.toString());
				
				preparedDBUtil.setString(1, userId);
				preparedDBUtil.setInt(2, user.getUserSn().intValue());
				preparedDBUtil.setString(3, user.getUserName());
				preparedDBUtil.setString(4, user.getUserPassword());
				preparedDBUtil.setString(5, user.getUserRealname());
				preparedDBUtil.setString(6, user.getUserPinyin() == null ? "" : user.getUserPinyin());
				preparedDBUtil.setString(7, user.getUserSex());
				preparedDBUtil.setString(8, user.getUserHometel() == null ? "" : user.getUserHometel());
				preparedDBUtil.setString(9, user.getUserWorktel() == null ? "" : user.getUserWorktel());
				preparedDBUtil.setString(10, user.getUserWorknumber() == null ? "" : user.getUserWorknumber());
				preparedDBUtil.setString(11, user.getUserMobiletel1() == null ? "" : user.getUserMobiletel1());
				preparedDBUtil.setString(12, user.getUserMobiletel2() == null ? "" : user.getUserMobiletel2());
				preparedDBUtil.setString(13, user.getUserFax() == null ? "" : user.getUserFax());
				preparedDBUtil.setString(14, user.getUserOicq() == null ? "" : user.getUserOicq());
				if(user.getUserBirthday()!=null){
					preparedDBUtil.setTimestamp(15, new Timestamp(user.getUserBirthday().getTime()));
				}else{
					preparedDBUtil.setNull(15, java.sql.Types.TIMESTAMP);
				}
				preparedDBUtil.setString(16, user.getUserEmail() == null ? "" : user.getUserEmail());
				preparedDBUtil.setString(17, user.getUserAddress() == null ? "" : user.getUserAddress());
				preparedDBUtil.setString(18, user.getUserPostalcode() == null ? "" : user.getUserPostalcode());
				preparedDBUtil.setString(19, user.getUserIdcard() == null ? "" : user.getUserIdcard());
				preparedDBUtil.setInt(20, user.getUserIsvalid().intValue());
				if(user.getUserRegdate() != null){
					preparedDBUtil.setTimestamp(21, new Timestamp(user.getUserRegdate().getTime()));
				}else{
					preparedDBUtil.setNull(21, java.sql.Types.TIMESTAMP);
				}
				preparedDBUtil.setInt(22, user.getUserLogincount().intValue());
				preparedDBUtil.setString(23, user.getUserType() == null ? "" : user.getUserType());
				preparedDBUtil.setString(24, user.getRemark1() == null ? "" : user.getRemark1());
				preparedDBUtil.setString(25, user.getRemark2() == null ? "" : user.getRemark2());
				preparedDBUtil.setString(26, user.getRemark3() == null ? "" : user.getRemark3());
				preparedDBUtil.setString(27, user.getRemark4() == null ? "" : user.getRemark4());
				preparedDBUtil.setString(28, user.getRemark5() == null ? "" : user.getRemark5());
				if(user.getPast_Time() != null){
					preparedDBUtil.setTimestamp(29, new Timestamp(StringUtil.stringToDate(user.getPast_Time()).getTime()));
				}else{
					preparedDBUtil.setNull(29, java.sql.Types.TIMESTAMP);
				}
//				preparedDBUtil.setString(29, user.getPast_Time() == null ? "" : user.getPast_Time());
				preparedDBUtil.setString(30, user.getDredgeTime() == null ? "" : user.getDredgeTime());
				preparedDBUtil.setInt(31, user.getIstaxmanager());
				preparedDBUtil.setString(32, user.getWorklength() == null ? "" : user.getWorklength());
				preparedDBUtil.setString(33, user.getPolitics() == null ? "" : user.getPolitics());
				preparedDBUtil.setTimestamp(34, new Timestamp(new Date().getTime()));
				preparedDBUtil.setInt(35, user.getPasswordDualedTime());
				preparedDBUtil.executePrepared();
				
				
//				hsql.append("insert into TD_SM_USER(USER_SN, USER_NAME, USER_PASSWORD, ")
//					.append("USER_REALNAME, USER_PINYIN, USER_SEX, USER_HOMETEL, USER_WORKTEL, ")
//					.append("USER_WORKNUMBER, USER_MOBILETEL1, USER_MOBILETEL2, USER_FAX, ")
//					.append("USER_OICQ, USER_BIRTHDAY, USER_EMAIL, USER_ADDRESS, USER_POSTALCODE, ")
//					.append("USER_IDCARD, USER_ISVALID, USER_REGDATE, USER_LOGINCOUNT, USER_TYPE, ")
//					.append("REMARK1, REMARK2, REMARK3, REMARK4, REMARK5, PAST_TIME, DREDGE_TIME, ISTAXMANAGER, WORKLENGTH, POLITICS) ")
//					.append("values('")
//					.append(user.getUserSn())
//					.append("', '")
//					.append(user.getUserName())
//					.append("', '")
//					.append(user.getUserPassword())
//					.append("', '")
//					.append(user.getUserRealname())
//					.append("', '")
//					.append(user.getUserPinyin() == null ? "" : user.getUserPinyin())
//					.append("', '")
//					.append(user.getUserSex())
//					.append("', '")
//					.append(user.getUserHometel() == null ? "" : user.getUserHometel())
//					.append("', '")
//					.append(user.getUserWorktel() == null ? "" : user.getUserWorktel())
//					.append("', '")
//					.append(user.getUserWorknumber() == null ? "" : user.getUserWorknumber())
//					.append("', '")
//					.append(user.getUserMobiletel1() == null ? "" : user.getUserMobiletel1())
//					.append("', '")
//					.append(user.getUserMobiletel2() == null ? "" : user.getUserMobiletel2())
//					.append("', '")
//					.append(user.getUserFax() == null ? "" : user.getUserFax())
//					.append("', '")
//					.append(user.getUserOicq() == null ? "" : user.getUserOicq())
//					.append("', ")
//					.append(String.valueOf(user.getUserBirthday()) == "null" ? "''"
//										: DBUtil.getDBDate(user.getUserBirthday().toString()))
//					.append(", '")
//					.append(user.getUserEmail() == null ? "" : user.getUserEmail())
//					.append("', '")
//					.append(user.getUserAddress() == null ? "" : user	.getUserAddress())
//					.append("', '")
//					.append(user.getUserPostalcode() == null ? "" : user.getUserPostalcode())
//					.append("', '")
//					.append(user.getUserIdcard() == null ? "" : user.getUserIdcard())
//					.append("', '")
//					.append(user.getUserIsvalid())
//					.append("', ")
//					.append(String.valueOf(user.getUserRegdate()) == "null" ? "''": DBUtil.getDBDate(user.getUserRegdate().toString()))
//					.append(", '").append(user.getUserLogincount()).append("', '")
//					.append(user.getUserType() == null ? "" : user.getUserType()).append("', '")
//					.append(user.getRemark1() == null ? "" : user.getRemark1()).append("', '")
//					.append(user.getRemark2() == null ? "" : user.getRemark2()).append("', '")
//					.append(user.getRemark3() == null ? "" : user.getRemark3()).append("', '")
//					.append(user.getRemark4() == null ? "" : user.getRemark4()).append("', '")
//					.append(user.getRemark5() == null ? "" : user.getRemark5()).append("', '")
//					.append(user.getPast_Time() == null ? "" : user.getPast_Time()).append("', '")
//					.append(user.getDredgeTime() == null ? "" : user.getDredgeTime()).append("', '")
//					.append(user.getIstaxmanager()).append("', '")
//					.append(user.getWorklength() == null ? "" : user.getWorklength()).append("', '")
//					.append(user.getPolitics() == null ? "" : user.getPolitics()).append("')");
//				Object ob = db.executeInsert(hsql.toString());
				// 得到主键
//				userId = String.valueOf(ob);
				
				tm.commit();
				if(isEvent){
					Event event = new EventImpl(userId, ACLEventType.USER_INFO_ADD);
					super.change(event);
				}
			} catch (SQLException e1) {
				
				throw new ManagerException(e1.getMessage());
			} catch (RollbackException e) {
				throw new ManagerException(e.getMessage());
			} catch (Exception e) {
				throw new ManagerException(e.getMessage());
			}
			finally
			{
				tm.release();
			}

		}
		return userId;
	}

	public boolean updateUser(User user) throws ManagerException {
		boolean state = false;
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd"); 
		java.util.Date currentTime = new java.util.Date(); 
		String riqi = formatter.format(currentTime);
		java.util.Date UserRegdate = new java.util.Date();
		
		String reg = user.getUserRegdate() == null?formatter.format(UserRegdate):formatter.format(user.getUserRegdate());
		if (user != null) {
			try {
				// 检查当前用户实例是否来自LDAP，因为LDAP中的用户ID(UID)是采用用户名标识其唯一性
//				if (user.getUserId() != null
//						&& user.getUserId().intValue() == -1) {
//					User oldUser = getUser("userName", user.getUserName());
//					if (oldUser == null)
//						user.setUserId(null);
//					else {
//
//						user.setUserId(oldUser.getUserId());
//						String oldpassword = StringUtil.replaceNull(oldUser
//								.getUserPassword());
//						String newpassword = StringUtil.replaceNull(user
//								.getUserPassword());
//						if (!oldpassword.equals(EncrpyPwd
//								.encodePassword(newpassword)))
//							user.setUserPassword(EncrpyPwd
//									.encodePassword(newpassword));
//
//					}
//
//				} else {
//				String password = StringUtil.replaceNull(user
//						.getUserPassword());
//				user.setUserPassword(EncrpyPwd.encodePassword(password));
//				}
				String sql = "update TD_SM_USER set USER_REALNAME=?, USER_IDCARD=?, USER_WORKTEL=?, USER_SEX=?,"
					+ "USER_HOMETEL=?, USER_EMAIL=?, USER_MOBILETEL1=?, USER_MOBILETEL2=?, REMARK4=?, REMARK5=?,"
					+ "USER_PINYIN=?, USER_TYPE=?, USER_POSTALCODE=?, USER_FAX=?, USER_OICQ=?, USER_BIRTHDAY=?,"
					+ "USER_ADDRESS=?, USER_ISVALID=?, DREDGE_TIME=?, USER_REGDATE=?, USER_SN=?, REMARK3=?,"
					+ "REMARK2=?, ISTAXMANAGER=?,USER_WORKNUMBER=?,Password_DualTime=? where USER_ID=?";
				TransactionManager tm = new TransactionManager();
				try
				{
					PreparedDBUtil pe = new PreparedDBUtil();
					tm.begin();
					pe.preparedUpdate(sql);
					pe.setString(1, user.getUserRealname());
					pe.setString(2, user.getUserIdcard() == null ? "" : user.getUserIdcard());
					pe.setString(3, user.getUserWorktel() == null ? "" : user.getUserWorktel());
					pe.setString(4, user.getUserSex());
					pe.setString(5, user.getUserHometel() == null ? "" : user.getUserHometel());
					pe.setString(6, user.getUserEmail() == null ? "" : user.getUserEmail());
					pe.setString(7, user.getUserMobiletel1() == null ? "" : user.getUserMobiletel1());
					pe.setString(8, user.getUserMobiletel2() == null ? "" : user.getUserMobiletel2());
					pe.setString(9, user.getRemark4() == null ? "" : user.getRemark4());
					pe.setString(10, user.getRemark5() == null ? "" : user.getRemark5());
					pe.setString(11, user.getUserPinyin() == null ? "" : user.getUserPinyin());
					pe.setString(12, user.getUserType());
					pe.setString(13, user.getUserPostalcode() == null ? "" : user.getUserPostalcode());
					pe.setString(14, user.getUserFax() == null ? "" : user.getUserFax());
					pe.setString(15, user.getUserOicq() == null ? "" : user.getUserOicq());
					pe.setDate(16, String.valueOf(user.getUserBirthday()) == "null" ? null :user.getUserBirthday());
					pe.setString(17, user.getUserAddress() == null ? "" : user.getUserAddress());
					pe.setInt(18, user.getUserIsvalid());
					pe.setString(19, user.getUserIsvalid().intValue()==2?riqi:"尚未开通");
					pe.setDate(20, reg == "null" ? null : user.getUserRegdate());
					pe.setInt(21, user.getUserSn());
					pe.setString(22, user.getRemark3() == null ? "" : user.getRemark3());
					pe.setString(23, user.getRemark2() == null ? "" : user.getRemark2());
					pe.setInt(24, user.getIstaxmanager());
					pe.setString(25, user.getUserWorknumber());
					pe.setInt(26, user.getPasswordDualedTime());
					
					pe.setInt(27, user.getUserId());
					
	//				System.out.println(sql);
					pe.executePrepared();
					initpasswordupdatetime(user.getUserId());
					tm.commit();
				}
				finally
				{
					tm.releasenolog();
				}
				
//				StringBuffer hsql = new StringBuffer();
//				hsql.append("update TD_SM_USER set USER_REALNAME='")
//					.append(user.getUserRealname()).append("', ")
//					.append("USER_PASSWORD='").append(user.getUserPassword()).append("', ")
//					.append("USER_IDCARD='")
//					.append(user.getUserIdcard() == null ? "" : user.getUserIdcard())
//					.append("', ")
//					.append("USER_WORKTEL='")
//					.append(user.getUserWorktel() == null ? "" : user.getUserWorktel())
//					.append("', ")
//					.append("USER_SEX='")
//					.append(user.getUserSex())
//					.append("', ")
//					.append("USER_HOMETEL='")
//					.append(user.getUserHometel() == null ? "" : user.getUserHometel())
//					.append("', ")
//					.append("USER_EMAIL='")
//					.append(user.getUserEmail() == null ? "" : user.getUserEmail())
//					.append("', ")
//					.append("USER_MOBILETEL1='")
//					.append(user.getUserMobiletel1() == null ? "" : user.getUserMobiletel1())
//					.append("', ")
//					.append("USER_MOBILETEL2='")
//					.append(user.getUserMobiletel2() == null ? "" : user.getUserMobiletel2())
//					.append("', ")
//					.append("REMARK4='")
//					.append(user.getRemark4() == null ? "" : user.getRemark4())
//					.append("', ")
//					.append("REMARK5='")
//					.append(user.getRemark5() == null ? "" : user.getRemark5())
//					.append("', ")
//					.append("USER_PINYIN='")
//					.append(user.getUserPinyin() == null ? "" : user.getUserPinyin())
//					.append("', ")
//					.append("USER_TYPE='")
//					.append(user.getUserType())
//					.append("', ")
//					.append("USER_POSTALCODE='")
//					.append(user.getUserPostalcode() == null ? "" : user.getUserPostalcode())
//					.append("', ")
//					.append("USER_FAX='")
//					.append(user.getUserFax() == null ? "" : user.getUserFax())
//					.append("', ")
//					.append("USER_OICQ='")
//					.append(user.getUserOicq() == null ? "" : user.getUserOicq())
//					.append("', ")
//					.append("USER_BIRTHDAY=")
//					.append(String.valueOf(user.getUserBirthday()) == "null" ? "null" :user.getUserBirthday())
////					.append(String.valueOf(user.getUserBirthday()) == "null" ? "''" : DBUtil.getDBDate(user.getUserBirthday().toString()))
//					.append(", ")
//					.append("USER_ADDRESS='")
//					.append(user.getUserAddress() == null ? "" : user.getUserAddress())
//					.append("', ")
//					.append("USER_ISVALID='")
//					.append(user.getUserIsvalid())
//					.append("', ")
//					.append("DREDGE_TIME='").append(user.getUserIsvalid().intValue()==2?riqi:"尚未开通")
//					.append("', ")
//					.append("USER_REGDATE='")
////					.append(String.valueOf(user.getUserRegdate()) == "null" ? "''" : DBUtil.getDBDate(user.getUserRegdate().toString()))
//					.append(String.valueOf(user.getUserRegdate()) == "null" ? "null" : reg)
//					.append("', ").append("USER_SN='").append(
//					user.getUserSn()).append("', ").append("REMARK3='").append(
//								user.getRemark3() == null ? "" : user
//										.getRemark3()).append("', ").append(
//								"REMARK2='").append(
//								user.getRemark2() == null ? "" : user
//										.getRemark2()).append("', ").append(
//								"ISTAXMANAGER=").append(user.getIstaxmanager())
//						.append(" ").append("where USER_ID=").append(
//								user.getUserId()).append("");
//				System.out.println(hsql.toString());
//				DBUtil db = new DBUtil();
//				db.executeUpdate(hsql.toString());
				state = true;
				Event event = new EventImpl(user.getUserId().toString(), ACLEventType.USER_INFO_CHANGE);
				super.change(event);
			} catch (Exception e) {
				throw new ManagerException(e);
			}

		}
		return state;
	}
	/**
	 * 获取用户密码过期时间，如果返回为null,表示密码永不过期
	 * @param userid
	 * @return
	 * @throws ManagerException 
	 */
	public Date getPasswordExpiredTime(int userid) throws ManagerException
	{
		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();
			Map updatetimes = SQLExecutor.queryObjectByRowHandler(new RowHandler<HashMap>(){

				@Override
				public void handleRow(HashMap rowValue, Record record)
						throws Exception {
					rowValue.put("password_updatetime", record.getTimestamp("password_updatetime"));
					
					rowValue.put("password_dualtime", record.getInt("password_dualtime"));
				}
				
			},HashMap.class,  "select password_updatetime,password_dualtime from TD_SM_USER where USER_id=?", userid);
			if(updatetimes == null)
			{
				return null;
			}
			Timestamp updatetime =(Timestamp)updatetimes.get("password_updatetime");
			if(updatetime == null )
				updatetime = this.initpasswordupdatetime(userid);
			tm.commit();
			
			return getPasswordExpiredTime(updatetime,((Integer)updatetimes.get("password_dualtime")).intValue());
		} catch (Exception e) {
			throw new ManagerException(e);
		}
		finally
		{
			tm.releasenolog();
		}
	}
	
	public int getDefaultPasswordDualTime()
	{
		return ConfigManager.getInstance().getConfigIntValue("password_dualtime", -1);
	}
	
	public int getUserPasswordDualTimeByUserAccount(String userAccount) throws ManagerException
	{
		if(this.isUserScopePasswordExpiredDays())
		{
			User user = this.getUserByName(userAccount);
			if(user == null)
				return 0;
			return user.getPasswordDualedTime();
		}
		else
		{
			return this.getDefaultPasswordDualTime();
		}
	}
	public  Date getPasswordExpiredTimeByUserAccount(String userAccount) throws ManagerException
	{
		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();
			Map updatetimes = SQLExecutor.queryObjectByRowHandler(new RowHandler<HashMap>(){

				@Override
				public void handleRow(HashMap rowValue, Record record)
						throws Exception {
					rowValue.put("password_updatetime", record.getTimestamp("password_updatetime"));
					
					rowValue.put("password_dualtime", record.getInt("password_dualtime"));
				}
				
			},HashMap.class,  "select password_updatetime,password_dualtime from TD_SM_USER where USER_name=?", userAccount);
			if(updatetimes == null)
				return null;
			Timestamp updatetime = (Timestamp)updatetimes.get("password_updatetime");
			if(updatetime == null )
				updatetime = this.initpasswordupdatetimeByUserAccount(userAccount);
			tm.commit();
			int expiredays = ((Integer)updatetimes.get("password_dualtime")).intValue();
			
			return getPasswordExpiredTime(updatetime,expiredays);
		} catch (Exception e) {
			throw new ManagerException(e);
		}
		finally
		{
			tm.releasenolog();
		}
	}
	/**
	 * 获取密码过期时间，如果返回为null,表示密码永不过期
	 * @param passwordupdatetime
	 * @return
	 */
	public Date getPasswordExpiredTime(Timestamp passwordupdatetime,int expiredays)
	{
		if(passwordupdatetime == null)
			return null;
		if(!isUserScopePasswordExpiredDays())
			expiredays = getDefaultPasswordDualTime();
		if(expiredays <= 0)
			return null;
		 Calendar calendar=Calendar.getInstance();   
		 calendar.setTime(passwordupdatetime); 
		 calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH)+expiredays);
		 return new Timestamp(calendar.getTime().getTime());
	}
	public Timestamp initpasswordupdatetimeByUserAccount(String userAccount) throws ManagerException
	{
		try {
			Timestamp updatetime = SQLExecutor.queryObject(Timestamp.class, "select password_updatetime from TD_SM_USER where USER_name=?", userAccount);
			if(updatetime == null)
			{
				updatetime =  new Timestamp(new Date().getTime());
				StringBuffer hsql = new StringBuffer();
				hsql.append("update TD_SM_USER set password_updatetime=? where ").append(
						"USER_name=?");
				SQLExecutor.update(hsql.toString(), updatetime,userAccount);
				return updatetime;
			}
			return updatetime;
		} catch (Exception e) {
			throw new ManagerException(e);
		}
	}
	public Timestamp initpasswordupdatetime(int userid) throws ManagerException
	{
		try {
			Timestamp updatetime = SQLExecutor.queryObject(Timestamp.class, "select password_updatetime from TD_SM_USER where USER_ID=?", userid);
			if(updatetime == null)
			{
				updatetime =  new Timestamp(new Date().getTime());
				StringBuffer hsql = new StringBuffer();
				hsql.append("update TD_SM_USER set password_updatetime=? where ").append(
						"USER_ID=?");
				SQLExecutor.update(hsql.toString(), updatetime,userid);
				return updatetime;
			}
			return updatetime;
		} catch (Exception e) {
			throw new ManagerException(e);
		}
	}
	public boolean ishistorypassword(int userid,String password)throws ManagerException
	{
		try {
			boolean passwordreusable = ConfigManager.getInstance().getConfigBooleanValue("passwordreusable", true);
			if(passwordreusable)
				return false;
			int count = SQLExecutor.queryObject(int.class, "select count(1) from TD_SM_PASSWORDHIS where user_id=? and PASSWORD_= ?", 
					userid,EncrpyPwd.encodePassword(password));
			return count > 0;
		} catch (Exception e) {
			throw new ManagerException(e);
		}
	}
	
	public boolean isUserScopePasswordExpiredDays()
	{
		return ConfigManager.getInstance().getConfigBooleanValue("enableUserScopePasswordExpiredDays", false);
	}
	/**
	 * 判断用户口令是否过期
	 * @param userid
	 * @return
	 * @throws ManagerException
	 */
	public boolean isPasswordExpired(int userid)throws ManagerException
	{
		try {
			
			Map updatetimes = SQLExecutor.queryObjectByRowHandler(new RowHandler<HashMap>(){

				@Override
				public void handleRow(HashMap rowValue, Record record)
						throws Exception {
					rowValue.put("password_updatetime", record.getTimestamp("password_updatetime"));
					
					rowValue.put("password_dualtime", record.getInt("password_dualtime"));
				}
				
			},HashMap.class,  "select password_updatetime,password_dualtime from TD_SM_USER where USER_ID=?", userid);
			Timestamp updatetime = updatetimes != null?(Timestamp)updatetimes.get("password_updatetime"):null;
			
			if(updatetime == null)
			{
				StringBuffer hsql = new StringBuffer();
				hsql.append("update TD_SM_USER set password_updatetime=? where ").append(
						"USER_ID=?");
				SQLExecutor.update(hsql.toString(), new Timestamp(new Date().getTime()),userid);
				return false;
			}
			int expiredays = ((Integer)updatetimes.get("password_dualtime")).intValue();
			if(expiredays <= 0)
			{
				if(!isUserScopePasswordExpiredDays())
					expiredays = getDefaultPasswordDualTime();
				
			}
			
			
			if(expiredays <= 0)
				return false;
//		   Calendar calendar=Calendar.getInstance();   
//		   calendar.setTime(updatetime); 
//		
//		   calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH)+expiredays); 
		   Calendar calendarnow=Calendar.getInstance();   
		   calendarnow.setTime(new Date());
//			return calendarnow.after(calendar);
			Date date = this.getPasswordExpiredTime(updatetime, expiredays);
			return new Date().after(date); 
			  
		} catch (Exception e) {
			throw new ManagerException(e);
		}
	}
	
	/**
	 * 判断用户口令是否过期
	 * @param userid
	 * @return
	 * @throws ManagerException
	 */
	public boolean isPasswordExpired(User user)throws ManagerException
	{
		try {
			int expiredays = user.getPasswordDualedTime();
			if(expiredays <= 0)
				return false;
			Timestamp updatetime = user.getPasswordUpdatetime();
			
			if(updatetime == null)
			{
				StringBuffer hsql = new StringBuffer();
				hsql.append("update TD_SM_USER set password_updatetime=? where ").append(
						"USER_ID=?");
				SQLExecutor.update(hsql.toString(), new Timestamp(new Date().getTime()),user.getUserId());
				return false;
			}
//		   Calendar calendar=Calendar.getInstance();   
//		   calendar.setTime(updatetime); 
//		
//		   calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH)+expiredays);//让日期加1  
//		   Calendar calendarnow=Calendar.getInstance();   
//		   calendarnow.setTime(new Date());
//			return calendarnow.after(calendar);
			Date date = this.getPasswordExpiredTime(updatetime, expiredays);
			return new Date().after(date);
			  
		} catch (Exception e) {
			throw new ManagerException(e);
		}
	}
	
	public boolean updateUserPassword(User user) throws ManagerException {
		boolean state = false;
		if (user != null) {
			StringBuffer hsql = new StringBuffer();
			
			
			hsql.append("update TD_SM_USER set USER_PASSWORD=? ,password_updatetime=? where ").append(
					"USER_ID=?");
			PreparedDBUtil db = new PreparedDBUtil();
			TransactionManager tm = new TransactionManager();
			try {
				tm.begin();
				if(this.ishistorypassword(user.getUserId(), user.getUserPassword()))
				{
					throw new ManagerException("密码曾经被使用过，请重新输入新的密码！");
				}
				Map oldpassword = SQLExecutor.queryObject(HashMap.class, "select USER_PASSWORD,password_updatetime from td_sm_user where user_id=?", user.getUserId());
				//记录旧密码
				SQLExecutor.insert("INSERT INTO TD_SM_PASSWORDHIS (   USER_ID, PASSWORD_, PASSWORD_TIME) VALUES ( ?,?,? )", 
						user.getUserId(),oldpassword.get("USER_PASSWORD"),oldpassword.get("PASSWORD_UPDATETIME")!=null?oldpassword.get("PASSWORD_UPDATETIME"):new Timestamp(new Date().getTime()));
				db.preparedUpdate( hsql.toString());
				db.setString(1, EncrpyPwd.encodePassword(user.getUserPassword()));
				db.setTimestamp(2, new Timestamp(new Date().getTime()));
				db.setInt(3, user.getUserId());		
				db.executePrepared();
				tm.commit();
				state = true;
			} catch (ManagerException e) {
				throw e;
			}
			 catch (Exception e) {
					throw new ManagerException(e);
				}
			finally
			{
				tm.releasenolog();
			}
		}
		return state;
	}
//	public boolean updateUserPassword(User user) throws ManagerException {
//		boolean state = false;
//		if (user != null) {
//			StringBuffer hsql = new StringBuffer();
//			hsql.append("update TD_SM_USER set USER_PASSWORD='").append(
//					EncrpyPwd.encodePassword(user.getUserPassword())).append("' where ").append(
//					"USER_ID='").append(user.getUserId()).append("'");
//			DBUtil db = new DBUtil();
//			try {
//				db.executeUpdate(hsql.toString());
//				state = true;
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//		return state;
//	}
	public boolean addUserOrg(String[] userIds, String orgId, String classType)
			throws ManagerException {
		return addUserOrg(userIds, orgId, classType,true);
	}
	public boolean addUserOrg(String[] userIds, String orgId, String classType,boolean broadcastevent)
			throws ManagerException {
		boolean state = false;
//		DBUtil db = new DBUtil();
		int samesn = 1;
		if ("lisan".equals(classType)) {
			try {
				// 如果是离散用户，当前机构设为它的主机构
				OrgManager orgmanager = SecurityDatabase.getOrgManager();

				for (int i = 0; i < userIds.length; i++) {
//					DBUtil dbUtil = new DBUtil();
					int size = SQLExecutor.queryObject(int.class,"select count(1) from td_sm_orguser where org_Id=? and user_id=?",orgId ,userIds[i]);
//					dbUtil.executeSelect(mainsql);
					if (size > 0) {
						continue;
					} else {
						orgmanager.addMainOrgnazitionOfUser(userIds[i], orgId,broadcastevent);
					}
				}
			} catch (SPIException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// 往td_sm_userjoborg表中插入记录
		try {
			for (int j = 0; j < userIds.length; j++) {
				int sn = SQLExecutor.queryObject(int.class,"select max(same_job_user_sn) as sn from  td_sm_userjoborg"
						+ " where  org_id =?",orgId);
				int dbsize = SQLExecutor.queryObject(int.class, "select count(1) from td_sm_userjoborg where user_id=? and org_id=? and job_id='1'",userIds[j] ,orgId);
//				db.executeSelect(sql2);
//				DBUtil dbutil = new DBUtil();
//				dbutil.executeSelect(sqlsn);
				if (dbsize > 0) {
					continue;
				} else {
					if (sn > 0) {
						samesn = sn + 1;
//						String sql = "insert into td_sm_userjoborg"
//								+ " (user_id,job_id,org_id,JOB_SN,SAME_JOB_USER_SN,JOB_STARTTIME,JOB_FETTLE)"
//								+ " values(" + userIds[j] + ",'" + 1 + "','"
//								+ orgId + "'," + 999 + "," + samesn
//								+ ",SYSDATE,1)";
//						db.executeInsert(sql);
						PreparedDBUtil prepareddbutil = new PreparedDBUtil();
						String sql_ = sqlUtil.getSQL("usermanagerimpl_addUserOrg");
						Map<String, String> variablevalues = new HashMap<String, String>();
						variablevalues.put("date", DBUtil.getDBAdapter().to_date(new Date()));
						String sql =  sqlUtil.evaluateSQL("mqgroupservice_addMQ_PERMISSIONS", sql_, variablevalues);
						prepareddbutil.preparedInsert(sql);
						prepareddbutil.setString(1,userIds[j] );
						prepareddbutil.setString(2,orgId );
						prepareddbutil.setInt(3,samesn );
						prepareddbutil.executePrepared();
					} else {
						samesn = 1;
//						String sql = "insert into td_sm_userjoborg"
//								+ "(user_id,job_id,org_id,JOB_SN,SAME_JOB_USER_SN,JOB_STARTTIME,JOB_FETTLE)"
//								+ " values(" + userIds[j] + ",'" + 1 + "','"
//								+ orgId + "'," + 999 + "," + samesn
//								+ ",SYSDATE,1)";
//						db.executeInsert(sql);
						PreparedDBUtil prepareddbutil_ = new PreparedDBUtil();
						String sql__ = sqlUtil.getSQL("usermanagerimpl_addUserOrg");
						Map<String, String> variablevalues = new HashMap<String, String>();
						variablevalues.put("date", DBUtil.getDBAdapter().to_date(new Date()));
						String sql_ =  sqlUtil.evaluateSQL("mqgroupservice_addMQ_PERMISSIONS", sql__, variablevalues);
						prepareddbutil_.preparedInsert(sql_);
						prepareddbutil_.setString(1,userIds[j] );
						prepareddbutil_.setString(2,orgId );
						prepareddbutil_.setInt(3,samesn );
						prepareddbutil_.executePrepared();
					}
					this.userOrgParamManager.fixuserorg(userIds[j] ,orgId);
				}
			}
			if(broadcastevent)
			{
				Event event = new EventImpl("", ACLEventType.USER_INFO_CHANGE);
				super.change(event);
			}
			state = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return state;
	}
	public boolean storeBatchUserOrg(String[] userIds, String[] orgIds, boolean isInsert) throws ManagerException
	{
		return storeBatchUserOrg(userIds, orgIds, isInsert,true);
	}
	
	public void fixuserorg(String[] userIds, String orgid)
	{
		for(int i = 0;  userIds != null && i < userIds.length; i ++)
		{
			String userId = userIds[i];
			this.userOrgParamManager.fixuserorg(userId, orgid);
		}
	}
	/**
	 * 批量插入用户主管机构与用户机构岗位
	 * @param userIds
	 * @param orgIds
	 * @param isInsert 为true时插入主机构关系
	 * @return
	 * @throws ManagerException
	 */
	public boolean storeBatchUserOrg(String[] userIds, String[] orgIds, boolean isInsert,boolean broadcastevent) throws ManagerException{
		boolean state = false;
//		StringBuffer sql;
		String sql;
		StringBuffer sql2;
		DBUtil dbUtil = new DBUtil();
		DBUtil db = new DBUtil();
		PreparedDBUtil preparedbutil = new PreparedDBUtil();
		PreparedDBUtil preparedbutil_ = new PreparedDBUtil();
		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();
			if(isInsert){
				for(int j = 0; j < userIds.length; j++){
					sql = sqlUtil.getSQL("userManagerImpl_storeBatchUserOrg");
					preparedbutil.preparedSelect(sql);
					preparedbutil.setString(1, userIds[j]);
					preparedbutil.executePrepared();
					
					if(preparedbutil.getInt(0, 0)<=0)
					{
						preparedbutil_.preparedInsert(sqlUtil.getSQL("userManagerImpl_storeBatchUserOrg_insert"));
						preparedbutil_.setString(1, userIds[j]);
						preparedbutil_.setString(2, orgIds[0]);
						preparedbutil_.executePrepared();
					}
//					sql = new StringBuffer();
//					sql.append("insert all when totalsize <= 0 then into TD_SM_ORGUSER(USER_ID, ORG_ID) values('")
//					   .append(userIds[j]).append("', '")
//					   .append(orgIds[0]).append("') select count(ORG_ID) totalsize ")
//					   .append(" from TD_SM_ORGUSER where USER_ID = '").append(userIds[j])
//					   .append("' ");
//					dbUtil.addBatch(sql.toString());
				}
			}
			
			for(int i = 0; i < orgIds.length; i++){
				for(int j = 0; j < userIds.length; j++){
					sql2 = new StringBuffer();
					String sql3 = "select * from td_sm_userjoborg where user_id='"+userIds[j]+"' and org_id='"+orgIds[i]+"'";
					db.executeSelect(sql3);
					if(db.size() > 0){
						continue;
					}
					sql2.append("insert into td_sm_userjoborg")
						.append("(user_id,job_id,org_id,JOB_SN,SAME_JOB_USER_SN,JOB_STARTTIME,JOB_FETTLE)")
						.append(" values(")
						.append(userIds[j]).append(",'")
						.append(1).append("','")
						.append(orgIds[i]).append("',")
						.append(999).append(",").append(1).append(",")
						.append(DBUtil.getDBAdapter().to_date(new Date())).append(",1)");
					if(i == 0)
					{
						this.userOrgParamManager.fixuserorg(userIds[j], orgIds[i]);
					}
					dbUtil.addBatch(sql2.toString());
				}
			}
			dbUtil.executeBatch();
			
			tm.commit();
			if(broadcastevent)
			{
				Event event = new EventImpl("", ACLEventType.USER_ROLE_INFO_CHANGE);
				super.change(event);
			}
			state = true;
		} catch (SQLException e) {
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		} catch (Exception e) {
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}finally{
			dbUtil.resetBatch();
		}
		return state;
	}
	
	/**
	 * 删除离散用户的主机构关系td_sm_orguser。。。。。。。。
	 * @return
	 */
	public boolean deleteDisperseOrguser(){
		boolean state = false;
		DBUtil db = new DBUtil();
		String sql = "delete from td_sm_orguser where user_id not in(select user_id from td_sm_userjoborg)";
		try {
			db.executeDelete(sql);
			state = true;
			Event event = new EventImpl("", ACLEventType.USER_ROLE_INFO_CHANGE);
			super.change(event);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return state;
	}
	
	/**
	 * 根据用户id判断是否为税管员
	 * @param userId
	 * @return
	 */
	public boolean isTaxmanager(String userId) throws ManagerException {
		boolean state = false;
		DBUtil dbUtil = new DBUtil();
		String sql = "select t.istaxmanager from td_sm_user t where t.user_id='"+userId+"'";
		try {
			dbUtil.executeSelect(sql);
			int i = dbUtil.getInt(0, "istaxmanager");
			if(i == 1){
				state = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return state;
	}
	
	/**
	 * 判断机构下是否有用户
	 * @param org
	 * @return
	 * @throws ManagerException
	 */
	public boolean isContainUser(Organization org) throws ManagerException {
		boolean state = false;
		String sql = "select count(*) from td_sm_orguser where org_id='"+org.getOrgId()+"'";
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(sql);
			if(db.getInt(0, 0) > 0){
				state = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return state;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.manager.UserManager#userOrgInfo(java.lang.String)
	 * 删除 和 调动 用户的时候, 判断用户的机构信息, 决定是否能执行 删除和调动操作. 
	 * 如果返回为空, 可以删除 和 调动 用户, 否则不允许删除.
	 */
	public String userOrgInfo(AccessControl control, String userId) {
		//如果是超级管理员或者时部门管理员， 都不允许删除。
		String checkMessage = "";
		List list = PurviewManagerImpl.getBussinessCheck().userDeleteCheck(control, userId);
		if(list == null || list.size()==0){
			return "";
		}
		for(int i = 0; i < list.size(); i++){
			if("".equals(checkMessage)){
				checkMessage = (String)list.get(i);
			}else{
				checkMessage += "," + (String)list.get(i);
			}
		}
//		OrgManagerImpl orgImpl = new OrgManagerImpl();		
//		
//		//判断是否部门管理员
//		List managerOrgs = orgImpl.getUserManageOrgs(userId);
//		if(control.isAdminByUserid(userId)){//是超级管理员
//			return "超级管理员" ;
//		}else if( managerOrgs != null && managerOrgs.size() > 0){//是部门管理员
//			return "部门管理员";
//		}else{
//			String userInfos = "";
//			StringBuffer sql = new StringBuffer();
//			sql.append("select * from table(f_getTaskByUser(").append(userId).append("))");
//			DBUtil db = new DBUtil();
//			try {
//				db.executeSelect(sql.toString());
//				for(int i=0; i<db.size(); i++){
//					if("".equalsIgnoreCase(userInfos)){
//						userInfos += db.getString(i,0);
//					}else{
//						userInfos += "," + db.getString(i,0);
//					}
//				}
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}			
		return checkMessage;
	}

	public String getUserMainOrgId(String userId) {
		String sql = " select * from td_sm_orguser where user_id='" + userId + "'";
		DBUtil db = new DBUtil();
		String orgId = "";
		try {
			db.executeSelect(sql);
			orgId = db.getString(0, "org_id");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return orgId;
	}

	public String[] getCuruserAdministrableDeleteUser(String curUserId,String[] selectUserNames) {
		String[] userNames = null;
		if(selectUserNames == null)
			return null;
		//得到用户管理员管理表
		StringBuffer selectUserName = new StringBuffer();
		boolean flag = false;
		for(int count = 0; count < selectUserNames.length; count ++){
			if(!flag){
				selectUserName.append("'").append(selectUserNames[count]).append("'");
				flag = true;
			}else{
				selectUserName.append(",'").append(selectUserNames[count]).append("'");
			}
		}
		String orgmanager_sql = "select org_id from td_sm_orgmanager where user_id='"+curUserId+"'";
		StringBuffer user_sql = new StringBuffer()
			.append("select u.USER_NAME from td_sm_orguser ou,td_sm_user u ")
			.append("where u.user_id = ou.user_id and u.user_name in (").append(selectUserName.toString())
			.append(") and ou.ORG_ID in (").append("SELECT DISTINCT org_id ")
			.append("FROM td_sm_organization START WITH org_id IN (")
			.append(orgmanager_sql).append(") CONNECT BY PRIOR org_id = parent_id)");
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(user_sql.toString());
			if(db.size() > 0){
				userNames = new String[db.size()];
				for(int i = 0; i < db.size(); i++){
					userNames[i] = db.getString(i, "user_name");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userNames;
	}
	
	public static void main(String[] args){
		UserManagerImpl userImpl = new UserManagerImpl();
//		User user;
//		try {
//			user = userImpl.getUserById("1277");
//			user.setUserSn(Integer.valueOf("110"));
//			user.setUserName("yubianyi");
//			user.setUserRealname("预编译");
//			user.setUserSex("F");
//			user.setUserPassword("123456");
//			userImpl.addUser(user);
//			System.out.println("添加用户成功！ " + user.getUserRealname());
//		} catch (ManagerException e) {
//			e.printStackTrace();
//		}
		String[] groupid = {"41","42"};
		try {
			userImpl.addUsergroup(new Integer(2659), groupid);
		} catch (ManagerException e) {
			e.printStackTrace();
		}
		
	}
	
}
