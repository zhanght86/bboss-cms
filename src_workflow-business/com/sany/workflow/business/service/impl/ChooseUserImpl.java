package com.sany.workflow.business.service.impl;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.frameworkset.platform.sysmgrcore.entity.User;
import com.frameworkset.util.ListInfo;
import com.sany.workflow.business.entity.SysUser;
import com.sany.workflow.business.service.ChooseUserService;

/**
 * 选择组织用户实现类
 * 
 * @todo
 * @author tanx
 * @date 2014年8月21日
 * 
 */
public class ChooseUserImpl implements ChooseUserService,
		org.frameworkset.spi.DisposableBean {

	private com.frameworkset.common.poolman.ConfigSQLExecutor executor;

	@Override
	public void destroy() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public SysUser queryUserByCode(String code) throws SQLException {
		SysUser user = null;
		List<SysUser> list = executor.queryList(SysUser.class,
				"queryUserByCode", code);
		if (list != null && list.size() > 0) {
			user = list.get(0);
		}
		return user;
	}

	@Override
	public List<User> selectUsersByCondition(User user, long offset,
			int pagesize) throws SQLException {
		ListInfo listInfo = executor.queryListInfoBean(User.class,
				"selectUsersByCondition", offset, pagesize, user);

		return listInfo.getDatas();
	}

	@Override
	public List<User> queryUsersByUsernames(String usernames)
			throws SQLException {
		List<String> usernameslist = Arrays.asList(usernames.split(","));
		Map<String, List<String>> param = new HashMap<String, List<String>>();
		param.put("usernames", usernameslist);

		List<User> users = executor.queryListBean(User.class,
				"queryUsersByUsername", param);

		return users;

	}

	@Override
	public List<User> selectUsersByCondition(User user) throws SQLException {

		return executor.queryListBean(User.class, "selectUsersByCondition",
				user);
	}

}
