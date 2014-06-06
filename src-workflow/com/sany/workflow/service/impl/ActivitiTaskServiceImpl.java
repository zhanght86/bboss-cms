package com.sany.workflow.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.platform.cms.util.StringUtil;
import com.frameworkset.platform.security.AccessControl;
import com.sany.workflow.entity.ActivitiNodeInfo;
import com.sany.workflow.entity.ActivitiVariable;
import com.sany.workflow.service.ActivitiService;
import com.sany.workflow.service.ActivitiTaskService;
import com.sany.workflow.service.ProcessException;

/**
 * 任务管理业务实现类
 * 
 * @todo
 * @author tanx
 * @date 2014年5月27日
 * 
 */
public class ActivitiTaskServiceImpl implements ActivitiTaskService,
		org.frameworkset.spi.DisposableBean {

	private com.frameworkset.common.poolman.ConfigSQLExecutor executor;

	private ActivitiService activitiService;

	@Override
	public void destroy() throws Exception {

	}

	/**
	 * 获取流程节点参数配置信息
	 * 
	 * @param nodeList
	 * @return 2014年5月27日
	 */
	private Map<String, Object> getVariableMap(List<ActivitiNodeInfo> nodeList) {

		Map<String, Object> variableMap = new HashMap<String, Object>();

		// 流程参数
		for (int i = 0; i < nodeList.size(); i++) {
			// 用户
//			if (!StringUtil.isEmpty(nodeList.get(i).getNode_users_id())) {
				variableMap.put(nodeList.get(i).getNode_key() + "_users",
						nodeList.get(i).getNode_users_id());
//			}
			// 组
//			if (!StringUtil.isEmpty(nodeList.get(i).getNode_groups_id())) {
				variableMap.put(nodeList.get(i).getNode_key() + "_groups",
						nodeList.get(i).getNode_groups_id());
//			}
			// 参数
//			if (!StringUtil.isEmpty(nodeList.get(0).getNode_param_id())) {
				variableMap.put(nodeList.get(0).getNode_param_id(),
						nodeList.get(0).getNode_param_value());
//			}
		}
		return variableMap;
	}

	@Override
	public void completeTask(String taskId, String taskState, String taskKey,
			List<ActivitiNodeInfo> nodeList) {

		try {

			// 获取参数配置信息
			Map<String, Object> variableMap = getVariableMap(nodeList);

			// 未签收任务处理
			if ("1".equals(taskState)) {

				if (StringUtil.isEmpty(taskKey)) {
					activitiService.completeTask(taskId, AccessControl
							.getAccessControl().getUserAccount(), variableMap);

				} else {
					activitiService.completeTaskWithLocalVariables(taskId,AccessControl
							.getAccessControl().getUserAccount(),variableMap,taskKey);
				}

				// 已签收任务处理
			} else if ("2".equals(taskState)) {

				if (StringUtil.isEmpty(taskKey)) {
					activitiService.completeTask(taskId, variableMap);

				} else {
					activitiService.completeTaskLoadCommonParams(taskId,
							variableMap, taskKey);
				}
			}
		} catch (Exception e) {
			throw new ProcessException(e);
		}

	}

	@Override
	public void rejectToPreTask(String taskId, List<ActivitiNodeInfo> nodeList) {

		// 获取参数配置信息
		Map<String, Object> variableMap = getVariableMap(nodeList);

		activitiService.rejecttoPreTask(taskId, variableMap);

	}

	@Override
	public void signTaskByUser(String taskId, String username) {
		activitiService.getTaskService().claim(taskId, username);
	}

	@Override
	public List<ActivitiNodeInfo> getNodeInfoById(String processKey,
			String processInstId) {
		TransactionManager tms = new TransactionManager();

		try {
			tms.begin();

			// 从扩展表中获取节点信息
			List<ActivitiNodeInfo> nodeList = executor
					.queryList(ActivitiNodeInfo.class, "queryAllActivitiNodes",
							processKey);

			if (nodeList == null) {
				tms.commit();
				return null;
			}

			// 根据流程实例ID 获取流程的参数变量信息
			List<ActivitiVariable> variableList = executor.queryList(
					ActivitiVariable.class, "getVariableListById_wf",
					processInstId);

			if (variableList != null && variableList.size() > 0) {

				StringBuffer users = new StringBuffer();
				StringBuffer groups = new StringBuffer();

				for (int i = 0; i < nodeList.size(); i++) {
					ActivitiNodeInfo ani = nodeList.get(i);

					// 非人工处理的过滤
					if (!"userTask".equals(ani.getNode_type())) {
						continue;
					}

					// 拼接匹配对象
					users.append(ani.getNode_key() + "_users");
					groups.append(ani.getNode_key() + "_groups");

					for (int j = 0; j < variableList.size(); j++) {

						ActivitiVariable av = variableList.get(j);

						if (StringUtil.isNotEmpty(av.getTEXT_())) {

							// 用户
							if (av.getNAME_().equals(users.toString())) {

								ani.setNode_users_id(av.getTEXT_());
								ani.setNode_users_name(activitiService
										.userIdToUserName(av.getTEXT_(), "1"));
							}

							// 组
							if (av.getNAME_().equals(groups.toString())) {

								ani.setNode_groups_id(av.getTEXT_());
							}
						}
					}

					users.setLength(0);
					groups.setLength(0);

				}
			}

			tms.commit();

			return nodeList;

		} catch (Exception e) {
			throw new ProcessException(e);
		} finally {
			tms.release();
		}
	}

	@Override
	public List<ActivitiNodeInfo> getNextNodeInfoById(
			final List<ActivitiNodeInfo> nodeList, String processInstId) {
		try {

			if (nodeList == null || nodeList.size() == 0) {
				return null;
			}

			// 获取当前节点信息
			HashMap currnetNodeMap = executor.queryObject(HashMap.class,
					"getCurrentNodeInfoById_wf", processInstId);

			// 判断当前节点是否有指向任意节点的权限(预留)

			List<ActivitiNodeInfo> nextNodeList = new ArrayList<ActivitiNodeInfo>();

			for (int i = 0; i < nodeList.size(); i++) {

				ActivitiNodeInfo ani = nodeList.get(i);

				// 过滤开始节点
				if ("startEvent".equals(ani.getNode_type())) {
					continue;
				}
				
				// 过滤exclusiveGateway
				if ("exclusiveGateway".equals(ani.getNode_type())) {
					continue;
				}

				// 过滤当前节点
				if (ani.getNode_key().equals(
						currnetNodeMap.get("TASK_DEF_KEY_"))) {
					continue;
				}

				nextNodeList.add(ani);
			}

			return nextNodeList;

		} catch (Exception e) {
			throw new ProcessException(e);
		}

	}

}
