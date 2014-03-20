package com.sany.hrm.workflow.service;

import java.util.List;
import java.util.Map;

/**
 * 工作流
 * 
 * @author gw_yaoht
 * @version 2012-8-14
 **/
public interface WorkflowService {

	/**
	 * 查询
	 * 
	 * @param warrantId
	 *            凭证
	 * @param firstResult
	 *            开始行，行号
	 * @param maxResults
	 *            最大返回行数
	 * @return 待办任务
	 * @throws Exception
	 */
	List<Map<String, Object>> findTask(String warrantId, int firstResult, int maxResults) throws Exception;

	/**
	 * 查询
	 * 
	 * @param warrantId
	 *            凭证
	 * @param taskId
	 *            待办任务标识
	 * @return 工作流日志
	 */
	List<Map<String, Object>> findWorkflowLog(String warrantId, String taskId) throws Exception;

	/**
	 * 读取
	 * 
	 * @param warrantId
	 *            凭证
	 * @return 待办任务数量
	 * @throws Exception
	 */
	Integer readTaskCount(String warrantId) throws Exception;
}
