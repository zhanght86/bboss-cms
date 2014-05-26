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
package com.sany.workflow.entity;

/**
 * @todo 流程实例查询条件
 * @author tanx
 * @date 2014年5月8日
 * 
 */
public class ProcessInstCondition {

	// 流程实例id
	private String wf_Inst_Id;

	// 流程实例开始时间
	private String wf_start_time1;
	private String wf_start_time2;

	// 流程实例结束时间
	private String wf_end_time1;
	private String wf_end_time2;

	// 流程定义id
	private String wf_denf_id;

	// 流程定义key
	private String wf_key;
	// 流程状态
	private String wf_state;
	// 流程版本
	private String[] wf_versions;
	
	// 流程状态
	private String wf_business_key;

	public String getWf_business_key() {
		return wf_business_key;
	}

	public void setWf_business_key(String wf_business_key) {
		this.wf_business_key = wf_business_key;
	}

	public String[] getWf_versions() {
		return wf_versions;
	}

	public void setWf_versions(String[] wf_versions) {
		this.wf_versions = wf_versions;
	}

	public String getWf_state() {
		return wf_state;
	}

	public void setWf_state(String wf_state) {
		this.wf_state = wf_state;
	}

	public String getWf_key() {
		return wf_key;
	}

	public String getWf_start_time1() {
		return wf_start_time1;
	}

	public void setWf_start_time1(String wf_start_time1) {
		this.wf_start_time1 = wf_start_time1;
	}

	public String getWf_start_time2() {
		return wf_start_time2;
	}

	public void setWf_start_time2(String wf_start_time2) {
		this.wf_start_time2 = wf_start_time2;
	}

	public String getWf_end_time1() {
		return wf_end_time1;
	}

	public void setWf_end_time1(String wf_end_time1) {
		this.wf_end_time1 = wf_end_time1;
	}

	public String getWf_end_time2() {
		return wf_end_time2;
	}

	public void setWf_end_time2(String wf_end_time2) {
		this.wf_end_time2 = wf_end_time2;
	}

	public void setWf_key(String wf_key) {
		this.wf_key = wf_key;
	}

	public String getWf_Inst_Id() {
		return wf_Inst_Id;
	}

	public void setWf_Inst_Id(String wf_Inst_Id) {
		this.wf_Inst_Id = wf_Inst_Id;
	}

	public String getWf_denf_id() {
		return wf_denf_id;
	}

	public void setWf_denf_id(String wf_denf_id) {
		this.wf_denf_id = wf_denf_id;
	}

}
