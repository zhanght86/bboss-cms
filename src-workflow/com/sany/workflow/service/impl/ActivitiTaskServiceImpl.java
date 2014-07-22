package com.sany.workflow.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.impl.bpmn.behavior.MultiInstanceActivityBehavior;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.task.Task;

import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.platform.cms.util.StringUtil;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.util.ListInfo;
import com.sany.workflow.entity.ActivitiNodeInfo;
import com.sany.workflow.entity.ActivitiVariable;
import com.sany.workflow.entity.NoHandleTask;
import com.sany.workflow.entity.Nodevariable;
import com.sany.workflow.entity.TaskCondition;
import com.sany.workflow.entity.TaskManager;
import com.sany.workflow.entrust.entity.WfEntrust;
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

	private String[] arraySysVariable = { "_users", "_groups", "loopCounter",
			"nrOfActiveInstances", "nrOfCompletedInstances", "nrOfInstances",
			"_user", ".bpmn.behavior.multiInstance.mode" };

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

			// 串/并行切换(多实例)
			String isMulti = activitiNodeCandidateList.get(i).getIsMulti();
			if ("1".equals(isMulti)) {
				variableMap
						.put(activitiNodeCandidateList.get(i).getNode_key()
								+ MultiInstanceActivityBehavior.multiInstanceMode_variable_const,
								MultiInstanceActivityBehavior.multiInstanceMode_sequential);
			} else if ("2".equals(isMulti)) {
				variableMap
						.put(activitiNodeCandidateList.get(i).getNode_key()
								+ MultiInstanceActivityBehavior.multiInstanceMode_variable_const,
								MultiInstanceActivityBehavior.multiInstanceMode_parallel);
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
	public void rejectToPreTask(TaskCondition task,
			List<ActivitiNodeInfo> nodeList,
			List<Nodevariable> nodevariableList, int rejectedtype) {

		// 获取参数配置信息
		Map<String, Object> variableMap = getVariableMap(nodeList,
				nodevariableList);

		if (StringUtil.isNotEmpty(task.getCompleteReason())) {
			activitiService.rejecttoPreTaskWithReson(task.getTaskId(),
					variableMap, task.getCompleteReason(), rejectedtype);
		} else {
			activitiService.rejecttoPreTask(task.getTaskId(), variableMap,
					rejectedtype);
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

			// 从扩展表中获取节点信息(包含工时)
			List<ActivitiNodeInfo> nodeList = executor.queryList(
					ActivitiNodeInfo.class, "getAllActivitiNodesInfo_wf",
					processInstId, processKey);

			if (nodeList == null) {
				tms.commit();
				return null;
			}

			// 判断是否邮件任务
			List<ActivityImpl> activties = activitiService
					.getActivitImplListByProcessKey(processKey);

			for (int i = 0; i < nodeList.size(); i++) {
				ActivitiNodeInfo nodeInfo = nodeList.get(i);

				// 节点处理工时转换
				if (nodeInfo.getDURATION_NODE() != null) {
					long worktime = Long.parseLong(nodeInfo.getDURATION_NODE());
					nodeInfo.setDURATION_NODE(formatDuring(worktime));
				}

				for (ActivityImpl activtie : activties) {
					if (activtie.getId().equals(nodeInfo.getNode_key())) {
						// 邮件任务
						if (activtie.isMailTask()) {
							nodeInfo.setNode_type("mailTask");
						}
						break;
					}
				}
			}

			// 根据流程实例ID 获取流程的参数变量信息
			List<ActivitiVariable> variableList = executor.queryList(
					ActivitiVariable.class, "getVariableListById_wf",
					processInstId);

			if (variableList != null && variableList.size() > 0) {

				StringBuffer users = new StringBuffer();
				StringBuffer groups = new StringBuffer();
				StringBuffer types = new StringBuffer();

				for (int i = 0; i < nodeList.size(); i++) {
					ActivitiNodeInfo ani = nodeList.get(i);

					// 拼接匹配对象
					users.append(ani.getNode_key() + "_users");
					groups.append(ani.getNode_key() + "_groups");
					types.append(ani.getNode_key()
							+ ".bpmn.behavior.multiInstance.mode");

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

							// 串/并行
							if (av.getNAME_().equals(types.toString())) {

								if (av.getTEXT_().equals("sequential")) {
									ani.setIsMulti("1");// 串行多实例
								} else if (av.getTEXT_().equals("parallel")) {
									ani.setIsMulti("2");// 并行多实例
								} else {
									ani.setIsMulti("0");// 不是多实例
								}

							}
						}
					}

					if (StringUtil.isEmpty(ani.getIsMulti())) {
						ani.setIsMulti("0");// 不是多实例
					}

					users.setLength(0);
					groups.setLength(0);
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

		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();
			
			boolean isAdmin = AccessControl.getAccessControl().isAdmin();
			String currentAccount = AccessControl.getAccessControl()
					.getUserAccount();

			// 数据查看权限管控
			task.setAdmin(isAdmin);
			// 当前用户登录id
			task.setAssignee(currentAccount);

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
			
			if (StringUtil.isNotEmpty(task.getCreateUser())) {
				task.setCreateUser("%" + task.getCreateUser() + "%");
			}
			
			if (StringUtil.isNotEmpty(task.getEntrustUser())) {
				task.setEntrustUser("%" + task.getEntrustUser() + "%");
			}

			// 处理的历史任务记录
			if (isAdmin) {
				listInfo = executor.queryListInfoBean(TaskManager.class,
						"selectHistoryTaskForAdmin_wf", offset, pagesize, task);
			}else {
				listInfo = executor.queryListInfoBean(TaskManager.class,
						"selectHistoryTaskForNotAdmin_wf", offset, pagesize, task);
			}
			
			// 用户转办记录

			// 获取分页中List数据
			List<TaskManager> taskList = listInfo.getDatas();

			if (taskList != null && taskList.size() > 0) {

				for (int i = 0; i < taskList.size(); i++) {
					TaskManager tmr = taskList.get(i);

					// 处理人转换
					activitiService.dealTaskInfo(tmr);
					// 转办关系处理
					activitiService.delegateTaskInfo(tmr);
					// 处理人委托转换
					activitiService.entrustTaskInfo(tmr);
					// 判断是否超时
					activitiService.judgeOverTime(tmr);
					// 耗时处理
					activitiService.handleDurationTime(tmr);
				}
			}
			
			tm.commit();

			return listInfo;
		} catch (Exception e) {
			throw new ProcessException(e);
		}finally {
			tm.release();
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
			sb.append(hours + "小时");
		}
		if (minutes != 0) {
			sb.append(minutes + "分钟");
		}
		if (seconds != 0) {
			sb.append(seconds + "秒");
		}

		return sb.toString();
	}

	@Override
	public Object[] getProcessVariable(String processInstId) {
		try {
			List<ActivitiVariable> variableList = executor.queryList(
					ActivitiVariable.class, "getVariableListById_wf",
					processInstId);

			// 非系统管理参数
			List<Nodevariable> nodevariableList = new ArrayList<Nodevariable>();
			// 系统参数
			List<Nodevariable> sysvariableList = new ArrayList<Nodevariable>();

			if (variableList != null && variableList.size() > 0) {

				for (int i = 0; i < variableList.size(); i++) {
					ActivitiVariable variable = variableList.get(i);

					Nodevariable node = new Nodevariable();
					node.setParam_name(variable.getNAME_());
					node.setParam_value(variable.getTEXT_());

					boolean isSysvariable = false;
					for (int j = 0; j < arraySysVariable.length; j++) {
						if (variable.getNAME_().endsWith(arraySysVariable[j])) {
							sysvariableList.add(node);

							isSysvariable = true;
							break;
						}
					}

					if (!isSysvariable) {
						nodevariableList.add(node);
					}
				}
			}

			return new Object[] { nodevariableList, sysvariableList };
		} catch (Exception e) {
			throw new ProcessException(e);
		}
	}

	@Override
	public List<NoHandleTask> getNoHandleTask(String pernr, String sysid,
			long offset, int pagesize, HttpServletRequest request) {
		List<NoHandleTask> list = new ArrayList<NoHandleTask>();

		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();

			// 当前用户的任务列表数据
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("assignee", pernr);
			params.put("sysid", sysid);

			List<NoHandleTask> taskList = executor.queryListBean(
					NoHandleTask.class, "selectNoHandleTask_wf", params);

			if (taskList != null && taskList.size() != 0) {
				for (int i = 0; i < taskList.size(); i++) {
					NoHandleTask nt = taskList.get(i);
					// 处理人格式化
					nt.setSender(activitiService.userIdToUserName(
							nt.getUserAccount(), "1"));

					String url = request.getContextPath()
							+ "/workflow/taskManage/toDealTaskForUnite.page?processKey="
							+ nt.getProcessKey() + "&processInstId="
							+ nt.getInstanceId() + "&taskId=" + nt.getTaskId()
							+ "&taskState=" + nt.getTaskState()
							+ "&suspensionState=" + nt.getSuspensionState();
					nt.setUrl(url);
					list.add(nt);
				}
			}

			// 委托给当前用户的任务列表数据
			Map<String, Object> entrustMap = new HashMap<String, Object>();
			entrustMap.put("isAdmin", false);
			entrustMap.put("entrust_user", pernr);

			// 根据当前用户获取委托关系列表数据
			List<WfEntrust> entrustList = executor.queryListBean(
					WfEntrust.class, "selectEntrustList", entrustMap);

			// 没有委托关系，不需要去查任务数据
			if (entrustList != null && entrustList.size() > 0) {
				params.put("entrustList", entrustList);
				// 根据当前用户获取委托关系列表数据
				List<NoHandleTask> entrustlist = executor.queryListBean(
						NoHandleTask.class, "selectNoHandleEntrustTask_wf",
						params);

				if (entrustlist != null && entrustlist.size() != 0) {
					for (int i = 0; i < entrustlist.size(); i++) {
						NoHandleTask nt = entrustlist.get(i);
						// 处理人格式化
						nt.setSender(activitiService.userIdToUserName(
								nt.getUserAccount(), "1"));

						String url = request.getContextPath()
								+ "/workflow/taskManage/toDealTaskForUnite.page?processKey="
								+ nt.getProcessKey() + "&processInstId="
								+ nt.getInstanceId() + "&taskId="
								+ nt.getTaskId() + "&taskState="
								+ nt.getTaskState() + "&suspensionState="
								+ nt.getSuspensionState();
						nt.setUrl(url);

						list.add(nt);
					}
				}
			}

			tm.commit();

			return list;
		} catch (Exception e) {
			throw new ProcessException(e);
		} finally {
			tm.release();
		}
	}

	@Override
	public void updateNodeChangeInfo(String taskId, String processIntsId,
			String processKey, String userId) {

		try {

			String fromUser = AccessControl.getAccessControl().getUserAccount();

			executor.insert("addNodeChangeInfo_wf", fromUser, userId, taskId,
					processIntsId, processKey, new Timestamp(new Date().getTime()));

		} catch (Exception e) {
			throw new ProcessException(e);
		}
	}

	@Override
	public int countTaskNum(String pernr, String sysid) {
		
		TransactionManager tm = new TransactionManager();
		try {
			
			tm.begin();
			
			// 当前用户的任务数
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("assignee", pernr);
			params.put("sysid", sysid);

			int taskNum = executor.queryObjectBean(int.class,
					"countTaskNum_wf", params);

			// 委托给当前用户的任务数
			Map<String, Object> entrustMap = new HashMap<String, Object>();
			entrustMap.put("isAdmin", false);
			entrustMap.put("entrust_user", pernr);

			// 根据当前用户获取委托关系列表数据
			List<WfEntrust> entrustList = executor.queryListBean(
					WfEntrust.class, "selectEntrustList", entrustMap);

			// 没有委托关系，不需要去查任务数据
			int entrustTaskNum = 0;
			if (entrustList != null && entrustList.size() > 0) {
				params.put("entrustList", entrustList);

				entrustTaskNum = executor.queryObjectBean(int.class,
						"countEntrustTaskNum_wf", params);
			}

			tm.commit();

			return taskNum + entrustTaskNum;
		} catch (Exception e) {
			throw new ProcessException(e);
		} finally {
			tm.release();
		}
	}

	@Override
	public void addEntrustTaskInfo(TaskCondition task) {

		try {

			executor.insert("addEntrustTaskInfo_wf", task.getTaskId(),
					task.getCreateUser(), task.getEntrustUser(),
					task.getProcessIntsId(), task.getProcessKey());

		} catch (Exception e) {
			throw new ProcessException(e);
		}
	}

	@Override
	public List<WfEntrust> getEntrustInfo() {
		try {
			String currentAccount = AccessControl.getAccessControl()
					.getUserAccount();

			return executor.queryList(WfEntrust.class, "getEntrustInfo_wf",
					currentAccount, currentAccount);

		} catch (Exception e) {
			throw new ProcessException(e);
		}
	}

	@Override
	public boolean judgeAuthority(String taskId) {

		try {

			boolean isAdmin = AccessControl.getAccessControl().isAdmin();

			if (isAdmin) {
				return true;
			} else {
				String currentAccount = AccessControl.getAccessControl()
						.getUserAccount();

				// 首先判断任务是否有没签收，如果签收，以签收人为准，如果没签收，则以该节点配置的人为准
				Task task = activitiService.getTaskById(taskId);

				if (StringUtil.isNotEmpty(task.getAssignee())) {

					if (currentAccount.equals(task.getAssignee())) {
						return true;
					}

				} else {
					// 任务未签收，根据任务id查询任务可处理人
					List<HashMap> candidatorList = executor.queryList(
							HashMap.class, "getNodeCandidates_wf", taskId,
							currentAccount);

					if (candidatorList != null && candidatorList.size() > 0) {
						return true;
					}
				}

				// 最后查看当前用户的委托关系
				List<WfEntrust> entrustList = executor.queryList(
						WfEntrust.class, "getEntrustRelation_wf",
						currentAccount, task.getTaskDefinitionKey(),
						new Timestamp(task.getCreateTime().getTime()),
						new Timestamp(task.getCreateTime().getTime()));

				if (entrustList == null || entrustList.size() == 0) {
					return false;
				} else {
					return true;
				}
			}

		} catch (Exception e) {
			throw new ProcessException(e);
		}
	}
}
