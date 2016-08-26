/*
  * @(#)BrowserVisitInfo.java
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
package com.frameworkset.platform.cms.countermanager.bean;

/**
 * @author gw_hel
 *
 */
public class BrowserVisitInfo {

	private String name;
	
	private long pv;
	
	private long ip;
	
	private String topPV;
	
	private String topIP;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getPv() {
		return pv;
	}

	public void setPv(long pv) {
		this.pv = pv;
	}

	public long getIp() {
		return ip;
	}

	public void setIp(long ip) {
		this.ip = ip;
	}

	public String getTopPV() {
		return topPV;
	}

	public void setTopPV(String topPV) {
		this.topPV = topPV;
	}

	public String getTopIP() {
		return topIP;
	}

	public void setTopIP(String topIP) {
		this.topIP = topIP;
	}
}
