package com.sany.workflow.webservice.action;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;
import org.frameworkset.util.annotations.ResponseBody;

import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.util.StringUtil;
import com.sany.workflow.entity.ActivitiNodeCandidate;
import com.sany.workflow.entity.ActivitiNodeInfo;
import com.sany.workflow.entity.NoHandleTask;
import com.sany.workflow.entity.Nodevariable;
import com.sany.workflow.entity.ProcessDef;
import com.sany.workflow.entity.TaskCondition;
import com.sany.workflow.entity.TaskDelegateRelation;
import com.sany.workflow.entity.TaskManager;
import com.sany.workflow.service.ActivitiConfigService;
import com.sany.workflow.service.ActivitiRelationService;
import com.sany.workflow.service.ActivitiService;
import com.sany.workflow.service.ActivitiTaskService;
import com.sany.workflow.webservice.entity.DataResponse;
import com.sany.workflow.webservice.entity.DealInfoResponse;
import com.sany.workflow.webservice.entity.DeploymentInfo;
import com.sany.workflow.webservice.entity.DeploymentZipFile;
import com.sany.workflow.webservice.entity.NoHandTaskInfo;
import com.sany.workflow.webservice.entity.NoHandTaskResponse;
import com.sany.workflow.webservice.entity.NodeTaskInfo;
import com.sany.workflow.webservice.entity.NodeTaskRelation;
import com.sany.workflow.webservice.entity.ResultResponse;
import com.sany.workflow.webservice.service.WorkflowService;

/**
 * @todo工作流服务发布
 * @author tanx
 * @date 2014年7月29日
 * 
 */
@WebService(name = "WorkflowService", targetNamespace = "com.sany.workflow.webservice.action.WorkflowService")
public class WorkflowController implements WorkflowService {

	private static Logger logger = Logger.getLogger(WorkflowController.class);
	private ActivitiService activitiService;
	private ActivitiTaskService activitiTaskService;
	private ActivitiConfigService activitiConfigService;
	private ActivitiRelationService activitiRelationService;

	/**
	 * 部署流程定义
	 * 
	 * 
	 * 部署信息
	 * 
	 * @return 2014年8月6日
	 */
	@WebMethod(exclude = true)
	public @ResponseBody(datatype = "json") ResultResponse deployZipProcess(
			DeploymentZipFile deployInfo) {
		return null;
	}

	/**
	 * 部署流程定义
	 * 
	 * @param deployMap
	 *            部署信息
	 * @return 2014年8月6日
	 */
	public @ResponseBody(datatype = "json") ResultResponse deployProcess(
			DeploymentInfo deployInfo) {
		ResultResponse rr = new ResultResponse();

		if (StringUtil.isEmpty(deployInfo)) {
			rr.setResultCode("7");
			rr.setResultMess("deployInfo为空");
			return rr;
		}

		if (StringUtil.isEmpty(deployInfo.getBusinessTypeId())) {
			rr.setResultCode("6");
			rr.setResultMess("businessTypeId为空");
			return rr;
		}

		if (StringUtil.isEmpty(deployInfo.getDeployName())) {
			rr.setResultCode("5");
			rr.setResultMess("deployName为空");
			return rr;
		}

		if (StringUtil.isEmpty(deployInfo.getNeedConfig())) {
			rr.setResultCode("4");
			rr.setResultMess("needConfig为空");
			return rr;
		}

		if (StringUtil.isEmpty(deployInfo.getProcessDefFile())) {
			rr.setResultCode("3");
			rr.setResultMess("processDefFile为空");
			return rr;
		}

		TransactionManager tm = new TransactionManager();
		try {

			tm.begin();
			// deployInfo.getProcessZipDef();
			// 部署资源 （将byte[]转换成ZipInputStream）
			ByteArrayInputStream zipStream = new ByteArrayInputStream(
					deployInfo.getProcessDefFile());
			ZipInputStream zip = new ZipInputStream(zipStream);
			ZipEntry ze = zip.getNextEntry();
			if (ze == null) {
				rr.setResultCode("2");
				rr.setResultMess("不是以zip结尾的压缩包");
				return rr;
			}

			Deployment deployment = activitiService.deployProcDefByZip(
					deployInfo.getDeployName(),
					new ZipInputStream(new ByteArrayInputStream(deployInfo
							.getProcessDefFile())), deployInfo
							.getUpgradepolicy());
			if (deployment != null) {
				ProcessDef pd = activitiService
						.getProcessDefByDeploymentId(deployment.getId());
				if (deployInfo.getNeedConfig().equals("1")) {
					activitiConfigService.addActivitiNodeInfo(activitiService
							.getPorcessKeyByDeployMentId(deployment.getId()));
				} else {
					activitiConfigService.updateActivitiNodeInfo(
							activitiService
									.getPorcessKeyByDeployMentId(deployment
											.getId()), deployInfo
									.getUpgradepolicy());
				}
				if (StringUtil.isNotEmpty(deployInfo.getParamFile())) {
					// 参数资源 （将byte[]转换成InputStream）
					ByteArrayInputStream paramStream = new ByteArrayInputStream(
							deployInfo.getParamFile());
					activitiConfigService.addNodeParams(paramStream,
							pd.getKEY_());
				}
				if (pd != null) {
					activitiConfigService.addProBusinessType(pd.getKEY_(),
							deployInfo.getBusinessTypeId());

					activitiRelationService.addAppProcRelation(pd,
							deployInfo.getWfAppId());

					activitiService.addIsContainHoliday(pd.getKEY_(), 0);
				}
			}
			tm.commit();

			rr.setResultCode("1");
			rr.setResultMess("流程定义部署成功");
			return rr;

		} catch (Exception e) {
			logger.error("流程定义部署出错：" + e.getMessage(), e);

			rr.setResultCode("0");
			rr.setResultMess("流程定义部署出错：" + e.getMessage());
			return rr;
		} finally {
			tm.release();
		}

	}

	/**
	 * 挂起流程定义
	 * 
	 * @param processDefId
	 *            流程定义id
	 * @return 2014年8月1日
	 */
	public @ResponseBody(datatype = "json") ResultResponse suspendProcessDef(
			String processDefId) {
		ResultResponse rr = new ResultResponse();

		if (StringUtil.isEmpty(processDefId)) {
			rr.setResultCode("2");
			rr.setResultMess("processDefId为空");
			return rr;
		}

		try {
			activitiService.suspendProcess(processDefId);

			rr.setResultCode("1");
			rr.setResultMess("挂起操作成功");
			return rr;

		} catch (Exception e) {
			logger.error("挂起流程定义" + processDefId + "失败：" + e.getMessage(), e);

			rr.setResultCode("0");
			rr.setResultMess("挂起操作失败：" + e.getMessage());
			return rr;
		}

	}

	/**
	 * 激活流程定义
	 * 
	 * @param processDefId
	 *            流程定义id
	 * @return 2014年8月1日
	 */
	public @ResponseBody(datatype = "json") ResultResponse activateProcessDef(
			String processDefId) {
		ResultResponse rr = new ResultResponse();

		if (StringUtil.isEmpty(processDefId)) {
			rr.setResultCode("2");
			rr.setResultMess("processDefId为空");
			return rr;
		}

		try {
			activitiService.activateProcess(processDefId);

			rr.setResultCode("1");
			rr.setResultMess("激活操作成功");
			return rr;

		} catch (Exception e) {
			logger.error("激活流程定义" + processDefId + "失败：" + e.getMessage(), e);

			rr.setResultCode("1");
			rr.setResultMess("激活操作失败：" + e.getMessage());
			return rr;

		}

	}

	/**
	 * 删除流程定义
	 * 
	 * @param deploymentids
	 *            流程发布id
	 * @return 2014年8月1日
	 */
	public @ResponseBody(datatype = "json") ResultResponse delDeployment(
			String processKeys) {
		ResultResponse rr = new ResultResponse();

		if (StringUtil.isEmpty(processKeys)) {
			rr.setResultCode("2");
			rr.setResultMess("processKeys为空");
			return rr;
		}

		try {
			String[] keys = processKeys.split(",");
			activitiService.deleteDeploymentAllVersions(keys);

			rr.setResultCode("1");
			rr.setResultMess("删除操作成功");
			return rr;

		} catch (Exception e) {
			logger.error("删除流程定义" + processKeys + "失败：" + e.getMessage(), e);

			rr.setResultCode("0");
			rr.setResultMess("删除操作失败：" + e.getMessage());
			return rr;
		}

	}

	/**
	 * 获取流程定义xml
	 * 
	 * @param processKey
	 *            流程key
	 * @param version
	 *            流程定义版本
	 * @return 2014年8月1日
	 */
	public @ResponseBody(datatype = "json") DataResponse getProccessXML(
			String processKey, String version) {
		DataResponse rr = new DataResponse();

		if (StringUtil.isEmpty(processKey)) {
			rr.setResultCode("3");
			rr.setResultMess("processKey为空");
			return rr;
		}

		if (StringUtil.isEmpty(version)) {
			rr.setResultCode("2");
			rr.setResultMess("version为空");
			return rr;
		}

		try {
			String processXML = activitiService.getProccessXMLByKey(processKey,
					version, "UTF-8");

			rr.setResultCode("1");
			rr.setResultMess("获取流程定义XML成功");
			rr.setResultData(processXML);
			return rr;

		} catch (Exception e) {
			logger.error("获取流程定义" + processKey + "XML失败：" + e.getMessage(), e);

			rr.setResultCode("0");
			rr.setResultMess("获取流程定义XML失败：" + e.getMessage());
			return rr;
		}

	}

	/**
	 * 获取流程定义图片
	 * 
	 * @param processDefId
	 *            流程定义ID
	 * @return 2014年8月1日
	 */
	public @ResponseBody(datatype = "json") void getProccessPic(
			String processDefId, HttpServletResponse response) {

		try {
			if (StringUtil.isNotEmpty(processDefId)) {
				OutputStream out = response.getOutputStream();
				activitiService.getProccessPic(processDefId, out);
			}
		} catch (Exception e) {
			logger.error("获取流程定义ID" + processDefId + "图片失败：" + e.getMessage(),
					e);
		}
	}

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
	public @ResponseBody(datatype = "json") ResultResponse startInstance(
			String processKey, String businessKey, String currentUser,
			List<HashMap<String, Object>> nodeInfoList,
			List<HashMap<String, Object>> variableList) {
		ResultResponse rr = new ResultResponse();

		if (StringUtil.isEmpty(processKey)) {
			rr.setResultCode("5");
			rr.setResultMess("processKey为空");
			return rr;
		}

		if (StringUtil.isEmpty(currentUser)) {
			rr.setResultCode("4");
			rr.setResultMess("businessKey为空");
			return rr;
		}

		// 节点配置转换
		List<ActivitiNodeCandidate> activitiNodeCandidateList = null;
		try {

			if (nodeInfoList != null && nodeInfoList.size() > 0) {

				activitiNodeCandidateList = new ArrayList<ActivitiNodeCandidate>();

				for (int i = 0; i < nodeInfoList.size(); i++) {
					ActivitiNodeCandidate node = new ActivitiNodeCandidate();
					Map<String, Object> map = nodeInfoList.get(i);

					String nodeKey = (String) map.get("nodeKey");
					node.setNode_key(nodeKey);

					String nodeUserIds = (String) map.get("nodeUserIds");
					node.setCandidate_users_id(nodeUserIds);

					String nodeGroupIds = (String) map.get("nodeGroupIds");
					node.setCandidate_groups_id(nodeGroupIds);

					String isMulti = (String) map.get("isMulti");
					node.setIsMulti(isMulti);

					// double durationNode =
					// Double.parseDouble(map.get("durationNode")+"");
					// node.setDuration_node(durationNode);

					activitiNodeCandidateList.add(node);
				}
			}
		} catch (Exception e) {
			logger.error("开启实例，节点配置转换出错：" + e.getMessage(), e);

			rr.setResultCode("3");
			rr.setResultMess("开启实例，节点配置转换出错：" + e.getMessage());
			return rr;
		}

		// 参数转换
		List<Nodevariable> nodevariableList = null;
		try {
			if (variableList != null && variableList.size() > 0) {

				nodevariableList = new ArrayList<Nodevariable>();

				for (int i = 0; i < variableList.size(); i++) {
					Nodevariable variable = new Nodevariable();
					Map<String, Object> map = variableList.get(i);

					String paramName = (String) map.get("paramName");
					variable.setParam_name(paramName);

					String paramValue = (String) map.get("paramValue");
					variable.setParam_value(paramValue);

					nodevariableList.add(variable);
				}
			}
		} catch (Exception e) {
			logger.error("开始实例，参数转换出错：" + e.getMessage(), e);

			rr.setResultCode("2");
			rr.setResultMess("开始实例，参数转换出错：" + e.getMessage());
			return rr;
		}

		try {

			// activitiService.startPorcessInstance(processKey, businessKey,
			// currentUser, activitiNodeCandidateList, nodevariableList);

			rr.setResultCode("1");
			rr.setResultMess("开启实例操作成功");
			return rr;

		} catch (Exception e) {
			logger.error("流程定义：" + processKey + "开始实例失败：" + e.getMessage(), e);

			rr.setResultCode("0");
			rr.setResultMess("开启实例操作失败：" + e.getMessage());
			return rr;
		}
	}

	/**
	 * 升级流程 gw_tanx
	 * 
	 * @param processKey
	 *            流程定义key
	 * @return
	 * @throws Exception
	 *             2014年7月29日
	 */
	public @ResponseBody(datatype = "json") ResultResponse upgradeInstancesByProcessKey(
			String processKey) {
		ResultResponse rr = new ResultResponse();

		if (StringUtil.isEmpty(processKey)) {
			rr.setResultCode("2");
			rr.setResultMess("processKey为空");
			return rr;
		}

		try {
			activitiService.upgradeInstances(processKey);

			rr.setResultCode("1");
			rr.setResultMess("升级操作成功");
			return rr;

		} catch (Exception e) {
			logger.error("流程定义：" + processKey + "升级失败：" + e.getMessage(), e);

			rr.setResultCode("0");
			rr.setResultMess("升级操作失败:" + e.getMessage());
			return rr;
		}

	}

	/**
	 * 逻辑删除流程
	 * 
	 * @param instancesIds
	 *            实例ids
	 * @param deleteReason
	 *            删除原因
	 * @return 2014年7月30日
	 */
	public @ResponseBody(datatype = "json") ResultResponse delInstancesForLogic(
			String instancesIds, String deleteReason, String processKey,
			String currentUser) {
		ResultResponse rr = new ResultResponse();

		if (StringUtil.isEmpty(currentUser)) {
			rr.setResultCode("5");
			rr.setResultMess("currentUser为空");
			return rr;
		}

		if (StringUtil.isEmpty(processKey)) {
			rr.setResultCode("4");
			rr.setResultMess("processKey为空");
			return rr;
		}

		if (StringUtil.isEmpty(deleteReason)) {
			rr.setResultCode("3");
			rr.setResultMess("deleteReason为空");
			return rr;
		}

		if (StringUtil.isEmpty(instancesIds)) {
			rr.setResultCode("2");
			rr.setResultMess("instancesIds为空");
			return rr;
		}

		try {
			// activitiService.cancleProcessInstances(instancesIds,
			// deleteReason,
			// "", processKey, currentUser);

			rr.setResultCode("1");
			rr.setResultMess("逻辑删除操作成功");
			return rr;

		} catch (Exception e) {
			logger.error("流程实例：" + instancesIds + ",逻辑删除失败：" + e.getMessage(),
					e);

			rr.setResultCode("0");
			rr.setResultMess("逻辑删除操作失败:" + e.getMessage());
			return rr;
		}
	}

	/**
	 * 物理删除流程
	 * 
	 * @param instancesIds
	 *            实例ids
	 * @return 2014年7月30日
	 */
	public @ResponseBody(datatype = "json") ResultResponse delInstancesForPhysics(
			String instancesIds) {
		ResultResponse rr = new ResultResponse();

		if (StringUtil.isEmpty(instancesIds)) {
			rr.setResultCode("2");
			rr.setResultMess("instancesIds为空");
			return rr;
		}

		try {
			activitiService.delProcessInstances(instancesIds);

			rr.setResultCode("1");
			rr.setResultMess("物理删除操作成功");
			return rr;
		} catch (Exception e) {
			logger.error("流程实例：" + instancesIds + ",物理删除失败：" + e.getMessage(),
					e);

			rr.setResultCode("0");
			rr.setResultMess("物理删除操作失败：" + e.getMessage());
			return rr;
		}
	}

	/**
	 * 挂起流程实例
	 * 
	 * @param instancesId
	 * @return 2014年7月30日
	 */
	public @ResponseBody(datatype = "json") ResultResponse suspendInstance(
			String instancesId) {
		ResultResponse rr = new ResultResponse();

		if (StringUtil.isEmpty(instancesId)) {
			rr.setResultCode("2");
			rr.setResultMess("instancesId为空");
			return rr;
		}

		try {
			activitiService.suspendProcessInst(instancesId);

			rr.setResultCode("1");
			rr.setResultMess("挂起流程实例操作成功");
			return rr;

		} catch (Exception e) {
			logger.error("流程实例：" + instancesId + ",挂起失败：" + e.getMessage(), e);

			rr.setResultCode("0");
			rr.setResultMess("挂起流程实例操作失败：" + e.getMessage());
			return rr;
		}
	}

	/**
	 * 激活流程实例
	 * 
	 * @param instancesId
	 *            实例id
	 * @return 2014年7月30日
	 */
	public @ResponseBody(datatype = "json") ResultResponse activateInstance(
			String instancesId) {
		ResultResponse rr = new ResultResponse();

		if (StringUtil.isEmpty(instancesId)) {
			rr.setResultCode("2");
			rr.setResultMess("instancesId为空");
			return rr;
		}

		try {
			activitiService.activateProcessInst(instancesId);

			rr.setResultCode("1");
			rr.setResultMess("激活流程实例操作成功");
			return rr;

		} catch (Exception e) {
			logger.error("流程实例：" + instancesId + ",激活失败：" + e.getMessage(), e);

			rr.setResultCode("0");
			rr.setResultMess("激活流程实例操作失败：" + e.getMessage());
			return rr;
		}
	}

	/**
	 * 处理记录
	 * 
	 * @param instancesId
	 *            实例id
	 * @return 2014年7月30日
	 */
	public @ResponseBody(datatype = "json") DealInfoResponse getInstanceDealInfo(
			String instancesId) {

		DealInfoResponse wr = new DealInfoResponse();

		if (StringUtil.isEmpty(instancesId)) {
			wr.setResultCode("2");
			wr.setResultMess("instancesId为空");
			return wr;
		}

		try {
			List<TaskManager> taskHistorList = activitiService
					.queryHistorTasks(instancesId, "");

			List<NodeTaskInfo> nodeList = new ArrayList<NodeTaskInfo>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			for (int i = 0; i < taskHistorList.size(); i++) {
				TaskManager tm = taskHistorList.get(i);

				NodeTaskInfo nti = new NodeTaskInfo();
				nti.setNodeName(tm.getACT_NAME_());
				nti.setStartTime(sdf.format(tm.getSTART_TIME_()));
				if (tm.getCLAIM_TIME_() != null) {
					nti.setClaimTime(sdf.format(tm.getCLAIM_TIME_()));
				} else {
					nti.setClaimTime("");
				}
				if (tm.getEND_TIME_() != null) {
					nti.setEndTime(sdf.format(tm.getEND_TIME_()));
				} else {
					nti.setEndTime("");
				}
				nti.setDurationNode(tm.getDURATION_NODE());
				nti.setIsContainHoliday(tm.getIS_CONTAIN_HOLIDAY());
				nti.setDuration(tm.getDURATION_());
				if (tm.getALERTTIME() != null) {
					nti.setAlertTime(sdf.format(tm.getALERTTIME()));
				} else {
					nti.setAlertTime("");
				}
				nti.setIsAlertTime(tm.getIsAlertTime());
				nti.setAdvanceSend(tm.getAdvancesend());
				if (tm.getOVERTIME() != null) {
					nti.setOverTime(sdf.format(tm.getOVERTIME()));
				} else {
					nti.setOverTime("");
				}
				nti.setIsOverTime(tm.getIsOverTime());
				nti.setOvertimeSend(tm.getOvertimesend());
				nti.setUserName(tm.getUSER_ID_NAME());
				nti.setAssigneeName(tm.getASSIGNEE_NAME());
				nti.setRemark(tm.getDELETE_REASON_());

				// 转办/委托关系
				if (tm.getDelegateTaskList() != null
						&& tm.getDelegateTaskList().size() > 0) {
					List<NodeTaskRelation> delegateTaskList = new ArrayList<NodeTaskRelation>();
					for (int j = 0; j < tm.getDelegateTaskList().size(); j++) {
						TaskDelegateRelation tdr = tm.getDelegateTaskList()
								.get(j);

						NodeTaskRelation ntr = new NodeTaskRelation();
						ntr.setFromUserName(tdr.getFROM_USER_NAME());
						ntr.setToUserName(tdr.getFROM_USER_NAME());
						if (tdr.getCHANGETIME() != null) {
							ntr.setChangeTime(sdf.format(tdr.getCHANGETIME()));
						} else {
							ntr.setChangeTime("");
						}
						ntr.setTaskRelation(tdr.getTASKRELATION());

						delegateTaskList.add(ntr);

					}
					nti.setDelegateTaskList(delegateTaskList);
				}
				nodeList.add(nti);
			}

			wr.setResultCode("1");
			wr.setResultMess("获取处理记录操作成功");
			wr.setDataList(nodeList);
			return wr;

		} catch (Exception e) {
			logger.error("获取处理记录失败：" + e.getMessage(), e);

			wr.setResultCode("0");
			wr.setResultMess("获取处理记录操作失败：" + e.getMessage());
			return wr;
		}
	}

	/**
	 * 签收任务
	 * 
	 * @param taskId
	 *            任务id
	 * @param userId
	 *            签收人id
	 * @return 2014年7月30日
	 */
	public @ResponseBody(datatype = "json") ResultResponse signTask(
			String taskId, String userId) {
		ResultResponse rr = new ResultResponse();

		if (StringUtil.isEmpty(taskId)) {
			rr.setResultCode("3");
			rr.setResultMess("taskId为空");
			return rr;
		}

		if (StringUtil.isEmpty(userId)) {
			rr.setResultCode("2");
			rr.setResultMess("userId为空");
			return rr;
		}

		try {
			activitiTaskService.signTaskByUser(taskId, userId);

			rr.setResultCode("1");
			rr.setResultMess("签收任务操作成功");
			return rr;

		} catch (Exception e) {
			logger.error("任务id：" + taskId + ",签收失败：" + e.getMessage(), e);

			rr.setResultCode("0");
			rr.setResultMess("签收任务操作失败：" + e.getMessage());
			return rr;
		}
	}

	/**
	 * 废弃任务
	 * 
	 * @param instancesId
	 *            流程实例id
	 * @param deleteReason
	 *            废弃原因
	 * @return 2014年7月30日
	 */
	public @ResponseBody(datatype = "json") ResultResponse discardTask(
			String instancesId, String deleteReason, String taskId,
			String processKey, String currentUser) {
		ResultResponse rr = new ResultResponse();

		if (StringUtil.isEmpty(currentUser)) {
			rr.setResultCode("6");
			rr.setResultMess("currentUser为空");
			return rr;
		}

		if (StringUtil.isEmpty(processKey)) {
			rr.setResultCode("5");
			rr.setResultMess("processKey为空");
			return rr;
		}

		if (StringUtil.isEmpty(taskId)) {
			rr.setResultCode("4");
			rr.setResultMess("taskId为空");
			return rr;
		}

		if (StringUtil.isEmpty(instancesId)) {
			rr.setResultCode("3");
			rr.setResultMess("instancesId为空");
			return rr;
		}

		if (StringUtil.isEmpty(deleteReason)) {
			rr.setResultCode("2");
			rr.setResultMess("deleteReason为空");
			return rr;
		}

		try {
			// activitiService.cancleProcessInstances(instancesId, deleteReason,
			// taskId, processKey, currentUser);

			rr.setResultCode("1");
			rr.setResultMess("废弃任务操作成功");
			return rr;

		} catch (Exception e) {
			logger.error("实例id：" + instancesId + ",废弃失败：" + e.getMessage(), e);

			rr.setResultCode("0");
			rr.setResultMess("废弃任务操作失败：" + e.getMessage());
			return rr;
		}
	}

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
	public @ResponseBody(datatype = "json") ResultResponse cancelTask(
			String instancesId, String processKey, String cancelReason,
			String taskId, String currentUser) {
		ResultResponse rr = new ResultResponse();

		if (StringUtil.isEmpty(currentUser)) {
			rr.setResultCode("6");
			rr.setResultMess("currentUser为空");
			return rr;
		}

		if (StringUtil.isEmpty(taskId)) {
			rr.setResultCode("5");
			rr.setResultMess("taskId为空");
			return rr;
		}

		if (StringUtil.isEmpty(processKey)) {
			rr.setResultCode("4");
			rr.setResultMess("instancesId为空");
			return rr;
		}

		if (StringUtil.isEmpty(instancesId)) {
			rr.setResultCode("3");
			rr.setResultMess("processKey为空");
			return rr;
		}

		if (StringUtil.isEmpty(cancelReason)) {
			rr.setResultCode("2");
			rr.setResultMess("cancelReason为空");
			return rr;
		}

		TransactionManager tm = new TransactionManager();

		try {

			tm.begin();

			// 获得流程的所有节点
			List<ActivityImpl> activties = activitiService
					.getActivitImplListByProcessKey(processKey);

			// 获得当前活动任务
			List<Task> task = activitiService.getTaskService()
					.createTaskQuery().processInstanceId(instancesId).list();

			// 任务跳转到第一个节点
			// activitiService.completeTaskLoadCommonParamsWithDest(task.get(0)
			// .getId(), activties.get(1).getId(), cancelReason);

			tm.commit();

			rr.setResultCode("1");
			rr.setResultMess("撤销任务操作成功");
			return rr;

		} catch (Exception e) {
			logger.error("实例id：" + instancesId + ",撤销失败：" + e.getMessage(), e);

			rr.setResultCode("0");
			rr.setResultMess("撤销任务操作失败：" + e.getMessage());
			return rr;
		} finally {
			tm.release();
		}
	}

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
	public @ResponseBody(datatype = "json") ResultResponse delegateTask(
			String taskId, String fromUserId, String toUserId,
			String instancesId, String processKey) {
		ResultResponse rr = new ResultResponse();

		if (StringUtil.isEmpty(taskId)) {
			rr.setResultCode("6");
			rr.setResultMess("taskId为空");
			return rr;
		}

		if (StringUtil.isEmpty(fromUserId)) {
			rr.setResultCode("5");
			rr.setResultMess("fromUserId为空");
			return rr;
		}

		if (StringUtil.isEmpty(toUserId)) {
			rr.setResultCode("4");
			rr.setResultMess("toUserId为空");
			return rr;
		}

		if (StringUtil.isEmpty(instancesId)) {
			rr.setResultCode("3");
			rr.setResultMess("instancesId为空");
			return rr;
		}

		if (StringUtil.isEmpty(processKey)) {
			rr.setResultCode("2");
			rr.setResultMess("processKey为空");
			return rr;
		}

		TransactionManager tm = new TransactionManager();

		try {

			tm.begin();

			// 判断是否有没被签收
			boolean isClaim = activitiTaskService.isSignTask(taskId);
			if (!isClaim) {
				// 先签收
				activitiService.claim(taskId, fromUserId);
			}

			// 再转办
			activitiService.delegateTask(taskId, toUserId);

			// 添加转办记录
			// activitiTaskService.updateNodeChangeInfo(taskId, instancesId,
			// processKey, fromUserId, toUserId);

			tm.commit();

			rr.setResultCode("1");
			rr.setResultMess("转办任务操作成功");
			return rr;

		} catch (Exception e) {
			logger.error("任务id：" + taskId + ",转办失败：" + e.getMessage(), e);

			rr.setResultCode("0");
			rr.setResultMess("转办任务操作失败");
			return rr;
		} finally {
			tm.release();
		}
	}

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
	public @ResponseBody(datatype = "json") ResultResponse rejectToPreTask(
			String taskId, String rejectedReason, int rejectedType,
			String processKey, String currentUser,
			List<HashMap<String, Object>> nodeInfoList,
			List<HashMap<String, Object>> variableList) {
		ResultResponse rr = new ResultResponse();

		if (StringUtil.isEmpty(taskId)) {
			rr.setResultCode("5");
			rr.setResultMess("taskId为空");
			return rr;
		}

		if (StringUtil.isEmpty(rejectedType)) {
			rr.setResultCode("4");
			rr.setResultMess("rejectedReason为空");
			return rr;
		}

		// 基本参数转换
		TaskCondition task = new TaskCondition();
		task.setTaskId(taskId);
		task.setCompleteReason(rejectedReason);

		// 节点配置转换
		List<ActivitiNodeInfo> activitiNodeCandidateList = null;
		try {

			if (nodeInfoList != null && nodeInfoList.size() > 0) {

				activitiNodeCandidateList = new ArrayList<ActivitiNodeInfo>();

				for (int i = 0; i < nodeInfoList.size(); i++) {
					ActivitiNodeInfo node = new ActivitiNodeInfo();
					Map<String, Object> map = nodeInfoList.get(i);

					String nodeKey = (String) map.get("nodeKey");
					node.setNode_key(nodeKey);

					String nodeUserIds = (String) map.get("nodeUserIds");
					node.setNode_users_id(nodeUserIds);

					String nodeGroupIds = (String) map.get("nodeGroupIds");
					node.setNode_groups_id(nodeGroupIds);

					String isMulti = (String) map.get("isMulti");
					node.setIsMulti(isMulti);

					activitiNodeCandidateList.add(node);
				}
			}
		} catch (Exception e) {
			logger.error("驳回任务，节点配置转换出错：" + e.getMessage(), e);

			rr.setResultCode("3");
			rr.setResultMess("驳回任务，节点配置转换出错：" + e.getMessage());
			return rr;
		}

		// 参数转换
		List<Nodevariable> nodevariableList = null;
		try {
			if (variableList != null && variableList.size() > 0) {

				nodevariableList = new ArrayList<Nodevariable>();

				for (int i = 0; i < variableList.size(); i++) {
					Nodevariable variable = new Nodevariable();
					Map<String, Object> map = variableList.get(i);

					String paramName = (String) map.get("paramName");
					variable.setParam_name(paramName);

					String paramValue = (String) map.get("paramValue");
					variable.setParam_value(paramValue);

					nodevariableList.add(variable);
				}
			}
		} catch (Exception e) {
			logger.error("驳回任务，参数转换出错：" + e.getMessage(), e);

			rr.setResultCode("2");
			rr.setResultMess("驳回任务，参数转换出错：" + e.getMessage());
			return rr;
		}

		try {

			// activitiTaskService.rejectToPreTask(task,
			// activitiNodeCandidateList, nodevariableList, rejectedType);

			rr.setResultCode("1");
			rr.setResultMess("驳回任务操作成功");
			return rr;

		} catch (Exception e) {
			logger.error("任务id：" + taskId + ",驳回失败：" + e.getMessage(), e);

			rr.setResultCode("0");
			rr.setResultMess("驳回任务操作失败:" + e.getMessage());
			return rr;
		}
	}

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
	public @ResponseBody(datatype = "json") ResultResponse completeTask(
			HashMap<String, Object> taskMap,
			List<HashMap<String, Object>> nodeInfoList,
			List<HashMap<String, Object>> variableList) {

		ResultResponse rr = new ResultResponse();

		// 基本参数转换
		TaskCondition task = new TaskCondition();

		if (taskMap == null || taskMap.isEmpty()) {
			rr.setResultCode("5");
			rr.setResultMess("taskMap为空");
			return rr;
		}

		try {
			// 委托人
			String createUser = (String) taskMap.get("createUser");
			task.setCreateUser(createUser);
			// 被委托人
			String entrustUser = (String) taskMap.get("entrustUser");
			task.setEntrustUser(entrustUser);
			// 完成原因
			String completeReason = (String) taskMap.get("completeReason");
			task.setCompleteReason(completeReason);
			// 任务key
			String taskKey = (String) taskMap.get("taskKey");
			task.setTaskDefKey(taskKey);

			if (taskMap.get("currentUser") == null
					|| taskMap.get("currentUser").equals("")) {
				rr.setResultCode("6");
				rr.setResultMess("currentUser为空");
				return rr;
			} else {
				// 当前用户id
				String currentUser = (String) taskMap.get("currentUser");
				task.setCurrentUser(currentUser);
			}

			if (taskMap.get("taskId") == null
					|| taskMap.get("taskId").equals("")) {
				rr.setResultCode("7");
				rr.setResultMess("taskId为空");
				return rr;
			} else {
				// 任务id
				String taskId = (String) taskMap.get("taskId");
				task.setTaskId(taskId);
			}

			if (taskMap.get("processKey") == null
					|| taskMap.get("processKey").equals("")) {
				rr.setResultCode("8");
				rr.setResultMess("processKey为空");
				return rr;
			} else {
				// 流程定义key
				String processKey = (String) taskMap.get("processKey");
				task.setProcessKey(processKey);
			}

			if (taskMap.get("instancesId") == null
					|| taskMap.get("instancesId").equals("")) {
				rr.setResultCode("9");
				rr.setResultMess("instancesId为空");
				return rr;
			} else {
				// 流程实例id
				String instancesId = (String) taskMap.get("instancesId");
				task.setProcessIntsId(instancesId);
			}

			if (taskMap.get("taskState") == null
					|| taskMap.get("taskState").equals("")) {
				rr.setResultCode("10");
				rr.setResultMess("taskState为空");
				return rr;
			} else {
				// 任务状态 0 所有任务 1 未签收 2 已签收
				String taskState = (String) taskMap.get("taskState");
				task.setTaskState(taskState);
			}

		} catch (Exception e) {
			logger.error("通过任务，节点基本参数转换出错：" + e.getMessage(), e);

			rr.setResultCode("4");
			rr.setResultMess("通过任务，节点基本参数转换出错：" + e.getMessage());
			return rr;
		}

		// 节点配置转换
		List<ActivitiNodeInfo> activitiNodeCandidateList = null;
		try {

			if (nodeInfoList != null && nodeInfoList.size() > 0) {

				activitiNodeCandidateList = new ArrayList<ActivitiNodeInfo>();

				for (int i = 0; i < nodeInfoList.size(); i++) {
					ActivitiNodeInfo node = new ActivitiNodeInfo();
					Map<String, Object> map = nodeInfoList.get(i);

					String nodeKey = (String) map.get("nodeKey");
					node.setNode_key(nodeKey);

					String nodeUserIds = (String) map.get("nodeUserIds");
					node.setNode_users_id(nodeUserIds);

					String nodeGroupIds = (String) map.get("nodeGroupIds");
					node.setNode_groups_id(nodeGroupIds);

					String isMulti = (String) map.get("isMulti");
					node.setIsMulti(isMulti);

					activitiNodeCandidateList.add(node);
				}
			}
		} catch (Exception e) {
			logger.error("通过任务，节点配置转换出错：" + e.getMessage(), e);

			rr.setResultCode("3");
			rr.setResultMess("通过任务，节点配置转换出错：" + e.getMessage());
			return rr;
		}

		// 参数转换
		List<Nodevariable> nodevariableList = null;
		try {
			if (variableList != null && variableList.size() > 0) {

				nodevariableList = new ArrayList<Nodevariable>();

				for (int i = 0; i < variableList.size(); i++) {
					Nodevariable variable = new Nodevariable();
					Map<String, Object> map = variableList.get(i);

					String paramName = (String) map.get("paramName");
					variable.setParam_name(paramName);

					String paramValue = (String) map.get("paramValue");
					variable.setParam_value(paramValue);

					nodevariableList.add(variable);
				}
			}
		} catch (Exception e) {
			logger.error("通过任务，参数转换出错：" + e.getMessage(), e);

			rr.setResultCode("2");
			rr.setResultMess("通过任务，参数转换出错：" + e.getMessage());
			return rr;
		}

		try {

			// 记录委托关系(有就处理，没有就不处理)
			if (StringUtil.isNotEmpty(task.getCreateUser())
					&& StringUtil.isNotEmpty(task.getEntrustUser())) {

				// 当前用户就是被委托的用户
				if (task.getCurrentUser().equals(task.getEntrustUser())) {

					activitiTaskService.addEntrustTaskInfo(task);

					// 委托任务重写DELETE_REASON_字段信息
					task.setCompleteReason("[" + task.getCreateUser()
							+ "]的任务委托给[" + task.getEntrustUser() + "]完成");
				}

			}

			// 完成任务
			// activitiTaskService.completeTask(task, activitiNodeCandidateList,
			// nodevariableList);

			rr.setResultCode("1");
			rr.setResultMess("通过任务操作成功");
			return rr;

		} catch (Exception e) {
			logger.error(
					"任务id：" + task.getTaskId() + ",通过失败：" + e.getMessage(), e);

			rr.setResultCode("0");
			rr.setResultMess("通过任务操作失败：" + e.getMessage());
			return rr;
		}
	}

	/**
	 * 任务代办数统计
	 * 
	 * @param pernr
	 *            用户登录账号
	 * @param sysId
	 *            系统编号
	 * @return 2014年7月31日
	 */
	public @ResponseBody(datatype = "json") DataResponse countTaskNum(
			String pernr, String sysId) {
		DataResponse rr = new DataResponse();

		if (StringUtil.isEmpty(pernr)) {
			rr.setResultCode("2");
			rr.setResultMess("pernr为空");
			return rr;
		}

		try {

			int taskNum = activitiTaskService.countTaskNum(pernr, sysId);

			rr.setResultCode("1");
			rr.setResultData(taskNum + "");
			rr.setResultMess("任务代办数统计操作成功");
			return rr;

		} catch (Exception e) {
			logger.error("统计用户：" + pernr + "代办数失败：" + e.getMessage(), e);

			rr.setResultCode("0");
			rr.setResultMess("任务代办数统计操作失败" + e.getMessage());
			return rr;
		}
	}

	/**
	 * 任务待办数据
	 * 
	 * @param pernr
	 *            用户登录账号
	 * @param sysId
	 *            系统编号
	 * @return 2014年8月1日
	 */
	public @ResponseBody(datatype = "json") NoHandTaskResponse getNoHandleTask(
			String pernr, String sysId) {

		NoHandTaskResponse nhtr = new NoHandTaskResponse();

		if (StringUtil.isEmpty(pernr)) {
			nhtr.setResultCode("2");
			nhtr.setResultMess("pernr为空");
			return nhtr;
		}

		try {

			List<NoHandleTask> dataList = activitiTaskService.getNoHandleTask(
					pernr, sysId, 0, 0);

			List<NoHandTaskInfo> taskList = new ArrayList<NoHandTaskInfo>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			for (int i = 0; i < dataList.size(); i++) {
				NoHandleTask nt = dataList.get(i);

				NoHandTaskInfo nhti = new NoHandTaskInfo();
				nhti.setAppUrl(nt.getAppUrl());
				nhti.setBusinessKey(nt.getBusinessKey());
				if (nt.getCreateTime() != null) {
					nhti.setCreateTime(sdf.format(nt.getCreateTime()));
				} else {
					nhti.setCreateTime("");
				}
				nhti.setDealButtionName(nt.getDealButtionName());
				nhti.setDefId(nt.getDefId());
				nhti.setFromUser(nt.getFromUser());
				nhti.setFromUserName(nt.getFromUserName());
				nhti.setInstanceId(nt.getInstanceId());
				nhti.setProcessKey(nt.getProcessKey());
				nhti.setSender(nt.getSender());
				nhti.setSuspensionState(nt.getSuspensionState());
				nhti.setTaskDefKey(nt.getTaskDefKey());
				nhti.setTaskId(nt.getTaskId());
				nhti.setTaskState(nt.getTaskState());
				nhti.setTaskType(nt.getTaskType());
				nhti.setTitle(nt.getTitle());
				nhti.setUrl(nt.getUrl());
				nhti.setUserAccount(nt.getUserAccount());
				taskList.add(nhti);
			}

			nhtr.setResultCode("1");
			nhtr.setResultMess("获取任务待办数据操作成功");
			nhtr.setDataList(taskList);
			return nhtr;

		} catch (Exception e) {
			logger.error("统一代办数据查询出错：" + e.getMessage(), e);

			nhtr.setResultCode("0");
			nhtr.setResultMess("获取任务待办数据操作失败：" + e.getMessage());
			return nhtr;
		}
	}

	/**
	 * 获取流程实例任务追踪图
	 * 
	 * @param instancesId
	 *            流程实例id
	 * @param response
	 *            2014年8月6日
	 */
	public  void getProccessActivePic(String instancesId,
			HttpServletResponse response) {

		try {
			if (StringUtil.isNotEmpty(instancesId)) {
				OutputStream out = response.getOutputStream();
				activitiService.getProccessPic(instancesId, out);
			}
		} catch (Exception e) {
			logger.error(
					"获取流程实例" + instancesId + ",任务追踪图片失败：" + e.getMessage(), e);
		}
	}

	@Override
	public @ResponseBody ResultResponse startInstanceWithBussinessKey(String processKey, String businessKey,
			String currentUser) {

		
		ResultResponse response = new ResultResponse();
		try {
			String pid = activitiService.startPorcessInstance(processKey, businessKey,
					currentUser);
			

			response.setResultCode("success pid:"+pid);
			response.setResultMess("启动流程成功：processKey="+processKey+",businessKey="+businessKey+",currentUser="+currentUser);

		} catch (Exception e) {
			logger.error("启动流程失败processKey="+processKey+",businessKey="+businessKey+",currentUser="+currentUser, e);

			response.setResultCode("error");
			response.setResultMess("启动流程成功：processKey="+processKey+",businessKey="+businessKey+",currentUser="+currentUser);
		}
		return response;
	}
	
	
	@Override
	public @ResponseBody ResultResponse startSimpleInstanceWithBussinessKey(String processKey,
			String currentUser) {
		String businessKey = System.currentTimeMillis() + "_processKey";
		return startInstanceWithBussinessKey(processKey, businessKey,
				currentUser);
	}

}