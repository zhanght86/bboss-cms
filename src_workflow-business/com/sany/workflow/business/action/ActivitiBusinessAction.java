/*
 * @(#)ActivitiRepository.java
 * 
 * Copyright @ 2001-2012 SANY Group Co.,Ltd.
 * All right reserved.
 * 
 * 这个软件是属于三一集团有限公司机密的和私有信息，不得泄露。
 * 并且只能由三一集团有限公司内部员工在得到许可的情况下才允许使用。
 * This software is the confidential and proprietary information
 * of SANY Group Co, Ltd. You shall not disclose such
 * Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with
 * SANY Group Co, Ltd.
 */
package com.sany.workflow.business.action;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.web.servlet.ModelMap;

import com.sany.workflow.business.entity.ActNode;
import com.sany.workflow.business.entity.ProIns;
import com.sany.workflow.business.service.ActivitiBusinessService;
import com.sany.workflow.business.util.WorkflowConstants;

/**
 * @todo 工作流业务管理类
 * @author tanx
 * @date 2014年8月20日
 * 
 */
public class ActivitiBusinessAction {

	private ActivitiBusinessService workflowService;

	public String toIndex(ModelMap model) {
		return "path:toIndex";
	}

	/**
	 * 跳转到流程申请页面
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 *             2014年8月20日
	 */
	public String toworkflowMain(ModelMap model) throws Exception {
		String processKey = "Mms.return";
		List<ActNode> actList = null;

		// 读取暂存form表单数据是否存在
		ProIns proIns = workflowService.getFormDatasByBusinessKey("流程开启设置3");
		if (proIns != null) {
			actList = proIns.getActs();
			model.addAttribute("pagestate", "2");
		} else {
			actList = workflowService.getWFNodeConfigInfoForCommon(processKey);
			model.addAttribute("pagestate", "1");
		}
		model.addAttribute("actList", actList);
		model.addAttribute("processKey", processKey);
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
	String tempSaveFormDatas(ProIns proIns, ModelMap model) throws Exception {
		try {
			workflowService.tempSaveFormDatas(proIns, "流程开启设置3", "Mms.return");
			return "success";
		} catch (Exception e) {
			return "fail:" + e.getMessage();
		}
	}

	/**
	 * 获取流程图片
	 * 
	 * @param processkey
	 * @param response
	 * @throws IOException
	 *             2014年8月20日
	 */
	public void getProccessPic(String processKey, HttpServletResponse response)
			throws IOException {
		if (processKey != null && !processKey.equals("")) {
			OutputStream out = response.getOutputStream();
			workflowService.getProccessPic(processKey, out);
		}
	}

	/**
	 * 获取流程追踪图片
	 * 
	 * @param processkey
	 * @param response
	 * @throws IOException
	 *             2014年8月20日
	 */
	public void getProccessActivePic(String processInstId,
			HttpServletResponse response) throws IOException {
		if (processInstId != null && !processInstId.equals("")) {
			OutputStream out = response.getOutputStream();
			workflowService.getProccessActivePic(processInstId, out);
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
	String startProc(ProIns proIns, ModelMap model) throws Exception {
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			workflowService
					.startProc(proIns, "流程开启设置3", "Mms.return", paramMap);
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
			String taskId, String userId, ModelMap model) throws Exception {

		workflowService.toDealTask("Mms.return",
				"d7268705-43e7-11e4-87d8-4437e6999a31",
				"d752ee3f-43e7-11e4-87d8-4437e6999a31", model);
		model.addAttribute("pagestate", WorkflowConstants.PRO_PAGESTATE_APPROVE);

		return "path:toIndex";
	}

	/**
	 * 跳转到处理任务页面
	 * 
	 * @param processKey
	 * @param taskId
	 * @param userId
	 * @return 2014年8月23日
	 */
	public String toViewTask(String processKey, String processId,
			String taskId, String userId, ModelMap model) throws Exception {

		workflowService.toViewTask("7bf0d519-43e5-11e4-b17a-4437e6999a31",
				"流程开启设置2", "chenm24", model);

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
			workflowService.approveWorkFlow(proIns, processKey, paramMap);
			return "success";
		} catch (Exception e) {
			return "fail:" + e.getMessage();
		}
	}
}
