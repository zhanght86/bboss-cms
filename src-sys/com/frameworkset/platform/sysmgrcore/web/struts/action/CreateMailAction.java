//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_4.1.0/xslt/JavaClass.xsl

package com.frameworkset.platform.sysmgrcore.web.struts.action;

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.frameworkset.platform.config.ConfigManager;
import com.frameworkset.platform.security.authentication.EncrpyPwd;
import com.frameworkset.platform.synchronize.httpclient.ApachePostMethodClient;
import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.platform.sysmgrcore.entity.User;
import com.frameworkset.platform.sysmgrcore.entity.Userattr;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.platform.sysmgrcore.manager.UserManager;
import com.frameworkset.platform.sysmgrcore.web.struts.form.CreateMailForm;
import com.frameworkset.platform.util.CNToshell;

;
/**
 * MyEclipse Struts Creation date: 04-07-2006
 * 
 * XDoclet definition:
 * 
 * @struts.action path="/createMail" name="createMailForm" scope="request"
 *                validate="true"
 */
public class CreateMailAction extends DispatchAction implements Serializable {

	// --------------------------------------------------------- Instance
	private static Logger logger = Logger.getLogger(CreateMailAction.class);

	// --------------------------------------------------------- Methods

	private void createMailAccounts(String username, String password,
			String domain) {
		String url = "http://"
				+ ConfigManager.getInstance().getConfigValue("mailServer")
				+ "/creator_create.asp?username=" + username
				+ "&pw=" + password + "&pw1=" + password + "&domain=" + domain;
		ApachePostMethodClient client = new ApachePostMethodClient(url);
		try {
			String response = client.sendRequest();
		} catch (Exception ex) {
			System.out.println(ex.toString());
		} finally {
			client = null;
		}
	}

	public ActionForward getUserList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String orgId = request.getParameter("orgId");
		request.setAttribute("orgId", orgId);
		return (mapping.findForward("mailUserList"));
	}

	/**
	 * 根据所选择的机构（机构下的所有用户）建立邮箱帐号
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward createMailByOrg(ActionMapping mapping,
			CreateMailForm form, HttpServletRequest request,
			HttpServletResponse response) {
		CreateMailForm cmform = (CreateMailForm) form;
		try {
			UserManager userMgr = SecurityDatabase.getUserManager();

			// 根据当前机构取该机构下的所有用户
			Organization org = new Organization();
			org.setOrgId(cmform.getOrgId());
			List userList = userMgr.getUserList(org);
			if (userList != null && !userList.isEmpty()) {

				for (int i = 0; i < userList.size(); i++) {
					User user = (User) userList.get(i);
					if (user.getUserPinyin() == null
							|| user.getUserPinyin().length() == 0) {
						// user.setUserPinyin(CNToshell.getString(user.getUserRealname().substring(0,1))+"."+CNToshell.getString(user.getUserRealname().substring(1)));
						user.setUserPinyin(CNToshell.getString(user
								.getUserRealname()));
					}
					if (user.getUserEmail() == null
							|| user.getUserEmail().length() == 0) {

						// 循环处理用户ID列表
						// 通过客户端提交过来的用户ID数组中的值取用户对象实例
						List list = userMgr.getUserList("userEmail", user
								.getUserPinyin()
								+ "%", true);
						String mailAccounts = user.getUserPinyin();
						if (!list.isEmpty()) {
							// 累加邮箱帐号的计数
							mailAccounts += list.size() + 1;
						}

						user.setUserEmail(mailAccounts + "@"
								+ cmform.getMailPostfix());
						user.setUserType(cmform.getUserType());

						// 保存用户对象实例
						if (userMgr.addUser(user) != null) {
							// 生成物理邮箱帐号
							createMailAccounts(mailAccounts, user
									.getUserPassword(), cmform.getMailPostfix());
						}
						Userattr attr = new Userattr();
						attr.setUser(user);
						attr.setUserattrId(null);
						attr.setUserattrName("mail");
						attr.setUserattrValue(user.getUserEmail());
						userMgr.storeUserattr(attr);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return (mapping.findForward("success"));
	}

	/**
	 * 根据所选择选择的用户建立邮箱帐号
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward createMailByUser(ActionMapping mapping,
			CreateMailForm form, HttpServletRequest request,
			HttpServletResponse response) {
		CreateMailForm cmform = (CreateMailForm) form;
		try {
			UserManager userMgr = SecurityDatabase.getUserManager();
			String[] userIds = cmform.getUserIds();

			// 循环处理用户ID列表
			for (int i = 0; i < userIds.length; i++) {
				// 通过客户端提交过来的用户ID数组中的值取用户对象实例
				User user = userMgr.getUserById(userIds[i]);
				if (user.getUserPinyin() == null
						|| user.getUserPinyin().length() == 0) {
					user.setUserPinyin(CNToshell.getString(user
							.getUserRealname()));
					// user.setUserPinyin(CNToshell.getString(user.getUserRealname().substring(0,1))+"."+CNToshell.getString(user.getUserRealname().substring(1)));
				}
				if (user.getUserEmail() == null
						|| user.getUserEmail().length() == 0) {
					List list = userMgr.getUserList("userEmail", user
							.getUserPinyin()
							+ "%", true);
					String mailAccounts = user.getUserPinyin();
					if (!list.isEmpty()) {
						// 累加邮箱帐号的计数
						mailAccounts += list.size() + 1;
					}

					user.setUserEmail(mailAccounts + "@"
							+ cmform.getMailPostfix());
					user.setUserType(cmform.getUserType());

					// 保存用户对象实例
					if (userMgr.addUser(user) != null) {
						// 生成物理邮箱帐号
						createMailAccounts(mailAccounts,
								user.getUserPassword(), cmform.getMailPostfix());
					}
					Userattr attr = new Userattr();
					attr.setUser(user);
					attr.setUserattrId(null);
					attr.setUserattrName("mail");
					attr.setUserattrValue(user.getUserEmail());
					userMgr.storeUserattr(attr);
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return (mapping.findForward("success"));
	}

	/**
	 * 标识选中用户,景峰
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward markUser(ActionMapping mapping, CreateMailForm form,
			HttpServletRequest request, HttpServletResponse response) {
		CreateMailForm cmform = (CreateMailForm) form;
		try {
			UserManager userMgr = SecurityDatabase.getUserManager();
			String[] userIds = cmform.getUserIds();

			// 循环处理用户ID列表
			for (int i = 0; i < userIds.length; i++) {
				// 通过客户端提交过来的用户ID数组中的值取用户对象实例
				User user = userMgr.getUserById(userIds[i]);
				String pwd = EncrpyPwd.decodePassword(user.getUserPassword());
				user.setUserType(cmform.getUserType());
				user.setUserPassword(pwd);
				// 保存用户对象实例
				userMgr.addUser(user);
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return (mapping.findForward("mailUserList"));
	}

	/**
	 * 表示机构下的所有用户,景峰
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward markAllUser(ActionMapping mapping, CreateMailForm form,
			HttpServletRequest request, HttpServletResponse response) {
		CreateMailForm cmform = (CreateMailForm) form;
		try {
			UserManager userMgr = SecurityDatabase.getUserManager();
			// 根据当前机构取该机构下的所有用户
			Organization org = new Organization();
			org.setOrgId(cmform.getOrgId());
			List userList = userMgr.getUserList(org);
			if (userList != null && !userList.isEmpty()) {
				for (int i = 0; i < userList.size(); i++) {
					User user = (User) userList.get(i);
					// 循环处理用户ID列表
					// 通过客户端提交过来的用户ID数组中的值取用户对象实例
					user.setUserType(cmform.getUserType());
					String pwd = EncrpyPwd.decodePassword(user.getUserPassword());
					user.setUserPassword(pwd);
					// 保存用户对象实例
					userMgr.addUser(user);
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return (mapping.findForward("mailUserList"));
	}
}
