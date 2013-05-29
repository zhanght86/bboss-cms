package com.sany.workflow.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task; 
import org.apache.commons.lang.StringUtils;

import com.sany.workflow.entity.ProcessDef;
import com.sany.workflow.service.ProcessException;

/**
 * 流程操作核心类
 * 此核心类主要处理：流程通过、驳回、会签、转办、中止、挂起等核心操作
 * @author fudk
 *
 */
public class ActivitiServiceImpl {

	private com.frameworkset.common.poolman.ConfigSQLExecutor executor;

	//	private ActivitiConfigService activitiConfigService;
	private ProcessEngine processEngine;
	private RepositoryService repositoryService;// 获得activiti服务
	private RuntimeService runtimeService;// 用于管理运行时流程实例
	private TaskService taskService;// 用于管理运行时任务
	@SuppressWarnings("unused")
	private FormService formService;// 用于管理任务表单
	private HistoryService historyService;// 管理流程实例、任务实例等历史数据
	@SuppressWarnings("unused")
	private ManagementService managementService;// 用于管理定时任务
//	private IdentityService identityService;// 用于管理组织结构

	public ActivitiServiceImpl(String xmlPath) {
		processEngine = ProcessEngineConfiguration
				.createProcessEngineConfigurationFromResource(xmlPath)
				.buildProcessEngine();

		repositoryService = processEngine.getRepositoryService();
		runtimeService = processEngine.getRuntimeService();
//		identityService = processEngine.getIdentityService();
		taskService = processEngine.getTaskService();
		historyService = processEngine.getHistoryService();
	}

	/**
	 * * 启动流程
	 * 
	 * @param variableMap
	 *            自定义参数
	 * @param process_key
	 *            流程key
	 * @param initor
	 *            流程创建者
	 * @return
	 */
	public ProcessInstance startProcDef(String process_key,
			Map<String, Object> variableMap) {

		// 启动部署流程的最新版本
		ProcessDefinition processDefinition = repositoryService
				.createProcessDefinitionQuery()
				.processDefinitionKey(process_key).latestVersion()
				.singleResult();

		//		identityService.setAuthenticatedUserId(initor);

		ProcessInstance processInstance = runtimeService
				.startProcessInstanceById(processDefinition.getId(),
						variableMap);
		return processInstance;
	}

	/**
	 * 启动流程(带businessKey)
	 * 
	 * @param businessKey 业务key
	 * @param variableMap 自定义参数
	 * @param process_key 流程key
	 * @return
	 */
	public ProcessInstance startProcDef(String businessKey, String process_key,
			Map<String, Object> map) {
		runtimeService = processEngine.getRuntimeService();
		ProcessInstance processInstance = runtimeService
				.startProcessInstanceByKey(process_key, businessKey, map);
		return processInstance;
	}
	

	/**
	 * 根据当前task，查询可以驳回的任务节点
	 * 
	 * @param task
	 *            当前任务
	 */
	public List<ActivityImpl> findBackAvtivity(String taskId) throws Exception {
		List<ActivityImpl> relist = new ArrayList<ActivityImpl>();
		//获取当前任务task
		Task task = findTaskById(taskId);
		//获取本节点前的ActivityImpl
		List<ActivityImpl> activitylist = findTaskBeforeActivitiImpl(task.getTaskDefinitionKey(), task.getProcessDefinitionId());
		//获取历史审批节点
		List<HistoricActivityInstance> histaskList = findHistoricUserTask(task.getProcessInstanceId());
		//排除未流转的节点
		if(!activitylist.isEmpty()&&!histaskList.isEmpty()){
			for(ActivityImpl activityImpl : activitylist){
				for(HistoricActivityInstance histask : histaskList){
					if(activityImpl.getId().equals(histask.getActivityId())){
						relist.add(activityImpl);
						//						histaskList.remove(histask);
					}
				}
			}
		}
		return removeDuplicateWithOrder(relist);
	}

	/**
	 * 驳回流程
	 * 
	 * @param instanceId
	 *            当前流程实例ID
	 * @param toTaskKey
	 *            驳回目标节点ID
	 * @param taskKey
	 *            当前节点Key
	 * @param variables
	 *            流程存储参数
	 * @throws Exception
	 */
	public void rejectProcess(String instanceId, String toTaskKey,String taskKey,String userName,
			Map<String, Object> variables) throws Exception {
		if (StringUtils.isEmpty(toTaskKey)) {
			throw new Exception("驳回目标节点ID为空！");
		}
		if (StringUtils.isEmpty(instanceId)) {
			throw new Exception("流程实例ID为空！");
		}

		Boolean rejectflag = false;//驳回完成标志位
		List<Task> taskList = taskService.createTaskQuery().processInstanceId(instanceId).list();
		while (!rejectflag) {
			for (Task task : taskList) {
				turnTransition(task.getId(), toTaskKey,userName, variables);
			}
			taskList = taskService.createTaskQuery().processInstanceId(instanceId).list();
			//如果无任务或者当前处理任务节点已经跳转，则完成驳回
			if(taskList.isEmpty()||!taskKey.equals(taskList.get(0).getTaskDefinitionKey())){
				rejectflag = true;
			}
			
		}
	}

	/**
	 * 中止流程(特权人直接审批通过等)
	 * 
	 * @param taskId
	 */
	public void endProcess(String taskId,String userName) throws Exception {
		ActivityImpl endActivity = findActivitiImpl(taskId, "end");
		commitProcess(taskId,userName, null, endActivity.getId());
	}

	/**
	 * 转办流程
	 * 
	 * @param taskId
	 *            当前任务节点ID
	 * @param userCode
	 *            被转办人Code
	 */
	public void transferAssignee(String taskId, String userCode) {
		taskService.setAssignee(taskId, userCode);
	}

	/**
	 * 部署流程
	 * 
	 * @return
	 */
	public Deployment deployProcDefByPath(String deploymentName,
			String xmlPath, String jpgPath) {

		Deployment deploy = repositoryService.createDeployment()
				.name(deploymentName).addClasspathResource(xmlPath).deploy();
		// .addClasspathResource(jpgPath).deploy();

		return deploy;
	}


	/**
	 * ***********************************start*******************************************
	 * **********************以下为根据 任务节点ID 获取流程各对象查询方法********************
	 * ***********************************************************************************
	 */
	
	/**
	 * 通过proDefId获得流程定义实体
	 * 
	 * @param proDefId
	 * @return
	 */
	public ProcessDefinitionEntity getProcessDefinitionEntityByProDefId(String proDefId) {
		ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition(proDefId);
		return def;
	}
	/**
	 * 根据任务ID获取流程定义
	 * 
	 * @param taskId
	 *            任务ID
	 * @return
	 * @throws Exception
	 */
	private ProcessDefinitionEntity findProcessDefinitionEntityByTaskId(
			String taskId) throws Exception {
		// 取得流程定义
		ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition(findTaskById(taskId)
						.getProcessDefinitionId());

		if (processDefinition == null) {
			throw new Exception("流程定义未找到!");
		}

		return processDefinition;
	}
	/**
	 * 通过processKey获得流程的所有节点
	 * 
	 * @param processKey
	 * @return
	 */
	public List<ActivityImpl> getActivitImplListByProcessKey(String processKey) {
		ProcessDefinition processDefinition = repositoryService
				.createProcessDefinitionQuery()
				.processDefinitionKey(processKey).latestVersion()
				.singleResult();// 最近版本的流程实例
		ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition(processDefinition.getId());

		List<ActivityImpl> activitiList = def.getActivities();// 获得当前任务的所有节点
		return activitiList;
	}
	/**
	 * 根据任务ID和流程定义ID获取活动节点 
	 * @param taskId
	 *            任务ID
	 * @param proDefId
	 *            流程定义ID 
	 * @return
	 * @throws Exception
	 */
	public List<ActivityImpl> findTaskBeforeActivitiImpl(String taskId, String proDefId)
			throws Exception {
		// 取得流程定义
		ProcessDefinitionEntity processDefinition = getProcessDefinitionEntityByProDefId(proDefId);

		List<ActivityImpl> relist = new ArrayList<ActivityImpl>();
		// 根据流程定义，获取该流程实例的结束节点
		for (ActivityImpl activityImpl : processDefinition.getActivities()) {
			if (taskId.equals(activityImpl.getId())) {
				break;
			}
			if("userTask".equals(activityImpl.getProperty("type"))){
				relist.add(activityImpl);
			}

		}

		return relist;
	}
	/**
	 * 根据任务ID和节点ID获取活动节点 
	 * @param taskId
	 *            任务ID
	 * @param activityId
	 *            活动节点ID 
	 *            如果为null或""，则默认查询当前活动节点 
	 *            如果为"end"，则查询结束节点
	 * 
	 * @return
	 * @throws Exception
	 */
	private ActivityImpl findActivitiImpl(String taskId, String activityId)
			throws Exception {
		// 取得流程定义
		ProcessDefinitionEntity processDefinition = findProcessDefinitionEntityByTaskId(taskId);

		// 获取当前活动节点ID
		if (StringUtils.isEmpty(activityId)) {
			activityId = findTaskById(taskId).getTaskDefinitionKey();
		}

		// 根据流程定义，获取该流程实例的结束节点
		if ("END".equals(activityId.toUpperCase())) {
			for (ActivityImpl activityImpl : processDefinition.getActivities()) {
				List<PvmTransition> pvmTransitionList = activityImpl
						.getOutgoingTransitions();
				if (pvmTransitionList.isEmpty()) {
					return activityImpl;
				}
			}
		}

		// 根据节点ID，获取对应的活动节点
		ActivityImpl activityImpl = ((ProcessDefinitionImpl) processDefinition)
				.findActivity(activityId);

		return activityImpl;
	}
	/**
	 * 通过部署ID获取流程定义
	 * 
	 * @return
	 */
	public ProcessDef getProcessDefByDeploymentId(String DeploymentId) {
		final ProcessDefinition processDefinition = processEngine
				.getRepositoryService().createProcessDefinitionQuery()
				.deploymentId(DeploymentId).singleResult();
		return convert(new ArrayList<ProcessDefinition>() {
			{
				add(processDefinition);
			}
		}).get(0);
	}

	/**
	 * 根据instanceId获得流程实例
	 * @param processInstanceId	流程实例ID
	 * @return
	 */
	public ProcessInstance getProcessInstanceById(String processInstanceId) {
		runtimeService = processEngine.getRuntimeService();
		ProcessInstance processInstance = runtimeService
				.createProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();
		return processInstance;
	}
	/**
	 * 根据任务ID获取对应的流程实例
	 * @param taskId
	 *            任务ID
	 * @return
	 * @throws Exception
	 */
	private ProcessInstance findProcessInstanceByTaskId(String taskId)
			throws Exception {
		// 找到流程实例
		ProcessInstance processInstance = runtimeService
				.createProcessInstanceQuery().processInstanceId(
						findTaskById(taskId).getProcessInstanceId())
						.singleResult();
		if (processInstance == null) {
			throw new Exception("流程实例未找到!");
		}
		return processInstance;
	}
	/**
	 * 根据任务ID获得任务实例
	 * 
	 * @param taskId
	 *            任务ID
	 * @return
	 * @throws Exception
	 */
	private TaskEntity findTaskById(String taskId) throws Exception {
		TaskEntity task = (TaskEntity) taskService.createTaskQuery().taskId(
				taskId).singleResult();
		if (task == null) {
			throw new Exception("任务实例未找到!");
		}
		return task;
	}
	
	/**
	 * 根据流程实例ID和任务key值查询所有同级任务集合
	 * 
	 * @param processInstanceId
	 * @param key
	 * @return
	 */
	private List<Task> findTaskListByKey(String processInstanceId, String taskKey) {
		return taskService.createTaskQuery().processInstanceId(
				processInstanceId).taskDefinitionKey(taskKey).list();
	}

	/**
	 * 查询指定任务节点的最新记录
	 * 
	 * @param processInstance
	 *            流程实例
	 * @param activityId
	 * @return
	 */
	public List<HistoricActivityInstance> findHistoricUserTask(
			String instanceId) {
		// 查询当前流程实例审批结束的历史节点
		List<HistoricActivityInstance> historicActivityInstances = historyService
				.createHistoricActivityInstanceQuery().activityType("userTask")
				.processInstanceId(instanceId).finished()
				.orderByHistoricActivityInstanceEndTime().desc().list();
		return historicActivityInstances;
	}
	/**
	 * ***********************************end*******************************************
	 * **********************以上为根据 任务节点ID 获取流程各对象查询方法********************
	 * ***********************************************************************************
	 */


	/**
	 * ************************************start***********************************************
	 * ********************************以下为流程转向操作核心逻辑*******************
	 * **********************************************************************************
	 */

	/**
	 * 提交流程，目标任务ID toTaskKey为空，就完成任务，否则跳转到toTaskKey任务节点
	 * @param taskId
	 *            当前任务ID
	 * @param variables
	 *            流程变量
	 * @param toTaskKey
	 *            流程转向执行任务节点ID
	 *            此参数为空，默认为提交操作
	 * @throws Exception
	 */
	public void commitProcess(String taskId,String userName, Map<String, Object> variables,
			String toTaskKey) throws Exception {
		if (variables == null) {
			variables = new HashMap<String, Object>();
		}
		// 跳转节点为空，默认提交操作
		if (StringUtils.isEmpty(toTaskKey)) {
			completeTask(taskId, userName, variables);
//			taskService.complete(taskId, variables);
		} else {// 流程转向操作
//			turnTransition(taskId, toTaskKey,userName, variables);
			completeTask(taskId, userName, variables,toTaskKey);
		}
	}
	
	/**
	 * 完成任务(先领用再完成)
	 * 
	 * @param taskId
	 * @param map
	 * @throws Exception 
	 */
	public void completeTask(String taskId, String username,
			Map<String, Object> variables) throws Exception {
		if(!StringUtils.isEmpty(username)){
			taskService.claim(taskId, username);
		}
		taskService.complete(taskId, variables);
	}
	
	/**
	 * 完成任务(先领用再完成)
	 * 
	 * @param taskId
	 * @param map
	 * @throws Exception 
	 */
	public void completeTask(String taskId, String username,
			Map<String, Object> variables,String toTaskKey) throws Exception {
		if(!StringUtils.isEmpty(username)){
			taskService.claim(taskId, username);
		}
		taskService.complete(taskId, variables,toTaskKey);
//		taskService.complete(taskId, variables);
	}
	/**
	 * 流程转向操作
	 * 
	 * @param taskId
	 *            当前任务ID
	 * @param toTaskKey
	 *            目标节点任务ID
	 * @param variables
	 *            流程变量
	 * @throws Exception
	 */
	private void turnTransition(String taskId, String toTaskKey,String userName,
			Map<String, Object> variables) throws Exception {
		// 当前节点
		ActivityImpl currActivity = findActivitiImpl(taskId, null);
		// 清空当前流向
		List<PvmTransition> oriPvmTransitionList = clearTransition(currActivity);

		// 创建新流向
		TransitionImpl newTransition = currActivity.createOutgoingTransition();
		// 目标节点
		ActivityImpl pointActivity = findActivitiImpl(taskId, toTaskKey);
		// 设置新流向的目标节点
		newTransition.setDestination(pointActivity);

		// 执行转向任务
//		taskService.complete(taskId, variables);
		completeTask(taskId, userName, variables);
		// 删除目标节点新流入
		pointActivity.getIncomingTransitions().remove(newTransition);

		// 还原以前流向
		restoreTransition(currActivity, oriPvmTransitionList);
	}

	/**
	 * 清空指定活动节点流向
	 * 
	 * @param activityImpl
	 *            活动节点
	 * @return 节点流向集合
	 */
	private List<PvmTransition> clearTransition(ActivityImpl activityImpl) {
		// 存储当前节点所有流向临时变量
		List<PvmTransition> oriPvmTransitionList = new ArrayList<PvmTransition>();
		// 获取当前节点所有流向，存储到临时变量，然后清空
		List<PvmTransition> pvmTransitionList = activityImpl
				.getOutgoingTransitions();
		for (PvmTransition pvmTransition : pvmTransitionList) {
			oriPvmTransitionList.add(pvmTransition);
		}
		pvmTransitionList.clear();

		return oriPvmTransitionList;
	}

	/**
	 * 还原指定活动节点流向
	 * 
	 * @param activityImpl
	 *            活动节点
	 * @param oriPvmTransitionList
	 *            原有节点流向集合
	 */
	private void restoreTransition(ActivityImpl activityImpl,
			List<PvmTransition> oriPvmTransitionList) {
		// 清空现有流向
		List<PvmTransition> pvmTransitionList = activityImpl
				.getOutgoingTransitions();
		pvmTransitionList.clear();
		// 还原以前流向
		for (PvmTransition pvmTransition : oriPvmTransitionList) {
			pvmTransitionList.add(pvmTransition);
		}
	}


	/**
	 * *******************************end*********************************************
	 * ****************************以上为流程转向操作核心逻辑***************************
	 * *******************************************************************************
	 */


	/**
	 * *****************************start***********************************************
	 * **********************以下为activiti 核心service*********************
	 * *********************************************************************************
	 */
	/**
	 * 获得activiti服务
	 * 
	 * @return activiti服务
	 */
	public RepositoryService getRepositoryService() {
		return processEngine.getRepositoryService();
	}
	/**
	 * 获得管理运行时流程实例服务
	 * 
	 * @return
	 */
	public RuntimeService getRuntimeService() {
		return processEngine.getRuntimeService();
	}
	/**
	 * 获得管理运行时任务服务;
	 * 
	 * @return
	 */
	public TaskService getTaskService() {
		return processEngine.getTaskService();
	}
	/**
	 * 获得管理流程实例、任务实例等历史数据服务
	 * 
	 * @return
	 */
	public HistoryService getHistoryService() {
		return processEngine.getHistoryService();
	}
	/**
	 * *****************************end***********************************************
	 * **********************以上为activiti 核心service*********************
	 * *********************************************************************************
	 */

	/**
	 * 类型转换
	 * @param defs
	 * @return
	 */
	private List<ProcessDef> convert(List<ProcessDefinition> defs) {
		List<ProcessDef> datas = new ArrayList<ProcessDef>(defs.size());
		for (int i = 0; defs != null && i < defs.size(); i++) {
			ProcessDef def = new ProcessDef();
			ProcessDefinition def_ = defs.get(i);
			def.setCATEGORY_(def_.getCategory());
			def.setDEPLOYMENT_ID_(def_.getDeploymentId());
			def.setDGRM_RESOURCE_NAME_(def_.getDiagramResourceName());
			def.setHAS_START_FORM_KEY_(def_.hasStartFormKey() ? 1 : 0);
			def.setID_(def_.getId());
			def.setKEY_(def_.getKey());
			def.setNAME_(def_.getName());
			def.setRESOURCE_NAME_(def_.getResourceName());
			// def.setREV_(def_.)
			def.setSUSPENSION_STATE_(def_.isSuspended() ? 1 : 0);
			def.setVERSION_(def_.getVersion());

			Deployment deployment = processEngine.getRepositoryService()
					.createDeploymentQuery()
					.deploymentId(def_.getDeploymentId()).singleResult();
			def.setDEPLOYMENT_NAME_(deployment.getName());
			def.setDEPLOYMENT_TIME_(deployment.getDeploymentTime());

			datas.add(def);
		}
		return datas;

	}

	/**
	 * list去重，并保持原list顺序
	 * @param list
	 * @return
	 */
	public List removeDuplicateWithOrder(List list) {
		Set set = new HashSet();
		List newList = new ArrayList();
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			Object element = iter.next();
			if (set.add(element))
				newList.add(element);
		} 
		return newList;
	}
	
	/**
	 * *****************************start***********************************************
	 * **********************以下为业务处理方法*********************
	 * *********************************************************************************
	 */
	
	/**
	 * 获取用户特定状态下的任务待办数
	 */
	public int countTasksByUser(String processkey,String businessType,String userAccount)
	{
		
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("userAccount", userAccount);
			map.put("businessType", businessType);
			map.put("processkey", processkey);
			
			return  executor.queryObjectBean(int.class, "countlistTaskAndVarsByUser", map);
		} catch (Exception e) {
	
			throw new ProcessException("获取待办任务数失败：processkey="+processkey+",businessType="+businessType
					+",userAccount="+userAccount,e);
		}
	}

}
