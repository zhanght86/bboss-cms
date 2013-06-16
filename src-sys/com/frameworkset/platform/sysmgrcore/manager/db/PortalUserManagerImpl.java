package com.frameworkset.platform.sysmgrcore.manager.db;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.transaction.RollbackException;

import org.frameworkset.event.EventHandle;
import org.frameworkset.spi.SPIException;

import com.frameworkset.platform.config.ConfigManager;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.security.authentication.EncrpyPwd;
import com.frameworkset.platform.sysmgrcore.entity.Accredit;
import com.frameworkset.platform.sysmgrcore.entity.Group;
import com.frameworkset.platform.sysmgrcore.entity.Job;
import com.frameworkset.platform.sysmgrcore.entity.Operation;
import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.platform.sysmgrcore.entity.Orgjob;
import com.frameworkset.platform.sysmgrcore.entity.Res;
import com.frameworkset.platform.sysmgrcore.entity.Role;
import com.frameworkset.platform.sysmgrcore.entity.Tempaccredit;
import com.frameworkset.platform.sysmgrcore.entity.User;
import com.frameworkset.platform.sysmgrcore.entity.Userattr;
import com.frameworkset.platform.sysmgrcore.entity.Usergroup;
import com.frameworkset.platform.sysmgrcore.entity.Userjoborg;
import com.frameworkset.platform.sysmgrcore.entity.Userresop;
import com.frameworkset.platform.sysmgrcore.entity.Userrole;
import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.platform.sysmgrcore.manager.UserManager;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.orm.transaction.TransactionException;
import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.util.ListInfo;

public class PortalUserManagerImpl extends EventHandle implements UserManager {

	public String addUser(User user) throws ManagerException {
		
		
		return null;
	}

	public boolean addUserOrg(String[] userIds, String orgId, String classType)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean addUsergroup(Integer userid, String[] groupid)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public void addUserrole(String userId, String[] roleIds)
			throws ManagerException {
		// TODO Auto-generated method stub

	}

	public void addUserrole(String userId, String[] roleIds,
			String currentUserId) throws ManagerException {
		// TODO Auto-generated method stub

	}

	public void creatorUser(User user, String orgId, String jobId)
			throws ManagerException {
		this.addUser(user, true);

	}

	public boolean delAlotJobRole(String[] ids, String[] jobid, String orgId)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public void delAlotUserOrg(String[] ids, String[] jobids, String orgId)
			throws ManagerException {
		// TODO Auto-generated method stub

	}

	public boolean delAlotUserRole(String[] ids, String[] roleid)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean deleteAccredit(Accredit accredit) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean deleteBatchUser(String[] userIds) throws ManagerException {
//		if(userIds != null && !"".equals(userIds)){
//			TransactionManager tm = new TransactionManager();
//			try {
//				tm.begin();
//				DBUtil db = new DBUtil();
//				User user = null;
//				for(int i = 0; i < userIds.length; i++){
//					try {
//						db.executeSelect("select user_name,user_realname from td_sm_user where user_id='"+userIds[i]+"'");
//						user = new User();
//						user.setUserName(db.getString(0, "user_name"));
//						user.setUserRealname(db.getString(0, "user_realname"));
//						user.setUserId(Integer.valueOf(userIds[i]));
//						this.deleteUser(user);
//					} catch (SQLException e) {
//						e.printStackTrace();
//					}
//				}
//				tm.commit();
//			} catch (TransactionException e1) {
//				try {
//					tm.rollback();
//				} catch (RollbackException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				e1.printStackTrace();
//			} catch (RollbackException e) {
//				// TODO Auto-generated catch block
//				try {
//					tm.rollback();
//				} catch (RollbackException e2) {
//					// TODO Auto-generated catch block
//					e2.printStackTrace();
//				}
//				e.printStackTrace();
//			}
//			
//		}
		return false;
	}

	public boolean deleteBatchUserRes(String[] userIds) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean deleteDisperseOrguser() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean deleteTempaccredit(Tempaccredit tempaccredit)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public String deleteUJOAjax(String uid, String[] jobIds, String orgId)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public void deleteUJOAjax_batch(String[] ids, String[] jobid, String orgId)
			throws ManagerException {
		// TODO Auto-generated method stub

	}

	public boolean deleteUser(User user) throws ManagerException {
		//是否同步portal真实用户
		boolean isSynPortalUser = ConfigManager.getInstance().getConfigBooleanValue("isSynPortalUser", false);
		if(!isSynPortalUser){
			String B_APP_ID = getB_APP_ID();
			String userName = user.getUserName();
			String sql = "delete sso_user_mapping where A_APP_ID='portal' and "
				+"B_APP_ID='"+B_APP_ID+"' and B_USER_NAME='"+userName+"'";
			DBUtil db = new DBUtil();
			try {
				db.executeDelete("portal",sql);
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{
			String userName = user.getUserName();
			TransactionManager tm = new TransactionManager();
			try {
				tm.begin();
				//第一步 查询是否在portal库存在该用户
				String user_sql = "select USERID,CONTACTID from user_ where SCREENNAME='"+userName+"'";
				DBUtil user_db = new DBUtil();
				user_db.executeSelect("portal",user_sql);
				//第二步 如果存在该用户，那么继续查询出该用户对应的group_表中的数据
				if(user_db.size() > 0){
					String user_id = user_db.getString(0, "USERID");
					String contactid = user_db.getString(0, "CONTACTID");
					String group_sql = "select GROUPID from group_ where CREATORUSERID='"+user_id+"'";
					DBUtil group_db = new DBUtil();
					group_db.executeSelect("portal", group_sql);
					//第四步 如果在group_中存在用户对应的数据，那么删除group_表中对应的LayoutSet表中的数据
					if(group_db.size() > 0){
						String group_id = group_db.getString(0, 0);
						DBUtil LayoutSet_del = new DBUtil();
						DBUtil group_del = new DBUtil();
						LayoutSet_del.executeDelete("portal", "delete LayoutSet where GROUPID='"+group_id+"'");
						LayoutSet_del.executeDelete("portal", "delete Layout where GROUPID='"+group_id+"'");
						group_del.executeDelete("portal", "delete group_ where CREATORUSERID='"+user_id+"'");
					}
					DBUtil user_del = new DBUtil();
					user_del.executeDelete("portal", "delete contact_ where CONTACTID='"+contactid+"'");
					user_del.executeDelete("portal", "delete USERS_ROLES where USERID='"+user_id+"'");
					user_del.executeDelete("portal", "delete USERS_GROUPS where USERID='"+user_id+"'");
					user_del.executeDelete("portal", "delete USERS_ORGS where USERID='"+user_id+"'");
					user_del.executeDelete("portal", "delete user_ where SCREENNAME='"+userName+"'");

				}
				
				tm.commit();
			} catch (TransactionException e) {
				e.printStackTrace();
			} catch (RollbackException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		return false;
	}

	public String deleteUserOrgJob(Integer uid, String orgId, String[] jobid) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean deleteUserRes(User user) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean deleteUserattr(Userattr userattr) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean deleteUsergroup(Usergroup usergroup) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean deleteUsergroup(User user) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean deleteUsergroup(Group group) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean deleteUsergroup(String userId, String groupId)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean deleteUsergroup(Integer userid, String[] groupids)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean deleteUserjoborg(Userjoborg userjoborg)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean deleteUserjoborg(Job job, Organization org)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean deleteUserjoborg(Job job, User user) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean deleteUserjoborg(Organization org, User user)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean deleteUserjoborg(User user) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean deleteUserjoborg(String userId, String jobId, String orgId)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean deleteUserjoborg(String userId, String jobId, String orgId,
			boolean sendEvent) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean deleteUserresop(Userresop userresop) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean deleteUserrole(Userrole userrole) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean deleteUserrole(User user) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean deleteUserrole(Organization org, Role role)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean deleteUserrole(Role role, Group group)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public void deleteUserrole(String userId, String[] roleIds)
			throws ManagerException {
		// TODO Auto-generated method stub

	}

	public void deleteUserrole(String userId, String[] roleIds, String roleTypes)
			throws ManagerException {
		// TODO Auto-generated method stub

	}

	public List getAccreditList(String userName) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] getCuruserAdministrableDeleteUser(String curUserId,
			String[] selectUserNames) {
		// TODO Auto-generated method stub
		return null;
	}

	public List getDicList() throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public List getOrgUserList(String orgid) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public List getOrgUserList(String orgid, String userId)
			throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}



	public List getTempaccredit(String userName) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public User getUser(String propName, String value) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public User getUser(String hql) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public User getUserById(String userId) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public User getUserByName(String userName) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public ListInfo getUserInfoList(String sql, int offset, int maxItem)
			throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public List getUserList(String propName, String value, boolean isLike)
			throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public List getUserList(Role role) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public List getUserList(Job job) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public ListInfo getUserList(String sql, int offset, int maxItem)
			throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public List getUserList(String sql) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public List getUserList(Organization org) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public List getUserList(Group group) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public List getUserList(Operation oper) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public List getUserList(Res res) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public List getUserList() throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public List getUserList(Organization org, Role role)
			throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public List getUserList(Organization org, Job job) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public List getUserList(Orgjob orgjob) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public List getUserList(String orgid, String jobid) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public List getUserList(String[][] orgjobs) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getUserMainOrgId(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getUserSN(String orgid, String userid) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean getUserSnList(String orgId, String jobId, int jobSn)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public List getUserjoborgList(String userId, String orgId)
			throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public List getUsersListOfRole(String roleid) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public List getUsersListOfRoleInOrg(String roleid, String orgId)
			throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public List getmemberTypeList(String typeid) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isContainUser(Organization org) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isTaxmanager(String userId) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isUserExist(User user) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isUserjoborgExist(Userjoborg userjoborg)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isUserroleExist(Userrole userrole) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public User loadAssociatedSet(String userId, String associated)
			throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public void resetUserMainOrg(String userid, String oldmainorg) {
		// TODO Auto-generated method stub

	}

	public boolean storeAccredit(Accredit accredit) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public String storeAllUserSnJobOrg(String orgId, String jobId,
			String jobSn, String[] userId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean storeAlotUserJob(String[] ids, String[] jobid, String orgid)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public void storeAlotUserOrg(String[] ids, String[] jobids, String orgid)
			throws ManagerException, SPIException {
		// TODO Auto-generated method stub

	}

	public boolean storeAlotUserRole(String[] ids, String[] roleid)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean storeBatchUserOrg(String[] userIds, String[] orgIds,
			boolean isInsert) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean storeLogincount(String userName) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public void storeOrgUserOrder(String orgId, String[] userId)
			throws Exception {
		// TODO Auto-generated method stub

	}

	public boolean storeTempaccredit(Tempaccredit tempaccredit)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public String storeUJOAjax(String uid, String[] jobIds, String orgId) {
		// TODO Auto-generated method stub
		return null;
	}

	public void storeUJOAjax_batch(String[] ids, String[] jobid, String orgid) {
		// TODO Auto-generated method stub

	}

	public boolean storeUser(User user, String propName, String value)
			throws ManagerException {
		return false;
	}

	public String storeUserOrgJob(Integer uid, String orgId, String[] jobid) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean storeUserattr(Userattr userattr) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean storeUsergroup(Usergroup usergroup) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean storeUserjoborg(Userjoborg userjoborg)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean storeUserjoborg(String userId, String[] jobId, String orgId)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean storeUserjoborg(String userId, String jobId, String orgId,
			boolean needevent) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean storeUserjoborg(String userId, String jobId, String orgId,
			String jobuserSn, String jobSn, boolean needevent)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean storeUserresop(Userresop userresop) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean storeUserrole(Userrole userrole) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public void storeUserrole(String userId, String roleId)
			throws ManagerException {
		// TODO Auto-generated method stub

	}

	public boolean updateUser(User user) throws ManagerException {
		//是否同步portal真实用户
		boolean isSynPortalUser = ConfigManager.getInstance().getConfigBooleanValue("isSynPortalUser", false);
		if(!isSynPortalUser){
			return true;
		}else{
			String userrealName = user.getUserRealname();
			String userName = user.getUserName();
			String sql = "update user_ set GREETING='欢迎 "+userrealName+"' where SCREENNAME='"+userName+"'";
//			String sql_contact = "update contact_ set FIRSTNAME='"+userrealName+"' where "
			DBUtil db = new DBUtil();
			try {
				db.executeSelect("portal", "select count(1) from user_ where SCREENNAME='"+userName+"'");
				if(db.getInt(0, 0) > 0){
					db.executeSelect("portal", "select CONTACTID from user_ where SCREENNAME='"+userName+"'");
					String CONTACTID = db.getString(0, 0);
					db.executeUpdate("portal", "update contact_ set FIRSTNAME='"+userrealName+"' where CONTACTID='"+CONTACTID+"'");
					db.executeUpdate("portal", sql);
					return true;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public boolean updateUserPassword(User user) throws ManagerException {
		//是否同步portal真实用户
		boolean isSynPortalUser = ConfigManager.getInstance().getConfigBooleanValue("isSynPortalUser", false);
		
		if(!isSynPortalUser){
			String B_APP_ID = getB_APP_ID();
			String userName = user.getUserName();
			String pwd = user.getUserPassword();
			String sql = "update sso_user_mapping set B_USER_PASSWORD='"+pwd+"' where A_APP_ID='portal' and "
				+"B_APP_ID='"+B_APP_ID+"' and B_USER_NAME='"+userName+"'";
			DBUtil db = new DBUtil();
			try {
				db.executeUpdate("portal",sql);
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{//同步portal真实用户
			//portal加密方式
			String pwd = EncrpyPwd.encodePortalPassword(user.getUserPassword());
			String userName = user.getUserName();
			StringBuffer sql = new StringBuffer()
				.append("update user_ set PASSWORD_='").append(pwd).append("' ")
				.append("where SCREENNAME='").append(userName).append("'");
			DBUtil db = new DBUtil();
			try {
				db.executeUpdate("portal", sql.toString());
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public String userOrgInfo(AccessControl control, String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean userResCopy(String userId, String[] userid2)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * 得到B应用ID
	 * @return
	 */
	private String getB_APP_ID(){
		AccessControl control = AccessControl.getAccessControl();
		String sybsystemId = control.getCurrentSystemID();
		String ipAdd = control.getRemoteAddr();
		String port = control.getPort();
		String contextpath = control.getContextPath();
		String B_APP_ID = sybsystemId+"::http://"+ipAdd+":"+port+contextpath;
		return B_APP_ID;
	}

	public String addUser(User user, boolean isEvent) throws ManagerException {
		//是否同步portal真实用户
		boolean isSynPortalUser = ConfigManager.getInstance().getConfigBooleanValue("isSynPortalUser", false);
		if(!isSynPortalUser){
			return null;
		}
		TransactionManager tm = new TransactionManager();
		String userName = user.getUserName();
		String EMAILADDRESS = null;
		if(user.getUserEmail() == null || "".equals(user.getUserEmail())){
			EMAILADDRESS = userName + "@sany.com.cn"; 
		}else{
			EMAILADDRESS = user.getUserEmail();
		}
		//配置portal加密方式
		String pwd = user.getUserPassword();
		pwd = EncrpyPwd.encodePortalPassword(pwd);
		try {
			//判断新增用户是否在portal库存在
			DBUtil isPortal = new DBUtil();
			isPortal.executeSelect("portal", "select USERID from user_ where SCREENNAME='"+userName+"'");
			if(isPortal.size() > 0){
				return null;
			}
			//当前操作新增的用户名称
			String curUserName = AccessControl.getAccessControl().getUserAccount();
			DBUtil queryUser = new DBUtil();
			String curUserId = null;
			//获取当前用户id
			queryUser.executeSelect("portal", "select USERID from user_ where SCREENNAME='"+curUserName+"'");
			if(queryUser.size() > 0){
				curUserId = queryUser.getString(0, 0); 
			}else{
				return null;
			}
			//portal库表contact_插入数据
			DBUtil seq_next = new DBUtil();
			DBUtil seq_contact = new DBUtil();
			seq_next.executeSelect("portal", "select seq_contact_id.nextval as val from dual");
			String seq_CONTACTID = seq_next.getString(0, 0); 
			seq_next.executeSelect("portal","select ACCOUNTID from account_ where COMPANYID='10108'");
			String ACCOUNTID = seq_next.getString(0, 0); 
			StringBuffer insertContact_ = new StringBuffer();
			insertContact_.append("insert into contact_(CONTACTID,COMPANYID,USERID,")
				.append("USERNAME,CREATEDATE,MODIFIEDDATE,ACCOUNTID,PARENTCONTACTID,FIRSTNAME,LASTNAME,PREFIXID,")
				.append("SUFFIXID,MALE,BIRTHDAY) values('").append(seq_CONTACTID).append("','10108','").append(curUserId)
				.append("','").append(curUserName).append(" ").append(curUserName).append(" ").append(curUserName)
				.append("',sysdate,sysdate,'").append(ACCOUNTID).append("','0','")
				.append(user.getUserRealname()).append("','").append(userName)
				.append("','0','0','1',sysdate)");
			seq_contact.executeInsert("portal", insertContact_.toString());
			//portal库表user_插入数据
			seq_contact.executeSelect("portal", "select seq_user_id.nextval as val from dual");
			String sqe_userid = seq_contact.getString(0, 0);
			StringBuffer insertUser_ = new StringBuffer();
			insertUser_.append("insert all when totalsize <= 0 then into user_(UUID_,USERID,COMPANYID,CREATEDATE,")
				.append("MODIFIEDDATE,DEFAULTUSER,CONTACTID,PASSWORD_,PASSWORDENCRYPTED,PASSWORDRESET,PASSWORDMODIFIEDDATE,")
				.append("REMINDERQUERYQUESTION,REMINDERQUERYANSWER,")
				.append("GRACELOGINCOUNT,SCREENNAME,EMAILADDRESS,PORTRAITID,LANGUAGEID,TIMEZONEID,GREETING,FAILEDLOGINATTEMPTS,")
				.append("LOCKOUT,AGREEDTOTERMSOFUSE,ACTIVE_) values(seq_user_uuid.nextval,'").append(sqe_userid).append("',")
				.append("'10108',sysdate,sysdate,'0','").append(seq_CONTACTID).append("','").append(pwd)
				.append("','1','0',sysdate,'what-is-your-primary-frequent-flyer-number','123456','0','").append(userName).append("','").append(EMAILADDRESS)
				.append("','0','zh_CN','Asia/Shanghai','欢迎 ").append(user.getUserRealname()).append("','")
				.append("0','0','1','1') select count(1) totalsize from user_ where SCREENNAME='")
				.append(userName).append("'");
			
			//portal库表group_插入数据
			StringBuffer inserGroup_ = new StringBuffer();
			//portal库表LayoutSet插入数据
			StringBuffer inserLayoutSet0 = new StringBuffer();
			StringBuffer inserLayoutSet1 = new StringBuffer();
			
			tm.begin();
			DBUtil dbuser = new DBUtil();
			DBUtil dbuser_ = new DBUtil();
			DBUtil dbgroup = new DBUtil();
			DBUtil dbgroup_ = new DBUtil();
			DBUtil dbLayoutSet0 = new DBUtil();
			DBUtil dbLayoutSet1 = new DBUtil();
			//第一步 往portal，user_表中插入数据
			dbuser_.executeInsert("portal", insertUser_.toString());
//			System.out.println("insertUser_ = " + insertUser_);
			//第二步 往portal，group_表中插入数据
			dbuser.executeSelect("portal","select USERID from user_ where SCREENNAME='"+userName+"'");
			String userId = dbuser.getString(0, 0); 
			inserGroup_.append("insert all when totalsize <= 0 then into group_(GROUPID,COMPANYID,CREATORUSERID,")
				.append("CLASSNAMEID,CLASSPK,PARENTGROUPID,LIVEGROUPID,TYPE_,FRIENDLYURL,ACTIVE_) values(")
				.append("SEQ_GROUP_ID.NEXTVAL,'10108','").append(userId).append("','10034','")
				.append(userId).append("','0','0','0','/").append(userName).append("','1') select count(1) ")
				.append("totalsize from group_ where CREATORUSERID='").append(userId).append("'");
			dbgroup_.executeInsert("portal", inserGroup_.toString());
			
//			System.out.println("inserGroup_ = " + inserGroup_);
			
			//第三步 往portal，LayoutSet表中插入数据
			dbgroup.executeSelect("portal","select GROUPID from group_ where CREATORUSERID='"+userId+"'");
			String groupId = dbgroup.getString(0, 0);
			seq_next.executeSelect("portal", "select SEQ_LAYOUTSET_ID.nextval as val from dual");
			String LAYOUTSETID0 = seq_next.getString(0, 0);
			seq_next.executeSelect("portal", "select SEQ_LAYOUTSET_ID.nextval as val from dual");
			String LAYOUTSETID1 = seq_next.getString(0, 0);
			inserLayoutSet0.append("insert all when totalsize <= 0 then into LayoutSet(LAYOUTSETID,GROUPID,")
				.append("COMPANYID,PRIVATELAYOUT,LOGO,LOGOID,THEMEID,COLORSCHEMEID,WAPTHEMEID,WAPCOLORSCHEMEID,")
				.append("PAGECOUNT) values('").append(LAYOUTSETID0).append("','").append(groupId).append("','10108',")
				.append("'0','0','0','mytheme','01','mobile','01','0') select count(1) totalsize from ")
				.append("LayoutSet where GROUPID='").append(groupId).append("' and PRIVATELAYOUT='0'");
			dbLayoutSet0.executeInsert("portal", inserLayoutSet0.toString());
			inserLayoutSet1.append("insert all when totalsize <= 0 then into LayoutSet(LAYOUTSETID,GROUPID,")
				.append("COMPANYID,PRIVATELAYOUT,LOGO,LOGOID,THEMEID,COLORSCHEMEID,WAPTHEMEID,WAPCOLORSCHEMEID,")
				.append("PAGECOUNT) values('").append(LAYOUTSETID1).append("','").append(groupId).append("','10108',")
				.append("'1','0','0','mytheme','01','mobile','01','0') select count(1) totalsize from ")
				.append("LayoutSet where GROUPID='").append(groupId).append("' and PRIVATELAYOUT='1'");
			dbLayoutSet1.executeInsert("portal", inserLayoutSet1.toString());
			
//			StringBuffer inserLayout0 = new StringBuffer();
//			inserLayout0.append("insert all when totalsize <= 0 then into Layout(PLID,GROUPID,COMPANYID,PRIVATELAYOUT,")
//				.append("LAYOUTID,PARENTLAYOUTID,NAME,TITLE,TYPE_,TYPESETTINGS,HIDDEN_,FRIENDLYURL,ICONIMAGE,ICONIMAGEID,")
//				.append("PRIORITY,DLFOLDERID) values(SEQ_PLID.nextval,'").append(groupId).append("','10108','0',")
//				.append("'1','0','<?xml version=''1.0'' encoding=''UTF-8''?><root available-locales=\"en_US\" default-locale=\"en_US\"><name language-id=\"en_US\">Welcome</name></root>',")
//				.append("'<root />','portlet','column-1=82,23,\nlayout-template-id=2_columns_ii\ncolumn-2=8,19,','0',")
//				.append("'/home','0','0','0','0') select count(1) totalsize from Layout where groupid='")
//				.append(groupId).append("' and PRIVATELAYOUT='0'");
//			StringBuffer inserLayout1 = new StringBuffer();
//			inserLayout1.append("insert all when totalsize <= 0 then into Layout(PLID,GROUPID,COMPANYID,PRIVATELAYOUT,")
//				.append("LAYOUTID,PARENTLAYOUTID,NAME,TITLE,TYPE_,TYPESETTINGS,HIDDEN_,FRIENDLYURL,ICONIMAGE,ICONIMAGEID,")
//				.append("PRIORITY,DLFOLDERID) values(SEQ_PLID.nextval,'").append(groupId).append("','10108','1',")
//				.append("'1','0','<?xml version=''1.0'' encoding=''UTF-8''?><root available-locales=\"en_US\" default-locale=\"en_US\"><name language-id=\"en_US\">Welcome</name></root>',")
//				.append("'<root />','portlet','column-1=71_INSTANCE_OY0d,82,23,61,\nlayout-template-id=2_columns_ii\ncolumn-2=11,29,8,19,','0',")
//				.append("'/home','0','0','0','0') select count(1) totalsize from Layout where groupid='")
//				.append(groupId).append("' and PRIVATELAYOUT='1'");
//			dbLayoutSet1.executeInsert("portal", inserLayout0.toString());
//			dbLayoutSet1.executeInsert("portal", inserLayout1.toString());
//			System.out.println("inserLayoutSet0 = " + inserLayoutSet0);
//			System.out.println("inserLayoutSet1 = " + inserLayoutSet1);
			//为新增用户初始角色 角色名称 select roleid from role_ where name='Power User' and COMPANYID='10108'
			dbLayoutSet1.executeSelect("portal", "select roleid from role_ where name='Power User' and COMPANYID='10108'");
			String roleid = dbLayoutSet1.getString(0, 0);
			dbLayoutSet1.executeInsert("portal", "insert into USERS_ROLES(USERID,ROLEID) values('"+sqe_userid+"','"+roleid+"')");
			
			tm.commit();
		} catch (TransactionException e) {
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		} catch (RollbackException e) {
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
		}
		return null;
	}

	public boolean deleteBatchUser(User[] users) throws ManagerException {
		for(int i = 0; i < users.length; i++){
			this.deleteUser(users[i]);
		}
		return false;
	}

	@Override
	public List<User> getOrgManager(String org_id) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void loadUsers(UserCacheManager userCache) throws ManagerException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.manager.UserManager#ishistorypassword(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean ishistorypassword(int userid, String password)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.manager.UserManager#isPasswordExpired(int)
	 */
	@Override
	public boolean isPasswordExpired(int userid) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.manager.UserManager#isPasswordExpired(com.frameworkset.platform.sysmgrcore.entity.User)
	 */
	@Override
	public boolean isPasswordExpired(User user) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.manager.UserManager#initpasswordupdatetime(int)
	 */
	@Override
	public Timestamp initpasswordupdatetime(int userid) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.manager.UserManager#getPasswordExpiredTime(int)
	 */
	@Override
	public Date getPasswordExpiredTime(int userid) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.manager.UserManager#getPasswordExpiredTime(java.sql.Timestamp)
	 */
	@Override
	public Date getPasswordExpiredTime(Timestamp passwordupdatetime) {
		// TODO Auto-generated method stub
		return null;
	}



}
