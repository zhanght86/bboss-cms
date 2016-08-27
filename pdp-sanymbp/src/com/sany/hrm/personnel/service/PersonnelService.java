package com.sany.hrm.personnel.service;

import java.util.List;
import java.util.Map;

import com.sany.hrm.workflow.dto.WfNodeDto;

/**
 * 人事事务
 * 
 * @author gw_yaoht
 * @version 2012-8-15
 **/
public interface PersonnelService {
	/**
	 * 查询
	 * 
	 * @param warrantId
	 *            凭证标识
	 * @param taskId
	 *            待办任务标识
	 * @return 可回退待办节点
	 * @throws Exception
	 */
	List<WfNodeDto> findDimissionTaskBackNode(String warrantId, String taskId) throws Exception;

	/**
	 * 获取
	 * 
	 * @param warrantId
	 *            凭证标识
	 * @param taskId
	 *            待办任务标识
	 * 
	 * @return 离司待办
	 * @throws Exception
	 */
	Map<String, Object> getDimissionTask(String warrantId, String taskId) throws Exception;

	/**
	 * 提交待办
	 * 
	 * @param warrantId
	 *            凭证标识
	 * @param model
	 *            参数
	 * 
	 * @return 待办处理结果
	 * @throws Exception
	 */
	String submitDimissionTask(String warrantId, Map<String, Object> model) throws Exception;

}
