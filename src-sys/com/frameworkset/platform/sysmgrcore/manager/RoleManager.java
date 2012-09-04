package com.frameworkset.platform.sysmgrcore.manager;

import java.io.Serializable;
import java.util.List;

import org.frameworkset.spi.Provider;

import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.security.authorization.AuthRole;
import com.frameworkset.platform.security.authorization.impl.AccessPermission;
import com.frameworkset.platform.sysmgrcore.control.PageConfig;
import com.frameworkset.platform.sysmgrcore.entity.Group;
import com.frameworkset.platform.sysmgrcore.entity.Operation;
import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.platform.sysmgrcore.entity.Res;
import com.frameworkset.platform.sysmgrcore.entity.Role;
import com.frameworkset.platform.sysmgrcore.entity.Roleresop;
import com.frameworkset.platform.sysmgrcore.entity.User;
import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.util.ListInfo;

/**
 * 项目：SysMgrCore <br>
 * 描述：角色管理接口 <br>
 * 版本：1.0 <br>
 * 
 * @author 吴卫雄
 */
public interface RoleManager extends Provider, Serializable {

	/**
	 * 删除角色实例同时将连带删除与该实例有关的所有实例，如：用户与角色关系实例。
	 * 
	 * @param role
	 * @return
	 * @throws ManagerException
	 */
	public boolean deleteRole(Role role) throws ManagerException;

	/**
	 * 删除资源资源操作实例
	 * 
	 * @param roleresop
	 * @return
	 * @throws ManagerException
	 */
	public boolean deleteRoleresop(Roleresop roleresop) throws ManagerException;

	/**
	 * 获取角色的用户列表
	 */
	public ListInfo getRoleUserList(String userId, String roleID, int offset, int maxItem, boolean tag);
	
	/**
	 * 根据 resId,roleId,restypeId 删除角色资源操作对象实例
	 * 
	 * @param resId
	 * @param roleId
	 * @param restypeId
	 * @return
	 * @throws ManagerException
	 */
	public boolean deleteRoleresop(String resId, String roleId, String restypeId)
			throws ManagerException;
	
	/**
	 * 删除角色类型/用户类型角色对应的资源操作许可
	 * @param resId 资源标识
	 * @param restypeId 资源类型
	 * @param roleid 角色id
	 * @param type role标识角色类型，user标识用户类型
	 * @return
	 * @throws ManagerException
	 */
	 
	public boolean deletePermissionOfRole(String resId, String restypeId, String roleid,String type)
			throws ManagerException;
	
	/**
	 * 删除角色类型/用户类型角色对应的资源操作许可
	 * 
	 * @param List resIds<String[] {resid,resname}>
	 * @param restypeId 资源类型
	 * @param roleid 角色id
	 * @param type role标识角色类型，user标识用户类型
	 * @return
	 * @throws ManagerException
	 */
	 
	public boolean deletePermissionOfRole(List resIds, String restypeId, String roleids[],String type)
			throws ManagerException;
	
	
	
	public boolean deletePermissionOfRole(String opid,String resId, String restypeId, String roleid, String type) 
	throws ManagerException; 
	/**
	 * 删除某一资源id及资源类型对应的所有角色及操作
	 * 景峰添加
	 * @param resId
	 * @param restypeId
	 * @return
	 * @throws ManagerException
	 */
	public boolean deleteRoleresop(String resId, String restypeId)
			throws ManagerException;

//	/**
//	 * 根据角色对象的属性和值取角色对象实例
//	 * 
//	 * @param propName
//	 * @param value
//	 * @return
//	 * @throws ManagerException
//	 */
//	public Role getRole(String propName, String value) throws ManagerException;

	/**
	 * 根据用户对象的属性名和值取角色列表
	 * 
	 * @param propName
	 * @param value
	 * @param isLike
	 *            用于描述在提取对象的过程中是否取大致相同的角色对象
	 * @return
	 * @throws ManagerException
	 */
	public List getRoleList(String propName, String value, boolean isLike)
			throws ManagerException;

	/**
	 * 取所有角色
	 * 
	 * @return List
	 * @throws ManagerException
	 */
	public List getRoleList() throws ManagerException;

	/**
	 * 根据操作取所有的角色
	 * 
	 * @param oper
	 * @return List
	 * @throws ManagerException
	 */
	public List getRoleList(Operation oper) throws ManagerException;

	/**
	 * 根据资源取所有相关的角色。
	 * 
	 * @param res
	 * @return List
	 * @throws ManagerException
	 */
	public List getRoleList(Res res) throws ManagerException;

	/**
	 * 通过用户id获取用户自身的角色）
	 */
	public List getRoleListByUserRole(User user) throws ManagerException;
	/**
	 * 根据用户取所有相关的角色。
	 * 
	 * @param user
	 * @return TList
	 * @throws ManagerException
	 */
	public List getRoleList(User user) throws ManagerException;

	// 吴卫雄删除：该函数所对应的功能由 UserManager 中实现
	// /**
	// * 根据用户删除该用户与角色的关系
	// *
	// * @param user
	// * @return Boolean
	// */
	// public boolean deleteRoleAndUser(User user);

	/**
	 * 装载指定角色所关联的对象并返回关联后的角色对象实例
	 * 
	 * @throws ManagerException
	 */
	public Role loadAssociatedSet(String roleId, String associated)
			throws ManagerException;

	/**
	 * 存储角色实例
	 * 修改角色时用
	 * @param role
	 * @return
	 * @throws ManagerException
	 */
	public boolean storeRole(Role role) throws ManagerException;

	/**
	 * 存储角色实例
	 * 新增角色时用
	 * @param role
	 * @return
	 * @throws ManagerException
	 */
	public boolean insertRole(Role role) throws ManagerException;

	
	/**
	 * 存储指定的角色资源操作对象实例
	 * 
	 * @param roleresop
	 * @return
	 * @throws ManagerException
	 */
	//public boolean storeRoleresop(Roleresop roleresop) throws ManagerException;

	/**
	 * 根据用户取所有相关的角色。
	 * 
	 * @param user
	 * @return TList
	 */
	public List getAllRoleList(User user) throws ManagerException;
	
	/**
	 * 根据用户取所有相关的角色。
	 * 
	 * @param user
	 * @return TList
	 */
	public List getAllRoleList(String userAccount) throws ManagerException;

	/**
	 * 根据用户取所有相关的(组角色关系)角色。
	 * 
	 * @param user
	 * @return TList
	 */
	public List getRoleListByGroupRole(User user) throws ManagerException;

	/**
	 * 根据组对应的角色。
	 * 
	 * @param user
	 * @return TList
	 */
	public List getRoleList(Group group) throws ManagerException;

	/**
	 * 获取指定的角色对象实例
	 * 
	 * @param resId,operName,restypeId
	 * @return
	 * @throws ManagerException
	 */
	public List getAllRoleHasPermissionInResource(String resId,
			String operName, String restypeId) throws ManagerException;

	/**
	 * 返回页面配置管理类
	 * 
	 * @return
	 * @throws ManagerException
	 */
	public PageConfig getPageConfig() throws ManagerException;
	
	/**
	 * 根据用户取所有相关的(机构角色关系)角色。
	 * 
	 * @param user
	 * @return TList
	 */
	public List getRoleListByOrgRole(User user) throws ManagerException;	
	
	/**
	 * 根据机构取相关的角色。
	 * 
	 * @param Organization
	 * @return TList
	 */
	public List getRoleList(Organization org) throws ManagerException;	
	/**王卓添加
	 * 删除指定的机构角色对象实例
	 * 
	 * @param orgrole
	 * @return
	 */
	public boolean deleteOrgrole(Role role) throws ManagerException;

	/**
	 * 删除角色所授予的用户
	 * @param role
	 * @return
	 * @throws ManagerException 
	 */
	public boolean deleteUserOfRole(Role role) throws ManagerException ;
	
	/**
	 * 判断被复制角色权限是否是该角色已拥有的权限，如果不是，则添加该权限
	 * @param rolecopyId
	 * @param id
	 * @return
	 * @throws ManagerException
	 */
    public boolean copyResOfRole(String rolecopyId, String[] id) throws ManagerException ;
    
    /**
     * 将所选角色复制给当前角色
     * @param rolecopyId 当前角色ID
     * @param id 所选角色ID
     * @return
     * @throws ManagerException
     */
    public boolean copyResOfRoleSelf(String rolecopyId, String[] id) throws ManagerException ;
	
	 /**
	 * 判断资源是否授予特定的角色，只要有角色拥有资源的一个操作权限，就返回true
	 * @param resourceType
	 * @param resourceID
	 * @return
	 */
	public boolean hasGrantedRoles(String resourceType,String resourceID) throws ManagerException;
	
	public List getRoleList(String hql) throws ManagerException;
	
	public ListInfo getRoleList(String hql, long offset, int pageItemsize) throws ManagerException;
	
	/**
	 * 对角色授权接口
	 * @param roleid 
	 * @param resourceid
	 * @param resourceType
	 * @param action
	 * @param roleType
	 */
	public void grantRolePermission(String roleid,
									  String resid,
									  String resTypeid,
									  String opid,
									  String resname)throws ManagerException ;
	
	/**
	 * 获取所有具有资源许可访问权限的所有用户列表
	 * @param resid 许可资源id
	 * @param resOpr 许可资源操作
	 * @param resType 许可资源类型
	 * @return java.util.List<com.frameworkset.platform.sysmgrcore.entity.User>
	 * @throws ManagerException
	 */
	public List getAllUserOfHasPermission(String resid,String resOpr,String resType) throws ManagerException ;
	/**
	 * 根据用户id，操作id，资源类型取用户有许可访问权限的机构
	 * @param userid
	 * @param opid
	 * @param resType
	 * @return
	 * @throws ManagerException
	 */
	public List getAllResource(String userid,String opid,String resType) throws ManagerException ;
	
	/**
	 * 判断资源是否授予了对应的角色
	 * @param resource
	 * @param resourceType
	 * @return
	 * @throws ManagerException
	 */
	public boolean hasGrantRole(AuthRole role,String resource ,String resourceType) throws ManagerException ;
	/**
	 * 存储指定的角色资源操作对象实例
	 * @param opid
	 * @param resid
	 * @param roleid
	 * @param restypeid
	 * @param resname
	 * @param role_type 角色类型
	 * @return
	 * @throws ManagerException
	 */
	public boolean storeRoleresop(String opid,String resid,String roleid,String restypeid,String resname,String role_type) throws ManagerException;
	
	/**
	 * 将资源及操作授予对应的角色数组中的角色
	 * @param opid
	 * @param resid
	 * @param roleids
	 * @param restypeid
	 * @param resname
	 * @param role_type 角色类型
	 * @return
	 * @throws ManagerException
	 */
	public boolean storeRoleresop(String opid,String resid,String roleids[],String restypeid,String resname,String role_type) throws ManagerException;
	
	
	/**
	 * 多操作许可授予相应类型的角色
	 * @param opids
	 * @param resid
	 * @param roleid
	 * @param restypeid
	 * @param resname
	 * @param roletype
	 * @return
	 * @throws ManagerException
	 */
	public boolean storeRoleresop(String opids[],String resid,String roleid,String restypeid,String resname,String roletype) throws ManagerException;
	
	
	/**
	 * 多操作许可授予相应类型的角色
	 * @param opids
	 * @param resid
	 * @param roleid
	 * @param restypeid
	 * @param resname
	 * @param roletype
	 * @param broadevent 是否广播事件
	 * @return
	 * @throws ManagerException
	 */
	public boolean storeRoleresop(String opids[],String resid,String roleid,String restypeid,String resname,String roletype,boolean broadevent) throws ManagerException;
	
	/**
	 * 多操作许可授予相应类型的角色
	 * @param opids
	 * @param resid
	 * @param roleid
	 * @param restypeid
	 * @param resname
	 * @param types
	 * @return
	 * @throws ManagerException
	 */
	public boolean restoreRoleresop(String opids[],List resids,String roleids[],String restypeid,String roletype) throws ManagerException;
	
	
	/**
	 * 多操作许可授予相应类型的角色(递归资源授予)
	 * @param opids
	 * @param resid
	 * @param roleid
	 * @param restypeid
	 * @param resname
	 * @param types
	 * @param isRecursive 0:表示不递归 1：递归
	 * @return
	 * @throws ManagerException
	 */
	public boolean grant(String opids[],String resid,String roleid,String restypeid,String resname,String roletype,String isRecursive)throws ManagerException;
	
	/**
	 * 将角色授予多个用户
	 * @param userids
	 * @param roleid
	 * @throws ManagerException
	 */
	public void grantRoleToUsers(String[] userids,String roleid) throws ManagerException;
	
	/**
	 * 删除角色的用户
	 * @param roleid
	 * @return
	 * @throws ManagerException
	 */
	public boolean deleteUserOfRole(String roleid) throws ManagerException;
	
	/**
	 * 获取许可的角色集合
	 * @param permissions
	 * @return
	 * @throws ManagerException
	 */
	public List getAllRoleTypeRoleHasPermission(AccessPermission[] permissions) throws ManagerException ;
	/**
	 * 获取许可的机构集合
	 * @param permissions
	 * @return
	 * @throws ManagerException
	 */
	public List getAllOrganizationTypeRoleHasPermission(AccessPermission[] permissions) throws ManagerException;
	/**
	 * 获取许可的用户类型角色集合
	 * @param permissions
	 * @return
	 * @throws ManagerException
	 */
	public List getAllUserTypeRoleHasPermission(AccessPermission[] permissions) throws ManagerException;
	/**
	 * 通过岗位机构ID获取机构下以赋予的岗位角色--2007-10-17 gao.tang
	 * @param job
	 * @return
	 * @throws ManagerException
	 */
	public List getJobListByRoleJob(String jobId, String orgId) throws ManagerException;
	
	/**
	 * 获取所有的角色列表
	 * @return
	 * @throws ManagerException
	 */
	public List getAllRoleList() throws ManagerException;
	
	/**
	 * 判断当前是否有角色
	 * @return
	 * @throws ManagerException
	 */
	public boolean hasRole() throws ManagerException;
	
	/**
	 * 通过岗位机构ID获取当前机构下的所有角色---2007.10.24 gao.tang
	 * @return
	 * @throws ManagerException
	 */
	public List getJobOrgAllRoleList(AccessControl accesscontrol,String jobId, String orgId) throws ManagerException;
	
	public List getAllRoleListBysql(String sql) throws ManagerException ;
	
	/**
	 * 通过用户ID获取用户岗位对应的角色 2007.11.01---gao.tang
	 * @param userId---用户ID
	 * @return
	 * @throws ManagerException
	 */
	public List getJobRoleByList (String userId) throws ManagerException ;
	
	/**
	 * 得到用户岗位角色所隶属的机构-岗位 gao.tang 2007.11.02
	 * @param userId--用户ID
	 * @param roleId--角色ID
	 * @return 隶属的机构-岗位列表
	 * @throws ManagerException
	 */
	public List getOrgJobList (String userId, String roleId) throws ManagerException ;
	
	/**
	 * 给多个角色复权限，如果操作组为null或者长度为0，则删除多个角色相应资源的操作权限
	 * @param opids
	 * @param resid
	 * @param roleids
	 * @param restypeid
	 * @param resname
	 * @param roletype
	 * @param needdelete
	 * @return
	 * @throws ManagerException
	 */
	public boolean grantRoleresop(String[] opids, String resid, String roleids[],
			String restypeid, String resname, String roletype,boolean needdelete)
			throws ManagerException ;
	
	/**
	 * *****************************************************************
	 * 用于用户管理的批量资源操作授予
	 * 不采取原有的先删再存的方式，采取只加权限的方式
	 * 2008-01-29 16:19
	 * *****************************************************************
	 * @param opids
	 * @param resid
	 * @param roleids
	 * @param restypeid
	 * @param resname
	 * @param roletype
	 * @param needdelete
	 * @return
	 * @throws ManagerException
	 */
	public boolean grantRoleresopForBatch(String[] opids, String resid, String roleids[],
			String restypeid, String resname, String roletype,boolean needdelete)
			throws ManagerException;
	
	
	public boolean deleteUsersOfRole(String userids[],String roleid) throws ManagerException;
	
	public Role getRoleById(String roleid) throws ManagerException ;	
	public Role getRoleByName(String roleName) throws ManagerException;
	
	/**
	 * 删除角色实例同时将连带删除与该实例有关的所有实例，如：用户与角色关系实例。
	 * 
	 * @param role
	 * @return
	 */
	public boolean deleteRoles(String[] roleids) throws ManagerException ;
	
	/**
	 * @param opids 操作类型ID
	 * @param resIds 资源ID
	 * @param roleids 用户/角色ID
	 * @param restypeid 资源类型
	 * @param roletype 标示是角色还是用户
	 * @param userId 当前用户ID
	 * @return
	 * @throws ManagerException
	 */
	public boolean saveResGrant(String[] opids, List resIds, String[] roleids,
			String restypeid, String roletype, String userId) throws ManagerException ;
	
	/**
	 * 用户有权限授权的角色
	 * @param userId
	 * @param control
	 * @return 
	 * RoleManager.java
	 * @author: ge.tao
	 */
	public List userHasPermissionRole(String userId, AccessControl control)throws Exception;
	
	/**
	 * 保存用户自定义资源操作项
	 * @param opids 		选中的自定义操作ID
	 * @param roleIds 		选中的自定义roleIds
	 * @param un_opids 		未选中的自定义操作ID 
	 * @param un_roleIds	未选中的自定义roleIds
	 * @param resIds		多个资源授予给多个用户或多个角色或多个机构
	 * @param types			资源类型--包括（用户：user;角色：role；机构：organization）
	 * @param restypeId		用户自定义资源的类型ID
	 * @param resName		用户自定义资源的描述
	 * @return
	 * @throws Exception
	 */
	public boolean saveRoleresop(String[] opids,String[] roleIds,String[] un_opids,String[] un_roleIds,
			String[] resIds,String types,String restypeId,String[] resName);
	
	/**
	 * 保存用户自定义资源操作项
	 * @param opids 		选中的自定义操作ID
	 * @param roleIds 		选中的自定义roleIds
	 * @param un_opids 		未选中的自定义操作ID 
	 * @param un_roleIds	未选中的自定义roleIds
	 * @param resIds		多个资源授予给多个用户或多个角色或多个机构
	 * @param types			资源类型--包括（用户：user;角色：role；机构：organization）
	 * @param restypeId		用户自定义资源的类型ID
	 * @param resName		用户自定义资源的描述
	 * @return
	 * @throws Exception
	 */
	public boolean saveBatchRoleresop(String[] opids,String[] roleIds,String[] un_opids,String[] un_roleIds,
			String[] resIds,String types,String restypeId,String[] resName);
	
	/**
	 * 批量保存用户自定义资源操作项给机构-机构游递归授予子机构的特殊授权
	 * @param opids 		选中操作ID
	 * @param orgId 		机构ID
	 * @param un_opids		没有被选中的操作ID
	 * @param resIds		资源ID
	 * @param types			资源类型
	 * @param restypeId		
	 * @param resName
	 * @param isRecursion	是否递归授予子机构
	 * @return
	 */
	public boolean saveBatchOrgRoleresop(String[] opids,String orgId,String[] un_opids,String[] resIds,
			String types,String restypeId,String[] resName,boolean isRecursion);
	
	/**
	 * 保存用户自定义资源操作项给机构-机构游递归授予子机构的特殊授权
	 * @param opids 		选中操作ID
	 * @param orgId 		机构ID
	 * @param un_opids		没有被选中的操作ID
	 * @param resIds		资源ID
	 * @param types			资源类型
	 * @param restypeId		
	 * @param resName
	 * @param isRecursion	是否递归授予子机构
	 * @return
	 */
	public boolean saveOrgRoleresop(String[] opids,String orgId,String[] un_opids,String[] resIds,
			String types,String restypeId,String[] resName,boolean isRecursion);
	
	/**
	 * 判断资源是否存在
	 * @param opid
	 * @param roleId
	 * @param resId
	 * @param types
	 * @param restypeId
	 * @return
	 */
	public boolean hasRoleresop(String opid,String roleId,String resId,String types,String restypeId);
}