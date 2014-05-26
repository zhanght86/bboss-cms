package com.sany.workflow.action;

import java.util.List;

import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.frameworkset.util.annotations.PagerParam;
import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.web.servlet.ModelMap;

import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.util.ListInfo;
import com.sany.workflow.entity.ActivitiNodeInfo;
import com.sany.workflow.entity.ProcessInst;
import com.sany.workflow.entity.TaskCondition;
import com.sany.workflow.entity.TaskManager;
import com.sany.workflow.service.ActivitiConfigService;
import com.sany.workflow.service.ActivitiService;
import com.sany.workflow.service.ProcessException;

/**
 * @todo 工作流任务管理模块
 * @author tanx
 * @date 2014年5月7日
 * 
 */
public class ActivitiTaskManageAction {

	private ActivitiService activitiService;

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

			if (processInst != null) {
				// 获取流程实例的处理记录
				List<TaskManager> taskHistorList = activitiService
						.queryHistorTasks(processInstId);

				model.addAttribute("taskHistorList", taskHistorList);
			}

			model.addAttribute("processInst", processInst);

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
			activitiService.signTaskByUser(taskId, AccessControl
					.getAccessControl().getUserAccount());

			return "success";
		} catch (Exception e) {
			throw new ProcessException(e);
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
			if ("1".equals(taskState)) {
				activitiService.completeTaskByUser(taskId, AccessControl
						.getAccessControl().getUserAccount());
				// 已签收任务处理
			} else if ("2".equals(taskState)) {
				activitiService.completeTask(taskId);
			}

			return "success";
		} catch (Exception e) {
			throw new ProcessException(e);
		}
	}

	/**
	 * 跳转至处理任务页面
	 * 
	 * @param processKey
	 * @param model
	 * @return 2014年5月22日
	 */
	public String toDealTask(String processKey, ModelMap model) {

		try {
			// 获取所有节点信息
			List<ActivitiNodeInfo> nodeInfoList = activitiConfigService
					.queryAllActivitiNodeInfo(processKey);
			
			model.addAttribute("nodeInfoList", nodeInfoList);
			model.addAttribute("process_key", processKey);

			return "path:dealTask";

		} catch (Exception e) {
			throw new ProcessException(e);
		}
	}

}