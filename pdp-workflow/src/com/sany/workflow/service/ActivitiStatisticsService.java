package com.sany.workflow.service;

import com.frameworkset.util.ListInfo;
import com.sany.workflow.entity.statistics.PersonalCountCondition;
import com.sany.workflow.entity.statistics.PersonalCounter;
import com.sany.workflow.entity.statistics.ProcessCountCondition;
import com.sany.workflow.entity.statistics.ProcessCounter;

/**
 * @todo 流程统计分析接口
 * @author tanx
 * @date 2014年8月13日
 * 
 */
public interface ActivitiStatisticsService {

	/**
	 * 分页查询流程统计数据
	 * 
	 * @param sortKey
	 * @param desc
	 * @param offset
	 * @param pagesize
	 * @param condition
	 * @param model
	 * @return 2014年8月13日
	 */
	public ListInfo queryProcessCountDataPage(ProcessCountCondition condition,
			long offset, int pagesize) throws Exception;

	/**
	 * 分页查询流程明细数据
	 * 
	 * @param condition
	 * @param offset
	 * @param pagesize
	 * @return
	 * @throws Exception
	 *             2014年8月14日
	 */
	public ListInfo queryProcessDetailDataPage(ProcessCountCondition condition,
			long offset, int pagesize) throws Exception;

	/**
	 * 获取流程统计效率分析数据
	 * 
	 * @param condition
	 * @param offset
	 * @param pagesize
	 * @return
	 * @throws Exception
	 *             2014年8月14日
	 */
	public ProcessCounter queryProcessAnalyseData(
			ProcessCountCondition condition) throws Exception;

	/**
	 * 分页查询个人明细数据
	 * 
	 * @param condition
	 * @param offset
	 * @param pagesize
	 * @return
	 * @throws Exception
	 *             2014年8月14日
	 */
	public ListInfo queryPersonalDetailDataPage(
			PersonalCountCondition condition, long offset, int pagesize)
			throws Exception;

	/**
	 * 分页查询个人统计数据
	 * 
	 * @param condition
	 * @param offset
	 * @param pagesize
	 * @return
	 * @throws Exception
	 *             2014年8月14日
	 */
	public ListInfo queryPersonalCountDataPage(
			PersonalCountCondition condition, long offset, int pagesize)
			throws Exception;
}
