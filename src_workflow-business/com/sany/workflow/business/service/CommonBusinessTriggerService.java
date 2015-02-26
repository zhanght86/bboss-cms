/*
 * @(#)WorkflowTriggerService.java
 * 
 * Copyright @ 2001-2015 SANY Group Co.,Ltd.
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
package com.sany.workflow.business.service;

import java.util.Map;

import com.sany.workflow.business.entity.ProIns;

/**
 * 
 * 
 * @author luoh19
 * @version 2015年2月9日,v1.0
 */
public interface CommonBusinessTriggerService {

	/**
	 * 创建统一业务订单
	 */
	public void createCommonOrder(ProIns proIns, String businessKey,
			String processKey, Map<String, Object> paramMap,
			boolean completeFirstTask) throws Exception;

	/**
	 * 修改统一业务订单
	 */
	public void modifyCommonOrder(ProIns proIns, String processKey,
			Map<String, Object> paramMap) throws Exception;

	/**
	 * 删除统一业务订单
	 */
	public void deleteCommonOrder(ProIns proIns, String processKey)
			throws Exception;

	public void addTodoTask(String processId, String lastOp, String lastOperName)
			throws Exception;

}
