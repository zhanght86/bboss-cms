package com.frameworkset.platform.cms.driver.publish.impl;

import java.util.Date;

/**
 * <p>Title: com.frameworkset.platform.cms.driver.publish.impl.PublishMessage.java</p>
 *
 * <p>Description: 记录一条发布消息，包括消息产生的时间和消息内容</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: iSany</p>
 * @Date 2007-1-25
 * @author biaoping.yin
 * @version 1.0
 */
public class PublishMessage implements java.io.Serializable {
	/**
	 * 发布人员信息
	 * {id,name,position}
	 * 用户id，用户名称，岗位/角色
	 */
	private String[] publisher;


	public PublishMessage(String msg,Date time,String[] publisher) {
		
		this.message = msg;
		this.date = time;
		this.publisher = publisher;
	}
	
	/**
	 * 发布消息内容
	 */
	private String message;
	
	/**
	 * 消息产生时间
	 */
	private Date date;
	
	
	public String toString()
	{
		return this.message;
	}


	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public String[] getPublisher() {
		return publisher;
	}
}
