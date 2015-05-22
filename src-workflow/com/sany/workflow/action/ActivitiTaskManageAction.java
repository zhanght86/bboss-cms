package com.sany.workflow.action;

import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.task.Task;
import org.frameworkset.util.annotations.PagerParam;
import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.web.servlet.ModelMap;

import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.platform.cms.util.StringUtil;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.util.ListInfo;
import com.sany.workflow.business.entity.TaskInfo;
import com.sany.workflow.entity.ActivitiNodeInfo;
import com.sany.workflow.entity.ActivitiVariable;
import com.sany.workflow.entity.DelegateTaskLog;
import com.sany.workflow.entity.NodeControlParam;
import com.sany.workflow.entity.Nodevariable;
import com.sany.workflow.entity.PageData;
import com.sany.workflow.entity.ProcessInst;
import com.sany.workflow.entity.RejectLog;
import com.sany.workflow.entity.TaskCondition;
import com.sany.workflow.entity.TaskManager;
import com.sany.workflow.entrust.entity.WfEntrust;
import com.sany.workflow.service.ActivitiService;
import com.sany.workflow.service.ActivitiTaskService;
import com.sany.workflow.service.ProcessException;
import com.sany.workflow.util.WorkFlowConstant;

/**
 * @todo 工作流任务管理模块
 * @author tanx
 * @date 2014年5月7日
 * 
 */
public class ActivitiTaskManageAction {

	private ActivitiService activitiService;

	private ActivitiTaskService activitiTaskService;

	/**
	 * 跳转至实时任务管理页面
	 * 
	 * @param processKey
	 * @param model
	 * @return 2014年5月7日
	 */
	public String ontimeTaskManager(String processKey, ModelMap model) {

		try {

			boolean isAdmin = AccessControl.getAccessControl().isAdmin();
			String currentAccount = AccessControl.getAccessControl()
					.getUserAccount();

			model.addAttribute("currentAccountName",
					activitiService.userIdToUserName(currentAccount, "2"));

			model.addAttribute("currentAccount", currentAccount);

			model.addAttribute("processKey", processKey);

			model.addAttribute("isAdmin", isAdmin);

			return "path:ontimeTaskManager";

		} catch (Exception e) {
			throw new ProcessException(e);
		}
	}

	/**
	 * 跳转至历史任务管理页面
	 * 
	 * @param processKey
	 * @param model
	 * @return 2014年6月18日
	 */
	public String historyTaskManager(String processKey, ModelMap model) {

		try {

			boolean isAdmin = AccessControl.getAccessControl().isAdmin();
			String currentAccount = AccessControl.getAccessControl()
					.getUserAccount();

			model.addAttribute("processKey", processKey);
			model.addAttribute("isAdmin", isAdmin);
			model.addAttribute("currentAccount", currentAccount);

			return "path:historyTaskManager";

		} catch (Exception e) {
			throw new ProcessException(e);
		}
	}

	/**
	 * 加载历史任务数据
	 * 
	 * @param sortKey
	 * @param desc
	 * @param offset
	 * @param pagesize
	 * @param task
	 * @param model
	 * @return 2014年6月18日
	 */
	public String queryHistoryTaskData(
			@PagerParam(name = PagerParam.SORT, defaultvalue = "") String sortKey,
			@PagerParam(name = PagerParam.DESC, defaultvalue = "false") boolean desc,
			@PagerParam(name = PagerParam.OFFSET) long offset,
			@PagerParam(name = PagerParam.PAGE_SIZE, defaultvalue = "10") int pagesize,
			TaskCondition task, ModelMap model) {

		try {
			ListInfo listInfo = activitiTaskService.queryHistoryTasks(task,
					offset, pagesize);

			Map<Integer, String> advanceSendMap = WorkFlowConstant
					.getAdvanceSend();

			Map<Integer, String> overtimeSendMap = WorkFlowConstant
					.getOvertimeSend();

			model.addAttribute("advanceSendMap", advanceSendMap);
			model.addAttribute("overtimeSendMap", overtimeSendMap);
			model.addAttribute("listInfo", listInfo);

			return "path:historyTaskList";

		} catch (Exception e) {
			throw new ProcessException(e);
		}

	}

	/**
	 * 加载实时任务数据
	 * 
	 * @param sortKey
	 * @param desc
	 * @param offset
	 * @param pagesize
	 * @param processDefCondition
	 * @param model
	 * @return 2014年5月7日
	 */
	public String queryOntimeTaskData(
			@PagerParam(name = PagerParam.SORT, defaultvalue = "") String sortKey,
			@PagerParam(name = PagerParam.DESC, defaultvalue = "false") boolean desc,
			@PagerParam(name = PagerParam.OFFSET) long offset,
			@PagerParam(name = PagerParam.PAGE_SIZE, defaultvalue = "10") int pagesize,
			TaskCondition task, ModelMap model) {

		try {

			// 实时任务
			ListInfo listInfo = activitiService.queryTasks(task, offset,
					pagesize);

			Map<Integer, String> advanceSendMap = WorkFlowConstant
					.getAdvanceSend();

			Map<Integer, String> overtimeSendMap = WorkFlowConstant
					.getOvertimeSend();

			model.addAttribute("advanceSendMap", advanceSendMap);
			model.addAttribute("overtimeSendMap", overtimeSendMap);
			model.addAttribute("listInfo", listInfo);

			return "path:ontimeTaskList";

		} catch (Exception e) {
			throw new ProcessException(e);
		}

	}

	/**
	 * 加载委托任务数据
	 * 
	 * @param sortKey
	 * @param desc
	 * @param offset
	 * @param pagesize
	 * @param task
	 * @param model
	 * @return 2014年7月10日
	 */
	public String queryEntrustTaskData(
			@PagerParam(name = PagerParam.SORT, defaultvalue = "") String sortKey,
			@PagerParam(name = PagerParam.DESC, defaultvalue = "false") boolean desc,
			@PagerParam(name = PagerParam.OFFSET) long offset,
			@PagerParam(name = PagerParam.PAGE_SIZE, defaultvalue = "10") int pagesize,
			TaskCondition task, ModelMap model) {

		try {

			ListInfo entrustlist = activitiService.queryEntrustTasks(task,
					offset, pagesize);

			Map<Integer, String> advanceSendMap = WorkFlowConstant
					.getAdvanceSend();

			Map<Integer, String> overtimeSendMap = WorkFlowConstant
					.getOvertimeSend();

			model.addAttribute("advanceSendMap", advanceSendMap);
			model.addAttribute("overtimeSendMap", overtimeSendMap);
			model.addAttribute("entrustlist", entrustlist);

			return "path:entrustTaskList";

		} catch (Exception e) {
			throw new ProcessException(e);
		}

	}

	/**
	 * 加载未读抄送任务数据
	 * 
	 * @param sortKey
	 * @param desc
	 * @param offset
	 * @param pagesize
	 * @param process_key
	 * @param businesskey
	 * @param model
	 * @return 2014年11月17日
	 */
	public String queryCopyTaskData(
			@PagerParam(name = PagerParam.SORT, defaultvalue = "") String sortKey,
			@PagerParam(name = PagerParam.DESC, defaultvalue = "false") boolean desc,
			@PagerParam(name = PagerParam.OFFSET) long offset,
			@PagerParam(name = PagerParam.PAGE_SIZE, defaultvalue = "10") int pagesize,
			String process_key, String businesskey, ModelMap model) {

		try {

			ListInfo copyTaskList = activitiTaskService.getUserCopyTasks(
					process_key, businesskey, offset, pagesize);

			model.addAttribute("copyTaskList", copyTaskList);

			return "path:copyTaskList";

		} catch (Exception e) {
			throw new ProcessException(e);
		}

	}

	/**
	 * 加载已读抄送任务数据
	 * 
	 * @param sortKey
	 * @param desc
	 * @param offset
	 * @param pagesize
	 * @param process_key
	 * @param businesskey
	 * @param model
	 * @return 2014年11月17日
	 */
	public String queryHiCopyTaskData(
			@PagerParam(name = PagerParam.SORT, defaultvalue = "") String sortKey,
			@PagerParam(name = PagerParam.DESC, defaultvalue = "false") boolean desc,
			@PagerParam(name = PagerParam.OFFSET) long offset,
			@PagerParam(name = PagerParam.PAGE_SIZE, defaultvalue = "10") int pagesize,
			String process_key, String businesskey, ModelMap model) {

		try {

			ListInfo hiCopyTaskList = activitiTaskService
					.getUserReaderCopyTasks(process_key, businesskey, offset,
							pagesize);

			model.addAttribute("hiCopyTaskList", hiCopyTaskList);

			return "path:hiCopyTaskList";

		} catch (Exception e) {
			throw new ProcessException(e);
		}

	}

	/**
	 * 查看特定流程的任务管理信息
	 * 
	 * @param processKey
	 * @param version
	 * @param model
	 * @return 2014年5月7日
	 */
	public String viewTaskInfo(String processKey, String version, ModelMap model) {
		TransactionManager tm = new TransactionManager();

		try {
			tm.begin();

			model.addAttribute("processDef",
					activitiService.queryProdefByKey(processKey, version));

			List<ActivityImpl> aList = activitiService
					.getActivitImplListByProcessKey(processKey);

			for (int i = 0; i < aList.size(); i++) {
				if (!aList.get(i).getProperty("type").equals("userTask")) {
					aList.remove(i);
				}
			}

			model.addAttribute("aList", aList);
			model.addAttribute("tabNum", 4);// 数字与Tab页位置下标对应

			tm.commit();

			return "path:viewTaskInfo";

		} catch (Exception e) {
			throw new ProcessException(e);
		} finally {
			tm.release();
		}
	}

	/**
	 * 流程明细查看跳转
	 * 
	 * @param processKey
	 * @return 2014年5月7日
	 */
	public String viewTaskDetailInfo(String processInstId, ModelMap model) {
		TransactionManager tm = new TransactionManager();

		try {
			tm.begin();

			// 获取流程实例信息
			ProcessInst processInst = activitiService
					.getProcessInstById(processInstId);
			model.addAttribute("processInst", processInst);

			// 判断是否有撤销功能(当前节点能被撤回，流程实例状态是运行中，当前用户是流程发起人或管理员)
			if (processInst.getTaskList() != null
					&& processInst.getTaskList().size() > 0) {
				String currentUser = AccessControl.getAccessControl()
						.getUserAccount();
				int isRecall = processInst.getTaskList().get(0).getIsRecall();
				if (processInst.getSUSPENSION_STATE_().equals("1")) {
					if ((isRecall == 1 && processInst.getSTART_USER_ID_()
							.equals(currentUser))
							|| AccessControl.getAccessControl().isAdmin()) {
						model.addAttribute("isRecall", 1);
						model.addAttribute("nowTaskId", processInst
								.getTaskList().get(0).getID_());
					}
				}

				// 如果是管理员查看，废弃功能要显示
				if (AccessControl.getAccessControl().isAdmin()) {
					model.addAttribute("isDiscard", 1);
					model.addAttribute("nowTaskId", processInst.getTaskList()
							.get(0).getID_());
				}
			}

			if (processInst != null) {
				// 获取流程实例的处理记录
				List<TaskManager> taskHistorList = activitiService
						.queryHistorTasks(processInstId, processInst.getKEY_());

				model.addAttribute("taskHistorList", taskHistorList);

				// 获取流程所有参数
				List<ActivitiVariable> instVariableList = activitiService
						.getInstVariableInfoById(processInstId);

				model.addAttribute("instVariableList", instVariableList);

				// 获取当前活动级别参数
				Object[] arryObj = activitiService
						.getCurTaskVariableInfoById(processInstId);

				model.addAttribute("taskVariableMap", arryObj[0]);
				model.addAttribute("variableRownum",
						Integer.parseInt(arryObj[1] + "") + 1);// 加标题行
				model.addAttribute("instanceRownum", arryObj[1]);

				// 获取流程实例下所有人工节点控制参数信息
				List<NodeControlParam> controlParamList = activitiTaskService
						.getNodeControlParamByProcessId(processInst.getKEY_(),
								processInstId);
				model.addAttribute("controlParamList", controlParamList);

				// 预警、超时状态转义
				Map<Integer, String> advanceSendMap = WorkFlowConstant
						.getAdvanceSend();
				Map<Integer, String> overtimeSendMap = WorkFlowConstant
						.getOvertimeSend();
				model.addAttribute("advanceSendMap", advanceSendMap);
				model.addAttribute("overtimeSendMap", overtimeSendMap);
			}

			tm.commit();

			return "path:viewTaskDetailInfo";

		} catch (Exception e) {
			throw new ProcessException(e);
		} finally {
			tm.release();
		}
	}

	/**
	 * 签收任务
	 * 
	 * @param processInstId
	 * @param model
	 * @return 2014年5月15日
	 */
	public @ResponseBody
	String signTask(String taskId, String processKey, ModelMap model) {
		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();
			boolean isAuthor = activitiTaskService.judgeAuthority(taskId,
					processKey);

			if (isAuthor) {

				activitiTaskService.signTaskByUser(taskId, AccessControl
						.getAccessControl().getUserAccount());
				tm.commit();
				return "success";
			} else {
				tm.commit();
				return "fail:您没有权限签收当前任务";
			}

		} catch (Exception e) {
			return "fail" + e.getMessage();
		} finally {
			tm.release();
		}
	}

	/**
	 * 处理任务
	 * 
	 * @param taskId
	 * @param taskState
	 * @param nodeList
	 * @param model
	 * @return 2014年5月26日
	 */
	public @ResponseBody
	String completeTask(TaskCondition task,
			List<ActivitiNodeInfo> activitiNodeCandidateList,
			List<Nodevariable> nodevariableList,
			List<NodeControlParam> nodeControlParamList, ModelMap model) {

		TransactionManager tm = new TransactionManager();

		try {
			tm.begin();
			boolean isAuthor = activitiTaskService.judgeAuthority(
					task.getTaskId(), task.getProcessKey());
			String result = "success";
			if (isAuthor) {

				String currentUser = AccessControl.getAccessControl()
						.getUserAccount();
				task.setCurrentUser(currentUser);

				String remark = "";

				// 记录委托关系(有就处理，没有就不处理)
				if (StringUtil.isNotEmpty(task.getCreateUser())
						&& StringUtil.isNotEmpty(task.getEntrustUser())) {

					// 当前用户就是被委托的用户
					if (currentUser.equals(task.getEntrustUser())) {

						activitiTaskService.addEntrustTaskInfo(task);

						remark = "["
								+ activitiService.getUserInfoMap().getUserName(
										task.getCreateUser())
								+ "]的任务委托给["
								+ activitiService.getUserInfoMap().getUserName(
										task.getEntrustUser()) + "]完成";
					}

				} else {
					remark = "["
							+ activitiService.getUserInfoMap().getUserName(
									task.getCurrentUser()) + "]完成";
				}

				task.setCompleteRemark(remark);

				Map<String, Object> variableMap = activitiTaskService
						.getVariableMap(activitiNodeCandidateList,
								nodevariableList, nodeControlParamList);

				// 保存控制变量参数
				activitiService.addNodeWorktime(task.getProcessKey(),
						task.getProcessIntsId(), nodeControlParamList);

				// 完成任务
				activitiTaskService.completeTask(task, variableMap);

				/** 注释掉，需要先保存控制参数，再完成任务 2014-12-31 by biaoping.yin */
				// // 保存控制变量参数
				// activitiService.addNodeWorktime(task.getProcessKey(),
				// task.getProcessIntsId(), nodeControlParamList);
				// 下一个任务处理人相同，自动完成任务逻辑 2014-12-31 by biaoping.yin
				NodeControlParam currrentNodeControlParam = this.activitiService
						.getNodeControlParamByTaskID(task.getProcessIntsId(),
								task.getTaskId());

				if (currrentNodeControlParam != null) {

					if (currrentNodeControlParam.getIS_AUTOAFTER() == 1) {
						// 获取当前任务信息
						TaskInfo nextTask = this.activitiTaskService
								.getCurrentNodeInfoByProcessInstanceid(
										task.getProcessIntsId(), currentUser);

						// 后续节点处理人是否一致，一致就可以自动通过
						if (null != nextTask
								&& currentUser.equals(nextTask.getAssignee())) {

							// 清除上一任务的处理意见和意见备注
							// proIns.setDealRemak("");
							// proIns.setDealReason("前后任务处理人一致，自动通过");
							activitiTaskService.autoCompleteTask(nextTask,
									"pass", "", "前后任务处理人一致，自动通过",
									task.getProcessIntsId(), currentUser);
							// autoCompleteTask(proIns, processKey);
						}
					}
				}

				result = "success";

			} else {

				result = "fail:您没有权限处理当前任务";
			}
			tm.commit();
			return result;

		} catch (Exception e) {
			return "fail" + e.getMessage();
		} finally {
			tm.release();
		}

	}

	/**
	 * 跳转至处理任务页面
	 * 
	 * @param processKey
	 * @param processInstId
	 * @param taskState
	 * @param taskId
	 * @param createUser
	 *            委托人
	 * @param entrustUser
	 *            被委托人
	 * @param model
	 * @return 2014年7月18日
	 */
	public String toDealTask(String processKey, String processInstId,
			String taskState, String taskId, String createUser,
			String entrustUser, String sysid, ModelMap model) {

		TransactionManager tm = new TransactionManager();

		try {
			tm.begin();

			// 当前任务节点信息
			TaskManager task = activitiService.getTaskByTaskId(taskId);
			model.addAttribute("task", task);

			// 判断当前任务是否存在
			if (null == task) {
				throw new ProcessException("任务不存在");
			}

			// 获取流程实例的处理记录
			List<TaskManager> taskHistorList = activitiService
					.queryHistorTasks(processInstId, processKey);
			model.addAttribute("taskHistorList", taskHistorList);

			// 当前流程实例下所有节点信息
			List<ActivitiNodeInfo> nodeList = activitiTaskService
					.getNodeInfoById(processKey, processInstId);
			model.addAttribute("nodeList", nodeList);

			// 指定驳回通过后直接返回本节点，不能任意跳转
			RejectLog rejectlog = activitiTaskService.getRejectlog(taskId);
			if (rejectlog == null) {
				// 可选的下一节点信息
				List<ActivitiNodeInfo> nextNodeList = activitiTaskService
						.getNextNodeInfoById(nodeList, processInstId);
				model.addAttribute("nextNodeList", nextNodeList);
			}

			// 可驳回的节点列表
			List<ActivitiNodeInfo> backActNodeList = activitiService
					.getBackActNode(processInstId, task.getTASK_DEF_KEY_());
			model.addAttribute("backActNodeList", backActNodeList);

			// 参数
			Object[] arrayVariable = activitiTaskService
					.getProcessVariable(processInstId);
			model.addAttribute("nodevariableList", arrayVariable[0]);// 非系统参数
			model.addAttribute("sysvariableList", arrayVariable[1]);// 系统参数

			// 获取流程实例下所有人工节点控制参数信息
			List<NodeControlParam> nodeControlParamList = activitiTaskService
					.getNodeControlParamByProcessId(processKey, processInstId);
			model.addAttribute("nodeControlParamList", nodeControlParamList);

			model.addAttribute("taskState", taskState);
			model.addAttribute("createUser", createUser);// 委托人
			model.addAttribute("entrustUser", entrustUser);// 被委托人
			model.addAttribute("processKey", processKey);
			model.addAttribute("sysid", sysid);
			model.addAttribute("currentUser", AccessControl.getAccessControl()
					.getUserAccount());
			model.addAttribute("isAdmin", AccessControl.getAccessControl()
					.isAdmin());

			// 预警、超时状态转义
			Map<Integer, String> advanceSendMap = WorkFlowConstant
					.getAdvanceSend();
			Map<Integer, String> overtimeSendMap = WorkFlowConstant
					.getOvertimeSend();
			model.addAttribute("advanceSendMap", advanceSendMap);
			model.addAttribute("overtimeSendMap", overtimeSendMap);

			tm.commit();

			return "path:dealTask";

		} catch (ProcessException e) {
			throw e;
		} catch (Exception e) {
			throw new ProcessException(e);
		} finally {
			tm.release();
		}
	}

	/**
	 * 驳回任务
	 * 
	 * @param task
	 * @param activitiNodeCandidateList
	 * @param nodevariableList
	 * @param rejectedtype
	 * @param model
	 * @return 2014年7月9日
	 */
	public @ResponseBody
	String rejectToPreTask(TaskCondition task,
			List<NodeControlParam> nodeControlParamList,
			List<ActivitiNodeInfo> activitiNodeCandidateList,
			List<Nodevariable> nodevariableList, int rejectedtype,
			ModelMap model) {
		try {

			boolean isAuthor = activitiTaskService.judgeAuthority(
					task.getTaskId(), task.getProcessKey());

			if (isAuthor) {

				String currentUser = AccessControl.getAccessControl()
						.getUserAccount();
				task.setCurrentUser(currentUser);

				Map<String, Object> variableMap = activitiTaskService
						.getVariableMap(activitiNodeCandidateList,
								nodevariableList, nodeControlParamList);

				// 保存控制变量参数
				activitiService.addNodeWorktime(task.getProcessKey(),
						task.getProcessIntsId(), nodeControlParamList);

				activitiTaskService.rejectToPreTask(task, variableMap,
						rejectedtype);

				return "success";
			} else {
				return "fail: 您没有权限驳回当前任务";
			}

		} catch (Exception e) {
			return "fail" + e.getMessage();
		}
	}

	/**
	 * 废弃任务
	 * 
	 * @param processInstIds
	 * @param deleteReason
	 * @param model
	 * @return 2014年7月9日
	 */
	public @ResponseBody
	String discardTask(String processInstIds, String deleteReason,
			String taskId, String processKey, ModelMap model) {
		try {

			boolean isAuthor = activitiTaskService.judgeAuthority(taskId,
					processKey);

			if (isAuthor) {

				String userAccount = AccessControl.getAccessControl()
						.getUserAccount();

				if (!activitiTaskService.isSignTask(taskId)) {
					// 先签收
					activitiService.claim(taskId, userAccount);
				}

				String dealRemak = "["
						+ activitiService.getUserInfoMap().getUserName(
								userAccount) + "]将任务废弃";

				activitiService.cancleProcessInstances(processInstIds,
						dealRemak, taskId, processKey, userAccount, "废弃任务",
						deleteReason);
				// 维护统一待办任务
				activitiService.refreshTodoList(processInstIds, "废弃任务", userAccount);
				return "success";

			} else {
				return "fail:您没有权限废弃当前任务";
			}

		} catch (Exception e) {
			return "fail" + e.getMessage();
		}
	}

	/**
	 * 转办任务(将自己任务转办给别人)
	 * 
	 * @param taskId
	 * @param userId
	 * @param processIntsId
	 * @param processKey
	 * @param model
	 * @return 2014年7月18日
	 */
	public @ResponseBody
	String delegateTask(TaskCondition task, ModelMap model) {
		TransactionManager tm = new TransactionManager();
		try {
			boolean isAuthor = activitiTaskService.judgeAuthority(
					task.getTaskId(), task.getProcessKey());

			if (isAuthor) {

				tm.begin();

				String currentUser = AccessControl.getAccessControl()
						.getUserAccount();
				task.setCurrentUser(currentUser);

				// 记录委托关系(有就处理，没有就不处理)
				if (StringUtil.isNotEmpty(task.getCreateUser())
						&& StringUtil.isNotEmpty(task.getEntrustUser())) {

					// 当前用户就是被委托的用户
					if (currentUser.equals(task.getEntrustUser())) {

						activitiTaskService.addEntrustTaskInfo(task);

					}

				}

				// 判断是否有没被签收
				boolean isClaim = activitiTaskService.isSignTask(task
						.getTaskId());
				if (!isClaim) {
					// 先签收
					activitiService.claim(task.getTaskId(), currentUser);
				}

				// 再转办
				activitiService.delegateTask(task.getTaskId(),
						task.getChangeUserId());

				String reamrk = "["
						+ activitiService.getUserInfoMap().getUserName(
								currentUser)
						+ "]将任务转办给["
						+ activitiService.getUserInfoMap().getUserName(
								task.getChangeUserId()) + "]";

				// 在扩展表中添加转办记录
				activitiTaskService.updateNodeChangeInfo(task.getTaskId(),
						task.getProcessIntsId(), task.getProcessKey(),
						currentUser, task.getChangeUserId(), reamrk,
						task.getCompleteReason(), 0);

				tm.commit();

				return "success";

			} else {
				return "fail:您没有权限转办当前任务";
			}

		} catch (Exception e) {
			return "fail" + e.getMessage();
		} finally {
			tm.release();
		}
	}

	/**
	 * 获取转派日志记录数据
	 * 
	 * @param processKey
	 *            流程key
	 * @param model
	 * @return 2014年10月23日
	 */
	public String queryDelegateTasksLogData(
			@PagerParam(name = PagerParam.SORT, defaultvalue = "") String sortKey,
			@PagerParam(name = PagerParam.DESC, defaultvalue = "false") boolean desc,
			@PagerParam(name = PagerParam.OFFSET) long offset,
			@PagerParam(name = PagerParam.PAGE_SIZE, defaultvalue = "10") int pagesize,
			DelegateTaskLog dekegateTaskLog, ModelMap model) {

		try {

			ListInfo listInfo = activitiTaskService.queryDelegateTasksLogData(
					dekegateTaskLog, offset, pagesize);

			model.addAttribute("listInfo", listInfo);

			return "path:delegateTasksLogList";

		} catch (Exception e) {
			throw new ProcessException(e);
		}
	}

	/**
	 * 转派任务(管理员权限，将A的任务转办给B)
	 * 
	 * @param processKeys
	 *            流程key
	 * @param fromuser
	 *            转派人
	 * @param touser
	 *            被转派人
	 * @param startUser
	 *            流程发起人
	 * @param model
	 * @return 2014年10月22日
	 */
	public @ResponseBody
	String delegateTasks(String processKeys, String fromuser, String touser,
			String startUser, ModelMap model) {

		try {

			activitiTaskService.delegateTasks(processKeys, fromuser, touser,
					startUser);

			return "success";

		} catch (Exception e) {
			return "fail" + e.getMessage();
		}
	}

	/**
	 * 撤销任务
	 * 
	 * @param taskId
	 * @param userId
	 * @param model
	 * @return 2014年7月9日
	 */
	public @ResponseBody
	String cancelTask(String taskId, String processKey, String processId,
			String cancelTaskReason, ModelMap model) {
		TransactionManager tm = new TransactionManager();

		try {
			tm.begin();

			// 获取流程实例信息
			ProcessInst processInst = activitiService
					.getProcessInstById(processId);

			String userAccount = AccessControl.getAccessControl()
					.getUserAccount();

			boolean isAdmin = AccessControl.getAccessControl().isAdmin();

			if (!isAdmin) {
				// 权限判断(当前用户id==发起人)
				if (processInst == null
						|| !processInst.getSTART_USER_ID_().equals(userAccount)) {

					tm.commit();
					return "fail:您没有权限撤销当前任务";
				}
			}

			String currentUser = activitiService.getUserInfoMap().getUserName(
					userAccount);

			// 获取第一个人工节点
			ActivitiNodeInfo nodeInfo = activitiTaskService
					.getFirstUserNode(processKey);

			// TaskManager hiTask = activitiService.getFirstTask(processId);

			String remark = "[" + currentUser + "]将任务撤回至["
					+ nodeInfo.getNode_name() + "]";

			// 获取第一人工节点信息
			// ActivityImpl act = activitiService.getTaskService()
			// .findFirstNodeByDefKey(processKey);

			// String remark = "[" + currentUser + "]将任务撤销至["
			// + act.getProperty("name") + "]";

			// 日志记录撤销操作
			activitiService.addDealTask(taskId, userAccount, currentUser, "2",
					processId, processKey, cancelTaskReason, "撤销任务", remark);

			// 撤销任务
			activitiService.cancelTask(taskId, nodeInfo.getNode_key(), remark,
					"撤销任务", cancelTaskReason);

			tm.commit();

			return "success";

		} catch (Exception e) {
			return "fail" + e.getMessage();
		} finally {
			tm.release();
		}
	}

	/**
	 * 查看当前用户的委托任务信息
	 * 
	 * @param model
	 * @return 2014年7月21日
	 */
	public String viewEntrustInfo(ModelMap model) {

		try {

			List<WfEntrust> entrustList = activitiTaskService.getEntrustInfo();

			model.addAttribute("entrustList", entrustList);

			return "path:viewEntrustInfo";

		} catch (Exception e) {
			throw new ProcessException(e);
		}
	}

	/**
	 * 手动发送消息
	 * 
	 * @param taskId
	 * @param processKey
	 * @param taskState
	 *            1未签收 2 已签收
	 * @param sentType
	 *            1 预警 2 超时
	 * @param model
	 * @return 2014年7月25日
	 */
	public @ResponseBody
	String sendMess(String taskId, String processKey, String taskState,
			String sentType, ModelMap model) {
		try {

			boolean isAuthor = activitiTaskService.judgeAuthority(taskId,
					processKey);

			if (isAuthor) {

				activitiTaskService.sendMess(taskId, taskState, sentType);

				return "success";

			} else {
				return "fail:您没有权限发送当前任务的消息";
			}

		} catch (Exception e) {
			return "fail" + e.getMessage();
		}
	}

	/**
	 * 删除变量参数
	 * 
	 * @param variableId
	 * @param model
	 *            2014年11月11日
	 */
	public @ResponseBody
	String delVariable(String variableId, ModelMap model) {

		try {
			activitiTaskService.delVariable(variableId);

			return "success";

		} catch (Exception e) {
			return "fail" + e.getMessage();
		}
	}

	/**
	 * 完成抄送任务
	 * 
	 * @param copyId
	 * @param model
	 * @return 2014年11月14日
	 */
	public String viewCopyTask(String processInstId, String copyId,
			ModelMap model) {

		TransactionManager tm = new TransactionManager();

		try {
			tm.begin();

			// 记录阅读人
			String user = AccessControl.getAccessControl().getUserAccount();

			activitiService.getTaskService().completeCopyTask(copyId, user);

			// 获取流程实例信息
			ProcessInst processInst = activitiService
					.getProcessInstById(processInstId);
			model.addAttribute("processInst", processInst);

			if (processInst != null) {
				// 获取流程实例的处理记录
				List<TaskManager> taskHistorList = activitiService
						.queryHistorTasks(processInstId, processInst.getKEY_());

				model.addAttribute("taskHistorList", taskHistorList);

				// 获取流程所有参数
				List<ActivitiVariable> instVariableList = activitiService
						.getInstVariableInfoById(processInstId);

				model.addAttribute("instVariableList", instVariableList);

				// 获取当前活动级别参数
				Object[] arryObj = activitiService
						.getCurTaskVariableInfoById(processInstId);

				model.addAttribute("taskVariableMap", arryObj[0]);
				model.addAttribute("variableRownum",
						Integer.parseInt(arryObj[1] + "") + 1);// 加标题行
				model.addAttribute("instanceRownum", arryObj[1]);

				// 获取流程实例下所有人工节点控制参数信息
				List<NodeControlParam> controlParamList = activitiTaskService
						.getNodeControlParamByProcessId(processInst.getKEY_(),
								processInstId);
				model.addAttribute("controlParamList", controlParamList);

				// 预警、超时状态转义
				Map<Integer, String> advanceSendMap = WorkFlowConstant
						.getAdvanceSend();
				Map<Integer, String> overtimeSendMap = WorkFlowConstant
						.getOvertimeSend();
				model.addAttribute("advanceSendMap", advanceSendMap);
				model.addAttribute("overtimeSendMap", overtimeSendMap);
			}

			tm.commit();

			return "path:viewTaskDetailInfo";

		} catch (Exception e) {
			throw new ProcessException(e);
		} finally {
			tm.release();
		}
	}

	public @ResponseBody(datatype = "json")
	PageData getUserPageList(String assigneeName, int limit, ModelMap model)
			throws Exception {

		return activitiTaskService.getUserPageList(assigneeName, limit);
	}

	/**
	 * 跳转到修改后续节点处理人界面
	 * 
	 * @param nodeName
	 * @param nodeValue
	 * @param processId
	 * @return 2015年1月29日
	 */
	public String toNodeAssignee(String processKey, String processId,
			ModelMap model) {

		try {

			// 当前流程实例下所有节点信息
			List<ActivitiNodeInfo> nodeList = activitiTaskService
					.getNodeInfoById(processKey, processId);

			// 获取当前任务key
			Task task = activitiService.getCurrentTask(processId);

			// 获取当前节点之前的节点
			List<ActivitiNodeInfo> backActNodeList = activitiService
					.getBackActNode(processId, task.getTaskDefinitionKey());

			// 需过滤的节点（不能修改的节点）
			StringBuffer filterNode = new StringBuffer();
			if (backActNodeList != null && backActNodeList.size() > 0) {
				for (ActivitiNodeInfo nodeInfo : backActNodeList) {
					filterNode.append(nodeInfo.getNode_key() + ",");
				}
			}
			filterNode.append(task.getTaskDefinitionKey());

			model.addAttribute("nodeList", nodeList);
			model.addAttribute("filterNode", filterNode.toString());
			model.addAttribute("processId", processId);
			model.addAttribute("processKey", processKey);

			return "path:udpNodeAssignee";

		} catch (Exception e) {
			throw new ProcessException(e);
		}
	}

	/**
	 * 修改未产生待办任务的节点处理人
	 * 
	 * @param nodeName
	 * @param nodeValue
	 * @param processId
	 * @return 2015年1月29日
	 */
	public @ResponseBody
	String udpNodeAssignee(List<ActivitiNodeInfo> nodeList, String processId) {

		try {

			activitiTaskService.udpNodeAssignee(nodeList, processId);

			return "success";

		} catch (Exception e) {
			return "fail" + e.getMessage();
		}
	}

}