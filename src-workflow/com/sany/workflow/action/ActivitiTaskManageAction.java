package com.sany.workflow.action;

import java.util.List;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.task.Task;
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
import com.sany.workflow.service.ActivitiConfigService;
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

	private ActivitiConfigService activitiConfigService;

	/**
	 * 跳转至实时任务管理页面
	 * 
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
	String completeTask(String taskId, String taskState, String taskKey,
			List<ActivitiNodeInfo> nodeList, ModelMap model) {
		try {

			activitiTaskService.completeTask(taskId, taskState, taskKey,
					nodeList);

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

			// 节点参数
			List<Nodevariable> nodevariableList = activitiConfigService
					.selectNodevariable(processKey, "", "");

			model.addAttribute("task", task);
			model.addAttribute("taskState", taskState);
			model.addAttribute("taskId", taskId);
			model.addAttribute("nodeList", nodeList);
			model.addAttribute("nextNodeList", nextNodeList);
			model.addAttribute("nodevariableList", nodevariableList);

			return "path:dealTask";

		} catch (Exception e) {
			throw new ProcessException(e);
		}
	}

	/**
	 * 驳回任务
	 * 
	 * @param taskId
	 * @param model
	 * @return 2014年5月27日
	 */
	public @ResponseBody
	String rejectToPreTask(String taskId, List<ActivitiNodeInfo> nodeList,
			ModelMap model) {
		try {

			activitiTaskService.rejectToPreTask(taskId, nodeList);

			return "success";
		} catch (Exception e) {
			return "fail" + e.getMessage();
		}

	}

}