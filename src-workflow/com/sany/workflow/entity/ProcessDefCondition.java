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
 * 流程定义查询条件
 * 
 * @author yinbp
 * @since 2012-3-22 下午6:58:33
 */
public class ProcessDefCondition {
	
	private String processId;
	
	private String processName;
	
	/**
	 * 流程键
	 */
	private String processKey;
	
	/**
	 * 资源名称
	 */
	private String resourceName;
	
	private String sortKey;
	
	private boolean desc;
	
	private long offset;
	
	private String businesstype_id;

	public String getSortKey() {
		return sortKey;
	}

	public void setSortKey(String sortKey) {
		this.sortKey = sortKey;
	}

	public boolean isDesc() {
		return desc;
	}

	public void setDesc(boolean desc) {
		this.desc = desc;
	}

	public long getOffset() {
		return offset;
	}

	public void setOffset(long offset) {
		this.offset = offset;
	}

	public int getPagesize() {
		return pagesize;
	}

	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}

	private int pagesize;

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getProcessKey() {
		return processKey;
	}

	public void setProcessKey(String processKey) {
		this.processKey = processKey;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getBusinesstype_id() {
		return businesstype_id;
	}

	public void setBusinesstype_id(String businesstype_id) {
		this.businesstype_id = businesstype_id;
	}
	
}
