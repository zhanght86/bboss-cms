package com.sany.workflow.job.service;

import java.util.List;
import java.util.Map;




public interface JobService {
	
	/**
	 * 查询所有正在处理或者未签收的流程节点
	 * 
	 * @return List<Process>
	 */
	
	public List<Map<String, String>> getProcessNodeUnComplete()throws Exception;
}