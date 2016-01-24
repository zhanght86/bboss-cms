package com.frameworkset.platform.sysmgrcore.manager;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.frameworkset.event.EventHandle;
import org.frameworkset.spi.SPIException;
import org.frameworkset.util.MoreListInfo;

import com.frameworkset.platform.security.AccessControl;
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
import com.frameworkset.platform.sysmgrcore.manager.db.UserCacheManager;
import com.frameworkset.util.ListInfo;

public class BaseUserManager extends EventHandle implements UserManager {

	@Override
	public boolean deleteBatchUserRes(String[] userIds, boolean broadcastevent)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void loadUsers(UserCacheManager userCache) throws ManagerException {
		// TODO Auto-generated method stub

	}

	@Override
	public void fixuserorg(String[] userId, String orgid) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<User> getUsers(String username) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MoreListInfo getMoreUsers(String username, long offset, int pagesize)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean ishistorypassword(int userid, String password)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getUserPasswordDualTimeByUserAccount(String userAccount)
			throws ManagerException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Date getPasswordExpiredTimeByUserAccount(String userAccount)
			throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getPasswordExpiredTime(int userid) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isPasswordExpired(int userid) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean storeUser(User user, String propName, String value)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean storeLogincount(String userName) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean storeUserattr(Userattr userattr) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean storeUserjoborg(Userjoborg userjoborg)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean storeUserrole(Userrole userrole) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean storeUsergroup(Usergroup usergroup) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean storeUserresop(Userresop userresop) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean storeTempaccredit(Tempaccredit tempaccredit)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean storeAccredit(Accredit accredit) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean storeAlotUserRole(String[] ids, String[] roleid)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean storeUserjoborg(String userId, String[] jobId, String orgId)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void storeUserrole(String userId, String roleId)
			throws ManagerException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean storeUserjoborg(String userId, String jobId, String orgId,
			boolean needevent) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean storeUserjoborg(String userId, String jobId, String orgId,
			String jobuserSn, String jobSn, boolean needevent)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean storeAlotUserJob(String[] ids, String[] jobid, String orgid)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteUser(User user) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteBatchUser(String[] userIds) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteBatchUser(User[] users) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteBatchUserRes(String[] userIds) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteUserattr(Userattr userattr) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteUserjoborg(Userjoborg userjoborg)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteUserjoborg(Job job, Organization org)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteUserjoborg(Job job, User user) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteUserjoborg(Organization org, User user)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteUserjoborg(User user) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteUsergroup(Usergroup usergroup) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteUsergroup(User user) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteUsergroup(Group group) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteUserrole(Userrole userrole) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteUserrole(User user) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteUserrole(Organization org, Role role)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteUserrole(Role role, Group group)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteUserresop(Userresop userresop) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteTempaccredit(Tempaccredit tempaccredit)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteAccredit(Accredit accredit) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteUserjoborg(String userId, String jobId, String orgId)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteUserjoborg(String userId, String jobId, String orgId,
			boolean sendEvent) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delAlotUserRole(String[] ids, String[] roleid)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteUsergroup(String userId, String groupId)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delAlotJobRole(String[] ids, String[] jobid, String orgId)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public User getUser(String propName, String value) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User getUserById(String userId) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User getUserByName(String userName) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User getUser(String hql) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getUserList(String propName, String value, boolean isLike)
			throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getUserList(Role role) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getUserList(Job job) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListInfo getUserList(String sql, int offset, int maxItem)
			throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListInfo getUserInfoList(String sql, int offset, int maxItem)
			throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getUserList(String sql) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getUserjoborgList(String userId, String orgId)
			throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getUserSnList(String orgId, String jobId, int jobSn)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List getUserList(Organization org) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getDicList() throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getUserList(Group group) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getUserList(Operation oper) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getUserList(Res res) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getUserList() throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getUserList(Organization org, Role role)
			throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getUserList(Organization org, Job job) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getUserList(Orgjob orgjob) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getAccreditList(String userName) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getTempaccredit(String userName) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getUserList(String orgid, String jobid) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getUserList(String[][] orgjobs) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getmemberTypeList(String typeid) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isUserExist(User user) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isUserroleExist(Userrole userrole) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isUserjoborgExist(Userjoborg userjoborg)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public User loadAssociatedSet(String userId, String associated)
			throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean userResCopy(String userId, String[] userid2)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void creatorUser(User user, String orgId, String jobId)
			throws ManagerException {
		// TODO Auto-generated method stub

	}

	@Override
	public List getOrgUserList(String orgid) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getOrgUserList(String orgid, String userId)
			throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getUsersListOfRole(String roleid) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getUsersListOfRoleInOrg(String roleid, String orgId)
			throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String storeAllUserSnJobOrg(String orgId, String jobId,
			String jobSn, String[] userId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void storeOrgUserOrder(String orgId, String[] userId)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public String deleteUJOAjax(String uid, String[] jobIds, String orgId)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getUserSN(String orgid, String userid) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void storeUJOAjax_batch(String[] ids, String[] jobid, String orgid) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteUJOAjax_batch(String[] ids, String[] jobid, String orgId)
			throws ManagerException {
		// TODO Auto-generated method stub

	}

	@Override
	public String deleteUserOrgJob(Integer uid, String orgId, String[] jobid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String storeUJOAjax(String uid, String[] jobIds, String orgId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String storeUserOrgJob(Integer uid, String orgId, String[] jobid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void resetUserMainOrg(String userid, String oldmainorg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void storeAlotUserOrg(String[] ids, String[] jobids, String orgid)
			throws ManagerException, SPIException {
		// TODO Auto-generated method stub

	}

	@Override
	public void delAlotUserOrg(String[] ids, String[] jobids, String orgId)
			throws ManagerException {
		// TODO Auto-generated method stub

	}

	@Override
	public String addUser(User user) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String addUser(User user, boolean isEvent) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateUser(User user) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateUserPassword(User user) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addUserOrg(String[] userIds, String orgId, String classType)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addUserOrg(String[] userIds, String orgId, String classType,
			boolean broadcastevent) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addUsergroup(Integer userid, String[] groupid)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteUsergroup(Integer userid, String[] groupids)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addUserrole(String userId, String[] roleIds)
			throws ManagerException {
		// TODO Auto-generated method stub

	}

	@Override
	public void addUserrole(String userId, String[] roleIds,
			String currentUserId) throws ManagerException {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteUserrole(String userId, String[] roleIds)
			throws ManagerException {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteUserrole(String userId, String[] roleIds, String roleTypes)
			throws ManagerException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean deleteUserRes(User user) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean storeBatchUserOrg(String[] userIds, String[] orgIds,
			boolean isInsert) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean storeBatchUserOrg(String[] userIds, String[] orgIds,
			boolean isInsert, boolean broadcastevent) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteDisperseOrguser() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isTaxmanager(String userId) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isContainUser(Organization org) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String userOrgInfo(AccessControl control, String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUserMainOrgId(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getCuruserAdministrableDeleteUser(String curUserId,
			String[] selectUserNames) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> getOrgManager(String org_id) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isPasswordExpired(User user) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Timestamp initpasswordupdatetime(int userid) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getPasswordExpiredTime(Timestamp passwordupdatetime,
			int expiredays) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getDefaultPasswordDualTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public User getUserByWorknumberOrUsername(String userId) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListInfo getUsersListInfoOfRole(String roleid, long offset,
			int pagesize) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteBatchUser(String[] userIds, boolean sendevent) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

}
