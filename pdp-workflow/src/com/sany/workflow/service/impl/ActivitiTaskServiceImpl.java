package com.sany.workflow.service.impl;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.persistence.entity.CopyTaskEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.task.Task;

import com.frameworkset.common.poolman.Record;
import com.frameworkset.common.poolman.handle.NullRowHandler;
import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.security.authentication.CheckCallBack;
import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.platform.sysmgrcore.manager.db.OrgCacheManager;
import com.frameworkset.platform.sysmgrcore.manager.db.UserCacheManager;
import com.frameworkset.util.ListInfo;
import com.frameworkset.util.StringUtil;
import com.sany.workflow.business.entity.TaskInfo;
import com.sany.workflow.business.entity.WfRunTask;
import com.sany.workflow.entity.ActivitiNodeInfo;
import com.sany.workflow.entity.ActivitiVariable;
import com.sany.workflow.entity.DelegateTaskLog;
import com.sany.workflow.entity.NoHandleTask;
import com.sany.workflow.entity.NodeControlParam;
import com.sany.workflow.entity.Nodevariable;
import com.sany.workflow.entity.PageData;
import com.sany.workflow.entity.ProcessInst;
import com.sany.workflow.entity.RejectLog;
import com.sany.workflow.entity.TaskCondition;
import com.sany.workflow.entity.TaskManager;
import com.sany.workflow.entity.User;
import com.sany.workflow.entrust.entity.WfEntrust;
import com.sany.workflow.service.ActivitiService;
import com.sany.workflow.service.ActivitiTaskService;
import com.sany.workflow.service.ProcessException;
import com.sany.workflow.service.TaskTrigger;
import com.sany.workflow.service.TempleService;

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

	private TempleService templeService;

	private String[] arraySysVariable = { "_users", "_groups", "loopCounter",
			"nrOfActiveInstances", "nrOfCompletedInstances", "nrOfInstances",
			"_user", ".bpmn.behavior.multiInstance.mode" };

	@Override
	public void destroy() throws Exception {

	}

	@Override
	public Map<String, Object> getVariableMap(
			List<ActivitiNodeInfo> activitiNodeCandidateList,
			List<Nodevariable> nodevariableList,
			List<NodeControlParam> nodeControlParamList) throws Exception {

		Map<String, Object> variableMap = new HashMap<String, Object>();

		// 流程参数
		if (activitiNodeCandidateList != null
				&& activitiNodeCandidateList.size() > 0) {
			for (int i = 0; i < activitiNodeCandidateList.size(); i++) {
				ActivitiNodeInfo nodeCandidate = activitiNodeCandidateList
						.get(i);

				// 过滤抄送节点
				if (nodeCandidate.getIs_copy() == 0) {
					// 用户
					if (!StringUtil.isEmpty(nodeCandidate.getNode_users_id())) {

						variableMap.put(nodeCandidate.getNode_key() + "_users",
								nodeCandidate.getNode_users_id());
					} else {
						variableMap.put(nodeCandidate.getNode_key() + "_users",
								"");
					}

					// 组
					if (!StringUtil.isEmpty(activitiNodeCandidateList.get(i)
							.getNode_groups_id())) {
						variableMap.put(
								nodeCandidate.getNode_key() + "_groups",
								nodeCandidate.getNode_groups_id());
					} else {
						variableMap.put(
								nodeCandidate.getNode_key() + "_groups", "");
					}
				} else {
					for (int j = 0; j < nodeControlParamList.size(); j++) {
						NodeControlParam controlParam = nodeControlParamList
								.get(j);

						if (!controlParam.getNODE_KEY().equals(
								nodeCandidate.getNode_key())) {
							continue;
						}

						controlParam.setIS_COPY(nodeCandidate.getIs_copy());
						controlParam.setCOPYUSERS(nodeCandidate
								.getNode_users_id());
						controlParam.setCOPYORGS(nodeCandidate
								.getNode_orgs_id());

						if (StringUtil.isNotEmpty(nodeCandidate
								.getNode_users_id())
								&& StringUtil.isNotEmpty(nodeCandidate
										.getNode_orgs_id())) {
							controlParam.setCOPYERSCNNAME(nodeCandidate
									.getNode_users_name()
									+ ","
									+ nodeCandidate.getNode_orgs_name());

						} else if (StringUtil.isEmpty(nodeCandidate
								.getNode_users_id())
								&& StringUtil.isNotEmpty(nodeCandidate
										.getNode_orgs_id())) {
							controlParam.setCOPYERSCNNAME(nodeCandidate
									.getNode_orgs_name());

						} else if (StringUtil.isNotEmpty(nodeCandidate
								.getNode_users_id())
								&& StringUtil.isEmpty(nodeCandidate
										.getNode_orgs_id())) {
							controlParam.setCOPYERSCNNAME(nodeCandidate
									.getNode_users_name());
						} else {
							controlParam.setCOPYERSCNNAME("");
						}
					}
				}
			}
		}

		if (nodevariableList != null && nodevariableList.size() > 0) {
			for (int i = 0; i < nodevariableList.size(); i++) {
				// 参数
				if (!StringUtil
						.isEmpty(nodevariableList.get(i).getParam_name())) {
					variableMap.put(nodevariableList.get(i).getParam_name(),
							nodevariableList.get(i).getParam_value());
				}
			}
		}
		return variableMap;
	}

	/**
	 * 没有执行统一待办任务刷新机制，外部方法调用时，需要自行执行统一待办刷新操作
	 */
	@Override
	public void completeTask(TaskCondition task, Map<String, Object> variableMap) {

		try {

			// // 获取参数配置信息
			// Map<String, Object> variableMap = getVariableMap(
			// activitiNodeCandidateList, nodevariableList,
			// nodeControlParamList);

			// 未签收任务处理
			if ("1".equals(task.getTaskState())) {

				if (StringUtil.isEmpty(task.getTaskDefKey())) {

					activitiService.completeTaskWithReason(task.getTaskId(),
							task.getCurrentUser(), variableMap,
							task.getCompleteRemark(), "完成任务",
							task.getCompleteReason());

				} else {

					activitiService.completeTaskWithLocalVariablesReason(
							task.getTaskId(), task.getCurrentUser(),
							variableMap, task.getTaskDefKey(),
							task.getCompleteRemark(), "完成任务",
							task.getCompleteReason());
				}

				// 已签收任务处理
			} else if ("2".equals(task.getTaskState())) {

				if (StringUtil.isEmpty(task.getTaskDefKey())) {

					activitiService.completeTaskWithReason(task.getTaskId(),
							variableMap, task.getCompleteRemark(), "完成任务",
							task.getCompleteReason());

				} else {
					activitiService.completeTaskLoadCommonParamsReason(
							task.getTaskId(), variableMap,
							task.getTaskDefKey(), task.getCompleteRemark(),
							"完成任务", task.getCompleteReason());
				}
			}
		} catch (Exception e) {
			throw new ProcessException(e);
		}

	}

	/**
	 * 没有执行统一待办刷新操作，如果需要刷新请外部程序自行处理
	 */
	@Override
	public void rejectToPreTask(TaskCondition task,
			Map<String, Object> variableMap, int rejectedtype) {

		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();

			if (!isSignTask(task.getTaskId())) {
				// 先签收
				activitiService.claim(task.getTaskId(), task.getCurrentUser());
			}

			// // 获取参数配置信息
			// Map<String, Object> variableMap = getVariableMap(nodeList,
			// nodevariableList, nodeControlParamList);

			String remark = "["
					+ activitiService.getUserInfoMap().getUserName(
							task.getCurrentUser()) + "]将任务驳回至["
					+ task.getToActName() + "]";

			// 日志记录驳回操作
			activitiService.addDealTask(task.getTaskId(),
					task.getCurrentUser(), activitiService.getUserInfoMap()
							.getUserName(task.getCurrentUser()), "1", task
							.getProcessIntsId(), task.getProcessKey(), task
							.getCompleteReason(), "驳回任务", remark);

			activitiService.getTaskService().rejecttoTask(task.getTaskId(),
					variableMap, remark, task.getRejectToActId(),
					rejectedtype == 1 ? true : false, "驳回任务",
					task.getCompleteReason());

			tm.commit();
		} catch (Exception e) {
			throw new ProcessException(e);
		} finally {
			tm.release();
		}
	}

	@Override
	public void signTaskByUser(String taskId, String username) {
		activitiService.getTaskService().claim(taskId, username);
	}
	
	public String getProcessInstanceidOfTask(String taskid) throws Exception
	{
		return this.executor.queryObject(String.class, "getProcessInstanceidOfTask", taskid);
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

			for (int i = 0; i < nodeList.size(); i++) {
				ActivitiNodeInfo nodeInfo = nodeList.get(i);

				// 节点处理工时转换
				// if (nodeInfo.getDuration_node() != null) {
				// long worktime = Long.parseLong(nodeInfo.getDuration_node());
				// nodeInfo.setDuration_node(StringUtil
				// .formatTimeToString(worktime));
				// }

				// 抄送节点去worktime获取处理人
				if (nodeInfo.getIs_copy() != 0) {
					nodeInfo.setNode_users_id(nodeInfo.getCopyusers());
					nodeInfo.setNode_orgs_id(nodeInfo.getCopyorgs());

					if (StringUtil.isNotEmpty(nodeInfo.getCopyerscnname())) {
						if (StringUtil.isNotEmpty(nodeInfo.getCopyusers())
								&& StringUtil
										.isNotEmpty(nodeInfo.getCopyorgs())) {

							String usersName = activitiService
									.userIdToUserName(nodeInfo.getCopyusers(),
											"1");
							nodeInfo.setNode_users_name(usersName);
							nodeInfo.setNode_orgs_name(nodeInfo
									.getCopyerscnname().substring(
											usersName.length() + 1));

						} else if (StringUtil.isEmpty(nodeInfo.getCopyusers())
								&& StringUtil
										.isNotEmpty(nodeInfo.getCopyorgs())) {
							nodeInfo.setNode_users_name("");
							nodeInfo.setNode_orgs_name(nodeInfo
									.getCopyerscnname());
						} else if (StringUtil.isNotEmpty(nodeInfo
								.getCopyusers())
								&& StringUtil.isEmpty(nodeInfo.getCopyorgs())) {
							nodeInfo.setNode_users_name(nodeInfo
									.getCopyerscnname());
							nodeInfo.setNode_orgs_name("");
						} else {
							nodeInfo.setNode_users_name("");
							nodeInfo.setNode_orgs_name("");
						}
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

				for (int i = 0; i < nodeList.size(); i++) {
					ActivitiNodeInfo ani = nodeList.get(i);

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

			if (!isAdmin) {
				// 当前用户登录id
				task.setAssignee(currentAccount);
			}

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

			if (StringUtil.isNotEmpty(task.getAppName())) {
				task.setAppName("%" + task.getAppName() + "%");
			}

			// 处理的历史任务记录
			if (isAdmin) {
				listInfo = executor.queryListInfoBean(TaskManager.class,
						"selectHistoryTaskForAdmin_wf", offset, pagesize, task);
			} else {
				listInfo = executor.queryListInfoBean(TaskManager.class,
						"selectHistoryTaskForNotAdmin_wf", offset, pagesize,
						task);
			}

			// 获取分页中List数据
			List<TaskManager> taskList = listInfo.getDatas();

			if (taskList != null && taskList.size() > 0) {

				for (int i = 0; i < taskList.size(); i++) {
					TaskManager tmr = taskList.get(i);

					// 处理人转换
					activitiService.dealTaskInfo(tmr);
					// 委托关系处理
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
		} finally {
			tm.release();
		}
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
					node.setId(variable.getID_());
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

	/**
	 * 统一代办获取用户自己的任务
	 * 
	 * @param params
	 * @param list
	 * @param request
	 *            2014年7月23日
	 */
	private void getOneselfTask(Map<String, Object> params,
			List<NoHandleTask> list) {
		try {
			// 当前用户的任务列表数据
			List<NoHandleTask> taskList = executor.queryListBean(
					NoHandleTask.class, "selectNoHandleTask_wf", params);

			if (taskList != null && taskList.size() != 0) {
				for (int i = 0; i < taskList.size(); i++) {
					NoHandleTask nt = taskList.get(i);
					// 处理人格式化
					nt.setSender(activitiService.userIdToUserName(
							nt.getUserAccount(), "1"));

					String url = nt.getAppUrl()
							+ "/workflow/taskManage/toDealTask.page?processKey="
							+ nt.getProcessKey() + "&processInstId="
							+ nt.getInstanceId() + "&taskId=" + nt.getTaskId()
							+ "&taskState=" + nt.getTaskState()
							+ "&suspensionState=" + nt.getSuspensionState()
							+ "&sysid=" + params.get("sysid");

					if (StringUtil.isNotEmpty(nt.getFromUser())) {

						nt.setFromUserName(activitiService.userIdToUserName(
								nt.getFromUser(), "1"));

						nt.setTitle((nt.getTitle() + "(" + nt.getBusinessKey()
								+ ")[" + nt.getFromUserName() + "转办]"));

						nt.setTaskType(2);// 转办任务
					} else {

						nt.setTitle((nt.getTitle() + "(" + nt.getBusinessKey() + ")"));

						nt.setTaskType(0);// 自己任务
					}
					nt.setUrl(url);

					// 处理按钮
					if (nt.getSuspensionState().equals("2")) {
						nt.setDealButtionName("处理[挂起]");
					} else {
						nt.setDealButtionName("处理");
					}

					list.add(nt);
				}
			}
		} catch (Exception e) {
			throw new ProcessException(e);
		}
	}

	/**
	 * 统一代办获取用户委托任务
	 * 
	 * @param params
	 * @param list
	 * @param request
	 *            2014年7月23日
	 */
	private void getEntrustTask(Map<String, Object> params,
			List<NoHandleTask> list) {
		try {
			Map<String, Object> entrustMap = new HashMap<String, Object>();
			entrustMap.put("isAdmin", false);
			entrustMap.put("entrust_user", params.get("assignee"));

			// 根据当前用户获取委托关系列表数据
			List<WfEntrust> entrustList = executor.queryListBean(
					WfEntrust.class, "selectEntrustList", entrustMap);

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
						nt.setFromUserName(activitiService.userIdToUserName(
								nt.getFromUser(), "1"));

						String url = nt.getAppUrl()
								+ "/workflow/taskManage/toDealTask.page?processKey="
								+ nt.getProcessKey() + "&processInstId="
								+ nt.getInstanceId() + "&taskId="
								+ nt.getTaskId() + "&taskState="
								+ nt.getTaskState() + "&suspensionState="
								+ nt.getSuspensionState() + "&sysid="
								+ params.get("sysid") + "&createUser="
								+ nt.getFromUser() + "&entrustUser="
								+ params.get("assignee");

						nt.setTitle((nt.getTitle() + "(" + nt.getBusinessKey()
								+ ")[" + nt.getFromUserName() + "委托]"));

						// 处理按钮
						if (nt.getSuspensionState().equals("2")) {
							nt.setDealButtionName("处理[挂起]");
						} else {
							nt.setDealButtionName("处理");
						}

						nt.setTaskType(1);// 委托任务
						nt.setUrl(url);
						list.add(nt);
					}
				}
			}
		} catch (Exception e) {
			throw new ProcessException(e);
		}
	}

	@Override
	public List<NoHandleTask> getNoHandleTask(String pernr, String sysid,
			long offset, int pagesize) {
		List<NoHandleTask> list = new ArrayList<NoHandleTask>();

		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("assignee", pernr);
			params.put("sysid", sysid);

			// 获取当前用户自己的任务(包括别人转办给当前用户的任务)
			getOneselfTask(params, list);

			// 获取别人委托给当前用户的任务
			getEntrustTask(params, list);

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
			String processKey, String fromuserID, String userId, String reamrk,
			String reason, int delegateType) {

		try {

			executor.insert("addNodeChangeInfo_wf", fromuserID, userId, taskId,
					processIntsId, processKey,
					new Timestamp(new Date().getTime()), reamrk, reason,
					delegateType);

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
					task.getProcessIntsId(), task.getProcessKey(),
					new Timestamp(new Date().getTime()));

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
	public boolean judgeAuthority(String taskId, String processKey) {
		return judgeAuthority(taskId, processKey,AccessControl.getAccessControl()
				.getUserAccount());
//		boolean isAdmin = AccessControl.getAccessControl().isAdmin();
//		if (isAdmin) {
//			return true;
//		}
//
//		if (StringUtil.isEmpty(taskId)) {
//			return false;
//		}
//
//		TransactionManager tm = new TransactionManager();
//
//		try {
//			tm.begin();
//
//			boolean haspermission = false;
//
//			String currentAccount = AccessControl.getAccessControl()
//					.getUserAccount();
//
//			// 首先判断任务是否有没签收，如果签收，以签收人为准，如果没签收，则以该节点配置的人为准
//			TaskManager task = executor.queryObject(TaskManager.class,
//					"getHiTaskIdByTaskId", taskId);
//
//			if (StringUtil.isNotEmpty(task.getASSIGNEE_())) {
//
//				if (currentAccount.equals(task.getASSIGNEE_())) {
//					haspermission = true;
//				}
//
//			} else {
//				// 任务未签收，根据任务id查询任务可处理人
//				List<HashMap> candidatorList = executor.queryList(
//						HashMap.class, "getNodeCandidates_wf", taskId,
//						currentAccount);
//
//				if (candidatorList != null && candidatorList.size() > 0) {
//					haspermission = true;
//				}
//			}
//
//			if (!haspermission) {
//				// 最后查看当前用户的委托关系
//				List<WfEntrust> entrustList = executor.queryList(
//						WfEntrust.class, "getEntrustRelation_wf",
//						currentAccount, processKey, new Timestamp(task
//								.getSTART_TIME_().getTime()), new Timestamp(
//								task.getSTART_TIME_().getTime()));
//
//				if (entrustList != null && entrustList.size() > 0) {
//
//					haspermission = true;
//				}
//			}
//			tm.commit();
//			return haspermission;
//
//		} catch (Exception e) {
//			throw new ProcessException(e);
//		} finally {
//			tm.release();
//		}
	}

	@Override
	public void sendMess(String taskId, String taskState, final String sentType)
			throws Exception {

		final List<Map<String, Object>> fieldList = new ArrayList<Map<String, Object>>();

		// 未签收的任务
		if ("1".equals(taskState)) {
			executor.queryByNullRowHandler(new NullRowHandler() {
				@Override
				public void handleRow(Record origine) throws Exception {
					Map<String, Object> map = new HashMap<String, Object>();
					// --任务id
					map.put("taskId", origine.getString("TASKID"));
					// --任务名称
					map.put("taskName", origine.getString("TASKNAME"));
					// --任务创建时间
					map.put("createTime", origine.getTimestamp("CREATETIME"));
					// --流程名称
					map.put("processName", origine.getString("PROCESSNAME"));
					// --流程实例id
					map.put("procInstanceId",
							origine.getString("PROCINSTANCEID"));
					// --任务节点key
					map.put("taskDefKey", origine.getString("TASKDEFKEY"));
					// --预警时间点
					map.put("alertTime", origine.getTimestamp("ALERTTIME"));
					// --超时时间点
					map.put("overTime", origine.getTimestamp("OVERTIME"));
					// 短信模板id
					map.put("messageTempleId",
							origine.getString("MESSAGETEMPLEID"));
					// 邮件模板id
					map.put("emailTempleId", origine.getString("EMAILTEMPLEID"));
					// 处理人所在部门
					map.put("orgId",
							activitiService.getUserInfoMap().getUserAttribute(
									origine.getString("USERID"), "orgId")
									+ "");
					// 手机号码
					map.put("mobile",
							activitiService.getUserInfoMap().getUserAttribute(
									origine.getString("USERID"),
									"userMobiletel1")
									+ "");
					// 处理人工号
					map.put("worknum",
							activitiService.getUserInfoMap().getUserAttribute(
									origine.getString("USERID"),
									"userWorknumber")
									+ "");
					// 处理人姓名
					String userName = activitiService.getUserInfoMap()
							.getUserName(origine.getString("USERID"));
					// 处理人邮件地址
					String userEmail = origine.getString("USERID")
							+ "@sany.com.cn";
					if (StringUtil.isNotEmpty(origine.getString("ENTRUSTUSER"))) {
						// 委托人姓名
						String entrustUserName = activitiService
								.getUserInfoMap().getUserName(
										origine.getString("ENTRUSTUSER"));
						// 委托人邮件地址
						String entrustUserEmail = origine
								.getString("ENTRUSTUSER") + "@sany.com.cn";

						map.put("mailAddress", new String[] { userEmail,
								entrustUserEmail });
						map.put("realName", userName + "(" + entrustUserName
								+ ")");
					} else {
						map.put("mailAddress", new String[] { userEmail });
						map.put("realName", userName);
					}

					if ("1".equals(sentType)) {
						map.put("subject", "流程预警提醒");
						map.put("noticeType", "1");
					} else {
						map.put("subject", "流程超时提醒");
						map.put("noticeType", "2");
					}

					fieldList.add(map);
				}
			}, "getNoSignTaskById", taskId);

		} else {// 已签收任务
			executor.queryByNullRowHandler(new NullRowHandler() {
				@Override
				public void handleRow(Record origine) throws Exception {
					Map<String, Object> map = new HashMap<String, Object>();
					// --任务id
					map.put("taskId", origine.getString("TASKID"));
					// --任务名称
					map.put("taskName", origine.getString("TASKNAME"));
					// --任务创建时间
					map.put("createTime", origine.getTimestamp("CREATETIME"));
					// --流程名称
					map.put("processName", origine.getString("PROCESSNAME"));
					// --流程实例id
					map.put("procInstanceId",
							origine.getString("PROCINSTANCEID"));
					// --任务节点key
					map.put("taskDefKey", origine.getString("TASKDEFKEY"));
					// --预警时间点
					map.put("alertTime", origine.getTimestamp("ALERTTIME"));
					// --超时时间点
					map.put("overTime", origine.getTimestamp("OVERTIME"));
					// 短信模板id
					map.put("messageTempleId",
							origine.getString("MESSAGETEMPLEID"));
					// 邮件模板id
					map.put("emailTempleId", origine.getString("EMAILTEMPLEID"));
					// 处理人所在部门
					map.put("orgId",
							activitiService.getUserInfoMap().getUserAttribute(
									origine.getString("USERID"), "orgId")
									+ "");
					// 手机号码
					map.put("mobile",
							activitiService.getUserInfoMap().getUserAttribute(
									origine.getString("USERID"),
									"userMobiletel1")
									+ "");
					// 处理人工号
					map.put("worknum",
							activitiService.getUserInfoMap().getUserAttribute(
									origine.getString("USERID"),
									"userWorknumber")
									+ "");

					// 处理人姓名
					String userName = activitiService.getUserInfoMap()
							.getUserName(origine.getString("USERID"));
					// 处理人邮件地址
					String userEmail = origine.getString("USERID")
							+ "@sany.com.cn";
					if (StringUtil.isNotEmpty(origine.getString("ENTRUSTUSER"))) {
						// 委托人姓名
						String entrustUserName = activitiService
								.getUserInfoMap().getUserName(
										origine.getString("ENTRUSTUSER"));
						// 委托人邮件地址
						String entrustUserEmail = origine
								.getString("ENTRUSTUSER") + "@sany.com.cn";

						map.put("mailAddress", new String[] { userEmail,
								entrustUserEmail });
						map.put("realName", userName + "(" + entrustUserName
								+ ")");
					} else {
						map.put("mailAddress", new String[] { userEmail });
						map.put("realName", userName);
					}

					if ("1".equals(sentType)) {
						map.put("subject", "流程预警提醒");
						map.put("noticeType", "1");
					} else {
						map.put("subject", "流程超时提醒");
						map.put("noticeType", "2");
					}

					fieldList.add(map);
				}
			}, "getSignedTaskById", taskId);
		}

		templeService.sendNotice(fieldList);

	}

	@Override
	public boolean isSignTask(String taskId) {
		try {

			HashMap map = executor.queryObject(HashMap.class,
					"getTaskInfoByTaskId_wf", taskId);

			if (map != null && map.get("ASSIGNEE_") != null) {
				return true;
			} else {
				return false;
			}

		} catch (Exception e) {
			throw new ProcessException(e);
		}
	}

	@Override
	public List<NodeControlParam> getNodeControlParamByProcessId(
			String processKey, String ProcessId) throws Exception {

		List<NodeControlParam> list = null;

		TransactionManager tm = new TransactionManager();

		try {
			tm.begin();

			// 控制参数查归档表
			list = executor.queryList(NodeControlParam.class,
					"getHiNodeControlParamByProcessId_wf", ProcessId);

			if (list == null || list.size() == 0) {

				// 归档表没数据在查实时表
				list = executor.queryList(NodeControlParam.class,
						"getNodeControlParamByProcessId_wf", ProcessId);
			}

			if (list != null && list.size() > 0) {
				// 判断串并行
				List<ActivityImpl> activties = activitiService
						.getActivitImplListByProcessKey(processKey);

				for (int i = 0; i < list.size(); i++) {
					NodeControlParam param = list.get(i);

					for (ActivityImpl activtie : activties) {
						if (activtie.getId().equals(param.getNODE_KEY())) {

							if (activtie.isMultiTask()) {
								param.setIS_MULTI_DEFAULT(1);
								param.setIS_MULTI(1);
							} else {
								param.setIS_MULTI_DEFAULT(0);
							}

							// 邮件任务
							if (activtie.isMailTask()) {
								param.setNODE_TYPE("mailTask");
							}

							break;
						}
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
	public RejectLog getRejectlog(String taskId) throws Exception {
		return executor.queryObject(RejectLog.class, "getRejectlog_wf", taskId);
	}

	@Override
	public List<TaskManager> getUserNoDealTasks(String userId) throws Exception {
		return executor.queryList(TaskManager.class,
				"selectUserNoDealTasks_wf", userId, userId);
	}

	@Override
	public ListInfo queryDelegateTasksLogData(DelegateTaskLog delegateTaskLog,
			long offset, int pagesize) throws Exception {

		if (StringUtil.isNotEmpty(delegateTaskLog.getProcessKey())) {
			delegateTaskLog.setProcessKey("%" + delegateTaskLog.getProcessKey()
					+ "%");
		}

		if (StringUtil.isNotEmpty(delegateTaskLog.getFromUser())) {
			delegateTaskLog.setFromUser("%" + delegateTaskLog.getFromUser()
					+ "%");
		}

		if (StringUtil.isNotEmpty(delegateTaskLog.getToUser())) {
			delegateTaskLog.setToUser("%" + delegateTaskLog.getToUser() + "%");
		}

		ListInfo listInfo = executor.queryListInfoBean(DelegateTaskLog.class,
				"selectDelegateTasksLogData_wf", offset, pagesize,
				delegateTaskLog);

		return listInfo;
	}

	@Override
	public void delegateTasks(String processKey, String fromuser,
			String touser, String startUser) throws Exception {
		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();

			String currentUser = AccessControl.getAccessControl()
					.getUserAccount();

			Map<String, Object> params = new HashMap<String, Object>();
			String[] pkeys = processKey.equals("") ? null : processKey.split(",");
			params.put("processKeys",
					pkeys);
			params.put("fromuser", fromuser);
			params.put("startUser", startUser);

			// 获取用户未处理的任务
			List<TaskManager> userTaskList = executor.queryListBean(
					TaskManager.class, "getDelegateTaskBycondion_wf", params);

			if (null != userTaskList && userTaskList.size() > 0) {

				// 日志记录
				String reamrk = "["
						+ activitiService.getUserInfoMap().getUserName(
								currentUser)
						+ "]将["
						+ activitiService.getUserInfoMap()
								.getUserName(fromuser) + "]任务转派给["
						+ activitiService.getUserInfoMap().getUserName(touser)
						+ "]";

				for (int i = 0; i < userTaskList.size(); i++) {

					TaskManager task = userTaskList.get(i);

					// 未签收
					if ("1".equals(task.getState())) {
						// 未签收(直接修改未签收的任务的处理人为touser)
						executor.update("updateNoSignUserId_wf", touser,
								fromuser, task.getID_());

						// 在扩展表中添加转派记录
						updateNodeChangeInfo(task.getID_(),
								task.getPROC_INST_ID_(), task.getKEY_(),
								fromuser, touser, reamrk, "", 1);

					} else if ("2".equals(task.getState())) {

						// 已签收
						// activitiService.delegateTask(task.getID_(), touser);
						executor.update("updateActHiTaskinstSignUserId_wf",
								touser, fromuser, task.getID_());
						executor.update("updateActHiActinstSignUserId_wf",
								touser, fromuser, task.getID_());
						executor.update("updateActRuTaskSignUserId_wf", touser,
								fromuser, task.getID_());

						// 在扩展表中添加转派记录
						updateNodeChangeInfo(task.getID_(),
								task.getPROC_INST_ID_(), task.getKEY_(),
								fromuser, touser, reamrk, "", 2);
					}

				}
			}

			this.activitiService.changeTasksToWithTrigger(fromuser, touser, startUser, pkeys);
			tm.commit();
		} catch (Exception e) {
			throw new ProcessException(e);
		} finally {
			tm.release();
		}

	}

	@Override
	public ActivitiNodeInfo getFirstUserNode(String processKey)
			throws Exception {

		ActivitiNodeInfo nodeInfo = executor.queryObject(
				ActivitiNodeInfo.class, "getFirstUserNode_wf", processKey);

		if (null == nodeInfo) {
			nodeInfo = executor.queryObject(ActivitiNodeInfo.class,
					"getDefaultFirstUserNode_wf", processKey);
		}

		return nodeInfo;
	}

	@Override
	public void delVariable(String variableId) throws Exception {
		executor.delete("delVariable_wf", variableId);
	}

	@Override
	public ListInfo getUserCopyTasks(String process_key, String businesskey,
			long offset, int pagesize) throws Exception {

		TransactionManager tm = new TransactionManager();

		try {
			tm.begin();

			boolean isAdmin = AccessControl.getAccessControl().isAdmin();

			ListInfo copyTaskList = null;

			if (!isAdmin) {

				String user = AccessControl.getAccessControl().getUserAccount();
				String orgid = (String) UserCacheManager.getInstance()
						.getUserAttribute(user, "mainOrg");
				Organization org = OrgCacheManager.getInstance()
						.getOrganization(orgid);
				String orgtreelevel = org.getOrgtreelevel();

				String[] arrayOrg = orgtreelevel.split("\\|");
				List<String> orgs = new ArrayList<String>();
				for (int i = arrayOrg.length - 1; i >= 0; i--) {
					if (!arrayOrg[i].equals("0")) {
						orgs.add(arrayOrg[i]);
					}
				}

				copyTaskList = activitiService.getTaskService()
						.getUserCopyTasks(user, orgs, process_key, businesskey,
								offset, pagesize);
			} else {
				copyTaskList = activitiService.getTaskService()
						.getAdminCopyTasks(process_key, businesskey, offset,
								pagesize);
			}

			// 转中文名
			List<CopyTaskEntity> copylist = copyTaskList.getDatas();
			if (copylist != null && copylist.size() > 0) {
				for (int i = 0; i < copylist.size(); i++) {
					CopyTaskEntity copyTask = copylist.get(i);
					// 用户
					if (copyTask.getCopertype() == 0) {
						copyTask.setCoperCNName(activitiService
								.getUserInfoMap().getUserName(
										copyTask.getCoper()));

					} else {
						// 部门
						Organization org = OrgCacheManager.getInstance()
								.getOrganization(copyTask.getCoper());
						if (org != null) {
							copyTask.setCoperCNName(org.getOrgName());
						}
					}

				}
			}
			tm.commit();
			return copyTaskList;

		} catch (Exception e) {
			throw new ProcessException(e);
		} finally {
			tm.release();
		}
	}

	@Override
	public ListInfo getUserReaderCopyTasks(String process_key,
			String businesskey, long offset, int pagesize) throws Exception {

		TransactionManager tm = new TransactionManager();

		try {
			tm.begin();

			boolean isAdmin = AccessControl.getAccessControl().isAdmin();

			ListInfo hiCopyTaskList = null;

			if (!isAdmin) {

				String user = AccessControl.getAccessControl().getUserAccount();

				hiCopyTaskList = activitiService.getTaskService()
						.getUserReaderCopyTasks(user, process_key, businesskey,
								offset, pagesize);

			} else {
				hiCopyTaskList = activitiService.getTaskService()
						.getAdminUserReaderCopyTasks(process_key, businesskey,
								offset, pagesize);
			}

			tm.commit();
			return hiCopyTaskList;

		} catch (Exception e) {
			throw new ProcessException(e);
		} finally {
			tm.release();
		}
	}

	@Override
	public PageData getUserPageList(String assigneeName, int limit)
			throws Exception {

		HashMap<String, String> param = new HashMap<String, String>();

		if (StringUtil.isNotEmpty(assigneeName)) {
			param.put("assignee", assigneeName + "%");
		}

		ListInfo listInfo = executor.queryListInfoBean(User.class,
				"getUserPageList_wf", 0, limit, param);

		PageData pageData = new PageData();
		if (null != listInfo) {
			pageData.setDatas(listInfo.getDatas());
			pageData.setTotalsize(listInfo.getTotalSize());
		}

		return pageData;
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
	public TaskInfo getCurrentNodeInfoByKey(String bussinesskey,
			String processKey, String userId) throws Exception {

		TransactionManager tm = new TransactionManager();

		try {
			tm.begin();

			TaskInfo taskInfo = null;

			// 根据Key获取流程实例
			ProcessInst inst = executor.queryObject(ProcessInst.class,
					"getProcessByKey_wf", bussinesskey, processKey);

			taskInfo = _getCurrentNodeInfoByProcessInstanceid(inst, userId);

			tm.commit();

			return taskInfo;

		} catch (Exception e) {
			throw new Exception("根据key获取当前任务节点信息出错:", e);
		} finally {
			tm.release();
		}
	}

	private TaskInfo _getCurrentNodeInfoByProcessInstanceid(ProcessInst inst,
			String userId) throws Exception {

		TaskInfo taskInfo = null;
		try {
			// 当前用户转成域账号
			userId = this.changeToDomainAccount(userId);

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

				if (StringUtil.isNotEmpty(nowTaskId)) {
					// 当前任务节点信息
					taskInfo = getCurrentNodeInfo(nowTaskId);
				}

			}

			return taskInfo;

		} catch (Exception e) {
			throw new Exception("根据key获取当前任务节点信息出错:", e);
		}
	}

	@Override
	public TaskInfo getCurrentNodeInfoByProcessInstanceid(
			String processinstanceid, String userId) throws Exception {

		TransactionManager tm = new TransactionManager();

		try {
			tm.begin();
			TaskInfo taskInfo = null;
			// 根据Key获取流程实例
			ProcessInst inst = executor.queryObject(ProcessInst.class,
					"getProcessByProcessId_wf", processinstanceid);
			taskInfo = _getCurrentNodeInfoByProcessInstanceid(inst, userId);

			tm.commit();

			return taskInfo;

		} catch (Exception e) {
			throw new Exception("根据key获取当前任务节点信息出错:", e);
		} finally {
			tm.release();
		}
	}
	public String getAssignees(String PROC_INST_ID_,String task_id,String task_key) throws SQLException
	{
		String v = task_key+"_users";
		return executor.queryObject(String.class,"getAssignees", PROC_INST_ID_,v);
		
		
	}
	
	private String convertNamestoworknos(String names)
	{
		String[] _names = names.split(",");
		StringBuilder b = new StringBuilder();
		for(String name:_names)
		{
			CheckCallBack user = UserCacheManager.getInstance().getUser(name);
			if(b.length() > 0)
				b.append(",");
			b.append(user.getUserAttribute("userName")+ "("+user.getUserAttribute("userWorknumber")+")");
			
		}
		return b.toString();
	}
	public int refreshTodoList(String processID,String lastOp, String lastOper) throws ProcessException
	{
		TransactionManager tm = new TransactionManager();
		String startUser = "";
		try
		{
			tm.begin();
			 startUser = executor.queryObject(String.class,
					"getProcessStarter", processID);
			List<WfRunTask> tasks = getTodoList(processID,startUser,lastOp, this.activitiService.getUserInfoMap().getUserName(lastOper));
			executor.delete("deleteWfRunTaskByKey", processID);
			 
			if(tasks != null && tasks.size() > 0)
			{
				executor.insertBeans("addWfRunTask", tasks);
			}
			tm.commit();
			if(tasks != null && tasks.size() > 0)
				return TaskTrigger.refresh_task_success;
			else
				return TaskTrigger.refresh_task_notask;
		}
		catch(ProcessException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new ProcessException("processID:"+processID+"startUser:"+startUser,e);
		}
		finally
		{
			tm.release();
		}
		
	}
	
	
	
	public List<WfRunTask> getTodoList(String processID,String startUser,String lastOp, String lastOper) throws ProcessException
	{
		if (StringUtil.isEmpty(processID)) {
			return null;
		}
		List<WfRunTask> taskInfos = null;
		List<WfRunTask> rettaskInfos = new ArrayList<WfRunTask>();
		
		Map<String,String> taskassigens = new HashMap<String,String>();
		try {
			taskInfos = executor.queryList(WfRunTask.class,
					"getRunTaskInfoByPINSTID", processID);//查询工作流runtask表中的数据
			 
			// 处理人行转列
			if (null != taskInfos && taskInfos.size() > 0) {	
				List<WfRunTask> etaskInfos = new ArrayList<WfRunTask>();
				for(WfRunTask taskInfo:taskInfos)
				{
					String assigne = taskassigens.get(taskInfo.getTaskKey());
					if(assigne == null)
					{
						assigne = this.getAssignees(processID, taskInfo.getTaskId(), taskInfo.getTaskKey());
						taskassigens.put(taskInfo.getTaskKey(), assigne);
					}
					if (StringUtil.isNotEmpty(taskInfo.getDealer())) {
	//				taskInfo.setAssigneeName(activitiService.getUserInfoMap()
	//						.getUserName(taskInfo.getAssignee()));
						
						CheckCallBack user = UserCacheManager.getInstance().getUser(taskInfo.getDealer());
						taskInfo.setDealerName((String)user.getUserAttribute("userName"));
						taskInfo.setDealerWorkno((String)user.getUserAttribute("userWorknumber"));
						user = UserCacheManager.getInstance().getUser(startUser);
						taskInfo.setSender(startUser);
						taskInfo.setSenderName((String)user.getUserAttribute("userName"));
						taskInfo.setSenderWorkno((String)user.getUserAttribute("userWorknumber"));
						taskInfo.setDealers(assigne);
						taskInfo.setDealerNames(convertNamestoworknos(assigne));	
						taskInfo.setLastOp(lastOp);
						taskInfo.setLastOper(lastOper);
						rettaskInfos.add(taskInfo);
						handDelegateTasks(  rettaskInfos, etaskInfos, taskInfo);
					} else {
						// 任务未签收，根据任务id查询任务可处理人
	//				List<HashMap> candidatorList = executor.queryList(
	//						HashMap.class, "getCandidatorOftask_wf",
	//						taskInfo.getTaskId());
	
						/**
						 * B.BUSINESS_KEY_ AS BUSINESSKEY,
					       C.PROCESS_KEY   AS PROCESSKEY,
					         C.IS_RECALL     AS ISRECALL,
					       C.IS_DISCARD    AS ISDISCARD,
					       C.IS_DISCARDED  AS ISDISCARDED,
					       C.IS_CANCEL     AS ISCANCEL,
					       C.IS_COPY       AS ISCOPY,
					       C.IS_AUTOAFTER  AS ISAUTOAFTER,
					       C.TASK_URL      AS TASKURL
						 */
	//				StringBuilder users = new StringBuilder();
	//				StringBuilder userNames = new StringBuilder();
						List<WfRunTask> tasks = executor.queryList(WfRunTask.class, "getTaskInfosByTaskId", taskInfo.getTaskId());
						if(tasks == null || tasks.size() == 0)
						{
							return null;
						}
						for(WfRunTask task:tasks)
						{
							CheckCallBack user = UserCacheManager.getInstance().getUser(task.getDealer());
//							task.setDealer(task.getAssignee());
							task.setDealerName((String)user.getUserAttribute("userName"));
							task.setDealerWorkno((String)user.getUserAttribute("userWorknumber"));
							user = UserCacheManager.getInstance().getUser(startUser);
							task.setSender(startUser);
							task.setSenderName((String)user.getUserAttribute("userName"));
							task.setSenderWorkno((String)user.getUserAttribute("userWorknumber"));
							task.setTaskUrl(taskInfo.getTaskUrl());
							task.setBusinessKey(taskInfo.getBusinessKey());
							task.setProcessKey(taskInfo.getProcessKey());
							
							task.setDealers(assigne);
							task.setDealerNames(convertNamestoworknos(assigne));	
							task.setLastOp(lastOp);
							task.setLastOper(lastOper);
							rettaskInfos.add(task);
							handDelegateTasks(  rettaskInfos, etaskInfos, task);
						}
	
	
						
					}
				}
				rettaskInfos.addAll(etaskInfos);
			}
		} catch (SQLException e) {
			throw new ProcessException("processID:"+processID+"startUser:"+startUser,e);
		}
		catch (Exception e) {
			throw new ProcessException("processID:"+processID+"startUser:"+startUser,e);
		}

		return rettaskInfos;
	}
	
	private boolean hasTask(List<WfRunTask> taskInfos,String user,String taskid)
	{
		if(taskInfos == null || taskInfos.size() == 0)
			return false;
		for(WfRunTask task:taskInfos)
		{
			if(task.getDealer().equals(user) && task.getTaskId().equals(taskid))
				return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param rettaskInfos
	 * @param taskInfo
	 */
	private void handDelegateTasks(List<WfRunTask> taskInfos,List<WfRunTask> rettaskInfos ,WfRunTask taskInfo) throws Exception
	{
//		String validateentrust = "select ENTRUST_USER,CREATE_USER ,START_DATE,END_DATE from TD_WF_ENTRUST where CREATE_USER=? and sts='有效'";
		List<WfEntrust> WfEntrusts = executor.queryList(WfEntrust.class, "getDelegateRelation_wf", taskInfo.getDealer());
		if(WfEntrusts != null && WfEntrusts.size() > 0)
		{
			Date now = new Date();
			for(WfEntrust WfEntrust:WfEntrusts)
			{
				if(WfEntrust.getWf_entrust_type().equals("全部委托") || 
						(WfEntrust.getWf_entrust_type().equals("选择流程委托") && taskInfo.getProcessKey().equals(WfEntrust.getProcdefId())))
				{
					boolean need = false;
					if(WfEntrust.getStart_date() != null && WfEntrust.getEnd_date() != null)
					{
						if(now.before(WfEntrust.getEnd_date()) && now.after(WfEntrust.getStart_date()))
						{
							need = true;
						}
						
					}
					else if(WfEntrust.getStart_date() == null && WfEntrust.getEnd_date() == null)
					{						
						need = true;
					}
					else if(WfEntrust.getStart_date() == null )
					{
						if(now.before(WfEntrust.getEnd_date()) )
						{							 
							need = true;
						}
					}
					else if(WfEntrust.getEnd_date() == null )
					{
						if(now.after(WfEntrust.getStart_date()) )
						{
							need = true;							 
						}
					}
					if(need && !hasTask(taskInfos,WfEntrust.getEntrust_user(),taskInfo.getTaskId()))
					{
						WfRunTask etaskInfo =  (WfRunTask)taskInfo.clone();
						etaskInfo.setDealer(WfEntrust.getEntrust_user());
						CheckCallBack user = UserCacheManager.getInstance().getUser(WfEntrust.getEntrust_user());
						etaskInfo.setDealerName((String)user.getUserAttribute("userName"));
						etaskInfo.setDealerWorkno((String)user.getUserAttribute("userWorknumber"));
						etaskInfo.setAUTH_ENDTIME(WfEntrust.getEnd_date());
						etaskInfo.setAUTH_STARTTIME(WfEntrust.getStart_date());
						user = UserCacheManager.getInstance().getUser(WfEntrust.getCreate_user());
						etaskInfo.setFROMUSER(WfEntrust.getCreate_user());
						etaskInfo.setFROMUSERNAME((String)user.getUserAttribute("userName"));
						etaskInfo.setENTRUST_ID(WfEntrust.getId());	
						etaskInfo.setLastOp("委托授权");
						etaskInfo.setTaskType(2);//将任务设置为委托授权任务							
						rettaskInfos.add(etaskInfo);
					}
				}
			}
		}
	}
	
	/**
	 * 刷新委托授权时影响的用户的委托授权信息
	 * @param entrustid 委托授权id
	 * @param lastOp
	 * @param userAccount
	 * @throws Exception
	 */
	public void refreshEntrustTodoList(String entrustid, String lastOp,
			String userAccount) throws Exception
	{
//		String validateentrust = "select ENTRUST_USER,CREATE_USER ,START_DATE,END_DATE from TD_WF_ENTRUST where id=? and sts='有效'";
		WfEntrust WfEntrust = executor.queryObject(WfEntrust.class, "validateentrust", entrustid);
		boolean validate = false;
		if(WfEntrust != null )
		{
			Date now = new Date();
			
			
			if(WfEntrust.getStart_date() != null)
			{
				if(WfEntrust.getEnd_date() != null)
				{
					if(now.before(WfEntrust.getEnd_date()))//有效
					{
						validate = true;
					}
					else//无效
					{
						validate = false;
					}
					
				}
				else//有效
				{
					validate = true;
				}
			}
			else//有效
			{
				validate = true;
			}
			
		}
		else//无效
		{
			validate = false;
		}
		executor.delete("deleteentrusttasks", entrustid);
		if(validate)
		{
			List<String> processKey = executor.queryList(String.class, "selectentrustprocesskeys", entrustid);
			List<WfRunTask> tasks = null;
			if(processKey == null || processKey.size() == 0)//所有流程都委托
			{
				tasks = executor.queryList(WfRunTask.class, "getentrusttasks",WfEntrust.getCreate_user(), WfEntrust.getEntrust_user());//排除被委托人已经存在的任务
				
			}
			else //选择流程委托
			{
//				tasks = SQLExecutor.queryList(WfRunTask.class, "select * from TD_WF_RUN_TASK t1 where t1.task_type=0 and t1.DEALER=? and PROCESS_KEY in  tasks --补充处理"
//						+ "not exist select 1 from TD_WF_RUN_TASK t2 where t2.task_type=0 and t2.DEALER=? and  t1.TASK_ID=t2.TASK_ID", WfEntrust.getCreate_user(),WfEntrust.getEntrust_user());//排除被委托人已经存在的任务
				Map params = new HashMap();
				params.put("fromUser", WfEntrust.getCreate_user());
				params.put("toUser", WfEntrust.getEntrust_user());
				params.put("processKeys", processKey);
				tasks = this.executor.queryListBean(WfRunTask.class, "getentrusttasksWithProcessKeys", params);
						
				
			}
			if(tasks != null && tasks.size() > 0)
			{
				for(WfRunTask task: tasks)
				{
					CheckCallBack user = UserCacheManager.getInstance().getUser(WfEntrust.getEntrust_user());
					task.setDealer(WfEntrust.getEntrust_user());
					task.setDealerName((String)user.getUserAttribute("userName"));
					task.setDealerWorkno((String)user.getUserAttribute("userWorknumber"));
					task.setAUTH_ENDTIME(WfEntrust.getEnd_date());
					task.setAUTH_STARTTIME(WfEntrust.getStart_date());
					user = UserCacheManager.getInstance().getUser(WfEntrust.getCreate_user());
					task.setFROMUSER(WfEntrust.getCreate_user());
					task.setFROMUSERNAME((String)user.getUserAttribute("userName"));
					task.setENTRUST_ID(WfEntrust.getId());
					task.setLastOper((String)UserCacheManager.getInstance().getUserAttribute(userAccount, "userName"));
					task.setLastOp("委托授权");
					task.setTaskType(2);//将任务设置为委托授权任务
				}
				executor.insertBeans("addWfRunTask", tasks);
			}
		}

	}
	
	@Override
	public TaskInfo getCurrentNodeInfo(String taskId) throws Exception {

		if (StringUtil.isEmpty(taskId)) {
			return null;
		}

		TaskInfo taskInfo = executor.queryObject(TaskInfo.class,
				"getTaskInfoByTaskId_wf_forautocomplete", taskId);

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

				StringBuilder users = new StringBuilder();
				StringBuilder userNames = new StringBuilder();

				if (candidatorList != null && candidatorList.size() != 0) {

					for (int k = 0; k < candidatorList.size(); k++) {
						HashMap candidatorMap = candidatorList.get(k);

						String userId = (String) candidatorMap.get("USER_ID_");
						if (StringUtil.isNotEmpty(userId)) {

							if (k == 0) {
								users.append(userId);
								userNames.append(activitiService
										.getUserInfoMap().getUserName(userId));
							} else {
								users.append(",").append(userId);
								userNames.append(",").append(
										activitiService.getUserInfoMap()
												.getUserName(userId));
							}
						}
					}
					taskInfo.setAssigneeName(userNames.toString());
					taskInfo.setAssignee(users.toString());
				}
			}
		}

		return taskInfo;
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
			List<String> candidatorList = null;
			// 首先判断任务是否有没签收，如果签收，以签收人为准，如果没签收，则以该节点配置的人为准
			TaskManager task = executor.queryObject(TaskManager.class,
					"getHiTaskIdByTaskId", taskId);
			String assigner = task.getASSIGNEE_();
			String processkey = task.getPROC_DEF_ID_().substring(0,task.getPROC_DEF_ID_().indexOf(':'));
			if (StringUtil.isNotEmpty(assigner)) {

				if (userAccount.equals(assigner)) {

					haspermission = true;
				}

			} else {
				// 任务未签收，根据任务id查询任务可处理人
				candidatorList = executor.queryList(
						String.class, "getNodeCandidates_wf", taskId);

				if (candidatorList != null && candidatorList.size() > 0 && candidatorList.contains(userAccount)) {

					haspermission = true;
				}
			}

			if (!haspermission) {
				// 最后查看当前用户的委托关系
				List<WfEntrust> entrustList = executor.queryList(
						WfEntrust.class, "getEntrustRelation_wf", userAccount);
				Date now = new Date();
				if (entrustList != null && entrustList.size() > 0) {
					for(WfEntrust WfEntrust:entrustList)
					{
						if((StringUtil.isNotEmpty(assigner) && WfEntrust.getCreate_user().equals(assigner)) 
								|| (candidatorList != null && candidatorList.size() > 0 && candidatorList.contains(WfEntrust.getCreate_user()))
								)
						{
							
							if(WfEntrust.getWf_entrust_type().equals("全部委托") || 
									(WfEntrust.getWf_entrust_type().equals("选择流程委托") && processkey.equals(WfEntrust.getProcdefId())))
							{
								if(WfEntrust.getStart_date() != null && WfEntrust.getEnd_date() != null)
								{
									if(now.before(WfEntrust.getEnd_date()) && now.after(WfEntrust.getStart_date()))
									{
										haspermission = true;
										break;
									}
									
								}
								else if(WfEntrust.getStart_date() == null && WfEntrust.getEnd_date() == null)
								{
									haspermission = true;
									break;
								}
								else if(WfEntrust.getStart_date() == null )
								{
									if(now.before(WfEntrust.getEnd_date()) )
									{
										haspermission = true;
										break;
									}
								}
								else if(WfEntrust.getEnd_date() == null )
								{
									if(now.after(WfEntrust.getStart_date()) )
									{
										haspermission = true;
										break;
									}
								}
							}
							
						}
					}
					
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

		}
		 catch (ProcessException e) {
				throw e;
			} 
		catch (Exception e) {
			throw new ProcessException(e);
		} finally {
			tm.release();
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
			throw new Exception("根据业务key获取当前任务节点信息出错:", e);
		} finally {
			tm.release();
		}
	}

	public void autoCompleteTask(String dealOption, String dealRemak,
			String dealReason, String processInstanceID, String currentUser)
			throws Exception {
		TaskInfo task = getCurrentNodeInfoByProcessInstanceid(
				processInstanceID, currentUser);
		autoCompleteTask(task, dealOption, dealRemak, dealReason,
				processInstanceID, currentUser);
	}

	@Override
	public boolean isSignTask(String taskId, String userId) throws Exception {
		return activitiService.isSignTask(taskId, userId);
	}

	/**
	 * 自动完成任务
	 * 
	 * @param proIns
	 * @throws Exception
	 *             2014年11月20日
	 */
	public void autoCompleteTask(TaskInfo task, String dealOption,
			String dealRemak, String dealReason, String processInstanceID,
			String currentUser) throws Exception {

		// 获取流程当前任务节点
		// TaskInfo task =
		// getCurrentNodeInfoByProcessInstanceid(processInstanceID,
		// currentUser);

		if (null != task) {

			// if (StringUtil.isEmpty(proIns.getDealOption())) {
			// proIns.setDealOption("提交任务");
			// }

			if (StringUtil.isEmpty(dealRemak)) {
				dealRemak = "["
						+ activitiService.getUserInfoMap().getUserName(
								currentUser) + "]的任务被自动完成";
				// proIns.setDealRemak(remark);
			}

			if (!isSignTask(task.getTaskId(), task.getAssignee())) {
				// 先签收
				activitiService.claim(task.getTaskId(), task.getAssignee());
			}

			// 完成任务
			activitiService.completeTaskWithReason(task.getTaskId(), null,
					dealRemak, dealOption, dealReason, true);

			// 后续节点自动审批
			if (task.getIsAutoafter() == 1
					&& task.getAssignee().equals(currentUser)) {

				// // 过滤流程开启自动通过第一个任务的处理意见
				// if (null == proIns.getOperateType()) {
				// // proIns.setOperateType("pass");
				// dealReason = "前后任务处理人一致，自动通过";
				// }
				autoCompleteTask(dealOption, dealRemak, dealReason,
						processInstanceID, currentUser);
			}
		}

	}

	@Override
	public void udpNodeAssignee(List<ActivitiNodeInfo> nodeList,
			String processId) throws Exception {

		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();

			// 普通节点List
			List<Map<String, String>> commonNodeList = new ArrayList<Map<String, String>>();

			// 抄送或通知节点List
			List<Map<String, String>> noticeNodeList = new ArrayList<Map<String, String>>();

			// 筛选节点
			if (nodeList != null && nodeList.size() > 0) {
				for (ActivitiNodeInfo node : nodeList) {
					if (node.getIs_copy() == 0) {// 普通节点
						Map<String, String> map = new HashMap<String, String>();
						map.put("nodeDealers", node.getNode_users_id());
						map.put("nodeKey", node.getNode_key() + "_users");
						map.put("processId", processId);
						commonNodeList.add(map);
					} else {
						// 抄送或通知节点

						if (StringUtil.isNotEmpty(node.getNode_users_id())
								&& StringUtil
										.isNotEmpty(node.getNode_orgs_id())) {
							node.setCopyerscnname(node.getNode_users_name()
									+ "," + node.getNode_orgs_name());

						} else if (StringUtil.isEmpty(node.getNode_users_id())
								&& StringUtil
										.isNotEmpty(node.getNode_orgs_id())) {
							node.setCopyerscnname(node.getNode_orgs_name());

						} else if (StringUtil.isNotEmpty(node
								.getNode_users_id())
								&& StringUtil.isEmpty(node.getNode_orgs_id())) {
							node.setCopyerscnname(node.getNode_users_name());
						} else {
							node.setCopyerscnname("");
						}

						Map<String, String> map = new HashMap<String, String>();
						map.put("copyUsers", node.getNode_users_id());
						map.put("copyOrgs", node.getNode_orgs_id());
						map.put("copyersCnname", node.getCopyerscnname());
						map.put("nodeKey", node.getNode_key());
						map.put("processId", processId);
						noticeNodeList.add(map);
					}
				}
			}

			// 修改普通节点处理人信息
			executor.updateBeans("udpNodeAssignee_wf", commonNodeList);

			// 修改抄送、通知节点处理人信息
			executor.updateBeans("udpNodeWorktime_wf", noticeNodeList);

			tm.commit();

		} catch (Exception e) {
			throw new Exception("修改未产生待办任务的节点处理人出错:", e);
		} finally {
			tm.release();
		}
	}

}
