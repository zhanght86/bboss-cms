package com.sany.workflow.business.action;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.web.servlet.ModelMap;

import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.platform.sysmgrcore.purviewmanager.db.FunctionDB;
import com.frameworkset.util.StringUtil;
import com.sany.workflow.business.service.ActivitiBusinessService;
import com.sany.workflow.business.service.ChooseUserService;
import com.sany.workflow.entity.User;
import com.sany.workflow.service.ProcessException;

/**
 * 选择组织用户控制类
 * 
 * @todo
 * @author tanx
 * @date 2014年8月21日
 * 
 */
public class ChooseUserAction {

	private ChooseUserService chooseUserService;

	private ActivitiBusinessService workflowService;

	private static Logger logger = Logger.getLogger(ChooseUserAction.class);

	public @ResponseBody
	List<com.frameworkset.platform.sysmgrcore.entity.User> queryUsers(
			String orgId, String userName, long offset, int pagesize) {

		try {
			com.frameworkset.platform.sysmgrcore.entity.User user = new com.frameworkset.platform.sysmgrcore.entity.User();
			if (StringUtils.isNotBlank(orgId)) {
				user.setOrgs(orgId);
			} else if (StringUtils.isNotBlank(userName)) {
				user.setUserName("%" + userName + "%");
			}

			List<com.frameworkset.platform.sysmgrcore.entity.User> userList = chooseUserService
					.selectUsersByCondition(user, offset, pagesize);

			if (userList != null && userList.size() > 0) {
				for (com.frameworkset.platform.sysmgrcore.entity.User u : userList) {
					u.setOrgName(FunctionDB.getUserorgjobinfos(u.getUserId()));
				}
			}
			return userList;
		} catch (SQLException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	public @ResponseBody
	List<com.frameworkset.platform.sysmgrcore.entity.User> queryUsersByOrgId(
			String orgId) {

		try {
			com.frameworkset.platform.sysmgrcore.entity.User user = new com.frameworkset.platform.sysmgrcore.entity.User();
			if (StringUtils.isNotBlank(orgId)) {
				user.setOrgs(orgId);
			}

			List<com.frameworkset.platform.sysmgrcore.entity.User> userList = chooseUserService
					.selectUsersByCondition(user);

			if (userList != null && userList.size() > 0) {
				for (com.frameworkset.platform.sysmgrcore.entity.User u : userList) {
					u.setOrgName(FunctionDB.getUserorgjobinfos(u.getUserId()));
				}
			}
			return userList;
		} catch (SQLException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	public @ResponseBody
	List<com.frameworkset.platform.sysmgrcore.entity.User> queryUsersByUsernames(
			String usernames) {
		try {
			if (StringUtils.isNotEmpty(usernames)) {
				List<com.frameworkset.platform.sysmgrcore.entity.User> userList = chooseUserService
						.queryUsersByUsernames(usernames);

				if (userList != null && userList.size() > 0) {
					for (com.frameworkset.platform.sysmgrcore.entity.User u : userList) {
						u.setOrgName(FunctionDB.getUserorgjobinfos(u
								.getUserId()));
					}
				}

				return userList;
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 获取用户列表转成json
	 * 
	 * @param user
	 * @return 2014年7月7日
	 */
	public @ResponseBody
	User getUserInfo(String userName) throws Exception {
		return chooseUserService.getUserInfo(userName);
	}

	public @ResponseBody
	List<User> queryUsersToJson(User user, long offset, int pagesize)
			throws Exception {
		try {

			if (StringUtils.isNotBlank(user.getUser_name())) {
				user.setUser_name("%" + user.getUser_name() + "%");
			}

			return chooseUserService.queryUsersForPage(user, offset, pagesize);
		} catch (Exception e) {
			logger.error("选择处理人页面，查询用户数据出错", e);
			throw e;
		}
	}

	public @ResponseBody
	List<User> queryUsersAndOrgToJson(User user, long offset, int pagesize)
			throws Exception {
		try {
			if (StringUtils.isNotBlank(user.getUser_name())) {
				user.setUser_name("%" + user.getUser_name() + "%");
			}

			return chooseUserService.queryUsersAndOrgToJson(user, offset,
					pagesize);
		} catch (Exception e) {
			logger.error("选择处理人页面，抄送节点查询用户数据出错", e);
			throw e;
		}
	}

	/**
	 * 跳转到待办用户选择页面
	 * 
	 * @param processKey
	 *            流程key
	 * @param candidateName
	 *            用户id
	 * @param candidateCNName
	 *            用户中文名
	 * @param candidateOrgId
	 *            部门id
	 * @param candidateOrgName
	 *            部门中文名
	 * @param realName
	 *            用户名称+部门名称
	 * @param nodekey
	 *            节点key
	 * @param index
	 *            可选人个数
	 * @param model
	 * @return 2014年11月18日
	 */
	public String toChooseUserPage(String processKey, String candidateName,
			String candidateCNName, String candidateOrgId,
			String candidateOrgName, String realName, String nodekey,
			String index, ModelMap model) {
		TransactionManager tm = new TransactionManager();

		try {
			tm.begin();

			// 判断是否是抄送节点
			boolean iscopy = workflowService.isCopyNodeByKey("usertask"
					+ nodekey, processKey);
			model.addAttribute("iscopy", iscopy);

			// 已选列表中的用户
			if (StringUtil.isNotEmpty(candidateName)) {
				List<User> userList = new ArrayList<User>();
				String[] arrayUser = candidateName.split(",");
				String[] arrayUserName = realName.split(",");

				for (int i = 0; i < arrayUser.length; i++) {
					User user = new User();
					user.setUser_name(arrayUser[i]);
					user.setUser_realname(arrayUserName[i]);
					userList.add(user);
				}
				model.addAttribute("userList", userList);
			}

			// 已选列表中的部门
			if (StringUtil.isNotEmpty(candidateOrgId)) {
				List<User> orgList = new ArrayList<User>();
				String[] arrayOrgid = candidateOrgId.split(",");
				String[] arrayOrgName = candidateOrgName.split(",");

				for (int i = 0; i < arrayOrgid.length; i++) {
					User user = new User();
					user.setOrg_id(arrayOrgid[i]);
					user.setOrg_name(arrayOrgName[i]);
					orgList.add(user);
				}
				model.addAttribute("orgList", orgList);
			}

			model.addAttribute("usernames", candidateName);
			model.addAttribute("user_realnames", candidateCNName);
			model.addAttribute("org_id", candidateOrgId);
			model.addAttribute("org_name", candidateOrgName);
			model.addAttribute("all_names", realName);
			model.addAttribute("node_key", nodekey);
			model.addAttribute("index", index);

			tm.commit();
			return "path:chooseusers";

		} catch (Exception e) {
			throw new ProcessException(e);
		} finally {
			tm.release();
		}
	}
}
