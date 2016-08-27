package com.sany.workflow.demo.entity;

/**
 * 演示示例实体
 * 
 * @todo
 * @author tanx
 * @date 2014年8月27日
 * 
 */
public class BusinessDemoTreeEntity {

	private String nodeId;

	private String nodeName;

	private String parentId;

	private String useFlag;

	private String remark;

	private String processname;

	private String processkey;

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getUseFlag() {
		return useFlag;
	}

	public void setUseFlag(String useFlag) {
		this.useFlag = useFlag;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getProcessname() {
		return processname;
	}

	public void setProcessname(String processname) {
		this.processname = processname;
	}

	public String getProcesskey() {
		return processkey;
	}

	public void setProcesskey(String processkey) {
		this.processkey = processkey;
	}

}
