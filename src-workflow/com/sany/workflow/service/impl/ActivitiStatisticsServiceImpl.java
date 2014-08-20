package com.sany.workflow.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.platform.sysmgrcore.purviewmanager.db.FunctionDB;
import com.frameworkset.util.ListInfo;
import com.frameworkset.util.StringUtil;
import com.sany.workflow.entity.TaskManager;
import com.sany.workflow.entity.statistics.PersonalCountCondition;
import com.sany.workflow.entity.statistics.PersonalCounter;
import com.sany.workflow.entity.statistics.PersonalDealTask;
import com.sany.workflow.entity.statistics.ProcessCountCondition;
import com.sany.workflow.entity.statistics.ProcessCounter;
import com.sany.workflow.service.ActivitiService;
import com.sany.workflow.service.ActivitiStatisticsService;
import com.sany.workflow.service.ProcessException;

/**
 * @todo 流程统计分析实现类
 * @author tanx
 * @date 2014年8月13日
 * 
 */
public class ActivitiStatisticsServiceImpl implements
		ActivitiStatisticsService, org.frameworkset.spi.DisposableBean {

	private com.frameworkset.common.poolman.ConfigSQLExecutor executor;

	private ActivitiService activitiService;

	@Override
	public void destroy() throws Exception {

	}

	@Override
	public ListInfo queryProcessCountDataPage(ProcessCountCondition condition,
			long offset, int pagesize) throws Exception {
		ListInfo listInfo = null;

		// 所属应用
		if (StringUtil.isNotEmpty(condition.getApp())) {
			condition.setApp("%" + condition.getApp() + "%");
		}

		listInfo = executor.queryListInfoBean(ProcessCounter.class,
				"getProcessCountDataPage", offset, pagesize, condition);

		return listInfo;
	}

	@Override
	public ListInfo queryProcessDetailDataPage(ProcessCountCondition condition,
			long offset, int pagesize) throws Exception {

		ListInfo listInfo = null;

		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();

			listInfo = executor.queryListInfoBean(ProcessCounter.class,
					"getProcessDetailDataPage", offset, pagesize, condition);

			List<ProcessCounter> processList = listInfo.getDatas();

			if (processList != null && processList.size() > 0) {

				for (int i = 0; i < processList.size(); i++) {
					ProcessCounter pc = processList.get(i);

					// 发起人转换
					pc.setStartUserName(activitiService.getUserInfoMap()
							.getUserName(pc.getStartUserId()));

					// 节点耗时转换
					if (StringUtil.isNotEmpty(pc.getDuration())) {
						long mss = Long.parseLong(pc.getDuration());
						pc.setDuration(StringUtil.formatTimeToString(mss));
					} else {
						Date startTime = pc.getStartTime();
						pc.setDuration(StringUtil.formatTimeToString(new Date()
								.getTime() - startTime.getTime()));
					}
				}
			}

			tm.commit();
			return listInfo;

		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			tm.release();
		}
	}

	@Override
	public ProcessCounter queryProcessAnalyseData(
			ProcessCountCondition condition) throws Exception {
		ProcessCounter pc = new ProcessCounter();

		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();

			HashMap map = executor.queryObjectBean(HashMap.class,
					"getProcessPassNumAndWorkTime", condition);

			if (map != null) {
				// 办结条数 (完成流程数)
				pc.setPassNum(((BigDecimal) map.get("PASSNUM")).intValue());

				// 平均办结工时 (完成流程时间之和/完成流程数)
				if (StringUtil.isNotEmpty(map.get("WORKTIME"))) {
					DecimalFormat df = new DecimalFormat("#.00");
					long mss = Long.parseLong(map.get("WORKTIME") + "");
					double hour = mss * 1.0 / (1000 * 60 * 60);
					pc.setAvgWorkTime(df.format(hour));
				}

				if (pc.getPassNum() != 0) {
					// 平均转办次数 (完成流程中的转办人次之和/完成流程数)
					HashMap delegateNumMap = executor.queryObjectBean(
							HashMap.class, "getProcessDelegateNum", condition);

					int delegateNum = ((BigDecimal) delegateNumMap
							.get("DELEGATENUM")).intValue();
					pc.setAvgDelegateNum(delegateNum * 1.0 / pc.getPassNum());

					// 平均驳回/废弃/撤销次数
					HashMap maps = executor.queryObjectBean(HashMap.class,
							"getProcessCancelNumAndDiscardNumAndRejectNum",
							condition);

					int discardNum = ((BigDecimal) maps.get("DISCARDNUM"))
							.intValue();
					pc.setAvgDiscardNum(discardNum * 1.0 / pc.getPassNum());

					int cancelNum = ((BigDecimal) maps.get("CANCELNUM"))
							.intValue();
					pc.setAvgCancelNum(cancelNum * 1.0 / pc.getPassNum());

					int rejectNum = ((BigDecimal) maps.get("REJECTNUM"))
							.intValue();
					pc.setAvgRejectNum(rejectNum * 1.0 / pc.getPassNum());

					// 平均办理人次(个人处理次数+驳回+废弃+撤销+转办)
					HashMap PersonlDealMap = executor.queryObjectBean(
							HashMap.class, "getProcessPersonlDealNum",
							condition);

					int personldealnum = ((BigDecimal) PersonlDealMap
							.get("PERSONLDEALNUM")).intValue();
					pc.setAvgPassNum((discardNum + cancelNum + rejectNum
							+ personldealnum + delegateNum)
							* 1.0 / pc.getPassNum());
				}
			}

			tm.commit();
			return pc;

		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			tm.release();
		}
	}

	@Override
	public ListInfo queryPersonalCountDataPage(
			PersonalCountCondition condition, long offset, int pagesize)
			throws Exception {// userID

		// 用户姓名
		if (StringUtil.isNotEmpty(condition.getRealName())) {
			condition.setRealName("%" + condition.getRealName() + "%");
		}

		ListInfo listInfo = null;

		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();

			listInfo = executor.queryListInfoBean(PersonalCounter.class,
					"getUserInfo", offset, pagesize, condition);

			List<PersonalCounter> personalList = listInfo.getDatas();

			if (personalList != null && personalList.size() > 0) {
				for (int i = 0; i < personalList.size(); i++) {

					PersonalCounter pc = personalList.get(i);

					// 所属部门全称
					pc.setOrgName(FunctionDB.getUserorgjobinfos(pc.getUserId()));

					condition.setUserName(pc.getUserName());

					// 启动流程数
					HashMap startNumMap = executor.queryObjectBean(
							HashMap.class, "getStartProcessNum", condition);

					int startNum = ((BigDecimal) startNumMap.get("STARTNUM"))
							.intValue();
					pc.setStartNum(startNum);

					// 转办/委托次数
					HashMap map = executor.queryObjectBean(HashMap.class,
							"getDelegateNumAndEntrustNum", condition);

					int delegateNum = ((BigDecimal) map.get("DELEGATENUM"))
							.intValue();
					pc.setDelegateNum(delegateNum);
					int delegatedNum = ((BigDecimal) map.get("DELEGATEDNUM"))
							.intValue();
					pc.setDelegatedNum(delegatedNum);
					int entrustNum = ((BigDecimal) map.get("ENTRUSTNUM"))
							.intValue();
					pc.setEntrustNum(entrustNum);
					int entrustedNum = ((BigDecimal) map.get("ENTRUSTEDNUM"))
							.intValue();
					pc.setEntrustedNum(entrustedNum);

					// 驳回/撤销/废弃次数
					HashMap maps = executor.queryObjectBean(HashMap.class,
							"getRejectNumAndCancelNumAndDiscardNum", condition);

					int rejectNum = ((BigDecimal) maps.get("REJECTNUM"))
							.intValue();
					pc.setRejectNum(rejectNum);
					int cancelNum = ((BigDecimal) maps.get("CANCELNUM"))
							.intValue();
					pc.setCancelNum(cancelNum);
					int discardNum = ((BigDecimal) maps.get("DISCARDNUM"))
							.intValue();
					pc.setDiscardNum(discardNum);

					// 处理总数 = 驳回 +撤销+废弃+转办+委托 总数

					pc.setDealNum(rejectNum + cancelNum + discardNum
							+ delegateNum + entrustNum);
				}
			}

			tm.commit();
			return listInfo;

		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			tm.release();
		}
	}

	@Override
	public ListInfo queryPersonalDetailDataPage(
			PersonalCountCondition condition, long offset, int pagesize)
			throws Exception {
		ListInfo listInfo = null;

		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();

			// 查询个人明细数据，获取流程信息
			listInfo = executor.queryListInfoBean(PersonalCounter.class,
					"getProcessPage", offset, pagesize, condition);

			List<PersonalCounter> personalList = listInfo.getDatas();

			if (personalList != null && personalList.size() > 0) {

				for (int i = 0; i < personalList.size(); i++) {

					PersonalCounter pc = personalList.get(i);

					// 获取用户处理任务信息
					List<PersonalDealTask> dealTaskList = executor.queryList(
							PersonalDealTask.class, "getDealTaskInfo",
							condition.getUserName(), pc.getProcessId(),
							condition.getUserName(), pc.getProcessId(),
							condition.getUserName(), pc.getProcessId(),
							condition.getUserName(), pc.getProcessId());

					if (dealTaskList != null && dealTaskList.size() > 0) {

						for (int j = 0; j < dealTaskList.size(); j++) {
							PersonalDealTask pdt = dealTaskList.get(j);

							// 处理时间转换
							if (StringUtil.isNotEmpty(pdt.getDuration())) {
								long duration = Long.parseLong(pdt
										.getDuration());
								pdt.setDuration(StringUtil
										.formatTimeToString(duration));
							}

							// 节点工时转换
							if (StringUtil.isNotEmpty(pdt.getDurationNode())) {
								long durationNode = Long.parseLong(pdt
										.getDurationNode());
								pdt.setDurationNode(StringUtil
										.formatTimeToString(durationNode));
							}

							// 是否超时预警判断
							judgeOverTime(pdt);
						}

					}

					pc.setDealTaskList(dealTaskList);

				}
			}

			tm.commit();
			return listInfo;

		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			tm.release();
		}
	}

	public void judgeOverTime(PersonalDealTask pdt) {
		try {
			Date endDate = null;// 任务结束时间点

			Calendar c = Calendar.getInstance();
			c.setTime(pdt.getEndTime());
			endDate = c.getTime();

			// 判断超时
			if (pdt.getOverTime() != null) {
				Date overDate = pdt.getOverTime();

				// 超时判断
				if (endDate.after(overDate)) {
					pdt.setIsOverTime("1");// 超时
				} else {
					pdt.setIsOverTime("0");// 没有超时
				}
			} else {
				pdt.setIsOverTime("0");// 没有超时
			}

			// 判断预警
			if (pdt.getAlertTime() != null) {
				Date alertDate = pdt.getAlertTime();// 节点任务预警时间点

				// 预警判断
				if (endDate.after(alertDate)) {
					pdt.setIsAlertTime("1");// 预警
				} else {
					pdt.setIsAlertTime("0");// 未预警
				}
			} else {
				pdt.setIsAlertTime("0");// 未预警
			}
		} catch (Exception e) {
			throw new ProcessException(e);
		}
	}

}
