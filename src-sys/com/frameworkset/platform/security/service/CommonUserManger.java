package com.frameworkset.platform.security.service;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import javax.jws.WebParam;

import org.apache.log4j.Logger;

import com.frameworkset.common.poolman.ConfigSQLExecutor;
import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.platform.cms.util.StringUtil;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.security.authentication.EncrpyPwd;
import com.frameworkset.platform.security.service.entity.CommonOrganization;
import com.frameworkset.platform.security.service.entity.CommonUser;
import com.frameworkset.platform.security.service.entity.Result;
import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.platform.sysmgrcore.manager.LogManager;
import com.frameworkset.platform.sysmgrcore.manager.OrgManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.platform.sysmgrcore.manager.db.OrgManagerImpl;
import com.frameworkset.platform.sysmgrcore.purviewmanager.GenerateServiceFactory;
import com.frameworkset.platform.sysmgrcore.purviewmanager.db.UserOrgParamManager;
import com.frameworkset.platform.util.EventUtil;

public class CommonUserManger implements CommonUserManagerInf,org.frameworkset.spi.InitializingBean{
	private static Logger log = Logger.getLogger(CommonUserManger.class);
	private ConfigSQLExecutor executor ;
	private UserOrgParamManager userOrgParamManager = new UserOrgParamManager();
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
			
			executor.insert("inituserorg", orgid,user.getUser_id());
			this.userOrgParamManager.fixuserorg(user.getUser_id()+"", orgid);
			user.setUser_password(p);
			result.setCode(Result.ok);
			result.setUser(user);
			tm.commit();
			 AccessControl control = AccessControl.getAccessControl();
				String operContent="";        
		        String operSource=control.getMachinedID();
		        String openModle="用户管理";
		        String userName = control.isGuest()?"通用用户部门管理服务":control.getUserName();
		        String description="";
		        LogManager logManager = SecurityDatabase.getLogManager(); 		
				operContent=new StringBuilder().append(userName).append("创建用户：").append(user.getUser_name()).append("(").append(user.getUser_realname()).append(")").toString(); 
				 description="";
		        logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description);       
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
	public Result addOrganization(CommonOrganization org)
	{
		return addOrganizationWithEventTrigger(org,true);
	}
	
	/**
	 *  常用字段：
 * 
 * orgId,
 * orgName,
 * parentId,
 * code,
 * creatingtime,
 * orgnumber,
 * orgdesc,
 * remark5, 显示名称
 * orgTreeLevel,部门层级，自动运算
 * orgleader 部门主管
 * 如果triggerEvent为false，需要调用程序自己触发以下事件
 * if(triggerEvent)
			{
				EventUtil.sendORGUNIT_INFO_ADD(org.getOrgId());				
			}
	 * @param org
	 * @return
	 */
	public Result addOrganizationWithEventTrigger(CommonOrganization org,boolean triggerEvent) {
		Result result = new Result();
		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();
			if(existOrg(org.getOrgId()))
			{
				result.setCode(Result.fail);
				result.setErrormessage(new StringBuilder().append("机构").append(org.getOrgId()).append("已经存在.").toString());
			}
			String orgid =org.getOrgId();
			if(orgid == null )
			{
			    orgid =  OrgManagerImpl.getKeyPrimary("td_sm_organization");
			    org.setOrgId(orgid);
			    
			}
			if(org.getParentId() != null && !org.getParentId().equals("") && !org.getParentId().equals("0"))
			{
				OrgManager orgManager = SecurityDatabase.getOrgManager();
				Organization parentOrg = orgManager.getOrgById(org.getParentId());
				String orgnumber = "";
				String parentOrgNumber = parentOrg.getOrgnumber();
				 
				if(StringUtil.isEmpty(org.getOrgnumber()))
				{
					int len = GenerateServiceFactory.getOrgNumberGenerateService().getOrgNumberLen();
					boolean orgNumberHiberarchy = GenerateServiceFactory.getOrgNumberGenerateService().enableOrgNumberGenerate(); 
					if(orgNumberHiberarchy){
						orgnumber = GenerateServiceFactory.getOrgNumberGenerateService().generateOrgNumber(parentOrg);
					}else{
						orgnumber = parentOrgNumber;
					}
					org.setOrgnumber(orgnumber);
				}
				
				
			}
			else
			{
				org.setParentId("0");
			}
			
			org.setOrgTreeLevel(OrgManagerImpl.getOrgTreeLevel(org.getParentId(), org.getOrgId()));
			org.setOrgSn(executor.queryObject(int.class, "maxorgsn", org.getParentId()));
			org.setCreatingtime(new Timestamp(System.currentTimeMillis()));			 
			executor.insertBean("addOrganization", org);			 
			result.setCode(Result.ok);
			result.setOrg(org);
			tm.commit();
			if(triggerEvent)
			{
				EventUtil.sendORGUNIT_INFO_ADD(org.getOrgId());				
			}
			 AccessControl control = AccessControl.getAccessControl();
			String operContent="";        
	        String operSource=control.getMachinedID();
	        String openModle="部门管理";
	        String userName = control.isGuest()?"通用用户部门管理服务":control.getUserName();
	        String description="";
	        LogManager logManager = SecurityDatabase.getLogManager(); 		
			operContent=new StringBuilder().append(userName).append("创建部门：").append(org.getOrgName()).append("(").append(org.getOrgId()).append(")").toString(); 
			 description="";
	        logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description);       
	       
			
		} catch (Exception e) {
			result.setCode(Result.fail);
			String m = new StringBuilder().append("创建机构").append(org.getOrgName()).append("失败:").append(e.getMessage()).toString();
			result.setErrormessage(m);
			log.error(m,e);
		}
		finally
		{
			tm.release();
		}
		return result;
	}
	
	private boolean existOrg(String orgId) throws SQLException {
		
		return executor.queryObject(int.class, "existOrg", orgId) > 0;
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
			executor.insert("inituserorg", orgid,user.getUser_id());
			user.setUser_password(p);
			this.userOrgParamManager.fixuserorg(user.getUser_id()+"", orgid);
			result.setCode(Result.ok);
			result.setUser(user);
			tm.commit();
			AccessControl control = AccessControl.getAccessControl();
			String operContent="";        
	        String operSource=control.getMachinedID();
	        String openModle="用户管理";
	        String userName = control.isGuest()?"通用用户部门管理服务":control.getUserName();
	        String description="";
	        LogManager logManager = SecurityDatabase.getLogManager(); 		
			operContent=new StringBuilder().append(userName).append("创建用户：").append(user.getUser_name()).append("(").append(user.getUser_realname()).append(")").toString(); 
			 description="";
	        logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description); 
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
	public Result buildUserOrgRelation(int userid,String orgid,boolean deleteotherorgjobrelation)
	{
		return buildUserOrgRelationWithEventTrigger(userid,orgid,true,true);
	}
	@Override
	public Result buildUserOrgRelationWithEventTrigger(int userid,String orgid,boolean broadcastevent,boolean deleteotherorgjobrelation) {
		Result result = new Result();
		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();
			if(existJobReleation(userid,  orgid))
			{
				 
				result.setErrormessage(new StringBuilder().append("用户机构岗位关系[user=").append(userid).append(",org=").append(orgid).append("]已经存在.").toString());
			}
			else
			 
//			user.setUser_isvalid(2);
//			user.setUser_type(2);
			{
		 
				if(deleteotherorgjobrelation)
				{
					executor.delete("deleteotherorgjobrelation", userid);
				}
				executor.insert("inituserjoborg", userid,orgid,new Date());
			}
			
			if(existOrgReleation(userid))
			{
				executor.update("updateuserorg", orgid,userid);
				executor.update("updateuserdepart", orgid,userid);
//				result.appendErrormessage(new StringBuilder().append("用户机构关系[user=").append(userid).append(",org=").append(orgid).append("]已经存在.").toString());
			}
			else
			{
			 
				executor.insert("inituserorg", orgid,userid);
				
			}
			
			this.userOrgParamManager.fixuserorg(userid+"", orgid);
			 
			result.setCode(Result.ok);
			 
			tm.commit();
			if(broadcastevent)
				EventUtil.sendUSER_ROLE_INFO_CHANGEEvent(userid+"");
		} catch (Exception e) {
			result.setCode(Result.fail);
			String m = new StringBuilder().append("构建用户机构关系[user=").append(userid).append(",org=").append(orgid).append("]失败.").toString();
			result.setErrormessage(m);
			log.error(m,e);
		}
		finally
		{
			tm.release();
		}
		return result;
	}
	
	

	private boolean existJobReleation(int userid, String orgid) throws SQLException {
		
		return executor.queryObject(int.class, "existJobReleation", orgid,userid) > 0;
	}
	private boolean existOrgReleation(int userid) throws SQLException {
		
		return executor.queryObject(int.class, "existOrgReleation",  userid) > 0;
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
	@Override
	public Result createOnlyUser(CommonUser user) {
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
			
//			user.setUser_isvalid(2);
//			user.setUser_type(2);
			String p = user.getUser_password();
			user.setUser_password(EncrpyPwd.encodePassword(user.getUser_password()));
			user.setUser_regdate(new Date());
			executor.insertBean("createcommonuser", user);
			
			user.setUser_password(p);
//			if(orgid != null && !orgid.equals(""))
//				this.userOrgParamManager.fixuserorg(user.getUser_id()+"", orgid);
			result.setCode(Result.ok);
			result.setUser(user);
			tm.commit();
			AccessControl control = AccessControl.getAccessControl();
			String operContent="";        
	        String operSource=control.getMachinedID();
	        String openModle="用户管理";
	        String userName = control.isGuest()?"通用用户部门管理服务":control.getUserName();
	        String description="";
	        LogManager logManager = SecurityDatabase.getLogManager(); 		
			operContent=new StringBuilder().append(userName).append("创建用户：").append(user.getUser_name()).append("(").append(user.getUser_realname()).append(")").toString(); 
			 description="";
	        logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description); 
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
	public Result updateOrganization(CommonOrganization org,  boolean broadcastevent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Result invalidateOrganization(String orgid,  boolean broadcastevent) {
		// TODO Auto-generated method stub
		return null;
	}
 
	

}
