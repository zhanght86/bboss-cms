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
import com.sany.workflow.business.entity.ProIns;
import com.sany.workflow.business.service.ActivitiBusinessService;
import com.sany.workflow.business.util.WorkflowConstants;
import com.sany.workflow.demo.service.BusinessDemoService;

/**
 * @todo 工作流任务管理模块
 * @author tanx
 * @date 2014年5月7日
 * 
 */
public class BusinessDemoAction {

	private ActivitiBusinessService workflowService;
	
	private BusinessDemoService demoService;

	private String processKey = "Mms.return";

	public String toIndex(ModelMap model) {
		return "path:toIndex";
	}

	public String toDemo(ModelMap model) {
		model.addAttribute("processKey", processKey);
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

		ListInfo listInfo = demoService.queryDemoData(processKey,
				businessKey, offset, pagesize);

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
			return "fail:" + e.getMessage();
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
}