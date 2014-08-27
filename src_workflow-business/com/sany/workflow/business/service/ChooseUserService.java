package com.sany.workflow.business.service;

import java.sql.SQLException;
import java.util.List;

import com.frameworkset.platform.sysmgrcore.entity.User;
import com.sany.workflow.business.entity.SysUser;

/**
 * 选择组织用户接口
 * 
 * @todo
 * @author tanx
 * @date 2014年6月9日
 * 
 */
public interface ChooseUserService {

	public SysUser queryUserByCode(String code) throws SQLException;

	public List<User> selectUsersByCondition(User user, long offset,
			int pagesize) throws SQLException;

	public List<User> selectUsersByCondition(User user) throws SQLException;

	public List<User> queryUsersByUsernames(String usernames)
			throws SQLException;

}
