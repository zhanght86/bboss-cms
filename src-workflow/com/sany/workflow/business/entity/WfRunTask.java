/**
 *  Copyright 2008-2010 biaoping.yin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.sany.workflow.business.entity;

import java.sql.Timestamp;

/**
 * <p>
 * Title: WfRunTask
 * </p>
 * <p>
 * Description: 代码生成管理服务实体类
 * </p>
 * <p>
 * bboss
 * </p>
 * <p>
 * Copyright (c) 2007
 * </p>
 * 
 * @Date 2015-05-19 11:09:44
 * @author yinbp
 * @version v1.0
 */
public class WfRunTask implements java.io.Serializable {

	private String taskId;
	private String dealer;
	private String businessKey;
	private Timestamp createDatetime;
	private String dealerName;
	private String dealerNames;
	private String dealers;
	private String dealerWorkno;
	private String lastOp;
	private String lastOper;
	private String processId;
	private String processKey;
	private String sender;
	private String senderName;
	private String senderWorkno;
	private String taskKey;
	private String taskName;
	private long taskType;
	private String taskUrl;

	public WfRunTask() {
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setDealer(String dealer) {
		this.dealer = dealer;
	}

	public String getDealer() {
		return dealer;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public String getBusinessKey() {
		return businessKey;
	}

	public void setCreateDatetime(Timestamp createDatetime) {
		this.createDatetime = createDatetime;
	}

	public Timestamp getCreateDatetime() {
		return createDatetime;
	}

	 

	public void setDealers(String dealers) {
		this.dealers = dealers;
	}

	public String getDealers() {
		return dealers;
	}

	 

	public void setLastOp(String lastOp) {
		this.lastOp = lastOp;
	}

	public String getLastOp() {
		return lastOp;
	}

	public void setLastOper(String lastOper) {
		this.lastOper = lastOper;
	}

	public String getLastOper() {
		return lastOper;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessKey(String processKey) {
		this.processKey = processKey;
	}

	public String getProcessKey() {
		return processKey;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getSender() {
		return sender;
	}

	 

	public void setTaskKey(String taskKey) {
		this.taskKey = taskKey;
	}

	public String getTaskKey() {
		return taskKey;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskType(long taskType) {
		this.taskType = taskType;
	}

	public long getTaskType() {
		return taskType;
	}

	public void setTaskUrl(String taskUrl) {
		this.taskUrl = taskUrl;
	}

	public String getTaskUrl() {
		return taskUrl;
	}

	public String getDealerName() {
		return dealerName;
	}

	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}

	public String getDealerNames() {
		return dealerNames;
	}

	public void setDealerNames(String dealerNames) {
		this.dealerNames = dealerNames;
	}

	public String getDealerWorkno() {
		return dealerWorkno;
	}

	public void setDealerWorkno(String dealerWorkno) {
		this.dealerWorkno = dealerWorkno;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getSenderWorkno() {
		return senderWorkno;
	}

	public void setSenderWorkno(String senderWorkno) {
		this.senderWorkno = senderWorkno;
	}
}