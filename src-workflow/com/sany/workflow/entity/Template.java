package com.sany.workflow.entity;

import java.util.Date;

/**
 * @todo 模板Bean
 * @author tanx
 * @date 2014年6月9日
 * 
 */
public class Template {

	private String templeId;

	private int templeType;

	private String templeTitle;

	private String templeContent;

	private Date createTime;

	private String creator;

	private Date lastUpdatetime;

	public String getTempleId() {
		return templeId;
	}

	public void setTempleId(String templeId) {
		this.templeId = templeId;
	}

	public int getTempleType() {
		return templeType;
	}

	public void setTempleType(int templeType) {
		this.templeType = templeType;
	}

	public String getTempleTitle() {
		return templeTitle;
	}

	public void setTempleTitle(String templeTitle) {
		this.templeTitle = templeTitle;
	}

	public String getTempleContent() {
		return templeContent;
	}

	public void setTempleContent(String templeContent) {
		this.templeContent = templeContent;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getLastUpdatetime() {
		return lastUpdatetime;
	}

	public void setLastUpdatetime(Date lastUpdatetime) {
		this.lastUpdatetime = lastUpdatetime;
	}

}
