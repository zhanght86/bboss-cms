package com.frameworkset.platform.sysmgrcore.manager.db;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.frameworkset.platform.security.authentication.CheckCallBack;
import com.frameworkset.platform.sysmgrcore.entity.User;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.util.StringUtil;


public class UserCacheManager {
	private Map<String,CheckCallBack> users = new HashMap<String,CheckCallBack>();
	private Map<String,CheckCallBack> usersByID = new HashMap<String,CheckCallBack>();
	private static UserCacheManager instance;
	private static final Logger log = Logger.getLogger(UserCacheManager.class);
	
	private void init()
	{
		try {
			log.debug("初始化用户缓存信息开始");
			 SecurityDatabase.getUserManager().loadUsers(this);
			log.debug("初始化用户缓存信息结束.");
		} catch (Exception e) {
			log.error("初始化用户缓存信息失败.",e);
		}
	}
	
	public void addUser(User user)
	{
		CheckCallBack user_ = buildCallback(  user ) ;
		users.put(user.getUserName(), user_);
		usersByID.put(String.valueOf(user.getUserId()), user_);
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

        String t = StringUtil.getFormatDate(user.getUserBirthday(), "yyyy-MM-dd HH:mm:ss");
        if (t != null)
            checkCallBack.setUserAttribute("userBirthday", t);
        t = StringUtil.getFormatDate(user.getUserRegdate(), "yyyy-MM-dd HH:mm:ss");
        if (t != null)
            checkCallBack.setUserAttribute("userRegdate", t);
        checkCallBack.setUserAttribute("userSn", user.getUserSn() + "");
        checkCallBack.setUserAttribute("userIsvalid", user.getUserIsvalid() + "");
        return checkCallBack;
    }
	
	public synchronized void refresh()
	{
		try {
			log.debug("刷新用户缓存信息开始");
			users.clear();
			usersByID.clear();
			SecurityDatabase.getUserManager().loadUsers(this);
			log.debug("刷新用户缓存信息结束.");
		} catch (Exception e) {
			log.error("刷新用户缓存信息失败.",e);
		}
	}
	
	public CheckCallBack getUser(String userAccount)
	{
		return users.get(userAccount);
	}
	
	public CheckCallBack getUserByID(String userId)
	{
		return usersByID.get(userId);
	}
	
	public Object getUserAttribute(String userAccount,String userAttribute)
	{
		CheckCallBack ss = getUser( userAccount);
		if(ss == null)
			return null;
		return ss.getUserAttribute(userAttribute);
	}
	
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

}
 