/*
 * @(#)LdapLoginModule.java
 * 
 * Copyright @ 2001-2012 SANY Group Co.,Ltd.
 * All right reserved.
 * 
 * 这个软件是属于三一集团有限公司机密的和私有信息，不得泄露。
 * 并且只能由三一集团有限公司内部员工在得到许可的情况下才允许使用。
 * This software is the confidential and proprietary information
 * of SANY Group Co, Ltd. You shall not disclose such
 * Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with
 * SANY Group Co, Ltd.
 */
package com.sany.ldap;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.frameworkset.spi.SPIException;

import com.frameworkset.platform.ca.CaProperties;
import com.frameworkset.platform.config.ConfigManager;
import com.frameworkset.platform.security.authentication.CheckCallBack;
import com.frameworkset.platform.security.authentication.EncrpyPwd;
import com.frameworkset.platform.security.authentication.LoginException;
import com.frameworkset.platform.sysmgrcore.authenticate.UserPasswordLoginModule;
import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.platform.sysmgrcore.entity.User;
import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.platform.sysmgrcore.manager.OrgManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.platform.sysmgrcore.purviewmanager.IpControlUtil;
import com.sany.ldap.ad.AdAccountLogin;

/**
 * AD认证
 * @author caix3
 * @since 2012-03-13
 */
public class LdapLoginModule extends UserPasswordLoginModule{

    private static final long serialVersionUID = -7772723568798883076L;
    
    private Logger logger = Logger.getLogger(LdapLoginModule.class);

    /**
     * 登录认证
     */
    @SuppressWarnings("rawtypes")
    protected boolean check(String userName, String password, CheckCallBack checkCallBack) throws LoginException {

        String password_i = password;
        
        /**
         * 匿名用户登录系统
         */
        if (userName.equals("guest___")) {
            checkCallBack.setUserAttribute("userName", "匿名用户");
            checkCallBack.setUserAttribute("userID", "-1");
            checkCallBack.setUserAttribute("password", EncrpyPwd.encodePassword("123456"));
            checkCallBack.setUserAttribute("password_i", "123456");
            checkCallBack.setUserAttribute("CHARGEORGID", (Organization) null);
            checkCallBack.setUserAttribute("orgName", "没有兼职单位");
            checkCallBack.setUserAttribute("secondOrgs", null);
            checkCallBack.setUserAttribute("userAccount", "guest___");
            return true;
        }

        try {
            User user = SecurityDatabase.getUserManager(registTable).getUserByName(userName);

            if (user == null) {
                logger.debug("用户名/口令有误,或者用户[" + userName + "]不存在");
                throw new LoginException("用户名/口令有误,或者用户[" + userName + "]不存在");

            }
            
            /******验证登录用户的IP是否限制*****/
          String userip =   com.frameworkset.util.StringUtil.getClientIP(request);
          boolean ip_control = IpControlUtil.validateIp(userName,userip);
           if(!ip_control){
        	   throw new LoginException("IP访问限制，请与管理员联系");
           }
           
            if (user.getUserIsvalid() != null && user.getUserIsvalid().intValue() != 2)
                throw new LoginException("用户[" + userName + "]无效,请与系统管理员联系");
            if (!enableusertype(user.getUserType()))
                throw new LoginException("用户[" + userName + "]的类型无法登录本系统:需要的类型为[userType=" + this.userTypes
                        + "],请与系统管理员联系");
          
            if(user.getUserType().equals("1"))
            {
	            AdAccountLogin test = new AdAccountLogin();
	            Map<String, String> loginRes = test.validateUser(userName, password, null);	            
	            if (loginRes.get("successFlag").equals("0")) {
	            	 throw new LoginException(loginRes.get("errorMsg"));	            
	            } else {
		        	 OrgManager orgManager = SecurityDatabase.getOrgManager();
		             Organization org = orgManager.getMainOrganizationOfUser(userName);
	            	buildCallback( checkCallBack,user ,userName,password,password_i,org);
	                return true;
	            }
            }
            else
            {
            	 
                
                 /**
                  * 是否启用了单点登录功能,如果启用了单点登录功能，cas服务端会传给子应用用户名称，
                  * 然后我们根据用户名称去数据库直接获取真实密码,然后用真实密码进行登录系统，所以不必要再进行加密登录
                  */
                 boolean isCasServer = ConfigManager.getInstance().getConfigBooleanValue("isCasServer", false);
                 boolean CA_LOGIN_SERVER = CaProperties.CA_LOGIN_SERVER;
                 if (!isCasServer || !CA_LOGIN_SERVER) {
                     password = EncrpyPwd.encodePassword(password);
                 }
                 if(user.getUserPassword().equals(password))
                 {
                	 OrgManager orgManager = SecurityDatabase.getOrgManager();
                     Organization org = orgManager.getMainOrganizationOfUser(userName);
                	 buildCallback( checkCallBack,user ,userName,password,password_i,org);
                	 return true;
                 }
                 else
                 {
                	 return false;
                 }
                
            }

        } catch (ManagerException ex) {
           
            throw new LoginException(ex.getMessage());
        } catch (SPIException ex) {
          
            throw new LoginException(ex.getMessage());
        }catch (Exception e) {
           
            logger.debug("未知错误:" + e.getClass() + "," + e.getMessage());
            throw new LoginException(e.getMessage());
        }
    }
    
  

    protected boolean check(HttpServletRequest request, String userName, String password, CheckCallBack checkCallBack)
            throws LoginException {

    	 boolean isWebSealServer = ConfigManager.getInstance()
    				.getConfigBooleanValue("isWebSealServer", false);
//    	System.out.println(">>>>>>>>>>>isWebSealServer:"+isWebSealServer);		 
    	String user_name = request.getHeader("iv-user");
    	String fromsso = (String)request.getAttribute("fromsso");
    	if(fromsso != null && fromsso.equals("true"))
    	{
    		return super.check(userName, password, checkCallBack);
    	}
    	else
    	{
	    	 if(isWebSealServer && user_name!= null && !user_name.equals(""))
		  	  {  
	    		 	
	    		 	return super.check(userName, password, checkCallBack);
		  	  }
		  	  else
		  	  {
		  		  	return check(userName, password, checkCallBack);
		  	  }
    	}
    	
    }
   
    
   
}

