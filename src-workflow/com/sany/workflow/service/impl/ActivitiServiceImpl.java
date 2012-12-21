package com.sany.workflow.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.frameworkset.util.CollectionUtils;

import com.frameworkset.util.ListInfo;
import com.sany.workflow.entity.ActivitiNodeCandidate;
import com.sany.workflow.entity.Nodevariable;
import com.sany.workflow.entity.ProcessDef;
import com.sany.workflow.entity.ProcessDefCondition;
import com.sany.workflow.service.ActivitiConfigService;
import com.sany.workflow.service.ActivitiService;
import com.sany.workflow.service.ProcessException;
import com.sany.workflow.util.WorkFlowConstant;

public class ActivitiServiceImpl implements ActivitiService {

	private com.frameworkset.common.poolman.ConfigSQLExecutor executor;

	private ActivitiConfigService activitiConfigService;

	private ProcessEngine processEngine;
	private RepositoryService repositoryService;// 获得activiti服务
	private RuntimeService runtimeService;// 用于管理运行时流程实例
	private TaskService taskService;// 用于管理运行时任务
	@SuppressWarnings("unused")
	private FormService formService;// 用于管理任务表单
	private HistoryService historyService;// 管理流程实例、任务实例等历史数据
	@SuppressWarnings("unused")
	private ManagementService managementService;// 用于管理定时任务
	private IdentityService identityService;// 用于管理组织结构

	public ActivitiServiceImpl(String xmlPath) {
		processEngine = ProcessEngineConfiguration
				.createProcessEngineConfigurationFromResource(xmlPath)
				.buildProcessEngine();
		
	}

	/**
	 * 获得activiti引擎
	 * @return
	 */
	@Override
	public ProcessEngine getProcessEngine(){
		return processEngine;
	}
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
	 * 获得表单服务
	 * 
	 * @return
	 */
	public FormService getFormService() {
		return processEngine.getFormService();
	}

	/**
	 * 获得用于管理定时任务服务
	 * 
	 * @return
	 */
	public ManagementService getManagementService() {
		return processEngine.getManagementService();
	}

	/**
	 * 获得用于管理组织结构服务
	 * 
	 * @return
	 */
	public IdentityService getIdentityService() {
		return processEngine.getIdentityService();
	}

	/**
	 * 为流程任务设置参数
	 * 
	 * @param taskId
	 *            任务ID
	 * @param map
	 *            参数MAP
	 */
	public void setVariables(String taskId, Map<String, Object> map) {
		processEngine.getTaskService().setVariables(taskId, map);
	}

	/**
	 * 修改任务参数
	 * 
	 * @param taskId
	 *            任务ID
	 * @param variable
	 *            参数名
	 * @param object
	 *            值
	 */
	public void setVariable(String taskId, String variable, Object object) {
		processEngine.getTaskService().setVariable(taskId, variable, object);
	}

	/**
	 * 获得任务参数
	 * 
	 * @param taskId
	 *            任务ID
	 * @param key
	 *            参数key
	 * @return
	 */
	public Object getVariable(String taskId, String key) {
		return processEngine.getTaskService().getVariable(taskId, key);
	}

	/**
	 * 根据流程定义KEY获取任务
	 * 
	 * @param definitionKey
	 *            流程定义key
	 * @return
	 */
	public List<Task> getActList(String definitionKey) {
		return processEngine.getTaskService().createTaskQuery()
				.taskDefinitionKey(definitionKey).list();
	}

	/**
	 * 获取activiti流程定义的历史版本
	 * 
	 * @return
	 */
	public List<ProcessDefinition> activitiListByprocesskey(String process_key) {

		repositoryService = processEngine.getRepositoryService();
		List<ProcessDefinition> procDefList = repositoryService
				.createProcessDefinitionQuery()
				.processDefinitionKey(process_key)
				.orderByProcessDefinitionVersion().desc().list();

		return procDefList;
	}

	/**
	 * 流程实例列表
	 * 
	 * @return
	 */
	public List<ProcessInstance> listProcInstByPdfid(String pdfid) {
		runtimeService = processEngine.getRuntimeService();
		List<ProcessInstance> procInstList = runtimeService
				.createProcessInstanceQuery().processDefinitionId(pdfid).list();

		return procInstList;
	}

	/**
	 * 当前任务的执行情况
	 * 
	 * @return
	 */
	public List<Execution> listExecutionByProId(String processInstanceId) {
		runtimeService = processEngine.getRuntimeService();
		List<Execution> exectionList = runtimeService.createExecutionQuery()
				.processInstanceId(processInstanceId).list();
		return exectionList;
	}

	/**
	 * 完成任务(普通)
	 * 
	 * @param taskId
	 * @param map
	 */
	public void completeTask(String taskId, Map<String, Object> map) {
		taskService = processEngine.getTaskService();
		taskService.complete(taskId, map);
	}

	/**
	 * 完成任务(加载组织机构节点参数配置)
	 * 
	 * @param taskId
	 *            任务ID
	 * @param orgId
	 *            组织机构ID
	 */
	public void completeTaskLoadOrgParams(String taskId, String orgId) {
		completeTaskLoadOrgParams(taskId, null, orgId);
	}

	/**
	 * 完成任务(加载组织机构节点参数配置)
	 * 
	 * @param taskId
	 *            任务ID
	 * @param map
	 *            自定义参数MAP
	 * @param orgId
	 *            组织机构ID
	 */
	public void completeTaskLoadOrgParams(String taskId,
			Map<String, Object> map, String orgId) {
		Task task = this.getTaskById(taskId);
		Map<String, Object> paramsMap = new HashMap<String, Object>();

		Nodevariable nodevariable = new Nodevariable();
		nodevariable.setNode_key(task.getName());
		nodevariable.setBusiness_id(orgId);
		nodevariable.setBusiness_type(WorkFlowConstant.BUSINESS_TYPE_ORG);

		List<Nodevariable> nodevariableList = activitiConfigService
				.queryNodevariable(nodevariable);
		for (Nodevariable item : nodevariableList) {
			paramsMap.put(item.getParam_name(), item.getParam_value());
		}
		if (map != null && map.size() > 0) {
			paramsMap.putAll(map);
		}
		taskService = processEngine.getTaskService();
		taskService.complete(taskId, paramsMap);
	}

	/**
	 * 完成任务(加载业务类型节点参数配置)
	 * 
	 * @param taskId
	 *            任务ID
	 * @param bussinesstypeId
	 *            业务类型ID
	 */
	public void completeTaskLoadBussinesstypeParams(String taskId,
			String bussinesstypeId) {
		completeTaskLoadBussinesstypeParams(taskId, null, bussinesstypeId);
	}

	/**
	 * 完成任务(加载业务类型节点参数配置)
	 * 
	 * @param taskId
	 *            任务ID
	 * @param map
	 *            自定义参数MAP
	 * @param bussinesstypeId
	 *            业务类型ID
	 */
	public void completeTaskLoadBussinesstypeParams(String taskId,
			Map<String, Object> map, String bussinesstypeId) {
		Task task = this.getTaskById(taskId);
		Map<String, Object> paramsMap = new HashMap<String, Object>();

		Nodevariable nodevariable = new Nodevariable();
		nodevariable.setNode_key(task.getName());
		nodevariable.setBusiness_id(bussinesstypeId);
		nodevariable
				.setBusiness_type(WorkFlowConstant.BUSINESS_TYPE_BUSINESSTYPE);

		List<Nodevariable> nodevariableList = activitiConfigService
				.queryNodevariable(nodevariable);
		for (Nodevariable item : nodevariableList) {
			paramsMap.put(item.getParam_name(), item.getParam_value());
		}
		if (map != null && map.size() > 0) {
			paramsMap.putAll(map);
		}
		taskService = processEngine.getTaskService();
		taskService.complete(taskId, paramsMap);
	}

	/**
	 * 完成任务(加载通用节点参数配置)
	 * 
	 * @param taskId
	 *            任务ID
	 * @param map
	 *            自定义参数MAP
	 */
	public void completeTaskLoadCommonParams(String taskId) {
		completeTaskLoadCommonParams(taskId, null);
	}

	/**
	 * 完成任务(加载通用节点参数配置)
	 * 
	 * @param taskId
	 *            任务ID
	 * @param map
	 *            自定义参数MAP
	 */
	public void completeTaskLoadCommonParams(String taskId,
			Map<String, Object> map) {
		Task task = this.getTaskById(taskId);
		Map<String, Object> paramsMap = new HashMap<String, Object>();

		Nodevariable nodevariable = new Nodevariable();
		nodevariable.setNode_key(task.getName());
		nodevariable.setBusiness_id("");
		nodevariable.setBusiness_type(WorkFlowConstant.BUSINESS_TYPE_COMMON);

		List<Nodevariable> nodevariableList = activitiConfigService
				.queryNodevariable(nodevariable);
		for (Nodevariable item : nodevariableList) {
			paramsMap.put(item.getParam_name(), item.getParam_value());
		}
		if (map != null && map.size() > 0) {
			paramsMap.putAll(map);
		}
		taskService = processEngine.getTaskService();
		taskService.complete(taskId, paramsMap);
	}

	/**
	 * 处理任务(加载配置好的参数)
	 * 
	 * @param taskId
	 *            任务ID
	 * @param business_id
	 *            业务配置ID
	 * @param business_type
	 *            业务配置类型
	 */
	public void completeTaskLoadParams(String taskId, String business_id,
			String business_type) {
		completeTaskLoadParams(taskId, null, business_id, business_type);
	}

	/**
	 * 处理任务(加载配置好的参数)
	 * 
	 * @param taskId
	 *            任务ID
	 * @param map
	 *            自定义参数
	 * @param business_id
	 *            业务配置ID
	 * @param business_type
	 *            业务配置类型
	 */
	public void completeTaskLoadParams(String taskId, Map<String, Object> map,
			String business_id, String business_type) {
		Task task = this.getTaskById(taskId);
		Map<String, Object> paramsMap = new HashMap<String, Object>();

		Nodevariable nodevariable = new Nodevariable();
		nodevariable.setNode_key(task.getName());
		nodevariable.setBusiness_id(business_id);
		nodevariable.setBusiness_type(business_type);

		List<Nodevariable> nodevariableList = activitiConfigService
				.queryNodevariable(nodevariable);
		for (Nodevariable item : nodevariableList) {
			paramsMap.put(item.getParam_name(), item.getParam_value());
		}
		if (map != null && map.size() > 0) {
			paramsMap.putAll(map);
		}
		taskService = processEngine.getTaskService();
		taskService.complete(taskId, paramsMap);
	}

	/**
	 * 完成任务(先领用再完成)
	 * 
	 * @param taskId
	 * @param map
	 */
	public void completeTask(String taskId, String username,
			Map<String, Object> map) {
		taskService = processEngine.getTaskService();
		taskService.claim(taskId, username);
		taskService.complete(taskId, map);
	}
	
	/**
	 * 完成任务(先领用再完成)
	 * 
	 * @param taskId
	 * @param map
	 */
	public void completeTaskWithLocalVariables(String taskId, String username,
			Map<String, Object> map) {
		taskService = processEngine.getTaskService();
		taskService.claim(taskId, username);
		taskService.setVariablesLocal(taskId, map);
		taskService.complete(taskId);
	}

	/**
	 * 获得历史任务实例by流程实例ID
	 * 
	 * @param processId
	 *            流程实例ID
	 * @return
	 */
	public List<HistoricTaskInstance> getHisTaskByProcessId(String processId) {
		historyService = processEngine.getHistoryService();
		List<HistoricTaskInstance> historyList = historyService
				.createHistoricTaskInstanceQuery().processInstanceId(processId)
				.list();
		return historyList;

	}

	/**
	 * 获得历史任务实例 by用户名
	 * 
	 * @param username
	 * @return
	 */
	public List<HistoricTaskInstance> getHisTaskByUsername(String username) {
		List<HistoricTaskInstance> historyTaskList = new ArrayList<HistoricTaskInstance>();
		try{
			Map<String,Object> map  = new HashMap<String,Object>();
			map.put("username", username);
			List<HistoricTaskInstanceEntity> list = executor.queryListBean(HistoricTaskInstanceEntity.class, "queryHisTask",map);
			historyTaskList.addAll(list);
		}catch(Exception e){
			e.printStackTrace();
		}
		return historyTaskList;
	}
	
	/**
	 * 获得历史任务实例 by用户名
	 * 
	 * @param username
	 * @return
	 */
	@Override
	public ListInfo getHisTaskByUsername(String username,long offset,int pagesize) {
		ListInfo listInfo = null;
		try{
			Map<String,Object> map  = new HashMap<String,Object>();
			map.put("username", username);
			listInfo = executor.queryListInfoBean(HistoricTaskInstanceEntity.class, "queryHisTask", offset, pagesize, map);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return listInfo;
	}
	
	/**
	 * 获得历史任务实例 by用户名和流程KEY
	 * 
	 * @param username
	 * @return
	 */
	public List<HistoricTaskInstance> getHisTaskByUsernameProcessKey(String username,String processKey) {
		List<HistoricTaskInstance> historyTaskList = new ArrayList<HistoricTaskInstance>();
		try{
			Map<String,Object> map  = new HashMap<String,Object>();
			map.put("username", username);
			String[] processKeys = new String[]{processKey};
			map.put("processKeys", processKeys);
			List<HistoricTaskInstanceEntity> list = executor.queryListBean(HistoricTaskInstanceEntity.class, "queryHisTask",map);
			historyTaskList.addAll(list);
		}catch(Exception e){
			e.printStackTrace();
		}
		return historyTaskList;
	}
	
	/**
	 * 获得历史任务实例 by用户名和流程KEY数组
	 * 分页列表
	 * 
	 * @param username
	 * @return
	 */
	@Override
	public ListInfo getHisTaskByUsernameProcessKey(String username,String processKey,int offset,int pagesize) {
		ListInfo listInfo = null;
		try{
			Map<String,Object> map  = new HashMap<String,Object>();
			map.put("username", username);
			String[] processKeys = new String[]{processKey};
			map.put("processKeys", processKeys);
			listInfo = executor.queryListInfoBean(HistoricTaskInstanceEntity.class, "queryHisTask", offset, pagesize, map);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return listInfo;
	}
	/**
	 * 获得历史任务实例 by用户名和流程KEY
	 * 分页列表
	 * 
	 * @param username
	 * @return
	 */
	@Override
	public ListInfo getHisTaskByUsernameProcessKey(String username,String[] processKeys,long offset,int pagesize) {
		ListInfo listInfo = null;
		try{
			Map<String,Object> map  = new HashMap<String,Object>();
			map.put("username", username);
			map.put("processKeys", processKeys);
			listInfo = executor.queryListInfoBean(HistoricTaskInstanceEntity.class, "queryHisTask", offset, pagesize, map);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return listInfo;
	}

	/**
	 * 认领任务
	 * 
	 * @param taskId
	 * @param userId
	 */
	public void claim(String taskId, String username) {
		taskService = processEngine.getTaskService();
		taskService.claim(taskId, username);
	}

	/**
	 * 为任务指定代理处理用户
	 * 
	 * @param taskId
	 * @param userId
	 */
	public void delegateTask(String taskId, String userId) {
		taskService = processEngine.getTaskService();
		taskService.delegateTask(taskId, userId);
	}

	/**
	 * 部署流程
	 * 
	 * @return
	 */
	public Deployment deployProcDefByZip(String deploymentName, String zip) {

		repositoryService = processEngine.getRepositoryService();

		/**
		 * 部署图片时，命名必须为leave.activiti.jpg或leave.activiti.{processKey}.jpg
		 * act_re_procdef.DGRM_RESOURCE_NAME_
		 */
		ZipInputStream inputStream = null;
		try {
			inputStream = new ZipInputStream(new FileInputStream(new File(zip)));
		} catch (Exception e) {
			e.printStackTrace();
		}

		Deployment deploy = repositoryService.createDeployment()
				.name(deploymentName)// act_re_deployment.NAME
				.addZipInputStream(inputStream).deploy();

		return deploy;
	}

	/**
	 * 部署流程
	 * 
	 * @return
	 */
	public Deployment deployProcDefByPath(String deploymentName,
			String xmlPath, String jpgPath) {
		repositoryService = processEngine.getRepositoryService();
		Deployment deploy = repositoryService.createDeployment()
				.name(deploymentName).addClasspathResource(xmlPath).deploy();
		// .addClasspathResource(jpgPath).deploy();

		return deploy;
	}

	/**
	 * 启动流程
	 * 
	 * @param variableMap
	 * @param process_key
	 * @return
	 */
	public ProcessInstance startProcDef(Map<String, Object> variableMap,
			String process_key) {
		runtimeService = processEngine.getRuntimeService();
		repositoryService = processEngine.getRepositoryService();
		ProcessDefinition processDefinition = repositoryService
				.createProcessDefinitionQuery()
				.processDefinitionKey(process_key).latestVersion()
				.singleResult();
		ProcessInstance processInstance = runtimeService
				.startProcessInstanceById(processDefinition.getId(),
						variableMap);
		return processInstance;
	}

	/**
	 * 启动流程
	 * 
	 * @param businessKey
	 * @param variableMap
	 * @param process_key
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
	 * 启动流程(普通)
	 * 
	 * @param businessKey
	 * @param process_key
	 * @return
	 */
	public ProcessInstance startProcDef(String businessKey, String process_key) {
		runtimeService = processEngine.getRuntimeService();
		ProcessDefinition processDefinition = repositoryService
				.createProcessDefinitionQuery()
				.processDefinitionKey(process_key).latestVersion()
				.singleResult();
		ProcessInstance processInstance = runtimeService
				.startProcessInstanceById(processDefinition.getId(),
						businessKey);
		return processInstance;
	}

	/**
	 * 启动流程(加载组织机构待办配置)
	 * 
	 * @param process_key
	 *            流程KEY
	 * @param orgId
	 *            组织机构ID
	 * @return
	 */
	public ProcessInstance startProcDefLoadOrgCandidate(String process_key,
			String orgId) {
		return startProcDefLoadOrgCandidate(null, process_key, orgId, null);
	}

	/**
	 * 启动流程(加载组织机构待办配置)
	 * 
	 * @param variableMap
	 *            自定义参数
	 * @param process_key
	 *            流程KEY
	 * @param orgId
	 *            组织机构ID
	 * @return
	 */
	public ProcessInstance startProcDefLoadOrgCandidate(
			Map<String, Object> variableMap, String process_key, String orgId) {
		return startProcDefLoadOrgCandidate(variableMap, process_key, orgId,
				null);
	}

	/**
	 * 启动流程(加载组织机构待办配置)
	 * 
	 * @param process_key
	 *            流程KEY
	 * @param orgId
	 *            组织机构ID
	 * @param initor
	 *            流程创建者
	 * @return
	 */
	public ProcessInstance startProcDefLoadOrgCandidate(String process_key,
			String orgId, String initor) {
		return startProcDefLoadOrgCandidate(null, process_key, orgId, initor);
	}

	
	public ProcessInstance startProcessLoadBusinessCandidate(
			Map<String, Object> variableMap, String process_key,
			String businessid, String initor) {
		repositoryService = processEngine.getRepositoryService();
		runtimeService = processEngine.getRuntimeService();
		Map<String, Object> candidateMap = new HashMap<String, Object>();

		// 设置查询参数
		ActivitiNodeCandidate activitiNodeCandidate = new ActivitiNodeCandidate();
		activitiNodeCandidate.setBusiness_id(businessid);
		activitiNodeCandidate
				.setBusiness_type(WorkFlowConstant.BUSINESS_TYPE_BUSINESSTYPE);
		activitiNodeCandidate.setProcess_key(process_key);

		// 查询配置
		List<ActivitiNodeCandidate> candidateList = activitiConfigService
				.queryActivitiNodeCandidate(activitiNodeCandidate);
		candidateMap = getCandidateMap(candidateList);
		// 查询的参数配置和传进来的参数组合
		if (variableMap != null && variableMap.size() > 0) {
			candidateMap.putAll(variableMap);
		}
		// 启动部署流程的最新版本
		ProcessDefinition processDefinition = repositoryService
				.createProcessDefinitionQuery()
				.processDefinitionKey(process_key).latestVersion()
				.singleResult();

		identityService.setAuthenticatedUserId(initor);

		ProcessInstance processInstance = runtimeService
				.startProcessInstanceById(processDefinition.getId(),
						candidateMap);
		return processInstance;
	}

	/**
	 * 启动流程(加载组织机构待办配置)
	 * 
	 * @param variableMap
	 *            自定义参数
	 * @param process_key
	 *            流程KEY
	 * @param orgId
	 *            组织机构ID
	 * @param initor
	 *            流程创建者
	 * 
	 * @return
	 */
	public ProcessInstance startProcDefLoadOrgCandidate(
			Map<String, Object> variableMap, String process_key, String orgId,
			String initor) {
		repositoryService = processEngine.getRepositoryService();
		runtimeService = processEngine.getRuntimeService();
		Map<String, Object> candidateMap = new HashMap<String, Object>();

		// 设置查询参数
		ActivitiNodeCandidate activitiNodeCandidate = new ActivitiNodeCandidate();
		activitiNodeCandidate.setBusiness_id(orgId);
		activitiNodeCandidate.setBusiness_type(WorkFlowConstant.BUSINESS_TYPE_ORG);
		activitiNodeCandidate.setProcess_key(process_key);

		// 查询配置
		List<ActivitiNodeCandidate> candidateList = activitiConfigService
				.queryActivitiNodeCandidate(activitiNodeCandidate);
		candidateMap = getCandidateMap(candidateList);
		// 查询的参数配置和传进来的参数组合
		if (variableMap != null && variableMap.size() > 0) {
			candidateMap.putAll(variableMap);
		}
		// 启动部署流程的最新版本
		ProcessDefinition processDefinition = repositoryService
				.createProcessDefinitionQuery()
				.processDefinitionKey(process_key).latestVersion()
				.singleResult();

		identityService.setAuthenticatedUserId(initor);

		ProcessInstance processInstance = runtimeService
				.startProcessInstanceById(processDefinition.getId(),
						candidateMap);
		return processInstance;
	}

	/**
	 * 启动流程(加载业务类型待办配置)
	 * 
	 * @param process_key
	 *            流程KEY
	 * @param bussinesstypeId
	 *            业务类型ID
	 * @return
	 */
	public ProcessInstance startProcDefLoadBussinessCandidate(
			String process_key, String bussinesstypeId) {
		return startProcessLoadBusinessCandidate(null, process_key,
				bussinesstypeId, null);
	}

	/**
	 * 启动流程(加载业务类型待办配置)
	 * 
	 * @param process_key
	 *            流程KEY
	 * @param bussinesstypeId
	 *            业务类型ID
	 * @param initor
	 *            流程创建者
	 * @return
	 */
	public ProcessInstance startProcDefLoadBussinessCandidate(
			String process_key, String bussinessId, String initor) {
		return startProcessLoadBusinessCandidate(null, process_key,
				bussinessId, initor);
	}

	/**
	 * 启动流程(加载业务类型待办配置)
	 * 
	 * @param variableMap
	 *            自定义参数
	 * @param process_key
	 *            流程KEY
	 * @param bussinesstypeId
	 *            业务类型ID
	 * @return
	 */
	public ProcessInstance startProcDefLoadBussinessCandidate(
			Map<String, Object> variableMap, String process_key,
			String bussinesstypeId) {
		return startProcessLoadBusinessCandidate(variableMap, process_key,
				bussinesstypeId, null);

	}


	/**
	 * 启动流程(加载待办配置)
	 * 
	 * @param process_key
	 *            流程KEY
	 * @param initor
	 *            流程创建者
	 * @return
	 */
	public ProcessInstance startProcDefLoadCommonCandidate(String process_key,
			String business_id, String business_type, String initor) {
		return startProcDefLoadCandidate(null, process_key, business_id,
				business_type, initor);
	}

	/**
	 * 启动流程(加载待办配置)
	 * 
	 * @param variableMap
	 *            自定义参数
	 * @param process_key
	 *            流程KEY
	 * @return
	 */
	public ProcessInstance startProcDefLoadCommonCandidate(
			Map<String, Object> variableMap, String process_key,
			String business_id, String business_type) {
		return startProcDefLoadCandidate(variableMap, process_key, business_id,
				business_type, null);
	}

	/**
	 * 启动流程(加载待办配置)
	 * 
	 * @param process_key
	 *            流程KEY
	 * @return
	 */
	public ProcessInstance startProcDefLoadCommonCandidate(String process_key,
			String business_id, String business_type) {
		return startProcDefLoadCandidate(null, process_key, business_id,
				business_type, null);
	}

	/**
	 * * 启动流程(加载待办配置)
	 * 
	 * @param variableMap
	 *            自定义参数
	 * @param process_key
	 *            流程key
	 * @param business_id
	 *            配置的待办类型ID
	 * @param business_type
	 *            配置的待办类型
	 * @param initor
	 *            流程创建者
	 * @return
	 */
	public ProcessInstance startProcDefLoadCandidate(
			Map<String, Object> variableMap, String process_key,
			String business_id, String business_type, String initor) {
		repositoryService = processEngine.getRepositoryService();
		runtimeService = processEngine.getRuntimeService();
		identityService = processEngine.getIdentityService();
		Map<String, Object> candidateMap = new HashMap<String, Object>();
		
		if(business_type.equals(WorkFlowConstant.BUSINESS_TYPE_COMMON)){
			business_id="";
		}
		// 设置查询参数
		ActivitiNodeCandidate activitiNodeCandidate = new ActivitiNodeCandidate();
		activitiNodeCandidate.setProcess_key(process_key);
		activitiNodeCandidate.setBusiness_id(business_id);
		activitiNodeCandidate.setBusiness_type(business_type);

		// 查询配置
		List<ActivitiNodeCandidate> candidateList = activitiConfigService
				.queryActivitiNodeCandidate(activitiNodeCandidate);
		candidateMap = getCandidateMap(candidateList);
		// 查询的参数配置和传进来的参数组合
		if (variableMap != null && variableMap.size() > 0) {
			candidateMap.putAll(variableMap);
		}

		// 启动部署流程的最新版本
		ProcessDefinition processDefinition = repositoryService
				.createProcessDefinitionQuery()
				.processDefinitionKey(process_key).latestVersion()
				.singleResult();

		identityService.setAuthenticatedUserId(initor);

		ProcessInstance processInstance = runtimeService
				.startProcessInstanceById(processDefinition.getId(),
						candidateMap);
		return processInstance;
	}

	/**
	 * 返回待办配置MAP
	 * 
	 * @param candidateList
	 * @return
	 */
	public Map<String, Object> getCandidateMap(
			List<ActivitiNodeCandidate> candidateList) {
		Map<String, Object> candidateMap = new HashMap<String, Object>();
		for (ActivitiNodeCandidate nodeCandidate : candidateList) {
			if (nodeCandidate.getCandidate_users_id() != null
					&& !nodeCandidate.getCandidate_users_id().equals("")) {
				candidateMap.put(nodeCandidate.getNode_key() + "_users", Arrays
						.asList(nodeCandidate.getCandidate_users_id()
								.split(",")));
			}else{
				candidateMap.put(nodeCandidate.getNode_key() + "_users",Arrays.asList(java.util.UUID.randomUUID().toString().split(",")));
			}
			if (nodeCandidate.getCandidate_groups_id() != null
					&& !nodeCandidate.getCandidate_groups_id().equals("")) {
				candidateMap.put(nodeCandidate.getNode_key() + "_groups",
						Arrays.asList(nodeCandidate.getCandidate_groups_id()
								.split(",")));
			}else{
				candidateMap.put(nodeCandidate.getNode_key() + "_groups",Arrays.asList(java.util.UUID.randomUUID().toString().split(",")));
			}
		}
		return candidateMap;
	}

	/**
	 * 删除部署的流程
	 * 
	 * @param process_key
	 */
	public void deleteProcDefByprocesskey(String process_key) {
		repositoryService = processEngine.getRepositoryService();
		taskService = processEngine.getTaskService();
		List<ProcessDefinition> procDefList = this
				.activitiListByprocesskey(process_key);
		for (ProcessDefinition task : procDefList) {
			List<Task> tasks = taskService.createTaskQuery()
					.processDefinitionId(task.getId()).list();
			for (Task t : tasks) {
				taskService.deleteTask(t.getId());
			}
			repositoryService.deleteDeployment(task.getDeploymentId());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sany.workflow.service.ActivitiService#deleteProcDefByprocessid(java
	 * .lang.String)
	 */
	public void deleteProcDef(String key) {
		List<ProcessDefinition> pdList = processEngine.getRepositoryService()
				.createProcessDefinitionQuery().processDefinitionKey(key)
				.list();
		if (pdList != null && !pdList.isEmpty()) {
			for (ProcessDefinition pd : pdList) {
				processEngine.getRepositoryService().deleteDeployment(
						pd.getDeploymentId(), true);
			}
		}
	}

	public List<ProcessDefinition> findAllRemoveListByProcessDeploymentids(
			String[] processDeploymentids) {
		processDeploymentids = Arrays.toString(processDeploymentids)
				.replace("[", "").replace("]", "").split(",");
		List<ProcessDefinition> removeList = new ArrayList<ProcessDefinition>();
		for (String processDeploymentid : processDeploymentids) {
			List<ProcessDefinition> pdList = processEngine
					.getRepositoryService().createProcessDefinitionQuery()
					.processDefinitionKey(processDeploymentid).list();
			if (pdList != null && !pdList.isEmpty()) {
				removeList.addAll(pdList);
			}
		}

		return removeList;
	}

	/**
	 * 获取流程图
	 * 
	 * @param execId
	 * @param process_key
	 * @return
	 */
	public ActivityImpl traceProcess(String execId, String process_key) {

		repositoryService = processEngine.getRepositoryService();
		runtimeService = processEngine.getRuntimeService();
		ProcessDefinition processDefinition = repositoryService
				.createProcessDefinitionQuery()
				.processDefinitionKey(process_key).latestVersion()
				.singleResult();// 最近版本的流程实例

		ProcessDefinitionImpl pdImpl = (ProcessDefinitionImpl) processDefinition;
		String processDefinitionId = pdImpl.getId();// 流程标识

		ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition(processDefinitionId);
		ActivityImpl actImpl = null;

		List<ActivityImpl> activitiList = def.getActivities();// 获得当前任务的所有节点

		ExecutionEntity execution = (ExecutionEntity) runtimeService
				.createExecutionQuery().executionId(execId).singleResult();// 执行实例
		if (execution == null) {
			return activitiList.get(activitiList.size() - 1);
		}
		String activitiId = execution.getActivityId();// 当前实例的执行到哪个节点

		for (ActivityImpl activityImpl : activitiList) {
			String id = activityImpl.getId();
			if (id.equals(activitiId)) {// 获得执行到那个节点
				actImpl = activityImpl;
				break;
			}
		}
		return actImpl;
	}

	public ActivityImpl getActivityImplById(String actId,
			String processInstanceId) {
		runtimeService = processEngine.getRuntimeService();
		repositoryService = processEngine.getRepositoryService();
		ActivityImpl actImpl = null;
		ProcessInstance processInstance = runtimeService
				.createProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();
		ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition(processInstance
						.getProcessDefinitionId());
		List<ActivityImpl> activitiList = def.getActivities();// 获得当前任务的所有节点
		for (ActivityImpl activityImpl : activitiList) {
			String id = activityImpl.getId();
			if (id.equals(actId)) {// 获得执行到那个节点
				actImpl = activityImpl;
				break;
			}
		}
		return actImpl;
	}

	/**
	 * 获取任务列表
	 * 
	 * @param execId
	 * @return
	 */
	public List<Task> listTaskByExecId(String execId) {
		taskService = processEngine.getTaskService();
		List<Task> taskList = taskService.createTaskQuery().executionId(execId)
				.list();

		return taskList;
	}

	/**
	 * @param processInstanceId
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
	 * 根据processInstanceId查询历史
	 * 
	 * @param processInstanceId
	 * @return
	 */
	public HistoricProcessInstance getHisProcessInstanceById(
			String processInstanceId) {
		historyService = processEngine.getHistoryService();
		HistoricProcessInstance processInstance = historyService
				.createHistoricProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();
		return processInstance;
	}

	/**
	 * 根据流程实例ID查询流程实例
	 * 
	 * @param processDefinitionId
	 * @return
	 */
	public ProcessDefinition getProcessDefinitionById(String processDefinitionId) {
		repositoryService = processEngine.getRepositoryService();
		ProcessDefinition processDefinition = repositoryService
				.createProcessDefinitionQuery()
				.processDefinitionId(processDefinitionId).singleResult();// 最近版本的流程实例
		return processDefinition;
	}

	/**
	 * 根据流程实例KEY查询流程实例
	 * 
	 * @param processDefinitionId
	 * @return
	 */
	public ProcessDefinition getProcessDefinitionByKey(String processKey) {
		repositoryService = processEngine.getRepositoryService();
		ProcessDefinition processDefinition = repositoryService
				.createProcessDefinitionQuery()
				.processDefinitionKey(processKey).latestVersion()
				.singleResult();// 最近版本的流程实例
		return processDefinition;
	}

	/**
	 * 根据部署ID查询部署的流程
	 * 
	 * @param deploymentId
	 * @return
	 */
	public Deployment getDeploymentById(String deploymentId) {
		repositoryService = processEngine.getRepositoryService();
		Deployment deployment = repositoryService.createDeploymentQuery()
				.deploymentId(deploymentId).singleResult();
		return deployment;
	}

	/**
	 * 根据用户名查询待办任务
	 * 
	 * @param processKey
	 * @param username
	 * @return
	 */
	public List<Task> listTaskByUser(String username) {
		taskService = processEngine.getTaskService();
		List<Task> list = new ArrayList<Task>();
		List<TaskEntity> userTaskList = null;
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("username", username);
			userTaskList = executor.queryListBean(TaskEntity.class,
					"selectAllTaskByUser", map);
			list.addAll(userTaskList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 根据用户名查询待办任务
	 * 
	 * @param processKey
	 * @param username
	 * @return
	 */
	@Override
	public ListInfo listTaskByUser(String username,long offset,int pagesize) {
		taskService = processEngine.getTaskService();
		ListInfo listInfo = null;
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("username", username);
			listInfo = executor.queryListInfoBean(TaskEntity.class,
					"selectAllTaskByUser",offset,pagesize, map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listInfo;
	}
	
	public ListInfo listTaskAndVarsByUserWithState(Class clazz,String processkey,String state,String userAccount,long offset,int pagesize)
	{
		
		ListInfo listInfo = null;
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("userAccount", userAccount);
			map.put("state", state);
			map.put("processkey", processkey);
			
			listInfo = executor.queryListInfoBean(clazz,
					"listTaskAndVarsByUserWithState",offset,pagesize, map);
		} catch (Exception e) {
	
			throw new ProcessException("获取待办任务失败：processkey="+processkey+",state="+state
					+",userAccount="+userAccount+",offset="+offset+",pagesize="+pagesize,e);
		}
		return listInfo;
	}
	/**
	 * 获取用户特定状态下的任务待办数
	 */
	public int countTasksByUserWithState(String processkey,String state,String userAccount)
	{
		
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("userAccount", userAccount);
			map.put("state", state);
			map.put("processkey", processkey);
			
			return  executor.queryObjectBean(int.class, "countlistTaskAndVarsByUserWithState", map);
		} catch (Exception e) {
	
			throw new ProcessException("获取待办任务数失败：processkey="+processkey+",state="+state
					+",userAccount="+userAccount,e);
		}
	}

	/**
	 * 根据用户名,流程KEY数组查询待办任务
	 * 
	 * @param processKey
	 * @param username
	 * @return
	 */
	public List<Task> listTaskByUser(String[] processKeys, String username) {
		taskService = processEngine.getTaskService();
		List<Task> list = new ArrayList<Task>();
		List<TaskEntity> userTaskList = null;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("processKeys", processKeys);
			map.put("username", username);
			userTaskList = executor.queryListBean(TaskEntity.class,
					"selectAllTaskByUser", map);
			list.addAll(userTaskList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 根据用户名,流程KEY查询待办任务分页列表
	 * 
	 * @param processKey
	 * @param username
	 * @return
	 */
	@Override
	public List<Task> listTaskByUser(String processKey, String username) {
		taskService = processEngine.getTaskService();
		List<Task> userTaskList = new ArrayList<Task>();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			String[] processKeys = new String[]{processKey};
			map.put("processKeys", processKeys);
			map.put("username", username);
			userTaskList.addAll(executor.queryListBean(TaskEntity.class,
					"selectAllTaskByUser", map));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userTaskList;
	}
	
	/**
	 * 根据用户名,流程KEY查询待办任务分页列表
	 * 
	 * @param processKey
	 * @param username
	 * @return
	 */
	@Override
	public ListInfo listTaskByUser(String processKey, String username,long offset,int pagesize) {
		taskService = processEngine.getTaskService();
		ListInfo userTaskList = null;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			String[] processKeys = new String[]{processKey};
			map.put("processKeys", processKeys);
			map.put("username", username);
			userTaskList = executor.queryListInfoBean(TaskEntity.class,
					"selectAllTaskByUser",offset,pagesize, map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userTaskList;
	}
	
	/**
	 * 根据用户名,流程KEY数组查询待办任务分页列表
	 * 
	 * @param processKey
	 * @param username
	 * @return
	 */
	public ListInfo listTaskByUser(String[] processKeys, String username,long offset,int pagesize) {
		taskService = processEngine.getTaskService();
		ListInfo userTaskList = null;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("processKeys", processKeys);
			map.put("username", username);
			userTaskList = executor.queryListInfoBean(TaskEntity.class,
					"selectAllTaskByUser",offset,pagesize, map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userTaskList;
	}

	/**
	 * 根据任务ID查询任务信息
	 * 
	 * @param taskId
	 * @return
	 */
	public Task getTaskById(String taskId) {
		taskService = processEngine.getTaskService();
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		return task;
	}

	/**
	 * 根据历史任务ID查询历史任务信息
	 * 
	 * @param taskId
	 * @return
	 */
	public HistoricTaskInstance getHisTaskById(String hisTaskId) {
		historyService = processEngine.getHistoryService();
		HistoricTaskInstance hisTask = historyService
				.createHistoricTaskInstanceQuery().taskId(hisTaskId)
				.singleResult();
		return hisTask;
	}

	public List<ActivityImpl> getActivitiImplListByProcessDefinitionId(
			String processDefinitionId) {
		ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition(processDefinitionId);
		List<ActivityImpl> activitiList = def.getActivities();// 获得当前任务的所有节点
		return activitiList;
	}


	/**
	 * 获得流程的所有节点
	 * 
	 * @param processKey
	 * @return
	 */
	public List<ActivityImpl> getActivitImplListByProcessKey(String processKey) {
		repositoryService = processEngine.getRepositoryService();
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
	 * 获取当前环节ID的所有下面环节ID
	 * 
	 * @param nodeKey
	 *            环节ID
	 * @param processKey
	 *            流程key
	 * @return
	 */
	public List<String> getNextNodes(String nodeKey, String processKey) {
		repositoryService = processEngine.getRepositoryService();
		ProcessDefinition processDefinition = repositoryService
				.createProcessDefinitionQuery()
				.processDefinitionKey(processKey).latestVersion()
				.singleResult();// 最近版本的流程实例
		ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition(processDefinition.getId());
		List<ActivityImpl> activitiList = def.getActivities();// 获得当前任务的所有节点
		List<String> nextNodes = new ArrayList<String>();
		for (ActivityImpl activityImpl : activitiList) {
			if (nodeKey.equals(activityImpl.getId())) {
				List<PvmTransition> outTransitions = activityImpl
						.getOutgoingTransitions();// 获取从某个节点出来的所有线路
				for (PvmTransition tr : outTransitions) {
					PvmActivity ac = tr.getDestination(); // 获取线路的终点节点
					nextNodes.add(ac.getId());
				}
			}
		}
		return nextNodes;
	}

	/**
	 * 根据部署的ID获得流程实例key
	 * 
	 * @param deploymentId
	 * @return
	 */
	public String getPorcessKeyByDeployMentId(String deploymentId) {
		repositoryService = processEngine.getRepositoryService();
		ProcessDefinition processDefinition = repositoryService
				.createProcessDefinitionQuery().deploymentId(deploymentId)
				.singleResult();
		return processDefinition.getKey();
	}

	public static void main(String agres[]) {
		ActivitiServiceImpl activitHelper = new ActivitiServiceImpl(
				"activiti.cfg.xml");
		// activitHelper.getNextNodes("usertask1","mms");
		activitHelper.deployProcDefByPath("test", "E://test.bpmn20.xml", null);
	}

	public Deployment deployProcDefByPath(String deploymentName, String xmlPath) {
		return null;
	}

	public Deployment deployProcDefByInputStream(String deploymentName,
			String xmlPath, InputStream processDef) {
		DeploymentBuilder deploymentBuilder = processEngine
				.getRepositoryService().createDeployment().name(deploymentName);
		deploymentBuilder.addInputStream(xmlPath, processDef);

		return deploymentBuilder.deploy();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sany.workflow.service.ActivitiService#deployProcDefByZip(java.lang
	 * .String, java.util.zip.ZipInputStream)
	 */
	public Deployment deployProcDefByZip(String deploymentName,
			ZipInputStream processDef) {
		DeploymentBuilder deploymentBuilder = processEngine
				.getRepositoryService().createDeployment().name(deploymentName);
		deploymentBuilder.addZipInputStream(processDef);

		return deploymentBuilder.deploy();
	}

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

	public ListInfo queryProcessDefs(long offset, int pagesize,
			ProcessDefCondition processDefCondition) {
		try{
			if(processDefCondition.getProcessKey()!=null&&!processDefCondition.getProcessKey().isEmpty()){
				processDefCondition.setProcessKey("%"+processDefCondition.getProcessKey()+"%");
			}
			if(processDefCondition.getResourceName()!=null&&!processDefCondition.getResourceName().isEmpty()){
				processDefCondition.setResourceName("%"+processDefCondition.getResourceName()+"%");
			}
			ListInfo listInfo = executor.queryListInfoBean(ProcessDef.class, "queryProdef", offset, pagesize, processDefCondition);
			return listInfo;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}

	}

	public void deleteDeploymentCascade(String[] processDeploymentids,
			boolean[] cascades) {
		processDeploymentids = Arrays.toString(processDeploymentids)
				.replace("[", "").replace("]", "").split(",");
		if (cascades != null && cascades.length > 0) {
			for (int i = 0; i < processDeploymentids.length; i++) {
				processEngine.getRepositoryService().deleteDeployment(
						processDeploymentids[i], cascades[i]);
			}
		} else {
			for (int i = 0; i < processDeploymentids.length; i++) {
				processEngine.getRepositoryService().deleteDeployment(
						processDeploymentids[i], true);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sany.workflow.service.ActivitiService#deleteDeploymentAllVersions
	 * (java.lang.String[])
	 */
	public void deleteDeploymentAllVersions(String[] processDeploymentids) {
		processDeploymentids = Arrays.toString(processDeploymentids)
				.replace("[", "").replace("]", "").split(",");
		for (String processDeploymentid : processDeploymentids) {
			deleteProcDef(processDeploymentid);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sany.workflow.service.ActivitiService#activateProcess(java.lang.String
	 * )
	 */
	public void activateProcess(String processId) {
		processEngine.getRepositoryService().activateProcessDefinitionById(
				processId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sany.workflow.service.ActivitiService#supendProcess(java.lang.String)
	 */
	public void suspendProcess(String processId) {
		processEngine.getRepositoryService().suspendProcessDefinitionById(
				processId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sany.workflow.service.ActivitiService#getResourceAsStream(java.lang
	 * .String, java.lang.String)
	 */
	public InputStream getResourceAsStream(String deploymentId,
			String resourceName) {
		return processEngine.getRepositoryService().getResourceAsStream(
				deploymentId, resourceName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sany.workflow.service.ActivitiService#getProcessDefByDeploymentId
	 * (java.lang.String)
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
	
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sany.workflow.service.ActivitiService#getNewestTask(java.lang.String)
	 */
	@Override
	public Task getCurrentTask(String processInstanceId) {
		List<Task> taskList = processEngine.getTaskService().createTaskQuery().processInstanceId(processInstanceId)
				.orderByTaskId().desc().list();

		if (!CollectionUtils.isEmpty(taskList)) {
			return taskList.get(0);
		}

		return null;
	}
	
	public List<Task> getCurrentTaskList(String processInstanceId) {
		List<Task> taskList = processEngine.getTaskService().createTaskQuery().processInstanceId(processInstanceId)
				.orderByTaskId().desc().list();

		if (!CollectionUtils.isEmpty(taskList)) {
			return taskList;
		}

		return null;
	}
	
	/**
	 * 根据任务ID查询任务的待办人
	 * @param taskId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> getUsersByTaskId(String taskId) {
		Task task = this.getTaskById(taskId);
		return (List<String>) this.getRuntimeService().getVariable(
				task.getProcessInstanceId(), task.getTaskDefinitionKey() + "_users");
	}
	
	/**
	 * 
	 * 撤消流程实例
	 * processInstanceId：要撤消的流程实例id
	 * deleteReason：撤消流程实例的原因
	 */
	public void cancleProcessInstance(String processInstanceId, String deleteReason)
	{
		this.runtimeService.deleteProcessInstance(processInstanceId, deleteReason);
	}
	
	public List<Task> listTaskByProcessInstanceId(String processInstanceId) {
        taskService = processEngine.getTaskService();
    	return taskService.createTaskQuery().processInstanceId(processInstanceId).list();
    }


}
