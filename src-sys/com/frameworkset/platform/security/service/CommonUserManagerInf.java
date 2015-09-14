package com.frameworkset.platform.security.service;

import com.frameworkset.platform.security.service.entity.CommonUser;
import com.frameworkset.platform.security.service.entity.Result;

public interface CommonUserManagerInf {
	public Result createUser(CommonUser user); 
	public Result updateUser(CommonUser user);
	public Result deleteUser(String useraccount);
	public Result deleteUserByID(String userid);
	public Result deleteUserByWorknumber(String worknumber);
	
	public Result disableUser(String useraccount);
	public Result disableUserByID(String userid);
	public Result disableUserByWorknumber(String worknumber);
	
	public Result getUser(String useraccount);
	public Result getUserByID(String userid);
	public Result getUserByWorknumber(String worknumber);
	
}
