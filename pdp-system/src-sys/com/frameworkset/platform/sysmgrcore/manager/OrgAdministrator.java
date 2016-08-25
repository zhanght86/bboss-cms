package com.frameworkset.platform.sysmgrcore.manager;

import java.io.Serializable;
import java.util.List;

import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.util.ListInfo;

public interface OrgAdministrator extends Serializable{
	/**
	 * 通过机构id获取机构的管理员
	 * @param orgID 机构id
	 * @return List<User>
	 */
	public List getAdministorsOfOrg(String orgID);
	
	/**
	 * 通过机构id获取机构的管理员
	 * @param orgID 机构id
	 * @return List<User>
	 */
	public ListInfo getAdministorsOfOrg(String orgID, int offset, int maxItem);
	
	/**
	 * 获取机构管理员列表，包含机构的上级管理员和本及管理员的集合
	 * @param orgID 机构id
	 * @return List<User>
	 */
	public List getAllAdministorsOfOrg(String orgID);

	/**
	 * 给机构添加一个部门管理员
	 * 首先要确保用户已有部门管理员角色
	 * */
	public boolean addOrgAdmin(String userId, String orgId);
	
	/**
	 * 在部门管理员用户和机构的映射表td_sm_orgmanager中删除记录
	 * 如果用户已不是人和机构的部门管理员，则要删除该用户的部门管理员角色
	 * */
	public boolean deleteOrgAdmin(String userId, String orgId);
	
	/**
	 * 给机构添加一个部门管理员
	 * 首先要确保用户已有部门管理员角色
	 * */
	public boolean addOrgAdmin(String[] userId, String orgId, String curUserId);
	
	/**
	 * 在部门管理员用户和机构的映射表td_sm_orgmanager中删除记录
	 * 如果用户已不是人和机构的部门管理员，则要删除该用户的部门管理员角色
	 * */
	public boolean deleteOrgAdmin(String[] userId, String orgId);
	
	/**
	 * 判断用户是否是机构的部门管理员
	 * */
	public boolean isOrgAdmin(String userId, String orgId);
	
	/**
	 * 判断用户能否管理机构：
	 * 1.用户是当前机构的管理员
	 * 2.用户是当前机构的向上路径机构的管理员
	 * */
	public boolean userAdminOrg(String userId, String orgId);
	
	/**
	 * 提供获取用户可管理机构列表接口
	 * */
	public List getManagerOrgsOfUserByID(String userId);
	
	/**
	 * 提供获取用户可管理机构列表接口
	 * */
	public List getManagerOrgsOfUserByAccount(String userAccount);
	
	/**
	 * 通过用户id递归获取用户能管理的机构列表
	 * */
	public ListInfo getAllManagerOrgsOfUserByID(String userId, String orgName, String orgnumber, 
			long offset, int maxPagesize) throws ManagerException ;
	
	/**
	 * 根据用户id判断该用户是否为部门管理员
	 * @param userId
	 * @return
	 */
	public boolean isOrgManager(String userId);
	
}
