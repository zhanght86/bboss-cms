package com.frameworkset.platform.sysmgrcore.authenticate;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.frameworkset.spi.SPIException;
import org.frameworkset.spi.support.MessageSource;
import org.frameworkset.web.servlet.support.RequestContextUtils;
import org.frameworkset.web.servlet.support.WebApplicationContextUtils;

import com.frameworkset.orm.annotation.TransactionType;
import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.platform.ca.CaProperties;
import com.frameworkset.platform.config.ConfigManager;
import com.frameworkset.platform.security.authentication.ACLLoginModule;
import com.frameworkset.platform.security.authentication.CheckCallBack;
import com.frameworkset.platform.security.authentication.EncrpyPwd;
import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.platform.sysmgrcore.entity.User;
import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.platform.sysmgrcore.manager.OrgManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.util.StringUtil;


/**
 *
 * <p>Title: UserPasswordLoginModule</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 *
 * @author biaoping.yin
 * @version 1.0
 */

public class UserPasswordLoginModule extends ACLLoginModule
{
    private static Logger log = Logger.getLogger(UserPasswordLoginModule.class);

   /** 
    * @roseuid 43FEB6D6001F
    */
   public UserPasswordLoginModule()
   {

   }

    protected boolean check(String userName,
                            String password,
                            CheckCallBack checkCallBack) throws
            LoginException
    {
    	String fromsso = null;
    	if(super.request != null)
    	{
    		fromsso =(String)request.getAttribute("fromsso");
    	}
    	
    		
    	String password_i = password;
    	/**
    	 * 匿名用户登录系统
    	 */
    	if(userName.equals("guest___"))
    	{
    		 checkCallBack.setUserAttribute("userName","匿名用户");
             checkCallBack.setUserAttribute("userID","-1");
             checkCallBack.setUserAttribute("password",EncrpyPwd.encodePassword("123456"));
             checkCallBack.setUserAttribute("password_i","123456");
             checkCallBack.setUserAttribute("CHARGEORGID",(Organization)null);
             checkCallBack.setUserAttribute("orgName","没有兼职单位");
             checkCallBack.setUserAttribute("secondOrgs",null);
             checkCallBack.setUserAttribute("userAccount","guest___");
             return true;
    	}
    	TransactionManager tm = new TransactionManager();
        try {
        	tm.begin(TransactionType.RW_TRANSACTION);
            User user = SecurityDatabase.getUserManager(registTable).getUserByName(userName);
            MessageSource messageSource = WebApplicationContextUtils.getWebApplicationContext();
            if(user == null)
            {
                log.debug("用户名/口令有误,或者用户[" + userName + "]不存在");
                throw new LoginException(messageSource.getMessage("sany.pdp.login.user.not.exist",RequestContextUtils.getRequestContextLocal(request)));
                
            }
//            if(user.getUserIsvalid() != null && user.getUserIsvalid().intValue()==0)
//            	throw new LoginException("用户[" + userName + "]无效,请与系统管理员联系");
            //2007-05-30修改by袁勇福,由于isvalid的值改为了:0：删除，1：申请，2：开通，3：停用
            if(user.getUserIsvalid() != null && user.getUserIsvalid().intValue()!=2)
            	throw new LoginException(messageSource.getMessage("sany.pdp.login.user.invaild",RequestContextUtils.getRequestContextLocal(request)));
            if(!enableusertype(user.getUserType()))
            	throw new LoginException(messageSource.getMessage("sany.pdp.login.user.invaild",RequestContextUtils.getRequestContextLocal(request)));
            	
        	/**
        	 * 是否启用了单点登录功能,如果启用了单点登录功能，cas服务端会传给子应用用户名称，
        	 * 然后我们根据用户名称去数据库直接获取真实密码,然后用真实密码进行登录系统，所以不必要再进行加密登录
        	 */
    		boolean isCasServer = ConfigManager.getInstance().getConfigBooleanValue("isCasServer", false);
    		boolean CA_LOGIN_SERVER = CaProperties.CA_LOGIN_SERVER;
    		if(!isCasServer || !CA_LOGIN_SERVER){
    			if(fromsso != null && fromsso.equals("true"))
    			{
    				
    			}
    			else
    			{
    				password = EncrpyPwd.encodePassword(password);
    			}
    		}
            	
            if(user.getUserPassword().equals(password))
            {
            	 if(SecurityDatabase.getUserManager(registTable).isPasswordExpired(user))
            	 {
            		 throw new LoginException("PasswordExpired");
            	 }
            	 OrgManager orgManager = SecurityDatabase.getOrgManager();
                 Organization org = orgManager.getMainOrganizationOfUser(userName);
               
                 buildCallback( checkCallBack,user ,userName,password,password_i,org);

//                //获取当前登陆用户所在机构列表，不包含主机构************
//                String  orgname = orgManager.getSecondOrganizations(userName);
//                if(orgname.equals("")||orgname==null){
//                	checkCallBack.setUserAttribute("orgName","没有兼职单位");
//                }else{
//                	checkCallBack.setUserAttribute("orgName",orgname);
//                }
                
                //userManager.storeLogincount(username);
                //userManager.storeUser(user);
                return true;
            }
            
            return false;

        }
        catch(LoginException e)
        {
        	throw e;
        }
        catch (ManagerException ex) {
        	
            throw new LoginException(StringUtil.exceptionToString(ex));
        } catch (SPIException ex) {
        	
           // ex.printStackTrace();
            throw new LoginException(StringUtil.exceptionToString(ex));
            /** @todo Handle this exception */
        }
        catch(Throwable e)
        {
            log.debug("未知错误:" + e.getClass() + "," + e.getMessage());
            //e.printStackTrace();
            //throw new LoginException("未知错误:" + e.getClass() + "," + e.getMessage());
            throw new LoginException(StringUtil.exceptionToString(e));
        }
        finally
        {
        	tm.releasenolog();
        }
    }
    protected boolean check(HttpServletRequest request,String userName, String password,CheckCallBack checkCallBack) throws
    LoginException{
    	 
//    	 try {
//	             CommonInfo info = new CommonInfo(); 
//	             UimUserInfo userinfo = info.validateUIM(request);
//	             userinfo.getUser_ip();
//	             userinfo.getUser_name();
//         }
//         catch(Throwable e)
//         {
//            e.printStackTrace();
//         }

    	return check(userName,
                password,
                checkCallBack);
    }
    
    protected void buildCallback( CheckCallBack checkCallBack,User user ,String userName,String password,String password_i,Organization org) throws ManagerException
    {
//    	 OrgManager orgManager = SecurityDatabase.getOrgManager();
//         Organization org = orgManager.getMainOrganizationOfUser(userName);
        checkCallBack.setUserAttribute("userName", user.getUserRealname());
        checkCallBack.setUserAttribute("userID", user.getUserId().toString());
        checkCallBack.setUserAttribute("password", password);
        checkCallBack.setUserAttribute("password_i", password_i);
        // 获取当前登陆用户所在主机构************
       
        if (org == null || org.getOrgName() == null) {
            checkCallBack.setUserAttribute("CHARGEORGID", (Organization) null);
        } else {
            checkCallBack.setUserAttribute("CHARGEORGID", org);
        }
//        // 获取当前登陆用户所在机构列表，不包含主机构************
//        String orgname = orgManager.getSecondOrganizations(userName);
//        if (orgname == null || orgname.equals("")) {
//            checkCallBack.setUserAttribute("orgName", "没有兼职单位");
//        } else {
//            checkCallBack.setUserAttribute("orgName", orgname);
//        }
//
//        // 获取当前登陆用户所在机构列表，不包含主机构************
//        List secondOrgs = orgManager.getSecondOrganizationsOfUser(userName);
//        checkCallBack.setUserAttribute("secondOrgs", secondOrgs);

        if (user.getUserLogincount() != null) {
            user.setUserLogincount(new Integer(user.getUserLogincount().intValue() + 1));
        } else {
            user.setUserLogincount(new Integer(1));
        }
        
        // -----保存用户登陆次数
        int count = user.getUserLogincount().intValue();
        checkCallBack.setUserAttribute("logincount", count + "");
        checkCallBack.setUserAttribute("userAccount", StringUtil.replaceNull(user.getUserName(), ""));
        checkCallBack.setUserAttribute("remark1", StringUtil.replaceNull(user.getRemark1(), ""));
        checkCallBack.setUserAttribute("remark2", StringUtil.replaceNull(user.getRemark2(), ""));
        checkCallBack.setUserAttribute("remark3", StringUtil.replaceNull(user.getRemark3(), ""));
        checkCallBack.setUserAttribute("remark4", StringUtil.replaceNull(user.getRemark4(), ""));
        checkCallBack.setUserAttribute("remark5", StringUtil.replaceNull(user.getRemark5(), ""));
        checkCallBack.setUserAttribute("userAddress", StringUtil.replaceNull(user.getUserAddress(), ""));
        checkCallBack.setUserAttribute("userEmail", StringUtil.replaceNull(user.getUserEmail(), ""));
        checkCallBack.setUserAttribute("userFax", StringUtil.replaceNull(user.getUserFax(), ""));
        checkCallBack.setUserAttribute("userHometel", StringUtil.replaceNull(user.getUserHometel(), ""));
        checkCallBack.setUserAttribute("userIdcard", StringUtil.replaceNull(user.getUserIdcard(), ""));
        checkCallBack.setUserAttribute("userMobiletel1", StringUtil.replaceNull(user.getUserMobiletel1(), ""));
        checkCallBack.setUserAttribute("userMobiletel2", StringUtil.replaceNull(user.getUserMobiletel2(), ""));
        checkCallBack.setUserAttribute("userOicq", StringUtil.replaceNull(user.getUserOicq(), ""));
        checkCallBack.setUserAttribute("userPinyin", StringUtil.replaceNull(user.getUserPinyin(), ""));
        checkCallBack.setUserAttribute("userPostalcode", StringUtil.replaceNull(user.getUserPostalcode(), ""));
        checkCallBack.setUserAttribute("userSex", StringUtil.replaceNull(user.getUserSex(), ""));
        checkCallBack.setUserAttribute("userType", StringUtil.replaceNull(user.getUserType(), ""));
        checkCallBack.setUserAttribute("userWorknumber", StringUtil.replaceNull(user.getUserWorknumber(), ""));
        checkCallBack.setUserAttribute("userWorktel", StringUtil.replaceNull(user.getUserWorktel(), ""));

        String t = StringUtil.getFormatDate(user.getUserBirthday(), "yyyy-MM-dd HH:mm:ss");
        if (t != null)
            checkCallBack.setUserAttribute("userBirthday", t);
        t = StringUtil.getFormatDate(user.getUserRegdate(), "yyyy-MM-dd HH:mm:ss");
        if (t != null)
            checkCallBack.setUserAttribute("userRegdate", t);
        checkCallBack.setUserAttribute("userSn", user.getUserSn() + "");
        checkCallBack.setUserAttribute("userIsvalid", user.getUserIsvalid() + "");
        
    }
}
