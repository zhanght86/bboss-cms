package com.frameworkset.platform.security.service;

import com.frameworkset.platform.security.service.entity.CommonUser;
import com.frameworkset.platform.security.service.entity.Result;

public interface CommonUserManagerInf {
	public Result createUser(CommonUser user); 
	public Result updateUser(CommonUser user);
	public Result deleteUser(String useraccount);
	public Result deleteUserByID(int userid);
	public Result deleteUserByWorknumber(String worknumber);
	
	public Result disableUser(String useraccount);
	public Result disableUserByID(int userid);
	public Result disableUserByWorknumber(String worknumber);
	public Result updatePassword(int user_id,String password);
	public Result getUserByUserAccount(String user_account);
	public Result getUserById(int user_id);
	public Result getUserByWorknumber(String user_worknumber);
	public boolean exist(String useraccount) throws Exception;
	
	public Result openUser(String useraccount) ;

	public Result openUserByID(int userid) ;

	public Result openUserByWorknumber(String worknumber) ;
	
}
