package com.frameworkset.platform.ca;

import java.sql.SQLException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.frameworkset.common.poolman.PreparedDBUtil;

/**
 * <p>
 * 类说明:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2009
 * </p>
 * <p>
 * Company: bbossgroups
 * </p>
 * 
 * @author gao.tang
 * @version V1.0 创建时间：Oct 23, 2009 11:55:36 AM
 */
public class CAManager {

	/**
	 * 检验证书序列是否已经使用到了用户表中
	 * 
	 * @param request
	 * @return
	 */
	public static boolean checkCertSn(HttpServletRequest request)
			throws CaException {
		return checkCertSn(request, null);
	}

	public static boolean checkCertSn(HttpServletRequest request, String appid)
			throws CaException {
		String certsn = getCertSnCookie(request);
		PreparedDBUtil db = new PreparedDBUtil();
		try {
			if (appid != null && !"".equals(appid)) {
				db.preparedSelect("select count(1) from " + appid
						+ ".td_sm_user where CERT_SN=?");
			} else {
				db
						.preparedSelect("select count(1) from td_sm_user where CERT_SN=?");
			}
			db.setString(1, certsn);
			db.executePrepared();
			if (db.getInt(0, 0) > 0) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 将证书序列更新到用户表中
	 * 
	 * @param request
	 * @return
	 * @throws CaException
	 */
	public static boolean updateUserCERT_SN(HttpServletRequest request,
			String userName) throws CaException {
		return updateUserCERT_SN(request, userName, null);
	}

	public static boolean updateUserCERT_SN(HttpServletRequest request,
			String userName, String appid) throws CaException {
		String certsn = getCertSnCookie(request);
		PreparedDBUtil db = new PreparedDBUtil();
		try {
			if (appid != null && !"".equals(appid)) {
				db.preparedUpdate("update " + appid
						+ ".td_sm_user set cert_sn=? where user_name=?");
			} else {
				db
						.preparedUpdate("update td_sm_user set cert_sn=? where user_name=?");
			}

			db.setString(1, certsn);
			db.setString(2, userName);
			db.executePrepared();

			return true;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 将用户的证书序列置空
	 * 
	 * @param userName
	 * @return
	 * @throws CaException
	 */
	public static boolean resetUserCERT_SN(String userName) throws CaException {
		return resetUserCERT_SN(userName, null);
	}

	public static boolean resetUserCERT_SN(String userName, String appid)
			throws CaException {
		PreparedDBUtil db = new PreparedDBUtil();
		try {
			if (appid != null && !"".equals(appid)) {
				db.preparedUpdate("update " + appid
						+ ".td_sm_user set cert_sn='' where user_name=?");
			} else {
				db
						.preparedUpdate("update td_sm_user set cert_sn='' where user_name=?");
			}
			db.setString(1, userName);
			db.executePrepared();

			return true;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 获取用户名称
	 * 
	 * @param request
	 * @return
	 * @throws CaException
	 */
	public static String getUserName(HttpServletRequest request)
			throws CaException {
		return getUserName(request, null);
	}

	public static String getUserName(HttpServletRequest request, String appid)
			throws CaException {
		String certsn = getCertSnCookie(request);
		PreparedDBUtil db = new PreparedDBUtil();
		try {
			// 如果是其他应用登入，则必须判断用户是否存在该应用
			if (appid != null && !"".equals(appid)) {

				db
						.preparedSelect("select user_name from td_sm_user where CERT_SN=?");
				db.setString(1, certsn);
				db.executePrepared();
				String userName = db.getString(0, 0);
				db.preparedSelect("select user_name from " + appid
						+ ".td_sm_user where user_name=?");
				db.setString(1, userName);
				db.executePrepared();
				if (db.size() > 0) {
					return db.getString(0, 0);
				}

			} else {
				db
						.preparedSelect("select user_name from td_sm_user where CERT_SN=?");
				db.setString(1, certsn);
				db.executePrepared();
				if (db.size() > 0) {
					return db.getString(0, 0);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取用户密码
	 * 
	 * @param request
	 * @return
	 * @throws CaException
	 */
	public static String getUserPassword(HttpServletRequest request)
			throws CaException {
		return getUserPassword(request, null);
	}

	public static String getUserPassword(HttpServletRequest request,
			String appid) throws CaException {
		String certsn = getCertSnCookie(request);
		PreparedDBUtil db = new PreparedDBUtil();
		try {
			if (appid != null && !"".equals(appid)) {
				db.preparedSelect("select USER_PASSWORD from " + appid
						+ ".td_sm_user where CERT_SN=?");
			} else {
				db
						.preparedSelect("select USER_PASSWORD from td_sm_user where CERT_SN=?");
			}
			db.setString(1, certsn);
			db.executePrepared();
			if (db.size() > 0) {
				return db.getString(0, 0);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取证书序列号
	 * 
	 * @param request
	 * @return
	 * @throws CaException
	 */
	private static String getCertSnCookie(HttpServletRequest request)
			throws CaException {
		Cookie[] cookies = request.getCookies();
		if (cookies == null)
			throw new CaException("cookies is null!请使用CA认证中心配置地址访问应用");
		int size = cookies.length;
		String certsn = null;
		for (int i = 0; i < size; i++) {
			if (CookieProperties.CERT_SN.equals(cookies[i].getName())) {
				certsn = cookies[i].getValue();
				break;
			}
		}
		if (certsn == null) {
			throw new CaException("cookies exit properties ["
					+ CookieProperties.CERT_SN + "]!");
		}
		return certsn;
	}

	/**
	 * 根据cookie名称，获取CA认证中心封装在cookie中的值
	 * 
	 * @param request
	 *            请求
	 * @param cookieName
	 *            cookie名称
	 * @return
	 * @throws CaException
	 */
	public static String getCookieValue(HttpServletRequest request,
			String cookieName) throws CaException {
		Cookie[] cookies = request.getCookies();
		if (cookies == null)
			throw new CaException("cookies is null!请使用CA认证中心配置地址访问应用");
		int size = cookies.length;
		String name = null;
		for (int i = 0; i < size; i++) {
			if (cookieName.equals(cookies[i].getName())) {
				name = cookies[i].getValue();
				break;
			}
		}
		if (name == null) {
			throw new CaException("cookies exit properties ["
					+ CookieProperties.CERT_SN + "]!");
		}
		return name;
	}

	/**
	 * 检验当前uk是否被替换掉,被替换掉返回false
	 * @param request
	 * @return
	 * @throws CaException
	 */
	public static boolean isChangeUK(HttpServletRequest request)
			throws CaException {
		// 是否启用了CA认证中心
		if (CaProperties.CA_LOGIN_SERVER) {
			String cert_sn = (String) request.getSession().getAttribute(
					CookieProperties.CERT_SN);
			if (cert_sn != null) {
				try {
					if (!cert_sn.equals(CAManager.getCookieValue(request,
							CookieProperties.CERT_SN))) {
						return false;
					}
				} catch (CaException e) {
					request.getSession().invalidate();
					e.printStackTrace();
				}
			}else{
				throw new CaException("session is not attribute[" + CookieProperties.CERT_SN + "]");
			}
		}
		return true;
	}

}
