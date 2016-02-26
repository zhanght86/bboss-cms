package com.frameworkset.platform.sysmgrcore.purviewmanager.action;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.frameworkset.spi.SPIException;
import org.frameworkset.spi.support.MessageSource;
import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.web.servlet.support.RequestContextUtils;
import org.frameworkset.web.servlet.support.WebApplicationContextUtils;

import com.frameworkset.common.poolman.SQLExecutor;
import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.platform.config.ConfigManager;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.security.event.ACLEventType;
import com.frameworkset.platform.security.service.CommonUserManagerInf;
import com.frameworkset.platform.security.service.entity.Result;
import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.platform.sysmgrcore.entity.User;
import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.platform.sysmgrcore.manager.LogGetNameById;
import com.frameworkset.platform.sysmgrcore.manager.LogManager;
import com.frameworkset.platform.sysmgrcore.manager.OrgManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.platform.sysmgrcore.manager.UserManager;
import com.frameworkset.platform.sysmgrcore.manager.db.OrgCacheManager;
import com.frameworkset.platform.sysmgrcore.manager.db.OrgManagerImpl;
import com.frameworkset.platform.sysmgrcore.purviewmanager.GenerateServiceFactory;
import com.frameworkset.platform.sysmgrcore.purviewmanager.PurviewManager;
import com.frameworkset.platform.sysmgrcore.purviewmanager.db.PurviewManagerImpl;
import com.frameworkset.platform.sysmgrcore.web.struts.action.OrgJobAction;
import com.frameworkset.platform.util.EventUtil;
import com.frameworkset.util.StringUtil;

public class UserOrgAction {
	private CommonUserManagerInf commonUserManager; 
	public UserOrgAction() {
		// TODO Auto-generated constructor stub
	}

	public @ResponseBody Result addUser(HttpServletRequest request, HttpServletResponse response)
			throws ManagerException {
		Result result = new Result();
		AccessControl accesscontroler = AccessControl.getInstance();
		if (!accesscontroler.checkManagerAccess(request, response)) {
			result.setErrormessage("没有新增用户权限!");
			result.setCode(result.fail);
			return result;
		}

		String newUserName = "";
		String errorMessage = "";
		boolean isAutoUserName = GenerateServiceFactory.getGenerateService().enableUserNameGenerate();
		String currOrgId = request.getParameter("orgId");

		if (currOrgId == null) {
			currOrgId = (String) request.getAttribute("orgId");
		}

		// 保存用户
		User user = new User();
		if (isAutoUserName) {
			try {
				Map map = new HashMap();
				map.put("orgId", currOrgId);
				user.setUserName(GenerateServiceFactory.getGenerateService().generateUserName(map));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			user.setUserName(request.getParameter("userName"));
		}
		user.setUserWorknumber(request.getParameter("userWorknumber"));
		user.setUserPassword(request.getParameter("userPassword"));
		user.setUserRealname(request.getParameter("userRealname"));
		user.setUserSn(new Integer(request.getParameter("userSn")));
		user.setUserSex(request.getParameter("userSex"));
		user.setUserIsvalid(new Integer(request.getParameter("userIsvalid")));
		user.setUserHometel(request.getParameter("homePhone"));
		user.setUserMobiletel1(request.getParameter("mobile"));
		user.setUserPostalcode(request.getParameter("postalCode"));
		user.setRemark2(request.getParameter("shortMobile"));
		user.setUserEmail(request.getParameter("mail"));
		user.setUserMobiletel2(request.getParameter("userMobiletel2"));
		user.setRemark1(request.getParameter("remark1"));
		user.setRemark3(request.getParameter("remark3"));
		user.setRemark4(request.getParameter("remark4"));
		user.setRemark5(request.getParameter("remark5"));

		user.setUserType(request.getParameter("userType"));
		user.setUserPinyin(request.getParameter("userPinyin"));

		user.setUserWorktel(request.getParameter("userWorktel"));
		user.setUserFax(request.getParameter("userFax"));
		user.setUserOicq(request.getParameter("userOicq"));
		if (!"".equals(request.getParameter("userBirthday")))
			user.setUserBirthday(Date.valueOf(request.getParameter("userBirthday")));

		user.setUserAddress(request.getParameter("userAddress"));
		user.setUserLogincount(new Integer(request.getParameter("userLogincount")));
		user.setUserIdcard(request.getParameter("userIdcard"));
		if (!"".equals(request.getParameter("userRegdate")))
			user.setUserRegdate(Date.valueOf(request.getParameter("userRegdate")));
		UserManager userManager = SecurityDatabase.getUserManager();
		String passwordDualedTime_ = request.getParameter("passwordDualedTime");
		int passwordDualedTime = userManager.getDefaultPasswordDualTime();
		try {
			passwordDualedTime = Integer.parseInt(passwordDualedTime_);
		} catch (Exception e) {

		}
		user.setPasswordDualedTime(passwordDualedTime);

		// 吴卫雄增加：判断用户是否存在，存在则转入操作失败页面
		// 潘伟林修改， 存在则提示用户登陆名重复，返回原页面，清空登陆名

		boolean isUserExist = false;
		if (userManager.isUserExist(user)) {
			request.setAttribute("isUserExist", "true");
			request.setAttribute("reFlush", "false");

			isUserExist = true;
		}

		// System.out.println("isUserExist = " + isUserExist);
		if (isUserExist == false)// 用户名存在不保存用户
		{
			newUserName = user.getUserName();
			try {
				if (newUserName != null && !"".equals(newUserName)) {
					userManager.creatorUser(user, currOrgId, "1");
				} else {
					if (isAutoUserName) {
						errorMessage = RequestContextUtils.getI18nMessage(
								"sany.pdp.userorgmanager.user.loginname.generate.system.exception", request);
					} else {
						errorMessage = RequestContextUtils.getI18nMessage("sany.pdp.userorgmanager.user.loginname.null",
								request);
					}
					result.setErrormessage(errorMessage);
					result.setCode(result.fail);
				}
			} catch (Exception e) {
				e.printStackTrace();
				errorMessage = StringUtil.exceptionToString(e);
				result.setErrormessage(errorMessage);
				result.setCode(result.fail);
				// if(errorMessage != null && !"".equals(errorMessage)){
				// errorMessage = errorMessage.replaceAll("\\n","\\\\n");
				// errorMessage = errorMessage.replaceAll("\\r","\\\\r");
				// }
			}
		} else {

			if (isAutoUserName) {

				errorMessage = "自动生成的账号用户已经存在：" + newUserName;
				result.setErrormessage(errorMessage);
				result.setCode(result.fail);
			} else {

				errorMessage = "用户已经存在：" + newUserName;
				result.setErrormessage(errorMessage);
				result.setCode(result.fail);
			}

		}
		if (errorMessage.length() == 0) {

			if (isAutoUserName) {

				result.setErrormessage("用户创建成功，自动生成的账号为:" + newUserName);
			} else
				result.setErrormessage("用户创建成功:" + newUserName);
			result.setCode(result.ok);

		}
		return result;
	}

	public @ResponseBody Result quiteDelUser(HttpServletRequest request, HttpServletResponse response) {
		Result result = new Result();
		String errorMessage = "";
		try {
			AccessControl accesscontroler = AccessControl.getInstance();
			if (!accesscontroler.checkManagerAccess(request, response)) {
				result.setErrormessage("没有删除用户权限!");
				result.setCode(result.fail);
				return result;
			}
			UserManager userManager = SecurityDatabase.getUserManager();

			String userId = request.getParameter("checks");
			String orgId = request.getParameter("orgId");

			String[] userIds = userId.split(",");
			String delUserIds = "";

			String userNamesNo = "";

			// 日志记录start
			String curUserName = accesscontroler.getUserName();
			String operContent = "";
			String operSource = accesscontroler.getMachinedID();
			String openModle = "用户管理";

			LogManager logManager = SecurityDatabase.getLogManager();
			OrgManager orgManager = SecurityDatabase.getOrgManager();

			String orgname = orgManager.getOrgById(orgId).getRemark5();

			long startdate = System.currentTimeMillis();
			for (int i = 0; i < userIds.length; i++) {
				User user = userManager.getUserById(userIds[i]);
				String userOrgInfo = userManager.userOrgInfo(accesscontroler, userIds[i]);
				if ("".equals(userOrgInfo)) {
					operContent = curUserName + " 从机构： " + orgname + " 中彻底删除了用户: " + user.getUserName();

					logManager.log(accesscontroler.getUserAccount(), operContent, openModle, operSource, "");

					if ("".equals(delUserIds)) {
						delUserIds = userIds[i];
					} else {
						delUserIds += "," + userIds[i];
					}
				} else {
					if ("".equals(userNamesNo)) {
						userNamesNo = "以下用户删除失败:\\n" + user.getUserRealname() + ":" + userOrgInfo;
					} else {
						userNamesNo += "\\n" + user.getUserRealname() + ":" + userOrgInfo;
					}

					result.setErrormessage(userNamesNo);
					result.setCode(result.ok);
				}
			}
			long enddate = System.currentTimeMillis();
			// 日志记录end
			// System.out.println((enddate - startdate)/1000);
			String[] delUserId = delUserIds.split(",");

			boolean state = userManager.deleteBatchUser(delUserId);

			if ("".equals(userNamesNo)) {
				if (state) {
					result.setErrormessage("用户删除成功!");
					result.setCode(result.ok);
				} else {
					result.setErrormessage("用户删除失败!");
					result.setCode(result.fail);
				}
			} else {
				result.setErrormessage(userNamesNo);
				result.setCode(result.ok);
			}

		} catch (SPIException e) {
			errorMessage = StringUtil.exceptionToString(e);
			result.setErrormessage(errorMessage);
			result.setCode(result.fail);
		} catch (ManagerException e) {
			errorMessage = StringUtil.exceptionToString(e);
			result.setErrormessage(errorMessage);
			result.setCode(result.fail);
		}
		return result;
	}

	public @ResponseBody Result userorderchange(HttpServletRequest request, HttpServletResponse response) {
		Result result = new Result();
		String errorMessage = "";
		try {
			AccessControl control = AccessControl.getInstance();
			if (!control.checkManagerAccess(request, response)) {
				result.setErrormessage("没有用户排序权限!");
				result.setCode(result.fail);
				return result;
			}

			// 记录日志
			// String operContent="";
			// String operSource=control.getMachinedID();
			// String userName = control.getUserName();
			// String description="";
			// LogManager logManager = SecurityDatabase.getLogManager();
			//
			String userId3 = request.getParameter("userId");
			String orgId = request.getParameter("orgId");
			String[] userId = null;

			if (userId3 != null && userId3.length() > 0) {
				userId = userId3.split(",");
			}

			if (userId != null && userId.length > 0) {
				// String orgName_log = LogGetNameById.getOrgNameByOrgId(orgId);
				// description="";
				// logManager.log(control.getUserAccount()
				// ,operContent,"",operSource,description);

				OrgJobAction.storeOrgUserOrder(orgId, userId);

			}
			result.setCode(result.ok);
			result.setErrormessage("用户排序完毕");
		} catch (SPIException e) {
			errorMessage = StringUtil.exceptionToString(e);
			result.setErrormessage(errorMessage);
			result.setCode(result.fail);
		} catch (Exception e) {
			errorMessage = StringUtil.exceptionToString(e);
			result.setErrormessage(errorMessage);
			result.setCode(result.fail);
		}
		return result;
	}

	public @ResponseBody Result foldDisperse(HttpServletRequest request, HttpServletResponse response) {
		Result result = new Result();
		String errorMessage = "";
		try {
			AccessControl control = AccessControl.getInstance();
			if (!control.checkManagerAccess(request, response)) {
				result.setErrormessage("没有用户调动权限!");
				result.setCode(result.fail);
				return result;
			}

			UserManager userManager = SecurityDatabase.getUserManager();
			OrgManager orgManager = SecurityDatabase.getOrgManager();
			// 被调出的机构id,从离散用户调入到机构时orgIdUser为空
			String orgIdUser = request.getParameter("orgId");
			String flag = request.getParameter("flag");

			String userIds = request.getParameter("userIds");
			// 调入到的机构id
			String orgIds = request.getParameter("orgIds");
			String[] userId = userIds.split(",");
			String[] orgId = orgIds.split(",");
			boolean state = false;
			TransactionManager tm = new TransactionManager();
			try {
				tm.begin();
				if (!ConfigManager.getInstance().getConfigBooleanValue("sys.user.enablemutiorg", true)) {
					// 调入多个机构开关为false时执行下面代码
					if (orgIdUser != null && !"".equals(orgIdUser)) {
						// boolean state2 =
						// orgManager.deleteOrg_UserJob(orgIdUser,
						// userId);//删除用户主机构与岗位,部门管理员
						boolean state2 = false;
						if (ConfigManager.getInstance().getConfigBooleanValue("isdelUserRes", true)) {// 调离用户时删除用户所有资源
							orgManager.deleteAllOrg_UserJob(userId);
							state2 = userManager.deleteBatchUserRes(userId, false);
						} else {
							state2 = orgManager.deleteAllOrg_UserJob(userId);
						}
						if (state2) {
							if (!"orgunit".equals(orgIds)) {// 如果是将用户调入离散用户就不需要保存主机构与岗位关系
								state = userManager.storeBatchUserOrg(userId, orgId, true, false);// 保存用户主机构与岗位
							} else {
								state = true;
								userManager.fixuserorg(userId, orgIds);
							}
						}
					} else {
						state = userManager.storeBatchUserOrg(userId, orgId, true, false);// 保存用户主机构与岗位
					}
				} else {
					if (orgIdUser != null && !"".equals(orgIdUser)) {
						if ("0".equals(flag)) {// flag的值为1时删除当前机构的用户
							boolean state2 = orgManager.deleteOrg_UserJob(orgIdUser, userId);
							if (state2) {
								state = userManager.storeBatchUserOrg(userId, orgId, false, false);
							}
						} else {
							state = userManager.storeBatchUserOrg(userId, orgId, false, false);// 保存用户岗位
						}
					} else {
						state = userManager.storeBatchUserOrg(userId, orgId, true, false);
					}
				}

				if (orgIdUser != null && !"".equals(orgIdUser)) {
					Organization orgold = orgManager.getOrgById(orgIdUser);
					if ("orgunit".equals(orgIds)) {// 如果条件成立则是将用户调入离散用户
						for (int j = 0; j < userId.length; j++) {
							User user = userManager.getUserById(userId[j]);
							String userName = control.getUserName();
							String operContent = "";
							String operSource = control.getMachinedID();
							String openModle = RequestContextUtils.getI18nMessage("sany.pdp.groupmanage.user.manage",
									request);
							LogManager logManager = SecurityDatabase.getLogManager();
							operContent = userName + RequestContextUtils.getI18nMessage(
									"sany.pdp.userorgmanager.user.dispatch.out.org.to.free",
									new Object[] { orgold.getOrgName(), user.getUserName() }, request);
							logManager.log(control.getUserAccount(), operContent, openModle, operSource, "");
						}
					} else {
						for (int i = 0; i < orgId.length; i++) {
							Organization org = orgManager.getOrgById(orgId[i]);
							// --用户调入其他机构记录日志
							for (int j = 0; j < userId.length; j++) {
								User user = userManager.getUserById(userId[j]);
								String userName = control.getUserName();
								String operContent = "";
								String operSource = control.getMachinedID();
								String openModle = RequestContextUtils
										.getI18nMessage("sany.pdp.groupmanage.user.manage", request);
								LogManager logManager = SecurityDatabase.getLogManager();
								operContent = userName
										+ RequestContextUtils.getI18nMessage(
												"sany.pdp.userorgmanager.user.dispatch.out.org.to.org", new Object[] {
														orgold.getOrgName(), user.getUserName(), org.getOrgName() },
												request);
								logManager.log(control.getUserAccount(), operContent, openModle, operSource, "");
							}
							// -------------
						}
					}
				} else {
					for (int i = 0; i < orgId.length; i++) {
						Organization org = orgManager.getOrgById(orgId[i]);
						// --用户调入机构记录日志
						for (int j = 0; j < userId.length; j++) {
							User user = userManager.getUserById(userId[j]);
							String userName = control.getUserName();
							String operContent = "";
							String operSource = control.getMachinedID();
							String openModle = RequestContextUtils.getI18nMessage("sany.pdp.groupmanage.user.manage",
									request);
							LogManager logManager = SecurityDatabase.getLogManager();
							operContent = userName + RequestContextUtils.getI18nMessage(
									"sany.pdp.userorgmanager.user.dispatch.out.free.to.org",
									new Object[] { user.getUserName(), org.getOrgName() }, request);
							logManager.log(control.getUserAccount(), operContent, openModle, operSource, "");
						}
						// -------------
					}
				}
				tm.commit();

				EventUtil.sendUSER_INFO_DELETEEvent(userIds);

				EventUtil.sendUSER_ROLE_INFO_CHANGEEvent();
			} finally {
				tm.release();
			}
			result.setCode(result.ok);
			result.setErrormessage("用户调动完毕");
		} catch (SPIException e) {
			errorMessage = StringUtil.exceptionToString(e);
			result.setErrormessage(errorMessage);
			result.setCode(result.fail);
		} catch (Exception e) {
			errorMessage = StringUtil.exceptionToString(e);
			result.setErrormessage(errorMessage);
			result.setCode(result.fail);
		}
		return result;
	}

	public @ResponseBody Result reclaimUserRes_do(HttpServletRequest request, HttpServletResponse response) {
		Result result = new Result();
		String errorMessage = "";
		try {
			AccessControl control = AccessControl.getInstance();
			if (!control.checkManagerAccess(request, response)) {
				result.setErrormessage("没有回收操作权限!");
				result.setCode(result.fail);
				return result;
			}

			String userIds = request.getParameter("userIds") == null ? "" : request.getParameter("userIds");
			String[] userIdList = userIds.split(",");
			// public parameter
			String directRes = request.getParameter("directRes") == null ? "" : request.getParameter("directRes");
			String userRoleRes = request.getParameter("userRoleRes") == null ? "" : request.getParameter("userRoleRes");
			String userOrgJobRes = request.getParameter("userOrgJobRes") == null ? ""
					: request.getParameter("userOrgJobRes");
			String userGroupRes = request.getParameter("userGroupRes") == null ? ""
					: request.getParameter("userGroupRes");

			PurviewManager manager = new PurviewManagerImpl();
			boolean isReclaimDirectRes = "".equals(directRes) ? false : true;
			boolean isReclaimUserRoles = "".equals(userRoleRes) ? false : true;
			boolean isReclaimUserJobs = "".equals(userOrgJobRes) ? false : true;
			boolean isReclaimUserGroups = "".equals(userGroupRes) ? false : true;

			List optFailedList = manager.reclaimUsersResources(control, userIdList, isReclaimDirectRes,
					isReclaimUserRoles, isReclaimUserJobs, isReclaimUserGroups);

			String promptStr = "";
			for (int i = 0; i < optFailedList.size(); i++) {
				// 格式 admin:业务1,业务2,
				if ("".equals(promptStr)) {
					promptStr = RequestContextUtils.getI18nMessage(
							"sany.pdp.purviewmanager.rolemanager.user.resource.recycle.fail", request) + "\\n";
					promptStr += String.valueOf(optFailedList.get(i)) + "\\n";
				} else {
					promptStr += String.valueOf(optFailedList.get(i)) + "\\n";
				}
			}
			if (!"".equals(promptStr)) {

			} else {

			}

			result.setCode(result.ok);
			result.setErrormessage("回收操作完毕");
		} catch (SPIException e) {
			errorMessage = StringUtil.exceptionToString(e);
			result.setErrormessage(errorMessage);
			result.setCode(result.fail);
		} catch (Exception e) {
			errorMessage = StringUtil.exceptionToString(e);
			result.setErrormessage(errorMessage);
			result.setCode(result.fail);
		}
		return result;
	}

	public @ResponseBody Result lisanuserInfo_deletehandle(HttpServletRequest request, HttpServletResponse response) {
		Result result = new Result();
		String errorMessage = "";
		AccessControl control = AccessControl.getInstance();
		if (!control.checkManagerAccess(request, response)) {
			result.setErrormessage("没有删除离散用户权限!");
			result.setCode(result.fail);
			return result;
		}
		String msg = null;
		List allMessage = new ArrayList();

		String[] ids = null;
		TransactionManager tm = new TransactionManager();
		try {
			// action start

			// 获取要删除的用户
			ids = request.getParameterValues("checkBoxOne");
			// System.out.println(ids);
			// System.out.println(ids.length);
			UserManager userManager = SecurityDatabase.getUserManager();

			User user = null;

			tm.begin();
			List<User> uids = new ArrayList<User>();
			for (int i = 0; ids != null && i < ids.length; i++) {

				user = userManager.getUserById(ids[i]);
				if (user == null) {
					allMessage.add("用户【" + ids[i] + "】不存在,无需删除！");
					continue;
				}
				// 获取用户是否可以被删除,调用系统校验接口，验证用户是否可以被删除
				List message = PurviewManagerImpl.getBussinessCheck().userDeleteCheck(control, ids[i]);
				if (message != null && message.size() > 0) {
					allMessage.addAll(message);
					continue;
				}

				uids.add(user);
				// userManager.deleteUser(user);

				// --从机构中删除用户记录日志

				// -------------
				// 删除该机构下的用户，该用户在其他机构下存在---关联表关系（td_sm_userjoborg,td_sm_orgmanager）
			}
			ids = null;
			ids = new String[uids.size()];
			int i = 0;
			StringBuilder b = new StringBuilder();

			for (User id : uids) {
				ids[i] = id.getUserId() + "";
				if (i == 0)
					b.append(id.getUserName());
				else
					b.append(",").append(id.getUserName());
				i++;
			}
			userManager.deleteBatchUser(ids, false);
			String operContent = "";
			String operSource = control.getMachinedID();
			String openModle = "用户管理";
			LogManager logManager = SecurityDatabase.getLogManager();
			operContent = control.getUserAccount() + " 删除了离散用户: " + b.toString();
			logManager.log(control.getUserAccount(), operContent, openModle, operSource);
			tm.commit();
			if (i > 0) // 发出离散用户删除事件
			{
				ConfigManager.getEventHandle().change("删除离散用户", ACLEventType.USER_INFO_DELETE);
			}

			// action end
		} catch (Exception e) {
			e.printStackTrace();
			allMessage.add(e.getMessage());
		} finally {
			tm.release();
		}
		result.setCode(result.ok);

		if (allMessage != null) {
			if (ids != null) {
				allMessage.add("实际删除用户[" + ids.length + "]个。");
			}
			if (allMessage.size() > 0) {
				// System.out.println("allMessage.size():" + allMessage.size());

				StringBuffer str = new StringBuffer();

				msg = StringUtil.buildStringMessage(allMessage);
				result.setErrormessage(msg);

			} else {
				result.setErrormessage("删除离散用户完毕");
			}

		}
		return result;
	}

	public @ResponseBody Result modifyorg_do(HttpServletRequest request, HttpServletResponse response) {
		Result result = new Result();
		String errorMessage = "";
		AccessControl control = AccessControl.getInstance();
		if (!control.checkManagerAccess(request, response)) {
			result.setErrormessage("没有删除离散用户权限!");
			result.setCode(result.fail);
			return result;
		}
		String msg = null;
		String orgId = StringUtil.replaceNull(request.getParameter("orgId"));

		OrgManager orgManager1 = new OrgManagerImpl();
		boolean orgs = orgManager1.isCurOrgManager(orgId, control.getUserID());

		Organization org = new Organization();
		org.setOrgdesc(StringUtil.replaceNull(request.getParameter("orgdesc")));
		org.setChargeOrgId(StringUtil.replaceNull(request.getParameter("chargeOrgId")));
		org.setChildren(StringUtil.replaceNull(request.getParameter("children")));
		org.setCode(StringUtil.replaceNull(request.getParameter("code")));
		org.setCreator(StringUtil.replaceNull(request.getParameter("creator")));
		org.setIsdirectguanhu(StringUtil.replaceNull(request.getParameter("isdirectguanhu")));
		org.setIsforeignparty(StringUtil.replaceNull(request.getParameter("isforeignparty")));
		org.setIsjichaparty(StringUtil.replaceNull(request.getParameter("isjichaparty")));
		org.setIspartybussiness(StringUtil.replaceNull(request.getParameter("ispartybussiness")));
		org.setIsdirectlyparty(StringUtil.replaceNull(request.getParameter("isdirectlyparty")));
		org.setJp(StringUtil.replaceNull(request.getParameter("jp")));
		org.setLayer(StringUtil.replaceNull(request.getParameter("layer")));
		org.setOrg_level(StringUtil.replaceNull(request.getParameter("org_level")));
		org.setOrg_xzqm(StringUtil.replaceNull(request.getParameter("org_xzqm")));
		org.setOrgId(orgId);
		org.setOrgName(StringUtil.replaceNull(request.getParameter("orgName")));
		org.setSatrapJobId(StringUtil.replaceNull(request.getParameter("satrapJobId")));
		org.setRemark5(StringUtil.replaceNull(request.getParameter("remark5")));
		org.setRemark4(StringUtil.replaceNull(request.getParameter("remark4")));
		org.setRemark3(StringUtil.replaceNull(request.getParameter("remark3")));
		org.setRemark2(StringUtil.replaceNull(request.getParameter("remark2")));
		org.setRemark1(StringUtil.replaceNull(request.getParameter("remark1")));
		org.setQp(StringUtil.replaceNull(request.getParameter("qp")));
		org.setPath(StringUtil.replaceNull(request.getParameter("path")));
		org.setParentId(StringUtil.replaceNull(request.getParameter("parentId")));
		org.setOrgSn(StringUtil.replaceNull(request.getParameter("orgSn")));
		org.setOrgnumber(StringUtil.replaceNull(request.getParameter("orgnumber")));

		boolean tag = true;
		MessageSource messageSource = WebApplicationContextUtils.getWebApplicationContext();
		String notice = messageSource.getMessage("sany.pdp.modify.failed");
		OrgManager orgManager = SecurityDatabase.getOrgManager();

		TransactionManager tm = new TransactionManager(); 
		
		try {
			tm.begin();
			// 得到原来机构的编号.显示名称,名称
			String orgnumber = request.getParameter("orgNumber");
			String oldOrgName = request.getParameter("oldOrgname");
			String oldremark5 = request.getParameter("oldremark5");

			// 吴卫雄增加：为了实现与LDAP同步时将当前机构添加到父机构的 member 属性下
			if (org.getParentId() != null) {
				Organization parentOrg = orgManager.getOrgById(org.getParentId());
				org.setParentOrg(parentOrg);
			}
			// 吴卫雄结束
			
			String sql = "select count(orgnumber) from TD_SM_ORGANIZATION where orgnumber=? and orgnumber<>?";
			int exist = SQLExecutor.queryObject(int.class, sql,  org.getOrgnumber(),orgnumber);
			
			if (exist > 0) {
				tag = false;
				notice = messageSource.getMessage("sany.pdp.orgno.exist");
			}

			// 判断机构名称是否存在 全局唯一
			// baowen.liu
			
			
			exist = SQLExecutor.queryObject(int.class, "select count(org_name) from TD_SM_ORGANIZATION where org_name=? and org_name<>?", org.getOrgName(),oldOrgName);
			if (exist > 0) {
				tag = false;
				notice = messageSource.getMessage("sany.pdp.orgname.exist", request.getLocale());
			}
			// 同级机构显示名称是否相同 同级唯一
			// baowen.liu
//			db.executeSelect("select remark5 from TD_SM_ORGANIZATION where parent_id='" + org.getParentId()
//					+ "' and remark5='" + org.getRemark5() + "' and remark5 <>'" + oldremark5 + "'");
			exist = SQLExecutor.queryObject(int.class, "select count(remark5) from TD_SM_ORGANIZATION where parent_id=? and remark5=? and remark5 <>?",org.getParentId(), org.getRemark5(),oldremark5);
			if (exist > 0) {
				tag = false;
				notice = messageSource.getMessage("sany.pdp.organization.remark5.exist", request.getLocale());
			}
			boolean r = false;
			if (tag) {
				r = orgManager.storeOrg(org,false);
				
				if (!r) {
					tag = false;
				}
				org = orgManager.getOrgByName(org.getOrgName());

				// 修改机构重新加载缓冲
				// --记日志--------------------------------

				String operContent = "";
				String operSource = control.getMachinedID();
				String openModle = messageSource.getMessage("sany.pdp.organization.manage");
				String userName = control.getUserName();
				String description = "";
				LogManager logManager = SecurityDatabase.getLogManager();
				operContent = userName + messageSource.getMessage("sany.pdp.modifyed") + org.getOrgName();
				description = "";
				logManager.log(control.getUserAccount(), operContent, openModle, operSource, description);
			}
			if (tag) {
				result.setErrormessage("部门修改成功!");

				if (control.isAdmin()) {
				} else if (orgs) {
					result.setErrormessage("enable");
				}

			} else {
				result.setErrormessage("部门修改失败：" + notice);

			}
			tm.commit();
			result.setCode(result.ok);
			if(tag)
			{
				EventUtil.sendORGUNIT_INFO_UPDATE(orgId);
			}
		} catch (Exception e) {
			result.setCode(result.fail);
			result.setErrormessage(StringUtil.exceptionToString(e));
		}
		finally
		{
			tm.release();
		}
		return result;

	}
	
	public @ResponseBody Result changeOrgStatus(String orgid,String status,HttpServletRequest request, HttpServletResponse response) {
		Result result = new Result();
		 
		AccessControl control = AccessControl.getInstance();
		if (!control.checkManagerAccess(request, response)) {
			result.setErrormessage("没有修改机构状态权限!");
			result.setCode(result.fail);
			return result;
		}
		if (!control.checkPermission("orgunit",
                "orgupdate", AccessControl.ORGUNIT_RESOURCE))
		{
			result.setErrormessage("没有修改机构状态权限!");
			result.setCode(result.fail);
			return result;
		}
		if(StringUtil.isEmpty(orgid))
		{
			result.setErrormessage("必须指定部门ID!");
			result.setCode(result.fail);
			return result;
		}
		if(StringUtil.isEmpty(status))
		{
			result.setErrormessage("必须指定要修改的部门状态!");
			result.setCode(result.fail);
			return result;
		}
		 

		 
		try {
			 if(status.equals("1"))
			 {
				 result = commonUserManager.enableOrganization(orgid, true);
			 }
			 else if(status.equals("0"))
			 {
				 result = commonUserManager.disableOrganization(orgid, true);
			 }
			 else
			 {
				 result.setErrormessage("状态不正确:"+status);
					result.setCode(result.fail);
					return result;
			 }
		} catch (Exception e) {
			result.setCode(result.fail);
			result.setErrormessage(StringUtil.exceptionToString(e));
		}
		 
		return result;

	}
	
	
	public @ResponseBody Result orgtran_do(HttpServletRequest request, HttpServletResponse response) {
		Result result = new Result();
		 
		AccessControl control = AccessControl.getInstance();
		if (!control.checkManagerAccess(request, response)) {
			result.setErrormessage("没有修改机构状态权限!");
			result.setCode(result.fail);
			return result;
		}
		if (!control.checkPermission("orgunit",
                "orgupdate", AccessControl.ORGUNIT_RESOURCE))
		{
			result.setErrormessage("没有修改机构状态权限!");
			result.setCode(result.fail);
			return result;
		}
		String tag = "0";
		boolean flag=true;
		String notice = RequestContextUtils.getI18nMessage("sany.pdp.userorgmanager.org.transfer.fail.admin", request);
		
		String parentId = request.getParameter("parentId");
		String orgId = request.getParameter("orgId");
		try
			{
			Organization org = OrgCacheManager.getInstance().getOrganization(orgId);
			
			String remark5 = org==null?"":org.getRemark5();
			OrgManager orgManger = SecurityDatabase.getOrgManager();
			//机构转移时判断是否有显示名称同名的机构
			//baowen.liu
			
			int size = SQLExecutor.queryObject(int.class,"select count(remark5) from TD_SM_ORGANIZATION org where PARENT_ID=? and remark5=?",parentId,remark5);
			if( size> 0 ){
			  notice = RequestContextUtils.getI18nMessage("sany.pdp.userorgmanager.org.transfer.name.exist", request);
			  flag=false;
			}
			
			if(flag){
				
				
				flag = orgManger.tranOrg(orgId,parentId);
				
				if(flag){
					tag = "2";
					//记录日志
					//---------------START--
					
					String operContent="";        
			        String operSource=control.getMachinedID();
			        String openModle=RequestContextUtils.getI18nMessage("sany.pdp.organization.manage", request);
			        String userName = control.getUserName();
			        LogManager logManager = SecurityDatabase.getLogManager(); 
			        operContent = RequestContextUtils.getI18nMessage("sany.pdp.userorgmanager.org.transfer.to.otherorg", new Object[] {userName, LogGetNameById.getOrgNameByOrgId(orgId), LogGetNameById.getOrgNameByOrgId(parentId)}, request);
			        logManager.log(control.getUserAccount() ,operContent,openModle,operSource,""); 
					//---------------END
				}
	        
			}	
			if(tag.equals("1"))
			{
				result.setCode(result.fail);
				result.setErrormessage(RequestContextUtils.getI18nMessage("sany.pdp.userorgmanager.org.transfer.to.self.no", request));
				 
			}
			else if(tag.equals("2"))
			{
				result.setCode(result.ok);
				result.setErrormessage(RequestContextUtils.getI18nMessage("sany.pdp.userorgmanager.org.transfer.success", request));
			 
			}
			else
			{
				result.setCode(result.fail);
				result.setErrormessage(notice);
			}
		}
		catch(Exception e)
		{
			result.setCode(result.fail);
			result.setErrormessage(StringUtil.exceptionToString(e));
		}
		
		return result;

	}
	

}
