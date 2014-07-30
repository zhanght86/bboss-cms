package com.sany.application.util;

import org.apache.log4j.Logger;

import com.frameworkset.platform.security.authentication.CheckCallBackWrapper;
import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.platform.sysmgrcore.entity.User;
import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.sany.ldap.LdapLoginModule;

public class TicketLDAPLoginModule extends LdapLoginModule{
	private static Logger logger = Logger.getLogger(TicketLDAPLoginModule.class); 

	@Override
	protected void buildCallback(CheckCallBackWrapper checkCallBack, User user,
			String userName, String password, String password_i,
			Organization org) throws ManagerException {
		
		super.buildCallback(checkCallBack, user, userName, password, password_i, org);
		
		try {
			
			String ticket = AppHelper.getTicket(userName, user.getUserWorknumber());
			
			checkCallBack.setUserAttribute("ticket", ticket);
			
		} catch (Exception e) {
			logger.error(e);
		}
		
	}
}
