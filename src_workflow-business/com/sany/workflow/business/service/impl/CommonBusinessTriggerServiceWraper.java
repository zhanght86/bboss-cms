package com.sany.workflow.business.service.impl;

import java.util.Map;

import com.sany.workflow.business.entity.ProIns;
import com.sany.workflow.business.service.CommonBusinessTriggerService;

public class CommonBusinessTriggerServiceWraper implements
		CommonBusinessTriggerService {

	private CommonBusinessTriggerService intrigger;
	private boolean enableTrigger;

	public CommonBusinessTriggerServiceWraper(
			CommonBusinessTriggerService intrigger, boolean enableTrigger) {
		this.intrigger = intrigger;
		this.enableTrigger = enableTrigger;
	}

	@Override
	public void addTodoTask(String processId, String lastOp, String lastOperName)
			throws Exception {
		if (intrigger != null && enableTrigger)
			intrigger.addTodoTask(processId, lastOp, lastOperName);

	}

	@Override
	public void createCommonOrder(ProIns proIns, String businessKey,
			String processKey, Map<String, Object> paramMap,
			boolean completeFirstTask) throws Exception {
		if (intrigger != null && enableTrigger)
			intrigger.createCommonOrder(proIns, businessKey, processKey,
					paramMap, completeFirstTask);
	}

	@Override
	public void modifyCommonOrder(ProIns proIns, String processKey,
			Map<String, Object> paramMap) throws Exception {
		if (intrigger != null && enableTrigger)
			intrigger.modifyCommonOrder(proIns, processKey, paramMap);
	}

	@Override
	public void deleteCommonOrder(ProIns proIns, String processKey)
			throws Exception {
		if (intrigger != null && enableTrigger)
			intrigger.deleteCommonOrder(proIns, processKey);
	}

}
