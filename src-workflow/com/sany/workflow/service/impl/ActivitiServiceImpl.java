package com.sany.workflow.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import org.activiti.bpmn.model.BpmnModel;
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
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.diagram.ProcessDiagramGenerator;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.db.upgrade.InstanceUpgrade;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.frameworkset.util.CollectionUtils;

import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.platform.cms.util.StringUtil;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.util.ListInfo;
import com.sany.workflow.entity.ActivitiNodeCandidate;
import com.sany.workflow.entity.LoadProcess;
import com.sany.workflow.entity.Nodevariable;
import com.sany.workflow.entity.ProcessDef;
import com.sany.workflow.entity.ProcessDefCondition;
import com.sany.workflow.entity.ProcessInst;
import com.sany.workflow.entity.ProcessInstCondition;
import com.sany.workflow.entity.TaskCondition;
import com.sany.workflow.entity.TaskManager;
import com.sany.workflow.entity.WfAppProcdefRelation;
import com.sany.workflow.service.ActivitiConfigService;
import com.sany.workflow.service.ActivitiRelationService;
import com.sany.workflow.service.ActivitiService;
import com.sany.workflow.service.ProcessException;
import com.sany.workflow.util.WorkFlowConstant;



public class ActivitiServiceImpl implements ActivitiService ,org.frameworkset.spi.DisposableBean{

	private com.frameworkset.common.poolman.ConfigSQLExecutor executor;

	private ActivitiConfigService activitiConfigService;

	private ProcessEngine processEngine;
	private InstanceUpgrade instanceUpgrade;
	private RepositoryService repositoryService;// 获得activiti服务
	private RuntimeService runtimeService;// 用于管理运行时流程实例
	private TaskService taskService;// 用于管理运行时任务
	@SuppressWarnings("unused")
	private FormService formService;// 用于管理任务表单
	private HistoryService historyService;// 管理流程实例、任务实例等历史数据
	@SuppressWarnings("unused")
	private ManagementService managementService;// 用于管理定时任务
	private IdentityService identityService;// 用于管理组织结构
	private ActivitiRelationService activitiRelationService;//应用管理

	  /**
	   * 将当前任务驳回到上一个任务处理人处，并更新流程变量参数
	   * @param taskId
	   * @param variables
	   */
   public void rejecttoPreTask(String taskId, Map<String, Object> variables)
   {
//	   taskService.claim(taskId, username);
	   this.taskService.rejecttoPreTask(taskId,variables);
   }
	  
	  /**
	   * 将当前任务驳回到上一个任务处理人处
	   * @param taskId
	   */
   public void rejecttoPreTask(String taskId){
	   this.taskService.rejecttoPreTask(taskId);
   }
   
   /**
	   * 将当前任务驳回到上一个任务处理人处，并更新流程变量参数
	   * @param taskId
	   * @param variables
	   */
public void rejecttoPreTask(String taskId, String username,Map<String, Object> variables)
{
	TransactionManager tm = new TransactionManager();
	try{
		tm.begin();
		
		taskService.claim(taskId, username);
		this.taskService.rejecttoPreTask(taskId,variables);
	   
		tm.commit();
	}catch(Exception e){
		throw new ProcessException(e);
	}finally {
		tm.release();
	}
}
	  
	  /**
	   * 将当前任务驳回到上一个任务处理人处
	   * @param taskId
	   */
public void rejecttoPreTask(String taskId,String username){
	TransactionManager tm = new TransactionManager();
	try{
		tm.begin();
		
		taskService.claim(taskId, username);
		this.taskService.rejecttoPreTask(taskId);
		
		tm.commit();
	}catch(Exception e){
		throw new ProcessException(e);
	}finally {
		tm.release();
	}
}
	public ActivitiServiceImpl(String xmlPath) {
		TransactionManager tm = new TransactionManager();
		try
		{
			tm.begin();
			
			ProcessEngineConfigurationImpl config = (ProcessEngineConfigurationImpl)ProcessEngineConfiguration
			.createProcessEngineConfigurationFromResource(xmlPath);
			processEngine = config.buildProcessEngine();
			this.taskService = processEngine.getTaskService();
			this.historyService = processEngine.getHistoryService();
			this.repositoryService = processEngine.getRepositoryService();
			this.formService = this.processEngine.getFormService();
			this.identityService = this.processEngine.getIdentityService();
			this.runtimeService = this.processEngine.getRuntimeService();
			this.managementService = this.processEngine.getManagementService();
			this.instanceUpgrade = config.getInstanceUpgrade();
			tm.commit();
		}
		catch(Exception e)
		{
			throw new ProcessException(e);
		}
		finally
		{
			tm.releasenolog();
		}
		
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
		return repositoryService;
	}

	/**
	 * 获得管理运行时流程实例服务
	 * 
	 * @return
	 */
	public RuntimeService getRuntimeService() {
		return runtimeService;
	}

	/**
	 * 获得管理运行时任务服务;
	 * 
	 * @return
	 */
	public TaskService getTaskService() {
		return taskService;
	}

	/**
	 * 获得管理流程实例、任务实例等历史数据服务
	 * 
	 * @return
	 */
	public HistoryService getHistoryService() {
		return historyService;
	}

	/**
	 * 获得表单服务
	 * 
	 * @return
	 */
	public FormService getFormService() {
		return this.formService;
	}

	/**
	 * 获得用于管理定时任务服务
	 * 
	 * @return
	 */
	public ManagementService getManagementService() {
		return managementService;
	}

	/**
	 * 获得用于管理组织结构服务
	 * 
	 * @return
	 */
	public IdentityService getIdentityService() {
		return identityService;
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
		taskService.setVariables(taskId, map);
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
		taskService.setVariable(taskId, variable, object);
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
		return taskService.getVariable(taskId, key);
	}

	/**
	 * 根据流程定义KEY获取任务
	 * 
	 * @param definitionKey
	 *            流程定义key
	 * @return
	 */
	public List<Task> getActList(String definitionKey) {
		return taskService.createTaskQuery()
				.taskDefinitionKey(definitionKey).list();
	}

	/**
	 * 获取activiti流程定义的历史版本
	 * 
	 * @return
	 */
	public List<ProcessDefinition> activitiListByprocesskey(String process_key) {

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
//		taskService = processEngine.getTaskService();
		taskService.complete(taskId, map);
	}
	
	/**
	 * 完成任务(普通)
	 * 
	 * @param taskId
	 * @param map
	 */
	public void completeTask(String taskId) {
//		taskService = processEngine.getTaskService();
		taskService.complete(taskId);
	}
	
	/**
	 * 完成任务(普通)
	 * 
	 * @param taskId
	 * @param map
	 */
	public void completeTaskWithDest(String taskId, Map<String, Object> map,String destinationTaskKey) {
//		taskService = processEngine.getTaskService();
		taskService.complete(taskId, map, destinationTaskKey);
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
		completeTaskLoadOrgParams(taskId, (Map<String, Object>)null, orgId);
	}
	/**
	 * 完成任务(加载组织机构节点参数配置)
	 * 
	 * @param taskId
	 *            任务ID
	 * @param orgId
	 *            组织机构ID,String destinationTaskKey
	 */
	public void completeTaskLoadOrgParamsWithDest(String taskId, String orgId,String destinationTaskKey) {
		completeTaskLoadOrgParams(taskId, (Map<String, Object>)null, orgId, destinationTaskKey);
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
			Map<String, Object> map, String orgId)
	{
		completeTaskLoadOrgParams( taskId,
				 map,  orgId,(String )null);
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
			Map<String, Object> map, String orgId,String destinationTaskKey) {
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
//		taskService = processEngine.getTaskService();
		taskService.complete(taskId, paramsMap,destinationTaskKey);
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
		completeTaskLoadBussinesstypeParams(taskId, (Map<String, Object>)null, bussinesstypeId,(String)null);
	}
	
	/**
	 * 完成任务(加载业务类型节点参数配置)
	 * 
	 * @param taskId
	 *            任务ID
	 * @param bussinesstypeId
	 *            业务类型ID,String destinationTaskKey
	 */
	public void completeTaskLoadBussinesstypeParamsWithDest(String taskId,
			String bussinesstypeId,String destinationTaskKey) {
		completeTaskLoadBussinesstypeParams(taskId, (Map<String, Object>)null, bussinesstypeId,destinationTaskKey);
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
	 *            ,String destinationTaskKey
	 */
	public void completeTaskLoadBussinesstypeParams(String taskId,
			Map<String, Object> map, String bussinesstypeId)
	{
		completeTaskLoadBussinesstypeParams( taskId,
				map,  bussinesstypeId,(String)null);
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
	 *            
	 */
	public void completeTaskLoadBussinesstypeParams(String taskId,
			Map<String, Object> map, String bussinesstypeId ,String destinationTaskKey) {
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
//		taskService = processEngine.getTaskService();
		taskService.complete(taskId, paramsMap,destinationTaskKey);
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
		completeTaskLoadCommonParams(taskId, (Map<String, Object>)null);
	}
	/**
	 * 完成任务(加载通用节点参数配置)
	 * 
	 * @param taskId
	 *            任务ID
	 * @param map
	 *            自定义参数MAP
	 */
	public void completeTaskLoadCommonParamsWithDest(String taskId,String destinationTaskKey) {
		completeTaskLoadCommonParams(taskId, (Map<String, Object>)null,destinationTaskKey);
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
		completeTaskLoadCommonParams( taskId,
				map,(String)null);
	}
	/**
	 * 完成任务(加载通用节点参数配置)
	 * 
	 * @param taskId
	 *            任务ID
	 * @param map
	 *            自定义参数MAP,String destinationTaskKey
	 */
	public void completeTaskLoadCommonParams(String taskId,
			Map<String, Object> map,String destinationTaskKey) {
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
//		taskService = processEngine.getTaskService();
		taskService.complete(taskId, paramsMap,destinationTaskKey);
	}
	/**
	 * 处理任务(加载配置好的参数)
	 * 
	 * @param taskId
	 *            任务ID
	 * @param business_id
	 *            业务配置ID
	 * @param business_type
	 *            业务配置类型,String destinationTaskKey
	 */
	public void completeTaskLoadParams(String taskId, String business_id,
			String business_type) {
		completeTaskLoadParamsWithDest( taskId,  business_id,
				 business_type,(String) null);
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
	public void completeTaskLoadParamsWithDest(String taskId, String business_id,
			String business_type,String destinationTaskKey) {
		completeTaskLoadParams(taskId, (Map<String, Object>)null, business_id, business_type, destinationTaskKey);
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
		completeTaskLoadParams( taskId,  map,
				 business_id,  business_type,(String)null);
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
	 *            业务配置类型,String destinationTaskKey
	 */
	public void completeTaskLoadParams(String taskId, Map<String, Object> map,
			String business_id, String business_type,String destinationTaskKey) {
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
//		taskService = processEngine.getTaskService();
		taskService.complete(taskId, paramsMap, destinationTaskKey);
	}

	/**
	 * 完成任务(先领用再完成)
	 * 
	 * @param taskId
	 * @param map
	 */
	public void completeTask(String taskId, String username,
			Map<String, Object> map) {
		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();
			
//		taskService = processEngine.getTaskService();
			taskService.claim(taskId, username);
			taskService.complete(taskId, map);
			
			tm.commit();
		}catch(Exception e){
			throw new ProcessException(e);
		}finally {
			tm.release();
		}
	}
	
	/**
	 * 完成任务(先领用再完成)
	 * 
	 * @param taskId
	 * @param map
	 */
	public void completeTask(String taskId, String username,
			Map<String, Object> map,String destinationTaskKey) {
		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();
			
	//		taskService = processEngine.getTaskService();
			taskService.claim(taskId, username);
			taskService.complete(taskId, map, destinationTaskKey);
		tm.commit();
		}catch(Exception e){
			throw new ProcessException(e);
		}finally {
			tm.release();
		}
	}
	
	/**
	 * 签收任务 gw_tanx
	 * 
	 * @param taskId
	 * @param map
	 */
	public void signTaskByUser(String taskId, String username){
		taskService.claim(taskId, username);
	}
	
	/**
	 * 完成任务(先领用再完成)
	 * 
	 * @param taskId
	 * @param map
	 */
	public void completeTaskByUser(String taskId, String username) {
		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();
			
	//		taskService = processEngine.getTaskService();
			taskService.claim(taskId, username);
			taskService.complete(taskId);
			
			tm.commit();
		}catch(Exception e){
			throw new ProcessException(e);
		}finally {
			tm.release();
		}
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
	 * 完成任务(先领用再完成)
	 * 
	 * @param taskId
	 * @param map
	 */
	public void completeTaskByUserWithDest(String taskId, String username,String destinationTaskKey) {
		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();
			
	//		taskService = processEngine.getTaskService();
			taskService.claim(taskId, username);
			taskService.completeWithDest(taskId, destinationTaskKey);
		
			tm.commit();
		}catch(Exception e){
			throw new ProcessException(e);
		}finally {
			tm.release();
		}
	}
	
	/**
	 * 完成任务(先领用再完成)
	 * 
	 * @param taskId
	 * @param map
	 */
	public void completeTaskWithLocalVariables(String taskId, String username,
			Map<String, Object> map) {
		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();
			
	//		taskService = processEngine.getTaskService();
			taskService.claim(taskId, username);
			taskService.setVariablesLocal(taskId, map);
			taskService.complete(taskId);
			
			tm.commit();
		}catch(Exception e){
			throw new ProcessException(e);
		}finally {
			tm.release();
		}
	}
	
	/**
	 * 完成任务(先领用再完成)
	 * 
	 * @param taskId
	 * @param map
	 */
	public void completeTaskWithLocalVariables(String taskId, String username,
			Map<String, Object> map,String destinationTaskKey) {
		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();
			
	//		taskService = processEngine.getTaskService();
			taskService.claim(taskId, username);
			taskService.setVariablesLocal(taskId, map);
			taskService.completeWithDest(taskId, destinationTaskKey);
			
			tm.commit();
		}catch(Exception e){
			throw new ProcessException(e);
		}finally {
			tm.release();
		}
	}

	/**
	 * 获得历史任务实例by流程实例ID
	 * 
	 * @param processId
	 *            流程实例ID
	 * @return
	 */
	public List<HistoricTaskInstance> getHisTaskByProcessId(String processId) {
//		historyService = processEngine.getHistoryService();
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
			throw new ProcessException(e);
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
			throw new ProcessException(e);
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
			throw new ProcessException(e);
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
			throw new ProcessException(e);
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
			throw new ProcessException(e);
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
//		taskService = processEngine.getTaskService();
		taskService.claim(taskId, username);
	}

	/**
	 * 为任务指定代理处理用户
	 * 
	 * @param taskId
	 * @param userId
	 */
	public void delegateTask(String taskId, String userId) {
		taskService.delegateTask(taskId, userId);
	}

	/**
	 * 部署流程
	 * 
	 * @return
	 */
	public Deployment deployProcDefByZip(String deploymentName, String zip) {


		/**
		 * 部署图片时，命名必须为leave.activiti.jpg或leave.activiti.{processKey}.jpg
		 * act_re_procdef.DGRM_RESOURCE_NAME_
		 */
		ZipInputStream inputStream = null;
		try {
			inputStream = new ZipInputStream(new FileInputStream(new File(zip)));
		} catch (Exception e) {
			throw new ProcessException(e);
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
		Deployment deploy = null;
		if(jpgPath != null && !jpgPath.equals(""))
		{
			deploy = repositoryService.createDeployment()
					.name(deploymentName).addClasspathResource(xmlPath)
			.addClasspathResource(jpgPath).deploy();
		}
		else
		{
			deploy = repositoryService.createDeployment()
				.name(deploymentName).addClasspathResource(xmlPath).deploy();
		}

		return deploy;
	}
	
	
	/**
	 * 部署流程
	 * 
	 * @return
	 */
	public Deployment deployProcDefByZip(String deploymentName, String zip,int deploypolicy) {


		/**
		 * 部署图片时，命名必须为leave.activiti.jpg或leave.activiti.{processKey}.jpg
		 * act_re_procdef.DGRM_RESOURCE_NAME_
		 */
		ZipInputStream inputStream = null;
		try {
			inputStream = new ZipInputStream(new FileInputStream(new File(zip)));
		} catch (Exception e) {
			throw new ProcessException(e);
		}

		Deployment deploy = repositoryService.createDeployment()
				.name(deploymentName)// act_re_deployment.NAME
				.addZipInputStream(inputStream).deploy(deploypolicy);

		return deploy;
	}

	/**
	 * 部署流程
	 * 
	 * @return
	 */
	public Deployment deployProcDefByPath(String deploymentName,
			String xmlPath, String jpgPath,int deploypolicy) {
		Deployment deploy = null;
		/**
		 * 参数deploypolicy可以为以下常量值：
		 *  DeploymentBuilder.Deploy_policy_default 
	 	 *	DeploymentBuilder.Deploy_policy_upgrade 
	     *  DeploymentBuilder.Deploy_policy_delete 
	     *
		 */
		if(jpgPath != null && !jpgPath.equals(""))
		{
			deploy = repositoryService.createDeployment()
					.name(deploymentName).addClasspathResource(xmlPath)
			.addClasspathResource(jpgPath).deploy(deploypolicy);
		}
		else
		{
			deploy = repositoryService.createDeployment()
				.name(deploymentName).addClasspathResource(xmlPath).deploy(deploypolicy);
		}

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
		
		ProcessInstance processInstance = runtimeService
				.startProcessInstanceByKey(process_key,
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
	 * 启动流程
	 * 
	 * @param businessKey
	 * @param variableMap
	 * @param process_key
	 * identityService.setAuthenticatedUserId(initor);
	 * @return
	 */
	public ProcessInstance startProcDef(
			Map<String, Object> map, String process_key,String initor)
	{
		identityService.setAuthenticatedUserId(initor);
		ProcessInstance processInstance = runtimeService
				.startProcessInstanceByKey(process_key,  map);
		return processInstance;	
	}
	

	/**
	 * 启动流程
	 * 
	 * @param businessKey
	 * @param variableMap
	 * @param process_key
	 * identityService.setAuthenticatedUserId(initor);
	 * @return
	 */
	public ProcessInstance startProcDef(String businessKey, String process_key,
			Map<String, Object> map,String initor)
	{
		identityService.setAuthenticatedUserId(initor);
		ProcessInstance processInstance = runtimeService
				.startProcessInstanceByKey(process_key, businessKey, map);
		return processInstance;	
	}
	
	
	/**
	 * 启动流程
	 * 
	 * @param businessKey
	 * @param variableMap
	 * @param process_key
	 * identityService.setAuthenticatedUserId(initor);
	 * @return
	 */
	public ProcessInstance startProcDef( String process_key,
			Map<String, Object> map,String initor)
	{
		identityService.setAuthenticatedUserId(initor);
		ProcessInstance processInstance = runtimeService
				.startProcessInstanceByKey(process_key,  map);
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
		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();

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
			tm.commit();
		} catch (Exception e) {
			throw new ProcessException(e);
		}finally {
			tm.release();
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
		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();
			
			List<ProcessDefinition> pdList = repositoryService
					.createProcessDefinitionQuery().processDefinitionKey(key)
					.list();
			if (pdList != null && !pdList.isEmpty()) {
				for (ProcessDefinition pd : pdList) {
					repositoryService.deleteDeployment(pd.getDeploymentId(),
							true);
					this.activitiConfigService.deleteActivitiNodeInfo(pd
							.getKey());

					WfAppProcdefRelation relation = new WfAppProcdefRelation();
					relation.setProcdef_id(pd.getId());
					activitiRelationService.deleteAppProcRelation(relation);
				}
			}
			
			tm.commit();
		} catch (Exception e) {
			throw new ProcessException(e);
		} finally {
			tm.release();
		}
	}

	public List<ProcessDefinition> findAllRemoveListByProcessDeploymentids(
			String[] processDeploymentids) {
		
		List<ProcessDefinition> removeList = new ArrayList<ProcessDefinition>();
		for (String processDeploymentid : processDeploymentids) {
			
			if (StringUtil.isNotEmpty(processDeploymentid)){
				
				List<ProcessDefinition> pdList = processEngine
						.getRepositoryService().createProcessDefinitionQuery()
						.processDefinitionKey(processDeploymentid).list();
				if (pdList != null && !pdList.isEmpty()) {
					removeList.addAll(pdList);
				}
			}
		}
		
		return removeList;
	}

	/**
	 * 获取流程图
	 * 
	 * @param execId
	 * @param process_key
	 * @deprecated
	 * @return
	 */
	public ActivityImpl traceProcess(String execId, String process_key) {

		return getActivityImpl(execId);
	}
	/**
	 * 获取流程图
	 * 
	 * @param execId
	 * @param process_key
	 * @return
	 */
	public ActivityImpl getActivityImpl(String execId) {

		
		ExecutionEntity execution = (ExecutionEntity) runtimeService
				.createExecutionQuery().executionId(execId).singleResult();// 执行实例
		if (execution == null) {
			return null;
		}
		
		ProcessDefinitionEntity def =  (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition(execution.getProcessDefinitionId());
     
		return def.findActivity(execution.getActivityId());

		
	}

	public ActivityImpl getActivityImplById(String actDefId,
			String processInstanceId) {
		ActivityImpl actImpl = null;
		ProcessInstance processInstance = runtimeService
				.createProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();
		if(processInstance == null)
			return null;
		ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition(processInstance
						.getProcessDefinitionId());
		List<ActivityImpl> activitiList = def.getActivities();// 获得当前任务的所有节点
		for (ActivityImpl activityImpl : activitiList) {
			String id = activityImpl.getId();
			if (id.equals(actDefId)) {// 获得执行到那个节点
				actImpl = activityImpl;
				break;
			}
		}
		return actImpl;
	}
	
	public ActivityImpl getActivityImplByDefId(String actDefId,
			String processDefineId) {
		ActivityImpl actImpl = null;
	
		ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition(processDefineId);
		return def.findActivity(actDefId);
	}

	/**
	 * 获取任务列表
	 * 
	 * @param execId
	 * @return
	 */
	public List<Task> listTaskByExecId(String execId) {
		List<Task> taskList = taskService.createTaskQuery().executionId(execId)
				.list();

		return taskList;
	}

	/**
	 * @param processInstanceId
	 * @return
	 */
	public ProcessInstance getProcessInstanceById(String processInstanceId) {
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
		HistoricProcessInstance processInstance = historyService
				.createHistoricProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();
		return processInstance;
	}

	/**
	 * 根据流程定义ID查询流程实例
	 * 
	 * @param processDefinitionId
	 * @return
	 */
	public ProcessDefinition getProcessDefinitionById(String processDefinitionId) {
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
		List<Task> list = new ArrayList<Task>();
		List<TaskEntity> userTaskList = null;
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("username", username);
			userTaskList = executor.queryListBean(TaskEntity.class,
					"selectAllTaskByUser", map);
			list.addAll(userTaskList);
		} catch (Exception e) {
			throw new ProcessException(e);
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
		ListInfo listInfo = null;
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("username", username);
			listInfo = executor.queryListInfoBean(TaskEntity.class,
					"selectAllTaskByUser",offset,pagesize, map);
		} catch (Exception e) {
			throw new ProcessException(e);
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
	
	public int countTasksByUserWithStates(List<String> processkeys, List<String> states, String userAccount)
	  {
	    try
	    {
	      Map map = new HashMap();
	      map.put("userAccount", userAccount);
	      map.put("states", states);
	      map.put("processkeys", processkeys);

	      return ((Integer)this.executor.queryObjectBean(Integer.class, "countlistTaskAndVarsByUserWithStates", map)).intValue();
	    } catch (Exception e) {
	    	throw new ProcessException("获取待办任务数失败：processkeys=" + processkeys.toString() + ",states=" + states.toString() + ",userAccount=" + userAccount, e);
	    }
	    
	  }
	
	 public ListInfo listTaskAndVarsByUserWithStates(Class paramClass, List<String> processkeys, List<String> states, String userAccount, long offset, int pagesize)
	  {
	    ListInfo listInfo = null;
	    try {
	      Map map = new HashMap();
	      map.put("userAccount", userAccount);
	      map.put("states", states);
	      map.put("processkeys", processkeys);
	      listInfo = this.executor.queryListInfoBean(paramClass, "listTaskAndVarsByUserWithStates", offset, pagesize, map);
	    }
	    catch (Exception e)
	    {
	      throw new ProcessException("获取待办任务失败：processkeys=" + processkeys.toString() + ",states=" + states.toString() + ",userAccount=" + userAccount + ",offset=" + offset + ",pagesize=" + pagesize, e);
	    }
	    return listInfo;
	  }

	/**
	 * 根据用户名,流程KEY数组查询待办任务
	 * 
	 * @param processKey
	 * @param username
	 * @return
	 */
	public List<Task> listTaskByUser(String[] processKeys, String username) {
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
			throw new ProcessException(e);
		}
		return list;
	}
	
	/** 根据条件获取任务列表,分页展示 gw_tanx
	 * @param task
	 * @param offset
	 * @param pagesize
	 * @return
	 * 2014年5月14日
	 */
	public ListInfo queryTasks(TaskCondition task, long offset, int pagesize) {
		ListInfo listInfo = null;
		try {
			// 数据查看权限管控
			task.setAdmin(AccessControl.getAccessControl().isAdmin());
			// 当前用户登录id
			task.setAssignee(AccessControl.getAccessControl()
					.getUserAccount());
			
			if(StringUtil.isNotEmpty(task.getProcessIntsId())){
				task.setProcessIntsId("%"+task.getProcessIntsId()+"%");
			}
			
			if(StringUtil.isNotEmpty(task.getTaskName())){
				task.setTaskName("%"+task.getTaskName()+"%");
			}
			
			if(StringUtil.isNotEmpty(task.getTaskId())){
				task.setTaskId("%"+task.getTaskId()+"%");
			}
			
			listInfo = executor.queryListInfoBean(TaskManager.class, 
					"selectTaskByUser_wf",offset, pagesize, task);
			
			// 获取分页中List数据
			List<TaskManager> taskList = listInfo.getDatas();
			
			dealTaskInfo(taskList);
			
			return listInfo;
		} catch (Exception e) {
			throw new ProcessException(e);
		}
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
		List<Task> userTaskList = new ArrayList<Task>();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			String[] processKeys = new String[]{processKey};
			map.put("processKeys", processKeys);
			map.put("username", username);
			userTaskList.addAll(executor.queryListBean(TaskEntity.class,
					"selectAllTaskByUser", map));
		} catch (Exception e) {
			throw new ProcessException(e);
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
		ListInfo userTaskList = null;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			String[] processKeys = new String[]{processKey};
			map.put("processKeys", processKeys);
			map.put("username", username);
			userTaskList = executor.queryListInfoBean(TaskEntity.class,
					"selectAllTaskByUser",offset,pagesize, map);
		} catch (Exception e) {
			throw new ProcessException(e);
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
		ListInfo userTaskList = null;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("processKeys", processKeys);
			map.put("username", username);
			userTaskList = executor.queryListInfoBean(TaskEntity.class,
					"selectAllTaskByUser",offset,pagesize, map);
		} catch (Exception e) {
			throw new ProcessException(e);
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
		Deployment deploy = repositoryService.createDeployment()
				.name(deploymentName).addClasspathResource(xmlPath).deploy();
		

		return deploy;
	}
	public Deployment deployProcDefByPath(String deploymentName, String xmlPath,int deploypolicy) {
		
		/**
		 * 参数deploypolicy可以为以下常量值：
		 *  DeploymentBuilder.Deploy_policy_default 
	 	 *	DeploymentBuilder.Deploy_policy_upgrade 
	     *  DeploymentBuilder.Deploy_policy_delete 
	     *
		 */
		
		 Deployment deploy = repositoryService.createDeployment()
				.name(deploymentName).addClasspathResource(xmlPath).deploy(deploypolicy);
		

		return deploy;
	}

	public Deployment deployProcDefByInputStream(String deploymentName,
			String xmlPath, InputStream processDef) {
		DeploymentBuilder deploymentBuilder = processEngine
				.getRepositoryService().createDeployment().name(deploymentName);
		deploymentBuilder.addInputStream(xmlPath, processDef);

		return deploymentBuilder.deploy();
	}
	
	public Deployment deployProcDefByInputStream(String deploymentName,
			String xmlPath, InputStream processDef,int upgradepolicy) {
		DeploymentBuilder deploymentBuilder = processEngine
				.getRepositoryService().createDeployment().name(deploymentName);
		deploymentBuilder.addInputStream(xmlPath, processDef);

		return deploymentBuilder.deploy(upgradepolicy);
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
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sany.workflow.service.ActivitiService#deployProcDefByZip(java.lang
	 * .String, java.util.zip.ZipInputStream)
	 */
	public Deployment deployProcDefByZip(String deploymentName,
			ZipInputStream processDef,int upgradepolicy) {
		DeploymentBuilder deploymentBuilder = processEngine
				.getRepositoryService().createDeployment().name(deploymentName);
		deploymentBuilder.addZipInputStream(processDef);		
		/**
		 * 参数upgradepolicy可以为以下常量值：
		 *  DeploymentBuilder.Deploy_policy_default 
	 	 *	DeploymentBuilder.Deploy_policy_upgrade 
	     *  DeploymentBuilder.Deploy_policy_delete 
	     *
		 */			
		return deploymentBuilder.deploy(upgradepolicy);
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

			Deployment deployment = repositoryService
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
			if(StringUtils.isNotEmpty(processDefCondition.getWf_app_name())){
				processDefCondition.setWf_app_name("%"+processDefCondition.getWf_app_name()+"%");
			}
			if(StringUtils.isNotEmpty(processDefCondition.getWf_app_mode_type_nonexist())){
				processDefCondition.setWf_app_mode_type_nonexist("%"+processDefCondition.getWf_app_mode_type_nonexist()+"%");
			}
			if(StringUtils.isNotEmpty(processDefCondition.getProcessName())){
				processDefCondition.setProcessName("%"+processDefCondition.getProcessName()+"%");
			}
			ListInfo listInfo = executor.queryListInfoBean(ProcessDef.class, "queryProdef", offset, pagesize, processDefCondition);
			return listInfo;
		}catch(Exception e){
			throw new ProcessException(e);
		}

	}

	public void deleteDeploymentCascade(String[] processDeploymentids,
			boolean[] cascades) {
//		processDeploymentids = Arrays.toString(processDeploymentids)
//				.replace("[", "").replace("]", "").split(",");
		if (cascades != null && cascades.length > 0) {
			for (int i = 0; i < processDeploymentids.length; i++) {
				repositoryService.deleteDeployment(
						processDeploymentids[i], cascades[i]);
			}
		} else {
			for (int i = 0; i < processDeploymentids.length; i++) {
				repositoryService.deleteDeployment(
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
		
		for (String processDeploymentid : processDeploymentids) {
			
			if (StringUtil.isNotEmpty(processDeploymentid)){
				
				deleteProcDef(processDeploymentid);
			}
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
		repositoryService.activateProcessDefinitionById(
				processId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sany.workflow.service.ActivitiService#supendProcess(java.lang.String)
	 */
	public void suspendProcess(String processId) {
		repositoryService.suspendProcessDefinitionById(
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
		return repositoryService.getResourceAsStream(
				deploymentId, resourceName);
	}
	
	public void getProccessPic(String processDefId, OutputStream out) throws IOException {
		InputStream is = null;
		try
		{
			if(processDefId!=null&&!processDefId.equals("")){
				ProcessDefinition processDefinition = getProcessDefinitionById(processDefId);
				String diagramResourceName = processDefinition.getDiagramResourceName();
				
				is = getResourceAsStream(processDefinition.getDeploymentId(),
						diagramResourceName);
				
				byte[] b = new byte[1024];
				int len = -1;
	//			OutputStream out = response.getOutputStream();
				while ((len = is.read(b, 0, 1024)) != -1) {
					out.write(b, 0, len);
				}
				out.flush();
			}
		}
		finally
		{
			try {
				if(is != null)
					is.close();
			} catch (Exception e) {
				
			}
		}
	}
	
	/**根据流程实例id获取对应版本的流程图 gw_tanx
	 * @param processInstId
	 * @param out
	 * @throws IOException
	 * 2014年5月13日
	 */
	public void getProccessActivePic(String processInstId, OutputStream out) {
		InputStream is = null;
		try {
			if (processInstId != null && !processInstId.equals("")) {
				// 运行中的活动id集合
				List<String> hightLightList = new ArrayList<String>();
				// 根据流程实例ID获取运行的实例
				List<Execution> exectionList = runtimeService
						.createExecutionQuery()
						.processInstanceId(processInstId).list();
				// 获取运行实例的运行活动节点
				for (Execution execution : exectionList) {
					ExecutionEntity exeEntity = (ExecutionEntity) runtimeService
							.createExecutionQuery()
							.executionId(execution.getId()).singleResult();
					String activitiId = exeEntity.getActivityId();
					hightLightList.add(activitiId);
				}
				// 根据流程实例iD获取流程定义KEY
				HistoricProcessInstance hiInstance = getHisProcessInstanceById(processInstId);
				// 根据流程定义ID获取流程定义对应的实体对象
				BpmnModel bpmnModel = repositoryService.getBpmnModel(hiInstance
						.getProcessDefinitionId());

				is = ProcessDiagramGenerator.generateDiagram(bpmnModel, "png",
						hightLightList);

				byte[] b = new byte[1024];
				int len = -1;
				while ((len = is.read(b, 0, 1024)) != -1) {
					out.write(b, 0, len);
				}
				out.flush();
			}
		} catch (Exception e) {
			throw new ProcessException(e);
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (Exception e1) {
				throw new ProcessException(e1);
			}
		}
	}
	
	public void getProccessPicByProcessKey(String processKey, OutputStream out) throws IOException {
		InputStream is = null;
		try
		{
			if(processKey!=null&&!processKey.equals("")){
				ProcessDefinition processDefinition = this.getProcessDefinitionByKey(processKey);
				String diagramResourceName = processDefinition.getDiagramResourceName();
				
				is = getResourceAsStream(processDefinition.getDeploymentId(),
						diagramResourceName);
				
				byte[] b = new byte[1024];
				int len = -1;
	//			OutputStream out = response.getOutputStream();
				while ((len = is.read(b, 0, 1024)) != -1) {
					out.write(b, 0, len);
				}
				out.flush();
			}
		}
		finally
		{
			try {
				if(is != null)
					is.close();
			} catch (Exception e) {
				
			}
		}
	}
	
	/**
	 * 获取流程定义xml
	 * @param processId
	 * @return
	 * @throws IOException
	 */
	public String getProccessXML(String processId) throws IOException 
	{
		return getProccessXML(processId,"UTF-8");
	}
	
	/**
	 * 获取流程定义xml
	 * @param processId
	 * @return
	 * @throws IOException
	 */
	public String getProccessXML(String processId,String encode) throws IOException 
	{
	
		ByteArrayOutputStream out = null;
		InputStream is = null;
		try
		{
			if(processId!=null&&!processId.equals("")){
				ProcessDefinition processDefinition = getProcessDefinitionById(processId);
				String diagramResourceName = processDefinition.getResourceName();
				
				is = getResourceAsStream(processDefinition.getDeploymentId(),
						diagramResourceName);
				
				byte[] b = new byte[1024];
				int len = -1;
				out = new ByteArrayOutputStream();
				while ((len = is.read(b, 0, 1024)) != -1) {
					out.write(b, 0, len);
				}
				return new String(out.toByteArray(),encode);
			}
			return null;
		}
		finally
		{
			try {
				if(out != null)
					out.close();
			} catch (Exception e) {
				
			}
			try {
				if(is != null)
					is.close();
			} catch (Exception e) {
				
			}
		}
	}
	
	
	/**
	 * 获取流程定义xml
	 * @param processId
	 * @return
	 * @throws IOException
	 */
	public String getProccessXMLByKey(String processKey) throws IOException 
	{
		return getProccessXMLByKey(processKey,null,"UTF-8");
	}
	
	/**
	 * 获取流程定义xml
	 * @param processId
	 * @return
	 * @throws IOException
	 */
	public String getProccessXMLByKey(String processKey,String version,String encode) throws IOException 
	{
		ByteArrayOutputStream out = null;
		InputStream is = null;
		try
		{
			if(processKey!=null&&!processKey.equals("")){
				ProcessDef processDefinition = this.queryProdefByKey(processKey, version);
				String diagramResourceName = processDefinition.getRESOURCE_NAME_();
				
				is = getResourceAsStream(processDefinition.getDEPLOYMENT_ID_(),
						diagramResourceName);
				
				byte[] b = new byte[1024];
				int len = -1;
				out = new ByteArrayOutputStream();
				while ((len = is.read(b, 0, 1024)) != -1) {
					out.write(b, 0, len);
				}
				return new String(out.toByteArray(),encode);
			}
			return null;
		}
		finally
		{
			try {
				if(out != null)
					out.close();
			} catch (Exception e) {
				
			}
			try {
				if(is != null)
					is.close();
			} catch (Exception e) {
				
			}
		}
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
	public ProcessDef queryProdefByKey(String processKey,String version) {
		try {
			if(StringUtil.isEmpty(version))
			{
				return this.executor.queryObject(ProcessDef.class, "queryProdefByKey", processKey);
			}
			else
			{
				return this.executor.queryObject(ProcessDef.class, "queryProdefByKeywithVersion", processKey,version);
				
			}
		} catch (SQLException e) {
			throw new ProcessException(e);
		}
	}
	
	public ProcessDef queryProdefByKey(String processKey) {
		try {
			return this.executor.queryObject(ProcessDef.class, "queryProdefByKey", processKey);
			
		} catch (SQLException e) {
			throw new ProcessException(e);
		}
	}
	
	
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sany.workflow.service.ActivitiService#getNewestTask(java.lang.String)
	 */
	@Override
	public Task getCurrentTask(String processInstanceId) {
		List<Task> taskList = taskService.createTaskQuery().processInstanceId(processInstanceId)
				.orderByTaskId().desc().list();

		if (!CollectionUtils.isEmpty(taskList)) {
			return taskList.get(0);
		}

		return null;
	}
	
	public List<Task> getCurrentTaskList(String processInstanceId) {
		List<Task> taskList = taskService.createTaskQuery().processInstanceId(processInstanceId)
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
    	return taskService.createTaskQuery().processInstanceId(processInstanceId).list();
    }
	
	public List<ProcessDef> getUnloadProcesses()
	{
		
		try {
			return executor.queryList(ProcessDef.class, "getUnloadProcesses");
		} catch (SQLException e) {
			throw new ProcessException(e);
		}
	}

	@Override
	public String loadProcess(List<LoadProcess> loadprocesses) {
		if(loadprocesses == null || loadprocesses.size() == 0)
			return "没有待装载的流程信息";
		TransactionManager tm = new TransactionManager();
		try
		{
			StringBuffer ret = new StringBuffer();
			tm.begin();
			ret.append("成功装载以下流程：<br>");
			for(int i =0 ; i < loadprocesses.size(); i ++)
			{
				
				LoadProcess LoadProcess = loadprocesses.get(i);
				ret.append(LoadProcess.getProcessKey()).append("->").append(LoadProcess.getProcessName()).append("<br>");
				activitiConfigService.addActivitiNodeInfo(LoadProcess.getProcessKey());	
				activitiConfigService.addProBusinessType(LoadProcess.getProcessKey(), LoadProcess.getBusinessType());
				ProcessDef proc = new ProcessDef();
				proc.setID_(LoadProcess.getProcdef_id());
				activitiRelationService.addAppProcRelation(proc, LoadProcess.getWf_app_id());
				
			}
			tm.commit();
			return ret.toString();
		}
		catch(Exception e)
		{
			throw new ProcessException(e); 
		}
		finally
		{
			tm.release();
		}
		
	}

	@Override
	public List<ProcessDef> queryProdefHisVersion(
			String processKey) {
		try {
			
			{
				return this.executor.queryList(ProcessDef.class,"queryProdefHisVersion",processKey);
			}
//			else
//			{
//				return this.executor.queryList(ProcessDef.class,"queryProdefHisVersionWithCurrentVersion",processKey,version);
//			}
		} catch (Exception e) {
			throw new ProcessException(e); 
		}
		
	}

	@Override
	public void destroy() throws Exception {
		
		
	}

	@Override
	public ListInfo queryProcessInsts(long offset, int pagesize,
			ProcessInstCondition processInstCondition) {

		TransactionManager tm = new TransactionManager();

		try {

			tm.begin();

			// 流程实例ID
			if (StringUtil.isNotEmpty(processInstCondition.getWf_Inst_Id())) {
				processInstCondition.setWf_Inst_Id("%"
						+ processInstCondition.getWf_Inst_Id() + "%");
			}

			// 业务主键
			if (StringUtil.isNotEmpty(processInstCondition.getWf_business_key())) {
				processInstCondition.setWf_business_key("%"
						+ processInstCondition.getWf_business_key() + "%");
			}

			// 分页获取流程实例数据
			ListInfo listInfo = executor.queryListInfoBean(ProcessInst.class,
					"queryProInst_wf", offset, pagesize, processInstCondition);

			List<ProcessInst> processInstList = listInfo.getDatas();

			if (processInstList != null && processInstList.size() != 0) {

				for (int i = 0; i < processInstList.size(); i++) {

					ProcessInst pi = processInstList.get(i);

					// 耗时转换
					if (StringUtil.isNotEmpty(pi.getDURATION_())) {
						long mss = Long.parseLong(pi.getDURATION_());
						pi.setDURATION_(formatDuring(mss));
					} else {
						// 流程未结束，以系统当前时间计算耗时
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy-MM-dd hh:mm:ss");
						Date startTime = sdf.parse(pi.getSTART_TIME_());

						pi.setDURATION_(formatDuring(new Date().getTime()
								- startTime.getTime()));
					}

					// 流程实例完成，不需要查询处理人和签收人
					if (StringUtil.isEmpty(pi.getSUSPENSION_STATE_())) {

						continue;
					}

					// 根据流程实例id获取任务信息
					List<TaskManager> taskList = executor.queryList(
							TaskManager.class, "getTaskInfoById_wf",
							pi.getPROC_INST_ID_());

					// 任务列表数据处理(处理人/组，行转列)
					dealTaskInfo(taskList);

					pi.setTaskList(taskList);

				}
			}

			tm.commit();

			return listInfo;
		} catch (Exception e) {
			throw new ProcessException(e);
		} finally {
			tm.release();
		}
	}
	
	/** 耗时转换 gw_tanx
	 * @param mss
	 * @return
	 * 2014年5月21日
	 */
	private String formatDuring(long mss) {
		long days = mss / (1000 * 60 * 60 * 24);
		long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
		long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
		long seconds = (mss % (1000 * 60)) / 1000;
		
		StringBuffer sb = new StringBuffer();
		if (days != 0){
			sb.append( days + "天");
		}
		if (hours != 0) {
			sb.append( hours + "时");
		}
		if (minutes != 0) {
			sb.append( minutes + "分");
		}
		if (seconds != 0) {
			sb.append( seconds + "秒");
		}
		
		return sb.toString();
	}
	
	/** 任务列表数据处理 gw_tanx
	 * @param taskList
	 * @throws Exception
	 * 2014年5月20日
	 */
	private void dealTaskInfo(List<TaskManager> taskList) throws Exception{
		if (taskList != null && taskList.size() != 0) {
			
			for (int j = 0; j < taskList.size(); j++) {
				
				TaskManager tm = taskList.get(j);
				
				// 实时任务中没有act_type_字段值，历史任务有
				if (tm.getACT_TYPE_() != null && !tm.getACT_TYPE_().equals("userTask")) {
					continue;
				}
				
				// 任务已签收，处理人 = 签收人
				if (StringUtil.isNotEmpty(tm.getASSIGNEE_())) {
					
					tm.setUSER_ID_(tm.getASSIGNEE_());
					continue;
				}
				
				// 任务未签收，根据任务id查询任务可处理人
				List<HashMap> candidatorList = executor.queryList(HashMap.class, 
						"getCandidatorOftask_wf", tm.getID_());
				
				StringBuffer users = new StringBuffer();
				StringBuffer groups = new StringBuffer();
				
				if ( candidatorList != null && candidatorList.size() != 0 ){
					
					for (int k = 0;  k < candidatorList.size(); k++){
						HashMap candidatorMap = candidatorList.get(k);
						
						// 处理人行转列
						String userId = (String)candidatorMap.get("USER_ID_");
						if(StringUtil.isNotEmpty(userId)){
							
							if(k == 0){	
								users.append(userId);
							}else{
								users.append(",").append(userId);
							}
						}
						
						// 处理组行转列
						String group = (String)candidatorMap.get("GROUP_ID_");
						if(StringUtil.isNotEmpty(group)){
							
							if(k == 0){	
								groups.append(group);
							}else{
								groups.append(",").append(userId);
							}
						}
					}
				}
				tm.setUSER_ID_(users.toString());
				tm.setGROUP_ID(groups.toString());	
				
			}
		}
	}

	@Override
	public void cancleProcessInstances(String processInstids,String deleteReason) {
		
		String[] ids = processInstids.split(",");
		
		TransactionManager tm = new TransactionManager();

		try {
			tm.begin();
			
			for (String processInstid : ids) {
				
				if (StringUtil.isNotEmpty(processInstid)){
					
					// 获取流程实例信息
					ProcessInst pi = executor.queryObject(ProcessInst.class, "queryProInstById_wf", processInstid);
					
					// 流程完成，不需要进行逻辑删除
					if (pi == null ) {
						continue;
					}
					
					this.runtimeService.deleteProcessInstance(processInstid,
							deleteReason);
				}
			}
			
			tm.commit();
		}catch(Exception e){
			throw new ProcessException(e);
		}
	}

	@Override
	public void upgradeInstances(String processKey) throws Exception {
		instanceUpgrade.upgradeInstances(processKey);
		
	}
	
	
	/** 删除某个具体流程(物理删除) gw_tan
	 * @param processInstid
	 * @throws Exception
	 * 2014年5月21日
	 */
	private void delProcessInstance(String processInstid) throws Exception{
		PreparedDBUtil dbUtil = new PreparedDBUtil();
		
		// 判断是否存在子流程
		dbUtil.preparedSelect("SELECT A.PROC_INST_ID_ FROM ACT_RU_EXECUTION A WHERE A.SUPER_EXEC_= ?");
		dbUtil.setString(1, processInstid);
		dbUtil.executePrepared();
		
		if (dbUtil.size() > 0){
			
			for (int i = 0 ; i< dbUtil.size();i++) {
				
				// 递归删除子流程
				delProcessInstance(dbUtil.getString(i, "PROC_INST_ID_"));
				
			}
			
		}
		
		// 获取流程实例信息
		ProcessInst pi = executor.queryObject(ProcessInst.class, "queryProInstById_wf", processInstid);
		
		// 流程完成，不需要进行逻辑删除
		if (pi != null) {
			// 停止流程实例在引擎中所有的逻辑关系
			cancleProcessInstance(processInstid,"物理删除");
		}
								
		dbUtil.preparedDelete("delete From act_ru_event_subscr a where a.proc_inst_id_=?");
		dbUtil.setString(1, processInstid);
		dbUtil.addPreparedBatch();
					
		dbUtil.preparedDelete("delete From act_ru_variable b where b.proc_inst_id_ =?");
		dbUtil.setString(1, processInstid);
		dbUtil.addPreparedBatch();
		
		dbUtil.preparedDelete("delete From act_ru_job c where c.process_instance_id_=?");
		dbUtil.setString(1, processInstid);
		dbUtil.addPreparedBatch();
		
		dbUtil.preparedDelete("delete From act_ru_identitylink d where d.proc_inst_id_ =?");
		dbUtil.setString(1, processInstid);
		dbUtil.addPreparedBatch();
		
		dbUtil.preparedDelete("delete From act_ru_task e where e.proc_inst_id_ =?");
		dbUtil.setString(1, processInstid);
		dbUtil.addPreparedBatch();
		
		dbUtil.preparedDelete("delete From act_ru_execution f where f.proc_inst_id_ =?");
		dbUtil.setString(1, processInstid);
		dbUtil.addPreparedBatch();
		
		dbUtil.preparedDelete("delete From act_hi_varinst g where g.proc_inst_id_ =?");
		dbUtil.setString(1, processInstid);
		dbUtil.addPreparedBatch();
		
		dbUtil.preparedDelete("delete From act_hi_taskinst h where h.proc_inst_id_ =?");
		dbUtil.setString(1, processInstid);
		dbUtil.addPreparedBatch();
		
		dbUtil.preparedDelete("delete From act_hi_procinst i where i.proc_inst_id_ =?");
		dbUtil.setString(1, processInstid);
		dbUtil.addPreparedBatch();
		
		dbUtil.preparedDelete("delete From act_hi_detail j where j.proc_inst_id_ =?");
		dbUtil.setString(1, processInstid);
		dbUtil.addPreparedBatch();
		
		dbUtil.preparedDelete("delete From act_hi_comment k where k.proc_inst_id_ =?");
		dbUtil.setString(1, processInstid);
		dbUtil.addPreparedBatch();
		
		dbUtil.preparedDelete("delete From act_hi_attachment l where l.proc_inst_id_ =?");
		dbUtil.setString(1, processInstid);
		dbUtil.addPreparedBatch();
		
		dbUtil.preparedDelete("delete From act_hi_actinst m where m.proc_inst_id_ =?");
		dbUtil.setString(1, processInstid);
		dbUtil.addPreparedBatch();		
		
		dbUtil.executePreparedBatch();
			
	}

	@Override
	public void delProcessInstances(String processInstanceIds) {

		TransactionManager tm = new TransactionManager();

		try {
			tm.begin();
			
			String[] ids = processInstanceIds.split(",");

			for (String processInstid : ids) {

				if (StringUtil.isNotEmpty(processInstid)) {

					delProcessInstance(processInstid);
				}
			}
			tm.commit();
		} catch (Exception e) {
			throw new ProcessException(e);
		}finally {
			tm.release();
		}

	}

	@Override
	public ProcessInst getProcessInstById(String processInstId) {

		TransactionManager tm = new TransactionManager();

		try {
			tm.begin();

			ProcessInstCondition pic = new ProcessInstCondition();
			pic.setWf_Inst_Id(processInstId);

			// 获取流程实例信息
			ProcessInst pi = executor.queryObjectBean(ProcessInst.class,
					"queryProInst_wf", pic);

			// 根据流程实例id获取任务信息
			List<TaskManager> taskList = executor.queryList(TaskManager.class,
					"getTaskInfoById_wf", pi.getPROC_INST_ID_());

			if (taskList != null && taskList.size() != 0) {

				// 任务列表数据处理(处理人/组，行转列)
				dealTaskInfo(taskList);

				pi.setTaskList(taskList);

			}
			
			tm.commit();
			
			return pi;
		} catch (Exception e) {
			throw new ProcessException(e);
		}finally {
			tm.release();
		}
	}

	@Override
	public List<TaskManager> queryHistorTasks(String processInstId) {

		TransactionManager tms = new TransactionManager();

		try {
			tms.begin();

			List<TaskManager> taskList = executor.queryList(TaskManager.class,
					"selectTaskHistorById_wf", processInstId);

			// 获取处理人列表
			dealTaskInfo(taskList);

			// 耗时处理
			if (taskList != null && taskList.size() != 0) {
				for (int i = 0; i < taskList.size(); i++) {
					TaskManager tm = taskList.get(i);

					if (StringUtil.isNotEmpty(tm.getDURATION_())) {
						long mss = Long.parseLong(tm.getDURATION_());
						tm.setDURATION_(formatDuring(mss));
					} else {
						// 流程未结束，以系统当前时间计算耗时
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy-MM-dd hh:mm:ss");
						Date startTime = sdf.parse(tm.getSTART_TIME_());

						tm.setDURATION_(formatDuring(new Date().getTime()
								- startTime.getTime()));
					}
				}
			}

			tms.commit();

			return taskList;
		} catch (Exception e) {
			throw new ProcessException(e);
		} finally {
			tms.release();
		}
	}

	@Override
	public ListInfo queryHistorTasks(TaskCondition task, long offset,
			int pagesize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void suspendProcessInst(String processInstId) {
		this.runtimeService.suspendProcessInstanceById(processInstId);
	}

	@Override
	public void activateProcessInst(String processInstId) {
		this.runtimeService.activateProcessInstanceById(processInstId);
	}

}
