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
		try{
			executor.queryByNullRowHandler(new NullRowHandler() {

				@Override
				public void handleRow(Record origine) throws Exception {
					Map<String, String> map = new HashMap<String, String>();
					map.put("state",origine.getString("state"));
					map.put("taskId",origine.getString("id"));
					map.put("taskName",origine.getString("taskName"));
					map.put("createTime",origine.getString("createTime"));
					map.put("userId",origine.getString("userId"));
					map.put("processName",origine.getString("processName"));
					map.put("duration",origine.getString("duration"));
					map.put("simpleDate",origine.getString("simpleDate"));
					map.put("messageTempleId",origine.getString("messageTempleId"));
					map.put("emailTempleId",origine.getString("emailTempleId"));
					map.put("realName",origine.getString("realName"));
					map.put("orgId",origine.getString("orgId"));
					map.put("mobile",origine.getString("mobile"));
					map.put("mailAddress", origine.getString("userId")+"@sany.com.cn");
					map.put("subject", origine.getString("节点任务超时通知"));
					list.add(map);
				}
			}, "getProcessNodeUnComplete");
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	

}