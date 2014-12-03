package com.sany.workflow.business.service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.frameworkset.web.servlet.ModelMap;

import com.frameworkset.util.ListInfo;
import com.sany.workflow.business.entity.ActNode;
import com.sany.workflow.business.entity.HisTaskInfo;
import com.sany.workflow.business.entity.ProIns;
import com.sany.workflow.business.entity.TaskInfo;

/**
 * 工作流业务接口
 * 
 * @todo
 * @author tanx
 * @date 2014年6月9日
 * 
 */
public interface ActivitiBusinessService {

	/**
	 * 处理任务权限判断(排除管理员判断)
	 * 
	 * @param taskId
	 *            任务id
	 * @param processKey
	 *            流程key
	 * @param userAccount
	 *            当前用户
	 * @return 2014年9月29日
	 */
	public boolean judgeAuthorityNoAdmin(String taskId, String processKey,
			String userAccount);

	/**
	 * 处理任务权限判断(考虑管理员判断)
	 * 
	 * @param taskId
	 *            任务id
	 * @param processKey
	 *            流程key
	 * @param userAccount
	 *            当前用户
	 * @return 2014年8月20日
	 */
	public boolean judgeAuthority(String taskId, String processKey,
			String userAccount);

	/**
	 * (还未开启流程实例前)获取流程组织结构类型节点配置
	 * 
	 * @param processKey
	 *            流程key
	 * @param userId
	 *            用户id，可以是工号或用户名等，需要与代办查询条件等一致即可
	 * @return
	 * @throws Exception
	 *             2014年8月26日
	 */
	public List<ActNode> getWFNodeConfigInfoForOrg(String processKey,
			String userId) throws Exception;

	/**
	 * (还未开启流程实例前)获取流程组织结构类型节点配置
	 * 
	 * @param processKey
	 *            流程key
	 * @param userId
	 *            用户id，可以是工号或用户名等，需要与代办查询条件等一致即可
	 * @param orgId
	 *            用户所在部门组织id
	 * @return
	 * @throws Exception
	 *             2014年10月15日
	 */
	public List<ActNode> getWFNodeConfigInfoForOrg(String processKey,
			String userId, String orgId) throws Exception;

	/**
	 * (还未开启流程实例前)1获取流程组织结构类型节点配置与通用配置的合集 2取组织结构类型节点配置中的处理人 3取通用配置中的控制参数
	 * 
	 * @param processKey
	 *            流程key
	 * @param userId
	 *            用户id，可以是工号或用户名等，需要与代办查询条件等一致即可
	 * @param orgId
	 *            用户所在部门组织id
	 * @return
	 * @throws Exception
	 *             2014年10月15日
	 */
	public List<ActNode> getWFNodeConfigInfoForOrgAndCommmon(String processKey,
			String userId, String orgId) throws Exception;

	/**
	 * (还未开启流程实例前) 1先已组织结构类型节点配置取处理人与控制参数配置 2如果前者没有再取通用配置里的处理人与控制参数配置
	 * 
	 * @param processKey
	 *            流程key
	 * @param userId
	 *            用户id，可以是工号或用户名等，需要与代办查询条件等一致即可
	 * @param orgId
	 *            用户所在部门组织id
	 * @return
	 * @throws Exception
	 *             2014年10月15日
	 */
	public List<ActNode> getWFNodeConfigInfoForFirstOrgSecondCommmon(
			String processKey, String userId, String orgId) throws Exception;

	/**
	 * (还未开启流程实例前)获取流程业务类型节点配置
	 * 
	 * @param processKey
	 *            流程key
	 * @param typeId
	 *            业务类型id
	 * @return
	 * @throws Exception
	 *             2014年10月20日
	 */
	public List<ActNode> getWFNodeConfigInfoForbussiness(String processKey,
			String typeId) throws Exception;

	/**
	 * (还未开启流程实例前)1获取流程业务类型节点配置与通用配置的合集 2取获取流程业务类型节点配置中的处理人 3取通用配置中的控制参数
	 * 
	 * @param processKey
	 *            流程key
	 * @param typeId
	 *            业务类型id
	 * @return
	 * @throws Exception
	 *             2014年10月20日
	 */
	public List<ActNode> getWFNodeConfigInfoForbussinessAndCommmon(
			String processKey, String typeId) throws Exception;

	/**
	 * (还未开启流程实例前)1先已流程业务类型节点配置取处理人与控制参数配置 2如果前者没有再取通用配置里的处理人与控制参数配置
	 * 
	 * @param processKey
	 *            流程key
	 * @param typeId
	 *            业务类型id
	 * @return
	 * @throws Exception
	 *             2014年10月20日
	 */
	public List<ActNode> getWFNodeConfigInfoForFirstbussinessSecondCommmon(
			String processKey, String typeId) throws Exception;

	/**
	 * 获取流程通用节点配置 (还未开启流程实例前)
	 * 
	 * @param processKey
	 *            流程key
	 * @return
	 * @throws Exception
	 *             2014年8月26日
	 */
	public List<ActNode> getWFNodeConfigInfoForCommon(String processKey)
			throws Exception;

	/**
	 * 获取流程节点配置(已开启流程实例)
	 * 
	 * @param processKey
	 *            processKey
	 * @param processInstId
	 *            流程Id
	 * @param taskId
	 *            任务Id
	 * @return
	 * @throws Exception
	 *             2014年9月29日
	 */
	public List<ActNode> getWFNodeConfigInfoByCondition(String processKey,
			String processInstId, String taskId) throws Exception;

	/**
	 * 获取流程图片
	 * 
	 * @param processKey
	 *            流程key
	 * @param response
	 * @return
	 * @throws Exception
	 *             2014年8月20日
	 */
	public void getProccessPic(String processKey, OutputStream out)
			throws IOException;

	/**
	 * 获取流程追踪图片
	 * 
	 * @param processId
	 *            流程Id
	 * @param response
	 * @return
	 * @throws Exception
	 *             2014年8月20日
	 */
	public void getProccessActivePic(String processId, OutputStream out)
			throws IOException;

	/**
	 * 暂存审批表单数据
	 * 
	 * @param proIns
	 *            流程实例参数类
	 * @param businessKey
	 *            业务主键
	 * @param processKey
	 *            流程key
	 * @param paramMap
	 *            参数信息
	 * @throws Exception
	 *             2014年8月22日
	 */
	public void tempSaveFormDatas(ProIns proIns, String businessKey,
			String processKey) throws Exception;

	/**
	 * 获取暂存审批表单数据
	 * 
	 * @param businessKey
	 *            业务主键
	 * @throws Exception
	 *             2014年9月23日
	 */
	public ProIns getFormDatasByBusinessKey(String businessKey)
			throws Exception;

	/**
	 * 删除暂存审批表单数据
	 * 
	 * @param businessKey
	 *            业务主键
	 * @throws Exception
	 *             2014年9月23日
	 */
	public void delFormDatasByBusinessKey(String businessKey) throws Exception;

	/**
	 * 开启流程实例并完成第一任务
	 * 
	 * @param proIns
	 *            流程实例参数类
	 * @param businessKey
	 *            业务主键
	 * @param processKey
	 *            流程key
	 * @param paramMap
	 *            参数信息
	 * @throws Exception
	 *             2014年8月22日
	 */
	public void startProc(ProIns proIns, String businessKey, String processKey,
			Map<String, Object> paramMap) throws Exception;

	/**
	 * 开启流程实例
	 * 
	 * @param proIns
	 *            流程实例参数类
	 * @param businessKey
	 *            业务主键
	 * @param processKey
	 *            流程key
	 * @param paramMap
	 *            参数信息
	 * @param completeFirstTask
	 *            是否完成第一个任务 true完成 false 不完成
	 * @throws Exception
	 *             2014年10月20日
	 */
	public void startProc(ProIns proIns, String businessKey, String processKey,
			Map<String, Object> paramMap, boolean completeFirstTask)
			throws Exception;

	/**
	 * 跳转至处理任务页面 (不显示没有设置处理人的节点)
	 * 
	 * @param processKey
	 *            流程key
	 * @param processId
	 *            流程实例id
	 * @param taskId
	 *            任务id
	 * @param model
	 * @return
	 * @throws Exception
	 *             2014年9月29日
	 */
	public ModelMap toDealTask(String processKey, String processId,
			String taskId, ModelMap model) throws Exception;

	/**
	 * 跳转至处理任务页面 (显示没有设置处理人的节点)
	 * 
	 * @param processKey
	 *            流程key
	 * @param processId
	 *            流程实例id
	 * @param taskId
	 *            任务id
	 * @param model
	 * @return
	 * @throws Exception
	 *             2014年9月29日
	 */
	public ModelMap toDealTaskContainNoAssignerNodes(String processKey,
			String processId, String taskId, ModelMap model) throws Exception;

	/**
	 * 跳转至查看任务页面(不显示没有设置处理人的节点)
	 * 
	 * @param taskId
	 *            任务id
	 * @param bussinessKey
	 *            业务主键key
	 * @param userId
	 *            用户id，可以是工号或用户名等，需要与代办查询条件等一致即可
	 * @param model
	 * @return
	 * @throws Exception
	 *             2014年8月31日
	 */
	public ModelMap toViewTask(String taskId, String bussinessKey,
			String userId, ModelMap model) throws Exception;

	/**
	 * 跳转至查看任务页面(显示没有设置处理人的节点)
	 * 
	 * @param taskId
	 *            任务id
	 * @param bussinessKey
	 *            业务主键key
	 * @param userId
	 *            用户id，可以是工号或用户名等，需要与代办查询条件等一致即可
	 * @param model
	 * @return
	 * @throws Exception
	 *             2014年8月31日
	 */
	public ModelMap toViewTaskContainNoAssignerNodes(String taskId,
			String bussinessKey, String userId, ModelMap model)
			throws Exception;

	/**
	 * 跳转至查看任务页面(显示没有设置处理人的节点)
	 * 
	 * @param taskId
	 *            任务id
	 * @param bussinessKey
	 *            业务主键key
	 * @param userId
	 *            用户id，可以是工号或用户名等，需要与代办查询条件等一致即可
	 * @param model
	 * @param processKey
	 *            流程key
	 * @return
	 * @throws Exception
	 *             2014年10月21日
	 */
	public ModelMap toViewTaskContainNoAssignerNodes(String taskId,
			String bussinessKey, String userId, ModelMap model,
			String processKey) throws Exception;

	/**
	 * 跳转至查看任务页面(不显示没有设置处理人的节点)
	 * 
	 * @param taskId
	 *            任务id
	 * @param bussinessKey
	 *            业务主键key
	 * @param userId
	 *            用户id，可以是工号或用户名等，需要与代办查询条件等一致即可
	 * @param model
	 * @param processKey
	 *            流程key
	 * @return
	 * @throws Exception
	 *             2014年10月21日
	 */
	public ModelMap toViewTask(String taskId, String bussinessKey,
			String userId, ModelMap model, String processKey) throws Exception;

	/**
	 * 跳转至查看任务页面(显示没有设置处理人的节点)
	 * 
	 * @param taskId
	 *            任务id
	 * @param userId
	 *            用户id，可以是工号或用户名等，需要与代办查询条件等一致即可
	 * @param model
	 * @return
	 * @throws Exception
	 *             2014年8月31日
	 */
	public ModelMap toViewTaskContainNoAssignerNodes(String taskId,
			String userId, ModelMap model) throws Exception;

	/**
	 * 跳转至查看任务页面(不显示没有设置处理人的节点)
	 * 
	 * @param taskId
	 *            任务id
	 * @param userId
	 *            用户id，可以是工号或用户名等，需要与代办查询条件等一致即可
	 * @param model
	 * @return
	 * @throws Exception
	 *             2014年8月31日
	 */
	public ModelMap toViewTask(String taskId, String userId, ModelMap model)
			throws Exception;

	/**
	 * 获取流程实例的处理记录(已完成的任务)
	 * 
	 * @param processId
	 *            流程实例id
	 * @return
	 * @throws Exception
	 *             2014年8月26日
	 */
	public List<HisTaskInfo> getProcHisInfo(String processId) throws Exception;

	/**
	 * 审批处理任务(任务id)
	 * 
	 * @param proIns
	 *            流程实例参数类
	 * @param processKey
	 *            流程key
	 * @param paramMap
	 *            节点配置参数
	 * @throws Exception
	 *             2014年8月29日
	 */
	public void approveWorkFlow(ProIns proIns, String processKey,
			Map<String, Object> paramMap) throws Exception;

	/**
	 * 审批处理任务(业务key)
	 * 
	 * @param proIns
	 *            流程实例参数类
	 * @param processKey
	 *            流程key
	 * @param paramMap
	 *            节点配置参数
	 * @throws Exception
	 *             2014年8月29日
	 */
	public void approveWorkFlowByBussinesskey(ProIns proIns, String processKey,
			Map<String, Object> paramMap) throws Exception;

	/**
	 * 通过任务
	 * 
	 * @param proIns
	 *            流程实例参数类
	 * @param processKey
	 *            流程key
	 * @param paramMap
	 *            参数配置信息
	 * @throws Exception
	 *             2014年8月26日
	 */
	public void completeTask(ProIns proIns, String processKey,
			Map<String, Object> paramMap) throws Exception;

	/**
	 * 通过任务 (原始API)
	 * 
	 * @param processKey
	 *            流程key
	 * @param BusinessKey
	 *            业务key
	 * @param taskId
	 *            任务Id
	 * @param businessop
	 *            操作类型
	 * @param businessReason
	 *            操作原因
	 * @param businessRemark
	 *            操作备注
	 * @throws Exception
	 *             2014年12月1日
	 */
	public void completeTask(String processKey, String BusinessKey,
			String taskId, String businessop, String businessReason,
			String businessRemark) throws Exception;

	/**
	 * 废弃任务
	 * 
	 * @param proIns
	 *            流程实例参数类
	 * @param processKey
	 *            流程key
	 * @param paramMap
	 *            参数配置信息
	 * @throws Exception
	 *             2014年8月26日
	 */
	public void discardTask(ProIns proIns, String processKey) throws Exception;

	/**
	 * 废弃任务
	 * 
	 * @param processId
	 *            流程实例id
	 * @param processKey
	 *            流程key
	 * @param taskId
	 *            任务id
	 * @param userAccount
	 *            用户域账号
	 * @param businessop
	 *            操作类型
	 * @param businessReason
	 *            操作原因
	 * @param businessRemark
	 *            操作备注
	 * @throws Exception
	 *             2014年12月1日
	 */
	public void discardTask(String processId, String processKey, String taskId,
			String userAccount, String businessop, String businessReason,
			String businessRemark) throws Exception;

	/**
	 * 转办任务
	 * 
	 * @param proIns
	 *            流程实例参数类
	 * @param processKey
	 *            流程key
	 * @param paramMap
	 *            参数配置信息
	 * @throws Exception
	 *             2014年8月26日
	 */
	public void delegateTask(ProIns proIns, String processKey) throws Exception;

	/**
	 * 转办任务
	 * 
	 * @param processId
	 *            流程实例id
	 * @param processKey
	 *            流程key
	 * @param taskId
	 *            任务id
	 * @param delegateUser
	 *            被转办人域账号
	 * @param currentUser
	 *            当前用户/转办人域账号
	 * @param businessop
	 *            操作类型
	 * @param businessReason
	 *            操作原因
	 * @param businessRemark
	 *            操作备注
	 * @throws Exception
	 *             2014年12月1日
	 */
	public void delegateTask(String processId, String processKey,
			String taskId, String delegateUser, String currentUser,
			String businessop, String businessReason, String businessRemark)
			throws Exception;

	/**
	 * 撤销任务
	 * 
	 * @param proIns
	 *            流程实例参数类
	 * @param processKey
	 *            流程key
	 * @throws Exception
	 *             2014年8月26日
	 */
	public void cancelTask(ProIns proIns, String processKey) throws Exception;

	/**
	 * 撤销任务
	 * 
	 * @param processId
	 *            流程实例id
	 * @param processKey
	 *            流程key
	 * @param taskId
	 *            任务id
	 * @param nodeKey
	 *            节点key
	 * @param userAccount
	 *            当前用户域账号
	 * @param businessop
	 *            操作类型
	 * @param businessReason
	 *            操作原因
	 * @param businessRemark
	 *            操作备注
	 * @throws Exception
	 *             2014年12月1日
	 */
	public void cancelTask(String processId, String processKey, String taskId,
			String nodeKey, String userAccount, String businessop,
			String businessReason) throws Exception;

	/**
	 * 驳回任务
	 * 
	 * @param proIns
	 *            流程实例参数类
	 * @param processKey
	 *            流程key
	 * @param paramMap
	 *            参数配置信息
	 * @throws Exception
	 *             2014年9月1日
	 */
	public void rejectToPreTask(ProIns proIns, String processKey,
			Map<String, Object> paramMap) throws Exception;

	/**
	 * 获取用户任务总数(待办，不包括委托)
	 * 
	 * @param currentUser
	 *            当前用户(工号，域账号都可以)
	 * @param processKey
	 *            流程key
	 * @throws Exception
	 *             2014年8月27日
	 */
	public int getMyselfTaskNum(String currentUser, String processKey)
			throws Exception;

	/**
	 * 获取用户委托任务总数
	 * 
	 * @param currentUser
	 *            当前用户(工号，域账号都可以)
	 * @param processKey
	 *            流程key
	 * @throws Exception
	 *             2014年8月27日
	 */
	public int getEntrustTaskNum(String currentUser, String processKey)
			throws Exception;

	/**
	 * 获取用户待办任务数据
	 * 
	 * @param currentUser
	 *            当前用户(工号，域账号都可以)
	 * @param processKey
	 *            流程key
	 * @param offset
	 * @param pagesize
	 * @return
	 * @throws Exception
	 *             2014年8月27日
	 */
	public ListInfo getMyselfTaskList(String currentUser, String processKey,
			long offset, int pagesize) throws Exception;

	/**
	 * 获取用户委托任务数据
	 * 
	 * @param currentUser
	 *            当前用户(工号，域账号都可以)
	 * @param processKey
	 *            流程key
	 * @param offset
	 * @param pagesize
	 * @return
	 * @throws Exception
	 *             2014年8月27日
	 */
	public ListInfo getEntrustTaskList(String currentUser, String processKey,
			long offset, int pagesize) throws Exception;

	/**
	 * 获取当前节点任务信息,根据任务id
	 * 
	 * @param taskId
	 *            任务id
	 * @throws Exception
	 *             2014年8月29日
	 */
	public TaskInfo getCurrentNodeInfo(String taskId) throws Exception;

	/**
	 * 获取当前节点任务信息,根据业务key(没有权限判断)
	 * 
	 * @param bussinesskey
	 * @return TaskInfo
	 * @throws Exception
	 *             2014年9月24日
	 */
	public List<TaskInfo> getCurrentNodeInfoByBussinessKey(String bussinesskey)
			throws Exception;

	/**
	 * 获取当前节点任务信息,根据业务key(没有权限判断)
	 * 
	 * @param bussinesskey
	 *            业务key
	 * @param processKey
	 *            流程key
	 * @return
	 * @throws Exception
	 *             2014年10月21日
	 */
	public List<TaskInfo> getCurrentNodeInfoByKey(String bussinesskey,
			String processKey) throws Exception;

	/**
	 * 获取当前节点任务信息,根据业务key (有权限判断)
	 * 
	 * @param bussinesskey
	 *            业务key
	 * @param userId
	 *            用户id，可以是工号或用户名等，需要与代办查询条件等一致即可
	 * @return 有权限的TaskInfo
	 * @throws Exception
	 *             2014年9月18日
	 */
	public TaskInfo getCurrentNodeInfoByBussinessKey(String bussinesskey,
			String userId) throws Exception;

	/**
	 * 获取当前节点任务信息,根据业务key (有权限判断)
	 * 
	 * @param bussinesskey
	 *            业务key
	 * @param processKey
	 *            流程key
	 * @param userId
	 *            用户id，可以是工号或用户名等，需要与代办查询条件等一致即可
	 * @return 有权限的TaskInfo
	 * @throws Exception
	 *             2014年10月21日
	 */
	public TaskInfo getCurrentNodeInfoByKey(String bussinesskey,
			String processKey, String userId) throws Exception;

	/**
	 * 跳转到任意节点
	 * 
	 * @param nowTaskId
	 *            当前任务id
	 * @param currentUser
	 *            当前任务处理人
	 * @param map
	 *            节点变量配置参数
	 * @param destinationTaskKey
	 *            跳转到的目标节点key
	 * @param completeReason
	 *            操作备注
	 * @param bussinessop
	 *            操作类型
	 * @param bussinessRemark
	 *            操作原因
	 * @throws Exception
	 *             2014年9月5日
	 */
	public void returnToNode(String nowTaskId, String currentUser,
			Map<String, Object> map, String destinationTaskKey,
			String completeReason, String bussinessop, String bussinessRemark)
			throws Exception;

	/**
	 * 判断任务是否被签收
	 * 
	 * @param taskId
	 * @param userId
	 * @return
	 * @throws Exception
	 *             2014年9月22日
	 */
	public boolean isSignTask(String taskId, String userId) throws Exception;

	/**
	 * 根据业务KEY判断流程是否开启
	 * 
	 * @param bussinesskey
	 *            业务key
	 * @return true 开启 false 没开启
	 * @throws Exception
	 *             2014年9月18日
	 */
	public boolean isStartProcByBussinesskey(String bussinesskey)
			throws Exception;

	/**
	 * 根据KEY判断流程是否开启
	 * 
	 * @param bussinesskey
	 *            业务key
	 * @param processKey
	 *            流程key
	 * @return
	 * @throws Exception
	 *             2014年10月21日
	 */
	public boolean isStartProcByKey(String bussinesskey, String processKey)
			throws Exception;

	/**
	 * 根据流程实例id判断流程是否开启
	 * 
	 * @param processId
	 *            流程实例id
	 * @return true 开启 false 没开启
	 * @throws Exception
	 *             2014年9月18日
	 */
	public boolean isStartProcByProcessId(String processId) throws Exception;

	/**
	 * 是否抄送节点
	 * 
	 * @param nodeKey
	 *            节点key
	 * @param processKey
	 *            流程key
	 * @return
	 * @throws Exception
	 *             2014年11月18日
	 */
	public boolean isCopyNodeByKey(String nodeKey, String processKey)
			throws Exception;

	/**
	 * 驳回列表(显示没有设置处理人的节点)
	 * 
	 * @param processId
	 *            流程实例id
	 * @param currentTaskKey
	 *            当前人key
	 * @return
	 * @throws Exception
	 *             2014年10月28日
	 */
	public List<ActNode> getBackActNodeContainNoAssigner(String processId,
			String currentTaskKey) throws Exception;

	/**
	 * 驳回列表(不显示没有设置处理人的节点)
	 * 
	 * @param processId
	 *            流程实例id
	 * @param currentTaskKey
	 *            当前人key
	 * @return
	 * @throws Exception
	 *             2014年10月28日
	 */
	public List<ActNode> getBackActNode(String processId, String currentTaskKey)
			throws Exception;

	/**
	 * 完成抄送任务
	 * 
	 * @param copytaskid
	 *            抄送任务id
	 * @param copyuser
	 *            抄送人
	 * @return
	 * @throws Exception
	 *             2014年11月19日
	 */
	public void completeCopyTask(String copytaskid, String copyuser)
			throws Exception;

	/**
	 * 获取根据活动任务id获取任务的阅读记录中文名称
	 * 
	 * @param actinstid
	 *            活动任务id
	 * @return
	 */
	public String getCopyTaskReadUserNames(String actinstid) throws Exception;

	/**
	 * 分页获取根据活动任务id获取任务的阅读记录中文名称
	 * 
	 * @param actinstid
	 *            活动任务id
	 * @return
	 */
	public String getCopyTaskReadUserNames(String actinstid, int limit)
			throws Exception;

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
	public ListInfo getUserCopyTasksByKey(String process_key,
			String businesskey, long offset, int pagesize) throws Exception;

	/**
	 * 根据活动id分页获取已阅抄送任务
	 * 
	 * @param process_key
	 * @param businesskey
	 * @param offset
	 * @param pagesize
	 * @return
	 * @throws Exception
	 *             2014年11月17日
	 */
	public ListInfo getCopyTaskReadUsersByActid(String actinstid, long offset,
			int pagesize) throws Exception;

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
	public ListInfo getUserReaderCopyTasksByKey(String process_key,
			String businesskey, long offset, int pagesize) throws Exception;

}
