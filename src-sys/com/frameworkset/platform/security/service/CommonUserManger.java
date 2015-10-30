package com.frameworkset.platform.security.service;

import java.util.Date;

import org.apache.log4j.Logger;

import com.frameworkset.common.poolman.ConfigSQLExecutor;
import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.platform.security.authentication.EncrpyPwd;
import com.frameworkset.platform.security.service.entity.CommonUser;
import com.frameworkset.platform.security.service.entity.Result;

public class CommonUserManger implements CommonUserManagerInf,org.frameworkset.spi.InitializingBean{
	private static Logger log = Logger.getLogger(CommonUserManger.class);
	private ConfigSQLExecutor executor ;
	public CommonUserManger() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public Result getUserById(int user_id)
	{
		Result result = new Result();
		try {			
			CommonUser user = executor.queryObject(CommonUser.class,"getuserbyuserid", user_id);
			result.setCode(Result.ok);
			result.setUser(user);
		} catch (Exception e) {
			result.setCode(Result.fail);
			String m = new StringBuilder().append("通过用户id获取用户").append(user_id).append("失败:").append(e.getMessage()).toString();
			result.setErrormessage(m);
			log.error(m,e);
		}
		return result;
	}
	@Override
	public Result getUserByWorknumber(String user_worknumber)
	{
		Result result = new Result();
		try {
			
	
			
			CommonUser user = executor.queryObject(CommonUser.class,"getuserbyworknumber", user_worknumber);
			result.setCode(Result.ok);
			result.setUser(user);
		} catch (Exception e) {
			result.setCode(Result.fail);
			String m = new StringBuilder().append("通过工号获取用户").append(user_worknumber).append("失败:").append(e.getMessage()).toString();
			result.setErrormessage(m);
			log.error(m,e);
		}
		return result;
	}
	@Override
	public Result getUserByUserAccount(String user_account)
	{
		Result result = new Result();
		try {
			
	
			
			CommonUser user = executor.queryObject(CommonUser.class,"getuserbyusername", user_account);
			result.setCode(Result.ok);
			result.setUser(user);
		} catch (Exception e) {
			result.setCode(Result.fail);
			String m = new StringBuilder().append("通过账号获取用户").append(user_account).append("失败:").append(e.getMessage()).toString();
			result.setErrormessage(m);
			log.error(m,e);
		}
		return result;
	}
	
	public boolean exist(String useraccount) throws Exception
	{
		int result = this.executor.queryObject(int.class, "existuser", useraccount) ;
		return result > 0;
	}
	@Override
	public Result createUser(CommonUser user) {
		Result result = new Result();
		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();
			if(exist(user.getUser_name()))
			{
				result.setCode(Result.fail);
				result.setErrormessage(new StringBuilder().append("用户").append(user.getUser_name()).append("已经存在.").toString());
			}
			String orgid = user.getDepart_id();
			if(orgid == null )
			{
			    orgid = "99999999";
			    user.setDepart_id(orgid);
			}
			user.setUser_isvalid(2);
			user.setUser_type(2);
			String p = user.getUser_password();
			user.setUser_password(EncrpyPwd.encodePassword(user.getUser_password()));
			user.setUser_regdate(new Date());
			executor.insertBean("createcommonuser", user);
			executor.insert("inituserjoborg", user.getUser_id(),orgid,new Date());
			user.setUser_password(p);
			result.setCode(Result.ok);
			result.setUser(user);
			tm.commit();
		} catch (Exception e) {
			result.setCode(Result.fail);
			String m = new StringBuilder().append("创建用户").append(user.getUser_name()).append("失败:").append(e.getMessage()).toString();
			result.setErrormessage(m);
			log.error(m,e);
		}
		finally
		{
			tm.release();
		}
		return result;
	}
	
	@Override
	public Result createTempUser(CommonUser user) {
		Result result = new Result();
		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();
			if(exist(user.getUser_name()))
			{
				result.setCode(Result.fail);
				result.setErrormessage(new StringBuilder().append("用户").append(user.getUser_name()).append("已经存在.").toString());
			}
			String orgid = user.getDepart_id();
			if(orgid == null )
			{
			    orgid = "99999999";
			    user.setDepart_id(orgid);
			}
//			user.setUser_isvalid(2);
//			user.setUser_type(2);
			String p = user.getUser_password();
			user.setUser_password(EncrpyPwd.encodePassword(user.getUser_password()));
			user.setUser_regdate(new Date());
			executor.insertBean("createcommonuser", user);
			executor.insert("inituserjoborg", user.getUser_id(),orgid,new Date());
			user.setUser_password(p);
			result.setCode(Result.ok);
			result.setUser(user);
			tm.commit();
		} catch (Exception e) {
			result.setCode(Result.fail);
			String m = new StringBuilder().append("创建用户").append(user.getUser_name()).append("失败:").append(e.getMessage()).toString();
			result.setErrormessage(m);
			log.error(m,e);
		}
		finally
		{
			tm.release();
		}
		return result;
	}

	@Override
	public Result updateUser(CommonUser user) {
		Result result = new Result();
		try {
			
//			if(exist(user.getUser_name()))
//			{
//				result.setCode(Result.fail);
//				result.setErrormessage(new StringBuilder().append("用户").append(user.getUser_name()).append("已经存在.").toString());
//			}
			user.setUpdate_time(new Date());	
			executor.updateBean("updatecommonuser", user);
			result.setCode(Result.ok);
			result.setUser(user);
		} catch (Exception e) {
			result.setCode(Result.fail);
			String m = new StringBuilder().append("更新用户").append(user.getUser_name()).append("失败:").append(e.getMessage()).toString();
			result.setErrormessage(m);
			log.error(m,e);
		}
		return result;
	}
	@Override
	public Result  updatePassword(int user_id,String password)
	{
		Result result = new Result();
		try {

			executor.update("updateuserpassword", EncrpyPwd.encodePassword(password),new Date(),user_id);
			result.setCode(Result.ok);
			result.setOperationData(""+user_id);
		} catch (Exception e) {
			result.setCode(Result.fail);
			String m = new StringBuilder().append("更新用户").append(user_id).append("口令失败:").append(e.getMessage()).toString();
			result.setErrormessage(m);
			log.error(m,e);
		}
		return result;
	}

	@Override
	public Result  deleteUser(String useraccount) {
		return _changeUserStatus(useraccount,0,0,"删除");
	}

	@Override
	public Result  deleteUserByID(int userid) {
		return _changeUserStatus(userid,2,0,"删除");
	}
	
	private void _upatestatus(int user_id,int status) throws Exception
	{
		this.executor.update("updateuserstatus", status,user_id);
	}

	@Override
	public Result deleteUserByWorknumber(String worknumber) {
		
		return _changeUserStatus(worknumber,1,0,"删除");
	
	}

	private Result _changeUserStatus(Object user_id,int type,int status,String message)
	{
		Result  user = null;
		TransactionManager tm = new TransactionManager();
		try
		{
			tm.begin();
			if(type == 0)
				user = this.getUserByUserAccount((String)user_id);
			else if(type == 1)
				user = this.getUserByWorknumber((String)user_id);
			else
				user = this.getUserById((Integer)user_id);
			if(user.getCode().equals(user.ok))
			{
				_upatestatus(((CommonUser)user.getUser()).getUser_id(),status);
				user = new Result();
				user.setCode(user.ok);
				user.setOperationData(new StringBuilder().append(message).append("用户").append(user_id).append("成功.").toString());				
			}
			tm.commit();
		}
		catch(Exception e)
		{
			user = new Result();
			user.setCode(Result.fail);
			String m = new StringBuilder().append(message).append("用户").append(user_id).append("失败:").append(e.getMessage()).toString();
			user.setErrormessage(m);
			log.error(m,e);
		}
		finally
		{
			tm.release();
		}
		return user;
	}
	@Override
	public Result disableUser(String useraccount) {
		return _changeUserStatus(useraccount,0,3,"通过账号禁用");
	}

	@Override
	public Result disableUserByID(int userid) {
		return _changeUserStatus(userid,2,3,"通过账号ID禁用");
	}

	@Override
	public Result disableUserByWorknumber(String worknumber) {
		return _changeUserStatus(worknumber,1,3,"通过工号禁用");
	}
	
	
	@Override
	public Result openUser(String useraccount) {
		return _changeUserStatus(useraccount,0,2,"开通");
		
	}

	@Override
	public Result openUserByID(int userid) {
		return _changeUserStatus(userid,2,2,"开通");
	}

	@Override
	public Result openUserByWorknumber(String worknumber) {
		return _changeUserStatus(worknumber,1,2,"开通");
	}
	
	

	@Override
	public void afterPropertiesSet() throws Exception {
		TransactionManager tm = new TransactionManager();
		try
		{
			tm.begin();
			int exist = this.executor.queryObject(int.class, "existcommonorg");
			if(exist <= 0)
			{
		
				executor.insert("createcommonorg", new Date());
				
				exist = this.executor.queryObject(int.class, "existcommonorgjob");
				if(exist <= 0)
					executor.insert("initcommonorgjob");
			}
			tm.commit();
		}
		catch(Exception e)
		{
			throw e;
		}
		finally
		{
			tm.release();
		}
	}

}
