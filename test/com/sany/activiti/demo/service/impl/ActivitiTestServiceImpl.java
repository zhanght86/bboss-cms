package com.sany.activiti.demo.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.RollbackException;

import org.activiti.engine.impl.pvm.process.ActivityImpl;

import com.frameworkset.orm.transaction.TransactionManager;
import com.sany.activiti.demo.pojo.ActivitiNode;
import com.sany.activiti.demo.pojo.CandidateGroup;
import com.sany.activiti.demo.pojo.CandidateUser;
import com.sany.activiti.demo.pojo.MaterielTest;
import com.sany.activiti.demo.pojo.TaskInfo;
import com.sany.activiti.demo.service.ActivitiTestService;
import com.sany.workflow.service.ActivitiService;

public class ActivitiTestServiceImpl implements ActivitiTestService {

	private com.frameworkset.common.poolman.ConfigSQLExecutor executor;

	private ActivitiService activitiService;

	public void applyMateriel(MaterielTest materiel) {
		try {
			if (materiel.getId() != null && !materiel.getId().isEmpty()) {
				executor.updateBean("updateMateriel", materiel);
			} else {
				String uuid = java.util.UUID.randomUUID().toString();
				materiel.setId(uuid);
				executor.insertBean("applyMateriel", materiel);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<MaterielTest> queryMaterielListByUser(String username) {
		try {
			MaterielTest materielTest = new MaterielTest();
			materielTest.setApply_name(username);
			List<MaterielTest> list = executor.queryListBean(
					MaterielTest.class, "selectMaterielByApplyUser",
					materielTest);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public MaterielTest queryMaterielTestByProcessId(String processInstanceId) {
		try {
			MaterielTest materielTest = new MaterielTest();
			materielTest.setProcess_instance_id(processInstanceId);
			List<MaterielTest> list = executor.queryListBean(
					MaterielTest.class, "selectByProcessId", materielTest);
			if (list != null && list.size() > 0) {
				return list.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<TaskInfo> queryTaskInfoByProcessId(String processInstanceId) {
		try {
			TaskInfo taskInfo = new TaskInfo();
			taskInfo.setProcess_instance_id(Long.parseLong(processInstanceId));
			List<TaskInfo> list = executor.queryListBean(TaskInfo.class,
					"selectTaskInfoByProcessId", taskInfo);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void insertTaskInfo(TaskInfo taskInfo) {
		try {
			String uuid = java.util.UUID.randomUUID().toString();
			taskInfo.setId(uuid);
			executor.insertBean("insertTaskInfo", taskInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addActivitiNode(String processKey) {
		TransactionManager tm = new TransactionManager();
		ActivitiNode aNode = new ActivitiNode();
		aNode.setProcess_key(processKey);
		try {
			tm.begin();
			executor.deleteBean("deleteActivitiNodeByKey", aNode);
			List<ActivityImpl> aList = activitiService
					.getActivitImplListByProcessKey(processKey);
			for (ActivityImpl actImpl : aList) {
				if(actImpl.getProperty("type").toString().equals("userTask")){
					aNode.setId(java.util.UUID.randomUUID().toString());
					aNode.setNode_id(actImpl.getId());
					aNode.setNode_name(actImpl.getProperty("name").toString());
					executor.insertBean("insertActivitiNode", aNode);
				}
			}
			tm.commit();
		} catch (Exception e) {
			try {
				e.printStackTrace();
				tm.rollback();
			} catch (RollbackException e1) {
				e1.printStackTrace();
			}
		}

	}

	/**
	 * 查询所有节点
	 * @param processKey
	 * @return
	 */
	public List<ActivitiNode> showNodeList(String processKey) {
		try {
			ActivitiNode node = new ActivitiNode();
			node.setProcess_key(processKey);
			List<ActivitiNode> nodeList = executor.queryListBean(
					ActivitiNode.class, "selectActivitiNodeByKey", node);
			return nodeList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 查询节点配置信息
	 * @param id 节点配置ID
	 * @return
	 */
	public ActivitiNode getActivitiNodeById(String id){
		try{
			ActivitiNode node = new ActivitiNode();
			node.setId(id);
			List<ActivitiNode> list = executor.queryListBean(
					ActivitiNode.class, "selectActivitiNodeById", node);
			if(list!=null&&list.size()>0){
				return list.get(0);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 查询节点配置信息
	 * @param processKey 流程KEY
	 * @param taskKey 任务key
	 * @return
	 */
	public ActivitiNode getActivitiNodeByKeys(String processKey,String taskKey){
		try{
			ActivitiNode node = new ActivitiNode();
			node.setProcess_key(processKey);
			node.setNode_id(taskKey);
			List<ActivitiNode> list = executor.queryListBean(
					ActivitiNode.class, "selectActivitiNodeByKeys", node);
			if(list!=null&&list.size()>0){
				return list.get(0);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 修改节点配置
	 * @param node
	 */
	public void updateActivitiNode(ActivitiNode node){
		try{
			executor.updateBean("updateActivitiNode", node);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据key和nodeId查询节点配置
	 * @param key
	 * @param nodeId
	 * @return
	 */
	public ActivitiNode getActivitiNodeByKeyAndNodeId(String key,String nodeId){
		try{
			ActivitiNode node = new ActivitiNode();
			node.setProcess_key(key);
			node.setNode_id(nodeId);
			List<ActivitiNode> list = executor.queryListBean(ActivitiNode.class, "selectActivitiNodeByKeyandNodeId", node);
			if(list!=null&&list.size()>0){
				return list.get(0);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 根据一组用户名查询CandidateUser
	 * @param usernames 用户名以","隔开
	 * @return
	 */
	public List<CandidateUser> getUserInfoByNames(String usernames) {
		try {
			String[] names = usernames.split(",");
			List<CandidateUser> list = null;
			if (names.length > 0) {
				Map<String, String[]> usernames_ = new HashMap<String, String[]>();
				usernames_.put("usernames", names);
				list = executor.queryListBean(CandidateUser.class,
						"selectUserInfoByNames", usernames_);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 根据一组用户名查询CandidateGroup
	 * @param groups 用户名以","隔开
	 * @return
	 */
	public List<CandidateGroup> getGroupInfoByNames(String groups) {
		try {
			String[] groupnames = groups.split(",");
			List<CandidateGroup> list = null;
			if (groupnames.length > 0) {
				Map<String, String[]> groups_ = new HashMap<String, String[]>();
				groups_.put("groups", groupnames);
				list = executor.queryListBean(CandidateGroup.class,
						"selectGroupInfoByNames", groups_);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 注入使用
	 * 
	 * @return
	 */
	public com.frameworkset.common.poolman.ConfigSQLExecutor getExecutor() {
		return executor;
	}

	public void setExecutor(
			com.frameworkset.common.poolman.ConfigSQLExecutor executor) {
		this.executor = executor;
	}
}
