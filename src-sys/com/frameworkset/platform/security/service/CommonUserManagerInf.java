package com.frameworkset.platform.security.service;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import com.frameworkset.platform.security.service.entity.CommonUser;
import com.frameworkset.platform.security.service.entity.Result;
@WebService(name = "commonuserService", targetNamespace = "com.frameworkset.platform.security.service.CommonuserService") 
public interface CommonUserManagerInf {
	public @WebResult(name = "result", partName = "partResult") Result createTempUser(@WebParam(name = "user", partName = "partUser") CommonUser user);
	public @WebResult(name = "result", partName = "partResult") Result createUser(@WebParam(name = "user", partName = "partUser") CommonUser user); 
	public @WebResult(name = "result", partName = "partResult") Result updateUser(@WebParam(name = "user", partName = "partUser") CommonUser user);
	public @WebResult(name = "result", partName = "partResult") Result deleteUser(@WebParam(name = "useraccount", partName = "partUseraccount") String useraccount);
	public @WebResult(name = "result", partName = "partResult") Result deleteUserByID(@WebParam(name = "userid", partName = "partUserid") int userid);
	public @WebResult(name = "result", partName = "partResult") Result deleteUserByWorknumber(@WebParam(name = "worknumber", partName = "partWorknumber") String worknumber);
	
	public @WebResult(name = "result", partName = "partResult") Result disableUser(@WebParam(name = "useraccount", partName = "partUseraccount") String useraccount);
	public @WebResult(name = "result", partName = "partResult")  Result disableUserByID(@WebParam(name = "userid", partName = "partUserid") int userid);
	public @WebResult(name = "result", partName = "partResult") Result disableUserByWorknumber(@WebParam(name = "worknumber", partName = "partWorknumber") String worknumber);
	public @WebResult(name = "result", partName = "partResult") Result updatePassword(@WebParam(name = "user_id", partName = "partUser_id") int user_id,@WebParam(name = "password", partName = "partPassword") String password);
	public @WebResult(name = "result", partName = "partResult") Result getUserByUserAccount(@WebParam(name = "user_account", partName = "partUser_account") String user_account);
	public @WebResult(name = "result", partName = "partResult") Result getUserById(@WebParam(name = "user_id", partName = "partUser_id") int user_id);
	public @WebResult(name = "result", partName = "partResult") Result getUserByWorknumber(@WebParam(name = "user_worknumber", partName = "partUser_worknumber") String user_worknumber);
	public @WebResult(name = "result", partName = "partResult") boolean exist(@WebParam(name = "useraccount", partName = "partUseraccount") String useraccount) throws Exception;
	
	public @WebResult(name = "result", partName = "partResult") Result openUser(@WebParam(name = "useraccount", partName = "partUseraccount") String useraccount) ;

	public @WebResult(name = "result", partName = "partResult") Result openUserByID(@WebParam(name = "userid", partName = "partUserid") int userid) ;

	public @WebResult(name = "result", partName = "partResult") Result openUserByWorknumber(@WebParam(name = "worknumber", partName = "partWorknumber") String worknumber) ;
	
}
