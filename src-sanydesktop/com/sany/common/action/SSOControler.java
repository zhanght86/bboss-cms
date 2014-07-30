package com.sany.common.action;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.frameworkset.util.FileCopyUtils;
import org.frameworkset.util.annotations.AssertDToken;
import org.frameworkset.web.token.TokenStore;

import com.frameworkset.platform.config.ConfigManager;
import com.frameworkset.platform.framework.Framework;
import com.frameworkset.platform.framework.Item;
import com.frameworkset.platform.framework.MenuHelper;
import com.frameworkset.platform.framework.MenuItem;
import com.frameworkset.platform.framework.Module;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.security.authorization.AccessException;
import com.frameworkset.util.StringUtil;
import com.liferay.portlet.iframe.action.SSOUserMapping;
import com.sany.webseal.LoginValidate.CommonInfo;
import com.sany.webseal.LoginValidate.UimUserInfo;

public class SSOControler {

	public String sso() {
		return "path:sso";
	}

	/**
	 * 强制要求系统必须携带令牌
	 * 
	 * @return
	 */
	@AssertDToken
	public void ssowithtoken(HttpServletRequest request,
			HttpServletResponse response) {
		// return "path:sso";
		
		String u = "", p = "", ck = "";

		String successRedirect = request.getParameter("successRedirect");
		//System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+successRedirect);
		if (!StringUtil.isEmpty(successRedirect)) {
			successRedirect = StringUtil.getRealPath(request, successRedirect,
					true);
		}
		String userName = (String)request.getAttribute(TokenStore.token_request_account_key);		
		String worknumber = (String)request.getAttribute(TokenStore.token_request_worknumber_key);
		String loginType = "1";
		if(StringUtil.isEmpty(userName ))
		{
			userName = worknumber;
			loginType = "2";
		}
		
		String loginMenu = request.getParameter("loginMenu");
		String contextpath = request.getContextPath();
		String menuid = "newGetDoc";
		if (loginMenu != null) {

			menuid = loginMenu;

		}
		HttpSession session = request.getSession();

		boolean isWebSealServer = ConfigManager.getInstance()
				.getConfigBooleanValue("isWebSealServer", false);

		if (isWebSealServer && userName == null) {

			String subsystem = "sany-mms";

			try// uim检测
			{
				CommonInfo info = new CommonInfo();
				UimUserInfo userinfo = null;
				String ip = "";
				userinfo = info.validateUIM(request);
				ip = userinfo.getUser_ip();
				userName = userinfo.getUser_name();
				AccessControl control = AccessControl.getInstance();
				control.checkAccess(request, response, false);
				String user = control.getUserAccount();
				request.setAttribute("fromsso", "true");

				if (user == null || "".equals(user) || !userName.equals(user)) {

					try {
						if (!userName.equals(user))
							control.resetSession(session);
						String password = SSOUserMapping
								.getUserPassword(userName);
						if(password == null)
							throw new AccessException("用户"+userName+"不存在。");
						control = AccessControl.getInstance();
						control.login(request, response, userName, password);

						if (StringUtil.isEmpty(successRedirect)) {
							Framework framework = Framework.getInstance(control
									.getCurrentSystemID());
							MenuItem menuitem = framework.getMenuByID(menuid);
							if (menuitem instanceof Item) {

								Item menu = (Item) menuitem;
								successRedirect = MenuHelper.getRealUrl(
										contextpath, Framework
												.getWorkspaceContent(menu,
														control),
										MenuHelper.sanymenupath_menuid, menu
												.getId());
							} else {

								Module menu = (Module) menuitem;
								String framepath = contextpath
										+ "/sanydesktop/singleframe.page?"
										+ MenuHelper.sanymenupath + "="
										+ menu.getPath();
								successRedirect = framepath;
							}
							AccessControl.recordIndexPage(request,
									successRedirect);
						} else {
							successRedirect = URLDecoder
									.decode(successRedirect);
						}
						response.sendRedirect(successRedirect);
						return;
					} catch (Exception e) {
						String msg = e.getMessage();
						if(msg == null) msg = "";
						response.sendRedirect(contextpath
								+ "/webseal/websealloginfail.jsp?userName="
								+ userName + "&ip=" + ip+ "&errormsg=" + java.net.URLEncoder.encode(msg,"UTF-8"));
						return;
					}

				} else {
					control.resetUserAttributes();
					if (StringUtil.isEmpty(successRedirect)) {
						Framework framework = Framework.getInstance(control
								.getCurrentSystemID());
						MenuItem menuitem = framework.getMenuByID(menuid);
						if (menuitem instanceof Item) {

							Item menu = (Item) menuitem;
							successRedirect = MenuHelper.getRealUrl(
									contextpath, Framework.getWorkspaceContent(
											menu, control),
									MenuHelper.sanymenupath_menuid, menu
											.getId());
						} else {

							Module menu = (Module) menuitem;
							String framepath = contextpath
									+ "/sanydesktop/singleframe.page?"
									+ MenuHelper.sanymenupath + "="
									+ menu.getPath();
							successRedirect = framepath;
						}
						AccessControl.recordIndexPage(request, successRedirect);
					} else {
						successRedirect = URLDecoder.decode(successRedirect);
					}
					response.sendRedirect(successRedirect);
					return;
				}

			} catch (Exception e)// 检测失败,继续平台登录
			{
				e.printStackTrace();
			}

		}

		else {
			try {
				AccessControl control = AccessControl.getInstance();
				control.checkAccess(request, response, false);
				String user = control.getUserAccount();
				
				worknumber = control.getUserAttribute("userWorknumber");
				boolean issameuser = false;
				if (loginType.equals("2")) {
					if (worknumber != null && !worknumber.equals(""))
						issameuser = userName.equals(worknumber);
				} else {
					if (user != null && !user.equals(""))
						issameuser = userName.equals(user);
				}

				if (user == null || "".equals(user) || !issameuser) {

					if (!issameuser) {
						control.resetSession(session);
					}

					try {
						// 1-域账号登录 2-工号登录
						String password = null;
						if (loginType.equals("1")) {

							password = SSOUserMapping.getUserPassword(userName);
							if(password == null)
								throw new AccessException("用户"+userName+"不存在。");
						} else {
							java.util.Map data = SSOUserMapping
									.getUserNameAndPasswordByWorknumber(userName);
							if(data == null)
								throw new AccessException("工号为"+userName+"的用户不存在。");
							userName = (String) data.get("USER_NAME");
							password = (String) data.get("USER_PASSWORD");
						}
						control = AccessControl.getInstance();
						request.setAttribute("fromsso", "true");
						//System.out.println("-----------userName="+userName+",password="+password);
						control.login(request, response, userName, password);
						if (StringUtil.isEmpty(successRedirect)) {
							Framework framework = Framework.getInstance(control
									.getCurrentSystemID());
							MenuItem menuitem = framework.getMenuByID(menuid);
							if (menuitem instanceof Item) {

								Item menu = (Item) menuitem;
								successRedirect = MenuHelper.getRealUrl(
										contextpath, Framework
												.getWorkspaceContent(menu,
														control),
										MenuHelper.sanymenupath_menuid, menu
												.getId());
							} else {

								Module menu = (Module) menuitem;
								String framepath = contextpath
										+ "/sanydesktop/singleframe.page?"
										+ MenuHelper.sanymenupath + "="
										+ menu.getPath();
								successRedirect = framepath;
							}
							AccessControl.recordIndexPage(request,
									successRedirect);
						} else {
							successRedirect = URLDecoder
									.decode(successRedirect);
						}
						response.sendRedirect(successRedirect);
						return;
					} catch (Exception e) {
						String msg = e.getMessage();
						if(msg == null) msg = "";
						response.sendRedirect(contextpath
								+ "/webseal/websealloginfail.jsp?userName="
								+ userName + "&errormsg=" +java.net.URLEncoder.encode(msg,"UTF-8"));
						return;
					}

				} else {
					control.resetUserAttributes();
					if (StringUtil.isEmpty(successRedirect)) {
						Framework framework = Framework.getInstance(control
								.getCurrentSystemID());
						MenuItem menuitem = framework.getMenuByID(menuid);
						if (menuitem instanceof Item) {

							Item menu = (Item) menuitem;
							successRedirect = MenuHelper.getRealUrl(
									contextpath, Framework.getWorkspaceContent(
											menu, control),
									MenuHelper.sanymenupath_menuid, menu
											.getId());
						} else {

							Module menu = (Module) menuitem;
							String framepath = contextpath
									+ "/sanydesktop/singleframe.page?"
									+ MenuHelper.sanymenupath + "="
									+ menu.getPath();
							successRedirect = framepath;
						}
						AccessControl.recordIndexPage(request, successRedirect);
					} else {
						successRedirect = URLDecoder.decode(successRedirect);
					}
					response.sendRedirect(successRedirect);
					return;
				}

			} catch (Throwable ex) {
				ex.printStackTrace();
				String errorMessage = ex.getMessage();
				if (errorMessage == null)
					errorMessage = "";
				
				
				try {
					FileCopyUtils.copy(errorMessage + ","+userName+"登陆失败，请确保输入的用户名和口令是否正确！", new OutputStreamWriter(response.getOutputStream(), "UTF-8"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}

	}

}
