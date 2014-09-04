package com.sany.application.util;


import org.apache.log4j.Logger;

import com.frameworkset.platform.security.authentication.CheckCallBackWrapper;
import com.frameworkset.platform.sysmgrcore.authenticate.UserPasswordLoginModule;
import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.platform.sysmgrcore.entity.User;
import com.frameworkset.platform.sysmgrcore.exception.ManagerException;


public class TicketUserPasswordLoginModule extends UserPasswordLoginModule {
	
	private static Logger logger = Logger.getLogger(TicketUserPasswordLoginModule.class); 

	@Override
	protected void buildCallback(CheckCallBackWrapper checkCallBack, User user,
			String userName, String password, String password_i,
			Organization org) throws ManagerException {
		
		super.buildCallback(checkCallBack, user, userName, password, password_i, org);
		
		try {
			//dosomething here 业务
			
			//获取业务信息设置到会话对象中
			String ticket = AppHelper.getTicket(userName, user.getUserWorknumber());			
			checkCallBack.setUserAttribute("ticket", ticket);
			
			//系统中获取ticket对象的方法:
//			AccessControl accesscontroler = AccessControl.getAccessControl();
//			String ticket_ = accesscontroler.getUserAttribute("ticket");
//			
		} catch (Exception e) {
			logger.error(e);
		}
		
	}

//	/**
//	 * 重置用户属性
//	 * 业务中使用方法：AccessControl.getAccessControl().resetUserAttribute("hotelcode");
//	 */
//	@Override
//	
//	public void resetUserAttribute(HttpServletRequest request,
//			CheckCallBack checkCallBack, String userAttribute) {
//		String userName = (String)checkCallBack.getUserAttribute("userAccount");
//		String userAttributevalue = "";//........;//获取最新的属性值			
//		checkCallBack.setUserAttribute(userAttribute, userAttributevalue);//更新属性值
//		// TODO Auto-generated method stub
//		//super.resetUserAttribute(request, checkCallBack, userAttribute);
//	}
//
//	/**
//	 * 更新用户会话属性
//	 * 业务中调用方法：AccessControl.getAccessControl().resetUserAttributes();
//	 */
//	@Override
//	public void resetUserAttributes(HttpServletRequest request,
//			CheckCallBack checkCallBack) {
//		//根据需要更新属性值
//		try {
//			String userName = (String)checkCallBack.getUserAttribute("userAccount");
//			String userWorknumber = (String)checkCallBack.getUserAttribute("userWorknumber");
//			String ticket = AppHelper.getTicket(userName, userWorknumber);			
//			checkCallBack.setUserAttribute("ticket", ticket);
//		} catch (Exception e) {
//			logger.error(e);
//		}
//	}
}
