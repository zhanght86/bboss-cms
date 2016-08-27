package com.sany.workflow.service.impl;

import java.util.Map;

import com.sany.workflow.service.ActivitiService;
import com.sany.workflow.service.ActivitiTaskService;
import com.sany.workflow.service.TaskTrigger;

/**
 * 统一待办触发器
 * @author yinbp
 *
 */
public class CommonTaskTrigger implements TaskTrigger {
	private ActivitiService activitiService;

	private ActivitiTaskService activitiTaskService;
	@Override
	public int createCommonOrder(String processId, String createUserAccount,
			String businessKey, String processKey,
			Map<String, Object> paramMap, boolean completeFirstTask)
			throws Exception {
		if(!completeFirstTask)
		{
			
		
		// 维护统一待办任务
			return this.refreshTodoList(processId, "发起流程", createUserAccount);
		}
		else
		{
			return this.refreshTodoList(processId, "提交任务", createUserAccount);
		}

	}

	@Override
	public int modifyCommonOrder(String processId, String lastOp,
			String userAccount, String processKey, Map<String, Object> paramMap)
			throws Exception {
		return this.refreshTodoList(
				processId,
				lastOp,
				
						userAccount);
	}

	@Override
	public int deleteCommonOrder(String processId,
			 String processKey,
			String userAccount,String lastOp,
			String opReason,
			Map<String, Object> paramMap) throws Exception {
		activitiService.deleteBussinessOrderFromTrigger( processKey, userAccount, lastOp, opReason,processId);
		return TaskTrigger.refresh_task_notask;

	}

	@Override
	public int refreshTodoList(String processId, String lastOp, String userAccount)
			throws Exception {
		return activitiTaskService.refreshTodoList(processId,  lastOp, userAccount);

	}
	
	
	public void deleteTodoListByKeys(String... processKeys) throws Exception
	{
		activitiService.deleteTodoListByKeys( processKeys);
	}
	/**
	 * 根据流程key，删除统一待办任务
	 * @param processKey
	 * @throws Exception 
	 */
	public void deleteTodoListByProinstids(String... processInstIDs) throws Exception
	{
		activitiService.deleteTodoListByProinstids(processInstIDs);
		
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
		activitiService.changeTasksTo(fromuser,touser,startUser,processKeys);
	}

}
