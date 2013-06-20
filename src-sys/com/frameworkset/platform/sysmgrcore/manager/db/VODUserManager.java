package com.frameworkset.platform.sysmgrcore.manager.db;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.frameworkset.event.Event;
import org.frameworkset.event.EventHandle;
import org.frameworkset.event.Listener;
import org.frameworkset.spi.SPIException;

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
import com.frameworkset.platform.sysmgrcore.manager.OrgManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.platform.sysmgrcore.manager.UserManager;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.util.ListInfo;
    
public class VODUserManager extends EventHandle implements UserManager {
  	  
        
	public boolean storeUser(User user) throws ManagerException {
		int range = Integer.MAX_VALUE / 3 * 2;
		Random rand = new Random(); 
		int randomId = rand.nextInt(range);// 随机数

		boolean r = false;
		PreparedDBUtil preDBUtil = new PreparedDBUtil();
		String sqlstr = "INSERT INTO TB_Employee (FD_Employee_ID,FD_Employee_Name,FD_Employee_LoginName,FD_Employee_Password,"
				+ "FD_Employee_Video,FD_Employee_Voice,FD_Employee_AddNew,FD_Employee_JobID,FD_Employee_LevelID,"
				+ "FD_Employee_Position,FD_Employee_IsDeleted,FD_Employee_Telephone,FD_Employee_Mobil,FD_Employee_Email) "
				+ "VALUES ("
				+ user.getUserId().intValue()
				+ ",'"
				+ user.getUserRealname()
				+ "','"
				+ user.getUserName()
				+ "',"
				+ "'"
				+ user.getUserPassword()
				+ "',1,1,1,0,0,"
				+ randomId
				+ ",0,0,0,0)";

		String sqlstr1 = "update TB_Employee set FD_Employee_Name='"
				+ user.getUserRealname() + "',FD_Employee_LoginName='"
				+ user.getUserName() + "',FD_Employee_Password='"
				+ user.getUserPassword() + "'" + "where FD_Employee_ID="
				+ user.getUserId().intValue() + "";

		String rtm = "rtm";

		try { // 新增用户

			preDBUtil.executeInsert(rtm, sqlstr);

			r = true;
		} catch (SQLException e) {
			try {
				preDBUtil.executeUpdate(rtm, sqlstr1); // 修改用户
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			r = true;
			e.printStackTrace();

		}

		return r;
	}

	public boolean storeUser(User user, String propName, String value)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean storeUserattr(Userattr userattr) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean storeUserjoborg(Userjoborg userjoborg)
			throws ManagerException {
		int range = Integer.MAX_VALUE / 3 * 2;
		Random rand = new Random();
		int randomId = rand.nextInt(range);
		UserManagerImpl um = new UserManagerImpl();
		User user = um.getUser("userId", userjoborg.getId().getUserId()
				.toString());
		int jobsn = userjoborg.getJobSn().intValue();
		int samejobusersn = userjoborg.getSameJobUserSn().intValue();
		String strSort = String.valueOf(jobsn) + String.valueOf(samejobusersn);

		// System.out.println(".........."+user.getUserRealname());
		boolean r = false;
		PreparedDBUtil preDBUtil = new PreparedDBUtil();
		String sqlstr = "update TB_Employee set FD_Employee_BranchID="
				+ Integer.parseInt(userjoborg.getId().getOrgId()) + ""
				+ ", FD_EMPLOYEE_POSITION=" + Integer.parseInt(strSort) + ""
				+ "where FD_Employee_ID="
				+ userjoborg.getId().getUserId().intValue() + "";
		String sqlstr1 = "INSERT INTO TB_Employee (FD_Employee_ID,FD_Employee_Name,FD_Employee_LoginName,FD_Employee_Password,FD_Employee_BranchID,FD_Employee_Video,FD_Employee_Voice,FD_Employee_AddNew) "
				+ "VALUES ("
				+ randomId
				+ ",'"
				+ user.getUserRealname()
				+ "','"
				+ user.getUserName()
				+ "','"
				+ user.getUserPassword()
				+ "',"
				+ Integer.parseInt(userjoborg.getId().getOrgId()) + ",1,1,1)";

		if (userjoborg != null) {

			try {
				String rtm = "rtm";

				preDBUtil.executeUpdate(rtm, sqlstr);
				// preDBUtil.executeInsert(rtm,sqlstr1);
				r = true;
			} catch (SQLException e) {
				e.printStackTrace();

			}
		}
		return r;
	}

	public boolean storeUserrole(Userrole userrole) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean storeUsergroup(Usergroup usergroup) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean storeUserresop(Userresop userresop) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean storeTempaccredit(Tempaccredit tempaccredit)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean storeAccredit(Accredit accredit) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean deleteUser(User user) throws ManagerException {
		boolean r = false;
		if (user != null) {
			PreparedDBUtil preDBUtil = new PreparedDBUtil();
			String sqlstr = "DELETE FROM TB_Employee WHERE FD_Employee_LoginName = '"
					+ user.getUserName() + "'";

			try {
				String rtm = "rtm";

				preDBUtil.executeDelete(rtm, sqlstr);
				r = true;

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return r;

	}

	public boolean deleteBatchUser(String[] userIds) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean deleteUserattr(Userattr userattr) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean deleteUserjoborg(Userjoborg userjoborg)
			throws ManagerException {
		boolean r = false;
		PreparedDBUtil preDBUtil = new PreparedDBUtil();
		String sqlstr = "update TB_Employee set FD_Employee_BranchID=1"
				+ "where FD_Employee_ID="
				+ userjoborg.getId().getUserId().intValue() + "";
		if (userjoborg != null) {

			try {
				String rtm = "rtm";

				preDBUtil.executeUpdate(rtm, sqlstr);
				r = true;
			} catch (SQLException e) {
				e.printStackTrace();

			}
		}
		return r;
	}

	public boolean deleteUserjoborg(Organization org, User user)
			throws ManagerException {
		boolean r = false;
		PreparedDBUtil preDBUtil = new PreparedDBUtil();
		String sqlstr = "update TB_Employee set FD_Employee_BranchID=1"
				+ "where FD_EMPLOYEE_ID="
				+ user.getUserId() + "";
		if (org != null && user != null) {

			try {
				String rtm = "rtm";

				preDBUtil.executeUpdate(rtm, sqlstr);
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
		PreparedDBUtil preDBUtil = new PreparedDBUtil();
		String sqlstr = "update TB_Employee set FD_Employee_BranchID=1"
				+ "where FD_Employee_BranchID="
				+ Integer.parseInt(org.getOrgId()) + "";
		if (job != null && org != null) {

			try {
				String rtm = "rtm";

				preDBUtil.executeUpdate(rtm, sqlstr);
				r = true;
			} catch (SQLException e) {
				e.printStackTrace();

			}
		}
		return r;
	}

	public boolean deleteUserjoborg(User user) throws ManagerException {
		boolean r = false;
		PreparedDBUtil preDBUtil = new PreparedDBUtil();
		String sqlstr = "update TB_Employee set FD_Employee_BranchID=1"
				+ "where FD_Employee_ID=" + user.getUserId().intValue() + "";
		if (user != null) {

			try {
				String rtm = "rtm";

				preDBUtil.executeUpdate(rtm, sqlstr);
				r = true;
			} catch (SQLException e) {
				e.printStackTrace();

			}
		}
		return r;
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

	public boolean deleteUserresop(Userresop userresop) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean deleteTempaccredit(Tempaccredit tempaccredit)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean deleteAccredit(Accredit accredit) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public User getUser(String propName, String value) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public User getUser(String hql) throws ManagerException {
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

	public List getUserList(String hsql) throws ManagerException {
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

	public List getAccreditList(String userName) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public List getTempaccredit(String userName) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isUserExist(User user) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isUserroleExist(Userrole userrole) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isUserjoborgExist(Userjoborg userjoborg)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public User loadAssociatedSet(String userId, String associated)
			throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}


	public void addListener(Listener listener) {
		// TODO Auto-generated method stub

	}

	public void change(Event source, boolean synchronizable) {
		// TODO Auto-generated method stub

	}

	public void change(Event source) {
		// TODO Auto-generated method stub

	}

	public boolean storeLogincount(String userName) {
		return false;

	}

	public boolean getUserSnList(String orgId, String jobId, int jobSn)
			throws ManagerException {
		boolean r = false;
		DBUtil db = new DBUtil();
		DBUtil db1 = new DBUtil();
		int jsn;
		int usn;
		int userId;
		try {
			
			String str ="select JOB_SN,SAME_JOB_USER_SN,user_id from  TD_SM_USERJOBORG " +
					"where  JOB_ID ='" + jobId + "'" + "and ORG_ID ='" + orgId +"'";
			db1.executeSelect(str);
			if (db1 != null && db1.size() > 0) {
				jsn = db1.getInt(0,"JOB_SN");
				
				
				for(int i=0;i<db1.size();i++){
					usn = db1.getInt(i,"SAME_JOB_USER_SN");
					String SN = String.valueOf(jsn)+String.valueOf(usn);
					userId = db1.getInt(i,"user_id");
					String sqlstr = "update TB_Employee set FD_EMPLOYEE_POSITION = "+ SN +""
					+ "where FD_Employee_ID=" + String.valueOf(userId) + "";
					db.executeUpdate(sqlstr);
	
				}
			}
			r = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return r;

	}

	public ListInfo getUserList(String sql, int offset, int maxItem)
			throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean deleteUserjoborg(Job job, User user) throws ManagerException {
		return false;

	}

	public List getUserjoborgList(String userId, String orgId)
			throws ManagerException {
		return null;

	}

	public List getDicList() throws ManagerException {
		return null;

	}

	public boolean storeUserjoborg(String userId, String jobId, String orgId,boolean needevent)
			throws ManagerException {
		boolean b = false;
		int userSN;
		int jobsn;
		try {
		
				
				DBUtil db = new DBUtil();

					 DBUtil db1 = new DBUtil();
					 DBUtil db2 = new DBUtil();
					String maxUser = "select max(SAME_JOB_USER_SN) as Sn from TD_SM_USERJOBORG where job_id ='"
						+ jobId + "' and" + " org_id ='" + orgId + "'";
					db1.executeSelect(maxUser);
					
					if (db1 != null && db1.size() > 0) {
						userSN = db1.getInt(0,"Sn");
					
						String jobSN = "select JOB_SN from TD_SM_ORGJOB where job_id ='"
							+ jobId + "' and" + " org_id ='" + orgId + "'";
						db2.executeSelect(jobSN);
					
						if (db2 != null && db2.size() > 0) {
							jobsn = db2.getInt(0,"JOB_SN");
							
							String strSort = String.valueOf(jobsn) + String.valueOf(userSN+1);
							String sql = "update TB_EMPLOYEE set FD_EMPLOYEE_BRANCHID ="+ orgId +"," +
									"FD_EMPLOYEE_POSITION="+ strSort +" where FD_EMPLOYEE_ID="+ userId +"";
						
						db.executeInsert(sql);
						}
					}
		

			b = true;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return b;

	}

	public boolean storeUserjoborg(String userId, String jobId[], String orgId)
			throws ManagerException {
	
		boolean b = false;
		int userSN;
		int jobsn;
		try {
			for (int i = 0; (jobId != null) && (i < jobId.length); i++) {
				
				DBUtil db = new DBUtil();
//				String existsql = "select *  from TD_SM_USERJOBORG where job_id ='"
//						+ jobId[i]
//						+ "' and"
//						+ " org_id ='"
//						+ orgId
//						+ "' and user_id =" + userId + "";
//				db.executeSelect(existsql);
//				// 如果记录已有，不进行操作
//				if (db.size() > 0) {
//					continue;
//				} else {
					 DBUtil db1 = new DBUtil();
					 DBUtil db2 = new DBUtil();
					String maxUser = "select max(SAME_JOB_USER_SN) as Sn from TD_SM_USERJOBORG where job_id ='"
						+ jobId[i] + "' and" + " org_id ='" + orgId + "'";
					db1.executeSelect(maxUser);
					
					if (db1 != null && db1.size() > 0) {
						userSN = db1.getInt(0,"Sn");
					
						String jobSN = "select JOB_SN from TD_SM_ORGJOB where job_id ='"
							+ jobId[i] + "' and" + " org_id ='" + orgId + "'";
						db2.executeSelect(jobSN);
					
						if (db2 != null && db2.size() > 0) {
							jobsn = db2.getInt(0,"JOB_SN");
							
							String strSort = String.valueOf(jobsn) + String.valueOf(userSN+1);
							String sql = "update TB_EMPLOYEE set FD_EMPLOYEE_BRANCHID ="+ orgId +"," +
									"FD_EMPLOYEE_POSITION="+ strSort +" where FD_EMPLOYEE_ID="+ userId +"";
						
						db.executeInsert(sql);
						}
					}
			}

			b = true;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return b;

	}

	public boolean deleteUserjoborg(String userId, String jobId, String orgId)
			throws ManagerException {
		boolean r = false;
		DBUtil db = new DBUtil();
		DBUtil db1 = new DBUtil();
		int jsn;
		int usn;
		String str="select * from td_sm_userjoborg where user_id="+ userId +" " +
				"and org_id='"+ orgId +"'";
		try {
			db.executeSelect(str);
			
			if(db.size()!=0){
				String sql ="select min(job_sn) as jsn,max(SAME_JOB_USER_SN) as usn from " +
						"td_sm_userjoborg where user_id="+ userId +" " +
						"and org_id='"+ orgId +"'";
				db1.executeSelect(sql);
				if (db1 != null && db1.size() > 0) {
					jsn = db1.getInt(0,"jsn");
					usn = db1.getInt(0,"usn");
					String SN = String.valueOf(jsn)+String.valueOf(usn);
					String sqlstr = "update TB_Employee set FD_EMPLOYEE_POSITION = "+ SN +""
						+ "where FD_Employee_ID=" + userId.toString() + "";
					db.executeUpdate(sqlstr);
				}	
				
			}else{
				String sqlstr = "update TB_Employee set FD_Employee_BranchID=1"
					+ "where FD_Employee_ID=" + userId.toString() + "";
				db.executeUpdate(sqlstr);
			}
		} catch (SQLException e1) {
		
			e1.printStackTrace();
		}
		
//		PreparedDBUtil preDBUtil = new PreparedDBUtil();
//		String sqlstr = "update TB_Employee set FD_Employee_BranchID=1"
//				+ "where FD_Employee_ID=" + userId.toString() + "";
//
//		try {
//			String rtm = "rtm";
//
//			preDBUtil.executeUpdate(rtm, sqlstr);
//			r = true;
//		} catch (SQLException e) {
//			e.printStackTrace();
//
//		}

		return r;

	}

	public boolean userResCopy(String userId, String[] userid)
			throws ManagerException {
		return false;

	}

	public List getUserList(String orgid, String jobid) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public List getUserList(String[][] orgjobs) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean deleteUsergroup(String userId, String groupId)
			throws ManagerException {
		return false;

	}

	public boolean storeAlotUserRole(String[] ids, String[] roleid)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean delAlotUserRole(String[] ids, String[] roleid)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean storeUserjoborg(String userId, String jobId, String orgId,
			String jobuserSn, String jobSn,boolean needevent) throws ManagerException {
		boolean r = false;
		PreparedDBUtil preDBUtil = new PreparedDBUtil();
		String strSort = jobSn + jobuserSn;
		String sqlstr = "update TB_Employee set FD_Employee_BranchID=" + orgId
				+ "" + ",FD_EMPLOYEE_POSITION ="+ strSort +" where FD_Employee_ID=" + userId.toString() + "";

		try {
			String rtm = "rtm";

			preDBUtil.executeUpdate(rtm, sqlstr);
			r = true;
		} catch (SQLException e) {
			e.printStackTrace();

		}

		return r;

	}

	public boolean storeAlotUserJob(String[] ids, String[] jobid, String orgid)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean delAlotJobRole(String[] ids, String[] jobid, String orgId)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}
	public List getmemberTypeList(String typeid) throws ManagerException{
		return null;
		 
	}
	public void storeUserrole(String userId,String roleId) throws ManagerException{
		
	}
	public void creatorUser(User user,String orgId,String jobId) throws ManagerException{
		System.out.println("新增用户同步了、、、、。。");
		System.out.println("orgId = " + orgId);
		System.out.println("jobId = " + jobId);
		System.out.println("user = " + user);
//		this.addUser(user);
	}

	public List getOrgUserList(String orgid) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public List getUsersListOfRole(String roleid) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public ListInfo getUserInfoList(String sql, int offset, int maxItem)
			throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 保存用户的排序
	 * @param orgId
	 * @param jobId
	 * @param jobSn
	 * @param userId
	 * @return
	 * @throws Exception 
	 * UserManager.java
	 * @author: ge.tao
	 */
	public  String storeAllUserSnJobOrg(String orgId, String jobId,
			String jobSn, String[] userId) throws Exception {
		try {
			UserManager userManager = SecurityDatabase.getUserManager();

			// 修改：需要完整用户对象信息
			// Organization org = new Organization();
			// org.setOrgId(orgId);
			OrgManager orgMgr = SecurityDatabase.getOrgManager();
			Organization org = orgMgr.getOrgById(orgId);
			// 修改结束
			Job job = new Job();
			job.setJobId(jobId);
			// 修改：需要完成用户对象信息
			// User user = new User();
			// user.setUserId(Integer.valueOf(userList[i]));

			// 在保存之前先删除该机构下改岗位的所有用户.然后再添加
			//userManager.deleteUserjoborg(job, org);
			DBUtil db = new DBUtil();
			String str="delete from td_sm_userjoborg where " +
					" org_id ='"+ orgId+ "' and job_id='"+ jobId +"'";
			db.executeDelete(str);

			for (int i = 0; userId != null && i < userId.length; i++) {
				User user = userManager.getUserById(userId[i]);
				// 修改结束
				Userjoborg userjoborg = new Userjoborg();
				userjoborg.setUser(user);
				userjoborg.setJob(job);
				userjoborg.setOrg(org);
				userjoborg.setSameJobUserSn( new Integer(i + 1));
				userjoborg.setJobSn(new Integer(Integer.parseInt(jobSn) + 1));
				userManager.storeUserjoborg(userjoborg);
			}

		} catch (Exception e) {
			return "fail";
		}
		return "success";
	}

	public void storeOrgUserOrder(String orgId, String[] userId) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public String deleteUJOAjax(String uid, String[] jobIds, String orgId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public int getUserSN(String orgid, String userid) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	public void storeUJOAjax_batch(String[] ids, String[] jobid, String orgid) {
		// TODO Auto-generated method stub
		
	}

	public void deleteUJOAjax_batch(String[] ids, String[] jobid, String orgId) throws ManagerException {
		// TODO Auto-generated method stub
		
	}

	public String deleteUserOrgJob(Integer uid, String orgId, String[] jobid) {
		// TODO Auto-generated method stub
		return null;
	}

	public String storeUJOAjax(String uid, String[] jobIds, String orgId) {
		// TODO Auto-generated method stub
		return null;
	}

	public String storeUserOrgJob(Integer uid, String orgId, String[] jobid) {
		// TODO Auto-generated method stub
		return null;
	}

	public void resetUserMainOrg(String userid, String oldmainorg) {
		// TODO Auto-generated method stub
		
	}

	public void storeAlotUserOrg(String[] ids, String[] jobids, String orgid) throws ManagerException, SPIException {
		// TODO Auto-generated method stub
		
	}

	public void delAlotUserOrg(String[] ids, String[] jobids, String orgId) throws ManagerException {
		// TODO Auto-generated method stub
		
	}

	public String addUser(User user) throws ManagerException {
//		System.out.println("新增用户同步了、、、、。。");
		return null;
	}

	public boolean updateUser(User user) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean updateUserPassword(User user) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.manager.UserManager#addUsergroup(java.lang.Integer, java.lang.String[])
	 */
	public boolean addUsergroup(Integer userid, String[] groupid) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.manager.UserManager#deleteUsergroup(java.lang.Integer, java.lang.String[])
	 */
	public boolean deleteUsergroup(Integer userid, String[] groupids) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.manager.UserManager#addUserrole(java.lang.String, java.lang.String[])
	 */
	public void addUserrole(String userId, String[] roleIds) throws ManagerException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.manager.UserManager#deleteUserrole(java.lang.String, java.lang.String[])
	 */
	public void deleteUserrole(String userId, String[] roleIds) throws ManagerException {
		// TODO Auto-generated method stub
		
	}

	public boolean addUserOrg(String[] userIds, String orgId, String classType) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public User getUserById(String userId) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public User getUserByName(String userName) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean deleteUserRes(User user) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean storeBatchUserOrg(String[] userIds, String[] orgIds, boolean isInsert) throws ManagerException{
		// TODO Auto-generated method stub
		return false;
	}

	public boolean deleteDisperseOrguser() {
		// TODO Auto-generated method stub
		return false;
	}

	public List getUsersListOfRoleInOrg(String roleid, String orgId) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isTaxmanager(String userId) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public List getOrgUserList(String orgid, String userId) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isContainUser(Organization org) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.manager.UserManager#addUserrole(java.lang.String, java.lang.String[], java.lang.String)
	 */
	public void addUserrole(String userId, String[] roleIds, String currentUserId) throws ManagerException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.manager.UserManager#deleteUserrole(java.lang.String, java.lang.String[], java.lang.String)
	 */
	public void deleteUserrole(String userId, String[] roleIds, String roleTypes) throws ManagerException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.manager.UserManager#userOrgInfo(java.lang.String)
	 */
	public String userOrgInfo(AccessControl control,String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean deleteBatchUserRes(String[] userIds) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public String getUserMainOrgId(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] getCuruserAdministrableDeleteUser(String curUserId,String[] selectUserNames) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean deleteUserjoborg(String userId, String jobId, String orgId,
			boolean sendEvent) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public String addUser(User user, boolean isEvent) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean deleteBatchUser(User[] users) throws ManagerException {
		// TODO Auto-generated method stub
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
	public boolean isPasswordExpired(User user) throws ManagerException {
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
	public Date getPasswordExpiredTime(Timestamp passwordupdatetime,int expiredays) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getDefaultPasswordDualTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.manager.UserManager#getUserPasswordDualTimeByUserAccount(java.lang.String)
	 */
	@Override
	public int getUserPasswordDualTimeByUserAccount(String userAccount)
			throws ManagerException {
		// TODO Auto-generated method stub
		return 0;
	}

	
	
}
