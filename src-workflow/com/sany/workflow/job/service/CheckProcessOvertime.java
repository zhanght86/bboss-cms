package com.sany.workflow.job.service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.sany.workflow.job.exception.ProcessParamCheckException;
import com.sany.workflow.service.ActivitiService;
import com.sany.workflow.service.TempleService;

public class CheckProcessOvertime {

	private TempleService templeService;
	private ActivitiService activitiService;
	private Calendar instance = Calendar.getInstance();

	/**
	 * @throws Exception
	 *             2014年7月24日
	 */
	public void checkProcessOvertimeJob() throws Exception {
		// 获取需要发送消息提醒的数据
		List<Map<String, Object>> list = activitiService
				.getProcessNodeUnComplete();

		if (null != list && list.size() > 0) {
			for (Map<String, Object> process : list) {

				// 检查数据
				checkPara(process);

				Timestamp alertTime = (Timestamp) process.get("alertTime");
				Timestamp overTime = (Timestamp) process.get("overTime");

				// 判断是否预警提醒
				if (compareTime(instance, alertTime)) {
					process.put("subject", "流程预警提醒");
					process.put("noticeType", "1");
				}

				// 判断是否超时提醒
				if (compareTime(instance, overTime)) {
					process.put("subject", "流程超时提醒");
					process.put("noticeType", "2");
				}
			}
			templeService.sendNotice(list);
		}

	}

	/**
	 * 比较当前时间和完成时间 当前时间小于完成时间返回true；大于则false
	 * 
	 * @param instance
	 * @param completeTime
	 * @return
	 * @throws Exception
	 *             2014年7月24日
	 */
	private boolean compareTime(Calendar instance, Timestamp completeTime)
			throws Exception {

		return instance.getTime().after(completeTime);

	}

	/**
	 * 参数检查
	 * 
	 * @param process
	 * @throws ProcessParamCheckException
	 *             2014年7月24日
	 */
	private void checkPara(Map<String, Object> process)
			throws ProcessParamCheckException {
		String id = process.get("taskId") + "";

		if (null == process.get("createTime")
				|| "".equals(process.get("createTime"))) {
			throw new ProcessParamCheckException("taskId=" + id
					+ "的节点任务--创建时间为空");
		}

		if (null == process.get("processName")
				|| "".equals(process.get("processName"))) {
			throw new ProcessParamCheckException("taskId=" + id
					+ "的节点任务--流程名称为空");
		}

		if (null == process.get("taskName")
				|| "".equals(process.get("taskName"))) {
			throw new ProcessParamCheckException("taskId=" + id
					+ "的节点任务--节点名称为空");
		}
		if (null == process.get("userId") || "".equals(process.get("userId"))) {
			throw new ProcessParamCheckException("taskId=" + id
					+ "的节点任务--用户名为空");
		}

		if (null == process.get("orgId") || "".equals(process.get("orgId"))) {
			throw new ProcessParamCheckException("taskId=" + id
					+ "的节点任务--部门编码为空");
		}
	}
}
