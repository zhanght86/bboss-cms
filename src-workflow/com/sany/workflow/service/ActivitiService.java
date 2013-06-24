package com.sany.workflow.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import com.frameworkset.util.ListInfo;
import com.sany.workflow.entity.ProcessDef;
import com.sany.workflow.entity.ProcessDefCondition;

public interface ActivitiService {

	  /**
	   * 将当前任务驳回到上一个任务处理人处，并更新流程变量参数
	   * @param taskId
	   * @param variables
	   */
	  void rejecttoPreTask(String taskId, Map<String, Object> variables);
	  
	  /**
	   * 将当前任务驳回到上一个任务处理人处
	   * @param taskId
	   */
	  void rejecttoPreTask(String taskId);
	  
	  /**
	   * 将当前任务驳回到上一个任务处理人处，并更新流程变量参数
	   * @param taskId
	   * @param variables
	   */
	void rejecttoPreTask(String taskId, String username,Map<String, Object> variables);
		  
		  /**
		   * 将当前任务驳回到上一个任务处理人处
		   * @param taskId
		   */
	void rejecttoPreTask(String taskId,String username);
	/**
	 * 获得activiti服务
	 * 
	 * @return activiti服务
	 */
	public RepositoryService getRepositoryService();

	/**
	 * 获得管理运行时流程实例服务
	 * 
	 * @return
	 */
	public RuntimeService getRuntimeService();

	/**
	 * 获得管理运行时任务服务;
	 * 
	 * @return
	 */
	public TaskService getTaskService();

	/**
	 * 获得管理流程实例、任务实例等历史数据服务
	 * 
	 * @return
	 */
	public HistoryService getHistoryService();

	/**
	 * 获得表单服务
	 * 
	 * @return
	 */
	public FormService getFormService();

	/**
	 * 获得用于管理定时任务服务
	 * 
	 * @return
	 */
	public ManagementService getManagementService();

	/**
	 * 获得用于管理组织结构服务
	 * 
	 * @return
	 */
	public IdentityService getIdentityService();

	/**
	 * 为流程任务设置参数
	 * 
	 * @param taskId
	 *            任务ID
	 * @param map
	 *            参数MAP
	 */
	public void setVariables(String taskId, Map<String, Object> map);

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
	public void setVariable(String taskId, String variable, Object object);

	/**
	 * 获得任务参数
	 * 
	 * @param taskId
	 *            任务ID
	 * @param key
	 *            参数key
	 * @return
	 */
	public Object getVariable(String taskId, String key);

	/**
	 * 根据流程定义KEY获取任务
	 * 
	 * @param definitionKey
	 *            流程定义key
	 * @return
	 */
	public List<Task> getActList(String definitionKey);

	/**
	 * activiti服务列表
	 * 
	 * @return
	 */
	public List<ProcessDefinition> activitiListByprocesskey(String process_key);

	/**
	 * 流程实例列表
	 * 
	 * @return
	 */
	public List<ProcessInstance> listProcInstByPdfid(String pdfid);

	/**
	 * 当前任务的执行情况
	 * 
	 * @return
	 */
	public List<Execution> listExecutionByProId(String processInstanceId);

	/**
	 * 部署流程
	 * 
	 * @return
	 */
	public Deployment deployProcDefByZip(String deploymentName, String zip);
	
	

	/**
	 * 部署流程
	 * 
	 * @param deploymentName
	 *            部署名称
	 * @param processDef
	 *            部署文件
	 * @return Deployment
	 */
	public Deployment deployProcDefByZip(String deploymentName,
			ZipInputStream processDef);
	
	public Deployment deployProcDefByPath(String deploymentName, String xmlPath,int deploypolicy) ;
	/**
	 * 部署流程
	 * 
	 * @param deploymentName
	 *            部署名称
	 * @param processDef
	 *            部署文件
	 * @return Deployment
	 */
	public Deployment deployProcDefByZip(String deploymentName,
			ZipInputStream processDef,int upgradepolicy);

	/**
	 * 部署流程
	 * 
	 * @return
	 */
	public Deployment deployProcDefByPath(String deploymentName,
			String xmlPath, String jpgPath);
	
	/**
	 * 部署流程
	 * 
	 * @return
	 */
	public Deployment deployProcDefByPath(String deploymentName,
			String xmlPath, String jpgPath,int deploypolicy);
	/**
	 * 部署流程
	 * 
	 * @return
	 */
	public Deployment deployProcDefByZip(String deploymentName, String zip,int deploypolicy) ;
	/**
	 * 部署流程
	 * 
	 * @return
	 */
	public Deployment deployProcDefByPath(String deploymentName, String xmlPath);

	/**
	 * 启动流程
	 * 
	 * @param businessKey
	 * @param variableMap
	 * @param process_key
	 * @return
	 */
	public ProcessInstance startProcDef(String businessKey, String process_key,
			Map<String, Object> map);
	
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
			Map<String, Object> map,String initor);

	/**
	 * 启动流程
	 * 
	 * @param variableMap
	 * @param process_key
	 * @return
	 */
	public ProcessInstance startProcDef(Map<String, Object> variableMap,
			String process_key);

	/**
	 * 启动流程
	 * 
	 * @param variableMap
	 * @param process_key
	 * @return
	 */
	public ProcessInstance startProcDef(String businessKey, String process_key);

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
			String business_id, String business_type, String initor);

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
			String business_id, String business_type);

	/**
	 * 启动流程(加载待办配置)
	 * 
	 * @param process_key
	 *            流程KEY
	 * @return
	 */
	public ProcessInstance startProcDefLoadCommonCandidate(String process_key,
			String business_id, String business_type);

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
			String business_id, String business_type, String initor);

	/**
	 * 删除部署的流程
	 * 
	 * @param process_key
	 */
	public void deleteProcDefByprocesskey(String process_key);

	/**
	 * 删除定义的流程
	 * 
	 * @param processDeploymentids
	 */
	public void deleteProcDef(String processDeploymentid);

	/**
	 * 获取流程图
	 * 
	 * @param execId
	 * @param process_key
	 * @return
	 */
	public ActivityImpl traceProcess(String execId, String process_key);

	/**
	 * 获取任务列表
	 * 
	 * @param execId
	 * @return
	 */
	public List<Task> listTaskByExecId(String execId);

	/**
	 * 获取用户下所有待办任务
	 * 
	 * @param username
	 * @return
	 */
	public List<Task> listTaskByUser(String username);

	public ProcessInstance getProcessInstanceById(String processInstanceId);

	/**
	 * 根据processInstanceId查询历史
	 * 
	 * @param processInstanceId
	 * @return
	 */
	public HistoricProcessInstance getHisProcessInstanceById(
			String processInstanceId);

	public List<ActivityImpl> getActivitiImplListByProcessDefinitionId(
			String processDefinitionId);
	public ActivityImpl getActivityImplByDefId(String actDefId,
			String processDefineId);
	/**
	 * 
	 * @param actId
	 * @param processInstanceId
	 * @return
	 */
	public ActivityImpl getActivityImplById(String actId,
			String processInstanceId);

	/**
	 * 完成任务(普通)
	 * 
	 * @param taskId
	 * @param map
	 */
	public void completeTask(String taskId, Map<String, Object> map);
	/**
	 * 完成任务(普通)，并驳回到指定节点
	 * 
	 * @param taskId
	 * @param map
	 */
	public void completeTaskWithDest(String taskId, Map<String, Object> map,String destinationTaskKey);

	/**
	 * 完成任务(加载组织机构节点参数配置)
	 * 
	 * @param taskId
	 *            任务ID
	 * @param orgId
	 *            组织机构ID
	 */
	public void completeTaskLoadOrgParams(String taskId, String orgId);
	
	/**
	 * 完成任务(加载组织机构节点参数配置),并驳回到指定节点
	 * 
	 * @param taskId
	 *            任务ID
	 * @param orgId
	 *            组织机构ID
	 */
	public void completeTaskLoadOrgParamsWithDest(String taskId, String orgId,String destinationTaskKey);

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
			Map<String, Object> map, String orgId);
	
	/**
	 * 完成任务(加载组织机构节点参数配置)，并驳回到指定节点
	 * 
	 * @param taskId
	 *            任务ID
	 * @param map
	 *            自定义参数MAP
	 * @param orgId
	 *            组织机构ID
	 */
	public void completeTaskLoadOrgParams(String taskId,
			Map<String, Object> map, String orgId,String destinationTaskKey);

	/**
	 * 完成任务(加载业务类型节点参数配置)
	 * 
	 * @param taskId
	 *            任务ID
	 * @param bussinesstypeId
	 *            业务类型ID
	 */
	public void completeTaskLoadBussinesstypeParams(String taskId,
			String bussinesstypeId);
	
	/**
	 * 完成任务(加载业务类型节点参数配置),并驳回到指定节点
	 * 
	 * @param taskId
	 *            任务ID
	 * @param bussinesstypeId
	 *            业务类型ID
	 */
	public void completeTaskLoadBussinesstypeParamsWithDest(String taskId,
			String bussinesstypeId,String destinationTaskKey);

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
			Map<String, Object> map, String bussinesstypeId);
	
	/**
	 * 完成任务(加载业务类型节点参数配置)，并驳回到指定节点
	 * 
	 * @param taskId
	 *            任务ID
	 * @param map
	 *            自定义参数MAP
	 * @param bussinesstypeId
	 *            业务类型ID
	 */
	public void completeTaskLoadBussinesstypeParams(String taskId,
			Map<String, Object> map, String bussinesstypeId,String destinationTaskKey);

	/**
	 * 完成任务(加载通用节点参数配置)
	 * 
	 * @param taskId
	 *            任务ID
	 * @param map
	 *            自定义参数MAP
	 */
	public void completeTaskLoadCommonParams(String taskId);
	
	/**
	 * 完成任务(加载通用节点参数配置),并驳回到指定节点
	 * 
	 * @param taskId
	 *            任务ID
	 * @param map
	 *            自定义参数MAP
	 */
	public void completeTaskLoadCommonParamsWithDest(String taskId,String destinationTaskKey);


	/**
	 * 完成任务(加载通用节点参数配置)
	 * 
	 * @param taskId
	 *            任务ID
	 * @param map
	 *            自定义参数MAP
	 */
	public void completeTaskLoadCommonParams(String taskId,
			Map<String, Object> map);
	
	/**
	 * 完成任务(加载通用节点参数配置),并驳回到指定节点
	 * 
	 * @param taskId
	 *            任务ID
	 * @param map
	 *            自定义参数MAP
	 */
	public void completeTaskLoadCommonParams(String taskId,
			Map<String, Object> map,String destinationTaskKey);


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
			String business_type);
	
	/**
	 * 处理任务(加载配置好的参数)，并驳回到指定节点
	 * 
	 * @param taskId
	 *            任务ID
	 * @param business_id
	 *            业务配置ID
	 * @param business_type
	 *            业务配置类型
	 */
	public void completeTaskLoadParamsWithDest(String taskId, String business_id,
			String business_type,String destinationTaskKey);

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
			String business_id, String business_type);
	
	/**
	 * 处理任务(加载配置好的参数),并驳回到指定节点
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
			String business_id, String business_type,String destinationTaskKey);

	/**
	 * 完成任务(先领用再完成)
	 * 
	 * @param taskId
	 * @param map
	 */
	public void completeTask(String taskId, String username,
			Map<String, Object> map);
	
	/**
	 * 完成任务(先领用再完成),并驳回到指定节点
	 * 
	 * @param taskId
	 * @param map
	 */
	public void completeTask(String taskId, String username,
			Map<String, Object> map,String destinationTaskKey);
	
	/**
	 * 完成任务(先领用再完成)
	 * 
	 * @param taskId
	 * @param map
	 */
	public void completeTaskByUser(String taskId, String username);
	
	/**
	 * 完成任务(先领用再完成),并驳回到指定节点
	 * 
	 * @param taskId
	 * @param map
	 */
	public void completeTaskByUserWithDest(String taskId, String username,String destinationTaskKey);
	
	public void completeTaskWithLocalVariables(String taskId, String username,
			Map<String, Object> map);
	
	/**
	 * 查询指定任务节点的最新记录
	 * 
	 * @param processInstance
	 *            流程实例
	 * @param activityId
	 * @return
	 */
	public List<HistoricActivityInstance> findHistoricUserTask(
			String instanceId) ;
	
	/**
	 * 完成任务并拨回到指定节点
	 * @param taskId
	 * @param username
	 * @param map
	 * @param destinationTaskKey
	 */
	public void completeTaskWithLocalVariables(String taskId, String username,
			Map<String, Object> map,String destinationTaskKey);

	/**
	 * 获得历史任务实例by流程实例ID
	 * 
	 * @param processId
	 *            流程实例ID
	 * @return
	 */
	public List<HistoricTaskInstance> getHisTaskByProcessId(String processId) ;

	/**
	 * 获得历史任务实例 by用户名
	 * 
	 * @param username
	 * @return
	 */
	public List<HistoricTaskInstance> getHisTaskByUsername(String username);
	
	/**
	 * 获得历史任务实例 by用户名和流程KEY
	 * 
	 * @param username
	 * @return
	 */
	public List<HistoricTaskInstance> getHisTaskByUsernameProcessKey(String username,String processKey);

	/**
	 * 根据流程实例ID查询流程实例
	 * 
	 * @param processDefinitionId
	 * @return
	 */
	public ProcessDefinition getProcessDefinitionById(String processDefinitionId);

	/**
	 * 根据部署ID查询部署的流程
	 * 
	 * @param deploymentId
	 * @return
	 */
	public Deployment getDeploymentById(String deploymentId);

	/**
	 * 根据任务ID查询任务信息
	 * 
	 * @param taskId
	 * @return
	 */
	public Task getTaskById(String taskId);

	/**
	 * 根据历史任务ID查询历史任务信息
	 * 
	 * @param taskId
	 * @return
	 */
	public HistoricTaskInstance getHisTaskById(String hisTaskId);

	/**
	 * 认领任务
	 * 
	 * @param taskId
	 * @param userId
	 */
	public void claim(String taskId, String userId);

	/**
	 * 为任务指定代理处理用户
	 * 
	 * @param taskId
	 * @param userId
	 */
	public void delegateTask(String taskId, String userId);


	/**
	 * 部署流程
	 * 
	 * @return
	 */
	public Deployment deployProcDefByInputStream(String deploymentName,
			String xmlPath, InputStream processDef);
	
	/**
	 * 部署流程
	 * 
	 * @return
	 */
	public Deployment deployProcDefByInputStream(String deploymentName,
			String xmlPath, InputStream processDef,int upgradepolicy);

	/**
	 * 根据条件查询流程定义清单
	 * 
	 * @param offset
	 * @param pagesize
	 * @param processDefCondition
	 * @return
	 */
	public ListInfo queryProcessDefs(long offset, int pagesize,
			ProcessDefCondition processDefCondition);

	/**
	 * Deletes the given deployment and cascade deletion to process instances,
	 * history process instances and jobs.
	 * 
	 * @param processDeploymentids
	 */

	public void deleteDeploymentCascade(String[] processDeploymentids,
			boolean[] cascades);

	/**
	 * 删除流程部署所有版本
	 * 
	 * @param processDeploymentids
	 * @param cascades
	 */
	public void deleteDeploymentAllVersions(String[] processDeploymentids);

	/**
	 * 启用流程
	 * 
	 * @param processId
	 */
	public void activateProcess(String processId);

	/**
	 * 停用流程
	 * 
	 * @param processId
	 */
	public void suspendProcess(String processId);

	/**
	 * 获得流程的所有节点
	 * 
	 * @param processKey
	 * @return
	 */
	public List<ActivityImpl> getActivitImplListByProcessKey(String processKey);

	/**
	 * 根据部署的ID获得流程实例key
	 * 
	 * @param deploymentId
	 * @return
	 */
	public String getPorcessKeyByDeployMentId(String deploymentId);

	/**
	 * 根据部署号和资源名称获取资源输入流
	 * 
	 * @param deploymentId
	 * @param resourceName
	 * @return
	 */
	public InputStream getResourceAsStream(String deploymentId,
			String resourceName);
	/**
	 * 获取流程定义xml
	 * @param processId
	 * @return
	 * @throws IOException
	 */
	public String getProccessXML(String processId) throws IOException ;
	public String getProccessXMLByKey(String processKey,String encode) throws IOException ;
	public String getProccessXMLByKey(String processKey) throws IOException; 
	/**
	 * 根据流程定义id获取对应版本的流程图
	 * @param processDefId
	 * @param out
	 * @throws IOException
	 */
	public void getProccessPic(String processDefId, OutputStream out) throws IOException ;
	/**
	 * 根据流程key获取流程的最新版本流程图
	 * @param processKey
	 * @param out
	 * @throws IOException
	 */
	public void getProccessPicByProcessKey(String processKey, OutputStream out) throws IOException;

	/**
	 * 获得所有待删除列表
	 * 
	 * @param processDeploymentids
	 * @return
	 */
	public List<ProcessDefinition> findAllRemoveListByProcessDeploymentids(
			String[] processDeploymentids);

	/**
	 * 根据流程部署ID获得流程定义信息
	 * 
	 * @param DeploymentId
	 * @return
	 */
	public ProcessDef getProcessDefByDeploymentId(String DeploymentId);

	/**
	 * 自定义查询待办任务
	 * 
	 * @param processKey
	 * @param username
	 * @return
	 */
	public List<Task> listTaskByUser(String[] processKeys, String username);

	/**
	 * 获取当前环节ID的所有下面环节ID
	 * 
	 * @param nodeKey
	 *            环节ID
	 * @param processKey
	 *            流程key
	 * @return
	 */
	public List<String> getNextNodes(String nodeKey, String processKey);

	/**
	 * 根据流程实例KEY查询流程实例
	 * 
	 * @param processDefinitionId
	 * @return
	 */
	public ProcessDefinition getProcessDefinitionByKey(String processKey);

	/**
	 * 获得流程所处的当前环节
	 * @param processInstanceId
	 * @return
	 */
	Task getCurrentTask(String processInstanceId);
	
	/**
	 * 根据任务ID查询任务的待办人
	 * @param taskId
	 * @return
	 */
	public List<String> getUsersByTaskId(String taskId);

	/**
	 * 查询待办任务分页列表
	 * @param processKey
	 * @param username
	 * @param offset
	 * @param pagesize
	 * @return
	 */
	ListInfo listTaskByUser(String[] processKeys, String username, long offset,
			int pagesize);

	/**
	 * 查询历史任务的分页列表
	 * @param username
	 * @param processKey
	 * @param offset
	 * @param pagesize
	 * @return
	 */
	ListInfo getHisTaskByUsernameProcessKey(String username,String[] processKeys,long offset,int pagesize);

	/**
	 * 获得历史任务实例分页列表 by用户名
	 * 
	 * @param username
	 * @return
	 */
	ListInfo getHisTaskByUsername(String username, long offset, int pagesize);

	/**
	 * 根据用户名查询待办任务分页列表
	 * @param username
	 * @param offset
	 * @param pagesize
	 * @return
	 */
	ListInfo listTaskByUser(String username, long offset, int pagesize);

	/**
	 * 获得历史任务实例分页列表 by用户名,流程KEY
	 * 
	 * @param username
	 * @return
	 */
	ListInfo getHisTaskByUsernameProcessKey(String username, String processKey,
			int offset, int pagesize);

	/**
	 * 根据processKey,用户名查询待办分页列表
	 * @param processKey
	 * @param username
	 * @param offset
	 * @param pagesize
	 * @return
	 */
	ListInfo listTaskByUser(String processKey, String username, long offset,
			int pagesize);

	List<Task> listTaskByUser(String processKey, String username);

	/**
	 * 获得activiti引擎
	 * @return
	 */
	ProcessEngine getProcessEngine();
	
	void cancleProcessInstance(String processInstanceId, String deleteReason);
	public ListInfo listTaskAndVarsByUserWithState(Class clazz,String processkey,String state,String userAccount,long offset,int pagesize);
	public int countTasksByUserWithState(String processkey,String state,String userAccount);
	public List<Task> listTaskByProcessInstanceId(String processInstanceId) ;
}
