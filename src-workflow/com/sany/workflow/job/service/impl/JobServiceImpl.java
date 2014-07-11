package com.sany.workflow.job.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import com.frameworkset.common.poolman.Record;
import com.frameworkset.common.poolman.handle.NullRowHandler;

import com.sany.workflow.job.service.JobService;


public class JobServiceImpl implements JobService {

	private com.frameworkset.common.poolman.ConfigSQLExecutor executor;
	
	/**
	 * 查询所有正在处理或者未签收的流程节点
	 * 
	 * @return List<Process>
	 */
		
	@Override
	public List<Map<String, String>> getProcessNodeUnComplete() throws Exception {
		final	List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		
			executor.queryByNullRowHandler(new NullRowHandler() {
				@Override
				public void handleRow(Record origine) throws Exception {
					Map<String, String> map = new HashMap<String, String>();
					map.put("state",origine.getString("state"));//任务状态（1未签收2未处理）
					map.put("taskId",origine.getString("taskId"));//--任务id
					map.put("taskName",origine.getString("taskName"));//--任务名称
					map.put("createTime",origine.getString("createTime"));//--任务创建时间
					map.put("userId",origine.getString("userId"));//--处理人
					map.put("processName",origine.getString("processName"));//--流程名称
					map.put("duration",origine.getString("duration"));//--处理工时（毫秒）
					map.put("procInstanceId",origine.getString("procInstanceId"));//--流程实例id
					map.put("taskDefKey",origine.getString("taskDefKey"));//--任务节点key
					map.put("noticeRate",origine.getString("noticeRate"));//--预警频率
					map.put("messageTempleId",origine.getString("messageTempleId"));//短信模板id
					map.put("emailTempleId",origine.getString("emailTempleId"));//邮件模板id
					map.put("isContainHoliday",origine.getString("isContainHoliday"));//工时规则0 ：24时工作制 1： 自然日 2 ：正常工作制
					map.put("realName",origine.getString("realName"));//处理人真实姓名
					map.put("orgId",origine.getString("orgId"));//处理人所在部门
					map.put("mobile",origine.getString("mobile"));//手机号码
					map.put("mailAddress", origine.getString("userId")+"@sany.com.cn");//邮箱地址
					list.add(map);
				}
			}, "getProcessNodeUnComplete");
			
		
		return list;
	}
	

}