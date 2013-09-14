/*
 * @(#)AppBom.java
 * 
 * Copyright @ 2001-2011 SANY Group Co.,Ltd.
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
package com.sany.appmonitor.entity;

import java.sql.Timestamp;
import java.util.Date;

import org.frameworkset.util.annotations.RequestParam;

public class PortalPendMonitorCondition {
	  private String id ;
	  private Integer appId;
	  private Integer useTime;
	  private Date monitorTime;
	  @RequestParam(dateformat="yyyy-MM-dd HH:mm:ss")
	  private Timestamp monitorTimeBegin;
	  @RequestParam(dateformat="yyyy-MM-dd HH:mm:ss")
	  private Timestamp monitorTimeEnd;
	  private String sign;
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getAppId() {
		return appId;
	}
	public void setAppId(Integer appId) {
		this.appId = appId;
	}
	public Integer getUseTime() {
		return useTime;
	}
	public void setUseTime(Integer useTime) {
		this.useTime = useTime;
	}
	public Date getMonitorTime() {
		return monitorTime;
	}
	public void setMonitorTime(Date monitorTime) {
		this.monitorTime = monitorTime;
	}
	public Timestamp getMonitorTimeBegin() {
		return monitorTimeBegin;
	}
	public void setMonitorTimeBegin(Timestamp monitorTimeBegin) {
		this.monitorTimeBegin = monitorTimeBegin;
	}
	public Timestamp getMonitorTimeEnd() {
		return monitorTimeEnd;
	}
	public void setMonitorTimeEnd(Timestamp monitorTimeEnd) {
		this.monitorTimeEnd = monitorTimeEnd;
	}  
}
