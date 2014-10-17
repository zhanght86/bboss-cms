/*
 * @(#)BusinessTypeManager.java
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
package com.sany.workflow.demo.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.activiti.engine.task.Task;

import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.util.ListInfo;
import com.frameworkset.util.StringUtil;
import com.sany.workflow.business.entity.TaskInfo;
import com.sany.workflow.business.service.ActivitiBusinessService;
import com.sany.workflow.demo.entity.DemoEntiy;
import com.sany.workflow.demo.service.BusinessDemoService;
import com.sany.workflow.service.ActivitiService;

/**
 * 工作流业务演示模块
 * 
 */
public class BusinessDemoServiceImpl implements BusinessDemoService {

	private com.frameworkset.common.poolman.ConfigSQLExecutor executor;

	private ActivitiService activitiService;

	private ActivitiBusinessService workflowService;

	/**
	 * 根据流程实例id获取当前任务
	 * 
	 * @param processID
	 * @return 2014年9月25日
	 */
	private List<TaskInfo> getCurrentNodeInfoByProcessID(String processID)
			throws Exception {
		List<TaskInfo> list = new ArrayList<TaskInfo>();
		// 根据流程实例ID获取运行任务
		List<Task> taskList = activitiService
				.listTaskByProcessInstanceId(processID);

		for (int i = 0; i < taskList.size(); i++) {
			Task task = taskList.get(i);
			TaskInfo taskInfo = workflowService
					.getCurrentNodeInfo(task.getId());
			if (null != taskInfo) {
				list.add(taskInfo);
			}
		}
		return list;
	}

	@Override
	public ListInfo queryDemoData(String processKey, String businessKey,
			long offset, int pagesize) throws Exception {

		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();

			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("processKey", processKey);
			map.put("businessKey", businessKey);

			ListInfo listInfo = executor.queryListInfoBean(DemoEntiy.class,
					"getDemoList_wf", offset, pagesize, map);

			List<DemoEntiy> demoList = listInfo.getDatas();

			if (demoList != null && demoList.size() != 0) {

				for (int i = 0; i < demoList.size(); i++) {
					DemoEntiy de = demoList.get(i);

					de.setSenderName(activitiService.getUserInfoMap()
							.getUserName(de.getSender()));

					if (StringUtil.isEmpty(de.getInstanceId())) {
						de.setTitle(de.getBusinessKey() + "[暂存]");

						de.setBusinessState(0);
					} else {

						if (StringUtil.isNotEmpty(de.getEndTime())) {
							de.setTitle(de.getBusinessKey() + "[结束]");
							de.setBusinessState(2);
						} else {
							de.setTitle(de.getBusinessKey() + "[运行中]");
							de.setBusinessState(1);
						}
					}

					// 获取任务及处理人
					if (StringUtil.isNotEmpty(de.getInstanceId())) {

						List<TaskInfo> taskList = getCurrentNodeInfoByProcessID(de
								.getInstanceId());

						if (null != taskList && taskList.size() > 0) {

							StringBuffer users = new StringBuffer();

							for (int j = 0; j < taskList.size(); j++) {

								TaskInfo taskInfo = taskList.get(j);

								if (j == 0) {
									users.append(taskInfo.getAssignee());
								} else {
									users.append(",").append(
											taskInfo.getAssignee());
								}
							}
							de.setAssigneeName(activitiService.getUserInfoMap()
									.getUserName(users.toString()));
							de.setAssignee(users.toString());
						}
					}

				}
			}

			tm.commit();

			return listInfo;
		} catch (Exception e) {
			throw new Exception("获取demo业务单数据出错：" + e.getMessage());
		} finally {
			tm.release();
		}
	}
}
