package com.sany.workflow.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.frameworkset.util.annotations.PagerParam;
import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.web.servlet.ModelMap;

import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.util.ListInfo;
import com.sany.workflow.entity.ProcessDefCondition;
import com.sany.workflow.entity.Task;
import com.sany.workflow.entity.TaskCondition;
import com.sany.workflow.service.ActivitiService;

/**
 * @todo 工作流任务管理模块
 * @author tanx
 * @date 2014年5月7日
 * 
 */
public class ActivitiTaskManageAction {

	private ActivitiService activitiService;

	/**
	 * 跳转至实时任务管理页面
	 * 
	 * @return 2014年5月7日
	 */
	public String ontimeTaskManager(String processKey, ModelMap model) {

		model.addAttribute("processKey", processKey);

		return "path:ontimeTaskManager";
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
		ListInfo listInfo = null;
		try {

			listInfo = activitiService.queryTasks(task, offset, pagesize);

			model.addAttribute("listInfo", listInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "path:ontimeTaskList";
	}

	/**
	 * 跳转至历史任务管理页面
	 * 
	 * @return 2014年5月7日
	 */
	public String historyTimeTaskManager(String processKey, ModelMap model) {

		model.addAttribute("processKey", processKey);

		return "path:historyTimeTaskManager";
	}

	/**
	 * 加载历史任务数据
	 * 
	 * @param sortKey
	 * @param desc
	 * @param offset
	 * @param pagesize
	 * @param Task
	 * @param model
	 * @return 2014年5月7日
	 */
	public String queryHistoryTimeTaskData(
			@PagerParam(name = PagerParam.SORT, defaultvalue = "resourceName") String sortKey,
			@PagerParam(name = PagerParam.DESC, defaultvalue = "false") boolean desc,
			@PagerParam(name = PagerParam.OFFSET) long offset,
			@PagerParam(name = PagerParam.PAGE_SIZE, defaultvalue = "10") int pagesize,
			Task task, String processKey, ModelMap model) {
		ListInfo listInfo = null;
		try {

			// listInfo = activitiService.getHisTaskById(task.getProc_def_id_(),
			// task.getProc_inst_id_(), processKey,"history", offset, pagesize);

			model.addAttribute("listInfo", listInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "path:historyTimeTaskList";
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
	}

	/**
	 * 查看特定流程的任务
	 * 
	 * @param sortKey
	 * @param desc
	 * @param offset
	 * @param pagesize
	 * @param processKey
	 * @param version
	 * @param state
	 * @param model
	 * @return 2014年5月7日
	 */
	public String queryProcessTaskInfo(
			@PagerParam(name = PagerParam.SORT, defaultvalue = "resourceName") String sortKey,
			@PagerParam(name = PagerParam.DESC, defaultvalue = "false") boolean desc,
			@PagerParam(name = PagerParam.OFFSET) long offset,
			@PagerParam(name = PagerParam.PAGE_SIZE, defaultvalue = "10") int pagesize,
			ProcessDefCondition processDefCondition, String tabNum,
			ModelMap model) {
		try {

			ListInfo listInfo = null;

			// 查看实时任务 数字对应tab页位置
			if (tabNum.equals("4")) {

				listInfo = activitiService.queryProcessDefs(offset, pagesize,
						processDefCondition);
				model.addAttribute("processDefs", listInfo);

				return "path:ontimeTaskManager";
				// 查看历史任务
			} else {
				listInfo = activitiService.queryProcessDefs(offset, pagesize,
						processDefCondition);
				model.addAttribute("processDefs", listInfo);

				return "path:historyTimeTaskManager";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "path:ontimeTaskManager";
		}
	}

	/**
	 * 流程明细查看跳转
	 * 
	 * @param processKey
	 * @return 2014年5月7日
	 */
	public String viewTaskDetailInfo(String processInstId, ModelMap model) {

		List<HistoricTaskInstance> list = activitiService
				.getHisTaskByProcessId(processInstId);

		// // 根据流程实例iD获取流程定义KEY
		// HistoricProcessInstance hiInstance =
		// activitiService.getHisProcessInstanceById(processInstId);

		model.addAttribute("list", list);
		model.addAttribute("processInstId", processInstId);
		return "path:viewTaskDetailInfo";
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
			activitiService.signTaskByUser(taskId, AccessControl
					.getAccessControl().getUserAccount());

			return "success";
		} catch (Exception e) {
			return "fail:" + e.getMessage();
		}

	}

	/**
	 * 完成任务
	 * 
	 * @param taskId
	 * @param taskState
	 * @param model
	 * @return 2014年5月16日
	 */
	public @ResponseBody
	String completeTask(String taskId, String taskState, ModelMap model) {
		try {

			// 未签收任务处理
			if (taskState.equals("1")) {
				activitiService.completeTaskByUser(taskId, AccessControl
						.getAccessControl().getUserAccount());

			} else if (taskState.equals("2")) {// 已签收任务处理
				activitiService.completeTask(taskId);
			}

			return "success";
		} catch (Exception e) {
			return "fail:" + e.getMessage();
		}
	}

}