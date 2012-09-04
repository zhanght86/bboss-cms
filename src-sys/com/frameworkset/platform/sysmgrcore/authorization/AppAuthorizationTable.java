//Source file: D:\\environment\\eclipse\\workspace\\cjxerpsecurity\\src\\com\\westerasoft\\common\\security\\websphere\\authorization\\impl\\AppAuthorizationTable.java

package com.frameworkset.platform.sysmgrcore.authorization;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.frameworkset.spi.SPIException;

import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.platform.config.ConfigManager;
import com.frameworkset.platform.security.authorization.AuthRole;
import com.frameworkset.platform.security.authorization.AuthUser;
import com.frameworkset.platform.security.authorization.impl.BaseAuthorizationTable;
import com.frameworkset.platform.security.authorization.impl.SecurityException;
import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.platform.sysmgrcore.entity.Role;
import com.frameworkset.platform.sysmgrcore.entity.User;
import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.platform.sysmgrcore.manager.OrgManager;
import com.frameworkset.platform.sysmgrcore.manager.RoleManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.platform.sysmgrcore.manager.UserManager;

/**
 * 
 * 应用角色/用户/用户组关系表
 * 
 * @author biaoping.yin
 * @version 1.0
 */
public class AppAuthorizationTable extends BaseAuthorizationTable implements Serializable{
	private static final Logger log = Logger
			.getLogger(AppAuthorizationTable.class);

//	/** 
//	 * Description:获取用户的所有被授予的角色
//	 * 
//	 * @param appName
//	 * @param server
//	 * @param cell
//	 * @param userName
//	 * @return
//	 * @see com.frameworkset.platform.security.authorization.impl.AppBaseAuthorizationTable#getAllRoleOfPrincipal(java.lang.String,
//	 *      java.lang.String, java.lang.String, java.lang.String)
//	 */
//	public String[] getAllRoleOfPrincipal(String userName)
//			throws SecurityException {
//		try {
//			// 吴卫雄修订：使用方法错误
//			// User user = new User();
//			// user.setUserName(userName);
//			// List list =
//			// SecurityDatabase.getRoleManager(super.getProviderType())
//			// .getRoleList("userName",userName,false);
//
//			UserManager userMgr = SecurityDatabase.getUserManager(super
//					.getProviderType());
//			User user = userMgr.getUser("userName", userName);
//			if (user == null) {
//				log.debug(userName + " is not exist");
//
//				throw new SecurityException("Error: user as null.");
//			}
//
//			// 通过 User 对象实例取得相关的角色列表
//			RoleManager roleMgr = SecurityDatabase.getRoleManager(super
//					.getProviderType());
//
//			List list = roleMgr.getAllRoleList(user);
//			// 吴卫雄修订结束
//
//			boolean enableuserrole = ConfigManager.getInstance()
//					.getConfigBooleanValue("enableuserrole", true);
//			String roles[] = null;
//			if (list == null && list.size() == 0) {
//
//				if (enableuserrole) {
//					roles = new String[1];
//					roles[0] = userName;
//				}
//				return roles;
//
//				// throw new SecurityException("No role assign to user["
//				// + userName + "]");
//			}
//			/**
//			 * 是否允许用户作为一种用户类型的角色，如果为true则将用户帐号作为用户固有的一种用户类型角色 否则将不做为固定角色，缺省为true
//			 */
//			Role role = null;
//			if (enableuserrole) {
//				roles = new String[list.size() + 1];
//				roles[0] = userName;
//				for (int i = 1; i < list.size() + 1; i++) {
//					role = (Role) list.get(i - 1);
//					roles[i] = role.getRoleName();
//				}
//			} else {
//				roles = new String[list.size()];
//
//				for (int i = 0; i < list.size(); i++) {
//					role = (Role) list.get(i);
//					roles[i] = role.getRoleName();
//				}
//			}
//			return roles;
//
//		} catch (SecurityException e) {
//			throw e;
//			// log.error("Get AllRoleOfPrincipal error of user["
//			// + userName + "]:"
//			// + e.getMessage());
//			// throw new SecurityException("Get AllRoleOfPrincipal error:"
//			// + e.getMessage());
//		} catch (SPIException e) {
//			throw new SecurityException("Get AllRoleOfPrincipal error:"
//					+ e.getMessage());
//		} catch (ManagerException e) {
//			e.printStackTrace();
//			throw new SecurityException("Get AllRoleOfPrincipal error:"
//					+ e.getMessage());
//		}
//	}

	public AuthRole[] getAllRoleOfPrincipal(String userName)
			throws SecurityException {
		TransactionManager tm = new TransactionManager (); 
		try {
			
			UserManager userMgr = SecurityDatabase.getUserManager(super
					.getProviderType());
			tm.begin(tm.RW_TRANSACTION);
			User user = userMgr.getUserByName(userName);
			if (user == null) {
				log.debug(userName + " not exist");
				throw new SecurityException("Error: user[userAccount=" + userName + "] not exist.");
			}

			// 通过 User 对象实例取得相关的普通角色列表
			RoleManager roleMgr = SecurityDatabase.getRoleManager(super
					.getProviderType());

			List list = roleMgr.getAllRoleList(user.getUserId().toString());
		

			boolean enableuserrole = ConfigManager.getInstance()
					.getConfigBooleanValue("enableuserrole", true);
			boolean enableorgrole = ConfigManager.getInstance()
			.getConfigBooleanValue("enableorgrole", true);
			AuthRole[] roles = null;
			if (list == null || list.size() == 0) {//用户没有授予任何普通角色时，根据系统开关是否将用户本身和用户隶属的机构作为用户的角色返回
				if(enableorgrole) //允许将机构作为角色
				{
					//新用户没有任何角色（实际上有普通用户角色时），应该要考虑来自机构直接被赋的资源。
					OrgManager orgMgr = SecurityDatabase.getOrgManager();
					List orgList = orgMgr.getOrgListOfUser(user.getUserId()+"");
					if(orgList == null)
						orgList = new ArrayList();
					if (enableuserrole) {
						roles = new AuthRole[orgList.size()+1];		
					}
					else
					{
						roles = new AuthRole[orgList.size()];
					}
					for(int i=0; i < orgList.size(); i++)
					{
						Organization org = (Organization)orgList.get(i);
						AuthRole authrole = new AuthRole();
						authrole.setRoleName(org.getOrgName());
						authrole.setRoleId(org.getOrgId());
						authrole.setRoleType(AuthRole.TYPE_ORG);
						roles[i] = authrole;
					}
					
					if (enableuserrole) {
						AuthRole authrole = new AuthRole();
						authrole.setRoleName(userName);
						authrole.setRoleId(user.getUserId() + "");
						authrole.setRoleType(AuthRole.TYPE_USER);
						roles[orgList.size()] = authrole;					
					}
				}
				else if (enableuserrole) {//允许将用户作为角色				
					roles = new AuthRole[1];		
					AuthRole authrole = new AuthRole();
					authrole.setRoleName(userName);
					authrole.setRoleId(user.getUserId() + "");
					authrole.setRoleType(AuthRole.TYPE_USER);
					roles[0] = authrole;	
				}
				
				return roles;

				// throw new SecurityException("No role assign to user["
				// + userName + "]");
			}
			
			if(enableorgrole)
			{
				/**
				 * 获取用户所属的机构角色
				 */
				List orglistOfuser = SecurityDatabase.getOrgManager().getOrgListOfUser(user.getUserId() + "");
				if( orglistOfuser == null)
					orglistOfuser = new ArrayList();
				/**
				 * 是否允许用户作为一种用户类型的角色，如果为true则将用户帐号作为用户固有的一种用户类型角色 否则将不做为固定角色，缺省为true
				 */
				
				AuthRole authrole = null;
				if (enableuserrole) {
					int size = orglistOfuser.size() + list.size() + 1;
					roles = new AuthRole[size];
					authrole = new AuthRole();
					authrole.setRoleName(userName);
					authrole.setRoleId(user.getUserId() + "");
					authrole.setRoleType(AuthRole.TYPE_USER);
					roles[0] = authrole;
					for (int i = 1; i < list.size() + 1; i++) {
						Role role = (Role) list.get(i - 1);
						authrole = new AuthRole();
						authrole.setRoleName(role.getRoleName());
						authrole.setRoleId(role.getRoleId());
						authrole.setRoleType(AuthRole.TYPE_ROLE);
						roles[i] = authrole;
						
					}
					
					for(int i = list.size() + 1; i < size; i ++)
					{
						Organization org = (Organization)orglistOfuser.get(i - list.size() - 1);
						authrole = new AuthRole();
						authrole.setRoleName(org.getOrgName());
						authrole.setRoleId(org.getOrgId());
						authrole.setRoleType(AuthRole.TYPE_ORG);
						roles[i] = authrole;
					}
					
					
				} else {
					int size = orglistOfuser.size() + list.size();
					roles = new AuthRole[size];
	
					for (int i = 0; i < list.size(); i++) {
						Role role = (Role) list.get(i);
						authrole = new AuthRole();
						authrole.setRoleName(role.getRoleName());
						authrole.setRoleId(role.getRoleId());
						authrole.setRoleType(AuthRole.TYPE_ROLE);
						roles[i] = authrole;
					}
					for(int i = list.size(); i < size; i ++)
					{
						Organization org = (Organization)orglistOfuser.get(i - list.size());
						authrole = new AuthRole();
						authrole.setRoleName(org.getOrgName());
						authrole.setRoleId(org.getOrgId());
						authrole.setRoleType(AuthRole.TYPE_ORG);
						roles[i] = authrole;
					}
				}
			}
			else
			{
				AuthRole authrole = null;
				if (enableuserrole) {
					int size = list.size() + 1;
					roles = new AuthRole[size];
					authrole = new AuthRole();
					authrole.setRoleName(userName);
					authrole.setRoleId(user.getUserId() + "");
					authrole.setRoleType(AuthRole.TYPE_USER);
					roles[0] = authrole;
					for (int i = 1; i < list.size() + 1; i++) {
						Role role = (Role) list.get(i - 1);
						authrole = new AuthRole();
						authrole.setRoleName(role.getRoleName());
						authrole.setRoleId(role.getRoleId());
						authrole.setRoleType(AuthRole.TYPE_ROLE);
						roles[i] = authrole;
					}
				}
			}
			
			return roles;

		} catch (SecurityException e) {
			//throw e;
			e.printStackTrace();
			//add by 20080721 gao.tang 抛出SecurityException(e)
			throw e;
		} catch (SPIException e) {
			throw new SecurityException("Get AllRoleOfPrincipal error:"
					+ e.getMessage());
		} catch (ManagerException e) {
			e.printStackTrace();
			throw new SecurityException("Get AllRoleOfPrincipal error:"
					+ e.getMessage());
		}
		//add by 20080721 gao.tang 添加Exception
		catch(Exception e){
			e.printStackTrace();
			throw new SecurityException(e);
		}
		finally
		{
			tm.releasenolog();
		}
	}

	public AuthUser[] getAllPermissionUsersOfResource(String resourceid, String operation, String resourceType) throws SecurityException {		
		try {

			RoleManager roleMgr = SecurityDatabase.getRoleManager(super
					.getProviderType());

			List list = roleMgr.getAllUserOfHasPermission(resourceid, operation, resourceType);
			if(list == null || list.size() == 0)
				return null;
			AuthUser[] authUsers = new AuthUser[list.size()];
			for(int i = 0; i < authUsers.length; i ++)
			{
				AuthUser authUser = new AuthUser();
				User user = (User)list.get(i);
				authUser.setUserAccount(user.getUserName());
				authUser.setUserID(user.getUserId() + "");
				authUser.setUserName(user.getUserRealname());
				authUsers[i] = authUser;
				
				
			}
			return authUsers;
			

		} catch (Exception e) {
			//add by 20080721 gao.tang 添加异常输出信息e.printStackTrace();
			e.printStackTrace();
			throw new SecurityException(e.getMessage());
		}


	}

}
