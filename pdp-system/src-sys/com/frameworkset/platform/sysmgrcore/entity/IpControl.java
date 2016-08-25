package com.frameworkset.platform.sysmgrcore.entity;

import java.io.Serializable;

/**
 * IP限制实体类
 * @author houtt2
 * 2014.03.13
 */
public class IpControl implements Serializable{
	private static final long serialVersionUID = 2044770586261062423L;
	private String id;//id
	private String ip;//ip范围
	private String controluser;//限制用户名
	private int filtertype;//过滤类型 0：白名单 1：黑名单
	private String ipdesc; //备注
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getControluser() {
		return controluser;
	}
	public void setControluser(String controluser) {
		this.controluser = controluser;
	}
	public int getFiltertype() {
		return filtertype;
	}
	public void setFiltertype(int filtertype) {
		this.filtertype = filtertype;
	}
	public String getIpdesc() {
		return ipdesc;
	}
	public void setIpdesc(String ipdesc) {
		this.ipdesc = ipdesc;
	}
	

}
