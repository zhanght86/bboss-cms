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
package com.sany.workflow.business.service.impl;

import java.util.Map;

import com.frameworkset.common.poolman.ConfigSQLExecutor;
import com.sany.workflow.business.entity.CommonBusinessBean;
import com.sany.workflow.business.util.WorkflowConstants;
import com.sany.workflow.service.impl.CommonTaskTrigger;

/**
 * 
 * 
 * @author luoh19
 * @version 2015年2月9日,v1.0
 */
public class CommonBusinessTrigger extends CommonTaskTrigger {

	private ConfigSQLExecutor executor;

	 

	/**
	 * 创建统一业务订单 
	 * 返回值说明：
	 * TaskTrigger.refresh_task_success 刷新流程待办任务成功，并且流程实例还有有任务（没有结束）
	 * TaskTrigger.refresh_task_notask 刷新流程待办任务成功，并且流程实例没有任务（已经结束）
	 * TaskTrigger.refresh_task_fail 刷新流程待办任务失败
	 * TaskTrigger.refresh_task_noenable 统一待办机制未启用
	 */
	@Override
	public int createCommonOrder(String processId, String createUserAccount,String businessKey,
			String processKey, Map<String, Object> paramMap,
			boolean completeFirstTask) throws Exception {

		if (paramMap != null
				&& paramMap.containsKey(WorkflowConstants.COMMON_BUSINESS_BEAN)) {
			CommonBusinessBean businessBean = (CommonBusinessBean) paramMap
					.get(WorkflowConstants.COMMON_BUSINESS_BEAN);
			if (businessBean != null) {
				businessBean.setProcessKey(processKey);
				businessBean.setOrderId(businessKey);
				executor.insertBean("insertCommonBusinessInfo", businessBean);
//				if (completeFirstTask) {
//					this.modifyCommonOrder(proIns, processKey, paramMap);
//				}
			}
		}

		int result = super.createCommonOrder(processId, createUserAccount, businessKey, processKey, paramMap, completeFirstTask);
		return result;
	}

	/**
	 * 修改统一业务订单 
	 * 返回值说明：
	 * TaskTrigger.refresh_task_success 刷新流程待办任务成功，并且流程实例还有有任务（没有结束）
	 * TaskTrigger.refresh_task_notask 刷新流程待办任务成功，并且流程实例没有任务（已经结束）
	 * TaskTrigger.refresh_task_fail 刷新流程待办任务失败
	 * TaskTrigger.refresh_task_noenable 统一待办机制未启用
	 */
	@Override
	public int modifyCommonOrder(String processId,
			String lastOp,
			String userAccount, String processKey,
			Map<String, Object> paramMap) throws Exception {
		// // 获取当前流程所有任务
		// List<TaskInfo> taskInfoList = executor.queryList(TaskInfo.class,
		// "qryTaskTodoInfoByProcessId", proIns.getProInsId(),
		// proIns.getProInsId());
		// // 删除当前任务
		// executor.delete("deleteTaskInfoByProcessId", proIns.getProInsId());
		// // 插入最新的待办
		// executor.insertBeans("insertRunTaskInfo", taskInfoList);
		int result = super.modifyCommonOrder(processId, lastOp, userAccount, processKey, paramMap);
		return result;
	}

	/**
	 * 删除统一业务订单 
	 * 返回值说明：
	 * TaskTrigger.refresh_task_success 刷新流程待办任务成功，并且流程实例还有有任务（没有结束）
	 * TaskTrigger.refresh_task_notask 刷新流程待办任务成功，并且流程实例没有任务（已经结束）
	 * TaskTrigger.refresh_task_fail 刷新流程待办任务失败
	 * TaskTrigger.refresh_task_noenable 统一待办机制未启用
	 */
	@Override
	public int deleteCommonOrder(String processId,
			 String processKey,
			String userAccount,String lastOp,
			String opReason,Map<String, Object> paramMap)
			throws Exception {

		int result = super.deleteCommonOrder(processId, processKey, userAccount, lastOp, opReason, paramMap);
		return result;
	}

//	@Override
//	public void addTodoTask(String processId, String lastOp, String lastOper)
//			throws Exception {
//
//		TransactionManager tm = new TransactionManager();
//
//		try {
//
//			tm.begin();
//
//			executor.delete("deleteTaskInfoByProcessId", processId);
//
//			// 根据流程实例ID获取运行任务
//			List<TaskInfo> taskInfoList = new ArrayList<TaskInfo>();
//			List<Task> taskList = activitiService
//					.listTaskByProcessInstanceId(processId);
//
//			for (int i = 0; i < taskList.size(); i++) {
//				Task task = taskList.get(i);
//				TaskInfo taskInfo = activitiTaskService.getCurrentNodeInfo(task
//						.getId());
//				if (null != taskInfo) {
//					taskInfoList.add(taskInfo);
//				}
//			}
//			// 查询流程实例，获取流程发起人
//			ProcessInst inst = executor.queryObject(ProcessInst.class,
//					"getProcessByProcessId_wf", processId);
//			// 流程未结束
//			if (taskInfoList != null && taskInfoList.size() > 0) {
//
//				// 增加参数
//				for (TaskInfo task : taskInfoList) {
//
//					// 域账号转工号
//					StringBuffer worknos = new StringBuffer();
//					String[] arrayAssignee = task.getAssignee().split(",");
//
//					for (int i = 0; i < arrayAssignee.length; i++) {
//						if (i == 0) {
//							worknos.append(activitiService.getUserInfoMap()
//									.getUserAttribute(arrayAssignee[i],
//											"userWorknumber"));
//						} else {
//							worknos.append(",").append(
//									activitiService.getUserInfoMap()
//											.getUserAttribute(arrayAssignee[i],
//													"userWorknumber"));
//						}
//					}
//					task.setDealerWorkNo(worknos.toString());
//					task.setLastOp(lastOp);
//					task.setLastOperName(lastOper);
//
//					
//
//					task.setSender(inst.getSTART_USER_ID_());
//					task.setSenderName(activitiService.getUserInfoMap()
//							.getUserName(inst.getSTART_USER_ID_()));
//
//				}
//
//				executor.insertBeans("addTodoTask_wf", taskInfoList);
//
//			}
//
//			tm.commit();
//
//		} catch (Exception e) {
//			throw new Exception("统一待办任务维护出错：",e);
//		} finally {
//			tm.release();
//		}
//	}
	
	@Override
	/**
	 *  
	 * 返回值说明：
	 * TaskTrigger.refresh_task_success 刷新流程待办任务成功，并且流程实例还有有任务（没有结束）
	 * TaskTrigger.refresh_task_notask 刷新流程待办任务成功，并且流程实例没有任务（已经结束）
	 * TaskTrigger.refresh_task_fail 刷新流程待办任务失败
	 * TaskTrigger.refresh_task_noenable 统一待办机制未启用
	 */
	public int refreshTodoList(String processId, String lastOp, String lastOper)
			throws Exception {

		int result = super.refreshTodoList(processId, lastOp, lastOper);
		return result;
	}

	@Override
	public void deleteTodoListByKeys(String... processKeys) throws Exception {
		// TODO Auto-generated method stub
		super.deleteTodoListByKeys(processKeys);
	}

	@Override
	public void deleteTodoListByProinstids(String... processInstIDs)
			throws Exception {
		// TODO Auto-generated method stub
		super.deleteTodoListByProinstids(processInstIDs);
	}
	
	public void changeTasksTo(String fromuser,String touser,String startUser,String... processKeys) throws Exception
	{
		super.changeTasksTo( fromuser, touser, startUser,processKeys) ;
	}
}
