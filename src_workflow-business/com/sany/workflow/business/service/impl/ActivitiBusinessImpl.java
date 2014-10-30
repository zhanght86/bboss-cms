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
import org.activiti.engine.impl.bpmn.diagram.ProcessDiagramGenerator;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.frameworkset.soa.ObjectSerializable;
import org.frameworkset.web.servlet.ModelMap;

import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.util.ListInfo;
import com.frameworkset.util.StringUtil;
import com.sany.workflow.business.entity.ActNode;
import com.sany.workflow.business.entity.FormCache;
import com.sany.workflow.business.entity.HisTaskInfo;
import com.sany.workflow.business.entity.ProIns;
import com.sany.workflow.business.entity.TaskInfo;
import com.sany.workflow.business.service.ActivitiBusinessService;
import com.sany.workflow.business.util.WorkflowConstants;
import com.sany.workflow.entity.ActivitiNodeInfo;
import com.sany.workflow.entity.ActivitiVariable;
import com.sany.workflow.entity.NodeControlParam;
import com.sany.workflow.entity.ProcessInst;
import com.sany.workflow.entity.TaskCondition;
import com.sany.workflow.entity.TaskManager;
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
	public boolean judgeAuthorityNoAdmin(String taskId, String processKey,
			String userAccount) {

		if (StringUtil.isEmpty(taskId)) {
			return false;
		}

		TransactionManager tm = new TransactionManager();

		try {
			tm.begin();
			boolean haspermission = false;
			userAccount = this.changeToDomainAccount(userAccount);

			// 首先判断任务是否有没签收，如果签收，以签收人为准，如果没签收，则以该节点配置的人为准
			TaskManager task = executor.queryObject(TaskManager.class,
					"getHiTaskIdByTaskId", taskId);

			if (StringUtil.isNotEmpty(task.getASSIGNEE_())) {

				if (userAccount.equals(task.getASSIGNEE_())) {

					haspermission = true;
				}

			} else {
				// 任务未签收，根据任务id查询任务可处理人
				List<HashMap> candidatorList = executor.queryList(
						HashMap.class, "getNodeCandidates_wf", taskId,
						userAccount);

				if (candidatorList != null && candidatorList.size() > 0) {

					haspermission = true;
				}
			}

			if (!haspermission) {
				// 最后查看当前用户的委托关系
				List<WfEntrust> entrustList = executor.queryList(
						WfEntrust.class, "getEntrustRelation_wf", userAccount,
						processKey, new Timestamp(task.getSTART_TIME_()
								.getTime()), new Timestamp(task
								.getSTART_TIME_().getTime()));

				if (entrustList != null && entrustList.size() > 0) {

					haspermission = true;
				}
			}
			tm.commit();
			return haspermission;

		} catch (Exception e) {
			throw new ProcessException(e);
		} finally {
			tm.release();
		}
	}

	@Override
	public boolean judgeAuthority(String taskId, String processKey,
			String userAccount) {

		if (AccessControl.isAdmin(userAccount)) {
			return true;
		}

		TransactionManager tm = new TransactionManager();

		try {
			tm.begin();

			userAccount = this.changeToDomainAccount(userAccount);

			boolean flag = judgeAuthorityNoAdmin(taskId, processKey,
					userAccount);

			tm.commit();

			return flag;

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
	private void isParrealOrSequence(List<ActNode> actNodeList,
			String processKey) {

		if (actNodeList != null && actNodeList.size() > 0) {
			// 获取流程定义XML中节点定义信息
			List<ActivityImpl> activties = activitiService
					.getActivitImplListByProcessKey(processKey);

			for (int i = 0; i < actNodeList.size(); i++) {

				ActNode actNode = actNodeList.get(i);

				for (ActivityImpl activtie : activties) {

					if (activtie.getId().equals(actNode.getActId())) {

						if (activtie.isMultiTask()) {
							actNode.setIsMultiDefault(1);

							if (actNode.getIsSequential() == 1) {// 多实例串行
								actNode.setApproveType(WorkflowConstants.PRO_ACT_SEQ);
							} else if (actNode.getIsSequential() == 0) {// 多实例并行
								actNode.setApproveType(WorkflowConstants.PRO_ACT_MUL);
							}
						} else {
							actNode.setIsMultiDefault(0);

							if (actNode.getIsMulti() == 0) {
								actNode.setApproveType(WorkflowConstants.PRO_ACT_ONE);// 单实例
							} else if (actNode.getIsMulti() == 1
									&& actNode.getIsSequential() == 1) {// 多实例串行
								actNode.setApproveType(WorkflowConstants.PRO_ACT_SEQ);
							} else if (actNode.getIsMulti() == 1
									&& actNode.getIsSequential() == 0) {// 多实例并行
								actNode.setApproveType(WorkflowConstants.PRO_ACT_MUL);
							}
						}
						break;
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

		// 串并行判断
		isParrealOrSequence(actNodeList, processKey);

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
	private void getVariableMap(ProIns proIns, Map<String, Object> paramMap) {

		List<ActNode> actNodeList = proIns.getActs();

		// 流程参数
		if (actNodeList != null && actNodeList.size() > 0) {

			for (int i = 0; i < actNodeList.size(); i++) {
				ActNode node = actNodeList.get(i);

				// 过滤当前节点处理人(当前节点处理人不能被修改)
				if (node.getActId().equals(proIns.getNowTaskKey())) {
					continue;
				}

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
				} else {
					paramMap.put(node.getActId() + "_users", "");
				}

			}
		}

	}

	public void startProc(ProIns proIns, String businessKey, String processKey,
			Map<String, Object> paramMap) throws Exception {
		startProc(proIns, businessKey, processKey, paramMap, true);
	}

	@Override
	public void startProc(ProIns proIns, String businessKey, String processKey,
			Map<String, Object> paramMap, boolean completeFirstTask)
			throws Exception {

		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();

			// 当前流程发起人，转域账号
			String currentUser = changeToDomainAccount(proIns.getUserAccount());

			List<ActNode> actNodeList = proIns.getActs();

			// 节点控制参数信息
			List<NodeControlParam> controlParamList = new ArrayList<NodeControlParam>();

			// 节点配置参数转换
			getVariableMap(proIns, paramMap);

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
						nodeControl.setIS_DISCARDED(node.getIsDiscarded());
						nodeControl.setIS_EDIT(node.getIsEdit());
						nodeControl.setIS_EDITAFTER(node.getIsEditAfter());
						nodeControl.setIS_RECALL(node.getIsRecall());
						nodeControl.setIS_VALID(node.getIsValid());
						if (WorkflowConstants.PRO_ACT_ONE.equals(node
								.getApproveType())) {// 单实例
							nodeControl.setIS_MULTI(0);
							nodeControl.setIS_SEQUENTIAL(0);
						} else if (WorkflowConstants.PRO_ACT_SEQ.equals(node
								.getApproveType())) {// 多实例串行
							nodeControl.setIS_MULTI(1);
							nodeControl.setIS_SEQUENTIAL(1);
						} else if (WorkflowConstants.PRO_ACT_MUL.equals(node
								.getApproveType())) {// 多实例并行
							nodeControl.setIS_MULTI(1);
							nodeControl.setIS_SEQUENTIAL(0);
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

			// 清除暂存表单数据
			delFormDatasByBusinessKey(businessKey);

			// 记录节点控制变量与流程实例关联
			activitiService.addNodeWorktime(processKey,
					processInstance.getId(), controlParamList);

			if (completeFirstTask) {
				// 获取流程第一个任务节点
				Task task = activitiService.getCurrentTask(processInstance
						.getId());

				if (StringUtil.isEmpty(proIns.getDealOption())) {
					proIns.setDealOption("提交任务");
				}

				if (StringUtil.isEmpty(proIns.getDealRemak())) {
					String remark = "["
							+ activitiService.getUserInfoMap().getUserName(
									currentUser) + "]提交";
					proIns.setDealRemak(remark);
				}

				if (!isSignTask(task.getId(), task.getAssignee())) {
					// 先签收
					activitiService.claim(task.getId(), task.getAssignee());
				}

				// 完成任务
				activitiService.completeTaskWithReason(task.getId(), null,
						proIns.getDealRemak(), proIns.getDealOption(),
						proIns.getDealReason());
			}

			tm.commit();

		} catch (Exception e) {
			throw new Exception("开启流程出错:" + e.getMessage());
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
					if (StringUtil.isNotEmpty(hti.getDEALUSER())) {
						hti.setASSIGNEE_NAME(activitiService.userIdToUserName(
								hti.getDEALUSER(), "2"));
					} else {
						hti.setASSIGNEE_NAME(activitiService.userIdToUserName(
								hti.getASSIGNEE_(), "2"));
					}
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
	public List<ActNode> getWFNodeConfigInfoByCondition(String processKey,
			String processInstId, String taskId) throws Exception {

		TransactionManager tms = new TransactionManager();

		try {
			tms.begin();

			List<ActNode> nodeList = getWFNodeInfoByCondition(processKey,
					processInstId);

			// 串并行判断
			isParrealOrSequence(nodeList, processKey);

			tms.commit();

			return nodeList;

		} catch (Exception e) {
			throw new ProcessException(e);
		} finally {
			tms.release();
		}
	}

	private List<ActNode> getWFNodeInfoByCondition(String processKey,
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
			String userId, String orgId) throws Exception {

		try {

			if (StringUtil.isNotEmpty(userId)) {
				return this.getWFNodeConfigInfoForOrg(processKey, userId);
			}

			if (StringUtil.isNotEmpty(orgId)) {
				// 用户组织节点和层级
				String userLevel = executor.queryObject(String.class,
						"getUserLevelByOrgId", orgId);

				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("userLevel", userLevel.split("\\|"));

				return this.getWFNodeConfigInfoForOrg(processKey, map);
			}

			if (StringUtil.isEmpty(userId) && StringUtil.isEmpty(orgId)) {

				return null;
			}

		} catch (Exception e) {
			throw new Exception("获取组织类型节点配置出错：" + e.getMessage());
		}

		return null;
	}

	private List<ActNode> getWFNodeConfigInfoForOrg(String processKey,
			HashMap<String, Object> map) throws Exception {

		map.put("processKey", processKey);
		map.put("businessType", "1");

		List<ActNode> actNodeList = this.getWFNodeConfigInfo(processKey, map);

		if (actNodeList != null && actNodeList.size() > 0) {

			// 通过组织结构配置的流程，节点人为空采用默认配置的判断
			return setNodeDefaultConfig(actNodeList,
					(String[]) map.get("userLevel"));
		} else {
			return actNodeList;
		}
	}

	@Override
	public List<ActNode> getWFNodeConfigInfoForOrg(String processKey,
			String userId) throws Exception {
		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();

			userId = changeToDomainAccount(userId);

			// 用户组织节点和层级
			String userLevel = executor.queryObject(String.class,
					"getUserLevel", userId);

			tm.commit();

			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("userLevel", userLevel.split("\\|"));

			return getWFNodeConfigInfoForOrg(processKey, map);

		} catch (Exception e) {
			throw new Exception("通过userId获取组织类型节点配置出错：" + e.getMessage());
		} finally {
			tm.release();
		}
	}

	@Override
	public List<ActNode> getWFNodeConfigInfoForOrgAndCommmon(String processKey,
			String userId, String orgId) throws Exception {

		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();

			List<ActNode> orgNodeList = null;

			if (StringUtil.isNotEmpty(userId)) {
				orgNodeList = this
						.getWFNodeConfigInfoForOrg(processKey, userId);
			} else {

				if (StringUtil.isNotEmpty(orgId)) {
					// 用户组织节点和层级
					String userLevel = executor.queryObject(String.class,
							"getUserLevelByOrgId", orgId);

					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("userLevel", userLevel.split("\\|"));

					orgNodeList = this.getWFNodeConfigInfoForOrg(processKey,
							map);
				}
			}

			tm.commit();

			if (null == orgNodeList) {
				return null;
			} else {

				List<ActNode> commonNodeList = this
						.getWFNodeConfigInfoForCommon(processKey);

				return filterNodeConfig(orgNodeList, commonNodeList);
			}

		} catch (Exception e) {
			throw new Exception("获取组织类型节点配置出错1：" + e.getMessage());
		} finally {
			tm.release();
		}

	}

	@Override
	public List<ActNode> getWFNodeConfigInfoForFirstOrgSecondCommmon(
			String processKey, String userId, String orgId) throws Exception {

		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();

			List<ActNode> orgNodeList = null;

			if (StringUtil.isNotEmpty(userId)) {
				orgNodeList = this
						.getWFNodeConfigInfoForOrg(processKey, userId);
			} else {

				if (StringUtil.isNotEmpty(orgId)) {
					// 用户组织节点和层级
					String userLevel = executor.queryObject(String.class,
							"getUserLevelByOrgId", orgId);

					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("userLevel", userLevel.split("\\|"));

					orgNodeList = this.getWFNodeConfigInfoForOrg(processKey,
							map);
				}
			}

			tm.commit();

			if (null == orgNodeList) {
				return this.getWFNodeConfigInfoForCommon(processKey);
			} else {
				return orgNodeList;
			}

		} catch (Exception e) {
			throw new Exception("获取组织类型节点配置出错1：" + e.getMessage());
		} finally {
			tm.release();
		}

	}

	/**
	 * 过滤组织结构维度和通用配置维度查询的节点配置
	 * 
	 * @param nodeList
	 *            组织结构维度查询的节点配置or业务类型维度查询的节点配置
	 * @param commonNodeList
	 *            通用配置维度查询的节点配置
	 * @return 2014年10月16日
	 */
	private List<ActNode> filterNodeConfig(List<ActNode> nodeList,
			List<ActNode> commonNodeList) {

		// 不为空，节点处理人以组织维度为准，控制参数以通用配置为准
		for (int i = 0; i < commonNodeList.size(); i++) {
			ActNode commonActNode = commonNodeList.get(i);
			ActNode actNode = nodeList.get(i);

			commonActNode.setCandidateName(actNode.getCandidateName());
			commonActNode.setRealName(actNode.getRealName());
		}

		return commonNodeList;
	}

	@Override
	public List<ActNode> getWFNodeConfigInfoForbussiness(String processKey,
			String typeId) throws Exception {

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("processKey", processKey);
		map.put("businessType", "2");
		map.put("userLevel", new String[] { typeId });

		return this.getWFNodeConfigInfo(processKey, map);
	}

	@Override
	public List<ActNode> getWFNodeConfigInfoForbussinessAndCommmon(
			String processKey, String typeId) throws Exception {

		List<ActNode> bussinessNodeList = getWFNodeConfigInfoForbussiness(
				processKey, typeId);

		if (null == bussinessNodeList) {
			return null;
		} else {

			List<ActNode> commonNodeList = this
					.getWFNodeConfigInfoForCommon(processKey);

			return filterNodeConfig(bussinessNodeList, commonNodeList);
		}
	}

	@Override
	public List<ActNode> getWFNodeConfigInfoForFirstbussinessSecondCommmon(
			String processKey, String typeId) throws Exception {

		try {
			List<ActNode> bussinessNodeList = getWFNodeConfigInfoForbussiness(
					processKey, typeId);

			if (null == bussinessNodeList) {
				return this.getWFNodeConfigInfoForCommon(processKey);
			} else {
				return bussinessNodeList;
			}

		} catch (Exception e) {
			throw new Exception("获取组织类型节点配置出错1：" + e.getMessage());
		}
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

		TransactionManager tm = new TransactionManager();
		try {

			tm.begin();

			// 当前用户转成域账号
			String userAccount = this.changeToDomainAccount(proIns
					.getUserAccount());
			proIns.setUserAccount(userAccount);

			// 委托用户转成域账号
			String fromUser = this.changeToDomainAccount(proIns
					.getNowTaskFromUser());
			// 被委托用户转成域账号
			String toUser = this.changeToDomainAccount(proIns
					.getNowTaskToUser());

			// 权限判断
			// if (!judgeAuthority(proIns.getNowtaskId(), processKey,
			// userAccount)) {
			// throw new ProcessException("您没有权限通过任务！");
			// }

			// 记录委托关系(有就处理，没有就不处理)
			String dealRemak = "";
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
					dealRemak = "["
							+ activitiService.getUserInfoMap().getUserName(
									userAccount)
							+ "]的任务委托给["
							+ activitiService.getUserInfoMap().getUserName(
									toUser) + "]通过";
				}

			} else {
				dealRemak = "["
						+ activitiService.getUserInfoMap().getUserName(
								userAccount) + "]通过";
			}

			if (StringUtil.isEmpty(proIns.getDealRemak())) {

				proIns.setDealRemak(dealRemak);

			}

			// 节点配置参数转换
			getVariableMap(proIns, paramMap);

			completeTask(proIns, paramMap);

			tm.commit();

		} catch (ProcessException e) {
			throw e;
		} catch (Exception e) {
			throw new Exception("处理任务出错：" + e.getMessage());
		} finally {
			tm.release();
		}
	}

	private void completeTask(ProIns proIns, Map<String, Object> paramMap)
			throws Exception {
		// 未签收
		if (!isSignTask(proIns.getNowtaskId(), proIns.getUserAccount())) {

			if (StringUtil.isEmpty(proIns.getToTaskKey())) {

				activitiService.completeTaskWithReason(proIns.getNowtaskId(),
						proIns.getUserAccount(), paramMap,
						proIns.getDealRemak(), proIns.getDealOption(),
						proIns.getDealReason());

			} else {

				activitiService.completeTaskWithLocalVariablesReason(
						proIns.getNowtaskId(), proIns.getUserAccount(),
						paramMap, proIns.getToTaskKey(), proIns.getDealRemak(),
						proIns.getDealOption(), proIns.getDealReason());
			}

		} else {// 已签收

			if (StringUtil.isEmpty(proIns.getToTaskKey())) {

				activitiService.completeTaskWithReason(proIns.getNowtaskId(),
						paramMap, proIns.getDealRemak(),
						proIns.getDealOption(), proIns.getDealReason());

			} else {
				activitiService.completeTaskLoadCommonParamsReason(
						proIns.getNowtaskId(), paramMap, proIns.getToTaskKey(),
						proIns.getDealRemak(), proIns.getDealOption(),
						proIns.getDealReason());
			}

		}
	}

	@Override
	public void discardTask(ProIns proIns, String processKey) throws Exception {
		TransactionManager tm = new TransactionManager();
		try {

			tm.begin();

			// 当前用户转成域账号
			String userAccount = this.changeToDomainAccount(proIns
					.getUserAccount());

			// 获取流程实例
			// ProcessInst inst = executor.queryObject(ProcessInst.class,
			// "getProcessByProcessId_wf", proIns.getProInsId());

			// 权限判断(当前用户id==发起人)
			// if (inst == null) {
			// throw new ProcessException("流程已结束或不存在！");
			// }
			//
			// if (!inst.getSTART_USER_ID_().equals(userAccount)) {
			// // 权限判断
			// if (!judgeAuthority(proIns.getNowtaskId(), processKey,
			// userAccount)) {
			// throw new ProcessException("您没有权限废弃任务！");
			// }
			// }

			if (!isSignTask(proIns.getNowtaskId(), userAccount)) {
				// 先签收
				activitiService.claim(proIns.getNowtaskId(), userAccount);
			}

			String dealRemak = "["
					+ activitiService.getUserInfoMap().getUserName(userAccount)
					+ "]将任务废弃";

			activitiService.cancleProcessInstances(proIns.getProInsId(),
					dealRemak, proIns.getNowtaskId(), processKey, userAccount,
					proIns.getDealOption(), proIns.getDealReason());

			tm.commit();

		} catch (ProcessException e) {
			throw e;
		} catch (Exception e) {
			throw new Exception("废弃任务出错：" + e.getMessage());
		} finally {
			tm.release();
		}
	}

	/**
	 * 日志处理
	 * 
	 * @param proIns
	 *            2014年9月24日
	 */
	private void dealLogInfo(ProIns proIns) {
		String operType = proIns.getOperateType().toLowerCase();

		if (WorkflowConstants.PRO_OPE_TYPE_PASS.equals(operType)) {// 通过任务
			if (StringUtil.isEmpty(proIns.getDealOption())) {
				proIns.setDealOption("通过任务");
			}
		} else if (WorkflowConstants.PRO_OPE_TYPE_REJECT.equals(operType)) {// 驳回任务
			if (StringUtil.isEmpty(proIns.getDealOption())) {
				proIns.setDealOption("驳回任务");
			}
		} else if (WorkflowConstants.PRO_OPE_TYPE_RECALL.equals(operType)) {// 撤回任务
			if (StringUtil.isEmpty(proIns.getDealOption())) {
				proIns.setDealOption("撤回任务");
			}
		} else if (WorkflowConstants.PRO_OPE_TYPE_TOEND.equals(operType)) {// 废弃任务
			if (StringUtil.isEmpty(proIns.getDealOption())) {
				proIns.setDealOption("废弃任务");
			}
		} else if (WorkflowConstants.PRO_OPE_TYPE_TURNTO.equals(operType)) {// 转办任务
			if (StringUtil.isEmpty(proIns.getDealOption())) {
				proIns.setDealOption("转办任务");
			}
		}
	}

	@Override
	public boolean isSignTask(String taskId, String userId) throws Exception {
		return activitiService.isSignTask(taskId, userId);
	}

	@Override
	public void delegateTask(ProIns proIns, String processKey) throws Exception {
		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();
			// 当前用户转成域账号
			String userAccount = this.changeToDomainAccount(proIns
					.getUserAccount());

			// 委托用户转成域账号
			String fromUser = this.changeToDomainAccount(proIns
					.getNowTaskFromUser());

			// 被委托用户转成域账号
			String toUser = this.changeToDomainAccount(proIns
					.getNowTaskToUser());

			// 转办用户转成域账号
			String delegateUser = this.changeToDomainAccount(proIns
					.getDelegateUser());

			// // 权限判断
			// if (!judgeAuthority(proIns.getNowtaskId(), processKey,
			// userAccount)) {
			// throw new ProcessException("您没有权限转办任务！");
			// }

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

			if (!isSignTask(proIns.getNowtaskId(), userAccount)) {
				// 先签收
				activitiService.claim(proIns.getNowtaskId(), userAccount);
			}

			// 再转办
			activitiService.delegateTask(proIns.getNowtaskId(), delegateUser);

			// 备注
			if (StringUtil.isEmpty(proIns.getDealRemak())) {
				String dealRemak = "["
						+ activitiService.getUserInfoMap().getUserName(
								userAccount)
						+ "]将任务转办给["
						+ activitiService.getUserInfoMap().getUserName(
								delegateUser) + "]";
				proIns.setDealRemak(dealRemak);

			}

			// 在扩展表中添加转办记录
			activitiTaskService.updateNodeChangeInfo(proIns.getNowtaskId(),
					proIns.getProInsId(), processKey, userAccount,
					delegateUser, proIns.getDealRemak(),
					proIns.getDealReason(), 0);

			tm.commit();
		} catch (ProcessException e) {
			throw e;
		} catch (Exception e) {
			throw new Exception("转办任务出错:" + e.getMessage());
		} finally {
			tm.release();
		}
	}

	@Override
	public void cancelTask(ProIns proIns, String processKey) throws Exception {
		TransactionManager tm = new TransactionManager();

		try {
			tm.begin();

			// 当前用户转成域账号
			String userAccount = this.changeToDomainAccount(proIns
					.getUserAccount());

			// 获取流程实例
			// ProcessInst inst = executor.queryObject(ProcessInst.class,
			// "getProcessByProcessId_wf", proIns.getProInsId());

			// 权限判断(当前用户id==发起人)
			// if (inst == null ||
			// !inst.getSTART_USER_ID_().equals(userAccount)) {
			// throw new ProcessException("您没有权限撤销任务！");
			// }

			// TaskManager hiTask = activitiService.getFirstTask(proIns
			// .getProInsId());

			// 获取第一个人工节点
			ActivitiNodeInfo nodeInfo = activitiTaskService
					.getFirstUserNode(processKey);

			String currentUser = activitiService.getUserInfoMap().getUserName(
					userAccount);

			if (StringUtil.isEmpty(proIns.getDealRemak())) {
				String remark = "[" + currentUser + "]将任务撤回至["
						+ nodeInfo.getNode_name() + "]";
				proIns.setDealRemak(remark);
			}

			// 撤销任务
			activitiService.cancelTask(proIns.getNowtaskId(),
					nodeInfo.getNode_key(), proIns.getDealRemak(),
					proIns.getDealOption(), proIns.getDealReason());

			// 日志记录撤销操作
			activitiService.addDealTask(proIns.getNowtaskId(), userAccount,
					currentUser, "2", proIns.getProInsId(), processKey,
					proIns.getDealReason(), proIns.getDealOption(),
					proIns.getDealRemak());

			tm.commit();

		} catch (ProcessException e) {
			throw e;
		} catch (Exception e) {
			throw new Exception("撤销任务出错:" + e.getMessage());
		} finally {
			tm.release();
		}
	}

	// public void cancelTask(ProIns proIns, String processKey, String
	// initorUser,
	// String currentUser) throws Exception {
	// TransactionManager tm = new TransactionManager();
	//
	// try {
	// tm.begin();
	//
	// // 当前用户转成域账号
	// String userAccount = this.changeToDomainAccount(initorUser);
	// String currentUserAccount = null;
	// if (initorUser.equals(currentUser)) {
	// currentUserAccount = userAccount;
	//
	// } else {
	// currentUserAccount = this.changeToDomainAccount(currentUser);
	// }
	//
	// // 获取流程实例
	// ProcessInst inst = executor.queryObject(ProcessInst.class,
	// "getProcessByProcessId_wf", proIns.getProInsId());
	//
	// // 权限判断(当前用户id==发起人)
	// if (inst == null || !inst.getSTART_USER_ID_().equals(userAccount)) {
	// throw new ProcessException("您没有权限撤销任务！");
	// }
	//
	// TaskManager hiTask = activitiService.getFirstTask(proIns
	// .getProInsId());
	//
	// String currentUserName = activitiService.getUserInfoMap()
	// .getUserName(currentUserAccount);
	//
	// if (StringUtil.isEmpty(proIns.getDealRemak())) {
	// String remark = "[" + currentUserName + "]将任务撤回至["
	// + hiTask.getNAME_() + "]";
	// proIns.setDealRemak(remark);
	// }
	//
	// // 撤销任务
	// activitiService.completeTaskLoadCommonParamsWithDest(
	// proIns.getNowtaskId(), hiTask.getTASK_DEF_KEY_(),
	// proIns.getDealRemak(), proIns.getDealOption(),
	// proIns.getDealReason());
	//
	// // 日志记录撤销操作
	// activitiService.addDealTask(proIns.getNowtaskId(), userAccount,
	// currentUser, "2", proIns.getProInsId(), processKey,
	// proIns.getDealReason(), proIns.getDealOption(),
	// proIns.getDealRemak());
	//
	// tm.commit();
	//
	// } catch (ProcessException e) {
	// throw e;
	// } catch (Exception e) {
	// throw new Exception("撤销任务出错:" + e.getMessage());
	// } finally {
	// tm.release();
	// }
	// }

	@Override
	public void rejectToPreTask(ProIns proIns, String processKey,
			Map<String, Object> paramMap) throws Exception {

		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();

			// 当前用户转成域账号
			String userAccount = this.changeToDomainAccount(proIns
					.getUserAccount());

			// 权限判断
			// if (!judgeAuthority(proIns.getNowtaskId(), processKey,
			// userAccount)) {
			// throw new ProcessException("您没有权限驳回任务！");
			// }

			// 获取参数配置信息
			getVariableMap(proIns, paramMap);

			if (!isSignTask(proIns.getNowtaskId(), userAccount)) {
				// 先签收
				activitiService.claim(proIns.getNowtaskId(), userAccount);
			}

			if (StringUtil.isEmpty(proIns.getDealRemak())) {
				String dealRemak = "["
						+ activitiService.getUserInfoMap().getUserName(
								userAccount) + "]将任务驳回至["
						+ proIns.getToActName() + "]";
				proIns.setDealRemak(dealRemak);
			}

			activitiService.getTaskService().rejecttoTask(
					proIns.getNowtaskId(), paramMap, proIns.getDealRemak(),
					proIns.getRejectToActId(),
					proIns.getIsReturn() == 1 ? true : false,
					proIns.getDealOption(), proIns.getDealReason());

			// 日志记录驳回操作
			activitiService.addDealTask(proIns.getNowtaskId(), userAccount,
					activitiService.getUserInfoMap().getUserName(userAccount),
					"1", proIns.getProInsId(), processKey,
					proIns.getDealReason(), proIns.getDealOption(),
					proIns.getDealRemak());

			tm.commit();

		} catch (ProcessException e) {
			throw e;
		} catch (Exception e) {
			throw new ProcessException(e);
		} finally {
			tm.release();
		}
	}

	@Override
	public int getMyselfTaskNum(String currentUser, String processKey)
			throws Exception {

		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();

			// 当前用户转成域账号
			currentUser = this.changeToDomainAccount(currentUser);

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("assignee", currentUser);
			params.put("processKey", processKey);

			// 当前用户的任务数
			int taskNum = executor.queryObjectBean(int.class,
					"countTaskNum_wf", params);

			tm.commit();
			return taskNum;

		} catch (Exception e) {
			throw new Exception("获取用户待办任务总数出错：" + e.getMessage());
		} finally {
			tm.release();
		}
	}

	@Override
	public int getEntrustTaskNum(String currentUser, String processKey)
			throws Exception {

		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();

			// 当前用户转成域账号
			currentUser = this.changeToDomainAccount(currentUser);

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

			tm.commit();
			return entrustTaskNum;
		} catch (Exception e) {
			throw new Exception("获取用户委托任务总数出错：" + e.getMessage());
		} finally {
			tm.release();
		}
	}

	@Override
	public ListInfo getMyselfTaskList(String currentUser, String processKey,
			long offset, int pagesize) throws Exception {

		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();

			// 当前用户转成域账号
			currentUser = this.changeToDomainAccount(currentUser);

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("assignee", currentUser);
			params.put("processKey", processKey);

			ListInfo listInfo = executor.queryListInfoBean(TaskInfo.class,
					"getMyselfTask_wf", offset, pagesize, "countTaskNum_wf",
					params);

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

		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();

			// 当前用户转成域账号
			currentUser = this.changeToDomainAccount(currentUser);

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

	public List<ActNode> getBackActNodeContainNoAssigner(String processId,
			String currentTaskKey) throws Exception {
		return _getBackActNode(processId, currentTaskKey, true);
	}

	public List<ActNode> getBackActNode(String processId, String currentTaskKey)
			throws Exception {
		return _getBackActNode(processId, currentTaskKey, false);
	}

	private List<ActNode> _getBackActNode(String processId,
			String currentTaskKey, boolean shownoassignnodes) throws Exception {

		// 获取当前流程实例下处理过的节点集合
		List<HashMap> taskKeyList = null;

		if (shownoassignnodes) {
			// 包含自动通过节点
			taskKeyList = executor.queryList(HashMap.class,
					"getTaskKeyList_wf", processId);
		} else {
			// 不包含自动通过节点
			taskKeyList = executor.queryList(HashMap.class,
					"getTaskKeyListNoAssignerNodes_wf", processId);
		}

		List<ActNode> backActNodeList = new ArrayList<ActNode>();

		// 去重复key
		if (taskKeyList != null && taskKeyList.size() > 0) {

			for (int i = 0; i < taskKeyList.size(); i++) {
				HashMap map = taskKeyList.get(i);
				String taskKey = map.get("ACT_ID_") + "";
				String taskName = map.get("ACT_NAME_") + "";

				if (taskKey.equals(currentTaskKey)) {
					break;
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
			dealLogInfo(proIns);
			this.completeTask(proIns, processKey, paramMap);
		} else if (WorkflowConstants.PRO_OPE_TYPE_REJECT.equals(operType)) {// 驳回任务
			dealLogInfo(proIns);
			this.rejectToPreTask(proIns, processKey, paramMap);
		} else if (WorkflowConstants.PRO_OPE_TYPE_RECALL.equals(operType)) {// 撤回任务
			dealLogInfo(proIns);
			this.cancelTask(proIns, processKey);
		} else if (WorkflowConstants.PRO_OPE_TYPE_TOEND.equals(operType)) {// 废弃任务
			dealLogInfo(proIns);
			this.discardTask(proIns, processKey);
		} else if (WorkflowConstants.PRO_OPE_TYPE_TURNTO.equals(operType)) {// 转办任务
			dealLogInfo(proIns);
			this.delegateTask(proIns, processKey);
		}
	}

	@Override
	public void approveWorkFlowByBussinesskey(ProIns proIns, String processKey,
			Map<String, Object> paramMap) throws Exception {

		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();

			TaskInfo nowTask = getCurrentNodeInfoByBussinessKey(
					proIns.getBusinessKey(), proIns.getUserAccount());

			if (nowTask == null) {
				throw new ProcessException("任务不存在或您没有权限处理当前任务");
			} else {
				proIns.setNowtaskId(nowTask.getTaskId());
			}

			approveWorkFlow(proIns, processKey, paramMap);

			tm.commit();

		} catch (ProcessException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		} finally {
			tm.release();
		}
	}

	/**
	 * 处理抄送任务
	 * 
	 * @param task
	 *            2014年10月8日
	 */
	private void dealCopyTask(TaskInfo task, ModelMap model) {
		if (null != task && task.getIsCopy() == 1) {
			// 完成任务
			activitiService.completeTaskWithReason(task.getTaskId(), null, "",
					"抄送任务", "");
			// 抄送节点，页面状态为第三方查看
			model.addAttribute(WorkflowConstants.PRO_PAGESTATE,
					WorkflowConstants.PRO_PAGESTATE_SHOW);
		}
	}

	public ModelMap toDealTaskContainNoAssignerNodes(String processKey,
			String processId, String taskId, ModelMap model) throws Exception {

		return _toDealTask(processKey, processId, taskId, model, true);
	}

	public ModelMap toDealTask(String processKey, String processId,
			String taskId, ModelMap model) throws Exception {

		return _toDealTask(processKey, processId, taskId, model, false);
	}

	private ModelMap _toDealTask(String processKey, String processId,
			String taskId, ModelMap model, boolean shownoassignnodes)
			throws Exception {

		// 当前任务节点信息
		TaskInfo task = getCurrentNodeInfo(taskId);
		model.addAttribute("task", task);

		// 判断当前任务是否存在
		if (null == task) {
			throw new ProcessException("任务不存在");
		}

		model.addAttribute("processKey", processKey);

		// 获取流程实例的处理记录
		List<HisTaskInfo> taskHistorList = getProcHisInfo(task.getInstanceId());
		model.addAttribute("taskHistorList", taskHistorList);

		// 获取流程节点配置信息
		List<ActNode> actList = getWFNodeConfigInfoByCondition(processKey,
				task.getInstanceId(), taskId);
		model.addAttribute("actList", actList);

		// 可驳回的节点信息
		List<ActNode> backActNodeList = _getBackActNode(task.getInstanceId(),
				task.getTaskDefKey(), shownoassignnodes);
		model.addAttribute("backActNodeList", backActNodeList);

		// 页面状态
		model.addAttribute(WorkflowConstants.PRO_PAGESTATE,
				WorkflowConstants.PRO_PAGESTATE_APPROVE);

		dealCopyTask(task, model);

		return model;
	}

	private ModelMap _toViewTask(String taskId, String bussinessKey,
			String userId, ModelMap model, String processKey,
			boolean shownoassignnodes) throws Exception {
		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();

			// 当前用户转成域账号
			String userAccount = "";
			if (StringUtil.isNotEmpty(userId)) {
				userAccount = this.changeToDomainAccount(userId);
			} else {
				userAccount = AccessControl.getAccessControl().getUserAccount();
			}

			// 获取流程实例
			ProcessInst inst = null;
			if (StringUtil.isNotEmpty(processKey)) {
				inst = executor.queryObject(ProcessInst.class,
						"getProcessByKey_wf", bussinessKey, processKey);
				model.addAttribute("processKey", processKey);
			} else {
				inst = executor.queryObject(ProcessInst.class,
						"getProcessByBusinesskey_wf", bussinessKey);
				model.addAttribute("processKey", inst.getKEY_());
			}

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

				// 可驳回的节点信息
				List<ActNode> backActNodeList = _getBackActNode(
						inst.getPROC_INST_ID_(), task.getTaskDefKey(),
						shownoassignnodes);
				model.addAttribute("backActNodeList", backActNodeList);

				// 当前用户是流程发起人
				if (inst.getSTART_USER_ID_().equals(userAccount)) {

					if (authorFlag) {
						// 当前审批人或者管理员查看
						model.addAttribute(
								WorkflowConstants.PRO_PAGESTATE,
								WorkflowConstants.PRO_PAGESTATE_APPLYER_AND_APPROVE);
					} else {
						// 当前用户
						model.addAttribute(WorkflowConstants.PRO_PAGESTATE,
								WorkflowConstants.PRO_PAGESTATE_APPLYER);
					}

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

				// 只有是当前审批人或者管理员才进行抄送任务判断
				if (authorFlag) {
					dealCopyTask(task, model);
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
		} finally {
			tm.release();
		}
	}

	@Override
	public ModelMap toViewTaskContainNoAssignerNodes(String taskId,
			String bussinessKey, String userId, ModelMap model,
			String processKey) throws Exception {

		return _toViewTask(taskId, bussinessKey, userId, model, processKey,
				true);
	}

	@Override
	public ModelMap toViewTask(String taskId, String bussinessKey,
			String userId, ModelMap model, String processKey) throws Exception {

		return _toViewTask(taskId, bussinessKey, userId, model, processKey,
				false);
	}

	@Override
	public ModelMap toViewTask(String taskId, String bussinessKey,
			String userId, ModelMap model) throws Exception {
		return _toViewTask(taskId, bussinessKey, userId, model, null, false);
	}

	@Override
	public ModelMap toViewTaskContainNoAssignerNodes(String taskId,
			String bussinessKey, String userId, ModelMap model)
			throws Exception {
		return _toViewTask(taskId, bussinessKey, userId, model, null, false);
	}

	@Override
	public ModelMap toViewTaskContainNoAssignerNodes(String taskId,
			String userId, ModelMap model) throws Exception {
		// 当前任务节点信息
		TaskInfo task = getCurrentNodeInfo(taskId);

		// 判断当前任务是否存在
		if (null == task) {
			throw new ProcessException("任务不存在");
		}

		return _toViewTask(taskId, task.getBusinessKey(), userId, model,
				task.getProcessKey(), true);
	}

	@Override
	public ModelMap toViewTask(String taskId, String userId, ModelMap model)
			throws Exception {
		// 当前任务节点信息
		TaskInfo task = getCurrentNodeInfo(taskId);

		// 判断当前任务是否存在
		if (null == task) {
			throw new ProcessException("任务不存在");
		}

		return _toViewTask(taskId, task.getBusinessKey(), userId, model,
				task.getProcessKey(), false);
	}

	@Override
	public void returnToNode(String nowTaskId, String currentUser,
			Map<String, Object> map, String destinationTaskKey,
			String completeReason, String bussinessop, String bussinessRemark)
			throws Exception {

		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();

			// 当前用户转成域账号
			currentUser = this.changeToDomainAccount(currentUser);

			activitiService.completeTaskWithLocalVariablesReason(nowTaskId,
					currentUser, map, destinationTaskKey, completeReason,
					bussinessop, bussinessRemark);

			tm.commit();
		} catch (Exception e) {
			throw new Exception("跳转到任意节点出错:" + e.getMessage());
		} finally {
			tm.release();
		}
	}

	@Override
	public TaskInfo getCurrentNodeInfo(String taskId) throws Exception {

		TaskInfo taskInfo = executor.queryObject(TaskInfo.class,
				"getTaskInfoByTaskId_wf", taskId);

		// 处理人行转列
		if (null != taskInfo) {
			if (StringUtil.isNotEmpty(taskInfo.getAssignee())) {
				taskInfo.setAssigneeName(activitiService.getUserInfoMap()
						.getUserName(taskInfo.getAssignee()));
			} else {
				// 任务未签收，根据任务id查询任务可处理人
				List<HashMap> candidatorList = executor.queryList(
						HashMap.class, "getCandidatorOftask_wf",
						taskInfo.getTaskId());

				StringBuffer users = new StringBuffer();

				if (candidatorList != null && candidatorList.size() != 0) {

					for (int k = 0; k < candidatorList.size(); k++) {
						HashMap candidatorMap = candidatorList.get(k);

						String userId = (String) candidatorMap.get("USER_ID_");
						if (StringUtil.isNotEmpty(userId)) {

							if (k == 0) {
								users.append(userId);
							} else {
								users.append(",").append(userId);
							}
						}
					}
					taskInfo.setAssigneeName(activitiService.getUserInfoMap()
							.getUserName(users.toString()));
					taskInfo.setAssignee(users.toString());
				}
			}
		}

		return taskInfo;
	}

	/**
	 * 根据流程实例id获取当前任务
	 * 
	 * @param processID
	 * @return 2014年9月25日
	 */
	private List<TaskInfo> getCurrentNodeInfoByProcessID(String processID)
			throws Exception {
		List<TaskInfo> list = new ArrayList<TaskInfo>();
		// 根据流程实例ID获取运行任务
		List<Task> taskList = activitiService
				.listTaskByProcessInstanceId(processID);

		for (int i = 0; i < taskList.size(); i++) {
			Task task = taskList.get(i);
			TaskInfo taskInfo = getCurrentNodeInfo(task.getId());
			if (null != taskInfo) {
				list.add(taskInfo);
			}
		}
		return list;
	}

	@Override
	public List<TaskInfo> getCurrentNodeInfoByBussinessKey(String bussinesskey)
			throws Exception {
		// 根据bussinessKey获取流程实例
		ProcessInst inst = executor.queryObject(ProcessInst.class,
				"getProcessByBusinesskey_wf", bussinesskey);

		if (inst != null) {
			return getCurrentNodeInfoByProcessID(inst.getPROC_INST_ID_());
		} else {
			return null;
		}
	}

	@Override
	public List<TaskInfo> getCurrentNodeInfoByKey(String bussinesskey,
			String processKey) throws Exception {
		// 根据bussinessKey获取流程实例
		ProcessInst inst = executor.queryObject(ProcessInst.class,
				"getProcessByKey_wf", bussinesskey, processKey);

		if (inst != null) {
			return getCurrentNodeInfoByProcessID(inst.getPROC_INST_ID_());
		} else {
			return null;
		}
	}

	@Override
	public TaskInfo getCurrentNodeInfoByBussinessKey(String bussinesskey,
			String userId) throws Exception {

		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();

			// 当前用户转成域账号
			userId = this.changeToDomainAccount(userId);

			// 根据bussinessKey获取流程实例
			ProcessInst inst = executor.queryObject(ProcessInst.class,
					"getProcessByBusinesskey_wf", bussinesskey);

			if (inst != null) {

				// 根据流程实例ID获取运行任务
				List<Task> taskList = activitiService
						.listTaskByProcessInstanceId(inst.getPROC_INST_ID_());

				String nowTaskId = "";

				for (int i = 0; i < taskList.size(); i++) {
					Task task = taskList.get(i);

					// 判断用户是不是当前审批人
					if (judgeAuthorityNoAdmin(task.getId(), inst.getKEY_(),
							userId)) {

						nowTaskId = task.getId();
						break;
					}
				}

				// 当前任务节点信息
				TaskInfo taskInfo = getCurrentNodeInfo(nowTaskId);

				tm.commit();

				return taskInfo;
			} else {

				tm.commit();
				return null;
			}

		} catch (Exception e) {
			throw new Exception("根据业务key获取当前任务节点信息出错:" + e.getMessage());
		} finally {
			tm.release();
		}
	}

	@Override
	public TaskInfo getCurrentNodeInfoByKey(String bussinesskey,
			String processKey, String userId) throws Exception {

		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();

			// 当前用户转成域账号
			userId = this.changeToDomainAccount(userId);

			// 根据Key获取流程实例
			ProcessInst inst = executor.queryObject(ProcessInst.class,
					"getProcessByKey_wf", bussinesskey, processKey);

			if (inst != null) {

				// 根据流程实例ID获取运行任务
				List<Task> taskList = activitiService
						.listTaskByProcessInstanceId(inst.getPROC_INST_ID_());

				String nowTaskId = "";

				for (int i = 0; i < taskList.size(); i++) {
					Task task = taskList.get(i);

					// 判断用户是不是当前审批人
					if (judgeAuthorityNoAdmin(task.getId(), inst.getKEY_(),
							userId)) {

						nowTaskId = task.getId();
						break;
					}
				}

				// 当前任务节点信息
				TaskInfo taskInfo = getCurrentNodeInfo(nowTaskId);

				tm.commit();

				return taskInfo;
			} else {

				tm.commit();
				return null;
			}

		} catch (Exception e) {
			throw new Exception("根据key获取当前任务节点信息出错:" + e.getMessage());
		} finally {
			tm.release();
		}
	}

	@Override
	public boolean isStartProcByBussinesskey(String bussinesskey)
			throws Exception {

		ProcessInst inst = executor.queryObject(ProcessInst.class,
				"getProcessByBusinesskey_wf", bussinesskey);

		if (inst != null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean isStartProcByKey(String bussinesskey, String processKey)
			throws Exception {

		ProcessInst inst = executor.queryObject(ProcessInst.class,
				"getProcessByKey_wf", bussinesskey, processKey);

		if (inst != null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean isStartProcByProcessId(String processId) throws Exception {
		ProcessInst inst = executor.queryObject(ProcessInst.class,
				"getProcessByProcessId_wf", processId);

		if (inst != null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void tempSaveFormDatas(ProIns proIns, String businessKey,
			String processKey) throws Exception {

		delFormDatasByBusinessKey(businessKey);

		// 对象序列化
		String xml = ObjectSerializable.toXML(proIns);

		executor.insert("saveFormData_wf", businessKey, processKey, xml);

	}

	@Override
	public ProIns getFormDatasByBusinessKey(String businessKey)
			throws Exception {
		FormCache fc = executor.queryObject(FormCache.class,
				"getFormDatasByBusinessKey_wf", businessKey);

		if (fc != null) {
			// 对象反序列化
			ProIns newBean = ObjectSerializable.toBean(fc.getFormdata(),
					ProIns.class);
			return newBean;
		} else {
			return null;
		}
	}

	@Override
	public void delFormDatasByBusinessKey(String businessKey) throws Exception {
		executor.delete("delFormDatasByBusinessKey_wf", businessKey);
	}

}
