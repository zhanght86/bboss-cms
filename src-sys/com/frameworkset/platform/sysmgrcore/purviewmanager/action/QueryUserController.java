package com.frameworkset.platform.sysmgrcore.purviewmanager.action;

import java.util.ArrayList;
import java.util.List;

import org.frameworkset.util.MoreListInfo;
import org.frameworkset.util.annotations.ResponseBody;

import com.frameworkset.platform.sysmgrcore.entity.User;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.platform.sysmgrcore.manager.UserManager;

/**
 * 用户信息查询
 * @author yinbp
 *
 */
public class QueryUserController {
	public @ResponseBody(datatype="jsonp") List<User> queryUser(String username) throws Exception
	{
		if(username == null || username.equals(""))
		{
			return new ArrayList<User>();
		}
		UserManager um = SecurityDatabase.getUserManager(); 
		return um.getUsers(username);
		
	}
	
	public @ResponseBody(datatype="jsonp") MoreListInfo queryUserMore(String username,long offset,int pagesize) throws Exception
	{
		if(username == null || username.equals(""))
		{
			return new MoreListInfo();
		}
		UserManager um = SecurityDatabase.getUserManager(); 
		return um.getMoreUsers(username, offset, pagesize);
		
	}

}
