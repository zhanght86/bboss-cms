package com.sany.workflow.entity;

import org.frameworkset.util.annotations.RequestParam;

/**
 * 节点控制变量参数
 * 
 * @todo
 * @author tanx
 * @date 2014年8月29日
 * 
 */
public class NodeControlParam {

	private String ID;

	private String NODE_ID;// 业主主键id或组织机构id

	private String BUSINESS_ID;// 业主主键id或组织机构id

	private String BUSINESS_TYPE;// 节点类型 0通用 1组织结构 2业务类型

	private double DURATION_NODE;// 节点工时
	@RequestParam(name = "${NODE_KEY}_IS_VALID")
	private int IS_VALID;// 是否有效 0否1是
	@RequestParam(name = "${NODE_KEY}_IS_EDIT")
	private int IS_EDIT;// 本节点是否能被修改 0不是1是
	@RequestParam(name = "${NODE_KEY}_IS_AUTO")
	private int IS_AUTO;// 是否是自动审批节点 0不是1是
	@RequestParam(name = "${NODE_KEY}_IS_AUTOAFTER")
	private int IS_AUTOAFTER;// 后续节点自动审批 0不是1是
	@RequestParam(name = "${NODE_KEY}_IS_EDITAFTER")
	private int IS_EDITAFTER;// 是否能修改后续节点 0 不能1能
	@RequestParam(name = "${NODE_KEY}_IS_RECALL")
	private int IS_RECALL;// 可撤回 0不能1能
	@RequestParam(name = "${NODE_KEY}_IS_CANCEL")
	private int IS_CANCEL;// 可驳回 0 不能 1 能
	@RequestParam(name = "${NODE_KEY}_IS_DISCARD")
	private int IS_DISCARD;// 可废弃 0 不能 1 能
	@RequestParam(name = "${NODE_KEY}_IS_COPY")
	private int IS_COPY;// 可抄送 0 不能 1 能
	@RequestParam(name = "${NODE_KEY}_IS_MULTI")
	private int IS_MULTI;// 是否多实例 0 单实例 1 多实例
	@RequestParam(name = "${NODE_KEY}_IS_SEQUENTIAL")
	private int IS_SEQUENTIAL;// 是否串行 0 串行 1 并行

	private int IS_MULTI_DEFAULT;// 默认多实例 0 否 1 是

	private String TASK_URL;// 待办URL

	private String NODE_DESCRIBE;// 节点描述

	private String BUSSINESSCONTROLCLASS;// 业务控制类

	private String NODE_KEY;// 节点key

	private String NODE_NAME;// 节点名称

	private String NODE_TYPE;// 节点类型

	private String PROCESS_KEY;// 流程key

	private String PROCESS_ID;// 流程实例id

	private int ORDER_NUM;// 排序字段

	public int getIS_MULTI_DEFAULT() {
		return IS_MULTI_DEFAULT;
	}

	public void setIS_MULTI_DEFAULT(int iS_MULTI_DEFAULT) {
		IS_MULTI_DEFAULT = iS_MULTI_DEFAULT;
	}

	public String getNODE_TYPE() {
		return NODE_TYPE;
	}

	public void setNODE_TYPE(String nODE_TYPE) {
		NODE_TYPE = nODE_TYPE;
	}

	public int getORDER_NUM() {
		return ORDER_NUM;
	}

	public void setORDER_NUM(int oRDER_NUM) {
		ORDER_NUM = oRDER_NUM;
	}

	public int getIS_MULTI() {
		return IS_MULTI;
	}

	public void setIS_MULTI(int iS_MULTI) {
		IS_MULTI = iS_MULTI;
	}

	public int getIS_SEQUENTIAL() {
		return IS_SEQUENTIAL;
	}

	public void setIS_SEQUENTIAL(int iS_SEQUENTIAL) {
		IS_SEQUENTIAL = iS_SEQUENTIAL;
	}

	public String getPROCESS_ID() {
		return PROCESS_ID;
	}

	public void setPROCESS_ID(String pROCESS_ID) {
		PROCESS_ID = pROCESS_ID;
	}

	public String getPROCESS_KEY() {
		return PROCESS_KEY;
	}

	public void setPROCESS_KEY(String pROCESS_KEY) {
		PROCESS_KEY = pROCESS_KEY;
	}

	public int getIS_AUTOAFTER() {
		return IS_AUTOAFTER;
	}

	public void setIS_AUTOAFTER(int iS_AUTOAFTER) {
		IS_AUTOAFTER = iS_AUTOAFTER;
	}

	public String getNODE_KEY() {
		return NODE_KEY;
	}

	public void setNODE_KEY(String nODE_KEY) {
		NODE_KEY = nODE_KEY;
	}

	public String getNODE_NAME() {
		return NODE_NAME;
	}

	public void setNODE_NAME(String nODE_NAME) {
		NODE_NAME = nODE_NAME;
	}

	public String getBUSSINESSCONTROLCLASS() {
		return BUSSINESSCONTROLCLASS;
	}

	public void setBUSSINESSCONTROLCLASS(String bUSSINESSCONTROLCLASS) {
		BUSSINESSCONTROLCLASS = bUSSINESSCONTROLCLASS;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getNODE_ID() {
		return NODE_ID;
	}

	public void setNODE_ID(String nODE_ID) {
		NODE_ID = nODE_ID;
	}

	public String getBUSINESS_ID() {
		return BUSINESS_ID;
	}

	public void setBUSINESS_ID(String bUSINESS_ID) {
		BUSINESS_ID = bUSINESS_ID;
	}

	public String getBUSINESS_TYPE() {
		return BUSINESS_TYPE;
	}

	public void setBUSINESS_TYPE(String bUSINESS_TYPE) {
		BUSINESS_TYPE = bUSINESS_TYPE;
	}

	public double getDURATION_NODE() {
		return DURATION_NODE;
	}

	public void setDURATION_NODE(double dURATION_NODE) {
		DURATION_NODE = dURATION_NODE;
	}

	public int getIS_VALID() {
		return IS_VALID;
	}

	public void setIS_VALID(int iS_VALID) {
		IS_VALID = iS_VALID;
	}

	public int getIS_EDIT() {
		return IS_EDIT;
	}

	public void setIS_EDIT(int iS_EDIT) {
		IS_EDIT = iS_EDIT;
	}

	public int getIS_AUTO() {
		return IS_AUTO;
	}

	public void setIS_AUTO(int iS_AUTO) {
		IS_AUTO = iS_AUTO;
	}

	public int getIS_EDITAFTER() {
		return IS_EDITAFTER;
	}

	public void setIS_EDITAFTER(int iS_EDITAFTER) {
		IS_EDITAFTER = iS_EDITAFTER;
	}

	public int getIS_RECALL() {
		return IS_RECALL;
	}

	public void setIS_RECALL(int iS_RECALL) {
		IS_RECALL = iS_RECALL;
	}

	public int getIS_CANCEL() {
		return IS_CANCEL;
	}

	public void setIS_CANCEL(int iS_CANCEL) {
		IS_CANCEL = iS_CANCEL;
	}

	public int getIS_DISCARD() {
		return IS_DISCARD;
	}

	public void setIS_DISCARD(int iS_DISCARD) {
		IS_DISCARD = iS_DISCARD;
	}

	public int getIS_COPY() {
		return IS_COPY;
	}

	public void setIS_COPY(int iS_COPY) {
		IS_COPY = iS_COPY;
	}

	public String getTASK_URL() {
		return TASK_URL;
	}

	public void setTASK_URL(String tASK_URL) {
		TASK_URL = tASK_URL;
	}

	public String getNODE_DESCRIBE() {
		return NODE_DESCRIBE;
	}

	public void setNODE_DESCRIBE(String nODE_DESCRIBE) {
		NODE_DESCRIBE = nODE_DESCRIBE;
	}

}
