package com.sany.workflow.service;

import java.util.List;

import com.frameworkset.util.ListInfo;
import com.sany.workflow.entity.ActivitiNodeInfo;
import com.sany.workflow.entity.Nodevariable;
import com.sany.workflow.entity.TaskCondition;

/**
 * 任务管理业务接口
 * 
 * @todo
 * @author tanx
 * @date 2014年5月27日
 * 
 */
public interface ActivitiTaskService {

	/**
	 * 根据条件获取历史任务列表,分页展示
	 * 
	 * @param task
	 * @param offset
	 * @param pagesize
	 * @return 2014年6月18日
	 */
	public ListInfo queryHistoryTasks(TaskCondition task, long offset,
			int pagesize);

	/**
	 * 处理任务
	 * 
	 * @param taskId
	 * @param taskState
	 * @param taskKey
	 * @param nodeList
	 *            2014年5月27日
	 */
	public void completeTask(TaskCondition task,
			List<ActivitiNodeInfo> activitiNodeCandidateList,
			List<Nodevariable> nodevariableList);

	/**
	 * 驳回任务
	 * 
	 * @param taskId
	 * @param nodeList
	 *            2014年5月27日
	 */
	public void rejectToPreTask(TaskCondition task,
			List<ActivitiNodeInfo> nodeList, List<Nodevariable> nodevariableList);

	/**
	 * 签收任务 gw_tanx
	 * 
	 * @param taskId
	 * @param map
	 */
	public void signTaskByUser(String taskId, String username);

	/**
	 * 获取节点信息 gw_tanx
	 * 
	 * @param processKey
	 * @param processInstId
	 * @return 2014年5月23日
	 */
	public List<ActivitiNodeInfo> getNodeInfoById(String processKey,
			String processInstId);

	/**
	 * 获取流程级别参数
	 * 
	 * @param processInstId
	 * @return 2014年6月27日
	 */
	public List<Nodevariable> getProcessVariable(String processInstId);

	/**
	 * 获取下一流向节点信息 gw_tanx
	 * 
	 * @param nodeList
	 * @return 2014年5月26日
	 */
	public List<ActivitiNodeInfo> getNextNodeInfoById(
			final List<ActivitiNodeInfo> nodeList, String processInstId);

}
