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

import javax.servlet.http.HttpServletResponse;

import com.sany.workflow.business.service.ActivitiBusinessService;

/**
 * @todo 工作流业务管理类
 * @author tanx
 * @date 2014年8月20日
 * 
 */
public class ActivitiBusinessAction {

	private ActivitiBusinessService workflowService;

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

}
