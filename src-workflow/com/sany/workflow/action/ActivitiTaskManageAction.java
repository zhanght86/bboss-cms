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
import com.sany.workflow.entity.ActivitiNodeInfo;
import com.sany.workflow.entity.ActivitiVariable;
import com.sany.workflow.entity.Nodevariable;
import com.sany.workflow.entity.ProcessInst;
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

			model.addAttribute("processKey", processKey);

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

			if (processInst != null) {
				// 获取流程实例的处理记录
				List<TaskManager> taskHistorList = activitiService
						.queryHistorTasks(processInstId);

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
		try {

			boolean isAuthor = activitiTaskService.judgeAuthority(taskId,
					processKey);

			if (isAuthor) {

				activitiTaskService.signTaskByUser(taskId, AccessControl
						.getAccessControl().getUserAccount());

				return "success";
			} else {
				return "fail:您没有权限签收当前任务";
			}

		} catch (Exception e) {
			return "fail" + e.getMessage();
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
			List<Nodevariable> nodevariableList, ModelMap model) {

		TransactionManager tm = new TransactionManager();

		try {

			boolean isAuthor = activitiTaskService.judgeAuthority(
					task.getTaskId(), task.getProcessKey());

			if (isAuthor) {

				tm.begin();

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

						// 追加DELETE_REASON_字段信息
						remark = "<br/>备注:[" + task.getCreateUser() + "]的任务委托给["
								+ task.getEntrustUser() + "]完成";
					}

				} else {
					remark = "<br/>备注:[" + task.getCurrentUser() + "]完成";
				}

				if (StringUtil.isNotEmpty(task.getCompleteReason())) {
					task.setCompleteReason(task.getCompleteReason() + remark);
				}

				// 完成任务
				activitiTaskService.completeTask(task,
						activitiNodeCandidateList, nodevariableList);

				tm.commit();

				return "success";

			} else {
				return "fail:您没有权限处理当前任务";
			}

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

			// // 用来获取流程业务主题和状态
			// ProcessInstance instance = activitiService
			// .getProcessInstanceById(processInstId);
			// model.addAttribute("businessKey", instance.getBusinessKey());//
			// 业务主题
			// model.addAttribute("suspended", instance.isSuspended());// 状态

			// 获取流程实例的处理记录
			List<TaskManager> taskHistorList = activitiService
					.queryHistorTasks(processInstId);
			model.addAttribute("taskHistorList", taskHistorList);

			// 当前流程实例下所有节点信息
			List<ActivitiNodeInfo> nodeList = activitiTaskService
					.getNodeInfoById(processKey, processInstId);
			model.addAttribute("nodeList", nodeList);

			// 可选的下一节点信息
			List<ActivitiNodeInfo> nextNodeList = activitiTaskService
					.getNextNodeInfoById(nodeList, processInstId);
			model.addAttribute("nextNodeList", nextNodeList);
			// 获取当前节点的上
			// ActivityImpl[] activityTask = activitiService.getTaskService()
			// .findRejectedActivityNode(taskId);

			// 当前任务节点信息
			TaskManager task = activitiService.getTaskByTaskId(taskId);
			model.addAttribute("task", task);

			// 参数
			Object[] arrayVariable = activitiTaskService
					.getProcessVariable(processInstId);
			model.addAttribute("nodevariableList", arrayVariable[0]);// 非系统参数
			model.addAttribute("sysvariableList", arrayVariable[1]);// 系统参数

			model.addAttribute("taskState", taskState);
			model.addAttribute("taskId", taskId);
			model.addAttribute("createUser", createUser);// 委托人
			model.addAttribute("entrustUser", entrustUser);// 被委托人
			model.addAttribute("processKey", processKey);
			model.addAttribute("sysid", sysid);
			model.addAttribute("currentUser", AccessControl.getAccessControl()
					.getUserAccount());

			// 预警、超时状态转义
			Map<Integer, String> advanceSendMap = WorkFlowConstant
					.getAdvanceSend();
			Map<Integer, String> overtimeSendMap = WorkFlowConstant
					.getOvertimeSend();
			model.addAttribute("advanceSendMap", advanceSendMap);
			model.addAttribute("overtimeSendMap", overtimeSendMap);

			tm.commit();

			return "path:dealTask";

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
			List<ActivitiNodeInfo> activitiNodeCandidateList,
			List<Nodevariable> nodevariableList, int rejectedtype,
			ModelMap model) {
		try {

			boolean isAuthor = activitiTaskService.judgeAuthority(
					task.getTaskId(), task.getProcessKey());

			if (isAuthor) {

				activitiTaskService.rejectToPreTask(task,
						activitiNodeCandidateList, nodevariableList,
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

				activitiService.cancleProcessInstances(processInstIds,
						deleteReason);

				return "success";

			} else {
				return "fail:您没有权限废弃当前任务";
			}

		} catch (Exception e) {
			return "fail" + e.getMessage();
		}
	}

	/**
	 * 转办任务
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

				// 在扩展表中添加转办记录
				activitiTaskService.updateNodeChangeInfo(task.getTaskId(),
						task.getProcessIntsId(), task.getProcessKey(),
						currentUser, task.getChangeUserId());

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

			boolean isAuthor = activitiTaskService.judgeAuthority(taskId,
					processKey);

			if (isAuthor) {

				tm.begin();

				// 获得流程的所有节点
				List<ActivityImpl> activties = activitiService
						.getActivitImplListByProcessKey(processKey);

				// 获得当前活动任务
				List<Task> task = activitiService.getTaskService()
						.createTaskQuery().processInstanceId(processId).list();

				activitiService.completeTaskLoadCommonParamsWithDest(task
						.get(0).getId(), activties.get(1).getId(),
						cancelTaskReason);

				tm.commit();

				return "success";

			} else {
				return "fail:您没有权限撤销当前任务";
			}

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

}