package com.sany.workflow.action;

import java.util.List;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.task.Task;
import org.frameworkset.task.TaskService;
import org.frameworkset.util.annotations.PagerParam;
import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.web.servlet.ModelMap;

import com.frameworkset.platform.cms.util.StringUtil;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.util.ListInfo;
import com.sany.workflow.entity.ActivitiNodeInfo;
import com.sany.workflow.entity.ActivitiVariable;
import com.sany.workflow.entity.Nodevariable;
import com.sany.workflow.entity.ProcessInst;
import com.sany.workflow.entity.TaskCondition;
import com.sany.workflow.entity.TaskManager;
import com.sany.workflow.service.ActivitiService;
import com.sany.workflow.service.ActivitiTaskService;
import com.sany.workflow.service.ProcessException;

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

			model.addAttribute("processKey", processKey);

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
			ListInfo listInfo = activitiService.queryTasks(task, offset,
					pagesize);

			model.addAttribute("listInfo", listInfo);

			return "path:ontimeTaskList";

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

		try {
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

			return "path:viewTaskInfo";

		} catch (Exception e) {
			throw new ProcessException(e);
		}
	}

	/**
	 * 流程明细查看跳转
	 * 
	 * @param processKey
	 * @return 2014年5月7日
	 */
	public String viewTaskDetailInfo(String processInstId, ModelMap model) {
		try {

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
			}

			return "path:viewTaskDetailInfo";

		} catch (Exception e) {
			throw new ProcessException(e);
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
	String signTask(String taskId, ModelMap model) {
		try {

			activitiTaskService.signTaskByUser(taskId, AccessControl
					.getAccessControl().getUserAccount());

			return "success";
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
		try {

			activitiTaskService.completeTask(task, activitiNodeCandidateList,
					nodevariableList);

			return "success";
		} catch (Exception e) {
			return "fail" + e.getMessage();
		}
	}

	/**
	 * 跳转至处理任务页面
	 * 
	 * @param processKey
	 * @param model
	 * @return 2014年5月22日
	 */
	public String toDealTask(String processKey, String processInstId,
			String taskState, String taskId, ModelMap model) {

		try {
			// 统一任务管理界面过来，没有指定processKey
			if (StringUtil.isEmpty(processKey)) {
				// 根据流程实例id获取流程Key
				HistoricProcessInstance hiInstance = activitiService
						.getHisProcessInstanceById(processInstId);

				processKey = activitiService
						.getRepositoryService()
						.getProcessDefinition(
								hiInstance.getProcessDefinitionId()).getKey();
			}
			// 当前流程实例下所有节点信息
			List<ActivitiNodeInfo> nodeList = activitiTaskService
					.getNodeInfoById(processKey, processInstId);

			// 可选的下一节点信息
			List<ActivitiNodeInfo> nextNodeList = activitiTaskService
					.getNextNodeInfoById(nodeList, processInstId);

			// 当前任务节点信息
			Task task = activitiService.getTaskById(taskId);
			task.setAssignee(activitiService.userIdToUserName(
					task.getAssignee(), "2"));

			// 参数
			Object [] arrayVariable = activitiTaskService
					.getProcessVariable(processInstId);

			model.addAttribute("task", task);
			model.addAttribute("taskState", taskState);
			model.addAttribute("taskId", taskId);
			model.addAttribute("nodeList", nodeList);
			model.addAttribute("nextNodeList", nextNodeList);
			model.addAttribute("nodevariableList", arrayVariable[0]);//非系统参数
			model.addAttribute("sysvariableList", arrayVariable[1]);//系统参数

			return "path:dealTask";

		} catch (Exception e) {
			throw new ProcessException(e);
		}
	}

	/**驳回任务
	 * @param task
	 * @param activitiNodeCandidateList
	 * @param nodevariableList
	 * @param rejectedtype
	 * @param model
	 * @return
	 * 2014年7月9日
	 */
	public @ResponseBody
	String rejectToPreTask(TaskCondition task,
			List<ActivitiNodeInfo> activitiNodeCandidateList,
			List<Nodevariable> nodevariableList, int rejectedtype,
			ModelMap model) {
		try {

			activitiTaskService.rejectToPreTask(task,
					activitiNodeCandidateList, nodevariableList, rejectedtype);

			return "success";
		} catch (Exception e) {
			return "fail" + e.getMessage();
		}
	}
	
	/** 废弃任务
	 * @param processInstIds
	 * @param deleteReason
	 * @param model
	 * @return
	 * 2014年7月9日
	 */
	public @ResponseBody
	String discardTask(String processInstIds, String deleteReason,
			ModelMap model) {
		try {

			activitiService
					.cancleProcessInstances(processInstIds, deleteReason);

			return "success";
		} catch (Exception e) {
			return "fail" + e.getMessage();
		}
	}
	
	/** 转办任务
	 * @param taskId
	 * @param userId
	 * @param model
	 * @return
	 * 2014年7月9日
	 */
	public @ResponseBody
	String delegateTask(String taskId, String userId, ModelMap model) {
		try {

			activitiService.delegateTask(taskId, userId);

			return "success";
		} catch (Exception e) {
			return "fail" + e.getMessage();
		}
	}
	
	/** 撤销任务
	 * @param taskId
	 * @param userId
	 * @param model
	 * @return
	 * 2014年7月9日
	 */
	public @ResponseBody
	String cancelTask(String taskId, String processKey, String processId,
			String cancelTaskReason, ModelMap model) {
		try {
			//获得流程的所有节点
			List<ActivityImpl> activties = activitiService
					.getActivitImplListByProcessKey(processKey);

			//获得当前活动任务
			List<Task> task = activitiService.getTaskService()
					.createTaskQuery().processInstanceId(processId).list();

			activitiService.completeTaskLoadCommonParamsWithDest(task.get(0)
					.getId(), activties.get(1).getId(), cancelTaskReason);

			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "fail" + e.getMessage();
		}
	}

}