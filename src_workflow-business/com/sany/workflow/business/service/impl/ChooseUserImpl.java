package com.sany.workflow.business.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.util.ListInfo;
import com.frameworkset.util.StringUtil;
import com.sany.workflow.business.entity.SysUser;
import com.sany.workflow.business.service.ChooseUserService;
import com.sany.workflow.entity.User;

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

	/**
	 * 根据查询条件查询用户列表
	 * 
	 * @param offset
	 * @param pagesize
	 * @return
	 */
	public User getUserInfo(String userName) throws Exception {
		return executor.queryObject(User.class, "selectUsersByUserName_wf",
				userName);
	}

	@Override
	public List<User> queryUsersForPage(User user, long offset, int pagesize)
			throws Exception {
		ListInfo listInfo = executor.queryListInfoBean(User.class,
				"selectUsersByCondition", offset, pagesize, user);
		return listInfo.getDatas();
	}

	/**
	 * 根据查询条件查询用户列表和下级部门列表
	 * 
	 * @param offset
	 * @param pagesize
	 * @return
	 */
	@Override
	public List<User> queryUsersAndOrgToJson(User user, long offset,
			int pagesize) throws Exception {

		TransactionManager tm = new TransactionManager();
		try {

			tm.begin();

			List<User> userOrgList = new ArrayList<User>();

			// 用户列表
			ListInfo listInfo = executor.queryListInfoBean(User.class,
					"selectUsersByCondition", offset, pagesize, user);

			if (null != listInfo.getDatas()) {
				userOrgList.addAll(listInfo.getDatas());
			}

			// 部门列表
			if (StringUtil.isNotEmpty(user.getOrg_id())) {
				List<User> orgList = executor.queryList(User.class,
						"selectOrgsByCondition", user.getOrg_id(),
						user.getOrg_id());

				userOrgList.addAll(orgList);
			}

			tm.commit();
			return userOrgList;

		} catch (Exception e) {
			throw new Exception("查询数据列表出错:" + e);
		} finally {
			tm.release();
		}
	}

}
