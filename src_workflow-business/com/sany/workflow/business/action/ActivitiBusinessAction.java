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

/**
 * @todo 工作流业务管理类
 * @author tanx
 * @date 2014年8月20日
 * 
 */
public class ActivitiBusinessAction {

	private ActivitiBusinessService workflowService;

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

		ListInfo listInfo = workflowService.queryDemoData(processKey,
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
