package com.sany.workflow.business.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.bpmn.diagram.ProcessDiagramGenerator;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.frameworkset.web.servlet.ModelMap;

import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.util.ListInfo;
import com.frameworkset.util.StringUtil;
import com.sany.workflow.business.entity.ActNode;
import com.sany.workflow.business.entity.HisTaskInfo;
import com.sany.workflow.business.entity.ProIns;
import com.sany.workflow.business.entity.TaskInfo;
import com.sany.workflow.business.service.ActivitiBusinessService;
import com.sany.workflow.business.util.WorkflowConstants;
import com.sany.workflow.entity.ActivitiVariable;
import com.sany.workflow.entity.NodeControlParam;
import com.sany.workflow.entity.ProcessInst;
import com.sany.workflow.entity.TaskCondition;
import com.sany.workflow.entrust.entity.WfEntrust;
import com.sany.workflow.service.ActivitiService;
import com.sany.workflow.service.ActivitiTaskService;
import com.sany.workflow.service.ProcessException;
import com.sany.workflow.service.impl.PlatformKPIServiceImpl;

import edu.emory.mathcs.backport.java.util.Collections;

/**
 * @todo 工作流业务实现类
 * @author tanx
 * @date 2014年6月9日
 * 
 */
public class ActivitiBusinessImpl implements ActivitiBusinessService,
		org.frameworkset.spi.DisposableBean {

	private static Log logger = LogFactory.getLog(ActivitiBusinessImpl.class);

	private com.frameworkset.common.poolman.ConfigSQLExecutor executor;

	private ActivitiService activitiService;

	private ActivitiTaskService activitiTaskService;

	@Override
	public void destroy() throws Exception {
		// TODO Auto-generated method stub
	}

	/**
	 * 工号转域账号
	 * 
	 * @param userId
	 *            2014年8月22日
	 */
	public String changeToDomainAccount(String userId) {

		return AccessControl.getUserAccounByWorknumberOrUsername(userId);

	}

	@Override
	public void judgeAuthorityToPageState(String taskId, String processKey,
			String businessKey, String userAccount, ModelMap model) {

		// 页面状态 新增初始状态
		if (StringUtil.isEmpty(taskId) && StringUtil.isEmpty(businessKey)) {
			model.addAttribute(WorkflowConstants.PRO_PAGESTATE,
					WorkflowConstants.PRO_PAGESTATE_INIT);
		} else if (StringUtil.isNotEmpty(taskId)) {

		}
	}

	@Override
	public boolean judgeAuthority(String taskId, String processKey,
			String userAccount) {
		TransactionManager tm = new TransactionManager();

		userAccount = this.changeToDomainAccount(userAccount);

		try {
			tm.begin();

			if (AccessControl.isAdmin(userAccount)) {

				tm.commit();
				return true;

			} else {

				if (StringUtil.isEmpty(taskId)) {

					tm.commit();
					return false;

				} else {

					// 首先判断任务是否有没签收，如果签收，以签收人为准，如果没签收，则以该节点配置的人为准
					HistoricTaskInstance hisTask = activitiService
							.getHistoryService()
							.createHistoricTaskInstanceQuery().taskId(taskId)
							.singleResult();

					if (StringUtil.isNotEmpty(hisTask.getAssignee())) {

						if (userAccount.equals(hisTask.getAssignee())) {

							tm.commit();
							return true;
						}

					} else {
						// 任务未签收，根据任务id查询任务可处理人
						List<HashMap> candidatorList = executor.queryList(
								HashMap.class, "getNodeCandidates_wf", taskId,
								userAccount);

						if (candidatorList != null && candidatorList.size() > 0) {

							tm.commit();
							return true;
						}
					}

					// 最后查看当前用户的委托关系
					List<WfEntrust> entrustList = executor.queryList(
							WfEntrust.class, "getEntrustRelation_wf",
							userAccount, processKey, new Timestamp(hisTask
									.getStartTime().getTime()), new Timestamp(
									hisTask.getStartTime().getTime()));

					if (entrustList == null || entrustList.size() == 0) {

						tm.commit();
						return false;
					} else {

						tm.commit();
						return true;
					}
				}
			}

		} catch (Exception e) {
			throw new ProcessException(e);
		} finally {
			tm.release();
		}
	}

	/**
	 * 串并行判断
	 * 
	 * @param actNodeList
	 *            节点信息集合
	 * @param processKey
	 *            流程key 2014年8月21日
	 */
	private void isParrealOrSequence(ActNode actNode, String processKey) {
		// 获取流程定义XML中节点定义信息
		List<ActivityImpl> activties = activitiService
				.getActivitImplListByProcessKey(processKey);

		for (ActivityImpl activtie : activties) {

			if (activtie.getId().equals(actNode.getActId())) {

				if (activtie.isMultiTask()) {
					actNode.setIsMultiDefault(1);

					if (actNode.getIsSequential() == 0) {// 多实例串行
						actNode.setApproveType(WorkflowConstants.PRO_ACT_SEQ);
					} else if (actNode.getIsSequential() == 1) {// 多实例并行
						actNode.setApproveType(WorkflowConstants.PRO_ACT_MUL);
					}
				} else {
					actNode.setIsMultiDefault(0);

					if (actNode.getIsMulti() == 0) {
						actNode.setApproveType(WorkflowConstants.PRO_ACT_ONE);// 单实例
					} else if (actNode.getIsMulti() == 1
							&& actNode.getIsSequential() == 0) {// 多实例串行
						actNode.setApproveType(WorkflowConstants.PRO_ACT_SEQ);
					} else if (actNode.getIsMulti() == 1
							&& actNode.getIsSequential() == 1) {// 多实例并行
						actNode.setApproveType(WorkflowConstants.PRO_ACT_MUL);
					}
				}

			}
		}
	}

	@Override
	public void getProccessPic(String processKey, OutputStream out)
			throws IOException {
		InputStream is = null;
		try {
			if (StringUtil.isNotEmpty(processKey)) {
				ProcessDefinition processDefinition = activitiService
						.getProcessDefinitionByKey(processKey);
				String diagramResourceName = processDefinition
						.getDiagramResourceName();

				is = activitiService.getResourceAsStream(
						processDefinition.getDeploymentId(),
						diagramResourceName);

				byte[] b = new byte[1024];
				int len = -1;

				while ((len = is.read(b, 0, 1024)) != -1) {
					out.write(b, 0, len);
				}
				out.flush();
			}
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (Exception e) {

			}
		}
	}

	@Override
	public void getProccessActivePic(String processInstId, OutputStream out) {
		InputStream is = null;
		try {
			if (processInstId != null && !processInstId.equals("")) {
				// 运行中的活动id集合
				List<String> hightLightList = new ArrayList<String>();
				// 根据流程实例ID获取运行的实例
				List<Execution> exectionList = activitiService
						.getRuntimeService().createExecutionQuery()
						.processInstanceId(processInstId).list();
				// 获取运行实例的运行活动节点
				for (Execution execution : exectionList) {
					ExecutionEntity exeEntity = (ExecutionEntity) activitiService
							.getRuntimeService().createExecutionQuery()
							.executionId(execution.getId()).singleResult();
					String activitiId = exeEntity.getActivityId();
					hightLightList.add(activitiId);
				}
				// 根据流程实例iD获取流程定义KEY
				HistoricProcessInstance hiInstance = activitiService
						.getHisProcessInstanceById(processInstId);
				// 根据流程定义ID获取流程定义对应的实体对象
				BpmnModel bpmnModel = activitiService.getRepositoryService()
						.getBpmnModel(hiInstance.getProcessDefinitionId());

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
			}
		}
	}

	private List<ActNode> getWFNodeConfigInfo(String processKey,
			HashMap<String, Object> map) throws Exception {

		List<ActNode> actNodeList = executor.queryListBean(ActNode.class,
				"getWFNodeInfoList", map);

		if (actNodeList != null && actNodeList.size() > 0) {

			for (int i = 0; i < actNodeList.size(); i++) {

				ActNode actNode = actNodeList.get(i);
				// 串并行判断
				isParrealOrSequence(actNode, processKey);
			}
		}
		return actNodeList;
	}

	/**
	 * 设置节点默认配置
	 * 
	 * @param actNode
	 *            节点信息
	 * @param userLevel
	 *            用户部门层级按从大到小排列，如：0|50020020|50020025|50524052|50524186|
	 *            50527225
	 * @throws Exception
	 *             2014年8月21日
	 */
	private List<ActNode> setNodeDefaultConfig(List<ActNode> actNodeList,
			String[] orgArray) throws Exception {

		List<ActNode> newActNodeList = new ArrayList<ActNode>();

		String defaultKey = "";// 暂存节点key

		List<ActNode> oldActNodeList = null;

		for (int i = 0; i < actNodeList.size(); i++) {

			ActNode actNode = actNodeList.get(i);

			if (actNode.getActId().equals(defaultKey)) {
				oldActNodeList.add(actNode);

			} else {
				// 设置默认配置
				if (!defaultKey.equals(actNode.getActId())
						&& StringUtil.isNotEmpty(defaultKey)) {
					filteNodeList(orgArray, oldActNodeList, newActNodeList);
				}

				// 重置集合
				oldActNodeList = new ArrayList<ActNode>();

				oldActNodeList.add(actNode);

				// 记录当前nodeKey
				defaultKey = actNode.getActId();
			}

			// 最后一条数据
			if (actNodeList.size() - 1 == i) {
				filteNodeList(orgArray, oldActNodeList, newActNodeList);
			}

		}

		return newActNodeList;
	}

	/**
	 * 过滤节点数据
	 * 
	 * @param orgArray
	 *            用户组织层级
	 * @param oldActNodeList
	 *            原节点数据
	 * @param newActNodeList
	 *            新节点数据 2014年8月21日
	 */
	private void filteNodeList(String[] orgArray, List<ActNode> oldActNodeList,
			List<ActNode> newActNodeList) {

		for (int j = orgArray.length - 1; j >= 0; j--) {

			String orgid = orgArray[j];

			boolean outFlag = false;// 外层循环跳出标志

			for (int k = 0; k < oldActNodeList.size(); k++) {

				ActNode node = oldActNodeList.get(k);

				if (orgid.equals(node.getOrgId())
						&& StringUtil.isNotEmpty(node.getCandidateName())) {
					newActNodeList.add(node);

					outFlag = true;
					break;
				}
			}

			if (outFlag) {
				break;
			}

			// 循环到最上一层，如果还没有值,取一个对象设置为准
			if (j == 0) {
				newActNodeList.add(oldActNodeList.get(0));
			}
		}
	}

	/**
	 * 获取流程节点参数配置信息
	 * 
	 * @param nodeList
	 * @return 2014年5月27日
	 */
	private void getVariableMap(List<ActNode> actNodeList,
			Map<String, Object> paramMap) {

		// 流程参数
		if (actNodeList != null && actNodeList.size() > 0) {

			for (int i = 0; i < actNodeList.size(); i++) {
				ActNode node = actNodeList.get(i);

				// 节点配置
				if (StringUtil.isNotEmpty(node.getCandidateName())) {

					String[] userId = node.getCandidateName().split(",");

					StringBuffer accounts = new StringBuffer();
					if (userId.length > 0) {

						for (int j = 0; j < userId.length; j++) {

							if (j == 0) {
								accounts.append(changeToDomainAccount(userId[j]));
							} else {
								accounts.append(",").append(
										changeToDomainAccount(userId[j]));
							}
						}
					} else {
						accounts.append(changeToDomainAccount(userId[0]));
					}
					paramMap.put(node.getActId() + "_users",
							accounts.toString());

				}

			}
		}

	}

	@Override
	public void startProc(ProIns proIns, String businessKey, String processKey,
			Map<String, Object> paramMap) throws Exception {

		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();

			// 当前流程发起人，转域账号
			String currentUser = changeToDomainAccount(proIns.getUserAccount());

			List<ActNode> actNodeList = proIns.getActs();

			// 节点控制参数信息
			List<NodeControlParam> controlParamList = new ArrayList<NodeControlParam>();

			// 节点配置参数转换
			getVariableMap(actNodeList, paramMap);

			// 控制参数变量转换
			if (actNodeList != null && actNodeList.size() > 0) {

				for (int i = 0; i < actNodeList.size(); i++) {
					ActNode node = actNodeList.get(i);

					if (StringUtil.isNotEmpty(node.getActId())) {
						NodeControlParam nodeControl = new NodeControlParam();

						nodeControl.setDURATION_NODE(node.getNodeWorkTime());
						nodeControl.setIS_AUTO(node.getIsAuto());
						nodeControl.setIS_AUTOAFTER(node.getIsAutoAfter());
						nodeControl.setIS_CANCEL(node.getIsCancel());
						nodeControl.setIS_COPY(node.getIsCopy());
						nodeControl.setIS_DISCARD(node.getIsDiscard());
						nodeControl.setIS_EDIT(node.getIsEdit());
						nodeControl.setIS_EDITAFTER(node.getIsEditAfter());
						nodeControl.setIS_RECALL(node.getIsRecall());
						nodeControl.setIS_VALID(node.getIsValid());
						if (node.getApproveType().equals(
								WorkflowConstants.PRO_ACT_ONE)) {// 单实例
							nodeControl.setIS_MULTI(0);
							nodeControl.setIS_SEQUENTIAL(0);
						} else if (node.getApproveType().equals(
								WorkflowConstants.PRO_ACT_SEQ)) {// 多实例串行
							nodeControl.setIS_MULTI(1);
							nodeControl.setIS_SEQUENTIAL(0);
						} else if (node.getApproveType().equals(
								WorkflowConstants.PRO_ACT_MUL)) {// 多实例并行
							nodeControl.setIS_MULTI(1);
							nodeControl.setIS_SEQUENTIAL(1);
						}
						nodeControl.setNODE_DESCRIBE(node.getNodeDescribe());
						nodeControl.setNODE_KEY(node.getActId());
						nodeControl.setNODE_NAME(node.getActName());
						nodeControl.setPROCESS_KEY(processKey);
						nodeControl.setTASK_URL(node.getTaskUrl());
						nodeControl.setBUSSINESSCONTROLCLASS(node
								.getBussinessControlClass());

						controlParamList.add(nodeControl);
					}
				}
			}

			// kpi设值
			PlatformKPIServiceImpl.setWorktimelist(controlParamList);

			// 开启流程实例
			ProcessInstance processInstance = activitiService.startProcDef(
					businessKey, processKey, paramMap, currentUser);

			// 记录节点控制变量与流程实例关联
			activitiService.addNodeWorktime(processKey,
					processInstance.getId(), controlParamList);

			// 获取流程第一个任务节点
			Task task = activitiService.getCurrentTask(processInstance.getId());

			String remark = "提交流程&nbsp;&nbsp;&nbsp;&nbsp;备注:["
					+ activitiService.getUserInfoMap().getUserName(currentUser)
					+ "]提交";

			// 完成任务
			activitiService.completeTaskWithReason(task.getId(), currentUser,
					null, remark);

			tm.commit();

		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			PlatformKPIServiceImpl.setWorktimelist(null);
			tm.release();
		}
	}

	@Override
	public List<HisTaskInfo> getProcHisInfo(String processId) throws Exception {
		TransactionManager tms = new TransactionManager();

		try {
			tms.begin();

			// 历史任务记录
			List<HisTaskInfo> taskList = executor.queryList(HisTaskInfo.class,
					"selectTaskHistorById_wf", processId);

			// 转办记录与历史记录排序
			delegateTaskInfo(taskList, processId);

			if (taskList != null && taskList.size() != 0) {

				for (int j = 0; j < taskList.size(); j++) {

					HisTaskInfo hti = taskList.get(j);

					// 处理人转成中文名称
					hti.setASSIGNEE_NAME(activitiService.userIdToUserName(
							hti.getASSIGNEE_(), "2"));
					// 判断是否超时
					judgeOverTime(hti);
					// 处理耗时
					handleDurationTime(hti);
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

	/**
	 * 转办关系日志处理
	 * 
	 * @param hti
	 *            2014年9月2日
	 */
	public void delegateTaskInfo(List<HisTaskInfo> taskList,
			String processInstId) {

		try {
			// 转办记录
			List<HisTaskInfo> delegateTaskList = executor.queryList(
					HisTaskInfo.class, "getChangeTaskInfoById_wf",
					processInstId);

			if (delegateTaskList != null && delegateTaskList.size() > 0) {

				taskList.addAll(delegateTaskList);

				Collections.sort(taskList, new Comparator<HisTaskInfo>() {
					public int compare(HisTaskInfo a, HisTaskInfo b) {

						Timestamp starttime = a.getEND_TIME_() == null ? new Timestamp(
								new Date().getTime()) : a.getEND_TIME_();
						Timestamp endtime = b.getEND_TIME_() == null ? new Timestamp(
								new Date().getTime()) : b.getEND_TIME_();

						return starttime.compareTo(endtime);
					}
				});
			}

		} catch (Exception e) {
			throw new ProcessException(e);
		}
	}

	/**
	 * 处理耗时
	 * 
	 * @param taskList
	 *            2014年7月1日
	 */
	private void handleDurationTime(HisTaskInfo tm) {

		// 节点耗时转换
		if (StringUtil.isNotEmpty(tm.getDURATION_())) {
			long mss = Long.parseLong(tm.getDURATION_());
			tm.setDURATION_(StringUtil.formatTimeToString(mss));
		} else {
			// 流程未结束，以系统当前时间计算耗时
			Date startTime = tm.getSTART_TIME_();

			tm.setDURATION_(StringUtil.formatTimeToString(new Date().getTime()
					- startTime.getTime()));
		}

		// 节点处理工时转换
		if (tm.getDURATION_NODE() != null) {
			long worktime = Long.parseLong(tm.getDURATION_NODE());
			tm.setDURATION_NODE(StringUtil.formatTimeToString(worktime));
		}

	}

	/**
	 * 预警超时判断
	 * 
	 * @param hti
	 *            2014年8月23日
	 */
	private void judgeOverTime(HisTaskInfo hti) {

		Calendar c = Calendar.getInstance();

		Date endDate = null;// 任务结束时间点

		// 已完成节点任务
		c.setTime(hti.getEND_TIME_());
		endDate = c.getTime();

		// 判断超时
		if (hti.getOVERTIME() != null) {
			Date overDate = hti.getOVERTIME();// 节点任务超时时间点

			// 超时判断
			if (endDate.after(overDate)) {
				hti.setIsOverTime("1");// 超时
			} else {
				hti.setIsOverTime("0");// 没有超时
			}
		} else {
			hti.setIsOverTime("0");// 没有超时
		}

		// 判断预警
		if (hti.getALERTTIME() != null) {
			Date alertDate = hti.getALERTTIME();// 节点任务预警时间点

			// 预警判断
			if (endDate.after(alertDate)) {
				hti.setIsAlertTime("1");// 预警
			} else {
				hti.setIsAlertTime("0");// 未预警
			}
		} else {
			hti.setIsAlertTime("0");// 未预警
		}
	}

	@Override
	public ModelMap toDealTask(String processKey, String processId,
			String taskId, ModelMap model) throws Exception {

		model.addAttribute("processKey", processKey);

		// 获取流程实例的处理记录
		List<HisTaskInfo> taskHistorList = getProcHisInfo(processId);
		model.addAttribute("taskHistorList", taskHistorList);

		// 获取流程节点配置信息
		List<ActNode> actList = getWFNodeConfigInfoByCondition(processKey,
				processId, taskId);
		model.addAttribute("actList", actList);

		// 当前任务节点信息
		TaskInfo task = getCurrentNodeInfo(taskId);
		model.addAttribute("task", task);

		// 可驳回的节点信息
		List<ActNode> backActNodeList = getBackActNode(processId,
				task.getTaskDefKey());
		model.addAttribute("backActNodeList", backActNodeList);

		return model;
	}

	@Override
	public List<ActNode> getWFNodeConfigInfoByCondition(String processKey,
			String processInstId, String taskId) throws Exception {

		TransactionManager tms = new TransactionManager();

		try {
			tms.begin();

			// 从扩展表中获取节点信息
			List<ActNode> nodeList = executor.queryList(ActNode.class,
					"getAllActivitiNodesInfo_wf", processInstId, processKey);

			if (nodeList == null) {
				tms.commit();
				return null;
			}

			if (nodeList != null && nodeList.size() > 0) {

				for (int i = 0; i < nodeList.size(); i++) {

					ActNode actNode = nodeList.get(i);
					// 串并行判断
					isParrealOrSequence(actNode, processKey);
				}
			}

			// 根据流程实例ID 获取流程的参数变量信息
			List<ActivitiVariable> variableList = executor.queryList(
					ActivitiVariable.class, "getVariableListById_wf",
					processInstId);

			if (variableList != null && variableList.size() > 0) {

				StringBuffer users = new StringBuffer();

				for (int i = 0; i < nodeList.size(); i++) {
					ActNode ani = nodeList.get(i);

					// 拼接匹配对象
					users.append(ani.getActId() + "_users");

					for (int j = 0; j < variableList.size(); j++) {

						ActivitiVariable av = variableList.get(j);

						if (StringUtil.isNotEmpty(av.getTEXT_())) {

							// 用户
							if (av.getNAME_().equals(users.toString())) {

								ani.setCandidateName(av.getTEXT_());
								ani.setRealName(activitiService
										.userIdToUserName(av.getTEXT_(), "1"));
							}

						}
					}
					users.setLength(0);
				}
			}

			tms.commit();

			return nodeList;

		} catch (Exception e) {
			throw new ProcessException(e);
		} finally {
			tms.release();
		}
	}

	@Override
	public List<ActNode> getWFNodeInfoByCondition(String processKey,
			String processInstId) throws Exception {

		TransactionManager tms = new TransactionManager();

		try {
			tms.begin();

			// 从扩展表中获取节点信息
			List<ActNode> nodeList = executor.queryList(ActNode.class,
					"getAllActivitiNodesInfo_wf", processInstId, processKey);

			if (nodeList == null) {
				tms.commit();
				return null;
			}

			// 根据流程实例ID 获取流程的参数变量信息
			List<ActivitiVariable> variableList = executor.queryList(
					ActivitiVariable.class, "getVariableListById_wf",
					processInstId);

			if (variableList != null && variableList.size() > 0) {

				StringBuffer users = new StringBuffer();

				for (int i = 0; i < nodeList.size(); i++) {
					ActNode ani = nodeList.get(i);

					// 拼接匹配对象
					users.append(ani.getActId() + "_users");

					for (int j = 0; j < variableList.size(); j++) {

						ActivitiVariable av = variableList.get(j);

						if (StringUtil.isNotEmpty(av.getTEXT_())) {

							// 用户
							if (av.getNAME_().equals(users.toString())) {

								ani.setCandidateName(av.getTEXT_());
								ani.setRealName(activitiService
										.userIdToUserName(av.getTEXT_(), "1"));
							}

						}
					}

					users.setLength(0);

				}
			}

			tms.commit();

			return nodeList;

		} catch (Exception e) {
			throw new ProcessException(e);
		} finally {
			tms.release();
		}
	}

	@Override
	public List<ActNode> getWFNodeConfigInfoForOrg(String processKey,
			String userId) throws Exception {

		userId = changeToDomainAccount(userId);

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("processKey", processKey);
		map.put("businessType", "1");

		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();

			// 用户组织节点和层级
			String userLevel = executor.queryObject(String.class,
					"getUserLevel", userId);

			map.put("userLevel", userLevel.split("\\|"));

			List<ActNode> actNodeList = this.getWFNodeConfigInfo(processKey,
					map);

			tm.commit();

			if (actNodeList != null && actNodeList.size() > 0) {

				// 通过组织结构配置的流程，节点人为空采用默认配置的判断
				return setNodeDefaultConfig(actNodeList,
						(String[]) map.get("userLevel"));
			} else {
				return actNodeList;
			}

		} catch (Exception e) {
			throw new Exception("获取组织类型节点配置出错：" + e.getMessage());
		} finally {
			tm.release();
		}
	}

	@Override
	public List<ActNode> getWFNodeConfigInfoForbussiness(String processKey)
			throws Exception {

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("processKey", processKey);
		map.put("businessType", "2");

		return this.getWFNodeConfigInfo(processKey, map);
	}

	@Override
	public List<ActNode> getWFNodeConfigInfoForCommon(String processKey)
			throws Exception {

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("processKey", processKey);
		map.put("businessType", "0");

		return this.getWFNodeConfigInfo(processKey, map);
	}

	@Override
	public void completeTask(ProIns proIns, String processKey,
			Map<String, Object> paramMap) throws Exception {

		// 当前用户转成域账号
		String userAccount = this
				.changeToDomainAccount(proIns.getUserAccount());
		proIns.setUserAccount(userAccount);

		// 委托用户转成域账号
		String fromUser = this.changeToDomainAccount(proIns
				.getNowTaskFromUser());
		// 被委托用户转成域账号
		String toUser = this.changeToDomainAccount(proIns.getNowTaskToUser());

		// 权限判断
		if (!judgeAuthority(proIns.getNowtaskId(), processKey, userAccount)) {
			throw new Exception("您没有权限通过任务！");
		}

		TransactionManager tm = new TransactionManager();
		try {

			tm.begin();

			String remark = "";

			// 记录委托关系(有就处理，没有就不处理)
			if (StringUtil.isNotEmpty(userAccount)
					&& StringUtil.isNotEmpty(toUser)) {

				// 当前用户就是被委托的用户
				if (userAccount.equals(toUser)) {

					TaskCondition task = new TaskCondition();
					task.setTaskId(proIns.getNowtaskId());
					task.setCreateUser(fromUser);
					task.setEntrustUser(toUser);
					task.setProcessIntsId(proIns.getProInsId());
					task.setProcessKey(processKey);

					activitiTaskService.addEntrustTaskInfo(task);

					// 追加DELETE_REASON_字段信息
					remark = "&nbsp;&nbsp;&nbsp;&nbsp;备注:["
							+ activitiService.getUserInfoMap().getUserName(
									userAccount)
							+ "]的任务委托给["
							+ activitiService.getUserInfoMap().getUserName(
									toUser) + "]完成";
				}

			} else {
				remark = "&nbsp;&nbsp;&nbsp;&nbsp;备注:["
						+ activitiService.getUserInfoMap().getUserName(
								userAccount) + "]完成";
			}

			proIns.setRemark("通过流程&nbsp;&nbsp;&nbsp;&nbsp;"
					+ proIns.getRemark() + remark);

			// 节点配置参数转换
			getVariableMap(proIns.getActs(), paramMap);

			completeTask(proIns, paramMap);

			tm.commit();

		} catch (Exception e) {
			throw new Exception("处理任务出错：" + e.getMessage());
		} finally {
			tm.release();
		}
	}

	private void completeTask(ProIns proIns, Map<String, Object> paramMap)
			throws Exception {
		// 未签收
		if (!isSignTask(proIns.getNowtaskId())) {

			if (StringUtil.isEmpty(proIns.getToTaskKey())) {

				if (StringUtil.isEmpty(proIns.getRemark())) {

					activitiService.completeTask(proIns.getNowtaskId(),
							proIns.getUserAccount(), paramMap);
				} else {
					activitiService.completeTaskWithReason(
							proIns.getNowtaskId(), proIns.getUserAccount(),
							paramMap, proIns.getRemark());
				}

			} else {

				if (StringUtil.isEmpty(proIns.getRemark())) {

					activitiService.completeTaskWithLocalVariables(
							proIns.getNowtaskId(), proIns.getUserAccount(),
							paramMap, proIns.getToTaskKey());
				} else {
					activitiService
							.completeTaskWithLocalVariablesReason(
									proIns.getNowtaskId(),
									proIns.getUserAccount(), paramMap,
									proIns.getToTaskKey(), proIns.getRemark());
				}
			}

		} else {// 已签收

			if (StringUtil.isEmpty(proIns.getToTaskKey())) {

				if (StringUtil.isEmpty(proIns.getRemark())) {

					activitiService.completeTask(proIns.getNowtaskId(),
							paramMap);

				} else {
					activitiService
							.completeTaskWithReason(proIns.getNowtaskId(),
									paramMap, proIns.getRemark());
				}

			} else {
				if (StringUtil.isEmpty(proIns.getRemark())) {
					activitiService.completeTaskLoadCommonParams(
							proIns.getNowtaskId(), paramMap,
							proIns.getToTaskKey());
				} else {
					activitiService.completeTaskLoadCommonParamsReason(
							proIns.getNowtaskId(), paramMap,
							proIns.getToTaskKey(), proIns.getRemark());
				}
			}

		}
	}

	@Override
	public void discardTask(ProIns proIns, String processKey) throws Exception {

		// 当前用户转成域账号
		String userAccount = this
				.changeToDomainAccount(proIns.getUserAccount());

		// 权限判断
		if (!judgeAuthority(proIns.getNowtaskId(), processKey, userAccount)) {
			throw new Exception("您没有权限废弃任务！");
		}

		if (!isSignTask(proIns.getNowtaskId())) {
			// 先签收
			activitiService.claim(proIns.getNowtaskId(), userAccount);
		}

		activitiService.cancleProcessInstances(proIns.getProInsId(),
				proIns.getRemark(), proIns.getNowtaskId(), processKey,
				userAccount);
	}

	/**
	 * 判断任务是否被签收
	 * 
	 * @param taskId
	 * @return 2014年8月26日
	 */
	private boolean isSignTask(String taskId) throws Exception {

		HashMap map = executor.queryObject(HashMap.class,
				"getCurrentTaskInfoById_wf", taskId);

		if (map != null && map.get("ASSIGNEE_") != null) {
			return true;
		} else {
			return false;
		}

	}

	@Override
	public void delegateTask(ProIns proIns, String processKey) throws Exception {
		// 当前用户转成域账号
		String userAccount = this
				.changeToDomainAccount(proIns.getUserAccount());

		// 委托用户转成域账号
		String fromUser = this.changeToDomainAccount(proIns
				.getNowTaskFromUser());

		// 被委托用户转成域账号
		String toUser = this.changeToDomainAccount(proIns.getNowTaskToUser());

		// 转办用户转成域账号
		String delegateUser = this.changeToDomainAccount(proIns
				.getDelegateUser());

		// 权限判断
		if (!judgeAuthority(proIns.getNowtaskId(), processKey, userAccount)) {
			throw new Exception("您没有权限转办任务！");
		}

		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();

			// 记录委托关系(有就处理，没有就不处理)
			if (StringUtil.isNotEmpty(userAccount)
					&& StringUtil.isNotEmpty(toUser)) {

				// 当前用户就是被委托的用户
				if (userAccount.equals(toUser)) {

					TaskCondition task = new TaskCondition();
					task.setTaskId(proIns.getNowtaskId());
					task.setCreateUser(fromUser);
					task.setEntrustUser(toUser);
					task.setProcessIntsId(proIns.getProInsId());
					task.setProcessKey(processKey);

					activitiTaskService.addEntrustTaskInfo(task);
				}

			}

			if (!isSignTask(proIns.getNowtaskId())) {
				// 先签收
				activitiService.claim(proIns.getNowtaskId(), userAccount);
			}

			// 再转办
			activitiService.delegateTask(proIns.getNowtaskId(), delegateUser);

			String reamrk = "转办流程&nbsp;&nbsp;&nbsp;&nbsp;"
					+ proIns.getRemark()
					+ "&nbsp;&nbsp;&nbsp;&nbsp;备注:["
					+ activitiService.getUserInfoMap().getUserName(userAccount)
					+ "]将任务转办给["
					+ activitiService.getUserInfoMap()
							.getUserName(delegateUser) + "]";

			// 在扩展表中添加转办记录
			activitiTaskService.updateNodeChangeInfo(proIns.getNowtaskId(),
					proIns.getProInsId(), processKey, userAccount,
					delegateUser, reamrk);

			tm.commit();
		} catch (Exception e) {
			throw new Exception("转办任务出错:" + e.getMessage());
		} finally {
			tm.release();
		}
	}

	@Override
	public void cancelTask(ProIns proIns, String processKey) throws Exception {
		// 当前用户转成域账号
		String userAccount = this
				.changeToDomainAccount(proIns.getUserAccount());

		// 权限判断
		if (!judgeAuthority(proIns.getNowtaskId(), processKey, userAccount)) {
			throw new Exception("您没有权限撤销任务！");
		}

		TransactionManager tm = new TransactionManager();

		try {
			tm.begin();

			// 获取第一人工节点信息
			HistoricTaskInstance hiTask = activitiService.getFirstTask(proIns
					.getProInsId());

			String currentUser = activitiService.getUserInfoMap().getUserName(
					userAccount);

			String remark = "撤回流程&nbsp;&nbsp;&nbsp;&nbsp;" + proIns.getRemark()
					+ "&nbsp;&nbsp;&nbsp;&nbsp;备注:" + currentUser + "将任务撤回至["
					+ hiTask.getName() + "]";

			if (!isSignTask(proIns.getNowtaskId())) {
				// 先签收
				activitiService.claim(proIns.getNowtaskId(), userAccount);
			}

			// 撤销任务
			activitiService.completeTaskLoadCommonParamsWithDest(
					proIns.getNowtaskId(), hiTask.getTaskDefinitionKey(),
					remark);

			// 日志记录撤销操作
			activitiService.addDealTask(proIns.getNowtaskId(), currentUser,
					"2", proIns.getProInsId(), processKey, remark,
					hiTask.getTaskDefinitionKey(), hiTask.getName());

			tm.commit();
		} catch (Exception e) {
			throw new Exception("撤销任务出错:" + e.getMessage());
		} finally {
			tm.release();
		}
	}

	@Override
	public void rejectToPreTask(ProIns proIns, String processKey,
			Map<String, Object> paramMap) throws Exception {

		// 当前用户转成域账号
		String userAccount = this
				.changeToDomainAccount(proIns.getUserAccount());

		// 权限判断
		if (!judgeAuthority(proIns.getNowtaskId(), processKey, userAccount)) {
			throw new Exception("您没有权限驳回任务！");
		}

		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();

			// 获取参数配置信息
			getVariableMap(proIns.getActs(), paramMap);

			if (!isSignTask(proIns.getNowtaskId())) {
				// 先签收
				activitiService.claim(proIns.getNowtaskId(), userAccount);
			}

			String remark = "驳回流程&nbsp;&nbsp;&nbsp;&nbsp;" + proIns.getRemark()
					+ "&nbsp;&nbsp;&nbsp;&nbsp;备注:"
					+ activitiService.getUserInfoMap().getUserName(userAccount)
					+ "将任务驳回至[" + proIns.getToActName() + "]";

			if (StringUtil.isNotEmpty(proIns.getRemark())) {
				activitiService.getTaskService().rejecttoTask(
						proIns.getNowtaskId(), paramMap, remark,
						proIns.getRejectToActId(),
						proIns.getIsReturn() == 1 ? true : false);
			} else {
				activitiService.getTaskService().rejecttoTask(
						proIns.getNowtaskId(), paramMap,
						proIns.getRejectToActId(),
						proIns.getIsReturn() == 1 ? true : false);
			}

			// 日志记录驳回操作
			activitiService.addDealTask(proIns.getNowtaskId(), activitiService
					.getUserInfoMap().getUserName(userAccount), "1", proIns
					.getProInsId(), processKey, remark, proIns
					.getRejectToActId(), proIns.getToActName());

			tm.commit();
		} catch (Exception e) {
			throw new ProcessException(e);
		} finally {
			tm.release();
		}
	}

	@Override
	public int getMyselfTaskNum(String currentUser, String processKey)
			throws Exception {
		// 当前用户转成域账号
		currentUser = this.changeToDomainAccount(currentUser);

		try {

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("assignee", currentUser);
			params.put("processKey", processKey);

			// 当前用户的任务数
			int taskNum = executor.queryObjectBean(int.class,
					"countTaskNum_wf", params);

			return taskNum;
		} catch (Exception e) {
			throw new Exception("获取用户待办任务总数出错：" + e.getMessage());
		}
	}

	@Override
	public int getEntrustTaskNum(String currentUser, String processKey)
			throws Exception {
		// 当前用户转成域账号
		currentUser = this.changeToDomainAccount(currentUser);

		try {

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("assignee", currentUser);
			params.put("processKey", processKey);

			// 根据当前用户获取委托关系列表数据
			List<WfEntrust> entrustList = executor.queryList(WfEntrust.class,
					"selectEntrustList", currentUser);

			// 没有委托关系，不需要去查任务数据
			int entrustTaskNum = 0;
			if (entrustList != null && entrustList.size() > 0) {
				params.put("entrustList", entrustList);

				entrustTaskNum = executor.queryObjectBean(int.class,
						"countEntrustTaskNum_wf", params);
			}

			return entrustTaskNum;
		} catch (Exception e) {
			throw new Exception("获取用户委托任务总数出错：" + e.getMessage());
		}
	}

	@Override
	public ListInfo getMyselfTaskList(String currentUser, String processKey,
			long offset, int pagesize) throws Exception {

		// 当前用户转成域账号
		currentUser = this.changeToDomainAccount(currentUser);

		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("assignee", currentUser);
			params.put("processKey", processKey);

			ListInfo listInfo = executor.queryListInfoBean(TaskInfo.class,
					"getMyselfTask_wf", offset, pagesize, params);

			List<TaskInfo> taskList = listInfo.getDatas();

			if (taskList != null && taskList.size() != 0) {

				for (int i = 0; i < taskList.size(); i++) {
					TaskInfo ti = taskList.get(i);

					ti.setFromUserName(activitiService.getUserInfoMap()
							.getUserName(ti.getFromUser()));

					ti.setSenderName(activitiService.getUserInfoMap()
							.getUserName(ti.getSender()));

					if (StringUtil.isNotEmpty(ti.getFromUser())) {
						ti.setTaskType(2);// 转办任务
					} else {
						ti.setTaskType(0);// 自己任务
					}
				}
			}

			tm.commit();

			return listInfo;
		} catch (Exception e) {
			throw new Exception("获取待办任务数据出错：" + e.getMessage());
		} finally {
			tm.release();
		}
	}

	@Override
	public ListInfo getEntrustTaskList(String currentUser, String processKey,
			long offset, int pagesize) throws Exception {
		// 当前用户转成域账号
		currentUser = this.changeToDomainAccount(currentUser);

		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();

			// 根据当前用户获取委托关系列表数据
			List<WfEntrust> entrustRelationList = executor.queryList(
					WfEntrust.class, "selectEntrustList", currentUser);

			if (entrustRelationList != null && entrustRelationList.size() > 0) {

				Map<String, Object> entrustMap = new HashMap<String, Object>();
				entrustMap.put("entrustList", entrustRelationList);
				entrustMap.put("assignee", currentUser);

				// 根据当前用户获取委托关系列表数据
				ListInfo listInfo = executor.queryListInfoBean(TaskInfo.class,
						"selectNoHandleEntrustTask_wf", offset, pagesize,
						entrustMap);

				List<TaskInfo> taskList = listInfo.getDatas();

				if (taskList != null && taskList.size() != 0) {

					for (int i = 0; i < taskList.size(); i++) {
						TaskInfo ti = taskList.get(i);

						ti.setFromUserName(activitiService.getUserInfoMap()
								.getUserName(ti.getFromUser()));

						ti.setSenderName(activitiService.getUserInfoMap()
								.getUserName(ti.getSender()));

					}
				}

				tm.commit();

				return listInfo;

			} else {

				tm.commit();

				return null;
			}

		} catch (Exception e) {
			throw new Exception("获取待办任务数据出错：" + e.getMessage());
		} finally {
			tm.release();
		}
	}

	@Override
	public List<ActNode> getBackActNode(String processId, String currentTaskKey)
			throws Exception {

		// 获取当前流程实例下处理过的节点集合
		List<HashMap> taskKeyList = executor.queryList(HashMap.class,
				"getTaskKeyList_wf", processId);

		List<ActNode> backActNodeList = new ArrayList<ActNode>();

		// 去重复key
		if (taskKeyList != null && taskKeyList.size() > 0) {
			StringBuffer keys = new StringBuffer();

			for (int i = 0; i < taskKeyList.size(); i++) {
				HashMap map = taskKeyList.get(i);
				String taskKey = map.get("ACT_ID_") + "";
				String taskName = map.get("ACT_NAME_") + "";

				if (taskKey.equals(currentTaskKey)) {
					break;
				}

				if (keys.toString().contains(taskKey)) {
					continue;
				}

				if (i == 0) {
					keys.append(taskKey);
				} else {
					keys.append(",").append(taskKey);
				}

				ActNode node = new ActNode();
				node.setActId(taskKey);
				node.setActName(taskName);

				backActNodeList.add(node);
			}
		}

		return backActNodeList;
	}

	@Override
	public void approveWorkFlow(ProIns proIns, String processKey,
			Map<String, Object> paramMap) throws Exception {

		String operType = proIns.getOperateType().toLowerCase();

		if (WorkflowConstants.PRO_OPE_TYPE_PASS.equals(operType)) {// 通过任务
			this.completeTask(proIns, processKey, paramMap);
		} else if (WorkflowConstants.PRO_OPE_TYPE_REJECT.equals(operType)) {// 驳回任务
			this.rejectToPreTask(proIns, processKey, paramMap);
		} else if (WorkflowConstants.PRO_OPE_TYPE_RECALL.equals(operType)) {// 撤回任务
			this.cancelTask(proIns, processKey);
		} else if (WorkflowConstants.PRO_OPE_TYPE_TOEND.equals(operType)) {// 废弃任务
			this.discardTask(proIns, processKey);
		} else if (WorkflowConstants.PRO_OPE_TYPE_TURNTO.equals(operType)) {// 转办任务
			this.delegateTask(proIns, processKey);
		}
	}

	@Override
	public TaskInfo getCurrentNodeInfo(String taskId) throws Exception {
		return executor.queryObject(TaskInfo.class, "getTaskInfoByTaskId_wf",
				taskId);
	}

	@Override
	public ModelMap toViewTask(String taskId, String bussinessKey,
			String userId, ModelMap model) throws Exception {

		// 当前用户转成域账号
		String userAccount = this.changeToDomainAccount(userId);

		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();

			// 根据bussinessKey获取流程实例
			ProcessInst inst = executor.queryObject(ProcessInst.class,
					"getProcessByBusinesskey_wf", bussinessKey);
			model.addAttribute("processKey", inst.getKEY_());

			// 获取流程实例的处理记录
			List<HisTaskInfo> taskHistorList = getProcHisInfo(inst
					.getPROC_INST_ID_());
			model.addAttribute("taskHistorList", taskHistorList);

			// 流程未完成
			if (inst.getEND_TIME_() == null) {

				String taskIdTemp = "";
				boolean authorFlag = false; // 当前用户是不是审批人或者管理员

				if (StringUtil.isEmpty(taskId)) {

					// 根据流程实例ID获取运行任务
					List<Task> taskList = activitiService
							.listTaskByProcessInstanceId(inst
									.getPROC_INST_ID_());

					for (int i = 0; i < taskList.size(); i++) {
						Task task = taskList.get(i);

						// 判断用户是不是当前审批人或者管理员
						if (judgeAuthority(task.getId(), inst.getKEY_(),
								userAccount)) {

							taskIdTemp = task.getId();
							authorFlag = true;
							break;
						}

						if (i == taskList.size() - 1) {
							taskIdTemp = task.getId();// 默认可以查看最后一个任务的信息
							authorFlag = false;
						}
					}

				} else {

					taskIdTemp = taskId;

					// 判断用户是不是当前审批人或者管理员
					if (judgeAuthority(taskIdTemp, inst.getKEY_(), userAccount)) {
						authorFlag = true;
					} else {
						authorFlag = false;
					}
				}

				// 获取流程节点配置信息(含控制变量参数)
				List<ActNode> actList = getWFNodeConfigInfoByCondition(
						inst.getKEY_(), inst.getPROC_INST_ID_(), taskIdTemp);
				model.addAttribute("actList", actList);

				// 当前任务节点信息
				TaskInfo task = getCurrentNodeInfo(taskIdTemp);
				model.addAttribute("task", task);

				// 当前用户是流程发起人
				if (inst.getSTART_USER_ID_().equals(userAccount)) {
					// // 获取流程第一个任务节点
					// HistoricTaskInstance hiTask = activitiService
					// .getFirstTask(inst.getPROC_INST_ID_());
					// TaskInfo taskInfo = getCurrentNodeInfo(hiTask.getId());
					// task.setIsRecall(taskInfo.getIsRecall());
					// task.setIsCancel(taskInfo.getIsCancel());
					// task.setIsDiscard(taskInfo.getIsDiscard());

					model.addAttribute(WorkflowConstants.PRO_PAGESTATE,
							WorkflowConstants.PRO_PAGESTATE_APPLYER);

				} else {

					if (authorFlag) {
						// 当前审批人或者管理员查看
						model.addAttribute(WorkflowConstants.PRO_PAGESTATE,
								WorkflowConstants.PRO_PAGESTATE_APPROVE);
					} else {
						// 被授权的第三方查看或者是处理过的人查看
						model.addAttribute(WorkflowConstants.PRO_PAGESTATE,
								WorkflowConstants.PRO_PAGESTATE_SHOW);
					}
				}

			} else {
				// 流程结束查看
				model.addAttribute(WorkflowConstants.PRO_PAGESTATE,
						WorkflowConstants.PRO_PAGESTATE_ENDSHOW);
			}

			tm.commit();

			return model;
		} catch (Exception e) {
			throw new Exception("查看跳转查询数据出错:" + e.getMessage());
		}
	}

	@Override
	public void returnToNode(String nowTaskId, String currentUser,
			Map<String, Object> map, String destinationTaskKey,
			String completeReason) throws Exception {

		// 当前用户转成域账号
		currentUser = this.changeToDomainAccount(currentUser);

		activitiService.completeTaskWithLocalVariablesReason(nowTaskId,
				currentUser, map, destinationTaskKey, completeReason);
	}

}
