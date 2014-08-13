package com.sany.workflow.webservice.service;

import java.util.HashMap;
import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import com.sany.workflow.webservice.entity.DataResponse;
import com.sany.workflow.webservice.entity.DealInfoResponse;
import com.sany.workflow.webservice.entity.DeploymentInfo;
import com.sany.workflow.webservice.entity.NoHandTaskResponse;
import com.sany.workflow.webservice.entity.ResultResponse;

@WebService(name = "WorkflowService", targetNamespace = "com.sany.workflow.webservice.action.WorkflowService")
public interface WorkflowService {

	/**
	 * 部署流程定义
	 * 
	 * @param deployMap
	 *            部署信息
	 * @return 2014年8月6日
	 */
	public @WebResult(name = "deployProcess", partName = "partDeployProcess")
	ResultResponse deployProcess(
			@WebParam(name = "deployInfo", partName = "partDeployInfo") DeploymentInfo deployInfo);

	/**
	 * 挂起流程定义
	 * 
	 * @param processDefId
	 *            流程定义id
	 * @return 2014年8月1日
	 */
	public @WebResult(name = "suspendProcessDef", partName = "partSuspendProcessDef")
	ResultResponse suspendProcessDef(
			@WebParam(name = "processDefId", partName = "partProcessDefId") String processDefId);

	/**
	 * 激活流程定义
	 * 
	 * @param processDefId
	 *            流程定义id
	 * @return 2014年8月1日
	 */
	public @WebResult(name = "activateProcessDef", partName = "partActivateProcessDef")
	ResultResponse activateProcessDef(
			@WebParam(name = "processDefId", partName = "partProcessDefId") String processDefId);

	/**
	 * 删除流程定义
	 * 
	 * @param deploymentids
	 *            流程发布id
	 * @return 2014年8月1日
	 */
	public @WebResult(name = "delDeployment", partName = "partDelDeployment")
	ResultResponse delDeployment(
			@WebParam(name = "processKeys", partName = "partProcessKeys") String processKeys);

	/**
	 * 获取流程定义xml
	 * 
	 * @param processKey
	 *            流程key
	 * @param version
	 *            流程定义版本
	 * @return 2014年8月1日
	 */
	public @WebResult(name = "getProccessXML", partName = "partGetProccessXML")
	DataResponse getProccessXML(
			@WebParam(name = "processKey", partName = "partProcessKey") String processKey,
			@WebParam(name = "version", partName = "partVersion") String version);

	/**
	 * 开启流程实例
	 * 
	 * @param processKey
	 *            流程key
	 * @param businessKey
	 *            业务主题
	 * @param nodeInfoList
	 *            节点配置信息
	 * @param variableList
	 *            参数变量信息
	 * @return 2014年8月1日
	 */
	public @WebResult(name = "startInstance", partName = "partStartInstance")
	ResultResponse startInstance(
			@WebParam(name = "processKey", partName = "partProcessKey") String processKey,
			@WebParam(name = "businessKey", partName = "partBusinessKey") String businessKey,
			@WebParam(name = "currentUser", partName = "partCurrentUser") String currentUser,
			@WebParam(name = "nodeInfoMap", partName = "partNodeInfoMap") List<HashMap<String, Object>> nodeInfoList,
			@WebParam(name = "variableMap", partName = "partVariableMap") List<HashMap<String, Object>> variableList);

	/**
	 * 升级流程
	 * 
	 * @param processKey
	 *            流程定义key
	 * @return
	 * @throws Exception
	 *             2014年7月29日
	 */
	public @WebResult(name = "upgradeInstances", partName = "partUpgradeInstances")
	ResultResponse upgradeInstancesByProcessKey(
			@WebParam(name = "processKey", partName = "partProcessKey") String processKey);

	/**
	 * 逻辑删除流程
	 * 
	 * @param instancesIds
	 *            实例ids
	 * @param deleteReason
	 *            删除原因
	 * @return 2014年7月30日
	 */
	public @WebResult(name = "delInstancesForLogic", partName = "partDelInstancesForLogic")
	ResultResponse delInstancesForLogic(
			@WebParam(name = "instancesIds", partName = "partInstancesIds") String instancesIds,
			@WebParam(name = "deleteReason", partName = "partDeleteReason") String deleteReason);

	/**
	 * 物理删除流程
	 * 
	 * @param instancesIds
	 *            实例ids
	 * @return 2014年7月30日
	 */
	public @WebResult(name = "delInstancesForPhysics", partName = "partDelInstancesForPhysics")
	ResultResponse delInstancesForPhysics(
			@WebParam(name = "instancesIds", partName = "partInstancesIds") String instancesIds);

	/**
	 * 挂起流程实例
	 * 
	 * @param instancesId
	 *            实例id
	 * @return 2014年7月30日
	 */
	public @WebResult(name = "suspendInstance", partName = "partSuspendInstance")
	ResultResponse suspendInstance(
			@WebParam(name = "instancesId", partName = "partInstancesId") String instancesId);

	/**
	 * 激活流程实例
	 * 
	 * @param instancesId
	 *            实例id
	 * @return 2014年7月30日
	 */
	public @WebResult(name = "activateInstance", partName = "partActivateInstance")
	ResultResponse activateInstance(
			@WebParam(name = "instancesId", partName = "partInstancesId") String instancesId);

	/**
	 * 处理记录
	 * 
	 * @param instancesId
	 *            实例id
	 * @return 2014年7月30日
	 */
	public @WebResult(name = "getInstanceDealInfo", partName = "partGetInstanceDealInfo")
	DealInfoResponse getInstanceDealInfo(
			@WebParam(name = "instancesId", partName = "partInstancesId") String instancesId);

	/**
	 * 分页获取流程实例
	 * 
	 * @param offset
	 * @param pagesize
	 * @param conditionMap
	 *            查询条件参数map
	 * @return 2014年7月30日
	 */
	// public @WebResult(name = "getInstanceListPage", partName =
	// "partGetInstanceListPage")
	// String getInstanceListPage(
	// @WebParam(name = "offset", partName = "partOffset") long offset,
	// @WebParam(name = "pagesize", partName = "partPagesize") int pagesize,
	// @WebParam(name = "conditionMap", partName = "partConditionMap")
	// HashMap<String, Object> conditionMap);

	/**
	 * 签收任务
	 * 
	 * @param taskId
	 *            任务id
	 * @param userId
	 *            签收人id
	 * @return 2014年7月30日
	 */
	public @WebResult(name = "signTask", partName = "partSignTask")
	ResultResponse signTask(
			@WebParam(name = "taskId", partName = "partTaskId") String taskId,
			@WebParam(name = "userId", partName = "partUserId") String userId);

	/**
	 * 废弃任务
	 * 
	 * @param instancesId
	 *            流程实例id
	 * @param deleteReason
	 *            废弃原因
	 * @return 2014年7月30日
	 */
	public @WebResult(name = "discardTask", partName = "partDiscardTask")
	ResultResponse discardTask(
			@WebParam(name = "instancesId", partName = "partInstancesId") String instancesId,
			@WebParam(name = "deleteReason", partName = "partDeleteReason") String deleteReason);

	/**
	 * 撤销任务
	 * 
	 * @param instancesId
	 *            流程实例id
	 * @param processKey
	 *            流程key
	 * @param cancelReason
	 *            撤销原因
	 * @return 2014年7月30日
	 */
	public @WebResult(name = "cancelTask", partName = "partCancelTask")
	ResultResponse cancelTask(
			@WebParam(name = "instancesId", partName = "partInstancesId") String instancesId,
			@WebParam(name = "processKey", partName = "partProcessKey") String processKey,
			@WebParam(name = "cancelReason", partName = "partCancelReason") String cancelReason);

	/**
	 * 转办任务
	 * 
	 * @param taskId
	 *            任务id
	 * @param fromUserId
	 *            当前用户id
	 * @param toUserId
	 *            转办人id
	 * @param instancesId
	 *            流程实例id
	 * @param processKey
	 *            流程key
	 * @return 2014年7月30日
	 */
	public @WebResult(name = "delegateTask", partName = "partDelegateTask")
	ResultResponse delegateTask(
			@WebParam(name = "taskId", partName = "partTaskId") String taskId,
			@WebParam(name = "fromUserId", partName = "partFromUserId") String fromUserId,
			@WebParam(name = "toUserId", partName = "partToUserId") String toUserId,
			@WebParam(name = "instancesId", partName = "partInstancesId") String instancesId,
			@WebParam(name = "processKey", partName = "partProcessKey") String processKey);

	/**
	 * 驳回任务
	 * 
	 * @param taskId
	 *            任务id
	 * @param rejectedReason
	 *            驳回原因
	 * @param rejectedType
	 *            驳回类型 0 上一个任务对应的节点 1当前节点的上一个节点
	 * @param nodeInfoMap
	 *            节点配置信息
	 * @param variableMap
	 *            参数变量信息
	 * @return 2014年7月30日
	 */
	public @WebResult(name = "rejectToPreTask", partName = "partRejectToPreTask")
	ResultResponse rejectToPreTask(
			@WebParam(name = "taskId", partName = "partTaskId") String taskId,
			@WebParam(name = "rejectedReason", partName = "partRejectedReason") String rejectedReason,
			@WebParam(name = "rejectedType", partName = "partRejectedType") int rejectedType,
			@WebParam(name = "nodeInfoMap", partName = "partNodeInfoMap") List<HashMap<String, Object>> nodeInfoList,
			@WebParam(name = "variableMap", partName = "partVariableMap") List<HashMap<String, Object>> variableList);

	/**
	 * 完成任务
	 * 
	 * @param taskMap
	 *            任务基本信息
	 * @param nodeInfoList
	 *            节点配置信息
	 * @param variableList
	 *            参数变量信息
	 * @return 2014年7月31日
	 */
	public @WebResult(name = "completeTask", partName = "partCompleteTask")
	ResultResponse completeTask(
			@WebParam(name = "taskMap", partName = "partTaskMap") HashMap<String, Object> taskMap,
			@WebParam(name = "nodeInfoMap", partName = "partNodeInfoMap") List<HashMap<String, Object>> nodeInfoList,
			@WebParam(name = "variableMap", partName = "partVariableMap") List<HashMap<String, Object>> variableList);

	/**
	 * 任务代办数统计
	 * 
	 * @param pernr
	 *            用户登录账号
	 * @param sysId
	 *            系统编号
	 * @return 2014年7月31日
	 */
	public @WebResult(name = "countTaskNum", partName = "partCountTaskNum")
	DataResponse countTaskNum(
			@WebParam(name = "pernr", partName = "partPernr") String pernr,
			@WebParam(name = "sysId", partName = "partSysId") String sysId);

	/**
	 * 任务待办数据
	 * 
	 * @param pernr
	 *            用户登录账号
	 * @param sysId
	 *            系统编号
	 * @return 2014年8月1日
	 */
	public @WebResult(name = "getNoHandleTask", partName = "partGetNoHandleTask")
	NoHandTaskResponse getNoHandleTask(
			@WebParam(name = "pernr", partName = "partPernr") String pernr,
			@WebParam(name = "sysId", partName = "partSysId") String sysId);

}
