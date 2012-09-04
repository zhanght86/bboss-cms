package com;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.RollbackException;

import com.frameworkset.platform.config.ConfigManager;
import com.frameworkset.platform.security.authentication.EncrpyPwd;
import com.frameworkset.platform.sysmgrcore.entity.User;
import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.orm.transaction.TransactionException;
import com.frameworkset.orm.transaction.TransactionManager;


public class SynPortalUser {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		addUser();
	}
	
	public static void addUser(){
		List<User> list = new ArrayList<User>();
		String sql = "select * from td_sm_user";
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(sql);
			list = getUsers(db);
			for(int i = 0; i < list.size(); i++){
				addUser(list.get(i));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ManagerException e) {
			e.printStackTrace();
		}

	}
	
	private static List<User> getUsers(DBUtil dbUtil) throws ManagerException {
		List<User> list = new ArrayList<User>();
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
				
				user.setUserEmail(dbUtil.getString(i, "USER_EMAIL"));
				user.setUserAddress(dbUtil.getString(i, "USER_ADDRESS"));
				user.setUserPostalcode(dbUtil.getString(i, "USER_POSTALCODE"));
				user.setUserIdcard(dbUtil.getString(i, "USER_IDCARD"));
				user.setUserIsvalid(new Integer(dbUtil.getInt(i, "USER_ISVALID")));
				
				//注册日期
//				if(String.valueOf(dbUtil.getTimestamp(i, "USER_REGDATE")) != "null"){
//					user.setUserRegdate(new java.sql.Date(dbUtil.getTimestamp(i,"USER_REGDATE").getTime()));
//				}else{
//					user.setUserRegdate((java.sql.Date)dbUtil.getDate(i, "USER_REGDATE"));
//				}
				
				user.setUserLogincount(new Integer(dbUtil.getInt(i,"USER_LOGINCOUNT")));
//				user.setUserType(dbUtil.getString(i, "USER_TYPE"));
//				user.setRemark1(dbUtil.getString(i, "REMARK1"));
//				user.setRemark2(dbUtil.getString(i, "REMARK2"));
//				user.setRemark3(dbUtil.getString(i, "REMARK3"));
//				user.setRemark4(dbUtil.getString(i, "REMARK4"));
//				user.setRemark5(dbUtil.getString(i, "REMARK5"));
				
				//过期日期
//				if(String.valueOf(dbUtil.getTimestamp(i, "PAST_TIME")) != "null"){
//					user.setPastTime(new java.sql.Date(dbUtil.getTimestamp(i, "PAST_TIME").getTime()));
//				}else{
//					user.setPastTime((java.sql.Date)dbUtil.getDate(i, "PAST_TIME"));
//				}
//				
//				user.setDredgeTime(dbUtil.getString(i, "DREDGE_TIME"));
				
				//用户最后登陆时间
//				if(String.valueOf(dbUtil.getTimestamp(i, "LASTLOGIN_DATE")) != "null"){
//					user.setLastlogindate(new java.sql.Date(dbUtil.getTimestamp(i,"LASTLOGIN_DATE").getTime()));
//				}else{
//					user.setLastlogindate((java.sql.Date)dbUtil.getDate(i, "LASTLOGIN_DATE"));
//				}
				
				//user.setWorklength(dbUtil.getString(i, "WORKLENGTH"));
				//user.setPolitics(dbUtil.getString(i, "POLITICS"));
				//user.setIstaxmanager(dbUtil.getInt(i, "ISTAXMANAGER"));
				list.add(user);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public static String addUser(User user) {
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
			String curUserName = "admin";
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
}
