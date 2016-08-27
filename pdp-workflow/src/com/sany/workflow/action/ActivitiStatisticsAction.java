package com.sany.workflow.action;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.frameworkset.util.annotations.PagerParam;
import org.frameworkset.web.servlet.ModelMap;

import com.frameworkset.util.ListInfo;
import com.sany.workflow.entity.statistics.PersonalCountCondition;
import com.sany.workflow.entity.statistics.PersonalCounter;
import com.sany.workflow.entity.statistics.ProcessCountCondition;
import com.sany.workflow.entity.statistics.ProcessCounter;
import com.sany.workflow.service.ActivitiStatisticsService;

/**
 * 流程统计分析模块
 * 
 * @todo
 * @author tanx
 * @date 2014年8月13日
 * 
 */
public class ActivitiStatisticsAction {

	private ActivitiStatisticsService statisticsService;

	/**
	 * 跳转至流程统计页面
	 * 
	 * @param model
	 * @return 2014年8月13日
	 */
	public String toProcessCount(ModelMap model) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());

		// 当天+1 例如2014-08-18 00:00:00
		c.add(Calendar.DATE, 1);
		model.addAttribute("count_end_time", sdf.format(c.getTime()));

		// 获取当月第一天 例如2014-08-01 00:00:00
		c.set(Calendar.DATE, 1);
		model.addAttribute("count_start_time", sdf.format(c.getTime()));

		return "path:processCount";
	}

	/**
	 * 跳转至个人统计页面
	 * 
	 * @param model
	 * @return 2014年8月13日
	 */
	public String toPersonalCount(ModelMap model) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());

		// 当天+1 例如2014-08-18 00:00:00
		c.add(Calendar.DATE, 1);
		model.addAttribute("count_end_time", sdf.format(c.getTime()));

		// 获取当月第一天 例如2014-08-01 00:00:00
		c.set(Calendar.DATE, 1);
		model.addAttribute("count_start_time", sdf.format(c.getTime()));

		return "path:personalCount";
	}

	/**
	 * 分页查询个人统计数据
	 * 
	 * @param sortKey
	 * @param desc
	 * @param offset
	 * @param pagesize
	 * @param condition
	 * @param model
	 * @return
	 * @throws Exception
	 *             2014年8月14日
	 */
	public String queryPersonalCountData(
			@PagerParam(name = PagerParam.SORT, defaultvalue = "") String sortKey,
			@PagerParam(name = PagerParam.DESC, defaultvalue = "false") boolean desc,
			@PagerParam(name = PagerParam.OFFSET) long offset,
			@PagerParam(name = PagerParam.PAGE_SIZE, defaultvalue = "10") int pagesize,
			PersonalCountCondition condition, ModelMap model) throws Exception {

		ListInfo listInfo = statisticsService.queryPersonalCountDataPage(
				condition, offset, pagesize);

		model.addAttribute("listInfo", listInfo);

		return "path:personalCountList";

	}

	/**
	 * 分页查询个人明细数据
	 * 
	 * @param sortKey
	 * @param desc
	 * @param offset
	 * @param pagesize
	 * @param condition
	 * @param model
	 * @return 2014年8月14日
	 */
	public String queryPersonalDetailData(
			@PagerParam(name = PagerParam.SORT, defaultvalue = "") String sortKey,
			@PagerParam(name = PagerParam.DESC, defaultvalue = "false") boolean desc,
			@PagerParam(name = PagerParam.OFFSET) long offset,
			@PagerParam(name = PagerParam.PAGE_SIZE, defaultvalue = "10") int pagesize,
			PersonalCountCondition condition, ModelMap model) throws Exception {

		ListInfo listInfo = statisticsService.queryPersonalDetailDataPage(
				condition, offset, pagesize);

		model.addAttribute("listInfo", listInfo);

		return "path:viewPersonalInfo";

	}

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
	public String queryProcessCountData(
			@PagerParam(name = PagerParam.SORT, defaultvalue = "") String sortKey,
			@PagerParam(name = PagerParam.DESC, defaultvalue = "false") boolean desc,
			@PagerParam(name = PagerParam.OFFSET) long offset,
			@PagerParam(name = PagerParam.PAGE_SIZE, defaultvalue = "10") int pagesize,
			ProcessCountCondition condition, ModelMap model) throws Exception {

		ListInfo listInfo = statisticsService.queryProcessCountDataPage(
				condition, offset, pagesize);

		model.addAttribute("listInfo", listInfo);

		return "path:processCountList";

	}

	/**
	 * 分页查询流程明细数据
	 * 
	 * @param sortKey
	 * @param desc
	 * @param offset
	 * @param pagesize
	 * @param condition
	 * @param model
	 * @return 2014年8月14日
	 */
	public String queryProcessDetailData(
			@PagerParam(name = PagerParam.SORT, defaultvalue = "") String sortKey,
			@PagerParam(name = PagerParam.DESC, defaultvalue = "false") boolean desc,
			@PagerParam(name = PagerParam.OFFSET) long offset,
			@PagerParam(name = PagerParam.PAGE_SIZE, defaultvalue = "10") int pagesize,
			ProcessCountCondition condition, ModelMap model) throws Exception {

		ListInfo listInfo = statisticsService.queryProcessDetailDataPage(
				condition, offset, pagesize);

		model.addAttribute("listInfo", listInfo);

		return "path:viewProcessInfo";

	}

	/**
	 * 跳转至流程效率分析页面
	 * 
	 * @param model
	 * @return 2014年8月13日
	 */
	public String toProcessAnalyse(ProcessCountCondition condition,
			ModelMap model) throws Exception {

		ProcessCounter pc = statisticsService
				.queryProcessAnalyseData(condition);

		model.addAttribute("ProcessCounter", pc);

		return "path:viewProcessAnalyse";
	}

	/**
	 * 跳转至个人效率分析页面
	 * 
	 * @param model
	 * @return 2014年8月13日
	 */
	public String toPersonalAnalyse(PersonalCountCondition condition,
			ModelMap model) throws Exception {

		DecimalFormat df = new DecimalFormat("#.00");

		Timestamp startTime = condition.getCount_start_time();
		Timestamp endTime = condition.getCount_end_time();

		// 统计日期时间差
		long mss = endTime.getTime() - startTime.getTime();
		double hour = mss * 1.0 / (1000 * 60 * 60);

		PersonalCounter pc = new PersonalCounter();
		// 启动率
		if (condition.getStartNum() != 0) {
			pc.setStartEff(df.format(hour / condition.getStartNum()));
		}

		// 处理效率
		if (condition.getDealNum() != 0) {
			pc.setDealEff(df.format(hour / condition.getDealNum()));
		}

		// 转办效率
		if (condition.getDelegateNum() != 0) {
			pc.setDelegateEff(df.format(hour / condition.getDelegateNum()));
		}

		// 委托效率
		if (condition.getEntrustNum() != 0) {
			pc.setEntrustEff(df.format(hour / condition.getEntrustNum()));
		}

		// 被委托效率
		if (condition.getEntrustedNum() != 0) {
			pc.setEntrustedEff(df.format(hour / condition.getEntrustedNum()));
		}

		// 驳回效率
		if (condition.getRejectNum() != 0) {
			pc.setRejectEff(df.format(hour / condition.getRejectNum()));
		}

		// 撤销效率
		if (condition.getCancelNum() != 0) {
			pc.setCancelEff(df.format(hour / condition.getCancelNum()));
		}

		// 废弃效率
		if (condition.getDiscardNum() != 0) {
			pc.setDiscardEff(df.format(hour / condition.getDiscardNum()));
		}

		model.addAttribute("PersonalCounter", pc);

		return "path:viewPersonalAnalyse";
	}
}