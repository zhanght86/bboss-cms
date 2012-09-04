package com.frameworkset.platform.cms.workflowmanager;

/**
 * 流程实体类
 * @author jxw
 *
 * 2007-9-25
 */
public class Workflow implements java.io.Serializable {
	/**
	 * 流程id
	 */
	private int workflowId;
	
	/**
	 * 流程name
	 */
	private String workflowName;
	
	/**
	 * 源状态id
	 */
	private int srcStatusId;
	
	/**
	 * 源状态name
	 */
	private String srcStatusName;
	
	/**
	 * 目标状态id
	 */
	private int destStatusId;
	
	/**
	 * 目标状态name
	 */
	private String destStatusName;
	
	/**
	 * 文档状态间可迁移id transision_id
	 */
	private int transisionId;

	public int getDestStatusId() {
		return destStatusId;
	}

	public void setDestStatusId(int destStatusId) {
		this.destStatusId = destStatusId;
	}

	public String getDestStatusName() {
		return destStatusName;
	}

	public void setDestStatusName(String destStatusName) {
		this.destStatusName = destStatusName;
	}

	public int getSrcStatusId() {
		return srcStatusId;
	}

	public void setSrcStatusId(int srcStatusId) {
		this.srcStatusId = srcStatusId;
	}

	public String getSrcStatusName() {
		return srcStatusName;
	}

	public void setSrcStatusName(String srcStatusName) {
		this.srcStatusName = srcStatusName;
	}

	public int getTransisionId() {
		return transisionId;
	}

	public void setTransisionId(int transisionId) {
		this.transisionId = transisionId;
	}

	public int getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(int workflowId) {
		this.workflowId = workflowId;
	}

	public String getWorkflowName() {
		return workflowName;
	}

	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}

}
