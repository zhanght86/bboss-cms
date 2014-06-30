package com.sany.workflow.job.bean;
		/* state,--任务状态
		taskName,--任务名称
		createTime,--任务创建时间
		userId,--处理人
		 processName,--流程名称
		duration,--节点处理工时
		simpleDate,-- 时间单位简称
		messageTempleId,--短信模板
		emailTempleId-- 邮件模板
		realName -- 真实姓名
		orgId  -- 部门编码
		mobile -- 手机号码
		mailAddress --  邮箱地址
		taskId -- 节点任务Id
		*/
	public class FlowProcess {
			private String state;  
			private String taskName;
			private String  createTime;
			private String  userId;
			private String processName;
			private String duration;
			private String simpleDate;
			private String messageTempleId;
			private String emailTempleId;
			private String realName;
			private String orgId;
			private String taskId;
			public String getTaskId() {
				return taskId;
			}
			public void setTaskId(String taskId) {
				this.taskId = taskId;
			}
			public String getMobile() {
				return mobile;
			}
			public void setMobile(String mobile) {
				this.mobile = mobile;
			}
			public String getMailAddress() {
				return mailAddress;
			}
			public void setMailAddress(String mailAddress) {
				this.mailAddress = mailAddress;
			}
			private String mobile;
			private String mailAddress;
			public String getRealName() {
				return realName;
			}
			public void setRealName(String realName) {
				this.realName = realName;
			}
			public String getOrgId() {
				return orgId;
			}
			public void setOrgId(String orgId) {
				this.orgId = orgId;
			}
			public String getState() {
				return state;
			}
			public void setState(String state) {
				this.state = state;
			}
			public String getTaskName() {
				return taskName;
			}
			public void setTaskName(String taskName) {
				this.taskName = taskName;
			}
			public String getCreateTime() {
				return createTime;
			}
			public void setCreateTime(String createTime) {
				this.createTime = createTime;
			}
			public String getUserId() {
				return userId;
			}
			public void setUserId(String userId) {
				this.userId = userId;
			}
			public String getProcessName() {
				return processName;
			}
			public void setProcessName(String processName) {
				this.processName = processName;
			}
			public String getDuration() {
				return duration;
			}
			public void setDuration(String duration) {
				this.duration = duration;
			}
			public String getSimpleDate() {
				return simpleDate;
			}
			public void setSimpleDate(String simpleDate) {
				this.simpleDate = simpleDate;
			}
			public String getMessageTempleId() {
				return messageTempleId;
			}
			public void setMessageTempleId(String messageTempleId) {
				this.messageTempleId = messageTempleId;
			}
			public String getEmailTempleId() {
				return emailTempleId;
			}
			public void setEmailTempleId(String emailTempleId) {
				this.emailTempleId = emailTempleId;
			}
			
			
			}
