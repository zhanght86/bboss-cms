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
 * <p>Title: DomainSAPConf.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>bboss workgroup</p>
 * @Date 2013-8-27
 * @author biaoping.yin
 * @version 1.0
 */
public class DomainSAPConf {
  //connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, "318");//集团号，生产机318，测试机150
  private String client = "318";
//  connectProperties.setProperty(DestinationDataProvider.JCO_USER," ");//用户名
//  connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD,"");//密码
//  connectProperties.setProperty(DestinationDataProvider.JCO_LANG, "en");//语言
//  connectProperties.setProperty(DestinationDataProvider.JCO_MSHOST,"sapci.sany.com.cn");//消息服务器，生产机sapci.sany.com.cn
//  connectProperties.setProperty(DestinationDataProvider.JCO_GROUP, "RFC");//登录组名称，测试机和生产机均为RFC
//  connectProperties.setProperty(DestinationDataProvider.JCO_MSSERV,"3600");//消息服务器端口号，生产机：3600，测试机3601
  private String user = "esb_rfc";
	private String passward = "ESB@1qaz";
	private String host = "10.0.13.11";
//	private String sysnr = "01";
	private String jco_group = "RFC";
	private String jco_msserv = "3600";
	private String lang = "ZH";
	private int jco_pool_capacity;
	private int jco_peak_limit;
	private String sysnr = "01";
	public String getSysnr() {
		return sysnr;
	}
	public void setSysnr(String sysnr) {
		this.sysnr = sysnr;
	}
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
	public String getJco_group() {
		return jco_group;
	}
	public void setJco_group(String jco_group) {
		this.jco_group = jco_group;
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
	public String getJco_msserv() {
		return jco_msserv;
	}
	public void setJco_msserv(String jco_msserv) {
		this.jco_msserv = jco_msserv;
	}

}
