package com.sany.workflow.job.action;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.sany.workflow.job.exception.ProcessParamCheckException;
import com.sany.workflow.service.ActivitiService;
import com.sany.workflow.service.TempleService;

public class CheckProcessOvertime {

	private TempleService templeService;
	private ActivitiService activitiService;
	// private static String rexp_completeTime =
	// "\\d{4}\\-\\d{1,2}\\-\\d{1,2}\\s\\d{1,2}:\\d{1,2}:\\d{1,2}";
	private Calendar instance = Calendar.getInstance();
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static void main(String[] args0) {
		String s1 = "2014-06-16 17:14:14.233";
		System.out.print(s1.split("\\.")[0]);
	}

	/**
	 * 检查流程超时,预警节点任务 1.查出数据库中所有正在处理和未签收的节点任务 2.检查参数是否存在为空； 3.根据用户名找到用户所属的部门ID；
	 * 4.计算最后工作完成时间 5.如果超时则发邮件
	 */
	public void checkProcessOvertimeJob() throws Exception {

		// 获取需要发送消息提醒的数据
		List<Map<String, String>> list = activitiService
				.getProcessNodeUnComplete();

		if (null != list && list.size() > 0) {
			for (Map<String, String> process : list) {

				// 检查数据
				checkPara(process);

				String alertTime = process.get("alertTime");
				String overTime = process.get("overTime");

				// 判断是否预警提醒
				if (compareTime(instance, alertTime)) {
					process.put("subject", "流程预警提醒");
					process.put("noticeType", "0");
				}

				// 判断是否超时提醒
				if (compareTime(instance, overTime)) {
					process.put("subject", "流程超时提醒");
					process.put("noticeType", "1");
				}
			}
			templeService.sendNotice(list);
		}

	}

	/**
	 * 比较当前时间和完成时间 当前时间小于完成时间返回true；大于则false
	 */

	private boolean compareTime(Calendar instance, String completeTime)
			throws Exception {

		return instance.getTime().before(df.parse(completeTime));

	}

	/**
	 * 参数检查
	 */
	private void checkPara(Map<String, String> process)
			throws ProcessParamCheckException {
		String id = process.get("taskId");
		if (null == process.get("createTime")
				|| "".equals(process.get("createTime"))) {
			throw new ProcessParamCheckException("taskId=" + id
					+ "的节点任务--创建时间为空");
		}
		// if (null == process.get("duration")
		// || "".equals(process.get("duration"))) {
		// throw new ProcessParamCheckException("taskId=" + id
		// + "的节点任务--工时长度为空");
		// }

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
		// if (null == process.get("isContainHoliday")
		// || "".equals(process.get("isContainHoliday"))) {
		// throw new ProcessParamCheckException("taskId=" + id
		// + "的节点任务--完成时间计算方式为空");
		// }
		/*
		 * if(null ==process.get("noticeRate")
		 * ||"".equals(process.get("noticeRate"))){ throw new
		 * ProcessParamCheckException("taskId="+id+"的节点任务--预警比例为空"); }
		 */
	}
}
