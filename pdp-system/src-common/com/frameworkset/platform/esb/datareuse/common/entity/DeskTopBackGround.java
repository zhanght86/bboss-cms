/**  
* @Title: DeskTopBlackGround.java
* @Package com.frameworkset.platform.esb.datareuse.common.entity
* @Description: TODO(用一句话描述该文件做什么)
* @Copyright:Copyright (c) 2011
* @Company:三一集团
* @author qian.wang
* @date 2011-9-19 上午10:04:38
* @version V1.0  
*/
package com.frameworkset.platform.esb.datareuse.common.entity;

import java.sql.Timestamp;

import org.frameworkset.web.multipart.MultipartFile;

import com.frameworkset.orm.annotation.Column;

public class DeskTopBackGround {

	private String userid;
	private String subsystem;
	private String filename;
	private String fit;
	private String cn_name;
	private Timestamp creatdate;
	@Column(type="blobfile")
	private MultipartFile picture;

	
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getSubsystem() {
		return subsystem;
	}
	public void setSubsystem(String subsystem) {
		this.subsystem = subsystem;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getFit() {
		return fit;
	}
	public void setFit(String fit) {
		this.fit = fit;
	}
	public Timestamp getCreatdate() {
		return creatdate;
	}
	public void setCreatdate(Timestamp creatdate) {
		this.creatdate = creatdate;
	}
	public String getCn_name() {
		return cn_name;
	}
	public void setCn_name(String cn_name) {
		this.cn_name = cn_name;
	}
	public MultipartFile getPicture() {
		return picture;
	}
	public void setPicture(MultipartFile picture) {
		this.picture = picture;
	}

	
	
	
}
