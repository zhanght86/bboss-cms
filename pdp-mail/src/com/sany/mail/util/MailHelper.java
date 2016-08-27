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
package com.sany.mail.util;

import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.DefaultApplicationContext;

import com.sany.mail.SendMail;

/**
 * @todo
 * @author tanx
 * @date 2014年6月16日
 * 
 */
public class MailHelper {
	private static SendMail sendMail;

	static {
		BaseApplicationContext context = DefaultApplicationContext.getApplicationContext("com/sany/mail/property-mail.xml");
		sendMail = context.getTBeanObject("sendMail", SendMail.class);	
	}

	public static void destroy() {
		sendMail = null;
	}

	public static SendMail getSendMail() {
		return sendMail;
	}

	public static void setSendMail(SendMail sendMail) {
		MailHelper.sendMail = sendMail;
	}

}
