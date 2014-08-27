package com.sany.workflow.business.service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.frameworkset.web.servlet.ModelMap;

import com.sany.workflow.business.entity.ActNode;
import com.sany.workflow.business.entity.HisTaskInfo;
import com.sany.workflow.business.entity.ProIns;

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
	 * 权限判断
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
			String businessKey, String userAccount);

	/**
	 * 获取流程组织结构类型节点配置 (还未开启流程实例前)
	 * 
	 * @param processKey
	 *            流程key
	 * @param businessType
	 *            0 通用配置 1组织结构配置2业务类型配置
	 * @param userId
	 *            用户id，可以是工号或用户名等，需要与代办查询条件等一致即可
	 * @return
	 * @throws Exception
	 *             2014年8月21日
	 */
	public List<ActNode> getWFNodeConfigInfoForOrg(String processKey,
			String userId) throws Exception;

	/**
	 * 获取流程业务类型节点配置 (还未开启流程实例前)
	 * 
	 * @param processKey
	 *            流程key
	 * @param businessType
	 *            0 通用配置 1组织结构配置2业务类型配置
	 * @param userId
	 *            用户id，可以是工号或用户名等，需要与代办查询条件等一致即可
	 * @return
	 * @throws Exception
	 *             2014年8月21日
	 */
	public List<ActNode> getWFNodeConfigInfoForbussiness(String processKey,
			String userId) throws Exception;

	/**
	 * 获取流程通用节点配置 (还未开启流程实例前)
	 * 
	 * @param processKey
	 *            流程key
	 * @param businessType
	 *            0 通用配置 1组织结构配置2业务类型配置
	 * @param userId
	 *            用户id，可以是工号或用户名等，需要与代办查询条件等一致即可
	 * @return
	 * @throws Exception
	 *             2014年8月21日
	 */
	public List<ActNode> getWFNodeConfigInfoForCommon(String processKey,
			String userId) throws Exception;

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
	public List<HisTaskInfo> getProcHisInfo(String processId) throws Exception;

	/**
	 * 跳转至处理任务页面
	 * 
	 * @param processKey
	 * @param processId
	 * @param taskId
	 * @param userId
	 * @return
	 * @throws Exception
	 *             2014年8月23日
	 */
	public ModelMap toDealTask(String processKey, String processId,
			String taskId, String userId, ModelMap model) throws Exception;

}
