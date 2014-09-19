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
	 * @param businessKey
	 *            业务主键
	 * @param userAccount
	 *            当前用户
	 * @return 2014年8月20日
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
	 * @param businessKey
	 *            业务主键
	 * @param userAccount
	 *            当前用户
	 * @return 2014年8月20日
	 */
	public boolean judgeAuthority(String taskId, String processKey,
			String userAccount);

	/**
	 * 获取流程组织结构类型节点配置 (还未开启流程实例前)
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
	 * 获取流程业务类型节点配置 (还未开启流程实例前)
	 * 
	 * @param processKey
	 *            流程key
	 * @return
	 * @throws Exception
	 *             2014年8月26日
	 */
	public List<ActNode> getWFNodeConfigInfoForbussiness(String processKey)
			throws Exception;

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
	 *            流程key
	 * @param processInstId
	 *            流程Id
	 * @return
	 * @throws Exception
	 *             2014年8月21日
	 */
	public List<ActNode> getWFNodeConfigInfoByCondition(String processKey,
			String processInstId, String taskId) throws Exception;

	/**
	 * 获取流程节点信息(不带串并行切换信息)
	 * 
	 * @param processKey
	 *            流程key
	 * @param processInstId
	 *            流程Id
	 * @return
	 * @throws Exception
	 *             2014年8月21日
	 */
	public List<ActNode> getWFNodeInfoByCondition(String processKey,
			String processInstId) throws Exception;

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
	 * @throws Exception
	 *             2014年8月22日
	 */
	public void startProc(ProIns proIns, String businessKey, String processKey,
			Map<String, Object> paramMap) throws Exception;

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
	 * 跳转至处理任务页面
	 * 
	 * @param processKey
	 *            流程key
	 * @param processId
	 *            流程实例id
	 * @param taskId
	 *            任务id
	 * @return
	 * @throws Exception
	 *             2014年8月23日
	 */
	public ModelMap toDealTask(String processKey, String processId,
			String taskId, ModelMap model) throws Exception;

	/**
	 * 跳转至查看任务页面
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
	 * 获取流程可驳回到的节点信息
	 * 
	 * @param processId
	 *            流程实例id
	 * @param currentTaskKey
	 *            任务key ，如 usertask1
	 * @return
	 * @throws Exception
	 *             2014年8月28日
	 */
	public List<ActNode> getBackActNode(String processId, String currentTaskKey)
			throws Exception;

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
	 * 获取当前节点任务信息,根据任务id
	 * 
	 * @param taskId
	 *            任务id
	 * @throws Exception
	 *             2014年8月29日
	 */
	public TaskInfo getCurrentNodeInfo(String taskId) throws Exception;

	/**
	 * 获取当前节点任务信息,根据业务key
	 * 
	 * @param bussinesskey
	 *            业务key
	 * @param userId
	 *            用户id，可以是工号或用户名等，需要与代办查询条件等一致即可
	 * @return
	 * @throws Exception
	 *             2014年9月18日
	 */
	public TaskInfo getCurrentNodeInfoByBussinessKey(String bussinesskey,
			String userId) throws Exception;

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
	 *            备注
	 * @throws Exception
	 *             2014年9月5日
	 */
	public void returnToNode(String nowTaskId, String currentUser,
			Map<String, Object> map, String destinationTaskKey,
			String completeReason) throws Exception;

	/**
	 * 判断任务是否被签收
	 * 
	 * @param taskId
	 *            任务id
	 * @return
	 * @throws Exception
	 *             2014年9月18日
	 */
	public boolean isSignTask(String taskId) throws Exception;

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
	 * 根据流程实例id判断流程是否开启
	 * 
	 * @param processId
	 *            流程实例id
	 * @return true 开启 false 没开启
	 * @throws Exception
	 *             2014年9月18日
	 */
	public boolean isStartProcByProcessId(String processId) throws Exception;

}
