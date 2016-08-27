package com.sany.workflow.service;

import java.util.Map;

/**
 * 
 * @author yinbp
 *
 */
public interface TaskTrigger {
	/**刷新流程待办任务成功，并且流程实例还有有任务（没有结束）*/
	public final int refresh_task_success = 1;
	/**刷新流程待办任务成功，并且流程实例没有任务（已经结束）*/
	public final int refresh_task_notask = 2;
	/**刷新流程待办任务失败*/
	public final int refresh_task_fail = 0;
	/**
	 * 统一待办机制未启用
	 */
	public final int refresh_task_noenable = 3;
	/**
	 * 创建统一业务订单
	 * 返回值说明：
	 * TaskTrigger.refresh_task_success 刷新流程待办任务成功，并且流程实例还有有任务（没有结束）
	 * TaskTrigger.refresh_task_notask 刷新流程待办任务成功，并且流程实例没有任务（已经结束）
	 * TaskTrigger.refresh_task_fail 刷新流程待办任务失败
	 * TaskTrigger.refresh_task_noenable 统一待办机制未启用
	 */
	public int createCommonOrder(String processId, String createUserAccount,String businessKey,
			String processKey, Map<String, Object> paramMap,
			boolean completeFirstTask) throws Exception;

	/**
	 * 修改统一业务订单
	 * 返回值说明：
	 * TaskTrigger.refresh_task_success 刷新流程待办任务成功，并且流程实例还有有任务（没有结束）
	 * TaskTrigger.refresh_task_notask 刷新流程待办任务成功，并且流程实例没有任务（已经结束）
	 * TaskTrigger.refresh_task_fail 刷新流程待办任务失败
	 * TaskTrigger.refresh_task_noenable 统一待办机制未启用
	 */
	public int modifyCommonOrder(String processId,
			String lastOp,
			String userAccount, String processKey,
			Map<String, Object> paramMap) throws Exception;

	/**
	 * 删除统一业务订单
	  * 返回值说明：
	 * TaskTrigger.refresh_task_success 刷新流程待办任务成功，并且流程实例还有有任务（没有结束）
	 * TaskTrigger.refresh_task_notask 刷新流程待办任务成功，并且流程实例没有任务（已经结束）
	 * TaskTrigger.refresh_task_fail 刷新流程待办任务失败
	 * TaskTrigger.refresh_task_noenable 统一待办机制未启用
	 */
	public int deleteCommonOrder(String processId,
			 String processKey,
			String userAccount,String lastOp,
			String opReason,Map<String, Object> paramMap)
			throws Exception;
	/**
	 * 刷新统一待办方法
	 * 返回值说明：
	 * TaskTrigger.refresh_task_success 刷新流程待办任务成功，并且流程实例还有有任务（没有结束）
	 * TaskTrigger.refresh_task_notask 刷新流程待办任务成功，并且流程实例没有任务（已经结束）
	 * TaskTrigger.refresh_task_fail 刷新流程待办任务失败
	 * TaskTrigger.refresh_task_noenable 统一待办机制未启用
	 */
	public int refreshTodoList(String processId, String lastOp, String lastOperName)
			throws Exception;
	/**
	 * 后台执行
	 * @param processKeys
	 * @throws Exception
	 */
	public void deleteTodoListByKeys(String... processKeys) throws Exception;
	/**
	 * 根据流程key，删除统一待办任务，后台执行
	 * @param processKey
	 * @throws Exception 
	 */
	public void deleteTodoListByProinstids(String... processInstIDs) throws Exception;
	
	/**
	 *  转派任务，后台执行
	 * @param fromuser 任务原来拥有人
	 * @param touser 任务转派给用户
	 * @param startUser 只转派发起人的任务，可以不指定，就是所有发起人的任务
	 * @param processKeys 只转派指定流程的任务，可以不指定，就是所有流程的任务都转派
	 * @throws Exception
	 */
	public void changeTasksTo(String fromuser,String touser,String startUser,String... processKeys) throws Exception;
	
	
}
