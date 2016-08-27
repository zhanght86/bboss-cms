package com.sany.mbp.login.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.frameworkset.web.servlet.ModelMap;

import com.frameworkset.platform.framework.ItemQueue;
import com.frameworkset.platform.framework.MenuHelper;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.security.authorization.AccessException;

/**
 * 登录
 * 
 * @author fudk
 * @version 2012-8-11
 **/

public class LoginController {

	private static Logger logger = Logger.getLogger(LoginController.class);

	/**
	 * 方法描述 移动平台登录
	 * 
	 * @param userName
	 *            输入的用户名
	 * @param password
	 *            输入的密码
	 * @return 映射地址
	 * @author: fudk
	 * @date: 2012-4-26 下午02:14:10
	 */
	@SuppressWarnings("unchecked")
	public String login(HttpServletRequest request, HttpServletResponse response, String userName, String password,
			ModelMap model) {
		Boolean canlogin = false; // 登录成功标示位
		String message = "";
		try {
			if (userName != null && password != null) {
				AccessControl control = AccessControl.getInstance();
				canlogin = control.login(request, response, userName, password);				
			}
		} catch (AccessException e) {
			logger.error(e);
			message = "用户名或密码错误！";
		}
		if("".equals(message)){
			model.put("message", message);
		}
		if (canlogin) {
			return "path:main";
		} else {
			return "path:login";
		}
	}

	public String index(ModelMap model) {
		AccessControl control = AccessControl.getAccessControl();
		MenuHelper menuHelper = new MenuHelper(control.getCurrentSystemID(), control);
		ItemQueue itemQueue = menuHelper.getItems();
		model.addAttribute("itemQueue", itemQueue);

		return "path:index";
	}
}
