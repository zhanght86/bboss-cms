package com.sany.application.util;

import org.apache.log4j.Logger;

import com.frameworkset.platform.config.ConfigManager;
import com.frameworkset.platform.security.authentication.CheckCallBackWrapper;
import com.frameworkset.platform.security.authentication.LoginException;
import com.sany.ldap.LdapLoginModule;
import com.sany.application.util.GetUimCookies;

public class WebSealLDAPLoginModule extends LdapLoginModule{
	private static Logger logger = Logger.getLogger(TicketLDAPLoginModule.class);

	@Override
	protected boolean check(String userName, String password,
			CheckCallBackWrapper checkCallBack) throws LoginException {
		// TODO Auto-generated method stub
		boolean loginsuccess = super.check(userName, password, checkCallBack);
		
		
		if(loginsuccess && checkCallBack.getUserAttribute("userType").equals("1"))//域账号
		{
			 boolean isWebSealServer = ConfigManager.getInstance()
	    				.getConfigBooleanValue("isWebSealServer", false);
//	    	System.out.println(">>>>>>>>>>>isWebSealServer:"+isWebSealServer);		 
	    	String user_name = checkCallBack.getRequest().getHeader("iv-user");
	    	String fromsso = (String)checkCallBack.getRequest().getAttribute("fromsso");
	    	if(fromsso != null && fromsso.equals("true"))
	    	{
	    		
	    	}
	    	else
	    	{
		    	 if(isWebSealServer && user_name!= null && !user_name.equals(""))
			  	  {  
		    		 	
		    		 
			  	  }
			  	  else
			  	  {
			  		  
				  		GetUimCookies uim = new GetUimCookies();
						
						try {
							uim.getCookie(checkCallBack.getRequest(), checkCallBack.getResponse(), userName, password);
						} catch (Exception e) {
							
							throw new LoginException(e);
						}
			  	  }
	    	}
			
		}
		return loginsuccess;
	} 

	
		

}
