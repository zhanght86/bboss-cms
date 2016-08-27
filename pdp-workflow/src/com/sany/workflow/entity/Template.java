package com.sany.workflow.entity;

import java.sql.Timestamp;

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

	private Timestamp createTime;

	private String creator;

	private Timestamp lastUpdatetime;

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

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getLastUpdatetime() {
		return lastUpdatetime;
	}

	public void setLastUpdatetime(Timestamp lastUpdatetime) {
		this.lastUpdatetime = lastUpdatetime;
	}

}
