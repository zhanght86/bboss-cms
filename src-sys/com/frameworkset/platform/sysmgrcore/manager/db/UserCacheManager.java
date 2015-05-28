package com.frameworkset.platform.sysmgrcore.manager.db;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.frameworkset.spi.BaseApplicationContext;

import com.frameworkset.common.poolman.Record;
import com.frameworkset.common.poolman.SQLExecutor;
import com.frameworkset.common.poolman.handle.NullRowHandler;
import com.frameworkset.platform.config.ConfigManager;
import com.frameworkset.platform.security.authentication.CheckCallBack;
import com.frameworkset.platform.sysmgrcore.entity.User;
import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.platform.sysmgrcore.manager.UserManager;
import com.frameworkset.platform.sysmgrcore.purviewmanager.db.FunctionDB;
import com.frameworkset.util.StringUtil;

/**
 * userName
userID
password
orgId
logincount
userAccount
remark1
remark2
remark3
remark4
remark5
userAddress
userEmail
userFax
userHometel
userIdcard
userMobiletel1
userMobiletel2
userOicq
userPinyin
userPostalcode
userSex
userType
userWorknumber
userWorktel
userBirthday
userRegdate
userSn
userIsvalid
 * @author yinbp
 *
 */
public class UserCacheManager {
	private Map<String,CheckCallBack> users = new HashMap<String,CheckCallBack>();
	private Map<String,CheckCallBack> usersByID = new HashMap<String,CheckCallBack>();
	private Map<String,CheckCallBack> usersByWorkNo = new HashMap<String,CheckCallBack>();
	private static UserCacheManager instance;
	private static final Logger log = Logger.getLogger(UserCacheManager.class);
	private static boolean hasloadUsers =false;
	private static Method loadUsers;
	static 
	{
		try {
			loadUsers = UserManager.class.getMethod("loadUsers", UserCacheManager.class);
			if(loadUsers != null)
				hasloadUsers = true;
		} catch (SecurityException e) {
			hasloadUsers = false;
		} catch (NoSuchMethodException e) {
			hasloadUsers = false;
		}
		
		BaseApplicationContext.addShutdownHook(new Runnable(){

			@Override
			public void run() {
				destroy();
				
			}
			
		});
	}
	
	void _destory()
	{
		users.clear();
		usersByID.clear();
		this.usersByWorkNo.clear();
		
	}
	
	public static void destroy()
	{
		if(instance != null)
		{
			instance._destory();
			
		}
	}
	
	private void init()
	{
		try {
			boolean initusercache = ConfigManager.getInstance().getConfigBooleanValue("initusercache",false);
			if(initusercache)
			{
				log.debug("初始化用户缓存信息开始");	
				if(hasloadUsers)
				{
					UserManager m = SecurityDatabase.getUserManager();//.loadUsers(this);
					loadUsers.invoke(m, this);
				}
				log.debug("初始化用户缓存信息结束.");
			}
		} catch (Exception e) {
			log.error("初始化用户缓存信息失败.",e);
		}
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
			user.setPasswordDualedTime(dbUtil.getInt( "Password_DualTime"));
			user.setPasswordUpdatetime(dbUtil.getTimestamp("password_updatetime"));
			user.setPasswordExpiredTime((Timestamp) SecurityDatabase.getUserManager().getPasswordExpiredTime(user.getPasswordUpdatetime(),user.getPasswordDualedTime()));
			
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
	private User _loadUserByAccount(String userAccount) throws ManagerException
	{
		String sql = "select u.*,ou.org_id from td_sm_user u left join td_sm_orguser ou on u.user_id = ou.user_id where u.USER_NAME=?";
		final User user = new User();
		try {
			
			SQLExecutor.queryByNullRowHandler(new NullRowHandler<User>(){

				

				@Override
				public void handleRow(Record record) throws Exception {
					int userid = record.getInt("user_id");
//					User user = new User();
                    getUser( user,record) ;

					String orgjob = FunctionDB.getUserorgjobinfos(userid);
					if(orgjob.endsWith(","))
					{
						orgjob = orgjob.substring(0, orgjob.length() - 1);
					}
					user.setOrgName(orgjob);
					
					//System.out.println("orgId = " + dbUtil.getString(i, "org_id"));
					user.setMainOrg(record.getString( "org_id"));
					
				}
				
			}, sql,userAccount);
			if(user.getUserName() == null || user.getUserName().equals(""))
				return null;
			return user;
		} catch (Exception e) {
			throw new ManagerException(e);
		}

	}
	
	private User _loadUserByWorkNo(String userWorkNo) throws ManagerException
	{
		String sql = "select u.*,ou.org_id from td_sm_user u left join td_sm_orguser ou on u.user_id = ou.user_id where u.USER_WORKNUMBER=?";
		final User user = new User();
		try {
			
			SQLExecutor.queryByNullRowHandler(new NullRowHandler<User>(){

				

				@Override
				public void handleRow(Record record) throws Exception {
					int userid = record.getInt("user_id");
//					User user = new User();
                    getUser( user,record) ;

					String orgjob = FunctionDB.getUserorgjobinfos(userid);
					if(orgjob.endsWith(","))
					{
						orgjob = orgjob.substring(0, orgjob.length() - 1);
					}
					user.setOrgName(orgjob);
					
					//System.out.println("orgId = " + dbUtil.getString(i, "org_id"));
					user.setMainOrg(record.getString( "org_id"));
					
				}
				
			}, sql,userWorkNo);
			if(user.getUserName() == null || user.getUserName().equals(""))
				return null;
			return user;
		} catch (Exception e) {
			throw new ManagerException(e);
		}

	}
	
	private User _loadUserByID(int userID) throws ManagerException
	{
		String sql = "select u.*,ou.org_id from td_sm_user u left join td_sm_orguser ou on u.user_id = ou.user_id where u.user_id=?";

		try {
			final User user = new User();
			SQLExecutor.queryByNullRowHandler(new NullRowHandler<User>(){

				

				@Override
				public void handleRow(Record record) throws Exception {
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
				
			}, sql,userID);
			return user;
		} catch (Exception e) {
			throw new ManagerException(e);
		}

	}
	
	public void addUser(User user)
	{
		CheckCallBack user_ = buildCallback(  user ) ;
		users.put(user.getUserName(), user_);
		usersByID.put(String.valueOf(user.getUserId()), user_);
		this.usersByWorkNo.put(user.getUserWorknumber(), user_);
	}
	
	protected CheckCallBack buildCallback( User user ) 
    {
		CheckCallBack checkCallBack = new CheckCallBack();
//    	 OrgManager orgManager = SecurityDatabase.getOrgManager();
//         Organization org = orgManager.getMainOrganizationOfUser(userName);
        checkCallBack.setUserAttribute("userName", user.getUserRealname());
        checkCallBack.setUserAttribute("userID", user.getUserId().toString());
        checkCallBack.setUserAttribute("password", user.getUserPassword());
        // 获取当前登陆用户所在主机构************
       
     
            checkCallBack.setUserAttribute("orgId", user.getMainOrg());
        
//        // 获取当前登陆用户所在机构列表，不包含主机构************
//        String orgname = orgManager.getSecondOrganizations(userName);
//        if (orgname == null || orgname.equals("")) {
//            checkCallBack.setUserAttribute("orgName", "没有兼职单位");
//        } else {
//            checkCallBack.setUserAttribute("orgName", orgname);
//        }
//
//        // 获取当前登陆用户所在机构列表，不包含主机构************
//        List secondOrgs = orgManager.getSecondOrganizationsOfUser(userName);
//        checkCallBack.setUserAttribute("secondOrgs", secondOrgs);

        if (user.getUserLogincount() != null) {
            user.setUserLogincount(new Integer(user.getUserLogincount().intValue() + 1));
        } else {
            user.setUserLogincount(new Integer(1));
        }
        
        // -----保存用户登陆次数
        int count = user.getUserLogincount().intValue();
        checkCallBack.setUserAttribute("logincount", count + "");
        checkCallBack.setUserAttribute("userAccount", StringUtil.replaceNull(user.getUserName(), ""));
        checkCallBack.setUserAttribute("remark1", StringUtil.replaceNull(user.getRemark1(), ""));
        checkCallBack.setUserAttribute("remark2", StringUtil.replaceNull(user.getRemark2(), ""));
        checkCallBack.setUserAttribute("remark3", StringUtil.replaceNull(user.getRemark3(), ""));
        checkCallBack.setUserAttribute("remark4", StringUtil.replaceNull(user.getRemark4(), ""));
        checkCallBack.setUserAttribute("remark5", StringUtil.replaceNull(user.getRemark5(), ""));
        checkCallBack.setUserAttribute("userAddress", StringUtil.replaceNull(user.getUserAddress(), ""));
        checkCallBack.setUserAttribute("userEmail", StringUtil.replaceNull(user.getUserEmail(), ""));
        checkCallBack.setUserAttribute("userFax", StringUtil.replaceNull(user.getUserFax(), ""));
        checkCallBack.setUserAttribute("userHometel", StringUtil.replaceNull(user.getUserHometel(), ""));
        checkCallBack.setUserAttribute("userIdcard", StringUtil.replaceNull(user.getUserIdcard(), ""));
        checkCallBack.setUserAttribute("userMobiletel1", StringUtil.replaceNull(user.getUserMobiletel1(), ""));
        checkCallBack.setUserAttribute("userMobiletel2", StringUtil.replaceNull(user.getUserMobiletel2(), ""));
        checkCallBack.setUserAttribute("userOicq", StringUtil.replaceNull(user.getUserOicq(), ""));
        checkCallBack.setUserAttribute("userPinyin", StringUtil.replaceNull(user.getUserPinyin(), ""));
        checkCallBack.setUserAttribute("userPostalcode", StringUtil.replaceNull(user.getUserPostalcode(), ""));
        checkCallBack.setUserAttribute("userSex", StringUtil.replaceNull(user.getUserSex(), ""));
        checkCallBack.setUserAttribute("userType", StringUtil.replaceNull(user.getUserType(), ""));
        checkCallBack.setUserAttribute("userWorknumber", StringUtil.replaceNull(user.getUserWorknumber(), ""));
        checkCallBack.setUserAttribute("userWorktel", StringUtil.replaceNull(user.getUserWorktel(), ""));
       
       
       
        if (user.getPasswordUpdatetime() != null)
        {
        	String t = StringUtil.getFormatDate(user.getPasswordUpdatetime(), "yyyy-MM-dd HH:mm:ss");
            checkCallBack.setUserAttribute("passwordUpdateTime", t);
        }
        else
        {
        	checkCallBack.setUserAttribute("passwordUpdateTime", "");
        }
        
        if (user.getPasswordExpiredTime() != null)
        {
        	String t = StringUtil.getFormatDate(user.getPasswordExpiredTime(), "yyyy-MM-dd HH:mm:ss");
            checkCallBack.setUserAttribute("passwordExpiredTime", t);
        }
        else
        {
        	checkCallBack.setUserAttribute("passwordExpiredTime", "");
        }
        String t = StringUtil.getFormatDate(user.getUserBirthday(), "yyyy-MM-dd HH:mm:ss");
        if (t != null)
            checkCallBack.setUserAttribute("userBirthday", t);
        t = StringUtil.getFormatDate(user.getUserRegdate(), "yyyy-MM-dd HH:mm:ss");
        if (t != null)
            checkCallBack.setUserAttribute("userRegdate", t);
        checkCallBack.setUserAttribute("userSn", user.getUserSn() + "");
        checkCallBack.setUserAttribute("userIsvalid", user.getUserIsvalid() + "");
        checkCallBack.setUserAttribute("orgName", user.getOrgName());
        checkCallBack.setUserAttribute("mainOrg", user.getMainOrg());
        return checkCallBack;
    }
	
	public synchronized void refresh()
	{
		try {
			log.debug("刷新用户缓存信息开始");
			users.clear();
			usersByID.clear();
			init();
			log.debug("刷新用户缓存信息结束.");
		} catch (Exception e) {
			log.error("刷新用户缓存信息失败.",e);
		}
	}
	 
	public CheckCallBack getUserByWorkNo(String worknumber)
	{
		CheckCallBack user = this.usersByWorkNo.get(worknumber);
		if(user !=null )
			return user;
		else
		{
			synchronized(UserCacheManager.class)
			{
				user = this.usersByWorkNo.get(worknumber);
				if(user !=null )
					return user;
				User user_ = null;
				try {
					user_ = this._loadUserByWorkNo(worknumber);
					if(user_!= null)
					{
						user = buildCallback(  user_ ) ;
						users.put(user_.getUserName(), user);
						usersByID.put(String.valueOf(user_.getUserId()), user);
						usersByWorkNo.put(user_.getUserWorknumber(), user);
					}
				} catch (ManagerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			return user;
		}
	}
	public CheckCallBack getUser(String userAccount)
	{
		CheckCallBack user = users.get(userAccount);
		if(user !=null )
			return user;
		else
		{
			synchronized(UserCacheManager.class)
			{
				user = users.get(userAccount);
				if(user !=null )
					return user;
				User user_ = null;
				try {
					user_ = this._loadUserByAccount(userAccount);
					if(user_!= null)
					{
						user = buildCallback(  user_ ) ;
						users.put(user_.getUserName(), user);
						usersByID.put(String.valueOf(user_.getUserId()), user);
						usersByWorkNo.put(user_.getUserWorknumber(), user);
					}
				} catch (ManagerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			return user;
		}
	}
	
	public CheckCallBack getUserByID(String userId)
	{
//		return usersByID.get(userId);
		CheckCallBack user = usersByID.get(userId);
		if(user !=null )
			return user;
		else
		{
			synchronized(UserCacheManager.class)
			{
				user = usersByID.get(userId);
				if(user !=null )
					return user;
				User user_ = null;
				try {
					user_ = this._loadUserByID(Integer.parseInt(userId));
					if(user_!= null)
					{
						user = buildCallback(  user_ ) ;
						users.put(user_.getUserName(), user);
						usersByID.put(String.valueOf(user_.getUserId()), user);
						usersByWorkNo.put(user_.getUserWorknumber(), user);
					}
				} catch (ManagerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			return user;
		}
	}
	/**
     * userName
userID
password
orgId
logincount
userAccount
remark1
remark2
remark3
remark4
remark5
userAddress
userEmail
userFax
userHometel
userIdcard
userMobiletel1
userMobiletel2
userOicq
userPinyin
userPostalcode
userSex
userType
userWorknumber
userWorktel
userBirthday
userRegdate
userSn
userIsvalid
passwordExpiredTime
passwordUpdateTime
     * @param userAttribute
     * @return
     */
	public Object getUserAttribute(String userAccount,String userAttribute)
	{
		CheckCallBack ss = getUser( userAccount);
		if(ss == null)
			return null;
		return ss.getUserAttribute(userAttribute);
	}
	/**
     * userName
userID
password
orgId
logincount
userAccount
remark1
remark2
remark3
remark4
remark5
userAddress
userEmail
userFax
userHometel
userIdcard
userMobiletel1
userMobiletel2
userOicq
userPinyin
userPostalcode
userSex
userType
userWorknumber
userWorktel
userBirthday
userRegdate
userSn
userIsvalid
passwordExpiredTime
passwordUpdateTime
     * @param userAttribute
     * @return
     */
	public Object getUserAttributeByID(String userID,String userAttribute)
	{
		CheckCallBack ss = getUserByID( userID);
		if(ss == null)
			return null;
		return ss.getUserAttribute(userAttribute);
	}

	public static UserCacheManager getInstance() {
		if(instance != null)
			return instance;
		synchronized(UserCacheManager.class)
		{
			if(instance != null)
				return instance;
			UserCacheManager i = new UserCacheManager();
			i.init();
			instance = i;			
		}
		return instance;
	}
	public static void main(String[] args)
	{
		Object value = UserCacheManager.getInstance().getUserAttribute("yinbp","userName");
		System.out.println(value);
		value = UserCacheManager.getInstance().getUserAttribute("yinbp","userAccount");
		
		value = UserCacheManager.getInstance().getUserAttributeByID("1","userAccount");
		System.out.println(value);
		value = UserCacheManager.getInstance().getUserAttributeByID("1","userAccount");
	}

}
 