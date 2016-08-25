package com.frameworkset.platform.sysmgrcore.purviewmanager;

import org.activiti.engine.impl.identity.UserInfoMap;

import com.frameworkset.platform.sysmgrcore.manager.db.UserCacheManager;

/**
 * <p>
 * Title: PDPUserInfoMapImpl.java
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: 三一集团
 * </p>
 * 
 * @Date 2013-5-28 下午3:49:39
 * @author biaoping.yin
 * @version 1.0.0
 */
public class PDPUserInfoMapImpl implements UserInfoMap {

	@Override
	public String getUserName(String userAccount) {
		String userName = (String)UserCacheManager.getInstance().getUserAttribute(userAccount, "userName");
		if(userName == null)
			return userAccount;
		else
			return userName;
		
	}
	
	public Object getUserAttribute(String userAccount,String userAttribute)
	{
		Object userName = UserCacheManager.getInstance().getUserAttribute(userAccount, userAttribute);
		return userName;
	}
	
	public Object getUserAttributeByID(String userID,String userAttribute)
	{
		Object userName = UserCacheManager.getInstance().getUserAttributeByID(userID, userAttribute);
		return userName;
	}

}
