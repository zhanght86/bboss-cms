package com.frameworkset.platform.cms.documentmanager.bean;

import java.util.Date;
/**
 * 文档置顶管理
 * @作者 xinwang.jiao 
 * @日期 2007-3-5 9:47:31
 * @版本 v1.0
 * @版权所有 bbossgroups
 */
public class ArrangeDoc implements java.io.Serializable {
	
	/**
	 * 文档ID
	 */
	private int documentId;
	/**
	 * 开始置顶时间
	 */
	private String startTime;
	/**
	 * 置顶结束时间
	 */
	private String endTime;
	/**
	 * 权重
	 */
	private int orderNo;
	/**
	 * 操作人
	 */
	private int opUser;
	/**
	 * 操作时间
	 */
	private int overtime;
	/**
	 * 置顶是否过期
	 */
	private Date opTime;
	//附加的（操作人的真实名字）
	private String username;
	//附加的（文档标题）
	private String doctitle;
	private int channelid;
	
	public int getDocumentId() {
		return documentId;
	}
	
	public void setDocumentId(int documentId) {
		this.documentId = documentId;
	}
	
	public String getEndTime() {
		return endTime;
	}
	
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	public Date getOpTime() {
		return opTime;
	}
	
	public void setOpTime(Date opTime) {
		this.opTime = opTime;
	}
	
	public int getOpUser() {
		return opUser;
	}
	
	public void setOpUser(int opUser) {
		this.opUser = opUser;
	}
	
	public int getOrderNo() {
		return orderNo;
	}
	
	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}
	
	public String getStartTime() {
		return startTime;
	}
	
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getDoctitle() {
		return doctitle;
	}

	public void setDoctitle(String doctitle) {
		this.doctitle = doctitle;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	public int getOvertime()
	{
		return this.overtime;
	}
	public void setOvertime(int overtime)
	{
		this.overtime=overtime;
	}

	public int getChannelid() {
		return channelid;
	}

	public void setChannelid(int channelid) {
		this.channelid = channelid;
	}

}
