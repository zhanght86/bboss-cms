package com.frameworkset.platform.security.service;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.frameworkset.spi.support.MessageSource;
import org.frameworkset.web.servlet.support.WebApplicationContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.frameworkset.common.poolman.ConfigSQLExecutor;
import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.common.poolman.SQLExecutor;
import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.platform.config.ConfigManager;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.security.authentication.EncrpyPwd;
import com.frameworkset.platform.security.service.entity.CommonOrganization;
import com.frameworkset.platform.security.service.entity.CommonUser;
import com.frameworkset.platform.security.service.entity.Result;
import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.platform.sysmgrcore.manager.LogGetNameById;
import com.frameworkset.platform.sysmgrcore.manager.LogManager;
import com.frameworkset.platform.sysmgrcore.manager.OrgManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.platform.sysmgrcore.manager.UserManager;
import com.frameworkset.platform.sysmgrcore.manager.db.OrgManagerImpl;
import com.frameworkset.platform.sysmgrcore.purviewmanager.GenerateServiceFactory;
import com.frameworkset.platform.sysmgrcore.purviewmanager.db.UserOrgParamManager;
import com.frameworkset.platform.util.EventUtil;
import com.frameworkset.util.StringUtil;

public class CommonUserManger implements CommonUserManagerInf,org.frameworkset.spi.InitializingBean{
	private static Logger log = LoggerFactory.getLogger(CommonUserManger.class);
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
		Result result = new Result();
		if(StringUtil.isEmpty(org.getOrgId()))
		{
			

			result.setCode(result.fail);
			result.setErrormessage("部门ID为空");
			return result;
		}


		boolean tag = true;
		MessageSource messageSource = WebApplicationContextUtils.getWebApplicationContext();
		String notice = messageSource.getMessage("sany.pdp.modify.failed");
		 
		TransactionManager tm = new TransactionManager(); 
		
		try {
			tm.begin();
			// 得到原来机构的编号.显示名称,名称
			String orgnumber = org.getOrgnumber();
			 

		 
			// 吴卫雄结束
			
			String sql = "select count(orgnumber) from TD_SM_ORGANIZATION where orgnumber=? and orgnumber<>?";
			int exist = SQLExecutor.queryObject(int.class, sql,  org.getOrgnumber(),orgnumber);
			
			if (exist > 0) {
				tag = false;
				notice = messageSource.getMessage("sany.pdp.orgno.exist");
			}
 
			
 
		 
			if (tag) {
				executor.updateBean("updateOrganization", org);
				// 修改机构重新加载缓冲
				// --记日志--------------------------------
				AccessControl control = AccessControl.getAccessControl();
				String operContent = "";
				String operSource = control.getMachinedID();
				String openModle ="部门管理";
				String userName = control.getUserName();
				String description = "";
				LogManager logManager = SecurityDatabase.getLogManager();
				operContent = userName + "修改"+ org.getOrgName();
				description = "";
				logManager.log(control.getUserAccount(), operContent, openModle, operSource, description);
			}
			if (tag) {
				result.setErrormessage("部门修改成功!");

			 

			} else {
				result.setErrormessage("部门修改失败：" + notice);

			}
			tm.commit();
			result.setCode(result.ok);
			if(tag)
			{
				EventUtil.sendORGUNIT_INFO_UPDATE(org.getOrgId());
			}
		} catch (Exception e) {
			result.setCode(result.fail);
			result.setErrormessage(StringUtil.exceptionToString(e));
		}
		finally
		{
			tm.release();
		}
		return result;
	}
	@Override
	public Result disableOrganization(String orgid,  boolean broadcastevent) {
		Result result = new Result();
		if(StringUtil.isEmpty(orgid))
		{
			

			result.setCode(result.fail);
			result.setErrormessage("部门ID为空");
			return result;
		}

		try {
			_updateOrganizationStatus(orgid, "0");
			result.setCode(result.ok);
			result.setErrormessage("禁用部门成功！");
			if(broadcastevent)
			{
				EventUtil.sendORGUNIT_INFO_UPDATE(orgid);
			}
		} catch (Exception e) {
			result.setCode(result.fail);
			result.setErrormessage(StringUtil.exceptionToString(e));
		} 
		return result;
	}
	
	@Override
	public Result enableOrganization(String orgid,  boolean broadcastevent) {
		Result result = new Result();
		if(StringUtil.isEmpty(orgid))
		{
			

			result.setCode(result.fail);
			result.setErrormessage("部门ID为空");
			return result;
		}

		try {
			_updateOrganizationStatus(orgid, "1");
			result.setCode(result.ok);
			result.setErrormessage("启用部门成功！");
			if(broadcastevent)
			{
				EventUtil.sendORGUNIT_INFO_UPDATE(orgid);
			}
		} catch (Exception e) {
			result.setCode(result.fail);
			result.setErrormessage(StringUtil.exceptionToString(e));
		} 
		return result;
	}
	
	private void _updateOrganizationStatus(String orgid, String status ) throws Exception
	{
		
		executor.update("updateOrganizationStatus", status,orgid);
		
	}
	@Override
	public Result deleteOrganization(String orgid, boolean triggerEvent) {
		 
		Result result = new Result();
		if(StringUtil.isEmpty(orgid))
		{
			

			result.setCode(result.fail);
			result.setErrormessage("部门ID为空");
			return result;
		}

		OrgManager orgManager = SecurityDatabase.getOrgManager();
		
		 
		//Organization org = orgManager.getOrgById(orgId);

		//String orgId = org.getOrgId();
		//String parentId = org.getParentId();
		//request.setAttribute("orgId", orgId);
		//request.setAttribute("parentId", parentId);
		AccessControl control = AccessControl.getAccessControl(); 
		//--记日志-----
		String operContent = "";
		String operSource = control.getMachinedID();
		String openModle = "部门管理";
		String userName = control.getUserName();
		String description = "";
		LogManager logManager = SecurityDatabase.getLogManager();
		operContent = userName + "删除"
				+ LogGetNameById.getOrgNameByOrgId(orgid);
		description = "";
		//--------

		
		
		//获取当前机构下的所有用户的ID
		//String  sql = " select distinct b.USER_ID from TD_SM_USERJOBORG b where b.ORG_ID in ( "
	//	+ "select distinct a.ORG_ID from TD_SM_ORGANIZATION a start with a.ORG_ID = '" + orgId 
		//+ "' connect by prior a.ORG_ID = a.PARENT_ID)";
		String sql = "select distinct b.USER_ID from TD_SM_USERJOBORG b where b.ORG_ID in ("

  +"select a.ORG_ID from TD_SM_ORGANIZATION a where a.org_tree_level like  "
+"	(select concat(org_tree_level, '%') from TD_SM_ORGANIZATION c where c.ORG_ID = ?))" ;



		//根据用户ID删除用户所拥有的一切资源(除超级管理员外)
		UserManager userManager = SecurityDatabase.getUserManager() ;
		TransactionManager tm = new TransactionManager();
		String[] userIds = null;
		boolean tag = true;
		
		try
		{
			tm.begin();
			PreparedDBUtil db = new PreparedDBUtil();
			db.preparedSelect(sql);
			db.setString(1, orgid);
			db.executePrepared();
			//如果使用了离散用户，删除机构只将机构下的用户的资源和关系删掉，变为离散用户。如果没有离散用户将机构下的用户彻底删除
			boolean islisan = ConfigManager.getInstance().getConfigBooleanValue("enableorgusermove",true);
			if(db.size()>0)
			{
				 userIds = new String[db.size()];
				for(int i= 0; i<db.size(); i++)
				{
					userIds[i] = String.valueOf(db.getInt(i,"USER_ID"));
				}
				if(userIds.length > 0){
					if(islisan){
						userManager.deleteBatchUserRes(userIds,false);
					}else{
						userManager.deleteBatchUser(userIds,false);
					}
				}
			}
			//递归删除机构
			tag = orgManager.deleteOrg(orgid,false);
			tm.commit();
			if(userIds != null)
			{
				result.setOperationData("remove users");
			}
			if(triggerEvent)
			{
				EventUtil.sendORGUNIT_DELETEEVENT(result.getOperationData(), orgid);
//				if(userIds != null)
//					EventUtil.sendUSER_INFO_DELETEEvent(userIds);
//				EventUtil.sendUSER_ROLE_INFO_CHANGEEvent(orgid);
//				EventUtil.sendORGUNIT_INFO_DELETEEvent(orgid);
			}
		}
		catch(Exception e)
		{
			tag = false ;
			result.setCode(result.fail);
			result.setErrormessage(StringUtil.exceptionToString(e));
		}
		finally
		{
			tm.release();
		}
		if (tag) 
		{
			try {
				logManager.log(control.getUserAccount() ,
					operContent, openModle, operSource, description);
			} catch (ManagerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			result.setCode(result.ok);
			result.setErrormessage("删除部门成功");
		}
		else
	    {
			
		}
		return result;
	}
	 
 
	

}
