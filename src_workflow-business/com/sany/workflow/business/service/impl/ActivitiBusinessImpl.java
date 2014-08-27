package com.sany.workflow.business.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.bpmn.behavior.MultiInstanceActivityBehavior;
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
import com.frameworkset.util.StringUtil;
import com.sany.workflow.business.entity.ActNode;
import com.sany.workflow.business.entity.HisTaskInfo;
import com.sany.workflow.business.entity.ProIns;
import com.sany.workflow.business.service.ActivitiBusinessService;
import com.sany.workflow.business.util.WorkflowConstants;
import com.sany.workflow.entity.ActivitiVariable;
import com.sany.workflow.entrust.entity.WfEntrust;
import com.sany.workflow.service.ActivitiService;
import com.sany.workflow.service.ProcessException;
import com.sany.workflow.service.impl.PlatformKPIServiceImpl;

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
	public boolean judgeAuthority(String taskId, String processKey,
			String businessKey, String userAccount) {
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
					if (activtie.isParreal()) {
						actNode.setApproveType(WorkflowConstants.PRO_ACT_MUL);// 2并行
					} else if (activtie.isSequence()) {
						actNode.setApproveType(WorkflowConstants.PRO_ACT_SEQ);// 串行
					}
				} else {
					actNode.setApproveType(WorkflowConstants.PRO_ACT_SEQ);// 串行
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
			String businessType, String userId) throws Exception {

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("processKey", processKey);
		map.put("businessType", businessType);

		// 通过组织结构配置的流程
		if (businessType.equals("1")) {

			if (StringUtil.isNotEmpty(userId)) {
				userId = changeToDomainAccount(userId);
			}
			// 用户组织节点和层级
			String userLevel = executor.queryObject(String.class,
					"getUserLevel", userId);

			map.put("userLevel", userLevel.split("\\|"));
		}

		List<ActNode> actNodeList = executor.queryListBean(ActNode.class,
				"getWFNodeInfoList", map);

		if (actNodeList != null && actNodeList.size() > 0) {

			for (int i = 0; i < actNodeList.size(); i++) {

				ActNode actNode = actNodeList.get(i);
				// 串并行判断
				isParrealOrSequence(actNode, processKey);

			}

			// 通过组织结构配置的流程，节点人为空采用默认配置的判断
			return setNodeDefaultConfig(actNodeList,
					(String[]) map.get("userLevel"));
		} else {
			return actNodeList;
		}

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

				// 最后一条数据
				if (actNodeList.size() - 1 == i) {
					filteNodeList(orgArray, oldActNodeList, newActNodeList);
				}
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

		for (int j = orgArray.length - 1; j > 0; j--) {

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
			if (orgArray.length - 1 == 0) {
				newActNodeList.add(oldActNodeList.get(0));
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

			// 节点工时提醒次数集合
			List<Map<String, Object>> worktimeList = new ArrayList<Map<String, Object>>();

			List<ActNode> actNodeList = proIns.getActs();

			if (actNodeList != null && actNodeList.size() > 0) {

				for (int i = 0; i < actNodeList.size(); i++) {
					ActNode node = actNodeList.get(i);

					// 节点配置
					if (StringUtil.isNotEmpty(node.getCandidateName())) {
						paramMap.put(node.getActId() + "_users",
								node.getCandidateName());
					}

					// 串/并行
					if (node.getApproveType().equals(
							WorkflowConstants.PRO_ACT_SEQ)) {// 多实例串行
						paramMap.put(
								node.getActId()
										+ MultiInstanceActivityBehavior.multiInstanceMode_variable_const,
								MultiInstanceActivityBehavior.multiInstanceMode_sequential);
					} else if (node.getApproveType().equals(
							WorkflowConstants.PRO_ACT_MUL)) {// 多实例并行
						paramMap.put(
								node.getActId()
										+ MultiInstanceActivityBehavior.multiInstanceMode_variable_const,
								MultiInstanceActivityBehavior.multiInstanceMode_parallel);
					}

					// 工时
					if (StringUtil.isNotEmpty(node.getActId())) {

						Map<String, Object> worktimeMap = new HashMap<String, Object>();
						worktimeMap.put("PROCESS_KEY", processKey);
						worktimeMap.put("NODE_KEY", node.getActId());
						worktimeMap
								.put("DURATION_NODE", node.getNodeWorkTime());

						worktimeMap.put("IS_EDIT_CANDIDATE", node.getCanEdit());

						worktimeMap.put("IS_VALID", node.getIsValid());

						worktimeMap.put("IS_AUTO_CANDIDATE",
								node.getAutoApprove());

						worktimeMap.put("IS_RECALL_CANDIDATE",
								node.getCanRecall());

						worktimeMap.put("IS_EDITAFTER_CANDIDATE",
								node.getEditAfter());
						worktimeList.add(worktimeMap);
					}
				}
			}

			// kpi设值
			PlatformKPIServiceImpl.setWorktimelist(worktimeList);

			// 开启流程实例
			ProcessInstance processInstance = activitiService.startProcDef(
					businessKey, processKey, paramMap, currentUser);

			// 记录节点工时与流程实例关联
			activitiService.addNodeWorktime(processKey,
					processInstance.getId(), worktimeList);

			// 获取流程第一个任务节点
			Task task = activitiService.getCurrentTask(processInstance.getId());

			// 完成任务
			activitiService.completeTaskWithReason(task.getId(), currentUser,
					null, currentUser + "提交任务");

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

			List<HisTaskInfo> taskList = executor.queryList(HisTaskInfo.class,
					"selectTaskHistorById_wf", processId);

			if (taskList != null && taskList.size() != 0) {

				for (int j = 0; j < taskList.size(); j++) {

					HisTaskInfo hti = taskList.get(j);

					// 处理人转成中文名称
					hti.setASSIGNEE_NAME(activitiService.userIdToUserName(
							hti.getASSIGNEE_(), "2"));

					// 转办/委托关系处理

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
			String taskId, String userId, ModelMap model) throws Exception {

		if (judgeAuthority(taskId, processKey, "流程开启设置2", userId)) {

			// 获取流程实例的处理记录
			List<HisTaskInfo> taskHistorList = getProcHisInfo(processId);
			model.addAttribute("taskHistorList", taskHistorList);

			List<ActNode> actList = getWFNodeConfigInfoByCondition(processKey,
					processId);

			model.addAttribute("actList", actList);

			model.addAttribute("processKey", processKey);

		} else {
		}

		return model;
	}

	@Override
	public List<ActNode> getWFNodeConfigInfoByCondition(String processKey,
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
				StringBuffer types = new StringBuffer();

				for (int i = 0; i < nodeList.size(); i++) {
					ActNode ani = nodeList.get(i);

					// 拼接匹配对象
					users.append(ani.getActId() + "_users");
					types.append(ani.getActId()
							+ ".bpmn.behavior.multiInstance.mode");

					for (int j = 0; j < variableList.size(); j++) {

						ActivitiVariable av = variableList.get(j);

						if (StringUtil.isNotEmpty(av.getTEXT_())) {

							// 用户
							if (av.getNAME_().equals(users.toString())) {

								ani.setCandidateName(av.getTEXT_());
								ani.setRealName(activitiService
										.userIdToUserName(av.getTEXT_(), "1"));
							}

							// 串/并行
							if (av.getNAME_().equals(types.toString())) {

								if (av.getTEXT_().equals("sequential")) {
									ani.setApproveType(WorkflowConstants.PRO_ACT_MUL);// 串行多实例
								} else if (av.getTEXT_().equals("parallel")) {
									ani.setApproveType(WorkflowConstants.PRO_ACT_SEQ);// 并行多实例
								} else {
									ani.setApproveType(WorkflowConstants.PRO_ACT_SEQ);// 不是多实例
								}

							}
						}
					}

					if (StringUtil.isEmpty(ani.getApproveType())) {
						ani.setApproveType(WorkflowConstants.PRO_ACT_SEQ);// 不是多实例
					}

					users.setLength(0);
					types.setLength(0);

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
		return this.getWFNodeConfigInfo(processKey, "1", userId);
	}

	@Override
	public List<ActNode> getWFNodeConfigInfoForbussiness(String processKey,
			String userId) throws Exception {
		return this.getWFNodeConfigInfo(processKey, "2", userId);
	}

	@Override
	public List<ActNode> getWFNodeConfigInfoForCommon(String processKey,
			String userId) throws Exception {
		return this.getWFNodeConfigInfo(processKey, "0", userId);
	}

}
