package com.sany.hrm.common.service;

/**
 * 登录及相关服务
 * 
 * @author gw_yaoht
 * @version 2012-8-11
 **/
public interface LoginService {
	/**
	 * 获取
	 * 
	 * @return 凭证
	 */
	String getWarrantId();

	/**
	 * 获取
	 * 
	 * @param userCode
	 *            用户编号
	 * @return 凭证
	 */
	String getWarrantId(String userCode);
}