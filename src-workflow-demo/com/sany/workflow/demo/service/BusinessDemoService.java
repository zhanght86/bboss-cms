package com.sany.workflow.demo.service;

import com.frameworkset.util.ListInfo;

/**
 * 任务管理业务接口
 * 
 * @todo
 * @author tanx
 * @date 2014年5月27日
 * 
 */
public interface BusinessDemoService {
	/**
	 * demo演示实例获取业务单数据
	 * 
	 * @param processKey
	 * @param offset
	 * @param pagesize
	 * @return
	 * @throws Exception
	 *             2014年10月13日
	 */
	public ListInfo queryDemoData(String processKey, String businessKey,
			long offset, int pagesize) throws Exception;

}
