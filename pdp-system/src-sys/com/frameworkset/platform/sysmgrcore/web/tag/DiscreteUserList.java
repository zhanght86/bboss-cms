package com.frameworkset.platform.sysmgrcore.web.tag;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import com.frameworkset.common.poolman.ConfigSQLExecutor;
import com.frameworkset.common.poolman.Record;
import com.frameworkset.common.poolman.handle.RowHandler;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.platform.sysmgrcore.entity.User;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.platform.sysmgrcore.manager.UserManager;
import com.frameworkset.util.ListInfo;

/**
 * 离散用户的列表
 * select count(*) from td_sm_user  user0_ 
where user0_.user_id in ( select user1_.USER_ID from td_sm_user user1_ 
      minus select userjoborg1_.user_id from td_sm_userjoborg userjoborg1_ )
 * @author 
 * @file DiscreteUserList.java Created on: Apr 26, 2006
 */
public class DiscreteUserList extends DataInfoImpl implements Serializable {
	ConfigSQLExecutor executor_ = new ConfigSQLExecutor("com/frameworkset/platform/sysmgrcore/web/tag/userListSn.xml");
	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
			int maxPagesize) {
		
		ListInfo listInfo = null;
		TransactionManager tm = new TransactionManager(); 
		try {
			
			Map params = new HashMap();
			String userName = request.getParameter("userName");
			String userRealname = request.getParameter("userRealname");
			 
			if (userName != null && userName.length() > 0) {
				params.put("userName", "%" + userName + "%");
			}
			if (userRealname != null && userRealname.length() > 0) {
				params.put("userRealname", "%" + userRealname + "%");
			}
		 
			final UserManager userManager = SecurityDatabase.getUserManager();
//			System.out.println(hsql.toString());
			tm.begin();
			listInfo = executor_.queryListInfoBeanByRowHandler(new RowHandler<User>(){

				@Override
				public void handleRow(User user, Record dbUtil) throws Exception {
					user.setUserId(new Integer(dbUtil.getInt(  "user_id")));
					user.setUserName(dbUtil.getString( "user_name"));
					user.setUserRealname(dbUtil.getString( "user_realname"));
					user.setUserType(dbUtil.getString( "user_type"));
					user.setUserEmail(dbUtil.getString( "user_email"));
					user.setUserIdcard(dbUtil.getString("USER_IDCARD"));
					user.setUserWorknumber(dbUtil.getString("USER_WORKNUMBER"));
					user.setUserMobiletel1(dbUtil.getString("USER_MOBILETEL1"));
					user.setPasswordUpdatetime(dbUtil.getTimestamp("password_updatetime"));
					user.setPasswordDualedTime(dbUtil.getInt( "Password_DualTime"));
					user.setPasswordExpiredTime((Timestamp)userManager.getPasswordExpiredTime(user.getPasswordUpdatetime(),user.getPasswordDualedTime()));
					
				}
				
			}, User.class, "DiscreteUserList", offset, maxPagesize, params);
			tm.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			tm.release();
		}
		
//		String userName = request.getParameter("userName");
//		String userRealname = request.getParameter("userRealname");
//		try {
//			StringBuffer hsql = new StringBuffer("from User u where 1=1 ");
//			if (userName != null && userName.length() > 0) {
//				hsql.append(" and u.userName like '%" + userName + "%' ");
//			}
//			if (userRealname != null && userRealname.length() > 0) {
//				hsql
//						.append(" and u.userRealname like '%" + userRealname
//								+ "%'");
//			}
////			hsql
////					.append(" and u.userId not in (select distinct ujo.id.userId from Userjoborg ujo"
////							+ ")");
//			hsql
//			.append(" and u.userId in (select u1.userId from User u1 minus select ujo.id.userId from Userjoborg ujo"
//					+ ")");
//			UserManager userManager = SecurityDatabase.getUserManager();
//
//			List list = null;
//
//			PageConfig pageConfig = userManager.getPageConfig();
//			pageConfig.setPageSize(maxPagesize);
//			pageConfig.setStartIndex((int) offset);
//			list = userManager.getUserList(hsql.toString());
//
//			listInfo.setTotalSize(pageConfig.getTotalSize());
//
//			listInfo.setDatas(list);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		return listInfo;
	}

	protected ListInfo getDataList(String arg0, boolean arg1) {
		return null;
	}

}
