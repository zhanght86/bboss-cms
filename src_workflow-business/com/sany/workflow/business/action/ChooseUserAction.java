package com.sany.workflow.business.action;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.frameworkset.util.annotations.ResponseBody;

import com.frameworkset.platform.sysmgrcore.entity.User;
import com.frameworkset.platform.sysmgrcore.purviewmanager.db.FunctionDB;
import com.sany.workflow.business.service.ChooseUserService;

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
	private static Logger logger = Logger.getLogger(ChooseUserAction.class);

	public @ResponseBody
	List<User> queryUsers(String orgId, String userName, long offset,
			int pagesize) {

		try {
			User user = new User();
			if (StringUtils.isNotBlank(orgId)) {
				user.setOrgs(orgId);
			} else if (StringUtils.isNotBlank(userName)) {
				user.setUserName("%" + userName + "%");
			}

			List<User> userList = chooseUserService.selectUsersByCondition(
					user, offset, pagesize);

			if (userList != null && userList.size() > 0) {
				for (User u : userList) {
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
	List<User> queryUsersByOrgId(String orgId) {

		try {
			User user = new User();
			if (StringUtils.isNotBlank(orgId)) {
				user.setOrgs(orgId);
			}

			List<User> userList = chooseUserService
					.selectUsersByCondition(user);

			if (userList != null && userList.size() > 0) {
				for (User u : userList) {
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
	List<User> queryUsersByUsernames(String usernames) {
		try {
			if (StringUtils.isNotEmpty(usernames)) {
				List<User> userList = chooseUserService.queryUsersByUsernames(usernames);
				
				if(userList!=null&&userList.size()>0){
					for(User u:userList){
						u.setOrgName(FunctionDB.getUserorgjobinfos(u.getUserId()));
					}
				}
				
				return userList;
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
		return null;
	}
}
