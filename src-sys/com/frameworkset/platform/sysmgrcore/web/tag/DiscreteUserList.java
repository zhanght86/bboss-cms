package com.frameworkset.platform.sysmgrcore.web.tag;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.frameworkset.platform.sysmgrcore.entity.User;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.platform.sysmgrcore.manager.UserManager;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
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

	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
			int maxPagesize) {
		ListInfo listInfo = new ListInfo();
		DBUtil dbUtil = new DBUtil();
		try {
			String userName = request.getParameter("userName");
			String userRealname = request.getParameter("userRealname");
			StringBuffer hsql = new StringBuffer("select user_id,user_name,user_realname,user_type,user_email,PASSWORD_UPDATETIME from td_sm_user  user0_ where 1=1 ");
			if (userName != null && userName.length() > 0) {
				hsql.append(" and user_name like '%" + userName + "%' ");
			}
			if (userRealname != null && userRealname.length() > 0) {
				hsql
						.append(" and user_realname like '%" + userRealname
								+ "%'");
			}
//			hsql
//					.append(" and u.userId not in (select distinct ujo.id.userId from Userjoborg ujo"
//							+ ")");
			hsql
			.append(" and user0_.user_id in (select user1_.USER_ID from td_sm_user user1_ where not exists( select user_id from td_sm_userjoborg where user_id =user1_.USER_ID)"
					+ ")");
//			System.out.println(hsql.toString());
			dbUtil.executeSelect(hsql.toString(),offset,maxPagesize);
			User user = null;
			List users = new ArrayList();
			 UserManager userManager = SecurityDatabase.getUserManager();
			for(int i = 0; i < dbUtil.size(); i ++)
			{
				user = new User();
				user.setUserId(new Integer(dbUtil.getInt(i, "user_id")));
				user.setUserName(dbUtil.getString(i, "user_name"));
				user.setUserRealname(dbUtil.getString(i, "user_realname"));
				user.setUserType(dbUtil.getString(i, "user_type"));
				user.setUserEmail(dbUtil.getString(i, "user_email"));
				user.setPasswordUpdatetime(dbUtil.getTimestamp(i,"password_updatetime"));
				user.setPasswordDualedTime(dbUtil.getInt(i, "Password_DualTime"));
				user.setPasswordExpiredTime((Timestamp)userManager.getPasswordExpiredTime(user.getPasswordUpdatetime(),user.getPasswordDualedTime()));
				
				users.add(user);
				
			}
			listInfo.setDatas(users);
			listInfo.setTotalSize(dbUtil.getTotalSize());
		} catch (Exception e) {
			e.printStackTrace();
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
