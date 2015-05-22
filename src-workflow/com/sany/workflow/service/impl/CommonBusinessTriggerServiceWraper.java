package com.sany.workflow.service.impl;

import java.util.Map;

import com.sany.workflow.service.TaskTrigger;

public class CommonBusinessTriggerServiceWraper implements TaskTrigger {

	private TaskTrigger intrigger;
	private boolean enableTrigger;

	public CommonBusinessTriggerServiceWraper(
			TaskTrigger intrigger, boolean enableTrigger) {
		this.intrigger = intrigger;
		this.enableTrigger = enableTrigger;
	}

	@Override
	public int refreshTodoList(String processId, String lastOp, String lastOperName)
			throws Exception {
		if (intrigger != null && enableTrigger)
			return intrigger.refreshTodoList(  processId,   lastOp,   lastOperName)
					 ;
		return TaskTrigger.refresh_task_noenable;

	}

	@Override
	public int createCommonOrder(String processId, String createUserAccount,String businessKey,
			String processKey, Map<String, Object> paramMap,
			boolean completeFirstTask) throws Exception {
		if (intrigger != null && enableTrigger)
			return intrigger.createCommonOrder(  processId,   createUserAccount,  businessKey,
					  processKey,   paramMap,
					  completeFirstTask);
		return TaskTrigger.refresh_task_noenable;
	}

	@Override
	public int modifyCommonOrder(String processId,
			String lastOp,
			String userAccount, String processKey,
			Map<String, Object> paramMap)throws Exception {
		if (intrigger != null && enableTrigger)
			return intrigger.modifyCommonOrder(  processId,
					  lastOp,
					  userAccount,   processKey,
					  paramMap);
		return TaskTrigger.refresh_task_noenable;
	}

	@Override
	public int deleteCommonOrder(String processId,
			 String processKey,
			String userAccount,String lastOp,
			String opReason,Map<String, Object> paramMap)
			throws Exception {
		if (intrigger != null && enableTrigger)
			return intrigger.deleteCommonOrder(  processId,
					   processKey,
						  userAccount,  lastOp,
						  opReason, paramMap);
		return TaskTrigger.refresh_task_noenable;
	}

	@Override
	public void deleteTodoListByKeys(String... processKeys) throws Exception {
		if (intrigger != null && enableTrigger)
			 intrigger.deleteTodoListByKeys(processKeys);
		
	}

	@Override
	public void deleteTodoListByProinstids(String... processInstIDs)
			throws Exception {
		if (intrigger != null && enableTrigger)
			 intrigger.deleteTodoListByProinstids(processInstIDs);
		
	}
	/**
	 *  转派任务，后台执行
	 * @param fromuser 任务原来拥有人
	 * @param touser 任务转派给用户
	 * @param startUser 只转派发起人的任务，可以不指定，就是所有发起人的任务
	 * @param processKeys 只转派指定流程的任务，可以不指定，就是所有流程的任务都转派
	 * @throws Exception
	 */
	public void changeTasksTo(String fromuser,String touser,String startUser,String... processKeys) throws Exception
	{
		if (intrigger != null && enableTrigger)
			 intrigger.changeTasksTo( fromuser, touser, startUser,processKeys) ;
	}
}
