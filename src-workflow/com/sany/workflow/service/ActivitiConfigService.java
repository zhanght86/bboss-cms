package com.sany.workflow.service;

import java.io.InputStream;
import java.util.List;

import com.frameworkset.util.ListInfo;
import com.sany.workflow.entity.ActivitiNodeCandidate;
import com.sany.workflow.entity.ActivitiNodeInfo;
import com.sany.workflow.entity.Group;
import com.sany.workflow.entity.Nodevariable;
import com.sany.workflow.entity.OrganizationDTO;
import com.sany.workflow.entity.User;
import com.sany.workflow.entity.VariableResource;

public interface ActivitiConfigService {

	/**
	 * 查询组织机构
	 * @param parentId 父组织结构ID
	 * @return 
	 */
	public List<OrganizationDTO> getOrgsByParentId(String parentId);
	
	/**
	 * 查询节点配置信息
	 * @param processKey 流程KEY
	 * @param taskKey 任务key
	 * @return
	 */
	public ActivitiNodeInfo getActivitiNodeByKeys(String processKey,String taskKey);
	
	/**
	 * 根据查询条件查询用户分页列表
	 * @param offset
	 * @param pagesize
	 * @return
	 */
	public List<User> queryUsers(User user);
	
	/**
	 * 根据一组用户名查询CandidateUser
	 * @param usernames 用户名以","隔开
	 * @return
	 */
	public List<User> queryUsersByNames(String usernames);
	
	/**
	 * 根据一组用户名查询CandidateGroup
	 * @param groups 用户名以","隔开
	 * @return
	 */
	public List<Group> getGroupInfoByNames(String groups) ;
	
	/**
	 * 根据条件查询用户组分页列表
	 * @param group 
	 * @param offset
	 * @param pagesize
	 * @return
	 */
	public List<Group> queryGroups(Group group);
	
	/**
	 * 查询节点配置信息
	 * @param id 节点配置ID
	 * @return
	 */
	public ActivitiNodeCandidate getActivitiNodeCandidateById(String id);
	
	/**
	 * 查询流程的待办配置
	 * @param activitiNodeCandidate
	 * @return
	 */
	public List<ActivitiNodeCandidate> queryActivitiNodeCandidate(ActivitiNodeCandidate activitiNodeCandidate);
	
	/**
	 * 查询流程的待办配置
	 * @param process_key		
	 * @param business_id		
	 * @param business_type
	 * @return
	 */
	public List<ActivitiNodeCandidate> queryActivitiNodeCandidate(String process_key,String business_id,String business_type);
	
	/**
	 * 查询流程的待办配置
	 * @param process_key
	 * @return
	 */
	public List<ActivitiNodeCandidate> queryActivitiNodeCandidate(String process_key);
	
	/**
	 * 查询流程的待办配置
	 * @param process_key
	 * @param activityKey
	 * @return
	 *//*
	public ActivitiNodeCandidate queryActivitiNodeCandidate(String process_key,String activityKey);
	*/

	/**
	 * 查询流程的待办配置
	 * @param process_key
	 * @param activityKey
	 * @param business_id
	 * @param business_type
	 * @return
	 */
	public ActivitiNodeCandidate queryActivitiNodeCandidate(String process_key,String activityKey,String business_id,String business_type);
	
	/**
	 * 根据登陆名查询用户真实姓名
	 * @param username 用户登陆名
	 * @return
	 */
	public String getRealNameByName(String username);
	
	
	/**
	 * 保存流程节点基本信息
	 * @param processKey
	 */
	public void addActivitiNodeInfo(String processKey) throws ActivitiConfigException ;
	
	/**
	 * 保存节点待办信息
	 * @param activitiNodeCandidate
	 */
	public void addActivitiNodeCandidate(List<ActivitiNodeCandidate> activitiNodeCandidates) throws ActivitiConfigException;
	
	/**
	 * 查询节点待办信息
	 * @param nodeId
	 * @return
	 *//*
	public ActivitiNodeCandidate getActivitiNodeCandidateByNodeId(String nodeId,String orgId);*/
	
	/**
	 * 查询节点信息
	 * @param id 节点ID
	 * @return
	 */
	public ActivitiNodeInfo getActivitiNodeInfoById(String id);
	
	/**
	 * 新增节点参数配置
	 * @param nodevariable
	 */
	public String addNodevariable(Nodevariable nodevariable);
	
	/**
	 * 修改节点参数配置
	 * @param nodevariable
	 */
	public void updateNodevariableParamvalue(Nodevariable nodevariable);
	
	
	/**
	 * 查询节点参数配置列表
	 * @param processKey
	 * @param offset
	 * @param pagesize
	 * @return
	 */
	public List<Nodevariable> selectNodevariable(String processKey);
	
	/**
	 * 查询节点参数配置列表
	 * @param processKey
	 * @param activityKey
	 * @param offset
	 * @param pagesize
	 * @return
	 */
	public List<Nodevariable> selectNodevariable(String processKey,String activityKey);
	
	
	/**
	 * 查询节点参数配置分页列表
	 * @param processKey
	 * @param business_id
	 * @param business_type
	 * @param offset
	 * @param pagesize
	 * @return
	 */
	public List<Nodevariable> selectNodevariable(String processKey,String business_id,String business_type);
	
	/**
	 * 查询节点参数配置列表
	 * @param processKey
	 * @param activityKey
	 * @param business_id
	 * @param business_type
	 * @param offset
	 * @param pagesize
	 * @return
	 */
	public List<Nodevariable> selectNodevariable(String processKey,
			String activityKey, String business_id, String business_type
			);
	
	/**
	 * 查询节点参数配置列表
	 * @param nodevariable
	 * @return
	 */
	public List<Nodevariable> queryNodevariable(Nodevariable nodevariable);
	
	/**
	 * 查询节点参数配置对象
	 * @param nodevariable
	 * @return
	 */
	public Nodevariable getNodevariableById(Nodevariable nodevariable);
	
	/**
	 * 根据组织机构ID查询组织机构名称
	 * @param orgId
	 * @return
	 */
	public String getOrgNameByOrgId(String orgId);
	
	/**
	 * 删除节点参数配置
	 * @param nodevariable
	 */
	public void deleteNodevariable(Nodevariable nodevariable) throws Exception;
	
	/**
	 * 查询流程节点信息列表
	 * @param processKey
	 * @param offset
	 * @param pagesize
	 * @return
	 */
	public List<ActivitiNodeCandidate> selectNodeInfo(String processKey);
	
	public List<ActivitiNodeCandidate> queryActivitiNodeInfo(String process_key);

	/**
	 * 读取参数配置文件
	 * @param paramFileInputStream
	 * @return
	 * @throws ActivitiConfigException
	 */
	String addNodeParams(InputStream paramFileInputStream,String key)
			throws ActivitiConfigException;
	
	/**
	 * 加载参数配置资源
	 * @param process_key
	 * @return
	 * @throws ActivitiConfigException
	 */
	List<Nodevariable> loadVariableResource(String process_key)
			throws ActivitiConfigException;

	void addNodeVariableFromResource(String resourceId, String businessId,
			String businessType);

	/**
	 * 批量保存参数配置
	 * @param nodevariableList
	 * @param business_id
	 * @param business_type
	 * @param process_key
	 * @return
	 */
	String saveNodevariable(List<Nodevariable> nodevariableList,
			String business_id, String business_type, String process_key);

	void addProBusinessType(String processKey, String businessTypeId)  throws ActivitiConfigException;

	String queryBusinessName(String process_key) throws ActivitiConfigException;
}