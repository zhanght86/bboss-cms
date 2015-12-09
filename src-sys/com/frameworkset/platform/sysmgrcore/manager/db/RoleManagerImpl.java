package com.frameworkset.platform.sysmgrcore.manager.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.RollbackException;

import org.apache.log4j.Logger;
import org.frameworkset.event.Event;
import org.frameworkset.event.EventHandle;
import org.frameworkset.event.EventImpl;
import org.frameworkset.persitent.util.SQLUtil;

import com.frameworkset.common.poolman.ConfigSQLExecutor;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.common.poolman.Record;
import com.frameworkset.common.poolman.SQLExecutor;
import com.frameworkset.common.poolman.SQLParams;
import com.frameworkset.common.poolman.handle.NullRowHandler;
import com.frameworkset.common.poolman.handle.RowHandler;
import com.frameworkset.common.poolman.util.SQLManager;
import com.frameworkset.orm.transaction.TransactionException;
import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.platform.config.model.ResourceInfo;
import com.frameworkset.platform.menu.MenuResTree;
import com.frameworkset.platform.resource.ResourceManager;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.security.authorization.AuthRole;
import com.frameworkset.platform.security.authorization.impl.AccessPermission;
import com.frameworkset.platform.security.event.ACLEventType;
import com.frameworkset.platform.sysmgrcore.entity.Group;
import com.frameworkset.platform.sysmgrcore.entity.Operation;
import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.platform.sysmgrcore.entity.Res;
import com.frameworkset.platform.sysmgrcore.entity.Role;
import com.frameworkset.platform.sysmgrcore.entity.Roleresop;
import com.frameworkset.platform.sysmgrcore.entity.User;
import com.frameworkset.platform.sysmgrcore.entity.UserJobRole;
import com.frameworkset.platform.sysmgrcore.entity.UserJobs;
import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.platform.sysmgrcore.manager.LogManager;
import com.frameworkset.platform.sysmgrcore.manager.ResManager;
import com.frameworkset.platform.sysmgrcore.manager.RoleManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.platform.util.EventUtil;
import com.frameworkset.util.ListInfo;

/**
 * 项目：SysMgrCore <br>
 * 描述：角色管理实现类 <br>
 * 版本：1.0 <br>
 * 
 * @author 
 */
public class RoleManagerImpl extends EventHandle implements RoleManager {
	
	private static ConfigSQLExecutor executor = new ConfigSQLExecutor("com/frameworkset/platform/sysmgrcore/manager/db/user.xml"); 

	/**
	 * 
	 */
	
	private  SQLUtil sqlUtilInsert = SQLUtil.getInstance("org/frameworkset/insert.xml");
	private static final long serialVersionUID = 1L;

	private static Logger logger = Logger.getLogger(RoleManagerImpl.class );


	/**
	 * 删除角色实例同时将连带删除与该实例有关的所有实例，如：用户与角色关系实例。
	 * 
	 * @param role
	 * @return
	 */
	public boolean deleteRole(Role role) throws ManagerException {
		return this.deleteRoles(new String[] {role.getRoleId()});
	}
	
	/**
	 * 获取当前登陆用户可以查看的角色授予的用户列表,
	 * @userId 当前登陆的用户
	 * @roleId 要授予的用户
	 */
	public ListInfo getRoleUserList(String userId, String roleId, int offset, int maxItem, boolean tag)
	{
 		ListInfo listInfo = new ListInfo();
		DBUtil dbUtil = new DBUtil();
		DBUtil db = new DBUtil();
		String sql = "";
		if(tag)
		{
			sql = "select a.* from td_sm_user a "+
		       "inner join td_sm_userrole b " +
		       "on a.user_id = b.user_id and b.role_id='" + roleId + "' ";
		}
		else
		{
			sql = "select a.* from td_sm_user a "+
		       "inner join td_sm_userrole b " +
		       "on a.user_id = b.user_id and b.role_id='" + roleId + "' "+
		       "where a.user_id in"+
		             "("+
		              "select ujo.user_id from td_sm_userjoborg ujo "+
		                     "where ujo.org_id in "+
		                           "( "+
		                            "select beckham.org_id " +
		                            "from v_tb_res_org_user beckham "+
		                            "where beckham.user_id='" + userId + "' "+
		                            //add by ge.tao
		                            //增加查询条件: 操作ID
		                            //union用户管理的机构
		                            "and beckham.op_id = 'userroleset' " + 
		                            "union " +
		                            "select orgs.org_id " +
		                            "from td_sm_organization orgs " +
		                            "start with orgs.org_id = " +
		                                        "(select beckham.org_id " +
		                                           "from td_sm_orgmanager beckham " +
		                                          "where beckham.user_id = ' " + userId + 
		                            "') connect by prior orgs.org_id = orgs.parent_id) " +
		                           ") ";
		}
		
		
		try {
			dbUtil.executeSelect(sql, offset, maxItem);			
			List list = new ArrayList();
			UserJobs uj;
			for (int i = 0; i < dbUtil.size(); i++) {
				String orgjob = "";
				String str = "select getuserorgjobinfos('" + dbUtil.getInt(i, "user_id") + "') as org from dual";
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
	 * 删除角色实例同时将连带删除与该实例有关的所有实例，如：用户与角色关系实例。
	 * 
	 * @param role
	 * @return
	 */
	public boolean deleteRoles(String[] roleids) throws ManagerException {
		boolean r = false;
		
		
		DBUtil db = new DBUtil();
		TransactionManager tm = new TransactionManager();
		if (roleids != null || roleids.length>0) {
			try {
				tm.begin();
				String ids = this.build(roleids);
				String sql0 = "delete from TD_SM_ROLERESOP  "
						+ " where res_id in (" + ids
						+ ") and types='role'";
				db.addBatch(sql0);			

				String sql1 = "delete from td_sm_grouprole where role_id in ( " + ids + ")";
				db.addBatch(sql1);
				
				String sql2 = "delete from td_sm_userrole where role_id in ( " + ids + ")";
				db.addBatch(sql2);
				
				String sql3 = "delete from td_sm_orgrole where role_id in ( " + ids + ")";
				db.addBatch(sql3);				
				
				//删除当前角色所关联的 td_sm_orgjobrole 表中的记录 ----gao.tang 2007.10.31
				String hsql = "delete from td_sm_orgjobrole where role_id in ( " + ids + ")";
				db.addBatch(hsql);
				
				String sql4 = "delete from td_sm_role where role_id in ( " + ids + ")";
				db.addBatch(sql4);
				
				db.executeBatch();
				tm.commit();
				// 触发事件
				Event event = new EventImpl(ids,
						ACLEventType.ROLE_INFO_CHANGE);
				super.change(event);
				
				r = true;
			} catch (SQLException e) {
				try {
					tm.rollback();
				} catch (RollbackException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				logger.error(e);
				
				throw new ManagerException(e.getMessage());
			} catch (Exception e) {
				try {
					tm.rollback();
				} catch (RollbackException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				logger.error(e);
				throw new ManagerException(e.getMessage());
			}finally{
				db.resetBatch();
			}
		}

		return r;
	}

	/**
	 * 没有被使用的方法
	 * @deprecated 不推荐使用的方法，方法实现已经被注释掉
	 */
	public boolean deleteRoleresop(Roleresop roleresop) throws ManagerException {
		boolean r = false;

//		if (roleresop != null) {
//			try {
//				// 删除当前操作资源关系对象
//				Parameter p = new Parameter();
//				p.setCommand(Parameter.COMMAND_DELETE);
//				p.setObject("from Roleresop rro where rro.id.restypeId = '"
//						+ roleresop.getId().getRestypeId()
//						+ "' and rro.id.resId = '"
//						+ roleresop.getId().getResId()
//						+ "' and rro.id.roleId='"
//						+ roleresop.getId().getRoleId() + "' and rro.id.opId='"
//						+ roleresop.getId().getOpId() + "' ");
//				// p.setObject("from Roleresop rro where rro.id.opId = '"
//				// + roleresop.getId().getOpId()
//				// + "' and rro.id.resId = '"
//				// + roleresop.getId().getResId()
//				// + "' and rro.id.roleId = '"
//				// + roleresop.getId().getRoleId() + "'");
//				if (cb.execute(p) != null)
//					r = true;
//				Event event = new EventImpl("",
//						ACLEventType.RESOURCE_ROLE_INFO_CHANGE);
//				super.change(event);
//				// Event event = new
//				// EventImpl(roleresop.getRole().getRoleName(),
//				// ACLEventType.PERMISSION_DELETE);
//				// super.change(event);
//			} catch (ControlException e) {
//				logger.error(e);
//				throw new ManagerException(e.getMessage());
//			}
//		}

		return r;
	}

	/**
	 * 没用的方法
	 * @deprecated 不推荐使用的方法，方法实现已经被注释掉
	 */
	public boolean deleteRoleresop(String resId, String roleId, String restypeId)
			throws ManagerException {
		boolean r = false;
//
//		try {
//			// 删除当前操作资源关系对象
//			Parameter p = new Parameter();
//			p.setCommand(Parameter.COMMAND_DELETE);
//			p.setObject("from Roleresop rro where rro.id.resId = '" + resId
//					+ "' and rro.id.roleId = '" + roleId
//					+ "' and rro.id.restypeId = '" + restypeId + "' ");
//			if (cb.execute(p) != null)
//				r = true;
//			Event event = new EventImpl("",
//					ACLEventType.RESOURCE_ROLE_INFO_CHANGE);
//			super.change(event);
//		} catch (ControlException e) {
//			logger.error(e);
//			throw new ManagerException(e.getMessage());
//		}
//		StringBuffer sql = new StringBuffer()
//			.append("delete td_sm_roleresop where res_id='").append(resId).append("' and ")
//			.append("ROLE_ID='").append(roleId).append("' and ")
//			.append("RESTYPE_ID='").append(restypeId).append("' ");
//		DBUtil db = new DBUtil();
//		try {
//			db.executeDelete(sql.toString());
//			Event event = new EventImpl("",
//				ACLEventType.RESOURCE_ROLE_INFO_CHANGE);
//			super.change(event);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}

		return r;
	}

	/**
	 * 没有被使用的方法
	 * 删除某一资源id及资源类型对应的所有角色及操作 添加
	 * 
	 * @param resId
	 * @param restypeId
	 * @return
	 * @throws ManagerException
	 * @deprecated 不推荐使用的方法，方法实现已经被注释掉
	 */
	public boolean deleteRoleresop(String resId, String restypeId)
			throws ManagerException {
		boolean r = false;

//		try {
//			// 删除当前操作资源关系对象
//			Parameter p = new Parameter();
//			p.setCommand(Parameter.COMMAND_DELETE);
//			p.setObject("from Roleresop rro where rro.id.resId = '" + resId
//					+ "' and rro.id.restypeId = '" + restypeId + "'");
//			if (cb.execute(p) != null)
//				r = true;
//			Event event = new EventImpl("",
//					ACLEventType.RESOURCE_ROLE_INFO_CHANGE);
//			super.change(event);
//		} catch (ControlException e) {
//			logger.error(e);
//			throw new ManagerException(e.getMessage());
//		}

		return r;
	}

//	public Role getRole(String propName, String value) throws ManagerException {
//		Role role = null;
//		try {
//			Parameter p = new Parameter();
//			p.setCommand(Parameter.COMMAND_GET);
//			p.setObject("from Role r where r." + propName + "='" + value + "'");
//			List list = (List) cb.execute(p);
//
//			if (list != null && !list.isEmpty())
//				role = (Role) list.get(0);
//		} catch (ControlException e) {
//			logger.error(e);
//			throw new ManagerException(e.getMessage());
//		}
//
//		return role;
//	}
	
	private Role getRole(DBUtil dBUtil) throws SQLException{
		if(dBUtil.size() == 0)
			return null;
		Role role = new Role();		
		
		role.setRemark1(dBUtil.getString(0, "remark1"));
		role.setRemark2(dBUtil.getString(0, "remark2"));
		role.setRemark3(dBUtil.getString(0, "remark3"));
		role.setRoleDesc(dBUtil.getString(0, "ROLE_DESC"));
		role.setRoleId(dBUtil.getString(0, "ROLE_ID"));
		role.setRoleName(dBUtil.getString(0, "ROLE_NAME"));
		role.setRoleType(dBUtil.getString(0, "ROLE_TYPE"));
		role.setRoleUsage(dBUtil.getString(0, "ROLE_USAGE"));
		role.setOwner_id(dBUtil.getInt(0,"owner_id"));
		return role;
		
	}
	private void getRole(Role role,Record dBUtil) throws SQLException{
		
				
		
		role.setRemark1(dBUtil.getString(  "remark1"));
		role.setRemark2(dBUtil.getString(  "remark2"));
		role.setRemark3(dBUtil.getString(  "remark3"));
		role.setRoleDesc(dBUtil.getString(  "ROLE_DESC"));
		role.setRoleId(dBUtil.getString(  "ROLE_ID"));
		role.setRoleName(dBUtil.getString( "ROLE_NAME"));
		role.setRoleType(dBUtil.getString(  "ROLE_TYPE"));
		role.setRoleUsage(dBUtil.getString(  "ROLE_USAGE"));
		role.setOwner_id(dBUtil.getInt( "owner_id"));
		 
		
	}
	
	private List getRoles(DBUtil dBUtil) throws SQLException{
		if(dBUtil.size() == 0)
			return null;
		List list = new ArrayList();
		for(int i=0;i<dBUtil.size();i++)
		{
			Role role = new Role();					
			role.setRemark1(dBUtil.getString(i, "remark1"));
			role.setRemark2(dBUtil.getString(i, "remark2"));
			role.setRemark3(dBUtil.getString(i, "remark3"));
			role.setRoleDesc(dBUtil.getString(i, "ROLE_DESC"));
			role.setRoleId(dBUtil.getString(i, "ROLE_ID"));
			role.setRoleName(dBUtil.getString(i, "ROLE_NAME"));
			role.setRoleType(dBUtil.getString(i, "ROLE_TYPE"));
			role.setRoleUsage(dBUtil.getString(i, "ROLE_USAGE"));
			role.setOwner_id(dBUtil.getInt(i,"owner_id"));
			list.add(role);
		}
		return list;
		
		
	}
	
	
	public Role getRoleById(String roleid) throws ManagerException {
		
		Role role = null;
		try {
			String sql = "select * from td_sm_role where role_id = ?";
			
			PreparedDBUtil dbUtil = new PreparedDBUtil();
			dbUtil.preparedSelect(sql);
			dbUtil.setString(1, roleid);
			role = (Role) dbUtil.executePreparedForObject(Role.class,new RowHandler<Role>(){

				@Override
				public void handleRow(Role rowValue, Record record)
						throws Exception {
					getRole(rowValue,record);
					
				}
				
			});
		
			

			
		} catch (Exception e) {
			logger.error("",e);
			throw new ManagerException(e.getMessage());
		}

		return role;
	}
	
	public Role getRoleByName(String roleName) throws ManagerException {
		
		Role role = null;
		try {
			String sql = "select * from td_sm_role where ROLE_NAME = ?";
			
			PreparedDBUtil dbUtil = new PreparedDBUtil();
			dbUtil.preparedSelect(sql);
			dbUtil.setString(1, roleName);
			
			role = (Role) dbUtil.executePreparedForObject(Role.class,new RowHandler<Role>(){

				@Override
				public void handleRow(Role rowValue, Record record)
						throws Exception {
					getRole(rowValue,record);
					
				}
				
			});
			

			
		} catch (Exception e) {
			logger.error("",e);
			throw new ManagerException(e.getMessage());
		}

		return role;
	}

	/**
	 * 取所有角色
	 * 
	 * @return List
	 */
	public List getRoleList() throws ManagerException {

		
		List list = new ArrayList();
		String sql = "select * from td_sm_role t " + 
						" order by t.role_name";
		DBUtil dBUtil = new DBUtil();
		try{
			dBUtil.executeSelect(sql);
			list = this.getRoles(dBUtil);
										
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 没有被使用的方法
	 * 
	 * 根据操作取所有的角色
	 * 
	 * @param oper
	 * @return List
	 * @deprecated 不推荐使用的方法，方法实现已经被注释掉
	 */
	public List getRoleList(Operation oper) throws ManagerException {
		List list = new ArrayList();
//		try {
//			Parameter p = new Parameter();
//			p.setCommand(Parameter.COMMAND_GET);
//			p
//					.setObject("from Role role where role.roleId in ('"
//							+ "select rro.id.roleId from Roleresop rro where rro.id.opId = '"
//							+ oper.getOpId() + "')");
//			list = (List) cb.execute(p);
//		} catch (ControlException e) {
//			logger.error(e);
//			throw new ManagerException(e.getMessage());
//		}

		return list;
	}

	/**
	 * 没有被使用的方法
	 * 根据资源取所有相关的角色。
	 * 
	 * @param res
	 * @return List
	 * @deprecated 不推荐使用的方法，方法实现已经被注释掉
	 */
	public List getRoleList(Res res) throws ManagerException {
		List list = new ArrayList();
//		try {
//			Parameter p = new Parameter();
//			p.setCommand(Parameter.COMMAND_GET);
//			p
//					.setObject("from Role role where role.roleId in ('"
//							+ "select rro.id.roleId from Roleresop rro where rro.id.resId = '"
//							+ res.getResId() + "')");
//			list = (List) cb.execute(p);
//		} catch (ControlException e) {
//			logger.error(e);
//			throw new ManagerException(e.getMessage());
//		}

		return list;
	}

	/**
	 * 根据用户取所有相关的(用户角色关系)角色(包含用户自己的，机构的，组的）。
	 * 
	 * @param user
	 * @return TList
	 */
	public List getRoleList(User user) throws ManagerException {
		// List list = null;
		// try {
		// Parameter p = new Parameter();
		// p.setCommand(Parameter.COMMAND_GET);
		// p
		// .setObject("from Role role where role.roleId in ("
		// + "select ur.id.roleId from Userrole ur where ur.id.userId = '"
		// + user.getUserId() + "')");
		// list = (List) cb.execute(p);
		// } catch (ControlException e) {
		// logger.error(e);
		// throw new ManagerException(e.getMessage());
		// }

		return this.getAllRoleList(user.getUserId().toString());
	}

	/**
	 * 没有被使用的方法
	 * 根据用户取所有相关的(组角色关系)角色。
	 * 
	 * @param user
	 * @return TList
	 * @deprecated 不推荐使用的方法，方法实现已经被注释掉
	 */
	public List getRoleListByGroupRole(User user) throws ManagerException {
		List list = new ArrayList();
//		try {
//			Parameter p = new Parameter();
//			p.setCommand(Parameter.COMMAND_GET);
//			p.setObject("from Role role where role.roleId in ("
//							+ "select gr.id.roleId from Grouprole gr where gr.id.groupId in "
//							+ " (select ug.id.groupId from Usergroup ug where ug.id.userId = '"
//							+ user.getUserId() + "'))");
//			list = (List) cb.execute(p);
//		} catch (ControlException e) {
//			logger.error(e);
//			throw new ManagerException(e.getMessage());
//		}

		return list;
	}

	/**
	 * 没有被使用的方法
	 * 根据用户取所有相关的角色。
	 * 
	 * @param user
	 * @return TList
	 * @deprecated 不推荐使用的方法，方法实现已经被注释掉
	 */
	public List getAllRoleList(User user) throws ManagerException {
		List list = new ArrayList();
//		try {
//			Parameter p = new Parameter();
//			p.setCommand(Parameter.COMMAND_GET);
//			p
//					.setObject("from Role role where role.roleId in ("
//							+ "select ur.id.roleId from Userrole ur where ur.id.userId = '"
//							+ user.getUserId()
//							+ "') or role.roleId in ( select gr.id.roleId from Grouprole gr where gr.id.groupId in "
//							+ " (select ug.id.groupId from Usergroup ug where ug.id.userId = '"
//							+ user.getUserId()
//							+ "') ) "
//							+ " or role.roleId in ( select or.id.roleId from Orgrole or where or.id.orgId in "
//							+ " (select ujo.id.orgId from Userjoborg ujo where ujo.id.userId = '"
//							+ user.getUserId() + "') )");
//			list = (List) cb.execute(p);
//		} catch (ControlException e) {
//			logger.error(e);
//			throw new ManagerException(e.getMessage());
//		}

		return list;
	}

	/**
	 * 去掉hibernate后的方法
	 * 根据组对应的角色。
	 * 
	 * @param user
	 * @return TList
	 */
	public List getRoleList(Group group) throws ManagerException {
		List list = null;
//		try {
//			Parameter p = new Parameter();
//			p.setCommand(Parameter.COMMAND_GET);
//			p
//					.setObject("from Role role where role.roleId in ("
//							+ " select gr.id.roleId from Grouprole gr where gr.id.groupId = '"
//							+ group.getGroupId() + "') ");
//			list = (List) cb.execute(p);
//		} catch (ControlException e) {
//			logger.error(e);
//			throw new ManagerException(e.getMessage());
//		}
		String sql = "select * from td_sm_role where role_id in(select role_id from td_sm_grouprole where "
			+ "group_id = '" + group.getGroupId() + "')";
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(sql);
			list = getRoles(db);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * 没有被使用的方法
	 * @deprecated 不推荐使用的方法，方法实现已经被注释掉
	 */
	public Role loadAssociatedSet(String roleId, String associated)
			throws ManagerException {
		Role role = new Role();

//		try {
//			Parameter par = new Parameter();
//			par.setCommand(Parameter.COMMAND_GET);
//			par.setObject("from Role r left join fetch r." + associated
//					+ " where r.roleId = '" + roleId + "'");
//
//			List list = (List) cb.execute(par);
//			if (list != null && !list.isEmpty()) {
//				role = (Role) list.get(0);
//			}
//		} catch (ControlException e) {
//			logger.error(e);
//			throw new ManagerException(e.getMessage());
//		}

		return role;
	}

	/**
	 * 存储角色实例
	 * 存储角色信息用
	 * @param role
	 * @return
	 */
	public boolean storeRole(Role role) throws ManagerException {
		boolean r = false;
		if (role != null) {
			try {
				String sql = "update TD_SM_role set role_DESC='" + role.getRoleDesc() + "',role_type='" + role.getRoleType() + "',role_name='" + role.getRoleName() + "' " +
					"where role_id='" + role.getRoleId() + "'";
				DBUtil db = new DBUtil();
				db.executeUpdate(sql);
				
				r = true;
				// 触发事件
				Event event = new EventImpl(role.getRoleName(),
						ACLEventType.ROLE_INFO_CHANGE);
				super.change(event);
			} catch (Exception e) {
				logger.error(e);
				throw new ManagerException(e.getMessage());
			}
		}
		return r;
	}
	
	/**
	 * 存储角色实例
	 * 存储角色信息用
	 * @param role
	 * @return
	 */
	public boolean insertRole(Role role) throws ManagerException {
		boolean r = false;
		if (role != null) {
			try {
				PreparedDBUtil preparedDBUtil = new PreparedDBUtil();
				String roleId = preparedDBUtil.getNextStringPrimaryKey("TD_SM_role");
				
				role.setRoleId(roleId);
				
				StringBuffer sql = new StringBuffer()
					.append("insert into TD_SM_role(role_id,role_NAME,role_DESC,OWnER_ID,role_type) values ")
					.append("(?,?,?,?,?)");
				preparedDBUtil.preparedInsert(sql.toString());
				
				preparedDBUtil.setString(1, roleId);
				preparedDBUtil.setString(2, role.getRoleName());
				preparedDBUtil.setString(3, role.getRoleDesc());
				preparedDBUtil.setInt(4, role.getOwner_id());
				preparedDBUtil.setString(5, role.getRoleType());
				preparedDBUtil.executePrepared();
				
				
				r = true;
				// 触发事件
				Event event = new EventImpl(role.getRoleName(),
						ACLEventType.ROLE_INFO_CHANGE);
				super.change(event);
			} catch (Exception e) {
				logger.error(e);
				throw new ManagerException(e.getMessage());
			}
		}
		return r;
	}

	/**
	 * 没有被使用的方法
	 * @param roleresop
	 * @return
	 * @throws ManagerException
	 * @deprecated 不推荐使用的方法，方法实现已经被注释掉
	 */
	public boolean storeRoleresop(Roleresop roleresop) throws ManagerException {
		boolean r = false;

//		if (roleresop != null) {
//			try {
//				Parameter p = new Parameter();
//				p.setCommand(Parameter.COMMAND_STORE);
//				p.setObject(roleresop);
//
//				if (cb.execute(p) != null)
//					r = true;
//				Event event = new EventImpl("",
//						ACLEventType.RESOURCE_ROLE_INFO_CHANGE);
//				super.change(event);
//			} catch (ControlException e) {
//				logger.error(e);
//				throw new ManagerException(e.getMessage());
//			}
//		}

		return r;
	}

	/**
	 * 没有被使用的方法
	 * @see com.frameworkset.platform.sysmgrcore.manager.RoleManager#getRoleList(java.lang.String,
	 *      java.lang.String, boolean)
	 * @deprecated 不推荐使用的方法，方法实现已经被注释掉
	 */
	public List getRoleList(String propName, String value, boolean isLike)
			throws ManagerException {
		List list = new ArrayList();

//		try {
//			Parameter p = new Parameter();
//			p.setCommand(Parameter.COMMAND_GET);
//			if (!isLike)
//				p.setObject("from Role u where u." + propName + " = '" + value
//						+ "'");
//			else
//				p.setObject("from Role u where u." + propName + " like '"
//						+ value + "'");
//
//			list = (List) cb.execute(p);
//		} catch (ControlException e) {
//			throw new ManagerException(e.getMessage());
//		}

		return list;
	}

	public AccessPermission createPermission(String resid, String opid,
			String restypeid) {
		return new AccessPermission(resid, opid, restypeid);
	}

	/**
	 * 获取许可得角色类型角色集合
	 * 
	 * @param permissions
	 * @return
	 * @throws ManagerException
	 */
	public List getAllRoleTypeRoleHasPermission(AccessPermission[] permissions)
			throws ManagerException {
		List list = new ArrayList();
		DBUtil dbUtil = new DBUtil();
		StringBuffer rolesql = new StringBuffer();

		for (int i = 0; i < permissions.length; i++) {
			if (i == 0)
				rolesql
						.append(
								"select ROLE_ID as id,ROLE_NAME as name,'role' as roletype from TD_SM_ROLE where ROLE_ID in ")
						.append(
								"(select role_id from TD_SM_ROLERESOP where OP_ID='")
						.append(permissions[i].getAction())
						.append("' and RES_ID='" + permissions[i].getResource())
						.append("' and RESTYPE_ID='").append(
								permissions[i].getResourceType()).append(
								"' and (types='role' or types is null))");
			else

				rolesql.append(
								" union select ROLE_ID as id,ROLE_NAME as name,'role' as roletype from TD_SM_ROLE where ROLE_ID in ")
						.append(
								"(select role_id from TD_SM_ROLERESOP where OP_ID='")
						.append(permissions[i].getAction())
						.append("' and RES_ID='" + permissions[i].getResource())
						.append("' and RESTYPE_ID='").append(
								permissions[i].getResourceType()).append(
								"' and (types='role' or types is null))");

		}
		// 包含系统管理员角色
		rolesql.append(" union select role_id as id,role_name as name,'role' as roletype from td_sm_role where role_name = 'administrator'");

		try {
			dbUtil.executeSelect(rolesql.toString());
			for (int i = 0; i < dbUtil.size(); i++) {
				Role role = new Role();
				role.setRoleId(dbUtil.getString(i, "id"));
				role.setRoleName(dbUtil.getString(i, "name"));
				role.setRoleType(dbUtil.getString(i, "roletype"));
				list.add(role);

			}
		} catch (SQLException e) {
			throw new ManagerException(e.getMessage());

		}
		return list;
	}

	/**
	 * 获取许可得机构类型角色集合
	 * 
	 * @param permissions
	 * @return
	 * @throws ManagerException
	 */
	public List getAllOrganizationTypeRoleHasPermission(
			AccessPermission[] permissions) throws ManagerException {
		List list = new ArrayList();
		DBUtil dbUtil = new DBUtil();
		StringBuffer rolesql = new StringBuffer();

		for (int i = 0; i < permissions.length; i++) {
			if (i == 0) {
				// 被直接授予权限的机构
				rolesql
						.append(
								"select a.org_id as orgId,a.org_name as orgName from td_sm_organization a where org_id in ")
						.append(
								"(select role_id from TD_SM_ROLERESOP where OP_ID='")
						.append(permissions[i].getAction())
						.append("' and RES_ID='" + permissions[i].getResource())
						.append("' and RESTYPE_ID='").append(
								permissions[i].getResourceType()).append(
								"' and types='organization')");
				// 通过角色授予权限的机构
				rolesql
						.append(
								" union select a.org_id as orgId,a.org_name as orgName ")
						.append("from td_sm_organization a,td_sm_orgrole b ")
						.append("where a.org_id = b.org_id and b.role_id in ")
						.append(
								"(select role_id from TD_SM_ROLERESOP where OP_ID='")
						.append(permissions[i].getAction())
						.append("' and RES_ID='" + permissions[i].getResource())
						.append("' and RESTYPE_ID='").append(
								permissions[i].getResourceType()).append(
								"' and (types='role' or types is null))");
			} else
				rolesql
						.append(
								" union select a.org_id as orgId,a.org_name as orgName from td_sm_organization a where org_id in ")
						.append(
								"(select role_id from TD_SM_ROLERESOP where OP_ID='")
						.append(permissions[i].getAction())
						.append("' and RES_ID='" + permissions[i].getResource())
						.append("' and RESTYPE_ID='").append(
								permissions[i].getResourceType()).append(
								"' and types='organization')");
			rolesql
					.append(
							" union select a.org_id as orgId,a.org_name as orgName ")
					.append("from td_sm_organization a,td_sm_orgrole b ")
					.append("where a.org_id = b.org_id and b.role_id in ")
					.append(
							"(select role_id from TD_SM_ROLERESOP where OP_ID='")
					.append(permissions[i].getAction()).append(
							"' and RES_ID='" + permissions[i].getResource())
					.append("' and RESTYPE_ID='").append(
							permissions[i].getResourceType()).append(
							"' and (types='role' or types is null))");

		}
		// 包含为系统管理员角色的机构
		rolesql
				.append(
						" union select a.org_id as orgId,a.org_name as orgName ")
				.append(
						"from td_sm_organization a ,td_sm_orgrole b,td_sm_role c ")
				.append("where a.org_id = b.org_id and b.role_id = c.role_id ")
				.append("and role_name = 'administrator'");

		try {
			dbUtil.executeSelect(rolesql.toString());
			for (int i = 0; i < dbUtil.size(); i++) {
				Organization org = new Organization();
				org.setOrgId(dbUtil.getString(i, "orgId"));
				org.setOrgName(dbUtil.getString(i, "orgName"));
				list.add(org);

			}
		} catch (SQLException e) {
			throw new ManagerException(e.getMessage());

		}
		return list;
	}

	/**
	 * 获取许可的用户类型角色集合
	 * 
	 * @param permissions
	 * @return
	 * @throws ManagerException
	 */
	public List getAllUserTypeRoleHasPermission(AccessPermission[] permissions)
			throws ManagerException {
		List list = new ArrayList();
		DBUtil dbUtil = new DBUtil();
		StringBuffer rolesql = new StringBuffer();

		for (int i = 0; i < permissions.length; i++) {
			if (i == 0) {
				// 被直接授予权限的用户
				rolesql
						.append(
								"select a.user_id as userId,a.user_realname as userName from td_sm_user a where user_id in ")
						.append(
								"(select role_id from TD_SM_ROLERESOP where OP_ID='")
						.append(permissions[i].getAction())
						.append("' and RES_ID='" + permissions[i].getResource())
						.append("' and RESTYPE_ID='").append(
								permissions[i].getResourceType())

						.append("' and types='user')");
			} else {
				// 被直接授予权限的用户
				rolesql
						.append(
								" union select a.user_id as userId,a.user_realname as userName from td_sm_user a where user_id in ")
						.append(
								"(select role_id from TD_SM_ROLERESOP where OP_ID='")
						.append(permissions[i].getAction())
						.append("' and RES_ID='" + permissions[i].getResource())
						.append("' and RESTYPE_ID='").append(
								permissions[i].getResourceType())

						.append("' and types='user')");
			}
			// 通过角色授予权限的用户
			rolesql
					.append(
							" union select a.user_id as userId,a.user_realname as userName ")
					.append("from td_sm_user a,td_sm_userrole b ")
					.append("where a.user_id = b.user_id and b.role_id in ")
					.append(
							"(select role_id from TD_SM_ROLERESOP where OP_ID='")
					.append(permissions[i].getAction()).append(
							"' and RES_ID='" + permissions[i].getResource())
					.append("' and RESTYPE_ID='").append(
							permissions[i].getResourceType()).append(
							"' and (types='role' or types is null))");
			// 通过机构授予权限的用户，分两种情况：1、机构被直接授予权限。2、机构通过角色授予权限
			rolesql
					.append(
							" union select a.user_id as userId,a.user_realname as userName ")
					.append("from td_sm_user a,td_sm_userjoborg b ")
					.append("where a.user_id = b.user_id and b.org_id in ")
					.append(
							"(select role_id from TD_SM_ROLERESOP where OP_ID='")
					.append(permissions[i].getAction()).append(
							"' and RES_ID='" + permissions[i].getResource())
					.append("' and RESTYPE_ID='").append(
							permissions[i].getResourceType()).append(
							"' and types='organization')");
			rolesql
					.append(
							" union select a.user_id as userId,a.user_realname as userName ")
					.append("from td_sm_user a,td_sm_userjoborg b ")
					.append("where a.user_id = b.user_id and b.org_id in ")
					.append(
							"(select org_id from td_sm_orgrole where role_id in(")
					.append(
							"(select role_id from TD_SM_ROLERESOP where OP_ID='")
					.append(permissions[i].getAction()).append(
							"' and RES_ID='" + permissions[i].getResource())
					.append("' and RESTYPE_ID='").append(
							permissions[i].getResourceType()).append(
							"' and (types='role' or types is null))))");
		}
		// 用户为系统管理员角色
		rolesql
				.append(
						" union select a.user_id as userId,a.user_realname as userName ")
				.append("from td_sm_user a,td_sm_userrole b,td_sm_role c ")
				.append(
						"where a.user_id = b.user_id and b.role_id = c.role_id ")
				.append("and role_name = 'administrator'");
		// 用户所在的机构是系统管理员角色
		rolesql
				.append(
						" union select a.user_id as userId,a.user_realname as userName ")
				.append(
						"from td_sm_user a,td_sm_orguser b,td_sm_orgrole c,td_sm_role d ")
				.append(
						"where a.user_id = b.user_id and b.org_id = c.org_id and c.role_id = d.role_id ")
				.append("and d.role_name = 'administrator'");

		try {
			dbUtil.executeSelect(rolesql.toString());
			for (int i = 0; i < dbUtil.size(); i++) {
				User user = new User();
				user.setUserId(new Integer(dbUtil.getInt(i, "userId")));
				user.setUserName(dbUtil.getString(i, "userName"));
				list.add(user);
			}
		} catch (SQLException e) {
			throw new ManagerException(e.getMessage());

		}
		return list;
	}

	
	/**
	 * 得到用户的自身直接资源，角色的自身直接资源。发生改变
	 */
	public AuthRole[] getSecurityrolesInResource(final String resId,
			final String operName, final String restypeId) throws ManagerException {
//		if(true){
//			throw new ManagerException("yichang !!");
//		}
		
		List list = new ArrayList();
//		String sql = "SELECT role_id,types FROM TD_SM_ROLERESOP WHERE     OP_ID = 'visible' AND RES_ID = 'businessDemo' AND RESTYPE_ID = 'column'";
		String sql = "SELECT role_id,types FROM TD_SM_ROLERESOP WHERE     OP_ID = ? AND RES_ID = ? AND RESTYPE_ID = ?";
		final List<AuthRole> authRoles = new ArrayList<AuthRole>();
		try {
			SQLExecutor.queryByNullRowHandler(new NullRowHandler(){

				@Override
				public void handleRow(Record origine) throws Exception {
					String types= origine.getString("types");
					String role_id= origine.getString("role_id");
					try
					{
						
						AuthRole role = new AuthRole();
						role.setRoleId(role_id);
						role.setRoleType(types);
						
						if(types.equals("role"))
						{
							Role r = RoleCacheManager.getInstance().getRoleByID(role_id);
							if(r != null)
								role.setRoleName(r.getRoleName());
							else
							{
								Exception e = new Exception(new StringBuilder().append("build roleinfo for resource[resId=").append(resId).append(",operName=").append(operName).append(",restypeId=").append(restypeId).append("]failed:types[").append(types).append("]").append("roleid[").append(role_id).append("] not exist.").toString());
								RoleManagerImpl.logger.error("build roleinfo failed:", e);
								return;
							}
						}
						else if(types.equals("user"))
						{
							String userAccount = (String)UserCacheManager.getInstance().getUserAttributeByID(role_id,"userAccount");
							if(userAccount != null)
								role.setRoleName(userAccount);
							else
							{
								Exception e = new Exception(new StringBuilder().append("build roleinfo for resource[resId=").append(resId).append(",operName=").append(operName).append(",restypeId=").append(restypeId).append("]failed:types[").append(types).append("]").append("roleid[").append(role_id).append("] not exist.").toString());
								RoleManagerImpl.logger.error("build roleinfo failed:", e);
								return;
							}
						}
						else if(types.equals("organization"))
						{
							Organization  org = OrgCacheManager.getInstance().getOrganization(role_id);
							if(org != null)
							{
								String org_name = org.getOrgName();
								role.setRoleName(org_name);
							}
							else
							{
								Exception e = new Exception(new StringBuilder().append("build roleinfo for resource[resId=").append(resId).append(",operName=").append(operName).append(",restypeId=").append(restypeId).append("]failed:types[").append(types).append("]").append("roleid[").append(role_id).append("] not exist.").toString());
								RoleManagerImpl.logger.error("build roleinfo failed:", e);
								return;
							}
						}
						authRoles.add(role);
					}	
					catch(Exception e)
					{
						RoleManagerImpl.logger.error(new StringBuilder().append("build roleinfo for resource[resId=").append(resId).append(",operName=").append(operName).append(",restypeId=").append(restypeId).append("]failed:types[").append(types).append("]").append("roleid[").append(role_id).append("] failed:").toString(), e);
					}
				}
				
			}, sql, operName,resId,restypeId);
		} catch (SQLException e) {
			throw new ManagerException(e.getMessage(),e);
		}
		if(authRoles.size() > 0)
		{
			AuthRole[] temp = new AuthRole[authRoles.size()];
			authRoles.toArray(temp);
			return temp;
		}
		else
			return null;

	}
	
	
	public List getAllRoleHasPermissionInResource(String resId,
			String operName, String restypeId) throws ManagerException {
//		if(true){
//			throw new ManagerException("yichang !!");
//		}
		
		List list = new ArrayList();
//		DBUtil dbUtil = new DBUtil();
		StringBuffer rolesql = new StringBuffer();
		rolesql.append("select ROLE_ID as id,ROLE_NAME as name,'role' as roletype from TD_SM_ROLE where ROLE_ID in ")
			.append("(select role_id from TD_SM_ROLERESOP where OP_ID=#[operName] and RES_ID=#[resId]")
			
			.append(" and RESTYPE_ID=#[restypeId]")
			
			.append( " and types='role')")
			.append(" union all ")
			.append( "select user_ID")
			.append(SQLManager.getInstance().getDBAdapter().getOROPR()
				+ "'' as id,USER_NAME as name,'user' as roletype from TD_SM_USer where user_ID in "
				+ "(select role_id from TD_SM_ROLERESOP where OP_ID=#[operName] and RES_ID=#[resId] and RESTYPE_ID=#[restypeId] and types='user')")
				.append(" union all ")
				.append("select org_id"
				+ SQLManager.getInstance().getDBAdapter().getOROPR())
				.append("'' as id,org_name as name,'organization' as roletype from td_sm_organization where org_id in ")
				.append("(select role_id from TD_SM_ROLERESOP where OP_ID=#[operName] and RES_ID=#[resId] and RESTYPE_ID=#[restypeId] and types='organization')");
		
//		String sql = rolesql + " union all " + usersql + " union all " + orgsql;
		
		try {
			SQLParams params = new SQLParams(); 
			params.addSQLParam("operName", operName, SQLParams.STRING);
			params.addSQLParam("resId", resId, SQLParams.STRING);
			params.addSQLParam("restypeId", restypeId, SQLParams.STRING);
			list = SQLExecutor.queryListBeanByRowHandler(new RowHandler<Role>(){

				@Override
				public void handleRow(Role role, Record record)
						throws Exception {
					role.setRoleId(record.getString( "id"));
					role.setRoleName(record.getString( "name"));
					role.setRoleType(record.getString( "roletype"));
				}}, Role.class, rolesql.toString(), params);
//			dbUtil.executeSelect(sql);
//			for (int i = 0; i < dbUtil.size(); i++) {
//				Role role = new Role();
//				role.setRoleId(dbUtil.getString(i, "id"));
//				role.setRoleName(dbUtil.getString(i, "name"));
//				role.setRoleType(dbUtil.getString(i, "roletype"));
//				list.add(role);
//
//			}
		} catch (SQLException e) {
			throw new ManagerException(e.getMessage());

		}
		//add by 20080721 gao.tang 添加异常Exception抛出
		catch(Exception e){
			e.printStackTrace();
			throw new ManagerException(e.getMessage());
		}
		// try {
		// Parameter p = new Parameter();
		// p.setCommand(Parameter.COMMAND_GET);
		// // p.setObject("from Role role where role.roleId in ("
		// // + "select rro.id.roleId from Roleresop rro where rro.id.opId = "
		// // + "(select o.opId from Operation o where
		// // o.opName='"+operName+"')"
		// // + " and rro.id.resId='" + resId
		// // + "' and rro.id.restypeId='" + restypeId + "')");
		// p
		// .setObject("from Role role where role.roleId in ("
		// + "select rro.id.roleId from Roleresop rro where rro.id.opId = '"
		// + operName + "'" + " and rro.id.resId='" + resId
		// + "' and rro.id.restypeId='" + restypeId + "')");
		// list = (List) cb.execute(p);
		// } catch (ControlException e) {
		// logger.error(e);
		// throw new ManagerException(e.getMessage());
		// }

		return list;
	}


	/**
	 * 没有被使用的方法
	 * @deprecated 不推荐使用的方法，方法实现已经被注释掉
	 */
	public List getRoleListByOrgRole(User user) throws ManagerException {
		List list = new ArrayList();
//		try {
//			Parameter p = new Parameter();
//			p.setCommand(Parameter.COMMAND_GET);
//			p
//					.setObject("from Role role where role.roleId in ("
//							+ "select or.id.roleId from Orgrole or where or.id.orgId in "
//							+ " (select ujo.id.orgId from Userjoborg ujo where ujo.id.userId = '"
//							+ user.getUserId() + "'))");
//			list = (List) cb.execute(p);
//		} catch (ControlException e) {
//			logger.error(e);
//			throw new ManagerException(e.getMessage());
//		}

		return list;
	}

	/**
	 * 获取机构已有的角色列表
	 * 危达，200711161014
	 * @param Organization
	 * @return TList
	 */
	public List getRoleList(Organization org) throws ManagerException {
		List list = new ArrayList();
		String sql = "select * from td_sm_role t where t.role_id in (select d.role_id from td_sm_orgrole d where d.org_id=?) order by t.role_name";
		PreparedDBUtil dBUtil = new PreparedDBUtil();
		try{
			dBUtil.preparedSelect(sql);
			dBUtil.setString(1, org.getOrgId());
			list = dBUtil.executePreparedForList(Role.class, new RowHandler<Role>(){

				@Override
				public void handleRow(Role role, Record dBUtil)
						throws Exception {
					role.setRemark1(dBUtil.getString(  "remark1"));
					role.setRemark2(dBUtil.getString(  "remark2"));
					role.setRemark3(dBUtil.getString(  "remark3"));
					role.setRoleDesc(dBUtil.getString(  "ROLE_DESC"));
					role.setRoleId(dBUtil.getString(  "ROLE_ID"));
					role.setRoleName(dBUtil.getString(  "ROLE_NAME"));
					role.setRoleType(dBUtil.getString(  "ROLE_TYPE"));
					role.setRoleUsage(dBUtil.getString(  "ROLE_USAGE"));
					
				}
				
			});
//			if(dBUtil.size() > 0){
//				for(int i=0;i<dBUtil.size();i++)
//				{
//					Role role = new Role();					
//					role.setRemark1(dBUtil.getString(i, "remark1"));
//					role.setRemark2(dBUtil.getString(i, "remark2"));
//					role.setRemark3(dBUtil.getString(i, "remark3"));
//					role.setRoleDesc(dBUtil.getString(i, "ROLE_DESC"));
//					role.setRoleId(dBUtil.getString(i, "ROLE_ID"));
//					role.setRoleName(dBUtil.getString(i, "ROLE_NAME"));
//					role.setRoleType(dBUtil.getString(i, "ROLE_TYPE"));
//					role.setRoleUsage(dBUtil.getString(i, "ROLE_USAGE"));
//					list.add(role);
//				}
//			}							
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * 去掉hibernate后的方法
	 * 王卓添加 2006-4-21 * (non-Javadoc)
	 * 
	 * @see com.frameworkset.platform.sysmgrcore.manager.RoleManager#deleteGrouprole(com.frameworkset.platform.sysmgrcore.entity.Role)
	 */
	public boolean deleteOrgrole(Role role) throws ManagerException {
		boolean r = false;

//		if (role != null) {
//			try {
//				Parameter p = new Parameter();
//				p.setCommand(Parameter.COMMAND_DELETE);
//
//				// 删除机构与角色的关系
//				p.setObject("from Orgrole or where or.id.roleId = '"
//						+ role.getRoleId() + "'");
//				if (cb.execute(p) != null)
//					r = true;
//				Event event = new EventImpl(role.getRoleName(),
//						ACLEventType.ORGUNIT_ROLE_CHANGE);
//				super.change(event);
//			} catch (ControlException e) {
//				logger.error(e);
//				throw new ManagerException(e.getMessage());
//			}
//		}
		String sql = "delete td_sm_orgrole where role_id='"+role.getRoleId()+"'";
		DBUtil db = new DBUtil();
		try {
			db.executeDelete(sql);
			r = true;
			Event event = new EventImpl(role.getRoleName(),
					ACLEventType.ORGUNIT_ROLE_CHANGE);
			super.change(event);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return r;
	}

	/*
	 * 尹标平添加 2006-4-21 删除角色对应的所以用户 * (non-Javadoc)
	 * 
	 * @see com.frameworkset.platform.sysmgrcore.manager.RoleManager#deleteGrouprole(com.frameworkset.platform.sysmgrcore.entity.Role)
	 */
	public boolean deleteUserOfRole(Role role) throws ManagerException {
		boolean r = false;

		if (role != null) {
			return deleteUserOfRole(role.getRoleId());
		}

		return r;
	}

	/*
	 * 尹标平添加 2007-5-26 删除角色对应的所以用户 * (non-Javadoc)
	 * 
	 * @see com.frameworkset.platform.sysmgrcore.manager.RoleManager#deleteGrouprole(com.frameworkset.platform.sysmgrcore.entity.Role)
	 */
	public boolean deleteUserOfRole(String roleid) throws ManagerException {
		boolean r = false;

		if (roleid != null && !roleid.equals("")) {
			try {
				String deleteroleuser = "delete from td_sm_userrole where role_id="
						+ roleid;
				DBUtil dbUtil = new DBUtil();
				dbUtil.executeDelete(deleteroleuser);
				Event event = new EventImpl(roleid,
						ACLEventType.USER_ROLE_INFO_CHANGE);
				super.change(event);
				return true;
			} catch (Exception e) {
				throw new ManagerException("删除角色用户失败：" + e.getMessage());
			}
		}

		return r;
	}
	
	private String build(String[] ids)
	{
		String ret = "";
		for(int i = 0; i < ids.length; i ++)
		{
			if(i == 0)
			{
				ret += "'" + ids[i] + "'";
			}
			else
			{
				ret += ",'" + ids[i] + "'";
			}
		}
		return ret;
	}
	
	/*
	 * 尹标平添加 2007-11-26 删除角色对应的所以用户 * (non-Javadoc)
	 * 
	 * @see com.frameworkset.platform.sysmgrcore.manager.RoleManager#deleteGrouprole(com.frameworkset.platform.sysmgrcore.entity.Role)
	 */
	public boolean deleteUsersOfRole(String userids[],String roleid) throws ManagerException {
		boolean r = false;
		if(userids == null || userids.length == 0)
			return true;
		if (roleid != null && !roleid.equals("")) {
			try {
				int[] ids = new int[userids.length];
				for(int i =0;i < userids.length; i ++)
				{
					ids[i] = Integer.parseInt(userids[i]);
				}
				SQLParams params = new SQLParams();
				params.addSQLParam("roleid", roleid, SQLParams.STRING);
				params.addSQLParam("userids", ids, SQLParams.OBJECT);
				this.executor.deleteBean("deleteUsersOfRole", params);
//				String deleteroleuser = "delete from td_sm_userrole where role_id="
//						+ roleid + " and user_id in (" + build(userids) + ")";
//				DBUtil dbUtil = new DBUtil();
//				dbUtil.executeDelete(deleteroleuser);
				Event event = new EventImpl(roleid,
						ACLEventType.USER_ROLE_INFO_CHANGE);
				super.change(event);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				throw new ManagerException("删除角色用户失败：" + e.getMessage());
			}
		}

		return r;
	}

	public boolean hasGrantedRoles(String resourceType, String resourceID) throws ManagerException{
//		String sql = "select count(1) from td_sm_roleresop where res_id='"
//				+ resourceID + "' and restype_id='" + resourceType + "'";
		String sql = "select count(1) from td_sm_roleresop where res_id=? and restype_id=?";
		PreparedDBUtil dbUtil = new PreparedDBUtil();
		try {
			return SQLExecutor.queryObject(int.class, sql, resourceID,resourceType) > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ManagerException("执行sql[ " + sql +"]失败：" + e.getMessage());
		}
		
	}

	public List getRoleList(String hql) throws ManagerException {
		List list = new ArrayList();
		DBUtil dBUtil = new DBUtil();
		try{
			dBUtil.executeSelect(hql);
			if(dBUtil.size() > 0){
				list = this.getRoles(dBUtil);
			}		
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 用于 菜单管理-菜单资源授权-角色列表分页
	 * 2007.12.26
	 * */
	public ListInfo getRoleList(String hql, long offset, int pageItemsize) throws ManagerException {
		List list = new ArrayList();
		ListInfo listInfo  = new ListInfo();
		DBUtil dBUtil = new DBUtil();
		try{
			dBUtil.executeSelect(hql, offset, pageItemsize);
			if(dBUtil.size() > 0){
				list = this.getRoles(dBUtil);
				listInfo.setDatas(list);
				listInfo.setTotalSize(dBUtil.getTotalSize());
			}					
		}catch(Exception e){
			e.printStackTrace();
		}
		return listInfo;
	}

	public List getAllRoleList() throws ManagerException {
		DBUtil dbUtil = new DBUtil();
		List list = new ArrayList();
		try {
			dbUtil.executeSelect("select * from td_sm_role order by role_id asc");
			for (int i = 0; i < dbUtil.size(); i++) {
				Role role = new Role();
				role.setRoleId(dbUtil.getString(i, "role_id"));
				role.setRoleName(dbUtil.getString(i, "role_name"));
				role.setRoleDesc(dbUtil.getString(i, "role_desc"));
				role.setRoleUsage(dbUtil.getString(i, "ROLE_USAGE"));
				role.setRoleType("role");
				role.setRemark1(dbUtil.getString(i, "REMARK1"));
				role.setRemark2(dbUtil.getString(i, "REMARK2"));
				role.setRemark3(dbUtil.getString(i, "REMARK3"));
				list.add(role);

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;
	}
	
	public List getAllRoleListBysql(String sql) throws ManagerException {
		DBUtil dbUtil = new DBUtil();
		List list = new ArrayList();
		try {
			dbUtil.executeSelect(sql);
			for (int i = 0; i < dbUtil.size(); i++) {
				Role role = new Role();
				role.setRoleId(dbUtil.getString(i, "role_id"));
				role.setRoleName(dbUtil.getString(i, "role_name"));
				role.setRoleDesc(dbUtil.getString(i, "role_desc"));
				role.setRoleUsage(dbUtil.getString(i, "ROLE_USAGE"));
				role.setRoleType("role");
				role.setRemark1(dbUtil.getString(i, "REMARK1"));
				role.setRemark2(dbUtil.getString(i, "REMARK2"));
				role.setRemark3(dbUtil.getString(i, "REMARK3"));
				list.add(role);

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;
	}
	
	/**
	 * 获取根据权限过滤的角色列表
	 * @param accessControl 访问控制器
	 * @param operation 角色操作
	 * @param sql 数据库语句
	 * @return 
	 * @throws ManagerException
	 */
	public List getAllRoleListBysql(AccessControl accessControl,String operation,String sql) throws ManagerException {
		DBUtil dbUtil = new DBUtil();
		List list = new ArrayList();
		try {
			dbUtil
					.executeSelect(sql);
			for (int i = 0; i < dbUtil.size(); i++) {
				if(accessControl.checkPermission(dbUtil.getString(i, "role_id"),
						operation,
						AccessControl.ROLE_RESOURCE))
				{
					Role role = new Role();
					role.setRoleId(dbUtil.getString(i, "role_id"));
					role.setRoleName(dbUtil.getString(i, "role_name"));
					role.setRoleDesc(dbUtil.getString(i, "role_desc"));
					role.setRoleUsage(dbUtil.getString(i, "ROLE_USAGE"));
					role.setRoleType("role");
					role.setRemark1(dbUtil.getString(i, "REMARK1"));
					role.setRemark2(dbUtil.getString(i, "REMARK2"));
					role.setRemark3(dbUtil.getString(i, "REMARK3"));
					list.add(role);
				}

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;
	}

	public boolean copyResOfRole(String rolecopyId, String[] id)
			throws ManagerException {
//		DBUtil db = new DBUtil();
		PreparedDBUtil preparedDBUtil = new PreparedDBUtil();
		
		try {
			if (id != null) {
//				StringBuilder sql = new StringBuilder();
//				sql.append("insert into td_sm_roleresop(op_id,res_id,res_name,restype_id,types,role_id) ")
//					.append(" select b.op_id,b.res_id,b.res_name,b.restype_id,b.types,? from ")
//					.append(" td_sm_roleresop b  where b.role_id = ?  minus select ")
//					.append(" c.op_id,c.res_id,c.res_name,c.restype_id,c.types,c.role_id from td_sm_roleresop c ")
//					.append(" where c.role_id= ?");
				String sql = sqlUtilInsert.getSQL("roleManagerImpl_copyResOfRole");
				for (int i = 0; i < id.length; i++) {
					preparedDBUtil.preparedInsert(sql);
					preparedDBUtil.setString(1, id[i]);
					preparedDBUtil.setString(2, rolecopyId);
					preparedDBUtil.setString(3, id[i]);
					preparedDBUtil.setString(4, id[i]);
//					String sql = "insert into td_sm_roleresop(op_id,res_id,res_name,restype_id,types,role_id) "
//							+ " select b.op_id,b.res_id,b.res_name,b.restype_id,b.types,'"
//							+ id[i]
//							+ "' from "
//							+ " td_sm_roleresop b  where b.role_id ='"
//							+ rolecopyId
//							+ "' minus select "
//							+ " c.op_id,c.res_id,c.res_name,c.restype_id,c.types,c.role_id from td_sm_roleresop c "
//							+ " where c.role_id='" + id[i] + "'";
//					System.out.println(sql);
					preparedDBUtil.addPreparedBatch();
//					db.addBatch(sql);
				}
//				db.executeBatch();
				preparedDBUtil.executePreparedBatch();
			}
			Event event = new EventImpl("",
					ACLEventType.RESOURCE_ROLE_INFO_CHANGE);
			super.change(event);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
//			db.resetBatch();
			preparedDBUtil.resetBatch();
		}
		return false;
	}
	
	public boolean copyResOfRoleSelf(String rolecopyId, String[] id) throws ManagerException {
		if(id == null){
			return false;
		}
		DBUtil db = new DBUtil();
		boolean state = false;
		try{
			for(int i = 0; i < id.length; i++){
				StringBuffer sql = new StringBuffer();
//				sql.append("insert into td_sm_roleresop(op_id,res_id,res_name,restype_id,types,role_id) ")
//					.append(" select b.op_id,b.res_id,b.res_name,b.restype_id,b.types,'").append(rolecopyId)
//					.append("' from  td_sm_roleresop b  where b.role_id ='").append(id[i]).append("' ")
//					.append("minus select  c.op_id,c.res_id,c.res_name,c.restype_id,c.types,c.role_id from ")
//					.append(" td_sm_roleresop c  where c.role_id='").append(rolecopyId).append("'");
//				db.addBatch(sql.toString());
				sql.append("insert into td_sm_roleresop(op_id,res_id,res_name,restype_id,types,role_id) ")
				.append("  select b.op_id,b.res_id,b.res_name,b.restype_id,b.types,'").append(rolecopyId)
				.append("' from  td_sm_roleresop b where b.role_id ='").append(id[i]).append("' ")
				.append("and not exists ( select  c.op_id,c.res_id,c.res_name,c.restype_id,c.types,c.role_id from  td_sm_roleresop c ")
				.append(" where c.role_id='11' and b.op_id=c.op_id and b.res_id=c.res_id and b.res_name=c.res_name  and b.restype_id=c.restype_id and b.types=c.types and c.role_id='")
				.append(rolecopyId).append("')");
			db.addBatch(sql.toString());
//				System.out.println(sql.toString());
			}
			
			db.executeBatch();
			state = true;
			Event event = new EventImpl("",
					ACLEventType.RESOURCE_ROLE_INFO_CHANGE);
			super.change(event);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			db.resetBatch();
		}
		return state;
	}

	/**
	 * 对角色授权接口
	 * 
	 * @param roleid
	 * @param resourceid
	 * @param resourceType
	 * @param action
	 * @param roleType
	 */
	public void grantRolePermission(String roleid, String resid,
			String resTypeid, String opid, String resname)
			throws ManagerException {
		DBUtil db = new DBUtil();
		try {
			String sql = "insert into TD_SM_ROLERESOP(OP_ID,RES_ID,ROLE_ID,RESTYPE_ID,RES_NAME,TYPES) "
					+ " values('"
					+ opid
					+ "','"
					+ resid
					+ "','"
					+ roleid
					+ "','" + resTypeid + "','" + resname + "','user')";
			db.executeInsert(sql);
			Event event = new EventImpl("",
					ACLEventType.RESOURCE_ROLE_INFO_CHANGE);
			super.change(event);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * 获取有资源操作权限的所有用户列表
	 */
	public List getAllUserOfHasPermission(String resid, String resOpr,
			String resType) throws ManagerException {
		DBUtil db = new DBUtil();
		// AccessControl control = AccessControl.getInstance();
		// 取预算单位主管处室主管岗位下的人
		String userSql = "select user_id,USER_NAME,USER_REALNAME from td_sm_user where user_id in("
				+ "select user_id from td_sm_userrole where role_id in("
				+ " select role_id from td_sm_roleresop where op_id = '"
				+ resOpr
				+ "' and res_id = '"
				+ resid
				+ "' and restype_id='"
				+ resType + "' and types='role'))";

		String orgSql = "select user_id,USER_NAME,USER_REALNAME from td_sm_user where user_id in("
				+ "select user_id from TD_SM_USERJOBORG where ORG_ID in("
				+ " select ORG_ID from TD_SM_ORGROLE where role_id in ("
				+ " select role_id from td_sm_roleresop where op_id = '"
				+ resOpr
				+ "' and res_id = '"
				+ resid
				+ "' and restype_id='"
				+ resType + "' and types='role')))";

		String orgroleSql = "select user_id,USER_NAME,USER_REALNAME from td_sm_user where user_id in("
				+ "select user_id from TD_SM_USERJOBORG where ORG_ID in("
				+ " select role_id from td_sm_roleresop where op_id = '"
				+ resOpr
				+ "' and res_id = '"
				+ resid
				+ "' and restype_id='"
				+ resType + "' and types='organization'))";

		String groupSql = "select user_id,USER_NAME,USER_REALNAME from td_sm_user where user_id in("
				+ "select user_id from TD_SM_USERGROUP where GROUP_ID in("
				+ " select GROUP_ID from TD_SM_GROUPROLE where role_id in ("
				+ " select role_id from td_sm_roleresop where op_id = '"
				+ resOpr
				+ "' and res_id = '"
				+ resid
				+ "' and restype_id='"
				+ resType + "' and types='role')))";

		String userselfSql = "select user_id,USER_NAME,USER_REALNAME from td_sm_user where user_id in("
				+ " select role_id from td_sm_roleresop where op_id = '"
				+ resOpr
				+ "' and res_id = '"
				+ resid
				+ "' and restype_id='"
				+ resType + "' and types='user')";

		String adminUsers = "select user_id,USER_NAME,USER_REALNAME from td_sm_user where user_id in("
				+ " select user_id from td_sm_userrole where role_name ='administrator' )";

		String sql = userSql + " union " + orgSql + " union " + groupSql
				+ " union " + userselfSql + " union " + adminUsers + " union "
				+ orgroleSql;

		// System.out.println(sql);

		List retUsers = new ArrayList();

		try {
			db.executeSelect(sql);
			// System.out.println(db.size());
			for (int i = 0; i < db.size(); i++) {

				// String userName = db.getString(i, "USER_NAME");
				//					
				// if(control.checkPermission(new
				// AuthPrincipal(userName,null,null),resid,resOpr,resType))
				// {
				User user = new User();
				int userid = db.getInt(i, "user_id");
				user.setUserId(new Integer(userid));
				user.setUserName(db.getString(i, "USER_NAME"));
				user.setUserRealname(db.getString(i, "USER_REALNAME"));
				retUsers.add(user);
				// }
			}
			return retUsers;
		} catch (SQLException e) {
			throw new ManagerException("获取有资源操作权限[resid=" + resid + ",operation=" +resOpr + ","
					+ "resType=" + resType + "]的所有用户列表失败:" + e.getMessage());
			
		}
		catch (Exception e) {
			throw new ManagerException("获取有资源操作权限[resid=" + resid + ",operation=" +resOpr + ","
					+ "resType=" + resType + "]的所有用户列表失败:" + e.getMessage());
			
		}

		
	}

	public List getAllResource(String userid, String opid, String resType)
			throws ManagerException {
		DBUtil db = new DBUtil();
		String orgrole = "select org_id,org_name,remark5 from TD_SM_ORGANIZATION where org_id in( "
				+ "select res_id from td_sm_roleresop where op_id ='"
				+ opid
				+ "' and "
				+ "restype_id ='"
				+ resType
				+ "' and types ='role' and role_id in( "
				+ "select role_id from td_sm_userrole where user_id = "
				+ userid + "))";
		String orguser = "select org_id,org_name,remark5 from TD_SM_ORGANIZATION where org_id in( "
				+ "select res_id from td_sm_roleresop where op_id ='"
				+ opid
				+ "' "
				+ "and restype_id ='"
				+ resType
				+ "' "
				+ "and types ='user' " + "and role_id ='" + userid + "')";
		String sql = orgrole + " union " + orguser;
		List retOrgs = new ArrayList();
		try {
			db.executeSelect(sql);

			for (int i = 0; i < db.size(); i++) {

				Organization org = new Organization();
				org.setOrgId(db.getString(i, "org_id"));
				org.setOrgName(db.getString(i, "org_name"));
				org.setRemark5(db.getString(i, "remark5"));
				retOrgs.add(org);

			}
			return retOrgs;
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return retOrgs;

	}

	/**
	 * 通过用户id获取用户的所有角色（包括用户自身的，以及从机构和用户组、用户岗位继承的角色）
	 * @return List<Role>
	 */
	public List getAllRoleList(String userId) throws ManagerException {
		List list = null;
		StringBuffer sql = new StringBuffer();
		
		try {
			

//			sql.append("select t.* from td_sm_role t where t.role_id in (")
//			//获取用户自身角色
//			.append("select role_id from td_sm_userrole where user_id=")
//			.append(userId)
//			.append(" union ")
//			//获取用户隶属用户组角色
//			.append("select a.role_id from td_sm_grouprole a inner join td_sm_usergroup b on a.group_id = b.group_id where b.user_id=")
//			.append(userId)
//			.append(" union ")
//			//获取用户隶属机构角色
//			.append("select a.role_id from td_sm_orgrole a inner join td_sm_userjoborg b on a.org_id = b.org_id where b.user_id=")
//			.append(userId)
//			.append(" union ")
//			//获取用户隶属机构岗位角色
//			.append("select a.role_id from td_sm_orgjobrole a inner join td_sm_userjoborg b on a.org_id = b.org_id and a.job_id = b.job_id where b.user_id=")
//			.append(userId)
//			.append(")");
			
			sql.append("select t.* from td_sm_role t where t.role_id in (")
			//获取用户自身角色
			.append("select role_id from td_sm_userrole where user_id=?")
//			.append(userId)
			.append(" union ")
			//获取用户隶属用户组角色
			.append("select a.role_id from td_sm_grouprole a inner join td_sm_usergroup b on a.group_id = b.group_id where b.user_id=?")
//			.append(userId)
			.append(" union ")
			//获取用户隶属机构角色
			.append("select a.role_id from td_sm_orgrole a inner join td_sm_userjoborg b on a.org_id = b.org_id where b.user_id=?")
//			.append(userId)
			.append(" union ")
			//获取用户隶属机构岗位角色
			.append("select a.role_id from td_sm_orgjobrole a inner join td_sm_userjoborg b on a.org_id = b.org_id and a.job_id = b.job_id where b.user_id=?")
//			.append(userId)
			.append(")");
//			DBUtil dbUtil = new DBUtil();
//			System.out.println(sql.toString());
			list =  SQLExecutor.queryListByRowHandler(new RowHandler<Role>(){

				@Override
				public void handleRow(Role role, Record record)
						throws Exception {
					
					role.setRoleId(record.getString( "role_id"));
					role.setRoleName(record.getString( "role_name"));
					role.setRoleDesc(record.getString( "role_desc"));
					role.setRoleUsage(record.getString( "ROLE_USAGE"));
					role.setRoleType("role");
					role.setRemark1(record.getString( "REMARK1"));
					role.setRemark2(record.getString( "REMARK2"));
					role.setRemark3(record.getString( "REMARK3"));
					
				}
				
			}, Role.class, sql.toString(), userId,userId,userId,userId);
//			dbUtil.executeSelect(sql.toString());
//			list =  new ArrayList();
//			for (int i = 0; i < dbUtil.size(); i++) {
//				
//					Role role = new Role();
//					role.setRoleId(dbUtil.getString(i, "role_id"));
//					role.setRoleName(dbUtil.getString(i, "role_name"));
//					role.setRoleDesc(dbUtil.getString(i, "role_desc"));
//					role.setRoleUsage(dbUtil.getString(i, "ROLE_USAGE"));
//					role.setRoleType("role");
//					role.setRemark1(dbUtil.getString(i, "REMARK1"));
//					role.setRemark2(dbUtil.getString(i, "REMARK2"));
//					role.setRemark3(dbUtil.getString(i, "REMARK3"));
//					list.add(role);
//			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ManagerException("获取用户[userId="+ userId +"]所有角色失败:" + e.getMessage());
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new ManagerException("获取用户[userId="+ userId +"]所有角色失败:" + e.getMessage());
		}

		return list;
	}

	/**
	 * 通过用户id获取用户自身的角色）
	 */
	public List getRoleListByUserRole(User user) throws ManagerException {
		List list = null;
//		try {
//			Parameter p = new Parameter();
//			p.setCommand(Parameter.COMMAND_GET);
//			p
//					.setObject("from Role role where role.roleId in ("
//							+ "select ur.id.roleId from Userrole ur where ur.id.userId = '"
//							+ user.getUserId() + "')");
//			list = (List) cb.execute(p);
//		} catch (ControlException e) {
//			logger.error(e);
//			throw new ManagerException(e.getMessage());
//		}
		PreparedDBUtil db = new PreparedDBUtil();
//		String sql = "select b.* from td_sm_userrole a,td_sm_role b where a.role_id=b.role_id and user_id='"+user.getUserId()+"' order by b.ROLE_TYPE,b.ROLE_NAME";
		String sql = "select b.* from td_sm_userrole a,td_sm_role b where a.role_id=b.role_id and user_id=? order by b.ROLE_TYPE,b.ROLE_NAME";
		try {
			db.preparedSelect(sql);
			db.setInt(1, user.getUserId());
			list = db.executePreparedForList(Role.class, new RowHandler<Role>() {

				@Override
				public void handleRow(Role role, Record record)
						throws Exception {
					role.setRoleId(record.getString( "role_id"));
					role.setRoleName(record.getString( "role_name"));
					role.setRoleType(record.getString( "role_type"));
					
				}
			});
			
//			Role role = null;
//			if(db.size() > 0){
//				list = new ArrayList();
//				for(int i = 0; i < db.size(); i++){
//					role = new Role();
//					role.setRoleId(db.getString(i,"role_id"));
//					role.setRoleName(db.getString(i,"role_name"));
//					role.setRoleType(db.getString(i,"role_type"));
//					list.add(role);
//				}
//			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public boolean hasGrantRole(AuthRole role, String resource,
			String resourceType) throws ManagerException {
		boolean r = false;
		PreparedDBUtil db = new PreparedDBUtil();
		String sql = "";
		try {
			sql = "select count(1) from td_sm_roleresop where role_id=? and types=? and RES_ID=? and RESTYPE_ID=?";
//			db.preparedSelect(sql);
//			db.setString(1, role.getRoleId());
//			db.setString(2, role.getRoleType());
//			db.setString(3, resource);
//			db.setString(4, resourceType);
//			db.executePrepared();
			int count = SQLExecutor.queryObject(int.class, sql, role.getRoleId(),role.getRoleType(),resource,resourceType);
			if (count > 0) {
				r = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ManagerException("执行sql[" + sql + "]失败：" + e.getMessage() );
		} catch (Exception e) {
			e.printStackTrace();
			throw new ManagerException("执行sql[" + sql + "]失败：" + e.getMessage() );
		}

		return r;

	}

	public boolean storeRoleresop(String opid, String resid, String roleid,
			String restypeid, String resname, String types,boolean sendevent)
			throws ManagerException {
		boolean b = false;
		String sql = "select count(op_id) totalsize from td_sm_roleresop where OP_ID=? and RES_ID=? and ROLE_ID=? and TYPES=? and RESTYPE_ID=?";
		try {
			PreparedDBUtil db = new PreparedDBUtil();
			db.preparedSelect(sql);
			db.setString(1, opid);
			db.setString(2, resid);
			db.setString(3, roleid);
			db.setString(4, types);
			db.setString(5, restypeid);
			db.executePrepared();
			if(db.getInt(0, 0) <= 0)
			{
				sql = "insert into td_sm_roleresop(OP_ID,RES_ID,ROLE_ID,RESTYPE_ID,RES_NAME,TYPES)"
						+ " values(?,?,?,?,?,?)";
				db.preparedInsert(sql);
				db.setString(1, opid);
				db.setString(2, resid);
				db.setString(3, roleid);
				db.setString(4, restypeid);
				db.setString(5, resname);
				db.setString(6, types);
				db.executePrepared();
			}
		
		
		// String sqlselect = "select count(*) from td_sm_roleresop where " +
		// "OP_ID='"+opid+"' and RES_ID='"+resid+"' and ROLE_ID='"+roleid+"'" +
		// " and RESTYPE_ID='"+restypeid+"' and RES_NAME='"+resname+"' and
		// TYPES='"+types+"'" ;
		// System.out.println("=="+sql);
		
			// db.executeSelect(sqlselect);
			// if(db.getInt(0,0)==0){
			
			b = true;
			// }
			if(sendevent)
			{
				EventUtil.sendRESOURCE_ROLE_INFO_CHANGEEvent();
			}
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			// e.printStackTrace();
		}
		return b;

	}
	

	public boolean deletePermissionOfRole(String resId, String restypeId,
			String roleid, String type,boolean sendevent) throws ManagerException {
		boolean b = false;
		DBUtil db = new DBUtil();
		String sql = "delete from td_sm_roleresop where RES_ID='" + resId
				+ "' and " + "RESTYPE_ID='" + restypeId + "' and TYPES='"
				+ type + "' and role_id='" + roleid + "'";

		// System.out.println("=="+sql);
		try {
			db.executeDelete(sql);
			b = true;
			if(sendevent)
			{
//				Event event = new EventImpl("",
//						ACLEventType.RESOURCE_ROLE_INFO_CHANGE);
//				super.change(event);
				EventUtil.sendRESOURCE_ROLE_INFO_CHANGEEvent();
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return b;
	}

	public boolean deletePermissionOfRole(String opid, String resId,
			String restypeId, String roleid, String type,boolean sendevent)
			throws ManagerException {
		boolean b = false;
		DBUtil db = new DBUtil();
		String sql = "delete from td_sm_roleresop where op_id='" + opid
				+ "' and RES_ID='" + resId + "' and " + "RESTYPE_ID='"
				+ restypeId + "' and TYPES='" + type + "' and role_id='"
				+ roleid + "'";

		// System.out.println("=="+sql);
		try {
			db.executeDelete(sql);
			b = true;
			if(sendevent)
				EventUtil.sendRESOURCE_ROLE_INFO_CHANGEEvent();
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return b;
	}

	public boolean storeRoleresop(String[] opids, String resid, String roleid,
			String restypeid, String resname, String roletype) throws ManagerException
	{
		return storeRoleresop(opids, resid, roleid,
				restypeid, resname, roletype,true);
	}
//	public boolean storeRoleresop(String[] opids, String resid, String roleid,
//			String restypeid, String resname, String roletype,boolean broadevent)
//			throws ManagerException {
//		boolean b = false;
//		PreparedDBUtil db = new PreparedDBUtil();
//		try {
//			for (int i = 0; i < opids.length; i++) {
//				String sql = "insert all when totalsize <= 0 then into td_sm_roleresop(OP_ID,RES_ID,ROLE_ID,RESTYPE_ID,RES_NAME,TYPES)"
//						+ " values('"
//						+ opids[i]
//						+ "','"
//						+ resid
//						+ "','"
//						+ roleid
//						+ "','"
//						+ restypeid
//						+ "','"
//						+ resname
//						+ "','"
//						+ roletype
//						+ "') select count(OP_ID) totalsize from td_sm_roleresop where OP_ID='"
//						+ opids[i]
//						+ "' and RES_ID='"
//						+ resid
//						+ "' and ROLE_ID='"
//						+ roleid
//						+ "' and TYPES='"
//						+ roletype + "' and RESTYPE_ID='" + restypeid + "'";
//				//System.out.println("=="+sql);
//				db.addBatch(sql);
//
//			}
//			db.executeBatch();
//			b = true;
//			if(broadevent)
//			{
//				Event event = new EventImpl("",
//						ACLEventType.RESOURCE_ROLE_INFO_CHANGE);
//				super.change(event);
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//			throw new ManagerException(e.getMessage());
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new ManagerException(e.getMessage());
//		}finally{
//			db.resetBatch();
//		}
//		return b;
//	}
	
	
	public boolean storeRoleresop(String[] opids, String resid, String roleid,
			String restypeid, String resname, String roletype,boolean broadevent)
			throws ManagerException {
		boolean b = false;
		PreparedDBUtil db = new PreparedDBUtil();
		
		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();
			String sql = "insert into td_sm_roleresop(OP_ID,RES_ID,ROLE_ID,RESTYPE_ID,RES_NAME,TYPES)"
					+ " values(?,?,?,?,?,?) ";
			db.preparedInsert(sql);
			int s = -1;
			for (int i = 0; i < opids.length; i++) {
				int totalsize = SQLExecutor.queryObject(int.class, "select count(OP_ID) totalsize from td_sm_roleresop where OP_ID=? and RES_ID=? and ROLE_ID=? and TYPES=?"
						+ " and RESTYPE_ID=?", opids[i],resid,roleid,roletype,restypeid);
				if(totalsize <=0)
				{
					s ++;
					db.setString(1, opids[i]);
					db.setString(2, resid);
					db.setString(3, roleid);
					db.setString(4, restypeid);
					db.setString(5, resname);
					db.setString(6, roletype);
					 
						 
					//System.out.println("=="+sql);
					db.addPreparedBatch();
				}

			}
			if(s > 0)
				db.executePreparedBatch();
			tm.commit();
			b = true;
			if(broadevent)
			{
				EventUtil.sendRESOURCE_ROLE_INFO_CHANGEEvent();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ManagerException(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ManagerException(e.getMessage());
		}finally{
			tm.release();
		}
		return b;
	}
	
	
	/**
	 * 给多个角色复权限，如果操作组为null或者长度为0，则删除多个角色相应资源的操作权限
	 * @param opids
	 * @param resid
	 * @param roleids
	 * @param restypeid
	 * @param resname
	 * @param roletype
	 * @param needdelete
	 * @return
	 * @throws ManagerException
	 */
	public boolean grantRoleresop(String[] opids, String resid, String roleids[],
			String restypeid, String resname, String roletype,boolean needdelete)
			throws ManagerException {
		
		
		boolean b = false;
		PreparedDBUtil pd = new PreparedDBUtil();
		
		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();
			StringBuffer sql = new StringBuffer();
			
			if(needdelete || opids == null || opids.length == 0)
			{
				SQLParams params = new SQLParams();
				params.addSQLParam("resid", resid, SQLParams.STRING);
				params.addSQLParam("restypeid", restypeid, SQLParams.STRING);
				params.addSQLParam("roletype", roletype, SQLParams.STRING);
				params.addSQLParam("roleids", roleids, SQLParams.OBJECT);
				executor.deleteBean("grantRoleresop_delete", params);
//				sql.append("delete from td_sm_roleresop where RES_ID='");
//				sql.append(resid)
//				   .append("' and " )
//				   .append( "RESTYPE_ID='" )
//				   .append( restypeid )
//				   .append( "' and TYPES='")
//				   .append(roletype)
//				   .append("' and role_id in (");
//				
//				for(int i = 0; i < roleids.length; i ++)
//				{
//					if(i == 0)
//					{
//						sql.append("'").append(roleids[i] )
//						   .append("'");
//					}
//					else
//					{
//						sql.append(",'").append(roleids[i] )
//						   .append("'");
//					}
//					
//					
//				}
//				sql.append(")");
//				db.executeDelete(sql.toString());
//				db.addBatch(sql.toString());
//				sql.setLength(0);
			}
//			sql.append("insert all when totalsize <= 0 then into td_sm_roleresop(OP_ID,RES_ID,ROLE_ID,RESTYPE_ID,RES_NAME,TYPES)");
			
			
			int len = sql.length();
			PreparedDBUtil preparedDBUtilour = new PreparedDBUtil();
//			PreparedDBUtil tempDBUtil = new PreparedDBUtil();
			String sql1 = sqlUtilInsert.getSQL("roleManagerImpl_grantRoleresop");
			String sql2 = sqlUtilInsert.getSQL("roleManagerImpl_grantRoleresop2");
			boolean state = false;
			for(int j = 0;  j < roleids.length; j ++)
			{
				for (int i = 0; opids != null && i < opids.length; i++) {
//					StringBuffer sql1 = new StringBuffer();
//					System.out.println(sqlUtilInsert.getSQL("roleManagerImpl_grantRoleresop"));
//					sql1.append();
					System.out.println("sql1 = " + sql1.toString());
//					System.out.println(sql1.toString());
					preparedDBUtilour.preparedSelect(sql1.toString());
					preparedDBUtilour.setString(1, opids[i]);
					preparedDBUtilour.setString(2, resid);
					preparedDBUtilour.setString(3, roleids[j]);
					preparedDBUtilour.setString(4, roletype);
					preparedDBUtilour.setString(5, restypeid);
//					System.out.println(sql1.toString());
					preparedDBUtilour.executePrepared();
					
//					preparedDBUtilour.addPreparedBatch();
//					preparedDBUtilour.executePreparedBatch();
					System.out.println("size - " + preparedDBUtilour.size());
					int count = preparedDBUtilour.getInt(0, 0);
					System.out.println("count = " + count);
					if(count<=0)
					{
//						System.out.println("ok");
						
//						StringBuffer sql2 = new StringBuffer();
//						sql2.append(sqlUtilInsert.getSQL("roleManagerImpl_grantRoleresop2"));
						pd.preparedInsert(sql2);
						pd.setString(1,opids[i] );
						pd.setString(2,resid );
						pd.setString(3, roleids[j]);
						pd.setString(4, restypeid);
						pd.setString(5, resname);
						pd.setString(6, roletype);
						pd.addPreparedBatch();
//						pd.executePrepared();
//						tempDBUtil.addPreparedBatch();
//						tempDBUtil.executePreparedBatch();
						System.out.println(sql2);
						state = true;
//						sql2.setLength(0);
//						sql1.setLength(0);
						
						
					}
					
//					sql.append(" values('").append(opids[i])
//					   .append("','")
//					   .append(resid)
//					   .append("','")
//					   .append(roleids[j])
//					   .append("','")
//					   .append(restypeid)
//					   .append("','")
//					   .append(resname)
//					   .append("','")
//					   .append(roletype)
//					   .append("') select count(OP_ID) totalsize from td_sm_roleresop where OP_ID='")
//					   .append(opids[i])
//					   .append( "' and RES_ID='")
//					   .append( resid)
//					   .append( "' and ROLE_ID='")
//					   .append( roleids[j])
//					   .append( "' and TYPES='")
//					   .append( roletype )
//					   .append( "' and RESTYPE_ID='" )
//					   .append( restypeid )
//					   .append( "'");
//					db.addBatch(sql.toString());
//					sql.setLength(0);
				}

			}
			
			//begin log
			if(opids != null && opids.length > 0){
				ResourceManager resourceManager = new ResourceManager();
				StringBuffer roleidstr = new StringBuffer();
				for(int i = 0; i < roleids.length; i++){
					roleidstr.append(roleids[i]);
					if(i < roleids.length -1){
						roleidstr.append(",");
					}
				}
				StringBuffer opidstr = new StringBuffer();
				for(int i = 0; i < opids.length; i++){
					if(resourceManager.getGlobalOperation(restypeid, opids[i]) != null){
						opidstr.append(resourceManager.getGlobalOperation(restypeid, opids[i]).getName());
					}
					if(i < opids.length -1){
						opidstr.append(",");
					}
				}
				LogManager logManager = new LogManagerImpl();
				
				ResourceInfo resourceInfo = resourceManager.getResourceInfoByType(restypeid);
				String resourceName = "";
				if(resourceInfo != null){
					resourceName = resourceInfo.getName();
				}
				StringBuffer operContent = new StringBuffer();
				if(roletype.equals("user")){
					operContent.append("为用户(用户ID)");
				}else if(roletype.equals("role")){
					operContent.append("为角色(角色ID)");
				}else if(roletype.equals("organization")){
					operContent.append("为机构(机构ID)");
				}	
				operContent.append("【").append(roleidstr.toString()).append("】授予").append(resourceName)
					.append("中的全局资源操作项【").append(opidstr).append("】");
				logManager.log(operContent.toString(),"权限管理");
			}
			//end log
//			db.executeBatch();
			if(state){
				pd.executePreparedBatch();
			}
			tm.commit();
			
			b = true;
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
			throw new ManagerException(e.getMessage());
		} catch (Exception e) {
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			throw new ManagerException(e.getMessage());
		}
		return b;
	}
	
	
	/**
	 * *****************************************************************
	 * 用于用户管理的批量资源操作授予
	 * 不采取原有的先删再存的方式，采取只加权限的方式
	 * 2008-01-29 16:19
	 * *****************************************************************
	 * @param opids
	 * @param resid
	 * @param roleids
	 * @param restypeid
	 * @param resname
	 * @param roletype
	 * @param needdelete
	 * @return
	 * @throws ManagerException
	 */
	public boolean grantRoleresopForBatch(String[] opids, String resid, String roleids[],
			String restypeid, String resname, String roletype,boolean needdelete)
			throws ManagerException {
		
		boolean b = false;
		TransactionManager tm = new TransactionManager();
		try {
			
			tm.begin();
			DBUtil db = new DBUtil();
			//记录授权日志
			DBUtil db_log = new DBUtil();
			
			int count = 0;
			for(int j = 0;  j < roleids.length; j ++)
			{
				for (int i = 0; opids != null && i < opids.length; i++) {
					StringBuffer sql = new StringBuffer();
					sql.append("insert all when totalsize <= 0 then into td_sm_roleresop(OP_ID,RES_ID,ROLE_ID,RESTYPE_ID,RES_NAME,TYPES)");
					
					sql.append(" values('").append(opids[i])
					   .append("','")
					   .append(resid)
					   .append("','")
					   .append(roleids[j])
					   .append("','")
					   .append(restypeid)
					   .append("','")
					   .append(resname)
					   .append("','")
					   .append(roletype)
					   .append("') select count(OP_ID) totalsize from td_sm_roleresop where OP_ID='")
					   .append(opids[i])
					   .append( "' and RES_ID='")
					   .append( resid)
					   .append( "' and ROLE_ID='")
					   .append( roleids[j])
					   .append( "' and TYPES='")
					   .append( roletype )
					   .append( "' and RESTYPE_ID='" )
					   .append( restypeid )
					   .append( "'");
					//如果用户已有权限，则插入会发生报错，不捕捉错误，继续执行下一条插入--update  批量执行插入操作
					db.addBatch(sql.toString());
					count ++ ;
					if(count > 900){
						db.executeBatch();
						count = 0;
					}
				}
			}
			db.executeBatch();
			//begin log
			if(opids != null && opids.length > 0){
				ResourceManager resourceManager = new ResourceManager();
				StringBuffer roleidstr = new StringBuffer();
				for(int i = 0; i < roleids.length; i++){
					roleidstr.append(roleids[i]);
					if(i < roleids.length -1){
						roleidstr.append(",");
					}
				}
				StringBuffer opidstr = new StringBuffer();
				for(int i = 0; i < opids.length; i++){
					if(resourceManager.getGlobalOperation(restypeid, opids[i]) != null){
						opidstr.append(resourceManager.getGlobalOperation(restypeid, opids[i]).getName());
					}
					if(i < opids.length -1){
						opidstr.append(",");
					}
				}
				LogManager logManager = new LogManagerImpl();
				
				ResourceInfo resourceInfo = resourceManager.getResourceInfoByType(restypeid);
				String resourceName = "";
				if(resourceInfo != null){
					resourceName = resourceInfo.getName();
				}
				StringBuffer operContent = new StringBuffer();
				if(roletype.equals("user")){
					operContent.append("批量为用户(用户ID)");
				}else if(roletype.equals("role")){
					operContent.append("批量为角色(角色ID)");
				}else if(roletype.equals("organization")){
					operContent.append("批量为机构(机构ID)");
				}
				operContent.append("【").append(roleidstr.toString()).append("】授予").append(resourceName)
					.append("中的全局资源操作项【").append(opidstr).append("】");
				logManager.log(operContent.toString(),"权限管理");
			}
			//end log
			tm.commit();
			
			b = true;
			Event event = new EventImpl("",
					ACLEventType.RESOURCE_ROLE_INFO_CHANGE);
			super.change(event);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				e1.printStackTrace();
			}
			throw new ManagerException(e.getMessage());
		}
		return b;
	}


	public boolean grant(String opids[], String resid, String roleid,
			String restypeid, String resname, String roletype,
			String isRecursive) throws ManagerException {
		boolean b = false;
		DBUtil db = new DBUtil();
		DBUtil dbutil = new DBUtil();
		String str;
		try {
			boolean sendevent = false;

			for (int i = 0; i < opids.length; i++) {
				// 是站点资源并且递归的话
				if ((restypeid.equals("site") && isRecursive.equals("1"))
						|| (restypeid.equals("sitetpl") && isRecursive
								.equals("1"))
						|| (restypeid.equals("sitefile") && isRecursive
								.equals("1"))) {
					str = "SELECT t.site_id,t.name FROM TD_cms_site t START WITH "
							+ " t.site_id="
							+ resid
							+ " CONNECT BY PRIOR t.site_id=t.mainsite_ID";
					dbutil.executeSelect(str);
					if (dbutil.size() > 0) {
						for (int k = 0; k < dbutil.size(); k++) {
							String id = dbutil.getInt(k, "site_id") + "";
							String name = dbutil.getString(k, "name");
							this.storeRoleresop(opids[i], id, roleid,
									restypeid, name, roletype,false);
							sendevent = true;
						}
					}
				}

				// 是站点频道资源并且递归的话
				if ((restypeid.equals("channel") && isRecursive.equals("1"))
						|| (restypeid.equals("channeldoc") && isRecursive
								.equals("1"))) {
					str = "SELECT t.channel_id,t.name FROM TD_cms_channel t START WITH "
							+ " t.channel_id="
							+ resid
							+ " CONNECT BY PRIOR t.channel_id = t.parent_ID";
					dbutil.executeSelect(str);
					if (dbutil.size() > 0) {
						for (int k = 0; k < dbutil.size(); k++) {
							String id = dbutil.getInt(k, "channel_id") + "";
							String name = dbutil.getString(k, "name");
							this.storeRoleresop(opids[i], id, roleid,
									restypeid, name, roletype,false);
							sendevent = true;
						}
					}
				}
				// 是机构资源并且递归的话
				if (restypeid.equals("orgunit") && isRecursive.equals("1")) {
					str = "SELECT t.org_id,t.org_name FROM TD_SM_ORGANIZATION t START WITH "
							+ " t.org_id='"
							+ resid
							+ "' CONNECT BY PRIOR t.org_id=t.PARENT_ID";
					dbutil.executeSelect(str);
					if (dbutil.size() > 0) {
						for (int k = 0; k < dbutil.size(); k++) {
							String id = dbutil.getString(k, "org_id");
							String name = dbutil.getString(k, "org_name");
							this.storeRoleresop(opids[i], id, roleid,
									restypeid, name, roletype,false);
							sendevent = true;
						}
					}
				}
				// 是用户组资源并且递归的话
				if (restypeid.equals("group") && isRecursive.equals("1")) {
					str = "SELECT t.group_id,t.group_name FROM TD_SM_group t START WITH "
							+ "t.group_id="
							+ resid
							+ " CONNECT BY PRIOR t.group_id=t.PARENT_ID";
					dbutil.executeSelect(str);
					if (dbutil.size() > 0) {
						for (int k = 0; k < dbutil.size(); k++) {
							String id = dbutil.getString(k, "group_id");
							String name = dbutil.getString(k, "group_name");
							this.storeRoleresop(opids[i], id, roleid,
									restypeid, name, roletype,false);
							sendevent = true;
						}
					}
				}
				if (isRecursive.equals("0")) {
					this.storeRoleresop(opids[i], resid, roleid, restypeid,
							resname, roletype,false);
					sendevent = true;
				}
			}

			b = true;
//			Event event = new EventImpl("",
//					ACLEventType.RESOURCE_ROLE_INFO_CHANGE);
//			super.change(event);
			if(sendevent)
				EventUtil.sendRESOURCE_ROLE_INFO_CHANGEEvent();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ManagerException(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ManagerException(e.getMessage());
		}
		return b;
	}

	/**
	 * 将角色授予多个用户
	 * 
	 * @param userids
	 * @param roleid
	 * @throws ManagerException
	 */
	public void grantRoleToUsers(String[] userids, String roleid)
			throws ManagerException {
		if (userids == null || userids.length == 0 || roleid == null
				|| roleid.length() == 0)
			return;
		String front = "insert into td_sm_userrole(role_id,user_id) values(?,?)";
		
		int len = front.length();
		PreparedDBUtil db = new PreparedDBUtil();
		try {
			db.preparedInsert(front.toString());
			for (int i = 0; i < userids.length; i++) {
				db.setString(1, roleid);
				db.setInt(2, Integer.parseInt(userids[i]));
				
				db.addPreparedBatch();
				
			}
			db.executePreparedBatch();
			
			Event event = new EventImpl("",ACLEventType.USER_ROLE_INFO_CHANGE);
			super.change(event,true);
		} catch (Exception e) {
			StringBuffer users = new StringBuffer();
			for (int i = 0; i < userids.length; i++) {
				users.append(userids[i]).append(",");
			}
			throw new ManagerException("批量授予角色用户失败[roleid=]" + roleid
					+ ",[userids=" + users.toString() + "]:" + e.getMessage());
		}

	}

	/**
	 * 将资源及操作授予对应的角色数组中的角色
	 * 
	 * @param opid
	 * @param resid
	 * @param roleids
	 * @param restypeid
	 * @param resname
	 * @param types
	 * @return
	 * @throws ManagerException
	 *             added by biaoping.yin on 2007.8.6
	 */
	public boolean storeRoleresop(String opid, String resid, String[] roleids,
			String restypeid, String resname, String role_type)
			throws ManagerException {

		boolean b = false;
		DBUtil db = new DBUtil();
		try {
			for (int i = 0; i < roleids.length; i++) {
				String sql = "insert all when totalsize <= 0 then into td_sm_roleresop(OP_ID,RES_ID,ROLE_ID,RESTYPE_ID,RES_NAME,TYPES)"
						+ " values('"
						+ opid
						+ "','"
						+ resid
						+ "','"
						+ roleids[i]
						+ "','"
						+ restypeid
						+ "','"
						+ resname
						+ "','"
						+ role_type
						+ "') select count(op_id) totalsize from td_sm_roleresop where OP_ID='"
						+ opid
						+ "' and RES_ID='"
						+ resid
						+ "' and ROLE_ID='"
						+ roleids[i]
						+ "' and TYPES='"
						+ role_type
						+ "' and RESTYPE_ID='" + restypeid + "'";
				// System.out.println("=="+sql);
				db.addBatch(sql);

			}
			db.executeBatch();
			b = true;
			Event event = new EventImpl("",
					ACLEventType.RESOURCE_ROLE_INFO_CHANGE);
			super.change(event);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ManagerException(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ManagerException(e.getMessage());
		}finally{
			db.resetBatch();
		}
		return b;
	}

	/**
	 * 通过岗位机构ID获取机构下以赋予的岗位角色--2007-10-17 gao.tang
	 * 
	 * @param role
	 * @return
	 * @throws ManagerException
	 */
	public List getJobListByRoleJob(String jobId, String orgId)
			throws ManagerException {
		List list = new ArrayList();
		DBUtil db = new DBUtil();
		StringBuffer hsql = new StringBuffer(
				"select role.role_id,role.role_name,role.role_type from td_sm_role role where role.role_id in ("
						+ "select a.role_id from td_sm_orgjobrole a where a.job_id = '"
						+ jobId + "' and a.org_id = '" + orgId
						+ "') order by role.role_type,role.role_name");
		try {
			db.executeSelect(hsql.toString());
			for (int i = 0; i < db.size(); i++) {
				Role role = new Role();
				role.setRoleId(db.getString(i, "ROLE_ID"));
				role.setRoleName(db.getString(i, "ROLE_NAME"));
				role.setRoleType(db.getString(i, "role_type"));
				list.add(role);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 判断当前系统是否有角色
	 */
	public boolean hasRole() throws ManagerException {
		String sql = "select count(*) from td_sm_role ";
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(sql);
			return db.getInt(0, 0) > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 通过岗位机构ID获取当前机构下的所有角色
	 */
	public List getJobOrgAllRoleList(AccessControl accesscontrol,String jobId, String orgId)
			throws ManagerException {
		StringBuffer hsql = new StringBuffer("select a.* from td_sm_role a ");
		hsql.append("where a.role_name not in('orgmanager','orgmanagerroletemplate', '");
		if(!"1".equals(accesscontrol.getUserID())){
			hsql.append(AccessControl.getAdministratorRoleName())
				.append("','");
		}
		hsql.append(AccessControl.getEveryonegrantedRoleName())
			.append("') order by a.role_id");
		return this.getAllRoleListBysql(accesscontrol,"jobset",hsql.toString());
	}
	
	/**
	 * 通过用户ID获取用户岗位对应的角色 2007.11.01---gao.tang
	 */
	public List getJobRoleByList (String userId) throws ManagerException {
		List list = null;
		StringBuffer hsql = new StringBuffer();
		hsql.append("select role_id,role_name,ROLE_TYPE from td_sm_role  where role_id in (")
			.append("select a.role_id from td_sm_orgjobrole a inner join td_sm_userjoborg b on a.org_id = b.org_id and a.job_id = b.job_id where b.user_id=?) order by ROLE_TYPE,role_name");
		PreparedDBUtil dbUtil = new PreparedDBUtil();
		try {
			dbUtil.preparedSelect(hsql.toString());
			dbUtil.setInt(1, Integer.parseInt(userId));
			list = dbUtil.executePreparedForList(UserJobRole.class, new RowHandler<UserJobRole>(){

				@Override
				public void handleRow(UserJobRole userJobRole, Record record)
						throws Exception {
					// TODO Auto-generated method stub
					userJobRole.setRoleId(record.getString(  "role_id"));
					userJobRole.setRoleName(record.getString(  "role_name"));
					userJobRole.setRoleType(record.getString(  "ROLE_TYPE"));
				}
				
			});
			
//			for(int i = 0; i < dbUtil.size(); i++){
//				UserJobRole userJobRole = new UserJobRole();
//				userJobRole.setRoleId(dbUtil.getString(i, "role_id"));
//				userJobRole.setRoleName(dbUtil.getString(i, "role_name"));
//				userJobRole.setRoleType(dbUtil.getString(i, "ROLE_TYPE"));
//				list.add(userJobRole);
//			} 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 得到用户岗位角色所隶属的机构-岗位 gao.tang 2007.11.02
	 */
	public List getOrgJobList (String userId, String roleId) throws ManagerException {
		List list = new ArrayList();
		StringBuffer hsql = new StringBuffer();
		hsql.append("select org_sn,org_name,job_name from (select distinct b.org_sn,b.org_name,c.job_name from ")
			.append("td_sm_role a,")
			.append("td_sm_organization b,")
			.append("td_sm_job c,")
			.append("td_sm_userjoborg d,")
			.append("td_sm_orgjobrole e ")
			.append("where ")
			.append("a.role_id = '").append(roleId).append("' ")
			.append("and a.role_id = e.role_id and b.org_id = e.org_id and c.job_id = d.job_id ")
			.append("and d.job_id = e.job_id and d.user_id = '").append(userId).append("' and b.org_id in(")
			.append("select distinct org_id from td_sm_userjoborg where user_id='").append(userId)
			.append("')")
			.append(") order by org_sn");
		DBUtil dbUtil = new DBUtil();
		try {
			dbUtil.executeSelect(hsql.toString());
			for(int i = 0; i < dbUtil.size(); i++){
				UserJobRole userJobRole = new UserJobRole();
				userJobRole.setOrgName(dbUtil.getString(i, "org_name"));
				userJobRole.setJobName(dbUtil.getString(i, "job_name"));
				list.add(userJobRole);
			} 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public boolean deletePermissionOfRole(List resIds, String restypeId, String[] roleids, String type) throws ManagerException {
		boolean b = false;
		DBUtil db = new DBUtil();
		if(resIds == null || resIds.size() == 0 || roleids == null || roleids.length == 0)
		{
			return true;
		}
		StringBuffer resids = new StringBuffer();
		boolean flag = false;
		for(int i =0; i < resIds.size(); i ++)
		{
			String[] temp = (String[])resIds.get(i);
			if(!flag)
			{
				resids.append("'").append(temp[0]).append("'");
				flag = true;
			}
			else
			{
				resids.append(",").append("'").append(temp[0]).append("'");
			}
			
		}
		StringBuffer roles = new StringBuffer();
		boolean flag1 = false;
		for(int i = 0; i < roleids.length ; i ++)
		{
			if(!flag1)
			{
				roles.append("'").append(roleids[i]).append("'");
				flag1 = true;
			}
			else
			{
				
				roles.append(",").append("'").append(roleids[i]).append("'");
			}
		}
		
		String sql = "delete from td_sm_roleresop where RES_ID in (" + resids.toString()
		+ ") and " + "RESTYPE_ID='" + restypeId + "' and TYPES='"
		+ type + "' and role_id in (" + roles.toString() + ")";
		

		// System.out.println("=="+sql);
		try {
			db.executeDelete(sql);
			b = true;
			Event event = new EventImpl("",
					ACLEventType.RESOURCE_ROLE_INFO_CHANGE);
			super.change(event);
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return b;
		
	}
    /**
     * 对这些角色roleids授予资源类型为roletype的资源类restypeid,操作为opids, 资源ID为resIds
     * 相对固定的是 roleids + roletype + restypeid ,可供选择的是操作和资源ID opids+resIds
     */
	public boolean restoreRoleresop(String[] opids, List resIds, String[] roleids, String restypeid, String roletype) throws ManagerException {
		boolean b = false;
		//创建事务
		TransactionManager tm = new TransactionManager();		
		DBUtil db = new DBUtil();
		if(resIds == null || resIds.size() == 0 || roleids == null || roleids.length == 0 || opids == null || opids.length == 0){
			return true;
		}
		//把资源ID拼成串 id1,id2...
		StringBuffer resids = new StringBuffer();
		resids.setLength(0);
		for(int i =0; i < resIds.size(); i ++)
		{
			String[] temp = (String[])resIds.get(i);
			if(resids.length()==0)
			{
				resids.append("'").append(temp[0]).append("'");
			}else{
				resids.append(",").append("'").append(temp[0]).append("'");
			}
			
		}
		//把角色ID拼成串 id1,id2...
		StringBuffer ops = new StringBuffer();
		ops.setLength(0);
		for(int i = 0; i < opids.length ; i ++)
		{
			if(ops.length()==0){
				ops.append("'").append(opids[i]).append("'");
			}else{
				
				ops.append(",").append("'").append(opids[i]).append("'");
			}
		}
		//把操作ID拼成串 id1,id2...
		StringBuffer roles = new StringBuffer();
		roles.setLength(0);
		for(int i = 0; i < roleids.length ; i ++)
		{
			if(roles.length()==0){
				roles.append("'").append(roleids[i]).append("'");
			}else{
				
				roles.append(",").append("'").append(roleids[i]).append("'");
			}
		}
//		modify by ge.tao
//		先删后存的策略, 修改成找出要删除的记录, 进行递归收回 删除
//		String sql = "delete from td_sm_roleresop where RES_ID in (" + resids.toString()
//		+ ") and " + "RESTYPE_ID='" + restypeid + "' and TYPES='"
//		+ roletype + "' and role_id in (" + roles.toString() + ")";
		
		//add by ge.tao 
		//date 2008-01-04
		//先删后存的策略,先找出 对于 特定的对象群,特定的资源类型,特定的资源类型ID  要删除的记录(资源ID,操作ID).
		ResManager resManager = new ResManagerImpl();
		String will_delete_resop = "select OP_ID,RES_ID,ROLE_ID,RESTYPE_ID,TYPES " +
				"from td_sm_roleresop where  " +				
		        "RESTYPE_ID='" + restypeid + "' "+
		        "and TYPES='"+ roletype + "' " +
		        "and role_id in (" + roles.toString() + ")"+
		        "and RES_ID not in (" + resids.toString() + ") " +//不在资源ID群里面的
		        "and OP_ID not in (" + ops.toString() + ") " ;//不在操作ID群里面的
		DBUtil helpDB = new DBUtil();	
		try {
			helpDB.executeSelect(will_delete_resop.toString());
			//开始框事务
			tm.begin();
			for(int i=0;i<helpDB.size();i++){
				String restypeid_ = helpDB.getString(i,"RESTYPE_ID");				
				String resId_ = helpDB.getString(i,"RES_ID");
				String opId_ = helpDB.getString(i,"OP_ID");
				//把被取消资源的对象, 作为授权对象 去查找 通过他 授权出去的资源
				String origineId_ = helpDB.getString(i,"ROLE_ID");
				//把被取消资源的对象的资源类型, 作为授权对象的授权类型 去查找 通过他 授权出去的资源
				String origineType_ = helpDB.getString(i,"TYPES");				
				resManager.reclaimResOp(db,restypeid_, resId_, opId_, origineId_, origineType_);				
			}			
			StringBuffer insert = new StringBuffer();
			for(int i = 0 ; i < roleids.length; i ++){
				for(int j = 0; j < opids.length; j ++){
					for(int k = 0; k < resIds.size(); k ++){
						String[] temps = (String[])resIds.get(k);
						insert.append("insert all when totalsize <= 0 then into td_sm_roleresop(OP_ID,RES_ID,ROLE_ID,RESTYPE_ID,RES_NAME,TYPES)")
							  .append(" values('")
							  .append(opids[j])
							  .append( "','")
							  .append( temps[0])
							  .append( "','")
							  .append( roleids[i])
							  .append("','")
							  .append( restypeid)
							  .append("','")
							  .append(temps[1])
							  .append("','")
							  .append(roletype)
							  .append( "') select count(OP_ID) totalsize from td_sm_roleresop where OP_ID='")
							  .append( opids[j])
							  .append("' and RES_ID='")
							  .append(temps[0])
							  .append("' and ROLE_ID='")
							  .append( roleids[i])
							  .append( "' and TYPES='")
							  .append( roletype )
							  .append( "' and RESTYPE_ID='" )
							  .append( restypeid )
							  .append( "'");
						db.addBatch(insert.toString());
						insert.setLength(0);
					}
				}
			}
			db.executeBatch();
			tm.commit();
			//事务结束
			b = true;
			//发事件,刷新内存
			Event event = new EventImpl("",ACLEventType.RESOURCE_ROLE_INFO_CHANGE);
			super.change(event);
		} catch (SQLException e) {
			try {
				//回滚
				tm.rollback();
			} catch (RollbackException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
		
	}
	
	/**
	 * @param opids 操作类型ID
	 * @param resIds 资源ID
	 * @param roleids 用户/角色ID
	 * @param restypeid 资源类型
	 * @param roletype 标示是角色还是用户
	 * @param userId 当前用户ID
	 * @param isCS 如果资源类型是菜单,则标示是否有CS菜单资源
	 * @return
	 * @throws ManagerException
	 */
	public boolean saveResGrant(String[] opids, List resIds, String[] roleids, String restypeid, String roletype, String userId) throws ManagerException {
		boolean b = false;
		
		//如果资源类型是菜单,则标示是否有CS菜单资源
		boolean isCS = false;
		if("column".equals(restypeid))
			isCS = MenuResTree.isCS;
		
		//创建事务
		TransactionManager tm = new TransactionManager();
		DBUtil db = new DBUtil();
		if(roleids == null || roleids.length == 0 || opids == null || opids.length == 0){
			return true;
		}
		
//		把资源ID拼成串 id1,id2...
		StringBuffer resids = new StringBuffer();
		for(int i =0; resIds != null && i < resIds.size(); i ++)
		{
			
			String[] temp = (String[])resIds.get(i);
			if(resids.length()==0)
			{
				resids.append("'").append(temp[0]).append("'");
			}else{
				resids.append(",").append("'").append(temp[0]).append("'");
			}
			
		}
			
		
//		把角色ID拼成串 id1,id2...
		StringBuffer ops = new StringBuffer();
		ops.setLength(0);
		for(int i = 0; i < opids.length ; i ++)
		{
			if(ops.length()==0){
				ops.append("'").append(opids[i]).append("'");
			}else{
				
				ops.append(",").append("'").append(opids[i]).append("'");
			}
		}
		//把操作ID拼成串 id1,id2...
		 
		StringBuffer roles = new StringBuffer();
		roles.setLength(0);
		for(int i = 0; i < roleids.length ; i ++)
		{
			if(roles.length()==0){
				roles.append("'").append(roleids[i]).append("'");
			}else{
				
				roles.append(",").append("'").append(roleids[i]).append("'");
			}
		}
		String str2 = roles.toString();
//		modify by ge.tao
//		先删后存的策略, 修改成找出要删除的记录, 进行递归收回 删除
//		String sql = "delete from td_sm_roleresop where RES_ID in (" + resids.toString()
//		+ ") and " + "RESTYPE_ID='" + restypeid + "' and TYPES='"
//		+ roletype + "' and role_id in (" + roles.toString() + ")";
		
		//add by ge.tao 
		//date 2008-01-04
		//先删后存的策略,先找出 对于 特定的对象群,特定的资源类型,特定的资源类型ID  要删除的记录(资源ID,操作ID).
		ResManager resManager = new ResManagerImpl();
		String will_delete_resop = "select OP_ID,RES_ID,ROLE_ID,RESTYPE_ID,TYPES " +
				" from td_sm_roleresop where  ";
		if(isCS){
			will_delete_resop += " RESTYPE_ID in ('"+restypeid+"','cs_column','report_column') ";
		}else{
			will_delete_resop += " RESTYPE_ID = '"+restypeid+"' ";
		}
		will_delete_resop += " and TYPES='"+ roletype + "' " +
				" and role_id in (" + roles.toString() + ") "+
		        " and OP_ID in (" + ops.toString() + ") " ;//不在操作ID群里面的
		if(resIds != null && resIds.size() > 0){
			will_delete_resop += " and RES_ID not in (" + resids.toString() + ") " ;//不在资源ID群里面的
		}
		DBUtil helpDB = new DBUtil();	
		try {
			helpDB.executeSelect(will_delete_resop.toString());
			//开始框事务
			
			tm.begin();
			for(int i=0;i<helpDB.size();i++){
				String restypeid_ = helpDB.getString(i,"RESTYPE_ID");				
				String resId_ = helpDB.getString(i,"RES_ID");
				String opId_ = helpDB.getString(i,"OP_ID");
				//把被取消资源的对象, 作为授权对象 去查找 通过他 授权出去的资源
				String origineId_ = helpDB.getString(i,"ROLE_ID");
				//把被取消资源的对象的资源类型, 作为授权对象的授权类型 去查找 通过他 授权出去的资源
				String origineType_ = helpDB.getString(i,"TYPES");
				
				resManager.reclaimResOp(db,restypeid_, resId_, opId_, origineId_, origineType_);
//				if(opId_.equals("readorgname")){
//					resManager.reclaimResOp(db,restypeid_, resId_, "read", origineId_, origineType_);
//					resManager.reclaimResOp(db,restypeid_, resId_, "write", origineId_, origineType_);
//				}
			}		
			
			StringBuffer insert = new StringBuffer();
			for(int i = 0 ; roleids!=null && i < roleids.length; i ++){
				for(int j = 0; opids !=null && j < opids.length; j ++){
					for(int k = 0; resIds != null && k < resIds.size(); k ++){
						if(roleids[i]==null || roleids[i].trim().length()==0) continue;
						String[] temps = (String[])resIds.get(k);
						if(temps==null || temps.length<3){
							continue;
						}
						insert.append("insert all when totalsize <= 0 then into td_sm_roleresop(OP_ID,RES_ID,ROLE_ID,RESTYPE_ID,RES_NAME,TYPES)")
							  .append(" values('")
							  .append(opids[j])
							  .append( "','")
							  .append( temps[0])
							  .append( "','")
							  .append( roleids[i].trim())
							  .append("','")
							  .append(temps[2])
							  .append("','")
							  .append(temps[1])
							  .append("','")
							  .append(roletype)
							  .append( "') select count(OP_ID) totalsize from td_sm_roleresop where OP_ID='")
							  .append( opids[j])
							  .append("' and RES_ID='")
							  .append(temps[0])
							  .append("' and ROLE_ID='")
							  .append( roleids[i].trim())
							  .append( "' and TYPES='")
							  .append( roletype )
							  .append( "' and RESTYPE_ID='" )
							  .append(temps[2]).append( "'");
						db.addBatch(insert.toString());
//						String str1 = insert.toString();
						insert.setLength(0);
						
//						resManager.saveResOpOrigin(restypeid,temps[0],opids[j],
//								roleids[i],roletype,userId);
					}
				}
			}
			
			db.executeBatch();
			tm.commit();
			//事务结束
			b = true;
			//发事件,刷新内存
			
			Event event = new EventImpl("",ACLEventType.RESOURCE_ROLE_INFO_CHANGE);
			super.change(event);
			
		} catch (SQLException e) {
			try {
				//回滚
				tm.rollback();
			} catch (RollbackException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} catch (Exception e) {
			try {
				//回滚
				tm.rollback();
			} catch (RollbackException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		
		return b;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.manager.RoleManager#userHasPermissionRole(java.lang.String, com.frameworkset.platform.security.AccessControl)
	 */
	public List userHasPermissionRole(String userId, AccessControl accessControl)throws Exception {
		RoleManager roleManager = SecurityDatabase.getRoleManager();
		String sql = "";
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
		return allRole;
	}
	/**
	 * roleIds.length == opids.length
	 * un_roleIds.length == un_opids.length
	 * resIds.length == resNames.length 
	 */
	private boolean saveRoleresop(String[] opids, String[] roleIds,
			String[] un_opids, String[] un_roleIds, String[] resIds,
			String types, String restypeId, String[] resNames,boolean isBatch){
		TransactionManager tm = new TransactionManager();
		DBUtil dbInsert = new DBUtil();
		DBUtil dbDelete = new DBUtil();
		StringBuffer insertSql = new StringBuffer();
		StringBuffer deleteSql = new StringBuffer();
		int insertCount = 0;
		int deleteCount = 0;
		try {
			tm.begin();
			for(int i = 0; i < resIds.length; i++){
				//添加自定义权限
				for(int j = 0; opids != null && j < opids.length; j++){
					insertSql.append("insert all when totalsize <= 0 then into td_sm_roleresop(OP_ID,RES_ID,ROLE_ID,RESTYPE_ID,RES_NAME,TYPES)")
						.append(" values('").append(opids[j]).append("',")
						.append("'").append(resIds[i]).append("',")
						.append("'").append(roleIds[j]).append("',")
						.append("'").append(restypeId).append("',")
						.append("'").append(resNames[i]).append("',")
						.append("'").append(types).append("') ")
						.append(" select count(OP_ID) totalsize from td_sm_roleresop where OP_ID='")
						.append(opids[j]).append("' and RES_ID='").append(resIds[i])
						.append("' and ROLE_ID='").append(roleIds[j]).append("' and TYPES='")
						.append(types).append("' and RESTYPE_ID='").append(restypeId)
						.append("'");
					dbInsert.addBatch(insertSql.toString());
					//System.out.println("insert_sql = " + insertSql.toString());
					insertSql.setLength(0);
					insertCount ++;
					if(insertCount > 900){
						dbInsert.executeBatch();
						insertCount = 0;
					}
				}
				//删除自定义权限
				if(!isBatch){
					for(int z = 0; un_opids != null && z < un_opids.length; z ++){
						deleteSql.append("delete from td_sm_roleresop where OP_ID='").append(un_opids[z])
							.append("' and RES_ID='").append(resIds[i]).append("' and ROLE_ID='")
							.append(un_roleIds[z]).append("' and TYPES='").append(types).append("' and RESTYPE_ID='")
							.append(restypeId).append("'");
						dbDelete.addBatch(deleteSql.toString());
						deleteSql.setLength(0);
						deleteCount ++;
						if(deleteCount > 900){
							dbDelete.executeBatch();
							deleteCount = 0;
						}
					}
				}
			}
			dbInsert.executeBatch();
			if(!isBatch){
				dbDelete.executeBatch();
			}
			tm.commit();
			Event event = new EventImpl("",ACLEventType.RESOURCE_ROLE_INFO_CHANGE);
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
	/**
	 * roleIds.length == opids.length
	 * un_roleIds.length == un_opids.length
	 * resIds.length == resNames.length 
	 */
	public boolean saveBatchRoleresop(String[] opids, String[] roleIds,
			String[] un_opids, String[] un_roleIds, String[] resIds,
			String types, String restypeId, String[] resNames){
		return saveRoleresop(opids, roleIds,
				 un_opids, un_roleIds, resIds,
				types, restypeId, resNames,true);
	}
	/**
	 * roleIds.length == opids.length
	 * un_roleIds.length == un_opids.length
	 * resIds.length == resNames.length 
	 */
	public boolean saveRoleresop(String[] opids, String[] roleIds,
			String[] un_opids, String[] un_roleIds, String[] resIds,
			String types, String restypeId, String[] resNames){
		return saveRoleresop(opids, roleIds,
				 un_opids, un_roleIds, resIds,
				types, restypeId, resNames,false);
	}

	/**
	 * 判断资源是否存在
	 */
	public boolean hasRoleresop(String opid, String roleId, String resId,
			String types, String restypeId) {
		StringBuffer sql = new StringBuffer()
			.append("select count(op_id) from td_sm_roleresop where OP_ID='")
			.append(opid).append("' and RES_ID='").append(resId).append("' and ROLE_ID='")
			.append(roleId).append("' and TYPES='").append(types).append("' and RESTYPE_ID='")
			.append(restypeId).append("'");
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(sql.toString());
			if(db.getInt(0, 0) > 0){
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean saveBatchOrgRoleresop(String[] opids, String orgId,
			String[] un_opids, String[] resIds, String types, String restypeId,
			String[] resName, boolean isRecursion) {
		return saveOrgRoleresop(opids,orgId,un_opids,resIds,types,restypeId,resName,isRecursion,true);
	}
	

	public boolean saveOrgRoleresop(String[] opids,String orgId,String[] un_opids,String[] resIds,
			String types,String restypeId,String[] resName,boolean isRecursion){
		return saveOrgRoleresop(opids,orgId,un_opids,resIds,types,restypeId,resName,isRecursion,false);
	}
	
	/**
	 * roleIds.length == opids.length
	 * un_roleIds.length == un_opids.length
	 * resIds.length == resNames.length 
	 */
	private boolean saveOrgRoleresop(String[] opids, String orgId,
			String[] un_opids, String[] resIds,String types, String restypeId, 
			String[] resNames,boolean isRecursion,boolean isBatch){
		TransactionManager tm = new TransactionManager();
		DBUtil dbInsert = new DBUtil();
		DBUtil dbDelete = new DBUtil();
		StringBuffer insertSql = new StringBuffer();
		StringBuffer deleteSql = new StringBuffer();
		int insertCount = 0;
		int deleteCount = 0;
		String sunorg_sql = "select org_id from td_sm_organization start with org_id='"+orgId+"' connect by prior org_id=parent_id";
		try {
			tm.begin();
			for(int i = 0; i < resIds.length; i++){
				//添加自定义权限
				for(int j = 0; opids != null && j < opids.length; j++){
					if(isRecursion){//如果是递归授予子机构
						DBUtil sunorg_db = new DBUtil();
						sunorg_db.executeSelect(sunorg_sql);
						for(int orgCount = 0; orgCount < sunorg_db.size(); orgCount ++){
							String orgid = sunorg_db.getString(orgCount, "org_id");
							insertSql.append("insert all when totalsize <= 0 then into td_sm_roleresop(OP_ID,RES_ID,ROLE_ID,RESTYPE_ID,RES_NAME,TYPES)")
								.append(" values('").append(opids[j]).append("',")
								.append("'").append(resIds[i]).append("',")
								.append("'").append(orgid).append("',")
								.append("'").append(restypeId).append("',")
								.append("'").append(resNames[i]).append("',")
								.append("'").append(types).append("') ")
								.append(" select count(OP_ID) totalsize from td_sm_roleresop where OP_ID='")
								.append(opids[j]).append("' and RES_ID='").append(resIds[i])
								.append("' and ROLE_ID='").append(orgid).append("' and TYPES='")
								.append(types).append("' and RESTYPE_ID='").append(restypeId)
								.append("'");
							dbInsert.addBatch(insertSql.toString());
							insertSql.setLength(0);
							insertCount ++;
							if(insertCount > 900){
								dbInsert.executeBatch();
								insertCount = 0;
							}
						}
					}else{
						insertSql.append("insert all when totalsize <= 0 then into td_sm_roleresop(OP_ID,RES_ID,ROLE_ID,RESTYPE_ID,RES_NAME,TYPES)")
							.append(" values('").append(opids[j]).append("',")
							.append("'").append(resIds[i]).append("',")
							.append("'").append(orgId).append("',")
							.append("'").append(restypeId).append("',")
							.append("'").append(resNames[i]).append("',")
							.append("'").append(types).append("') ")
							.append(" select count(OP_ID) totalsize from td_sm_roleresop where OP_ID='")
							.append(opids[j]).append("' and RES_ID='").append(resIds[i])
							.append("' and ROLE_ID='").append(orgId).append("' and TYPES='")
							.append(types).append("' and RESTYPE_ID='").append(restypeId)
							.append("'");
						dbInsert.addBatch(insertSql.toString());
						insertSql.setLength(0);
						insertCount ++;
						if(insertCount > 900){
							dbInsert.executeBatch();
							insertCount = 0;
						}
					}
					//System.out.println("insert_sql = " + insertSql.toString());
					
				}
				//删除自定义权限
				if(!isBatch){
					for(int z = 0; un_opids != null && z < un_opids.length; z ++){
						if(isRecursion){
							DBUtil sunorg_db = new DBUtil();
							sunorg_db.executeSelect(sunorg_sql);
							for(int orgCount = 0; orgCount < sunorg_db.size(); orgCount ++){
								String orgid = sunorg_db.getString(orgCount, "org_id");
								deleteSql.append("delete from td_sm_roleresop where OP_ID='").append(un_opids[z])
									.append("' and RES_ID='").append(resIds[i]).append("' and ROLE_ID='")
									.append(orgid).append("' and TYPES='").append(types).append("' and RESTYPE_ID='")
									.append(restypeId).append("'");
								dbDelete.addBatch(deleteSql.toString());
								deleteSql.setLength(0);
								deleteCount ++;
								if(deleteCount > 900){
									dbDelete.executeBatch();
									deleteCount = 0;
								}
							}
						}else{
							deleteSql.append("delete from td_sm_roleresop where OP_ID='").append(un_opids[z])
								.append("' and RES_ID='").append(resIds[i]).append("' and ROLE_ID='")
								.append(orgId).append("' and TYPES='").append(types).append("' and RESTYPE_ID='")
								.append(restypeId).append("'");
							dbDelete.addBatch(deleteSql.toString());
							deleteSql.setLength(0);
							deleteCount ++;
							if(deleteCount > 900){
								dbDelete.executeBatch();
								deleteCount = 0;
							}
						}
					}
				}
			}
			dbInsert.executeBatch();
			if(!isBatch){
				dbDelete.executeBatch();
			}
			tm.commit();
			Event event = new EventImpl("",ACLEventType.RESOURCE_ROLE_INFO_CHANGE);
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