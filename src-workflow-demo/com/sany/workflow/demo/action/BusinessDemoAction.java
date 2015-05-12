package com.sany.workflow.demo.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.frameworkset.util.annotations.PagerParam;
import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.web.servlet.ModelMap;

import bboss.org.jgroups.util.UUID;

import com.frameworkset.util.ListInfo;
import com.frameworkset.util.StringUtil;
import com.sany.workflow.business.entity.ActNode;
import com.sany.workflow.business.entity.HisTaskInfo;
import com.sany.workflow.business.entity.ProIns;
import com.sany.workflow.business.entity.TaskInfo;
import com.sany.workflow.business.service.ActivitiBusinessService;
import com.sany.workflow.business.util.WorkflowConstants;
import com.sany.workflow.demo.entity.ListData;
import com.sany.workflow.demo.entity.PageData;
import com.sany.workflow.demo.service.BusinessDemoService;
import com.sany.workflow.service.ProcessException;

/**
 * @todo 工作流任务管理模块
 * @author tanx
 * @date 2014年5月7日
 * 
 */
public class BusinessDemoAction {

	private ActivitiBusinessService workflowService;

	private BusinessDemoService demoService;

	public String toIndex(ModelMap model) {
		return "path:toIndex";
	}

	public String toDemo(ModelMap model) {
		return "path:toDemo";
	}

	/**
	 * 获取业务单数据
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 *             2014年10月11日
	 */
	public String queryDemoData(
			@PagerParam(name = PagerParam.SORT, defaultvalue = "") String sortKey,
			@PagerParam(name = PagerParam.DESC, defaultvalue = "false") boolean desc,
			@PagerParam(name = PagerParam.OFFSET) long offset,
			@PagerParam(name = PagerParam.PAGE_SIZE, defaultvalue = "10") int pagesize,
			String processKey, String businessKey, ModelMap model)
			throws Exception {

		ListInfo listInfo = demoService.queryDemoData(processKey, businessKey,
				offset, pagesize);

		model.addAttribute("listInfo", listInfo);

		return "path:toDemoList";
	}

	/**
	 * 跳转到流程申请页面
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 *             2014年8月20日
	 */
	public String toworkflowMain(String businessKey, String processKey,
			ModelMap model) throws Exception {
		List<ActNode> actList = null;

		// 读取暂存form表单数据是否存在
		ProIns proIns = workflowService.getFormDatasByBusinessKey(businessKey);
		if (proIns != null) {
			actList = proIns.getActs();
			model.addAttribute(WorkflowConstants.PRO_PAGESTATE,
					WorkflowConstants.PRO_PAGESTATE_READD);
		} else {
			actList = workflowService.getWFNodeConfigInfoForCommon(processKey);
			model.addAttribute(WorkflowConstants.PRO_PAGESTATE,
					WorkflowConstants.PRO_PAGESTATE_INIT);
		}
		model.addAttribute("actList", actList);
		model.addAttribute("processKey", processKey);

		model.addAttribute("businessKey", businessKey);
		return "path:toIndex";
	}

	/**
	 * 暂存审批表单数据
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 *             2014年8月20日
	 */
	public @ResponseBody
	String tempSaveFormDatas(ProIns proIns, String businessKey, ModelMap model)
			throws Exception {
		try {

			if (StringUtil.isEmpty(businessKey)) {
				businessKey = UUID.randomUUID().toString();
			}
			workflowService
					.tempSaveFormDatas(proIns, businessKey, "Mms.return");
			return "success";
		} catch (Exception e) {
			return "fail:" + e.getMessage();
		}
	}

	/**
	 * 开启流程实例
	 * 
	 * @param processkey
	 * @param response
	 * @throws IOException
	 *             2014年8月20日
	 */
	public @ResponseBody
	String startProc(ProIns proIns, String businessKey, String processKey,
			ModelMap model) throws Exception {
		try {

			if (StringUtil.isEmpty(businessKey)) {
				businessKey = UUID.randomUUID().toString();
			}

			Map<String, Object> paramMap = new HashMap<String, Object>();

			workflowService
					.startProc(proIns, businessKey, processKey, paramMap);
			return "success";
		} catch (Exception e) {
			return "fail:" + StringUtil.formatBRException(e);
		}
	}

	/**
	 * 跳转到处理任务页面
	 * 
	 * @param processKey
	 * @param taskId
	 * @param userId
	 * @return 2014年8月23日
	 */
	public String toDealTask(String processKey, String processId,
			String taskId, ModelMap model) throws Exception {

		workflowService.toDealTask(processKey, processId, taskId, model);

		return "path:toIndex";
	}

	/**
	 * 跳转到查看任务页面
	 * 
	 * @param processKey
	 * @param taskId
	 * @param userId
	 * @return 2014年8月23日
	 */
	public String toViewTask(String taskId, String businessKey, String userId,
			ModelMap model) throws Exception {

		workflowService.toViewTask(taskId, businessKey, userId, model);

		model.addAttribute("businessKey", businessKey);
		return "path:toIndex";
	}

	/**
	 * 处理任务
	 * 
	 * @param proIns
	 * @param processKey
	 * @param paramMap
	 * @return
	 * @throws Exception
	 *             2014年9月19日
	 */
	public @ResponseBody
	String approveWorkFlow(ProIns proIns, String processKey,
			Map<String, Object> paramMap) throws Exception {

		try {
			workflowService.approveWorkFlow(proIns, processKey,
					new HashMap<String, Object>());
			return "success";
		} catch (Exception e) {
			return "fail:" + e.getMessage();
		}
	}

	/**
	 * 处理任务
	 * 
	 * @param proIns
	 * @param processKey
	 * @param paramMap
	 * @return
	 * @throws Exception
	 *             2014年9月19日
	 */
	public @ResponseBody(datatype = "json")
	PageData getBusinessKeyPageList(String businessKey, int limit,
			ModelMap model) throws Exception {

		return demoService.getBusinessKeyList(businessKey, limit);
	}

	/**
	 * 处理任务
	 * 
	 * @param proIns
	 * @param processKey
	 * @param paramMap
	 * @return
	 * @throws Exception
	 *             2014年9月19日
	 */
	public @ResponseBody(datatype = "json")
	List<ListData> getBusinessKeyList(String businessKey, int limit,
			ModelMap model) throws Exception {

		return demoService.getBusinessKeyList(businessKey);
	}

	/**
	 * 获取日志记录
	 * 
	 * @param processInstId
	 *            流程实例id
	 * @param filterLog
	 *            是否合并并行节点的任务撤销、驳回、废弃的日志
	 * @return
	 * @throws Exception
	 *             2014年12月25日
	 */
	public String getHisTaskInfo(String businessKey, boolean filterLog,
			ModelMap model) throws Exception {

		List<HisTaskInfo> taskHistorList = demoService.getHisTaskInfo(
				businessKey, filterLog);

		model.addAttribute("taskHistorList", taskHistorList);

		model.addAttribute("filterLog", filterLog);

		return "path:hiTaskList";
	}

	/**
	 * 跳转到修改后续节点处理人界面
	 * 
	 * @param nodeName
	 * @param nodeValue
	 * @param processId
	 * @return 2015年1月29日
	 */
	public String toNodeAssignee(String businessKey, String processKey,
			ModelMap model) {

		try {

			// 当前任务节点信息
			List<TaskInfo> task = workflowService
					.getCurrentNodeInfoByBussinessKey(businessKey);
			model.addAttribute("task", task);

			// 获取流程节点配置信息
			List<ActNode> actList = workflowService
					.getWFNodeConfigInfoByCondition(processKey, task.get(0)
							.getInstanceId(), task.get(0).getTaskId());
			model.addAttribute("actList", actList);

			// 获取当前节点之前的节点
			List<ActNode> backActNodeList = workflowService.getBackActNode(task
					.get(0).getInstanceId(), task.get(0).getTaskDefKey());

			// 需过滤的节点（不能修改的节点）
			StringBuffer filterNode = new StringBuffer();
			if (backActNodeList != null && backActNodeList.size() > 0) {
				for (ActNode nodeInfo : backActNodeList) {
					filterNode.append(nodeInfo.getActId() + ",");
				}
			}
			filterNode.append(task.get(0).getTaskDefKey());

			model.addAttribute("actList", actList);
			model.addAttribute("filterNode", filterNode.toString());
			model.addAttribute("processId", task.get(0).getInstanceId());
			model.addAttribute("processKey", processKey);

			return "path:toUdpNodeAssignee";

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
	String udpNodeAssignee(ProIns proIns) {

		try {

			workflowService.udpNodeAssignees(proIns.getActs(),
					proIns.getProInsId());

			return "success";

		} catch (Exception e) {
			return "fail" + e.getMessage();
		}
	}

}