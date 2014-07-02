package com.sany.workflow.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.platform.cms.util.StringUtil;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.util.ListInfo;
import com.sany.workflow.entity.ActivitiNodeInfo;
import com.sany.workflow.entity.ActivitiVariable;
import com.sany.workflow.entity.Nodevariable;
import com.sany.workflow.entity.TaskCondition;
import com.sany.workflow.entity.TaskManager;
import com.sany.workflow.service.ActivitiService;
import com.sany.workflow.service.ActivitiTaskService;
import com.sany.workflow.service.ProcessException;

/**
 * 任务管理业务实现类
 * 
 * @todo
 * @author tanx
 * @date 2014年5月27日
 * 
 */
public class ActivitiTaskServiceImpl implements ActivitiTaskService,
		org.frameworkset.spi.DisposableBean {

	private com.frameworkset.common.poolman.ConfigSQLExecutor executor;

	private ActivitiService activitiService;

	@Override
	public void destroy() throws Exception {

	}

	/**
	 * 获取流程节点参数配置信息
	 * 
	 * @param nodeList
	 * @return 2014年5月27日
	 */
	private Map<String, Object> getVariableMap(
			List<ActivitiNodeInfo> activitiNodeCandidateList,
			List<Nodevariable> nodevariableList) {

		Map<String, Object> variableMap = new HashMap<String, Object>();

		// 流程参数
		for (int i = 0; i < activitiNodeCandidateList.size(); i++) {
			// 用户
			if (!StringUtil.isEmpty(activitiNodeCandidateList.get(i)
					.getNode_users_id())) {

				variableMap.put(activitiNodeCandidateList.get(i).getNode_key()
						+ "_users", activitiNodeCandidateList.get(i)
						.getNode_users_id());
			} else {
				variableMap.put(activitiNodeCandidateList.get(i).getNode_key()
						+ "_users", "");
			}

			// 组
			if (!StringUtil.isEmpty(activitiNodeCandidateList.get(i)
					.getNode_groups_id())) {
				variableMap.put(activitiNodeCandidateList.get(i).getNode_key()
						+ "_groups", activitiNodeCandidateList.get(i)
						.getNode_groups_id());
			} else {
				variableMap.put(activitiNodeCandidateList.get(i).getNode_key()
						+ "_groups", "");
			}

		}

		for (int i = 0; i < nodevariableList.size(); i++) {
			// 参数
			if (!StringUtil.isEmpty(nodevariableList.get(i).getParam_name())) {
				variableMap.put(nodevariableList.get(i).getParam_name(),
						nodevariableList.get(i).getParam_value());
			}
		}
		return variableMap;
	}

	@Override
	public void completeTask(TaskCondition task,
			List<ActivitiNodeInfo> activitiNodeCandidateList,
			List<Nodevariable> nodevariableList) {

		try {

			// 获取参数配置信息
			Map<String, Object> variableMap = getVariableMap(
					activitiNodeCandidateList, nodevariableList);

			// 未签收任务处理
			if ("1".equals(task.getTaskState())) {

				if (StringUtil.isEmpty(task.getTaskDefKey())) {

					if (StringUtil.isEmpty(task.getCompleteReason())) {

						activitiService.completeTask(task.getTaskId(),
								AccessControl.getAccessControl()
										.getUserAccount(), variableMap);
					} else {
						activitiService.completeTaskWithReason(
								task.getTaskId(), AccessControl
										.getAccessControl().getUserAccount(),
								variableMap, task.getCompleteReason());
					}

				} else {

					if (StringUtil.isEmpty(task.getCompleteReason())) {

						activitiService.completeTaskWithLocalVariables(task
								.getTaskId(), AccessControl.getAccessControl()
								.getUserAccount(), variableMap, task
								.getTaskDefKey());
					} else {
						activitiService.completeTaskWithLocalVariablesReason(
								task.getTaskId(), AccessControl
										.getAccessControl().getUserAccount(),
								variableMap, task.getTaskDefKey(), task
										.getCompleteReason());
					}
				}

				// 已签收任务处理
			} else if ("2".equals(task.getTaskState())) {

				if (StringUtil.isEmpty(task.getTaskDefKey())) {

					if (StringUtil.isEmpty(task.getCompleteReason())) {

						activitiService.completeTask(task.getTaskId(),
								variableMap);

					} else {
						activitiService.completeTaskWithReason(
								task.getTaskId(), variableMap,
								task.getCompleteReason());
					}

				} else {
					if (StringUtil.isEmpty(task.getCompleteReason())) {
						activitiService.completeTaskLoadCommonParams(
								task.getTaskId(), variableMap,
								task.getTaskDefKey());
					} else {
						activitiService.completeTaskLoadCommonParamsReason(
								task.getTaskId(), variableMap,
								task.getTaskDefKey(), task.getCompleteReason());
					}
				}
			}
		} catch (Exception e) {
			throw new ProcessException(e);
		}

	}

	@Override
	public void rejectToPreTask(TaskCondition task, List<ActivitiNodeInfo> nodeList,
			List<Nodevariable> nodevariableList) {

		// 获取参数配置信息
		Map<String, Object> variableMap = getVariableMap(nodeList,
				nodevariableList);

		if (StringUtil.isNotEmpty(task.getCompleteReason())) {
			activitiService.rejecttoPreTaskWithReson(task.getTaskId(), variableMap,task.getCompleteReason());
		}else {
			activitiService.rejecttoPreTask(task.getTaskId(), variableMap);
		}

	}

	@Override
	public void signTaskByUser(String taskId, String username) {
		activitiService.getTaskService().claim(taskId, username);
	}

	@Override
	public List<ActivitiNodeInfo> getNodeInfoById(String processKey,
			String processInstId) {
		TransactionManager tms = new TransactionManager();

		try {
			tms.begin();

			// 从扩展表中获取节点信息
			List<ActivitiNodeInfo> nodeList = executor
					.queryList(ActivitiNodeInfo.class, "queryAllActivitiNodes",
							processKey);

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
				StringBuffer groups = new StringBuffer();

				for (int i = 0; i < nodeList.size(); i++) {
					ActivitiNodeInfo ani = nodeList.get(i);

					// 非人工处理的过滤
					if (!"userTask".equals(ani.getNode_type())) {
						continue;
					}

					// 拼接匹配对象
					users.append(ani.getNode_key() + "_users");
					groups.append(ani.getNode_key() + "_groups");

					for (int j = 0; j < variableList.size(); j++) {

						ActivitiVariable av = variableList.get(j);

						if (StringUtil.isNotEmpty(av.getTEXT_())) {

							// 用户
							if (av.getNAME_().equals(users.toString())) {

								ani.setNode_users_id(av.getTEXT_());
								ani.setNode_users_name(activitiService
										.userIdToUserName(av.getTEXT_(), "1"));
							}

							// 组
							if (av.getNAME_().equals(groups.toString())) {

								ani.setNode_groups_id(av.getTEXT_());
							}
						}
					}

					users.setLength(0);
					groups.setLength(0);

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
	public List<ActivitiNodeInfo> getNextNodeInfoById(
			final List<ActivitiNodeInfo> nodeList, String processInstId) {
		try {

			if (nodeList == null || nodeList.size() == 0) {
				return null;
			}

			// 获取当前节点信息
			HashMap currnetNodeMap = executor.queryObject(HashMap.class,
					"getCurrentNodeInfoById_wf", processInstId);

			// 判断当前节点是否有指向任意节点的权限(预留)

			List<ActivitiNodeInfo> nextNodeList = new ArrayList<ActivitiNodeInfo>();

			for (int i = 0; i < nodeList.size(); i++) {

				ActivitiNodeInfo ani = nodeList.get(i);

				// 过滤开始节点
				if ("startEvent".equals(ani.getNode_type())) {
					continue;
				}

				// 过滤exclusiveGateway
				if ("exclusiveGateway".equals(ani.getNode_type())) {
					continue;
				}

				// 过滤当前节点
				if (ani.getNode_key().equals(
						currnetNodeMap.get("TASK_DEF_KEY_"))) {
					continue;
				}

				nextNodeList.add(ani);
			}

			return nextNodeList;

		} catch (Exception e) {
			throw new ProcessException(e);
		}

	}

	@Override
	public ListInfo queryHistoryTasks(TaskCondition task, long offset,
			int pagesize) {

		ListInfo listInfo = null;

		try {
			// 数据查看权限管控
			task.setAdmin(AccessControl.getAccessControl().isAdmin());
			// 当前用户登录id
			task.setAssignee(AccessControl.getAccessControl().getUserAccount());

			if (StringUtil.isNotEmpty(task.getProcessIntsId())) {
				task.setProcessIntsId("%" + task.getProcessIntsId() + "%");
			}

			if (StringUtil.isNotEmpty(task.getTaskName())) {
				task.setTaskName("%" + task.getTaskName() + "%");
			}

			if (StringUtil.isNotEmpty(task.getTaskId())) {
				task.setTaskId("%" + task.getTaskId() + "%");
			}

			if (StringUtil.isNotEmpty(task.getBusinessKey())) {
				task.setBusinessKey("%" + task.getBusinessKey() + "%");
			}

			if (StringUtil.isNotEmpty(task.getProcessKey())) {
				task.setProcessKey("%" + task.getProcessKey() + "%");
			}

			listInfo = executor.queryListInfoBean(TaskManager.class,
					"selectHistoryTask_wf", offset, pagesize, task);

			// 获取分页中List数据
			List<TaskManager> taskList = listInfo.getDatas();

			if (taskList != null && taskList.size() > 0) {
				// 判断是否超时
				activitiService.judgeOverTime(taskList);

				for (int i = 0; i < taskList.size(); i++) {
					TaskManager tm = taskList.get(i);
					// 处理人格式化
					tm.setASSIGNEE_(activitiService.userIdToUserName(
							tm.getASSIGNEE_(), "2"));
					// 节点耗时格式化
					if (StringUtil.isNotEmpty(tm.getDURATION_())) {
						long mss = Long.parseLong(tm.getDURATION_());
						tm.setDURATION_(formatDuring(mss));
					}
					// 节点处理工时格式化
					if (StringUtil.isNotEmpty(tm.getDURATION_NODE())) {
						long worktime = Long.parseLong(tm.getDURATION_NODE());
						tm.setDURATION_NODE(formatDuring(worktime));
					}
				}
			}

			return listInfo;
		} catch (Exception e) {
			throw new ProcessException(e);
		}
	}

	/**
	 * 耗时转换 gw_tanx
	 * 
	 * @param mss
	 * @return 2014年5月21日
	 */
	private String formatDuring(long mss) {
		long days = mss / (1000 * 60 * 60 * 24);
		long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
		long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
		long seconds = (mss % (1000 * 60)) / 1000;

		StringBuffer sb = new StringBuffer();
		if (days != 0) {
			sb.append(days + "天");
		}
		if (hours != 0) {
			sb.append(hours + "时");
		}
		if (minutes != 0) {
			sb.append(minutes + "分");
		}
		if (seconds != 0) {
			sb.append(seconds + "秒");
		}

		return sb.toString();
	}

	@Override
	public List<Nodevariable> getProcessVariable(String processInstId) {
		try {
			List<ActivitiVariable> variableList = executor.queryList(
					ActivitiVariable.class, "getVariableListById_wf",
					processInstId);

			List<Nodevariable> nodevariableList = new ArrayList<Nodevariable>();

			if (variableList != null && variableList.size() > 0) {

				for (int i = 0; i < variableList.size(); i++) {
					ActivitiVariable variable = variableList.get(i);

					if (variable.getNAME_().endsWith("_users")) {
						continue;
					} else if (variable.getNAME_().endsWith("_groups")) {
						continue;
					} else if (variable.getNAME_().endsWith("loopCounter")) {
						continue;
					} else if (variable.getNAME_().endsWith("nrOfActiveInstances")) {
						continue;
					} else if (variable.getNAME_().endsWith("nrOfCompletedInstances")) {
						continue;
					} else if (variable.getNAME_().endsWith("nrOfInstances")) {
						continue;
					} else if (variable.getNAME_().endsWith("_user")) {
						continue;
					}else {
						Nodevariable node = new Nodevariable();
						node.setParam_name(variable.getNAME_());
						node.setParam_value(variable.getTEXT_());
						nodevariableList.add(node);
					}
				}
			}

			return nodevariableList;
		} catch (Exception e) {
			throw new ProcessException(e);
		}
	}
}
