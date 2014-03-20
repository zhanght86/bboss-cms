package com.sany.mbp.login.action;

import org.apache.log4j.Logger;
import org.frameworkset.web.servlet.ModelMap;

import com.sany.mbp.login.service.ClientLoginService;

/**
 * 单点登录
 * 
 * @author fudk
 * @version 2012-8-11
 **/

public class SsoController {

	private static Logger logger = Logger.getLogger(SsoController.class);
	
	private ClientLoginService clientLoginService;
	
	public ClientLoginService getClientLoginService() {
		return clientLoginService;
	}

	public void setClientLoginService(ClientLoginService clientLoginService) {
		this.clientLoginService = clientLoginService;
	}

	public String ssoLogin(String pdhsessionid, String pdid, ModelMap model) {
		pdhsessionid = "4_t40sls8liKF94TIbKdCi54YNj04hNIU6+4ADHe6lKBRqesmw";
		pdid = "ANCAAeHitX6f9lvMul9TnSz47LnSFUbOuO8SXIYZ1nkU0uHplqT8tuJCKssb7VE/6lGNbemHuiILysNbhcl1bWZ8SGIjZZWf7f6xlWc93IRj2FiIaPCWvlY76T7qP95nmvR6KNhqxZD06UD1+TWNecl+QlP6Y6CPdyIKe+eSwqPASHU9hmIURyJJLANIJT/LeLF0evvtuWQ/M72tkviafNJ8UhK+iMaHw0yVJwTrkZwu23ZvppwTHHoOWyDp8G1NedHoW6Ch9xM=";
		
		clientLoginService.httpclientLogin(pdhsessionid, pdid);
		logger.info("sso login ");
		
		return null;
	}
}
