package com.sany.workflow.service;

import java.util.List;
import java.util.Map;

import com.frameworkset.util.ListInfo;
import com.sany.workflow.business.entity.TaskInfo;
import com.sany.workflow.entity.ActivitiNodeCandidate;
import com.sany.workflow.entity.ActivitiNodeInfo;
import com.sany.workflow.entity.DelegateTaskLog;
import com.sany.workflow.entity.NoHandleTask;
import com.sany.workflow.entity.NodeControlParam;
import com.sany.workflow.entity.Nodevariable;
import com.sany.workflow.entity.PageData;
import com.sany.workflow.entity.RejectLog;
import com.sany.workflow.entity.TaskCondition;
import com.sany.workflow.entity.TaskManager;
import com.sany.workflow.entrust.entity.WfEntrust;

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
	public void completeTask(TaskCondition task, Map<String, Object> variableMap);

	/**
	 * 驳回任务
	 * 
	 * @param taskId
	 * @param nodeList
	 *            2014年5月27日
	 */
	public void rejectToPreTask(TaskCondition task,
			Map<String, Object> variableMap, int rejectedtype);

	/**
	 * 驳回任务
	 * 
	 * @param taskId
	 * @param nodeList
	 *            2014年8月11日
	 */
	public boolean isSignTask(String taskId);

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
	public Object[] getProcessVariable(String processInstId);

	/**
	 * 获取下一流向节点信息 gw_tanx
	 * 
	 * @param nodeList
	 * @return 2014年5月26日
	 */
	public List<ActivitiNodeInfo> getNextNodeInfoById(
			final List<ActivitiNodeInfo> nodeList, String processInstId);

	/**
	 * 统一代办取待办任务接口
	 * 
	 * @param pernr
	 *            登录账号
	 * @param sysid
	 *            系统管理id
	 * @param offset
	 * @param pagesize
	 * @return ListInfo 统一代办集合数据，包含分页参数 2014年7月3日
	 */
	public List<NoHandleTask> getNoHandleTask(String pernr, String sysid,
			long offset, int pagesize);

	/**
	 * 统一代办获取任务的条数
	 * 
	 * @param pernr
	 * @param sysid
	 * @return 2014年7月16日
	 */
	public int countTaskNum(String pernr, String sysid);

	/**
	 * 修改节点转办信息 gw_tanx
	 * 
	 * @param taskId
	 * @param changeInfo
	 *            2014年7月14日
	 */
	public void updateNodeChangeInfo(String taskId, String processIntsId,
			String processKey, String fromuserID, String userId, String reamrk,
			String reason, int delegateType);

	/**
	 * 记录已处理委托任务信息
	 * 
	 * @param taskId
	 * @param entrustUser
	 *            委托用户 2014年7月17日
	 */
	public void addEntrustTaskInfo(TaskCondition task);

	/**
	 * 查看当前用户的委托任务信息
	 * 
	 * @param taskId
	 * @param entrustUser
	 *            委托用户 2014年7月17日
	 */
	public List<WfEntrust> getEntrustInfo();

	/**
	 * 权限判断
	 * 
	 * @param processKey
	 * @param taskId
	 * @return 2014年7月23日
	 */
	public boolean judgeAuthority(String taskId, String processKey);

	/**
	 * 手动发送信息
	 * 
	 * @param taskId
	 * @param sentType
	 * @throws Exception
	 *             2014年7月25日
	 */
	public void sendMess(String taskId, String taskState, String sentType)
			throws Exception;

	/**
	 * 获取开启流程实例后节点控制参数信息
	 * 
	 * @param ProcessId
	 *            流程实例id
	 * @return
	 * @throws Exception
	 *             2014年9月5日
	 */
	public List<NodeControlParam> getNodeControlParamByProcessId(
			String processKey, String ProcessId) throws Exception;

	/**
	 * 获取驳回日志记录
	 * 
	 * @param ProcessId
	 *            流程实例id
	 * @return
	 * @throws Exception
	 *             2014年9月5日
	 */
	public RejectLog getRejectlog(String taskId) throws Exception;

	/**
	 * 获取用户未处理的任务
	 * 
	 * @param userId
	 *            域账号
	 * @return
	 * @throws Exception
	 *             2014年10月22日
	 */
	public List<TaskManager> getUserNoDealTasks(String userId) throws Exception;

	/**
	 * 转派任务
	 * 
	 * @param processKey
	 *            流程key
	 * @param fromuser
	 *            转派人
	 * @param touser
	 *            被转派人
	 * @param startUser
	 *            流程发起人
	 * @throws Exception
	 *             2014年10月24日
	 */
	public void delegateTasks(String processKey, String fromuser,
			String touser, String startUser) throws Exception;

	/**
	 * 根据条件获取转派日志列表数据,分页展示
	 * 
	 * @param task
	 * @param offset
	 * @param pagesize
	 * @return 2014年6月18日
	 */
	public ListInfo queryDelegateTasksLogData(DelegateTaskLog delegateTaskLog,
			long offset, int pagesize) throws Exception;

	/**
	 * 获取第一个人工任务节点
	 * 
	 * @param processKey
	 *            流程key
	 * @return
	 * @throws Exception
	 *             2014年10月29日
	 */
	public ActivitiNodeInfo getFirstUserNode(String processKey)
			throws Exception;

	/**
	 * 删除变量参数
	 * 
	 * @param variableId
	 * @throws Exception
	 *             2014年11月11日
	 */
	public void delVariable(String variableId) throws Exception;

	/**
	 * 分页获取抄送任务
	 * 
	 * @param process_key
	 * @param businesskey
	 * @param offset
	 * @param pagesize
	 * @return
	 * @throws Exception
	 *             2014年11月17日
	 */
	public ListInfo getUserCopyTasks(String process_key, String businesskey,
			long offset, int pagesize) throws Exception;

	/**
	 * 分页获取已读抄送任务
	 * 
	 * @param process_key
	 * @param businesskey
	 * @param offset
	 * @param pagesize
	 * @return
	 * @throws Exception
	 *             2014年11月18日
	 */
	public ListInfo getUserReaderCopyTasks(String process_key,
			String businesskey, long offset, int pagesize) throws Exception;

	/**
	 * 分页获取处理人名字
	 * 
	 * @param assignee
	 * @param limit
	 * @param model
	 * @return
	 * @throws Exception
	 *             2014年12月22日
	 */
	public PageData getUserPageList(String assigneeName, int limit)
			throws Exception;

	public String changeToDomainAccount(String userId);

	public TaskInfo getCurrentNodeInfoByKey(String bussinesskey,
			String processKey, String userId) throws Exception;

	public boolean judgeAuthorityNoAdmin(String taskId, String processKey,
			String userAccount);

	public boolean judgeAuthority(String taskId, String processKey,
			String userAccount);

	public TaskInfo getCurrentNodeInfo(String taskId) throws Exception;

	public TaskInfo getCurrentNodeInfoByBussinessKey(String bussinesskey,
			String userId) throws Exception;

	public TaskInfo getCurrentNodeInfoByProcessInstanceid(
			String processinstanceid, String userId) throws Exception;

	public boolean isSignTask(String taskId, String userId) throws Exception;

	public void autoCompleteTask(TaskInfo task, String dealOption,
			String dealRemak, String dealReason, String processInstanceID,
			String currentUser) throws Exception;

	/**
	 * 获取流程节点参数配置信息(准备流程处理人和变量参数及控制参数)
	 * 
	 * @param nodeList
	 * @return 2014年5月27日
	 */
	public Map<String, Object> getVariableMap(
			List<ActivitiNodeInfo> activitiNodeCandidateList,
			List<Nodevariable> nodevariableList,
			List<NodeControlParam> nodeControlParamList) throws Exception;

	/**
	 * 修改未产生待办节点的处理人
	 * 
	 * @param nodeList
	 *            节点处理人信息
	 * @param processId
	 *            流程实例ID
	 * @throws Exception
	 *             2015年2月7日
	 */
	public void udpNodeAssignee(List<ActivitiNodeInfo> nodeList,
			String processId) throws Exception;
}
