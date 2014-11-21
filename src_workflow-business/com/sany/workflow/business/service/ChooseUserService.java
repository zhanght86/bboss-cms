package com.sany.workflow.business.service;

import java.sql.SQLException;
import java.util.List;

import com.sany.workflow.business.entity.SysUser;
import com.sany.workflow.entity.User;

/**
 * 选择组织用户接口
 * 
 * @todo
 * @author tanx
 * @date 2014年6月9日
 * 
 */
public interface ChooseUserService {

	/**
	 * 获取单个用户信息
	 * @param offset
	 * @param pagesize
	 * @return
	 */
	public User getUserInfo(String userName) throws Exception;

	/**
	 * 根据查询条件查询用户分页列表
	 * 
	 * @param offset
	 * @param pagesize
	 * @return
	 */
	public List<User> queryUsersForPage(User user, long offset, int pagesize)
			throws Exception;

	/**
	 * 根据查询条件查询用户分页列表
	 * 
	 * @param offset
	 * @param pagesize
	 * @return
	 */
	public List<User> queryUsersAndOrgToJson(User user, long offset,
			int pagesize) throws Exception;

}
