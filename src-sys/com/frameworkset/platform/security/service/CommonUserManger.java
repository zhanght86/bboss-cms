package com.frameworkset.platform.security.service;

import org.apache.log4j.Logger;

import com.frameworkset.common.poolman.ConfigSQLExecutor;
import com.frameworkset.platform.security.service.entity.CommonUser;
import com.frameworkset.platform.security.service.entity.Result;

public class CommonUserManger implements CommonUserManagerInf{
	private static Logger log = Logger.getLogger(CommonUserManger.class);
	private ConfigSQLExecutor exector ;
	public CommonUserManger() {
		// TODO Auto-generated constructor stub
	}

	public boolean exist(String useraccount) throws Exception
	{
		int result = this.exector.queryObject(int.class, "existuser", useraccount) ;
		return result > 0;
	}
	@Override
	public Result createUser(CommonUser user) {
		Result result = new Result();
		try {
			
			if(exist(user.getUser_name()))
			{
				result.setCode(Result.fail);
				result.setErrormessage(new StringBuilder().append("用户").append(user.getUser_name()).append("已经存在.").toString());
			}
				
			exector.insertBean("createcommonuser", user);
			result.setCode(Result.ok);
			result.setData(user);
		} catch (Exception e) {
			result.setCode(Result.fail);
			String m = new StringBuilder().append("创建用户").append(user.getUser_name()).append("失败:").append(e.getMessage()).toString();
			result.setErrormessage(m);
			log.error(m,e);
		}
		return result;
	}

	@Override
	public Result updateUser(CommonUser user) {
		Result result = new Result();
		try {
			
			if(exist(user.getUser_name()))
			{
				result.setCode(Result.fail);
				result.setErrormessage(new StringBuilder().append("用户").append(user.getUser_name()).append("已经存在.").toString());
			}
				
			exector.insertBean("createcommonuser", user);
			result.setCode(Result.ok);
			result.setData(user);
		} catch (Exception e) {
			result.setCode(Result.fail);
			String m = new StringBuilder().append("创建用户").append(user.getUser_name()).append("失败:").append(e.getMessage()).toString();
			result.setErrormessage(m);
			log.error(m,e);
		}
		return result;
	}

	@Override
	public Result deleteUser(String useraccount) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result deleteUserByID(String userid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result deleteUserByWorknumber(String worknumber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result disableUser(String useraccount) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result disableUserByID(String userid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result disableUserByWorknumber(String worknumber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result getUser(String useraccount) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result getUserByID(String userid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result getUserByWorknumber(String worknumber) {
		// TODO Auto-generated method stub
		return null;
	}

}
