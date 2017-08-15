/**
 * 
 */
package com.frameworkset.platform.sysmgrcore.purviewmanager;

import java.io.Serializable;
import java.util.List;

import com.frameworkset.platform.security.AccessControl;

/**
 * <p>Title: BussinessCheck.java</p>
 *
 * <p>Description:
 * 定义用户/机构/岗位变动时进行先决条件检查的业务校验接口,判断是否可以执行这些变更
 * 如果不允许的话,就返回相关的信息
 * </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: bbossgroups</p>
 * @Date 2008-3-6 10:23:19
 * @author ge.tao
 * @version 1.0
 */
public interface BussinessCheck extends Serializable{
	/**
	 * 超级管理员角色ID
	 */
	public static final String ADMIN_ROLEID = "1";
	/**
	 * 部门管理员角色ID
	 */
	public static final String ORGMANAGER_ROLEID = "3";
	/**
	 * 在职岗位ID
	 */
	public static final String JOBMANAGER_JOBID = "1";
	
	/**
	 * 用户权限回收 的时候, 判断用户的机构信息, 决定是否能执行 用户权限回收的操作. 
	 * @param control
	 * @param userId
	 * @return 
	 * BussinessCheck.java
	 * @author: ge.tao
	 */
	public List userReclaimCheck(AccessControl control, String userId);
	
	/**
	 * 删除 的时候, 判断用户的机构信息, 决定是否能执行 删除和调动和权限回收的操作. 
	 * 如果返回为空, 可以删除 和 调动 用户, 否则不允许删除.
	 * @param control
	 * @param userId
	 * @return 
	 * PurviewManager.java
	 * @author: ge.tao
	 */
	public List userDeleteCheck(AccessControl control, String userId);
	
	/**
	 * 调动 用户的时候, 判断用户的机构信息, 决定是否能执行 删除和调动和权限回收的操作. 
	 * 如果返回为空, 可以删除 和 调动 用户, 否则不允许调动.
	 * @param control
	 * @param userId
	 * @return 
	 * PurviewManager.java
	 * @author: ge.tao
	 */
	public List userMoveCheck(AccessControl control, String userId);
	
	/**
	 * 机构权限回收的时候, 判断机构信息, 决定是否能执行 .
	 * @param control
	 * @param orgId
	 * @return 
	 * BussinessCheck.java
	 * @author: ge.tao
	 */
	public List orgReclaimCheck(AccessControl control, String orgId);
	
	/**
	 * 删除 的时候, 判断机构信息, 决定是否能执行 .
	 * @param control
	 * @param userId
	 * @return 
	 * PurviewManager.java
	 * @author: ge.tao
	 */
	public List orgDeleteCheck(AccessControl control, String userId);
	
	/**
	 * 转移 机构的时候, 判断用户的机构信息, 决定是否能执行 删除和调动和权限回收的操作. 
	 * 如果返回为空, 可以转移 机构, 否则不允许转移.
	 * @param control
	 * @param userId
	 * @return 
	 * PurviewManager.java
	 * @author: ge.tao
	 */
	public List orgMoveCheck(AccessControl control, String userId);
	
	/**
	 * 判断角色是否允许资源回收
	 * 缺省: 超级管理员角色 和 部门管理员角色不允许 资源回收 
	 * @param roleId
	 * @return 
	 * BussinessCheck.java
	 * @author: ge.tao
	 */
	public String roleReclaimCheck(String roleId);
	
	/**
	 * 判断角色是否允许删除
	 * 缺省: 超级管理员角色 和 部门管理员角色不允许 删除
	 * @param roleId
	 * @return 
	 * BussinessCheck.java
	 * @author: ge.tao
	 */
	public String roleDeleteCheck(String roleId);
	
	/**
	 * 判断岗位是否被机构设置
	 * @param jobId
	 * @return
	 */
	public boolean jobDeleteCheck(String jobId);
	
	
	

}
