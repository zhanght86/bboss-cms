package com.liferay.portlet.iframe.action;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.common.poolman.SQLExecutor;
import com.frameworkset.util.ListInfo;

public class SSOUserMapping {

	/**
	 * 建立用户映射
	 * 
	 * @param a_app
	 * @param a_user
	 * @param b_app
	 * @param b_user
	 */
	public int userMapping(String a_app, String a_user, String b_app,
			String b_user, String b_password, String remark) {
		PreparedDBUtil dbutil = new PreparedDBUtil();
		
		String insertsql = "insert into  sso_user_mapping"
				+ "(A_APP_ID,A_USER_NAME,B_APP_ID,B_USER_NAME,B_USER_PASSWORD,USER_REMARK,ID) values"
				+ "(?,?,?,?,?,?,SEQ_USER_MAPPING.NEXTVAL)";
		try {
			dbutil.preparedInsert("portal", insertsql);
			dbutil.setString(1, a_app);
			dbutil.setString(2, a_user);
			dbutil.setString(3, b_app);
			dbutil.setString(4, b_user);
			dbutil.setString(5, b_password);
			dbutil.setString(6, remark);
			dbutil.executePrepared();
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		
	}

	/**
	 * 
	 * @param a_app_id
	 * @param a_userName
	 * @param b_app_id
	 * @return UserMapping
	 */
	public UserMapping getUserMapping(String a_app_id, String a_userName,
			String b_app_id) {
		PreparedDBUtil dbutil = new PreparedDBUtil();

		String selectsql = "select * from  SSO_USER_MAPPING where "
				+ "A_APP_ID=? and A_USER_NAME= ? and B_APP_ID= ?";
		try {
			dbutil.preparedSelect("portal", selectsql);
			dbutil.setString(1, a_app_id);
			dbutil.setString(2, a_userName);
			dbutil.setString(3, b_app_id);

			dbutil.executePrepared();
			UserMapping userMapping = null;
			if(dbutil.size() > 0){
				userMapping = new UserMapping();
				userMapping.setA_APP_ID(dbutil.getString(0, "A_APP_ID"));
				userMapping.setA_USER_NAME(dbutil.getString(0, "A_USER_NAME"));
				userMapping.setB_APP_ID(dbutil.getString(0, "B_APP_ID"));
				userMapping.setB_USER_NAME(dbutil.getString(0, "B_USER_NAME"));
				userMapping.setB_USER_PASSWORD(dbutil.getString(0, "B_USER_PASSWORD"));
				userMapping.setUSER_REMARK(dbutil.getString(0, "USER_REMARK"));
				userMapping.setID(dbutil.getInt(0, "ID"));
			}
//			UserMapping userMapping = (UserMapping) dbutil
//					.executePreparedForObject(UserMapping.class);
			return userMapping;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 列出所有的用户
	 * 
	 * @return UserMapping
	 */
	public ListInfo getAllMapping() {
		PreparedDBUtil dbutil = new PreparedDBUtil();
		ListInfo listInfo = new ListInfo();
		List list = new ArrayList();
		String selectsql = "select * from  SSO_USER_MAPPING ";
		try {
			dbutil.preparedSelect("portal", selectsql);
			dbutil.executePrepared();
			if(dbutil.size() > 0){
				UserMapping userMapping = null;
				for(int i = 0; i < dbutil.size(); i++){
					userMapping = new UserMapping();
					userMapping.setA_APP_ID(dbutil.getString(0, "A_APP_ID"));
					userMapping.setA_USER_NAME(dbutil.getString(0, "A_USER_NAME"));
					userMapping.setB_APP_ID(dbutil.getString(0, "B_APP_ID"));
					userMapping.setB_USER_NAME(dbutil.getString(0, "B_USER_NAME"));
					userMapping.setB_USER_PASSWORD(dbutil.getString(0, "B_USER_PASSWORD"));
					userMapping.setUSER_REMARK(dbutil.getString(0, "USER_REMARK"));
					userMapping.setID(dbutil.getInt(0, "ID"));
					list.add(userMapping);
				}
			}
//			list = dbutil.executePreparedForList(UserMapping.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		listInfo.setDatas(list);
		return listInfo;
	}


	/**
	 * 修改映射
	 * 
	 * @return UserMapping
	 */
	public boolean modifyMapping(UserMapping um) {
		PreparedDBUtil dbutil = new PreparedDBUtil();
		String selectsql = "update SSO_USER_MAPPING set A_APP_ID=?,A_USER_NAME=?,B_APP_ID=?,"
				+ "B_USER_NAME=?,B_USER_PASSWORD=?,USER_REMARK=? where ID= ? ";
		try {
			dbutil.preparedUpdate("portal", selectsql.toString());
			dbutil.setString(1, um.getA_APP_ID());
			dbutil.setString(2, um.getA_USER_NAME());
			dbutil.setString(3, um.getB_APP_ID());
			dbutil.setString(4, um.getB_USER_NAME());
			dbutil.setString(5, um.getB_USER_PASSWORD());
			dbutil.setString(6, um.getUSER_REMARK());
			dbutil.setInt(7, um.getID());
			System.out.println(selectsql.toString());
			System.out.println("id  : "+ um.getID());
			dbutil.executePrepared();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 根据ID删除记录信息
	 * 
	 * @param String
	 *            id
	 * @return boolean
	 */
	public boolean deleteUserMapping(String id) {
		StringBuffer sql = new StringBuffer(
				"delete from SSO_USER_MAPPING where ID in (" + id + ")");
		try {
			PreparedDBUtil db = new PreparedDBUtil();
			db.executeDelete("portal",sql.toString());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 根据用户名获取用户密码
	 * @param userName
	 * @return
	 */
	public static String getUserPassword(String userName){
		String password = "";
		
		try {
			password = SQLExecutor.queryObject(String.class,"select USER_PASSWORD from td_sm_user where user_name=?",userName);
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return password;
	}
	
	/**
	 * 根据用户名获取用户密码
	 * @param userName
	 * @return
	 */
	public static Map getUserNameAndPasswordByWorknumber(String worknumber){
		
		Map users = null;
		try {
			users = SQLExecutor.queryObject(HashMap.class,"select user_name,USER_PASSWORD from td_sm_user where USER_WORKNUMBER=?",worknumber);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return users;
	}
	
	/**
	 * 判断该用户是否存在该应用中
	 * @param userName
	 * @return
	 */
	public static boolean isIncludeUser(String userName){
		boolean state = false;
		DBUtil db = new DBUtil();
		try {
			db.executeSelect("select count(1) from td_sm_user where user_name='"+userName+"'");
			if(db.getInt(0, 0) > 0){
				state = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return state;
	}
	
	/**
	 * 应用支撑平台，判断用户是否存在
	 * @param userName
	 * @param subsystem
	 * @return
	 * @deprecated
	 */
	public static boolean isAppIncludeUser(String userName,String subsystem){
		boolean state = false;
		DBUtil db = new DBUtil();
		try {

			String dbuser = "";
			if (null != subsystem && !subsystem.trim().equals("")) {
				dbuser = "app_" + subsystem + ".";
			}
			db.executeSelect("select count(1) from " + dbuser
					+ "td_sm_user where user_name='" + userName + "'");
	
			//System.out.println("db.getInt(0, 0) = " + db.getInt(0, 0));
	
			if (db.getInt(0, 0) > 0) {
				state = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return state;
	}
	
	/**
	 * 应用支撑平台，获取用户密码
	 * @param userName
	 * @param subsystem
	 * @return
	 * @deprecated
	 */
	public static String getAppUserPassword(String userName,String subsystem){
		DBUtil db = new DBUtil();
		try {

			String dbuser = "";
			if (null != subsystem && !subsystem.trim().equals("")) {
				dbuser = "app_" + subsystem + ".";
			}
			db.executeSelect("select user_password from " + dbuser
					+ "td_sm_user where user_name='" + userName + "'");
			if(db.size() > 0){
				return db.getString(0, 0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
