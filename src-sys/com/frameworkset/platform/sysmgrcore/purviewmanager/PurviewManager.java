/**
 * 
 */
package com.frameworkset.platform.sysmgrcore.purviewmanager;

import java.io.Serializable;
import java.util.List;

import com.frameworkset.platform.security.AccessControl;


/**
 * <p>Title: PurviewManager.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date 2008-3-6 9:33:50
 * @author ge.tao
 * @version 1.0
 */
public interface PurviewManager extends Serializable {	
	/**
	 * "在职" 岗位ID
	 */
	public static final String EVERYONE_JOBID = "1";
	/**
	 * 回收直接授权给用户的资源
	 * @param userId
	 * @return 
	 * PurviewManager.java
	 * @author: ge.tao
	 */
	public String reclaimUserDirectResources(String userId);
	
	/**
	 * 回收授予用户的角色
	 * @param userId
	 * @return 
	 * PurviewManager.java
	 * @author: ge.tao
	 */
	public String reclaimUserRoles(String userId);
	
	/**
	 * 回收授予用户的岗位
	 * @param userId
	 * @return 
	 * PurviewManager.java
	 * @author: ge.tao
	 */
	public String reclaimUserJobs(String userId);
	
	/**
	 * 回收直接授权给机构的资源
	 * @param orgId
	 * @return 
	 * PurviewManager.java
	 * @author: ge.tao
	 */
	public String reclaimOrgDirectResources(String orgId);
	
	/**
	 * 回收授予机构的角色
	 * @param orgId
	 * @return 
	 * PurviewManager.java
	 * @author: ge.tao
	 */
	public String reclaimOrgRoles(String orgId);
	
	
	/**
	 * 回收用户的资源,包括: 直接资源, 角色, 岗位
	 * @param control
	 * @param userId
	 * @param isReclaimDirectRes
	 * @param isReclaimUserRoles
	 * @param isReclaimUserJobs
	 * @return 
	 * PurviewManager.java
	 * @author: ge.tao
	 */
	public String reclaimUserResources(AccessControl control,
			                            String userId,
			                            boolean isReclaimDirectRes, 
			                            boolean isReclaimUserRoles, 
			                            boolean isReclaimUserJobs,
			                            boolean isReclaimUserGroupRes);
	
	
	/**
	 * 回收机构的资源,包括: 直接资源, 角色
	 * @param orgId
	 * @param isReclaimOrgDirectRes
	 * @param isReclaimOrgRoleRes
	 * @param isRecursion 是否递归
	 * @return 
	 * PurviewManager.java
	 * @author: ge.tao
	 */
	public boolean reclaimOrgResources(String orgId, 
			                           boolean isReclaimOrgDirectRes, 
			                           boolean isReclaimOrgRoleRes,
			                           boolean isRecursion);
	
	
	
	/**
	 * 回收 多个 用户的资源,包括: 直接资源, 角色, 岗位
	 * @param control
	 * @param userIds
	 * @param isReclaimDirectRes
	 * @param isReclaimUserRoles
	 * @param isReclaimUserJobs
	 * @return 
	 * PurviewManager.java
	 * @author: ge.tao
	 */
	public List reclaimUsersResources(AccessControl control,
			                            String[] userIds,
			                            boolean isReclaimDirectRes, 
			                            boolean isReclaimUserRoles, 
			                            boolean isReclaimUserJobs,
			                            boolean isReclaimUserGroupRes);
	
	/**
	 * 回收机构下的用户资源, 包括: 直接资源, 角色, 岗位，用户组
	 * 可以递归
	 * @param control
	 * @param orgId
	 * @param isReclaimOrgDirectRes
	 * @param isReclaimOrgRoleRes
	 * @param isReclaimDirectRes
	 * @param isReclaimUserRoles
	 * @param isReclaimUserJobs
	 * @param isReclaimUserGroupRes
	 * @param isRecursion true:递归回收子机构下的用户资源, false:回收当前机构下用户资源
	 * @return 
	 * PurviewManager.java
	 * @author: ge.tao
	 */
	public List reclaimOrgUsersResources(AccessControl control,
			                             String orgId, 
			                             boolean isReclaimDirectRes, 
				                         boolean isReclaimUserRoles, 
				                         boolean isReclaimUserJobs,
				                         boolean isReclaimUserGroupRes,
				                         boolean isRecursion);
	
	/**
	 * 回收授予角色的资源
	 * 不能回收授予给 超级管理员角色、部门管理员角色的资源
	 * @param roleIds
	 * @return List<rolenmae:memo>
	 * PurviewManager.java
	 * @author: ge.tao
	 */
	public List reclaimRoleResources(String[] roleIds);
	
	/**
	 * 复制当前用户权限给其他用户
	 * @param userId 当前用户Id
	 * @param checkUserIds 所选用户的ID
	 * @param userSelf 复制用户自身资源给其他用户：当userSelf值为“self”复制用户自身资源权限
	 * @param userRole 复制用户角色资源给其他用户：当userRole值为“role”复制用户角色资源权限
	 * @param userJob  是否复制岗位资源给：当userJob值为“job”复制岗位资源权限
	 * @param curOrgId 所选复制权限的用户机构ID
	 * @return
	 * @author gao.tang
	 */
	public boolean userResCopy(String userId, String[] checkUserIds, String userSelf, String userRole, String userJob, String curOrgId);
	
	/**
	 * 复制所选用户的权限给当前用户
	 * @param userId 当前用户Id
	 * @param checkUserIds 所选用户的ID
	 * @param userSelf 复制用户自身资源给其他用户：当userSelf值为“self”复制给其他用户自身资源权限
	 * @param userRole 复制用户角色资源给其他用户：当userRole值为“role”复制给其他用户角色资源权限
	 * @param userJob  是否复制岗位资源给：当userJob值为“job”复制岗位资源权限
	 * @param curOrgId 所选复制权限的用户机构ID
	 * @return
	 * @author gao.tang
	 */
	public boolean userResCopySelf(String userId, String[] checkUserIds, String userSelf, String userRole, String userJob, String curOrgId);
	
	/**
	 * 判断用户是否是部门管理员或拥有超级管理员角色
	 * @param curOrgId
	 * @param userId
	 * @author gao.tang
	 */
	public boolean isAdminOrOrgmanager(String curOrgId, String userId);
	
	/**
	 * 根据机构ID获取机构名称
	 * @param 
	 * @return 
	 * PurviewManager.java
	 * @author: ge.tao
	 */
	public String getOrgNameByOrgId(String orgIds);
	
	/**
	 * 根据用户ID获取用户登录名
	 * @param userIds
	 * @return 
	 * PurviewManager.java
	 * @author: ge.tao
	 */
	public String getUserNamesByUserIds(String userIds);
	
	/**
	 * 保存树 权限授予资源
	 * @param opId 操作ID
	 * @param checkValues  res_id^#restype_id^#res_name 选中复选框的值
	 * @param restypeId 资源类型ID（菜单retype_id包括'column','cs_column','report_column'） 
	 * @param roleId 
	 * @param types 授予类型类别
	 * @param isColumn 是否是菜单资源
	 * @return
	 */
	public boolean saveTreeRoleresop(String opId, String restype_id, String[] checkValues, String roleId, String types, String isColumn,String[] unselectValues);
	
	/**
	 * 保存角色授予权限
	 * @param opId 操作ID
	 * @param resType_id 资源
	 * @param checkValues 选中项
	 * @param un_checkValues 未选中项 
	 * @param role_id 
	 * @param types 类型
	 * @return
	 */
	public boolean saveRoleListRoleresop(String opId, String resType_id, String[] checkValues, String[] un_checkValues, String role_id, String types);
	
	/**
	 * 保存树 多个用户权限授予资源
	 * @param opId 操作ID
	 * @param checkValues  res_id^#restype_id^#res_name 选中复选框的值
	 * @param restypeId 资源类型ID（菜单retype_id包括'column','cs_column','report_column'） 
	 * @param roleId 
	 * @param types 授予类型类别
	 * @return
	 */
	public boolean batchSaveTreeRoleresop(String opId, String restype_id, String[] checkValues, String roleId, String types);
	
	/**
	 * 多个用户保存角色授予权限
	 * @param opId 操作ID
	 * @param resType_id 资源
	 * @param checkValues 选中项
	 * @param role_id 
	 * @param types 类型
	 * @return
	 */
	public boolean batchSaveRoleListRoleresop(String opId, String resType_id, String[] checkValues, String role_id, String types);
	
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
	 * @param isBatch       是否批量资源操作授予
	 * @return
	 * @throws Exception
	 */
	public boolean saveSelfDefineRoleresop(String[] opids,String[] roleIds,String[] un_opids,String[] un_roleIds,
			String[] resIds,String types,String restypeId,String[] resName,boolean isBatch);
	
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
	public boolean saveSelfDefineOrgRoleresop(String[] opids,String orgId,String[] un_opids,String[] resIds,
			String types,String restypeId,String[] resName,boolean isRecursion);
	
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
	public boolean saveSelfDefineBatchOrgRoleresop(String[] opids,String orgId,String[] un_opids,String[] resIds,
			String types,String restypeId,String[] resName,boolean isRecursion);
	

}
