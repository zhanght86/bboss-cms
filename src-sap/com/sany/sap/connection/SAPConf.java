/**
 *  Copyright 2008 biaoping.yin
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
package com.sany.sap.connection;

/**
 * <p> SAPConf.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2009 </p>
 * 
 * @Date 2012-9-24 下午5:53:26
 * @author biaoping.yin
 * @version 1.0
 */
public class SAPConf {
	

	/**
	 * 10.0.13.11

01

150

esb_rfc

ESB@1qaz
	 */
	private String client = "150";
	private String user = "esb_rfc";
	private String passward = "ESB@1qaz";
	private String host = "10.0.13.11";
	private String sysnr = "01";
	private String lang = "ZH";
	private int jco_pool_capacity;
	private int jco_peak_limit;
	
	public static final String rfcName_BAPI_ACC_DOCUMENT_POST = "BAPI_ACC_DOCUMENT_POST";
	public static final String rfcName_commit = "BAPI_TRANSACTION_COMMIT";
	public static final String rfcName_rollback = "BAPI_TRANSACTION_ROLLBACK";
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassward() {
		return passward;
	}
	public void setPassward(String passward) {
		this.passward = passward;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getSysnr() {
		return sysnr;
	}
	public void setSysnr(String sysnr) {
		this.sysnr = sysnr;
	}
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	public int getJco_pool_capacity() {
		return jco_pool_capacity;
	}
	public void setJco_pool_capacity(int jco_pool_capacity) {
		this.jco_pool_capacity = jco_pool_capacity;
	}
	public int getJco_peak_limit() {
		return jco_peak_limit;
	}
	public void setJco_peak_limit(int jco_peak_limit) {
		this.jco_peak_limit = jco_peak_limit;
	}

}
