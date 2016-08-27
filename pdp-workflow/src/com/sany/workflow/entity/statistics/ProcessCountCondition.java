/*
 * @(#)ProcessDefCondition.java
 * 
 * Copyright @ 2001-2012 SANY Group Co.,Ltd.
 * All right reserved.
 * 
 * 这个软件是属于三一集团有限公司机密的和私有信息，不得泄露。
 * 并且只能由三一集团有限公司内部员工在得到许可的情况下才允许使用。
 * This software is the confidential and proprietary information
 * of SANY Group Co, Ltd. You shall not disclose such
 * Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with
 * SANY Group Co, Ltd.
 */
package com.sany.workflow.entity.statistics;

import java.sql.Timestamp;

import org.frameworkset.util.annotations.RequestParam;

/**
 * @todo 流程统计查询条件
 * @author tanx
 * @date 2014年5月8日
 * 
 */
public class ProcessCountCondition {

	private String app;// 所属应用

	@RequestParam(dateformat = "yyyy-MM-dd")
	private Timestamp count_start_time;// 统计开始时间

	@RequestParam(dateformat = "yyyy-MM-dd")
	private Timestamp count_end_time;// 统计结束时间

	private String businessType;// 业务类型

	private String processKey;// 流程key

	public String getProcessKey() {
		return processKey;
	}

	public void setProcessKey(String processKey) {
		this.processKey = processKey;
	}

	public String getApp() {
		return app;
	}

	public void setApp(String app) {
		this.app = app;
	}

	public Timestamp getCount_start_time() {
		return count_start_time;
	}

	public void setCount_start_time(Timestamp count_start_time) {
		this.count_start_time = count_start_time;
	}

	public Timestamp getCount_end_time() {
		return count_end_time;
	}

	public void setCount_end_time(Timestamp count_end_time) {
		this.count_end_time = count_end_time;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

}
