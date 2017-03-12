/*
 *  Copyright 2008 bbossgroups
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.sany.application.service.impl;

import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.web.token.AppValidateResult;
import org.frameworkset.web.token.Application;
import org.frameworkset.web.token.BaseValidateApplication;
import org.frameworkset.web.token.TokenException;
import org.frameworkset.web.token.ValidateApplicationException;

import com.sany.application.entity.WfApp;
import com.sany.application.service.AppcreateService;

/**
 * <p>
 * Title: SYSValidationApplication.java
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * bboss workgroup
 * </p>
 * <p>
 * Copyright (c) 2008
 * </p>
 * 
 * @Date 2014年4月24日
 * @author biaoping.yin
 * @version 3.8.0
 */
public class SYSValidationApplication extends BaseValidateApplication {
	private AppcreateService service = null;

	@Override
	public boolean checkApp(String appid, String secret) throws TokenException {
		// try {
		// if(service == null)
		// {
		// synchronized(this)
		// {
		// if(service == null)
		// {
		// BaseApplicationContext context =
		// org.frameworkset.web.servlet.support.WebApplicationContextUtils.getWebApplicationContext();
		// if(context != null)
		// {
		// service = context.getTBeanObject("application.appcreateService",
		// AppcreateService.class);
		// }
		// }
		// }
		// }
		//
		// if(service != null)
		// {
		// return service.validateAppSecret(appid, secret);
		// }
		// else
		// return true;
		// } catch (TokenException e) {
		// throw e;
		// } catch (Exception e) {
		// throw new TokenException(TokenStore.ERROR_CODE_APPVALIDATERROR,e);
		// }
		AppValidateResult result = validateApp(appid, secret);
		if (result == null || !result.getResult())
			return false;
		else {
			return true;
		}
	}

	// @Override
	// public AppValidateResult validateApp(String appid, String secret)
	// throws TokenException {
	// try {
	// if(service == null)
	// {
	// synchronized(this)
	// {
	// if(service == null)
	// {
	// BaseApplicationContext context =
	// org.frameworkset.web.servlet.support.WebApplicationContextUtils.getWebApplicationContext();
	// if(context != null)
	// {
	// service = context.getTBeanObject("application.appcreateService",
	// AppcreateService.class);
	// }
	// }
	// }
	// }
	//
	// if(service != null)
	// {
	// return service.validateAppSecret(appid, secret);
	// }
	// else
	// return null;
	// } catch (TokenException e) {
	// throw e;
	// } catch (Exception e) {
	// throw new TokenException(TokenStore.ERROR_CODE_APPVALIDATERROR,e);
	// }
	// }
	@Override
	public Application getApplication(String appid) throws ValidateApplicationException {
		if (service == null) {
			synchronized (this) {
				if (service == null) {
					BaseApplicationContext context = org.frameworkset.web.servlet.support.WebApplicationContextUtils
							.getWebApplicationContext();
					if (context != null) {
						service = context.getTBeanObject("application.appcreateService", AppcreateService.class);
					}
				}
			}
		}

		if (service != null) {
			try {
				WfApp WfApp = service.getApplication(appid);
				Application app = new Application();
				app.setAppid(WfApp.getSystem_id());
				app.setSecret(WfApp.getSystem_secret());
				app.setTicketlivetime(WfApp.getTickettime());

				int needsign = WfApp.getNeedsign();
				app.setSign(needsign == 1);
				app.setCertAlgorithm("RSA");
				return app;
			} catch (Exception e) {
				throw new ValidateApplicationException("", e);
			}
		}
		return null;
	}

}
