package com.sany.workflow.service;

import java.util.List;

import com.sany.workflow.entity.ActivitiNodeInfo;

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
	 * 处理任务
	 * 
	 * @param taskId
	 * @param taskState
	 * @param taskKey
	 * @param nodeList
	 *            2014年5月27日
	 */
	public void completeTask(String taskId, String taskState, String taskKey,
			List<ActivitiNodeInfo> nodeList);

	/** 驳回任务 
	 * @param taskId
	 * @param nodeList
	 * 2014年5月27日
	 */
	public void rejectToPreTask(String taskId, List<ActivitiNodeInfo> nodeList);
	
	/**
	 * 签收任务 gw_tanx
	 * 
	 * @param taskId
	 * @param map
	 */
	public void signTaskByUser(String taskId, String username);
	
	/** 获取节点信息 gw_tanx
	 * @param processKey
	 * @param processInstId
	 * @return
	 * 2014年5月23日
	 */
	public List<ActivitiNodeInfo> getNodeInfoById(String processKey,String processInstId);
	
	/** 获取下一流向节点信息 gw_tanx
	 * @param nodeList
	 * @return
	 * 2014年5月26日
	 */
	public List<ActivitiNodeInfo> getNextNodeInfoById(final List<ActivitiNodeInfo> nodeList,String processInstId);
	
}
