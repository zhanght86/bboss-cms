package com.sany.activiti.demo.service;

import java.util.List;

import com.sany.activiti.demo.pojo.ActivitiNode;
import com.sany.activiti.demo.pojo.CandidateGroup;
import com.sany.activiti.demo.pojo.CandidateUser;
import com.sany.activiti.demo.pojo.MaterielTest;
import com.sany.activiti.demo.pojo.TaskInfo;

public interface ActivitiTestService {
	
	/**
	 * 申请物料
	 * @param materiel
	 */
	public void applyMateriel(MaterielTest materiel);
	
	/**
	 * 根据流程ID查询物料申请单
	 * @param processInstanceId
	 * @return
	 */
	public MaterielTest queryMaterielTestByProcessId(String processInstanceId);
	
	public void insertTaskInfo(TaskInfo taskInfo);
	
	public List<TaskInfo> queryTaskInfoByProcessId(String processInstanceId);
	
	/**
	 * 查询用户的申请单
	 * @param username
	 * @return
	 */
	public List<MaterielTest> queryMaterielListByUser(String username);
	
	/**
	 * 把流程的节点读出来放入节点表
	 * @param processKey
	 */
	public void addActivitiNode(String processKey);
	
	/**
	 * 查询所有节点
	 * @param processKey
	 * @return
	 */
	public List<ActivitiNode> showNodeList(String processKey);
	
	
	/**
	 * 查询节点配置信息
	 * @param id 节点配置ID
	 * @return
	 */
	public ActivitiNode getActivitiNodeById(String id);
	
	/**
	 * 修改节点配置
	 * @param node
	 */
	public void updateActivitiNode(ActivitiNode node);
	
	/**
	 * 根据key和nodeId查询节点配置
	 * @param key
	 * @param nodeId
	 * @return
	 */
	public ActivitiNode getActivitiNodeByKeyAndNodeId(String key,String nodeId);
	
	/**
	 * 查询节点配置信息
	 * @param processKey 流程KEY
	 * @param taskKey 任务key
	 * @return
	 */
	public ActivitiNode getActivitiNodeByKeys(String processKey,String taskKey);
	
	/**
	 * 根据一组用户名查询CandidateUser
	 * @param usernames 
	 * @return
	 */
	public List<CandidateUser> getUserInfoByNames(String usernames);
	
	/**
	 * 根据一组用户名查询CandidateGroup
	 * @param groups 用户名以","隔开
	 * @return
	 */
	public List<CandidateGroup> getGroupInfoByNames(String groups);
	
	public void setExecutor(
			com.frameworkset.common.poolman.ConfigSQLExecutor executor);
}
